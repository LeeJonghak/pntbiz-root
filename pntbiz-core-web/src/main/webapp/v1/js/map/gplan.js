

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
        console.log(mapPopup.$form);
        mapPopup.$element.find('.popup-close-btn').click(function() {
            mapPopup.hide();
        });
        callback(mapPopup.$element);
    });
}
MapPopup.prototype.reload = function(callback) {
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
        console.log(mapPopup.$form);
        mapPopup.$element.find('.popup-close-btn').click(function() {
            mapPopup.hide();
        });
        if(typeof(callback)!='undefined') {
            callback(mapPopup.$element);
        }
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





var FencingMap = function(googleMap, bssServiceRequest, readyFunc) {
    this.googleMap = googleMap;
    this.bssServiceRequest = bssServiceRequest;
    this.readyFunc = readyFunc;
    this.floor = 1;
    this.polys = {};

    this.visible = {
        systemFence: true,
        generalFence: true
    };

    /**
     * 펜스 색상설정
     */
    this.color = {
        // 일반펜스
        general: {
            // 펜스 외곽선 색상
            outline: {
                // 일반
                general: '#FF0000',
                // 마우스오버
                over: '#FF1919'
            },
            // 팬스 내부색상
            fill: {
                // 일반
                general: '#FF0000',
                // 마우스 오버
                over: '#FF1919'
            }
        },
        // 시스템펜스
        system: {
            // 펜스 외곽선 색상
            outline: {
                // 일반
                general: '#6600CC',
                // 마우스오버
                over: '#8330FF'
            },
            // 펜스 내부 색상
            fill: {
                // 일반
                general: '#6600FF',
                // 마우스 오버
                over: '#8330FF'
            }
        }
    }
};
FencingMap.prototype.ready = function() {
    this.readyFunc();
}
FencingMap.prototype.load = function(floor) {
    if(typeof(floor)!='undefined') {
        this.floor = floor;
    }

    var _this = this;
    var data = {floor: this.floor};
    this.bssServiceRequest.call('geofencing/info/all.json', data, function(response) {

        for(var i=0; i<response.geofencings.length; i++) {
            var geofecing = response.geofencings[i];
            _this.drawFencing(geofecing);
        }
    });

};
FencingMap.prototype.clearFencing = function() {
    $.each(this.polys, function(k, poly) {
        poly.setMap(null);
    });
    this.polys = {};
}
FencingMap.prototype.drawFencing = function(geofencing) {

    var _this = this;

    /**
     * 펜스 수정 후 저장을 위한 InfoWindow
     */
    var btnSave = '<input type="button" id="infowindow-fence-edit-confirm-save-'+geofencing.fcNum+'" value="Save" class="btn btn-primary btn-sm" style="margin-right:5px;" />';
    var btnCancel = '<input type="button" id="infowindow-fence-edit-confirm-cancel-'+geofencing.fcNum+'" value="Cancel" class="btn btn-default btn-sm" />';
    var html = '<div><div>'+vm.saveConfirm+'</div><hr style="margin-top: 5px; margin-bottom:7px;" /><div>'+btnSave+''+btnCancel+'</div></div>';
    var infoWindow = new google.maps.InfoWindow({
        content: html,
        poly: null
    });
    google.maps.event.addListener(infoWindow, 'closeclick', function() {
        var geofencing = this.poly.geofencing;
        var confirmWindow = this;
        /**
         * 펜스 도형 수정: InfoWindow Close 버튼 클릭
         */
        //if(window.confirm('수정한 내용을 취소하시겠습니까?')) {
            if(geofencing.fcShape=='C') {
                var latlng = geofencing.latlngs[0];
                var center = new google.maps.LatLng(latlng.lat, latlng.lng);
                var radius = latlng.radius;
                confirmWindow.poly.setCenter(center);
                confirmWindow.poly.setRadius(radius);
            }
            else if(geofencing.fcShape=='P') {
                confirmWindow.poly.getPath().clear();
                for(var i=0;i<geofencing.latlngs.length;i++) {
                    var latlng = geofencing.latlngs[i];
                    confirmWindow.poly.getPath().push(new google.maps.LatLng(latlng.lat, latlng.lng));
                }

            }
            confirmWindow.close();
        //}
    });
    google.maps.event.addListener(infoWindow, 'domready', function() {
        var geofencing = this.poly.geofencing;
        var confirmWindow = this;
        if(this.readyButtonEvent!=true) {
            /**
             * 펜스 도형 수정: 저장 버튼 클릭
             */
            $('#infowindow-fence-edit-confirm-save-'+geofencing.fcNum).click(function() {
                /**
                 * 파라미터 정리
                 */
                var data = { fcNum: geofencing.fcNum,
                             fcShape: geofencing.fcShape,
                             fcName: geofencing.fcName,
                             floor: geofencing.floor,
                             shapeData:'' };
                var shapeData = [];
                if(geofencing.fcShape=='C') {
                    shapeData[shapeData.length] = {
                        lat: confirmWindow.poly.getCenter().lat(),
                        lng: confirmWindow.poly.getCenter().lng(),
                        radius: confirmWindow.poly.getRadius(),
                        order: 0
                    }
                }
                else if(geofencing.fcShape=='P') {
                    var path = confirmWindow.poly.getPath();
                    for(var i=0; i<path.getLength(); i++) {
                        shapeData[shapeData.length] = {
                            lat: path.getAt(i).lat(),
                            lng: path.getAt(i).lng(),
                            radius: 0,
                            order: i
                        }
                    }
                }
                data.shapeData = JSON.stringify(shapeData);
                /**
                 * 데이터 전송
                 * geofencing/info/mod.do
                 */
                _this.bssServiceRequest.post('geofencing/info/mod.do', data, function(response) {
                    if (response.result == '1') {
                        geofencing.latlngs = shapeData;
                        confirmWindow.close();
                    }
                    else if (response.result != '1') {
                        window.alert(vm.modError);
                    }
                })

            });

            $('#infowindow-fence-edit-confirm-cancel-'+geofencing.fcNum).click(function() {
                /**
                 * 펜스 도형 수정: 취소 버튼 클릭
                 */
                //if(window.confirm('수정한 내용을 취소하시겠습니까?')) {
                    if (geofencing.fcShape == 'C') {
                        var latlng = geofencing.latlngs[0];
                        var center = new google.maps.LatLng(latlng.lat, latlng.lng);
                        var radius = latlng.radius;
                        confirmWindow.poly.setCenter(center);
                        confirmWindow.poly.setRadius(radius);
                    }
                    else if (geofencing.fcShape == 'P') {
                        confirmWindow.poly.getPath().clear();
                        for (var i = 0; i < geofencing.latlngs.length; i++) {
                            var latlng = geofencing.latlngs[i];
                            confirmWindow.poly.getPath().push(new google.maps.LatLng(latlng.lat, latlng.lng));
                        }

                    }
                    confirmWindow.close();
                //}
            });

            this.readyButtonEvent = true;
        }

    });
    infoWindow.setOptions({disableAutoPan:true});

    if(geofencing.fcShape=='C') {
        /**
         * 원형 펜스 드로잉
         */
        var latLng = new google.maps.LatLng(geofencing.latlngs[0].lat, geofencing.latlngs[0].lng);
        var populationOptions = {
            strokeColor: geofencing.fcType=='S'?this.color.system.outline.general:this.color.general.outline.general,
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: geofencing.fcType=='S'?this.color.system.fill.general:this.color.general.fill.general,
            fillOpacity: 0.35,
            center: latLng,
            radius: geofencing.latlngs[0].radius,
            geofencing: geofencing
        };
        var circle = new google.maps.Circle(populationOptions);
        if(geofencing.fcType=='S') {
            if(this.visible.systemFence==true) {
                circle.setMap(this.googleMap.map);
            } else {
                circle.setMap(null);
            }
        } else if(geofencing.fcType=='G') {
            if(this.visible.generalFence==true) {
                circle.setMap(this.googleMap.map);
            } else {
                circle.setMap(null);
            }
        }

        this.polys[geofencing.fcNum] = circle;

        /**
         * 펜스 이벤트 등록
         */
        google.maps.event.addListener(circle, 'click', function(event) {
            _this.onFencingClickEvent(event, geofencing, circle)
        });
        google.maps.event.addListener(circle, 'radius_changed', function() {
            if(this.infoWindow.getMap()==null) {
                this.infoWindow.open(this.map);
            }
        });
        google.maps.event.addListener(circle, 'center_changed', function(event) {
            this.infoWindow.setPosition(this.getCenter());
            if(this.infoWindow.getMap()==null) {
                this.infoWindow.open(this.map);
            }
        });

        /**
         * 마우스 오버시 펜스 색상 변화
         */
        google.maps.event.addListener(circle, 'mouseover', function() {
            if(geofencing.fcType=='S') {
                circle.setOptions({strokeColor:_this.color.system.outline.over,fillColor:_this.color.system.fill.over});
            } else {
                circle.setOptions({strokeColor:_this.color.general.outline.over,fillColor:_this.color.general.fill.over});
            }
        });
        google.maps.event.addListener(circle, 'mouseout', function() {
            if(geofencing.fcType=='S') {
                circle.setOptions({strokeColor:_this.color.system.outline.general,fillColor:_this.color.system.fill.general});
            } else {
                circle.setOptions({strokeColor:_this.color.general.outline.general,fillColor:_this.color.general.fill.general});
            }
        });


        /**
         * InfoWindow Center
         */
        infoWindow.setPosition(circle.getCenter());
        infoWindow.poly = circle;
        circle.infoWindow = infoWindow;


    }
    else if(geofencing.fcShape=='P') {

        /**
         * 다각형 펜스 드로잉
         */
        var poly = new google.maps.Polygon({
            strokeColor: geofencing.fcType=='S'?this.color.system.outline.general:this.color.general.outline.general,
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: geofencing.fcType=='S'?this.color.system.fill.general:this.color.general.fill.general,
            fillOpacity: 0.35,
            geofencing: geofencing
        });

        if(geofencing.fcType=='S') {
            if(this.visible.systemFence==true) {
                poly.setMap(this.googleMap.map);
            } else {
                poly.setMap(null);
            }
        } else if(geofencing.fcType=='G') {
            if(this.visible.generalFence==true) {
                poly.setMap(this.googleMap.map);
            } else {
                poly.setMap(null);
            }
        }

        for(var i=0; i<geofencing.latlngs.length; i++) {
            var latLng = new google.maps.LatLng(geofencing.latlngs[i].lat, geofencing.latlngs[i].lng);
            poly.getPath().push(latLng);
        }

        this.polys[geofencing.fcNum] = poly;

        /**
         * 펜스 클릭 이벤트 등록
         */
        google.maps.event.addListener(poly, 'click', function(event) {
            _this.onFencingClickEvent(event, geofencing, poly)
        });
        google.maps.event.addListener(poly.getPath(), 'insert_at', function() {
            if(this.infoWindow.getMap()==null) {
                this.infoWindow.open(this.map);
            }
        });
        google.maps.event.addListener(poly.getPath(), 'remove_at', function() {
            if(this.infoWindow.getMap()==null) {
                this.infoWindow.open(this.map);
            }
        });
        google.maps.event.addListener(poly.getPath(), 'set_at', function() {
            var center = this.getAt(0);
            this.infoWindow.setPosition(center);
            if(this.infoWindow.getMap()==null) {
                this.infoWindow.open(this.map);
            }
        });

        /**
         * 마우스 오버시 펜스 색상 변화
         */
        google.maps.event.addListener(poly, 'mouseover', function() {
            if(geofencing.fcType=='S') {
                poly.setOptions({strokeColor:_this.color.system.outline.over,fillColor:_this.color.system.fill.over});
            } else {
                poly.setOptions({strokeColor:_this.color.general.outline.over,fillColor:_this.color.general.fill.over});
            }
        });
        google.maps.event.addListener(poly, 'mouseout', function() {
            if(geofencing.fcType=='S') {
                poly.setOptions({strokeColor:_this.color.system.outline.general,fillColor:_this.color.system.fill.general});
            } else {
                poly.setOptions({strokeColor:_this.color.general.outline.general,fillColor:_this.color.general.fill.general});
            }
        });

        /**
         * 모서리 제거 이벤트
         */
        google.maps.event.addListener(poly, 'rightclick', function(mev){
            if (mev.vertex != null && this.getPath().getLength() > 3) {
                this.getPath().removeAt(mev.vertex);
            }
        });

        /**
         * InfoWindow Center
         */
        var center = poly.getPath().getAt(0);
        infoWindow.setPosition(center);
        infoWindow.poly = poly;
        poly.infoWindow = infoWindow;
        poly.getPath().infoWindow = infoWindow;
        poly.getPath().map = poly.map;
    }



};
FencingMap.prototype.onFencingClickEvent = function(event, geofencing, poly) {

    elementHandler.mapPopup.remote('map/geofencingInfo.ajax.do',{fcNum:geofencing.fcNum}, function($element) {
        $element.find('#modBtn').click(function() {
            elementHandler.mapPopup.submit('geofencing/info/mod.do', function(data) {
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
    elementHandler.mapPopup.show();


};
FencingMap.prototype.visibleGeneralFence = function(visible) {
    this.visible.generalFence = visible;
    var _this = this;
    $.each(this.polys, function(k, poly) {
        var fenceType = poly.geofencing.fcType;
        if(fenceType=='G') {
            if(visible==true) {
                poly.setMap(_this.googleMap.map);
            } else {
                poly.setMap(null);
            }
        }
    });
}
FencingMap.prototype.visibleSystemFence = function(visible) {
    this.visible.systemFence = visible;
    var _this = this;
    $.each(this.polys, function(k, poly) {
        var fenceType = poly.geofencing.fcType;
        if(fenceType=='S') {
            if(visible==true) {
                poly.setMap(_this.googleMap.map);
            } else {
                poly.setMap(null);
            }
        }
    });
}
FencingMap.prototype.enableEditable = function(enable) {

    $.each(this.polys, function(k, poly) {
        if(enable==true) {
            poly.setEditable(true);
            poly.setDraggable(true);
        } else {
            poly.setEditable(false);
            poly.setDraggable(false);
        }
    });
}


elementHandler.ready(function() {

    /**
     * 정보 표시 팝업
     * @type {MapPopup}
     */
    elementHandler.mapPopup = new MapPopup('popup-info');

    /**
     * 구글 지도 초기화
     * @type {GoogleMap}
     */
    elementHandler.googleMap = new GoogleMap('map-canvas', function() {
        this.map.setZoom(20);

        var mapControls = elementHandler.googleMap.map.controls;
        var controlPosition = google.maps.ControlPosition.RIGHT_TOP;
        mapControls[controlPosition].push(elementHandler.mapPopup.element);

        this.bssServiceRequest = new BssServiceRequest(elementHandler.baseurl);
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
                    if (floorMap.currentFloor == floor.floor) {
                        selectTag = ' selected="selected" ';
                    }
                    var optionHtml = '<option value="' + floor.floor + '" ' + selectTag + '>' + floor.floor + 'F</option>';
                    $select.append(optionHtml);
                    added.push(floor.floor);
                }
            })
        });

        this.fencingMap = new FencingMap(this, this.bssServiceRequest, function() {});

        /**
         * 중앙 버튼 배치
         */
        var controlPosition = google.maps.ControlPosition.TOP_CENTER;
        var modeButtons = document.getElementById('map-mode-buttons');
        mapControls[controlPosition].push(modeButtons);

        /**
         * 지도 표지 옵션 버튼 추가
         */
        $('#checkbox-visible-sf').change(function() {
            elementHandler.googleMap.fencingMap.visibleSystemFence($(this).prop('checked'));
        });
        $('#checkbox-visible-gf').change(function() {
            elementHandler.googleMap.fencingMap.visibleGeneralFence($(this).prop('checked'));
        });
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
            elementHandler.googleMap.fencingMap.floor = floor;
            elementHandler.googleMap.fencingMap.clearFencing();
            elementHandler.googleMap.fencingMap.load();
        });


        $('#btn-map-mode1').on('change', function() {
            if($(this).prop('checked')==true) {
                elementHandler.googleMap.fencingMap.enableEditable(false);
            }
        });
        $('#btn-map-mode2').on('change', function() {
            if($(this).prop('checked')==true) {
                elementHandler.googleMap.fencingMap.enableEditable(true);
            }
        });
    });
});








