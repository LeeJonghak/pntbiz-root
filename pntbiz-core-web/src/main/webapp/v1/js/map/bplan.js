



var MapEditHistory = function() {
    this.jobstack = [];
    this.datastack = [];
    this.onRedoFunc = null;
    this.onPushJobFunc = null;

    this.pushJob = function(callback, data) {
        this.jobstack.push(callback);
        this.datastack.push(data);

        if(this.onPushJobFunc!=null) {
            this.onPushJobFunc();
        }
    }

    this.redo = function() {
        var job = this.jobstack.pop();
        var data = this.datastack.pop();
        job(this, data);
        if(this.onRedoFunc!=null) {
            this.onRedoFunc();
        }
    }

    this.setOnPushJob = function(callback) {
        this.onPushJobFunc = callback;
    }

    this.setOnRedo = function(callback) {
        this.onRedoFunc = callback;
    }
}


/**
 * 혼돈지역 관리
 * create: nohsoo 2015-04-21
 * @param googleMap
 * @param bssServiceRequest
 * @constructor
 */
var ChaosAreaMap = function(googleMap, bssServiceRequest) {
    this.googleMap = googleMap;
    this.bssServiceRequest = bssServiceRequest;

    this.areas = {};
    this.floor = 1;

    this.visible = {
        area: false
    }
    this.mode = ChaosAreaMap.MODE_READONLY;

    var _this = this;
    google.maps.event.addListener(this.googleMap.map, 'click', function(event) {
        _this.onMapClickEvent(event);
    });

}
ChaosAreaMap.MODE_READONLY = 1; // 읽기전용
ChaosAreaMap.MODE_CREATEAREA = 2; // 혼돈지역생성
ChaosAreaMap.MODE_MOVEAREA = 3; // 혼돈지역이동
ChaosAreaMap.prototype.load = function() {
    var _this = this;

}
ChaosAreaMap.prototype.changeMode = function(mode) {
    this.mode = mode;
}
ChaosAreaMap.prototype.clear = function() {
    for(var key in this.areas) {
        var circle = this.areas[key];
        circle.infoWindow.close();
        circle.setMap(null);
    }
    this.areas = {};
}
ChaosAreaMap.prototype.changeVisible = function(visible) {
    if(visible==true) {
        this.visible.area = true;
        for(key in this.areas) {
            var circle = this.areas[key];
            circle.setMap(this.googleMap.map);
        }
    } else {
        this.visible.area = false;
        for(key in this.areas) {
            var circle = this.areas[key];
            circle.infoWindow.close();
            circle.setMap(null);
        }
    }
}
ChaosAreaMap.prototype.load = function() {
    var _this = this;
    var data = {floor:this.floor};
    this.bssServiceRequest.call('map/listChaosArea.do', data, function(response) {
        // TODO: clear chaos area object
        _this.clear();
        for(var i=0; i<response.chaosareas.length; i++) {
            var chaosareas = response.chaosareas[i];
            var latlng = new google.maps.LatLng(chaosareas.lat, chaosareas.lng);
            var circle = _this.createCircle(latlng, chaosareas.radius, chaosareas, false, _this.visible.area);
            _this.areas[chaosareas.areaNum] = circle;
        }

    });
}
ChaosAreaMap.prototype.onMapClickEvent = function(event) {
    var _this = this;
    if(this.mode==ChaosAreaMap.MODE_CREATEAREA) {
        var circle = this.createCircle(event.latLng, 5, {}, true, true);
        var data = {
            floor:this.floor,
            lat:event.latLng.lat(),
            lng:event.latLng.lng(),
            radius:5
        };
        this.bssServiceRequest.post('map/registerChaosArea.do', data, function(response) {
            if(response.result=='1') {
                circle.data = response.chaosArea;
                _this.areas[response.chaosArea.areaNum] = circle;
            } else {
                circle.setMap(null);
                window.alert(vm.addFail);
            }
        });
    }
}
ChaosAreaMap.prototype.createCircle = function(latlng, radius, data, editable, visible) {
    var circle = new google.maps.Circle({
        strokeColor: 'red',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#000000',
        fillOpacity: 0.35,
        center: latlng,
        radius: radius,
        editable: false,
        draggable: false,
        data: data
    });
    if(editable==true) {
        circle.setEditable(true);
        //circle.setDraggable(true);
    }
    var _this = this;
    if(visible==true) {
        circle.setMap(this.googleMap.map);
    }
    circle.radiusSpanId = window.makeid(10);
    circle.infoWindow = new google.maps.InfoWindow({ maxWidth: 300});
    circle.infoWindow.setContent('<span id="'+circle.radiusSpanId+'">'+circle.getRadius()+'m</span>');
    circle.infoWindow.setPosition(circle.getCenter());
    if(editable==true) {
        circle.infoWindow.open(this.googleMap.map);
    }
    google.maps.event.addListener(circle, 'click', function() {
        circle.infoWindow.open(this.getMap());
        var span = document.getElementById(circle.radiusSpanId);
        if(typeof span!='undefined' && span!=null) {
            span.innerHTML = ''+circle.getRadius()+'m';
        }
    });
    google.maps.event.addDomListener(window, 'keydown', function(event) {
        window.ctrlKey = event.ctrlKey;
    });
    google.maps.event.addDomListener(window, 'keyup', function(event) {
        window.ctrlKey = event.ctrlKey;
    });
    google.maps.event.addListener(circle, 'rightclick', function(event) {

        if(window.ctrlKey==true) {
            /**
             * 컨트롤키와 마우스 우클릭할 경우 삭제요청
             */
            var data = {
                areaNum: this.data.areaNum
            };
            _this.bssServiceRequest.post('map/deleteChaosArea.do', data, function(response) {
                if(response.result=='1') {
                    _this.areas[data.areaNum].infoWindow.close();
                    _this.areas[data.areaNum].setMap(null);
                    delete _this.areas[data.areaNum];
                }
            });
        } else {
            /**
             * 마우스만 우클릭할 경우 편집모드 토글
             */
            if(this.getEditable()==true) {
                this.setEditable(false);
                //this.setDraggable(false);
            } else {
                this.setEditable(true);
                //this.setDraggable(true);
            }
        }
    });
    google.maps.event.addListener(circle, 'center_changed', function() {
        this.infoWindow.setPosition(this.getCenter());
        ///map/modifyChaosArea.do
        var data = {
            areaNum: this.data.areaNum,
            lat: this.getCenter().lat(),
            lng: this.getCenter().lng(),
            radius: this.getRadius()
        };
        _this.bssServiceRequest.post('map/modifyChaosArea.do', data, function(response) {
//            console.log(response);
        });
    });
    google.maps.event.addListener(circle, 'radius_changed', function() {
        var span = document.getElementById(this.radiusSpanId);
        if(typeof span!='undefined' && span!=null) {
            span.innerHTML = ''+this.getRadius()+'m';
        }
        var data = {
            areaNum: this.data.areaNum,
            lat: this.getCenter().lat(),
            lng: this.getCenter().lng(),
            radius: this.getRadius()
        };
        _this.bssServiceRequest.post('map/modifyChaosArea.do', data, function(response) {
//            console.log(response);
        });
    });

    return circle;
}





/**
 * 비콘 지도
 * @param googleMap
 * @param bssServiceRequest
 * @constructor
 */
