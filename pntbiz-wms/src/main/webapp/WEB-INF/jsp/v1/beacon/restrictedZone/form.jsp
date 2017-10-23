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
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/layout.js"></script>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.8.2/moment-with-locales.min.js"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
    <header id="topbar" class="alt">
        <div class="topbar-left">
            <ol class="breadcrumb">
                <li class="crumb-active"><a href="###"><spring:message code="menu.100105" /></a></li>
                <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
                <li class="crumb-trail"><spring:message code="menu.100000" /></li>
                <li class="crumb-trail"><spring:message code="menu.100100" /></li>
            </ol>
        </div>
        <div class="topbar-right">
            <span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.form"></span>
        </div>
    </header>

    <section id="content" class="table-layout animated fadeIn">

        <div class="row col-md-12">
            <div class="panel">

                <div class="panel-heading"></div>

                <div class="panel-body">
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.uuid" /></label>
                        <div class="col-sm-10">
                            <input type="hidden"  id="beaconNum" name="beaconNum" value="${beacon.beaconNum}">
                            <input type="text" id="UUID" name="UUID" value="${beacon.UUID}" size="50" required="required" maxlength="36" class="form-control" readonly="readonly"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.restricted.zone.type" /></label>
                        <div class="col-sm-10">
                            <input type="hidden" name="permitted" value="${permitted}"/>
                            <input type="text" id="permitted" size="50" required="required" maxlength="10" class="form-control" readonly="readonly"
                                    <c:if test="${permitted=='TRUE'}"> value=<spring:message code="word.permitted"/> </c:if>
                                    <c:if test="${permitted=='FALSE'}"> value=<spring:message code="word.restricted"/> </c:if>
                            />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.zone.type" /></label>
                        <div class="col-sm-10">
                            <select id="zoneType" name="zoneType" required="required" class="form-control" >
                                <option value="FLOOR"><spring:message code="word.floor"/> </option>
                                <option value="GEOFENCE"><spring:message code="word.geofencing.zone"/> </option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" id="floor"><spring:message code="word.floor" /></label>
                        <label class="col-sm-2 control-label" id="geofence" style="display: none;"><spring:message code="word.geofencing" /> <spring:message code="word.number" /></label>
                        <div class="col-sm-10">
                            <input type="text" id="zoneId" name="zoneId" size="50" required="required" maxlength="20" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.additional.attribute" /></label>
                        <div class="col-sm-10">
                            <div id="dynamic-column">
                                <div class="form-group">
                                    <div class="col-sm-4">
                                        <input type="text" name="key" placeholder="<spring:message code="word.key" />" class="txt-col-key form-control"/>
                                    </div>
                                    <div class="col-sm-4">
                                        <input type="text" name="value" placeholder="<spring:message code="word.value" />" class="txt-col-value form-control"/>
                                    </div>
                                    <div class="col-sm-1">
                                        <button type="button" id="addColumn" class="btn btn-default btn-sm">
                                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <spring:message code="word.column.register" />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="hide">
                        <div id="column-source" class="form-group">
                            <div class="col-sm-4">
                                <input type="text" name="key" placeholder="<spring:message code="word.key"/>" class="txt-col-key form-control"/>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" name="value" placeholder="<spring:message code="word.value"/>" class="txt-col-value form-control"/>
                            </div>
                            <div class="col-sm-1">
                                <button type="button" class="btn-col-remove btn btn-default btn-sm">
                                    <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> <spring:message code="btn.delete"/>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.start.date" /></label>
                        <input type="hidden" id="startDateTimestamp" name="startDate" value="${beaconRestrictedZone.startDate}" />
                        <div class=" col-sm-10">
                            <div class="input-group date" id="datetimepicker1">
                                <input type="text" class="col-sm-11 form-control" id="showStartDate" placeholder="<spring:message code="word.start.date" />" />
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.end.date" /></label>
                        <input type="hidden" id="endDateTimestamp" name="endDate" value="${beaconRestrictedZone.endDate}"/>
                        <div class=" col-sm-10">
                            <div class="input-group date" id="datetimepicker2">
                                <input type="text" class="col-sm-11 form-control" id="showEndDate" placeholder="<spring:message code="word.end.date" />" />
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel-footer clearfix">
                    <div class="pull-right">
                        <button id="restrictedZoneRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
                        <button id="restrictedZoneListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/beacon/restrictedZone/list.do?beaconNum=${beacon.beaconNum}&UUID=${beacon.UUID}');"><spring:message code="btn.restricted.zone.list" /></button>
                    </div>
                </div>

            </div>
        </div>
    </section>
</form>
</body>
</html>
