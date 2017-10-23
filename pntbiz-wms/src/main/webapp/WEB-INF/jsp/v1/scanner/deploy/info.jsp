<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/scanner.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerDeploy.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110200" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
		
		<!--  
		<div class="panel-heading">	
		</div>
		--> 
              
		<div class="panel-body pn">
			<div class="bs-component">
				<blockquote class="blockquote-danger">
					<small><spring:message code="word.scanner.deploy.text001" /> <spring:message code="word.scanner.deploy.text002" /></small>
					<hr />
					<select name="depType" id="depType" class="form-control">
						<option value="">=<spring:message code="word.scanner.deploy.type" />=</option>
						<c:forEach var="depCD" items="${depCD}">
							<option value="${depCD.sCD}">
								<c:if test="${empty depCD.langCode}">${depCD.sName}</c:if>
								<c:if test="${not empty depCD.langCode}"><spring:message code="${depCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
					
					<select name="depFormat" id="depFormat" class="form-control">
						<!--<option value="js">JS</option>-->
						<option value="json">JSON</option>
					</select>
					
					<button id="scannerDeployMasterBtn" class="btn btn-primary form-control" type="button"><spring:message code="btn.deploy" /></button>
				</blockquote>
			</div>
		</div>
		
		<!-- 
		<div class="panel-footer clearfix">
			<div class="pull-right">				
			</div>
		</div>
		-->
		
	</div>
</div>
</section>
</form>
</body>
</html>