var BeaconMap = function(googleMap, bssServiceRequest) {
    this.googleMap = googleMap;
    this.bssServiceRequest = bssServiceRequest;
    this.url = 'beacon/info/all.json';
    this.markers = {};
    this.floor = 1;
    this.beaconType ='F';

    this.beaconMoveEnabled = false;

    this.visible = {
        beacon: true
    }
}
BeaconMap.prototype.load = function() {
    var _this = this;
    var data = {floor:this.floor, beaconType:this.beaconType};

    var startMinor = common.getQueryString('startMinor');
    var endMinor = common.getQueryString('endMinor');
    if(startMinor!='' && endMinor!='') {
        data.startMinor = startMinor;
        data.endMinor = endMinor;
    }

    this.bssServiceRequest.call(this.url, data, function(response) {
        _this.clearMarker();
        for(var i=0; i<response.beacons.length; i++) {
            var beacon = response.beacons[i];
            var latlng = new google.maps.LatLng(beacon.lat, beacon.lng);
            var marker = new google.maps.Marker({
                position: latlng,
                title: 'beacon:'+beacon.beaconNum,
                draggable: _this.beaconMoveEnabled,
                icon: 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png',
                beacon: beacon
            });

            if(_this.visible.beacon==true) {
                marker.setMap(_this.googleMap.map);
            }

            google.maps.event.addListener(marker, 'click', function(marker, beacon) {
                return function() {
                    _this.onClickBeaconMaker(marker, beacon);
                }
            }(marker, beacon));
            google.maps.event.addListener(marker, 'dragend', function(marker, beacon) {
                return function() {
                    _this.onDragEnd(marker, beacon);
                }
            }(marker, beacon));

            _this.markers[beacon.beaconNum] = marker;
        }
    });
}
BeaconMap.prototype.visibleBeacon = function(visible) {
    this.visible.beacon = visible;
    var _this = this;
    $.each(this.markers, function(k, marker) {
        if(visible==true) {
            marker.setMap(_this.googleMap.map);
        } else {
            marker.setMap(null);
        }
    });
}
BeaconMap.prototype.setBeaconMoveEnable = function(enabled) {
    this.beaconMoveEnabled = enabled;

    $.each(this.markers, function(k, marker) {
        marker.setOptions({'draggable': enabled});
    });
}
BeaconMap.prototype.clearMarker = function() {
    $.each(this.markers, function(k, object) {
        object.setMap(null);
    });
    this.markers = {};
}
BeaconMap.prototype.removeMarker = function(beaconNum) {
    this.markers[beaconNum].setMap(null);
    delete this.markers[beaconNum];
}
BeaconMap.prototype.onClickBeaconMaker = function(marker, beacon) {
    elementHandler.mapPopup.remote('map/beaconInfo.ajax.do',{beaconNum:beacon.beaconNum}, function($element) {

        elementHandler.currentObject = {
            type: 'beacon',
            beacon: beacon,
            marker: marker
        }

        //$element.find('#UUID').mask('99999999-9999-9999-9999-999999999999');
        $element.find('#modBtn').click(function() {
            elementHandler.mapPopup.submit('beacon/info/mod.do', function(data) {
				var result = {};
				if(typeof(data)=='string') {
					result = $.parseJSON(data);
				} else {
					result = data;
				}
                var resultCode = result.result;
                if(resultCode=='1') {
                    common.success(vm.modSuccess);
                } else if (resultCode != '1') {
                    common.error(vm.modError);
                }
            });
        });
        $element.find('#bcImgUploadBtn').click(function() {
            elementHandler.mapPopup.upload('beacon/info/bcImgUpload.do', function(data) {
				var result = {};
				if(typeof(data)=='string') {
					result = $.parseJSON(data);
				} else {
					result = data;
				}
                var resultCode = result.result;
                if(resultCode=='1') {
                    common.success(vm.modSuccess);
                } else if (resultCode != '1') {
                    common.error(vm.modError);
                }
            });
        });
        $element.find('#delBtn').click(function() {
            if(window.confirm(vm.delConfirm)) {
                elementHandler.mapPopup.submit('beacon/info/del.do', function(data) {
					var result = {};
					if(typeof(data)=='string') {
						result = $.parseJSON(data);
					} else {
						result = data;
					}
                    var resultCode = result.result;
                    if(resultCode=='1') {
                        var beaconNum = $element.find('#beaconNum').val();
                        elementHandler.googleMap.beaconMap.removeMarker(beaconNum);
                        elementHandler.mapPopup.hide();

                    } else if (resultCode != '1') {
                        common.error(vm.delError);
                    }
                });
            }
        });
        $element.find('#bcImgDelBtn').click(function() {
            if(window.confirm(vm.delConfirm)) {
            	$element.find(".fileupload-preview > img").attr("src", ""); // 이미지 초기화
			
                elementHandler.mapPopup.submit('beacon/info/bcImgDel.do', function(data) {
					var result = {};
					if(typeof(data)=='string') {
						result = $.parseJSON(data);
					} else {
						result = data;
					}
                    var resultCode = result.result;
                    if(resultCode=='1') {
                    	common.success(vm.modSuccess);
                    } else if (resultCode != '1') {
                        common.error(vm.delError);
                    }
                });
            }
        });
        $element.find('#btn-content-search').click(function() {
            var $input = $('#input-content-search');
            if($.trim($input.val())=='') {
                $('#popup-content-error-message').removeClass('hide').html('keyword');
                $('#input-content-search').focus();
            } else {
                $('#popup-content-error-message').addClass('hide').html('');
            }
        });
    });
    elementHandler.mapPopup.setOnClose(function() {
        elementHandler.currentObject = null;
    });
    elementHandler.mapPopup.show();
//    console.log(beacon);
};

/**
 * 비콘 이동 이벤트 핸들러
 *
 * @author nohsoo 2015-03-15
 * @param marker
 * @param beacon
 */
BeaconMap.prototype.onDragEnd = function(marker, beacon) {
    var position = marker.getPosition();

    /**
     * 데이터 전송
     * beacon/info/latlng.do
     */
    var data = {beaconNum:beacon.beaconNum, lat:position.lat(), lng:position.lng()};

    this.bssServiceRequest.post('beacon/info/latlng.do', data, function(response) {
        if (response.result == '1') {
            var history = elementHandler.googleMap.mapEditHistory;
            var rollbackRequest = new RequestInstance(
                {url:elementHandler.baseurl+'beacon/info/latlng.do',
                    dataType:'json',
                    type:'post',
                    data:{beaconNum:beacon.beaconNum, lat:beacon.lat, lng:beacon.lng}
                });
            history.pushJob(function(history, data) {
                data.marker.setPosition(new google.maps.LatLng(data.lat, data.lng));
                data.rollbackRequest.call();
                data.beacon.lat = data.lat;
                data.beacon.lng = data.lng;
            }, {lat: beacon.lat, lng:beacon.lng, marker:marker, beacon:beacon, rollbackRequest:rollbackRequest});

            beacon.lat = position.lat();
            beacon.lng = position.lng();
        }
        else if (response.result != '1') {
            window.alert(vm.infoError);
        }
    })

}




/**
 * 노드 지도(NodeMap)
 *
 * @param googleMap
 * @param bssServiceRequest
 * @constructor
 */
var NodeMap = function(googleMap, bssServiceRequest) {
    this.googleMap = googleMap;
    this.bssServiceRequest = bssServiceRequest;
    this.url = 'map/nodeAll.json';
    this.url = {
        listNode: 'map/nodeAll.json',
        addNode: 'map/addNode.do',
        delNode: 'map/delNode.do',
        modifyNode: 'map/modifyNode.do',
        listPair: 'map/pairAll.json',
        connectPair: 'map/registerPair.do',
        disconnectPair: 'map/delPair.do'
    }
    this.markers = {};
    this.lines = {};
    this.poiWindows = {};
    this.jointNameWindows = {};
    this.floor = 1;

    // data
    this.nodeData = {};
    this.pairData = {};
    this.pairSelectedNodeID = null;
    this.pairSelectedNodeMarker = null;

    // option
    this.nodeAddEnabled = true;
    this.nodeEditFormEnabled = true;
    this.nodeDeleteEnabled = true;
    this.nodeMoveEnabled = true;
    this.pairAddEnabled = true;
    this.pairDeleteEnabled = true;
    this.visible = {
        node:true,
        pair:true,
        poi:true,
        jointName:false
    }

    // mode
    this.mode = NodeMap.MODE_EDITNODE;

    // event handler
    var _this = this;
    this.googleMap.addEventListener('click', function(event) {
        if(_this.mode==NodeMap.MODE_CREATENODE) {
            if(_this.nodeAddEnabled==true){
                _this.quickAddNode(event.latLng);
            }
        }
    });

    this.googleMap.addEventListener('rightclick', function(event) {
        if(_this.mode==NodeMap.MODE_CREATENODE) {
            if(_this.nodeAddEnabled==true){
                elementHandler.googleMap.markerGenerator.show();
            }
        }
    });


    // node pair infoWindow
    this.pairInfoWindow = new google.maps.InfoWindow({ maxWidth: 300});
    google.maps.event.addListener(this.pairInfoWindow,'closeclick',function(){
        _this.pairSelectedNodeID = null;
        _this.pairSelectedNodeMarker = null;
    });
}
NodeMap.MODE_READONLY = 1; // 읽기전용
NodeMap.MODE_CREATENODE = 2; // 노드편집
NodeMap.MODE_MOVENODE = 3; // 노드편집
NodeMap.MODE_ADDPAIR = 4; // 페어연결
NodeMap.MODE_DELETEPAIR = 5; // 페어삭제

