<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/tracking.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceBeaconLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.170311" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.170000" /></li>
		<li class="crumb-trail"><spring:message code="menu.170300" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<c:set var="listCnt" value="${fn:length(list)}"/>
<div class="row col-md-12">
	<div class="panel">

		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-10 col-md-10">
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="phoneNumber" ${param.opt == 'phoneNumber' ? 'selected' : ''}><spring:message code="word.phone.number" /></option>
						<option value="deviceInfo" ${param.opt == 'deviceInfo' ? 'selected' : ''}><spring:message code="word.device.info" /></option>
						<option value="fcNum" ${param.opt == 'fcNum' ? 'selected' : ''}><spring:message code="word.geofencing.name" /> <spring:message code="word.no" /></option>
						<option value="fcName" ${param.opt == 'fcName' ? 'selected' : ''}><spring:message code="word.geofencing.name" /></option>
						<option value="nodeID" ${param.opt == 'nodeID' ? 'selected' : ''}><spring:message code="word.node.id" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="presenceBeaconLogKeyword" name="keyword" class="form-control" placeholder="<spring:message code="word.search.keyword" />" value="${param.keyword}" />
					</div>
					<button id="presenceBeaconLogSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
				</div>
				<div class="col-xs-2 col-md-2 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${listCnt}</span>
				</div>
			</div>
		</div>

		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<%-- <th class="hidden-xs"><spring:message code="word.no" /></th> --%>
					<th><spring:message code="word.phone.number" /></th>
					<th><spring:message code="word.device.info" /></th>
					<th><spring:message code="word.lat.lng" /></th>
					<th class="hidden-xs"><spring:message code="word.geofencing.name" /> <spring:message code="word.no" /></th>
					<th><spring:message code="word.geofencing.name" /></th>
					<th><spring:message code="word.floor" /></th>
					<%-- <th class="hidden-xs"><spring:message code="word.node.id" /></th>
					<th><spring:message code="word.regdate" /></th> --%>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<%-- <td class="hidden-xs"><span class="label bg-primary">${list.logNum}</span></td> --%>
					<td>${list[8]}</td>
					<td>${list[2]}</td>
					<td>${list[6]} / ${list[7]}</td>
					<td class="hidden-xs"><span class="label bg-danger">${list[4]}</span></td>
					<td>${list[3]}</td>
					<td>${list[5]}</td>
					<%-- <td class="hidden-xs">${list.nodeID}</td>
					<td>
						<jsp:useBean id="regDate" class="java.util.Date"/>
						<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
						<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
					</td> --%>
				</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${listCnt == 0}">
						<tr>
							<td colspan="9" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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