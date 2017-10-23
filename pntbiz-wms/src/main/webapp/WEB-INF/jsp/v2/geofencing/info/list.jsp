<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-25
  Time: 오전 11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
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
			src="${viewProperty.staticUrl}/js/geofencing/geofencing.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>

	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '05', submenu: '01', location: ['<spring:message code="menu.160000" />', '<spring:message code="menu.160100"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
	<div class="contentBox">
		<div class="title">
			<b><spring:message code="menu.160101"/></b>
		</div>
		<div class="boxCon">
			<div class="listResearch">
				<select name="opt" id="opt" class="w150" is_element_select = "true">
					<option value=""><spring:message code="word.search.option"/></option>
					<option value="fcName" <c:if test="${param.opt eq 'fcName'}">selected</c:if>><spring:message code="word.geofencing.name"/></option>
				</select>
				<input type="text" placeholder="<spring:message code="word.enter.search.term"/>" name="keyword" id="keyword" class="w300" value="${param.keyword}">
				<input type="submit" id="searchBtn" value="<spring:message code="btn.search"/>" class="btn-inline btn-search">
				<a href="list.do" class="btn-inline btn-refresh"><spring:message code="word.reset"/></a>
			</div>
			<div class="tableTopBtn aright">
				<input type="button" id="syncBtn" class="btn-inline" value="<spring:message code="btn.synchronization" />"/>
				<input type="button" id="formBtn" class="btn-inline btn-regist" value="<spring:message code="btn.register"/>"/>
			</div>
			<div class="list rollover">
				<table data-clickUrl="<c:url value="/geofencing/info/mform.do"/>">
					<thead>
					<tr>
						<th scope="col"><spring:message code="word.geofencing.name"/></th>
						<th scope="col"><spring:message code="word.geofencing.group.name" /></th>
						<th scope="col"><spring:message code="word.geofencing.type" /></th>
						<th scope="col"><spring:message code="word.geofencing.isNodeEnable" /></th>
						<th scope="col"><spring:message code="word.register" /></th>
						<th scope="col"><spring:message code="word.regdate" /></th>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="list" items="${list}">
						<tr data-param="fcNum=${list.fcNum}">
							<td>${list.fcName}</td>
							<td>${list.fcGroupName}</td>
							<td>
								<c:if test="${list.fcType eq 'G'}"><spring:message code="word.general"/></c:if>
								<c:if test="${list.fcType eq 'S'}"><spring:message code="word.system"/></c:if>
							</td>
							<td>
								<c:if test="${list.isNodeEnable eq 'Y'}"><spring:message code="word.geofencing.isNodeEnable.enable"/></c:if>
								<c:if test="${list.isNodeEnable eq 'N'}"><spring:message code="word.geofencing.isNodeEnable.disable"/></c:if>
							</td>
							<td><c:out value="${list.userID}" default="-"/></td>
							<td>${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- Paging -->
			<div class="paging v2">
				<c:choose>
					<c:when test="${cnt ==0}">
					</c:when>
					<c:otherwise>
						<span class="total"><spring:message code="word.total.count"/> ${cnt}</span>
						<div class="original">
								${pagination}
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<!-- //Paging -->
		</div>
	</div>
</form>
</body>
</html>