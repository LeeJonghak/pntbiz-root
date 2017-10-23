<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
	<link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/css/contents.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contentsInfo.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.130101" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.130000" /></li>
		<li class="crumb-trail"><spring:message code="menu.130100" /></li>
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
				<div class="col-xs-5 col-md-5">
					<select name="conType" id="conType" class="form-control">
						<option value="">=<spring:message code="word.contents.type" />=</option>
						<c:forEach var="conCD" items="${conCD}">
							<option value="${conCD.sCD}" <c:if test="${param.conType eq conCD.sCD}">selected</c:if>>
								<c:if test="${empty conCD.langCode}">${conCD.sName}</c:if>
								<c:if test="${not empty conCD.langCode}"><spring:message code="${conCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="conName" ${param.opt == 'conName' ? 'selected' : ''}><spring:message code="word.contents.name" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="contentsKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="contentsSearchBtn"  class="btn btn-default btn-sm inline-block"><spring:message code="btn.search" /></button>
				</div>
				
				<div class="col-xs-5 col-md-5">
					<select name="selectBoxMapping" id="selectBoxMapping" class="form-control">
						<option value="">=<spring:message code="word.mapping.type" />=</option>
						<option value="BC"><spring:message code="word.beacon" /></option>
						<option value="BCG"><spring:message code="word.beacon.group" /></option>
						<option value="GF"><spring:message code="word.geofencing" /></option>
						<option value="GFG"><spring:message code="word.geofencing.group" /></option>
					</select>
						
					<select id="fcEventType" class="form-control" style="display:none;">
						<option value="">=<spring:message code="word.sub.mapping.type" />=</option>
						<option value="ENTER">ENTER</option>
						<option value="STAY">STAY</option>
						<option value="LEAVE">LEAVE</option>
					</select>
					
					<select id="multiselect1" class="form-control">
                       <option value="0" selected><spring:message code="word.event.no" /></option>
                       <optgroup label='Event List'>
                       	<c:forEach var="eventList" items="${eventList}">
							<option value="${eventList.evtNum}"><spring:message code="${eventList.evtName}"/></option>
						</c:forEach>
                       </optgroup>
                    </select>

 					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><i class="imoon imoon-link"></i></span>
							<div class="multi-select">
								<select id="multiselect4" multiple="multiple"></select>
							</div>    
						</div>
						<button type="button" id="contentsMappingBtn"  class="btn btn-info btn-sm"><spring:message code="btn.mapping" /></button>
					</div>
				</div>
				
				<div class="col-xs-2 col-md-2 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button type="button" id="contentsFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>	
	
		<div class="panel-body pn">
			<table class="table table-hover">
				<thead>
				<tr>
					<th class="text-center" width="50px"><input type="checkbox" value="" id="checkAll"/></th>
					<th onclick="sortColumn(event)"><spring:message code="word.no" /></th>
					<th onclick="sortColumn(event)"><spring:message code="word.contents.type" /></th>
					<th onclick="sortColumn(event)"><spring:message code="word.contents.name" /></th>
					<th onclick="sortColumn(event)" class="hidden-xs" ><spring:message code="word.advertising.provider" /></th>
					<th onclick="sortColumn(event)" class="hidden-xs" ><spring:message code="word.register" /></th>
					<th onclick="sortColumn(event)" class="hidden-xs" > <spring:message code="word.validity.date" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr>
						<td class="text-center"><input type="checkbox" value="${list.conNum}" id="checkbox" name="checkbox" /></td>
						<td><span class="label bg-primary">${list.conNum}</span></td>
						<td>
							<c:if test="${empty list.langCode}">${list.conTypeText}</c:if>
							<c:if test="${not empty list.langCode}"><spring:message code="${list.langCode}"/></c:if>
						</td>
						<td>
							<a href="<c:url value="mform.do?conNum=${list.conNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&conType=${param.conType}"/>">${list.conName}</a>
						</td>
						<td class="hidden-xs"><c:out value="${list.acName}" default="-"/></td>
						<td class="hidden-xs"><c:out value="${list.userID}" default="-"/></td>
						<td class="hidden-xs">${list.sDateText}<br/>${list.eDateText}</td>
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