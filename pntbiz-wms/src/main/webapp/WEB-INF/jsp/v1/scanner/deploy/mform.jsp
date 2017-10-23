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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerDeploy.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" name="depNum" value="${scannerDeployInfo.depNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110403" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110400" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-heading">	
		</div> 
		
		<div class="panel-body">		
			<div class="form-group">
				<label class="col-sm-2 control-label">Host</label>
				<div class="col-sm-10">
					<select name="servNum" id="servNum" class="form-control">
						<option value="">=Host=</option>
						<c:forEach var="slist" items="${slist}">
							<option value="${slist.servNum}" <c:if test="${scannerDeployInfo.servNum eq slist.servNum}">selected</c:if> >${slist.ftpHost}:${slist.ftpPort}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.scanner.deploy.type" /></label>
				<div class="col-sm-10">
					<select name="depType" id="depType" class="form-control">
						<option value="">=<spring:message code="word.scanner.deploy.type" />=</option>
						<c:forEach var="depCD" items="${depCD}">
							<option value="${depCD.sCD}" <c:if test="${scannerDeployInfo.depType eq depCD.sCD}">selected</c:if>>
								<c:if test="${empty depCD.langCode}">${depCD.sName}</c:if>
								<c:if test="${not empty depCD.langCode}"><spring:message code="${depCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.deploy.name" /></label>
				<div class="col-sm-10">
					<input type="text" id="depName" name="depName" value="${scannerDeployInfo.depName}" maxlength="25" class="form-control" placeholder="배포명"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.path" /></label>
				<div class="col-sm-10">
					<input type="text" id="depPath" name="depPath" value="${scannerDeployInfo.depPath}" maxlength="100" class="form-control" placeholder="절대경로"  />
					ex) /data/scanner
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.filename" /></label>
				<div class="col-sm-10">
					<input type="text" id="depFile" name="depFile" value="${scannerDeployInfo.depFile}" maxlength=50" class="form-control" placeholder="파일명"  />
					<spring:message code="word.scanner.configuration.deploy.text001" /> 
					<spring:message code="word.scanner.configuration.deploy.text002" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.deploy.info" /></label>
				<div class="col-sm-10">
				<textarea class="form-control" rows="20">${scannerDeployInfo.depContents}	</textarea>	
				<spring:message code="word.scanner.configuration.deploy.text003" />
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="scannerDeployModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
				<button id="scannerDeployDelBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
				<button id="scannerDeployListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>
		
	</div>
</div>
</section>
</form>
</body>
</html>