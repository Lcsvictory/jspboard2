<%@page import="utils.CommentPage"%>
<%@page import="java.util.Arrays"%>
<%@page import="comments.CommentsDAO"%>
<%@page import="comments.CommentsDTO"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="utils.BoardPage"%>
<%@page import="board.BoardDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
BoardDAO dao;
BoardDTO dto;

String idx = request.getParameter("idx");

boolean hasCookie = false;
Cookie[] cookies = request.getCookies();

// 쿠키 검사
if (cookies != null) {
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("visited_" + idx)) {
            hasCookie = true;
            break;
        }
    }
}

// 쿠키가 없는 경우에만 조회수 업데이트 및 쿠키 생성\
//hasCookie가 false면 쿠키가 없다면. 조회수를 증가시킨다.
if (!hasCookie) {
    dao = new BoardDAO();
    dao.updateVisitCount(idx); // 조회수 업데이트
    dto = dao.selectView(idx); // 게시글 조회
    dao.close();

    // 새 쿠키 생성
    Cookie newCookie = new Cookie("visited_" + idx, "1");
    newCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (예: 24시간)
    newCookie.setPath("/"); // 쿠키 경로 설정
    response.addCookie(newCookie); // 응답에 쿠키 추가
} else {//쿠키가 있다면 조회수 증가 없다.
    dao = new BoardDAO();
    dto = dao.selectView(idx); // 쿠키가 있는 경우, 조회수 증가 없이 게시글만 조회
    dao.close();
}

//dao.updateVisitCount(idx);
//BoardDTO dto = dao.selectView(idx);
//dao.close();

//엔터를 줄바꿈으로. 키보드 엔터 = \r\n
dto.setContent(dto.getContent().replace("\r\n", "<br>"));

//첨부파일 확장자 추출 및 이미지 타입 확인

String[] sfilenames = dto.getSfile().split(",");
String[] mimeStr = {"png", "jpg", "gif"};
List<String> mimeList = Arrays.asList(mimeStr);




//댓글처리
Map<String, Object> param = new HashMap<String, Object>(); //start, en페이지 저장용도.
int totalCount = Integer.parseInt(dto.getComment_count()); //현재 댓글 갯수
int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE")); //한페이지에 10개
int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK")); //10개가 한블럭
int pageNum = 1; //일단 현재페이지를 1로 하자.
String pageTemp = request.getParameter("pageNum");
if (pageTemp != null && !pageTemp.equals("")){ //페이지값이 있다면 그걸로 바꾸자.
	pageNum = Integer.parseInt(pageTemp);
}
int start = (pageNum - 1) * pageSize + 1;
int end = pageNum * pageSize;
param.put("start", start);
param.put("end", end);

CommentsDAO commentsdao = new CommentsDAO();
List<CommentsDTO> comments = commentsdao.selectView(dto.getIdx(), param);
commentsdao.close();

String backUriTemp = request.getRequestURI(); /* 글쓰기 페이지로 넘어가기 전에 요청한 위치를 저장해서 같이 보내줌. */
String backUrl = backUriTemp.substring(backUriTemp.lastIndexOf("/") + 1);
backUrl = URLEncoder.encode(backUrl, "UTF-8"); //한글 인코딩문제 해결

int temp = Integer.parseInt(dto.getUpcount());
int temp2 = Integer.parseInt(dto.getDowncount());
String vote = temp - temp2 + ""; //추천수 계산 비추가 많으면 -추천수;




%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/style.css">
<title>정치 게시판</title>
<script type="text/javascript">
function goBack(){
    window.history.back();
 }
 function submit(){
	 document.getElementById('downloadForm').submit();
 }
 function validateForm(form){
		if (form.name.value == ""){
			alert("작성자를 입력하세요.");
			form.name.focus();
			return false;
		}
		if (form.title.value == "") {
			alert("제목을 입력하세요.");
			form.title.focus();
			return false;
		}
		if (form.content.value == ""){
			alert("내용을 입력하세요.");
			form.content.focus();
			return false;
		}
		if (form.pass.value == ""){
			alert("비밀번호를 입력하세요.");
			form.pass.focus();
			return false;
		}
	}
</script>
</head>
<body style="overflow-x: hidden;">
<%@ include file="common/header.jsp" %> <!-- 상단 네비게이션바 -->

