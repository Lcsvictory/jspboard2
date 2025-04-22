package board;

import java.io.IOException;
import java.util.ArrayList;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JSFunction;

@WebServlet("/WriteServlet.do")
@MultipartConfig(
		maxFileSize = 10240 * 10240 * 1, //한개당 10mb로 변경
		maxRequestSize = 10240 * 10240 * 10 //총 100mb가능
	)


public class WriteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			//실제 디렉토리에 실제 파일을 저장한다.
			String saveDirectory = getServletContext().getRealPath("/Uploads");
//			System.out.println(saveDirectory); 
			ArrayList<String> listFileName = null;
			//첨부파일이 있으면 파일을 업로드해라.
			listFileName = FileUtil.multipleFile(req, saveDirectory); //일단 무조건 멀티파일 메소드 실행.		
			int idx = insertMyNoFile(req); //db에 파일 제외한 데이터 저장. 저장된idx가져옴.
			if (listFileName != null) {
		            for (String originalFileName : listFileName) {//저장된파일명 변경한다.숫자로만구성되게
		                String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
		                insertFile(idx, originalFileName, savedFileName); // 파일 데이터를 게시글 ID에 연결
		            }
		        }
			String backUrl = req.getParameter("backUrl"); //글쓰기 페이지가 아닌 리스트로 이동.
//			System.out.println("리다이렉트 테스트");
//			System.out.println(backUrl);
			resp.sendRedirect(backUrl);
		} catch (Exception e) {
			e.printStackTrace();
			JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", req.getParameter("currentPage"));
			//글쓰기 페이지로 다시 이동.
		}
		
	}
	
	//db에 저장된 게시물에 파일을 추가한다.
	private void insertFile(int idx, String oFileName, String sFileName) {
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();
		dto.setOfile(oFileName);
		dto.setSfile(sFileName);
		
			
		dao.updateFile(idx, dto);
		dao.close();
			
	}
	//파일 없이 데이터만 일단 저장함.
	private int insertMyNoFile(HttpServletRequest request) {
		int result = 0;
		BoardDAO dao = new BoardDAO();
		//파일 업로드 외 처리
			BoardDTO dto = new BoardDTO(); 
			dto.setName(request.getParameter("name"));
			dto.setTitle(request.getParameter("title"));
			dto.setContent(request.getParameter("content"));
			dto.setPass(request.getParameter("pass"));
			dto.setBoardid(request.getParameter("boardid"));
			
			result = dao.insertWriteGetIdx(dto);
			dao.close();
			return result;
			
	}
}
