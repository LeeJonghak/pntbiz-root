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
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();
		});
	</script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/event/event.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" name="evtNum" value="${eventInfo.evtNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.140103" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.140000" /></li>
		<li class="crumb-trail"><spring:message code="menu.140100" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.event.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="evtName" name="evtName" value="${eventInfo.evtName}" size="50" required="required" maxlength="50" class="form-control" placeholder="<spring:message code="word.event.name"/>"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.event.description"/></label>
				<div class="col-sm-10">
					<input type="text" id="evtDesc" name="evtDesc" value="${eventInfo.evtDesc}" size="200" maxlength="200" class="form-control" placeholder="<spring:message code="word.event.description"/>"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.event.type"/></label>
				<div class="col-sm-10">
					<select id="evtTypeCode" name="evtTypeCode" required="required" class="form-control">
						<option></option>
						<c:forEach items="${eventTypeList}" var="eventType">
							<option value="${eventType.evtTypeCode}" <c:if test="${eventType.evtTypeCode eq eventInfo.evtTypeCode}">selected="selected"</c:if>>${eventType.evtTypeName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.event.condition"/></label>
				<div class="col-sm-10" id="ajax-condition">
				</div>
			</div>
		</div>
			
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="modBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify" /></button>
				<button id="delBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
				<button id="listBtn" type="button" class="btn btn-default btn-sm" ><spring:message code="btn.list" /></button>
			</div>
		</div>	

	</div>
</div>
</section>
</form>
</body>
</html>