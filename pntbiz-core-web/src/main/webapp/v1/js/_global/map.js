/**
 * 구글 지도
 * @param element_id 지도 DOM ID
 * @param init_func 지도 로딩 후 실행될 함수
 * @constructor
 */
var GoogleMap = function(element_id, init_func, mapOption) {
    GoogleMap.instances.push(this);
    this.key = GoogleMap.key;
    this.element = document.getElementById(element_id);
    this.map = null;
    this.callback_func_name = 'GoogleMap.readyScript';
    this.init_func = init_func;
    this.infoWindow = null;
    this.markers = [];
    this.mapOption = mapOption;

    if(GoogleMap.instances.length<=1) {
        this.loadScript();
    }
    if(GoogleMap.complate_load_script==true) {
        this.initialize();
    }
}
GoogleMap.instances = [];
GoogleMap.key = 'AIzaSyC5flBQIVK7pcBA6IDA3MZHvIcAWPDMqPQ';
GoogleMap.defaultLat = '';
GoogleMap.defaultLng = '';
GoogleMap.setKey = function(key) {
    GoogleMap.key = key;
}
GoogleMap.setDefaultCenter = function(lat, lng) {
    GoogleMap.defaultLat = lat;
    GoogleMap.defaultLng = lng;
}
GoogleMap.complate_load_script = false;
GoogleMap.readyScript = function() {
    for(var i=0; i<GoogleMap.instances.length; i++) {
        GoogleMap.instances[i].initialize();
    }
    GoogleMap.complate_load_script = true;
}
GoogleMap.prototype.initialize = function() {
    if(this.mapOption!=null) this.map = new google.maps.Map(this.element, this.mapOption);
    else this.map = new google.maps.Map(this.element);

    var center = new google.maps.LatLng(GoogleMap.defaultLat, GoogleMap.defaultLng);
    this.map.setCenter(center);
    this.map.setZoom(14);

    if(typeof(this.init_func)!='undefined') {
        this.init_func();
    }
}
GoogleMap.prototype.loadScript = function() {
    if(typeof location.protocol=='undefined') location.protocol = 'http:';
    var script_url = location.protocol+'//maps.googleapis.com/maps/api/js?v=3.exp&key='+this.key
 // jhlee 20160324 sensor option 제거
//        +'&sensor=SET_TO_TRUE_OR_FALSE&callback='+this.callback_func_name
        +'&callback='+this.callback_func_name
        +'&libraries=visualization,geometry,places';
    var head= document.getElementsByTagName('head')[0];
    var script= document.createElement('script');
    script.type = 'text/javascript';
    script.src = script_url;
    head.appendChild(script);
}
GoogleMap.prototype.addEventListener = function(event_type, callback_func) {
    google.maps.event.addListener(this.map, event_type, callback_func);
}
GoogleMap.prototype.bindSearchBox = function(element_id, map_controls_position) {
    // ex: bindSearchBox('pac-input', this.map.controls[google.maps.ControlPosition.TOP_RIGHT]);
    var _this = this;
    var element = document.getElementById(element_id);
    if(map_controls_position!=null) {
        map_controls_position.push(element);
    }
    var searchBox = new google.maps.places.SearchBox(element);
    google.maps.event.addListener(searchBox, 'places_changed', function() {
        var places = searchBox.getPlaces();
        if (places.length == 0) {
            return;
        }
        var bounds = new google.maps.LatLngBounds();
        for (var i = 0, place; place = places[i]; i++) {
            bounds.extend(place.geometry.location);
        }
        _this.map.fitBounds(bounds);
    });
}
GoogleMap.prototype.showInfoWindow = function(content, marker, maxWidth) {
    if(this.infoWindow==null) {
        this.infoWindow = new google.maps.InfoWindow({ maxWidth: maxWidth==null?300:maxWidth})
    }
    this.infoWindow.setContent(content);
    this.infoWindow.open(this.map, marker);

    return this.infoWindow;
}
GoogleMap.prototype.drawMarker = function(latlng, title, draggable, click_event_callback) {

    var marker = new google.maps.Marker({
        position: latlng,
        title: title,
        draggable: draggable,
        map: this.map
    });
    this.markers.push(marker);
    if(click_event_callback) {
        google.maps.event.addListener(marker, 'click', click_event_callback);
    }
}
GoogleMap.prototype.clearMarker = function() {
    while(this.markers.length>0) {
        var marker = this.markers.pop();
        marker.setMap(null);
    }
}
GoogleMap.prototype.closeInfoWindow = function() {
    if(this.infoWindow!=null) this.infoWindow.close();
}


