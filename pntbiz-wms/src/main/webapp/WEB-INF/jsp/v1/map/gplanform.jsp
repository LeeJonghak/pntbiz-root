<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/bplan.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<div class="col-sm-6">
	<h3>지오펜싱 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">비콘</a></li>
		  <li class="active">지오펜싱 등록</li>
		</ol>
	</span>
</div>
<div class="clearfix"></div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>

<div class="form-group">
	<label class="col-sm-8 control-label"></label>
	<div class="col-sm-4">
		<h4>지오펜싱 기본 정보</h4>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-8 control-label">펜스명</label>
	<div class="col-sm-4">
		<input type="text" id="fenceName" name="fenceName" value="" size="50" maxlength="36" class="form-control" placeholder="펜스명"  />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-8">콘텐츠</label>
	<div class="col-sm-4">	
		<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">	
			<thead>
			<tr class="active">
				<th width="20%">타입</th>
				<th width="20%">보정치</th>	
				<th width="35%">콘텐츠명</th>	
				<th width="15%">삭제</th>
				<th width="15%">등록</th>
			</tr>
			</thead>
			<tbody>
			<tr align="center">
				<td>ENTER</td>
				<td>
					<input type="text" id="num" name="num" value="3" size="50" maxlength="36" class="form-control" placeholder="보정치"  />
					</td>
				<td>콘텐츠1</td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">X</button></td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">등록</button></td>
			</tr>
			<tr align="center">
				<td>LEAVE</td>
				<td><input type="text" id="num" name="num" value="3" size="50" maxlength="36" class="form-control" placeholder="보정치"  /></td>
				<td>콘텐츠2</td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">X</button></td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">등록</button></td>
			</tr>
			<tr align="center">
				<td>STAY</td>
				<td><input type="text" id="num" name="num" value="10" size="50" maxlength="36" class="form-control" placeholder="보정치"  /></td>
				<td>콘텐츠3</td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">X</button></td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">등록</button></td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-8 control-label">Task</label>
	<div class="col-sm-4">	
		<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">	
			<thead>
			<tr class="active">
				<th>No.</th>
				<th>Code</th>	
				<th>삭제</th>
			</tr>
			</thead>
			<tbody>
			<tr align="center">
				<td>100000</td>
				<td>ActionCode</td>
				<td><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">X</button></td>
			</tr>
			<tr align="center">
				<td colspan="3" class="text-right"><button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">Code 검색등록</button></td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-8 control-label"></label>
	<div class="col-sm-4">	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">수정</button>
		<button id="beaconRegBtn" type="button" class="btn btn-default btn-sm">삭제</button>
	</div>
</div>
<hr />
<div class="center">
	<button id="beaconRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="beaconListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/geofencing/info/list.do')">리스트</button>
</div>
</form>
</body>
</html>