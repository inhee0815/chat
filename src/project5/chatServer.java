package project5;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;

@ServerEndpoint("/chatServer")
public class chatServer {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println(message);
		synchronized (clients) {
			for(Session client : clients) {
				if(!client.equals(session))
				client.getBasicRemote().sendText(message);
			}
			
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println(session);
		clients.add(session);
		
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println(session);
		clients.remove(session);
	}
	/*JSONObject obj = new JSONObject();
	obj.put("update", IsUpdate);
	try {
		FileWriter file = new FileWriter("c:\\myJson.json");
		file.write(obj.toJSONString());
		file.flush();
		file.close();
	} catch (IOException e) {
		e.printStackTrace();
	}*/
	//System.out.println("Create JSON Object : " + obj);


}
