<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/scanner.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	$.gmap.load({
		lat: '${companyInfo.lat}',
		lng: '${companyInfo.lng}',
		zoom: 19,
		width: "100%",
		height: "300px",
		div: "map-canvas",
		click: (function(event) {
			$("#lat").val(event.latLng.lat());
			$("#lng").val(event.latLng.lng());
			try {
				var latlng = event.latLng.lat() + "," + event.latLng.lng();
				$("#latlng").val($("#latlng").val() + latlng + "<br />");
			} catch(e) {}
			if($.gmap.is($.gmap.markers["1"]) == true) {
				$.gmap.moveMarker({id:1, lat: event.latLng.lat(), lng: event.latLng.lng() });
			} else {
				$.gmap.addMarker({id:1, lat: event.latLng.lat(), lng: event.latLng.lng() });
			}
		}),
		func: function(){
			scanner._floor = ${scannerInfo.floor};
			scanner.setFloor();
		}
	});
	var setmapURL = "/tracking/presence/setmap.info.do";
	var floorURL = "/map/floor/list.do";
	$.when(
		$.ajax({ type: "POST", url: setmapURL, data: "", success: scanner.setmapInfoResult }),
		$.ajax({ type: "POST", url: floorURL, data: "", success: scanner.floorInfoResult })
	).then(function(){
	}).done(function() {
		var formDataLoader = new PNTFormDataLoader('form1');
		formDataLoader.load('get.json',{scannerNum:common.getQueryString('scannerNum')});
	});
});

$(window).load(function(){
	scanner.setFloor();
	if($("#lat").val() != 0 && $("#lng").val() != 0) {
		$.gmap.addMarker({id:1, lat: $("#lat").val(), lng: $("#lng").val() });
	}
});
</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="scannerNum" name="scannerNum" value="${scannerInfo.scannerNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110103" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110100" /></li>
	</ol>
</div>
<div class="topbar-right">
</div>
</header>

<section id="content" class="table-layout animated fadeIn">
<div class="row col-md-12">
	<div class="panel">

		<div class="panel-heading">
		</div>

		<div class="panel-body">

			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.mac.address" /></label>
				<div class="col-sm-10">
					<input type="text" id="macAddr" name="macAddr" value="${scannerInfo.macAddr}" maxlength="35" class="form-control" placeholder="AA:BB:CC:DD:EE:FF"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.major.version" /></label>
				<div class="col-sm-10">
					<input type="text" id="majorVer" name="majorVer" value="${scannerInfo.majorVer}" maxlength="100" class="form-control" placeholder="<spring:message code="word.major.version" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.scanner.name" /></label>
				<div class="col-sm-10">
					<input type="text" id="scannerName" name="scannerName" value="${scannerInfo.scannerName}" maxlength="25" class="form-control" placeholder="<spring:message code="word.scanner.name" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.identifier" /></label>
				<div class="col-sm-10">
					<input type="text" id="sid" name="sid" value="${scannerInfo.sid}" maxlength="10" class="form-control" placeholder="gate1, group1" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.lat.lng" /></label>
				<div class="col-sm-10">
					<div class="col-sm-3 form-group">
						<input type="text" id="lat" name="lat" value="${scannerInfo.lat}" class="form-control" placeholder="<spring:message code="word.lat" />" />
					</div>
					<div class="col-sm-3 form-group">
						<input type="text" id="lng" name="lng" value="${scannerInfo.lng}" class="form-control" placeholder="<spring:message code="word.lng" />" />
					</div>
					<div class="col-sm-6 form-group">
						<button id="scannerMapBtn" type="button" class="btn btn-info btn-sm"><spring:message code="word.show.map" /></button>
					</div>
					<div id="map-canvas" style="width:100%;height:350px;"></div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="rssi" name="rssi" value="${scannerInfo.rssi}" maxlength="6" class="form-control" placeholder="0.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Sole RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="srssi" name="srssi" value="${scannerInfo.srssi}" maxlength="6" class="form-control" placeholder="15.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Min RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="mrssi" name="mrssi" value="${scannerInfo.mrssi}" maxlength="6" class="form-control" placeholder="-100.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">MaxDiff RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="drssi" name="drssi" value="${scannerInfo.drssi}" maxlength="6" class="form-control" placeholder="100.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Exclusive Meter</label>
				<div class="col-sm-10">
					<input type="text" id="exMeter" name="exMeter" value="${scannerInfo.exMeter}" maxlength="6" class="form-control" placeholder="15.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Cal point num</label>
				<div class="col-sm-10">
					<input type="text" id="calPoint" name="calPoint" value="${scannerInfo.calPoint}" maxlength="3" class="form-control" placeholder="4.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Max Signal</label>
				<div class="col-sm-10">
					<input type="text" id="maxSig" name="maxSig" value="${scannerInfo.maxSig}" maxlength="6" class="form-control" placeholder="30.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Max RSSI Buffer</label>
				<div class="col-sm-10">
					<input type="text" id="maxBuf" name="maxBuf" value="${scannerInfo.maxBuf}" maxlength="6" class="form-control" placeholder="20.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Firmware Version</label>
				<div class="col-sm-10">
					<input type="text" id="fwVer" name="fwVer" value="${scannerInfo.fwVer}" maxlength="30" class="form-control" placeholder="1.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.floor" /></label>
				<div class="col-sm-10">
					<select id="floor" name="floor" class="form-control">
						<option value="">==<spring:message code="word.floor" />==</option>
						<c:forEach var="floorList" items="${floorList}">
							<option value="${floorList.floor}" <c:if test="${scannerInfo.floor eq floorList.floor}">selected</c:if>><c:out value="${floorList.floorName}"/></option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>

		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="scannerModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
				<button id="scannerDelBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
				<button id="scannerListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>