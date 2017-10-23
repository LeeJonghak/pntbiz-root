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
<link rel="stylesheet" href="${viewProperty.staticUrl}/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/pnt.map.presence.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/dashboard/dashboard.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>


	<script type="text/javascript">
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
		prop.put('marker.click.tooltip', pnt.util.getUrlParameter('tooltip')=='yes'?true:false); // 마커클릭시 툴팁표시여부
		prop.put('marker.label', true);

		prop.put('floor.autofit', false); // 층 변환시 지도이미지 사이즈에 맞게 뷰 조정
		prop.put('tooltip.last', null); // 마지막 선택 툴팁

		prop.put('beacon.show.time', 10000); // 마지막 신호 이후 마커 유지 시간
		prop.put('map.debug', true); // 디버그 모드 활성 여부
		prop.put('socket.io.server', '<spring:eval expression='@config[presenceURL]'/>')
		prop.put('socket.io.channel', 'presence-${companyInfo.UUID}');
		prop.put('icon.beacon', '${viewProperty.staticUrl}/images/inc/marker_green_10.png');
		prop.put('icon.beaconSos', '${viewProperty.staticUrl}/images/inc/marker_red_10.png');
		prop.put('icon.scanner', '${viewProperty.staticUrl}/images/inc/marker_scanner_10.png');
		prop.put('icon.nodeBeacon', '${viewProperty.staticUrl}/images/inc/marker_blue_10.png');
		prop.put('icon.nodeScanner', '${viewProperty.staticUrl}/images/inc/marker_sky_10.png');
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
		})
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<input type="hidden" id="comNum" name="comNum" value="${comNum}" />
<input type="hidden" id="userID" name="userID" value="${userID}" />
<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.000000" /></a></li>
		<li class="crumb-icon"><a href="/"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.000000" /></li>
	</ol>
</div>
<div class="topbar-right">
</div>
</header>

