package board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fileupload.FileUtil;

@WebServlet("/Download.do")
public class DownloadServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ofile = req.getParameter("ofile");
		String sfile = req.getParameter("sfile");
		String idx = req.getParameter("idx");
		
		//fileutil에서 만들어놓은 메소드 활용. 아니였으면 여기서 다 구현했어야함.
		FileUtil.download(req, resp, "/Uploads", sfile, ofile);
		
		//다운로드 카운트 1증가
		BoardDAO dao = new BoardDAO();
		dao.downloadCountPlus(idx);
		dao.close();
	}
}	
