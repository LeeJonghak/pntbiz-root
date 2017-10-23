<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h5>구역 정보</h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" <c:if test="${param.tab eq null or param.tab=='1'}">class="active"</c:if>><a href="#info-basic" aria-controls="basic" role="tab" data-toggle="tab">기본정보</a></li>
</ul>
<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보
    -->
    <div role="tabpanel" class="tab-pane <c:if test="${param.tab eq null or param.tab=='1'}">active</c:if>" id="info-basic">
        <form name="map-popup-form" id="map-popup-form" class="form-horizontal" role="form" style="margin-top:10px;">

            <c:if test="${areaInfo!=null}">
                <input type="hidden" id="areaNum" name="areaNum" value="${areaInfo.areaNum}" />
            </c:if>

            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label">구역이름</label>
                <div class="col-sm-9">
                    <input type="text" id="areaName" name="areaName" placeholder="구역이름" value="<c:out value="${areaInfo.areaName}" default="" />" size="50" required="required" maxlength="20" class="form-control input-sm" />
                </div>
            </div>
            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <c:if test="${areaInfo==null}">
                    <button id="regBtn" type="button" class="btn btn-primary btn-sm">저장</button>
                </c:if>
                <c:if test="${areaInfo!=null}">
                    <button id="modBtn" type="button" class="btn btn-primary btn-sm">수정</button>
                    <button id="nodeUpdateBtn" type="button" class="btn btn-success btn-sm">노드 업데이트</button>
                    <button id="delBtn" type="button" class="btn btn-danger btn-sm">삭제</button>
                </c:if>

                <button type="button" class="btn btn-default btn-sm popup-close-btn">닫기</button>
            </div>
        </form>
    </div>

</div>