/**
 * Ajax 요청 관련
 * @param base_url
 * @constructor
 */
var BssServiceRequest = function(base_url) {
    this.base_url = base_url;
    this.ajaxReady = true;

    this._postQueue = [];
    this._postsending = false;
}
BssServiceRequest.prototype.call = function(url, data, callback_func) {
    console.log('## ServiceRequest>call');
    console.log('url:'+this.base_url+url);
    var _this = this;
    _this.ajaxReady = false;
    $.ajax({
        type: "get",
        dataType: 'json',
        url: this.base_url + url,
        data: data,
        cache: false
    }).done(function (json) {

		if(typeof(json)=='string') {
			json = JSON.parse(json);
		}

        _this.ajaxReady = true;
        callback_func(json);
    });
}
BssServiceRequest.prototype.post = function(url, data, callback_func) {
    this._postQueue.push({
        url: this.base_url + url,
        data: data,
        callback_func: callback_func
    });

    if(this._postsending!=true) {
        this.sendPost();
    }
}
BssServiceRequest.prototype.sendPost = function() {
    this._postsending = true;
    if(this._postQueue.length>0) {

        var info = this._postQueue.shift();

        var _this = this;
        _this.ajaxReady = false;

        console.log('## ServiceRequest>post');
        console.log('url:'+info.url);
        $.ajax({
            type: "post",
            dataType: 'json',
            url: info.url,
            data: info.data
        }).done(function( json ) {
			if(typeof(json)=='string') {
				json = JSON.parse(json);
			}
            _this.ajaxReady = true;
            info.callback_func(json);
            _this.sendPost();
        });
    } else {

        this._postsending = false;

    }
}

var RequestInstance = function(ajaxmeta, callback) {
    this.ajaxmeta = ajaxmeta;
    this.callback = callback;
}
RequestInstance.prototype.call = function() {
    var callback = this.callback==null?function() {}:this.callback;
    $.ajax(this.ajaxmeta).done(callback);
}


/**
 * 구글 지도에 층 이미지 표시
 *
 * @param googleMap
 * @param bssServiceRequest
 * @constructor
 */
