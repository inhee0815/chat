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



public class ChatListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("GET~~~");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("START~~~~~");

		Connection conn = null;
		String remove_chk = null;
		String ipAddress = request.getRemoteAddr();
		System.out.println("ipAddress : " + ipAddress);
		try {
			conn = ChatServer.getConnection();
			if (ChatServer.isUpdated) {
				remove_chk = " chat.num not in(select rmv_num from remove where ipAddress=\"" + ipAddress
						+ "\") and ";
				String sql = "SELECT chat.num,chat.message,chat.ipAddress FROM chat, temp where" + remove_chk
						+ "chat.num >= temp.fk_num and temp.ipAddress=\"" + ipAddress + "\"";
				System.out.println("sql : " + sql);
				PreparedStatement astmt = conn.prepareStatement(sql);
				ResultSet ars = astmt.executeQuery();
				
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				while (ars.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("num", ars.getInt("num"));
					map.put("message", ars.getString("message"));
					map.put("addr", ars.getString("ipAddress"));
					
					list.add(map);
				}
				Gson gson = new Gson();
				String jsonArray = gson.toJson(list);
				System.out.println(jsonArray);
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
