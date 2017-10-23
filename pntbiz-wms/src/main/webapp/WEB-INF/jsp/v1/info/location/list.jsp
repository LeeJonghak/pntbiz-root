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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/info/locationInquiry.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990401" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990400" /></li>
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
				<div class="col-xs-10 col-md-10">
					<div class="input-group date" id="datetimepicker1">
						<label class="sr-only" for="sDate"><spring:message code="word.start.date" /></label>
						<input type="text" id="sDate" name="sDate" value="${sDate}" class="form-control" placeholder="<spring:message code="word.start.date" />" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="input-group date" id="datetimepicker2">
						<label class="sr-only" for="eDate"><spring:message code="word.end.date" /></label>
						<input type="text" id="eDate" name="eDate" value="${eDate}" class="form-control" placeholder="<spring:message code="word.end.date" />" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<select name="os" id="os" class="form-control">
						<option value="">=<spring:message code="word.os"/>=</option>
						<c:forEach var="osCD" items="${osCD}">
							<option value="${osCD.sCD}" <c:if test="${param.os eq osCD.sCD}">selected</c:if>>
								<c:if test="${empty osCD.langCode}">${osCD.sName}</c:if>
								<c:if test="${not empty osCD.langCode}"><spring:message code="${osCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					<select name="service" id="service" class="form-control">
						<option value="">=<spring:message code="word.service"/>=</option>
						<c:forEach var="serviceCD" items="${serviceCD}">
							<option value="${serviceCD.sCD}" <c:if test="${param.service eq serviceCD.sCD}">selected</c:if>>
								<c:if test="${empty serviceCD.langCode}">${serviceCD.sName}</c:if>
								<c:if test="${not empty serviceCD.langCode}"><spring:message code="${serviceCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option"/>=</option>
						<option value="userName" <c:if test="${param.opt eq 'userName'}">selected</c:if>><spring:message code="word.admin.user.name"/></option>
						<option value="mobile" <c:if test="${param.opt eq 'mobile'}">selected</c:if>><spring:message code="word.phonenumber"/>(last 4 digits)</option>
					</select>
					
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="locationInquiryKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="locationInquirySearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
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
					<th><spring:message code="word.serial.number"/></th>
					<th><spring:message code="word.admin.user.name"/></th>
					<th><spring:message code="word.phonenumber"/>(last 4 digits)</th>
					<th><spring:message code="word.os"/></th>
					<th><spring:message code="word.service"/></th>
					<th><spring:message code="word.regdate"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >		
				<tr>
					<td>${list.logNum}</td>
					<td>${list.userName}</td>
					<td>${list.mobile}</td>
					<td>${list.os}</td>
					<td>${list.service}</td>
					<td>
						<jsp:useBean id="regDate" class="java.util.Date"/>
						<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
						<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
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
			<ul class="pagination pull-right">
				${page}
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>