var FloorMap = function(googleMap, bssServiceRequest, initFloor) {
    this.bssServiceRequest = bssServiceRequest;
    this.googleMap = googleMap;
    this.currentFloor = 1;
    this.floorData = {};
    this.defaultMapZoom = 20;
    this.onFloorData = null;

    var _this = this;

    FloorMap.ExtendOverlay.prototype = new google.maps.OverlayView();
    FloorMap.ExtendOverlay.prototype.onAdd = function() {
        this.div = document.createElement('div');
        this.div.style.borderWidth = '0px';
        this.div.style.position = 'absolute';
        this.div.style.webkitTransform = 'rotate('+this.deg+'deg)';
        this.div.style.mozTransform    = 'rotate('+this.deg+'deg)';
        this.div.style.msTransform     = 'rotate('+this.deg+'deg)';
        this.div.style.oTransform      = 'rotate('+this.deg+'deg)';
        this.div.style.transform       = 'rotate('+this.deg+'deg)';
        this.img = document.createElement('img');
        this.img.src = this.image_;
        this.img.style.width = '100%';
        this.img.style.height = '100%';
        this.img.style.opacity = '1';
        this.img.style.position = 'absolute';
        this.div.appendChild(this.img);
        this.div_ = this.div;
        var panes = this.getPanes();
        panes.overlayLayer.appendChild(this.div);
    };
    FloorMap.ExtendOverlay.prototype.draw = function() {
        var overlayProjection = this.getProjection();
        var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
        var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());
        this.div = this.div_;
        this.div.style.left = sw.x + 'px';
        this.div.style.top = ne.y + 'px';
        this.div.style.width = (ne.x - sw.x) + 'px';
        this.div.style.height = (sw.y - ne.y) + 'px';
    };
    FloorMap.ExtendOverlay.prototype.updateBounds = function(bounds){
        this.bounds_ = bounds;
        this.draw();
    };
    FloorMap.ExtendOverlay.prototype.onRemove = function() {
        this.div_.parentNode.removeChild(this.div_);
        this.div_ = null;
    };

    this.bssServiceRequest.post('map/floor/list.do', {}, function(response) {
        if(response.result=='1') {
            _this.floorData = {};
            var defaultFloor = null;

            // 2015-11-27 nohsoo 층 정렬
            response.data.sort(function(a, b) {
                return parseInt(b.floor)-parseInt(a.floor);
            });
            // end

            for(var i=0; i<response.data.length; i++) {
                var floor = response.data[i];
                _this.floorData[floor.floorNum] = floor;
                var intFloor = parseInt(floor.floor);
                if(defaultFloor==null || intFloor<parseInt(defaultFloor)) {
                    defaultFloor = floor.floor;
                }
            }
            console.log('defaultFloor:'+defaultFloor);
            if(initFloor != "" && initFloor != null) {
            	_this.changeFloor(initFloor);
            } else {
            	_this.changeFloor(defaultFloor);
            }
            if(_this.onFloorData!=null) {
                _this.onFloorData(response.data);
            }
            $('#floor-selector').trigger('change');
        }
        else if(response.result!='1') {
            window.alert(vm.infoError);
        }
    });
};
FloorMap.ExtendOverlay = function(bounds, image, map, deg) {
    this.bounds_ = bounds;
    this.image_ = image;
    this.map_ = map;
    this.div_ = null;
    this.deg = deg;
    this.setMap(map);
};

FloorMap.prototype.setOnFloorData = function(callback) {
    this.onFloorData = callback;
}
FloorMap.prototype.getCurrentFloor = function() {
    return this.currentFloor;
}
FloorMap.prototype.drawFloorImage = function(floor) {
    if(typeof(floor)!='undefined') {
        this.currentFloor = floor;
    }

    this.overlay = [];
    for(var key in this.floorData) {

        var obj = this.floorData[key];
        if(this.currentFloor==obj.floor) {

            if (obj['swLat'] == "" || obj['swLng'] == "" || obj['neLat'] == "" || obj['neLng'] == "") {
                obj['swLat'] = this.initSWLat;
                obj['swLng'] = this.initSWLng;
                obj['neLat'] = this.initNELat;
                obj['neLng'] = this.initNELng;
            }
            var swLatLng = new google.maps.LatLng(obj['swLat'], obj['swLng']);
            var neLatLng = new google.maps.LatLng(obj['neLat'], obj['neLng']);
            var bounds = new google.maps.LatLngBounds(swLatLng, neLatLng);
            var overlay = new FloorMap.ExtendOverlay(bounds, obj['imgURL'], this.googleMap.map, obj['deg']);
            overlay.setMap(this.googleMap.map);
            this.overlay.push(overlay);
            this.deg = obj['deg'];
        }
    }
}
FloorMap.prototype.changeFloor = function(floor) {
    if(this.overlay) {
        for(var i=0; i<this.overlay.length; i++) {
            this.overlay[i].setMap(null);
        }
    }
    this.currentFloor = floor;
    this.drawFloorImage();
}



/**
 * 비콘 표시 지도
 * @param googleMap GoogleMap
 * @param bssServiceRequest ServiceRequest
 * @constructor
 */
