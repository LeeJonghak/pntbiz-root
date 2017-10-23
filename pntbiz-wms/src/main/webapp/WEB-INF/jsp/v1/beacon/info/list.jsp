<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
    <link type="text/css" rel="stylesheet"
		  href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>"/>
    <script type="text/javascript"
			src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript"
			src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>', '<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
    <script type="text/javascript"
			src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
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
	<span class="glyphicon glyphicon-question-sign help" id="help" help="beacon.list"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel">

		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-6 col-md-6">
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="beaconName" ${param.opt == 'beaconName' ? 'selected' : ''}><spring:message code="word.beacon.name" /></option>
						<option value="floor" ${param.opt == 'floor' ? 'selected' : ''}><spring:message code="word.floor" /></option>
						<option value="majorVer" ${param.opt == 'majorVer' ? 'selected' : ''}><spring:message code="word.major.version" /></option>
						<option value="minorVer" ${param.opt == 'minorVer' ? 'selected' : ''}><spring:message code="word.minor.version" /></option>
						<option value="externalId" ${param.opt == 'externalId' ? 'selected' : ''}><spring:message code="word.external.id" /></option>
						<option value="barcode" ${param.opt == 'barcode' ? 'selected' : ''}><spring:message code="word.barcode" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="keyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="searchBtn" class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-6 col-md-6 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button id="beaconFormBtn" class="btn btn-primary btn-sm" type="button" onClick="common.redirect('/beacon/info/form.do');"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>

		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
	            <thead>
	            <tr>
	                <th><spring:message code="word.uuid" /></th>
	                <th><spring:message code="word.beacon.name" /></th>
	                <th><spring:message code="word.beacon.group.name" /></th>
	                <th><spring:message code="word.beacon.type" /></th>
	                <th class="hidden-xs"><spring:message code="word.battery.level" /></th>
	                <th class="hidden-xs"><spring:message code="word.txpower" /></th>
	                <th class="hidden-xs"><spring:message code="word.floor" /></th>
	                <th class="hidden-xs"><spring:message code="word.regdate" /></th>
	                <th class="hidden-xs"><spring:message code="word.last.date" /></th>
					<th class="hidden-xs"><spring:message code="word.external.id" /></th>
					<th class="hidden-xs"><spring:message code="word.barcode" /></th>
                    <th class="hidden-xs"><spring:message code="word.restricted.zone" /></th>
	            </tr>
	            </thead>
	            <tbody>
	            <c:forEach var="list" items="${list}">
                <tr>
                    <td>
                        <a href="<c:url value="/beacon/info/mform.do?beaconNum=${list.beaconNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&field=${param.field}&sort=${param.sort}"/>">${list.UUID}_${list.majorVer}_${list.minorVer}</a>
                    </td>
                    <td>
                        <a href="<c:url value="/beacon/info/mform.do?beaconNum=${list.beaconNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&field=${param.field}&sort=${param.sort}"/>">${list.beaconName}</a>
                    </td>
                    <td>${list.beaconGroupName}</td>
                    <td><spring:message code="${list.beaconTypeLangCode}" /></td>
                    <td class="hidden-xs"><c:out value="${list.battery}" default="-"/></td>
                    <td class="hidden-xs"><c:out value="${list.txPower}" default="-"/></td>
                    <td class="hidden-xs"><c:out value="${list.floor}" default="-"/></td>
                    <td class="hidden-xs">
                    	<%--${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}--%>
						<span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
                    <td class="hidden-xs">
                    	<%--${dateutil:timestamp2str(list.lastDate,'yyyy-MM-dd HH:mm:ss')}--%>
						<span class="pnt-timestamp" data-timestamp="${list.lastDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
					<td>${list.externalId}</td>
					<td>${list.barcode}</td>
                    <td class="hidden-xs">
                        <a href="<c:url value="/beacon/restrictedZone/list.do?beaconNum=${list.beaconNum}&UUID=${list.UUID}"/>"><button type="button" id="restrictedZoneBtn" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button></a>
                    </td>
				</tr>
	            </c:forEach>
	            <c:choose>
	                <c:when test="${cnt == 0}">
	                    <tr>
	                        <td colspan="12" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
	                    </tr>
	                </c:when>
	                <c:otherwise>
	                </c:otherwise>
	            </c:choose>
	            </tbody>
	        </table>
		</div>

		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				${pagination}
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>
