<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
    <div class="col-sm-6">
        <h3>OAuth 클라이언트 수정<small></small></h3>
    </div>
    <div class="col-sm-6">
            <span class="pull-right">
                <ol class="breadcrumb">
                    <li><a href="#">OAuth 클라이언트</a></li>
                    <li class="active">OAuth 클라이언트 수정</li>
                </ol>
            </span>
    </div>
    <hr/>

    <div class="col-sm-9">

    </div>
    <div class="col-sm-3 text-right">
        <button id="clientFormBtn" class="btn btn-default btn-sm" type="button" onClick="common.redirect('/oauth/client/form.do');">클라이언트 등록</button>
    </div>
    <div class="clearfix"></div>
    <div class="bh20"></div>

    <div class="col-sm-12">

        <div class="table-responsive">
            <table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">
                <thead>
                <tr class="active">
                    <th>No.</th>
                    <th>클라이언트 아이디</th>
                    <th>시크릿 키</th>
                    <th>그랜트 유형</th>
                    <th>비고</th>
                    <th>등록시간</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="6" height="150" align="center">검색 결과가 없습니다.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}" varStatus="status">
                            <tr align="center">
                                <td>${status.index + 1}</td>
                                <td><a href="<c:url value="/oauth/client/mform.do?id=${item.clientID}&page=${param.page}"/>">${item.clientID }</a></td>
                                <td>${item.clientSecret }</td>
                                <td>${item.grantTypes}</td>
                                <td>${item.memo }</td>
                                <td>${dateutil:timestamp2str(item.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
        <div class="center">
            <ul class="pagination">
                ${page}
            </ul>
        </div>

    </div>
</body>
</html>
