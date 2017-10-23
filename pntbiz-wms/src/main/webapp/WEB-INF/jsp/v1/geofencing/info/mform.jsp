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
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js"></script>--%>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.js"></script>
	<script type="text/javascript">
		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();
		});
		GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');

		/**
		 * 기초데이터
		 */
		elementHandler.fence = {
			type:'${geofencing.fcShape}',
			points : new Array()
		}
		<c:forEach items="${latlngList}" var="latlng">
		elementHandler.fence.points.push({
			lat: '${latlng.lat}',
			lng: '${latlng.lng}',
			radius: <c:out value="${latlng.radius}" default="0"/>,
			order: <c:out value="${latlng.orderSeq}" default="0"/>
		});
		</c:forEach>
	</script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/geofencing/geofencing.js"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" name="fcNum" value="${geofencing.fcNum}" />
<input type="hidden" id="shapeData" name="shapeData" value="" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.160103" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.160000" /></li>
		<li class="crumb-trail"><spring:message code="menu.160100" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.fence.type" /></label>
				<div class="col-sm-10">
					<input type="radio" name="fcShape" value="C" required placeholder="<spring:message code="word.fence.type.circle" />" <c:if test="${geofencing.fcShape eq 'C'}">checked</c:if> /> <spring:message code="word.fence.type.circle" />
					<%--<input type="radio" name="fenceType" value="S" placeholder="사각"  /> 사각--%>
					<input type="radio" name="fcShape" value="P" placeholder="<spring:message code="word.fence.type.polygon" />" <c:if test="${geofencing.fcShape eq 'P'}">checked</c:if> /> <spring:message code="word.fence.type.polygon" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.geofencing.name" /></label>
				<div class="col-sm-10">
					<input type="text" id="fcName" name="fcName" value="${geofencing.fcName}" size="30" required minlength="2" maxlength="25" class="form-control" placeholder="<spring:message code="word.geofencing.name" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.floor" /></label>
				<div class="col-sm-10">
					<select class="form-control" name="floor" id="floor-selector" data-default-floor="${geofencing.floor}">
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.geofencing.group.name" /></label>
				<div class="col-sm-10">
					<select class="form-control" name="fcGroupNum">
						<option value="">[<spring:message code="word.geofencing.group.name" />]</option>
						<c:forEach items="${fcGroupList}" var="fcGroupList">
							<option value="${fcGroupList.fcGroupNum}" <c:if test="${fcGroupList.fcGroupNum eq geofencing.fcGroupNum}">selected</c:if>>${fcGroupList.fcGroupName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.geofencing.isNodeEnable" /></label>
				<div class="col-sm-10">
					<input type="radio" name="isNodeEnable" value="Y" required placeholder="<spring:message code="word.geofencing.isNodeEnable.enable" />" <c:if test="${geofencing.isNodeEnable eq 'Y'}">checked</c:if> /> <spring:message code="word.geofencing.isNodeEnable.enable" />
					<%--<input type="radio" name="fenceType" value="S" placeholder="사각"  /> 사각--%>
					<input type="radio" name="isNodeEnable" value="N" required placeholder="<spring:message code="word.geofencing.isNodeEnable.disable" />" <c:if test="${geofencing.isNodeEnable eq 'N'}">checked</c:if> /> <spring:message code="word.geofencing.isNodeEnable.disable" />
				</div>
			</div>

			<div class="form-group form-inline">
				<label class="col-sm-2 control-label"><spring:message code="word.geofencing.field" /> 1~5</label>
				<div class="col-sm-10">
					<input type="text" id="field1" name="field1"  value="${geofencing.field1}" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 1" />
					<input type="text" id="field2" name="field2"  value="${geofencing.field2}" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 2" />
					<input type="text" id="field3" name="field3"  value="${geofencing.field3}" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 3" />
					<input type="text" id="field4" name="field4"  value="${geofencing.field4}" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 4" />
					<input type="text" id="field5" name="field5"  value="${geofencing.field5}" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 5" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.map" /></label>
				<div class="col-sm-10">
					<div id="map-canvas" style="width:100%; height:500px"></div>
				</div>
			</div>
		</div>		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="modBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
				<button id="delBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
				<button id="listBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>