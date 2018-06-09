package kr.ac.skuniv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Generated;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	PreparedStatement ps = null;
	ResultSet rs = null;		
	Connection conn = null;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request ,HttpServletResponse response)  throws ServletException, IOException {
		System.out.println("login");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		boolean loginCheck = false;	
		String id ="";
		String pw="";
		String name = "";

		//파라미터로 아이디와 비밀번호 받음
		String parent_id = (String) request.getParameter("parent_id");
		String parent_pw = (String) request.getParameter("parent_pw");

		System.out.println("id = " + parent_id + "pw = " + parent_pw);
		
		String sql = "select * from client";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			int i = 0;
			while(rs.next()) {
				id = rs.getString(1);
				pw = rs.getString(2);
				name = rs.getString(3);
				
				if(parent_id.equals(id) && parent_pw.equals(pw)) {
					loginCheck = true;
					break;
				}
			}
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				conn.close();
				//rs.close();
				System.out.println("mysql 끝");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(name);
		
		if(loginCheck)
			response.getWriter().print(name);
		else
			response.getWriter().print("fail");
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public void join(HttpServletRequest request ,HttpServletResponse response)  throws ServletException, IOException {
		System.out.println("join");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String new_id = (String) request.getParameter("new_id");
		String new_pw = (String) request.getParameter("new_pw");
		String new_name = (String) request.getParameter("new_name");
		
		String id ="";
		String result="";
		
		String sql = "select id from client";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			int i = 0;
			while(rs.next()) {
				id = rs.getString(1);
				
				if(new_id.equals(id)) {
					result = "overlap";
					break;
				}
			}
			
			if(result.equals("")) {
				sql = "insert into client values ('" + new_id + "','" + new_pw + "','" + new_name + "');";
				ps = conn.prepareStatement(sql);
				ps.executeUpdate();
				result = "success";
				System.out.println("insert success!!!");
			}
			
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				conn.close();
				System.out.println("mysql 끝");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}		
		
		if(result.equals("overlap")) {
			response.getWriter().print("overlap");
		}
		else if(result.equals("success")) {
			response.getWriter().print("success");
		}
		else {
			response.getWriter().print("fail");
		}
		
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://117.17.142.207:3306/yesterday", "kimys", "kim111");
		return c;
	}

}
