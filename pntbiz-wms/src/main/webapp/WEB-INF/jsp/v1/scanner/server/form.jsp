<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/scanner.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerServer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110302" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110300" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-heading"></div> 
		
		<div class="panel-body">
		
			<div class="form-group">
				<label class="col-sm-2 control-label">Host</label>
				<div class="col-sm-10">
					<input type="text" id="ftpHost" name="ftpHost" value="" maxlength="50" class="form-control" placeholder="localhost"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Port</label>
				<div class="col-sm-10">
					<input type="text" id="ftpPort" name="ftpPort" value="" maxlength="5" class="form-control" placeholder="22"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">ID</label>
				<div class="col-sm-10">
					<input type="text" id="ftpID" name="ftpID" value="" maxlength="30" class="form-control" placeholder="id"  />		
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Passwd</label>
				<div class="col-sm-10">
					<input type="password" id="ftpPW" name="ftpPW" value="" maxlength="30" class="form-control" placeholder="**********" />		
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="scannerServerRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="scannerServerListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>