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

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		Connection conn = null;
		String remove_chk = null;
		String userid = "dlsgmlek";
		//System.out.println("ChatListServlet : " + userid);
		try {
			conn = ChatServer.getConnection();
			if (ChatServer.isUpdated) {
				remove_chk = " chat.num not in(select rmv_num from removal where userid=\"" + userid
						+ "\") and ";
				String sql = "SELECT chat.num,chat.message,chat.userid,chat.reg_date FROM chat, temp where" + remove_chk
						+ "chat.num >= temp.fk_num and temp.userid=\"" + userid  + "\"";
				PreparedStatement astmt = conn.prepareStatement(sql);
				ResultSet ars = astmt.executeQuery();
				
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				while (ars.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("num", ars.getInt("num"));
					map.put("message", ars.getString("message"));
					map.put("userid", ars.getString("userid"));
					map.put("reg_date",ars.getTimestamp("reg_date"));
					
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
