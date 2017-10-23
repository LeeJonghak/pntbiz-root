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
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/tracking/pnt.map.presence.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '01', submenu: '02',
			location: ['<spring:message code="menu.000000" />', '<spring:message code="menu.000101" />']
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
		prop.put('marker.click.tooltip', false); // 마커클릭시 툴팁표시여부
		prop.put('marker.label', true);

		prop.put('floor.autofit', true); // 층 변환시 지도이미지 사이즈에 맞게 뷰 조정
		prop.put('tooltip.last', null); // 마지막 선택 툴팁

		prop.put('beacon.show.time', 5000); // 마지막 신호 이후 마커 유지 시간
		prop.put('map.debug', false); // 디버그 모드 활성 여부
		prop.put('socket.io.server', '<spring:eval expression='@config[presenceURL]'/>')
		prop.put('socket.io.channel', 'presence-${companyInfo.UUID}');
		prop.put('icon.beacon', 'http://static.pntbiz.com/indoorplus/images/inc/marker_green_10.png');
		prop.put('icon.beaconSos', 'http://static.pntbiz.com/indoorplus/images/inc/marker_red_10.png');
		prop.put('icon.scanner', 'http://static.pntbiz.com/indoorplus/images/inc/marker_scanner_10.png');
		prop.put('icon.nodeBeacon', 'http://static.pntbiz.com/indoorplus/images/inc/marker_blue_10.png');
		prop.put('icon.nodeScanner', 'http://static.pntbiz.com/indoorplus/images/inc/marker_sky_10.png');
		prop.put('url.beacon', '<c:url value="/maptool/data/beacon.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.scanner', '<c:url value="/maptool/data/scanner.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.node', '<c:url value="/maptool/data/node.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.nodeedge', '<c:url value="/maptool/data/nodeEdge.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.geofence', '<c:url value="/maptool/data/geofence.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.floor', '<c:url value="/maptool/data/floor.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.area', '<c:url value="/maptool/data/area.do?comNum=${companyInfo.comNum}"/>');
		prop.put('url.remoteJson', '<c:url value="/common/remoteJson.do"/>');
		prop.put('url.static', '${viewProperty.staticUrl}');
		prop.put('url.tracking.log', '<c:url value="/maptool/data/presenceLog.do"/>');
		prop.put('url.api.server', '<spring:eval expression='@config[apiServerUrl]'/>');
		prop.put('comNum', '${companyInfo.comNum}');
		prop.put('uuid', '${companyInfo.UUID}');


		window.console.info = function() {};
		window.console.debug = function() {};

		pnt.util.onReady(function() {
			pnt.module.run('presence.map', {prop:prop});

			var socketIo = new pnt.util.SocketIo(prop.get('socket.io.server'));
			var channel = socketIo.channel(prop.get('socket.io.channel'));
			this.channel = channel;
			channel.on('updateMarker', function (data) {

			});
		});
	</script>
