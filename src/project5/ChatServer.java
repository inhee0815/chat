package project5;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;

@ServerEndpoint(value = "/ChatServer", configurator = ChatRoom.class)
public class ChatServer {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	java.util.Date d = new java.util.Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static boolean isUpdated = false;
	
	// session : �����ڸ��� �Ѱ��� ������ �����Ǿ� ������ ��ż������� ���
	// �Ѱ��� ���������� �������� ���� ����ؼ� �����ϸ� session�� ���� �ٸ����� httpsession�� ����
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);

		String username = (String) session.getUserProperties().get("username"); // ������
																				// ����Ǿ�
																				// �ִ�
																				// ����
																				// ������
		String ipAddress = (String) session.getUserProperties().get("ipAddress");

		if (username != null) {
			synchronized (clients) { // ���� ���� ��� �̿��ڿ��� �޽����� �����Ѵ�.
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData(username, message));
						// getBasicRemote() : ��ü�� ���ϰ�
						// sendText() : �ؽ�Ʈ�� ���Ѵ�.
						// �޽��� ������ ������ ������ ��� ������ ����.
						// �ᱹ �̿��ڿ��� �޽����� ������� ����.
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			// username ���� ó��
		}
		JSONObject obj = new JSONObject();
		obj.put(username, message);
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("C:\\myJson.json", true));
			fw.write(obj.toJSONString());
			fw.newLine();
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Create JSON Object : " + obj);
		try {
			Connection conn = getConnection();
			String sql = "INSERT INTO chat (message, reg_date, ipAddress) VALUES (?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username + " : " + message);
			pstmt.setString(2, formatter.format(d));
			pstmt.setString(3, ipAddress);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			System.out.println("Message Insert Complete");

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(EndpointConfig endpointConfig, Session session) {

		System.out.println("���� : " + session);
		session.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
		session.getUserProperties().put("ipAddress", endpointConfig.getUserProperties().get("ipAddress"));
		clients.add(session);
		String username = (String) session.getUserProperties().get("username");
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
		if (username != null) {
			synchronized (clients) {
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData2(username, "���� �����߽��ϴ�."));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		String sessionId = (String) session.toString();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement nstmt = null;
		PreparedStatement mtmt = null;
		PreparedStatement stmt = null;
		ResultSet mrs = null;
		ResultSet srs = null;
		try {
			conn = getConnection();
			String sql = "INSERT INTO person (ipAddress, username) VALUES (?,?)"; // �����
																					// ����
																					// ���̺�
																					// ����
																					// �Է�
			String sql2 = "INSERT INTO temp(session_id,ipAddress,fk_num) VALUES (?,?,?)"; // open����
																							// ��
																							// ��ȭ����
																							// row
																							// �ѹ�
																							// ����
			String sql3 = "SELECT * FROM person"; // ����� ���� ���̺� �б�
			String sql4 = "SELECT * FROM chat";// ��ȭ����
												// ���
			pstmt = conn.prepareStatement(sql);
			nstmt = conn.prepareStatement(sql2);
			mtmt = conn.prepareStatement(sql3);
			stmt = conn.prepareStatement(sql4 + " order by num desc limit 1"); // ��ȭ����
																				// row
																				// �ѹ�
			// �о�ͼ� ��
			// �������� ��
			// �����ֱ�

			mrs = mtmt.executeQuery();
			srs = stmt.executeQuery();
			while (mrs.next()) {
				System.out.println("ipAddress = " + ipAddress + ", mrs.getString = " + mrs.getString("ipAddress"));
				if (ipAddress.equals(mrs.getString("ipAddress"))) //
				{
					System.out.println("������ ���ƿ�");
					isUpdated = true;
					break;
				}
			}

			if (!isUpdated) { // ip �ּҰ� ����. ���� ���Գ�?
				System.out.println("ip �ּ� ����");
				if (srs.next()) { // ���̺� ��ȭ���� ������
					int num = Integer.parseInt(srs.getString("num")) + 1;
					pstmt.setString(1, ipAddress);
					pstmt.setString(2, username);
					nstmt.setString(1, sessionId);
					nstmt.setString(2, ipAddress);
					nstmt.setInt(3, num);
				} else {
					int num = Integer.parseInt(srs.getString("num"));
					pstmt.setString(1, ipAddress);
					pstmt.setString(2, username);
					nstmt.setString(1, sessionId);
					nstmt.setString(2, ipAddress);
					nstmt.setInt(3, num);
				}
				pstmt.executeUpdate();
				nstmt.executeUpdate();
			}

			System.out.println("Insert Complete");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mrs != null)
				try {
					mrs.close();
				} catch (SQLException ex) {
				}
			if (srs != null)
				try {
					srs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			if (nstmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			if (mtmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}

		}

	}

	@OnClose
	public void onClose(Session session) {
		String username = (String) session.getUserProperties().get("username");
		if (username != null) {
			synchronized (clients) {
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData2(username, "���� �����߽��ϴ�."));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Ŭ���� : " + session);
		clients.remove(session);

	}

	@SuppressWarnings("unchecked")
	public String buildJsonData(String username, String message) {

		JSONObject obj = new JSONObject();
		obj.put("message", username + " : " + message);
		// System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();

	}

	@SuppressWarnings("unchecked")
	public String buildJsonData2(String username, String message) {

		JSONObject obj = new JSONObject();
		obj.put("message", username + " " + message);
		// System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();

	}

	public static Connection getConnection() throws Exception {
		Properties props = new Properties();
		String path = ChatServer.class.getResource("db.properties").getPath();

		try {
			props.load(new FileReader(path));
			String driver = props.getProperty("driver");
			String url = props.getProperty("url");
			String username = props.getProperty("username");
			String password = props.getProperty("password");
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");
			return conn;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	/*
	 * public static Connection getConnection() throws Exception { try { String
	 * driver = "com.mysql.jdbc.Driver"; String url =
	 * "jdbc:mysql://localhost:3306/test"; String username = "root"; String
	 * password = "1234"; Class.forName(driver);
	 * 
	 * 
	 * 
	 * 
	 * Connection conn = DriverManager.getConnection(url, username, password);
	 * System.out.println("Connected"); return conn;
	 * 
	 * } catch (Exception e) { System.out.println(e); } return null; }
	 */

}
