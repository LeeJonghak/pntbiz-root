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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/service.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/service/attendance.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.140300" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.140000" /></li>
		<li class="crumb-trail"><spring:message code="menu.140300" /></li>
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
				<div class="col-xs-8 col-md-8">				
					<div class="input-group date" id="datetimepicker1">
						<input type="text" id="attdDate" name="attdDate" class="form-control" placeholder="<spring:message code="word.attendance.date"/>" value="${attdDate}" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="sPhoneNumber" ${param.opt == 'sPhoneNumber' ? 'selected' : ''}><spring:message code="word.phonenumber"/></option>
						<option value="sidNum" ${param.opt == 'sidNum' ? 'selected' : ''}><spring:message code="word.attendance.student.id" /></option>
						<option value="subject" ${param.opt == 'subject' ? 'selected' : ''}><spring:message code="word.attendance.subject"/></option>
						<option value="sName" ${param.opt == 'sName' ? 'selected' : ''}><spring:message code="word.name"/></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="attendanceKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="attendanceSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-4 col-md-4 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button type="button" id="resetBtn" class="btn btn-primary btn-sm"><spring:message code="btn.init" /></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr class="active">
					<th><spring:message code="word.attendance.subject" /></th>
					<th><spring:message code="word.attendance.student.id" /></th>
					<th><spring:message code="word.attendance.student.name" /></th>
					<th><spring:message code="word.phonenumber" /></th>
					<th><spring:message code="word.attendance.date" /></th>
					<th><spring:message code="word.attendance.regdate" /></th>
					<!-- <th>출석수정일</th> -->
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >		
				<tr align="center">
					<td>${list.subject}</td>
					<td>${list.sidNum}</td>
					<td>${list.sName}</td>
					<td>${list.sPhoneNumber}</td>
					<td>${list.attdDate}</td>
					<td>
						<jsp:useBean id="regDate" class="java.util.Date"/>
						<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
						<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
					</td>
					<!--  
					<td>
						<jsp:useBean id="modDate" class="java.util.Date"/>
						<jsp:setProperty name="modDate" property="time" value="${list.modDate}000"/>
						<fmt:formatDate value="${modDate}" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
					-->
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