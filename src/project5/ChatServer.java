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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	static boolean isUpdated = false;
	// session : 접속자마다 한개의 세션이 생성되어 데이터 통신수단으로 사용
	// 한개의 브라우저에서 여러개의 탭을 사용해서 접속하면 session은 서로 다르지만 httpsession은 동일
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);

		String username = (String) session.getUserProperties().get("username"); // 서버와
																				// 연결되어
																				// 있는
																				// 정보
																				// 가져옴
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
		if (username != null) {
			synchronized (clients) { // 접속 중인 모든 이용자에게 메시지를 전송한다.
				for (Session client : clients) {
					try {
						if (!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData(username, message));
						// getBasicRemote() : 객체를 구하고
						// sendText() : 텍스트를 더한다.
						// 메시지 전송이 성공할 때까지 계속 루프를 돈다.
						// 결국 이용자에게 메시지를 보내고야 만다.
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			// username 수정 처리
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
			pstmt.setString(2, sdf.format(d));
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
		
		System.out.println("오픈 : " + session);
		session.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
		session.getUserProperties().put("ipAddress", endpointConfig.getUserProperties().get("ipAddress"));
		clients.add(session);
		String username = (String) session.getUserProperties().get("username");
		String ipAddress = (String) session.getUserProperties().get("ipAddress");
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
			String sql = "INSERT INTO person (ipAddress, username) VALUES (?,?)"; // 사용자
																					// 정보
																					// 테이블에
																					// 정보
																					// 입력
			String sql2 = "INSERT INTO temp(session_id,ipAddress,fk_num) VALUES (?,?,?)"; // open했을
																							// 때
																							// 대화내용
																							// row
																							// 넘버
																							// 저장
			String sql3 = "SELECT * FROM person"; // 사용자 정보 테이블 읽기
			String sql4 = "SELECT * FROM chat";// 대화내용
																			// 목록
			 pstmt = conn.prepareStatement(sql);
			 nstmt = conn.prepareStatement(sql2);
			 mtmt = conn.prepareStatement(sql3);
			 stmt = conn.prepareStatement(sql4 + " order by num desc limit 1"); // 대화내용 row 넘버
																	// 읽어와서 그
																	// 다음부터 쭉
																	//  보여주기

			 mrs = mtmt.executeQuery();
			 srs = stmt.executeQuery();
			while (mrs.next()) {
				System.out.println("ipAddress = " + ipAddress + ", mrs.getString = " + mrs.getString("ipAddress"));
				if (ipAddress.equals(mrs.getString("ipAddress"))) // 
			    { 
			     System.out.println("아이피 같아요");
			     isUpdated=true;
			     break;
			    }
			   }

			if(isUpdated==false){ //ip 주소가 없다. 새로 들어왔네?
				System.out.println("ip 주소 없지");
				if (srs.next()) { // 테이블에 대화내용 있으면
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
			 if (mrs != null) try { mrs.close(); } catch(SQLException ex) {}
			 if (srs != null) try { srs.close(); } catch(SQLException ex) {}
			 if (pstmt != null) try { stmt.close(); } catch(SQLException ex) {}
			 if (nstmt != null) try { stmt.close(); } catch(SQLException ex) {}
			 if (mtmt != null) try { stmt.close(); } catch(SQLException ex) {}
		     if (stmt != null) try { stmt.close(); } catch(SQLException ex) {}
		     if (conn != null) try { conn.close(); } catch(SQLException ex) {}
			
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
							client.getBasicRemote().sendText(buildJsonData2(username, "님이 퇴장했습니다."));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("클로즈 : " + session);
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

	public ArrayList<String>getArrayList(String msg)
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add(msg);
		return (list);
		
	}

	/*public static Connection getConnection() throws Exception {
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
	}*/

	
	 public static Connection getConnection() throws Exception { try { String driver
	 = "com.mysql.jdbc.Driver"; String url =
	 "jdbc:mysql://localhost:3306/test"; String username = "root"; String
	 password = "1234"; Class.forName(driver);
	 
	 
	 
	 
	 Connection conn = DriverManager.getConnection(url, username, password);
	 System.out.println("Connected"); return conn;
	 
	 } catch (Exception e) { System.out.println(e); } return null; }
	 

}
