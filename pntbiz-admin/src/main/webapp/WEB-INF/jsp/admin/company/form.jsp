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
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/company.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<div class="col-sm-6">
	<h3>업체 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">업체</a></li>
		  <li class="active">업체 등록</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">UUID</label>
	<div class="col-sm-10">
		<input type="text" id="UUID" name="UUID" value="" maxlength="36" class="form-control" placeholder="UUID"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">업체명</label>
	<div class="col-sm-10">
		<input type="text" id="comName" name="comName" value="" maxlength="25" class="form-control" placeholder="업체명"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">사업자번호</label>
	<div class="col-sm-10">
		<input type="text" id="comBizNum" name="comBizNum" value="" maxlength="12"  class="form-control" placeholder="사업자번호" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">허용역할</label>
	<div class="col-sm-10">
		<c:forEach items="${roleList}" var="role">
			<input type="checkbox" value="${role.roleNum}" name="role" /> <c:out value="${role.roleName}" /><br />
		</c:forEach>
	</div>
</div>
<hr />
<div class="center">
	<button id="companyRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="companyListBtn" type="button" class="btn btn-default btn-sm" >리스트</button>
</div>
</form>
</body>
</html>