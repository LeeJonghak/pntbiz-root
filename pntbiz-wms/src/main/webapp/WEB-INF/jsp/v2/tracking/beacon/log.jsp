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
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceBeaconLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '08', submenu: '07',
			location: ['<spring:message code="menu.170000" />', '<spring:message code="menu.170311" />']
		};
	</script>
</head>
<body>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.170311" /></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<select name="opt" id="opt" class="w150">
				<option value="">=<spring:message code="word.search.option" />=</option>
				<option value="phoneNumber" ${param.opt == 'phoneNumber' ? 'selected' : ''}><spring:message code="word.phone.number" /></option>
				<option value="deviceInfo" ${param.opt == 'deviceInfo' ? 'selected' : ''}><spring:message code="word.device.info" /></option>
				<option value="fcNum" ${param.opt == 'fcNum' ? 'selected' : ''}><spring:message code="word.geofencing.name" /> <spring:message code="word.no" /></option>
				<option value="fcName" ${param.opt == 'fcName' ? 'selected' : ''}><spring:message code="word.geofencing.name" /></option>
				<option value="nodeID" ${param.opt == 'nodeID' ? 'selected' : ''}><spring:message code="word.node.id" /></option>
			</select>
			<input type="text" placeholder="<spring:message code="word.enter.search.term"/>" id="presenceBeaconLogKeyword" name="keyword" value="${param.keyword}" class="w300">
			<input type="button" id="presenceBeaconLogSearchBtn"  value="<spring:message code="btn.search"/>" class="btn-inline btn-search">
			<a href="log.do" class="btn-inline btn-refresh"><spring:message code="word.reset" /></a>
		</div>
		<div class="list">
			<table>
				<thead>
				<tr>
					<th scope="col"><spring:message code="word.no" /></th>
					<th scope="col"><spring:message code="word.phone.number" /></th>
					<th scope="col"><spring:message code="word.device.info" /></th>
					<th scope="col"><spring:message code="word.lat.lng" /></th>
					<th scope="col"><spring:message code="word.geofencing.name" /> <spring:message code="word.no" /></th>
					<th scope="col"><spring:message code="word.geofencing.name" /></th>
					<th scope="col"><spring:message code="word.floor" /></th>
					<th scope="col"><spring:message code="word.node.id" /></th>
					<th scope="col"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<td>${list.logNum}</td>
					<td>${list.phoneNumber}</td>
					<td>${list.deviceInfo}</td>
					<td>
						<label class="lat"><spring:message code="word.lat"/> ${list.lat}</label>
						<label class="lon"><spring:message code="word.lng"/> ${list.lng}</label>
					</td>
					<td><span class="label bg-danger">${list.fcNum}</span></td>
					<td>${list.fcName}</td>
					<td>${list.floor}</td>
					<td><span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- Paging -->
		<%--<div class="paging v2">
			<span class="total">총 카운트 ${cnt}</span>
			<div class="original">
				${pagination}
			</div>
		</div>--%>
		<!-- //Paging -->
	</div>
</div>

</body>
</html>