<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*,java.text.*"%>
<%
	request.setCharacterEncoding("euc-kr");
	
	java.util.Date d = new java.util.Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/test";
	String username = "root";
	String password = "1234";
	Class.forName(driver);
	String message = request.getParameter("message");


	try {
		Connection conn = DriverManager.getConnection(url, username, password);

		String sql = "INSERT INTO chat (message,reg_date) VALUES (?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, message);
		pstmt.setString(2, sdf.format(d));
		pstmt.executeUpdate();
		pstmt.close();

		conn.close();
	} catch (SQLException e) {
		out.println(e.toString());
	}
%>
