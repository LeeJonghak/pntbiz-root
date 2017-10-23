<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-20
  Time: 오후 4:06
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

<html>
<head>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerServer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '03', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110300"/>']};
	</script>
</head>
<body>
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110301"/></b>
	</div>
	<div class="boxCon">
		<div class="tableTopBtn aright">
			<input type="button" class="btn-inline btn-regist" id="scannerServerFormBtn" value="<spring:message code="btn.register"/>"/>
		</div>
		<div class="list rollover">
			<table data-clickUrl="<c:url value="/scanner/server/mform.do"/>">
				<thead>
				<tr>
					<th scope="col">Host</th>
					<th scope="col">Port</th>
					<th scope="col">ID</th>
					<th scope="col"><spring:message code="word.moddate"/></th>
					<th scope="col"><spring:message code="word.regdate"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr data-param="servNum=${list.servNum}">
						<td>${list.ftpHost}</td>
						<td>${list.ftpPort}</td>
						<td>${list.ftpID}</td>
						<td><span class="pnt-timeStamp" data-timestamp="${list.modDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
						<td><span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="6" height="150" align="center"><spring:message code="message.search.notmatch"/></td>
						</tr>
					</c:when>
				</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>
</body>
</html>