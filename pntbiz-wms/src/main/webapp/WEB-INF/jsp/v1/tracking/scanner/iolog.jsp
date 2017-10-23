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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.170303" /></a></li>
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
						<option value="targetName" ${param.opt == 'targetName' ? 'selected' : ''}><entry key="word.target.name"></entry></option>
						<option value="beaconName" ${param.opt == 'beaconName' ? 'selected' : ''}><entry key="word.beacon.name"></entry></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="presenceLogKeyword" name="keyword" class="form-control" placeholder="<spring:message code="word.search.keyword" />" value="${param.keyword}" />
					</div>
					<button id="presenceLogSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
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
					<th class="hidden-xs"><spring:message code="word.no" /></th>
					<th><spring:message code="word.uuid" /></th>
					<th><spring:message code="word.inout" /></th>
					<th><spring:message code="word.status" /></th>
					<th><spring:message code="word.gate.order" /></th>
					<th><spring:message code="word.beacon.name" /></th>
					<th class="hidden-xs"><spring:message code="word.target.name" /></th>
					<th><spring:message code="word.floor" /></th>
					<th><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<td class="hidden-xs"><span class="label bg-primary">${list.logNum}</span></td>
					<td>${list.UUID}_${list.majorVer}_${list.minorVer}</td>
					<td>${list.io}</td>
					<td>${list.state}</td>
					<td>${list.gate}</td>
					<td>${list.beaconName}</td>
					<td class="hidden-xs">${list.targetName}</td>
					<td>${list.floor}</td>
					<td>
						<jsp:useBean id="regDate" class="java.util.Date"/>
						<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
						<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
					</td>
				</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${listCnt == 0}">
						<tr>
							<td colspan="8" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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