<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<li class="crumb-active"><a href="###"><spring:message code="menu.990603" /></a></li>
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
				<label for="clientID" class="col-sm-2 control-label"><spring:message code="word.oauth.client.id" /></label>
				<div class="col-sm-10">
					<input type="text" id="clientID" name="clientID" value="${oauthClientInfo.clientID}" readonly="readonly" class="form-control" />
				</div>
			</div>
			<div class="form-group">
				<label for="clientSecret" class="col-sm-2 control-label"><spring:message code="word.oauth.secret.key" /></label>
				<div class="col-sm-10">
					<input type="text" id="clientSecret" name="clientSecret" value="${oauthClientInfo.clientSecret}" readonly="readonly" class="form-control" />
				</div>
			</div>
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
					<textarea id="memo" name="memo" class="form-control" autofocus="autofocus" maxlength="200" rows="3"><c:out value="${oauthClientInfo.memo}" escapeXml="true"/></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="memo" class="col-sm-2 control-label"><spring:message code="word.oauth.token" /></label>
				<div class="col-sm-10">
					<table class="table table-responsive">
						<thead>
							<tr class="active">
								<td class="text-center"><spring:message code="word.oauth.token.access" /></td>
								<%--<td class="text-center">리플레쉬 토큰</td>--%>
								<td class="text-center"><spring:message code="word.oauth.token.expiration.time" /></td>
							</tr>
						</thead>
						<c:forEach var="item" items="${accessTokenList}" varStatus="status">
						<tbody>
							<tr>
								<td>${item.tokenObject.value}</td>
								<%--<td>${item.tokenObject.refreshToken.value}</td>--%>
								<td class="text-center"><fmt:formatDate value="${item.tokenObject.expiration}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</tbody>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="regBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.update" /></button>
				<button id="delBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete" /></button>
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
				url: 'oauth/client/mod.do',
				result: {
					1: {message: vm.modSuccess,
						redirect:'oauth/client/list.do?page=\#{page}'},
					2: vm.modError,
					3: vm.infoFail
					//3: 'OAuth2 클라이언트 정보를 찾을 수 없습니다. 시스템 오류이거나 이미 삭제된 정보일 수 있습니다.'
				}
			},
			delBtn: {
				action: 'submit',
				form: 'form1',
				url: 'oauth/client/del.do',
				confirm: vm.delConfirm,
				result: {
					1: {message: vm.delSuccess,
						redirect:'oauth/client/list.do?page=\#{page}'},
					2: vm.delError,
					3: vm.infoFail
					//3: 'OAuth2 클라이언트 정보를 찾을 수 없습니다. 시스템 오류이거나 이미 삭제된 정보일 수 있습니다.'
				}
			}
		});
	</script>
</body>
</html>