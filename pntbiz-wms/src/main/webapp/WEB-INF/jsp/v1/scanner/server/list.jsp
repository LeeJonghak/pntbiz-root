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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/scanner.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerServer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110301" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110300" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<!--<div class="panel-heading"></div>--> 
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-6 col-md-6">
				</div>
				<div class="col-xs-6 col-md-6 text-right">					
					<button id="scannerServerFormBtn" class="btn btn-primary btn-sm" type="button"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">		
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<th>Host</th>
					<th>Port</th>
					<th>ID</th>
					<th class="hidden-xs"><spring:message code="word.moddate" /></th>
					<th class="hidden-xs"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >		
				<tr>	
					<td><a href="javascript:scannerServer.mform(${list.servNum});">${list.ftpHost}</a></td>
					<td>${list.ftpPort}</td>
					<td>${list.ftpID}</td>
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
							<td colspan="6" height="150" align="center"><spring:message code="message.search.notmatch"/></td>
						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>
		
		<div class="panel-footer clearfix">
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>
