<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
	<link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
	<link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
	<script type="text/javascript"
			src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript"
			src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready(function () {
			elementHandler.init();
		});
		GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>', '<sec:authentication property="principal.lng" htmlEscape="false"/>');
	</script>
	<script type="text/javascript"
			src="${viewProperty.staticUrl}/js/geofencing/geofencing.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.160101" /></a></li>
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
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-6 col-md-6">
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="fcName" <c:if test="${param.opt eq 'fcName'}">selected</c:if>><spring:message code="word.geofencing.name" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="keyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}"/>
					</div>
					<button type="button" id="searchBtn" class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-6 col-md-6 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button id="syncBtn" class="btn btn-primary btn-sm" type="button"><spring:message code="btn.synchronization" /></button>
					<button id="formBtn" class="btn btn-primary btn-sm" type="button"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>

		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<th><spring:message code="word.geofencing.name" /></th>
					<th><spring:message code="word.geofencing.group.name" /></th>
					<th><spring:message code="word.geofencing.type" /></th>
					<th><spring:message code="word.geofencing.isNodeEnable" /></th>
					<th><spring:message code="word.register" /></th>
					<th class="hidden-xs"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr>
						<td>
							<a href="<c:url value="/geofencing/info/mform.do?fcNum=${list.fcNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}"/>">${list.fcName}</a>
						</td>
						<td>${list.fcGroupName}</td>
						<td>
							<c:if test="${list.fcType eq 'G'}"><spring:message code="word.general" /></c:if>
							<c:if test="${list.fcType eq 'S'}"><spring:message code="word.system" /></c:if>
						</td>
						<td>
							<c:if test="${list.isNodeEnable eq 'Y'}"><spring:message code="word.geofencing.isNodeEnable.enable" /></c:if>
							<c:if test="${list.isNodeEnable eq 'N'}"><spring:message code="word.geofencing.isNodeEnable.disable" /></c:if>
						</td>
						<td><c:out value="${list.userID}" default="-"/></td>
						<td class="hidden-xs">${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="5" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>

		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				${pagination}
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>