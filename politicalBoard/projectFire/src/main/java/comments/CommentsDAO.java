package comments;


import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.ConnectionPool;


public class CommentsDAO extends ConnectionPool{
	public CommentsDAO() { //db연결
		super();
	}
//	public int selectCount(String idx) {} 댓글수는 boardDAO의 selectListPage에서 구함.

	//내림차순 오름차순 추천순 구현
	public List<CommentsDTO> selectView(String idx, Map<String, Object> map) {
		List<CommentsDTO> comment = new Vector<>();
		
		//String query = "SELECT * FROM comments c join board b ON c.idx = b.idx WHERE b.idx=?";
		String query = "SELECT * FROM ("
			    + " SELECT Tb.*, ROW_NUMBER() OVER (ORDER BY Tb.comment_id asc) AS rNum FROM "
			    + " (SELECT * FROM comments ORDER BY comment_id asc) Tb"
			    + ") result WHERE rNum BETWEEN ? AND ? AND idx=?";
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			pstmt.setString(3, idx);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CommentsDTO dto = new CommentsDTO();
				dto.setComment_id(rs.getString(1));
				dto.setIdx(rs.getString(2));
				dto.setName(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPass(rs.getString(5));
				dto.setPostdate(rs.getString(6));
				dto.setUpcount(rs.getString(7));
				dto.setDowncount(rs.getString(8));
				
				comment.add(dto);
			}
		} catch (Exception e) {
			System.out.println("댓글 조회 중 예외 발생");
			e.printStackTrace();
		}
		return comment;
	}
	
	public int insertCommentWrite(CommentsDTO dto) {
		int result = 0;
		try {
			String query = "INSERT INTO comments( "
						+ " idx, name, content, pass)"
						+ " VALUES ( "
						+ " ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getIdx());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPass());
			result = pstmt.executeUpdate(); //1행이 업데이트 되었습니다.
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean confirmCommentPassword(String pass, String comment_id) {
		boolean isCorr = true;
		try {
			String sql = "SELECT COUNT(*) FROM comments WHERE pass=? AND comment_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pass);
			pstmt.setString(2, comment_id);
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) == 0) { //첫번째 행의 값이 0이다. 일치하는게 없다. 틀렸다.
				isCorr = false;
			}
		} catch (Exception e) {
			isCorr = false;
			e.printStackTrace();
		}
		return isCorr;
	}
	public void deleteComment(String comment_id) { //댓글 삭제 메소드
		String query = "DELETE FROM comments WHERE comment_id=?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, comment_id);
			pstmt.executeUpdate(); //몇개행이 삭제될지 모른다. 1개이상이 삭제될듯. 0개일 수도 있어.
			//댓글이 없는 게시글일수도있잖아
			
		} catch (Exception e) {
			System.out.println("댓글 삭제 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	public void updateCommentUPDOWNCount(String comment_id, String isUPDOWN) {
		String query = "";
		if (isUPDOWN.equals("UP")) {
			query = "UPDATE comments SET upcount=upcount+1 WHERE comment_id=?";
		} else {
			query = "UPDATE comments SET downcount=downcount+1 WHERE comment_id=?";
		}
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, comment_id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("댓글 추천비추천 수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
}
