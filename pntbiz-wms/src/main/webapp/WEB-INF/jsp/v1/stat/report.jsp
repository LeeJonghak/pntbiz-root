<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>리포트 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">통계</a></li>
		  <li class="active">리포트</li>
		</ol>
	</span>
</div>
<div class="clearfix"></div>
<hr />
</form>
</body>
</html>