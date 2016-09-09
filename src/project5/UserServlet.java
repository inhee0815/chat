package project5;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project5.util.EncryptUtil;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.util.Date d = new java.util.Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String username = request.getParameter("user_name");
		String userid = request.getParameter("user_id");
		String userpass = request.getParameter("user_pass");
		String strSHA = EncryptUtil.getSHA256(userpass);
		Connection conn = null;
		System.out.println(username);
		System.out.println(userid);
		System.out.println(strSHA);
		try {
			conn = ChatServer.getConnection();
			String sql = "INSERT INTO user (name,id,pw,reg_date) VALUES (?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, userid);
			pstmt.setString(3, strSHA);
			pstmt.setString(4, sdf.format(d));
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.html");
		dispatcher.forward(request, response);

	}

}
