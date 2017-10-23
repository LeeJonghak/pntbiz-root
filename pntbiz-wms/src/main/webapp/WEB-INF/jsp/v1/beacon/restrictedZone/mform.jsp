<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/layout.js"></script>
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100106" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.mform"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel">

		<div class="panel-heading"></div>

		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.uuid" /></label>
				<div class="col-sm-10">
					<input type="hidden"  id="beaconNum" name="beaconNum" value="${beacon.beaconNum}">
					<input type="text" id="UUID" name="UUID" value="${beacon.UUID}" size="50" required="required" maxlength="36" class="form-control" readonly="readonly"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.restricted.zone.type" /></label>
				<div class="col-sm-10">
					<input type="hidden" name="permitted" value="${beaconRestrictedZone.permitted}"/>
					<input type="text" id="permitted" size="50" required="required" maxlength="10" class="form-control" readonly="readonly"
							<c:if test="${beaconRestrictedZone.permitted=='TRUE'}"> value=<spring:message code="word.permitted"/> </c:if>
							<c:if test="${beaconRestrictedZone.permitted=='FALSE'}"> value=<spring:message code="word.restricted"/> </c:if> />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.zone.type" /></label>
				<div class="col-sm-10">
					<input type="hidden" name="zoneType" value="${beaconRestrictedZone.zoneType}">
					<input type="text" readonly="readonly"  class="form-control"
						   <c:if test="${beaconRestrictedZone.zoneType=='FLOOR'}">value=<spring:message code="word.floor" /></c:if>
							<c:if test="${beaconRestrictedZone.zoneType=='GEOFENCE'}">value=<spring:message code="word.geofencing.zone" /></c:if>
					/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" id="floor" ${beaconRestrictedZone.zoneType=='FLOOR' ? '' : 'style="display: none;"'}><spring:message code="word.floor" /></label>
				<label class="col-sm-2 control-label" id="geofence" ${beaconRestrictedZone.zoneType=='GEOFENCE' ? '' : 'style="display: none;"'}><spring:message code="word.geofencing" /><spring:message code="word.number" /></label>
				<div class="col-sm-10">
					<input type="text" readonly="readonly"  name="zoneId" class="form-control" value="${beaconRestrictedZone.zoneId}" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.additional.attribute" /></label>
				<div class="col-sm-10">
					<div id="dynamic-column">
						<input type="hidden" id="jsonData" value="<c:out value="${beaconRestrictedZone.additionalAttributeRaw}"  escapeXml="true" />" />
						<div class="form-group">
							<div class="col-sm-4">
								<input type="text" name="key" placeholder="<spring:message code="word.key" />" class="txt-col-key form-control"/>
							</div>
							<div class="col-sm-4">
								<input type="text" name="value" placeholder="<spring:message code="word.value" />" class="txt-col-value form-control"/>
							</div>
							<div class="col-sm-1">
								<button type="button" id="addColumn" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <spring:message code="word.column.register" />
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="hide">
				<div id="column-source" class="form-group">
					<div class="col-sm-4">
						<input type="text" name="key" placeholder="<spring:message code="word.key"/>" class="txt-col-key form-control"/>
					</div>
					<div class="col-sm-4">
						<input type="text" name="value" placeholder="<spring:message code="word.value"/>" class="txt-col-value form-control"/>
					</div>
					<div class="col-sm-1">
						<button type="button" class="btn-col-remove btn btn-default btn-sm">
							<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> <spring:message code="btn.delete"/>
						</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.start.date" /></label>
				<input type="hidden" id="startDateTimestamp" name="startDate" value="${beaconRestrictedZone.startDate}" />
				<div class=" col-sm-10">
					<div class="input-group date" id="datetimepicker1">
						<input type="text" class="col-sm-11 form-control pnt-timestamp" id="showStartDate" placeholder="<spring:message code="word.start.date" />"
							   data-timestamp="${beaconRestrictedZone.startDate}" data-format="YYYY-MM-DD HH:mm:ss" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.end.date" /></label>
				<input type="hidden" id="endDateTimestamp" name="endDate" value="${beaconRestrictedZone.endDate}"/>
				<div class=" col-sm-10">
					<div class="input-group date" id="datetimepicker2">
						<input type="text" class="col-sm-11 form-control pnt-timestamp" id="showEndDate" placeholder="<spring:message code="word.end.date" />"
							   data-timestamp="${beaconRestrictedZone.endDate}" data-format="YYYY-MM-DD HH:mm:ss" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>
		</div>

		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="restrictedZoneModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
				<button id="restrictedZoneDelBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
				<button id="restrictedZoneListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/beacon/restrictedZone/list.do?beaconNum=${beacon.beaconNum}&UUID=${beacon.UUID}');"><spring:message code="btn.restricted.zone.list" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>