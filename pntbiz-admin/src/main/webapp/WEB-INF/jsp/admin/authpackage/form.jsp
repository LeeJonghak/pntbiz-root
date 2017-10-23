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
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="comNum" name="comNum" value="${comNum}" />
<div class="col-sm-6">
	<h3>인증 패키지 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">인증</a></li>
		  <li class="active">인증 패키지</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">패키지 명</label>
	<div class="col-sm-10">
		<input type="text" id="packageName" name="packageName" value="" maxlength="50" class="form-control" placeholder="com.pntbiz.beacon"  />
	</div>
</div>
<hr />
<div class="center">
	<button id="authpackageRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="authpackageListBtn" type="button" class="btn btn-default btn-sm">리스트</button>
</div>
</form>
</body>
</html>