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
			<th>번호</th>
			<th>제목</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
	<c:forEach var="board" items="${boardList}" varStatus="loop">
		<tr>
			<td>${board.num}</td>
			<td><a href="<c:url value="/boardRead/${board.num}" />"> ${board.title}</a></td>
			<td>${board.date}</td>
			<td>${board.count}</td>
		</tr>
	</c:forEach>
	</table>
	<a href="<c:url value="/write" />">글쓰기</a>

</body>
</html>