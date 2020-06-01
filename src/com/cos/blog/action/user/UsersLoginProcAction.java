package com.cos.blog.action.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blog.action.Action;
import com.cos.blog.model.Users;
import com.cos.blog.repository.UsersRepository;
import com.cos.blog.util.Script;

public class UsersLoginProcAction implements Action{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 0. 유효성 검사
		if
		(
				request.getParameter("username").equals("") ||
				request.getParameter("username") == null ||
				request.getParameter("password").equals("") ||
				request.getParameter("password") == null ||
				request.getParameter("remember").equals("") ||
				request.getParameter("remember") == null
		) {
			return;
		}
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsersRepository usersRepository = UsersRepository.getInstance();
		Users user = usersRepository.findByUsernameAndPassword(username, password);
		//로그인 성공 (세션은 request가 들고 있음)
		if(user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("principal", user); // 인증 주체
			
			if(request.getParameter("remember") != null) { // null이 아니면 무조건 체크박스를 체크했다는 것!
				Cookie cookie = new Cookie("rmemeber", user.getUsername());
				response.addCookie(cookie);
			}
			Script.href("로그인 성공", "/blog/board?cmd=home", response);
		}else {
			Script.back("로그인 실패", response);
		}
	}
	
}

