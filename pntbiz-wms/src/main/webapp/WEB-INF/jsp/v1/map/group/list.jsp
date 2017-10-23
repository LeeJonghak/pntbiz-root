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
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/map/group.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.100101" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
	
	
	
	
    <div class="col-sm-6">
        <h3>그룹 목록 <small></small></h3>
    </div>
    <div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
            <li><a href="#">맵</a></li>
            <li><a href="#">그룹 관리</a></li>
            <li class="active">그룹 목록</li>
        </ol>
	</span>
    </div>
    <div class="clearfix"></div>
    <hr />
    <div class="col-sm-9">
    </div>
    <div class="col-sm-3 text-right">
        <button id="formBtn" class="btn btn-default btn-sm" type="button">그룹등록</button>
    </div>
    <div class="clearfix"></div>
    <div class="bh20"></div>
    <div class="col-sm-9">
        <div id="error-opt" class="pull-left"></div>
        <div id="error-beaconKeyword" class="pull-left"></div>
    </div>
    <div class="col-sm-3 text-right">
        <p class="text-info">총 카운트 : ${cnt} 건</p>
    </div>
    <div class="clearfix"></div>
    <div class="bh10"></div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">
            <thead>
            <tr class="active">
                <th>No.</th>
                <th>그룹명</th>
                <th>등록시간</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="list" items="${list}" >
                <tr align="center">
                    <td><a href="javascript:;" groupNum="${list.groupNum}" class="mformBtn">${list.groupNum}</a></td>
                    <td><a href="javascript:;" groupNum="${list.groupNum}" class="mformBtn">${list.groupName}</a></td>
                    <td>${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
                </tr>
            </c:forEach>
            <c:choose>
                <c:when test="${cnt == 0}">
                    <tr>
                        <td colspan="3" height="150" align="center">검색 결과가 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
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
</div>
</section>
</form>
</body>
</html>