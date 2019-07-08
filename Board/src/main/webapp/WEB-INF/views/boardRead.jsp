<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MayEye BAEKJH Board</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>제목</th>
			<td>${boardDTO.title}</td>
		</tr>
		<tr>
			<th>내용</th>
			<td>${boardDTO.contents}</td>
		</tr>
		<tr>
			<th>작성일</th>
			<td>${boardDTO.date}</td>
		</tr>	
		<tr>
			<th>조회수</th>
			<td>${boardDTO.count}</td>
		</tr>	
	</table>
	<div>
		<a href="<c:url value="/boardEdit/${boardDTO.num}" />">수정</a>
		<a href="<c:url value="/boardDelete/${boardDTO.num}" />">삭제</a>
		<a href="<c:url value="/boardSearchList" />">목록</a>
	</div>
</body>
</html>