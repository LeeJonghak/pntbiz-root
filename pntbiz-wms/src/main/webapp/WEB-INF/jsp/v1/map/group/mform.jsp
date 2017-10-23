<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/map/group.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="groupNum" name="groupNum" value="${groupInfo.groupNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100101" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
	
	
	
    <div class="col-sm-6">
        <h3>그룹 등록 <small></small></h3>
    </div>
    <div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
            <li><a href="#">맵</a></li>
            <li><a href="#">그룹 관리</a></li>
            <li class="active">그룹 등록</li>
        </ol>
	</span>
    </div>
    <div class="clearfix"></div>
    <hr />
    <div id="error-message" class="alert alert-danger hide"></div>
    <div class="form-group">
        <label class="col-sm-2 control-label">그룹명</label>
        <div class="col-sm-10">
            <input type="text" id="groupName" name="groupName" value="${groupInfo.groupName}" size="50" required="required" maxlength="50" class="form-control" placeholder="그룹명"  />
        </div>
    </div>
    <div class="center">
        <button id="modBtn" type="button" class="btn btn-primary btn-sm">수정</button>
        <button id="delBtn" type="button" class="btn btn-default btn-sm">삭제</button>
        <button id="listBtn" type="button" class="btn btn-default btn-sm">리스트</button>
    </div>
    

	</div>
</div>
</section>
</form>
</body>
</html>