<section id="content" class="table-layout animated fadeIn">
	<div class="tray tray-center admin-panels">	
		<div class="row">
			<div class="col-sm-3 col-xl-3">
				<div class="panel panel-tile text-center br-a br-grey">
					<div class="panel-body">
						<h1 class="fs30 mt5 mbn">${beaconCnt}</h1>
						<h6 class="text-system"><spring:message code="dashboard.total.beacons" /></h6>
					</div>
				</div>
			</div>
			<div class="col-sm-3 col-xl-3">
				<div class="panel panel-tile text-center br-a br-grey">
					<div class="panel-body">
						<h1 class="fs30 mt5 mbn">${scannerCnt}</h1>
						<h6 class="text-success"><spring:message code="dashboard.total.scanners" /></h6>
					</div>
				</div>
			</div>
			<div class="col-sm-3 col-xl-3">
				<div class="panel panel-tile text-center br-a br-grey">
					<div class="panel-body">
						<h1 class="fs30 mt5 mbn">${geofenceCnt}</h1>
						<h6 class="text-warning"><spring:message code="dashboard.total.geofencings" /></h6>
					</div>
				</div>
			</div>
			<div class="col-sm-3 col-xl-3">
				<div class="panel panel-tile text-center br-a br-grey">
					<div class="panel-body">
						<h1 class="fs30 mt5 mbn">${floorCnt}</h1>
						<h6 class="text-info"><spring:message code="dashboard.total.floors" /></h6>
					</div>
				</div>
			</div>
		</div>
		<!--
		admin panel (value를 true가 아닌 attribute를 제거 하면 사용함으로 변경 됨)
		data-panel-remove="false" 판넬 제거
		data-panel-title="false" 타이틀명 변경
		data-panel-color="false" 타이틀 색상 변경
		data-panel-collapse="false" 판넬 콜랩스
		data-panel-fullscreen="false" 풀스크린  
		sort-disable class 판넬이동 사용 안함.
		-->
		<div id="scanner-monitor-anchor" class="row">		
			<div class="col-md-12 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-system"><i class="glyphicon glyphicon-saved"></i></span>
						<span class="panel-title text-system"><spring:message code="menu.170101" /></span> 
					</div>
					<div class="panel-body pn">
						<!--
						<iframe src="" id="map-frame" name="map-frame" width="100%" height="360" style="border: 0px; overflow:hidden;"></iframe>
						-->
						<div id="map-canvas" class="map"></div>
					</div>
				</div>
			</div>
			<%-- 	
			<div class="col-md-6 admin-grid">				
				<div class="panel tagcloud-widget sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-system"><i class="imoon imoon-screen"></i> </span>
						<span class="panel-title text-system"><spring:message code="dashboard.scanner.monitor" /></span>
					</div>
					<div class="panel-body pn">
						<table class="table">
							<thead>
							</thead>
							<tbody>
							<tr>
								<td id="scannerStatusList"></td>
							</tr>
							<tr>
								<td></td>
							</tr>
							</tbody>							
						</table>
						<div class="panel-scroller scroller-md scroller-system">
						<table id="scannerStatusPanel" class="table">	
							<tbody>
							</tbody>
						</table>
						<!--  
						<table id="scannerStatusPanel" class="table">							
							<thead>
							<tr class="bg-light">
								<th style="width: 25%"><spring:message code="word.scanner.name" /></th>
								<th style="width: 7%"><spring:message code="word.ping" /></th>
								<th style="width: 30%"><spring:message code="word.cpu" /></th>
								<th style="width: 30%"><spring:message code="word.memory" /></th>
								<th style="width: 8%"><spring:message code="word.up" />/<spring:message code="word.down" /> <spring:message code="word.time" /></th>
							</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						-->
						</div>
						
					</div>
				</div>
			</div>
			--%>
		</div>
		
		<%--		
		<div class="row">
			<div class="col-md-6 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
      				<div class="panel-heading">
     					<span class="panel-icon text-info"><i class="octicon octicon-graph"></i></span>
						<span class="panel-title text-info"><spring:message code="dashboard.loss.beacon.status" /></span>
            		</div>
					<!--             	
					<div class="panel-menu pn bg-white">
		            	<ul class="nav nav-justified text-center fw600 chart-legend" data-chart-id="#high-column3">
		                	<li>
		                		<a href="#" class="legend-item" data-chart-id="0"> Tech </a>
		                	</li>
		                	<li class="br-l">
		                  		<a href="#" class="legend-item" data-chart-id="1"> Support </a>
		                	</li>
		                	<li class="br-l">
		                  		<a href="#" class="legend-item" data-chart-id="2"> Service </a>
		                	</li>
		                	<li class="br-l">
		                  		<a href="#" class="legend-item" data-chart-id="3"> Another </a>
		                	</li>
		            	</ul>
		            </div> 
		            -->				            
		            <div class="panel-body pbn">
		           		<div id="high-column3" style="width: 100%; height: 285px; margin: 0 auto"></div>
		
		           		<p class="hidden br-t pt15 text-muted text-center mh20 fw400">A percent measure of tickets with
		                <b class="text-info">first</b> reply time</p>
		            </div>
					<!--
					<div class="panel-footer p15">
						<p class="text-muted text-center mbn">A percent measure of tickets with
				        <b class="text-info">first</b> reply time</p>
				    </div> 
				    -->
				</div>
			</div>		
			
			<div class="col-md-6 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-info"><i class="fa fa-clock-o"></i></span>
						<span class="panel-title text-info"><spring:message code="dashboard.loss.beacon" /></span>
					</div>
					<div class="panel-body pn panel-scroller scroller-md scroller-info">
						<ol class="timeline-list">
							<c:forEach var="lossBeList" items="${lossBeList}" >
							<li class="timeline-item">
								<div class="timeline-icon bg-dark light">
									<span class="fa fa-rss"></span>
								</div>
								<div class="timeline-desc">
									<b>${lossBeList.beaconName}</b> <a href="<c:url value="/map/bsplan.do"/>#${lossBeList.floor}/${lossBeList.beaconNum}"><span class="imoon imoon-map2"></span></a>
								</div>
								<div class="timeline-date">
									<c:if test="${lossBeList.regDate ne null}" >
										<jsp:useBean id="regDate" class="java.util.Date"/>
										<jsp:setProperty name="regDate" property="time" value="${lossBeList.regDate}000"/>
										<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
									</c:if>
								</div>
							</li>
							</c:forEach>
						</ol>
					</div>
				</div>
			</div>
			
		</div>
		-->
		
		<%--
		<div class="row">
			<div class="col-md-6 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-success"><i class="icon-chart-graph2"></i></span>
						<span class="panel-title text-success"><spring:message code="menu.170313" /></span> 
					</div> 
					<div class="panel-menu">
						<div class="chart-legend div-high-line3" data-chart-id="#high-line3">
							<!-- data-chart-id : 변경. js에서  high-line3(series 순서) -->
							<!-- <a type="button" data-chart-id="0" class="legend-item btn btn-sm btn-primary mr10">Data 1</a> 
							<a type="button" data-chart-id="1" class="legend-item btn btn-info btn-sm">Data 2</a> -->
						</div>
					</div>
					<div class="panel-body pn">
						<div id="high-line3" style="width: 100%; height: 285px; margin: 0 auto"></div>
					</div>
				</div>
			</div>		
			
			<!-- 개발 필요 -->
			<div class="col-md-6 admin-grid">				
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
	            	<!-- Circle Stats -->
	                <div class="panel-heading">
	                	<span class="panel-icon text-success"><i class="icon-chart-pie"></i></span>
	                	<span class="panel-title text-success"><spring:message code="menu.170314" /></span>
	                </div>
	              	<div class="panel-menu text-right">
	                    <span class="fs11 text-muted">
							<i class="fa fa-circle text-primary fs12 pr5"></i> ENTER</span>
	                    <span class="fs11 text-muted ml10">
	                      	<i class="fa fa-circle text-info fs12 pr5"></i> LEAVE</span>
	                    <span class="fs11 text-muted ml10">
							<i class="fa fa-circle text-warning fs12 pr5"></i> STAY</span>
	                </div>
	                <div class="panel-body pn panel-scroller scroller-md scroller-info">
						<table class="table">
							<tbody>
							    <c:set value="0" var="count"/>
								<c:forEach var="list" items="${presenceGfInOutList}" varStatus="status">									
									
									<tr align="center">
									<td style="padding-left:30px">
										<div class="info-circle" id="c${count+1}" title="${list.fcName}" value="${list.enterCount}" style="width: 180px; height: 180px;"
											data-circle-color="primary"></div></td>
									<td><div class="info-circle" id="c${count+2}" title="${list.fcName}" value="${list.leaveCount}" style="width: 180px; height: 180px;"
											data-circle-color="info"></div></td>
									<td style="padding-right:30px">
										<div class="info-circle" id="c${count+3}" title="${list.fcName}" value="${list.stayCount}" style="width: 180px; height: 180px;"
											data-circle-color="warning"></div></td>
									</tr>
									
									<c:set value="${count+3}" var="count"/>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		-->
		 
		<!-- 스캐너 위치측위현황, 비콘 위치측위 현황 현재 data는 js에 임의로 넣어놓은 data를 보여주고 있음
			 홍부대표님이 7월 25일 업체에 보여줘야한다는 이유룰 data 연동없이 그래프만 노출  -->
		<%--
		<div class="row">
			<div class="col-md-6">
				<!-- Filterable Column Chart -->
		    	<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
		        	<div class="panel-heading">
		            	<span class="panel-icon text-warning"><i class="fa fa-rss"></i></span>
						<span class="panel-title text-warning"><spring:message code="menu.170304" /></span>
		            </div>
		            <div class="panel-body pn">
		              <div id="high-line" style="width: 100%; height: 275px; margin: 0 auto"></div>
		          </div>
				</div> 
			</div>		
			
			<div class="col-md-6">
				<!-- Filterable Column Chart -->
		          <div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
		            <div class="panel-heading">
		              	<span class="panel-icon text-warning"><i class="fa fa-mobile-phone"></i></span>
						<span class="panel-title text-warning"><spring:message code="menu.170312" /></span>
		            </div>
		            <div class="panel-body pn">
		              <div id="high-line-1" style="width: 100%; height: 275px; margin: 0 auto"></div>
		          </div>
				</div>	
			</div>	
		</div>
		-->
		
		<%-- 
		<div class="row">
			<div class="col-md-6 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-warning"><i class="fa fa-rss"></i></span>
						<span class="panel-title text-warning">스캐너 위치측위 현황</span>
					</div>	
					<div class="panel-body pn panel-scroller scroller-sm scroller-warning">
						<table class="table mbn">
							<thead>
							<tr>
								<th>minor</th>
								<th>beaconName</th>
								<th>lat/lng</th>
								<th>regDate</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="presenceLogList" items="${presenceLogList}" >
							<tr>
								<td>${presenceLogList.majorVer}_${presenceLogList.minorVer}</td>
								<td>${presenceLogList.beaconName}</td>
								<td>${presenceLogList.lat}/${presenceLogList.lng}</td>
								<td>
								<jsp:useBean id="slogRegDate" class="java.util.Date"/>
								<jsp:setProperty name="slogRegDate" property="time" value="${presenceLogList.regDate}000"/>
								<fmt:formatDate value="${slogRegDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
								</td>	
							</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>		
			
			<div class="col-md-6 admin-grid">
				<div class="panel sort-disable" data-panel-remove="false" data-panel-title="false" data-panel-color="false">
					<div class="panel-heading">
						<span class="panel-icon text-warning"><i class="fa fa-mobile-phone"></i></span>
						<span class="panel-title text-warning">비콘 위치측위 현황</span>
					</div>
					<div class="panel-body pn panel-scroller scroller-sm scroller-warning">
						<table class="table mbn">
							<thead>
							<tr>
								<th>phoneNumber</th>
								<th>deviceInfo</th>
								<th>lat/lng</th>
								<th>regDate</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="beaconLogList" items="${beaconLogList}" >
							<tr>
								<td>${beaconLogList.phoneNumber}</td>
								<td>${beaconLogList.deviceInfo}</td>
								<td>${beaconLogList.lat}/${beaconLogList.lng}</td>
								<td>
								<jsp:useBean id="blogRegDate" class="java.util.Date"/>
								<jsp:setProperty name="blogRegDate" property="time" value="${beaconLogList.regDate}000"/>
								<fmt:formatDate value="${blogRegDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
								</td>	
							</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>		
		</div> 
		--%>
		
	</div>
</section>
</form>
<%--
  <script type="text/javascript">
	jQuery(document).ready(function() {
		"use strict";
        $.ajax({
            type: 'post',
            url: 'logData.ajax.do',
            data: '',
            dataType: 'json'
        }).done(function(json) {	
        	// This page contains more Initilization Javascript than normal.
    	    // As a result it has its own js page. See charts.js for more info
    	    highCharts.init(json.presenceGfList, json.lossBeChartList, null, null);
        }).fail(function() {
           	// window.alert('오류가 발생하였습니다. #2');
        });
	});
  </script>
--%>
</body>
</html>