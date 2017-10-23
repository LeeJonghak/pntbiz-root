<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/info/codeAction.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990201" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990200" /></li>
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
					<select name="codeType" id="codeType" class="form-control">
						<option value="">=<spring:message code="word.code.type"/>=</option>
						<c:forEach var="codeActCD" items="${codeActCD}">
							<option value="${codeActCD.sCD}" <c:if test="${param.codeType eq codeActCD.sCD}">selected</c:if>>
								<c:if test="${empty codeActCD.langCode}">${codeActCD.sName}</c:if>
								<c:if test="${not empty codeActCD.langCode}"><spring:message code="${codeActCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					<select name="codeType" id="codeType" class="form-control">
						<option value="">=<spring:message code="word.code.type"/>=</option>
						<c:forEach var="codeActCD" items="${codeActCD}">
							<option value="${codeActCD.sCD}" <c:if test="${param.codeType eq codeActCD.sCD}">selected</c:if>>
								<c:if test="${empty codeActCD.langCode}">${codeActCD.sName}</c:if>
								<c:if test="${not empty codeActCD.langCode}"><spring:message code="${codeActCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option"/>=</option>
						<option value="code" <c:if test="${param.opt eq 'code'}">selected</c:if>><spring:message code="word.code"/></option>
						<option value="codeName" <c:if test="${param.opt eq 'codeName'}">selected</c:if>><spring:message code="word.code.name"/></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="codeActionKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="codeActionSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-2 col-md-2 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button type="button" id="codeActionFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">	
				<thead>
				<tr>
					<th class="hidden-xs"><spring:message code="word.no"/></th>
					<th><spring:message code="word.code.type"/></th>
					<th><spring:message code="word.code"/></th>
					<th><spring:message code="word.code.name"/></th>
					<th class="hidden-xs"><spring:message code="word.regdate"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr>
						<td class="hidden-xs"><span class="label bg-primary">${list.codeNum}</span></td>
						<td>${list.codeTypeText}</td>
						<td>
							<a href="<c:url value="mform.do?codeNum=${list.codeNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&codeType=${param.codeType}"/>">${list.code}</a>
						</td>
						<td>
							<a href="<c:url value="mform.do?codeNum=${list.codeNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&codeType=${param.codeType}"/>">${list.codeName}</a>
						</td>
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