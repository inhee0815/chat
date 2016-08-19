package project5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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



@ServerEndpoint(value="/chatServer", configurator=chatRoom.class)
public class chatServer {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	//session : 접속자마다 한개의 세션이 생성되어 데이터 통신수단으로 사용
	//한개의 브라우저에서 여러개의 탭을 사용해서 접속하면 session은 서로 다르지만 httpsession은 동일
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
		System.out.println("오픈 : " + session);
		session.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
		clients.add(session);
		
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("클로즈 : " + session);
		clients.remove(session);
	}
	
	@SuppressWarnings("unchecked")
	public String buildJsonData(String username,String message){
   	
		JSONObject obj = new JSONObject();
		obj.put("message", username + " : " + message);
		//System.out.println("json string : " + obj.toJSONString());
		return obj.toJSONString();
		
    }



}
