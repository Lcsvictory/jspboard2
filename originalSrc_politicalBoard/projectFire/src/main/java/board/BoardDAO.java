package board;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.ConnectionPool;
import fileupload.FileUtil;

public class BoardDAO extends ConnectionPool{
	public BoardDAO() {
		super();
	}
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		//게시판의 종류도 구분해서 가져와야함.
		String query = "select count(*) from board";
		if (map.get("boardid") != "0") { //게시판조회.
			query += " WHERE boardid=" + map.get("boardid");
		}
		else { //전체 검색이다. boardid == 0일때
			if (map.get("searchWord") != null) {
				query += " WHERE " + map.get("searchField") + " " + "LIKE '%" + map.get("searchWord") + "%'";
			}
		}
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			totalCount = rs.getInt(1); //첫번째 컬럼 값을 가져옴
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		return totalCount;
	}
	public int selectCount() {
		int totalCount = 0;
		//게시글이 총 몇개냐.
		String query = "select count(*) from board";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			totalCount = rs.getInt(1); //첫번째 컬럼 값을 가져옴
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		return totalCount;
	}
	
	//페이징처리 가능함.
	public List<BoardDTO> selectListPage(Map<String, Object> map){ //검색창에 입력한 값을 request영역에서 가져와서 map객체에 저장하고 그걸 인자로 받음
		List<BoardDTO> bbs = new Vector<BoardDTO>();
		
		String query = "SELECT * FROM ( "
					+ " 	SELECT Tb.*, ROW_NUMBER() OVER (ORDER BY Tb.idx DESC) AS rNum FROM ( "
					+ " 		SELECT * FROM board";
		
		if (map.get("searchWord") != null ) { //검색어가 있는 전체검색이면.
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
		}
		else if (map.get("boardid") != "0"){ //0이 아니면. 즉 게시물조회면.
			query += " WHERE boardid=" + map.get("boardid");
		}
		//검색어가 없는 전체검색이면.
		query += "		ORDER BY idx DESC "
				+"		) Tb"
				+" ) AS Result"
				+" WHERE rNum BETWEEN ? and ?";
		
		//해당게시물의 댓글수조회
		String commentQuery = "SELECT p.idx, COUNT(c.comment_id) AS comment_count" 
		+ " FROM board p" 
		+ " LEFT JOIN comments c ON p.idx = c.idx WHERE p.idx=? GROUP BY p.idx";
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setIdx(rs.getString("idx"));
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setPass(rs.getString("pass"));
				dto.setPostdate(rs.getString("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				dto.setDownloadcount(rs.getString("downloadcount"));
				dto.setUpcount(rs.getString("upcount"));
				dto.setDowncount(rs.getString("downcount"));
				dto.setBoardid(rs.getString("boardid"));
				
				PreparedStatement commentPstmt = con.prepareStatement(commentQuery);
				commentPstmt.setString(1, dto.getIdx()); // 게시글 idx 뽑아와서 해당게시물의 댓글수조회
			    ResultSet commentRs = commentPstmt.executeQuery();
			    if (commentRs.next()) {
			        dto.setComment_count(commentRs.getString("comment_count"));
			    }
				bbs.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return bbs;
		
	}
	public List<BoardDTO> selectListSort(Map<String, Object> map){ //추천순인지, 댓글순인지.
		List<BoardDTO> bbs = new Vector<BoardDTO>();
		String query = "";
		if (map.get("orderby").equals("ddabong")) {
			query = "SELECT * FROM ( "
					+ " 	SELECT Tb.*, ROW_NUMBER() OVER (ORDER BY Tb.upcount DESC) AS rNum FROM ( "
					+ " 		SELECT * FROM board"
					+ "			ORDER BY upcount DESC "
					+ "			) Tb"
					+ " 	) AS Result WHERE rNum BETWEEN 1 and 5";
			
		} else if (map.get("orderby").equals("comment")) {
			query = "select * from( "
					 + " 	select Tb.*, ROW_NUMBER() OVER (ORDER BY Tb.comment_count DESC) AS rNum from( "
					 + " 		SELECT p.*, COUNT(c.comment_id) AS comment_count FROM board p LEFT JOIN comments c ON p.idx = c.idx GROUP BY p.idx order by comment_count desc "
					 + " 	) Tb"
					 + " ) as result where rNum between 1 and 5";
		}
		
		
		//해당게시물의 댓글수조회
		String commentQuery = "SELECT p.idx, COUNT(c.comment_id) AS comment_count" 
		+ " FROM board p" 
		+ " LEFT JOIN comments c ON p.idx = c.idx WHERE p.idx=? GROUP BY p.idx";
		
		try {
			pstmt = con.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setIdx(rs.getString("idx"));
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setPass(rs.getString("pass"));
				dto.setPostdate(rs.getString("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				dto.setDownloadcount(rs.getString("downloadcount"));
				dto.setUpcount(rs.getString("upcount"));
				dto.setDowncount(rs.getString("downcount"));
				dto.setBoardid(rs.getString("boardid"));
				
				PreparedStatement commentPstmt = con.prepareStatement(commentQuery);
				commentPstmt.setString(1, dto.getIdx()); // 게시글 idx 뽑아와서 해당게시물의 댓글수조회
			    ResultSet commentRs = commentPstmt.executeQuery();
			    if (commentRs.next()) {
			        dto.setComment_count(commentRs.getString("comment_count"));
			    }
				bbs.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return bbs;
		
	}
	
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		
		try {
			String query = "INSERT INTO board( "
						+ " name, title, content, pass, ofile, sfile, boardid)"
						+ " VALUES ( "
						+ " ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPass());
			pstmt.setString(5, dto.getOfile());
			pstmt.setString(6, dto.getSfile());
			pstmt.setString(7, dto.getBoardid());
			
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	public int insertWriteGetIdx(BoardDTO dto) { //파일첨부때문에 만든메소드
		int result = 0;
		
		try {
			String query = "INSERT INTO board( "
						+ " name, title, content, pass, ofile, sfile, boardid)"
						+ " VALUES ( "
						+ " ?, ?, ?, ?, ?, ?, ?)";
//			pstmt = con.prepareStatement(query); 잘 안되면 이걸로 변경
			pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //가장최근에 만든 idx값 가져옴.
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPass());
			pstmt.setString(5, dto.getOfile());
			pstmt.setString(6, dto.getSfile());
			pstmt.setString(7, dto.getBoardid());
			
			
			pstmt.executeUpdate(); //db에 저장했음. 그럼 1이 리턴됨
			
//			String Idxquery = "SELECT MAX(idx) AS LatestIdx FROM board";
//			Statement stmt = con.createStatement();
//			rs = stmt.executeQuery(Idxquery);
//			rs.next();
//			result = rs.getInt(1); 
			ResultSet rs = pstmt.getGeneratedKeys();
//			ResultSet rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()"); 잘 안되면 이걸로 변경
			if (rs.next()) {
			    result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	public int updateFile(int idx, BoardDTO dto) { //파일 여러개 추가할떄 사용하는 메소드
		int result = 0;
		
		try {

			
			 // 먼저 기존 파일 이름을 조회합니다.
	        String existingFilesQuery = "SELECT ofile, sfile FROM board WHERE idx = ?";
	        PreparedStatement pstmtSelect = con.prepareStatement(existingFilesQuery);
	        pstmtSelect.setInt(1, idx);
	        ResultSet rs = pstmtSelect.executeQuery();
	        
	        String existingOfile = "";
	        String existingSfile = "";
	        if (rs.next()) {
	            existingOfile = rs.getString("ofile");
	            existingSfile = rs.getString("sfile");
	        }

	        // 새 파일 이름을 기존 파일 이름에 추가합니다.
	        String updatedOfile = existingOfile == null ? dto.getOfile() : existingOfile + "," + dto.getOfile();
	        String updatedSfile = existingSfile == null ? dto.getSfile() : existingSfile + "," + dto.getSfile();
	        
	        System.out.println(updatedOfile);
	        System.out.println(updatedSfile);
	        // 파일 정보를 업데이트하는 쿼리를 실행합니다.
	        String query = "UPDATE board SET ofile = ?, sfile = ? WHERE idx = ?";
	        PreparedStatement pstmtUpdate = con.prepareStatement(query);
	        pstmtUpdate.setString(1, updatedOfile);
	        pstmtUpdate.setString(2, updatedSfile);
	        pstmtUpdate.setInt(3, idx);
	        
	        result = pstmtUpdate.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물에 파일 추가 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	//View. 게시물 내용 보는것.
	public BoardDTO selectView(String idx) {
		BoardDTO dto = new BoardDTO();
		String query = "SELECT * FROM board WHERE idx=?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPass(rs.getString(5));
				dto.setPostdate(rs.getString(6));
				dto.setOfile(rs.getString(7));
				dto.setSfile(rs.getString(8));
				dto.setVisitcount(rs.getString(9));
				dto.setDownloadcount(rs.getString(10));
				dto.setUpcount(rs.getString(11));
				dto.setDowncount(rs.getString(12));
				dto.setBoardid(rs.getString(13));
			}
			//댓글수 가져오기 추가
			String commentQuery = "SELECT p.idx, COUNT(c.comment_id) AS comment_count" 
					+ " FROM board p" 
					+ " LEFT JOIN comments c ON p.idx = c.idx WHERE p.idx=? GROUP BY p.idx";
			
			PreparedStatement commentPstmt = con.prepareStatement(commentQuery);
			commentPstmt.setString(1, idx); // 게시글 idx 설정
		    ResultSet commentRs = commentPstmt.executeQuery();
		    if (commentRs.next()) {
		        dto.setComment_count(commentRs.getString("comment_count"));
		    }
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}
	
	public void updateVisitCount(String idx) {
		String query = "UPDATE board SET visitcount=visitcount+1 WHERE idx=?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	public void updateUPDOWNCount(String idx, String isUPDOWN) {
		String query = "";
		if (isUPDOWN.equals("UP")) {
			query = "UPDATE board SET upcount=upcount+1 WHERE idx=?";
		} else {
			query = "UPDATE board SET downcount=downcount+1 WHERE idx=?";
		}
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 추천비추천 수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public void downloadCountPlus(String idx) {
		String sql = "UPDATE board SET"
					+ " downloadcount=downloadcount+1"
					+ " WHERE idx=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("다운로드 횟수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true;
		try {
			String sql = "SELECT COUNT(*) FROM board WHERE pass=? AND idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pass);
			pstmt.setString(2, idx);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) == 0) {
				isCorr = false;
			}
		} catch (Exception e) {
			isCorr = false;
			e.printStackTrace();
		}
		return isCorr;
	}
	public int updatePost(BoardDTO dto) { //수정, idx를 반환한다.
		int result = 0;
		
		try {
			String query = "UPDATE board SET "
						+ " name=?, title=?, content=?, pass=?, boardid=? WHERE idx=?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPass());
//			pstmt.setString(5, dto.getOfile());
//			pstmt.setString(6, dto.getSfile());
			pstmt.setString(5, dto.getBoardid());
			pstmt.setString(6, dto.getIdx());
			
			
			pstmt.executeUpdate(); //db에 내용을 수정했음.
			
			result = Integer.parseInt(dto.getIdx());
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	public void deletePost(String idx) { //삭제
		
		
		String query = "DELETE FROM board WHERE idx=?";
		try {
			//fk인 댓글 먼저 삭제하는것임. 게시글먼저 삭제는 안됨. 진짜 안되는것.
			
			pstmt = con.prepareStatement(query);//게시글은 무조건 1개행이 삭제된다.
			pstmt.setString(1, idx); // 게시글삭제
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 삭제(댓글 삭제 후) 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public void deletePostComment(String idx) { //게시글 삭제시 댓글 삭제먼저 하기위한 메소드
		String commentdelete = "DELETE FROM comments WHERE idx=?";
		try {
			pstmt = con.prepareStatement(commentdelete);
			pstmt.setString(1, idx);
			pstmt.executeUpdate(); //몇개행이 삭제될지 모른다. 1개이상이 삭제될듯. 0개일 수도 있어.
			//댓글이 없는 게시글일수도있잖아
			
		} catch (Exception e) {
			System.out.println("게시물 삭제(댓글먼저 삭제) 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
}
