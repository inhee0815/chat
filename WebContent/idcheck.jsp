<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*,java.text.*"%>
<%
	request.setCharacterEncoding("UTF-8");

	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/test";
	String username = "root";
	String password = "1234";
	Class.forName(driver);
	String userid= request.getParameter("hidden_id");
	boolean idchk=false;
	try {
		Connection conn = DriverManager.getConnection(url, username, password);
		String sql = "select * from member where MEM_ID=\"" + userid + "\"";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			idchk = true;
		}
		conn.close();
	} catch (SQLException e) {
		out.println(e.toString());
	}

%>
<script>
	var idchk = <%=idchk%>;
	parent.idCheck(idchk);
</script>