<main>
	<%@ include file="common/ad.jsp" %>
	<div class="container">
		<table>
				<tr>
					<td style="border-bottom: 3px solid gray; font-size: 1.5em;">
					<%!
					String type = "";
					String style = "";
					String retunPage ="";
					%>
						<%
						switch(Integer.parseInt(dto.getBoardid())) {
						case 1: type = "진보"; style="color: blue;"; retunPage = "Left.jsp"; break;
						case 2: type = "보수"; style="color: red;"; retunPage = "Right.jsp"; break;
						case 3: type = "토론"; style="color: brown;"; retunPage = "Discussion.jsp"; break;
						case 4: type = "자유"; style="color: sky;"; retunPage = "Free.jsp"; break;
						case 5: type = "뉴스"; break;
						case 999 : type= "공지"; style="text-decoration: underline;"; break;
						}
						%>
						<%=type %>게시판
					</td>
				</tr>
				<tr>
					<td style="width: 5%; text-align: left; border-bottom: none; font-size: 1.2em;"><b><%=dto.getTitle() %></b></td>
				</tr>
				<tr>
					<td>
						<span><%=dto.getName() %></span>&nbsp;&nbsp;&nbsp;<span><%=dto.getPostdate() %></span>
						<div style="width: 100%; text-align: right;">
				            조회수 <%=dto.getVisitcount() %>
				            추천수 <%=vote %>
				            댓글수 <%=dto.getComment_count() %>
	        			</div>
					</td>
				</tr>
				<tr>
					<td style="border-bottom: none;">
					<br><br><br>
					<%=dto.getContent() %>
					<br>
					<%!String[] sfiles = {}; String[] ofiles = {}; %>
					<%
					if (dto.getSfile() != null) { //일단 파일이 있으면
					sfiles = dto.getSfile().split(",");
					ofiles = dto.getOfile().split(",");
						for (int i = 0; i < sfiles.length; i++){
							String ext = null;
							ext = sfiles[i].substring(sfiles[i].lastIndexOf(".")+1);//substring(2) 2부터 끝까지
							if (mimeList.contains(ext)) {//이미지라면
					%>
					<br><img src="Uploads/<%=sfiles[i] %>" style="max-width:100%"><!--본문에 이미지 첨부  -->
					<%
							}//이미지 아니면 아무것도 추가하지 않음.
						} 
					} 
					%>
					</td>
				</tr>
				<tr>
					<td style="text-align: center; border-bottom: none;"> 
					<a href="Ddabong.do?isUP=yes&idx=<%=dto.getIdx() %>"><img src="img/따봉.jpg" width="30px;" height="30px;"></a><%=dto.getUpcount() %>
					<a href="Ddabong.do?isUP=no&idx=<%=dto.getIdx() %>"><img src="img/노따봉.jpg" width="30px;" height="30px;"></a><%=dto.getDowncount() %></td>
				</tr>
				<tr >
					<td >
						<fieldset>
							<legend>파일 리스트</legend>
							<%
							if (dto.getOfile() != null){ //파일 있으면 파일 리스트에 다운가능한 파일을 보여줘.
								for (int i = 0; i < ofiles.length; i++){
							%>
							&nbsp;&nbsp;<span>
							<a href="Download.do?ofile=<%=ofiles[i] %>&sfile=<%=sfiles[i] %>&idx=<%=dto.getIdx() %>"><%=ofiles[i] %></a></span>
							<%
								}
							}%>
						</fieldset>
					</td>
				</tr>
				
				<!-- 댓글란 -->
				<%
				
				
				if (comments != null && !comments.isEmpty()){
					for (CommentsDTO comdto : comments){
						int com_temp = Integer.parseInt(comdto.getUpcount());
						int com_temp2 = Integer.parseInt(comdto.getDowncount());
						String com_vote = com_temp - com_temp2 + ""; //추천수 계산 비추가 많으면 -추천수;
				%>
				<tr class="comment" >
					<td style="padding-top: 10px;">
					<span style="font-size: 0.8em;"><%=comdto.getName() %></span>
					 <%=comdto.getContent() %> 
					<div style="float:right; width: auto;"> <!-- 날짜와 추천수 오른쪽정렬 -->
					<span style="font-size: 0.8em;"><%=comdto.getPostdate() %></span> &nbsp;
					<span>
						<a href="Ddabong.do?isUP=yes&comment_id=<%=comdto.getComment_id() %>&idx=<%=dto.getIdx() %>"><img src="img/따봉.jpg" width="12px;" height="12px;"></a><%=comdto.getUpcount() %> 
						<a href="Ddabong.do?isUP=no&comment_id=<%=comdto.getComment_id() %>&idx=<%=dto.getIdx() %>"><img src="img/노따봉.jpg" width="12px;" height="12px;"></a><%=comdto.getDowncount() %>
						<a href="PassCheck.jsp?mode=delete&comment_id=<%=comdto.getComment_id() %>&idx=<%=dto.getIdx() %>&backUrl=<%=currentPage %>">삭제</a>
						<!-- 현재페이지. 댓글은 view페이지에서만 볼 수 있으니까. -->
					</span>
					</div>
					</td>
				</tr>
				<%} 
				} 
				else{
				%>
					<tr>
						<td style="text-align: center; font-size: 1.5em;">첫번째 댓글의 주인공이 되어보세요.</td>
					</tr>
				<%} %>
				<tr>
					<td style="text-align: center;">
						<%= CommentPage.pagingStr(totalCount, pageSize, blockPage, pageNum, request.getRequestURI(), idx) %>
					</td>
				</tr>
		</table>
		<fieldset style="margin-bottom: 10px; margin-top: 10px;">
		<legend>댓글 입력</legend>
			<form method="post" action="CommentWrite.do" onsubmit="return validateForm(this);"><!-- 댓글 입력창-->
			<input type="hidden" name="idx" value="<%=dto.getIdx() %>">
			<input type="hidden" name="currentPage" value="<%=currentPage %>">
					<div style="width: auto; ">
						<input type="text" name="name" placeholder="작성자">
					</div> 
					<div style="width: auto; ">
						<input type="password" name="pass" placeholder="비밀번호">
					</div>
					<div style="width: 100%; margin-top: 5px;">
						<textarea rows="3" cols="100" name="content"></textarea>
					</div>
					<button style="float: right" type="submit">작성완료</button>
			</form>
		</fieldset>
		<table>
			<tr>
				<td style="text-align: right;">
				<button onclick="location.href='PassCheck.jsp?mode=edit&idx=<%=dto.getIdx() %>'">수정하기</button><!-- backUrl필요없다. -->
				<button onclick="location.href='PassCheck.jsp?mode=delete&idx=<%=dto.getIdx() %>&backUrl=<%=retunPage %>'">삭제하기</button><!-- 미완성-->
				<!-- 게시글 목록으로 이동. 게시글이 삭제되었으니 View로 이동할 수가 없다. -->
					<button type="button" onclick="location.href='<%=retunPage%>'">목록보기</button>
				</td>
			</tr>
		</table>
	</div>
</main>

<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>