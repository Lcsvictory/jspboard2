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
String searchField = request.getParameter("searchField");
String searchWord = request.getParameter("searchWord");
param.put("boardid", "0"); //전체검색 결과 페이지.
if (searchWord != null){
	param.put("searchField", searchField);
	param.put("searchWord", searchWord);
}

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
	<span style="font-size: 1.3em;"><b>통합 검색결과</b></span>
	<div class="container">
		<table>
			<thead>
				<tr>
					<th style="width: 5%; text-align: left;">번호</th>
					<th style="width: 5%; text-align: left;"></th>
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
		String type = "";
		String style ="";
		switch(Integer.parseInt(dto.getBoardid())) {
		case 1: type = "진보"; style="color: blue;"; break;
		case 2: type = "보수"; style="color: red;"; break;
		case 3: type = "토론"; style="color: brown;"; break;
		case 4: type = "자유"; style="color: sky;"; break;
		case 5: type = "뉴스"; break;
		case 999 : type= "공지"; style="text-decoration: underline;"; break;
		}
	
%>
				<tr>
					<td style="text-align: center;"><%= virtualNum %></td>
					<td style="text-align: center; <%=style %>"><b><%= type %></b></td>
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
				
			</tr>
		</table>
	</div>
</main>

<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>