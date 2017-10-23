<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/cmmFloorSel.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/fCode.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>


$(document).ready(function() {
	cmmFloorSel._floorCodeArr = ${floorCodeList};

    $("#divLctForm").hide();
    $("#floorLocationList").html("");

    var rtlLctArr2 = cmmFloorSel.getLocationHierarchy("${floorCodeInfo.upperNodeId}");
    if(rtlLctArr2 != null && rtlLctArr2.length>0){
        for(var idx in rtlLctArr2){
            var tgtSel = cmmFloorSel.makeLocationSel(rtlLctArr2[idx].levelNo);
            $(tgtSel).html("<option value='"+rtlLctArr2[idx].nodeId+"'>"+rtlLctArr2[0].nodeName1+"</option>");
            $(tgtSel).attr("readonly",true);
        }
    }
});
</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="floorCodeNum" name="floorCodeNum" value="${floorCodeInfo.floorCodeNum}" />

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
                    <input type="text" id="nodeId" name="nodeId" value="${floorCodeInfo.nodeId}" maxlength="10" class="form-control" readonly="readonly"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.node.name"/></label>
                <div class="col-sm-10">
                    <input type="text" id="nodeName1" name="nodeName1" value="${floorCodeInfo.nodeName1}" maxlength="100" class="form-control" />
                    <input type="hidden" id="levelNo" name="levelNo" value="${floorCodeInfo.levelNo}" value=""/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.node.field"/></label>
                <div class="col-sm-10">
                    <input type="text" id="nodeField" name="nodeField" value="${floorCodeInfo.nodeField}" maxlength="50"  class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.floor.code.sort.no"/></label>
                <div class="col-sm-10">
                    <input type="text" id="sortNo" name="sortNo" value="${floorCodeInfo.sortNo}" maxlength="2" class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><spring:message code="word.use"/></label>
                <div class="col-sm-10">
                <select class="form-control" name="useFlag" placeholder="useFlag">
                    <option value="Y" <c:if test="${floorCodeInfo.useFlag eq 'Y'}">selected</c:if>>Y</option>
                    <option value="N" <c:if test="${floorCodeInfo.useFlag eq 'N'}">selected</c:if>>N</option>
                </select>
                </div>
            </div>

	        <div class="form-group" id="divLctForm">
	            <label class="col-sm-2 control-label">dept</label>
	            <div class="col-sm-10">
	                <select class="form-control" placeholder="dept" readonly="readonly">
	                    <option value="">선택하세요</option>
	                </select>
	            </div>
	        </div>
        </div>

        <div class="panel-footer clearfix">
            <div class="pull-right">
                <button id="floorCodeModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
                <button id="floorCodeDelBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete"/></button>
                <button id="floorCodeListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list"/></button>
            </div>
        </div>

    </div>
</div>
</section>
</form>
</body>
</html>