/**
 * 편집 모드 변경
 * @param mode
 */
NodeMap.prototype.changeMode = function(mode) {
//    console.log('NodeMap: Change Mode: '+mode);

    this.pairSelectedNodeID = null;
    this.pairSelectedNodeMarker = null;
    this.pairInfoWindow.close();

    this.mode = mode;
    if(mode==NodeMap.MODE_READONLY) {
        $.each(this.markers, function(key, object) {
            object.setDraggable(false);
        });
        if(typeof elementHandler.googleMap!='undefined' && typeof elementHandler.googleMap.markerGenerator!='undefined' && elementHandler.googleMap.markerGenerator._visible==true) {
            elementHandler.googleMap.markerGenerator.hide();
        }
    }
    else if(mode==NodeMap.MODE_CREATENODE) {
        $.each(this.markers, function(key, object) {
            object.setDraggable(false);
        });
    }
    else if(mode==NodeMap.MODE_MOVENODE) {
        if(this.nodeMoveEnabled==true) {
//            console.log('start movenode mode!!');
            $.each(this.markers, function(key, object) {
                object.setDraggable(true);
            });
//            console.log('end movenode mode!!');
        }
        if(typeof elementHandler.googleMap!='undefined' && typeof elementHandler.googleMap.markerGenerator!='undefined' && elementHandler.googleMap.markerGenerator._visible==true) {
            elementHandler.googleMap.markerGenerator.hide();
        }
    }
    else if(mode==NodeMap.MODE_ADDPAIR) {
        $.each(this.markers, function(key, object) {
            object.setDraggable(false);
        });
        if(typeof elementHandler.googleMap!='undefined' && typeof elementHandler.googleMap.markerGenerator!='undefined' && elementHandler.googleMap.markerGenerator._visible==true) {
            elementHandler.googleMap.markerGenerator.hide();
        }
    }
    else if(mode==NodeMap.MODE_DELETEPAIR) {
        $.each(this.markers, function(key, object) {
            object.setDraggable(false);
        });
        if(typeof elementHandler.googleMap!='undefined' && typeof elementHandler.googleMap.markerGenerator!='undefined' && elementHandler.googleMap.markerGenerator._visible==true) {
            elementHandler.googleMap.markerGenerator.hide();
        }
    }

}

/**
 * 노드 데이터 로딩, 노드 지도에 표시
 */
NodeMap.prototype.load = function() {
    var _this = this;
    /**
     * 모든 노드데이터 로드
     */
    var data = {floor:this.floor};
    this.bssServiceRequest.call(this.url.listNode, data, function(response) {

		console.log('floor response', response);

        _this.clearPOIWindow();
        _this.clearMarker();
        _this.clearJoinNameWindow();
        _this.nodeData = {};

        var ms = [];
        for(var i=0; i<response.nodes.length; i++) {
            var node = response.nodes[i];
            var nodeID = node.nodeID;
            var latlng = new google.maps.LatLng(node.lat, node.lng);
            var marker = _this.createMarker(latlng, node);
            ms.push(marker);
            _this.createPOIWindow(marker, node);
            _this.createJoinNameWindow(marker, node);
            _this.nodeData[nodeID] = node;
        }
        //var markerCluster = new MarkerClusterer(_this.googleMap.map, ms,  {gridSize: 50, maxZoom: 19});

        /**
         * 모든 노드연결정보 로드
         */
        _this.bssServiceRequest.call(_this.url.listPair, {floor:_this.floor}, function(response) {

            _this.clearLine();
            _this.pairData = {};
            for(var i=0; i<response.pairs.length; i++) {
                var pair = response.pairs[i];

                if(typeof(_this.pairData[pair.startPoint+'-'+pair.endPoint])=='undefined'
                    && typeof(_this.pairData[pair.endPoint+'-'+pair.startPoint])=='undefined') {

                    var pairID = pair.startPoint+'-'+pair.endPoint;
                    var startNode = _this.nodeData[pair.startPoint];
                    var endNode = _this.nodeData[pair.endPoint];

                    if(typeof(startNode)!='undefined' && typeof(endNode)!='undefined') {
                        var startLatLng = new google.maps.LatLng(startNode.lat, startNode.lng);
                        var endLatLng = new google.maps.LatLng(endNode.lat, endNode.lng);
                        _this.pairData[pairID] = pair;
                        var line = _this.createLine(pair.startPoint, pair.endPoint, startLatLng, endLatLng);


                        if(typeof(_this.nodeData[startNode.nodeID].lines)=='undefined') {
                            _this.nodeData[startNode.nodeID].lines = [];
                        }
                        _this.nodeData[startNode.nodeID].lines[_this.nodeData[startNode.nodeID].lines.length] = {
                            polyline: line,
                            pathIndex: 0,
                            toNodeID: endNode.nodeID
                        }

                        if(typeof(_this.nodeData[endNode.nodeID].lines)=='undefined') {
                            _this.nodeData[endNode.nodeID].lines = [];
                        }
                        _this.nodeData[endNode.nodeID].lines[_this.nodeData[endNode.nodeID].lines.length] = {
                            polyline: line,
                            pathIndex: 1,
                            toNodeID: startNode.nodeID
                        }
                    }

                }
            }
        });
    });


}

NodeMap.prototype.createPOIWindow = function(marker, node) {
    if(typeof(node.nodeName)!='undefined' && node.nodeName!=null && node.nodeName.length>0) {
        var infoWindow = new google.maps.InfoWindow({
            content: node.nodeName,
            marker: marker,
            disableAutoPan:true
        });
        this.poiWindows[node.nodeID] = infoWindow;

        if(this.visible.poi==true) {
            infoWindow.open(this.googleMap.map, marker);
        }
    }
}

NodeMap.prototype.clearPOIWindow = function() {
    $.each(this.poiWindows, function(k, infoWindow) {
        infoWindow.close();
    });
    this.poiWindows = {};
}

NodeMap.prototype.createJoinNameWindow = function(marker, node) {
    if(typeof(node.jointName)!='undefined' && node.jointName!=null && node.jointName.length>0) {
        var infoWindow = new google.maps.InfoWindow({
            content: node.jointName,
            marker: marker,
            disableAutoPan:true
        });
        this.jointNameWindows[node.nodeID] = infoWindow;

        if(this.visible.jointName==true) {
            infoWindow.open(this.googleMap.map, marker);
        }
    }
}

NodeMap.prototype.clearJoinNameWindow = function() {
    $.each(this.jointNameWindows, function(k, infoWindow) {
        infoWindow.close();
    });
    this.jointNameWindows = {};
}


/**
 * 마커 생성
 * @param latlng
 * @param node
 * @returns {google.maps.Marker}
 */
