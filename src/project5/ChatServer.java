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
import java.util.Date;
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
	static boolean isUpdated ;
	long now = System.currentTimeMillis();
	Date date = new Date(now);
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);
		boolean first = false;
		String userid = (String) session.getUserProperties().get("userid"); 
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
		try {
			Connection conn = getConnection();
			String sql = "SELECT * FROM chat where send_date>=date_sub(now(),interval 1 day);";
			String sql2 = "INSERT INTO chat (MESSAGE, USER_ID) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			PreparedStatement pstmt = null;
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next()){
				first = true;
				sql="INSERT INTO chat (MESSAGE) VALUES (now())";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
			}
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, message);
			pstmt.setString(2, userid);
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
			synchronized (clients) { // 접속 중인 모든 이용자에게 메시지를 전송한다.
				for (Session client : clients) {
					try {
						if (!client.equals(session)){
							client.getBasicRemote().sendText(buildJsonData(userid, message));
						// getBasicRemote() : 객체를 구하고
						// sendText() : 텍스트를 더한다.
						// 메시지 전송이 성공할 때까지 계속 루프를 돈다.
						// 결국 이용자에게 메시지를 보내고야 만다.
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		JSONObject obj = new JSONObject();
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
		System.out.println("Create JSON Object : " + obj);
		
	}

	@OnOpen
	public void onOpen(EndpointConfig endpointConfig, Session session) {
		System.out.println("오픈 : " + session);
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
			String sql = "INSERT INTO user (USER_ID,ENTER_NUM) VALUES (?,?)"; //들어왔을 때 대화목록번호,사용자정보 저장
			String sql2 = "INSERT INTO participant (PART_ID) VALUES (?)";
			String sql3 = "SELECT * FROM user where USER_ID=\""+userid+"\""; // 사용자 정보 테이블 읽기
			String sql4 = "SELECT * FROM chat";// 대화내용
			String sql5 = "SELECT * FROM participant where PART_ID=\""+userid+"\"";
			
			ptmt = conn.prepareStatement(sql);
			ttmt = conn.prepareStatement(sql2);
			pstmt = conn.prepareStatement(sql3);
			cstmt = conn.prepareStatement(sql4 + " order by chat_no desc limit 1"); // 대화내용
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
				if (crs.next()) { // 테이블에 대화내용 있으면
					int num = Integer.parseInt(crs.getString("chat_no")) + 1;
					ptmt.setString(1, userid);
					ptmt.setInt(2, num);
				} else {
					int num = Integer.parseInt(crs.getString("chat_no"));
					ptmt.setString(1, userid);
					ptmt.setInt(2, num);
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
		
		System.out.println("클로즈 : " + session);
		clients.remove(session);
		try {
			Connection conn = getConnection();
			String sql = "DELETE FROM participant where PART_ID=\""+userid+"\"";;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
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
		obj.put("message",  message);
		obj.put("userid", userid);
		// System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();

	}

	@SuppressWarnings("unchecked")
	public String buildJsonData2(String userid) {

		JSONObject obj = new JSONObject();
		obj.put("come", userid);
		return obj.toJSONString();

	}
	@SuppressWarnings("unchecked")
	public String buildJsonData3(String userid) {

		JSONObject obj = new JSONObject();
		obj.put("out", userid);
		return obj.toJSONString();

	}
	@SuppressWarnings("unchecked")
	public String buildJsonData4(String date) {

		JSONObject obj = new JSONObject();
		obj.put("date", date);
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



}
