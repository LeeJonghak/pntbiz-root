<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/tracking.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceTarget.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.table/bootstrap-table.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/tableExport.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/jquery.base64.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/html2canvas.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.table/bootstrap-table.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>
$(document).ready(function() {
	$.gmap.load({
		lat: '${companyInfo.lat}',
		lng: '${companyInfo.lng}',
		zoom: 19,
		width: "100%",
		height: "700px",
		div: "map-canvas",
		func: function(){presenceTarget.init()}
	});	
});
</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.170201" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.170000" /></li>
		<li class="crumb-trail"><spring:message code="menu.170200" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-12 col-md-12">
					<input type="hidden" id="sDateTimestamp" name="sDateTimestamp"/>
					<div class="input-group date" id="datetimepicker1">
						<label class="sr-only" for="sDate"><spring:message code="word.start.date" /></label>
						<input type="text" id="sDate" name="sDate" value="" class="form-control" placeholder="<spring:message code="word.start.date" />" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>

					<div class="input-group date" id="datetimepicker2">
						<input type="hidden" id="eDateTimestamp" name="eDateTimestamp" />
						<label class="sr-only" for="eDate"><spring:message code="word.end.date" /></label>
						<input type="text" id="eDate" name="eDate" value="" class="form-control" placeholder="<spring:message code="word.end.date" />" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<select name="viewType" id="viewType" class="form-control">
						<option value="heatmap"><spring:message code="word.heatmap" /></option>
						<option value="marker"><spring:message code="word.marker" /></option>
						<option value="movement"><spring:message code="word.movement" /></option>
					</select>
					<div class="input-group">						
						<input type="hidden" id="beaconNum" name="beaconNum" value="" />
						<input type="text" id="beaconName" name="beaconName" class="form-control" placeholder="<spring:message code="word.beacon.name" />" value="${param.beaconName}" />
					</div>
					<button id="presenceTargetRunBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.run" /></button>
						<button id="presenceTargetDataBtn" type="button" class="btn btn-default btn-sm" data-toggle="collapse" data-target="#presenceLog" aria-expanded="false" aria-controls="presenceLog"><spring:message code="btn.log.view" /> <span id="logBadge" class="badge">0</span></button>
						<button id="presenceLogDownloadBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.log.download" /></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">
			<div class="collapse" id="presenceLog">
				<table class="table table-striped table-hover" id="presenceLogTable" data-toggle="table" data-height="300">
					<thead>
					<tr class="active" width="97%">
						<th data-field="logNum">No.</th>
						<th data-field="uuid">UUID</th>	
						<th data-field="targetName">targetName</th>
						<th data-field="beaconName">beaconName</th>
						<th data-field="logDesc">fenceName</th>
						<th data-field="lat">lat</th>
						<th data-field="lng">lng</th>
						<th data-field="floor">floor</th>
						<th data-field="regDate">regDate</th>
					</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div id="map-canvas"></div>
		</div>
	
	</div>
</div>
</section>
</form>
</body>
</html>