NodeMap.prototype.createMarker = function(latlng, node) {
    var draggable = false;

    if(this.nodeMoveEnabled==true && this.mode==NodeMap.MODE_MOVENODE) {
        draggable = true;
    }
    /**
     * 마커 생성
     */
    var iconUrl = 'https://static.pntbiz.com/indoorplus/'+ 'images/inc/marker_blue_10.png';
	if(node.conNum) {
		iconUrl = 'https://static.pntbiz.com/indoorplus/'+ 'images/inc/marker_purple_10.png'
	    var icon = {
	        path: google.maps.SymbolPath.CIRCLE,
	        scale: 4,
	        strokeColor: '#ffffff',
	        strokeWeight: 1,
	        fillColor: 'purple',
	        fillOpacity: 1
	    };
	} else if(node.nodeName) {
        iconUrl = 'https://static.pntbiz.com/indoorplus/'+ 'images/inc/marker_green_10.png'
        var icon = {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 4,
            strokeColor: '#ffffff',
            strokeWeight: 1,
            fillColor: 'green',
            fillOpacity: 1
        };
    } else {
        var icon = {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 4,
            strokeColor: '#ffffff',
            strokeWeight: 1,
            fillColor: 'blue',
            fillOpacity: 1
        };
    }

    var marker = new google.maps.Marker({
        position: latlng,
        title: node.nodeName,
        draggable: draggable,
        icon: iconUrl,
        //icon: icon,
        node: node
    });
    if(this.visible.node==true) {
        marker.setMap(this.googleMap.map);
    }
    var _this = this;

    /**
     * 마커 이벤트 등록
     */
    google.maps.event.addListener(marker, 'click', function(marker, node) {
        return function() {
            _this.onNodeMarkerClick(event, marker, node);
        }
    }(marker, node));
    google.maps.event.addListener(marker, 'rightclick', function(marker, node) {
        return function() {
            _this.onNodeMarkerRightClick(event, marker, node);
        }
    }(marker, node));
    google.maps.event.addListener(marker,'dragend',function(event) {
        _this.onNodeMarkerDragEnd(event, marker, node);
    });

    this.markers[node.nodeID] = marker;

    return marker;
}

NodeMap.prototype.visibleNode = function(visible) {
    this.visible.node = visible;
    var _this = this;
    $.each(this.markers, function(k, marker) {
        if(visible==true) {
            marker.setMap(_this.googleMap.map);
        } else {
            marker.setMap(null);
        }
    });
}

NodeMap.prototype.visiblePair = function(visible) {
    this.visible.pair = visible;
    var _this = this;

    $.each(this.lines, function(k, line) {
        if(visible==true) {
            line.setMap(_this.googleMap.map);
        } else {
            line.setMap(null);
        }
    });
}

NodeMap.prototype.visiblePOI = function(visible) {
    this.visible.poi = visible;
    var _this = this;

    /**
     * POI 표시 상태 변경
     */
    $.each(this.poiWindows, function(k, infoWindow) {
        if(visible==true) {
            infoWindow.open(_this.googleMap.map, infoWindow.marker);
        } else {
            infoWindow.close();
        }
    });
}

NodeMap.prototype.visibleJointName = function(visible) {
    this.visible.jointName = visible;
    var _this = this;

    $.each(this.jointNameWindows, function(k, infoWindow) {
        if(visible==true) {
            infoWindow.open(_this.googleMap.map, infoWindow.marker);
        } else {
            infoWindow.close();
        }
    });
}


/**
 * 노드 마커 제거
 * @param nodeID
 */
NodeMap.prototype.removeMarker = function(nodeID) {
    this.markers[nodeID].setMap(null);
    delete this.markers[nodeID];
}

/**
 * 모든 노드 마커 제거
 */
NodeMap.prototype.clearMarker = function() {
    $.each(this.markers, function(k, object) {
        object.setMap(null);
    });
    this.markers = {};
}

/**
 * 지도에 선 그리기(Node Pair)
 *
 * @param startNodeID
 * @param endNodeID
 * @param startLatLng
 * @param endLatLng
 * @returns {google.maps.Polyline}
 */
NodeMap.prototype.createLine = function(startNodeID, endNodeID, startLatLng, endLatLng) {
    var polyOptions = {
        strokeColor: 'red',
        strokeOpacity: 0.8,
        strokeWeight: 1.5
    };
    var line = new google.maps.Polyline(polyOptions);
    line.getPath().push(startLatLng);
    line.getPath().push(endLatLng);

    if(this.visible.pair==true) {
        line.setMap(this.googleMap.map);
    }

    var pairID = startNodeID+'-'+endNodeID;
    if(typeof(this.lines[endNodeID+'-'+startNodeID])!='undefined') {
        pairID = endNodeID+'-'+startNodeID;
    }

    this.lines[pairID] = line;

    return line;
}

NodeMap.prototype.removeLine = function(startNodeID, endNodeID) {

    var exists = false;

//    console.log(this.lines[startNodeID+'-'+endNodeID]);
    if(typeof(this.lines[startNodeID+'-'+endNodeID])!='undefined') {
        this.lines[startNodeID+'-'+endNodeID].setMap(null);
        delete this.lines[startNodeID+'-'+endNodeID];
        exists = true;
    }

//    console.log(this.lines[endNodeID+'-'+startNodeID]);
    if(typeof(this.lines[endNodeID+'-'+startNodeID])!='undefined') {
        this.lines[endNodeID+'-'+startNodeID].setMap(null);
        delete this.lines[endNodeID+'-'+startNodeID];
        exists = true;
    }

    return exists;
}

NodeMap.prototype.clearLine = function() {
    $.each(this.lines, function(k, object) {
        object.setMap(null);
    });
    this.lines = {};
}

/**
 * 노드마커 마우스 왼쪽 클릭
 * @param event
 * @param marker
 */
