<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/beacon.css" />
    <style type="text/css">@import url('${viewProperty.staticUrl}/external/js.tablesort/tablesort.css');</style>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.js"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('/');
		elementHandler.staticUrl = '${viewProperty.staticUrl}/';
        $(document).ready( function() {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
    <%--<script type="text/javascript" src="/js/map/bplan.js"></script>--%>
    <script type="text/javascript">

        GoogleMap.prototype.createNodeMarker = function(nodeInfo) {

            var icon = elementHandler.staticUrl+'images/inc/marker_blue_10.png';
            var latlng = new google.maps.LatLng(nodeInfo.lat, nodeInfo.lng);
            if(nodeInfo.nodeName) icon = elementHandler.staticUrl+'images/inc/marker_green_10.png';
            var marker = elementHandler.googleMap.createMarker('node', 'node_'+nodeInfo.nodeID, latlng, {icon:icon}, nodeInfo, function() {
                if(elementHandler.mode==elementHandler.EDITMODE_MOVE) {
                    this.changeDraggable(false);
                }
                this.onCreateComplete(true);
            });
            if($('#checkbox-map-visible-node').prop('checked')!=true) {
                marker.setVisible(false);
            }

            google.maps.event.addListener(marker, 'click', function() {
                elementHandler.mapPopup.remote('map/nodeInfo.ajax.do',{nodeNum:this.data.nodeNum}, function($element) {
                    $element.find('#nodeModBtn').click(function() {
                        var $form = $('#map-popup-form-node');
                        if($form.valid()) {
                            if($('#joinName').val()=='') $('#joinName').val('-');
                            $.ajax({ type: "POST",
                                contentType: 'application/x-www-form-urlencoded',
                                processData: false,
                                url: elementHandler.mapPopup.baseurl + 'map/modifyNode.do',
                                data: $form.serialize(),
                                success: function(data) {
									var result;
									if(typeof(data)=='string') {
										result = $.parseJSON(data);
										if(typeof(result)=='string') {
											result = $.parseJSON(result);
										}
									} else {
										result = data;
									}

                                    var responseCode = result.result;
                                    if(responseCode=='1') {
                                        common.success('수정되었습니다.');
                                    } else if (responseCode != '1') {
                                        common.error("오류 발생하였습니다.");
                                    }
                                }
                            });
                        }
                    });

                });
                elementHandler.mapPopup.setOnClose(function() {
                    elementHandler.currentObject = null;
                });
                elementHandler.mapPopup.show();
            });
            google.maps.event.addListener(marker, 'mouseover', function() {
                var content = String(this.data.nodeID);
                if(typeof this.data.nodeName!='undefined' && this.data.nodeName!=null && this.data.nodeName!='') {
                    content += '<br />노드명:'+this.data.nodeName;
                }
                if(typeof this.data.areaName!='undefined' && this.data.areaName!=null && this.data.areaName!='') {
                    content += '<br />구역명:'+this.data.areaName;
                }

                var infoWindow = this.getGoogleMap().getShapeObject('infoWindow','moverwindow','moverwindow');
                console.log(content);
                if(infoWindow==false) {
                    infoWindow = this.getGoogleMap().createInfoWindow('moverwindow','moverwindow', content, this.data);
                    infoWindow.open(this.getMap(), marker);
                } else {
                    infoWindow.setContent(content);
                    infoWindow.data = this.data;
                    infoWindow.open(this.getMap(), marker);
                }
            });
            google.maps.event.addListener(marker, 'mouseout', function() {
                var infoWindow = this.getGoogleMap().getShapeObject('infoWindow','moverwindow','moverwindow');
                if(infoWindow!=false) {
                    infoWindow.close();
                }
            });

            return marker;
        }

        GoogleMap.prototype.onClickAreaPolygon = function(polygon) {
            var googleMap = this;
            elementHandler.mapPopup.remote('map/areaform.ajax.do',{areaNum:polygon.data.areaNum}, function($element) {

                var areaPolygons = googleMap.getShapeObjects('polygon', 'area');
                for(var i=0; i<areaPolygons.length; i++) {
                    areaPolygons[i].changeEditable(false);
                    areaPolygons[i].changeDraggable(false);
                }
                polygon.changeEditable(true);
                polygon.changeDraggable(true);

                $element.find('#modBtn').on('click', function() {

                    var areaNum = $element.find('#areaNum').val();
                    var floor = googleMap.floorService.getCurrentFloor();
                    var areaName= $element.find('#areaName').val();
                    var latlngJson = JSON.stringify(polygon.data.areaLatlngs);

                    $.ajax({ type: "POST",
                        contentType: 'application/x-www-form-urlencoded',
                        processData: true,
                        cache: false,
                        url: elementHandler.mapPopup.baseurl + 'map/area/mod.do',
                        data: {areaNum: areaNum, areaName: areaName, floor: floor, latlngJson: latlngJson},
                        dataType: 'json',
                        success: function(result) {
							if(typeof(result)=='string') {
								result = $.parseJSON(result);
								if(typeof(result)=='string') {
									result = $.parseJSON(result);
								}
							}
                            var responseCode = result.result;
                            if(responseCode=='1') {

                                polygon.changeEditable(false);
                                polygon.changeDraggable(false);
                                elementHandler.mapPopup.hide();

                            } else if (responseCode != '1') {
                                window.alert("오류 발생하였습니다.");
                            }
                        }
                    });
                });

                $element.find('#delBtn').on('click', function() {

                    var areaNum = $element.find('#areaNum').val();
                    $('#btn-area-list').children().each(function(index, element) {
                        if(areaNum==$(element).data('area-num')) {
                            $(element).remove();
                        }
                    });

                    var polygons = googleMap.getShapeObjects('polygon','area');
                    var areaPolygon = null;
                    for(var k=0; k<polygons.length; k++) {
                        var polygon = polygons[k];
                        if(polygon.editable==true) {
                            areaPolygon = polygon;
                            break;
                        }
                    }

                    var nodes = googleMap.getShapeObjects('marker','node');
                    var inNodes = [];
                    var inNodeMarkers = [];
                    for(var k=0; k<nodes.length; k++) {
                        if(google.maps.geometry.poly.containsLocation(nodes[k].getPosition(), areaPolygon)==true) {
                            inNodes.push(nodes[k].data.nodeNum);
                            inNodeMarkers.push(nodes[k]);
                        }
                    }

                    var areaName= $element.find('#areaName').val();
                    if(inNodes.length>0) {
                        $.ajax({ type: "POST",
                            contentType: 'application/x-www-form-urlencoded',
                            processData: true,
                            cache: false,
                            url: elementHandler.mapPopup.baseurl + 'map/area/nodeUpdateBatch.do',
                            data: {areaName: null, areaNum: null, nodeNumArray: inNodes},
                            dataType: 'json',
                            success: function(result) {
								if(typeof(result)=='string') {
									result = $.parseJSON(result);
									if(typeof(result)=='string') {
										result = $.parseJSON(result);
									}
								}
                                console.log(inNodeMarkers);
                                for(var i=0; i<inNodeMarkers.length; i++) {
                                    inNodeMarkers[i].data.areaName = null;
                                }
                            }
                        });
                    }

                    $.ajax({ type: "POST",
                        contentType: 'application/x-www-form-urlencoded',
                        processData: true,
                        cache: false,
                        url: elementHandler.mapPopup.baseurl + 'map/area/del.do',
                        data: {areaNum: areaNum},
                        dataType: 'json',
                        success: function(result) {
							if(typeof(result)=='string') {
								result = $.parseJSON(result);
								if(typeof(result)=='string') {
									result = $.parseJSON(result);
								}
							}
                            var responseCode = result.result;
                            if(responseCode=='1') {
                                areaPolygon.remove();
                                elementHandler.mapPopup.hide();
                            } else if (responseCode != '1') {
                                window.alert("오류 발생하였습니다.");
                            }
                        }
                    });

                });

                $element.find('#nodeUpdateBtn').on('click', function() {

                    var polygons = googleMap.getShapeObjects('polygon','area');
                    var areaPolygon = null;
                    for(var k=0; k<polygons.length; k++) {
                        var polygon = polygons[k];
                        if(polygon.editable==true) {
                            areaPolygon = polygon;
                            break;
                        }
                    }

                    var nodes = googleMap.getShapeObjects('marker','node');
                    var inNodes = [];
                    var inNodeMarkers = [];
                    for(var k=0; k<nodes.length; k++) {
                        if(google.maps.geometry.poly.containsLocation(nodes[k].getPosition(), areaPolygon)==true) {
                            inNodes.push(nodes[k].data.nodeNum);
                            inNodeMarkers.push(nodes[k]);
                        }
                    }

                    if(inNodes.length<=0) {
                        window.alert('노드가 존재하지 않습니다.');
                        return;
                    }

                    var areaName= $element.find('#areaName').val();
                    var areaNum= $element.find('#areaNum').val();
                    $.ajax({ type: "POST",
                        contentType: 'application/x-www-form-urlencoded',
                        processData: true,
                        cache: false,
                        url: elementHandler.mapPopup.baseurl + 'map/area/nodeUpdateBatch.do',
                        data: {areaName: areaName, areaNum:areaNum, nodeNumArray: inNodes},
                        dataType: 'json',
                        success: function(result) {
							if(typeof(result)=='string') {
								result = $.parseJSON(result);
								if(typeof(result)=='string') {
									result = $.parseJSON(result);
								}
							}
                            var responseCode = result.result;
                            if(responseCode=='1') {
                                console.log(inNodeMarkers);
                                for(var i=0; i<inNodeMarkers.length; i++) {
                                    inNodeMarkers[i].data.areaName = areaName;
                                }
                                window.alert("정상적으로 업데이트되었습니다.");
                            } else if (responseCode != '1') {
                                window.alert("오류 발생하였습니다.");
                            }
                        }
                    });

                });
            });
            elementHandler.mapPopup.setOnClose(function() {
                var areaPolygons = googleMap.getShapeObjects('polygon', 'area');
                for(var i=0; i<areaPolygons.length; i++) {
                    areaPolygons[i].changeEditable(false);
                    areaPolygons[i].changeDraggable(false);
                }
            });
            elementHandler.mapPopup.show();
        }

        GoogleMap.prototype.onChangeAreaPolygon = function(polygon) {

            polygon.data.areaLatlngs = [];
            var path = polygon.getPath();
            for(var i=0; i<path.length; i++) {
                var latlng = path.getAt(i);
                polygon.data.areaLatlngs.push({
                    lat: latlng.lat(),
                    lng: latlng.lng(),
                    orderSeq: i
                });
            }
            console.log('polygon', polygon);
            console.log('polygon.data.areaLatlngs', polygon.data.areaLatlngs);
        }

        elementHandler.ready(function() {

            /**
             * 노드, 비콘 검색
             * 2015-04-01 nohsoo
             */
            elementHandler.searchForm = (function($) {
                var showed = false;
                var searchOption = 'node';
                var infoWindows = [];

                $('#search-option-node').click(function() {
                    elementHandler.searchForm.changeOption('node');
                });
                $('#search-hide-btn').click(function() {
                    elementHandler.searchForm.hide();
                });
                $('#search-btn').click(function() {
                    elementHandler.searchForm.search();
                });

                $('#search-keyword').keypress(function(event) {
                    var code = event.keyCode || event.which;
                    if(code == 13) {
                        elementHandler.searchForm.search();
                    }
                });

                return {
                    changeOption: function(option) {
                        if(option=='node') {
                            $('#search-menu-btn').html('노드 <span class="caret"></span>');
                            searchOption = option;
                        }
                    },
                    search: function(keyword) {
                        if(showed==false) {
                            this.show();
                        } else {
                            var word = keyword || $('#search-keyword').val();

                            if(word) {
                                elementHandler.searchForm.clearInfoWindow();
                                var matchCount = 0;
                                if(searchOption=='node') {

                                    var nodeMarkers = elementHandler.googleMap.getShapeObjects('marker','node');
                                    for(var i=0;i<nodeMarkers.length; i++) {
                                        var nodeName = nodeMarkers[i].data.nodeName;
                                        var nodeID = nodeMarkers[i].data.nodeID;
                                        if (String(nodeName).match(eval('/'+word+'.*/'))) {
                                            matchCount++;
                                            elementHandler.searchForm.createInfoWindow(nodeMarkers[i], nodeName);
                                        } else if (String(nodeID).match(eval('/'+word+'.*/'))) {
                                            matchCount++;
                                            elementHandler.searchForm.createInfoWindow(nodeMarkers[i], nodeID);
                                        }
                                    }
                                }
                                if(matchCount==0) {
                                    window.alert('찾을 수 없습니다.');
                                }
                            }

                        }
                    },
                    show: function() {
                        showed = true;
                        $('#search-option-group').show();
                        $('#search-keyword').show();
                    },
                    hide: function() {
                        showed = false;
                        $('#search-option-group').hide();
                        $('#search-keyword').hide();
                    },
                    createInfoWindow: function(marker, content) {
                        var infoWindow = new google.maps.InfoWindow({
                            content: String(content),
                            marker: marker
                        });
                        infoWindow.open(elementHandler.googleMap.getMap(), marker);
                        infoWindows.push(infoWindow);
                    },
                    clearInfoWindow: function() {
                        for(var i=0; i<infoWindows.length; i++) {
                            infoWindows[i].close();
                        }
                    }
                }
            })($);
            elementHandler.searchForm.hide();

            elementHandler.EDITMODE_READONLY = 1;
            elementHandler.EDITMODE_AREA_CREATE = 2;
            elementHandler.editmode = elementHandler.EDITMODE_READONLY;

            elementHandler.serviceRequest = new ServiceRequest(elementHandler.baseurl);
            elementHandler.serviceRequest.bindLoadingBar('map-loading-bar');

            elementHandler.googleMap = new GoogleMap('map-canvas', function() {
                var googleMap = this;

                /**
                 * 팝업윈도우
                 */
                elementHandler.mapPopup = new MapPopup('map-popup', elementHandler.baseurl);

                /**
                 * 상단 버튼 목록 맵에 표시
                 */
                var mapControls = this.getMap().controls;
                var modeButtons = document.getElementById('map-mode-buttons');
                mapControls[google.maps.ControlPosition.TOP_CENTER].push(modeButtons);

                /**
                 * 레프트 버튼
                 */
                var mapViewOptions = document.getElementById('map-view-options');
                mapControls[google.maps.ControlPosition.LEFT_TOP].push(mapViewOptions);



                /**
                 * 팝업 윈도우 최상위 엘리멘트를 구글맵에 삽입
                 */
                mapControls[google.maps.ControlPosition.RIGHT_TOP].push(elementHandler.mapPopup.element);

                /**
                 * 층 서비스
                 */
                this.floorService = new FloorService(this, elementHandler.serviceRequest);
                this.floorService.bindFloorSelectBox('floor-selector');
                this.floorService.setOnChangeFloor(function(floorNum) {
                    /**
                     * 오브젝트 초기화
                     */
                    /*googleMap.getMarkerCluster().clearMarkers();*/
                    googleMap.clearShapeObject('marker', 'node');
                    googleMap.clearShapeObject('polygon', 'area');
                    googleMap.clearShapeObject('infoWindow', 'infowindow-areaname');

                    /**
                     * 노드 로드
                     */
                    elementHandler.serviceRequest.get('map/nodeAll.json', {floor:floorNum,type:'S'}, function(response) {
                        for(var i=0; i<response.nodes.length; i++) {
                            var node = response.nodes[i];
                            googleMap.createNodeMarker(node);
                        }
                    });

                    /**
                     * 구역정보
                     */
                    elementHandler.serviceRequest.get('map/area/list.do', {floor:floorNum}, function(response) {
                        console.log(response);

                        $('#btn-area-list').html('');

                        for(var i=0; i<response.list.length; i++) {
                            var areaLatlng = response.list[i].areaLatlngs;
                            var path = [];
                            for(var j=0; j<areaLatlng.length; j++) {
                                path.push({lat: areaLatlng[j].lat, lng: areaLatlng[j].lng});
                            }
                            var areaPolygon = googleMap.createPoygon('area', 'area-'+response.list[i].areaNum, path, response.list[i]);
                            areaPolygon.getPath().polygon = areaPolygon;
                            if($('#checkbox-map-visible-area').prop('checked')!=true) {
                                areaPolygon.setVisible(false);
                            }
                            areaPolygon.addListener('click', function(event) {
                                googleMap.onClickAreaPolygon(this);
                            });
                            areaPolygon.addListener('dragend', function() {
                                googleMap.onChangeAreaPolygon(this);
                            });
                            google.maps.event.addListener(areaPolygon.getPath(), 'insert_at', function() {
                                googleMap.onChangeAreaPolygon(this.polygon);
                            });
                            google.maps.event.addListener(areaPolygon.getPath(), 'remove_at', function() {
                                googleMap.onChangeAreaPolygon(this.polygon);
                            });
                            google.maps.event.addListener(areaPolygon.getPath(), 'set_at', function() {
                                googleMap.onChangeAreaPolygon(this.polygon);
                            });
                            google.maps.event.addListener(areaPolygon, 'rightclick', function(mev){
                                if (mev.vertex != null && this.getPath().getLength() > 3) {
                                    this.getPath().removeAt(mev.vertex);
                                }
                            });


                            // 구역정보 InfoWindow
                            var infoWindow = googleMap.createInfoWindow('infowindow-areaname', 'infowindow-areaname'+response.list[i].areaNum, response.list[i].areaName, response.list[i]);
                            infoWindow.setPosition(areaPolygon.getPath().getAt(0));
                            infoWindow.open(googleMap.getMap());

                            // 구역 노드 선택 강조
                            var label = $('<label class="btn btn-default btn-sm" data-area-num="'+response.list[i].areaNum+'">'+response.list[i].areaName+'<label>');
                            var input = $('<input type="radio" name="options" autocomplete="off" data-area-name="'+response.list[i].areaName+'" />');
                            input.on('change', function() {
                                var areaName = $(this).data('area-name');
                                var nodes = elementHandler.googleMap.getShapeObjects('marker','node');
                                for(var i=0; i<nodes.length; i++) {
                                    var node = nodes[i];
                                    if(node.data.areaName==areaName) {
                                        var icon = elementHandler.staticUrl+'images/inc/marker_red_10.png';
                                        node.setIcon(icon);
                                    } else {
                                        var icon = elementHandler.staticUrl+'images/inc/marker_blue_10.png';
                                        if(node.data.nodeName) icon = elementHandler.staticUrl+'images/inc/marker_green_10.png';
                                        node.setIcon(icon);
                                    }
                                }
                            });
                            label.append(input);
                            $('#btn-area-list').append(label);
                        }
                    });

                });

                /**
                 * 구글지도 클릭 이벤트
                 */
                this.addListener('click', function(event) {
                    if(elementHandler.mode == elementHandler.EDITMODE_AREA_CREATE) {

                    }
                });



                /**
                 * 펜스 그리기 완성 처리, InfoWindow 표시
                 */
                googleMap.getPolygonDrawingManager().setOnCompleteHandler(function() {

                    elementHandler.mapPopup.remote('map/areaform.ajax.do',{}, function($element) {

                        $element.find('#regBtn').on('click', function() {

                            var path = googleMap.getPolygonDrawingManager().getPolygon().getPath();
                            var areaLatlngList = [];
                            for(var i=0; i<path.length; i++) {
                                var latlng = path.getAt(i);
                                var areaLatlng = {
                                    lat: latlng.lat(),
                                    lng: latlng.lng(),
                                    orderSeq: i
                                }
                                areaLatlngList.push(areaLatlng);
                            }
                            var floor = elementHandler.googleMap.floorService.getCurrentFloor();
                            var areaName= $element.find('#areaName').val();
                            var latlngJson = JSON.stringify(areaLatlngList);

                            $.ajax({ type: "POST",
                                contentType: 'application/x-www-form-urlencoded',
                                processData: true,
                                cache: false,
                                url: elementHandler.mapPopup.baseurl + 'map/area/reg.do',
                                data: {areaName: areaName, floor: floor, latlngJson: latlngJson},
                                dataType: 'json',
                                success: function(result) {
									if(typeof(result)=='string') {
										result = $.parseJSON(result);
										if(typeof(result)=='string') {
											result = $.parseJSON(result);
										}
									}
                                    var responseCode = result.result;
                                    if(responseCode=='1') {

                                        var path = googleMap.getPolygonDrawingManager().getPolygon().getPath();
                                        var polygon = googleMap.createPoygon('area', 'area-'+result.area.areaNum, path, result.area);
                                        path.polygon = polygon;
                                        if($('#checkbox-map-visible-area').prop('checked')!=true) {
                                            polygon.setVisible(false);
                                        }
                                        polygon.addListener('click', function(event) {
                                            googleMap.onClickAreaPolygon(this);
                                        });
                                        polygon.addListener('dragend', function() {
                                            googleMap.onChangeAreaPolygon(this);
                                        });
                                        google.maps.event.addListener(polygon.getPath(), 'insert_at', function() {
                                            googleMap.onChangeAreaPolygon(this.polygon);
                                        });
                                        google.maps.event.addListener(polygon.getPath(), 'remove_at', function() {
                                            googleMap.onChangeAreaPolygon(this.polygon);
                                        });
                                        google.maps.event.addListener(polygon.getPath(), 'set_at', function() {
                                            googleMap.onChangeAreaPolygon(this.polygon);
                                        });
                                        google.maps.event.addListener(polygon, 'rightclick', function(mev){
                                            if (mev.vertex != null && this.getPath().getLength() > 3) {
                                                this.getPath().removeAt(mev.vertex);
                                                googleMap.onChangeAreaPolygon(this.polygon);
                                            }
                                        });

                                        elementHandler.mapPopup.hide();

                                        googleMap.getPolygonDrawingManager().clear();
                                        googleMap.getPolygonDrawingManager().startDrawing();

                                        // 구역 노드 선택 강조
                                        var label = $('<label class="btn btn-default btn-sm" data-area-num="'+result.area.areaNum+'">'+result.area.areaName+'<label>');
                                        var input = $('<input type="radio" name="options" autocomplete="off" data-area-name="'+result.area.areaName+'" />');
                                        input.on('change', function() {
                                            var areaName = $(this).data('area-name');
                                            var nodes = elementHandler.googleMap.getShapeObjects('marker','node');
                                            for(var i=0; i<nodes.length; i++) {
                                                var node = nodes[i];
                                                if(node.data.areaName==areaName) {
                                                    var icon = elementHandler.staticUrl+'images/inc/marker_red_10.png';
                                                    node.setIcon(icon);
                                                } else {
                                                    var icon = elementHandler.staticUrl+'images/inc/marker_blue_10.png';
                                                    if(node.data.nodeName) icon = elementHandler.staticUrl+'images/inc/marker_green_10.png';
                                                    node.setIcon(icon);
                                                }
                                            }
                                        });
                                        label.append(input);
                                        $('#btn-area-list').append(label);
                                    } else if (responseCode != '1') {
                                        window.alert("오류 발생하였습니다.");
                                    }
                                }
                            });
                        });
                    });
                    elementHandler.mapPopup.setOnClose(function() {
                        googleMap.getPolygonDrawingManager().clear();
                        googleMap.getPolygonDrawingManager().startDrawing();
                    });
                    elementHandler.mapPopup.show();

                    console.log('drawingManager complete');
                });


                this.floorService.load();
                this.getMap().setZoom(20);

            });
        });

        elementHandler.bind({
            'btn-map-mode1': {
                event : 'change',
                action: function() {
                    if($(this).prop('checked')==true) {
                        var polygonDrawingManager = elementHandler.googleMap.getPolygonDrawingManager();
                        polygonDrawingManager.stopDrawing();
                        polygonDrawingManager.setVisible(false);
                    }
                }
            },
            'btn-map-mode2': {
                event : 'change',
                action: function() {
                    if($(this).prop('checked')==true) {
                        var polygonDrawingManager = elementHandler.googleMap.getPolygonDrawingManager();
                        if (!polygonDrawingManager.isComplete()) {
                            polygonDrawingManager.startDrawing();
                        }
                        polygonDrawingManager.setVisible(true);
                    }
                }
            },
            'checkbox-map-visible-area': {
                event : 'change',
                action: function() {
                    if($(this).prop('checked')!=true) {
                        elementHandler.googleMap.eachShapeObject('polygon', 'area', function(polygon) {
                            polygon.hide();
                        });
                    } else {
                        elementHandler.googleMap.eachShapeObject('polygon', 'area', function(polygon) {
                            polygon.show();
                        });
                    }
                }
            },
            'checkbox-map-visible-node': {
                event : 'change',
                action: function() {
                    if($(this).prop('checked')!=true) {
                        elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                            marker.hide();
                        });
                    } else {
                        elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                            marker.show();
                        });
                    }
                }
            }
        });

    </script>
