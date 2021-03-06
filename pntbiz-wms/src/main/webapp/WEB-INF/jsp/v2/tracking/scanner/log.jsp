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
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/tracking/presenceLog.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '08', submenu: '04',
			location: ['<spring:message code="menu.170000" />', '<spring:message code="menu.170300" />']
		};
	</script>
</head>
<body>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.170300" /></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<form name="form1" id="form1" class="form-inline" role="form">
				<select id="opt" name="opt" class="w150">
					<option value=""><spring:message code="word.search.option" /></option>
					<option value="targetName" ${param.opt == 'targetName' ? 'selected' : ''}><spring:message code="word.target.name" /></option>
					<option value="beaconName" ${param.opt == 'beaconName' ? 'selected' : ''}><spring:message code="word.beacon.name" /></option>
				</select>
				<input type="text" placeholder="<spring:message code="word.enter.search.term"/>" id="presenceLogKeyword" name="keyword" value="${param.keyword}" class="w300">
				<input type="button" id="presenceLogSearchBtn" value="<spring:message code="btn.search"/>" class="btn-inline btn-search">
				<a href="log.do" class="btn-inline btn-refresh">초기화</a>
			</form>
		</div>
		<div class="list">
			<table>
				<thead>
				<tr>
					<th scope="col"><spring:message code="word.no" /></th>
					<th scope="col"><spring:message code="word.uuid" /></th>
					<th scope="col"><spring:message code="word.target.name" /></th>
					<th scope="col"><spring:message code="word.beacon.name" /></th>
					<th scope="col"><spring:message code="word.floor" /></th>
					<th scope="col"><spring:message code="word.description" /></th>
					<th scope="col"><spring:message code="word.lat.lng" /></th>
					<th scope="col"><spring:message code="word.regdate" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
				<tr>
					<td>${list.logNum}</td>
					<td>${list.UUID}_${list.majorVer}_${list.minorVer}</td>
					<td>${list.targetName}</td>
					<td>${list.beaconName}</td>
					<td>${list.floor}</td>
					<td>${list.logDesc}</td>
					<td>
						<label class="lat"><spring:message code="word.lat"/> ${list.lat}</label>
						<label class="lon"><spring:message code="word.lng"/> ${list.lng}</label>
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