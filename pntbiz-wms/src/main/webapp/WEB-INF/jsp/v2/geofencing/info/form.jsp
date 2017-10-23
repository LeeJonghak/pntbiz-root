<%--
  Created by IntelliJ IDEA.
  User: hyun-sun
  Date: 2017-09-25
  Time: 오후 2:12
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
	<%--<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css?v=<spring:eval expression='@config[buildVersion]'/>" />--%>
	<%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>
	<script type="text/javascript">
		var elementHandler = new ElementHandler('<c:url value="/"/>');
		$(document).ready( function() {
			elementHandler.init();
		});
	</script>
	<link rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/v1/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/geofencing/geofencing.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript">
		window.layoutGnb = {mainmenu: '05', submenu: '01', location: ['<spring:message code="menu.160000" />', '<spring:message code="menu.160100"/>']};

		/* TODO 1 : 지도 확대 막기(ctrl을 누르고 확대/축소)
			TODO 2 : POLYGON, CIRCLE 그리기....
			TODO 3 : 지오팬싱 정보 넘기기;3c pnt.map */
		$(document).ready(function() {
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
				console.log("눌림");
				// 폴리곤을 그리고싶어요
				var originalGeofencing = pntmap.getObjectManager().get(pnt.map.object.type.POLYGON, 'polygon');
				if(originalGeofencing) {
					pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, 'polygon');
				}

			});

			$('input[name=fcShape]').on('change', function() {
				console.log('팬스 타입 변경됨 : ' + $(this).val());
			});
		});
	</script>
</head>
<body>
<form id="form1" name="form1" class="form-horizontal" role="form">
<input type="hidden" id="shapeData" name="shapeData" value="" />
<div class="contentBox">
	<div class="title">
		<b><spring:message code="menu.160102"/></b>
	</div>
	<div class="boxCon">
		<div class="btnArea">
			<div class="fl-l">
				<input type="button" id="listBtn" class="btn-inline btn-list" value="<spring:message code="btn.list"/>"/>
			</div>
			<div class="fl-r">
				<input type="submit" id="regBtn" value="<spring:message code="btn.register"/>" class="btn-inline btn-regist" />
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
					<th scope="row"><spring:message code="word.fence.type"/></th>
					<td>
						<label class="checkBox"><input type="radio" name="fcShape" value="C" required /> <spring:message code="word.fence.type.circle"/></label>
						<label class="checkBox"><input type="radio" name="fcShape" value="P" required /> <spring:message code="word.fence.type.polygon"/></label>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.geofencing.name" /></th>
					<td><input type="text" name="fcName" id="fcName" class="w100p" value="" required minlength="2" maxlength="25" placeholder="<spring:message code="word.geofencing.name" />"></td>
				</tr>
				<tr>
					<!--TODO : 층선택 -->
					<th scope="row"><spring:message code="word.floor"/></th>
					<td>
						<div class="selectUlWrap tree w50p">
							<b><spring:message code="word.floor"/></b>
							<ul class="selectUl" data-name="floor" id="floorCodeUl">
								<%--<li class="active selected"><a href="#" data-value=""><spring:message code="word.floor"/></a></li>--%>
							</ul>
						</div>

						<select name="floor" id="floor" class="w150" style="display: none;" required="required">
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

						<%--<div class="selectUlWrap tree w100p">
							<b><spring:message code="word.floor"/></b>
							<ul class="selectUl" data-name="floor" id="testUL">
								&lt;%&ndash;<li class="active selected"><a href="#" data-value=""><spring:message code="word.floor"/></a></li>&ndash;%&gt;
							</ul>
						</div>
						<select name="floor" id="floor" class="w100p" style="display:none;">
							<c:forEach var="floorCodeList" items="${floorCodeList}">
								<option value="${floorCodeList.nodeId}">${floorCodeList.nodeName1}</option>
							</c:forEach>
						</select>
						<script type="text/javascript">
							var floorList = [];
							<c:forEach var="list" items="${floorCodeList}">
							floorList.push({floorCodeNum:'${list.floorCodeNum}', comNum:'${list.comNum}',
								nodeId:'${list.nodeId}', nodeName1:'${list.nodeName1}', nodeField:'${list.nodeField}',
								typeCode:'${list.typeCode}', upperNodeId:'${list.upperNodeId}', levelNo:'${list.levelNo}',
								sortNo: '${list.sortNo}', useFlag: '${list.useFlag}'});
							</c:forEach>

							var $root = $('#testUL');
							for(var index in floorList) {
								if(floorList[index].levelNo == 0) {
									$root.append('<li class="active" id="' + '">' + '<a href="#" data-value="' + floorList[index].nodeId + '">' + floorList[index].nodeName1 + '</a>'
											+ '<ul id="' + floorList[index].nodeId + '">' + '</ul>'+'</li>');
									//$testDOM += '<li id="' + floorList[index].nodeId + '">' + '<a href="#" data-value="' + floorList[index].nodeId + '">' + floorList[index].nodeName1 + '</a>'+'</li>';
								} else if (floorList[index].levelNo > 0) {
									var upper = floorList[index].upperNodeId;
									$('#'+upper).append('<li class="" id="' + '">' + '<a href="#" data-value="' + floorList[index].nodeId + '">' + floorList[index].nodeName1 + '</a>'
											+ '<ul id="' + floorList[index].nodeId + '">' + '</ul>' +'</li>');
									//$u.append('<li id="' + floorList[index].nodeId + '">' + '<a href="#" data-value="' + floorList[index].nodeId + '">' + floorList[index].nodeName1 + '</a>'+'</li>');
								}
							}
						</script>--%>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.geofencing.group.name" /></th>
					<td>
						<select class="w100p" name="fcGroupNum" is_element_select = "true">
							<option value=""><spring:message code="word.geofencing.group.name" /></option>
							<c:forEach var="fcGroupList" items="${fcGroupList}">
								<option value="${fcGroupList.fcGroupNum}">${fcGroupList.fcGroupName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.geofencing.isNodeEnable" /></th>
					<td>
						<label class="checkBox"><input type="radio" name="isNodeEnable" value="Y"> <spring:message code="word.geofencing.isNodeEnable.enable" /></label>
						<label class="checkBox"><input type="radio" name="isNodeEnable" value="N"> <spring:message code="word.geofencing.isNodeEnable.disable" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.geofencing.field" /> 1~5</th>
					<td>
						<input type="text" id="field1" class="w100" name="field1"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 1" />
						<input type="text" id="field2" class="w100" name="field2"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 2" />
						<input type="text" id="field3" class="w100" name="field3"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 3" />
						<input type="text" id="field4" class="w100" name="field4"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 4" />
						<input type="text" id="field5" class="w100" name="field5"  value="" size="15" minlength="1" maxlength="50" class="form-control" placeholder="<spring:message code="word.geofencing.field" /> 5" />
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="word.map"/></th>
					<td colspan="2">
						<div class="mapZone" id="map-canvas" style="width:100%; height:500px"></div>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="btnArea">
			<div class="fl-l">
				<input type="button" id="listBtn2" class="btn-inline btn-list" value="<spring:message code="btn.list"/>"/>
			</div>
			<div class="fl-r">
				<input type="submit" id="regBtn2" value="<spring:message code="btn.register"/>" class="btn-inline btn-regist" />
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>
