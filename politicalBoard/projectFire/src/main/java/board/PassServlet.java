package board;

import java.io.IOException;

import comments.CommentsDAO;
import fileupload.FileUtil;
import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JSFunction;

@WebServlet("/PassServlet.do")
public class PassServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mode = req.getParameter("mode");
		String idx = req.getParameter("idx");
		String comment_id = req.getParameter("comment_id"); //값이 없다면 문자열 "null"이 출력됨
		String pass = req.getParameter("pass");
		String backUrl = req.getParameter("backUrl"); //댓글이라면 view.jsp가 오고 게시글이라면 게시글 리스트.jsp가 온다.(Left, Right 등등)
		boolean Confirmed = false; 
		
		if (idx != null && "null".equals(comment_id)) { //게시글이라면.null 이 아닌 "null"인 이유는 null 을 쌍따옴표로 묶어서 Parameter에 담아서 보냈기 때문에 문자열 "null"이 되버린거다.
			System.out.println("게시글");
			BoardDAO dao = new BoardDAO();
			BoardDTO dto = new BoardDTO();
			dto = dao.selectView(idx);//파일 삭제를 위한 현재 게시글의 dto를 가져온다.
			Confirmed = dao.confirmPassword(pass, idx);
			if (Confirmed) { //비번이 맞다면
				if (mode.equals("delete")) {//삭제면
					
					
					
					dao.deletePostComment(idx);
					dao.deletePost(idx);
					FileUtil.deleteFile(req, "/Uploads", dto.getSfile());
					dao.close();
					resp.sendRedirect(backUrl); //삭제버튼을 누른 페이지로 이동한다.
				}
				else {//수정이면
					req.setAttribute("dto", dto);//수정할 게시글의 idx도 담겨있음.
					req.getRequestDispatcher("Edit.jsp").forward(req, resp);//edit.jsp로 리다이렉트
					
				}
			}else { //비번이 틀렸다면.
				JSFunction.alertBack(resp, "비밀번호가 다릅니다");
			}
		}
		else if (comment_id != null && !comment_id.isEmpty() && backUrl.equals("View.jsp")) { //댓글이라면.
			System.out.println("댓글");
			CommentsDAO comdao = new CommentsDAO();
			Confirmed = comdao.confirmCommentPassword(pass, comment_id);
			if (Confirmed) {
				if (mode.equals("delete")) {//삭제면
					comdao.deleteComment(comment_id);
					comdao.close();
					resp.sendRedirect(backUrl + "?idx=" + idx);
				}
				else {//수정이면 0427 11:23 댓글 수정기능은 미구현. View.jsp에서 수정해야하는데 새로고침을 하지않고 댓글을 업데이트 하는 방법을 모르겠음.
					
				}
			}else { //비번이 틀렸다면.
				JSFunction.alertBack(resp, "비밀번호가 다릅니다");
			}
		}else {
			JSFunction.alertBack(resp, "게시글도, 댓글도 아닙니다. 무엇을 삭제하고싶은겁니까?");
		}
		
		
	}
}	
