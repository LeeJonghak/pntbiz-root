<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<spring:eval expression="@config['map.type']" var="mapType"/>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/info/infoCompany.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<c:if test="${empty mapType or mapType eq 'googlemaps'}">
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
			func: function(){infoCompany.init()}
		});
	});
	</script>
</c:if>
<c:if test="${mapType eq 'openlayers'}">
	<link rel="stylesheet" href="${viewProperty.staticUrl}/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		pnt.util.onReady(function() {
			window.company = {};
			company.lat = ${companyInfo.lat};
			company.lng = ${companyInfo.lng};
		});
	</script>
</c:if>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990100" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-heading"></div> 
		
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.company.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="comName" name="comName" value="${companyInfo.comName}" maxlength="25" class="form-control" placeholder="<spring:message code="word.company.name"/>" disabled />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">UUID</label>
				<div class="col-sm-10">
					<input type="text" id="UUID" name="UUID" value="${companyInfo.UUID}" maxlength="36" class="form-control" placeholder="UUID"  disabled />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.business.number"/></label>
				<div class="col-sm-10">
					<input type="text" id="comBizNum" name="comBizNum" value="${companyInfo.comBizNum}" maxlength="12"  class="form-control" placeholder="<spring:message code="word.business.number"/>" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.lat.lng" /></label>
				<div class="col-sm-10">	
					<div class="col-sm-3 form-group">
						<input type="text" id="lat" name="lat" value="${companyInfo.lat}" class="form-control" placeholder="<spring:message code="word.lat" />" />
					</div>
					<div class="col-sm-3 form-group">
						<input type="text" id="lng" name="lng" value="${companyInfo.lng}" class="form-control" placeholder="<spring:message code="word.lng" />" />
					</div>
					<div class="col-sm-6 form-group">
						<button id="infoCompanyMapBtn" type="button" class="btn btn-info btn-sm"><spring:message code="word.show.map" /></button>
					</div>
					<div id="map-canvas" style=""></div>
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="infoCompanyModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>