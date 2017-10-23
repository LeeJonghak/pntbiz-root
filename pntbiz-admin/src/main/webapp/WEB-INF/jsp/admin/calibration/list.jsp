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
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/calibration.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>캘리브레이션 목록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">캘리브레이션</a></li>
		  <li class="active">캘리브레이션 목록</li>
		</ol>
	</span>
</div>
<hr />
<div class="col-sm-9">
	<div class="form-group">
		<select name="maker" id="maker" class="form-control">
			<option value="">=제조사=</option>
			<c:forEach var="makerCD" items="${makerCD}">
				<option value="${makerCD.sCD}" <c:if test="${param.maker eq makerCD.sCD}">selected</c:if>>${makerCD.sName}</option>
			</c:forEach>
		</select>
	</div>
	<div class="form-group">
		<select name="telecom" id="telecom" class="form-control">
			<option value="">=통신사=</option>
			<c:forEach var="telecomCD" items="${telecomCD}">
				<option value="${telecomCD.sCD}" <c:if test="${param.telecom eq telecomCD.sCD}">selected</c:if>>${telecomCD.sName}</option>
			</c:forEach>
		</select>
	</div>
	<div class="form-group">
		<select name="opt" id="opt" class="form-control">
			<option value="">=검색옵션=</option>
			<option value="modelName" ${param.opt == 'modelName' ? 'selected' : ''}>모델명</option>
			<option value="deviceName" ${param.opt == 'deviceName' ? 'selected' : ''}>장치명</option>
			<option value="os" ${param.opt == 'os' ? 'selected' : ''}>OS</option>
			<option value="rssi" ${param.opt == 'rssi' ? 'selected' : ''}>RSSI</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" id="calibrationKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
	</div>
	<div class="form-group">
		<button id="calibrationSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
	</div>
</div>
<div class="col-sm-3 text-right">
	<button id="calibrationFormBtn" class="btn btn-default btn-sm" type="button">캘리브레이션 등록</button>
</div>
<div class="clearfix"></div>
<div class="bh20"></div>
<div class="col-sm-9">
	<div id="error-opt" class="pull-left"></div>
	<div id="error-companyKeyword" class="pull-left"></div>
</div>
<div class="col-sm-3 text-right">
	<p class="text-info">총 카운트 : ${cnt} 건</p>
</div>
<div class="clearfix"></div>
<div class="bh10"></div>
<div class="table-responsive">

<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">
	<thead>
	<tr class="active">
		<th>일련번호</th>
		<th>제조사</th>
		<th>통신사</th>
		<th>모델명</th>
		<th>기기명</th>
		<th>OS/버전</th>
		<th>보정RSSI</th>
		<th>등록일</th>
	</tr>
	</thead>
	<c:forEach var="list" items="${list}" >
	<tr align="center">
		<td>${list.calNum}</td>
		<td>${list.maker}</td>
		<td>${list.telecom}</td>
		<td><a href="javascript:calibration.mform('${list.calNum}')">${list.modelName}</a></td>
		<td>${list.deviceName}</td>
		<td>${list.os}</td>
		<td>${list.rssi}</td>
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
				<td colspan="12" height="150" align="center">검색 결과가 없습니다.</td>
			</tr>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
</table>
</div>
<div class="center">
	<ul class="pagination">
	    ${page}
	</ul>
</div>
</form>
</body>
</html>