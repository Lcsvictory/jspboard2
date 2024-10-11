package board;

import java.io.IOException;

import comments.CommentsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JSFunction;

@WebServlet("/Ddabong.do")
public class DdabongServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		

		String idx = req.getParameter("idx");
		String comment_id = req.getParameter("comment_id"); //comment_id가 없다면 null 이 할당. "null"아님.
		String isUP = req.getParameter("isUP");
		Cookie[] cookies = req.getCookies();
		boolean hasCookie = false; //추천이나 비추는 한번만 하면 되니까 전역변수로 선언해도 된다.
		
		if (comment_id == null) { //게시글이라면. 코멘트아이디가 없다는건 게시글이라는말.
			// 쿠키 검사
			if (cookies != null) { 
			    for (Cookie cookie : cookies) {
			        if (cookie.getName().equals("boardisUP_" + idx) || cookie.getName().equals("boardisDOWN_" + idx)) {
			        	hasCookie = true; //이미 추천이나 비추를 했다.
			            break;
			        }
			    }
			}
			if (hasCookie) {//이미 쿠키가 있다? 그럼 더이상 추천or비추 못함.
				JSFunction.alertBack(resp, "이미 추천or비추를 하셨습니다."); //View.jsp로 이동.
			} else {//쿠키가 없다? 그럼 가능하지.
				BoardDAO dao = new BoardDAO();
				if (isUP.equals("yes")) { //추천이라면.
					dao.updateUPDOWNCount(idx, "UP");
					dao.close();
					// 새 쿠키 생성
				    Cookie newCookie = new Cookie("boardisUP_" + idx, "1");
				    newCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (예: 24시간)
				    newCookie.setPath("/"); // 쿠키 경로 설정
				    resp.addCookie(newCookie); // 응답에 쿠키 추가
				    resp.sendRedirect("View.jsp?idx=" + idx); //view.jsp로 이동
				} else {//비추라면.
					dao.updateUPDOWNCount(idx, "DOWN");
					dao.close();
					// 새 쿠키 생성
				    Cookie newCookie = new Cookie("boardisDOWN_" + idx, "1");
				    newCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (예: 24시간)
				    newCookie.setPath("/"); // 쿠키 경로 설정
				    resp.addCookie(newCookie); // 응답에 쿠키 추가
				    resp.sendRedirect("View.jsp?idx=" + idx); //view.jsp로 이동
				}
			}
			
		}else { //댓글이라면. comment_id가 "null"이 아니라는 뜻.
			// 쿠키 검사
			if (cookies != null) { 
			    for (Cookie cookie : cookies) {
			        if (cookie.getName().equals("commentisUP_" + comment_id) || cookie.getName().equals("commentisDOWN_" + idx)) {
			        	hasCookie = true; //이미 추천이나 비추를 했다.
			            break;
			        }
			    }
			}
			if (hasCookie) {//이미 쿠키가 있다? 그럼 더이상 추천or비추 못함.
				JSFunction.alertBack(resp, "이미 추천or비추를 하셨습니다."); //View.jsp로 이동.
			} else {//쿠키가 없다? 그럼 가능하지.
				CommentsDAO dao = new CommentsDAO();
				if (isUP.equals("yes")) { //추천이라면.
					dao.updateCommentUPDOWNCount(comment_id, "UP");
					dao.close();
					// 새 쿠키 생성
				    Cookie newCookie = new Cookie("commentisUP_" + comment_id, "1");
				    newCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (예: 24시간)
				    newCookie.setPath("/"); // 쿠키 경로 설정
				    resp.addCookie(newCookie); // 응답에 쿠키 추가
				    resp.sendRedirect("View.jsp?idx=" + idx); //view.jsp로 이동
				} else {//비추라면.
					dao.updateCommentUPDOWNCount(comment_id, "DOWN");
					dao.close();
					// 새 쿠키 생성
				    Cookie newCookie = new Cookie("commentisDOWN_" + idx, "1");
				    newCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (예: 24시간)
				    newCookie.setPath("/"); // 쿠키 경로 설정
				    resp.addCookie(newCookie); // 응답에 쿠키 추가
				    resp.sendRedirect("View.jsp?idx=" + idx); //view.jsp로 이동
				}
			}
		}
		
	}
}
