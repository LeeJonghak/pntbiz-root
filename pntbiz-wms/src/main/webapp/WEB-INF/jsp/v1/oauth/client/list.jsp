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
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>
</head>
<body>

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.990601" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.990000" /></li>
		<li class="crumb-trail"><spring:message code="menu.990600" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-6 col-md-6"></div>
				<div class="col-xs-6 col-md-6 text-right">
					<button id="clientFormBtn" class="btn btn-primary btn-sm" type="button" onClick="common.redirect('/oauth/client/form.do');"><spring:message code="btn.register"/></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
                <thead>
                <tr>
                    <!--<th>No.</th>-->
                    <th><spring:message code="word.oauth.client.id"/></th>
                    <th><spring:message code="word.oauth.secret.key"/></th>
                    <th><spring:message code="word.oauth.grant.type"/></th>
                    <th><spring:message code="word.description"/></th>
                    <th><spring:message code="word.regdate"/></th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="6" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}" varStatus="status">
                            <tr>
                                <!--<td>${status.index + 1}</td>-->
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
        
        <div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				${page}
			</ul>
		</div>    

	</div>
</div>
</section>
</body>
</html>
