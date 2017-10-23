<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-19
  Time: 오전 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/scanner/scanner.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '02', submenu: '01', location: ['<spring:message code="menu.110000" />', '<spring:message code="menu.110100"/>']};

		$(document).ready(function() {
			var pntmap = new pnt.map.OfflineMap('map-canvas', {
				usetile: true,
				control: true,
				dragPan: true,
				doubleClickZoom: true,
				mouseWheelZoom: true,
				zoom: 20
			});

			<c:if test="${not empty scannerInfo.lat and not empty scannerInfo.lng}">
			var center = [${scannerInfo.lng}, ${scannerInfo.lat}];
			var options = {
				position: pnt.util.transformCoordinates(center), label:'', data:{},
				tag:'scanner',
				labelStyle: {
					backgroundColor:'rgba(255, 230, 230, 0.8)'
				}
			};
			var scanner = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'scanner', options);
			</c:if>
			<c:if test="${empty scannerInfo.lat or empty scannerInfo.lng}">
			var center = [
				<sec:authentication property="principal.lng" htmlEscape="false"/>,
				<sec:authentication property="principal.lat" htmlEscape="false"/>
			];
			</c:if>

			pntmap.setCenter(pnt.util.transformCoordinates(center));
			pntmap.on('click', function(event) {
				var originalBeacon = pntmap.getObjectManager().get(pnt.map.object.type.VMARKER, 'scanner');
				if(originalBeacon) {
					pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'scanner');
				}
				var options = {
					position: event.coordinate, label:'', data:{},
					tag:'scanner',
					labelStyle: {
						backgroundColor:'rgba(255, 230, 230, 0.8)'
					}
				};
				var scanner = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'scanner', options);

				var lonlat = pnt.util.transformLonLat(event.coordinate);
				$('#form1').find('[name=lat]').val(lonlat[1]);
				$('#form1').find('[name=lng]').val(lonlat[0]);
			});

			/**
			 * 층 처리
			 */
			var floorManager = new pnt.map.FloorManager(pntmap, {
				defaultFloor: '${scannerInfo.floor}'
			});
			window.floorManager = floorManager;
			var floorDataLoader = new pnt.util.DataLoader();
			floorDataLoader.addUrl('floorList', '<c:url value="/maptool/data/floor.do"/>');
			floorDataLoader.load(function(id, data, complete, error) {
				if (complete == true) {
					var floorData = this.getData('floorList').data;

					floorManager.load(floorData);
					floorManager.onChange(function (event) {

					});
					floorManager.changeDefault();
				}
			});
			floorManager.getElement('floor').style.display = 'none';
			floorManager.getElement('label').style.display = 'none';
			$('#floor').on('change', function() {
				floorManager.changeFloor(this.value);
			});
		});
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
	<input type="hidden" id="scannerNum" name="scannerNum" value="${scannerInfo.scannerNum}" />
	<div class="contentBox">
		<div class="title">
			<b><spring:message code="menu.110103"/></b>
		</div>
		<div class="boxCon">
			<div class="btnArea">
				<div class="fl-l">
					<a href="<c:url value="/scanner/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
				</div>
				<div class="fl-r">
					<input type="button" id="scannerModBtn" value="<spring:message code="btn.modify"/>" class="btn-inline btn-modify"/>
					<input type="button" id="scannerDelBtn" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete"/>
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
						<th scope="row"><spring:message code="word.mac.address"/></th>
						<td>
							<input type="text" id="macAddr" name="macAddr" class="w100p" value="${scannerInfo.macAddr}" maxlength="35" placeholder="AA:BB:CC:DD:EE:FF" required="true" />
						</td>
					</tr>
					<tr>
						<th scope="row">Major Version</th>
						<td>
							<input type="text" name="majorVer" id="majorVer" class="w100p" value="${scannerInfo.majorVer}" maxlength="100" placeholder="<spring:message code="word.major.version"/>" required="true" />
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.scanner.name"/></th>
						<td>
							<input type="text" name="scannerName" id="scannerName" class="w100p" value="${scannerInfo.scannerName}" maxlength="25" placeholder="<spring:message code="word.scanner.name"/>" required="true" />
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.identifier"/></th>
						<td>
							<input type="text" name="sid" id="sid" class="w100p" value="${scannerInfo.sid}" maxlength="10" placeholder="gate1, group1" />
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.lat.lng"/></th>
						<td>

							<table style="border-collapse:initial;border-top:0px;">
								<tr>
									<td><input type="text" name="lat" id="lat" class="w40p" value="${scannerInfo.lat}" placeholder="<spring:message code="word.lat"/>" style="width:100%;" /></td>
									<td><input type="text" name="lng" id="lng" class="w40p" value="${scannerInfo.lng}" placeholder="<spring:message code="word.lng"/>" style="width:100%;" /></td>
								</tr>
								<tr>
									<td colspan="2"><div id="map-canvas" class="mapZone" style="height:400px;"></div></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.floor"/></th>
						<td>
							<%--<select id="floor" name="floor" class="w150">
								<option value="">==<spring:message code="word.floor"/>==</option>
								<c:forEach var="floorCodeList" items="${floorCodeList}">
									<option value="${floorCodeList.nodeId}" ${scannerInfo.floor eq floorCodeList.nodeId ? 'selected' : ''}>${floorCodeList.nodeName1}</option>
								</c:forEach>
							</select>--%>
							<div class="selectUlWrap tree w50p">
								<b id="originalFloorB"></b>
								<ul class="selectUl" data-name="floor" id="floorCodeUl">
								</ul>
							</div>
							<select id="floor" name="floor" class="w150" style="display: none;">
								<c:forEach var="floorCodeList" items="${floorCodeList}">
									<option value="${floorCodeList.nodeId}" ${scannerInfo.floor eq floorCodeList.nodeId ? 'selected' : ''}>${floorCodeList.nodeName1}</option>
								</c:forEach>
							</select>

							<script type="text/javascript">
								(function() {
									var floorList = [];
									<c:forEach var="list" items="${floorCodeList}">
									floorList.push({floorCodeNum:'${list.floorCodeNum}', comNum:'${list.comNum}',
										nodeId:'${list.nodeId}', nodeName1:'${list.nodeName1}', nodeField:'${list.nodeField}',
										typeCode:'${list.typeCode}', upperNodeId:'${list.upperNodeId}', levelNo:'${list.levelNo}',
										sortNo: '${list.sortNo}', useFlag: '${list.useFlag}'});
									</c:forEach>

									rendSelectFloorCode('floorCodeUl', floorList, '${scannerInfo.floor}');
								})();
							</script>
						</td>
					</tr>
					<tr>
						<th scope="row">RSSI</th>
						<td><input type="text" name="rssi" id="rssi" class="w100p" value="${scannerInfo.rssi}" maxlength="6" placeholder="0.0" /></td>
					</tr>
					<tr>
						<th scope="row">Sole RSSI</th>
						<td><input type="text" name="srssi" id="srssi" class="w100p" value="${scannerInfo.srssi}" maxlength="6" placeholder="15.0" /></td>
					</tr>
					<tr>
						<th scope="row">Min RSSI</th>
						<td><input type="text" name="mrssi" id="mrssi" class="w100p" value="${scannerInfo.mrssi}" maxlength="6" placeholder="100.0" /></td>
					</tr>
					<tr>
						<th scope="row">MaxDiff RSSI</th>
						<td><input type="text" name="drssi" id="drssi" class="w100p" value="${scannerInfo.drssi}" maxlength="6" placeholder="100.0" /></td>
					</tr>
					<tr>
						<th scope="row">Exclusive Meiter</th>
						<td><input type="text" name="exMeter" id="exMeter" class="w100p" value="${scannerInfo.exMeter}" maxlength="6" placeholder="15.0" /></td>
					</tr>
					<tr>
						<th scope="row">Cal Point Num</th>
						<td><input type="text" name="calPoint" id="calPoint" class="w100p" value="${scannerInfo.calPoint}" maxlength="3" placeholder="4.0" /></td>
					</tr>
					<tr>
						<th scope="row">Max Signal</th>
						<td><input type="text" name="maxSig" id="maxSig" class="w100p" value="${scannerInfo.maxSig}" maxlength="6" placeholder="30.0" /></td>
					</tr>
					<tr>
						<th scope="row">Max RSSI Buffer</th>
						<td><input type="text" name="maxBuf" id="maxBuf" class="w100p" value="${scannerInfo.maxBuf}" maxlength="6" placeholder="20.0" /></td>
					</tr>
					<tr>
						<th scope="row">Firmware Version</th>
						<td><input type="text" name="fwVer" id="fwVer" class="w100p" value="${scannerInfo.fwVer}" maxlength="30" placeholder="1.0" /></td>
					</tr>


					</tbody>
				</table>
			</div>
			<div class="btnArea">
				<div class="fl-l">
					<a href="<c:url value="/scanner/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
				</div>
				<div class="fl-r">
					<input type="button" id="scannerModBtn2" value="<spring:message code="btn.modify"/>" class="btn-inline btn-modify"/>
					<input type="button" id="scannerDelBtn2" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete"/>
				</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>

