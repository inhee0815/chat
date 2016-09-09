package project5;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		HttpSession httpSession = request.getSession(true);
		String userid = request.getParameter("userid"); //아이디 정보 먼저 가져온다
		String ipAddress = request.getRemoteAddr();
		httpSession.setAttribute("userid", userid); // session에 접근
		httpSession.setAttribute("ipAddress", ipAddress);
		request.setAttribute("userid", userid);
		if (userid != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("chatClient.jsp");
			dispatcher.forward(request, response);
		}
	}
}




 



