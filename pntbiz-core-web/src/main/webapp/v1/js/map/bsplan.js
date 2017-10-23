elementHandler.ready(function() {

    elementHandler.googleMap = new GoogleMap('map-canvas', function() {
        var googleMap = this;
        googleMap.serviceRequest = new ServiceRequest(elementHandler.baseurl);
        googleMap.serviceRequest.bindLoadingBar('map-loading-bar');

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
        $('#checkbox-map-visible-beacon').change(function() {
            var that = this;
            googleMap.eachShapeObject('marker', 'beacon', function(marker) {
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

        /**
         * 팝업 레이어 지도에 등록
         */
        googleMap.mapPopup = new MapPopup('info-popup');
        elementHandler.mapPopup = googleMap.mapPopup;
        //var controlPosition = google.maps.ControlPosition.RIGHT_TOP;
        //mapControls[controlPosition].push(googleMap.mapPopup.element);

        /**
         * 노드, 비콘 검색
         */
        elementHandler.searchForm = (function($) {
            var showed = false;
            var searchOption = 'beacon';
            var infoWindows = [];

            $('#search-option-beacon').click(function() {
                elementHandler.searchForm.changeOption('beacon');
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
                    if(option=='beacon') {
                        //$('#search-menu-btn').html('비콘 <span class="caret"></span>');
                    	$('#search-menu-btn').html($("#search-option-beacon").text() + ' <span class="caret"></span>');
                        searchOption = option;
                    }
                },
                search: function(keyword) {
                    if(showed==false) {
                        this.show();
                    } else {
                        var word = keyword || $('#search-keyword').val();

                        if(word) {
                            googleMap.clearShapeObject('infoWindow', 'info');
                            var matchCount = 0;
                            if(searchOption=='beacon') {
                                var markers = googleMap.getShapeObjects('marker', 'beacon');
                                console.log(markers);
                                for(var i=0; i<markers.length; i++) {
                                    var marker = markers[i];
                                    var beacon = marker.data;
                                    console.log((String(word)), (String(beacon.minorVer)));
                                    if((String(beacon.minorVer))==(String(word))) {
                                        matchCount++;
                                        elementHandler.googleMap.createInfoWindow('info', 'info-'+marker.data.beaconNum, (String(beacon.minorVer)), marker.data)
                                            .open(googleMap.getMap(), marker);

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
                }
            }
        })($);
        elementHandler.searchForm.hide();

        /**
         * 층 선택
         */
        this.floorService = new FloorService(this, googleMap.serviceRequest);
        this.floorService.bindFloorSelectBox('floor-selector');
        this.floorService.setOnChangeFloor(function(floorNum) {
            console.log('floor change:' + floorNum);
            this.getServiceRequest().get('beacon/info/all.json',{floor:this.getCurrentFloor()}, function(response) {
                /**
                 * 표시되어 있는 비콘 아이콘 모두 제거
                 */
                googleMap.clearShapeObject('marker', 'beacon');

                for(var i=0; i<response.beacons.length; i++) {
                    var beacon = response.beacons[i];
                    var latlng = new google.maps.LatLng(beacon.lat, beacon.lng);

                    /**
                     * 비콘 상태에 따른 아이콘 설정
                     */
                    var markerOption = {};
                    var mtime = 60*60*24*30; // 30일 동안 업데이트 되지 않은 경우 주황색 마크로 표시
                    var nowtime = new Date().getTime()/1000;

                    if(beacon.state=='2') {
                        markerOption.icon = 'https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_red_10.png';
                    } else if(beacon.state=='1') {
                        markerOption.icon = 'https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_green_10.png';
                    } else {
                        if(nowtime-beacon.lastDate>=mtime) {
                            markerOption.icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_orange_10.png';
                        } else {
                            markerOption.icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_blue_10.png';
                        }
                    }

                    /**
                     * 마커 생성
                     */
                    var marker = googleMap.createMarker('beacon', 'beacon-'+beacon.beaconNum, latlng, markerOption, beacon, function() {
                        this.onCreateComplete(true);
                    });

                    /**
                     * 비콘 클릭 이벤트
                     */
                    marker.addListener('click', function() {
                        googleMap.clearShapeObject('infoWindow', 'info');
                        var infoWindowHtml = '<span>'+this.data.minorVer+'</span> <input id="infowindow-beacon-'+this.data.beaconNum+'" type="button" value="Info" class="btn btn-default btn-sm" />';
                        googleMap.createInfoWindow('info', 'info-'+this.data.beaconNum, infoWindowHtml, this.data)
                            .open(googleMap.getMap(), this);

                        var that = this;
                        $('#infowindow-beacon-'+this.data.beaconNum).click(function() {
                            //googleMap.clearShapeObject('infoWindow', 'info', 'info-'+that.data.beaconNum);
                            googleMap.mapPopup.remote('map/beaconStatus.ajax.do',{beaconNum:that.data.beaconNum}, function($element) {

                            });
                            googleMap.mapPopup.show();
                        });
                    });

                    /**
                     * 비콘 표시 옵션 확인
                     */
                    if($('#checkbox-map-visible-beacon').prop('checked')!=true) {
                        marker.hide();
                    }
                }



                if(hash!=null & hash!='') {
                    var beaconNum = hash.split('/')[1];
                    var marker = googleMap.getShapeObject('marker', 'beacon', 'beacon-'+beaconNum);
                    googleMap.clearShapeObject('infoWindow', 'info');
                    var infoWindowHtml = '<span>'+marker.data.minorVer+'</span> <input id="infowindow-beacon-'+marker.data.beaconNum+'" type="button" value="Info" class="btn btn-default btn-sm" />';
                    googleMap.createInfoWindow('info', 'info-'+marker.data.beaconNum, infoWindowHtml, marker.data)
                        .open(googleMap.getMap(), marker);

                    googleMap.mapPopup.remote('map/beaconStatus.ajax.do',{beaconNum:marker.data.beaconNum}, function($element) {

                    });
                    googleMap.mapPopup.show();
                }
            });
        });

        var hash = window.location.hash.substr(1);
        if(hash!=null & hash!='') {
            var floor = hash.split('/')[0];
            this.floorService.load(floor);
        } else {
            this.floorService.load();
        }
        this.getMap().setZoom(20);

        window.setInterval(function() {
            elementHandler.googleMap.floorService.changeFloor(elementHandler.googleMap.floorService.getCurrentFloor());
            console.log(elementHandler.googleMap.floorService.getCurrentFloor());
        }, 30000);
    });
});
