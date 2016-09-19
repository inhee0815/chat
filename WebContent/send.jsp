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
	int num = Integer.parseInt(request.getParameter("num"));
	String userid = request.getParameter("userid");


	try {
		Connection conn = DriverManager.getConnection(url, username, password);

		String sql = "INSERT INTO removal (userid,rmv_num) VALUES (?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, userid);
		pstmt.setInt(2, num);
		pstmt.executeUpdate();
		pstmt.close();

		conn.close();
	} catch (SQLException e) {
		out.println(e.toString());
	}
%>
<script language=javascript>
	self.window.alert("입력한 글을 삭제하였습니다.");
	
</script>