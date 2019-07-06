<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MayEye BAEKJH Board</title>
</head>
<body>
<script type="text/javascript">
	function page(index) {
		var pageNum = index;
		var contentNum = $("#contentNum option:selected").val();
		location.href="/boardPageList?pageNum="+pageNum+"&contentNum=10"+contentNum";
	}
</script>
	<select name="contentNum" id="contentNum">
		<option value="10">10</option>
		<option value="20">20</option>
		<option value="30">30</option>
	</select>
	<span>${user.name}님 환영합니다.</span>
	<a href="<c:url value="/logout" />">로그아웃</a>
	<table border="1">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="board" items="${boardPageList}" varStatus="loop">
			<tr>
				<td>${board.num}</td>
				<td><a href="<c:url value="/boardRead/${board.num}" />"> ${board.title}</a></td>
				<td>${board.name}</td>
				<td>${board.date}</td>
				<td>${board.count}</td>
			</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="2">
					<c:if test="${page.prev}">
						<a style="text-decoration: none;" href="javascript:page(${page.getStartPage()-1});">&laquo;</a>
					</c:if>
					<!-- 페이지 번호 표시 -->
					<c:forEach begin="${page.getStartPage()}" end="${page.getEndPage()}" var=index">
						<a style="text-decoration: none;" href="javascript:page(${index});">${index}</a>
					</c:forEach>
					<c:if test="${page.next}">
						<a style="text-decoration: none;" href="javascript:page(${page.getEndPage()-1});">&raquo;</a>
					</c:if>
		</tfoot>
	</table>
	<a href="<c:url value="/boardWrite" />">글쓰기</a>
	<c:if test="${msg ne null}">
		<p style="color:#f00;">${msg}</p>
	</c:if>
</body>
</html>