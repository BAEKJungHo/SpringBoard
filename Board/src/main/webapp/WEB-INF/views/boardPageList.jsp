<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MayEye BAEKJH Board</title>
</head>
<body>
	<span>${user.name}님 환영합니다.</span>
	<a href="<c:url value="/logout" />">로그아웃</a>
	<table border="1">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<c:forEach var="board" items="${list}" varStatus="loop">
		<tr>
			<td>${board.num}</td>
			<td><a href="<c:url value="/boardRead/${board.num}" />"> ${board.title}</a></td>
			<td>${board.name}</td>
			<td>${board.date}</td>
			<td>${board.count}</td>
		</tr>
		</c:forEach>
	</table>
	<table>
		<tr>
		    <c:if test="${pageMaker.prev}">
		    <td>
		        <a href='<c:url value="/boardPageList?page=${pageMaker.startPage-1}"/>'>&laquo;</a>
		    </td>
		    </c:if>
		    <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
		    <td>
		        <a href='<c:url value="/boardPageList?page=${idx}"/>'>${idx}</a>
		    </td>
		    </c:forEach>
		    <c:if test="${pageMaker.next && pageMaker.endPage >0}">
		    <td>
		        <a href='<c:url value="/boardPageList?page=${pageMaker.endPage+1}"/>'>&raquo;</a>
		    </td>
		    </c:if>
		</tr>
	</table>
	<a href="<c:url value="/boardWrite" />">글쓰기</a>
	<c:if test="${msg ne null}">
		<p style="color:#f00;">${msg}</p>
	</c:if>
</body>
</html>