<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/map.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/gmap.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/cmmFloorSel.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/fplan2.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>

<link rel="stylesheet" href="${viewProperty.staticUrl}/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>

<script>

$(document).ready(function() {
	/*$.gmap.load({
		lat: Number('${companyInfo.lat}'),
		lng: Number('${companyInfo.lng}'),
		zoom: 19,
		width: "100%",
		height: "100vh",
		div: "map-canvas",
		click: function(event) {
			var lat = event.latLng.lat();
			var lng = event.latLng.lng();
			$("#lat").val(lat);
			$("#lng").val(lng);
			var id = fplan._cnt.toString();
			fplan._cnt++;
			$.gmap.addMarker({
				id: id,
				lat: lat,
				lng: lng,
				title: "",
				draggable: true,
				clickable: true,
				click: function() {
					this.infoWindow.open(this.map, this);
					var latlng = this.getPosition();
					var lat = latlng.lat();
					var lng = latlng.lng();
					$("#lat").val(lat);
					$("#lng").val(lng);
				},
				rightclick: function () {
					$.gmap.removeMarker({id:id});
				},
				drag: function() {
					var latlng = this.getPosition();
					var lat = latlng.lat();
					var lng = latlng.lng();
					this.infoWindow.setContent("lat : " + lat + "<br />" + "lng : " + lng);
					this.infoWindow.open(this.map, this);
					$("#lat").val(lat);
					$("#lng").val(lng);
				},
				infoWindowOption: {
					id: id,
					content: "lat : " + lat + "<br />" + "lng : " + lng,
					maxWidth: 500
				}
			});
		}
	});*/



	fplan._initLat = Number('${companyInfo.lat}');
	fplan._initLng = Number('${companyInfo.lng}');
	cmmFloorSel._floorCodeArr = ${floorCodeList};

    $("#divLctForm").hide();

	window.pntmap = new pnt.map.OfflineMap('map-canvas');


	window.pntmap.on('image.modify', function(event) {

		var lonlat1 = pnt.util.transformLonLat(event.coordinates[0]);
		var lonlat2 = pnt.util.transformLonLat(event.coordinates[2]);

		$('#swLat').val(lonlat1[1]);
		$('#swLng').val(lonlat1[0]);
		$('#neLat').val(lonlat2[1]);
		$('#neLng').val(lonlat2[0]);
	});

	window.pntmap.on('image.rotate', function(event) {
		$('#deg').val(event.angle);
	});


	$(window).resize(function() {
		var windowHeight = $(window).height();
		var offsetTop = $('#map-canvas').offset().top;
		$('#map-canvas').height(windowHeight-offsetTop-46);
	});
	$(window).trigger('resize');
	window.pntmap.getOlMap().updateSize();

	/*$('#swLat').on('focusout', function() {

		if($('#swLat').val()!='' && $('#swLng').val()!='' && $('#neLat').val()!='' && $('#neLng').val()!='') {
			var sw = [$('#swLng').val(), $('#swLat').val()];
			var ne = [$('#neLng').val(), $('#neLat').val()];
			var swLonlat = pnt.util.transformCoordinates(sw);
			var neLonlat = pnt.util.transformCoordinates(ne);

			var list = pntmap.getObjectManager().findTag('floor');
			if(typeof(list)!='undefined' && list!=null && list.length>0) {
				var extent = pnt.util.transformExtentCoordinates([swLonlat[0],swLonlat[1],neLonlat[0],neLonlat[1]]);

				console.log('extent', [swLonlat[0],swLonlat[1],neLonlat[0],neLonlat[1]], extent);

				list[0].setExtent(extent);
			}
		}


	});*/
});


</script>
</head>
<body>

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.120100" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.120000" /></li>
		<li class="crumb-trail"><spring:message code="menu.120100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<form name="form1" id="form1" class="form-horizontal" role="form" enctype="multipart/form-data">
<input type="hidden" id="comNum" name="comNum" value="${companyInfo.comNum}">
<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12 admin-form">
	<div class="panel">

		<div class="panel-heading"></div>

		<div class="panel-body">

			<div class="col-xs-3 col-md-3">

				<div id="floorForm" class="panel">

					<div class="panel-body">
                        <input type="hidden" id="floorNum" name="floorNum" value="" />
						<%-- <div class="form-group">
							<label class="col-lg-4 control-label"><spring:message code="word.floor" /></label>
							<div class="col-lg-8">
								<input type="text" id="floor" name="floor" value="" maxlength="3" class="form-control" placeholder="<spring:message code="word.floor" />" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-4 control-label"><spring:message code="word.floor.name" /></label>
							<div class="col-lg-8">
								<input type="text" id="floorName"  name="floorName" value="" maxlength="50" class="form-control" placeholder="<spring:message code="word.floor.name" />" />
							</div>
						</div> --%>
						<div class="form-group">
							<label class="col-lg-4 control-label">swLat</label>
							<div class="col-lg-8">
								<input type="text" id="swLat" name="swLat" value="" class="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-4 control-label">swLng</label>
							<div class="col-lg-8">
								<input type="text" id="swLng" name="swLng" value="" class="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-4 control-label">neLat</label>
							<div class="col-lg-8">
								<input type="text" id="neLat" name="neLat" value="" class="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-4 control-label">neLng</label>
							<div class="col-lg-8">
								<input type="text" id="neLng" name="neLng" value="" class="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-4 control-label">deg</label>
							<div class="col-lg-8">
								<input type="text" id="deg" name="deg" value="" class="form-control" />
							</div>
						</div>

                        <div id="floorLocationList">
                        </div>

						<div class="form-group">
							<div class="col-lg-12">
								<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
									<div class="fileupload-preview thumbnail mb20 row">
										<img src="" data-src="" id="img-thumbnail" alt="<spring:message code="word.image" />" width="100%" height="100" />
									</div>
									<div class="row">
										<span class="button btn-system btn-file btn-block">
											<span class="fileupload-new"><spring:message code="word.choose" /></span>
											<span class="fileupload-exists"><spring:message code="word.change" /></span>
											<input type="file" id="imgSrc" name="imgSrc" size="10" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
										</span>
									</div>
								</div>
							</div>
						</div>


						<div class="form-group" id="divRegBtn">
							<input type="button" id="floorRegBtn" class="btn btn-primary btn-block form-control" value="<spring:message code="btn.register" />" />
						</div>
						<div class="form-group">
							<input type="button" id="floorModBtn" class="btn btn-primary btn-block form-control" value="<spring:message code="btn.modify" />" />
							<input type="button" id="floorDelBtn" class="btn btn-danger btn-block form-control" value="<spring:message code="btn.delete" />" />
						</div>
					</div>
				</div>

                <div class="form-group" id="divLctForm">
                    <label class="col-lg-4 control-label">dept</label>
                    <div class="col-lg-8">
                        <select class="form-control" placeholder="dept" onchange="cmmFloorSel.callLocationSel(this)">
                            <option value="">선택하세요</option>
                        </select>
                   </div>
                </div>

				<button id="floorFormBtn" type="button" class="btn btn-primary btn-lg btn-block">(<spring:message code="word.floor" />) <spring:message code="btn.add" /></button>
				<div id="floorList"></div>

			</div>

			<div class="col-xs-9 col-md-9 admin-form">
				<div id="mapForm">
					<div id="map-canvas"></div>
				</div>
			</div>

		</div>

	</div>
</div>
</section>
</form>
</body>
</html>