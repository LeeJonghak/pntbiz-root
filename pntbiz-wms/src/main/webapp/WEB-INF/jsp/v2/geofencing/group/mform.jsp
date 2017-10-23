<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-25
  Time: 오후 4:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/geofencing/geofencingGroup.js"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '05', submenu: '02', location: ['<spring:message code="menu.160000" />', '<spring:message code="menu.160200"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
	<input type="hidden" id="fcGroupNum" name="fcGroupNum" value="${geofencingGroup.fcGroupNum}" />
	<div class="contentBox">
		<div class="title">
			<b><spring:message code="menu.160203"/></b>
		</div>
		<div class="boxCon">
			<div class="btnArea">
				<div class="fl-l">
					<input type="button" id="geofencingGroupListBtn" class="btn-inline btn-list" value="<spring:message code="btn.list"/>"/>
				</div>
				<div class="fl-r">
					<input type="submit" id="geofencingGroupModBtn" value="<spring:message code="btn.modify"/>" class="btn-inline btn-modify">
					<input type="button" id="geofencingGroupDelBtn" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete"/>
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
						<th scope="row"><spring:message code="word.geofencing.group.name" /></th>
						<td><input type="text" id="fcGroupName" name="fcGroupName" class="w100p" value="${geofencingGroup.fcGroupName}" required="required" size="30" maxlength="25" placeholder="<spring:message code="word.geofencing.group.name"/>"/></td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="btnArea">
				<div class="fl-l">
					<input type="button" id="geofencingGroupListBtn2" class="btn-inline btn-list" value="<spring:message code="btn.list"/>"/>
				</div>
				<div class="fl-r">
					<input type="submit" id="geofencingGroupModBtn2" value="<spring:message code="btn.register"/>" class="btn-inline btn-modify">
					<input type="button" id="geofencingGroupDelBtn2" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete"/>
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>
