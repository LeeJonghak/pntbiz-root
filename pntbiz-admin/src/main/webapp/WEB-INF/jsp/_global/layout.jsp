<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
             <c:when test="${pageContext.request.serverName eq 'wms.indoorplus.co.kr'}">
                <decorator:title default="▒ IndoorPlus+ WMS ▒"/>
            </c:when>
            <c:otherwise>
                <decorator:title default="▒ Pntbiz Beacon WMS ▒"/>
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
        <c:otherwise>
            <link rel="shortcut icon" href="<spring:eval expression='@config[staticURL]'/>/images/_global/favicon.ico" type="image/x-icon"/>
            <link rel="icon" href="<spring:eval expression='@config[staticURL]'/>/images/_global/favicon.ico" type="image/x-icon"/>
        </c:otherwise>
    </c:choose>
    <link rel="shortcut icon" href="<spring:eval expression='@config[staticURL]'/>/images/_global/favicon.ico" type="image/x-icon"/>
    <link rel="icon" href="<spring:eval expression='@config[staticURL]'/>/images/_global/favicon.ico" type="image/x-icon"/>
    <link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/_global/style.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.bootstrap/css/bootstrap.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery/jquery.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.bootstrap/js/bootstrap.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.validator/dist/jquery.validate.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.validator/dist/localization/messages_ko.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.validator/dist/additional-methods.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.jquery.cookie/jquery.cookie.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/_global/common.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/_global/input.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/_global/login.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <decorator:head/>
</head>
<body>

<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-labelledby="1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title"><span id="modal-title"></span></h4>
            </div>
            <div class="modal-body" id="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>

<!-- wrapper 시작 -->
<div id="wrapper">
    <div id="wrap">
        <!-- header 시작 -->
        <div id="header">
            <page:applyDecorator page="/WEB-INF/jsp/_global/top.jsp" name="top"/>
        </div>
        <!-- header 끝 -->

        <!-- container 시작 -->
        <div id="container">
            <div class="content">
                <decorator:body/>
            </div>
        </div>
        <!-- container 끝 -->
    </div>
    <!-- wrap 끝 -->
    <hr/>
    <!-- footer 시작 -->
    <div id="footer">
        <page:applyDecorator page="/WEB-INF/jsp/_global/footer.jsp" name="footer"/>
    </div>
    <!-- footer 끝 -->
</div>
<!-- wrapper 끝 -->
</body>
</html>