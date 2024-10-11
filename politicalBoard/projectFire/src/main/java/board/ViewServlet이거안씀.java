package board;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/View.do")
public class ViewServlet이거안씀 extends HttpServlet{
private static final long serialVersionUID = 1L;
	@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BoardDAO dao = new BoardDAO();
			String idx = req.getParameter("idx");
			dao.updateVisitCount(idx);
			BoardDTO dto = dao.selectView(idx);
			dao.close();
			
			//엔터를 줄바꿈으로. 키보드 엔터 = \r\n
			dto.setContent(dto.getContent().replace("\r\n", "<br>"));
			
			//첨부파일 확장자 추출 및 이미지 타입 확인
			String ext = null, fileName = dto.getSfile();
			if (fileName != null) {
				ext = fileName.substring(fileName.lastIndexOf(".")+1);//substring(2) 2부터 끝까지
			}
			String[] mimeStr = {"png", "jpg", "gif"};
			List<String> mimeList = Arrays.asList(mimeStr);
			boolean isImage = false;
			if (mimeList.contains(ext)) {
				isImage = true;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("isImage", isImage);
			req.getRequestDispatcher("/View.jsp").forward(req, resp);
		}
	
}
