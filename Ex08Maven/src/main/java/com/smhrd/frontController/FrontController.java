package com.smhrd.frontController;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smhrd.controller.JoinService;
import com.smhrd.controller.LoginService;
import com.smhrd.controller.LogoutService;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// FrontController 패턴
	// : 어떠한 요청(request)이 들어오더라도 가장 먼저 실행되며,
	//	 모든 요청을 받고 처리하는 서블릿!
	//	 현) 기능 요청을 하는 service(controller, servlet) 중심 설계
	//	 servlet의 단점 : 실행시에 상속관계에 있는 파일들을 실행해야 하며
	//	 				servlet의 개수가 많아질수록 서버에 부하가 걸릴 수 있다.
	// 리팩토링 진행 방식) frontController만 servlet 형태이고
	//					나머지 servlet(controller)는 일반 클래스로 변형.
	// 1. 요청 형식을 단일화
	//	  기존) join.jsp -> JoinService
	//			update.jsp -> UpdateService
	//	  변경안) join.jsp -> JoinService.do
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 2. 요청이 들어온 url 구분
		String url = request.getRequestURI();
		System.out.println("요청 url : " + url);
		// substring : 문자열 자르도록 해주는 메서드
		// substriong(2): 인덱스 0번부터 시작해서 0~1번은 자르고 2번~끝까지의 문자열 리턴(결과값)
		// 2.1 필요한 servlet의 이름 가져오기
		String path = request.getContextPath();
		System.out.println("요청 path : "+ path);
		System.out.println("요청 path의 길이 : "+ path.length());
		// 2.2 substring 이용해서 필요한 부분 추출
		String result = url.substring(path.length()+1);
		System.out.println("최종 url : " + result);
		// 3. 요청된 url에 따라 실행해야하는 기능(service)연결
		//	흐름 : jsp 실행 -> 요청 처리하는 servlet(frontController)
		//		-> 각각의 요청에 맞는 기능을 처리하는 service로 이동
		String move = null;
		// move 변수에 담기는 데이터는 각각 service클래스에서 execute 메서드의 리턴값(어디로 이동할지)
		if (result.equals("LoginService.do")) {
			LoginService login = new LoginService();
			// execute 메서드를 통해서 기능 실행
			move = login.execute(request, response);
		} else if(result.equals("LogoutService.do")) {
			LogoutService logout = new LogoutService();
			move = logout.execute(request, response);
		} else {
			move = null;
		}
		if(move.contains("redirect:/")) {
			System.out.println(move.substring(10));
			response.sendRedirect(move.substring(10));
		}else {
			//포워드방식
			RequestDispatcher rd = request.getRequestDispatcher(move);
			rd.forward(request, response);
		}
		
	}

}
