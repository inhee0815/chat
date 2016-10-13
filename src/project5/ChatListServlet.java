package project5;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
	SimpleDateFormat formatter = new SimpleDateFormat("aa HH:mm ");
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn = null;
		String remove_chk = null;
		String userid = request.getParameter("userid");
		try {
			conn = ChatServer.getConnection();
			
				remove_chk = " chat.chat_no not in(select del_num from delmsg where user_id=\"" + userid
						+ "\") and ";
				String sql = "SELECT chat.chat_no,chat.message,chat.user_id,chat.send_date FROM chat, user where" + remove_chk
						+ "chat.chat_no >= user.enter_num and user.user_id=\"" + userid  + "\"";
				PreparedStatement astmt = conn.prepareStatement(sql);
				ResultSet ars = astmt.executeQuery();
				
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
				while (ars.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("num", ars.getInt("chat_no"));
					map.put("message", ars.getString("message"));
					map.put("userid", ars.getString("user_id"));
					map.put("send_date",formatter.format(ars.getTimestamp("send_date")));
					list.add(map);
				}
				Gson gson = new Gson();
				String jsonArray = gson.toJson(list);
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(jsonArray);
			    
				
	
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
