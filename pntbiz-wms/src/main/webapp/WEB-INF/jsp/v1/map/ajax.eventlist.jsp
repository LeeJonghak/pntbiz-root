<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div style="margin-top:10px; margin-bottom:0px;">
    <div id="popup-event-success-message" class="alert alert-success hide"></div>
    <div id="popup-event-error-message" class="alert alert-danger hide"></div>

    <div class="row">
        <div class="col-sm-3">
            <button type="button" id="btn-beacon-assign-event" class="btn btn-primary btn-sm" disabled><spring:message code="word.assign.event" /></button>
        </div>
        <div class="col-sm-9">
            <br /><br />

            <%--<div class="input-group">
                <input type="hidden" id="conNum" name="conNum" />
                <div class="input-group-btn">
                    <select class="input-sm" id="content-list-s-column">
                        <option value="conName" <c:if test="${param.opt=='conName'}">selected</c:if>>콘텐츠명</option>
                    </select>
                </div>
                <input id="content-list-s-keyword" name="conName" type="text" value="${param.keyword}" class="form-control input-sm typeahead" />
                <div class="input-group-btn">
                    <button type="button" id="btn-content-search" class="btn btn-default btn-sm">검색</button>
                </div>
            </div>--%>
        </div>`
    </div>
</div>
<table class="table table-striped table-hover">
    <thead>
    <tr>
        <th width="31" class="">
            <input type="checkbox" style="margin-top:0px;"/>
        </th>
        <th width="100"><spring:message code="word.no"/></th>
        <th><spring:message code="word.event.name"/></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td colspan="3" style="padding:0px;">
            <div style="max-height:260px; overflow-y: auto; padding:0px; margin:0px;">
                <table class="table table-striped table-hover">
                    <tbody>
                    <c:forEach items="${list}" var="item">
                        <tr>
                            <td width="30">
                                <input type="radio" class="chk-evt-num" name="chk-evt-num" value="${item.PNS_STAGE_SEQNO}" />
                            </td>
                            <td width="100">${item.PNS_STAGE_SEQNO}</td>
                            <td>${item.EVENT_NAME}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
    <c:if test="${list.size()==0}">
        <tr>
            <td class="center" colspan="3"><spring:message code="message.search.notmatch" /></td>
        </tr>
    </c:if>
    </tbody>
</table>