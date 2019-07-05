<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style>
		input[type=text] { /* text 창에만 적용 */
			color : red; 
		}
		input:hover, textarea:hover { /* 마우스 올라 올 때 */
			background : aliceblue;
		}
		input[type=text]:focus, input[type=password]:focus { /* 포커스 받을 때 */
			font-size : 120%; 
		}
		label {
			display : block; /* 새 라인에서 시작 */
			padding : 5px;
		}
		label span {
			display : inline-block;
			width : 90px;
			text-align : right;
			padding : 10px;
		}
	</style>
<title>MayEye BAEKJH Board</title>
</head>
<body>
	<center><br>
	<h3>Users Login</h3><br>
	<hr>
	<form:form commandName="usersDTO" mehtod="post">
		<form:label path="id"><span>ID:</span>
			<form:input path="id" />
			<form:errors path="id" />
		</form:label>
		<form:label path="pwd"><span>Password:</span>
			<form:input path="pwd" />
			<form:errors path="pwd" />
			</form:label><br>
		<input type="submit" value="로그인">&nbsp;&nbsp;
		<input type="reset" value="취소">
	</form:form>
	
	<c:if test="${msg ne null}">
		<p style="color:#f00;">${msg}</p>
	</c:if>
</body>
</html>