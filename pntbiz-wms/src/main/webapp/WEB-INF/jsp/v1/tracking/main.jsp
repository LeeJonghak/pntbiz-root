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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/tracking.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<c:choose>
<c:when test="${pageContext.request.serverName eq 'onticplace.cwit.co.kr'}">
<script type="text/javascript" src="https://112.76.23.6:10000/socket.io/socket.io.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</c:when>
<c:otherwise>
<script type="text/javascript" src="<spring:eval expression='@config[presenceURL]'/>/socket.io/socket.io.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</c:otherwise>
</c:choose>	
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceMap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>
$(window).ready( function() {
	presenceMap.init({
		"comNum":${companyInfo.comNum},
		"lat":${companyInfo.lat},
		"lng":${companyInfo.lng},
		"UUID":"${companyInfo.UUID}",
		"initZoom":${setmapInfo.initZoom},
		"initFloor":"${setmapInfo.initFloor}",
		"checkTimeInterval":${setmapInfo.comNum},
		"removeTimeInterval":${setmapInfo.removeTimeInterval},
		"moveTimeInterval":${setmapInfo.moveTimeInterval},
		"moveUnit":${setmapInfo.moveUnit},
		"width": "100%",
		"height": 800,
		<c:choose>
		<c:when test="${pageContext.request.serverName eq 'onticplace.cwit.co.kr'}">
		"socketURL": "https://112.76.23.6:10000"
		</c:when>
		<c:otherwise>
		"socketURL": "<spring:eval expression='@config[presenceURL]'/>"
		</c:otherwise>
		</c:choose>
	}); 
	var floor;	
	<c:forEach items="${floorList}" var="floorList">
	floor = {floorNum: ${floorList.floorNum}, floor: "${floorList.floor}", floorName : "${floorList.floorName}", 
			swLat : ${floorList.swLat},	swLng : ${floorList.swLng}, 
			neLat : ${floorList.neLat}, neLng : ${floorList.neLng},
			deg : ${floorList.deg}, imgURL : "${floorList.imgURL}"};
	
	presenceMap.floorList[floor.floorNum.toString()] = floor;     
	</c:forEach>
	presenceMap.setFloorGroupList();	
});	
	
$(window).on('beforeunload', function(){
    try { socket.close(); } catch(e) { }
});
$(window).load(function() {		
	$("body").css("overflow", "hidden");
});
</script>
</head>
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
			<div id="map-canvas" class="map"></div>
			<!--<iframe src="https://beacon.pntbiz.com:10000/login/force/${userID}/%2Fpresence%2F" id="map-frame" name="map-frame" width="100%" height="100%" style="border: 0px; overflow:hidden; min-height:800px;"></iframe>-->
		</div>
	</div>
</div>	
</section>
</form>
</body>
</html>