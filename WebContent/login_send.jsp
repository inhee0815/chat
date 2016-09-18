<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*,java.text.*"%>
<%@ page import="project5.util.EncryptUtil"%>
<%
	request.setCharacterEncoding("UTF-8");

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/test";
	String username = "root";
	String password = "apmsetup";
	Class.forName(driver);
	String userid= request.getParameter("hidden_id");
	String pass= request.getParameter("hidden_pw");
	String strSHA = EncryptUtil.getSHA256(pass);
	boolean loginchk=false;
	try {
		Connection conn = DriverManager.getConnection(url, username, password);
		String sql = "select * from user where id=\"" + userid + "\" and pw=\"" + userid + strSHA + "\"";
		//System.out.println(sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			loginchk=true;
		}
		conn.close();
	} catch (SQLException e) {
		out.println(e.toString());
	}

%>
<script>
	var loginchk = <%=loginchk%>;
	parent.infoCheck(loginchk);
</script>
