<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/css/admin.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/js/admin/calibration.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<div class="col-sm-6">
	<h3>캘리브레이션 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">캘리브레이션</a></li>
		  <li class="active">캘리브레이션 등록</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">제조사</label>
	<div class="col-sm-10">		
		<select name="maker" id="maker" class="form-control">
			<option value="">=제조사=</option>
			<c:forEach var="makerCD" items="${makerCD}">
				<option value="${makerCD.sCD}" <c:if test="${param.maker eq makerCD.sCD}">selected</c:if>>${makerCD.sName}</option>
			</c:forEach>
		</select>		
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">통신사</label>
	<div class="col-sm-10">		
		<select name="telecom" id="telecom" class="form-control">
			<option value="">=통신사=</option>
			<c:forEach var="telecomCD" items="${telecomCD}">
				<option value="${telecomCD.sCD}" <c:if test="${param.telecom eq telecomCD.sCD}">selected</c:if>>${telecomCD.sName}</option>
			</c:forEach>
		</select>	
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">모델명</label>
	<div class="col-sm-10">
		<input type="text" id="modelName" name="modelName" maxlength="30" class="form-control" placeholder="모델명 예)SHV-E210K"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">기기명</label>
	<div class="col-sm-10">
		<input type="text" id="deviceName" name="deviceName" maxlength="30" class="form-control" placeholder="기기명 예)갤럭시S4"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">OS/버전</label>
	<div class="col-sm-10">
		<input type="text" id="os" name="os" maxlength="50" class="form-control" placeholder="OS/버전 예)Lollipop 5.1"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">RSSI</label>
	<div class="col-sm-10">
		<input type="text" id="rssi" name="rssi" maxlength="5" class="form-control" placeholder="RSSI"  />
	</div>
</div>
<hr />
<div class="center">
	<button id="calibrationRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="calibrationListBtn" type="button" class="btn btn-default btn-sm">리스트</button>
</div>
</form>
</body>
</html>