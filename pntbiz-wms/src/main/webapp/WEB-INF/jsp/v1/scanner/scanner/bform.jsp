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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.110104" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.110000" /></li>
		<li class="crumb-trail"><spring:message code="menu.110100" /></li>
	</ol>
</div>
<div class="topbar-right">
</div>
</header>

<section id="content" class="table-layout animated fadeIn">
<div class="row col-md-12">
	<div class="panel">
	
		<div class="panel-heading"></div> 
		
		<div class="panel-body">

			<div class="form-group">
				<label class="col-sm-2 control-label">RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="rssi" name="rssi" value="" maxlength="5" class="form-control" placeholder="0.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Sole RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="srssi" name="srssi" value="" maxlength="5" class="form-control" placeholder="15.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Min RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="mrssi" name="mrssi" value="" maxlength="5" class="form-control" placeholder="-100.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">MaxDiff RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="drssi" name="drssi" value="" maxlength="5" class="form-control" placeholder="100.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">exclusive Meter</label>
				<div class="col-sm-10">
					<input type="text" id="exMeter" name="exMeter" value="" maxlength="5" class="form-control" placeholder="15.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">cal point num</label>
				<div class="col-sm-10">
					<input type="text" id="calPoint" name="calPoint" value="" maxlength="2" class="form-control" placeholder="4.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Max Signal</label>
				<div class="col-sm-10">
					<input type="text" id="maxSig" name="maxSig" value="" maxlength="5" class="form-control" placeholder="30.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Max RSSI Buffer</label>
				<div class="col-sm-10">
					<input type="text" id="maxBuf" name="maxBuf" value="" maxlength="5" class="form-control" placeholder="20.0"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Firmware Version</label>
				<div class="col-sm-10">
					<input type="text" id="fwVer" name="fwVer" value="" maxlength="30" class="form-control" placeholder="1.0"  />
				</div>
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<div class="pull-right">
				<button id="scannerBatchBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.batch.update" /></button>
				<button id="scannerListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</div>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>