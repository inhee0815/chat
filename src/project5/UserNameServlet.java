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
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		HttpSession httpSession = request.getSession(true);
		String username=request.getParameter("username");
		httpSession.setAttribute("username", username);
		if(username!=null){
			RequestDispatcher rd;
			rd=getServletContext().getRequestDispatcher("/chatClient");
			rd.forward(request,response);
		}
	}

}
