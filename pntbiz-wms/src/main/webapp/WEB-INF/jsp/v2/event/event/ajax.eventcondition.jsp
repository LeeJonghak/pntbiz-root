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
        <div>
            ${column.evtColName}<br />
            <small>(${column.evtColID})</small>
        </div>
        <div>
        <c:if test="${column.evtColType eq 'SES'}">
            <c:set var="items" value="${fn:split(column.evtColItems,',')}" />
            <select name="${column.evtColID}" is_element_select="true" required="required">
                <c:forEach items="${items}" var="item">
                    <option <c:if test="${valuesMap[column.evtColNum].beginValue eq item}">selected="selected"</c:if>>${item}</option>
                </c:forEach>
            </select>
        </c:if>
        <c:if test="${column.evtColType eq 'SEM'}">
            <c:set var="items" value="${fn:split(column.evtColItems,',')}" />
            <c:set var="itemValues" value="${fn:split(valuesMap[column.evtColNum].beginValue,',')}" />
            <c:forEach items="${items}" var="item">
                <div>
                    <label class='checkBox'><input type="checkbox" name="${column.evtColID}" value="${item}" <c:forEach items="${itemValues}" var="itemValue"><c:if test="${item eq itemValue}">checked="checked"</c:if></c:forEach> /> ${item}</label>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${column.evtColType eq 'SSS'}">
            <div>
                <input type="text" name="${column.evtColID}" value="${valuesMap[column.evtColNum].beginValue}"  required="required"/>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'NNN'}">
            <div>
                <input type="number" name="${column.evtColID}" value="${valuesMap[column.evtColNum].beginValue}"  required="required"/>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'NNR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div>
                <input type="number" name="${columnIDs[0]}" class="w150" value="${valuesMap[column.evtColNum].beginValue}"  required="required"/> ~
                <input type="number" name="${columnIDs[1]}" class="w150" value="${valuesMap[column.evtColNum].endValue}"  required="required"/>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDD'}">
            <div>
                <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                <input type="text" class="useDatepicker" name="${column.evtColID}" value="${valuesMap[column.evtColNum].beginValue}" required="required">
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'TTT'}">
            <div>
                <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                <input type="text" class="useDatepicker" name="${column.evtColID}" value="${valuesMap[column.evtColNum].beginValue}"  required="required">
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDT'}">
            <div>
                <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                <input type="text" class="useDatepicker" name="${column.evtColID}" value="${valuesMap[column.evtColNum].beginValue}"  required="required">
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'TTR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div>
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[0]}" value="${valuesMap[column.evtColNum].beginValue}"  required="required">
                    ~
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[1]}" value="${valuesMap[column.evtColNum].endValue}"  required="required">
                </div>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DDR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div>
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[0]}" value="${valuesMap[column.evtColNum].beginValue}"  required="required">
                    ~
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[1]}" value="${valuesMap[column.evtColNum].endValue}"  required="required">
                </div>
            </div>
        </c:if>
        <c:if test="${column.evtColType eq 'DTR'}">
            <c:set var="columnIDs" value="${fn:split(column.evtColID,',')}" />
            <div class="row">
                <div>
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[0]}" value="${valuesMap[column.evtColNum].beginValue}"  required="required">
                    ~
                    <input type="hidden" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
                    <input type="text" class="useDatepicker" name="${columnIDs[1]}" value="${valuesMap[column.evtColNum].endValue}"  required="required">
                </div>
            </div>
        </c:if>
        </div>
    </div>
</c:forEach>