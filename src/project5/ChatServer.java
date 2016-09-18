package project5;

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
	static boolean isUpdated ;
	
	// session : �����ڸ��� �Ѱ��� ������ �����Ǿ� ������ ��ż������� ���
	// �Ѱ��� ���������� �������� ���� ����ؼ� �����ϸ� session�� ���� �ٸ����� httpsession�� ����
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);

		String userid = (String) session.getUserProperties().get("userid"); // ������
																				// ����Ǿ�
																				// �ִ�
																				// ����
																				// ������
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
		try {
			Connection conn = getConnection();
			String sql = "INSERT INTO chat (message, reg_date, ipAddress, userid) VALUES (?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid + " : " + message);
			pstmt.setString(2, formatter.format(d));
			pstmt.setString(3, ipAddress);
			pstmt.setString(4, userid);
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
		if (userid != null) {
			synchronized (clients) { // ���� ���� ��� �̿��ڿ��� �޽����� �����Ѵ�.
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData(userid, message));
						// getBasicRemote() : ��ü�� ���ϰ�
						// sendText() : �ؽ�Ʈ�� ���Ѵ�.
						// �޽��� ������ ������ ������ ��� ������ ����.
						// �ᱹ �̿��ڿ��� �޽����� ������� ����.
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		/*JSONObject obj = new JSONObject();
		obj.put(userid, message);
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("C:\\myJson.json", true));
			fw.write(obj.toJSONString());
			fw.newLine();
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Create JSON Object : " + obj);*/
		
	}

	@OnOpen
	public void onOpen(EndpointConfig endpointConfig, Session session) {
		System.out.println("���� : " + session);
		session.getUserProperties().put("userid", endpointConfig.getUserProperties().get("userid"));
		session.getUserProperties().put("ipAddress", endpointConfig.getUserProperties().get("ipAddress"));
		clients.add(session);
		isUpdated = false;
		String userid = (String) session.getUserProperties().get("userid");
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
		if (userid != null) {
			synchronized (clients) {
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData2(userid));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		Connection conn = null;
		PreparedStatement ptmt = null;
		PreparedStatement ttmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement cstmt = null;
		PreparedStatement tstmt = null;
		ResultSet prs = null;
		ResultSet crs = null;
		ResultSet trs = null;
		try {
			conn = getConnection();
			String sql = "INSERT INTO person (userid,ipAddress,fk_num) VALUES (?,?,?)"; //������ �� ��ȭ��Ϲ�ȣ,��������� ����
			String sql2 = "INSERT INTO temp (userid) VALUES (?)";
			String sql3 = "SELECT * FROM person where userid=\""+userid+"\""; // ����� ���� ���̺� �б�
			String sql4 = "SELECT * FROM chat";// ��ȭ����
			String sql5 = "SELECT * FROM temp where userid=\""+userid+"\"";
			
			ptmt = conn.prepareStatement(sql);
			ttmt = conn.prepareStatement(sql2);
			pstmt = conn.prepareStatement(sql3);
			cstmt = conn.prepareStatement(sql4 + " order by num desc limit 1"); // ��ȭ����
			tstmt = conn.prepareStatement(sql5);
			
			prs = pstmt.executeQuery();
			crs = cstmt.executeQuery();
			trs = tstmt.executeQuery();
			
			if(!trs.next()){
				ttmt.setString(1, userid);
				ttmt.executeUpdate();
			}
			
			if(prs.next()){
					isUpdated = true;
			} else {
				if (crs.next()) { // ���̺� ��ȭ���� ������
					int num = Integer.parseInt(crs.getString("num")) + 1;
					ptmt.setString(1, userid);
					ptmt.setString(2, ipAddress);
					ptmt.setInt(3, num);
				} else {
					int num = Integer.parseInt(crs.getString("num"));
					ptmt.setString(1, userid);
					ptmt.setString(2, ipAddress);
					ptmt.setInt(3, num);
				}
				ptmt.executeUpdate();
			}

			System.out.println("Insert Complete");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (prs != null)
				try {
					prs.close();
				} catch (SQLException ex) {
				}
			if (crs != null)
				try {
					crs.close();
				} catch (SQLException ex) {
				}
			if (ptmt != null)
				try {
					ptmt.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (cstmt != null)
				try {
					cstmt.close();
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
		String userid = (String) session.getUserProperties().get("userid");
		if (userid != null) {
			synchronized (clients) {
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData3(userid));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Ŭ���� : " + session);
		clients.remove(session);
		try {
			Connection conn = getConnection();
			String sql = "DELETE FROM temp where userid=\""+userid+"\"";;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			System.out.println("EXIT~~~~~");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public String buildJsonData(String userid, String message) {

		JSONObject obj = new JSONObject();
		obj.put("message", userid + " : " + message);
		// System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();

	}

	@SuppressWarnings("unchecked")
	public String buildJsonData2(String userid) {

		JSONObject obj = new JSONObject();
		obj.put("come", userid);
		// System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();

	}
	@SuppressWarnings("unchecked")
	public String buildJsonData3(String userid) {

		JSONObject obj = new JSONObject();
		obj.put("out", userid);
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
