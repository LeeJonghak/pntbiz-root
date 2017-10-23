<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div style="margin-top:10px; margin-bottom:0px;">
    <div id="popup-content-success-message" class="alert alert-success hide"></div>
    <div id="popup-content-error-message" class="alert alert-danger hide"></div>

    <div class="row">
        <div class="col-sm-3">
        <c:if test="${param.refType eq 'BC' or param.refType eq 'ND'}">
            <button type="button" id="btn-beacon-assign-content" class="btn btn-primary btn-sm" disabled><spring:message code="word.assign.content"/></button>
        </c:if>
        <c:if test="${param.refType eq 'GF'}">
            <div class="btn-group">
                <button type="button" id="btn-geofencing-assign-content" class="btn btn-primary disabled btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <spring:message code="word.assign.content"/> <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li disabled class="disabled"><a href="javascript:;" id="btn-geofencing-assign-content-enter"><spring:message code="word.assign.event" />(ENTER)</a></li>
                    <li disabled class="disabled"><a href="javascript:;" id="btn-geofencing-assign-content-stay"><spring:message code="word.assign.event" />(STAY)</a></li>
                    <li disabled class="disabled"><a href="javascript:;" id="btn-geofencing-assign-content-leave"><spring:message code="word.assign.event" />(LEAVE)</a></li>
                </ul>
            </div>
        </c:if>
        </div>
        <div class="col-sm-9">
            <div class="input-group">
                <input type="hidden" id="conNum" name="conNum" />
                <div class="input-group-btn">
                    <select class="input-sm" id="content-list-s-column">
                        <option value="conName" <c:if test="${param.opt=='conName'}">selected</c:if>><spring:message code="word.contents.name"/></option>
                    </select>
                </div>
                <input id="content-list-s-keyword" name="conName" type="text" value="${param.keyword}" class="form-control input-sm typeahead" />
                <div class="input-group-btn">
                    <button type="button" id="btn-content-search" class="btn btn-default btn-sm"><spring:message code="btn.search"/></button>
                </div>
            </div>
        </div>`
    </div>
</div>
<table class="table table-striped table-hover">
    <thead>
    <tr>
        <th width="31" class="">
            <c:if test="${param.refType eq 'BC' or param.refType eq 'ND'}">
                <%--<input type="checkbox" style="margin-top:0px;"/>--%>
            </c:if>
        </th>
        <th width="100"><spring:message code="word.no"/></th>
        <th><spring:message code="word.contents.name"/></th>
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
                                <%/*
                                    비콘에 할당가능한 컨텐츠 목록일 경우 여러개의 컨텐츠를 할당할 수 있도록 체크박스 사용
                                */%>
                                <c:if test="${param.refType eq 'BC' or param.refType eq 'ND'}">
                                    <input type="checkbox" class="chk-con-num" name="chk-con-num" value="${item.conNum}" />
                                </c:if>
                                <%/*
                                    지오펜스에 할당가능한 컨텐츠 목록일 경우 단일 컨텐츠만 할당할 수 있도록 레디오 박스 사용
                                */%>
                                <c:if test="${param.refType eq 'GF'}">
                                    <input type="radio" class="chk-con-num" name="chk-con-num" value="${item.conNum}" />
                                </c:if>
                            </td>
                            <td width="100">${item.conNum}</td>
                            <td>${item.conName}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
    <c:if test="${list.size()==0}">
        <tr>
            <td class="center" colspan="4"><spring:message code="message.search.notmatch"/></td>
        </tr>
    </c:if>
    </tbody>
</table>