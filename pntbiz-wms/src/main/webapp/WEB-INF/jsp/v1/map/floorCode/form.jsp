<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/cmmFloorSel.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/fCode.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>


$(document).ready(function() {
    $("#divLctForm").hide();

	cmmFloorSel._floorCodeArr = ${floorCodeList};
    cmmFloorSel.callLocationSel();
});
</script>

</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
        <li class="crumb-active"><a href="###"><spring:message code="menu.120700" /></a></li>
        <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
        <li class="crumb-trail"><spring:message code="menu.120000" /></li>
        <li class="crumb-trail"><spring:message code="menu.120700" /></li>
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
            <div id="floorLocationList"></div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.node.id"/></label>
                <div class="col-sm-10">
                    <input type="text" id="nodeId" name="nodeId" maxlength="10" class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.node.name"/></label>
                <div class="col-sm-10">
                    <input type="text" id="nodeName1" name="nodeName1" maxlength="100" class="form-control" />
                    <input type="hidden" id="levelNo" name="levelNo"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.node.field"/></label>
                <div class="col-sm-10">
                    <input type="text" id="nodeField" name="nodeField" maxlength="50"  class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.floor.code.sort.no"/></label>
                <div class="col-sm-10">
                    <input type="text" id="sortNo" name="sortNo" maxlength="2" class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.use"/></label>
                <div class="col-sm-10">
                <select class="form-control" name="useFlag" placeholder="useFlag">
                    <option value="Y">Y</option>
                    <option value="N">N</option>
                </select>
                </div>
            </div>
        </div>

        <div class="form-group" id="divLctForm">
            <label class="col-sm-2 control-label">dept</label>
            <div class="col-sm-10">
                <select class="form-control" placeholder="dept" onchange="cmmFloorSel.callLocationSel(this)">
                    <option value="">선택하세요</option>
                </select>
            </div>
        </div>
	</div>

	<div class="panel-footer clearfix">
		<div class="pull-right">
			<button id="floorCodeRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register"/></button>
			<button id="floorCodeListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list"/></button>
		</div>
	</div>

</div>

</section>
</form>
</body>
</html>