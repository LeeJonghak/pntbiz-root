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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110101" /> - Sample</a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110100" /></li>
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
						<option value="scannerName" ${param.opt == 'scannerName' ? 'selected' : ''}><spring:message code="word.scanner.name" /></option>
						<option value="macAddr" ${param.opt == 'macAddr' ? 'selected' : ''}><spring:message code="word.mac.address" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="scannerKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="scannerSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-6 col-md-6 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button type="button" id="scannerBFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.batch.update" /></button>
					<button type="button" id="scannerFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>		
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr>
					<th><spring:message code="word.mac.address" /></th>
					<th><spring:message code="word.major.version" /></th>
					<th><spring:message code="word.scanner.name" /></th>
					<th class="hidden-xs"><spring:message code="word.scanner.information" /></th>
					<th class="hidden-xs"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<td><a href="javascript:scanner.mform(${list.scannerNum});">${list.macAddr}</a></td>
					<td><a href="javascript:scanner.mform(${list.scannerNum});">${list.majorVer}</a></td>
					<td><a href="javascript:scanner.mform(${list.scannerNum});">${list.scannerName}</a></td>
					<td class="hidden-xs">
						<c:if test="${list.sid}"><span class="label label-xs bg-info"><spring:message code="word.identifier" /> ${list.sid}</span></c:if>
						<span class="label label-xs bg-danger"><spring:message code="word.lat" /> ${list.lat}</span>
						<span class="label label-xs bg-danger"><spring:message code="word.lng" /> ${list.lng}</span>
						<span class="label label-xs bg-info">rssi ${list.rssi}</span>
						<span class="label label-xs bg-info">srssi ${list.srssi}</span>
						<span class="label label-xs bg-info">mrssi ${list.mrssi}</span>
						<span class="label label-xs bg-info">drssi ${list.drssi}</span>
						<span class="label label-xs bg-info">exMeter ${list.exMeter}</span>
						<span class="label label-xs bg-info">calPoint ${list.calPoint}</span>
						<span class="label label-xs bg-info">maxSig ${list.maxSig}</span>						
						<span class="label label-xs bg-info">maxBuf ${list.maxBuf}</span>
						<span class="label label-xs bg-info">fwVer ${list.fwVer}</span>
						<span class="label label-xs bg-alert"><spring:message code="word.floor" /> ${list.floor}</span>		
					</td>
					<td class="hidden-xs">
					<jsp:useBean id="regDate" class="java.util.Date"/>
					<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
					<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
					</td>				
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