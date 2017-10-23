<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-19
  Time: 오후 1:38
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
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/gmap.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '01', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110100"/>']};
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
	<div class="contentBox">
		<div class="title">
			<b><spring:message code="menu.110104"/></b>
		</div>
		<div class="boxCon">
			<div class="view">
				<table>
					<colgroup>
						<col width="18%">
						<col width="">
					</colgroup>
					<tbody>
					<tr>
						<th scope="row">RSSI</th>
						<td>
							<input type="text" id="rssi" name="rssi" value="" maxlength="5" class="w100p" placeholder="0.0"/>
						</td>
					</tr>
					<tr>
						<th scope="row">Sole RSSI</th>
						<td>
							<input type="text" id="srssi" name="srssi" value="" maxlength="5" class="w100p" placeholder="15.0"/>
						</td>
					</tr>
					<tr>
						<th scope="row">Min RSSI</th>
						<td>
							<input type="text" id="mrssi" name="mrssi" value="" maxlength="5" class="w100p" placeholder="-100.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">MaxDiff RSSI</th>
						<td>
							<input type="text" id="drssi" name="drssi" value="" maxlength="5" class="w100p" placeholder="100.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">exclusive Meter</th>
						<td>
							<input type="text" id="exMeter" name="exMeter" value="" maxlength="5" class="w100p" placeholder="15.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">cal point num</th>
						<td>
							<input type="text" id="calPoint" name="calPoint" value="" maxlength="2" class="w100p" placeholder="4.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">Max Signal</th>
						<td>
							<input type="text" id="maxSig" name="maxSig" value="" maxlength="5" class="w100p" placeholder="30.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">Max RSSI Buffer</th>
						<td>
							<input type="text" id="maxBuf" name="maxBuf" value="" maxlength="5" class="w100p" placeholder="20.0" />
						</td>
					</tr>
					<tr>
						<th scope="row">Firmware Version</th>
						<td>
							<input type="text" id="fwVer" name="fwVer" value="" maxlength="30" class="w100p" placeholder="1.0" />
						</td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="btnArea">
				<div class="fl-l">
					<a href="<c:url value="/scanner/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
				</div>
				<div class="fl-r">
					<input type="submit" id="scannerBatchBtn" value="<spring:message code="btn.batch.update" />" class="btn-inline btn-regist">
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>
