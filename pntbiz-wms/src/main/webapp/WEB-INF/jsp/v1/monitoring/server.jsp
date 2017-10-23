<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/scanner.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/monitoring/monitoring.js"></script>

<script>
$(document).ready(function () {
    monitor.init("server");
});
</script>
<style>
.host-init {
    font-weight: bold;
    color: #000;
}
.status-init {
    background-color: #FFF;
    color: #000;
}
.status-up {
    background-color: #5CB85C;
    color: #FFF;
}
.status-down {
    background-color: #D9534F;
    color: #FFF;
}
</style>
</head>
<body>

<header id="topbar" class="alt">
<div class="topbar-left">
    <ol class="breadcrumb">
        <li class="crumb-active"><a href="###"><spring:message code="menu.180100" /></a></li>
        <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
        <li class="crumb-trail"><spring:message code="menu.180000" /></li>
        <li class="crumb-trail"><spring:message code="menu.180100" /></li>
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
                <div class="col-xs-6 col-md-6"></div>
                <div class="col-xs-6 col-md-6 text-right">
                    <span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
                </div>
            </div>
        </div>

        <div class="panel-body pn">
            <table class="table table-striped table-hover" onclick="sortColumn(event)">
                <thead>
                <tr>
                    <th>HOST</th>
                    <th>STATUS</th>
                    <th>SERVICE</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}" >
                <tr class="trLoop" id="${list.hostname}">
                    <td><c:out value="${list.name}"/>
                        <br/><c:out value="${list.hostname}"/>
                        <br /><span class="spHost">-</span></td>
                    <td><span></span></td>
                    <td><span class="label bg-dark">Ping</span>
                        <span class="label bg-dark">PROCESS[<span class="spProcVal">${list.proc}]</span></span>
                        <span class="label bg-dark">CPU Status [<span class="spCpuVal"><fmt:formatNumber value="${list.cpu}" pattern=".00"/></span>%]</span>
                        <span class="label bg-dark">Memory Usage [<span class="spMemVal"><fmt:formatNumber value="${list.mem}" pattern=".00" /></span>%]</span>
                        <span class="label bg-dark">Uptime [<span class="spUpVal">${list.uptime}</span>]</span>
                        <span class="label bg-dark">DownTime [<span class="spDownVal">${list.downtime}</span>]</span>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty list}">
                    <tr>
                        <td colspan="5" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>


    </div>
</div>
</section>
</body>
</html>