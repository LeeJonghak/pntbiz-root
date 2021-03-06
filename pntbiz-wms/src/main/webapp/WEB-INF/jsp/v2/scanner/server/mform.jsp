<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-20
  Time: 오후 4:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerServer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '03', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110300"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" name="servNum" value="${scannerServerInfo.servNum}" />
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110303"/></b>
	</div>
	<div class="boxCon">
		<div class="btnArea">
			<div class="fl-l">
				<a href="<c:url value="/scanner/server/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
			</div>
			<div class="fl-r">
				<input type="submit" id="scannerServerModBtn" value="<spring:message code="btn.modify"/>" class="btn-inline btn-modify" />
				<input type="submit" id="scannerServerDelBtn" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete" />
			</div>
		</div>
		<div class="view">
			<table>
				<colgroup>
					<col width="18%">
					<col width="">
				</colgroup>
				<tbody>
				<tr>
					<th scope="row">Host</th>
					<td>
						<input type="text" id="ftpHost" name="ftpHost" value="${scannerServerInfo.ftpHost}" maxlength="50" class="w100p" placeholder="localhost" />
					</td>
				</tr>
				<tr>
					<th scope="row">Port</th>
					<td>
						<input type="text" id="ftpPort" name="ftpPort" value="${scannerServerInfo.ftpPort}" maxlength="5" class="w100p" placeholder="22" />
					</td>
				</tr>
				<tr>
					<th scope="row">ID</th>
					<td>
						<input type="text" id="ftpID" name="ftpID" value="${scannerServerInfo.ftpID}" maxlength="30" class="w100p" placeholder="id" />
					</td>
				</tr>
				<tr>
					<th scope="row">Password</th>
					<td>
						<input type="password" id="ftpPW" name="ftpPW" value="${scannerServerInfo.ftpPW}" maxlength="30" class="w100p" placeholder="**********" />
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="btnArea">
			<div class="fl-l">
				<a href="<c:url value="/scanner/server/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
			</div>
			<div class="fl-r">
				<input type="submit" id="scannerServerModBtn2" value="<spring:message code="btn.modify"/>" class="btn-inline btn-modify" />
				<input type="submit" id="scannerServerDelBtn2" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete" />
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>