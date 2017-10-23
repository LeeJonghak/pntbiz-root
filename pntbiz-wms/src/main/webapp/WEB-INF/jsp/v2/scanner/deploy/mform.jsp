<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-20
  Time: 오후 6:14
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
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scannerDeploy.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '04', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110400"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" name="depNum" value="${scannerDeployInfo.depNum}" />
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.110403"/></b>
	</div>
	<div class="boxCon">
		<div class="btnArea">
			<div class="fl-l">
				<a href="<c:url value="/scanner/deploy/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
			</div>
			<div class="fl-r">
				<input type="button" id="scannerDeployModBtn" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify" />
				<input type="button" id="scannerDeployDelBtn" value="<spring:message code="btn.delete" />" class="btn-inline btn-delete" />
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
						<select name="servNum" id="servNum" class="w100p" is_element_select = "true">
							<option value="">Host</option>
							<c:forEach var="slist" items="${slist}">
								<option value="${slist.servNum}" ${scannerDeployInfo.servNum eq slist.servNum ? 'selected' : ''}>${slist.ftpHost}:${slist.ftpPort}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.scanner.deploy.type" /></th>
					<td>
						<select name="depType" id="depType" class="w100p" is_element_select = "true">
							<option value=""><spring:message code="word.scanner.deploy.type" /></option>
							<c:forEach var="depCD" items="${depCD}">
								<option value="${depCD.sCD}" ${scannerDeployInfo.depType eq depCD.sCD ? 'selected' : ''}>
									<c:if test="${empty depCD.langCode}">${depCD.sName}</c:if>
									<c:if test="${not empty depCD.langCode}"><spring:message code="${depCD.langCode}"/></c:if>
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.deploy.name" /></th>
					<td>
						<input type="text" id="depName" name="depName" value="${scannerDeployInfo.depName}" maxlength="25" class="w100p" placeholder="<spring:message code="word.deploy.name" />" />
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.path" /></th>
					<td>
						<input type="text" id="depPath" name="depPath" value="${scannerDeployInfo.depPath}" maxlength="100" class="w100p" placeholder="<spring:message code="word.path" />" />
						<p>Ex) /data/scanner</p>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.filename" /></th>
					<td>
						<input type="text" id="depFile" name="depFile" value="${scannerDeployInfo.depFile}" maxlength=50" class="w100p" placeholder="<spring:message code="word.filename" />" />
						<p><spring:message code="word.scanner.configuration.deploy.text001" /> <spring:message code="word.scanner.configuration.deploy.text002" /></p>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.deploy.info" /></th>
					<td>
						<textarea class="w100p" rows="8">${scannerDeployInfo.depContents}</textarea>
						<p><spring:message code="word.scanner.configuration.deploy.text003" /></p>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="btnArea">
			<div class="fl-l">
				<a href="<c:url value="/scanner/deploy/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
			</div>
			<div class="fl-r">
				<input type="button" id="scannerDeployModBtn2" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify" />
				<input type="button" id="scannerDeployDelBtn2" value="<spring:message code="btn.delete" />" class="btn-inline btn-delete" />
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>




