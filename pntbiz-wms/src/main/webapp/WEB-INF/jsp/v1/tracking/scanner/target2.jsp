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
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/tableExport.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/jquery.base64.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.tableExport/html2canvas.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<link rel="stylesheet" href="${viewProperty.staticUrl}/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/pnt.map.tracking.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">

		window.prop = new pnt.util.Properties();
		prop.put('icon.beacon', '${viewProperty.staticUrl}/images/inc/marker_green_10.png');
		prop.put('icon.scanner', '${viewProperty.staticUrl}/images/inc/marker_scanner_10.png');
		prop.put('icon.nodeBeacon', '${viewProperty.staticUrl}/images/inc/marker_blue_10.png');
		prop.put('icon.nodeScanner', '${viewProperty.staticUrl}/images/inc/marker_sky_10.png');
		prop.put('url.floor', '<c:url value="/maptool/data/floor.do"/>');
		prop.put('url.area', '<c:url value="/maptool/data/area.do"/>');
		prop.put('url.tracking.log', '<c:url value="/maptool/data/presenceLog.do"/>');
		prop.put('url.beacon', '<c:url value="/tracking/presence/scanner/target.beacon.list.do"/>');
		prop.put('comNum', '${companyInfo.comNum}');
		prop.put('uuid', '${companyInfo.UUID}');
		prop.put('export.data.column',['logNum', 'UUID', 'targetName', 'beaconName', 'lat', 'lng', 'floor', 'regDate']);

		pnt.util.onReady(function() {
			var trackingMap = pnt.module.run('tracking.map', {prop:prop});

			$('#datetimepicker1')
				.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true})
				.on('dp.change', function(event) {
					$('#sDateTimestamp').val(event.date.unix());
				});
			$('#datetimepicker2')
				.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true})
				.on('dp.change', function(event) {
					$('#eDateTimestamp').val(event.date.unix());
				});

			$('#beaconName').typeahead({
				minLength: 2,
				items: 100,
				source: function(query, process) {
					trackingMap.beaconList(query, process);
				},
				updater: function(item) {
					$('#beaconNum').val(trackingMap.beaconMap[item]);
					//$('#presenceTargetRunBtn').removeClass().addClass("btn btn-primary btn-sm");
					//presenceTarget.flag = true;
					return item;
				}
			});

			$('#presenceTargetRunBtn').on('click', function(evt) {

				var viewType = $('#viewType').val();
				if(viewType=='heatmap') {
					trackingMap.tracking.setDisplayMode('heatmap');
				} else if(viewType=='marker') {
					trackingMap.tracking.setDisplayMode('dot');
				} else if(viewType=='movement') {
					trackingMap.tracking.setDisplayMode('line');
				}

				var sTimestamp = $('#sDateTimestamp').val();
				var eTimestamp = $('#eDateTimestamp').val();
				var beaconNum = $('#beaconNum').val();

				trackingMap.playTrackingTimestamp(beaconNum, null, sTimestamp, eTimestamp);
				$('#presenceLogTable>tbody').html('');
				$('#logBadge').html('0');
			});

			$('#presenceLogDownloadBtn').bind('click', function() {
				trackingMap.exportCsv();
			});

			$(window).resize(function() {
				var windowHeight = $(window).height();
				var offsetTop = $('#map-canvas').offset().top;
				$('#map-canvas').height(windowHeight-offsetTop-46);
			});
			$(window).trigger('resize');
			trackingMap.pntmap.getOlMap().updateSize();
		});
	</script>
</head>
<body>
	<form name="form1" id="form1" class="form-inline" role="form">

		<header id="topbar" class="alt">
			<div class="topbar-left">
				<ol class="breadcrumb">
					<li class="crumb-active"><a href="###"><spring:message code="menu.170100" /></a></li>
					<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
					<li class="crumb-trail"><spring:message code="menu.170000" /></li>
					<li class="crumb-trail"><spring:message code="menu.170100" /></li>
				</ol>
			</div>
			<div class="topbar-right">
				<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
			</div>
		</header>

		<section id="content" class="table-layout animated fadeIn">
			<div class="row col-md-12">
				<div class="panel">
					<div class="panel-body pn">
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
							</div>

						</div>


						<div id="map-canvas" class="map" style="height:800px;"></div>
					</div>
				</div>
			</div>
		</section>
	</form>
</body>
</html>
