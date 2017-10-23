<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-20
  Time: 오후 3:54
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
		window.layoutGnb = {mainmenu: '02', submenu: '02', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110200"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110200"/></b>
	</div>
	<div class="boxCon">
		<p class="guide">
			※ <spring:message code="word.scanner.deploy.text001"/>  <spring:message code="word.scanner.deploy.text002"/> <b></b>
		</p>
		<select name="depType" id="depType" class="w150">
			<c:forEach var="depCD" items="${depCD}">
				<option value="${depCD.sCD}" >
					<c:if test="${empty depCD.langCode}">${depCD.sName}</c:if>
					<c:if test="${not empty depCD.langCode}"><spring:message code="${depCD.langCode}"/></c:if>
				</option>
			</c:forEach>
		</select>
		<select name="depFormat" id="depFormat" class="w150">
			<option value="json">JSON</option>
		</select>
		<div class="btnArea">
			<div class="fl-r">
				<input type="button" id="scannerDeployMasterBtn" value="<spring:message code="btn.deploy"/>" class="btn-inline btn-regist">
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>