var BeaconMap = function(googleMap, bssServiceRequest) {
    this.googleMap = googleMap;
    this.bssServiceRequest = bssServiceRequest;
    this.dataurl = 'beacon/listNear.do';
    this.param = null;
    this.distance = 100; // 비콘 탐색 반지
    this.circle = null;
    this.beaconMarkers = [];
    this.beaconMarkerMap = {};
    this.beaconMarkerClickEvent = null;
    this.onLoadBeaconData = null;
    this.onLoadBeaconBefore = null;
    this.option = {
        showInfoWindow: true,
        maxRadius:100000
    };

    var _this = this;
    google.maps.event.addListener(this.googleMap.map, 'click', function(event) {

        if(_this.onLoadBeaconBefore==null || (_this.onLoadBeaconBefore!=null && _this.onLoadBeaconBefore())) {
            _this.drawDistance(event.latLng.lat(),event.latLng.lng());
            var param = {lat:event.latLng.lat(), lng:event.latLng.lng(), distance:_this.distance};
            _this.param = param;
            _this.bssServiceRequest.call(_this.dataurl, param, function(json) {
                _this.clearBeaconMarker();
                for(var i=0; i<json.ArrayList.length; i++) {
                    var beaconInfo = json.ArrayList[i];
                    _this.drawBeaconMarker(beaconInfo);
                }
                _this.onLoadBeaconData(json.ArrayList);
            });
        }
    });

}
BeaconMap.prototype.setDistance = function(distance) {
    this.distance = distance;
}
BeaconMap.prototype.setDataUrl = function(url) {
    this.dataurl = url;
}
BeaconMap.prototype.setOption = function(option, value) {
    this.option[option] = value;
}
BeaconMap.prototype.drawDistance = function(lat, lng) {
    if(this.circle==null) {
        var populationOptions = {
            strokeColor: '#0000FF',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#0000FF',
            fillOpacity: 0.35,
            map: this.googleMap.map,
            center: new google.maps.LatLng(lat, lng),
            radius: this.distance
        };
        // Add the circle for this city to the map.
        this.circle = new google.maps.Circle(populationOptions);
    } else {
        this.circle.setCenter(new google.maps.LatLng(lat, lng));
    }
}
BeaconMap.prototype.drawBeaconMarker = function(beaconInfo) {
    var lat = beaconInfo.lat;
    var lng = beaconInfo.lng;
    var latlng = new google.maps.LatLng(lat, lng);
    var marker = new google.maps.Marker({
        position: latlng,
        title: beaconInfo.beacon_name,
        draggable:false,
        map: this.googleMap.map,
        beacon: beaconInfo
    });

    var _this = this;
    google.maps.event.addListener(marker, 'click', function(event) {
        if(_this.option.showInfoWindow==true) {
            _this.showBeaconInfoWindow(marker.beacon.beacon_id);
        }
        _this.beaconMarkerClickEvent(event, marker, marker.beacon);
    });
    this.beaconMarkers.push(marker);
    this.beaconMarkerMap[beaconInfo.beacon_id] = marker;
}
BeaconMap.prototype.setBeaconMarkerClickEvent = function(callback_func) {
    this.beaconMarkerClickEvent = callback_func;
}
BeaconMap.prototype.setOnLoadBeaconData = function(callback_func) {
    this.onLoadBeaconData = callback_func;
}
BeaconMap.prototype.setOnLoadBeaconBefore = function(callback_func) {
    this.onLoadBeaconBefore = callback_func;
}
BeaconMap.prototype.clearBeaconMarker = function() {
    for(var i=0; i<this.beaconMarkers.length; i++) {
        var marker = this.beaconMarkers[i];
        marker.setMap(null);
    }
}
BeaconMap.prototype.getBeaconMarker = function(beacon_id) {
    return this.beaconMarkerMap[beacon_id];
}
BeaconMap.prototype.showBeaconInfoWindow = function(beacon_id) {
    var marker = this.getBeaconMarker(beacon_id);
    var beacon = marker.beacon;

    //var infoContent = '<div style="width:150px;">고유코드:'+beacon.beacon_id+'</div>';
    var infoContent = '<div style="width:150px;">Code:'+beacon.beacon_id+'</div>';
    this.googleMap.showInfoWindow(infoContent, marker, 300);
}


/**
 * 구글지도에 동그라미 도형 그리기
 * @param googleMap
 * @param defaultRadius
 * @constructor
 */
