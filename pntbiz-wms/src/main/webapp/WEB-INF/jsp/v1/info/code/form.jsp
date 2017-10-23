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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/info/codeAction.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990202" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990200" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.code.type" /></label>
				<div class="col-sm-10">
					<!-- <input type="radio" name="codeType" value="T" placeholder="원" checked /> Task -->		
					<select name="codeType" id="codeType" class="form-control">
						<!--<option value="">=코드구분=</option>-->
						<c:forEach var="codeActCD" items="${codeActCD}">
							<option value="${codeActCD.sCD}" <c:if test="${param.codeType eq codeActCD.sCD}">selected</c:if>>
								<c:if test="${empty codeActCD.langCode}">${codeActCD.sName}</c:if>
								<c:if test="${not empty codeActCD.langCode}"><spring:message code="${codeActCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.code" /></label>
				<div class="col-sm-10">
					<input type="text" id="code" name="code" value="" maxlength="36" class="form-control" placeholder="<spring:message code="word.code" />"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.code.name" /></label>
				<div class="col-sm-10">
					<input type="text" id="codeName" name="codeName" value="" maxlength="25" class="form-control" placeholder="<spring:message code="word.code.name" />"  />
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="codeActionRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register"/></button>
				<button id="codeActionListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list"/></button>
			</div>
		</div>
		
	</div>
</div>
</section>
</form>
</body>
</html>