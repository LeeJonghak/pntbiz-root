<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:forEach items="${columnList}" var="column">
    <div>
        <div class="col-sm-2 control-label">
            ${column.evtColName}<br />
            <small>(${column.evtColID})</small>
        </div>
        <div class="col-sm-10 form-group">
        <c:if test="${column.evtColType eq 'SES'}">
            <c:set var="items" value="${fn:split(column.evtColItems,',')}" />
            <select name="${column.evtColID}" class="form-control">
                <c:forEach items="${items}" var="item">
                    <option <c:if test="${valuesMap[column.evtColNum].beginValue eq item}">selected="selected"</c:if>>${item}</option>
                </c:forEach>
            </select>
        </c:if>
        <c:if test="${column.evtColType eq 'SEM'}">
            <c:set var="items" value="${fn:split(column.evtColItems,',')}" />
            <c:set var="itemValues" value="${fn:split(valuesMap[column.evtColNum].beginValue,',')}" />
            <c:forEach items="${items}" var="item">
                <div class="checkbox">
                    <label><input type="checkbox" name="${column.evtColID}" value="${item}" <c:forEach items="${itemValues}" var="itemValue"><c:if test="${item eq itemValue}">checked="checked"</c:if></c:forEach> /> ${item}</label>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${column.evtColType eq 'SSS'}">
            <input type="text" name="${column.evtColID}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
        </c:if>
        <c:if test="${column.evtColType eq 'NNN'}">
            <input type="number" name="${column.evtColID}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
        </c:if>
        <c:if test="${column.evtColType eq 'NNR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div class="col-sm-5">
                    <input type="number" name="${columnIDs[0]}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                </div>
                <div class="col-sm-2 center">
                    ~
                </div>
                <div class="col-sm-5">
                    <input type="number" name="${columnIDs[1]}" class="form-control" value="${valuesMap[column.evtColNum].endValue}" />
                </div>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDD'}">
            <div class="input-group date datetimepicker">
                <input type="date" name="${column.evtColID}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'TTT'}">
            <div class="input-group time datetimepicker">
                <input type="text" name="${column.evtColID}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDT'}">
            <div class="input-group datetime datetimepicker">
                <input type="text" name="${column.evtColID}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'TTR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div class="col-sm-5">
                    <div class="input-group time datetimepicker">
                        <input type="text" name="${columnIDs[0]}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <div class="col-sm-2 center">
                    ~
                </div>
                <div class="col-sm-5">
                    <div class="input-group time datetimepicker">
                        <input type="text" name="${columnIDs[1]}" class="form-control" value="${valuesMap[column.evtColNum].endValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div class="col-sm-5">
                    <div class="input-group date datetimepicker">
                        <input type="date" name="${columnIDs[0]}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <div class="col-sm-2 center">
                    ~
                </div>
                <div class="col-sm-5">
                    <div class="input-group date datetimepicker">
                        <input type="date" name="${columnIDs[1]}" class="form-control" value="${valuesMap[column.evtColNum].endValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DTR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div class="col-sm-5">
                    <div class="input-group datetime datetimepicker">
                        <input type="datetime" name="${columnIDs[0]}" class="form-control" value="${valuesMap[column.evtColNum].beginValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <div class="col-sm-2 center">
                    ~
                </div>
                <div class="col-sm-5">
                    <div class="input-group datetime datetimepicker">
                        <input type="datetime" name="${columnIDs[1]}" class="form-control" value="${valuesMap[column.evtColNum].endValue}" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
            </div>
        </c:if>
        </div>
    </div>
</c:forEach>

<script type="text/javascript">
    $('.datetimepicker.datetime').datetimepicker({format: 'YYYY-MM-DD HH:mm:SS'});
    $('.datetimepicker.date').datetimepicker({format: 'YYYY-MM-DD'});
    $('.datetimepicker.time').datetimepicker({format: 'HH:mm:SS'});
</script>