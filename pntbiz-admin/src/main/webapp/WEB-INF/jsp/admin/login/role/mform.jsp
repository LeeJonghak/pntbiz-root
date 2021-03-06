<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/login/role.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="roleNum" name="roleNum" value="${loginRole.roleNum}" />
<div class="col-sm-6">
	<h3>역할 수정 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">계정</a></li>
		  <li class="active">역할 관리</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">역할 번호</label>
	<div class="col-sm-10">
		<c:out value="${loginRole.roleNum}" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">역할 이름</label>
	<div class="col-sm-10">
		<input type="text" id="roleName" name="roleName" value="${loginRole.roleName}" maxlength="50" class="form-control" placeholder="역할 이름"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">권한</label>
	<div class="col-sm-10">
		<c:forEach items="${authList}" var="auth">
			<div>
				<input type="checkbox" value="${auth.authNum}" name="auth" <c:if test="${assignedAuthNumList.contains(auth.authNum)}">checked="checked"</c:if> /> <c:out value="${auth.authName}" />
			</div>
		</c:forEach>
	</div>
</div>
<hr />
<div class="center">
	<button id="loginRoleModBtn" type="button" class="btn btn-primary btn-sm">수정</button>
	<button id="loginRoleDelBtn" type="button" class="btn btn-default btn-sm">삭제</button>
	<button id="loginRoleListBtn" type="button" class="btn btn-default btn-sm" >리스트</button>
</div>
</form>
</body>
</html>