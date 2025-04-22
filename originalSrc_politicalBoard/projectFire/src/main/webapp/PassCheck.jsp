<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function goBack(){
    window.history.back();
 }
</script>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="common/header.jsp" %> <!-- 상단 네비게이션바 -->
<%
String mode = request.getParameter("mode"); //수정할건지 삭제할건지.
String idx = request.getParameter("idx"); //게시글이면 게시글 번호
String comment_id = request.getParameter("comment_id"); //댓글이면 댓글번호
String backUrl = request.getParameter("backUrl");
%>

<div style="text-align: center; width: 50%; margin: 100px auto;" >
	<form method="post" action="PassServlet.do">
	<input type="hidden" name="mode" value="<%=mode %>">
	<input type="hidden" name="idx" value="<%=idx %>">
	<input type="hidden" name="comment_id" value="<%=comment_id %>">
	<input type="hidden" name="backUrl" value="<%=backUrl%>">
		<fieldset>
			<legend>비밀번호 확인</legend>
			<input type="password" name="pass">
			<button type="button" onclick="goBack();">돌아가기</button>
			<button type="submit" >확인</button>
		</fieldset>
	</form>
</div>

<%@ include file="common/footer.jsp" %> <!-- 하단 정보 -->
</body>
</html>