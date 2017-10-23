<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<!DOCTYPE html>
<html>
<head>
<title>
    <c:choose>
        <c:when test="${pageContext.request.serverName eq 'wms.idatabank.com'}">
            <decorator:title default="Top in IT Solutions :: DATABANK SYSTEMS"/>
        </c:when>
        <c:when test="${pageContext.request.serverName eq 'onticplace.cwit.co.kr'}">
            <decorator:title default="CHOONGWAE INFORMATION TECHNOLOGY"/>
        </c:when>
        <c:otherwise>
            <decorator:title default="▒ IndoorPlus+ WMS ▒"/>
        </c:otherwise>
    </c:choose>        
</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta http-equiv="imagetoolbar" content="no"/>
<meta http-equiv='Cache-Control' ConTENT='no-cache'>
<meta http-equiv='Pragma' ConTENT='no-cache'>
<meta http-equiv="Expire" ConTENT="-1">
<meta name="Subject" content="pntbiz.com"/>
<meta name="verify-v1" content="c7qPZTfB1pUMtQBmsxdVRpF2Rn4M5+mypsJhQ1yQds4="/>
<meta name="Author" content="pntbiz"/>
<meta name="Publisher" content="pntbiz.com"/>
<meta name="Other Agent" content="jhlee@pntbiz.com"/>
<meta name="keywords" content="people&technology"/>
<c:choose>
    <c:when test="${pageContext.request.serverName eq 'wms.idatabank.com'}">
    </c:when>
    <c:when test="${pageContext.request.serverName eq 'onticplace.cwit.co.kr'}">
    	<link rel="shortcut icon" href="${viewProperty.staticUrl}/images/_global/favicon_cwit.ico" type="image/x-icon"/>
        <link rel="icon" href="${viewProperty.staticUrl}/images/_global/favicon_cwit.ico" type="image/x-icon"/>
    </c:when>
    <c:otherwise>
        <link rel="shortcut icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon"/>
        <link rel="icon" href="${viewProperty.staticUrl}/images/_global/favicon.ico" type="image/x-icon"/>
    </c:otherwise>
</c:choose>
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
<script>var locale = "${locale}"</script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/_global/style.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/_global/chart.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Font CSS (Via CDN) -->
<link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" />
<!-- FullCalendar Plugin CSS -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/fullcalendar/fullcalendar.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Theme CSS -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/skin/default_skin/css/theme.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Admin Forms CSS -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/admin-tools/admin-forms/css/admin-forms.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Glyphicons Pro CSS(font) -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/fonts/glyphicons-pro/glyphicons-pro.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Icomoon CSS(font) -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/fonts/icomoon/icomoon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Iconsweets CSS(font) -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/fonts/iconsweets/iconsweets.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Octicons CSS(font) -->
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/assets/fonts/octicons/octicons.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- Required Plugin CSS -->
<link rel="stylesheet" type="text/css" href="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/select2/css/core.css">
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/nestable/nestable.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
<!-- jQuery -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/jquery/jquery-1.11.1.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/jquery/jquery_ui/jquery-ui.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- Site Javascript -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/jquery.validate.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/localization/messages_${locale}.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.validator/dist/additional-methods.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.cookie/jquery.cookie.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.mask/jquery.mask.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/layout.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/common.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/login.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<decorator:head/>
</head>
<body class="external-page external-alt sb-l-c sb-r-c">

<div id="main">
<page:applyDecorator page="${viewProperty.viewPathPrefix}/_global/logintop.jsp" name="logintop"/>

<!-- Start: Content-Wrapper -->
<section id="content_wrapper">
<!-- Begin: Content -->
<section id="content">
<decorator:body/>
</section>
</section>

</div>

<!-- Page Javascript -->
<!-- CanvasBG Plugin(creates mousehover effect) -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/canvasbg/canvasbg.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- HighCharts Plugin -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/highcharts/highcharts.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- JvectorMap Plugin + US Map (more maps in plugin/assets folder) -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/jvectormap/jquery.jvectormap.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/jvectormap/assets/jquery-jvectormap-us-lcc-en.js?v=<spring:eval expression='@config[buildVersion]'/>"></script> 
<!-- Bootstrap Tabdrop Plugin -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/tabdrop/bootstrap-tabdrop.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- FullCalendar Plugin + moment Dependency -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/fullcalendar/lib/moment.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/vendor/plugins/fullcalendar/fullcalendar.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- Theme Javascript -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/assets/js/utility/utility.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/assets/js/demo/demo.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/assets/js/main.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<!-- Widget Javascript -->
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.absolute/assets/js/demo/widgets.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	"use strict";
	// Init Theme Core
	Core.init();
    // Init Demo JS
    //Demo.init();
});
</script>

</body>
</html>