<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/code.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>코드 목록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">코드</a></li>
		  <li class="active">코드 목록</li>
		</ol>
	</span>
</div>
<hr />
<div class="col-sm-9">
	<div class="form-group">
		<select name="opt" id="opt" class="form-control">
			<option value="">=검색옵션=</option>
			<option value="gCD" ${param.opt == 'gCD' ? 'selected' : ''}>그룹코드</option>
			<option value="sCD" ${param.opt == 'sCD' ? 'selected' : ''}>서브코드</option>
			<option value="gName" ${param.opt == 'gName' ? 'selected' : ''}>그룹명</option>
			<option value="sName" ${param.opt == 'sName' ? 'selected' : ''}>서브명</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" id="codeKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
	</div>
	<div class="form-group">
		<button id="codeSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
	</div>
</div>
<div class="col-sm-3 text-right">
	<button id="codeFormBtn" class="btn btn-default btn-sm" type="button">코드 등록</button>
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
		<th>그룹코드</th>
		<th>그룹명</th>
		<th>서브코드</th>
		<th>서브명</th>
	</tr>
	</thead>
	<c:forEach var="list" items="${list}" >
	<tr align="center">
		<td><a href="javascript:code.mform('${list.gCD}', '${list.sCD}')">${list.gCD}</a></td>
		<td><a href="javascript:code.mform('${list.gCD}', '${list.sCD}')">${list.gName}</a></td>
		<td><a href="javascript:code.mform('${list.gCD}', '${list.sCD}')">${list.sCD}</a></td>
		<td><a href="javascript:code.mform('${list.gCD}', '${list.sCD}')">${list.sName}</a></td>
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