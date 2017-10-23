<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="▒ Pntbiz Beacon WMS ▒" /></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="imagetoolbar" content="no" />
<meta http-equiv='Cache-Control' ConTENT='no-cache'>
<meta http-equiv='Pragma' ConTENT='no-cache'>
<meta http-equiv="Expire" ConTENT="-1">
<meta name="Subject" content="pntbiz.com" />
<meta name="verify-v1" content="c7qPZTfB1pUMtQBmsxdVRpF2Rn4M5+mypsJhQ1yQds4=" />
<meta name="Author" content="pntbiz" />
<meta name="Publisher" content="pntbiz.com" />
<meta name="Other Agent" content="jhlee@pntbiz.com" />
<meta name="keywords" content="people technology" />
<link rel="shortcut icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon" />
<link rel="icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/_global/style.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap/css/bootstrap.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery/jquery.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap/js/bootstrap.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/jquery.validate.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/localization/messages_ko.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/additional-methods.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.cookie/jquery.cookie.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.mask/jquery.mask.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/common.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/login.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<decorator:head/>
</head>
<body>

<!-- wrapper 시작 -->
<div id="wrapper">
	<div id="wrap">	
		<!-- container 시작 -->
		<div id="container">
			<div class="content">
				<decorator:body/>
			</div>
		</div>
		<!-- container 끝 -->
	</div>
	<!-- wrap 끝 -->
</div>
<!-- wrapper 끝 -->
</body>
</html>