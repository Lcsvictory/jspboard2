<%@page import="utils.BoardPage"%>
<%@page import="board.BoardDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/style.css">
<title>정치 게시판</title>
<script type="text/javascript">
   function goBack(){
      window.history.back();
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
   function retry() {
	   if(confirm("정말 다시 작성하시겠습니까?")){
			alert("정상적으로 리셋되었습니다.");
			document.getElementById('form').reset();
		}else{
			alert("작성된 내용이 보존됩니다.");
		}
   }
</script>
</head>
<body style="overflow-x: hidden;">
<%@ include file="common/header.jsp" %> <!-- 상단 네비게이션바 -->
<%
BoardDTO dto = (BoardDTO)request.getAttribute("dto");
%>
<main>
	<!-- 수정 페이지에선 광고 안넣음. -->
	<div class="container">
	<form id="form" method="post" action="EditServlet.do" enctype="multipart/form-data" onsubmit="return validateForm(this);">
	<input type="hidden" name="idx" value="<%= dto.getIdx()%>"> <!-- 현재페이지 -->
	<input type="hidden" name="predOfile" value="<%= dto.getOfile()%>"><!-- 원래 첨부파일 -->
	<input type="hidden" name="prevSfile" value="<%= dto.getSfile()%>"><!-- 원래 첨부파일 -->
	
	
		<table>
			<tr>
				<td>
				게시판 선택 
					<select name="boardid">
						<option value="1">진보</option>
						<option value="3">토론</option>
						<option value="2">보수</option>
						<option value="4">자유</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					작성자 <input type="text" name="name" size=10 value="<%=dto.getName() %>">
				</td>
				<td>
					비밀번호 <input type="password" name="pass" size=10>
				</td>
			</tr>
			<tr >
				<td colspan="2">
					제목 <input type="text" name="title" size=50 value="<%=dto.getTitle() %>">
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<textarea rows="20" cols="70" name="content"><%=dto.getContent() %></textarea>
				</td>
			</tr>
			<tr>
				<td>
					파일 첨부 <input type="file" name="ofile" multiple> (최대 10MB)
				</td>
			</tr>
		</table>
			<table>
				<tr>
					<td style="text-align: right;">
						<button type="button" onclick="retry();">다시작성</button>	
						<!-- <button type="button" onclick="goBack();">목록보기</button>	 -->
						<button type="submit">작성완료</button>					
					</td>
				</tr>
			</table>
		</form>
	</div>
</main>


<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>