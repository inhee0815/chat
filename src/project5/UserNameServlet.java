package project5;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
		String ipAddress=request.getRemoteAddr();
		Connection conn=null;
		HashMap<Integer , String> map = new HashMap<Integer , String>();
		
		httpSession.setAttribute("username", username); //session에 접근
		httpSession.setAttribute("ipAddress", ipAddress);
		//httpSession.setAttribute("chatList", list);
		if (username != null) {
			/*
			 * RequestDispatcher rd;
			 * rd=getServletContext().getRequestDispatcher("/chatClient");
			 * rd.forward(request,response);DDD
			 */
			printWriter.println("<html>");
			printWriter.println("<head><link href=\"style.css?<?=filemtime('style.css')?>\" rel=\"stylesheet\" type=\"text/css\"></head>");
			printWriter.println("<body>");
			printWriter.println("<mark>username:" + username + "</mark><br>");
			printWriter.println("<div class=\"msg_box\" style=\"right: 290px\">");
			printWriter.println("<div style=\"color:black; text-align:center;\" class=\"msg_head\">비즈커머스개발팀");
			printWriter.println("<div class=\"close\" onclick=\"button_close();\">X</div>");
			printWriter.println("<div class=\"modify\">★</div></div>");
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
			printWriter.println("var webSocket = new WebSocket(\"ws://172.21.25.189:8080/project5/ChatServer\");");
			//printWriter.println("var webSocket = new WebSocket(\"ws://localhost:8080/project5/ChatServer\");");
			printWriter.println("var inputMessage = document.getElementById(\"msg_input\");");
			printWriter.println("var sendE = document.getElementById(\"send\");");
			printWriter.println("webSocket.onerror = function(event) {");
			printWriter.println("onError(event)");
			printWriter.println("};");
			printWriter.println("webSocket.onopen = function(event) {");
			printWriter.println("onOpen(event)");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("};");
			printWriter.println("webSocket.onclose = function(event) {");
			printWriter.println("onClose(event)");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("};");
			printWriter.println("webSocket.onmessage = function(event) {");
			printWriter.println("onMessage(event)");
			printWriter.println("var d = document.getElementById(\"msg_body\");");
			printWriter.println("d.scrollTop=d.scrollHeight-d.offsetHeight");
			printWriter.println("};");
			printWriter.println("inputMessage.onkeydown = function(event) {");
			printWriter.println("if (!event)");
			printWriter.println("event = window.event;");
			/*printWriter.println("if(event.keyCode==9) { ");
			printWriter.println("inputMessage.focus();");
			printWriter.println("var space = \"\\n\";"); 
			printWriter.println("inputMessage.selection=document.body.createTextRange;"); 
			printWriter.println("inputMessage.selection.text=space;"); 
			printWriter.println("event.returnValue = false;");  
			printWriter.println("}");*/
			printWriter.println("if (event.keyCode == 13) {");
			printWriter.println("event.preventDefault();"); //줄바꿈 막음
			printWriter.println("var temp =inputMessage.value;");
			printWriter.println("var pattern6= /^\\s+|\\s+$/g;");
			printWriter.println("inputMessage.value=temp.replace(pattern6,\"\");");
			printWriter.println("if (inputMessage.value != \"\") {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("div.id='msg_b';");
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
			printWriter.println("div.id='msg_a';");
			printWriter.println("div.className='msg_a';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("function onOpen(event) {");
			try{
			    conn=ChatServer.getConnection();
			    
			    if(ChatServer.isUpdated==true){
			     String sql5 = "SELECT chat.num,chat.message,chat.ipAddress FROM chat, temp where chat.num >= temp.fk_num and temp.ipAddress=\"" + ipAddress + "\"";
			     System.out.println("sql5 : " + sql5);
			     PreparedStatement astmt = conn.prepareStatement(sql5);
			     ResultSet ars = astmt.executeQuery();
			     while (ars.next()) {
			      int num = ars.getInt("num");
			      String message = ars.getString("message");
			      String addr = ars.getString("ipAddress");
			      map.put(num,message);
			      
			      printWriter.println("var input=\"\"");
			      printWriter.println("var div = document.createElement('div');");
			     
			      if(addr.equals(ipAddress)){
			    	  printWriter.println("div.id='msg_b';");
			    	  printWriter.println("div.className='msg_b';");
			    	  printWriter.println("input+=" + num + ";");
			    	  printWriter.println("div.innerHTML =\"" + message + "\";");
			    	  printWriter.println("document.getElementById('msg_body').appendChild(div);");
		
			     } else {
			    	 printWriter.println("div.id='msg_a';");
			    	 printWriter.println("div.className='msg_a';");
			      	 printWriter.println("div.innerHTML =\"" + message + "\";");
			      	 printWriter.println("document.getElementById('msg_body').appendChild(div);");
			     }

			      printWriter.println("div.ondblclick=function() {");
			      printWriter.println("var retVal = confirm(\"선택한 메시지를 삭제하시겠습니까?\");");
			      printWriter.println("if (retVal == true) {");
			      printWriter.println("alert(" + num + ");");
			      map.remove(num);
			      printWriter.println("}");	
			      printWriter.println("}");
			      
			     }
			    }
			   
			  }catch(Exception e){
			   System.out.println(e.toString());
			  }finally{
			   try {
			    conn.close();
			    
			   } catch (SQLException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }
			  }
			  

			/*printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.id='msg_push';");
			printWriter.println("div.className='msg_push';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("}");*/
			printWriter.println("}");
			printWriter.println("function onClose(event) {");
			printWriter.println("var div = document.createElement('div');");
			printWriter.println("var jsonData = JSON.parse(event.data);");
			printWriter.println("div.id='msg_push';");
			printWriter.println("div.className='msg_push';");
			printWriter.println("if(jsonData.message != null) {");
			printWriter.println("div.innerHTML = jsonData.message;");
			printWriter.println("document.getElementById('msg_body').appendChild(div);");
			printWriter.println("}");
			printWriter.println("}");
			printWriter.println("function onError(event) {");
			printWriter.println("alert(event.data);");
			printWriter.println("}");
			printWriter.println("function resize(obj) {");
			printWriter.println("obj.style.height = \"1px\";");
			printWriter.println("obj.style.height = (20+obj.scrollHeight)+\"px\";");
			printWriter.println("}");
			printWriter.println("function button_close() {");
			
			
			//printWriter.println("alert(\"닫기\");");
			printWriter.println("}");
			printWriter.println("</script>");
			printWriter.println("</html>");

		}
	}

}