NodeMap.prototype.onNodeMarkerClick = function(event, marker, node) {
    if(this.nodeEditFormEnabled==true) {
        /**
         * 노드 정보 표시(레이어팝업)
         */
//        console.log(event);
//        console.log(marker.node);
    }

    elementHandler.currentObject = {
        type: 'node',
        node: node,
        marker: marker
    }

    /**
     * 노드 연결 모드 일경우
     */
    if(this.mode==NodeMap.MODE_ADDPAIR && this.pairAddEnabled==true) {
        if(this.pairSelectedNodeID!=null) {
            /**
             * 노드 연결 처리
             */
            this.connectPair(this.pairSelectedNodeID, this.pairSelectedNodeMarker,
                node.nodeID, marker);
        }
        this.pairInfoWindow.setContent(vm.nodeNextError);
        this.pairInfoWindow.open(this.googleMap.map, marker);
        this.pairSelectedNodeID = node.nodeID;
        this.pairSelectedNodeMarker = marker;
    }
    /**
     * 노드 연결 제거 모드 일경우
     */
    else if(this.mode==NodeMap.MODE_DELETEPAIR && this.pairDeleteEnabled==true) {

        if(this.pairSelectedNodeID!=null) {
            /**
             * 노드 연결 제거 처리
             */
            this.disconnectPair(this.pairSelectedNodeID, this.pairSelectedNodeMarker,
                node.nodeID, marker);
        }
        this.pairInfoWindow.setContent(vm.nodeRemoveError);
        this.pairInfoWindow.open(this.googleMap.map, marker);
        this.pairSelectedNodeID = node.nodeID;
        this.pairSelectedNodeMarker = marker;
    }


    elementHandler.mapPopup.remote('map/nodeInfo.ajax.do',{nodeNum:node.nodeNum}, function($element) {
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
						var result = {};
						if(typeof(data)=='string') {
							result = $.parseJSON(data);
						} else {
							result = data;
						}
                        var resultCode = result.result;
                        if(resultCode=='1') {
                            common.success(vm.modSuccess);
                        } else if (resultCode != '1') {
                            common.error(vm.modError);
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
}

/**
 * 노드마커 마우스 오른쪽 클릭
 * @param event
 * @param marker
 */
NodeMap.prototype.onNodeMarkerRightClick = function(event, marker, node) {
    if(this.nodeDeleteEnabled==true && this.mode!=NodeMap.MODE_READONLY) {
        //if(window.confirm('삭제하시겠습니까?')) {
        this.deleteNode(node.nodeNum);
        //}
    }
}

/**
 * 노드마커 드래그앤드랍 종료 이벤트
 * @param event
 * @param marker
 * @param node
 */
NodeMap.prototype.onNodeMarkerDragEnd = function(event, marker, node) {
    if(this.nodeMoveEnabled==true && this.mode==NodeMap.MODE_MOVENODE) {
        this.moveNode(node.nodeNum, event.latLng);
    }
}

/**
 * 빠른 노드 추가 실행
 * @param latlng
 */
NodeMap.prototype.quickAddNode = function(latlng) {
    var node = {
        floor:this.floor,
        nodeName:'',
        lat:latlng.lat(),
        lng:latlng.lng()
    };


    var _this = this;

    //if(this.bssServiceRequest.ajaxReady==true) {
        /**
         * Ajax 요청 - 노드 추가
         */

        this.bssServiceRequest.post(this.url.addNode, node, function(result) {
            if(result.result=='1') {
                var nodeID = result.node.nodeID;
                _this.nodeData[nodeID] = result.node;

                var latlng = new google.maps.LatLng(result.node.lat, result.node.lng);
                var marker = _this.createMarker(latlng, result.node);

                /**
                 * Redo
                 */
                var rollbackRequest = new RequestInstance(
                    {url:elementHandler.baseurl+_this.url.delNode,
                        dataType:'json',
                        type:'post',
                        data:{nodeNum:result.node.nodeNum}
                    });

                var history = elementHandler.googleMap.mapEditHistory;
                history.pushJob(function(history, data) {
                    rollbackRequest.call();

                    data.nodeMap.removeMarker(data.node.nodeID);

                    if(typeof(data.nodeData[data.node.nodeID].lines)!='undefined') {
                        for (var i = 0; i < data.nodeData[data.node.nodeID].lines.length; i++) {
                            var line = data.nodeData[data.node.nodeID].lines[i];
                            data.nodeMap.removeLine(data.node.nodeID, line.toNodeID);

                            var pairID = null;
                            if (typeof(data.nodeMap.pairData[data.node.nodeID + '-' + line.toNodeID]) != 'undefined') {
                                pairID = data.node.nodeID + '-' + line.toNodeID;
                            }
                            else if (typeof(data.nodeMap.pairData[line.toNodeID + '-' + data.node.nodeID]) != 'undefined') {
                                pairID = line.toNodeID + '-' + data.node.nodeID;
                            }

                            delete data.nodeMap.pairData[pairID];


                        }
                    }

                    delete data.nodeMap.nodeData[data.node.nodeID];
                }, {node:_this.nodeData[nodeID], nodeData: _this.nodeData, nodeMap:_this, rollbackRequest:rollbackRequest});

            }
            else if(result.result!='1') {
                window.alert(vm.addError);
            }

        });
    //}
}

NodeMap.prototype.moveNode = function(nodeNum, latlng) {
    var _this = this;
    var data = {
        nodeNum: nodeNum,
        lat: latlng.lat(),
        lng: latlng.lng()
    }
    //if(this.bssServiceRequest.ajaxReady==true) {


        this.bssServiceRequest.post(this.url.modifyNode, data, function (response) {
            if (response.result == '1') {
                var node = _this.nodeData[response.node.nodeID];

                /**
                 * Redo
                 */
                var history = elementHandler.googleMap.mapEditHistory;
                var rollbackRequest = new RequestInstance(
                    {url:elementHandler.baseurl+_this.url.modifyNode,
                        dataType:'json',
                        type:'post',
                        data:{nodeNum:node.nodeNum, lat:node.lat, lng:node.lng}
                    });
                history.pushJob(function(history, data) {
//                    console.log(data.marker);
                    data.marker.setPosition(new google.maps.LatLng(data.lat, data.lng));
                    data.node.lat = data.lat;
                    data.node.lng = data.lng;

                    if(typeof(data.lines)!='undefined') {
                        for (var i = 0; i < data.lines.length; i++) {
                            var line = data.lines[i];
                            var path = line.polyline.getPath();
                            path.removeAt(line.pathIndex);
                            path.insertAt(line.pathIndex, new google.maps.LatLng(data.lat, data.lng));
                        }
                    }

                    data.rollbackRequest.call();
                }, {lat: node.lat, lng:node.lng, node:node, lines:node.lines, marker:_this.markers[node.nodeID],rollbackRequest:rollbackRequest});

                /**
                 * 라인이 연결되어 있는 경우
                 */
                if(typeof(node.lines)!='undefined') {
                    for (var i = 0; i < node.lines.length; i++) {
                        var line = node.lines[i];
                        var path = line.polyline.getPath();
                        path.removeAt(line.pathIndex);
                        path.insertAt(line.pathIndex, latlng);
                    }
                }

                node.lat = latlng.lat();
                node.lng = latlng.lng();
            }
            else if (response.result != '1') {
                window.alert(vm.modError);
            }
        });
    //}
}

NodeMap.prototype.connectPair = function(startNodeID, startMarker, endNodeID, endMarker) {


    /**
     * 선택한 두 노드가 같은 노드인지 체크
     * 2015-07-23 nohsoo
     */
    if(startNodeID==endNodeID) {
        window.alert(vm.nodeSameError);
        return;
    }
    /** end **/

    /**
     * 2015-11-18 nohsoo 다른 층 노드일 경우 무시
     */
    if(startMarker.node.floor!=endMarker.node.floor) {
        return;
    }
    /** end **/


    var _this = this;
    var data = {
        startNodeID: startNodeID,
        endNodeID: endNodeID,
        floor: this.floor
    }

    if(typeof(_this.pairData[startNodeID+'-'+endNodeID])=='undefined'
        && typeof(_this.pairData[endNodeID+'-'+startNodeID])=='undefined') {



        this.bssServiceRequest.post(this.url.connectPair, data, function (response) {
//            console.log(response);
            if (response.result == '1') {
                var pair = response.nodePair;
                if (typeof(_this.pairData[pair.startPoint + '-' + pair.endPoint]) == 'undefined'
                    && typeof(_this.pairData[pair.endPoint + '-' + pair.startPoint]) == 'undefined') {

                    var pairID = pair.startPoint + '-' + pair.endPoint;
                    var startNode = _this.nodeData[pair.startPoint];
                    var endNode = _this.nodeData[pair.endPoint];
                    var startLatLng = new google.maps.LatLng(startNode.lat, startNode.lng);
                    var endLatLng = new google.maps.LatLng(endNode.lat, endNode.lng);
                    _this.pairData[pairID] = pair;
                    var line = _this.createLine(pair.startPoint, pair.endPoint, startLatLng, endLatLng);

                    if (typeof(_this.nodeData[startNode.nodeID].lines) == 'undefined') {
                        _this.nodeData[startNode.nodeID].lines = [];
                    }
                    _this.nodeData[startNode.nodeID].lines[_this.nodeData[startNode.nodeID].lines.length] = {
                        polyline: line,
                        pathIndex: 0,
                        toNodeID: endNode.nodeID
                    }

                    if (typeof(_this.nodeData[endNode.nodeID].lines) == 'undefined') {
                        _this.nodeData[endNode.nodeID].lines = [];
                    }
                    _this.nodeData[endNode.nodeID].lines[_this.nodeData[endNode.nodeID].lines.length] = {
                        polyline: line,
                        pathIndex: 1,
                        toNodeID: startNode.nodeID
                    }
                }
            }
            else if (response.result != '1') {
                window.alert(vm.error);
            }

        });

    }

}
NodeMap.prototype.disconnectPair = function(startNodeID, startMarker, endNodeID, endMarker) {
    var _this = this;
    var data = {
        startNodeID: startNodeID,
        endNodeID: endNodeID
    }

    //if(this.bssServiceRequest.ajaxReady==true) {
        this.bssServiceRequest.post(this.url.disconnectPair, data, function (response) {
//            console.log(response);
            if (response.result == '1') {

                var pairID = null;
                if (typeof(_this.pairData[startNodeID + '-' + endNodeID]) != 'undefined') {
                    pairID = startNodeID + '-' + endNodeID;
                }
                if (typeof(_this.pairData[endNodeID + '-' + startNodeID]) != 'undefined') {
                    pairID = endNodeID + '-' + startNodeID;
                }


                if (pairID == null) return;

                var startNode = _this.nodeData[startNodeID];
                var endNode = _this.nodeData[endNodeID];

                if (_this.nodeData[endNode.nodeID].lines) {
                    for (var i = 0; i < _this.nodeData[endNode.nodeID].lines.length; i++) {
                        if (typeof(_this.nodeData[endNode.nodeID].lines[i]) != 'undefined') {
                            if (_this.nodeData[endNode.nodeID].lines[i].toNodeID == startNode.nodeID) {
                                delete _this.nodeData[endNode.nodeID].lines[i];
                            }
                        }
                    }
                }

                if (_this.nodeData[startNode.nodeID].lines) {
                    for (var i = 0; i < _this.nodeData[startNode.nodeID].lines.length; i++) {
                        if (typeof(_this.nodeData[startNode.nodeID].lines[i]) != 'undefined') {
                            if (_this.nodeData[startNode.nodeID].lines[i].toNodeID == endNode.nodeID) {
                                delete _this.nodeData[startNode.nodeID].lines[i];
                            }
                        }
                    }
                }


                _this.removeLine(startNodeID, endNodeID);
                delete _this.pairData[pairID];

            }
            else if (response.result != '1') {
                window.alert(vm.error);
            }
        });
    //}
}

/**
 * 노드 삭제 실행, 노드 지도에서 제거
 * @param nodeID
 */
NodeMap.prototype.deleteNode = function(nodeNum) {
    var _this = this;
    //if(this.bssServiceRequest.ajaxReady==true) {
        this.bssServiceRequest.post(this.url.delNode, {nodeNum: nodeNum}, function (response) {
            if (response.result == '1') {
                var removeLat = response.node.lat;
                var removeLng = response.node.lng;

                _this.removeMarker(response.node.nodeID);
                var removePairTo = [];

                if(typeof(_this.nodeData[response.node.nodeID].lines)!='undefined') {
                    for (var i = 0; i < _this.nodeData[response.node.nodeID].lines.length; i++) {
                        var line = _this.nodeData[response.node.nodeID].lines[i];
                        if(_this.removeLine(response.node.nodeID, line.toNodeID)==true) {
                            removePairTo.push(line.toNodeID);
                        }

                        var pairID = null;
                        if (typeof(_this.pairData[response.node.nodeID + '-' + line.toNodeID]) != 'undefined') {
                            pairID = response.node.nodeID + '-' + line.toNodeID;
                        }
                        else if (typeof(_this.pairData[line.toNodeID + '-' + response.node.nodeID]) != 'undefined') {
                            pairID = line.toNodeID + '-' + response.node.nodeID;
                        }

                        delete _this.pairData[pairID];
                    }
                }
//                console.log(removePairTo);

                var rollbackRequest = new RequestInstance(
                    {url:elementHandler.baseurl+_this.url.addNode,
                        dataType:'json',
                        type:'post',
                        data:{lat:removeLat, lng:removeLng, floor:elementHandler.googleMap.floorMap.getCurrentFloor()}
                    }, function(result) {
//                        console.log(result);
                        var nodeID = result.node.nodeID;
                        _this.nodeData[nodeID] = result.node;

                        var latlng = new google.maps.LatLng(result.node.lat, result.node.lng);
                        var marker = _this.createMarker(latlng, result.node);

                        for(var i=0; i<removePairTo.length; i++) {
//                            console.log('dl');
//                            console.log(removePairTo[i])
                            var toNodeID = removePairTo[i];
                            var toMarker = _this.markers[toNodeID];
                            _this.connectPair(nodeID, marker, toNodeID, toMarker);
                        }

                    });
                var history = elementHandler.googleMap.mapEditHistory;
                history.pushJob(function(history, data) {
                    data.rollbackRequest.call();
                }, {removePairTo: removePairTo, lat:removeLat, lng:removeLng, rollbackRequest:rollbackRequest});


                delete _this.nodeData[response.node.nodeID];
            }
            else if (response.result != '1') {
                window.alert(vm.error);
            }
        });
    //}
}



/**
 * 레이어 팝업
 *
 * @param elementId
 * @param baseurl
 * @constructor
 */
var MapPopup = function(elementId, baseurl) {
    this.baseurl = baseurl!=null?baseurl:'/';
    this.elementId = elementId;
    this.element = document.getElementById(this.elementId);
    this.$element = $(this.element);
    this.$form = null;

    this.$element.css('border-radius', '6px');
    this.$element.css('-webkit-box-shadow', '2px 2px 10px 0px rgba(0,0,0,0.40)');
    this.$element.css('-moz-box-shadow', '2px 2px 10px 0px rgba(0,0,0,0.40)');
    this.$element.css('box-shadow', '2px 2px 10px 0px rgba(0,0,0,0.40)');

    this.prevurl = null;
    this.prevdata = null;

    this._onCloseFunc = null;
}
MapPopup.prototype.setOnClose = function(func) {
    this._onCloseFunc = func;
}
MapPopup.prototype.onClose = function() {
    if(this._onCloseFunc!=null) {
        this._onCloseFunc();
    }
}
MapPopup.prototype.remote = function(url, data, callback) {
    this.prevurl = url;
    this.prevdata = data;

    var mapPopup = this;
    $.ajax({
        type: "get",
        dataType: 'html',
        url: this.baseurl+url,
        data: data!=null?data:{},
        cache: false
    }).done(function(html) {
        mapPopup.html(html);
        mapPopup.$form = mapPopup.$element.find('#map-popup-form');
//        console.log(mapPopup.$form);
        mapPopup.$uploadform = mapPopup.$element.find('#map-popup-upload-form');
        mapPopup.$element.find('.popup-close-btn').click(function() {
            mapPopup.onClose();
            mapPopup.hide();
        });
        
        $(callback(mapPopup.$element));
    });
}
MapPopup.prototype.reload = function() {
    var mapPopup = this;
    $.ajax({
        type: "get",
        dataType: 'html',
        url: this.baseurl+this.prevurl,
        data: this.prevdata!=null?this.prevdata:{},
        cache: false
    }).done(function(html) {
        mapPopup.html(html);
        mapPopup.$form = mapPopup.$element.find('#map-popup-form');
//        console.log(mapPopup.$form);
        mapPopup.$element.find('.popup-close-btn').click(function() {
            mapPopup.onClose();
            mapPopup.hide();
        });
        //callback(mapPopup.$element);
        //$(callback(mapPopup.$element));
    });
}
MapPopup.prototype.html = function(html) {
    this.$element.html(html);
}
MapPopup.prototype.show = function() {
    this.$element.show();
}
MapPopup.prototype.hide = function() {
    this.$element.hide();
}
MapPopup.prototype.submit = function (url, callback) {
    if(this.$form!=null) {
        if (this.$form.valid()) {
            $.ajax({ type: "POST",
                contentType: 'application/x-www-form-urlencoded',
                processData: false,
                url: this.baseurl + url,
                data: this.$form.serialize(),
                success: callback
            });
        }
    }
}
MapPopup.prototype.upload = function (url, callback) {
    if(this.$uploadform!=null) {
    	var imgSize = 1048576 * 5;
		var imgFormat = "png|jpe?g|gif";
		this.$uploadform.validate({
			rules: {
				imgSrc: { extension: imgFormat, filesize: imgSize }
	        },
	        messages: {
	        	imgSrc: { extension: vm.extension, filesize: vm.filesize }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});

        if (this.$uploadform.valid()) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				 url: this.baseurl + url,
				data: new FormData(this.$uploadform[0]),
				success: callback
			});
        }
    }
}
/*MapPopup.prototype.submitResult = function(data) {
 var result = $.parseJSON(data);
 var resultCode = result.result;
 if(resultCode=='1') {
 this.hide();
 } else if (resultCode != '1') {
 common.error(vm.error);
 }
 }*/




/**
 * 비콘 마커 클릭 이벤트
 */
elementHandler.onClickBeaconMaker = function(event, marker, beacon) {
//    console.log(beacon);
}



var contentSearch = {
    list: ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
        'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii',
        'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana',
        'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota',
        'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
        'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
        'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
        'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
        'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
    ],
    substringMatcher: function(strs) {
        return function findMatches(q, cb) {
            var matches, substrRegex;

            matches = [];

            substrRegex = new RegExp(q, 'i');

            $.each(strs, function(i, str) {
                if (substrRegex.test(str)) {
                    matches.push({ value: str });
                }
            });

            cb(matches);
        };
    },
    search: function() {
        return this.substringMatcher(this.list);
    }
};