</head>
<body>

	<div id="monitor">
		<div class="monitorDataWrap">
			<div class="monitorData">
				<div class="boxInner type1">
					<div class="title">
						<b>요약정보</b>
										<span class="btnArea">
											<a href="#" id="btn_modalNotice" class="btn-s btn-focus">알림</a>
										</span>
					</div>
					<div class="boxCon">
						<span>비콘수</span>
						<b>100</b>
					</div>
				</div>
				<div class="boxInner type2">
					<div class="title">
						<b>층 선택</b>
										<span class="btnArea">
											<a href="#" class="btn-s btn-focus">조회</a>
										</span>
					</div>
					<div class="boxCon">
						<select name="">
							<option value="">빌딩</option>
						</select>
						<select name="">
							<option value="">층</option>
						</select>
					</div>
				</div>
				<div class="boxInner type3">
					<div class="title">
						<b>진입정보</b>
										<span class="btnArea">
											<a href="#" id="btn_modalSearch" class="btn-s btn-focus">검색</a>
										</span>
					</div>
					<div class="boxCon">
						<table class="basic">
							<colgroup>
								<col />
							</colgroup>
							<thead>
							<tr>
								<th scope="col">대상명</th>
								<th scope="col">진입시간</th>
								<th scope="col">체류시간</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td>AAAA</td>
								<td>10:15</td>
								<td>1분</td>
							</tr>
							<tr>
								<td>AAAA</td>
								<td>10:15</td>
								<td>1분</td>
							</tr>
							<tr>
								<td>AAAA</td>
								<td>10:15</td>
								<td>1분</td>
							</tr>
							<tr>
								<td>AAAA</td>
								<td>10:15</td>
								<td>1분</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="boxInner type4">
					<div class="title"><b>관제설정</b></div>
					<div class="boxCon">
						<ul class="markZone">
							<li>
								<b>전체표시</b>
								<label class="checkBox"><input type="checkbox" name="" id="" value="" checked> 표시함</label>
							</li>
							<li>
								<b>펜스표시</b>
								<label class="checkBox"><input type="checkbox" name="" id="" value=""> 표시함</label>
							</li>
							<li>
								<b>노드표시</b>
								<label class="checkBox"><input type="checkbox" name="" id="" value=""> 표시함</label>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="boxInner type5" style="display:none;">
				<div class="title">
					<b>알림</b>
					<a href="#" class="btnClose">닫기</a>
				</div>
				<div class="boxCon">
					<ul>
						<li>
							<ul>
								<li class="icon1">2017. 08. 01</li>
								<li class="icon2">20:12:45</li>
							</ul>
							<p>비콘 HRED-273 가 층에 들어왔습니다.</p>
						</li>
						<li>
							<ul>
								<li class="icon1">2017. 08. 01</li>
								<li class="icon2">20:12:45</li>
							</ul>
							<p>비콘 HRED-273 가 층에 들어왔습니다.</p>
						</li>
						<li>
							<ul>
								<li class="icon1">2017. 08. 01</li>
								<li class="icon2">20:12:45</li>
							</ul>
							<p>비콘 HRED-273 가 층에 들어왔습니다.</p>
						</li>
						<li>
							<ul>
								<li class="icon1">2017. 08. 01</li>
								<li class="icon2">20:12:45</li>
							</ul>
							<p>비콘 HRED-273 가 층에 들어왔습니다.</p>
						</li>
						<li>
							<ul>
								<li class="icon1">2017. 08. 01</li>
								<li class="icon2">20:12:45</li>
							</ul>
							<p>비콘 HRED-273 가 층에 들어왔습니다.</p>
						</li>
					</ul>
					<div class="btnZone">
						<input type="button" value="닫기" class="btn-l">
					</div>
				</div>
			</div>
			<div class="boxInner type6" style="display:none;">
				<div class="title">
					<b>비콘검색</b>
					<a href="#" class="btnClose">닫기</a>
				</div>
				<div class="boxCon">
					<div class="topSearch">
						<select name="" class="w100">
							<option value="">비콘아이디</option>
						</select>
						<input type="text" name="" value="" class="w150">
						<input type="submit" value="검색" class="btn-inline btn-search">
					</div>
					<table class="basic">
						<colgroup>
							<col width="30%">
							<col width="30%">
							<col />
						</colgroup>
						<thead>
						<tr>
							<th scope="col">층</th>
							<th scope="col">진입횟수</th>
							<th scope="col">진입비콘수</th>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td>1층</td>
							<td>10,000</td>
							<td>100</td>
						</tr>
						<tr>
							<td>1층</td>
							<td>10,000</td>
							<td>100</td>
						</tr>
						<tr>
							<td>1층</td>
							<td>10,000</td>
							<td>100</td>
						</tr>
						<tr>
							<td>1층</td>
							<td>10,000</td>
							<td>100</td>
						</tr>
						</tbody>
					</table>
					<div class="btnZone">
						<input type="button" value="닫기" class="btn-l">
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="map-canvas" style="margin-left:370px; margin-top:-20px;height:800px;"></div>

</body>
</html>