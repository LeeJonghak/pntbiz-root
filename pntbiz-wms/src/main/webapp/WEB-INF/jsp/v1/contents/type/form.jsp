<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/contents.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contentsType.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="comNum" name="comNum" value="${comNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.130302" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.130000" /></li>
		<li class="crumb-trail"><spring:message code="menu.130300" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.contents.type.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="typeName" name="typeName" value="" maxlength="25" class="form-control" placeholder="<spring:message code="word.contents.type.name"/>"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.description" /></label>
				<div class="col-sm-10">
					<textarea id="typeDesc" name="typeDesc" class="form-control" placeholder="<spring:message code="word.description" />" cols="6" rows="5" value=""></textarea>
					<span id="byteLimit">0</span> / 200 byte
				</div>	
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.contents.component" /></label>
				<div class="col-sm-10">
					<div class="form-inline">
						<select name="compType" id="compType" class="form-control">
							<c:forEach var="compTypeCD" items="${compTypeCD}">
								<option value="${compTypeCD.sCD}" <c:if test="${param.sCD eq compTypeCD.sCD}">selected</c:if>>
									<c:if test="${empty compTypeCD.langCode}">${compTypeCD.sName}</c:if>
									<c:if test="${not empty compTypeCD.langCode}"><spring:message code="${compTypeCD.langCode}"/></c:if>
								</option>
							</c:forEach>
						</select>
						<button id="contentsTypeCompAddBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.add" /></button>
					</div>					
					<textarea name="fieldJson" id="fieldJson"></textarea>
					<hr class="short alt" />					
					<div class="dd" id="nestable">
						<ol class="dd-list">
						</ol>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				<button id="contentsTypeRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="contentsTypeListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>