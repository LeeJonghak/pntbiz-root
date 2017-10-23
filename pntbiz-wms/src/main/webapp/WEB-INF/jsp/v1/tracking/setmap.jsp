<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/tracking.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/setmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.120600" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.120000" /></li>
		<li class="crumb-trail"><spring:message code="menu.120600" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.init" /> <spring:message code="word.lat.lng" /></label>
				<div class="col-sm-10">
					${companyInfo.lat} / ${companyInfo.lng}
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.init" /> <spring:message code="word.zoom.level" /></label>
				<div class="col-sm-10">
					<input type="text" id="initZoom" name="initZoom" value="${presenceSetmapInfo.initZoom}" maxlength="3" class="form-control" placeholder="1~20"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.init" /> <spring:message code="word.floor" /></label>
				<div class="col-sm-10">
					<select id="initFloor" name="initFloor" class="form-control">
						<option value="">==<spring:message code="word.floor" />==</option>
						<c:forEach var="floorList" items="${floorList}">
							<option value="${floorList.floor}" <c:if test="${presenceSetmapInfo.initFloor eq floorList.floor}">selected</c:if>><c:out value="${floorList.floorName}"/></option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon" /> <spring:message code="word.check.interval" /></label>
				<div class="col-sm-10">
					<input type="text" id="checkTimeInterval" name="checkTimeInterval" value="${presenceSetmapInfo.checkTimeInterval}" maxlength="10" class="form-control" placeholder="1~20"  />
					* <entry key="word.tracking.setmap.text001">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon" /> <spring:message code="word.remove.interval" /></label>
				<div class="col-sm-10">
					<input type="text" id="removeTimeInterval" name="removeTimeInterval" value="${presenceSetmapInfo.removeTimeInterval}" maxlength="10" class="form-control" placeholder="1~20"  />
					* <entry key="word.tracking.setmap.text002">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon" /> <spring:message code="word.move.interval" /></label>
				<div class="col-sm-10">
					<input type="text" id="moveTimeInterval" name="moveTimeInterval" value="${presenceSetmapInfo.moveTimeInterval}" maxlength="10" class="form-control" placeholder="1~20"  />
					* <entry key="word.tracking.setmap.text003">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.beacon" /> <spring:message code="word.move.unit" /></label>
				<div class="col-sm-10">
					<input type="text" id="moveUnit" name="moveUnit" value="${presenceSetmapInfo.moveUnit}" maxlength="3" class="form-control" placeholder="1~20"  />
					* <spring:message code="word.lat.lng" /> <spring:message code="word.unit" />
				</div>
			</div>
		</div>

		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="setmapModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>