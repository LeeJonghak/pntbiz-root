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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript">
    var elementHandler = new ElementHandler('<c:url value="/"/>');
    $(document).ready( function() {
        elementHandler.init();
    });
    GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
</script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100102" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.form"></span>
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
					<!-- <input type="text" id="UUID" name="UUID" value="" size="50" required="required" maxlength="36" uuid="uuid"  class="form-control" placeholder="UUID"  />-->
					<input type="text" id="UUID" name="UUID" value="" size="50" required="required" maxlength="36" class="form-control" placeholder="<spring:message code="word.uuid" />"  />
				</div>
			</div>
			<div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="word.major.version" /></label>
			    <div class="col-sm-10">
			        <input type="number" id="majorVer" name="majorVer" value="" required="required" size="30" min="0" max="65535" maxlength="25" class="form-control" placeholder="<spring:message code="word.major.version" />"  />
			    </div>
			</div>
			<div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="word.minor.version" /></label>
			    <div class="col-sm-10">
			        <input type="text" id="minorVer" name="minorVer" value="" required="required" size="30" maxlength="25" class="form-control" placeholder="<spring:message code="word.minor.version" />"  />
			    </div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon.group.name" /></label>
				<div class="col-sm-10">
					<select class="form-control" name="beaconGroupNum">
						<option value="">[<spring:message code="word.beacon.group.name" />]</option>
						<c:forEach items="${beaconGroupList}" var="beaconGroupList">
							<option value="${beaconGroupList.beaconGroupNum}">${beaconGroupList.beaconGroupName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon.macaddr" /></label>
				<div class="col-sm-10">
					<input type="text" id="macAddr" name="macAddr" value="${beacon.macAddr}" size="50" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.macaddr" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon.name" /></label>
				<div class="col-sm-10">
					<input type="text" id="beaconName" name="beaconName" value="" required="required" size="30" maxlength="25" class="form-control" placeholder="<spring:message code="word.beacon.name" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon.type" /></label>
				<div class="col-sm-10">
					<select class="form-control" name="beaconType">
						<c:forEach items="${beaconTypeCD}" var="beaconTypeCD">
							<option value="${beaconTypeCD.sCD}"><spring:message code="${beaconTypeCD.langCode}" /></option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.txpower" /></label>
				<div class="col-sm-10">
					<input type="number" id="txPower" name="txPower" value="" required="required" size="15" maxlength="12"  class="form-control" placeholder="<spring:message code="word.txpower" />" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.lat.lng" /></label>
				<div class="col-sm-10">
					<div class="col-sm-3 form-group">
						<input type="number" id="lat" name="lat" value="" size="15" required min="-90" max="90" maxlength="20"  class="form-control" placeholder="<spring:message code="word.lat" />" />
					</div>
					<div class="col-sm-3 form-group">
						<input type="number" id="lng" name="lng" value="" size="15" required min="-180" max="180" maxlength="20"  class="form-control" placeholder="<spring:message code="word.lng" />" />
					</div>
					<div class="col-sm-6 form-group">
						&nbsp;<button id="beaconMapBtn" type="button" class="btn btn-info btn-sm"><spring:message code="word.show.map" /></button>
					</div>
			        <div id="map-canvas" style="width:100%;height:260px;">

			        </div>
				</div>
			</div>
			<div class="form-group">
			    <label class="col-sm-2 control-label"><spring:message code="word.floor" /></label>
			    <div class="col-sm-10">
			        <select class="form-control" name="floor" id="floor-selector">
			            <c:forEach items="${floorList}" var="floor">
			                <option value="${floor.floor}"><c:out value="${floor.floorName}"/></option>
			            </c:forEach>
			        </select>
			    </div>
			</div>
			<div class="form-group form-inline">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon.field" /> 1~5</label>
				<div class="col-sm-10">
					<input type="text" id="field1" name="field1"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.field" /> 1" />
					<input type="text" id="field2" name="field2"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.field" /> 2" />
					<input type="text" id="field3" name="field3"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.field" /> 3" />
					<input type="text" id="field4" name="field4"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.field" /> 4" />
					<input type="text" id="field5" name="field5"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.beacon.field" /> 5" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.external.id" /></label>
				<div class="col-sm-10">
					<input type="text" id="externalId" name="externalId" value="" size="30" maxlength="25" class="form-control" placeholder="<spring:message code="word.external.id" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.barcode" /></label>
				<div class="col-sm-10">
					<input type="text" id="barcode" name="barcode" value="" size="30" maxlength="25" class="form-control" placeholder="<spring:message code="word.barcode" />"  />
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label" id="externalAttribute"><spring:message code="word.external.attribute" /></label>
				<div class="col-sm-10">
					<div id="dynamic-column">
						<div class="form-group">
							<div class="col-sm-2">
								<input type="text" name="key"  maxlength="20" placeholder="<spring:message code="word.key" />" class="txt-col-key form-control"/>
							</div>
							<div class="col-sm-2">
								<input type="text" name="value" maxlength="50" placeholder="<spring:message code="word.value" />" class="txt-col-value form-control"/>
							</div>
							<div class="col-sm-3">
								<input type="text" name="displayName" placeholder="<spring:message code="word.display.name" />" class="txt-col-displayName  form-control"/>
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
					<div class="col-sm-2">
						<input type="text" name="key" placeholder="<spring:message code="word.key"/>" class="txt-col-key form-control"/>
					</div>
					<div class="col-sm-2">
						<input type="text" name="value" placeholder="<spring:message code="word.value"/>" class="txt-col-value form-control"/>
					</div>
					<div class="col-sm-3">
						<input type="text" name="displayName" placeholder="<spring:message code="word.display.name"/>" class="txt-col-displayName  form-control"/>
					</div>
					<div class="col-sm-1">
						<button type="button" class="btn-col-remove btn btn-default btn-sm">
							<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> <spring:message code="btn.delete"/>
						</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.description" /></label>
				<div class="col-sm-10">
					<textarea id="beaconDesc" name="beaconDesc" class="form-control" placeholder="<spring:message code="word.description" />" cols="6" rows="5" maxlength="200"></textarea>
					<span id="descByte">0 / 200 byte</span>
				</div>
			</div>
		</div>

		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="regBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="listBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>
