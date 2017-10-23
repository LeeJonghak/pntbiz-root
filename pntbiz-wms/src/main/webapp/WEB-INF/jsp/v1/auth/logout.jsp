<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<!-- <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/auth.css?v=<spring:eval expression='@config[buildVersion]'/>" />-->
<meta http-equiv='refresh' content='3;url=/'>
</head>
<body>	
<div id="login-form">
	<div class="login-box">
		<div class="login-box-top"></div>
		<div class="login-box-container">
			<img src="/images/auth/text_login.gif" alt="LOGIN" class="login-img" />
			<div class="bh10"></div>
			<p><span class="login-title">Beacon System</span></p>
			<div class="bh30"></div>			
			<div class="login-box-content">
				<form name="loginForm" id="loginForm">
				<div class="bh10"></div>
				<div class="center">
					<strong>로그아웃 되었습니다.<br /><br />
					3초 뒤에 자동으로 메인으로 이동합니다.
					</strong>
				</div>
				</form>
			</div>
		</div>
		<div class="login-box-bottom"></div>
	</div>
</div>

</body>
</html>