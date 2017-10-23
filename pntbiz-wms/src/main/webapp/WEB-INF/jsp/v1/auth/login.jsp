<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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
					 
<div class="admin-form theme-info mw500" id="login">
	        
	<!-- Login Panel/Form -->
	<div class="panel mt30 mb25">
		<c:choose>
		<c:when test="${authenticated}">
		<form name="loginForm" id="loginForm">
			<div class="panel-heading bg-light fs24 pb15">
				<span class="panel-icon"><i class="glyphicon glyphicon-log-out"></i> &nbsp;Log out</span>
			</div>
			<div class="panel-body bg-light p25 pb15">	
				<sec:authentication property="principal.displayName" var="displayName" />
				<div class="section">
					<label for="userid" class="field-label text-muted fs18 mb10">Welcome</label>					
				</div>		
				<spring:message code="auth.welcome" arguments="${displayName}" />		
			</div>
			<div class="panel-footer clearfix">
				<a href="/dashboard/main.do"><button type="button" class="button btn-primary mr10 pull-right" ><spring:message code="btn.home" /></button></a>
				<button type="submit" id="logoutBtn" class="button btn-primary mr10 pull-right" ><spring:message code="auth.logout" /></button>
			</div>
		</form>
		</c:when>
		<c:otherwise>			
		<form name="loginForm" id="loginForm" action='<c:url value="/auth/login" />' method="post">
			<div class="panel-heading bg-light fs24 pb15">
				<span class="panel-icon"><i class="glyphicon glyphicon-log-in"></i> &nbsp;Log in</span>
			</div>
			<div class="panel-body bg-light p25 pb15">	
				<!-- Username Input -->
				<div class="section">
					<label for="userid" class="field-label text-muted fs18 mb10"><spring:message code="auth.userid" /></label>
					<label for="userid" class="field prepend-icon">
						<input type="text" name="userid" id="userid" value="${userID}" class="gui-input" placeholder="<spring:message code="auth.userid" />" autofocus />
						<label for="userid" class="field-icon"><i class="fa fa-admin.user"></i></label>
					</label>
				</div>				
				<!-- Password Input -->
				<div class="section">
					<label for="userpw" class="field-label text-muted fs18 mb10"><spring:message code="auth.password" /></label>
					<label for="userpw" class="field prepend-icon">
						<input type="password" name="userpw" id="userpw" class="gui-input" placeholder="<spring:message code="auth.password" />">
						<label for="userpw" class="field-icon"><i class="fa fa-lock"></i></label>
					</label>
				</div>
			</div>
			<div class="panel-footer clearfix">
				<button type="submit" id="loginBtn" class="button btn-primary mr10 pull-right"><spring:message code="auth.login" /></button>
				<button type="button" id="loginOtpBtn" class="button btn-info mr10 pull-right"><spring:message code="auth.otp" /></button>
				<label class="switch ib switch-primary mt10">
					<input type="checkbox" name="remember" value="remember" id="remember" checked />
					<label for="remember" data-on="YES" data-off="NO"></label>
					<span><spring:message code="auth.rememeber" /></span>
				</label>
			</div>
		</form>
		</c:otherwise>
		</c:choose>	
	</div>

</div>