elementHandler.ready(function() {

    /**
     * 비콘 정보 표시 팝업
     * @type {MapPopup}
     */
    elementHandler.mapPopup = new MapPopup('popup-beacon-info');

    /**
     * 지도 생성
     * @type {GoogleMap}
     */
    elementHandler.googleMap = new GoogleMap('map-canvas', function() {

        this.map.setZoom(20);

        this.bssServiceRequest = new BssServiceRequest(elementHandler.baseurl);
        this.mapEditHistory = new MapEditHistory(this.bssServiceRequest);
        this.mapEditHistory.setOnPushJob(
            function() {
                if(this.jobstack.length>0) {
                    $('#btn-map-undo').prop('disabled', false);
                } else {
                    $('#btn-map-undo').prop('disabled', true);
                }
            }
        );
        this.mapEditHistory.setOnRedo(
            function() {
                if(this.jobstack.length>0) {
                    $('#btn-map-undo').prop('disabled', false);
                } else {
                    $('#btn-map-undo').prop('disabled', true);
                }
            }
        );
        $('#btn-map-undo').click(function() {
            elementHandler.googleMap.mapEditHistory.redo();
        });

        /**
         * 키보드를 이용한 비콘, 노드 이동 처리
         */
        google.maps.event.addDomListener(document, 'keydown', function (e) {
            if(elementHandler.mapMode==3) {
//                console.log('keydown:'+code);
                if(elementHandler.currentObject) {
                    var marker = elementHandler.currentObject.marker;
                    var code = (e.keyCode ? e.keyCode : e.which);
                    if(code==87) { // 상
                        KeyboardNodeBeaconControl.up(elementHandler.currentObject);
                    }
                    else if(code==83) { // 하
                        KeyboardNodeBeaconControl.down(elementHandler.currentObject);
                    }
                    else if(code==65) { // 좌
                        KeyboardNodeBeaconControl.left(elementHandler.currentObject);
                    }
                    else if(code==68) { // 우
                        KeyboardNodeBeaconControl.right(elementHandler.currentObject);
                    }
                }
            }
        });

        this.floorMap = new FloorMap(this, this.bssServiceRequest);
        this.floorMap.setOnFloorData(function(floors) {
            var $select = $('#floor-selector');
            /**
             * 층목록 선택박스에 추가
             */
            var floorMap = this;
            var added = [];
            $.each(floors, function(k, floor) {
                if(added.indexOf(floor.floor)==-1) {
                    var selectTag = '';
                    if(floorMap.currentFloor==floor.floor) {
                        selectTag = ' selected="selected" ';
                    }
                    var optionHtml = '<option value="'+floor.floor+'" '+selectTag+'>'+floor.floor+'F</option>';
                    $select.append(optionHtml);
                    added.push(floor.floor);
                }
            });
//            console.log('currentFloor', this.currentFloor);
            this.googleMap.beaconMap.floor = this.currentFloor;
            this.googleMap.beaconMap.load();
            this.googleMap.nodeMap.floor = this.currentFloor;
            this.googleMap.nodeMap.load();
            this.googleMap.chaosAreaMap.floor = this.currentFloor;
            this.googleMap.chaosAreaMap.load();
        });



        this.beaconMap = new BeaconMap(this, this.bssServiceRequest);
        //this.beaconMap.load();
        var mapControls = elementHandler.googleMap.map.controls;
        var controlPosition = google.maps.ControlPosition.RIGHT_TOP;
        mapControls[controlPosition].push(elementHandler.mapPopup.element);

        this.nodeMap = new NodeMap(this, this.bssServiceRequest);
        this.nodeMap.changeMode(NodeMap.MODE_READONLY);
        //this.nodeMap.load();

        this.chaosAreaMap = new ChaosAreaMap(this, this.bssServiceRequest);

        /**
         * 지도 모드 변경 버튼 추가
         */
        $('#btn-map-mode1').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_READONLY);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(false);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_READONLY);

                elementHandler.mapMode = 1;
            }
        });
        $('#btn-map-mode2').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_CREATENODE);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(false);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_READONLY);

                elementHandler.mapMode = 2;
            }
        });
        $('#btn-map-mode3').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_MOVENODE);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(true);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_MOVEAREA);

                elementHandler.mapMode = 3;
            }
        });
        $('#btn-map-mode4').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_ADDPAIR);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(false);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_READONLY);

                elementHandler.mapMode = 4;
            }
        });
        $('#btn-map-mode5').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_DELETEPAIR);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(false);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_READONLY);

                elementHandler.mapMode = 5;
            }
        });
        /**
         * 혼돈지역생성 모드
         * nohsoo 2015-04-21
         */
        $('#btn-map-mode6').on('change', function() {
            if($(this).prop('checked')==true) {
                var nodeMap = elementHandler.googleMap.nodeMap;
                nodeMap.changeMode(NodeMap.MODE_READONLY);

                var beaconMap = elementHandler.googleMap.beaconMap;
                beaconMap.setBeaconMoveEnable(false);

                var chaosAreaMap = elementHandler.googleMap.chaosAreaMap;
                chaosAreaMap.changeMode(ChaosAreaMap.MODE_CREATEAREA);

                elementHandler.mapMode = 6;
            }
        });
        var controlPosition = google.maps.ControlPosition.TOP_CENTER;
        var modeButtons = document.getElementById('map-mode-buttons');
        mapControls[controlPosition].push(modeButtons);

        /**
         * 지도 표지 옵션 버튼 추가
         */
        $('#checkbox-map-visible-beacon').change(function() {
            elementHandler.googleMap.beaconMap.visibleBeacon($(this).prop('checked'));
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });
        $('#checkbox-map-visible-node').change(function() {
            elementHandler.googleMap.nodeMap.visibleNode($(this).prop('checked'));
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });
        $('#checkbox-map-visible-pair').change(function() {
            elementHandler.googleMap.nodeMap.visiblePair($(this).prop('checked'));
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });
        $('#checkbox-map-visible-poi').change(function() {
            elementHandler.googleMap.nodeMap.visiblePOI($(this).prop('checked'));
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
            /**
             * POI 표시 상태를 변경할 경우 노드,비콘 검색 결과에대한 InfoWindow들을 제거
             * 2015-04-02 nohsoo
             */
            elementHandler.searchForm.clearInfoWindow();
        });
        /**
         * 교차점 표시/감춤
         */
        $('#checkbox-map-visible-jointname').change(function() {
            elementHandler.googleMap.nodeMap.visibleJointName($(this).prop('checked'));
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
            /**
             * POI 표시 상태를 변경할 경우 노드,비콘 검색 결과에대한 InfoWindow들을 제거
             * 2015-04-06 nohsoo
             */
            elementHandler.searchForm.clearInfoWindow();
        });
        /**
         * create: 2015-04-23 nohsoo 혼돈지역 표시 토글
         */
        $('#checkbox-map-visible-chaosarea').change(function() {
            elementHandler.googleMap.chaosAreaMap.changeVisible($(this).prop('checked'))
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });

        elementHandler.googleMap.nodeMap.visiblePOI(false);

        var controlPosition = google.maps.ControlPosition.LEFT_TOP;
        var optionButtons = document.getElementById('map-view-options');
        mapControls[controlPosition].push(optionButtons);



        /**
         * 층 선택 박스
         */
        $('#floor-selector').on('change', function() {
            var $select = $(this);
            var floor = $select.val();
            elementHandler.googleMap.floorMap.changeFloor(floor);
            elementHandler.googleMap.beaconMap.floor = floor;
            elementHandler.googleMap.beaconMap.load();
            elementHandler.googleMap.nodeMap.floor = floor;
            elementHandler.googleMap.nodeMap.load();
            elementHandler.googleMap.chaosAreaMap.floor = floor;
            elementHandler.googleMap.chaosAreaMap.load();
        });


        /**
         * 노드 일괄 생성 관련
         */
        elementHandler.googleMap.markerGenerator = new MarkerGenerator(elementHandler.googleMap);
        elementHandler.googleMap.markerGenerator.setOnCreateLatlngHandler(function(latlng, index) {
            if(elementHandler.googleMap.nodeMap.mode==NodeMap.MODE_CREATENODE) {
                if(elementHandler.googleMap.nodeMap.nodeAddEnabled==true){
//                    console.log(latlng, index);
                    elementHandler.googleMap.nodeMap.quickAddNode(latlng);
                }
            }
        });
    });


    /**
     * 노드, 비콘 검색
     * 2015-04-01 nohsoo
     */
    elementHandler.searchForm = (function($) {
        var showed = false;
        var searchOption = 'node';
        var infoWindows = [];

        $('#search-option-beacon').click(function() {
            elementHandler.searchForm.changeOption('beacon');
        });
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
//                if(option=='beacon') {
//                    $('#search-menu-btn').html('비콘 <span class="caret"></span>');
//                    searchOption = option;
//                } else if(option=='node') {
//                    $('#search-menu-btn').html('노드 <span class="caret"></span>');
//                    searchOption = option;
//                }
                if(option=='beacon') {
                    $('#search-menu-btn').html($("#search-option-beacon").text() + ' <span class="caret"></span>');
                    searchOption = option;
                } else if(option=='node') {
                    $('#search-menu-btn').html($("#search-option-node").text() + ' <span class="caret"></span>');
                    searchOption = option;
                }
            },
            search: function(keyword) {
                if(showed==false) {
                    this.show();
                } else {
                    var word = keyword || $('#search-keyword').val();

                    if(word) {
                        elementHandler.googleMap.nodeMap.visiblePOI(false);
                        elementHandler.googleMap.nodeMap.visibleJointName(false);
                        $('#checkbox-map-visible-poi').prop('checked', false);
                        $('#checkbox-map-visible-poi').parent().removeClass('active');
                        elementHandler.searchForm.clearInfoWindow();
                        var matchCount = 0;
                        if(searchOption=='node') {
                            var nodeData = elementHandler.googleMap.nodeMap.nodeData;
                            for(var key in nodeData) {
                                var node = nodeData[key];
                                if (String(node.nodeName).match(eval('/'+word+'.*/'))) {
                                    matchCount++;
                                    var marker = elementHandler.googleMap.nodeMap.markers[node.nodeID];
                                    elementHandler.searchForm.createInfoWindow(marker, node.nodeName);
                                }
                                /**
                                 * 2015-04-24 nohsoo nodeID 도 검색 되도록 추가
                                 */
                                else if (String(node.nodeID).match(eval('/'+word+'.*/'))) {
                                    matchCount++;
                                    var marker = elementHandler.googleMap.nodeMap.markers[node.nodeID];
                                    elementHandler.searchForm.createInfoWindow(marker, node.nodeID);
                                }
                            }
                        } else if(searchOption=='beacon') {
                            var markers = elementHandler.googleMap.beaconMap.markers;
                            for(var key in markers) {
                                var marker = markers[key];
                                var beacon = marker.beacon;
                                if((String(beacon.minorVer))==(String(word))) {
                                    matchCount++;
                                    elementHandler.searchForm.createInfoWindow(marker, (String(beacon.minorVer)));
                                }
                            }
                        }
                        if(matchCount==0) {
                            window.alert(vm.searchFail);
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
                infoWindow.open(elementHandler.googleMap.map, marker);
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

});


/**
 * 키보드를 이용한 노드,비콘 위치 이동
 * 2015-04-01 nohsoo
 */
var KeyboardNodeBeaconControl = {
    timeoutHandler: null,
    movedistance: 0.0000005, // 이동간격
    left: function(info) {
        var marker = info.marker;
        var latLng = marker.getPosition();
        var tLatLng = new google.maps.LatLng(latLng.lat(), latLng.lng()-this.movedistance);
        marker.setPosition(tLatLng);

        if(KeyboardNodeBeaconControl.timeoutHandler!=null) {
            window.clearTimeout(KeyboardNodeBeaconControl.timeoutHandler);
        }
        KeyboardNodeBeaconControl.timeoutHandler = window.setTimeout(function() {
            (function(info, latlng) {
                if(info.type=='node') {
                    elementHandler.googleMap.nodeMap.moveNode(info.node.nodeNum, latlng);
                } else if(info.type=='beacon') {
                    elementHandler.googleMap.beaconMap.onDragEnd(info.marker, info.beacon);
                }
            })(info, tLatLng);
        }, 1000);
    },
    right: function(info) {
        var marker = info.marker;
        var latLng = marker.getPosition();
        var tLatLng = new google.maps.LatLng(latLng.lat(), latLng.lng()+this.movedistance);
        marker.setPosition(tLatLng);

        if(KeyboardNodeBeaconControl.timeoutHandler!=null) {
            window.clearTimeout(KeyboardNodeBeaconControl.timeoutHandler);
        }
        KeyboardNodeBeaconControl.timeoutHandler = window.setTimeout(function() {
            (function(info, latlng) {
                if(info.type=='node') {
                    elementHandler.googleMap.nodeMap.moveNode(info.node.nodeNum, latlng);
                } else if(info.type=='beacon') {
                    elementHandler.googleMap.beaconMap.onDragEnd(info.marker, info.beacon);
                }
            })(info, tLatLng);
        }, 1000);
    },
    up: function(info) {
        var marker = info.marker;
        var latLng = marker.getPosition();
        var tLatLng = new google.maps.LatLng(latLng.lat()+this.movedistance, latLng.lng());
        marker.setPosition(tLatLng);

        if(KeyboardNodeBeaconControl.timeoutHandler!=null) {
            window.clearTimeout(KeyboardNodeBeaconControl.timeoutHandler);
        }
        KeyboardNodeBeaconControl.timeoutHandler = window.setTimeout(function() {
            (function(info, latlng) {
                if(info.type=='node') {
                    elementHandler.googleMap.nodeMap.moveNode(info.node.nodeNum, latlng);
                } else if(info.type=='beacon') {
                    elementHandler.googleMap.beaconMap.onDragEnd(info.marker, info.beacon);
                }
            })(info, tLatLng);
        }, 500);
    },
    down: function(info) {
        var marker = info.marker;
        var latLng = marker.getPosition();
        var tLatLng = new google.maps.LatLng(latLng.lat()-this.movedistance, latLng.lng());
        marker.setPosition(tLatLng);

        if(KeyboardNodeBeaconControl.timeoutHandler!=null) {
            window.clearTimeout(KeyboardNodeBeaconControl.timeoutHandler);
        }
        KeyboardNodeBeaconControl.timeoutHandler = window.setTimeout(function() {
            (function(info, latlng) {
                if(info.type=='node') {
                    elementHandler.googleMap.nodeMap.moveNode(info.node.nodeNum, latlng);
                } else if(info.type=='beacon') {
                    elementHandler.googleMap.beaconMap.onDragEnd(info.marker, info.beacon);
                }
            })(info, tLatLng);
        }, 1000);
    }
}



elementHandler.bind({
});
