<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<style type="text/css">
	.btn-login{
		padding-left:45px;
		color:#fff;
		background-color:#e69656;
		background-image:url(../images/icon_btnLogin.png);
		border-bottom-color:#cb7d40;
	}
</style>
<sec:authorize var="authenticated" access="isAuthenticated()" />

<div>
<c:if test="${param.failure=='1'}">
<script type="text/javascript">
	alert(vm.loginFail);
	/*
	Reason: ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
	*/
</script>
</c:if>
</div>

<div class="loginWrap">
	<div class="loginWrap">
		<c:choose>
			<c:when test="${authenticated}">
				<script type="text/javascript">
					window.location.href = '<c:url value="/dashboard/main.do" />';
				</script>
			</c:when>
			<c:otherwise>
				<form name="loginForm" id="loginForm" action="<c:url value='/auth/login' />" method="post">
				<ul class="tabBar">
					<li class="first"><a href="javascript:;"><spring:message code="auth.login" /></a></li>
					<li class="second"><a href="javascript:;"><spring:message code="auth.otp" /></a></li>
				</ul>
				<div class="tabPage loginCon">
					<ul>
						<li>
							<span><spring:message code="auth.userid" /></span><input type="text" name="userid" value="${userID}">
						</li>
						<li>
							<span><spring:message code="auth.password" /></span><input type="password" name="userpw" value="">
						</li>
						<li><span></span>
							<input type="submit" value="등록" class="btn-inline btn-login" />
						</li>
						<li><span><spring:message code="auth.rememeber" /></span>
							<div class="idSwitch">
								<div class="switch-wrapper">
									<input type="checkbox" id="" value="1" class="useSwitch" checked>
								</div>
							</div>
						</li>
					</ul>
				</div>
				<div class="tabPage loginCon">
					<ul>
						<li>
							<span><spring:message code="auth.userid" /></span><input type="text" name="" value="">
						</li>
						<li>
							<span><spring:message code="auth.password" /></span><input type="password" name="" value="">
						</li>
						<li><span></span><a href="./main.html" class="btnLogin"><img src="${viewProperty.staticUrl}/images/icon_btnLogin.png" alt=""><spring:message code="auth.otp" /></a></li>
						<li><span></span><a href="javascript:;" class="btnCord"><spring:message code="auth.otp.authcode" /> <spring:message code="btn.send" /></a></li>
					</ul>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