</head>
<body>


<header id="topbar" class="alt">
	<div class="topbar-left">
		<ol class="breadcrumb">
			<li class="crumb-active"><a href="###"><spring:message code="menu.120800" /></a></li>
			<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="crumb-trail"><spring:message code="menu.120000" /></li>
			<li class="crumb-trail"><spring:message code="menu.120800" /></li>
		</ol>
	</div>
	<div class="topbar-right">
		<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
	</div>
</header>

<section id="content" class="animated fadeIn">
	<div id="map-canvas" style="width:100%;height:700px;"></div>
	<div class="hide">
		<div id="map-popup" style="display:none;padding:10px; margin:10px; overflow-y:auto; overflow-x:hidden; width:500px; height:auto; border: 1px solid #cccccc; background-color:#ffffff;"></div>
		<div id="map-view-options" style="margin:10px;">

			<div id="btn-area-list"  class="btn-group" data-toggle="buttons" >
			</div>
			<div class="clearfix"></div>
			<div class="btn-group-vertical" data-toggle="buttons" style="margin-top: 10px;">
				<label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
					<input type="checkbox" id="checkbox-map-visible-area" autocomplete="off" checked> 구역 표시
				</label>
				<label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
					<input type="checkbox" id="checkbox-map-visible-node" autocomplete="off" checked> 노드 표시
				</label>
			</div>

			<div class="input-group" style="margin-top: 10px;">
				<div id="search-form" class="input-group input-group-sm">
					<div id="search-option-group" class="input-group-btn">
						<button type="button" id="search-menu-btn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">노드 <span class="caret"></span></button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="javascript:;" id="search-option-node">노드</a></li>
							<li class="divider"></li>
							<li><a href="javascript:;" id="search-hide-btn">감추기</a></li>
						</ul>
					</div>
					<input type="text" id="search-keyword" class="form-control input-sm" style="width:100px;" />
					<span class="input-group-btn">
						<button id="search-btn" class="btn btn-default btn-sm">
							검 색
						</button>
					</span>
				</div>
			</div>
		</div>
		<div id="map-mode-buttons" style="margin:10px;">
			층 선택
			<select id="floor-selector" class="input-sm" style="margin-top:4px;">
			</select>
			<div  class="btn-group" data-toggle="buttons">
				<label class="btn btn-default btn-sm active">
					<input type="radio" name="options" id="btn-map-mode1" autocomplete="off" checked> 편집
				</label>
				<label class="btn btn-default btn-sm">
					<input type="radio" name="options" id="btn-map-mode2" autocomplete="off"> 구역생성
				</label>
			</div>

			<div id="map-loading-bar" class="center" style="margin-top:10px;">
				Loading ...
			</div>
		</div>


	</div>
</section>

</body>
</html>