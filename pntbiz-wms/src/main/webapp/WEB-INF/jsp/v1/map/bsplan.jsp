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
    <meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; admin.user-scalable=no;" />
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <style type="text/css">
        #map-canvas {
            height: 300px;
        }
        @media (min-width: 768px) {
            #map-canvas {
                height:700px;
            }
        }
    </style>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/map/bsplan.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.120500" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.120000" /></li>
		<li class="crumb-trail"><spring:message code="menu.120500" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="animated fadeIn">	
	
<div id="map-canvas" style="width:100%;height:800px;"></div>

<div id="info-popup" style="position: absolute; top:100px; display:none;padding:10px; margin:10px; overflow-y:auto; overflow-x:hidden; width:260px; height:auto; border: 1px solid #cccccc; background-color:#ffffff;"></div>

<div class="hide">
    <div id="map-view-options" style="margin:10px;">
        <div class="btn-group-vertical" data-toggle="buttons">
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-beacon" autocomplete="off" checked> <spring:message code="word.show.beacon"/>
            </label>
        </div>

        <div class="input-group" style="margin-top: 10px;">
            <div id="search-form" class="input-group input-group-sm">
                <div id="search-option-group" class="input-group-btn">
                    <button type="button" id="search-menu-btn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><spring:message code="word.beacon"/> <span class="caret"></span></button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="javascript:;" id="search-option-beacon"><spring:message code="word.beacon"/></a></li>
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

        <div id="map-mode-buttons" style="margin:10px;">
            <spring:message code="word.floor" />
            <select id="floor-selector" class="input-sm" style="margin-top:4px;">
            </select>
        </div>
    </div>
    <div id="map-loading-bar" class="center" style="margin-top:10px;">
        Loading ...
    </div>
</div>

</section>
</body>
</html>