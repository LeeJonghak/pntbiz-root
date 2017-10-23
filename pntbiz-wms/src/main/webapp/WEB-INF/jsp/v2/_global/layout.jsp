<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<!DOCTYPE html>
<html>
<head>
	<title>
		<decorator:title default="▒ IndoorPlus+ WMS ▒"/>
	</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<meta http-equiv='Cache-Control' ConTENT='no-cache'>
	<meta http-equiv='Pragma' ConTENT='no-cache'>
	<meta http-equiv="Expire" ConTENT="-1">
	<meta name="Subject" content="pntbiz.com"/>
	<meta name="verify-v1" content="c7qPZTfB1pUMtQBmsxdVRpF2Rn4M5+mypsJhQ1yQds4="/>
	<meta name="Author" content="pntbiz"/>
	<meta name="Publisher" content="pntbiz.com"/>
	<meta name="Other Agent" content="jhlee@pntbiz.com"/>
	<meta name="keywords" content="people&technology"/>
	<link rel="shortcut icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon"/>
	<link rel="icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon"/>
	<c:choose>
		<c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'en'}">
			<c:set var="locale" value="en" />
		</c:when>
		<c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'jp'}">
			<c:set var="locale" value="jp" />
		</c:when>
		<c:otherwise>
			<c:set var="locale" value="ko" />
		</c:otherwise>
	</c:choose>
	<script>
		var locale = "${locale}";
	</script>
	<!-- jQuery -->
	<link rel="stylesheet" type="text/css" href="${viewProperty.staticUrl}/css/common.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<link rel="stylesheet" type="text/css" href="${viewProperty.staticUrl}/css/content.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/jquery-1.12.2.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/jquery-ui.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/jquery.library.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/jquery.validate.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/localization/messages_${locale}.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/additional-methods.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/common.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/common-old.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/layout.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<decorator:head/>
</head>
<body>

<div id="wrap">
	<div id="header">
		<div class="inner">
			<a href="javascript:;" class="btnMenu"></a>

			<ul id="location">
			</ul>

			<div id="language">
				<a href="javascript:;">언어선택<em></em></a>
				<ul>
					<li><a href="javascript:;" locale="ko">한국어</a></li>
					<li><a href="javascript:;" locale="en">English</a></li>
					<li><a href="javascript:;" locale="jp">日本語</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div id="container">
		<div class="inner">
			<div id="lnb">
				<div class="top">
					<h1><img src="${viewProperty.staticUrl}/images/logo.png" alt="indoorplus"></h1>
					<a href="#" class="user" title="<sec:authentication property="principal.displayName" />">
						<sec:authentication property="principal.displayName" /> 님</a>
				</div>
				<page:applyDecorator page="${viewProperty.viewPathPrefix}/_global/sidemenu.jsp" name="sidemenu"/>
			</div>
			<div id="content">
				<decorator:body/>
			</div>
		</div>
	</div>
	<page:applyDecorator page="${viewProperty.viewPathPrefix}/_global/footer.jsp" name="footer"/>
	<div id="overlay"></div>
	<div id="loading"><div></div></div>
</div>

<%--<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/pnotify/pnotify.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>

</body>
</html>