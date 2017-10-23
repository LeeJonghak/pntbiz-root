<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--
  Created by IntelliJ IDEA.
  User: sl.kim
  Date: 2017-09-01
  Time: 오전 11:31
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <link type="text/css" rel="stylesheet"
          href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
    <script type="text/javascript"
            src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript"
            src="${viewProperty.staticUrl}/js/info/infoInterface.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<form name="form1" id="form1" class="form-inline" role="form">

    <header id="topbar" class="alt">
        <div class="topbar-left">
            <ol class="breadcrumb">
                <li class="crumb-active"><a href="###"><spring:message code="menu.990801" /></a></li>
                <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
                <li class="crumb-trail"><spring:message code="menu.990000" /></li>
                <li class="crumb-trail"><spring:message code="menu.990800" /></li>
            </ol>
        </div>
    </header>
</form>
<section id="content" class="table-layout animated fadeIn">

    <div class="row col-md-12">
        <div class="panel">

            <div class="panel-menu">
                <div class="row">
                    <div class="col-xs-6 col-md-6 form-inline">
                        <select name="interfaceBindingType" id="interfaceBindingType" class="form-control">
                            <option value="">=<spring:message code="word.binding.type" />=</option>
                            <option value="LOCATION_COMMON" ${param.interfaceBindingType== 'LOCATION_COMMON' ? 'selected' : ''}><spring:message code="word.location.common" /></option>
                            <option value="FLOOR_COMMON" ${param.interfaceBindingType== 'FLOOR_COMMON' ? 'selected' : ''}><spring:message code="word.floor.common" /></option>
                            <option value="FLOOR" ${param.interfaceBindingType== 'FLOOR' ? 'selected' : ''}><spring:message code="word.specific.floor" /></option>
                            <option value="GEOFENCE_COMMON" ${param.interfaceBindingType== 'GEOFENCE_COMMON' ? 'selected' : ''}><spring:message code="word.geofence.common" /></option>
                            <option value="GEOFENCE_GROUP" ${param.interfaceBindingType== 'GEOFENCE_GROUP' ? 'selected' : ''}><spring:message code="word.geofencing.group" /></option>
                        </select>
                        <select name="interfaceCommandType" id="interfaceCommandType" class="form-control">
                            <option value="">=<spring:message code="word.command.type" />=</option>
                            <option value="LOCATION_CHANGE" ${param.interfaceCommandType== 'LOCATION_CHANGE' ? 'selected' : ''}><spring:message code="word.location.change" /></option>
                            <option value="FLOOR_IN" ${param.interfaceCommandType== 'FLOOR_IN' ? 'selected' : ''}><spring:message code="word.floor.in" /></option>
                            <option value="FLOOR_STAY" ${param.interfaceCommandType== 'FLOOR_STAY' ? 'selected' : ''}><spring:message code="word.floor.stay" /></option>
                            <option value="FLOOR_OUT" ${param.interfaceCommandType== 'FLOOR_OUT' ? 'selected' : ''}><spring:message code="word.floor.out" /></option>
                            <option value="GEOFENCE_IN" ${param.interfaceCommandType== 'GEOFENCE_IN' ? 'selected' : ''}><spring:message code="word.geofence.in" /></option>
                            <option value="GEOFENCE_STAY" ${param.interfaceCommandType== 'GEOFENCE_STAY' ? 'selected' : ''}><spring:message code="word.geofence.stay" /></option>
                            <option value="GEOFENCE_OUT" ${param.interfaceCommandType== 'GEOFENCE_OUT' ? 'selected' : ''}><spring:message code="word.geofence.out" /></option>
                            <option value="RESTRICTION_IN" ${param.interfaceCommandType== 'RESTRICTION_IN' ? 'selected' : ''}><spring:message code="word.restriction.in" /></option>
                            <option value="RESTRICTION_STAY" ${param.interfaceCommandType== 'RESTRICTION_STAY' ? 'selected' : ''}><spring:message code="word.restriction.stay" /></option>
                            <option value="RESTRICTION_OUT" ${param.interfaceCommandType== 'RESTRICTION_OUT' ? 'selected' : ''}><spring:message code="word.restriction.out" /></option>
                        </select>
                        <button id="init" class="btn btn-default" type="button"><spring:message code="btn.init" /></button>
                    </div>
                    <div class="col-xs-6 col-md-6 text-right">
                        <span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
                        <button id="interfaceConfigFormBtn" class="btn btn-primary btn-sm" type="button" onClick="common.redirect('/info/interface/form.do');"><spring:message code="btn.register" /></button>
                    </div>
                </div>
            </div>

            <div class="panel-body pn">
                <table class="table table-striped table-hover" onclick="sortColumn(event)">
                    <thead>
                    <tr>
                        <th><spring:message code="word.no" /></th>
                        <th><spring:message code="word.binding.type" /></th>
                        <th><spring:message code="word.zone.id" /></th>
                        <th><spring:message code="word.zone.name" /></th>
                        <th><spring:message code="word.command.type" /></th>
                        <th class="hidden-xs hidden-sm hidden-md"><spring:message code="word.interlocking.method" /></th>
                        <th class="hidden-xs hidden-sm hidden-md">
                            <spring:message code="word.target.info" />
                            <label class="label bg-success"><spring:message code="word.protocol"/></label>
                            <label class="label bg-info"><spring:message code="word.host"/></label>
                            <label class="label bg-warning"><spring:message code="word.port"/></label>
                            <label class="label bg-danger"><spring:message code="word.uri"/></label>
                            <label class="label bg-primary"><spring:message code="word.method"/></label>
                        </th>
                        <th class="hidden-xs hidden-sm hidden-md"><spring:message code="word.regdate" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${list}">
                        <tr>
                            <td>
                                <a href="<c:url value="/info/interface/mform.do?interfaceNum=${item.interfaceNum}&page=${page}&interfaceBindingType=${param.interfaceBindingType}&interfaceCommandType=${param.interfaceCommandType}"/>">${item.interfaceNum}</a>
                            </td>
                            <td>
                                <a href="<c:url value="/info/interface/mform.do?interfaceNum=${item.interfaceNum}&page=${page}&interfaceBindingType=${param.interfaceBindingType}&interfaceCommandType=${param.interfaceCommandType}"/>">
                                    <c:choose>
                                        <c:when test="${item.interfaceBindingType=='LOCATION_COMMON'}"><spring:message code="word.location.common"/></c:when>
                                        <c:when test="${item.interfaceBindingType=='FLOOR_COMMON'}"><spring:message code="word.floor.common"/></c:when>
                                        <c:when test="${item.interfaceBindingType=='FLOOR'}"><spring:message code="word.specific.floor"/></c:when>
                                        <c:when test="${item.interfaceBindingType=='GEOFENCE_COMMON'}"><spring:message code="word.geofence.common"/></c:when>
                                        <c:when test="${item.interfaceBindingType=='GEOFENCE_GROUP'}"><spring:message code="word.geofencing.group"/></c:when>
                                    </c:choose>
                                </a>
                            </td>
                            <td>
                                <a href="<c:url value="/info/interface/mform.do?interfaceNum=${item.interfaceNum}&page=${page}&interfaceBindingType=${param.interfaceBindingType}&interfaceCommandType=${param.interfaceCommandType}"/>">${item.bindingZoneId}</a>
                            </td>
                            <td>
                                <a href="<c:url value="/info/interface/mform.do?interfaceNum=${item.interfaceNum}&page=${page}&interfaceBindingType=${param.interfaceBindingType}&interfaceCommandType=${param.interfaceCommandType}"/>">${item.bindingZoneName}</a>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.interfaceCommandType=='LOCATION_CHANGE'}"><spring:message code="word.location.change"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='FLOOR_IN'}"><spring:message code="word.floor.in"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='FLOOR_STAY'}"><spring:message code="word.floor.stay"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='FLOOR_OUT'}"><spring:message code="word.floor.out"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='GEOFENCE_IN'}"><spring:message code="word.geofence.in"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='GEOFENCE_STAY'}"><spring:message code="word.geofence.stay"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='GEOFENCE_OUT'}"><spring:message code="word.geofence.out"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='RESTRICTION_IN'}"><spring:message code="word.restriction.in"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='RESTRICTION_STAY'}"><spring:message code="word.restriction.stay"/></c:when>
                                    <c:when test="${item.interfaceCommandType=='RESTRICTION_OUT'}"><spring:message code="word.restriction.out"/></c:when>
                                </c:choose>
                            </td>
                            <td class="hidden-xs hidden-sm hidden-md">RestFull</td>
                            <td class="hidden-xs hidden-sm hidden-md">
                                <input type="hidden" value="<c:out value="${item.targetInfo}"  escapeXml="true" />" id="targetInfo"/>
                                <label class="label bg-success" id="protocol"></label>
                                <label class="label bg-info" id="host"></label>
                                <label class="label bg-warning" id="port"></label>
                                <label class="label bg-danger" id="uri"></label>
                                <label class="label bg-primary" id="method"></label>
                            </td>
                            <td class="hidden-xs hidden-sm hidden-md">
                                <span class="pnt-timestamp" data-timestamp="${item.regDate}" data-format="YYYY-MM-DD HH:mm:ss" />
                            </td>
                        </tr>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${cnt == 0}">
                            <tr>
                                <td colspan="8" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <div class="panel-footer clearfix">
                <ul class="pagination pull-right">
                    ${pagination}
                </ul>
            </div>

        </div>
    </div>
</section>
</body>
</html>
