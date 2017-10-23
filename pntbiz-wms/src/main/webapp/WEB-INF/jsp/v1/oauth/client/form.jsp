<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>
</head>

<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990602" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990600" /></li>
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
				<label class="col-sm-2 control-label"><spring:message code="word.oauth.grant.type" /></label>
				<div class="col-sm-10">
					<select id="grantTypes" name="grantTypes" class="form-control input-sm" required="required">
						<option value="client_credentials">client_credentials</option>
					</select>
				</div>
			</div>
	
			<div class="form-group">
				<label for="memo" class="col-sm-2 control-label"><spring:message code="word.description" /></label>
				<div class="col-sm-10">
					<textarea id="memo" name="memo" class="form-control" autofocus="autofocus" maxlength="200" rows="3"></textarea>
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="regBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="listBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>
	
	</div>
</div>
</section>
</form>
<script type="text/javascript">
	var handler = new ElementHandler('<c:url value="/"/>');
	handler.bind({
		listBtn: function(){
			var page = common.getQueryString('page');
			var param = common.setQueryString({"page": ""});
			common.redirect('list.do'+param);
		},
		regBtn: {
			action: 'submit',
			form: 'form1',
			url: 'oauth/client/reg.do',
			result: {
				1: {message: vm.regSuccess,
					redirect:'oauth/client/list.do'},
				2: vm.regError
			}
		}
	});
</script>
	
</body>
</html>