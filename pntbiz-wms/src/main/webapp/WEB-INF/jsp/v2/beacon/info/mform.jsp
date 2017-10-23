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
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>

	<script type="text/javascript">
		window.layoutGnb = { mainmenu: '03', submenu: '01',
			location: ['<spring:message code="menu.100000" />', '<spring:message code="menu.100100" />', '<spring:message code="menu.100102" />']
		};

		var flagRenderMap = false;
		function renderMap() {

			if(flagRenderMap==false) {
				flagRenderMap = true;

				var pntmap = new pnt.map.OfflineMap('map-canvas', {
					usetile: true,
					control: true,
					dragPan: true,
					doubleClickZoom: true,
					mouseWheelZoom: true,
					zoom: 20
				});

				<c:if test="${not empty beacon.lat and not empty beacon.lng}">
				var center = [${beacon.lng}, ${beacon.lat}];
				var options = {
					position: pnt.util.transformCoordinates(center), label:'', data:{},
					tag:'beacon',
					labelStyle: {
						backgroundColor:'rgba(255, 230, 230, 0.8)'
					}
				};
				var beacon = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'beacon', options);
				</c:if>
				<c:if test="${empty beacon.lat or empty beacon.lng}">
				var center = [
					<sec:authentication property="principal.lng" htmlEscape="false"/>,
					<sec:authentication property="principal.lat" htmlEscape="false"/>
				];
				</c:if>

				pntmap.setCenter(pnt.util.transformCoordinates(center));
				pntmap.on('click', function(event) {
					var originalBeacon = pntmap.getObjectManager().get(pnt.map.object.type.VMARKER, 'beacon');
					if(originalBeacon) {
						pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'beacon');
					}
					var options = {
						position: event.coordinate, label:'', data:{},
						tag:'beacon',
						labelStyle: {
							backgroundColor:'rgba(255, 230, 230, 0.8)'
						}
					};
					var beacon = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'beacon', options);

					var lonlat = pnt.util.transformLonLat(event.coordinate);
					$('#form1').find('[name=lat]').val(lonlat[1]);
					$('#form1').find('[name=lng]').val(lonlat[0]);
				});

				/**
				 * 층 처리
				 */
				var floorManager = new pnt.map.FloorManager(pntmap, {
					defaultFloor: '${beacon.floor}'
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
			}
		}

		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();

			<c:if test="${beacon.beaconType eq 'F'}">
				renderMap();
			</c:if>
		});
	</script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/beacon/beacon.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.100101" /></b>
		<%--<a href="#" class="btnClose">닫기</a>--%>
	</div>
	<div class="boxCon">
		<form name="form1" id="form1" class="form-horizontal" role="form">
			<input type="hidden" name="beaconNum" value="${beacon.beaconNum}" />
			<div class="btnArea">
				<div class="fl-l">
					<a href="<c:url value="/beacon/info/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list" /></a>
				</div>
				<div class="fl-r">
					<%--<input type="button" value="<spring:message code="btn.register" />" class="btn-inline btn-regist">--%>
					<input type="button" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify">
					<input type="button" value="<spring:message code="btn.delete" />" class="btn-inline btn-delete">
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
						<th scope="row"><spring:message code="word.uuid" /></th>
						<td><input type="text" name="UUID" class="w100p" value="${beacon.UUID}" required="required" maxlength="36"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.major.version" /></th>
						<td><input type="text" name="majorVer" class="w100p" value="${beacon.majorVer}" required="required" size="30" min="0" max="65535" maxlength="25"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.minor.version" /></th>
						<td><input type="text" name="minorVer" class="w100p" value="${beacon.minorVer}" required="required" size="30" maxlength="25"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.group" /></th>
						<td>
							<select name="beaconGroupNum" class="w100p">
								<option value=""><spring:message code="word.beacon.group" /></option>
								<c:forEach items="${beaconGroupList}" var="beaconGroupList">
									<option value="${beaconGroupList.beaconGroupNum}" <c:if test="${beaconGroupList.beaconGroupNum eq beacon.beaconGroupNum}">selected</c:if>>${beaconGroupList.beaconGroupName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.macaddr" /></th>
						<td><input type="text" name="macAddr" class="w100p" value="${beacon.macAddr}"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.name" /></th>
						<td><input type="text" name="beaconName" class="w100p" value="${beacon.beaconName}"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.type" /></th>
						<td>
							<select id="beaconType" name="beaconType" class="w100p">
								<c:forEach items="${beaconTypeCD}" var="beaconTypeCD">
									<option value="${beaconTypeCD.sCD}" <c:if test="${beacon.beaconType eq beaconTypeCD.sCD}">selected</c:if>><spring:message code="${beaconTypeCD.langCode}" /></option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.txpower" /></th>
						<td><input type="text" name="txPower" class="w100p" value="${beacon.txPower}" required="required"></td>
					</tr>
					<tr id="tr-latlng" <c:if test="${beacon.beaconType ne 'F'}">style="display:none;"</c:if>>
						<th scope="row"><spring:message code="word.lat" />/<spring:message code="word.lat" /></th>
						<td>
							<table style="border-collapse:initial;border-top:0px;">
								<tr>
									<td><input type="text" id="lat" name="lat" value="${beacon.lat}" style="width:100%"></td>
									<td><input type="text" id="lng" name="lng" value="${beacon.lng}" style="width:100%"></td>
								</tr>
								<tr>
									<td colspan="2"><div id="map-canvas" class="mapZone" style="height:400px;"></div></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr id="tr-latlng2" <c:if test="${beacon.beaconType ne 'F'}">style="display:none;"</c:if>>
						<th scope="row"><spring:message code="word.floor" /></th>
						<td>
							<div class="selectUlWrap tree w50p">
								<b id="originalFloorB"></b>
								<ul class="selectUl" data-name="floor" id="floorCodeUl">
								</ul>
							</div>

							<select name="floor" id="floor" class="w150" style="display: none;">
								<c:forEach var="floorCodeList" items="${floorCodeList}">
									<option value="${floorCodeList.nodeId}">${floorCodeList.nodeName1}</option>
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

									<c:if test="${beacon.beaconType eq 'F'}">
										rendSelectFloorCode('floorCodeUl', floorList, '${beacon.floor}');
									</c:if>
									<c:if test="${beacon.beaconType ne 'F'}">
										rendSelectFloorCode('floorCodeUl', floorList, '');
									</c:if>
								})();
							</script>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.field" /> 1~5</th>
						<td>
							<table style="border-collapse:initial;border-top:0px;">
								<td style="min-width:80px;">
									<input type="text" name="field1" class="" value="" style="width:100%;">
								</td>
								<td>
									<input type="text" name="field2" class="" value="" style="width:100%;">
								</td>
								<td>
									<input type="text" name="field3" class="" value="" style="width:100%;">
								</td>
								<td>
									<input type="text" name="field4" class="" value="" style="width:100%;">
								</td>
								<td>
									<input type="text" name="field5" class="" value="" style="width:100%;">
								</td>
							</table>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.external.id" /></th>
						<td><input type="text" name="externalId" class="w100p" value=""></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.barcode" /></th>
						<td><input type="text" name="barcode" class="w100p" value=""></td>
					</tr>


					<tr>
						<th scope="row"><spring:message code="word.external.attribute" /></th>
						<td>
							<input type="hidden" name="jsonData" id="jsonData" value='${beacon.externalAttributeRaw}'>
							<table id="dynamic-column" style="border-collapse:initial;border-top:0px;">
								<tr id="column-source" style="display:none;">
									<td><input type="text" name="key" class="w100p txt-col-key" value="" placeholder="<spring:message code="word.key" />"></td>
									<td><input type="text" name="value" class="w100p txt-col-value" value="" placeholder="<spring:message code="word.value" />"></td>
									<td><input type="text" name="displayName" class="w100p txt-col-displayName" value="" placeholder="<spring:message code="word.display.name" />"></td>
									<td><button type="button" id="removeColumn" class="btn-s btn-col-remove"><spring:message code="btn.delete"/></button></td>
								</tr>
								<tr>
									<td><input type="text" name="key" class="w100p txt-col-key" value="" placeholder="<spring:message code="word.key" />"></td>
									<td><input type="text" name="value" class="w100p txt-col-value" value="" placeholder="<spring:message code="word.value" />"></td>
									<td><input type="text" name="displayName" class="w100p txt-col-displayName" value="" placeholder="<spring:message code="word.display.name" />"></td>
									<td><button type="button" id="addColumn" class="btn-s"><spring:message code="word.column.register" /></button></td>
								</tr>
							</table>

						</td>
					</tr>

					<tr>
						<th scope="row"><spring:message code="word.description" /></th>
						<td><textarea name="description" rows="10" class="w100p" data-limitByte="100"></textarea></td>
					</tr>
					<!-- 수정시에만 사용 -->
					<%--<tr>
						<th scope="row">최근응답</th>
						<td><input type="text" name="" class="w100p" value="2017-05-18 11:19:55" disabled="true"></td>
					</tr>--%>
					<!-- //수정시에만 사용 -->
					</tbody>
				</table>
			</div>
		</form>
		<div class="btnArea">
			<div class="fl-l">
				<a href="#" class="btn-inline btn-list"><spring:message code="btn.list" /></a>
			</div>
			<div class="fl-r">
				<%--<input type="button" value="<spring:message code="btn.register" />" class="btn-inline btn-regist">--%>
				<input type="submit" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify">
				<input type="button" value="<spring:message code="btn.delete" />" class="btn-inline btn-delete">
			</div>
		</div>
	</div>
</div>

</body>
</html>