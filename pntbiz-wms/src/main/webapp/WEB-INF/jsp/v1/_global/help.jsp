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
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/_global/style.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap/css/bootstrap.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery/jquery.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap/js/bootstrap.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/layout.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/help.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script>
</script>
</head>
<body>
<a id="helpTopAnchor" name="helpTopAnchor"></a>
<decorator:body/>
<hr />
<div class="pull-right">	
	<a href="#helpTopAnchor"><span class="glyphicon glyphicon-arrow-up"></span></a>
</div>
<div class="clearfix"></div>
</body>
</html>