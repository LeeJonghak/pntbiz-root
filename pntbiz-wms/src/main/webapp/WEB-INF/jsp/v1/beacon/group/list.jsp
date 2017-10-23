<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beaconGroup.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100301" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100300" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.group.list"></span>
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
						<option value="beaconGroupName" ${param.opt == 'beaconGroupName' ? 'selected' : ''}><spring:message code="word.beacon.group.name" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="beaconGroupSearchKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="beaconGroupSearchBtn" class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-6 col-md-6 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button id="beaconGroupFormBtn" class="btn btn-primary btn-sm" type="button"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>	
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<th><spring:message code="word.beacon.group.name" /></th>
					<th><spring:message code="word.beacon.count" /></th>
					<th><spring:message code="word.moddate" /></th>
					<th><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>				
				<c:forEach var="list" items="${list}">
				<tr>
					<td>
						<a href="/beacon/group/mform.do?beaconGroupNum=${list.beaconGroupNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}">${list.beaconGroupName}</a>
					</td>
					<td class="hidden-xs"><c:out value="${list.beaconCount}" default="0"/></td>
					<td class="hidden-xs">
						<span class="pnt-timestamp" data-timestamp="${list.modDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
					<td class="hidden-xs">
						<span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
				</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="6" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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