var FenceFigure = function(googleMap, defaultRadius, maxRadius) {

    var _this = this;

    this.googleMap = googleMap;
    this.defaultRadius = defaultRadius!=null?defaultRadius:150;
    this.circle = {
        circle: null,
        marker: null,
        lat:null,
        lng:null,
        radius:null,
        maxRadius:maxRadius,
        mapClickEvent:null,
        markerDragendEvent:null,
        markerDragEvent:null,
        markerClickEvent:null
    };
    this.poly = {
        points: new Array(),
        poly:null,
        flagComplete:false,
        mapClickEvent:null,
        firstMarkerClickEvent:null
    };
    this.currentMode = null;

    if($.isNumeric(maxRadius)==true) {
        this.circle.maxRadius = maxRadius;
    }

    if(this.circleRadiusInput==null) {
        this.circleRadiusInput = new google.maps.InfoWindow({ maxWidth: 200})
    }
    this.circleRadiusInputId = window.makeid(10);
    //var infoWindowHtml = '<div>반지름(미터): <input id="'+this.circleRadiusInputId+'" type="number" value="'+this.defaultRadius+'" style="width:80px" /></div>';
    var infoWindowHtml = '<div>Radius(meter): <input id="'+this.circleRadiusInputId+'" type="number" value="'+this.defaultRadius+'" style="width:80px" /></div>';
    this.circleRadiusInput.setContent(infoWindowHtml);
};
FenceFigure.prototype.changeMode = function(mode, forced) {
    var _this = this;
    if(forced==true || this.currentMode!=mode) {
        this.clear();
        if(mode=='circle') {
            this.currentMode = mode;
            var mapClickEvent = google.maps.event.addListener(this.googleMap.map, 'click', function(event) {
                if(_this.circle.circle==null) {
                    _this.drawCircle(event.latLng, _this.defaultRadius, true, true);
                }
                else {
                    _this.circle.circle.setCenter(event.latLng);
                    _this.circle.marker.setPosition(event.latLng);
                    _this.circle.lat = event.latLng.lat();
                    _this.circle.lng = event.latLng.lng();
                }
            });
            this.circle.mapClickEvent = mapClickEvent;

            // redraw
            if(this.circle.lat!=null && this.circle.lng!=null && this.circle.radius!=null) {
                var agoCircleLatlng = new google.maps.LatLng(this.circle.lat, this.circle.lng);
                this.drawCircle(agoCircleLatlng, this.circle.radius);
            }
        }
        else if(mode=='poly') {
            this.currentMode = mode;

            var polyOptions = {
                strokeColor: '#000000',
                strokeOpacity: 1.0,
                strokeWeight: 2,
                fillColor: '#FF0000',
                fillOpacity: 0.35
            };
            this.poly.poly = new google.maps.Polyline(polyOptions);
            this.poly.poly.setMap(this.googleMap.map);
            var mapClickEvent = google.maps.event.addListener(this.googleMap.map, 'click', function(event) {
                if(_this.poly.flagComplete==false) {
                    _this.drawPolyStroke(event.latLng, true, true);
                } else {
                    if(window.confirm(vm.redrawConfirm)) {
                        _this.changeMode('poly', true);
                    }
                }
            });
            this.poly.mapClickEvent = mapClickEvent;
        }
    }

};
FenceFigure.prototype.drawPolyStroke = function(latLng, movable, removable) {
    var _this = this;

    var index = this.poly.points.length;
    if(index>=10) {
        window.alert(vm.addLimit);
        return;
    }

    if(this.poly.poly==null) {
        var polyOptions = {
            strokeColor: '#000000',
            strokeOpacity: 1.0,
            strokeWeig2t: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        };
        this.poly.poly = new google.maps.Polyline(polyOptions);
    }
    this.poly.poly.getPath().push(latLng);

    // create marker
    var marker = new google.maps.Marker({
        position: latLng,
        title: '#' + index,
        draggable: movable,
        map: this.googleMap.map,
        icon: {
            url:'/images/inc/marker_new_read_01.png',
            size:new google.maps.Size(10, 10), anchor: new google.maps.Point(5, 5)
        },
        index: index
    });

    var point = {marker:marker, latLng:latLng, markerRightClickEvent:null, markerDragEvent: null};

    if(index==0 && this.poly.flagComplete==false) {
        var firstMarkerClickEvent = google.maps.event.addListener(marker, 'click', function(event) {
            var firstPoint = _this.poly.points[0];
            _this.poly.poly.getPath().push(firstPoint.latLng);
            _this.drawCompletePoly();

        });
        this.poly.firstMarkerClickEvent = firstMarkerClickEvent;
    }

    if(removable==true) {
        var markerRightClickEvent = google.maps.event.addListener(marker, 'rightclick', function() {
            var index = marker.index;
            if(_this.poly.points.length<=3) {
                alert(vm.delLimit);
            } else {
                var point = _this.poly.points[index];

                // remove path
                _this.poly.poly.getPath().removeAt(point.marker.index);
                if(index==0) {
                    _this.poly.poly.getPath().removeAt(_this.poly.points.length-1);
                    _this.poly.poly.getPath().push(_this.poly.points[1].latLng);
                }

                // remove marker
                point.marker.setMap(null);

                // 배열정리
                _this.poly.points = $.grep(_this.poly.points, function(val, idx) {
                    return index!=idx;
                });
                $.each(_this.poly.points, function(key, value) {
                    value.marker.index = key;
                });
            }
        });
        point.markerRightClickEvent = markerRightClickEvent;
    }

    if(movable==true) {
        var markerDragEvent = google.maps.event.addListener(marker, 'drag', function(event) {
            var path = _this.poly.poly.getPath();
            if(_this.poly.flagComplete==true && marker.index==0) {
                // first path move
                path.removeAt(marker.index);
                path.insertAt(marker.index, event.latLng)
                // last path move
                path.removeAt(_this.poly.points.length);
                path.insertAt(_this.poly.points.length, event.latLng);
            } else {
                path.removeAt(marker.index);
                path.insertAt(marker.index, event.latLng)
            }
            //배열 업데이트
            _this.poly.points[marker.index].latLng = event.latLng;
        });
        point.markerDragEvent = markerDragEvent;
    }

    this.poly.points.push(point);
};
FenceFigure.prototype.drawCompletePoly = function() {
    this.poly.flagComplete = true;
    if(this.poly.firstMarkerClickEvent!=null) {
        this.poly.firstMarkerClickEvent.remove();
        this.poly.firstMarkerClickEvent = null;
    }

    var orgpath = this.poly.poly.getPath();
    this.poly.poly.setMap(null);
    this.poly.poly = new google.maps.Polygon({
        paths: orgpath,
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        editable: false
    });
    this.poly.poly.setMap(this.googleMap.map);
    var poly = this.poly.poly;

    google.maps.event.addListener(poly, 'rightclick', function(mev){
        if (mev.vertex != null && this.getPath().getLength() > 3) {
            this.getPath().removeAt(mev.vertex);
        }
    });

};
FenceFigure.prototype.showCircleRadiusInput = function(marker) {
    var _this = this;
    this.circleRadiusInput.open(this.googleMap.map, marker);
    var $input = $('#'+this.circleRadiusInputId);
    $input.val(this.circle.radius);
    $input.on('keypress', function(event) {
       if(event.keyCode==13) {
           $input.trigger('change');
       }
    });
    $input.on('change', function() {
        var radius = parseInt($(this).val());
        if(radius>_this.circle.maxRadius) {
            radius = _this.circle.maxRadius;
            $(this).val(radius);
        }
        _this.changeCircleRadius(radius);
    });
}
FenceFigure.prototype.changeCircleRadius = function(radius) {
    this.circle.radius = radius;
    this.circle.circle.setRadius(radius);
}
FenceFigure.prototype.drawCircle = function(latLng, radius, movable, showRadiusInput) {

    this.circle.lat = latLng.lat();
    this.circle.lng = latLng.lng();
    this.circle.radius = radius;

    var _this = this;

    /**
     * draw Marker
     */
    this.circle.marker = new google.maps.Marker({
        position: latLng,
        title: 'GeoFence',
        draggable:false,
        map: this.googleMap.map,
        icon: '/images/inc/marker_new_read_01.png'
    });


    /**
     * Register Marker Event
     */
    if(showRadiusInput==true) {
        var markerClickEvent = google.maps.event.addListener(this.circle.marker, 'click', function(event) {
            _this.showCircleRadiusInput(this);
        });
        this.circle.markerClickEvent = markerClickEvent;
        this.showCircleRadiusInput(this.circle.marker);
    }

    /*if(movable==true) {
        var markerDragendEvent = google.maps.event.addListener(this.circle.marker, 'dragend', function(event) {
            _this.circle.circle.setCenter(event.latLng);
        });
        this.circle.markerDragendEvent = markerDragendEvent;
        var markerDragEvent = google.maps.event.addListener(this.circle.marker, 'drag', function(event) {
            _this.circle.circle.setCenter(event.latLng);
        });
        this.circle.markerDragEvent = markerDragEvent;
    }*/

    /**
     * draw Circle
     */
    var populationOptions = {
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        map: this.googleMap.map,
        center: latLng,
        radius: radius,
        editable: showRadiusInput,
        draggable: showRadiusInput
    };

    var circle = new google.maps.Circle(populationOptions);
    circle.marker = this.circle.marker ;
    this.circle.circle = circle;

    if(showRadiusInput==true) {
        google.maps.event.addListener(circle, 'radius_changed', function () {
            var $input = $('#'+_this.circleRadiusInputId);
            $input.val(parseInt(circle.getRadius()));
        });

        google.maps.event.addListener(circle, 'center_changed', function() {
            console.log(this);
            this.marker.setPosition(this.getCenter());
        });
    }
};
FenceFigure.prototype.clear = function() {

    /**
     * Clear Circle
     */
    if(this.circle.marker!=null) this.circle.marker.setMap(null);
    this.circle.marker = null;
    if(this.circle.circle!=null) this.circle.circle.setMap(null);
    this.circle.circle = null;
    if(this.circle.mapClickEvent!=null) {
        this.circle.mapClickEvent.remove();
        this.circle.mapClickEvent = null;
    }
    if(this.circle.markerDragendEvent!=null) {
        this.circle.markerDragendEvent.remove();
        this.circle.markerDragendEvent = null;
    }
    if(this.circle.markerDragendEvent!=null) {
        this.circle.markerDragEvent.remove();
        this.circle.markerDragEvent = null;
    }
    if(this.circle.markerClickEvent!=null) {
        this.circle.markerClickEvent.remove();
        this.circle.markerClickEvent = null;
    }

    /**
     * Clear Poly
     */
    if($.isArray(this.poly.points)) {
        for(var i=0; i<this.poly.points.length; i++) {
            var point = this.poly.points[i];
            if(point.markerRightClickEvent!=null) {
                point.markerRightClickEvent.remove();
                point.markerRightClickEvent = null;
            }
            if(point.markerDragEvent!=null) {
                point.markerDragEvent.remove();
                point.markerDragEvent = null;
            }
            point.marker.setMap(null);
            point = null;
        }
    }
    this.poly.points = new Array();
    if(this.poly.firstMarkerClickEvent!=null) {
        this.poly.firstMarkerClickEvent.remove();
        this.poly.firstMarkerClickEvent = null;
    }
    if(this.poly.mapClickEvent!=null) {
        this.poly.mapClickEvent.remove();
        this.poly.mapClickEvent = null;
    }
    if(this.poly.poly) {
        this.poly.poly.setMap(null);
        this.poly.poly = null;
    }
    this.poly.flagComplete = false;

}




