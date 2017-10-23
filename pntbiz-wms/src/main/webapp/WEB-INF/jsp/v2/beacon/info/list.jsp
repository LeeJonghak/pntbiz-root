<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
	window.layoutGnb = { mainmenu: '03', submenu: '01',
		location: ['<spring:message code="menu.100000" />', '<spring:message code="menu.100100" />', '<spring:message code="menu.100101" />']
	};
</script>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.100101" /></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<form name="form1" id="form1" action="list.do"  role="form">
				<select name="opt" id="opt" class="w150">
					<option value=""><spring:message code="word.search.option" /></option>
					<option value="beaconName" ${param.opt == 'beaconName' ? 'selected' : ''}><spring:message code="word.beacon.name" /></option>
					<option value="floor" ${param.opt == 'floor' ? 'selected' : ''}><spring:message code="word.floor" /></option>
					<option value="majorVer" ${param.opt == 'majorVer' ? 'selected' : ''}><spring:message code="word.major.version" /></option>
					<option value="minorVer" ${param.opt == 'minorVer' ? 'selected' : ''}><spring:message code="word.minor.version" /></option>
					<option value="externalId" ${param.opt == 'externalId' ? 'selected' : ''}><spring:message code="word.external.id" /></option>
					<option value="barcode" ${param.opt == 'barcode' ? 'selected' : ''}><spring:message code="word.barcode" /></option>
				</select>
				<input type="text" placeholder="<spring:message code="word.enter.search.term"/>" value="${param.keyword}" name="keyword" class="w300">
				<input type="submit" name="" value="검색" class="btn-inline btn-search">
				<a href="list.do" class="btn-inline btn-refresh"><spring:message code="word.reset"/></a>
			</form>
		</div>
		<div class="tableTopBtn aright">
			<a href="<c:url value="/beacon/info/form.do"/>" class="btn-inline btn-regist"><spring:message code="btn.register"/></a>
		</div>
		<div class="list rollover">
			<table data-clickUrl="<c:url value="/beacon/info/mform.do"/>">
				<thead>
				<tr>
					<th scope="col"><spring:message code="word.uuid" /></th>
					<th scope="col"><spring:message code="word.beacon.name" /></th>
					<th scope="col"><spring:message code="word.beacon.group.name" /></th>
					<th scope="col"><spring:message code="word.beacon.type" /></th>
					<th scope="col"><spring:message code="word.battery.level" /></th>
					<th scope="col"><spring:message code="word.txpower" /></th>
					<th scope="col"><spring:message code="word.floor" /></th>
					<th scope="col"><spring:message code="word.external.id" /></th>
					<th scope="col"><spring:message code="word.regdate" /></th>
					<th scope="col"><spring:message code="word.last.date" /></th>
					<th class="hidden-xs"><spring:message code="word.restricted.zone" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
				<tr data-param="beaconNum=${list.beaconNum},page=${page},opt=${param.opt},keyword=${param.keyword},field=${param.field},sort=${param.sort}">
					<td>${list.UUID}_${list.majorVer}_${list.minorVer}</td>
					<td>${list.beaconName}</td>
					<td><c:out value="${list.beaconGroupName}" default="-" /></td>
					<td><spring:message code="${list.beaconTypeLangCode}" /></td>
					<td><c:out value="${list.battery}" default="-"/></td>
					<td><c:out value="${list.txPower}" default="-"/></td>
					<td><c:out value="${list.floor}" default="-"/></td>
					<td>${list.externalId}</td>
					<td><span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
					<td><span class="pnt-timestamp" data-timestamp="${list.lastDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
					<td>
						<a href="<c:url value="/beacon/restrictedZone/list.do?beaconNum=${list.beaconNum}&UUID=${list.UUID}"/>"><button type="button" id="restrictedZoneBtn" class="btn-s"><spring:message code="btn.list" /></button></a>
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
	</div>
</div>
