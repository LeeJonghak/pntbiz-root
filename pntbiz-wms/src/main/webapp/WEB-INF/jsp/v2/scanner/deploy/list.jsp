<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-20
  Time: 오후 3:50
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
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerDeploy.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '04', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110400"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<input type="hidden" id="depNum" name="depNum" value="0" />
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110401"/></b>
	</div>
	<div class="boxCon">
		<div class="tableTopBtn aright">
			<input type="button" class="btn-inline btn-regist" id="scannerDeployFormBtn" value="<spring:message code="btn.register"/>"/>
		</div>
		<div class="list rollover">
			<table data-clickUrl="<c:url value="/scanner/deploy/mform.do"/>">
				<thead>
				<tr>
					<th scope="col">Host</th>
					<th scope="col"><spring:message code="word.scanner.deploy.type" /></th>
					<th scope="col"><spring:message code="word.deploy.name" /></th>
					<th scope="col"><spring:message code="word.path" /></th>
					<th scope="col"><spring:message code="word.filename" /></th>
					<th scope="col"><spring:message code="word.moddate" /></th>
					<th scope="col"><spring:message code="word.regdate" /></th>
					<th scope="col"><spring:message code="word.deploy" /></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}">
					<tr data-param="depNum=${list.depNum}">
						<td>${list.ftpHost}</td>
						<td>${list.depType}</td>
						<td>${list.depName}</td>
						<td>${list.depPath}</td>
						<td>${list.depFile}</td>
						<td><span class="pnt-timestamp" data-timestamp="${list.modDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
						<td><span class="pnt-timestamp" data-timestamp="${list.regDate}" data-format="YYYY-MM-DD HH:mm:ss" /></td>
						<td class="notClick">
							<button class="btn-s scannerDeployBtn" type="button" depNum="${list.depNum}"><spring:message code="btn.deploy" /></button>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
</form>
</body>
</html>