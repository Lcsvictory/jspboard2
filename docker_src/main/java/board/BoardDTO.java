package board;

public class BoardDTO {
	private String idx;
	private String name;
	private String title;
	private String content;
	private String pass;
	private String postdate;
	private String ofile;
	private String sfile;
	private String visitcount;
	private String downloadcount;
	private String upcount;
	private String downcount;
	private String boardid;
	private String comment_count;
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
	public String getBoardid() {
		return boardid;
	}
	public void setBoardid(String boardid) {
		this.boardid = boardid;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPostdate() {
		return postdate;
	}
	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}
	public String getOfile() {
		if (this.ofile == null) {
			return "";
		}
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfile() {
		if (this.sfile == null) {
			return "";
		}
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	public String getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(String visitcount) {
		this.visitcount = visitcount;
	}
	public String getDownloadcount() {
		return downloadcount;
	}
	public void setDownloadcount(String downloadcount) {
		this.downloadcount = downloadcount;
	}
	public String getUpcount() {
		return upcount;
	}
	public void setUpcount(String upcount) {
		this.upcount = upcount;
	}
	public String getDowncount() {
		return downcount;
	}
	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}
	
	
}
