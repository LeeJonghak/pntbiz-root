<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<sec:authorize var="authenticated" access="isAuthenticated()" />
<c:if test="${authenticated}"><sec:authentication var="loginCompanyNumber" property="principal.companyNumber" /></c:if>

<!-- Start: Header -->
<header class="navbar navbar-fixed-top navbar-shadow">
	<!-- login-top : language change bind class  -->
	<ul class="nav navbar-nav navbar-right login-top">
		<li class="dropdown menu-merge">
			<div class="navbar-btn btn-group">
				<div id="locale"></div>
				<button id="languageBtn" data-toggle="dropdown" class="btn btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
					Language <span class="caret"></span>
				</button>
				<ul class="dropdown-menu pv5 animated animated-short flipInX" role="menu" aria-labelledby="languageBtn" aria-expanded="false">
					<li role="presentation"><a role="menuitem" tabindex="-1" locale="ko">한국어</a></li>
					<li role="presentation" class="divider"></li>
					<li role="presentation"><a role="menuitem" tabindex="-1" locale="en">English</a></li>
					<li role="presentation" class="divider"></li>
					<li role="presentation"><a role="menuitem" tabindex="-1" locale="jp">日本語</a></li>
				</ul>
			</div>
		</li>
		<c:choose>
		<c:when test="${authenticated}">		
		<li class="dropdown menu-merge">
			<a href="#" class="dropdown-toggle fw600 p15" data-toggle="dropdown">				
				<span class="hidden-xs pl15"> <sec:authentication property="principal.displayName" /> </span>
				<span class="caret caret-tp hidden-xs"></span>
			</a>
			<ul class="dropdown-menu list-group dropdown-persist w250" role="menu">
				<li class="dropdown-header clearfix">
					<spring:message code="word.basic.information" />
				</li>
				<li class="list-group-item">
					<a href="#" class="animated animated-short fadeInUp">
					<span class="fa fa-admin.user"></span> <spring:message code="menu.180700" />
					</a>
				</li>
				<li class="dropdown-footer" id="logoutTopBtn">
					<a href="#" class="">
					<span class="fa fa-power-off pr5"></span> <spring:message code="btn.logout" /> </a>
				</li>
			</ul>
		</li>
		</c:when>
		<c:otherwise>
		<li class="dropdown menu-merge">
			<div class="navbar-btn">
				<button id="loginTopBtn" type="button" class="btn btn-sm btn-primary"><spring:message code="btn.login" /></button>
			</div>
		</li>
		</c:otherwise>
		</c:choose>
	</ul>
</header>
<!-- End: Header -->
