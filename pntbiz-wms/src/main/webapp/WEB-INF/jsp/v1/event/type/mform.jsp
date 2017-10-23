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
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/event.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript">
		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();
		});
	</script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/event/eventType.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.140203" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.140000" /></li>
		<li class="crumb-trail"><spring:message code="menu.140200" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.event.type.code"/></label>
				<div class="col-sm-10">
					<input type="text" id="evtTypeCode" name="evtTypeCode" value="${eventType.evtTypeCode}" size="10" required="required" maxlength="10" class="form-control" placeholder="<spring:message code="word.event.type.code"/>" readonly />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.event.type.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="evtTypeName" name="evtTypeName" value="${eventType.evtTypeName}" size="50" required="required" maxlength="50" class="form-control" placeholder="<spring:message code="word.event.type.name"/>"  />
				</div>
			</div>
		
			<!-- 동적 컬럼 추가 -->
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.condition.property" /></label>
				<div class="col-sm-10">
					<div id="dynamic-column">
						<c:forEach items="${evtTypeColumnList}" var="evtTypeColumn">
						<input type="hidden" name="evtColNum" value="${evtTypeColumn.evtColNum}" />
						<div class="form-group">
							<div class="col-sm-2">
								<select name="evtColType" class="slct-col-type form-control">
									<option><spring:message code="word.column.type" /></option>
									<c:forEach items="${columnCodeList}" var="code">
										<option value="${code.key}" <c:if test="${code.key eq evtTypeColumn.evtColType}">selected="selected"</c:if>>${code.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-sm-3">
								<input type="text" name="evtColID" maxlength="20" placeholder="<spring:message code="word.column.id"/>" value="${evtTypeColumn.evtColID}" class="txt-col-id form-control"/>
							</div>
							<div class="col-sm-3">
								<input type="text" name="evtColName" maxlength="50" placeholder="<spring:message code="word.column.name"/>" value="${evtTypeColumn.evtColName}" class="txt-col-name form-control"/>
							</div>
							<div class="col-sm-2">
								<input type="text" name="items" placeholder="<spring:message code="word.selectable.item"/>" value="${evtTypeColumn.evtColItems}" class="txt-col-enum  form-control"/>
							</div>
							<div class="col-sm-1">
							</div>
						</div>
						</c:forEach>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<small>
							* 범위 항목일 경우 컬럼명에 구분자(,)를 사용하여 시작 컬럼명과 종료 컬럼명을 구분하여 주세요. 예) startDate,endDate<br />
							* 단일선택항목, 다중선택항목일 경우 선택가능 항목에 구분자(,)를 사용하여 여러 항목을 입력해 주세요. 예) 월,화,수,목,금,토,일<br />
							* 구분자(,)를 사용할 경우 공백없이 입력해 주세요.
							</small>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="modBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
				<button id="delBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete"/></button>
				<button id="listBtn" type="button" class="btn btn-default btn-sm" ><spring:message code="btn.list"/></button>
			</div>
		</div>

		<div class="hide">
			<div id="column-source" class="form-group">
				<div class="col-sm-2">
					<select name="evtColType" class="slct-col-type form-control">
						<option><spring:message code="word.column.type"/></option>
						<c:forEach items="${columnCodeList}" var="code">
							<option value="${code.key}">${code.value}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-3">
					<input type="text" name="evtColID" placeholder="<spring:message code="word.column.id"/>" class="txt-col-id form-control"/>
				</div>
				<div class="col-sm-3">
					<input type="text" name="evtColName" placeholder="<spring:message code="word.column.name"/>" class="txt-col-name form-control"/>
				</div>
				<div class="col-sm-2">
					<input type="text" name="items" placeholder="<spring:message code="word.selectable.item"/>" class="txt-col-enum  form-control"/>
				</div>
				<div class="col-sm-1">
					<button type="button" class="btn-col-remove btn btn-default btn-sm">
						<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> <spring:message code="btn.delete"/>
					</button>
				</div>
			</div>
		</div>
	
	</div>
</div>
</section>
</form>
</body>
</html>