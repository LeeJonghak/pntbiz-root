\<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/contents.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contentsMapping.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.130200" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.130000" /></li>
		<li class="crumb-trail"><spring:message code="menu.130200" /></li>
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
				<div class="col-xs-9 col-md-9">
					<select name="conType" id="conType" class="form-control">
						<option value="">=<spring:message code="word.contents.type" />=</option>
						<c:forEach var="conCD" items="${conCD}">
							<option value="${conCD.sCD}" <c:if test="${param.conType eq conCD.sCD}">selected</c:if>>
								<c:if test="${empty conCD.langCode}">${conCD.sName}</c:if>
								<c:if test="${not empty conCD.langCode}"><spring:message code="${conCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>					
					<select name="refType" id="refType" class="form-control">
						<option value="">=<spring:message code="word.mapping.type" />=</option>
						<c:forEach var="refCD" items="${refCD}">
							<option value="${refCD.sCD}" <c:if test="${param.refType eq refCD.sCD}">selected</c:if>>
								<c:if test="${empty refCD.langCode}">${refCD.sName}</c:if>
								<c:if test="${not empty refCD.langCode}"><spring:message code="${refCD.langCode}"/></c:if>
							</option>
						</c:forEach>
						<c:forEach var="refCD2" items="${refCD2}">
							<option value="${refCD2.sCD}" <c:if test="${param.refType eq refCD2.sCD}">selected</c:if>>
								<c:if test="${empty refCD2.langCode}">${refCD2.sName}</c:if>
								<c:if test="${not empty refCD2.langCode}"><spring:message code="${refCD2.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>					
					<select name="refSubType" id="refSubType" class="form-control">
						<option value="">=<spring:message code="word.sub.mapping.type" />=</option>
						<c:forEach var="refSubCD" items="${refSubCD}">
							<option value="${refSubCD.sCD}" <c:if test="${param.refSubType eq refSubCD.sCD}">selected</c:if>>
								<c:if test="${empty refSubCD.langCode}">${refSubCD.sName}</c:if>
								<c:if test="${not empty refSubCD.langCode}"><spring:message code="${refSubCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>			
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="conName" <c:if test="${param.opt eq 'conName'}">selected</c:if>><spring:message code="word.contents.name" /></option>
						<option value="evtName" <c:if test="${param.opt eq 'evtName'}">selected</c:if>><spring:message code="word.event.name" /></option>
					</select>					
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="contentsMappingKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="contentsMappingSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-3 col-md-3 text-right">
						<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
						<button type="button" id="evnetMappingDelBtn" class="btn btn-info btn-sm"><spring:message code="word.event.delete" /></button>
						<button type="button" id="contentsMappingDelBtn" class="btn btn-primary btn-sm"><spring:message code="word.mapping.delete" /></button>
				</div>
			</div>
		</div>	
	
		<div class="panel-body pn">
			<table class="table table-striped table-hover">
				<thead>
				<tr>
					<th class="text-center" width="50px"><input type="checkbox" value="" id="checkAll"/></th>
					<th onclick="sortColumn(event)"><spring:message code="word.contents"/> <spring:message code="word.no"/></th>
					<th onclick="sortColumn(event)"><spring:message code="word.mapping.type" /></th>
					<th onclick="sortColumn(event)"><spring:message code="word.reference" /> <spring:message code="word.no"/></th>
					<th onclick="sortColumn(event)"><spring:message code="word.sub.mapping.type" /></th>
	                <c:if test="${not empty param.refType}">
					<th onclick="sortColumn(event)"><spring:message code="word.reference.information"/></th>
	                </c:if>
					<th onclick="sortColumn(event)"><spring:message code="word.contents.type" /></th>
					<th onclick="sortColumn(event)"><spring:message code="word.contents.name" /></th>
					<th onclick="sortColumn(event)"><spring:message code="word.event.name" /></th>
					<th onclick="sortColumn(event)" class="hidden-xs"><spring:message code="word.register" /></th>
					<th onclick="sortColumn(event)"class="hidden-xs"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr>
						<td class="text-center"><input type="checkbox" value="${list.conNum}|${list.refType}|${list.refNum}|${list.refSubTypeText}" id="checkbox" name="checkbox" /></td>
						<td><a href="/contents/mform.do?conNum=${list.conNum}"><span class="label bg-primary">${list.conNum}</span></a></td>						
						<td><spring:message code="${list.refTypeLangCode}"/></td>
						<td>
							<c:choose>
						        <c:when test="${list.refType eq 'BC'}">
						            <a href="/beacon/info/mform.do?beaconNum=${list.refNum}"><span class="label bg-danger">${list.refNum}</span></a>
						        </c:when>
						        <c:when test="${list.refType eq 'GF'}">
						            <a href="/geofencing/info/mform.do?fcNum=${list.refNum}"><span class="label bg-warning">${list.refNum}</span></a>
						        </c:when>
						        <c:when test="${list.refType eq 'BCG'}">
						            <a href="/beacon/group/mform.do?beaconGroupNum=${list.refNum}"><span class="label bg-alert">${list.refNum}</span></a>
						        </c:when>
						        <c:when test="${list.refType eq 'GFG'}">
						            <a href="/geofencing/group/mform.do?fcGroupNum=${list.refNum}"><span class="label bg-system">${list.refNum}</span></a>
						        </c:when>
						        <c:otherwise>
						        	<span class="label bg-info">${list.refNum}</span>
						        </c:otherwise>
						    </c:choose>	
						</td>
						<td>${list.refSubTypeText}</td>
	                    <c:if test="${not empty param.refType}">
						<td>${list.refName}</td>
	                    </c:if>
						<td><spring:message code="${list.conTypeLangCode}"/></td>
						<td>${list.conName}</td>
						<td>${list.evtName}</td>
						<td class="hidden-xs">${list.userID}</td>
						<td class="hidden-xs">${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="<c:if test="${not empty param.refType}">9</c:if><c:if test="${empty param.refType}">10</c:if>" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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