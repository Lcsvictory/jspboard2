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

@WebServlet("/EditServlet.do")
@MultipartConfig(
		maxFileSize = 10240 * 10240 * 1, //한개당 10mb로 변경
		maxRequestSize = 10240 * 10240 * 10 //총 100mb가능
	)


public class EditServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idx = 0;
		try {
			//실제 디렉토리에 실제 파일을 저장한다.
			String saveDirectory = getServletContext().getRealPath("/Uploads");
			ArrayList<String> listFileName = null;
			//첨부파일이 있으면 파일을 업로드해라.
			listFileName = FileUtil.multipleFile(req, saveDirectory); //일단 무조건 멀티파일 메소드 실행.		
			idx = UpdateMyNoFile(req); //db에 파일 제외한 데이터 저장. idx가져옴.
			
			if (listFileName != null && !listFileName.isEmpty()) { //새로 업데이트한 파일이 있습니까
		            for (String originalFileName : listFileName) {//저장된파일명 변경한다.숫자로만구성되게
		                String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
		                if(savedFileName.equals("noFile")) { //추가한 파일이 없다면.
		                	break;
		                }
		                insertFile(idx, originalFileName, savedFileName); // 파일 데이터를 게시글 ID에 연결
		            }
		        }
			//파일을 새로 안올렸으면 그냥 파일처리는 안해버리면됨.
			resp.sendRedirect("View.jsp?idx=" + idx);
		} catch (Exception e) {
			e.printStackTrace();
			JSFunction.alertLocation(resp, "수정에 실패했습니다.", "View.jsp?idx=" + idx);
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
	//파일 없이 데이터만 일단 수정함.
	private int UpdateMyNoFile(HttpServletRequest request) {
		int idx = 0;
		BoardDAO dao = new BoardDAO();
		//파일 업로드 외 처리
			BoardDTO dto = new BoardDTO(); 
			dto.setName(request.getParameter("name"));
			dto.setTitle(request.getParameter("title"));
			dto.setContent(request.getParameter("content"));
			dto.setPass(request.getParameter("pass"));
			dto.setIdx(request.getParameter("idx"));
			dto.setBoardid(request.getParameter("boardid"));
			//boardid는 변경하지 못함. 삭제하고 다시 만들어야 한다.
			//아니 변경하는게 맞다. 내가 해보니까 변경 가능해야한다. 0428 21:11
			
			idx = dao.updatePost(dto);
			dao.close();
			return idx;
	}
}
