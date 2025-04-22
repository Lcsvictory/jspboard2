<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="ad"
	style="display: inline-block; float: left; margin-left: 2%;">
	<!-- 광고 배너가 마우스 휠 따라가게 만들기 -->
	<a href="#"><img style="max-width: 100%"
		src="<%=application.getInitParameter("AD_LEFT")%>" width="100px"
		height="300px"></a>
</div>
<div class="ad"
	style="display: inline-block; float: right; margin-right: 2%;">
	<a href="#"><img
		src="<%=application.getInitParameter("AD_RIGHT")%>" width="100px"
		height="300px"></a>
</div>