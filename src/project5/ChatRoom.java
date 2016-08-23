package project5;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatRoom extends ServerEndpointConfig.Configurator {
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		sec.getUserProperties().put("username", (String)((HttpSession) request.getHttpSession()).getAttribute("username")); //������ ������ session ���
		sec.getUserProperties().put("ipAddress", (String)((HttpSession) request.getHttpSession()).getAttribute("ipAddress")); //������ ������ session ���
	}
}
