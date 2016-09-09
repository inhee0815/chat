package project5;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatRoom extends ServerEndpointConfig.Configurator {
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		sec.getUserProperties().put("userid", (String)((HttpSession) request.getHttpSession()).getAttribute("userid")); //서블릿이 전달한 session 출력
		sec.getUserProperties().put("ipAddress", (String)((HttpSession) request.getHttpSession()).getAttribute("ipAddress")); //서블릿이 전달한 session 출력
		//세션이란 클라이언트와 웹 서버간에 통신 연결에서 두 개체의 활성화된 접속을 뜻함
		//클라이언트가 웹서버에 요청하여 처음 접속하면 jsp는 요청한 클라이언트에 대하여 유일한 id를 부여하게 되는데 이 id를 세션이라고 부른다
		//세션 id를 임시로 저장하여 페이지 이동 시 이용하거나 클라이언트가 재접속했을 때 클라이언트를 구분할 수 있는 유일한 수단
		//세션과 쿠키의 차이점은 세션은 서버쪽에 정보를 저장, 쿠키는 클라이언트쪽에 정보를 저장
	}
}
