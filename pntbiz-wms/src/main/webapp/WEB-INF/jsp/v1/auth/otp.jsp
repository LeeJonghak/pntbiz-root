<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
<!-- <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/auth.css?v=<spring:eval expression='@config[buildVersion]'/>" />-->
<script type="text/javascript" src="${viewProperty.staticUrl}/js/auth.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<div class="admin-form theme-info mw500" id="login">
	        
	<!-- Login Panel/Form -->
	<div class="panel mt30 mb25">				
		<form name="form1" id="form1">
		<input type="hidden" name="key" id="key" value="" />
		<input type="hidden" name="failure" id="failure" value="${param.failure}" />
			<div class="panel-heading bg-light fs24 pb15">
				<span class="panel-icon"><i class="glyphicons glyphicons-lock"></i> &nbsp;OTP</span>
			</div>
			<div class="panel-body bg-light p25 pb15">	
				<!-- Username Input -->
				<div class="section">
					<label for="userID" class="field-label text-muted fs18 mb10"><spring:message code="auth.userid" /></label>
					<label for="userID" class="field prepend-icon">
						<input type="text" name="userID" id="userID" value="" class="gui-input" placeholder="<spring:message code="auth.userid" />" autofocus />
						<label for="userID" class="field-icon"><i class="fa fa-admin.user"></i></label>
					</label>
				</div>				
				<!-- Authcode Input -->
				<div class="section">
					<label for="otpNum" class="field-label text-muted fs18 mb10"><spring:message code="auth.otp.authcode" /></label>
					<label for="otpNum" class="field prepend-icon">
						<input type="password" name="otpNum" id="otpNum" maxlength="6" class="gui-input" placeholder="<spring:message code="auth.otp.authcode" />">
						<label for="otpNum" class="field-icon"><i class="fa fa-lock"></i></label>
					</label>
				</div>
				<div class="section">
					<div id="timer"></div>				
				</div>
			</div>
			<div class="panel-footer clearfix">
				<button type="button" id="otpActivateBtn" class="button btn-info mr10 pull-right"><spring:message code="auth.otp" /> <spring:message code="btn.authentication" /></button>
				<button type="button" id="otpNumBtn" class="button btn-primary mr10 pull-right"><spring:message code="auth.otp.authcode" /> <spring:message code="btn.send" /></button>			
			</div>
		</form>
	</div>
</div>				
				
</body>
</html>