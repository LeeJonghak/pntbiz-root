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
    <!--  
    <script type="text/javascript" src="https://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    -->
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/map/bplan.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.120200" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.120000" /></li>
		<li class="crumb-trail"><spring:message code="menu.120200" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="animated fadeIn">
<!--  
<div class="col-xs-12 row">
    <c:if test="${not empty param.startMinor}">
        <div class="form-inline">
            <div class="form-group">
                <input type="number" id="startMinor" name="startMinor" class="form-control" placeholder="시작 MinorVersion" value="${param.startMinor}" />
            </div>
            <div class="form-group">
                <input type="number" id="endMinor" name="endMinor" class="form-control" placeholder="마지막 MinorVersion" value="${param.endMinor}" />
            </div>
            <div class="form-group">
                <input type="button" class="form-control" value="조회" class="form-control btn btn-default" onclick="location.href = 'bplan.do?startMinor='+$('#startMinor').val()+'&endMinor='+$('#endMinor').val();" />
            </div>
        </div>
    </c:if>
</div>
-->
<div id="map-canvas" style="width:100%;height:700px;"></div>
<div class="hide">
    <div id="popup-beacon-info" style="display:none;padding:10px; margin:10px; overflow-y:auto; overflow-x:hidden; width:500px; height:auto; border: 1px solid #cccccc; background-color:#ffffff;"></div>
    <div id="map-view-options" style="margin:10px;">

        <div class="btn-group-vertical" data-toggle="buttons">
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-beacon" autocomplete="off" checked> <spring:message code="word.show.beacon"/>
            </label>
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-node" autocomplete="off" checked> <spring:message code="word.show.node"/>
            </label>
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-pair" autocomplete="off" checked> <spring:message code="word.show.pair"/>
            </label>
            <label class="btn btn-success btn-sm">
                <input type="checkbox" id="checkbox-map-visible-poi" autocomplete="off"> <spring:message code="word.show.poi"/>
            </label>
            <label class="btn btn-success btn-sm">
                <input type="checkbox" id="checkbox-map-visible-jointname" autocomplete="off"> <spring:message code="word.show.intersection"/>
            </label>
            <label class="btn btn-success btn-sm">
                <input type="checkbox" id="checkbox-map-visible-chaosarea" autocomplete="off"> <spring:message code="word.show.chaosarea"/>
            </label>
        </div>

        <div class="input-group" style="margin-top: 10px;">
            <div id="search-form" class="input-group input-group-sm">
                <div id="search-option-group" class="input-group-btn">
                    <button type="button" id="search-menu-btn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><spring:message code="word.node"/> <span class="caret"></span></button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="javascript:;" id="search-option-beacon"><spring:message code="word.beacon"/></a></li>
                        <li><a href="javascript:;" id="search-option-node"><spring:message code="word.node"/></a></li>
                        <li class="divider"></li>
                        <li><a href="javascript:;" id="search-hide-btn"><spring:message code="word.hide"/></a></li>
                    </ul>
                </div>
                <input type="text" id="search-keyword" class="form-control input-sm" style="width:100px;" />
                <span class="input-group-btn">
                    <button id="search-btn" class="btn btn-default btn-sm">
                        <spring:message code="btn.search" />
                    </button>
                </span>
            </div>
        </div>
    </div>
    <div id="map-mode-buttons" style="margin:10px;">
        <spring:message code="word.floor" />
        <select id="floor-selector" class="input-sm" style="margin-top:4px;">
        </select>
        <div  class="btn-group" data-toggle="buttons">
            <label class="btn btn-default btn-sm active">
                <input type="radio" name="options" id="btn-map-mode1" autocomplete="off" checked> <spring:message code="word.readonly"/>
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode3" autocomplete="off"> <spring:message code="word.move.beacon.node"/>
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode2" autocomplete="off"> <spring:message code="word.create.node"/>
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode6" autocomplete="off"> <spring:message code="word.create.chaosarea"/>
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode4" autocomplete="off"> <spring:message code="word.connect.node"/>
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode5" autocomplete="off"> <spring:message code="word.disconnect.node"/>
            </label>
        </div>

        <input type="button" name="options" id="btn-map-undo" disabled class="btn btn-default btn-sm" autocomplete="off" value="<spring:message code="word.undo"/>" />
        
        <div id="map-loading-bar" class="center" style="margin-top:10px;">
            
        </div>
    </div>

</div>

</section>
</body>
</html>