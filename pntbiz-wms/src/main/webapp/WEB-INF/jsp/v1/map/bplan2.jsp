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
    <link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/_global/google.map.markerclusterer.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
        GoogleMap.setDefaultCenter('<sec:authentication property="principal.lat" htmlEscape="false"/>','<sec:authentication property="principal.lng" htmlEscape="false"/>');
    </script>
    <%--<script type="text/javascript" src="${viewProperty.staticUrl}/js/map/bplan.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>--%>
    <script type="text/javascript">

        elementHandler.Node = (function($) {
            return {
                createMarker: function(undo, nodeInfo) {
                    var icon = 'http://static.pntbiz.com/indoorplus/'+'images/inc/marker_blue_10.png';
                    var latlng = new google.maps.LatLng(nodeInfo.lat, nodeInfo.lng);
                    if(nodeInfo.nodeName) icon = 'http://static.pntbiz.com/indoorplus/'+'images/inc/marker_green_10.png';
                    var marker = elementHandler.googleMap.createMarker('node', 'node_'+nodeInfo.nodeID, latlng, {icon:icon}, nodeInfo, function() {
                        if(elementHandler.mode==elementHandler.EDITMODE_MOVE) {
                            this.changeDraggable(true);
                        }
                        this.setOnUpdateProcHandler(function(marker) {
                            /**
                             * 노드 이동 처리
                             */
                            var param = {
                                nodeNum: marker.data.nodeNum,
                                lat: marker.getPosition().lat(),
                                lng: marker.getPosition().lng()
                            };

                            /**
                             * 노드 이동 되돌리기
                             */
                            elementHandler.mapUndoManager.pushJob(function(data) {
                                data.marker.setPosition(data.originalLatLng);
                                var param = {
                                    nodeNum: marker.data.nodeNum,
                                    lat: data.marker.getPosition().lat(),
                                    lng: data.marker.getPosition().lng()
                                };
                                elementHandler.serviceRequest.post('map/modifyNode.do', param, function(response) {});
                            }, {originalLatLng: new google.maps.LatLng(marker.data.lat, marker.data.lng), marker: marker});
                            /** end **/

                            elementHandler.serviceRequest.post('map/modifyNode.do', param, function(response) {
                                marker.data.lat = param.lat;
                                marker.data.lng = param.lng;
                                marker.onUpdateComplete(true);
                            });
                        });
                        this.onCreateComplete(true);
                    });
                    /**
                     * 노드마크 오른쪽 마우스 클릭시 삭제 처리
                     * 연결된 라인도 삭제되지만 되돌리기시 삭제된 라인을 복구 되지 않음.
                     */
                    marker.addListener('rightclick', function() {
                        this.setIcon('http://static.pntbiz.com/indoorplus/'+'images/inc/marker_gray_10.png');
                        var nodeMarker = this;
                        elementHandler.serviceRequest.post('map/delNode.do', {nodeNum:this.data.nodeNum}, function(response) {
                            nodeMarker.remove();

                            /**
                             * 라인 삭제
                             */
                            if(typeof nodeMarker.linePoints!='undefined') {
                                for(var i=0; i<nodeMarker.linePoints.length; i++) {
                                    var polyline = nodeMarker.linePoints[i].polyline;
                                    polyline.remove();
                                }
                            }

                            /**
                             * 노드 삭제 되돌리기 - 재생성
                             */
                            elementHandler.mapUndoManager.pushJob(function(data) {
                                var tempid = 'temp_' + window.makeid(10);
                                var icon = 'http://static.pntbiz.com/indoorplus/' + 'images/inc/marker_gray_10.png';
                                var tempdata = {lat: data.originalLatLng.lat(), lng: data.originalLatLng.lng()};
                                elementHandler.googleMap.createMarker('node_creating', tempid, data.originalLatLng, {icon: icon}, tempdata, function () {
                                    var node = {
                                        floor: elementHandler.googleMap.floorService.getCurrentFloor(),
                                        nodeName: '',
                                        lat: this.getPosition().lat(),
                                        lng: this.getPosition().lng()
                                    };
                                    elementHandler.serviceRequest.post('map/addNode.do', node, function (response) {
                                        elementHandler.googleMap.getShapeObject('marker', 'node_creating', tempid).remove();
                                        var nodeMarker = elementHandler.Node.createMarker(false, response.node);
                                    });
                                });
                            }, {originalLatLng: new google.maps.LatLng(marker.data.lat, marker.data.lng)});
                            /** end */
                        });
                    });

                    if(undo==true) {
                        /**
                         * 노드 생성 되돌리기 - 삭제
                         */
                        elementHandler.mapUndoManager.pushJob(function(data) {
                            elementHandler.serviceRequest.post('map/delNode.do', {nodeNum:data.nodeNum}, function(response) {});
                            data.marker.remove();
                        }, {nodeNum: nodeInfo.nodeNum, marker:marker});
                        /** end **/
                    }
                    return marker;
                }
            }
        })($);



        elementHandler.ready(function() {
            elementHandler.EDITMODE_READONLY = 1;
            elementHandler.EDITMODE_MOVE = 2;
            elementHandler.EDITMODE_NODECREATE = 3;
            elementHandler.EDITMODE_NODECONNECT = 4;
            elementHandler.EDITMODE_NODEDISCONNECT = 5;
            elementHandler.editmode = elementHandler.EDITMODE_READONLY;

            elementHandler.mapUndoManager = new MapUndoManager();
            elementHandler.mapUndoManager.bindUndoButton('btn-map-undo');

            elementHandler.serviceRequest = new ServiceRequest(elementHandler.baseurl);
            elementHandler.serviceRequest.bindLoadingBar('map-loading-bar');

            elementHandler.googleMap = new GoogleMap('map-canvas', function() {
                var googleMap = this;


                /*elementHandler.nodeConnectNotiWindow = new*/

                /**
                 * 상단 버튼 목록 맵에 표시
                 */
                var mapControls = this.getMap().controls;
                var controlPosition = google.maps.ControlPosition.TOP_CENTER;
                var modeButtons = document.getElementById('map-mode-buttons');
                mapControls[controlPosition].push(modeButtons);


                /**
                 * 층 서비스
                 */
                this.floorService = new FloorService(this, elementHandler.serviceRequest);
                this.floorService.bindFloorSelectBox('floor-selector');
                this.floorService.setOnChangeFloor(function(floorNum) {
                    /**
                     * 오브젝트 초기화
                     */
                    googleMap.getMarkerCluster().clearMarkers();
                    googleMap.clearShapeObject('marker', 'beacon');
                    googleMap.clearShapeObject('marker', 'node');
                    googleMap.getPairLineManager().clear();
                    elementHandler.mapUndoManager.clear();

                    /**
                     * 비콘 로드
                     */
                    elementHandler.serviceRequest.get('beacon/info/all.json', {floor:floorNum}, function(response) {
                        for(var i=0; i<response.beacons.length; i++) {
                            var beacon = response.beacons[i];
                            var latlng = new google.maps.LatLng(beacon.lat, beacon.lng);
                            googleMap.createMarker('beacon', 'beacon_'+beacon.beaconNum, latlng,
                                    {icon: 'http://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png'}, beacon, function() {
                                this.setColorset({default:{fill:'red'}});
                                //this.setMouseOverEffect(true);
                                this.setOnUpdateProcHandler(function(marker) {
                                    /**
                                     * 노드 이동 처리
                                     */
                                    var data = {
                                        beaconNum: marker.data.beaconNum,
                                        lat: marker.getPosition().lat(),
                                        lng: marker.getPosition().lng()
                                    };
                                    elementHandler.serviceRequest.post('beacon/info/latlng.do', data, function(response) {
                                        marker.onCreateComplete(true);
                                    });
                                });
                                this.onCreateComplete(true);
                            });
                        }
                    });

                    /**
                     * 노드 로드
                     */
                    elementHandler.serviceRequest.get('map/nodeAll.json', {floor:floorNum}, function(response) {
                        googleMap._nodeLoadComplete = true;
                        for(var i=0; i<response.nodes.length; i++) {
                            var node = response.nodes[i];
                            elementHandler.Node.createMarker(false, node);
                        }

                        if(googleMap._pairData) {
                            var pairLineManager = googleMap.getPairLineManager();
                            for(var i=0; i<googleMap._pairData.length; i++) {
                                var pair = googleMap._pairData[i];
                                var startNode = googleMap.getShapeObject('marker', 'node', 'node_'+pair.startPoint);
                                var endNode = googleMap.getShapeObject('marker', 'node', 'node_'+pair.endPoint);
                                if(startNode!=false && endNode!=false) {
                                    pairLineManager.connect(startNode, endNode);
                                }
                            }
                            if(googleMap.isMarkerClusterEnabled()!=true) googleMap.enableMarkerCluster(true);

                            googleMap._nodeLoadComplete = false;
                            googleMap._pairData = null;
                        }
                    });

                    /**
                     * 노드연결정보 로드
                     */
                    elementHandler.serviceRequest.get('map/pairAll.json', {floor:floorNum}, function(response) {
                        if(googleMap._nodeLoadComplete==true) {
                            var pairLineManager = googleMap.getPairLineManager();
                            for(var i=0; i<response.pairs.length; i++) {
                                var pair = response.pairs[i];
                                var startNode = googleMap.getShapeObject('marker', 'node', 'node_'+pair.startPoint);
                                var endNode = googleMap.getShapeObject('marker', 'node', 'node_'+pair.endPoint);
                                if(startNode!=false && endNode!=false) {
                                    pairLineManager.connect(startNode, endNode);
                                }
                            }
                            if(googleMap.isMarkerClusterEnabled()!=true) googleMap.enableMarkerCluster(true);
                            googleMap._nodeLoadComplete = false;
                        } else {
                            googleMap._pairData = response.pairs;
                        }
                    });
                });
                this.addListener('click', function(event) {
                    if(elementHandler.mode==elementHandler.EDITMODE_NODECREATE) {
                        /**
                         * 노드 생성
                         */
                        var tempid = 'temp_'+window.makeid(10);
                        var icon = 'http://static.pntbiz.com/indoorplus/'+'images/inc/marker_gray_10.png';
                        var tempdata = {lat:event.latLng.lat(), lng:event.latLng.lng()};
                        googleMap.createMarker('node_creating', tempid, event.latLng, {icon: icon}, tempdata, function() {
                            var node = {
                                floor:googleMap.floorService.getCurrentFloor(),
                                nodeName:'',
                                lat:event.latLng.lat(),
                                lng:event.latLng.lng()
                            };
                            elementHandler.serviceRequest.post('map/addNode.do', node, function(response) {
                                googleMap.getShapeObject('marker', 'node_creating', tempid).remove();
                                var nodeMarker = elementHandler.Node.createMarker(true, response.node);
                            });
                        });
                    }
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
                        elementHandler.mode = elementHandler.EDITMODE_READONLY;
                        if (typeof elementHandler.googleMap != 'undefined') {
                            elementHandler.googleMap.eachShapeObject('marker', 'beacon', function (marker) {
                                marker.changeDraggable(false);
                            });
                            elementHandler.googleMap.eachShapeObject('marker', 'node', function (marker) {
                                marker.changeDraggable(false);
                            });
                        }
                    }
                }
            },
            'btn-map-mode2': {
                event : 'change',
                action: function() {
                    if($(this).prop('checked')==true) {
                        elementHandler.mode = elementHandler.EDITMODE_MOVE;
                        if (typeof elementHandler.googleMap != 'undefined') {
                            elementHandler.googleMap.eachShapeObject('marker', 'beacon', function (marker) {
                                marker.changeDraggable(true);
                            });
                            elementHandler.googleMap.eachShapeObject('marker', 'node', function (marker) {
                                marker.changeDraggable(true);
                            });
                        }
                    }
                }
            },
            'btn-map-mode3': {
                event : 'change',
                action: function() {
                    elementHandler.mode = elementHandler.EDITMODE_NODECREATE;
                    if (typeof elementHandler.googleMap != 'undefined') {
                        elementHandler.googleMap.eachShapeObject('marker', 'beacon', function (marker) {
                            marker.changeDraggable(false);
                        });
                        elementHandler.googleMap.eachShapeObject('marker', 'node', function (marker) {
                            marker.changeDraggable(false);
                        });
                    }

                }
            },
            'btn-map-mode4': {
                event : 'change',
                action: function() {
                    elementHandler.mode = elementHandler.EDITMODE_NODECONNECT;
                    if (typeof elementHandler.googleMap != 'undefined') {
                        elementHandler.googleMap.eachShapeObject('marker', 'beacon', function (marker) {
                            marker.changeDraggable(false);
                        });
                        elementHandler.googleMap.eachShapeObject('marker', 'node', function (marker) {
                            marker.changeDraggable(false);
                        });
                    }
                }
            },
            'btn-map-mode5': {
                event : 'change',
                action: function() {
                    elementHandler.mode = elementHandler.EDITMODE_NODEDISCONNECT;
                    if (typeof elementHandler.googleMap != 'undefined') {
                        elementHandler.googleMap.eachShapeObject('marker', 'beacon', function (marker) {
                            marker.changeDraggable(false);
                        });
                        elementHandler.googleMap.eachShapeObject('marker', 'node', function (marker) {
                            marker.changeDraggable(false);
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
		<li class="crumb-active"><a href="###"><spring:message code="menu.100101" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.100000" /></li>
		<li class="crumb-trail"><spring:message code="menu.100100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 

<div class="col-sm-6">
	<h3>비콘 배치도 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">맵</a></li>
		  <li class="active">비콘 배치도</li>
		</ol>
	</span>
</div>
<hr />
<div class="clearfix"></div>

<div id="map-canvas" style="width:100%;height:700px;"></div>
<div class="hide">
    <div id="popup-beacon-info" style="display:none;padding:10px; margin:10px; overflow-y:auto; overflow-x:hidden; width:500px; height:auto; border: 1px solid #cccccc; background-color:#ffffff;"></div>
    <div id="map-view-options" style="margin:10px;">

        <div class="btn-group-vertical" data-toggle="buttons">
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-beacon" autocomplete="off" checked> 비콘 표시
            </label>
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-node" autocomplete="off" checked> 노드 표시
            </label>
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-pair" autocomplete="off" checked> 연결선 표시
            </label>
            <label class="btn btn-success btn-sm">
                <input type="checkbox" id="checkbox-map-visible-poi" autocomplete="off"> POI 표시
            </label>
            <label class="btn btn-success btn-sm active" style="background-color:#A8DB00;color:#171F00;">
                <input type="checkbox" id="checkbox-map-visible-grouping" autocomplete="off" checked> 그룹핑
            </label>
        </div>

        <div class="input-group" style="margin-top: 10px;">
            <div id="search-form" class="input-group input-group-sm">
                <div id="search-option-group" class="input-group-btn">
                    <button type="button" id="search-menu-btn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">노드 <span class="caret"></span></button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="javascript:;" id="search-option-beacon">비콘</a></li>
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
        <spring:message code="word.floor" />
        <select id="floor-selector" class="input-sm" style="margin-top:4px;">
        </select>
        <div  class="btn-group" data-toggle="buttons">
            <label class="btn btn-default btn-sm active">
                <input type="radio" name="options" id="btn-map-mode1" autocomplete="off" checked> 보기전용
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode2" autocomplete="off"> 비콘/노드이동
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode3" autocomplete="off"> 노드생성
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode4" autocomplete="off"> 노드연결
            </label>
            <label class="btn btn-default btn-sm">
                <input type="radio" name="options" id="btn-map-mode5" autocomplete="off"> 연결제거
            </label>
        </div>

        <input type="button" name="options" id="btn-map-undo" disabled class="btn btn-default btn-sm" autocomplete="off" value="되돌리기" />

        <div id="map-loading-bar" class="center" style="margin-top:10px;">
            Loading ...
        </div>
    </div>


</div>




	</div>
</div>
</section>
</body>
</html>