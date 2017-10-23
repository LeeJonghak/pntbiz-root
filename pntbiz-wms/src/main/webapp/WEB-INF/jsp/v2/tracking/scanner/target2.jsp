<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/external/js.jquery.tableExport/tableExport.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/external/js.jquery.tableExport/jquery.base64.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/external/js.jquery.tableExport/html2canvas.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/tracking/pnt.map.tracking.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '08', submenu: '02',
			location: ['<spring:message code="menu.170000" />', '<spring:message code="menu.170201" />']
		};

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
			window.trackingMap = pnt.module.run('tracking.map', {prop:prop});

			/*$('#beaconName').typeahead({
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
			});*/

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
				$('#map-canvas').height(windowHeight-offsetTop-80);
			});
			$(window).trigger('resize');
			trackingMap.pntmap.getOlMap().updateSize();
		});
	</script>
</head>
<body>






<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.170201" /></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<form name="form1" id="form1" class="form-inline" role="form">
				<input type="hidden" id="sDateTimestamp" name="sDateTimestamp" data-target-prefix="start" class="pnt-datetime-timestamp" />
				<input type="text" id="start-date" class="useDatepicker" value="">
				<select id="start-hour">
				<c:forEach var="hour" varStatus="i" begin="0" end="23" step="1">
					<option value="${hour}">${hour}<spring:message code="word.hour" /></option>
				</c:forEach>
				</select>
				<select id="start-minute">
					<c:forEach var="minute" varStatus="i" begin="0" end="59" step="1">
						<option value="${minute}">${minute}<spring:message code="word.minute" /></option>
					</c:forEach>
				</select>

				~

				<input type="hidden" id="eDateTimestamp" name="eDateTimestamp" data-target-prefix="end" class="pnt-datetime-timestamp" />
				<input type="text" id="end-date" class="useDatepicker">
				<select id="end-hour">
					<c:forEach var="hour" varStatus="i" begin="0" end="23" step="1">
						<option value="${hour}">${hour}<spring:message code="word.hour" /></option>
					</c:forEach>
				</select>
				<select id="end-minute">
					<c:forEach var="minute" varStatus="i" begin="0" end="59" step="1">
						<option value="${minute}">${minute}<spring:message code="word.minute" /></option>
					</c:forEach>
				</select>

				<select id="viewType" name="viewType" class="w150">
					<option value="heatmap"><spring:message code="word.heatmap" /></option>
					<option value="marker"><spring:message code="word.marker" /></option>
					<option value="movement"><spring:message code="word.movement" /></option>
				</select>
				<%--<input type="text" placeholder="<spring:message code="word.enter.search.term" />" name="beaconName" class="w300">--%>
				<input type="button" id="presenceTargetRunBtn" value="<spring:message code="btn.search" />" class="btn-inline btn-search">
				<a href="target2.do" class="btn-inline btn-refresh"><spring:message code="word.reset" /></a>
			</form>
		</div>
		<div class="tableTopBtn aright">
			<%-- 로그보기 --%>
			<input type="button" value="<spring:message code="btn.log.view" />" onclick="$('#pop-log-list').css('visibility','visible');" class="btn-inline" />
			<%-- 로그 다운로드 --%>
			<input type="button" value="<spring:message code="btn.log.download" />" id="presenceLogDownloadBtn" class="btn-inline" />
		</div>

		<div id="pop-log-list" class="modal" style="visibility: hidden;">
			<div class="inHeader">
				<%--position: absolute;top: 0;right: 10px;font-size: 20px;color: #fff;--%>
				<a href="javascript:;" class="" title="<spring:message code="word.close" />" style="position: absolute;top: 0;right: 10px;font-size: 20px;color: #fff;" onclick="$('#pop-log-list').css('visibility','hidden');">×</a>
			</div>
			<div class="inContent" style="height:400px;width:1000px;overflow-y:auto;">

				<div class="list" id="presenceLog" style="margin:0px;">
					<table id="presenceLogTable">
						<thead style="font-size:9pt;">
						<tr>
							<th scope="col" data-field="logNum">No.</th>
							<th scope="col" data-field="uuid">UUID</th>
							<th scope="col" data-field="targetName">targetName</th>
							<th scope="col" data-field="beaconName">beaconName</th>
							<th scope="col" data-field="lat">lat</th>
							<th scope="col" data-field="lng">lng</th>
							<th scope="col" data-field="floor">floor</th>
							<th scope="col" data-field="regDate">regDate</th>
						</tr>
						</thead>
						<tbody style="font-size:9pt;">
						</tbody>
					</table>
				</div>

			</div>
		</div>

		<div class="mapZone" id="map-canvas">
		</div>
	</div>
</div>

</body>
</html>