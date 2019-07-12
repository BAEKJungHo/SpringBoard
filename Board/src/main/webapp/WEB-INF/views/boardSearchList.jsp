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
	<div>
		<form name="searchForm" action="<c:url value="/boardSearchList" />" method="GET" >
			<select name="searchType">
				<option value="all" <c:out value="${searchType =='all'? 'selected':'' }"/>>제목+이름+내용</option>
				<option value="writer" <c:out value="${searchType =='writer'? 'selected':'' }"/>>이름</option>
				<option value="content" <c:out value="${searchType =='content'? 'selected':'' }"/>>내용</option>
				<option value="title" <c:out value="${searchType =='title'? 'selected':'' }"/>>제목</option>
			</select>
			<input type="text" name="keyword" value="${keyword}" placeholder="검색">
			<input id="submit" type="submit" value="검색">
		</form>
	</div>
	<br>
	<b>${count}</b>개의 게시물이 있습니다.
	<br><br>
	<table border="1">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<c:forEach var="board" items="${searchList}" varStatus="loop">
		<tr>
			<td>${loop.count}</td>
			<td><a href="<c:url value="/boardRead/${board.num}${pageMaker.makeSearch(pageMaker.cri.page)}" />"> ${board.title}</a></td>
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
		        <a href='<c:url value="/boardSearchList?${pageMaker.makeSearch(pageMaker.startPage-1)}"/>'>&laquo;</a>
		    </td>
		    </c:if>
		    <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
		    <td>
		        <a href='<c:url value="/boardSearchList${pageMaker.makeSearch(idx)}"/>'>${idx}</a>
		    </td>
		    </c:forEach>
		    <c:if test="${pageMaker.next && pageMaker.endPage >0}">
		    <td>
		        <a href='<c:url value="/boardSearchList${pageMaker.makeSearch(pageMaker.endPage+1)}"/>'>&raquo;</a>
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