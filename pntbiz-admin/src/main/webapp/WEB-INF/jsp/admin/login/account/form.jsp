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
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/login/account.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<div class="col-sm-6">
	<h3>계정 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">계정</a></li>
		  <li class="active">계정 관리</li>
		</ol>
	</span>
</div>

<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">아이디</label>
	<div class="col-sm-10">
		<input type="text" id="userID" name="userID" value="" maxlength="20" class="form-control" placeholder="아이디"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">이름</label>
	<div class="col-sm-10">
		<input type="text" id="userName" name="userName" value="" maxlength="25" class="form-control" placeholder="이름"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">상태</label>
	<div class="col-sm-10">
		<select id="state" name="state">
			<option value="3">미인증</option>
			<option value="0">대기</option>
			<option value="1" selected="selected">정상</option>
			<option value="2">중지</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">역할</label>
	<div class="col-sm-10">
		<select id="roleNum" name="roleNum">
			<c:forEach items="${roles}" varStatus="status" var="role">
				<option value="<c:out value="${role.roleNum}"/>" ><c:out value="${role.roleName}"/></option>
			</c:forEach>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">비밀번호</label>
	<div class="col-sm-10">
		<input type="password" id="userPW" name="userPW" value="" maxlength="30" class="form-control" placeholder="비밀번호"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">이메일주소</label>
	<div class="col-sm-10">
		<input type="text" id="userEmail" name="userEmail" value="" maxlength="50" class="form-control" placeholder="id@domain.com" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">휴대폰번호</label>
	<div class="col-sm-10">
		<input type="text" id="userPhoneNum" name="userPhoneNum" value="" maxlength="11" class="form-control" placeholder="휴대폰번호" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">업체</label>
	<div class="col-sm-10">
		<select id="comNum" name="comNum">
			<sec:authentication var="loginCompanyNumber" property="principal.companyNumber" />
			<c:if test="${loginCompanyNumber==0}">
				<option value="0"/>선택안함</option>
			</c:if>
			<c:forEach items="${companyList}" var="company">
				<option value="<c:out value="${company.comNum}"/>" ><c:out value="${company.comName}"/></option>
			</c:forEach>
		</select>
	</div>
</div>
<hr />
<div class="center">
	<button id="loginAccountRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="loginAccountListBtn" type="button" class="btn btn-default btn-sm" >리스트</button>
</div>
</form>
</body>
</html>