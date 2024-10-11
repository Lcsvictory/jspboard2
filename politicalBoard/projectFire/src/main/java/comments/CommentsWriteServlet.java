package comments;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JSFunction;

@WebServlet("/CommentWrite.do")
public class CommentsWriteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idx = req.getParameter("idx");
		String name = req.getParameter("name");
		String content = req.getParameter("content");
		String pass = req.getParameter("pass");
		String currentPage = req.getParameter("currentPage");
		
		CommentsDAO dao = new CommentsDAO();
		CommentsDTO dto = new CommentsDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setContent(content);
		dto.setPass(pass);
		
		int result = dao.insertCommentWrite(dto);
		dao.close();
		if (result == 1) {
			resp.sendRedirect(currentPage+ "?idx=" +idx); //새로고침하겠다.
			//req.getRequestDispatcher(currentPage).forward(req, resp);
		}
		else {
			JSFunction.alertLocation(resp, "댓글 작성에 실패하였습니다.", currentPage);
		}
	}
}
