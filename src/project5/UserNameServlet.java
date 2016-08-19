package project5;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserNameServlet
 */
public class UserNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("text/html; charset=UTF-8"); 
		PrintWriter printWriter = response.getWriter();
		HttpSession httpSession = request.getSession(true);
		String username = request.getParameter("username");
		httpSession.setAttribute("username", username);
		if (username != null) {
			/*
			 * RequestDispatcher rd;
			 * rd=getServletContext().getRequestDispatcher("/chatClient");
			 * rd.forward(request,response);
			 */
			printWriter.println("<html>");
			printWriter.println("<head><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"></head>");
			printWriter.println("<body>");
			printWriter.println("<mark>username:" + username + "</mark><br>");
			printWriter.println("<div class=\"msg_box\" style=\"right: 290px\">");
			printWriter.println("<div style=\"color:black; text-align:center;\" class=\"msg_head\">비즈커머스개발팀");
			printWriter.println("<div class=\"close\">x</div></div>");
			printWriter.println("<div class=\"msg_wrap\" style=\"display: block;\">");
			printWriter.println("<div id=\"msg_body\">");
			printWriter.println("<div id=\"msg_push\"></div>");
			printWriter.println("</div>");
			printWriter.println("<div class=\"msg_footer\">");
			printWriter.println("<textarea class=\"msg_input\" id=\"msg_input\" onkeyup=\"resize(this)\"></textarea>");
			printWriter.println("</div>");
			printWriter.println("</div>");
			printWriter.println("</div>");
			printWriter.println("</body>");
			printWriter.println("<script>");
			printWriter.println("var webSocket = new WebSocket(\"ws://172.21.25.189:8080/project5/chatServer\");");
			printWriter.println("var textarea = document.getElementById(\"msg_push\");");
			printWriter.println("var inputMessage = document.getElementById(\"msg_input\");");
			printWriter.println("var sendE = document.getElementById(\"send\");");

			printWriter.println("webSocket.onerror = function(event) {");
			printWriter.println("onError(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onopen = function(event) {");
			printWriter.println("onOpen(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onmessage = function(event) {");
			printWriter.println("onMessage(event)");
			printWriter.println("};");
			printWriter.println("inputMessage.onkeydown = function(event) {");
			printWriter.println("if (!event)");
			printWriter.println("event = window.event;");
			printWriter.println("if (event.keyCode == 13) {");
			printWriter.println("if (inputMessage.value != \"\") {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("div.className='msg_b'");
			printWriter.println("div.innerHTML = inputMessage.value;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("webSocket.send(inputMessage.value);");
			printWriter.println("inputMessage.value = \"\";");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("};");

			printWriter.println("function onMessage(event) {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.className='msg_a';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("function onOpen(event) {");
			printWriter.println("textarea.innerHTML = \"연결 성공\";");
			printWriter.println("}");
			printWriter.println("function onError(event) {");
			printWriter.println("alert(event.data);");
			printWriter.println("}");
			printWriter.println("function resize(obj) {");
			printWriter.println("obj.style.height = \"1px\";");
			printWriter.println("obj.style.height = (20+obj.scrollHeight)+\"px\";");
			printWriter.println("}");

			printWriter.println("</script>");
			printWriter.println("</body>");
			printWriter.println("</html>");

		}
	}

}
