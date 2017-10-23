<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
	<link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
	<script type="text/javascript"
			src="${viewProperty.staticUrl}/js/beacon/beaconState.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100201" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100200" /></li>
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
		
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-10 col-md-10">
					<select name="state" id="state" class="form-control">
						<option value="">==<spring:message code="word.state" />==</option>
						<option value="0" <c:if test="${param.state eq '0'}">selected</c:if>><spring:message code="word.unconfirmed" /></option>
						<option value="1" <c:if test="${param.state eq '1'}">selected</c:if>><spring:message code="word.normal" /></option>
						<option value="2" <c:if test="${param.state eq '2'}">selected</c:if>><spring:message code="word.loss" /></option>
					</select>
					<select name="field" id="field" class="form-control">
						<option value="">==<spring:message code="word.sort.field" />==</option>
						<option value="lastDate" <c:if test="${param.field eq 'lastDate'}">selected</c:if>><spring:message code="word.last.date" /></option>
						<option value="battery" <c:if test="${param.field eq 'battery'}">selected</c:if>><spring:message code="word.battery" /></option>
					</select>
					<select name="sort" id="sort" class="form-control">
						<option value="">==<spring:message code="word.sort.order" />==</option>
						<option value="desc" <c:if test="${param.sort eq 'desc'}">selected</c:if>><spring:message code="word.sort.descending" /></option>
						<option value="asc" <c:if test="${param.sort eq 'asc'}">selected</c:if>><spring:message code="word.sort.ascending" /></option>
					</select>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="beaconName" ${param.opt == 'beaconName' ? 'selected' : ''}><spring:message code="word.beacon.name" /></option>
						<option value="UUID" ${param.opt == 'UUID' ? 'selected' : ''}><spring:message code="word.uuid" /></option>
						<option value="majorVer" ${param.opt == 'majorVer' ? 'selected' : ''}><spring:message code="word.major.version" /></option>
						<option value="minorVer" ${param.opt == 'minorVer' ? 'selected' : ''}><spring:message code="word.minor.version" /></option>
						<option value="txPower" ${param.opt == 'txPower' ? 'selected' : ''}><spring:message code="word.txpower" /></option>
						<option value="floor" ${param.opt == 'floor' ? 'selected' : ''}><spring:message code="word.floor" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="beaconStateKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="beaconStateSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-2 col-md-2 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
				</div>
			</div>
		</div>	
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<th><spring:message code="word.no" /></th>
					<th><spring:message code="word.uuid" /></th>
					<th><spring:message code="word.beacon.name" /></th>
					<th><spring:message code="word.battery.level" /></th>
					<th><spring:message code="word.txpower" /></th>
					<th><spring:message code="word.last.date" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr>
						<td>${list.beaconNum}</td>
						<td>
							<a href="<c:url value="/beacon/info/mform.do?beaconNum=${list.beaconNum}&list=beaconState&page=${page}&opt=${param.opt}&keyword=${param.keyword}&field=${param.field}&sort=${param.sort}"/>">${list.UUID}_${list.majorVer}_${list.minorVer}</a>
						</td>
						<td>
							<a href="<c:url value="/beacon/info/mform.do?beaconNum=${list.beaconNum}&list=beaconState&page=${page}&opt=${param.opt}&keyword=${param.keyword}&field=${param.field}&sort=${param.sort}"/>">${list.beaconName}</a>
						</td>
						</td>
						<td><c:out value="${list.battery}" default="-"/></td>
						<td><c:out value="${list.txPower}" default="-"/></td>
						<td>
							<span class="pnt-timestamp" data-timestamp="${list.lastDate}" data-format="YYYY-MM-DD HH:mm:ss" />
						</td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="7" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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
