<%--
  Created by IntelliJ IDEA.
  User: sl.kim
  Date: 2017-09-01
  Time: 오전 11:30
  To change this template use File | Settings | File Templates.
--%>
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
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript"
            src="${viewProperty.staticUrl}/js/info/infoInterface.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">

    <header id="topbar" class="alt">
        <div class="topbar-left">
            <ol class="breadcrumb">
                <li class="crumb-active"><a href="###"><spring:message code="menu.990802" /></a></li>
                <li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
                <li class="crumb-trail"><spring:message code="menu.990000" /></li>
                <li class="crumb-trail"><spring:message code="menu.990800" /></li>
            </ol>
        </div>
    </header>

    <section id="content" class="table-layout animated fadeIn">

        <div class="row col-md-12">
            <div class="panel">

                <div class="panel-heading"></div>

                <div class="panel-body">
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.binding.type" /></label>
                        <div class="col-sm-10">
                            <select name="interfaceBindingType" id="interfaceBindingType" class="form-control">
                                <option value="LOCATION_COMMON"><spring:message code="word.location.common" /></option>
                                <option value="FLOOR_COMMON"><spring:message code="word.floor.common" /></option>
                                <option value="FLOOR"><spring:message code="word.specific.floor" /></option>
                                <option value="GEOFENCE_COMMON"><spring:message code="word.geofence.common" /></option>
                                <option value="GEOFENCE_GROUP"><spring:message code="word.geofencing.group" /></option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.zone.id" /></label>
                        <div class="col-sm-10">
                            <select id="bindingZoneId" name="bindingZoneId" class="form-control">
                            </select>
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.zone.name" /></label>
                        <div class="col-sm-10">
                            <select id="bindingZoneName" name="bindingZoneName" class="form-control" disabled="disabled">
                            </select>
                        </div>
                    </div>--%>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.command.type" /></label>
                        <div class="col-sm-10">
                            <select name="interfaceCommandType" id="interfaceCommandType" class="form-control"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.interlocking.method" /></label>
                        <div class="col-sm-10">
                            <select id="interlockingInterface" name="interlockingInterface" required="required" class="form-control">
                                <option value="Restfull" selected>Restfull</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><spring:message code="word.target.info" /></label>
                        <div class="col-sm-10">
                            <table class="table table-condensed">
                                <tr>
                                    <th class="hidden-xs"><spring:message code="word.protocol"/></th>
                                    <td>
                                        <select id="protocol" name="protocol" class="form-control">
                                            <option value="http">http</option>
                                            <option value="https">https</option>
                                        </select>
                                        <%--<input type="text" id="protocol" name="protocol"  value="" minlength="1" maxlength="50" class="form-control"  size="50" required="required" placeholder="<spring:message code="word.protocol" />" />--%>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="hidden-xs"><spring:message code="word.host"/></th>
                                    <td><input type="text" id="host" name="host"  value="" class="form-control"  size="50" required="required" placeholder="<spring:message code="word.host" />" /></td>
                                </tr>
                                <tr>
                                    <th class="hidden-xs"><spring:message code="word.port"/></th>
                                    <td><input type="number" id="port" name="port"  value="" min="1" max="9999" class="form-control" size="50" required="required" placeholder="<spring:message code="word.port" />" /></td>
                                </tr>
                                <tr>
                                    <th class="hidden-xs"><spring:message code="word.uri"/></th>
                                    <td><input type="text" id="uri" name="uri"  value="" class="form-control" size="50" required="required" placeholder="<spring:message code="word.uri" />" /></td>
                                </tr>
                                <tr>
                                    <th class="hidden-xs"><spring:message code="word.method"/></th>
                                    <td><input type="text" id="method" name="method"  value="" class="form-control" size="50" placeholder="<spring:message code="word.method" />" /></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" id="headers"><spring:message code="word.message.header" /></label>
                        <div class="col-sm-10">
                            <div id="dynamic-column">
                                <div class="form-group">
                                    <div class="col-sm-4">
                                        <input type="text" name="key"  placeholder="<spring:message code="word.key" />" class="txt-col-key form-control"/>
                                    </div>
                                    <div class="col-sm-4">
                                        <input type="text" name="value" placeholder="<spring:message code="word.value" />" class="txt-col-value form-control"/>
                                    </div>
                                    <div class="col-sm-2">
                                        <button type="button" id="addColumn" class="btn btn-default btn-sm">
                                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <spring:message code="word.column.register" />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="hide" id="replication-target">
                        <div  id="column-source" class="form-group">
                            <div class="col-sm-4">
                                <input type="text" name="key" placeholder="<spring:message code="word.key"/>" class="txt-col-key form-control" <%--required="required"--%>/>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" name="value" placeholder="<spring:message code="word.value"/>" class="txt-col-value form-control" <%--required="required"--%>/>
                            </div>
                            <div class="col-sm-2">
                                <button type="button" class="btn-col-remove btn btn-default btn-sm">
                                    <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> <spring:message code="btn.delete"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel-footer clearfix">
                    <div class="pull-right">
                        <button id="regBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
                        <button id="listBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
                    </div>
                </div>

            </div>
        </div>
    </section>
</form>
</body>
</html>