var MarkerGenerator = function(googleMap) {
    this._visible = false;
    this._googleMap = googleMap;
    this._groupid = 'markergnrt';
    this._marker1 = this._createMarker('start');
    this._marker2 = this._createMarker('end');
    this._marker1Event = null;
    this._marker2Event = null;
    this._line1 = null;
    this._inputid = window.makeid(10);
    this._btnid = window.makeid(10);
    this._infoWindow = new google.maps.InfoWindow({
        content: '<input id="'+this._inputid+'" type="text" class="input-sm" /> ' +
        '<input id="'+this._btnid+'" type="button" value="Create" class="btn btn-default btn-sm"/>'
    });
    this._onCreateLatlngHandler = null;
}
MarkerGenerator.prototype.setOnCreateLatlngHandler = function(func) {
    this._onCreateLatlngHandler = func;
}
MarkerGenerator.prototype._createMarker = function(type) {

    var icon = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 5,
        strokeColor: 'red',
        strokeWeight: 2,
        fillColor:  'blue',
        fillOpacity: 1
    };

    var marker = new google.maps.Marker({
        draggable: true,
        icon: icon,
        data: {}
    });

    return marker;
}
MarkerGenerator.prototype.exec = function() {
    var count = parseInt(document.getElementById(this._inputid).value);
    var that = this;
    this.splitLatLng(this._marker1.getPosition(), this._marker2.getPosition(), count, function(latlng, index) {
        console.log('handler', that._onCreateLatlngHandler);
        if(that._onCreateLatlngHandler!=null) {
            that._onCreateLatlngHandler.call(that, latlng, index);
        }
    });
    this.hide();
}
MarkerGenerator.prototype.show = function() {

    this._visible = true;

    var bounds = this._googleMap.map.getBounds();
    var northEast = bounds.getNorthEast();
    var southWest = bounds.getSouthWest();
    var lat = ((northEast.lat()-southWest.lat())/2)+southWest.lat();
    var leftLng = northEast.lng()+((southWest.lng()-northEast.lng())/4);
    var rightLng = southWest.lng()-((southWest.lng()-northEast.lng())/4);
    var marker1Position = new google.maps.LatLng(lat, leftLng);
    var marker2Position = new google.maps.LatLng(lat, rightLng);
    this._marker1.setPosition(marker1Position);
    this._marker2.setPosition(marker2Position);
    this._marker1.setMap(this._googleMap.map);
    this._marker2.setMap(this._googleMap.map);
    this._infoWindow.open(this._googleMap.map, this._marker1);
    var that = this;
    document.getElementById(this._btnid).addEventListener('click', function() {
        that.exec();
    });

    var lineData = {startMarker:this._marker1, endMarker:this._marker2};
    this._line1 = new google.maps.Polyline({
        path: [this._marker1.getPosition(), this._marker2.getPosition()],
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        editable: false,
        draggable: false,
        data: lineData,
        map: this._googleMap.map
    });

    this._marker1Event = this._marker1.addListener('drag', function(evt) {
        that._line1.getPath().removeAt(0);
        that._line1.getPath().insertAt(0, evt.latLng);
    });
    this._marker2Event = this._marker2.addListener('drag', function(evt) {
        that._line1.getPath().removeAt(1);
        that._line1.getPath().insertAt(1, evt.latLng);
    });

}
MarkerGenerator.prototype.hide = function() {

    this._visible = false;

    this._marker1.setMap(null);
    this._marker2.setMap(null);
    this._infoWindow.close();
    this._line1.setMap(null);

    google.maps.event.removeListener(this._marker1Event);
    google.maps.event.removeListener(this._marker2Event);
}
MarkerGenerator.prototype.splitLatLng = function(startLatLng, endLatLng, count, func) {
    var lat1 = startLatLng.lat();
    var lng1 = startLatLng.lng();
    var lat2 = endLatLng.lat();
    var lng2 = endLatLng.lng();
    var p1 = lat1>lat2?lat1-lat2:lat2-lat1;
    var p2 = lng1>lng2?lng1-lng2:lng2-lng1;
    var c1 = p1/(count-1);
    var c2 = p2/(count-1);

    func.call(this, startLatLng, 0, startLatLng, endLatLng);
    for(var i=1;i<=count-2;i++) {
        var nlat =  lat1>lat2?lat1-(c1*i):lat1+(c1*i);
        var nlng =  lng1>lng2?lng1-(c2*i):lng1+(c2*i);
        var latlng = new google.maps.LatLng(nlat, nlng);
        func.call(this, latlng, i, startLatLng, endLatLng);
    }
    func.call(this, endLatLng, count-1, startLatLng, endLatLng);
}
