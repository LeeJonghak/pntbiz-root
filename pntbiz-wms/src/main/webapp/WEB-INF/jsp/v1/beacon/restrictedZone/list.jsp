<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript"  src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

    <header id="topbar" class="alt">
        <div class="topbar-left">
            <ol class="breadcrumb">
                <li class="crumb-active"><a href="###"><spring:message code="menu.100104" /></a></li>
                <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
                <li class="crumb-trail"><spring:message code="menu.100000" /></li>
                <li class="crumb-trail"><spring:message code="menu.100100" /></li>
            </ol>
        </div>
        <div class="topbar-right">
            <span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.list"></span>
        </div>
    </header>

    <section id="content" class="table-layout animated fadeIn">

        <div class="row col-md-12">
            <div class="panel">

                <div class="panel-menu">
                    <div class="row">
                        <div class="col-xs-3 col-md-3">
                            <table class="table table-striped table-hover table-condensed">
                                <tr>
                                    <th class="col-xs-3 col-md-3 bg-info" style="text-align: center;"><spring:message code="word.uuid"/></th>
                                    <input type="hidden" id="beaconNum" name="beaconNum" value="${beacon.beaconNum}"/>
                                    <input type="hidden" id="UUID" name="beaconNum" value="${beacon.UUID}"/>
                                    <td class="col-xs-9 col-md-9">${beacon.UUID}</td>
                                </tr>
                            </table>
                        </div>
                        <div class="col-xs-6 col-md-6">
                            <div class="col-xs-12 col-md-4">
                                <input type="radio" class="permitted" id="permitted" name="permitted" value="TRUE" ${list['0'].permitted=='TRUE' ? 'checked="checked"' : ''}>
                                <label for="permitted"><spring:message code="word.permitted" /> <spring:message code="word.set.to"/></label>
                            </div>
                            <div class="col-xs-12 col-md-5">
                                <input type="radio" class="permitted" id="restricted" name="permitted"  value="FALSE" ${list['0'].permitted!='TRUE' ? 'checked="checked"' : ''}>
                                <label for="restricted"><spring:message code="word.restricted" /> <spring:message code="word.set.to"/></label>
                            </div>
                        </div>
                        <div class="col-xs-3 col-md-3 text-right">
                            <span id="total-count" class="label bg-system"><spring:message code="word.total.count" />${cnt}</span>
                            <button id="beaconListBtn" class="btn btn-primary btn-sm" type="button" onClick="common.redirect('/beacon/info/list.do');"><spring:message code="btn.beacon.list" /></button>
                            <button id="restrictedZoneFormBtn" class="btn btn-primary btn-sm" type="button"><spring:message code="btn.register" /></button>
                        </div>
                    </div>
                </div>

                <div class="panel-body pn">
                    <table class="table table-striped table-hover" onclick="sortColumn(event)">
                        <thead>
                        <tr>
                            <th><spring:message code="word.zone.type" /></th>
                            <th><spring:message code="word.zone.id" />(<spring:message code="word.floor" />/<spring:message code="word.geofencing.zone" />)</th>
                            <th><spring:message code="word.restricted.zone.type" /></th>
                            <th class="hidden-xs"><spring:message code="word.additional.attribute" /></th>
                            <th class="hidden-xs"><spring:message code="word.start.date" /></th>
                            <th class="hidden-xs"><spring:message code="word.end.date" /></th>
                            <th class="hidden-xs"><spring:message code="word.regdate" /></th>
                            <th class="hidden-xs"><spring:message code="word.moddate" /></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${list}">
                        <tr>
                            <td>
                                <a href="<c:url value="/beacon/restrictedZone/mform.do?beaconNum=${beacon.beaconNum}&zoneType=${item.zoneType}&zoneId=${item.zoneId}&page=${page}&UUID=${beacon.UUID}"/>">${item.zoneType}</a>
                            </td>
                            <td>
                                <a href="<c:url value="/beacon/restrictedZone/mform.do?beaconNum=${beacon.beaconNum}&zoneType=${item.zoneType}&zoneId=${item.zoneId}&page=${page}&UUID=${beacon.UUID}"/>">${item.zoneId}</a>
                            </td>
                            <td>
                                <c:if test="${item.permitted=='TRUE'}"><spring:message code="word.permitted" /></c:if>
                                <c:if test="${item.permitted=='FALSE'}"><spring:message code="word.restricted" /></c:if>
                            </td>
                            <td class="hidden-xs"><c:out value="${item.additionalAttributeRaw}"  escapeXml="true" /></td>
                            <td class="hidden-xs">
                                <c:choose>
                                    <c:when test="${item.startDate!=null}">
                                        <span class="pnt-timestamp" data-timestamp="${item.startDate}" data-format="YYYY-MM-DD HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="hidden-xs">
                                <c:choose>
                                    <c:when test="${item.endDate!=null}">
                                        <span class="pnt-timestamp" data-timestamp="${item.endDate}" data-format="YYYY-MM-DD HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="hidden-xs">
                                <span class="pnt-timestamp" data-timestamp="${item.regDate}" data-format="YYYY-MM-DD HH:mm:ss" />
                            </td>
                            <td class="hidden-xs">
                                <c:choose>
                                    <c:when test="${item.modDate!=null}">
                                        <span class="pnt-timestamp" data-timestamp="${item.modDate}" data-format="YYYY-MM-DD HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:forEach>
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
</form>
</body>
</html>
