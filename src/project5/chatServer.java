package project5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
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
	//session : �����ڸ��� �Ѱ��� ������ �����Ǿ� ������ ��ż������� ���
	//�Ѱ��� ���������� �������� ���� ����ؼ� �����ϸ� session�� ���� �ٸ����� httpsession�� ����
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);
		JSONObject obj = new JSONObject();
		obj.put(session, message);
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
		//String username=(String)session.getUserProperties().get("username");
		//if(username!=null){
			synchronized (clients) {
				for(Session client : clients) {
					try{
						if(!client.equals(session))
							client.getBasicRemote().sendText(message);
					} catch(Exception e) { e.printStackTrace(); }
				}
			}
		//}
	}

	@OnOpen
	public void onOpen(EndpointConfig endpointConfig, Session session) {
		System.out.println("���� : " + session);
		//session.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
		clients.add(session);
		
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Ŭ���� : " + session);
		clients.remove(session);
	}
	


}
