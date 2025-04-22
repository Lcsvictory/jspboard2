<%@page import="board.NaverNewsAPI"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="board.BoardDAO"%>
<%@page import="board.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
BoardDAO dao = new BoardDAO();
List<BoardDTO> boardLists;
Map<String, Object> param = new HashMap<String, Object>();

%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/style.css">
<title>정치 게시판</title>
</head>
<body style="overflow-x: hidden;">
<%@ include file="common/header.jsp" %> <!-- 상단 네비게이션바 -->
<main>
<%-- 서블릿 : <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %> 
JSP : <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %>  --%>

	<%@ include file="common/ad.jsp" %>
	<div class="container">
		<div class="headline left">
			<a href="https://news.naver.com" target="blank" style="text-decoration: none;"><b>뉴스</b></a>
			<ul>
				<li style="border: 0px;">
					<img class="headlineImg" src="img/메인뉴스이미지.jpg" width="420px"  alt="헤드라인뉴스이미지" style="margin-top: 10px;">
					
				</li>
			</ul>
		</div>
		
		<div class="sidenews right" style="position: relative; right: -3px; top: -10px;">
			<ul>
			<%
			String newses = NaverNewsAPI.APINews();
			String[] newss = newses.split("\n\n"); 
			for (String news : newss) {
				String title = news.substring(news.indexOf(" "), news.indexOf("\n"));
				String link = news.substring(news.indexOf("//")); //https://를 붙인채로 a링크에 할당하면 상대경로로 인식해버린다.
			%>
			
				<li><a href="<%=link%>" target="blank"><%=title %></a></li>
			<%} %>
				<!-- <li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li>
				<li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li>
				<li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li>
				<li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li>
				<li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li>
				<li><a><img src="" alt="뉴스썸네일">뉴스본문</a></li> -->
			</ul>
		</div>
	</div>

	<div class="container"> 
		<div class="todaybest left">
			<a href="#" style="text-decoration: none;"><b>추천순</b></a>
			<%
			param.put("orderby", "ddabong");
			boardLists = dao.selectListSort(param);
			%>
			<ul>
			<%
			for (BoardDTO dto : boardLists){ // 추천순이라서 순 추천수만 보여줌.
			%>
				<li><a href="View.jsp?idx=<%=dto.getIdx() %>"><%=dto.getTitle() %>[<%=dto.getComment_count() %>]</a><span style="float: right;"><%=dto.getUpcount() %></span></li>
			<%} %>
			
				<!-- <li><a href="#">두번째 게시물</a></li>
				<li><a href="#">세번째 게시물</a></li>
				<li><a href="#">네번째 게시물</a></li>
				<li><a href="#">다섯번째 게시물</a></li> -->
			
			</ul>
		</div>
		<div class="weekbest right">
			<!-- <a href="#"><img src="http://via.placeholder.com/260x45" alt="게시판제목"></a> -->
			<a href="#" style="text-decoration: none;"><b>댓글순</b></a>
			<%
			param.put("orderby", "comment");
			boardLists = dao.selectListSort(param);
			
			%>
			<ul>
			<%
			for (BoardDTO dto : boardLists){
			%>
				<li><a href="View.jsp?idx=<%=dto.getIdx() %>"><%=dto.getTitle() %>[<%=dto.getComment_count() %>]</a><span style="float: right;"><%=dto.getUpcount() %></span></li>
			<%} %>
				
				<!-- <li><a href="#">두번째 게시물</a></li>
				<li><a href="#">세번째 게시물</a></li>
				<li><a href="#">네번째 게시물</a></li>
				<li><a href="#">다섯번째 게시물</a></li> -->
			</ul>
		</div>
	</div>
	
	<div class="container"> 
		<div class="todaybest left">
			<a href="#" style="text-decoration: none;"><b>진보</b></a>
			<%
			param.put("start", "1");
			param.put("end", "5");
			param.put("boardid", "1");
			boardLists = dao.selectListPage(param);
			
			%>
			<ul>
				<%
			for (BoardDTO dto : boardLists){
			%>
				<li><a href="View.jsp?idx=<%=dto.getIdx() %>"><%=dto.getTitle() %>[<%=dto.getComment_count() %>]</a><span style="float: right;"><%=dto.getUpcount() %></span></li>
			<%} %>
			</ul>
		</div>
		<div class="weekbest right">
			<!-- <a href="#"><img src="http://via.placeholder.com/260x45" alt="게시판제목"></a> -->
			<a href="#" style="text-decoration: none;"><b>보수</b></a>
			<%
			param.put("start", "1");
			param.put("end", "5");
			param.put("boardid", "2");
			boardLists = dao.selectListPage(param);
			dao.close();
			%>
			<ul>
				<%
			for (BoardDTO dto : boardLists){
			%>
				<li><a href="View.jsp?idx=<%=dto.getIdx() %>"><%=dto.getTitle() %>[<%=dto.getComment_count() %>]</a><span style="float: right;"><%=dto.getUpcount() %></span></li>
			<%} %>
			</ul>
		</div>
	</div>
	
</main>

<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>