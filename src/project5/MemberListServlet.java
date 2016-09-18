package project5;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;



public class MemberListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn = null;
		try {
			conn = ChatServer.getConnection();
			if (ChatServer.isUpdated) {
				String sql = "SELECT * FROM temp";
				PreparedStatement astmt = conn.prepareStatement(sql);
				ResultSet ars = astmt.executeQuery();
				
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				while (ars.next()) {
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("userid", ars.getString("userid"));

					list.add(map);
				}
				Gson gson = new Gson();
				String jsonArray = gson.toJson(list);
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(jsonArray);
			    
				
			} else {
				return;
			}
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
		
		
	}
}
