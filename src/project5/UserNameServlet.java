package project5;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		HttpSession httpSession = request.getSession(true);
		String username = request.getParameter("username"); //이름 정보 먼저 가져온다
		String ipAddress = request.getRemoteAddr();
		httpSession.setAttribute("username", username); // session에 접근
		httpSession.setAttribute("ipAddress", ipAddress);
		request.setAttribute("username", username);
		if (username != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("chatClient.jsp");
			dispatcher.forward(request, response);
		}
	}
}




 



