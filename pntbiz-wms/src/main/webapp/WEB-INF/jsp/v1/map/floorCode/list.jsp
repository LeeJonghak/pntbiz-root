<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/info.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/cmmFloorSel.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/fCode.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>

var levelCss = ["bg-danger","bg-info","bg-alert","bg-system"];
$(document).ready(function() {
	cmmFloorSel._floorCodeArr = ${floorCodeList};

    $(".trDataList").each(function(){
    	var upperNodeId = $(this).attr("id");
    	var rtlLctArr2 = cmmFloorSel.getLocationHierarchy(upperNodeId);

    	for(var idx in rtlLctArr2){
    		textHtml = '<span class="label '+levelCss[rtlLctArr2[idx].levelNo]+'">'+rtlLctArr2[idx].typeCode+' '+rtlLctArr2[idx].nodeName1+'</span> ';
    		$(this).find("td:eq(4)").append(textHtml);
    	}
    })
});
</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

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

        <div class="panel-menu">
            <div class="row">
                <div class="col-xs-10 col-md-10">
                </div>
                <div class="col-xs-2 col-md-2 text-right">
                    <span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
                    <button type="button" id="floorCodeFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
                </div>
            </div>
        </div>

        <div class="panel-body pn">
            <table class="table table-striped table-hover" onclick="sortColumn(event)">
                <thead>
                <tr>
                    <th class="hidden-xs"><spring:message code="word.no"/></th>
                    <th><spring:message code="word.type"/></th>
                    <th><spring:message code="word.node.name"/></th>
                    <th><spring:message code="word.node.field"/></th>
                    <th><spring:message code="word.floor.code.upper.node"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}">
                    <tr class="trDataList" id="${list.upperNodeId}">
                        <%-- <td class="hidden-xs"><span class="label bg-primary">${list.floorCodeNum}</span></td> --%>
                        <td><a href="<c:url value="mform.do?floorCodeNum=${list.floorCodeNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}"/>">${list.nodeId}</a></td>
                        <td>${list.typeCode}</td>
                        <td>${list.nodeName1}</td>
                        <td>${list.nodeField}</td>
                        <td></td>
                    </tr>
                </c:forEach>

                <c:if test="${cnt == 0}">
                    <tr>
                       <td colspan="5" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="panel-footer clearfix">
            <ul class="pagination pull-right">${pagination}</ul>
        </div>

    </div>
</div>
</section>
</form>
</body>
</html>