package project5;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		String ipAddress = request.getRemoteAddr();
		Connection conn = null;
		int num = 0;
		String message = null;
		String addr = null;
		String remove_chk = null;
		httpSession.setAttribute("username", username); // session에 접근
		httpSession.setAttribute("ipAddress", ipAddress);
		// httpSession.setAttribute("chatList", list);
		if (username != null) {

			/*
			 * RequestDispatcher rd;
			 * rd=getServletContext().getRequestDispatcher("/chatClient");
			 * rd.forward(request,response);DDD
			 */
			printWriter.println("<html>");
			printWriter.println(
					"<head><link href=\"style.css?<?=filemtime('style.css')?>\" rel=\"stylesheet\" type=\"text/css\"></head>");
			printWriter.println("<body>");
			printWriter.println("<form id=\"frm\" method=\"post\" enctype=\"multipart/form-data\" action=\"img.jsp\">");
			printWriter.println("<input name=\"Filedata\" class=\"upload\" id=\"uploadInputBox\" type=\"file\" />");
			printWriter.println("<input type=\"submit\" value=\"전송\" onclick=\"imgUp();\"/>");
			printWriter.println("<iframe id=\"ifr\" name=\"ifr\" style=\"display:none;\"></iframe>");
			printWriter.println("</form>");
			printWriter.println("<mark>username:" + username + "</mark><br>");
			printWriter.println("<div class=\"msg_box\" style=\"right: 290px\">");
			printWriter.println("<div style=\"color:black; text-align:center;\" class=\"msg_head\">비즈커머스개발팀");
			printWriter.println("<div class=\"close\" onclick=\"button_close();\">X</div>");
			printWriter.println("<div class=\"emoji\" onclick=\"popupOpen();\">★</div></div>");
			printWriter.println("<div class=\"msg_wrap\" style=\"display: block;\">");
			printWriter.println("<div class=\"msg_body\" id=\"msg_body\">");
			printWriter.println("</div>");
			printWriter.println("</br><div class=\"msg_footer\">");
			printWriter.println("<textarea class=\"msg_input\" id=\"msg_input\" onkeyup=\"resize(this)\"></textarea>");
			printWriter.println("</div>");
			printWriter.println("</div>");
			printWriter.println("</div>");
			printWriter.println("</body>");
			printWriter.println("<script>");
			try {
				conn = ChatServer.getConnection();

				if (ChatServer.isUpdated == true) {
					remove_chk = " chat.num not in(select rmv_num from remove where ipAddress=\"" + ipAddress
							+ "\") and ";
					String sql5 = "SELECT chat.num,chat.message,chat.ipAddress FROM chat, temp where" + remove_chk
							+ "chat.num >= 584 and temp.ipAddress=\"" + ipAddress + "\"";
					// temp.fk_num
					System.out.println("sql5 : " + sql5);
					PreparedStatement astmt = conn.prepareStatement(sql5);
					ResultSet ars = astmt.executeQuery();
					while (ars.next()) {
						num = ars.getInt("num");
						message = ars.getString("message");
						addr = ars.getString("ipAddress");

						printWriter.println("var input=\"\"");
						printWriter.println("var div = document.createElement('div');");

						if (addr.equals(ipAddress)) {
							printWriter.println("div.id='msg_b';");
							printWriter.println("div.className='msg_b';");
							printWriter.println("div.innerHTML =\"" + message + "\";");
							printWriter.println("document.getElementById('msg_body').appendChild(div);");

						} else {
							printWriter.println("div.id='msg_a';");
							printWriter.println("div.className='msg_a';");
							printWriter.println("div.innerHTML =\"" + message + "\";");
							printWriter.println("document.getElementById('msg_body').appendChild(div);");
						}
						printWriter.println("div.ondblclick = function() {");
						printWriter.println("var retVal = confirm(\"선택한 메시지를 삭제하시겠습니까?\");");
						printWriter.println("if (retVal == true) {");
						printWriter.println("this.parentElement.removeChild(this);");
						printWriter.println("msg_rmv(\"post\",\"send.jsp\"," + num + ");");
						printWriter.println("} else {");
						printWriter.println("return;");
						printWriter.println("}");
						printWriter.println("};");
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			printWriter.println("var webSocket = new WebSocket(\"ws://172.21.25.189:8080/project5/ChatServer\");");
			// printWriter.println("var webSocket = new
			// WebSocket(\"ws://localhost:8080/project5/ChatServer\");");
			printWriter.println("var inputMessage = document.getElementById(\"msg_input\");");
			printWriter.println("var sendE = document.getElementById(\"send\");");
			printWriter.println("webSocket.onerror = function(event) {");
			printWriter.println("onError(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onopen = function(event) {");
			printWriter.println("onOpen(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onclose = function(event) {");
			printWriter.println("onClose(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onmessage = function(event) {");
			printWriter.println("onMessage(event)");
			printWriter.println("};");
			printWriter.println("inputMessage.onkeydown = function(event) {");
			printWriter.println("if (!event)");
			printWriter.println("event = window.event;");
			/*
			 * printWriter.println("if(event.keyCode==9) { ");
			 * printWriter.println("inputMessage.focus();");
			 * printWriter.println("var space = \"\\n\";"); printWriter.println(
			 * "inputMessage.selection=document.body.createTextRange;");
			 * printWriter.println("inputMessage.selection.text=space;");
			 * printWriter.println("event.returnValue = false;");
			 * printWriter.println("}");
			 */

			printWriter.println("if (event.keyCode == 13) {");
			printWriter.println("event.preventDefault();"); // 줄바꿈 막음
			printWriter.println("var str =inputMessage.value;");
			printWriter.println("if (inputMessage.value != \"\") {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("div.id='msg_b';");
			printWriter.println("div.className='msg_b'");

			printWriter.println("div.innerHTML = ConvertSystemSourcetoHtml(inputMessage.value);");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("webSocket.send(inputMessage.value);");
			printWriter.println("inputMessage.value = \"\";");
			printWriter.println("div.ondblclick = function() {");
			printWriter.println("var retVal = confirm(\"선택한 메시지를 삭제하시겠습니까?\");");
			printWriter.println("if (retVal == true) {");
			printWriter.println("this.parentElement.removeChild(this);");
			printWriter.println("msg_rmv(\"post\",\"send.jsp\", " + num + ");");
			printWriter.println("} else {");
			printWriter.println("return;");
			printWriter.println("}");
			printWriter.println("};");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("};");
			printWriter.println("function onMessage(event) {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.id='msg_a';");
			printWriter.println("div.className='msg_a';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("div.ondblclick = function() {");
			printWriter.println("var retVal = confirm(\"선택한 메시지를 삭제하시겠습니까?\");");
			printWriter.println("if (retVal == true) {");
			printWriter.println("this.parentElement.removeChild(this);");
			printWriter.println("msg_rmv(\"post\",\"send.jsp\", " + num + ");");
			printWriter.println("} else {");
			printWriter.println("return;");
			printWriter.println("}");
			printWriter.println("};");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("function onOpen(event) {");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.id='msg_push';");
			printWriter.println("div.className='msg_push';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("}");

			printWriter.println("}");
			printWriter.println("function onClose(event) {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.id='msg_push';");
			printWriter.println("div.className='msg_push';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("function onError(event) {");
			printWriter.println("alert(event.data);");
			printWriter.println("}");
			printWriter.println("function resize(obj) {");
			printWriter.println("obj.style.height = \"1px\";");
			printWriter.println("obj.style.height = (20+obj.scrollHeight)+\"px\";");
			printWriter.println("}");
			printWriter.println("function button_close(str) {");
			printWriter.println("alert(\"Coming Soon..\");");
			printWriter.println("}");

			printWriter.println("function msg_rmv(method,path,num) {");
			printWriter.println("alert(num);");
			printWriter.println("var form = document.createElement(\"form\");");
			printWriter.println("form.setAttribute(\"method\", method);");
			printWriter.println("form.setAttribute(\"action\", path);");

			printWriter.println("var hiddenNum = document.createElement(\"input\");");
			printWriter.println("hiddenNum.setAttribute(\"type\", \"hidden\");");
			printWriter.println("hiddenNum.setAttribute(\"name\", \"num\");");
			printWriter.println("hiddenNum.setAttribute(\"value\", num);");
			printWriter.println("var hiddenIp = document.createElement(\"input\");");
			printWriter.println("hiddenIp.setAttribute(\"type\", \"hidden\");");
			printWriter.println("hiddenIp.setAttribute(\"name\", \"ip\");");
			printWriter.println("hiddenIp.setAttribute(\"value\", \"" + ipAddress + "\");");
			printWriter.println("var iframe = document.createElement(\"iframe\");");
			printWriter.println("iframe.setAttribute(\"id\", \"hidden\");");
			printWriter.println("iframe.setAttribute(\"name\", \"hiddenifr\");");
			printWriter.println("iframe.setAttribute(\"scr\", path);");
			printWriter.println("iframe.setAttribute(\"style\", \"display:none;\");");
			printWriter.println("form.appendChild(hiddenNum);");
			printWriter.println("form.appendChild(hiddenIp);");
			printWriter.println("document.body.appendChild(form);");
			printWriter.println("document.body.appendChild(iframe);");
			printWriter.println("form.target = \"hiddenifr\";");
			printWriter.println("form.submit();");

			printWriter.println("}");
			printWriter.println("function imgUp(){");
			printWriter.println("document.getElementById(\"frm\").target = \"ifr\";");
			printWriter.println("return false;");
			printWriter.println("}");
			printWriter.println("function upload_img(addr,tag){");
			printWriter.println("alert(addr);");
			printWriter.println("alert(tag);");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("div.setAttribute(\"id\", \"msg_b\");");
			printWriter.println("div.setAttribute(\"class\", \"msg_b\");");
			printWriter.println("var img = document.createElement(\"img\");");
			printWriter.println("img.setAttribute(\"src\", addr);");
			printWriter.println("img.setAttribute(\"height\", \"100\");");
			printWriter.println("img.setAttribute(\"width\", \"100\");");
			printWriter.println("div.appendChild(img);");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			// printWriter.println("alert(\"<img src=\"+ str + \"height=\"100\"
			// width=\"100\">);");
			printWriter.println("webSocket.send(tag);");
			printWriter.println("}");

			printWriter.println("function popupOpen(){");
			printWriter.println("var popUrl = \"test.html\";");
			printWriter.println("var popOption = \"width=370, height=360, resizable=no, scrollbars=no, status=no;\";");
			printWriter.println("window.open(popUrl,\"\",popOption);");
			printWriter.println("}");

			printWriter.println("function ConvertSystemSourcetoHtml(str){");
			printWriter.println(" str = str.replace(\"\'\",\"\\'\");");
			//printWriter.println(" str = str.replace(\"\"\",\"안녕\");");
			/*printWriter.println("str = str.replace(/>/gi,\"&gt;\");");
			printWriter.println("str = str.replace(/\\\"/gi,\"&quot;\");");
			printWriter.println("str = str.replace(/\\\'/gi,\"&#39;\");");*/
			//printWriter.println("str = str.replace(/\n/g,\"<br/>\");");
			printWriter.println("alert(str);");
			printWriter.println("return str;");
			printWriter.println("}");
			printWriter.println("</script>");
			printWriter.println("</html>");

		}
	}

}
