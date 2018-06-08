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
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void home(HttpServletRequest request ,HttpServletResponse response)  throws ServletException, IOException {
		System.out.println("home");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String name = "";

		//파라미터로 아이디와 비밀번호 받음
		String parent_id = (String) request.getParameter("parent_id");
		String parent_pw = (String) request.getParameter("parent_pw");

		System.out.println("id = " + parent_id + "pw = " + parent_pw);

		try {
			//로그인 확인하는 메소드 호출
			name = Login(parent_id,parent_pw);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}

		//로그인 성공 실패 여부 확인해서 메세지 보내기
		if(name.equals("fail"))
			response.getWriter().print("fail");
		else response.getWriter().print(name);
	}
	
	public String Login(String parent_id,String parent_pw) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;		
		boolean loginCheck = false;
		String id ="";
		String pw="";
		String name="";
		String sql = "select * from logintest";
		try {
			System.out.println("mysql 접속중");
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
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				conn.close();
				rs.close();
				System.out.println("mysql 끝");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		if(loginCheck)
			return name;
		else
			return "fail";
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://117.17.142.207:3306/hanium", "kimys", "kim111");
		return c;
	}

}
