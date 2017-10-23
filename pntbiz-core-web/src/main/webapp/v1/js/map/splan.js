
elementHandler.Node = (function($) {

    var nodeIdMap = {};

    return {
        putNodeId: function(nodeNum, nodeId) {
            nodeIdMap[nodeNum] = nodeId;
        },
        getNodeId: function(nodeNum) {
            return nodeIdMap[nodeNum];
        },
        removeNodeId: function(nodeNum) {
            delete nodeIdMap[nodeNum];
        },
        clearNodeIdMap: function() {
            nodeIdMap = {};
        },
        createMarker: function(undo, nodeInfo) {
            var icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_blue_10.png';
            var latlng = new google.maps.LatLng(nodeInfo.lat, nodeInfo.lng);
            if(nodeInfo.nodeName) icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_green_10.png';
            var marker = elementHandler.googleMap.createMarker('node', 'node_'+nodeInfo.nodeID, latlng, {icon:icon}, nodeInfo, function() {
                if(elementHandler.googleMap.mapMode==2) {
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
                    elementHandler.googleMap.mapUndoManager.pushJob(function(data) {
                        data.marker.setPosition(data.originalLatLng);
                        var param = {
                            nodeNum: marker.data.nodeNum,
                            lat: data.marker.getPosition().lat(),
                            lng: data.marker.getPosition().lng()
                        };
                        elementHandler.googleMap.serviceRequest.post('map/modifyNode.do', param, function(response) {});
                    }, {originalLatLng: new google.maps.LatLng(marker.data.lat, marker.data.lng), marker: marker});
                    /** end **/

                    elementHandler.googleMap.serviceRequest.post('map/modifyNode.do', param, function(response) {
                        marker.data.lat = param.lat;
                        marker.data.lng = param.lng;
                        marker.onUpdateComplete(true);
                    });
                });
                this.onCreateComplete(true);
            });

            /**
             * 노드 마커를 마우스로 클릭할경우 정보창 표시
             */
            marker.addListener('click', function() {
                var that = this;
                that.getGoogleMap().mapPopup.remote('map/nodeInfo.ajax.do',{nodeNum:this.data.nodeNum}, function($element) {
                    $element.find('#nodeModBtn').click(function() {
                        var $form = $('#map-popup-form-node');
                        if($form.valid()) {
                            if($('#joinName').val()=='') $('#joinName').val('-');
                            $.ajax({ type: "POST",
                                contentType: 'application/x-www-form-urlencoded',
                                processData: false,
                                url: that.getGoogleMap().mapPopup.baseurl + 'map/modifyNode.do',
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
                that.getGoogleMap().mapPopup.show();
            });

            /**
             * 노드마크 오른쪽 마우스 클릭시 삭제 처리
             * 연결된 라인도 삭제되지만 되돌리기시 삭제된 라인을 복구 되지 않음.
             */
            marker.addListener('rightclick', function() {
                if(elementHandler.googleMap.mapMode==3) {
                    this.setIcon('https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_gray_10.png');
                    var nodeMarker = this;
                    elementHandler.googleMap.serviceRequest.post('map/delNode.do', {nodeNum: this.data.nodeNum}, function (response) {
                        nodeMarker.remove();

                        /**
                         * 라인 삭제
                         */
                        if (typeof nodeMarker.linePoints != 'undefined') {
                            for (var i = 0; i < nodeMarker.linePoints.length; i++) {
                                var polyline = nodeMarker.linePoints[i].polyline;
                                polyline.setColor('gray','gray');
                                polyline.setMouseOverEffect(false);
                                elementHandler.googleMap.getPairLineManager().remove(polyline);
                            }
                        }

                        /**
                         * 노드 삭제 되돌리기 - 재생성
                         */
                        elementHandler.googleMap.mapUndoManager.pushJob(function (data) {
                            var tempid = 'temp_' + window.makeid(10);
                            var icon = 'https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_gray_10.png';
                            var tempdata = {lat: data.originalLatLng.lat(), lng: data.originalLatLng.lng()};
                            elementHandler.googleMap.createMarker('node_creating', tempid, data.originalLatLng, {icon: icon}, tempdata, function () {
                                var node = {
                                    floor: elementHandler.googleMap.floorService.getCurrentFloor(),
                                    nodeName: '',
                                    lat: this.getPosition().lat(),
                                    lng: this.getPosition().lng(),
                                    type: 'S'
                                };
                                elementHandler.googleMap.serviceRequest.post('map/addNode.do', node, function (response) {
                                    elementHandler.googleMap.getShapeObject('marker', 'node_creating', tempid).remove();
                                    var nodeMarker = elementHandler.Node.createMarker(false, response.node);
                                });
                            });
                        }, {originalLatLng: new google.maps.LatLng(marker.data.lat, marker.data.lng)});
                        /** end */
                    });
                }
            });
            /**
             * 노드 마커 클릭 이벤트
             * 2015-05-12 nohsoo 노드 연결, 연결 해제
             */
            marker.addListener('click', function() {
                if(elementHandler.googleMap.mapMode==4) {
                    var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                    if(infoWindow.data.marker) {
                        /**
                         * 이전에 선택된 마크가 존재할 경우
                         */
                        console.log(infoWindow.data.marker._id);
                        infoWindow.setContent(vm.nodeNextError);
                        elementHandler.Node.connectNode(true, infoWindow.data.marker, this);
                    }

                    infoWindow.data.marker = this;
                    infoWindow.open(elementHandler.googleMap.getMap(), this);

                } else if(elementHandler.googleMap.mapMode==2) {
                    var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                    infoWindow.data.marker = this;
                    infoWindow.setContent(vm.keyboardMoveError);
                    infoWindow.open(elementHandler.googleMap.getMap(), this);
                }
            });

            if(undo==true) {
                /**
                 * 노드 생성 되돌리기 - 삭제
                 */
                elementHandler.googleMap.mapUndoManager.pushJob(function(data) {
                    elementHandler.googleMap.serviceRequest.post('map/delNode.do', {nodeNum:data.nodeNum}, function(response) {});
                    /**
                     * 라인 삭제
                     */
                    if (typeof data.marker.linePoints != 'undefined') {
                        for (var i = 0; i < data.marker.linePoints.length; i++) {
                            var polyline = data.marker.linePoints[i].polyline;
                            polyline.setColor('gray','gray');
                            polyline.setMouseOverEffect(false);
                            elementHandler.googleMap.getPairLineManager().remove(polyline);
                        }
                    }
                    data.marker.remove();

                }, {nodeNum: nodeInfo.nodeNum, marker:marker});
                /** end **/
            }
            return marker;
        },
        createNodeLine: function(undo, startMarker, endMarker, dumyLine) {
            if(dumyLine) {
                dumyLine.setColorset({defaultColor:{fill:'red',stroke:'red'},mouseover:{fill:'blue',stroke:'blue'}});
                dumyLine.setColor('red','red');
                dumyLine.setMouseOverEffect(true);
                dumyLine.setRemoved(true);
            } else {
                var pairLineManager = elementHandler.googleMap.getPairLineManager();
                dumyLine = pairLineManager.connect(startMarker, endMarker);
                if(dumyLine==false) return false;
            }
            dumyLine.addListener('rightclick', function() {
                if(elementHandler.googleMap.mapMode==4) {
                    this.setColor('gray','gray');
                    this.setMouseOverEffect(false);
                    elementHandler.googleMap.getPairLineManager().remove(this);
                }
            });
            dumyLine.setOnDeleteProcHandler(function(line) {
                /**
                 * 노드 연결 삭제
                 * edit 2015-06-24 nohsoo 노드 연결시 nodeNum이 startPoint, endPoint에 사용된 오류, nodeID가 저장되도록 수정
                 */
                var data = {
                    startNodeID: line.data.startMarker.data.nodeID,
                    endNodeID: line.data.endMarker.data.nodeID
                };
                elementHandler.googleMap.serviceRequest.post('map/delPair.do', data, function(response) {
                    line.remove();
                });
            });

        },
        connectNode: function(undo, startMarker, endMarker) {
            var pairLineManager = elementHandler.googleMap.getPairLineManager();
            var line = pairLineManager.connect(startMarker, endMarker);
            if(line) {
                line.setColorset({defaultColor:{fill:'gray',stroke:'gray'},mouseover:{fill:'gray',stroke:'gray'}});
                line.setColor('gray','gray');
                line.setMouseOverEffect(false);
                line.setRemoved(false);

                /**
                 * edit 2015-06-24 nohsoo 노드 연결시 nodeNum이 startPoint, endPoint에 사용된 오류, nodeID가 저장되도록 수정
                 */
                var data = {
                    startNodeID: startMarker.data.nodeID,
                    endNodeID: endMarker.data.nodeID,
                    floor: elementHandler.googleMap.floorService.getCurrentFloor(),
                    type: 'S'
                }
                elementHandler.googleMap.serviceRequest.post('map/registerPair.do', data, function(response) {
                    elementHandler.Node.createNodeLine(true, startMarker, endMarker, line);
                });
            }
        }
    }
})($);

elementHandler.Keyboard = (function($) {
    var timeoutHandler = null;
    var movedistance = 0.0000005;

    return {
        move: function(direction) {
            var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
            if(infoWindow.data.marker) {
                var latLng = infoWindow.data.marker.getPosition();
                var tLatLng = latLng;
                if(direction=='left') {
                    tLatLng = new google.maps.LatLng(latLng.lat(), latLng.lng()-movedistance);
                } else if(direction=='right') {
                    tLatLng = new google.maps.LatLng(latLng.lat(), latLng.lng()+movedistance);
                } else if(direction=='up') {
                    tLatLng = new google.maps.LatLng(latLng.lat()+movedistance, latLng.lng());
                } else if(direction=='down') {
                    tLatLng = new google.maps.LatLng(latLng.lat()-movedistance, latLng.lng());
                }
                infoWindow.data.marker.setPosition(tLatLng);
                infoWindow.data.marker.renderLinePoint(tLatLng);

                if(timeoutHandler!=null) {
                    window.clearTimeout(timeoutHandler);
                }
                timeoutHandler = window.setTimeout(function() {
                    (function(marker) {
                        if(marker.getGroupId()=='node') {
                            // ajax process
                            var param = {
                                nodeNum: marker.data.nodeNum,
                                lat: marker.getPosition().lat(),
                                lng: marker.getPosition().lng()
                            };
                            elementHandler.googleMap.serviceRequest.post('map/modifyNode.do', param, function(response) {});
                        }
                        else if(marker.getGroupId()=='scanner') {
                            // ajax process
                            var param = {
                                scannerNum: marker.data.scannerNum,
                                lat: marker.getPosition().lat(),
                                lng: marker.getPosition().lng()
                            };
                            elementHandler.googleMap.serviceRequest.post('scanner/mod.do', param, function(response) {});
                        }
                    })(infoWindow.data.marker);
                }, 1000);
            }
        },
        left: function() {
            this.move('left');
        },
        right: function() {
            this.move('right');
        },
        up: function() {
            this.move('up');
        },
        down: function() {
            this.move('down');
        }
    }
})($);

elementHandler.ready(function() {

    /**
     * 노드, 비콘 검색
     * 2015-06-19 nohsoo
     */
    elementHandler.searchForm = (function($) {
        var showed = false;
        var searchOption = 'node';
        var infoWindows = [];

        $('#search-option-scanner').click(function() {
            elementHandler.searchForm.changeOption('scanner');
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
//                if(option=='scanner') {
//                    $('#search-menu-btn').html('스캐너 <span class="caret"></span>');
//                    searchOption = option;
//                } else if(option=='node') {
//                    $('#search-menu-btn').html('노드 <span class="caret"></span>');
//                    searchOption = option;
//                }
                if(option=='scanner') {
                    $('#search-menu-btn').html($("#search-option-scanner").text() + ' <span class="caret"></span>');
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
                        $('#checkbox-map-visible-poi').prop('checked', false);
                        $('#checkbox-map-visible-poi').parent().removeClass('active');
                        elementHandler.searchForm.clearInfoWindow();
                        var matchCount = 0;
                        if(searchOption=='node') {
                            elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                                var node = marker.data;
                                if (String(node.nodeName).match(eval('/'+word+'.*/'))) {
                                    matchCount++;
                                    elementHandler.searchForm.createInfoWindow(marker, node.nodeName);
                                }
                                else if (String(node.nodeID).match(eval('/'+word+'.*/'))) {
                                    matchCount++;
                                    elementHandler.searchForm.createInfoWindow(marker, node.nodeID);
                                }
                            });
                        } else if(searchOption=='scanner') {
                            elementHandler.googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                                var node = marker.data;
                                if (String(node.scannerName).match(eval('/'+word+'.*/'))) {
                                    matchCount++;
                                    elementHandler.searchForm.createInfoWindow(marker, node.scannerName);
                                }
                            });
                        }
                        if(matchCount==0) {
                            window.alert(vim.searchFai)
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
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

    elementHandler.googleMap = new GoogleMap('map-canvas', function() {

        var googleMap = this;
        googleMap.mapMode = 1;
        googleMap.serviceRequest = new ServiceRequest(elementHandler.baseurl);
        googleMap.serviceRequest.bindLoadingBar('map-loading-bar');
        googleMap.mapUndoManager = new MapUndoManager();
        googleMap.mapUndoManager.bindUndoButton('btn-map-undo');

        /**
         * 지도 상단 모드 버튼
         */
        var mapControls = googleMap.getMap().controls;
        var modeButtons = document.getElementById('map-mode-buttons');
        mapControls[google.maps.ControlPosition.TOP_CENTER].push(modeButtons);

        /**
         * 지도 좌측 버튼
         */
        var optionButtons = document.getElementById('map-view-options');
        mapControls[google.maps.ControlPosition.LEFT_TOP].push(optionButtons);
        $('#checkbox-map-visible-scanner').change(function() {
            var that = this;
            googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                if($(that).prop('checked')==true) {
                    marker.show();
                } else {
                    marker.hide();
                }
            });
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });
        $('#checkbox-map-visible-node').change(function() {
            var that = this;
            googleMap.eachShapeObject('marker', 'node', function(marker) {
                if($(that).prop('checked')==true) {
                    marker.show();
                } else {
                    marker.hide();
                }
            });
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });
        $('#checkbox-map-visible-pair').change(function() {
            var that = this;
            googleMap.eachShapeObject('line', 'pairline', function(line) {
                if($(that).prop('checked')==true) {
                    line.show();
                } else {
                    line.hide();
                }
            });
            if($(this).prop('checked')!=true) {
                $(this).parent().css('background-color', '');
                $(this).parent().css('color', '#F7FCF7');
            } else {
                $(this).parent().css('background-color', '#A8DB00');
                $(this).parent().css('color', '#171F00');
            }
        });

        /**
         * 노드 연결, 연결해제에서 사용할 InfoWindow 생성
         */
        var nodeInfoWindow = googleMap.createInfoWindow('node-info-window', 'node-info-window', '다음 노드를 선택해 주세요', {marekr:null});
        google.maps.event.addListener(nodeInfoWindow,'closeclick',function(){
            this.data.marker = null;
        });

        /**
         * 팝업 레이어 지도에 등록
         */
        googleMap.mapPopup = new MapPopup('info-popup');
        elementHandler.mapPopup = googleMap.mapPopup;
        var controlPosition = google.maps.ControlPosition.RIGHT_TOP;
        mapControls[controlPosition].push(googleMap.mapPopup.element);

        /**
         * 키보드를 이용한 비콘, 노드 이동 처리
         */
        google.maps.event.addDomListener(document, 'keydown', function (e) {
            if(elementHandler.googleMap.mapMode==2) {
                var code = (e.keyCode ? e.keyCode : e.which);
                console.log('keydown:'+code);
                if(code==87) { // 상
                    elementHandler.Keyboard.up();
                }
                else if(code==83) { // 하
                    elementHandler.Keyboard.down();
                }
                else if(code==65) { // 좌
                    elementHandler.Keyboard.left();
                }
                else if(code==68) { // 우
                    elementHandler.Keyboard.right();
                }
            }
        });

        googleMap.addListener('click', function(event) {
            if(googleMap.mapMode==3) {
                /**
                 * 노드 생성
                 */
                var tempid = 'temp_'+window.makeid(10);
                var icon =  'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_gray_10.png';
                var tempdata = {lat:event.latLng.lat(), lng:event.latLng.lng()};
                googleMap.createMarker('node_creating', tempid, event.latLng, {icon: icon}, tempdata, function() {
                    var node = {
                        floor:googleMap.floorService.getCurrentFloor(),
                        nodeName:'',
                        lat:event.latLng.lat(),
                        lng:event.latLng.lng(),
                        type:'S'
                    };
                    googleMap.serviceRequest.post('map/addNode.do', node, function(response) {
                        googleMap.getShapeObject('marker', 'node_creating', tempid).remove();
                        var nodeMarker = elementHandler.Node.createMarker(true, response.node);
                    });
                });

            }
        });

        /**
         * 층 선택
         */
        this.floorService = new FloorService(this, googleMap.serviceRequest);
        this.floorService.bindFloorSelectBox('floor-selector');
        this.floorService.setOnChangeFloor(function(floorNum) {
            console.log(this);
            console.log('floor change:'+floorNum);

            googleMap.clearShapeObject('marker', 'scanner');
            googleMap.clearShapeObject('marker', 'node');
            //googleMap.clearShapeObject('line', 'pairline');
            googleMap.getPairLineManager().clear();
            elementHandler.Node.clearNodeIdMap();

            /**
             * 스캐너 로딩
             */
            this.getServiceRequest().get('map/splan/scanner/list.do',{floor:this.getCurrentFloor()}, function(response) {
                console.log(response);
                if(response.result=='1') {
                    for(var i=0; i<response.list.length; i++) {
                        var scannerInfo = response.list[i];

                        /**
                         * 스캐너 마커 생성
                         */
                        var latlng = new google.maps.LatLng(scannerInfo.lat, scannerInfo.lng);
                        var markerOption = {
                            icon: 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png'
                        };
                        var marker = googleMap.createMarker('scanner', 'scanner-'+scannerInfo.scannerNum, latlng, markerOption, scannerInfo, function() {
                            this.onCreateComplete(true);
                        });
                        marker.setOnUpdateProcHandler(function(marker) {
                            /**
                             * 마커 좌표 이동 처리
                             */
                            var data = {
                                scannerNum: this.data.scannerNum,
                                lat: this.getPosition().lat(),
                                lng: this.getPosition().lng()
                            }
                            googleMap.serviceRequest.get('map/splan/scanner/modify.do', data, function(response) {
                                if(response.result=='1') {
                                    marker.onUpdateComplete(true);
                                } else {
                                    console.log('scanner move error!!');
                                    console.log(response);
                                }
                            });
                        });
                        marker.addListener('click', function() {
                            googleMap.mapPopup.remote('map/splan/scanner/info.ajax.do',{scannerNum:this.data.scannerNum}, function($element) {
                                $element.find('#scannerModBtn').click(function() {
                                    elementHandler.mapPopup.submit('scanner/mod.do', function(data) {
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
                            });
                            googleMap.mapPopup.show();

                            if(elementHandler.googleMap.mapMode==2) {
                                var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                                infoWindow.data.marker = this;
                                infoWindow.setContent(vm.keyboardMoveError);
                                infoWindow.open(elementHandler.googleMap.getMap(), this);
                            }
                        });

                        /**
                         * 스캐너 표시 옵션 버튼 확인
                         */
                        if($('#checkbox-map-visible-scanner').prop('checked')!=true) {
                            marker.hide();
                        }
                    }
                } else {
                    console.log('scanner data loading error!!');
                }

            });

            /**
             * 노드 목록 로드
             */
            this.getServiceRequest().get('map/nodeAll.json',{floor:this.getCurrentFloor(),type:'S'}, function(response) {

                if(response.result=='1') {
                    for (var i = 0; i < response.nodes.length; i++) {
                        var nodeInfo = response.nodes[i];
                        console.log('nodeInfo');
                        console.log(nodeInfo);
                        var marker = elementHandler.Node.createMarker(false, nodeInfo);
                        elementHandler.Node.putNodeId(nodeInfo.nodeNum, nodeInfo.nodeId);

                        /**
                         * 스캐너 표시 옵션 버튼 확인
                         */
                        if($('#checkbox-map-visible-node').prop('checked')!=true) {
                            marker.hide();
                        }
                    }
                } else{
                    console.log('node data loading error!!');
                }

                /**
                 * 노드 연결정보 로드
                 */
                googleMap.floorService.getServiceRequest().get('map/pairAll.json',{floor:googleMap.floorService.getCurrentFloor(),type:'S'}, function(response) {
                    if(response.result=='1') {
                        console.log('pair data response');
                        console.log(response);
                        for(var i=0; i<response.pairs.length; i++) {
                            var pair = response.pairs[i];
                            //var startNodeId = elementHandler.Node.getNodeId(pair.startPoint);
                            var startNodeId = pair.startPoint;
                            var startMarker = googleMap.getShapeObject('marker', 'node', 'node_'+startNodeId);
                            //var endNodeId   = elementHandler.Node.getNodeId(pair.endPoint);
                            var endNodeId   = pair.endPoint
                            var endMarker   = googleMap.getShapeObject('marker', 'node', 'node_'+endNodeId);
                            var pairLine = elementHandler.Node.createNodeLine(false, startMarker, endMarker);

                            /**
                             * 스캐너 표시 옵션 버튼 확인
                             */
                            if($('#checkbox-map-visible-pair').prop('checked')!=true) {
                                pairLine.hide();
                            }
                        }
                    } else{
                        console.log('pair data loading error!!');
                    }
                });
            });
        });

        this.floorService.load();
        googleMap.getMap().setZoom(20);
        //googleMap.enableMarkerCluster(true);
    });
});


elementHandler.bind({
    'btn-map-mode1': {
        event : 'change',
        action: function() {
            if($(this).prop('checked')==true) {
                console.log('mode change:1');
                elementHandler.googleMap.mapMode = 1;
                elementHandler.googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                    marker.changeDraggable(false);
                });
                elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                    marker.changeDraggable(false);
                });

                var pairLineManager = elementHandler.googleMap.getPairLineManager();
                pairLineManager.eachPairLine(function(line){
                    console.log(line);
                   line.setMouseOverEffect(false);
                });

                var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                infoWindow.close();
            }
        }
    },
    'btn-map-mode2': {
        event : 'change',
        action: function() {
            if($(this).prop('checked')==true) {
                console.log('mode change:2');
                elementHandler.googleMap.mapMode = 2;
                elementHandler.googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                    marker.changeDraggable(true);
                });
                elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                    marker.changeDraggable(true);
                });

                var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                infoWindow.close();
            }
        }
    },
    'btn-map-mode3': {
        event: 'change',
        action: function () {
            if($(this).prop('checked')==true) {
                console.log('mode change:3');
                elementHandler.googleMap.mapMode = 3;
                elementHandler.googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                    marker.changeDraggable(false);
                });
                elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                    marker.changeDraggable(false);
                });

                var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                infoWindow.close();
            }
        }
    },
    'btn-map-mode4': {
        event: 'change',
        action: function () {
            if($(this).prop('checked')==true) {
                console.log('mode change:4');
                elementHandler.googleMap.mapMode = 4;
                elementHandler.googleMap.eachShapeObject('marker', 'scanner', function(marker) {
                    marker.changeDraggable(false);
                });
                elementHandler.googleMap.eachShapeObject('marker', 'node', function(marker) {
                    marker.changeDraggable(false);
                });

                var infoWindow = elementHandler.googleMap.getInfoWindow('node-info-window','node-info-window');
                infoWindow.close();
            }
        }
    }
});
