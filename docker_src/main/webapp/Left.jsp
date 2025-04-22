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
BoardDAO dao = new BoardDAO();

//사용자가 입력한 검색조건 저장
Map<String, Object> param = new HashMap<String, Object>();
//String searchField = request.getParameter("searchField"); 게시판 내 검색기능은 없다.톻합검색만존재.
//String searchWord = request.getParameter("searchWord");
param.put("boardid", "1"); //진보 게시판은 1번게시판.
//if (searchWord != null){
//	param.put("searchField", searchField);
//	param.put("searchWord", searchWord);
//}

int totalCount = dao.selectCount(param); //게시물 수 확인

//전체 페이지 수 계산
int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
int totalPage = (int)Math.ceil((double)totalCount / pageSize); //전체 페이지 수 (왜 double로 바꿈??)

//현재 페이지 확인
int pageNum = 1; //일단 현재페이지를 1로 하자.
String pageTemp = request.getParameter("pageNum");
if (pageTemp != null && !pageTemp.equals("")){ //페이지값이 있다면 그걸로 바꾸자.
	pageNum = Integer.parseInt(pageTemp);
}

//목록에 출력할 게시물 범위 계산
int start = (pageNum - 1) * pageSize + 1;
int end = pageNum * pageSize;
param.put("start", start);
param.put("end", end);

List<BoardDTO> boardLists = dao.selectListPage(param); //게시물 조회

dao.close();

String backUriTemp = request.getRequestURI(); /* 글쓰기 페이지로 넘어가기 전에 요청한 위치를 저장해서 같이 보내줌. */
String backUrl = backUriTemp.substring(backUriTemp.lastIndexOf("/") + 1);
backUrl = URLEncoder.encode(backUrl, "UTF-8"); //한글 인코딩문제 해결
%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/style.css">
<title>정치 게시판</title>
</head>
<body style="overflow-x: hidden;">
<%@ include file="common/header.jsp" %> <!-- 상단 네비게이션바 -->

<main>
	<%@ include file="common/ad.jsp" %>
	<div class="container">
		<table>
			<thead>
				<tr>
					<th style="width: 5%;">번호</th>
					<th style="width: 50%;">제목</th>
					<th style="width: 15%;">글쓴이</th>
					<th style="width: 10%;">조회수</th>
					<th style="width: 10%;">작성일</th>
					<th style="width: 15%;">추천수</th>
				</tr>
			</thead>
			<tbody>
<%
if (boardLists.isEmpty()){
%>
				<tr>
					<td colspan="6" align="center">등록된 게시물이 없습니다^^*</td>
				</tr>
<%
} else{
	int virtualNum = 0;
	int countNum = 0;
	for (BoardDTO dto : boardLists){
		//virtualNum = totalCount--;
		virtualNum = totalCount - (((pageNum - 1) * pageSize) + countNum++);
		int temp = Integer.parseInt(dto.getUpcount());
		int temp2 = Integer.parseInt(dto.getDowncount());
		String vote = temp - temp2 + ""; //추천수 계산 비추가 많으면 -추천수;
		/* String vote = String.valueOf(temp - temp2); 간단한 방법임. */
	
%>
				<tr>
					<td style="text-align: center;"><%= virtualNum %></td>
					<td><a href="View.jsp?idx=<%= dto.getIdx() %>"><%=dto.getTitle() %>[<%=dto.getComment_count() %>]</a></td>
					<td style="text-align: center;"><%= dto.getName() %></td>
					<td style="text-align: center;"><%= dto.getVisitcount()%></td>
					<td style="text-align: center;"><%= dto.getPostdate() %></td>
					<td style="text-align: center;"><%= vote %></td>
				</tr>
<%
	}
}
%>
			</tbody>
		</table>
		<table>
			<tr>
				<td style="text-align: center;">
					<%= BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, request.getRequestURI()) %>
				</td>
				<td style="text-align: right;">
					<button type="button" onclick="location.href='Write.jsp?backUrl=<%= backUrl%>';">글쓰기</button>
				</td>
			</tr>
		</table>
	</div>
</main>

<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>