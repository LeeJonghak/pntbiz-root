<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/info/infoAccount.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990100" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990100" /></li>
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
			<div class="form-group code-div">
				<label class="col-sm-2 control-label"><spring:message code="word.admin.user.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="userName" name="userName" value="${loginInfo.userName}" maxlength="50" class="form-control" placeholder="<spring:message code="word.admin.user.name"/>" />
				</div>
			</div>
			<div class="form-group code-div">
				<label class="col-sm-2 control-label"><spring:message code="word.passwd"/></label>
				<div class="col-sm-10">
					<input type="password" id="userPW" name="userPW" value="" maxlength="30" class="form-control" placeholder="<spring:message code="word.passwd"/>" />
					<spring:message code="word.passwd.text001"/>
				</div>
			</div>
			<div class="form-group code-div">
				<label class="col-sm-2 control-label"><spring:message code="word.email"/></label>
				<div class="col-sm-10">
					<input type="text" id="userEmail" name="userEmail" value="${loginInfo.userEmail}" maxlength="100" class="form-control" placeholder="id@domain" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.phonenumber"/></label>
				<div class="col-sm-10">
					<input type="text" id="userPhoneNum" name="userPhoneNum" value="${loginInfo.userPhoneNum}" maxlength="20" class="form-control" placeholder="<spring:message code="word.phonenumber"/>" />
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="infoAccountModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.update"/></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>