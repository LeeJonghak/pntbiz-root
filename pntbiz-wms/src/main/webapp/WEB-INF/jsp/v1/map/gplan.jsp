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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/gplan.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.120300" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.120000" /></li>
		<li class="crumb-trail"><spring:message code="menu.120300" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="animated fadeIn">
<%--<label class="col-sm-2 control-label">선택보기</label>
<div class="col-sm-10">
        <input type="checkbox" name="viewType" value="S" placeholder="시스템정의 펜스보기" checked /> 시스템정의 펜스보기
        <input type="checkbox" name="viewType" value="G" placeholder="일반 펜스보기"  checked /> 일반 펜스보기
</div>--%>
<%--<img src="/images/tmp/gplan.png" width="100%" />--%>
<div id="map-canvas" style="width:100%;height:800px;"></div>
</section>
</form>

<div class="hide">
    <div id="map-view-options" style="margin:10px;">
        <div class="btn-group-vertical" data-toggle="buttons">
            <label class="btn btn-success btn-sm active">
                <input type="checkbox" id="checkbox-visible-sf" autocomplete="off" checked> <spring:message code="word.show.geofencing.system" />
            </label>
            <label class="btn btn-success btn-sm active">
                <input type="checkbox" id="checkbox-visible-gf" autocomplete="off" checked> <spring:message code="word.show.geofencing" />
            </label>
        </div>
    </div>
    <div id="popup-info" style="display:none;padding:10px; margin:10px; overflow-y:auto; overflow-x:hidden; width:500px; height:auto; border: 1px solid #cccccc; background-color:#ffffff;"></div>
    <div id="map-mode-buttons" style="margin:10px;">
        <spring:message code="word.floor" />
        <select id="floor-selector" class="input-sm" style="margin-top:4px;">
        </select>
        <div  class="btn-group" data-toggle="buttons">
            <label class="btn btn-default btn-sm active">
                <input type="radio" name="options" id="btn-map-mode1" autocomplete="off" checked> <spring:message code="word.readonly" />
            </label>
            <label class="btn btn-default btn-sm ">
                <input type="radio" name="options" id="btn-map-mode2" autocomplete="off"> <spring:message code="word.geofencing.edit" />
            </label>

        </div>
    </div>
</div>
</body>
</html>