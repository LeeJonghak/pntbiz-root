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
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/authpackage.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>인증 패키지 목록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">인증</a></li>
		  <li class="active">인증 패키지 목록</li>
		</ol>
	</span>
</div>
<hr />
<div class="col-sm-9">
	<div class="form-group">
		<select name="opt" id="opt" class="form-control">
			<option value="">=검색옵션=</option>
			<option value="packageName" ${param.opt == 'packageName' ? 'selected' : ''}>패키지명</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" id="authpackageKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
	</div>
	<div class="form-group">
		<button id="authpackageSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
	</div>	
</div>
<div class="col-sm-3 text-right">
	<button id="authpackageFormBtn" class="btn btn-default btn-sm" type="button">패키지 등록</button>
</div>   
<div class="clearfix"></div>
<div class="bh20"></div>
<div class="col-sm-9">
	<div id="error-opt" class="pull-left"></div>
	<div id="error-keyword" class="pull-left"></div>
</div>
<div class="col-sm-3 text-right">
	<p class="text-info">총 카운트 : ${cnt}</p>
</div>
<div class="clearfix"></div>
<div class="bh10"></div>
<div class="table-responsive">
<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">	
	<thead>
	<tr class="active">
		<th>패키지명</th>	
	</tr>
	</thead>
	<tbody>
	<c:forEach var="list" items="${list}" >		
	<tr>
		<td><a href="javascript:authpackage.mform('${list.packageName}')">${list.packageName}</a></td>
	</tr>
	</c:forEach>
	<c:choose>
		<c:when test="${cnt == 0}">
			<tr>
				<td height="150" align="center">검색 결과가 없습니다.</td>
			</tr>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	</tbody>
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