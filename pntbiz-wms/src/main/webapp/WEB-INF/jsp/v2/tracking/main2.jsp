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
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/tracking/pnt.map.presence.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '08', submenu: '01',
			location: ['<spring:message code="menu.170000" />', '<spring:message code="menu.170100" />']
		};

		/**
		 * 설정 정보
		 */
		window.prop = new pnt.util.Properties();
		prop.put('map.visible.node.beacon', false);
		prop.put('map.visible.node.scanner', false);
		prop.put('map.visible.scanner', false);
		prop.put('map.visible.area', false);
		prop.put('map.visible.geofence', false);
		prop.put('map.visible.triangulation', false); // 삼각측
		prop.put('map.visible.differentialpos', false); // 상대위치결정
		prop.put('floor.autofit', false); // 층 변환시 지도이미지 사이즈에 맞게 뷰 조정
		prop.put('tooltip.last', null); // 마지막 선택 툴팁
		prop.put('marker.label', true);

		prop.put('beacon.show.time', 10000); // 마지막 신호 이후 마커 유지 시간
		// prop.put('map.debug', pnt.util.getUrlParameter('debug')=='yes'?true:false); // 디버그 모드 활성 여부
		prop.put('map.debug', true);
		prop.put('socket.io.server', '<spring:eval expression='@config[presenceURL]'/>')
		prop.put('socket.io.channel', 'presence-${companyInfo.UUID}');
		prop.put('icon.beacon', '${viewProperty.staticUrl}/images/inc/marker_green_10.png');
		prop.put('icon.beaconSos', '${viewProperty.staticUrl}/images/inc/marker_red_10.png');
		prop.put('icon.scanner', '${viewProperty.staticUrl}/images/inc/marker_scanner_10.png');
		prop.put('icon.nodeBeacon', '${viewProperty.staticUrl}/images/inc/marker_blue_10.png');
		prop.put('icon.nodeScanner', '${viewProperty.staticUrl}/images/inc/marker_sky_10.png');
		prop.put('url.static', '${viewProperty.staticUrl}');
		prop.put('url.beacon', '<c:url value="/maptool/data/beacon.do"/>');
		prop.put('url.scanner', '<c:url value="/maptool/data/scanner.do"/>');
		prop.put('url.node', '<c:url value="/maptool/data/node.do"/>');
		prop.put('url.nodeedge', '<c:url value="/maptool/data/nodeEdge.do"/>');
		prop.put('url.geofence', '<c:url value="/maptool/data/geofence.do"/>');
		prop.put('url.floor', '<c:url value="/maptool/data/floor.do"/>');
		prop.put('url.area', '<c:url value="/maptool/data/area.do"/>');
		prop.put('url.api.server', '<spring:eval expression='@config[apiServerUrl]'/>');
		prop.put('comNum', '${companyInfo.comNum}');
		prop.put('uuid', '${companyInfo.UUID}');
		prop.put('marker.click.tooltip', true);

		pnt.util.onReady(function() {
			var presenceMap = pnt.module.run('presence.map', {prop:prop});

			$(window).resize(function() {
				var windowHeight = $(window).height();
				var offsetTop = $('#map-canvas').offset().top;
				$('#map-canvas').height(windowHeight-offsetTop-46);
			});
			$(window).trigger('resize');
			presenceMap.pntmap.getOlMap().updateSize();
		});
	</script>
</head>
<body>

<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.170100" /></b>
	</div>
	<div class="boxCon">
		<div class="mapZone" id="map-canvas">

		</div>
	</div>
</div>
</body>
</html>
