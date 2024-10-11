<%@page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String uri = request.getRequestURI();
    String currentPage = uri.substring(uri.lastIndexOf("/") + 1);
%>
<header>
	<div class="logo">
		<a href="index.jsp"> <img src="img/logo.png" width="200px">
		</a>
	</div>
	<div class="search topsearch">
	<form method="get" action="./SearchResult.jsp">
		<select name="searchField">
			<option value="title">제목</option>
			<option value="content">내용</option>
			<option value="name">작성자</option>
		</select>
		<input class="searchWord" name="searchWord" type="text" placeholder="검색어 입력"> 
		<input type="submit" class="img-btn" value="">
	</form>
	</div>
	<div class="fun_info">
		<span>다양한 통계 표시</span> <!-- 새로고침 할 때마다 새로운 정보 가져오기 0428 22:47 나중에 구현하자..-->
	</div>
	<div class="menubar"> <!-- 상 하 마진 0 -->
		<nav class="navbar"> <!-- 상 하 마진 0 -->
			<ul class="navbar_list"> <!-- 상 하 마진 16 -->
				<li><a href="Left.jsp" style="<%= currentPage.equals("Left.jsp") ? "color:red;" : "" %>">진보</a></li>
				<li>&nbsp;&nbsp;&nbsp;<a href="Discussion.jsp" style="<%= currentPage.equals("Discussion.jsp") ? "color:red;" : "" %>">토론</a></li>
				<li>&nbsp;&nbsp;&nbsp;<a href="Right.jsp" style="<%= currentPage.equals("Right.jsp") ? "color:red;" : "" %>">보수</a></li>
				<li>&nbsp;&nbsp;&nbsp;<a href="Free.jsp" style="<%= currentPage.equals("Free.jsp") ? "color:red;" : "" %>">자유</a></li>
				<li>&nbsp;&nbsp;&nbsp;<a href="https://news.naver.com" target="blank"style="<%= currentPage.equals("News.jsp") ? "color:red;" : "" %>">뉴스</a></li>
				<li style="position: relative; left: 70%;">&nbsp;&nbsp;&nbsp;<a href="#" onclick="alert('미완성 기능입니다.');" >로그인</a></li>
				
			</ul>
		</nav>
	</div>
	
</header>

