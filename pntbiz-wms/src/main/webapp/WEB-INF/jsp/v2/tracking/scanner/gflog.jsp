<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceGeofenceLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '08', submenu: '05',
			location: ['<spring:message code="menu.170000" />', '<spring:message code="menu.170302" />']
		};
	</script>
</head>
<body>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.170302" /></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<select name="opt" id="opt" class="w150">
				<option value=""><spring:message code="word.search.option" /></option>
				<option value="fcName" ${param.opt == 'fcName' ? 'selected' : ''}><spring:message code="word.geofencing.name" /></option>
			</select>
			<input type="text" placeholder="<spring:message code="word.enter.search.term" />" id="presenceLogKeyword" name="keyword" value="${param.keyword}" class="w300">
			<input type="submit" id="presenceLogSearchBtn" value="<spring:message code="btn.search" />" class="btn-inline btn-search">
			<a href="gflog.do" class="btn-inline btn-refresh"><spring:message code="word.reset" /></a>
		</div>
		<div class="list">
			<table>
				<thead>
				<tr>
					<th scope="col"><spring:message code="word.no" /></th>
					<th scope="col"><spring:message code="word.uuid" /></th>
					<th scope="col"><spring:message code="word.geofencing" /> <spring:message code="word.no" /></th>
					<th scope="col"><spring:message code="word.geofencing.name" /></th>
					<th scope="col"><spring:message code="word.floor" /></th>
					<th scope="col"><spring:message code="word.permitted" />/<spring:message code="word.restricted" /></th>
					<th scope="col"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<td>${list.logNum}</td>
					<td>${list.UUID}_${list.majorVer}_${list.minorVer}</td>
					<td>${list.fcNum}</td>
					<td>${list.fcName}</td>
					<td>${list.floor}</td>
					<td>
						<c:if test="${list.permitted eq '1'}"><label class="lat"><spring:message code="word.permitted" /></label></c:if>
						<c:if test="${list.permitted ne '1'}"><label class="lon"><spring:message code="word.restricted" /></label></c:if>
					</td>
					<td>
						<span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" />
					</td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- Paging -->
		<div class="paging v2">
			<span class="total">총 카운트 ${cnt}</span>
			<div class="original">
				${pagination}
			</div>
		</div>
		<!-- //Paging -->
	</div>
</div>

</body>
</html>