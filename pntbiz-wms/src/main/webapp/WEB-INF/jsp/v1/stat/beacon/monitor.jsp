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
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100202" /></a></li>
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
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-12 col-md-12"></div>
			</div>
		</div>	
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">	
				<thead>
				<tr>
					<th width="40%"><spring:message code="word.stat.beacon.monitor.text001" /></th>
					<td>${totalCnt} <spring:message code="word.beacon.unit" /></td>
				</tr>
				<tr>
					<th><spring:message code="word.stat.beacon.monitor.text002" /></th>
					<td>${preCnt} <spring:message code="word.beacon.unit" /></td>
				</tr>	
				<tr>
					<th><spring:message code="word.stat.beacon.monitor.text003" /></th>
					<td>${lossCnt} <spring:message code="word.beacon.unit" /></td>
				</tr>
				<tr>
					<th><spring:message code="word.stat.beacon.monitor.text004" /></th>
					<td><spring:message code="word.stat.beacon.monitor.text005" arguments="${lastDate}" /></td>
				</tr>	
				</thead>
			</table>
			<table class="table table-striped table-hover" onclick="sortColumn(event)">	
				<thead>
				<tr>
					<th class="hidden-xs"><spring:message code="word.no" /></th>
					<th class="hidden-xs"><spring:message code="word.uuid" /></th>
					<th><spring:message code="word.beacon.name" /></th>
					<th type="Number"><spring:message code="word.battery.level" /> %</th>
					<th><spring:message code="word.recent.responsedate" /></th>
					<th class="hidden-xs"><spring:message code="word.description" /></th>
					<th><spring:message code="word.location.check" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >		
				<tr>
					<td class="hidden-xs"><span class="label bg-primary">${list.beaconNum}</span></td>
					<td class="hidden-xs">${list.UUID}_${list.majorVer}_${list.minorVer}</td>
					<td>${list.beaconName}</td>
					<td>${list.battery}</td>
					<td>
						<span class="pnt-timestamp" data-timestamp="${list.lastDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
					<td class="hidden-xs">${list.beaconDesc}</td>
					<td><a href="<c:url value="/map/bsplan.do"/>#${list.floor}/${list.beaconNum}" class="btn btn-info btn-sm"><spring:message code="btn.check" /></a></td>
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

	</div>
</div>
</section>
</form>
</body>
</html>
