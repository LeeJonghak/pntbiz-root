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


		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();

			var pntmap = new pnt.map.OfflineMap('map-canvas', {
				usetile: true,
				control: true,
				dragPan: true,
				doubleClickZoom: true,
				mouseWheelZoom: true,
				zoom: 20
			});
			var center = [
				<sec:authentication property="principal.lng" htmlEscape="false"/>,
				<sec:authentication property="principal.lat" htmlEscape="false"/>
			];
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
				$('#form1').find('[name=lat]').val(lonlat[1]).trigger('change');
				$('#form1').find('[name=lng]').val(lonlat[0]).trigger('change');
			});

			/**
			 * 층 처리
			 */
			var floorManager = new pnt.map.FloorManager(pntmap, {
				/*defaultFloor: prop.get('floor')||null, autofit: prop.get('floor.autofit')*/
			});
			window.floorManager = floorManager;

			var floorDataLoader = new pnt.util.DataLoader();
			floorDataLoader.addUrl('floorList', '<c:url value="/maptool/data/floor.do"/>');
			floorDataLoader.load(function(id, data, complete, error) {
				if (complete == true) {
					var floorData = this.getData('floorList').data;
					floorManager.load(floorData);
				}
			});
			floorManager.getElement('floor').style.display = 'none';
			floorManager.getElement('label').style.display = 'none';
			$('#floor').on('change', function() {
				floorManager.changeFloor(this.value);
			});

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
			<div class="btnArea">
				<div class="fl-l">
					<a href="<c:url value="/beacon/info/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list" /></a>
				</div>
				<div class="fl-r">
					<input type="button" value="<spring:message code="btn.register" />" class="btn-inline btn-regist">
					<%--<input type="button" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify">--%>
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
						<td><input type="text" name="UUID" class="w100p" value="" required="required" maxlength="36"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.major.version" /></th>
						<td><input type="text" name="majorVer" class="w100p" value="" required="required" size="30" min="0" max="65535" maxlength="25"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.minor.version" /></th>
						<td><input type="text" name="minorVer" class="w100p" value="" required="required" size="30" maxlength="25"></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.group" /></th>
						<td>
							<select name="beaconGroupNum" class="w100p">
								<option value=""><spring:message code="word.beacon.group" /></option>
								<c:forEach items="${beaconGroupList}" var="beaconGroupList">
									<option value="${beaconGroupList.beaconGroupNum}">${beaconGroupList.beaconGroupName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.macaddr" /></th>
						<td><input type="text" name="macAddr" class="w100p" value=""></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.name" /></th>
						<td><input type="text" name="beaconName" class="w100p" value=""></td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.beacon.type" /></th>
						<td>
							<select id="beaconType" name="beaconType" class="w100p">
								<c:forEach items="${beaconTypeCD}" var="beaconTypeCD">
									<option value="${beaconTypeCD.sCD}"><spring:message code="${beaconTypeCD.langCode}" /></option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="word.txpower" /></th>
						<td><input type="text" name="txPower" class="w100p" value="0" required="required"></td>
					</tr>
					<tr id="tr-latlng">
						<th scope="row"><spring:message code="word.lat" />/<spring:message code="word.lat" /></th>
						<td>
							<table style="border-collapse:initial;border-top:0px;">
								<tr>
									<td><input type="text" id="lat" name="lat" value="" style="width:100%"></td>
									<td><input type="text" id="lng" name="lng" value="" style="width:100%"></td>
								</tr>
								<tr>
									<td colspan="2"><div id="map-canvas" class="mapZone" style="height:400px;"></div></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr id="tr-latlng2">
						<th scope="row"><spring:message code="word.floor"/></th>
						<td>
							<div class="selectUlWrap tree w50p">
								<b><spring:message code="word.floor"/></b>
								<ul class="selectUl" data-name="floor" id="floorCodeUl">
									<%--<li class="active selected"><a href="#" data-value=""><spring:message code="word.floor"/></a></li>--%>
								</ul>
							</div>

							<select name="floor" id="floor-select" class="w150" style="display: none;" onchange="floorManager.changeFloor(this.value);">
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

									rendSelectFloorCode('floorCodeUl', floorList, '');
								})();
							</script>
						</td>
					</tr>
					<%--<tr>
						<th scope="row"><spring:message code="word.floor" /></th>
						<td>
							<select class="w100p" name="floor" id="floor-selector">
								<c:forEach items="${floorList}" var="floor">
									<option value="${floor.floor}"><c:out value="${floor.floorName}"/></option>
								</c:forEach>
							</select>
						</td>
					</tr>--%>
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
				<input type="button" value="<spring:message code="btn.register" />" class="btn-inline btn-regist">
				<%--<input type="submit" value="<spring:message code="btn.modify" />" class="btn-inline btn-modify">--%>
				<input type="button" value="<spring:message code="btn.delete" />" class="btn-inline btn-delete">
			</div>
		</div>
	</div>
</div>

</body>
</html>