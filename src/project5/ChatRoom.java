package project5;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatRoom extends ServerEndpointConfig.Configurator {
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		sec.getUserProperties().put("userid", (String)((HttpSession) request.getHttpSession()).getAttribute("userid")); //������ ������ session ���
		sec.getUserProperties().put("ipAddress", (String)((HttpSession) request.getHttpSession()).getAttribute("ipAddress")); //������ ������ session ���
		//�����̶� Ŭ���̾�Ʈ�� �� �������� ��� ���ῡ�� �� ��ü�� Ȱ��ȭ�� ������ ����
		//Ŭ���̾�Ʈ�� �������� ��û�Ͽ� ó�� �����ϸ� jsp�� ��û�� Ŭ���̾�Ʈ�� ���Ͽ� ������ id�� �ο��ϰ� �Ǵµ� �� id�� �����̶�� �θ���
		//���� id�� �ӽ÷� �����Ͽ� ������ �̵� �� �̿��ϰų� Ŭ���̾�Ʈ�� ���������� �� Ŭ���̾�Ʈ�� ������ �� �ִ� ������ ����
		//���ǰ� ��Ű�� �������� ������ �����ʿ� ������ ����, ��Ű�� Ŭ���̾�Ʈ�ʿ� ������ ����
	}
}
