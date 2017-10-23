<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-18
  Time: 오후 6:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spirng" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
	window.layoutGnb = {mainmenu: '02', submenu: '01', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110100"/>']};
</script>
</head>
<body>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110101"/></b>
	</div>
	<div class="boxCon">
		<div class="listResearch">
			<form name="form1" id="form1" role="form">
				<select name="opt" id="opt" class="w150" is_element_select = "true">
					<option value="" ><spring:message code="word.search.option" /></option>
					<option value="scannerName" ${param.opt == 'scannerName' ? 'selected' : ''}><spring:message code="word.scanner.name" /></option>
					<option value="macAddr" ${param.opt == 'macAddr' ? 'selected' : ''}><spring:message code="word.mac.address" /></option>
				</select>
				<input type="text" placeholder="<spring:message code="word.enter.search.term"/>" value="${param.keyword}" name="keyword" class="w300">
				<input type="submit" id="scannerSearchBtn" value="<spring:message code="btn.search"/>" class="btn-inline btn-search">
				<a href="list.do" class="btn-inline btn-refresh"><spring:message code="word.reset"/></a>
			</form>
		</div>
		<div class="tableTopBtn aright">
			<%--<a href="<c:url value="/scanner/form.do"/>" class="btn-inline btn-regist"><spring:message code="btn.register"/></a>
			<a href="<c:url value="/scanner/bform.do"/>" class="btn-inline"><spring:message code="btn.batch.update" /></a>--%>
			<button type="button" id="scannerFormBtn" class="btn-inline btn-regist"><spring:message code="btn.register"/></button>
			<button type="button" id="scannerBFormBtn" class="btn-inline"><spring:message code="btn.batch.update" /></button>
		</div>
		<div class="list rollover">
			<table data-clickUrl="<c:url value="/scanner/mform.do"/>">
				<thead>
				<tr>
					<th scope="col"><spring:message code="word.mac.address"/></th>
					<th scope="col"><spring:message code="word.major.version"/></th>
					<th scope="col"><spring:message code="word.scanner.name"/></th>
					<th scope="col"><spring:message code="word.scanner.information"/></th>
					<th scope="col"><spring:message code="word.regdate"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list }">
					<tr data-param="scannerNum=${list.scannerNum}">
						<td>${list.macAddr}</td>
						<td>${list.majorVer}</td>
						<td>${list.scannerName}</td>
						<td class="hidden-xs">
							<c:if test="${list.sid}"><span><spring:message code="word.identifier"/> ${list.sid}</span></c:if>
							<label class="lat"><spring:message code="word.lat"/> ${list.lat}</label>
							<label class="lon"><spring:message code="word.lng"/> ${list.lng}</label>
								<%--<label>rssi ${list.rssi}</label>
								<label>srssi ${list.srssi}</label>
								<label>mrssi ${list.mrssi}</label>
								<label>drssi ${list.drssi}</label>
								<label>exMeter ${list.exMeter}</label>
								<label>calPoint ${list.calPoint}</label>
								<label>maxSig ${list.maxSig}</label>
								<label>maxBuf ${list.maxBuf}</label>
								<label>fwVer ${list.fwVer}</label>
								<label><spring:message code="word.floor"/> ${list.floor}</label>--%>
						</td>
						<td><span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt ==0}">
						<tr>
							<td colspan="5" height="150" align="center"><spring:message code="message.search.notmatch"/></td>
						</tr>
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
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
	</div>
</div>
</body>
</html>




