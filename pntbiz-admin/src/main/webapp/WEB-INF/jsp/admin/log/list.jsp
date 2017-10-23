<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/adminLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>관리자 로그 목록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">로그</a></li>
		  <li class="active">관리자  로그</li>
		</ol>
	</span>
</div>
<hr />
<div class="col-sm-9">
	<div class="form-group">
		<select name="crudType" id="crudType" class="form-control">
			<option value="">=CRUD=</option>
			<option value="C" ${param.crudType == 'C' ? 'selected' : ''}>Create</option>
			<option value="R" ${param.crudType == 'R' ? 'selected' : ''}>Read</option>
			<option value="U" ${param.crudType == 'U' ? 'selected' : ''}>Update</option>
			<option value="D" ${param.crudType == 'D' ? 'selected' : ''}>Delete</option>
		</select>
	</div>
	<div class="form-group">
		<select name="opt" id="opt" class="form-control">
			<option value="">=검색옵션=</option>
			<option value="adminID" ${param.opt == 'adminID' ? 'selected' : ''}>아이디</option>
			<option value="tableName" ${param.opt == 'tableName' ? 'selected' : ''}>테이블명</option>
			<option value="key" ${param.opt == 'key' ? 'selected' : ''}>키</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" id="adminLogKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
	</div>
	<div class="form-group">
		<button id="adminLogSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
	</div>
</div>
<div class="col-sm-3 text-right">
</div>
<div class="clearfix"></div>
<div class="bh20"></div>
<div class="col-sm-9">
	<div id="error-opt" class="pull-left"></div>
	<div id="error-companyKeyword" class="pull-left"></div>
</div>
<div class="col-sm-3 text-right">
	<p class="text-info">총 카운트 : ${cnt} 건
</div>
<div class="clearfix"></div>
<div class="bh10"></div>
<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">
	<thead>
	<tr class="active">
		<th>로그일련번호</th>
		<th>로그인 아이디</th>
		<th>테이블명</th>
		<th>CRUD</th>
		<th>키</th>
		<th>등록시간</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="list" items="${list}" >
	<tr align="center">
		<td>${list.logNum}</td>
		<td>${list.adminID}</td>
		<td>${list.tableName}</td>
		<td>${list.crudType}</td>
		<td>${list.pk}</td>
		<td>
			<jsp:useBean id="regDate" class="java.util.Date"/>
			<jsp:setProperty name="regDate" property="time" value="${list.regDate*1000}"/>
			<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</tr>
	</c:forEach>
	<c:choose>
		<c:when test="${cnt == 0}">
			<tr>
				<td colspan="6" height="150" align="center">검색 결과가 없습니다.</td>
			</tr>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	</tbody>
</table>
<div class="center">
	<ul class="pagination">
		${page}
	</ul>
</div>
</form>
</body>
</html>