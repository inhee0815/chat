package project5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;



@ServerEndpoint(value="/ChatServer", configurator=ChatRoom.class)
public class ChatServer {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	//session : �����ڸ��� �Ѱ��� ������ �����Ǿ� ������ ��ż������� ���
	//�Ѱ��� ���������� �������� ���� ����ؼ� �����ϸ� session�� ���� �ٸ����� httpsession�� ����
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);
		
		String username=(String)session.getUserProperties().get("username");
		if(username!=null){
			synchronized (clients) {
				for(Session client : clients) {
					try{
						if(!client.equals(session))
							client.getBasicRemote().sendText(buildJsonData(username,message));
					} catch(Exception e) { e.printStackTrace(); }
				}
			}
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
	}

	@OnOpen
	public void onOpen(EndpointConfig endpointConfig, Session session) {
		System.out.println("���� : " + session);
		session.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
		session.getUserProperties().put("ipAddress", endpointConfig.getUserProperties().get("ipAddress"));
		clients.add(session);
		String username=(String)session.getUserProperties().get("username");
		String ipAddress=(String)session.getUserProperties().get("ipAddress");
		try {
			Connection conn=getConnection();
			String sql = "INSERT INTO person (ipAddress, username) VALUES (?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ipAddress);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		    System.out.println("Insert Complete");

		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
		
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Ŭ���� : " + session);
		clients.remove(session);
	}
	
	@SuppressWarnings("unchecked")
	public String buildJsonData(String username,String message){
   	
		JSONObject obj = new JSONObject();
		obj.put("message", username + " : " + message);
		//System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();
		
    }
	/*public Connection getConnection() throws Exception {
		FileInputStream fin=null;
		String path = ClassLoader.getSystemResource("/db.properties").getPath();
		 Properties props = new Properties();
		try {
			File filePath=new File(path);
			fin=new FileInputStream(filePath);
	        System.out.println(path);
	        props.load(fin);
	        
            String driver = props.getProperty("driver") ;
            System.out.println("driver : " + driver);
            String url = props.getProperty("url") ;
            String username= props.getProperty("username") ;
            String password = props.getProperty("password") ;
            Class.forName(driver);
			
            Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");
			return conn;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
*/

	public Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "1234";
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
