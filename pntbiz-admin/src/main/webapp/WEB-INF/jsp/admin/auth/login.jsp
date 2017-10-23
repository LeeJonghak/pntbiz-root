<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<sec:authorize var="authenticated" access="isAuthenticated()" />

<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/auth.css?v=<spring:eval expression='@config[buildVersion]'/>" />
</head>
<body>
<div class="bh50"></div>

<div id="login-form" class="box">
	<div class="login-box">
		<c:choose>
			<c:when test="${authenticated}">
				<form name="loginForm" id="loginForm">
					<div class="bh20"></div>
					<div class="center">
						<sec:authentication property="principal.displayName" />님 반갑습니다.
						<br /><br />
						<button id="logoutBtn" class="btn btn-lg btn-primary btn-block" type="submit">로그아웃</button>
					</div>
				</form>
			</c:when>
			<c:otherwise>
				<h2>Login <small>Beacon System</small></h2>
				<form name="loginForm" id="loginForm" action='<c:url value="/auth/login" />' method="post">
					<input type="text" name="userid" id="userid" value="${userID}" class="form-control" placeholder="UserID" autofocus  />
					<label for="userid"></label>
					<input type="password" name="userpw" id="userpw" value="" class="form-control" placeholder="Password" />
                    <label for="userpw"></label>
					<div class="clear"></div>
					<label class="checkbox">
						<input type="checkbox" name="remember-me" value="remember-me" id="rememeber-me">아이디저장
		       		</label>
					<button class="btn btn-lg btn-primary btn-block" type="submit" onclick="login.login('userid', 'userpw', 'saveID', REQUEST_URI);">로그인</button>
					<div>
						<c:if test="${param.failure=='1'}">
						<script type="text/javascript">
							alert('로그인에 실패하였습니다.\r\n아이디와 암호가 올바른지 확인해 주세요.');
							/*
							Reason: ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
							*/
						</script>
						</c:if>
					</div>
				</form>
			</c:otherwise>
		</c:choose>
	</div>
</div>

</body>
</html>