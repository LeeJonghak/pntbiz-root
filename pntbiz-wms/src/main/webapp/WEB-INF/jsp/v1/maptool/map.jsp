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
	<meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=no;" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <link rel="stylesheet" href="${viewProperty.staticUrl}/js/maptool/ol.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/ol.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/maptool/pnt.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<header id="topbar" class="alt">
	<div class="topbar-left">
		<ol class="breadcrumb">
			<li class="crumb-active"><a href="###"><spring:message code="${param['title.code']}" /></a></li>
			<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="crumb-trail"><spring:message code="menu.120000" /></li>
			<li class="crumb-trail"><spring:message code="${param['title.code']}" /></li>
		</ol>
	</div>
    <div class="topbar-right">
        <span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
    </div>
</header>

<section id="content" class="animated fadeIn">
    <div id="map-canvas"></div>
    <script type="text/javascript">

		window.POI = {};
		<c:forEach var="poi" items="${poiCateList}">
		POI['${poi.key}'] = '${poi.value}';
		</c:forEach>

		window.icon = {
			beacon:'${viewProperty.staticUrl}/images/inc/marker_red_10.png',
			scanner:'${viewProperty.staticUrl}/images/inc/marker_green_10.png',
			nodeBeacon:'${viewProperty.staticUrl}/images/inc/marker_blue_10.png',
			nodeScanner:'${viewProperty.staticUrl}/images/inc/marker_sky_10.png'
		};
		window.urls = {
			beacon: {
				list:'<c:url value="/maptool/data/beacon.do"/>',
				create:'<c:url value="/maptool/beacon/reg.do"/>',
				modify:'<c:url value="/maptool/beacon/mod.do"/>',
				remove:'<c:url value="/maptool/beacon/del.do"/>',
				groupList:'<c:url value="/maptool/data/beaconGroup.do"/>'
			},
			scanner: {
				list:'<c:url value="/maptool/data/scanner.do"/>',
				create:'<c:url value="/maptool/scanner/reg.do"/>',
				modify:'<c:url value="/maptool/scanner/mod.do"/>',
				remove:'<c:url value="/maptool/scanner/del.do"/>'
			},
			node: {
				list:'<c:url value="/maptool/data/node.do"/>',
				create:'<c:url value="/maptool/node/reg.do"/>',
				modify:'<c:url value="/maptool/node/mod.do"/>',
				remove:'<c:url value="/maptool/node/del.do"/>'
			},
			nodeedge: {
				list:'<c:url value="/maptool/data/nodeEdge.do"/>',
				create:'<c:url value="/maptool/nodeEdge/reg.do"/>',
				modify:'<c:url value="/maptool/nodeEdge/mod.do"/>',
				remove:'<c:url value="/maptool/nodeEdge/del.do"/>'
			},
			geofence: {
				list:'<c:url value="/maptool/data/geofence.do"/>',
				create:'<c:url value="/maptool/geofence/reg.do"/>',
				modify:'<c:url value="/maptool/geofence/mod.do"/>',
				remove:'<c:url value="/maptool/geofence/del.do"/>',
				groupList:'<c:url value="/maptool/data/geofenceGroup.do"/>'
			},
			floor: {
				list:'<c:url value="/maptool/data/floor.do"/>',
			},
			area: {
				list:'<c:url value="/maptool/data/area.do"/>',
				create:'<c:url value="/maptool/area/reg.do"/>',
				modify:'<c:url value="/maptool/area/mod.do"/>',
				remove:'<c:url value="/maptool/area/del.do"/>'
			},
			contents: {
				list: '<c:url value="/maptool/data/contents.do"/>',
				mappingList: '<c:url value="/maptool/data/contentsMapping.do"/>',
				createMapping: '<c:url value="/maptool/contentsMapping/reg.do"/>',
				removeMapping: '<c:url value="/maptool/contentsMapping/del.do"/>'
			}
		}

		window.form = {
			beacon: {
				reg: function(data) {
					var beaconGroupNumOptions = [{value:'0',text:'[[선택해주세요]]'}];
					var groupList = dataEvent.beaconGroup.find();
					for(var i=0; i<groupList.length; i++) {
						beaconGroupNumOptions[beaconGroupNumOptions.length] = {
							value: groupList[i].get('beaconGroupNum'),
							text: groupList[i].get('beaconGroupName')
						}
					}
					var form = new pnt.ui.builder.Form({id:'form', field: {
							lat: {type: 'hidden', value: data.lat},
							lng: {type: 'hidden', value: data.lng},
							floor: {type: 'hidden', value: data.floor},
							UUID: {type: 'text', label: 'UUID', value: '', required: true},
							majorVer: {type: 'text', label:'Major Ver', value: '', required: true},
							minorVer: {type: 'text', label:'Minor Ver', value: '', required: true},
							beaconName: {type: 'text', label: '비콘명', value: '', required: true},
							beaconType: {
								type: 'select', label:'비콘타입',
								options:{F:'고정형',M:'이동형',W:'웨어러블'}, value: 'F'
							},
							beaconGroupNum: {type: 'select', label:'그룹', options:beaconGroupNumOptions},
							beaconDesc: { type: 'textarea', label: '비고', value: ''},
							txPower: { type: 'text', label: 'Tx Power', value: '0'}
						}
					});

					var buttons = [];
					buttons[0] = new pnt.ui.builder.Button({text: '생성', click: function() {
						if($(this.get('form').getElement()).valid()) {
							dataEvent.beacon.create(this.get('form').getFormData());
						}
					},data:{form: form}});
					buttons[1] = new pnt.ui.builder.InputButton({text: '연속생성', value:'2', click: function() {
						var formData = this.get('form').getFormData();
						var startMinor = parseInt(formData.minorVer);
						var cnt = parseInt(this.getValue());
						if($(this.get('form').getElement()).valid()) {
							pntmap.getLineGenerator().start(function(positions) {

								for(var i=0; i<positions.length; i++) {
									var posLonlng = pnt.util.transformLonLat(positions[i]);
									formData.minorVer = startMinor+i;
									formData.beaconName = formData.minorVer;
									formData.lat = posLonlng[1];
									formData.lng = posLonlng[0];
									dataEvent.beacon.create(formData);
								}
							}, cnt);
							pntmap.getPopup().hide();
						}
					},data:{form: form}});
					buttons[1].setInputStyle({width:'80px', marginLeft:'5px'});
					var group = new pnt.ui.builder.ButtonGroup({buttons:buttons});

					return {body: form.getElement(), foot: group.getElement()};
				},
				mod: function(object) {
					var beaconGroupNumOptions = [{value:'0',text:'[[선택해주세요]]'}];
					var groupList = dataEvent.beaconGroup.find();
					for(var i=0; i<groupList.length; i++) {
						beaconGroupNumOptions[beaconGroupNumOptions.length] = {
							value: groupList[i].get('beaconGroupNum'),
							text: groupList[i].get('beaconGroupName')
						}
					}
					var beaconGroupNum = '';
					if(object.get('groupMapping').length>0) {
						beaconGroupNum = object.get('groupMapping')[0].beaconGroupNum;
					}

					var form = new pnt.ui.builder.Form({id:'form', field: {
							beaconNum: { type: 'hidden', value: object.get('beaconNum')},
							UUID: { type: 'label', label: 'UUID', value: object.get('UUID'), required: true},
							majorVer: { type: 'text', label:'Major Ver', value: object.get('majorVer'), required: true},
							minorVer: { type: 'text', label:'Minor Ver', value: object.get('minorVer'), required: true},
							beaconName: { type: 'text', label: '비콘명', value: object.get('beaconName'), required: true},
							beaconType: {
								type: 'select', label:'비콘타입', options:{F:'고정형',M:'이동형',W:'웨어러블'},
								value: object.get('beaconType')
							},
							beaconGroupNum: {type: 'select', label:'그룹', options:beaconGroupNumOptions, value:beaconGroupNum},
							beaconDesc: { type: 'textarea', label: '비고', value: object.get('beaconDesc')},
							txPower: { type: 'text', label: 'Tx Power', value: object.get('txPower')},
							lat: { type: 'text', label: '위도', value: object.get('lat'), readonly: true},
							lng: { type: 'text', label: '경도', value: object.get('lng'), readonly: true},
						}
					});

					var button = new pnt.ui.builder.Button({text:'수정', data:{form:form}});
					button.on('click' , function() {
						object.modify(this.get('form').getFormData());
					});

					return {body: form.getElement(), foot: button.getElement()};
				},
				status: function(object) {
					var form = new pnt.ui.builder.Form({id:'form', field: {
							beaconNum: { type: 'hidden', value: object.get('beaconNum')},
							imgSrc: { type: 'hidden', value: object.get('imgSrc')},
							UUID: { type: 'text', label: 'UUID', value: object.get('UUID'), required: true, readonly:true},
							majorVer: { type: 'text', label:'Major Ver', value: object.get('majorVer'), readonly:true},
							minorVer: { type: 'text', label:'Minor Ver', value: object.get('minorVer'), readonly:true},
							beaconName: { type: 'text', label: '비콘명', value: object.get('beaconName'), readonly:true},
							battery: { type: 'text', label: '배터리', value: object.get('battery'), readonly:true},
							state: {
								type: 'select', label:'상태',
								options:{0:'미확인',1:'확인',2:'망실'}, value: object.get('state')
							},
							stateReason: {type: 'text', label: '사유', value: object.get('stateReason')||''},
							battery: { type: 'text', label: '배터리', value: object.get('battery'), readonly:true},
							lastDate: { type: 'text', label:'최근응답시간',
								value: pnt.util.dateformat(object.get('lastDate')*1000,'yyyy-MM-dd hh:mm:ss'), readonly:true
							},
							photo: { type: 'img', label: '설치사진', value: object.get('imgUrl')},
							imgSrc: { type: 'file', onChange: function(evt) {
								var form = this.getElement();
								var value = $(form).find('[name=imgSrc]').val();
								if(typeof(value)!='undefined' && value!='') {
									var imgSize = 1048576 * 5;
									var imgFormat = "png|jpe?g|gif";
									$(form).validate({
										rules: {imgSrc: { extension: imgFormat, filesize: imgSize }},
										messages: {imgSrc: { extension: vm.extension, filesize: vm.filesize }}
									});

									$.ajax({
										type: 'POST',
										url: '/beacon/info/bcImgUpload.do',
										dataType: 'json',
										processData: false,
										contentType: false,
										data: new FormData(form)
									}).done(function(json) {

										if(typeof(json)=='string') {
											json = JSON.parse(json);
											if(typeof(json)=='string') {
												json = JSON.parse(json);
											}
										}
										if(json.result=='1') {
											$(form).find('[name=photo]').attr('src', json.imgUrl);
											object.setData({'imgUrl': json.imgUrl,'imgSrc': json.imgSrc});
										}
									}).fail(function() {
										window.alert('오류가 발생하였습니다. #2');
									});
								}
							}},
							imgButton: { type: 'button.group', label: '',
								buttons: {
									edit: {
										type: 'button', label: '업로드', callback: function() {
											$(this.getElement()).find('[name=imgSrc]').click();
										}
									},
									del: {
										type: 'button', label: '삭제', callback: function() {
											var form = this.getElement();
											if(window.confirm('삭제 하시겠습니까')) {
												$.ajax({
													type: 'POST',
													url: '/beacon/info/bcImgDel.do',
													dataType: 'json',
													contentType: 'application/x-www-form-urlencoded',
													processData: false,
													data: $(form).serialize()
												}).done(function(json) {
													if(typeof(json)=='string') {
														json = JSON.parse(json);
														if(typeof(json)=='string') {
															json = JSON.parse(json);
														}
													}
													if(json.result=='1') {
											 			$(form).find('[name=photo]').attr('src', '');
														object.setData({'imgUrl': '','imgSrc': ''});
													}
												}).fail(function() {
													window.alert('오류가 발생하였습니다. #2');
												});
											};
										}
									}
								}
							}
						}
					});
					var button = new pnt.ui.builder.Button({text:'수정', data:{form:form}});
					button.on('click' , function() {
						//object.modify(this.get('form').getFormData());
						var formData = this.get('form').getFormData();

						var state = formData.state;
						var stateReason = formData.stateReason;
						var beaconNum = formData.beaconNum;
						//var lastDate = formData.lastDate;
						$.ajax({
							type: 'post',
							url: '/map/beacon/state.do',
							data: {beaconNum:beaconNum, state:state, reason:stateReason},
							dataType: 'json'
						}).done(function(json) {
							if(typeof(json)=='string') {
								json = JSON.parse(json);
								if(typeof(json)=='string') {
									json = JSON.parse(json);
								}
							}
							if(json.result=='1') {
								object.setData({state:state, stateReason:stateReason});
								var originalData = object.getOriginalData();
								object.unload();
								dataEvent.beacon.loadData([originalData]);
								pntmap.getPopup().hide();

							} else if(json.result=='3') {
								window.alert('존재하지 않는 비콘 정보입니다.');
							} else {
								window.alert('오류가 발생하였습니다. #1');
							}
						}).fail(function() {
							window.alert('오류가 발생하였습니다. #2');
						});
					});

					return {body: form.getElement(), foot: button.getElement()};
				}
			},
			scanner: {
				reg: function(data) {
					var form = new pnt.ui.builder.Form({id:'form', field: {
							lat: {type: 'hidden', value: data.lat},
							lng: {type: 'hidden', value: data.lng},
							floor: {type: 'hidden', value: data.floor},
							macAddr: {type: 'text', label: '맥어드레스', value: '', required: true},
							majorVer: {type: 'text', label: 'Major Version', value: '', required: true},
							scannerName: {type: 'text', label: '스캐너명', value: '', required: true},
							sid: {type: 'text', label: '식별', value: ''},
							rssi: {type: 'text', label: 'RSSI', value: '0.0'},
							srssi: {type: 'text', label: 'Sole RSSI', value: '15.0'},
							mrssi: {type: 'text', label: 'Min RSSI', value: '-100.0'},
							drssi: {type: 'text', label: 'MaxDiff RSSI', value: '100.0'},
							exMeter: {type: 'text', label: 'Exclusive Meter', value: '15.0'},
							calPoint: {type: 'text', label: 'Cal point num', value: '4.0'},
							maxSig: {type: 'text', label: 'Max Signal', value: '30.0'},
							maxBuf: {type: 'text', label: 'Max RSSI Buffer', value: '20.0'},
							fwVer: {type: 'text', label: 'Firmware Version', value: '1.0'}
						}
					});
					var button = new pnt.ui.builder.Button({text:'생성', data:{form:form}});
					button.on('click', function() {
						if($(this.get('form').getElement()).valid()) {
							dataEvent.scanner.create(this.get('form').getFormData());
						}
					});

					return {body:form.getElement(), foot:button.getElement()};
				},
				mod: function(object) {
					var form = new pnt.ui.builder.Form({id:'form', field: {
							scannerNum: {type: 'hidden' , value: object.get('scannerNum')},
							macAddr: {type: 'text', label: '맥어드레스', value: object.get('macAddr'), required: true},
							majorVer: {type: 'text', label: 'Major Version', value: object.get('majorVer'), required: true},
							scannerName: {type: 'text', label: '스캐너명', value: object.get('scannerName'), required: true},
							sid: {type: 'text', label: '식별', value: object.get('sid')},
							rssi: {type: 'text', label: 'RSSI', value: object.get('rssi')},
							srssi: {type: 'text', label: 'Sole RSSI', value: object.get('srssi')},
							mrssi: {type: 'text', label: 'Min RSSI', value: object.get('mrssi')},
							drssi: {type: 'text', label: 'MaxDiff RSSI', value: object.get('drssi')},
							exMeter: {type: 'text', label: 'Exclusive Meter', value: object.get('exMeter')},
							calPoint: {type: 'text', label: 'Cal point num', value: object.get('calPoint')},
							maxSig: {type: 'text', label: 'Max Signal', value: object.get('maxSig')},
							maxBuf: {type: 'text', label: 'Max RSSI Buffer', value: object.get('maxBuf')},
							fwVer: {type: 'text', label: 'Firmware Version', value: object.get('fwVer')}
						}
					});
					var button = new pnt.ui.builder.Button({text:'수정', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							object.modify(this.get('form').getFormData());
						}
					}});

					return {body:form.getElement(), foot:button.getElement()};
				}
			},
			geofence:{
				reg: function(data) {
					var fcGroupNumOptions = [{value:'0',text:'[[선택해주세요]]'}];
					var groupList = dataEvent.geofenceGroup.find();
					for(var i=0; i<groupList.length; i++) {
						fcGroupNumOptions[fcGroupNumOptions.length] = {
							value: groupList[i].get('fcGroupNum'),
							text: groupList[i].get('fcGroupName')
						}
					}

					var table = new pnt.ui.builder.Table({});
					table.setHeader(['','보정치','최대횟수']);
					table.addRow(['ENTER','<input type="text" name="evtEnter" value="0" class="form-control input-sm" />',
						'<input type="text" name="numEnter" value="-1" class="form-control input-sm" />']);
					table.addRow(['LEAVE','<input type="text" name="evtLeave" value="0" class="form-control input-sm" />',
						'<input type="text" name="numLeave" value="-1" class="form-control input-sm" />']);
					table.addRow(['STAY','<input type="text" name="evtStay" value="0" class="form-control input-sm" />',
						'<input type="text" name="numStay" value="-1" class="form-control input-sm" />']);

					var form = new pnt.ui.builder.Form({id:'form', field: {
							latlngs: {type: 'hidden', value: data.latlngs},
							floor: {type: 'hidden', value: data.floor},
							fcShape: {type: 'hidden', value: 'P'},
							fcName: {type: 'text', label: '지오펜싱명', value: '', required: true},
							fcGroupNum: {type: 'select', label:'그룹', options:fcGroupNumOptions},
							field1: {type: 'text', label:'field1', value: ''},
							field2: {type: 'text', label:'field2', value: ''},
							field3: {type: 'text', label:'field3', value: ''},
							field4: {type: 'text', label:'field4', value: ''},
							field5: {type: 'text', label:'field5', value: ''},
							evt: {type: 'element', element: table.getElement()}
						}
					});
					var button = new pnt.ui.builder.Button({text:'생성', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							dataEvent.geofence.create(this.get('form').getFormData());
						}
					}});

					return {body:form.getElement(), foot:button.getElement()};
				},
				mod: function(object) {

					var fcGroupNumOptions = [{value:'0',text:'[[선택해주세요]]'}];
					var groupList = dataEvent.geofenceGroup.find();
					for(var i=0; i<groupList.length; i++) {
						fcGroupNumOptions[fcGroupNumOptions.length] = {
							value: groupList[i].get('fcGroupNum'),
							text: groupList[i].get('fcGroupName')
						}
					}
					var fcGroupNum = '';
					if(object.get('groupMapping').length>0) {
						fcGroupNum = object.get('groupMapping')[0].fcGroupNum;
					}

					var table = new pnt.ui.builder.Table({});
					table.setHeader(['','보정치','최대횟수']);
					table.addRow(['ENTER','<input type="number" name="evtEnter" value="'+object.get('evtEnter')+'" class="form-control input-sm" />',
						'<input type="number" name="numEnter" value="'+object.get('numEnter')+'" class="form-control input-sm" />']);
					table.addRow(['LEAVE','<input type="number" name="evtLeave" value="'+object.get('evtLeave')+'" class="form-control input-sm" />',
						'<input type="number" name="numLeave" value="'+object.get('numLeave')+'" class="form-control input-sm" />']);
					table.addRow(['STAY','<input type="number" name="evtStay" value="'+object.get('evtStay')+'" class="form-control input-sm" />',
						'<input type="number" name="numStay" value="'+object.get('numStay')+'" class="form-control input-sm" />']);

					var form = new pnt.ui.builder.Form({id:'form', field: {
							fcNum: {type: 'hidden' , value: object.get('fcNum')},
							fcName: {type: 'text', label: '지오펜싱명', value: object.get('fcName'), required: true},
							fcGroupNum: {type: 'select', label:'그룹', options:fcGroupNumOptions, value:fcGroupNum},
							field1: {type: 'text', label:'field1', value: object.get('field1')||''},
							field2: {type: 'text', label:'field2', value: object.get('field2')||''},
							field3: {type: 'text', label:'field3', value: object.get('field3')||''},
							field4: {type: 'text', label:'field4', value: object.get('field4')||''},
							field5: {type: 'text', label:'field5', value: object.get('field5')||''},
							evt: {type: 'element', element: table.getElement()}
						}
					});
					var button = new pnt.ui.builder.Button({text:'수정', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							object.modify(this.get('form').getFormData());
						}
					}});
					return {body:form.getElement(), foot:button.getElement()};
				}
			},
			area: {
				reg: function(data) {
					var form = new pnt.ui.builder.Form({id:'form', field: {
							latlngs: {type: 'hidden', value: data.latlngs},
							floor: {type: 'hidden', value: data.floor},
							areaName: {type: 'text', label: '구역명', value: '', required: true}
						}
					});
					var button = new pnt.ui.builder.Button({text:'생성', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							dataEvent.area.create(this.get('form').getFormData());
						}
					}});
					return {body:form.getElement(), foot:button.getElement()};
				},
				mod: function(object) {
					var form = new pnt.ui.builder.Form({id:'form', field: {
							areaNum: {type: 'hidden', value: object.get('areaNum')},
							areaName: {type: 'text', label: '구역명', value: object.get('areaName'), required: true}
						}
					});
					var buttons = [];
					buttons[0] = new pnt.ui.builder.Button({text:'수정', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							object.modify(this.get('form').getFormData());
						}
					}});
					buttons[1] = new pnt.ui.builder.Button({text:'노드 업데이트', data:{form:form, object: object}, click: function() {

						var areaNum = this.get('object').get('areaNum');
						var polygon = pntmap.getObjectManager().get(pnt.map.object.type.POLYGON, 'area-'+areaNum);
						console.log('polygon', polygon);
						var geometry = polygon.getGeometry();
						var nodes = pntmap.getObjectManager().findTag('node.beacon,node.scanner');
						if(geometry) {
							for(var i=0; i<nodes.length; i++) {
								if(geometry.intersectsCoordinate(nodes[i].getPosition())) {

									var data = nodes[i].getData();
									data.modify({areaNum:object.get('areaNum'),areaName:object.get('areaName')});
								}
							}
						}
					}});

					var group = new pnt.ui.builder.ButtonGroup({buttons:buttons});
					return {body:form.getElement(), foot:group.getElement()};
				}
			},
			node: {
				reg: function(data) {
					var form = new pnt.ui.builder.Form({id:'form', field:{
							type: {type: 'hidden', value: data.type || 'B'},
							floor: {type: 'hidden', value: data.floor}
						}
					});
					var button = new pnt.ui.builder.InputButton({text:'연속생성', value:'2', data:{form:form},
						click: function() {
							var formData = this.get('form').getFormData();
							var cnt = parseInt(this.getValue());
							if($(this.get('form').getElement()).valid()) {
								pntmap.getLineGenerator().start(function(positions) {

									var latlngs = [];
									for(var i=0; i<positions.length; i++) {
										var lonlat = pnt.util.transformLonLat(positions[i]);
										latlngs.push({lat:lonlat[1], lng:lonlat[0]});
									}

									formData.latlngs = JSON.stringify(latlngs);
									if(formData.type=='S') {
										dataEvent.nodeScanner.create(formData);
									} else {
										dataEvent.nodeBeacon.create(formData);
									}

								}, cnt);
								pntmap.getPopup().hide();
							}
						}
					});
					button.setInputStyle({width:'80px', marginLeft:'5px'});
					var group = new pnt.ui.builder.ButtonGroup({buttons:[button]});
					form.getElement().appendChild(group.getElement());
					return {body:form.getElement()};
				},
				mod: function(object) {
					var cateOption = {'':'선택않함'};
					for(var key in window.POI) {
						cateOption[key] = window.POI[key];
					}

					var form = new pnt.ui.builder.Form({id:'form', field: {
						nodeNum: {type: 'hidden' , value: object.get('nodeNum')},
						nodeName: {type: 'text', label: '노드명', value: object.get('nodeName') || ''},
						nodeID: {type: 'text', label: '노드ID', value: object.get('nodeID'), required:true},
						type: {
							type: 'select', label:'노드타입', options:{B:'비콘 노드',S:'스캐너 노드'},
							value: object.get('type'), readonly: true
						},
						lat: { type: 'text', label:'lat', value: object.get('lat'), readonly:true},
						lng: { type: 'text', label:'lng', value: object.get('lng'), readonly:true},
						cate: {
							type: 'select', label:'카테고리', options:cateOption,
							value: object.get('cate')
						},
						jointName: {type: 'text', label: '교차점명', value: object.get('jointName') || ''},
						areaNum:{type: 'text', label: '구역번호', value: object.get('areaNum') || ''},
						areaName:{type: 'text', label: '구역이름', value: object.get('areaName') || ''}
					}
					});
					var button = new pnt.ui.builder.Button({text:'수정', data:{form:form}, click: function() {
						if($(this.get('form').getElement()).valid()) {
							object.modify(this.get('form').getFormData());
						}
					}});

					return {body:form.getElement(), foot:button.getElement()};
				}
			},
			contents: {
				list: function(param) {
					var contentList = dataEvent.contents.find();
					var contentMappingList = dataEvent.contentsMapping.find(param);
					var contentMapping = {};
					for(var i=0; i<contentMappingList.length; i++) {
						var conNum = contentMappingList[i].get('conNum');
						contentMapping[conNum] = contentMappingList[i];
					}

					var table = new pnt.ui.builder.Table();
					table.setHeader(['할당','No.','컨텐츠명', '타입']);
					for(var i=0; i<contentList.length; i++) {
						var content = contentList[i];

						var checkbox = new pnt.ui.builder.Checkbox({value:content.get('conNum'), data:{param: param}});
						checkbox.on('click', function() {
							this.setEnabled(false);
							if(this.getChecked()) {
								var param = this.get('param');
								param.conNum = this.getValue();
								dataEvent.contentsMapping.create(param, pnt.util.bind(this, function(data) {
									this.setEnabled(true);
									if(typeof(data)!='undefined' && data!=null) {
										this.set('mappingObject', data);
									} else {
										this.setChecked(false);
									}
								}));
							} else {
								var mappingObject = this.get('mappingObject');
								if(typeof(mappingObject)!='undefined') {
									mappingObject.remove(pnt.util.bind(this, function(result) {
										this.setEnabled(true);
										if(result) {
											this.set('mappingObject', undefined);
										} else {
											this.setChecked(true);
										}
									}));
								}
							}
							return true;
						});

						if(typeof(contentMapping[content.get('conNum')])!='undefined') {
							var mappingObject = contentMapping[content.get('conNum')];
							checkbox.set('mappingObject', mappingObject);
							checkbox.setChecked(true);
						}
						table.addRow([checkbox.getElement(), content.get('conNum'),
							content.get('conName'), content.get('conType')]);


					}

					return {body:table.getElement()};
				}
			}
		}



        $(window).ready( function() {




            window.pntmap = new pnt.map.OfflineMap('map-canvas', {
                usetile: true
            });

			window.prop = new pnt.util.Properties();



			prop.put('map.cursor.mode', 1);
			prop.put('map.visible.node.beacon', false);
			prop.put('map.visible.node.scanner', false);
			prop.put('map.visible.area', false);
			var paramShow = pnt.util.split(pnt.util.getUrlParameter('show'),',');
			if(pnt.util.inArray('beacon', paramShow)) {
				prop.put('map.visible.beacon', true);
			} else {
				prop.put('map.visible.beacon', false);
			}
			if(pnt.util.inArray('scanner', paramShow)) {
				prop.put('map.visible.scanner', true);
			} else {
				prop.put('map.visible.scanner', false);
			}
			if(pnt.util.inArray('geofence', paramShow)) {
				prop.put('map.visible.geofence', true);
			} else {
				prop.put('map.visible.geofence', false);
			}



			/**
			 * ================================================================================
			 * 옵션 변경 핸들러
			 * ================================================================================
			 */
			prop.on('map.visible.beacon', 'modify', function(id, value) {
				var beacons = pntmap.getObjectManager().findTag('beacon');
				for(var i=0; i<beacons.length; i++) {
					if(value==true) {
						beacons[i].show();
					} else {
						beacons[i].hide();
					}
				}
			});
			prop.on('map.visible.scanner', 'modify', function(id, value) {
				var scanners = pntmap.getObjectManager().findTag('scanner');
				for(var i=0; i<scanners.length; i++) {
					if(value==true) {
						scanners[i].show();
					} else {
						scanners[i].hide();
					}
				}
			});
			prop.on('map.visible.geofence', 'modify', function(id, value) {
				var geofences = pntmap.getObjectManager().findTag('geofence');
				for(var i=0; i<geofences.length; i++) {
					if(value==true) {
						geofences[i].show();
					} else {
						geofences[i].hide();
					}
				}
			});
			prop.on('map.visible.node.beacon', 'modify', function(id, value) {
				var nodes = pntmap.getObjectManager().findTag('node.beacon');
				for(var i=0; i<nodes.length; i++) {
					if(value==true) {
						nodes[i].show();
					} else {
						nodes[i].hide();
					}
				}
				var nodeEdges = pntmap.getObjectManager().findTag('nodeedge.beacon');
				for(var i=0; i<nodeEdges.length; i++) {
					if(value==true) {
						nodeEdges[i].show();
					} else {
						nodeEdges[i].hide();
					}
				}
			});
			prop.on('map.visible.node.scanner', 'modify', function(id, value) {
				var nodes = pntmap.getObjectManager().findTag('node.scanner');
				for(var i=0; i<nodes.length; i++) {
					if(value==true) {
						nodes[i].show();
					} else {
						nodes[i].hide();
					}
				}
				var nodeEdges = pntmap.getObjectManager().findTag('nodeedge.scanner');
				for(var i=0; i<nodeEdges.length; i++) {
					if(value==true) {
						nodeEdges[i].show();
					} else {
						nodeEdges[i].hide();
					}
				}
			});
			prop.on('map.visible.area', 'modify', function(id, value) {
				var areas = pntmap.getObjectManager().findTag('area');
				for(var i=0; i<areas.length; i++) {
					if(value==true) {
						areas[i].show();
					} else {
						areas[i].hide();
					}
				}
			});
			prop.on('map.cursor.mode', 'modify', function(id, value) {

				if (value==2) {
					pntmap.setCursorLabel('비콘노드 생성');
					pntmap.setDragMode(1);
				}
				else if (value==3) {
					pntmap.setCursorLabel('스캐너노드 생성');
					pntmap.setDragMode(1);
				}
				else if(value==4) {
					pntmap.setCursorLabel('이동');
					pntmap.setDragMode(2);
				} else {
					pntmap.setCursorLabel(null);
					pntmap.setDragMode(1);
				}
			});



			/**
			 * ================================================================================
			 * 메뉴 설정
			 * ================================================================================
			 */
			window.defaultMenu = {
				M00: {text:'커서모드', submenu: {
						SM01: {
							text: '일반', callback: function () {
								prop.put('map.cursor.mode', 1);
							}
						},
						SM02: {text: '비콘노드 생성', callback: function() {
								prop.put('map.cursor.mode', 2);
							}
						},
						SM03: {text: '스캐너노드 생성', callback: function() {
								prop.put('map.cursor.mode', 3);
							}
						},
						SM04: {text: '이동', callback: function() {
								prop.put('map.cursor.mode', 4);
							}
						}
					}
				},
				M01: {text:'생성', submenu: {
						SC01: {text: '비콘 생성', callback: function() {
							var lonlat = pnt.util.transformLonLat(this.getPosition());
							var data = {lat:lonlat[1], lng:lonlat[0], floor:floorManager.getCurrentFloor()};
							pntmap.getPopup().title('비콘 등록');
							pntmap.getPopup().setBodyFoot(form.beacon.reg(data));
							pntmap.getPopup().show();
						}},
						SC02: {text: '스캐너 생성', callback: function() {
							var lonlat = pnt.util.transformLonLat(this.getPosition());
							var data = {lat:lonlat[1], lng:lonlat[0], floor:floorManager.getCurrentFloor()};
							pntmap.getPopup().title('스캐너 등록');
							pntmap.getPopup().setBodyFoot(form.scanner.reg(data));
							pntmap.getPopup().show();
						}},
						SC03: {text: '지오펜스 생성', callback: function() {
							pntmap.getPolygonGenerator().start(function(coords) {
								var latlngs = [];
								for(i=0; i<coords.length; i++) {
									var lonlat = pnt.util.transformLonLat(coords[i]);
									var latlng = {lat:lonlat[1], lng:lonlat[0], orderSeq:i};
									latlngs.push(latlng);
								}
								var data = {latlngs:JSON.stringify(latlngs), floor:floorManager.getCurrentFloor()};
								pntmap.getPopup().title('지오펜스 등록');
								pntmap.getPopup().setBodyFoot(form.geofence.reg(data));
								pntmap.getPopup().show();
							});
						}},
						SC04: {text: '구역 생성', callback: function() {
							pntmap.getPolygonGenerator().start(function(coords) {
								var latlngs = [];
								for(i=0; i<coords.length; i++) {
									var lonlat = pnt.util.transformLonLat(coords[i]);
									var latlng = {lat:lonlat[1], lng:lonlat[0], orderSeq:i};
									latlngs.push(latlng);
								}
								var data = {latlngs:JSON.stringify(latlngs), floor:floorManager.getCurrentFloor()};
								pntmap.getPopup().title('구역 등록');
								pntmap.getPopup().setBodyFoot(form.area.reg(data));
								pntmap.getPopup().show();
							});
						}},
						SC05: {text: '비콘노드 생성', callback: function() {
							var lonlat = pnt.util.transformLonLat(this.getPosition());
							var data = {type:'B', lat:lonlat[1], lng:lonlat[0], floor:floorManager.getCurrentFloor()};
							pntmap.getPopup().title('노드 등록');
							pntmap.getPopup().setBodyFoot(form.node.reg(data));
							pntmap.getPopup().show();
						}},
						SC06: {text: '스캐너노드 생성', callback: function() {
							var lonlat = pnt.util.transformLonLat(this.getPosition());
							var data = {type:'S', lat:lonlat[1], lng:lonlat[0], floor:floorManager.getCurrentFloor()};
							pntmap.getPopup().title('노드 등록');
							pntmap.getPopup().setBodyFoot(form.node.reg(data));
							pntmap.getPopup().show();
						}}
					}
				},
				M02: {text: '보기', submenu: {
						SV06: {text: prop.get('map.visible.beacon')==true?'비콘 감추기':'비콘 보기', callback: function(menu) {
							prop.put('map.visible.beacon', !prop.get('map.visible.beacon'));
							menu.setText(prop.get('map.visible.beacon')?'비콘 감추기':'비콘 보기');
						}},
						SV07: {text: prop.get('map.visible.scanner')==true?'스캐너 감추기':'스캐너 보기', callback: function(menu) {
							prop.put('map.visible.scanner', !prop.get('map.visible.scanner'));
							menu.setText(prop.get('map.visible.scanner')?'스캐너 감추기':'스캐너 보기');
						}},
						SV08: {text: prop.get('map.visible.geofence')==true?'지오펜스 감추기':'지오펜스 보기', callback: function(menu) {
							prop.put('map.visible.geofence', !prop.get('map.visible.geofence'));
							menu.setText(prop.get('map.visible.geofence')?'지오펜스 감추기':'지오펜스 보기');
						}},
						SV09: {text: prop.get('map.visible.area')==true?'구역 감추기':'구역 보기', callback: function(menu) {
							prop.put('map.visible.area', !prop.get('map.visible.area'));
							menu.setText(prop.get('map.visible.area')?'구역 감추기':'구역 보기');
						}},
						SV10: {text: prop.get('map.visible.node.beacon')==true?'비콘노드 감추기':'비콘노드 보기', callback: function(menu) {
							prop.put('map.visible.node.beacon', !prop.get('map.visible.node.beacon'));
							menu.setText(prop.get('map.visible.node.beacon')?'비콘노드 감추기':'비콘노드 보기');
						}},
						SV11: {text: prop.get('map.visible.node.scanner')==true?'스캐너노드 감추기':'스캐너노드 보기', callback: function(menu) {
							prop.put('map.visible.node.scanner', !prop.get('map.visible.node.scanner'));
							menu.setText(prop.get('map.visible.node.scanner')?'스캐너노드 감추기':'스캐너노드 보기');
						}}
					}
				}
				/*,03: {text: '선택도구', callback: function(menu) {
					pntmap.getPolygonGenerator().start(function(coords) {
						console.log(coords);
						pntmap.getContextMenu().show(coords[coords.length-1], {
							01: {text: '노드 가로정렬', callback: pnt.util.bind(this, function() {

							})},
							02: {text: '노드 세로정렬', callback: pnt.util.bind(this, function() {

							})}
						});
					});
				}}*/
			};
			var enable = pnt.util.split(pnt.util.getUrlParameter('enable'),',');
			if(!pnt.util.inArray('beacon', enable)) {
				delete defaultMenu['M01'].submenu['SC01'];
				delete defaultMenu['M02'].submenu['SV06'];
			}
			if(!pnt.util.inArray('scanner', enable)) {
				delete defaultMenu['M01'].submenu['SC02'];
				delete defaultMenu['M02'].submenu['SV07'];
			}
			if(!pnt.util.inArray('geofence', enable)) {
				delete defaultMenu['M01'].submenu['SC03'];
				delete defaultMenu['M02'].submenu['SV08'];
			}
			if(!pnt.util.inArray('area', enable)) {
				delete defaultMenu['M01'].submenu['SC04'];
				delete defaultMenu['M02'].submenu['SV09'];
			}
			if(!pnt.util.inArray('node.beacon', enable)) {
				delete defaultMenu['M01'].submenu['SC05'];
				delete defaultMenu['M02'].submenu['SV10'];
				delete defaultMenu['M00'].submenu['SM02'];
			}
			if(!pnt.util.inArray('node.scanner', enable)) {
				delete defaultMenu['M01'].submenu['SC06'];
				delete defaultMenu['M02'].submenu['SV11'];
				delete defaultMenu['M00'].submenu['SM03'];
			}
			pntmap.getContextMenu().setDefaultMenu(defaultMenu);





			/**
			 * 지도 이벤트 처리
			 */
			pntmap.on('click', function(evt) {
				var lonlat = pnt.util.transformLonLat(evt.coordinate);
				var mode = prop.get('map.cursor.mode');
				if(mode==2) {
					// 비콘노드 생성
					var nodeInfo = {
						type:'B',
						floor:floorManager.getCurrentFloor(),
						latlngs:JSON.stringify([{lat:lonlat[1],lng:lonlat[0]}])
					}
					dataEvent.nodeBeacon.create(nodeInfo);
				} else if(mode==3) {
					// 스캐너노드 생성
					var nodeInfo = {
						type:'S',
						floor:floorManager.getCurrentFloor(),
						latlngs:JSON.stringify([{lat:lonlat[1],lng:lonlat[0]}])
					}
					dataEvent.nodeScanner.create(nodeInfo);
				}

			});
			pntmap.on('vmarker.click', function(evt) {
				if(evt.object.containsTag('beacon')) {
					pntmap.getPopup().title('비콘 정보');
					pntmap.getPopup().setBodyFoot(form.beacon.mod(evt.object.getData()));
					pntmap.getPopup().show();
				}
				else if(evt.object.containsTag('node.beacon') || evt.object.containsTag('node.scanner')) {
					pntmap.getPopup().title('노드 정보');
					pntmap.getPopup().setBodyFoot(form.node.mod(evt.object.getData()));
					pntmap.getPopup().show();
				}
				else if(evt.object.containsTag('scanner')) {
					pntmap.getPopup().title('스캐너 정보');
					pntmap.getPopup().setBodyFoot(form.scanner.mod(evt.object.getData()));
					pntmap.getPopup().show();
				}
			});
			pntmap.on('vmarker.rightclick', function(evt) {
				if(evt.object.containsTag('beacon')) {
					pntmap.getContextMenu().show(evt.object.getPosition(), {
						01: {text: '비콘 삭제', callback: pnt.util.bind(evt.object, function() {
							this.getData().remove();
						})},
						02: {text: '컨텐츠 할당', callback: pnt.util.bind(evt.object, function() {
							var param = {
								refType: 'BC', refSubType: 'DETECT',
								refNum: evt.object.getData().get('beaconNum')
							}
							pntmap.getPopup().title('비콘 - 컨텐츠 할당');
							pntmap.getPopup().setBodyFoot(form.contents.list(param));
							pntmap.getPopup().show();
						})},
						03: {text: '설치 정보', callback: pnt.util.bind(evt.object, function() {
							pntmap.getPopup().title('비콘 설치정보');
							pntmap.getPopup().setBodyFoot(form.beacon.status(evt.object.getData()));
							pntmap.getPopup().show();
						})}
					});
				}
				else if(evt.object.containsTag('scanner')) {
					pntmap.getContextMenu().show(evt.object.getPosition(), {
						01: {text: '스캐너 삭제', callback: pnt.util.bind(evt.object, function() {
							this.getData().remove();
						})}
					});
				}
				else if(evt.object.containsTag('node.beacon') || evt.object.containsTag('node.scanner')) {
					pntmap.getContextMenu().show(evt.object.getPosition(), {
						01: {text: '노드 삭제', callback: pnt.util.bind(evt.object, function() {
							var lines = this.getData().get('lines');
							if(typeof(lines)!='undefined' && lines!=null) {
								for (var i = 0; i<lines.length; i++) {
									lines[i].getData().remove();
								}
							}
							this.getData().remove();
						})},
						02: {text: '연결 제거', callback: pnt.util.bind(evt.object, function() {
							var lines = this.getData().get('lines');
							if(typeof(lines)!='undefined' && lines!=null) {
								for (var i = 0; i<lines.length; i++) {
									lines[i].getData().remove();
								}
							}
						})},
						03: {text: '컨텐츠 할당', callback: pnt.util.bind(evt.object, function() {
							var param = {
								refType: 'ND', refSubType: 'DETECT',
								refNum: evt.object.getData().get('nodeNum')
							}
							pntmap.getPopup().title('스캐너노드 - 컨텐츠 할당');
							pntmap.getPopup().setBodyFoot(form.contents.list(param));
							pntmap.getPopup().show();
						})},
					});
				}
			});
			pntmap.on('vmarker.dragend', function(evt) {
				var lonlat = pnt.util.transformLonLat(evt.coordinate);
				if(evt.object.containsTag('beacon')) {
					evt.object.getData().modify({lat:lonlat[1], lng:lonlat[0]});
				}
				else if(evt.object.containsTag('scanner')) {
					evt.object.getData().modify({lat:lonlat[1], lng:lonlat[0]});
				}
				else if(evt.object.containsTag('node.beacon')) {
					var lines = evt.object.getData().get('lines', []);
					for(var i=0; i<lines.length; i++) {
						var line = lines[i];
						var startMarker = line.getData().get('startMarker');
						var endMarker = line.getData().get('endMarker');

						line.setPosition(startMarker.getPosition(), endMarker.getPosition());
					}
					evt.object.getData().modify({lat:lonlat[1], lng:lonlat[0]});
				}
				else if(evt.object.containsTag('node.scanner')) {
					var lines = evt.object.getData().get('lines', []);
					for(var i=0; i<lines.length; i++) {
						var line = lines[i];
						var startMarker = line.getData().get('startMarker');
						var endMarker = line.getData().get('endMarker');

						line.setPosition(startMarker.getPosition(), endMarker.getPosition());
					}
					evt.object.getData().modify({lat:lonlat[1], lng:lonlat[0]});
				}
			});
			pntmap.on('polygon.click', function(evt) {
				if(evt.object.containsTag('geofence')) {
					pntmap.getPopup().title('지오펜스 정보');
					pntmap.getPopup().setBodyFoot(form.geofence.mod(evt.object.getData()));
					pntmap.getPopup().show();
				}
				else if(evt.object.containsTag('area')) {
					pntmap.getPopup().title('구역 정보');
					pntmap.getPopup().setBodyFoot(form.area.mod(evt.object.getData()));
					pntmap.getPopup().show();
				}
			});
			pntmap.on('polygon.rightclick', function(evt) {
				if(evt.object.containsTag('geofence')) {
					pntmap.getContextMenu().show(evt.coordinate, {
						01: {text: '지오펜스 삭제', callback: pnt.util.bind(evt.object, function() {
							this.getData().remove();
						})},
						02: {text: evt.object.isEditable()?'폰리곤 편집 해제':'폰리곤 편집', callback: pnt.util.bind(evt.object, function(menu) {

							if(this.isEditable()) {
								this.setEditable(false);
							} else {
								this.setEditable(true);
							}
						}, this)},
						03: {text: '진입이벤트 컨텐츠 할당', callback: pnt.util.bind(evt.object, function() {
							var param = {
								refType: 'GF', refSubType: 'ENTER',
								refNum: evt.object.getData().get('fcNum')
							}
							pntmap.getPopup().title('지오펜스 - 진입이벤트 컨텐츠 할당 ');
							pntmap.getPopup().setBodyFoot(form.contents.list(param));
							pntmap.getPopup().show();
						})},
						04: {text: '머무름이벤트 컨텐츠 할당', callback: pnt.util.bind(evt.object, function() {
							var param = {
								refType: 'GF', refSubType: 'STAY',
								refNum: evt.object.getData().get('fcNum')
							}
							pntmap.getPopup().title('지오펜스 - 머무름이벤트 컨텐츠 할당 ');
							pntmap.getPopup().setBodyFoot(form.contents.list(param));
							pntmap.getPopup().show();
						})},
						05: {text: '퇴장이벤트 컨텐츠 할당', callback: pnt.util.bind(evt.object, function() {
							var param = {
								refType: 'GF', refSubType: 'LEAVE',
								refNum: evt.object.getData().get('fcNum')
							}
							pntmap.getPopup().title('지오펜스 - 퇴장이벤트 컨텐츠 할당 ');
							pntmap.getPopup().setBodyFoot(form.contents.list(param));
							pntmap.getPopup().show();
						})}
					});
				}
				else if(evt.object.containsTag('area')) {
					pntmap.getContextMenu().show(evt.coordinate, {
						01: {text: '구역 삭제', callback: pnt.util.bind(evt.object, function() {
							this.getData().remove();
						})},
						02: {text: evt.object.isEditable()?'폰리곤 편집 해제':'폰리곤 편집', callback: pnt.util.bind(evt.object, function(menu) {

							if(this.isEditable()) {
								this.setEditable(false);
							} else {
								this.setEditable(true);
							}
						}, this)}
					});
				}
			});
			pntmap.on('polygon.dragend', function(evt) {
				if(evt.object.containsTag('geofence') || evt.object.containsTag('area')) {
					var coordinates = evt.object.getCoordinates()[0];
					var latlngs = [];
					for(var i=0; i<coordinates.length; i++) {
						var lonlat = pnt.util.transformLonLat(coordinates[i]);
						latlngs.push({
							lat: lonlat[1],
							lng: lonlat[0],
						})
					}
					evt.object.getData().modify({latlngs:latlngs});
				}
			});
			pntmap.on('polygon.modifyend', function(evt) {
				if(evt.object.containsTag('geofence') || evt.object.containsTag('area')) {
					var coordinates = evt.object.getCoordinates()[0];
					var latlngs = [];
					for(var i=0; i<coordinates.length; i++) {
						var lonlat = pnt.util.transformLonLat(coordinates[i]);
						latlngs.push({
							lat: lonlat[1],
							lng: lonlat[0],
						})
					}
					evt.object.getData().modify({latlngs:latlngs});
				}
			});
			pntmap.on('vmarker.connect', function(evt) {
				var startMarker = evt.startObject;
				var endMarker = evt.endObject;

				if(startMarker.getData().get('type')==endMarker.getData().get('type')) {
					var nodeType = startMarker.getData().get('type');
					if(typeof(nodeType)!='undefined' && nodeType!=null && nodeType!='') {
						var nodeObject = null;
						if(nodeType=='B') {
							nodeObject = dataEvent.nodeEdgeBeacon;
						}
						else if(nodeType=='S') {
							nodeObject = dataEvent.nodeEdgeScanner;
						}
						if(nodeObject!=null) {
							nodeObject.create({
								floor: floorManager.getCurrentFloor(),
								startPoint: startMarker.getData().get('nodeID'),
								endPoint: endMarker.getData().get('nodeID'),
								type: startMarker.getData().get('type')
							});
						}
					}
				}
			});
			pntmap.on('line.rightclick', function(evt) {
				console.log('line.rightclick', evt);
			});




			/**
			 * ================================================================================
			 * 데이터 변화 핸들러
			 * ================================================================================
			 */
			window.dataEvent = {
				beacon: new pnt.util.DataEvent('beacon'),
				beaconGroup: new pnt.util.DataEvent('beaconGroup'),
				scanner: new pnt.util.DataEvent('scanner'),
				geofence: new pnt.util.DataEvent('geofence'),
				geofenceGroup: new pnt.util.DataEvent('geofenceGroup'),
				area: new pnt.util.DataEvent('area'),
				nodeBeacon: new pnt.util.DataEvent('nodeBeacon'),
				nodeEdgeBeacon: new pnt.util.DataEvent('nodeEdgeBeacon'),
				nodeScanner: new pnt.util.DataEvent('nodeScanner'),
				nodeEdgeScanner: new pnt.util.DataEvent('nodeEdgeScanner'),
				contents: new pnt.util.DataEvent('contents'),
				contentsMapping: new pnt.util.DataEvent('contentsMapping')
			}
			dataEvent.beacon.on('load', function(data) {

				/**
				 * 비콘 상태에 따른 아이콘 설정
				 */
				var markerIcon = icon.beacon;

				var enable = pnt.util.split(pnt.util.getUrlParameter('enable'),',');
				if(pnt.util.inArray('beacon.status', enable)) {
					var mtime = 60*60*24*30; // 30일 동안 업데이트 되지 않은 경우 주황색 마크로 표시
					var nowtime = new Date().getTime()/1000;

					if(data.get('state')=='2') {
						markerIcon = '${viewProperty.staticUrl}' + '/images/inc/marker_red_10.png';
					} else if(data.get('state')=='1') {
						markerIcon = '${viewProperty.staticUrl}' + '/images/inc/marker_green_10.png';
					} else {
						if(nowtime-data.get('lastDate')>=mtime) {
							markerIcon = '${viewProperty.staticUrl}'+'/images/inc/marker_orange_10.png';
						} else {
							markerIcon = '${viewProperty.staticUrl}'+'/images/inc/marker_blue_10.png';
						}
					}
				}

				var pos = [data.get('lng'),data.get('lat')];
				var id = data.get('UUID')+'-'+data.get('majorVer')+'-'+data.get('minorVer');
				var label = data.get('beaconName');
				if(data.get('groupNames')!='') {
					label += '('+data.get('groupNames')+')';
				}

				var marker = pntmap.getObjectManager().get(pnt.map.object.type.VMARKER, 'beacon-'+id);
				if(marker) {
					marker.changeIcon(markerIcon);
				} else {
					var options = {
						position: pnt.util.transformCoordinates(pos), label:label, data:data,
						tag:'beacon', url: markerIcon,
						labelStyle: {
							backgroundColor:'rgba(255, 230, 230, 0.8)'
						}
					};
					var beacon = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'beacon-'+id, options);
					if(beacon) {
						beacon.setDraggable(true);
						var visible = prop.get('map.visible.beacon');
						if(visible==false) {
							beacon.hide();
						}
					}
				}


			});
			dataEvent.beacon.on('unload', function(data) {
				var objectId = 'beacon-'+data.get('UUID')+'-'+data.get('majorVer')+'-'+data.get('minorVer');
				pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, objectId);
			});

			dataEvent.beacon.on('create', function(data) {
				pnt.util.post(urls.beacon.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.beacon.loadData([response.data]);
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.beacon.on('modify', function(data) {
				pnt.util.post(urls.beacon.modify, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
							dataEvent.beacon.loadData([response.data]);
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.beacon.on('remove', function(data) {
				pnt.util.post(urls.beacon.remove, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.scanner.on('load', function(data) {
				var pos = [typeof(data.get('lng'))=='string'?parseFloat(data.get('lng')):data.get('lng')
					,typeof(data.get('lat'))=='string'?parseFloat(data.get('lat')):data.get('lat')];
				var id = data.get('scannerNum');
				var options = {
					position: pnt.util.transformCoordinates(pos), label:data.get('scannerName'), data: data,
					tag:'scanner', url: icon.scanner,
					labelStyle: {
						backgroundColor:'rgba(230, 255, 230, 0.8)'
					}
				};
				var scanner = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'scanner-'+id, options);
				if(scanner) {
					scanner.setDraggable(true);
					var visible = prop.get('map.visible.scanner');
					if(visible==false) {
						scanner.hide();
					}
				}
			});
			dataEvent.scanner.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'scanner-'+data.get('scannerNum'));
			});
			dataEvent.scanner.on('create', function(data) {
				pnt.util.post(urls.scanner.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.scanner.loadData([response.data]);
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.scanner.on('modify', function(data) {
				pnt.util.post(urls.scanner.modify, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
                            data.unload();
                            dataEvent.scanner.loadData([response.data]);
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.scanner.on('remove', function(data) {
				pnt.util.post(urls.scanner.remove, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});

			dataEvent.geofence.on('load', function(data) {
				var latlngs = data.get('latlngs');
				var coords = [];
				if(typeof(latlngs)!='undefined' && latlngs!=null) {
					for (var i = 0; i < latlngs.length; i++) {
						coords.push(pnt.util.transformCoordinates([latlngs[i].lng, latlngs[i].lat]));
					}
				}

				var label = data.get('fcName');
				if(data.get('groupNames')!='') {
					label += '('+data.get('groupNames')+')';
				}
				var geofence = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, 'fence-'+data.get('fcNum'), {
					label: label, data: data, coords:coords,
					tag:'geofence', fill:{color:[255,0,0,0.1]}, stroke:{color:[255,0,0,0.5],width:1}
				});
				if(geofence) {
					geofence.setDraggable(true);
					var visible = prop.get('map.visible.geofence');
					if(visible==false) {
						geofence.hide();
					}
				}

			});
			dataEvent.geofence.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, 'fence-'+data.get('fcNum'));
			});
			dataEvent.geofence.on('create', function(data) {
				pnt.util.post(urls.geofence.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.geofence.loadData([response.data]);
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.geofence.on('modify', function(data) {
				var originalData = data.getOriginalData();
				if(typeof(originalData.latlngs)!='string') {
					originalData.latlngs = JSON.stringify(originalData.latlngs);
				}
				pnt.util.post(urls.geofence.modify, originalData, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							pntmap.getPopup().hide();
							var label = response.data.fcName;
							if(response.data.groupNames!='') {
								label += '('+response.data.groupNames+')';
							}
							data.setData(response.data);
							var geofence = pntmap.getObjectManager().get(pnt.map.object.type.POLYGON, 'fence-'+response.data.fcNum);
							geofence.setLabelText(label);
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.geofence.on('remove', function(data) {
				pnt.util.post(urls.geofence.remove, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});

			dataEvent.area.on('load', function(data) {
				var latlngs = data.get('latlngs');
				var coords = [];
				if(typeof(latlngs)!='undefined' && latlngs!=null) {
					for(var i=0;i<latlngs.length; i++) {
						coords.push(pnt.util.transformCoordinates([latlngs[i].lng, latlngs[i].lat]));
					}
				}

				var area = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, 'area-'+data.get('areaNum'), {
					label: data.get('areaName'), data: data,
					coords:coords, tag:'area', fill:{color:[147, 67, 251,0.1]}, stroke:{color:[147, 67, 251,0.5],width:1}
				});
				if(area) {
					area.setDraggable(true);
					var visible = prop.get('map.visible.area');
					if(visible==false) {
						area.hide();
					}
				}


			});
			dataEvent.area.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, 'area-'+data.get('areaNum'));
			});
			dataEvent.area.on('create', function(data) {
				pnt.util.post(urls.area.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.area.loadData([response.data]);
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.area.on('modify', function(data) {
				var originalData = data.getOriginalData();
				if(typeof(originalData.latlngs)!='string') {
					originalData.latlngs = JSON.stringify(originalData.latlngs);
				}
				pnt.util.post(urls.area.modify, originalData, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							var objectId = 'area-'+data.get('areaNum');
							var area = pntmap.getObjectManager().get(pnt.map.object.type.POLYGON, objectId);
							area.setLabelText(data.get('areaName'));
							pntmap.getPopup().hide();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.area.on('remove', function(data) {
				pnt.util.post(urls.area.remove, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});


			dataEvent.nodeBeacon.on('load', function(data) {

				var pos = [data.get('lng'),data.get('lat')];
				var options = {
					position: pnt.util.transformCoordinates(pos), data:data, label: '',
					tag:'node.beacon', url: icon.nodeBeacon,
					labelStyle: {
						backgroundColor:'rgba(255, 230, 230, 0.8)'
					}
				};
				var node = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'node.beacon-'+data.get('nodeID'), options);
				if(node) {
					node.setDraggable(true);
					node.enableVmc(true);
					var visible = prop.get('map.visible.node.beacon');
					if(visible==false) {
						node.hide();
					}

					var labelString = [];
					if(data.get('cate')) {
						if(typeof(window.POI[data.get('cate')])!='undefined') {
							labelString.push('['+window.POI[data.get('cate')]+']');
						}
					}
					if(data.get('nodeName')) {
						labelString.push(data.get('nodeName'));
					}
					if(data.get('jointName')) {
						labelString.push('('+data.get('jointName')+')');
					}
					if(data.get('areaName')) {
						labelString.push('<'+data.get('areaName')+'>');
					}
					if(labelString.length>0) {
						node.showLabel(labelString.join(''));
					}
				}

			});
			dataEvent.nodeBeacon.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'node.beacon-'+data.get('nodeID'));
			});
			dataEvent.nodeBeacon.on('create', function(data) {
				data.type = 'B';
				pnt.util.post(urls.node.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							if(typeof(response.data.length)!='undefined' && response.data.length>0) {
								dataEvent.nodeBeacon.loadData(response.data);
							} else {
								dataEvent.nodeBeacon.loadData([response.data]);
							}
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeBeacon.on('modify', function(data) {
				pnt.util.post(urls.node.modify, data.getOriginalData(), {
					success: function(response) {
						console.log('response.data', response.data)
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							pntmap.getPopup().hide();
							var node = pntmap.getObjectManager().get(pnt.map.object.type.VMARKER, 'node.beacon-'+response.data.nodeID);
							if(node) {
								var labelString = [];
								if (response.data.cate) {
									if (typeof(window.POI[response.data.cate]) != 'undefined') {
										labelString.push('['+window.POI[response.data.cate]+']');
									}

								}
								if(response.data.nodeName) {
									labelString.push(response.data.nodeName);
								}
								if(response.data.jointName) {
									labelString.push('('+response.data.jointName+')');
								}
								if(response.data.areaName) {
									labelString.push('<'+response.data.areaName+'>');
								}
								if (labelString.length > 0) {
									node.showLabel(labelString.join(''));
								} else {
									node.showLabel('');
								}
							}
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeBeacon.on('remove', function(data) {
				var param = {nodeNum: data.get('nodeNum')};
				pnt.util.post(urls.node.remove, param, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});

			dataEvent.nodeEdgeBeacon.on('load', function(data) {

				var startMarker = pntmap.getObjectManager().get('vmarker','node.beacon-'+data.get('startPoint'));
				var endMarker = pntmap.getObjectManager().get('vmarker','node.beacon-'+data.get('endPoint'));

				data.setData({
					startMarker: startMarker,
					endMarker: endMarker
				});

				var startPos = startMarker.getPosition();
				var endPos = endMarker.getPosition();
				if(startPos && endPos) {
					var options = {
						data: data,
						coords:[startPos, endPos], tag:'nodeedge.beacon', stroke:{color:[210, 41, 41,1],width:1}
					}
					var edge = pntmap.getObjectManager().create(pnt.map.object.type.LINE, 'nodeedge.beacon-'+data.get('edgeNum'), options);
					if(edge) {
						var visible = prop.get('map.visible.node.beacon');
						if (visible == false) {
							edge.hide();
						}

						startMarker.getData().get('lines', []).push(edge);
						endMarker.getData().get('lines', []).push(edge);
					}
				}
			});
			dataEvent.nodeEdgeBeacon.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.LINE, 'nodeedge.beacon-'+data.get('edgeNum'));
			});
			dataEvent.nodeEdgeBeacon.on('create', function(data) {
				pnt.util.post(urls.nodeedge.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.nodeEdgeBeacon.loadData([response.data]);
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeEdgeBeacon.on('remove', function(data) {
				var param = {edgeNum: data.get('edgeNum')};
				pnt.util.post(urls.nodeedge.remove, param, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});



			dataEvent.nodeScanner.on('load', function(data) {

				var pos = [data.get('lng'),data.get('lat')];
				var options = {
					position: pnt.util.transformCoordinates(pos), data:data, label: data.get('areaName'),
					tag:'node.scanner', url: icon.nodeScanner,
					labelStyle: {
						backgroundColor:'rgba(255, 230, 230, 0.8)'
					}
				};
				var node = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'node.scanner-'+data.get('nodeID'), options);
				if(node) {
					node.setDraggable(true);
					node.enableVmc(true);
					var visible = prop.get('map.visible.node.scanner');
					if(visible==false) {
						node.hide();
					}

					var labelString = [];
					if(data.get('cate')) {
						if(typeof(window.POI[data.get('cate')])!='undefined') {
							labelString.push('['+window.POI[data.get('cate')]+']');
						}
					}
					if(data.get('nodeName')) {
						labelString.push(data.get('nodeName'));
					}
					if(data.get('jointName')) {
						labelString.push('('+data.get('jointName')+')');
					}
					if(data.get('areaName')) {
						labelString.push('<'+data.get('areaName')+'>');
					}

					if(labelString.length>0) {
						node.showLabel(labelString.join(''));
					}
				}

			});
			dataEvent.nodeScanner.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'node.scanner-'+data.get('nodeID'));
			});
			dataEvent.nodeScanner.on('create', function(data) {
				//data.type = 'S';
				pnt.util.post(urls.node.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
                            if(typeof(response.data.length)!='undefined' && response.data.length>0) {
                                dataEvent.nodeScanner.loadData(response.data);
                            } else {
							    dataEvent.nodeScanner.loadData([response.data]);
                            }
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeScanner.on('modify', function(data) {
				pnt.util.post(urls.node.modify, data.getOriginalData(), {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							pntmap.getPopup().hide();
							var node = pntmap.getObjectManager().get(pnt.map.object.type.VMARKER, 'node.scanner-'+response.data.nodeID);
							if(node) {
								var labelString = [];
								if (response.data.cate) {
									if (typeof(window.POI[response.data.cate]) != 'undefined') {
										labelString.push('['+window.POI[response.data.cate]+']');
									}
								}
								if(response.data.nodeName) {
									labelString.push(response.data.nodeName);
								}
								if(response.data.jointName) {
									labelString.push('('+response.data.jointName+')');
								}
								if(response.data.areaName) {
									labelString.push('<'+response.data.areaName+'>');
								}
								if (labelString.length > 0) {
									node.showLabel(labelString.join(''));
								} else {
									node.showLabel('');
								}
							}
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeScanner.on('remove', function(data) {
				var param = {nodeNum: data.get('nodeNum')};
				pnt.util.post(urls.node.remove, param, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});

			dataEvent.nodeEdgeScanner.on('load', function(data) {

				var startMarker = pntmap.getObjectManager().get('vmarker','node.scanner-'+data.get('startPoint'));
				var endMarker = pntmap.getObjectManager().get('vmarker','node.scanner-'+data.get('endPoint'));

				data.setData({
					startMarker: startMarker,
					endMarker: endMarker
				});

				var startPos = startMarker.getPosition();
				var endPos = endMarker.getPosition();
				if(startPos && endPos) {
					var options = {
						data: data,
						coords:[startPos, endPos], tag:'nodeedge.scanner', stroke:{color:[210, 41, 41,1],width:1}
					}
					var edge = pntmap.getObjectManager().create(pnt.map.object.type.LINE, 'nodeedge.scanner-'+data.get('edgeNum'), options);
					if(edge) {
						var visible = prop.get('map.visible.node.scanner');
						if(visible==false) {
							edge.hide();
						}

						startMarker.getData().get('lines', []).push(edge);
						endMarker.getData().get('lines', []).push(edge);
					}
				}
			});
			dataEvent.nodeEdgeScanner.on('unload', function(data) {
				pntmap.getObjectManager().remove(pnt.map.object.type.LINE, 'nodeedge.scanner-'+data.get('edgeNum'));
			});
			dataEvent.nodeEdgeScanner.on('create', function(data) {
				pnt.util.post(urls.nodeedge.create, data, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							dataEvent.nodeEdgeScanner.loadData([response.data]);
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.nodeEdgeScanner.on('remove', function(data) {
				var param = {edgeNum: data.get('edgeNum')};
				pnt.util.post(urls.nodeedge.remove, param, {
					success: function(response) {
						if(response.result!='1') {
							alert('실패하였습니다.');
						} else {
							data.unload();
						}
					},
					error: function(error) {
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.contentsMapping.on('create', function(param, callback) {
				pnt.util.post(urls.contents.createMapping, param, {
					success: function(response) {
						if(response.result!='1') {
							callback(null);
							alert('실패하였습니다.');
						} else {
							var dataset = dataEvent.contentsMapping.loadData([response.data]);
							if(typeof(callback)!='undefined') {
								callback(dataset[0]);
							}
						}
					},
					error: function(error) {
						callback(null);
						alert('실패하였습니다.');
					}
				});
			});
			dataEvent.contentsMapping.on('remove', function(data, callback) {
				var param = {conNum:data.get('conNum'), refType:data.get('refType'),
					refSubType:data.get('refSubType'), refNum:data.get('refNum')
				};
				pnt.util.post(urls.contents.removeMapping, param, {
					success: function(response) {
						if(response.result!='1') {
							callback(false);
							alert('실패하였습니다.');
						} else {
							data.unload();
							if(typeof(callback)!='undefined') {
								callback(true);
							}
						}
					},
					error: function(error) {
						callback(false);
						alert('실패하였습니다.');
					}
				});
			});

            var areaManager = new pnt.map.AreaManager(pntmap);
            var floorManager = new pnt.map.FloorManager(pntmap, {
                autofit: true
            });
            this.floorManager = floorManager;
            areaManager.setFloorManager(floorManager);


			window.dataLoader = new pnt.util.DataLoader();
			dataLoader.addUrl('beaconList', urls.beacon.list);
			dataLoader.addUrl('beaconGroupList', urls.beacon.groupList);
			dataLoader.addUrl('scannerList', urls.scanner.list);
			dataLoader.addUrl('nodeBeaconList', urls.node.list, {type:'B'});
			dataLoader.addUrl('nodeEdgeBeaconList', urls.nodeedge.list, {type:'B'});
			dataLoader.addUrl('nodeScannerList', urls.node.list, {type:'S'});
			dataLoader.addUrl('nodeEdgeScannerList', urls.nodeedge.list, {type:'S'});
			dataLoader.addUrl('geofenceList', urls.geofence.list);
			dataLoader.addUrl('geofenceGroupList', urls.geofence.groupList);
			dataLoader.addUrl('contentsList', urls.contents.list);
			dataLoader.addUrl('contentsMappingList', urls.contents.mappingList);

			window.floorDataLoader = new pnt.util.DataLoader();
			floorDataLoader.addUrl('floorList', urls.floor.list);
			floorDataLoader.addUrl('areaList', urls.area.list);
			floorDataLoader.load(function(id, data, complete, error) {
				if(complete==true) {
					var floorData = this.getData('floorList').data;
                    floorManager.load(floorData);
					floorManager.onChange(function(event) {

						dataEvent.beacon.unload();
						dataEvent.scanner.unload();
						dataEvent.geofence.unload();
						dataEvent.area.unload();
						dataEvent.nodeBeacon.unload();
						dataEvent.nodeEdgeBeacon.unload();
						dataEvent.nodeScanner.unload();
						dataEvent.nodeEdgeScanner.unload();
						dataEvent.contents.unload();
						dataEvent.contentsMapping.unload();
						dataEvent.beaconGroup.unload();
						dataEvent.geofenceGroup.unload();

						var floorCondition = {floor: event.floor};
						if(floorDataLoader.getData('areaList').result=='1') {
							var areaData = pnt.util.findArrayData(floorDataLoader.getData('areaList').data, floorCondition);
							dataEvent.area.loadData(areaData);
						}
						dataLoader.reload(floorCondition);
					});

                    /*var areaData = this.getData('areaList').data;
                    if(typeof(areaData)!='undefined' && areaData!=null && areaData.length>0) {
                        areaManager.load(areaData);
                    } else {
                        floorManager.changeDefault();
                    }*/
					floorManager.changeDefault();

				}
			});

			dataLoader.onLoad(function(id, data, complete, error) {
				if(id=='beaconList') {
					dataEvent.beacon.loadData(this.getData('beaconList').data || []);
				}
				if(id=='beaconGroupList') {
					dataEvent.beaconGroup.loadData(this.getData('beaconGroupList').data || []);
				}
				else if(id=='scannerList') {
					dataEvent.scanner.loadData(this.getData('scannerList').data || []);
				}
				else if(id=='geofenceList') {
					dataEvent.geofence.loadData(this.getData('geofenceList').data || []);
				}
				if(id=='geofenceGroupList') {
					dataEvent.geofenceGroup.loadData(this.getData('geofenceGroupList').data || []);
				}
				else if(id=='contentsList') {
					dataEvent.contents.loadData(this.getData('contentsList').data || []);
				}
				else if(id=='contentsMappingList') {
					dataEvent.contentsMapping.loadData(this.getData('contentsMappingList').data || []);
				}

				if(complete==true) {
					dataEvent.nodeBeacon.loadData(this.getData('nodeBeaconList').data || []);
					dataEvent.nodeEdgeBeacon.loadData(this.getData('nodeEdgeBeaconList').data || []);
					dataEvent.nodeScanner.loadData(this.getData('nodeScannerList').data || []);
					dataEvent.nodeEdgeScanner.loadData(this.getData('nodeEdgeScannerList').data || []);
				}
			});

            window.pntmap = pntmap;


			$(window).resize(function() {
				var windowHeight = $(window).height();
				var offsetTop = $('#map-canvas').offset().top;
				$('#map-canvas').height(windowHeight-offsetTop-46);
			});
			$(window).trigger('resize');
			pntmap.getOlMap().updateSize();
        });


    </script>

</section>
</body>
</html>
