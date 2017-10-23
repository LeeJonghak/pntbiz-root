/**
 * 구글 지도
 *
 * @param elementId 구글지도를 표시할 Element ID
 * @param initFunc  초기화
 * @param mapOption 지도 옵션
 * @constructor
 */
var GoogleMap = function(elementId, initFunc, mapOption) {
    GoogleMap.instances.push(this);
    this._key = GoogleMap._key;
    this._element = document.getElementById(elementId);
    this._map = null;
    this._callbackFuncName = 'GoogleMap.readyScript';
    this._initFunc = initFunc;
    this._objects = {
        marker: {},
        circle: {},
        rectangle: {},
        polygon: {},
        line: {},
        image: {},
        group: {},
        focus: {},
        infoWindow: {}
    };
    this._mapOption = mapOption;
    this._googleMapPolygonDrawingManager = null;
    this._pairLineManager = new MapPairLine(this);
    this._markerCluster = null;
    this._markerClusterEnabled = false;

    if(GoogleMap.instances.length<=1) {
        this.loadScript();
    }
    if(GoogleMap.complate_load_script==true) {
        this.initialize();
    }
}
GoogleMap.instances = [];
/**
 * 구글 지도 API KEY
 */
GoogleMap._key = 'AIzaSyC5flBQIVK7pcBA6IDA3MZHvIcAWPDMqPQ';
/**
 * 지도 Center 기본값
 */
GoogleMap.defaultLat = 37.5651;
GoogleMap.defaultLng = 126.98955;
GoogleMap.setKey = function(key) {
    GoogleMap._key = key;
}
GoogleMap.prototype.getPairLineManager = function() {
    return this._pairLineManager;
}
GoogleMap.setDefaultCenter = function(lat, lng) {
    GoogleMap.defaultLat = lat;
    GoogleMap.defaultLng = lng;
}
/**
 * 구글지도 JS 파일이 로딩 여부
 */
GoogleMap.complate_load_script = false;
/**
 * OverlayView 를 확장하여 ImageOverlay 생성
 */
GoogleMap.ImageOverlay = function(bounds, image, map, degrees) {
    this.bounds_ = bounds;
    this.image_ = image;
    this.map_ = map;
    this.div_ = null;
    this.deg = degrees;
    this.setMap(map);
    this._editable = false;
    this._transform = null;
    this._border = '0px';
    this._opacity = '1';
}
/**
 * Circle, Polygon, Marker 등을 확장
 */
GoogleMap.extendsGoogleMapObject = function(object, type) {
    object.prototype._googleMap = null;
    object.prototype._type = type;
    object.prototype._id = null;
    object.prototype._groupid = null;
    object.prototype._focused = false;
    object.prototype._focusEffect = false;
    object.prototype._mouseOverEffect = false;
    object.prototype._onCreateProcHandler = null;
    object.prototype._onUpdateProcHandler = null;
    object.prototype._onDeleteProcHandler = null;
    object.prototype._onFocusHandler = null;
    object.prototype._onBlurHandler = null;
    object.prototype.__eventHandler = {};
    object.prototype.__loading = false;
    object.prototype._colorset = {
        defaultColor: {fill:'blue', stroke:'#ffffff'},
        mouseover: {fill:'#333333', stroke:'#ffffff'},
        focus: {fill:'blue', stroke:'red'},
        loading: {fill:'gray', stroke:'gray'}
    }
    object.prototype.setOnCreateProcHandler = function(func) {
        this._onCreateProcHandler = func;
    }
    object.prototype.setOnUpdateProcHandler = function(func) {
        this._onUpdateProcHandler = func;
    }
    object.prototype.setOnDeleteProcHandler = function(func) {
        this._onDeleteProcHandler = func;
    }
    object.prototype.setOnFocusHandler = function(func) {
        this._onFocusHandler = func;
    }
    object.prototype.setOnBlurHandler = function(func) {
        this._onBlurHandler = func;
    }
    object.prototype.onCreate = function(flagProcHandler, onCreateProcHandler) {
        if(typeof onCreateProcHandler!='undefined') this.setOnCreateProcHandler(onCreateProcHandler);
        this.setMap(this.getGoogleMap().getMap());
        if(this instanceof google.maps.Marker && this.getGoogleMap().isMarkerClusterEnabled()==true) {
            this.getGoogleMap().getMarkerCluster().addMarker(this);
        }
        this.setColor(this._colorset.loading.fill, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.loading.stroke);
        }
        this.__loading = true;
        if(this._onCreateProcHandler!=null && flagProcHandler==true) {
            this._onCreateProcHandler(this);
        } else {
            this.onCreateComplete(true);
        }
    }
    object.prototype.onCreateComplete = function(successful) {
        this.setColor(this._originalFillColor, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.defaultColor.stroke);
        }
        this.__loading = false;
        if(successful==true) {
            this.show();
        } else {
            this.remove();
        }
    }
    object.prototype.onUpdate = function(flagProcHandler) {
        this.setColor(this._colorset.loading.fill, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.loading.stroke);
        }
        this.__loading = true;
        if(this._onUpdateProcHandler!=null && flagProcHandler==true) {
            this._onUpdateProcHandler(this);
        } else {
            this.onUpdateComplete(true);
        }
    }
    object.prototype.onUpdateComplete = function(successful) {
        this.setColor(this._originalFillColor, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.defaultColor.stroke);
        }
        this.__loading = false;
        if(successful==true) {
            console.log('update complete success');
        }
    }
    object.prototype.onDelete = function(flagProcHandler) {
        this.setColor(this._colorset.loading.fill, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.loading.stroke);
        }
        this.__loading = true;
        if(this._onDeleteProcHandler!=null && flagProcHandler==true) {
            this._onDeleteProcHandler(this);
        } else {
            this.onDeleteComplete(true);
        }
    }
    object.prototype.onDeleteComplete = function(successful) {
        this.setColor(this._originalFillColor, null);
        if(this._focused!=true) {
            this.setColor(null, this._colorset.defaultColor.stroke);
        }
        this.__loading = false;
        if(successful==true) {
            this._googleMap.clearShapeObject(this._type, this.getGroupId(), this.getId());
        }
    }
    object.prototype.setGoogleMap = function(googleMap) {
        this._googleMap = googleMap;
    }
    object.prototype.getGoogleMap = function() {
        return this._googleMap;
    }
    object.prototype.getId = function() {
        return this._id;
    }
    object.prototype.setId = function(id) {
        this._id = id;
    }
    object.prototype.getGroupId = function() {
        return this._groupid;
    }
    object.prototype.setGroupId = function(id) {
        this._groupid = id;
    }
    object.prototype.addListener = function(eventType, callback) {
        google.maps.event.addListener(this, eventType, callback);
    };
    object.prototype.changeDraggable = function(draggable) {
        this.setDraggable(draggable);
        if(draggable==true) {
            if(typeof this.__eventHandler.mouseDragEnd=='undefined') {
                this.__eventHandler.mouseDragEnd = this.addListener('dragend', function() {
                    this.onUpdate(true);
                });
            }
        } else {
            google.maps.event.removeListener(this.__eventHandler.mouseDragEnd);
            delete this.__eventHandler.mouseDragEnd;
        }
    }
    object.prototype.setRemoved = function(removed) {
        this._removed = removed;
    }
    object.prototype.setMouseOverEffect = function(option) {
        this._mouseOverEffect = option;
        if(option==true) {
            if(typeof this.__eventHandler.mouseOverEffectOver=='undefined') {
                this.__eventHandler.mouseOverEffectOver = this.addListener('mouseover', function() {

                    if(this.__loading!=true) {
                        this.setColor(this._colorset.mouseover.fill, null);
                        if(this._focused!=true) {
                            this.setColor(null, this._colorset.mouseover.stroke);
                        }
                    }
                });
            }
            if(typeof this.__eventHandler.mouseOverEffectOut=='undefined') {
                this.__eventHandler.mouseOverEffectOut = this.addListener('mouseout', function () {
                    if (this.__loading != true) {
                        this.setColor(this._originalFillColor, null);
                        if(this._focused!=true) {
                            this.setColor(null, this._colorset.defaultColor.stroke);
                        }
                    }
                });
            }
        } else {
            google.maps.event.removeListener(this.__eventHandler.mouseOverEffectOver);
            google.maps.event.removeListener(this.__eventHandler.mouseOverEffectOut);
            delete this.__eventHandler.mouseOverEffectOver;
            delete this.__eventHandler.mouseOverEffectOut;
        }
    }
    object.prototype.setColorset = function(colorset) {
        if(typeof colorset.defaultColor!='undefined') {
            if(typeof colorset.defaultColor.fill!='undefined') {
                this._originalFillColor = colorset.defaultColor.fill;
                this._colorset.defaultColor.fill = colorset.defaultColor.fill;
            }
            if(typeof colorset.defaultColor.stroke!='undefined') this._colorset.defaultColor.stroke = colorset.defaultColor.stroke;
        }
        if(typeof colorset.mouseover!='undefined') {
            if (typeof colorset.mouseover.fill != 'undefined') this._colorset.mouseover.fill = colorset.mouseover.fill;
            if (typeof colorset.mouseover.stroke != 'undefined') this._colorset.mouseover.stroke = colorset.mouseover.stroke;
        }
        if(typeof colorset.focus!='undefined') {
            if (typeof colorset.focus.fill != 'undefined') this._colorset.focus.fill = colorset.focus.fill;
            if (typeof colorset.focus.stroke != 'undefined') this._colorset.focus.stroke = colorset.focus.stroke;
        }
        if(typeof colorset.loading!='undefined') {
            if (typeof colorset.loading.fill != 'undefined') this._colorset.loading.fill = colorset.loading.fill;
            if (typeof colorset.loading.stroke != 'undefined') this._colorset.loading.stroke = colorset.loading.stroke;
        }
    }
    object.prototype.setColor = function() {};
    object.prototype.setFocusEffect = function(enabled) {
        this._focusEffect = enabled;
    }
    object.prototype.setFocusGroup = function(groupid) {
        this._focusGroupid = groupid;
    }
    object.prototype.show = function() {
        /*this.setMap(this._googleMap.getMap());
         this._googleMap._markerCluster.addMarker(this);*/
        if(typeof this.setVisible!='undefined') {
            this.setVisible(true);
        } else {
            this.setMap(this._googleMap.getMap())
        }

    }
    object.prototype.hide = function() {
        /*this.setMap(null);
         this._googleMap._markerCluster.removeMarker(this);*/
        if(typeof this.setVisible!='undefined') {
            this.setVisible(false);
        } else {
            this.setMap(null);
        }
    }
    object.prototype.remove = function(flagProc) {
        if(this._removed==true) {
            if(flagProc==true) {
                this.onDelete(true);
            } else {
                this.onDeleteComplete(true);
            }
        } else {
            if(flagProc!=true) {
                this.onDeleteComplete(true);
            }
        }
    }
    object.prototype.getFocused = function() {
        return this._focused;
    }
    object.prototype.focus = function() {
        if(this._focused!=true) {
            this.setColor(null, this._colorset.focus.stroke);
            this._googleMap._objects.focus[this.getId()] = this;
            this._focused = true;
            /*if(this._onFocusHandler!=null) {

            }*/
        }
    }
    object.prototype.blur = function() {
        if(this._focused==true) {
            this.setColor(null, this._colorset.defaultColor.stroke);
            delete this._googleMap._objects.focus[this.getId()];
            this._focused = false;
            if (this._onBlurHandler != null) {
                console.log('exec on blur handler');
            }
        }
    }

    /**
     * 마크일경우 라인 이동 메소드 추가(Polyline)
     *     마크의 dragend 이벤트가 발생하면 연결된 라인의 포인트가 자동 이동 되도록
     */
    if(object==google.maps.Marker) {
        object.prototype.setSymbolIcon = function(fillColor, strokeColor) {
            this._originalFillColor = fillColor || this._colorset.defaultColor.fill;
            this.setIcon({
                path: google.maps.SymbolPath.CIRCLE,
                scale: 5,
                strokeColor: strokeColor || this._colorset.defaultColor.stroke,
                strokeWeight: 2,
                fillColor:  fillColor || this._colorset.defaultColor.fill,
                fillOpacity: 1
            });
        }
        object.prototype.setColor = function(fillColor, strokeColor, originalChange) {
            if(typeof originalChange!='undefined' && originalChange==true) {
                this._originalFillColor = fillColor;
            }
            var icon = this.getIcon();
            if(typeof fillColor!='undefined' && fillColor!=null) icon.fillColor = fillColor;
            if(typeof strokeColor!='undefined' && strokeColor!=null) icon.strokeColor = strokeColor;
            this.setIcon(icon);
        }
        object.prototype.addLinePoint = function(polyline, index) {
            if(typeof this.linePoints=='undefined') {
                this.linePoints = [];
                this.addListener('dragend', function(event) {
                    this.renderLinePoint(event.latLng);
                });
                this.addListener('drag', function(event) {
                    this.renderLinePoint(event.latLng);
                });
            }
            this.linePoints.push({
                polyline: polyline,
                index: index
            });
        }
        object.prototype.renderLinePoint = function(latLng) {
            if(typeof this.linePoints!='undefined') {
                for(var i=0; i<this.linePoints.length; i++) {
                    var line = this.linePoints[i];
                    var polyline = line.polyline;
                    polyline.getPath().removeAt(line.index);
                    polyline.getPath().insertAt(line.index, latLng);
                }
            }
        }
        object.prototype.getLinePoints = function() {
            return this.linePoints;
        }
        object.prototype.eachLinePoint = function(func) {
            if(typeof this.linePoints!='undefined') {
                for(var i=0; i<this.linePoints.length; i++) {
                    var line = this.linePoints[i];
                    var polyline = line.polyline;
                    var pointPosition = line.index;
                    func(polyline, pointPosition);
                }
            }
        }
    }

    /**
     * 도형일경우 편집 기능 추가
     */
    if(object==google.maps.Circle) {

        object.prototype.changeEditable = function(editable) {
            this.setEditable(editable);
        }

    }  else if(object==google.maps.Polygon) {

        object.prototype.changeEditable = function(editable) {
            this.setEditable(editable);
        }

    }  else if(object==google.maps.Polyline) {

        object.prototype.changeEditable = function(editable) {
            this.setEditable(editable);
        }
        object.prototype.setColor = function(fillColor, strokeColor, originalChange) {
            if(typeof originalChange!='undefined' && originalChange==true) {
                this._originalFillColor = fillColor;
            }
            //if(typeof fillColor!='undefined' && fillColor!=null) this.setOptions({strokeColor:fillColor});
            if(typeof strokeColor!='undefined' && strokeColor!=null) this.setOptions({strokeColor:strokeColor});
        }

    }  else if(object==google.maps.Rectangle) {

        object.prototype.changeEditable = function(editable) {
            this.setEditable(editable);
        }

    }  else if(object==GoogleMap.ImageOverlay) {

        object.prototype.changeEditable = function(editable) {
            if(editable==true) {
                console.log('edit mode');
            } else {
                console.log('readonly mode');
            }

        }

    }

}
/**
 * Google Map 스크립트가 사용준비되면 호출 됨.
 */
GoogleMap.readyScript = function() {

    /**
     * Google Map 확장
     */
    GoogleMap.extendsGoogleMapObject(google.maps.Marker, 'marker');
    GoogleMap.extendsGoogleMapObject(google.maps.Circle, 'circle');
    GoogleMap.extendsGoogleMapObject(google.maps.Polygon, 'polygon');
    GoogleMap.extendsGoogleMapObject(google.maps.Polyline, 'line');
    GoogleMap.extendsGoogleMapObject(google.maps.Rectangle, 'rectangle');

    /**
     * OverlayView 를 확장하여 ImageOverlay 생성
     */
    GoogleMap.ImageOverlay.prototype = new google.maps.OverlayView();
    GoogleMap.ImageOverlay.prototype.onAdd = function() {
        var that = this;

        this.div = document.createElement('div');
        this.div.style.border = this._border;
        //this.div.style.borderWidth = '1px';
        this.div.style.position = 'absolute';
        this.img = document.createElement('img');
        this.img.src = this.image_;
        this.img.style.width = '100%';
        this.img.style.height = '100%';
        this.img.style.opacity = this._opacity;
        this.img.style.position = 'absolute';
        this.div.appendChild(this.img);
        this.div_ = this.div;
        this.getPanes().overlayLayer.appendChild(this.div);

        this._transform = new GoogleMapTransformBox(this._googleMap, this.bounds_, this.deg);
        this._transform.setOnChangeDegrees(function(degrees) {
            that.setDegrees(degrees);
        });
        this._transform.setVisible(this._editable);
        this.setEditable(this._editable);
    }
    GoogleMap.ImageOverlay.prototype.getTransform = function() {
        return this._transform;
    }
    GoogleMap.ImageOverlay.prototype.setEditable = function(editable) {
        if(editable==true) {
            this.setOpacity(0.5);
            this.setBorder('1px solid red');
        } else {
            this.setOpacity(1);
            this.setBorder('0px');
        }
    }
    GoogleMap.ImageOverlay.prototype.setBorder = function(border) {
        this._border = border;
        if(this.img) {
            this.img.style.border = border;
        }
    }
    GoogleMap.ImageOverlay.prototype.setOpacity = function(opacity) {
        this._opacity = opacity;
        if(this.img) {
            this.img.style.opacity = opacity;
        }
    }
    GoogleMap.ImageOverlay.prototype.draw = function() {
        var overlayProjection = this.getProjection();
        var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
        var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());
        this.div = this.div_;
        this.div.style.left = sw.x + 'px';
        this.div.style.top = ne.y + 'px';
        this.div.style.width = (ne.x - sw.x) + 'px';
        this.div.style.height = (sw.y - ne.y) + 'px';
        this.div.style.webkitTransform = 'rotate('+this.deg+'deg)';
        this.div.style.mozTransform    = 'rotate('+this.deg+'deg)';
        this.div.style.msTransform     = 'rotate('+this.deg+'deg)';
        this.div.style.oTransform      = 'rotate('+this.deg+'deg)';
        this.div.style.transform       = 'rotate('+this.deg+'deg)';
    }
    GoogleMap.ImageOverlay.prototype.setSouthWestLatLng = function(southWestLatLng) {
        var northEastLatLng = this.bounds_.getNorthEast();
        var bounds = new google.maps.LatLngBounds(southWestLatLng, northEastLatLng);
        this.updateBounds(bounds);
    }
    GoogleMap.ImageOverlay.prototype.setNorthEastLatLng = function(northEastLatLng) {
        var southWestLatLng = this.bounds_.getSouthWest();
        var bounds = new google.maps.LatLngBounds(southWestLatLng, northEastLatLng);
        this.updateBounds(bounds);
    }
    GoogleMap.ImageOverlay.prototype.setDegrees = function(degrees) {
        this.deg = degrees;
        this.draw();
    }
    GoogleMap.ImageOverlay.prototype.updateBounds = function(bounds) {
        this.bounds_ = bounds;
        this.draw();
    }
    GoogleMap.ImageOverlay.prototype.onRemove = function() {
        this.div_.parentNode.removeChild(this.div_);
        this.div_ = null;
    }

    GoogleMap.extendsGoogleMapObject(GoogleMap.ImageOverlay, 'image');


    /**
     * 각 구글지도 인스턴스들을 초기화
     */
    for(var i=0; i<GoogleMap.instances.length; i++) {
        GoogleMap.instances[i].initialize();
    }
    GoogleMap.complate_load_script = true;


}
/**
 * 구글맵 인스턴트를 초기화
 */
GoogleMap.prototype.initialize = function() {

    if(this._mapOption!=null) this._map = new google.maps.Map(this._element, this._mapOption);
    else this._map = new google.maps.Map(this._element);

    var center = new google.maps.LatLng(GoogleMap.defaultLat, GoogleMap.defaultLng);
    this._map.setCenter(center);
    this._map.setZoom(14);
    this._map.setOptions({maxZoom:21});


    /**
     * 주요 객체 생성
     */
    this._googleMapPolygonDrawingManager = new GoogleMapPolygonDrawingManager(this);
    this._markerGenerator = new MarkerGenerator(this);

    if(typeof(this._initFunc)!='undefined') {
        this._initFunc();
    }

    /*this._markerCluster = new MarkerClusterer(this._map, [],  {gridSize: 50, maxZoom: 19});
    if(this._markerClusterEnabled==true) {
        this.enableMarkerCluster(true);
    }*/


}
/**
 * 구글지도JS 로드
 */
GoogleMap.prototype.loadScript = function() {
    if(typeof location.protocol=='undefined') location.protocol = 'http:';
    var script_url = location.protocol+'//maps.googleapis.com/maps/api/js?v=3.exp&key='+this._key
    // jhlee 20160324 sensor option 제거
    //    +'&sensor=SET_TO_TRUE_OR_FALSE&callback='+this._callbackFuncName
    	+'&callback='+this._callbackFuncName
        +'&libraries=visualization,geometry,places';
    var head= document.getElementsByTagName('head')[0];

    /*var script2= document.createElement('script');
    script2.type = 'text/javascript';
    script2.src = '/js/_global/google.map.markerclusterer.js';
    head.appendChild(script2);
	 */

    var script= document.createElement('script');
    script.type = 'text/javascript';
    script.src = script_url;
    head.appendChild(script);


}
GoogleMap.prototype.getMap = function() {
    return this._map;
}
GoogleMap.prototype.getPolygonDrawingManager = function() {
    return this._googleMapPolygonDrawingManager;
}
GoogleMap.prototype.addListener = function(eventType, func) {
    return google.maps.event.addListener(this._map, eventType, func);
}
/**
 * InputBox 를 구글 검색바로 등록
 *     mapControlsPosition 인자를 전달하지 않을 경우에는 지도안에 표시하지 않음
 * @param elementId InputBox ID
 * @param mapControlsPosition 지도에 표시할 위치.
 *                            ex: bindSearchBox('pac-input', this._map.controls[google.maps.ControlPosition.TOP_RIGHT]);
 */
GoogleMap.prototype.bindSearchBox = function(elementId, mapControlsPosition) {
    var _this = this;
    var element = document.getElementById(elementId);
    if(mapControlsPosition!=null) {
        mapControlsPosition.push(element);
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
        _this._map.fitBounds(bounds);
    });
}
GoogleMap.prototype.createMarker = function(groupid, id, latLng, option, data, onCreateProcHandler) {

    if(id==null) id = this.makeId();
    if(typeof this._objects.marker[id]=='undefined') {

        var icon = null;
        if(typeof(option)!='undefined' && typeof(option.icon)!='undefined') {
            icon = {
                url: option.icon, anchor: new google.maps.Point(5, 5), size: new google.maps.Size(10, 10)
            }
        }

        var marker = new google.maps.Marker({
            position: latLng,
            title: typeof(option)!='undefined' && typeof(option.title)!='undefined'?option.title:null,
            draggable: typeof(option)!='undefined' && option.draggable==true?true:false,
            icon: icon,
            data: data
        });
        if(typeof(option)=='undefined' || typeof(option.icon)=='undefined') {
            marker.setSymbolIcon();
        }
        marker.setId(id);
        marker.setGroupId(groupid);
        marker.setGoogleMap(this);
        if(typeof onCreateProcHandler!='undefined') marker.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.marker[id] = marker;
        if(typeof this._objects.group[groupid]=='undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        marker.onCreate(typeof onCreateProcHandler=='undefined'?false:true);

        return marker;
    } else {
        console.log('Error duplicate marker ID:'+id);
        return false;
    }
}
GoogleMap.prototype.createCircle = function(groupid, id, latLng, radius, option, data, onCreateProcHandler) {
    if(id==null) id = this.makeId();
    if(typeof this._objects.circle[id]=='undefined') {
        var circle = new google.maps.Circle({
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            center: latLng,
            radius: radius,
            editable: false,
            draggable: false,
            data: data,
            map: this._map
        });

        circle.setId(id);
        circle.setGroupId(groupid);
        circle.setGoogleMap(this);
        circle.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.circle[id] = circle;
        if (typeof this._objects.group[groupid] == 'undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        circle.onCreate(typeof onCreateProcHandler == 'undefined' ? false : true);
        return circle;
    } else {
        console.log('Error duplicate circle ID:'+id);
        return false;
    }
}

GoogleMap.prototype.createRectangle = function(groupid, id, southWestLatLng, northEastLatLng, data, onCreateProcHandler) {
    if(id==null) id = this.makeId();
    if(typeof this._objects.rectangle[id]=='undefined') {
        var bounds = new google.maps.LatLngBounds(southWestLatLng, northEastLatLng);
        var rectangle = new google.maps.Rectangle({
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            bounds: bounds,
            editable: false,
            draggable: false,
            data: data,
            map: this._map
        });

        rectangle.setId(id);
        rectangle.setGroupId(groupid);
        rectangle.setGoogleMap(this);
        rectangle.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.rectangle[id] = rectangle;
        if (typeof this._objects.group[groupid] == 'undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        rectangle.onCreate(typeof onCreateProcHandler == 'undefined' ? false : true);
        return rectangle;
    } else {
        console.log('Error duplicate rectangle ID:'+id);
        return false;
    }
}

GoogleMap.prototype.createPoygon = function(groupid, id, path, data, onCreateProcHandler) {
    if(id==null) id = this.makeId();
    if(typeof this._objects.polygon[id]=='undefined') {
        var polygon = new google.maps.Polygon({
            path: path,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            editable: false,
            draggable: false,
            data: data,
            map: this._map
        });

        polygon.setId(id);
        polygon.setGroupId(groupid);
        polygon.setGoogleMap(this);
        polygon.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.polygon[id] = polygon;
        if(typeof this._objects.group[groupid]=='undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        polygon.onCreate(typeof onCreateProcHandler=='undefined'?false:true);
        return polygon;
    } else {
        console.log('Error duplicate polygon ID:'+id);
        return false;
    }
}
GoogleMap.prototype.createLine = function(groupid, id, formLatLng, toLatLng, option, data, onCreateProcHandler) {
    if(id==null) id = this.makeId();
    if(typeof this._objects.line[id]=='undefined') {
        var polyline = new google.maps.Polyline({
            path: [formLatLng, toLatLng],
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            editable: false,
            draggable: false,
            data: data,
            map: this._map
        });

        polyline.setId(id);
        polyline.setGroupId(groupid);
        polyline.setGoogleMap(this);
        polyline.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.line[id] = polyline;
        if (typeof this._objects.group[groupid] == 'undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        polyline.onCreate(typeof onCreateProcHandler == 'undefined' ? false : true);
        return polyline;
    } else {
        console.log('Error duplicate line ID:'+id);
        return false;
    }

}

GoogleMap.prototype.createImageOverlay = function(groupid, id, southWestLatLng, northEastLatLng , degrees, imageUrl, onCreateProcHandler) {
    if(id==null) id = this.makeId();
    if(typeof this._objects.image[id]=='undefined') {
        var bounds = new google.maps.LatLngBounds(southWestLatLng, northEastLatLng);
        var imageOverlay = new GoogleMap.ImageOverlay(bounds, imageUrl, this._map, degrees);
        imageOverlay.setId(id);
        imageOverlay.setGroupId(groupid);
        imageOverlay.setGoogleMap(this);
        imageOverlay.setOnCreateProcHandler(onCreateProcHandler);
        this._objects.image[id] = imageOverlay;
        if (typeof this._objects.group[groupid] == 'undefined') this._objects.group[groupid] = [];
        this._objects.group[groupid].push(id);

        imageOverlay.onCreate(typeof onCreateProcHandler == 'undefined' ? false : true);
        return imageOverlay;
    } else {
        console.log('Error duplicate imageOverlay ID:'+id);
        return false;
    }
}
GoogleMap.prototype.getShapeObjects = function(type, groupid) {
    var result = [];
    if(typeof this._objects.group[groupid]!='undefined') {
        for(var i=0; i<this._objects.group[groupid].length; i++) {
            var id = this._objects.group[groupid][i];
            result.push(this._objects[type][id]);
        }
    }
    return result;
}
GoogleMap.prototype.eachShapeObject = function(type, groupid, func) {

    if(typeof this._objects.group[groupid]!='undefined') {
        for(var i=0; i<this._objects.group[groupid].length; i++) {
            var id = this._objects.group[groupid][i];
            func(this._objects[type][id]);
        }
    }
}
GoogleMap.prototype.getShapeObject = function(type, groupid, id) {

    var object = this._objects[type][id];
    if(typeof object!='undefined' && object.getGroupId()==groupid) {
        return object;
    } else {
        return false;
    }
}
GoogleMap.prototype.clearShapeObject = function(type, groupid, id) {

    if(typeof type!='undefined' && typeof groupid!='undefined' && typeof id!='undefined') {

        if(typeof this._objects[type][id]=='undefined') console.log('clearShapeObject Error, not found object:'+type+','+id)
        else {
            var object = this.getShapeObject(type, groupid, id);
            if(object) {
                object.setMap(null);
                if(type=='marker' && this._markerClusterEnabled==true) {
                    this._markerCluster.removeMarker(object);
                }
                delete this._objects[type][id];
                for(var i=0; this._objects.group[groupid].length; i++) {
                    if(this._objects.group[groupid][i]==id) {
                        this._objects.group[groupid].splice(i, 1);
                        break;
                    }
                }
            }
        }

    } else if(typeof type!='undefined' && typeof groupid!='undefined') {

        if(typeof this._objects.group[groupid]!='undefined') {
            for(var i=0; i<this._objects.group[groupid].length; i++) {
                var id = this._objects.group[groupid][i];
                var object = this.getShapeObject(type, groupid, id);
                if(object) {
                    object.setMap(null);
                    if(type=='marker' && this._markerClusterEnabled==true) {
                        this._markerCluster.removeMarker(object);
                    }
                    delete this._objects[type][id];
                }
            }
            delete this._objects.group[groupid];
        }
    }

}
GoogleMap.prototype.getFocusObjects = function() {
    return this._objects.focus;
}
GoogleMap.prototype.getLastFocusObject = function() {
    var focusObjects = this.getFocusObjects();
    var focusIds = Object.keys(focusObjects);
    return focusObjects[focusIds[focusIds.length-1]];
}
GoogleMap.prototype.eachFocusObjects = function(func) {
    for(var key in this._objects.focus) {
        var obj = this._objects.focus[key];
        func(obj);
    }
}
GoogleMap.prototype.blurAllFocusObject = function() {
    this.eachFocusObjects(function(marker) {
        marker.blur();
    });
}
GoogleMap.prototype.getMarkerCluster = function() {
    return this._markerCluster;
}
GoogleMap.prototype.getMarkerGenerator = function() {
    return this._markerGenerator;
}
GoogleMap.prototype.enableMarkerCluster = function(enabled) {
    if(typeof this._markerCluster!='undefined' && this._markerCluster!=null) {
        this._markerCluster.clearMarkers();
        this._markerClusterEnabled = enabled;

        for(var key in this._objects.marker) {
            var marker = this._objects.marker[key];
            if(marker.visible==true) {
                if(enabled==true) {
                    this._markerCluster.addMarker(marker);
                } else {
                    marker.setMap(this._map);
                }
            }
        }
    } else {
        this._markerClusterEnabled = enabled;
    }
}
GoogleMap.prototype.isMarkerClusterEnabled = function() {
    return this._markerClusterEnabled;
}
GoogleMap.prototype.splitLatLng = function(startLatLng, endLatLng, count, func) {
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
GoogleMap.prototype.makeId = function(length) {
    if(typeof length=='undefined') length = 10;
    var text = "";
    var possible = "abcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < length; i++ ) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
}
GoogleMap.prototype.createInfoWindow = function(groupid, id, content, data) {
    var infoWindow = new google.maps.InfoWindow({
        _id: id,
        _groupid: groupid,
        content: content,
        data: data
    });
    infoWindow.getId = function() {
        return this._id;
    }
    infoWindow.getGroupId = function() {
        return this._groupid;
    }

    this._objects.infoWindow[id] = infoWindow;
    if(typeof this._objects.group[groupid]=='undefined') this._objects.group[groupid] = [];
    this._objects.group[groupid].push(id);

    return infoWindow;
}
GoogleMap.prototype.getInfoWindow = function(groupid, id) {
    if(typeof id!='undefined') {
        return this._objects.infoWindow[id];
    } else {
        var windows = [];
        for(var i=0; i<this._objects.infoWindow[groupid].length; i++) {
            var wid = this._objects.infoWindow[groupid][i];
            var infoWindow = this._objects.infoWindow[wid];
            if(infoWindow) {
                windows.push(infoWindow);
            }
        }
        return windows;
    }
}


/**
 * 다각형 그리기 도구
 */
var GoogleMapPolygonDrawingManager = function(googleMap) {
    this._googleMap = googleMap;
    this._drawing = false;
    this._clickEventHandler = null;
    this._onCompleteHandler = null;
    this._polyline = null;
    this._polygon = null;
    this._completed = false;
}
GoogleMapPolygonDrawingManager.prototype.setVisible = function(visible) {
    if(visible==true) {
        if(this._polyline!=null) {
            this._polyline.setVisible(true);
        }
        if(this._polygon!=null) {
            this._polygon.setVisible(true);
        }
    } else {
        if(this._polyline!=null) {
            this._polyline.setVisible(false);
        }
        if(this._polygon!=null) {
            this._polygon.setVisible(false);
        }
    }
}
GoogleMapPolygonDrawingManager.prototype.getPolygon = function() {
    return this._polygon;
}
GoogleMapPolygonDrawingManager.prototype.isDrawing = function() {
    return this._drawing;
}
GoogleMapPolygonDrawingManager.prototype.isComplete = function() {
    return this._completed;
}
GoogleMapPolygonDrawingManager.prototype.clear = function() {
    if(this._polyline!=null) {
        this._polyline.setMap(null);
        this._polyline = null;
    }
    if(this._polygon!=null) {
        this._polygon.setMap(null);
        this._polygon = null;
    }
    this._completed = false;
    this.stopDrawing();
}
GoogleMapPolygonDrawingManager.prototype.startDrawing = function(path) {
    this._drawing = true;

    var that = this;

    if(typeof path != 'undefined') {
        if(that._polyline==null) {
            that._polyline = new google.maps.Polyline({
                path: path,
                geodesic: true,
                strokeColor: '#FF0000',
                strokeOpacity: 1.0,
                strokeWeight: 2,
                editable: true,
                map: that._googleMap.getMap()
            });
            google.maps.event.addListener(that._polyline, 'click', function (mev) {
                if (mev.vertex == 0) {
                    that.onComplete();
                }
            });
            google.maps.event.addListener(that._polyline, 'rightclick', function (mev) {
                if (mev.vertex != null && this.getPath().getLength() > 3) {
                    this.getPath().removeAt(mev.vertex);
                }
            });
        } else {
            that._polyline.setPath(path);
        }

    }

    this._clickEventHandler = google.maps.event.addListener(this._googleMap.getMap(), 'click', function(event) {
        if(that._polyline==null) {
            that._polyline = new google.maps.Polyline({
                path: [event.latLng],
                geodesic: true,
                strokeColor: '#FF0000',
                strokeOpacity: 1.0,
                strokeWeight: 2,
                editable: true,
                map:that._googleMap.getMap()
            });

            google.maps.event.addListener(that._polyline, 'click', function(mev){
                if(mev.vertex==0) {
                    that.onComplete();
                }
            });
            google.maps.event.addListener(that._polyline, 'rightclick', function(mev){
                if (mev.vertex != null && this.getPath().getLength() > 3) {
                    this.getPath().removeAt(mev.vertex);
                }
            });
        }  else {
            that._polyline.getPath().push(event.latLng);

        }

    });
}
GoogleMapPolygonDrawingManager.prototype.stopDrawing = function() {
    this._drawing = false;
    google.maps.event.removeListener(this._clickEventHandler);
}
GoogleMapPolygonDrawingManager.prototype.onComplete = function() {
    this._drawing = false;
    this._completed = true;
    google.maps.event.removeListener(this._clickEventHandler);

    this._polygon = new google.maps.Polygon({
        path: this._polyline.getPath(),
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        editable: true,
        draggable: true,
        map: this._googleMap.getMap()
    });
    google.maps.event.addListener(this._polygon, 'rightclick', function(mev){
        if (mev.vertex != null && this.getPath().getLength() > 3) {
            this.getPath().removeAt(mev.vertex);
        }
    });
    this._polyline.setMap(null);
    this._polyline = null;

    if(this._onCompleteHandler!=null) {
        this._onCompleteHandler();
    }
}
GoogleMapPolygonDrawingManager.prototype.setOnCompleteHandler = function(func) {
    this._onCompleteHandler = func;
}


/**
 * 사각형 모양 변경(사이즈, 회전)을 위한 클래스
 * @param googleMap
 * @param bounds
 * @param degrees
 * @constructor
 */
var GoogleMapTransformBox = function(googleMap, bounds, degrees) {
    this._googleMap = googleMap;
    this._bounds = bounds;
    this._degrees = degrees;
    this._onChangeDegreesFunc = null;
    this._onChangeBoundsFunc = null;


    var that = this;

    /**
     * 서남, 북동 좌표
     */
    var sw = this._bounds.getSouthWest();
    var ne = this._bounds.getNorthEast();


    /**
     * 사각형 중심점
     */
    var c1 = this.getCenter();

    /**
     * 각도 조절 핸들을 표시할 위치
     */
    var dist = sw.lat()>ne.lat()?sw.lat()-ne.lat():ne.lat()-sw.lat();
    var dg = (0-this._degrees)*Math.PI/180;
    var x = c1.lat() + (dist) * Math.cos(dg);
    var y = c1.lng() - (dist) * Math.sin(dg);
    var c2 = new google.maps.LatLng(x, y);

    var lineSymbol = {
        path: 'M 0,-1 0,1',
        strokeOpacity: 0.6,
        scale: 1.4
    };

    this.guideline = new google.maps.Polyline({
        path: [c1, c2],
        geodesic: true,
        icons: [{
            icon: lineSymbol,
            offset: '0',
            repeat: '7px'
        }],
        strokeColor: 'red',
        strokeOpacity: 0.0,
        strokeWeight: 0,
        editable: false,
        draggable: false,
        suppressUndo: true,
        map: this._googleMap._map
    });

    this.degreesHandle = new google.maps.Marker({
        position: c2,
        icon: {
            path:google.maps.SymbolPath.CIRCLE,
            scale:5,
            strokeColor: 'red',
            strokeWeight: 1.5,
            strokeOpacity: 1,
            fillColor: '#ffffff',
            fillOpacity: 1
        },
        draggable: true,
        guideline: this.guideline,
        map: this._googleMap._map
    });

    google.maps.event.addListener(this.degreesHandle, 'mouseover', function() {
        that.degreesHandle.setIcon({
            path:google.maps.SymbolPath.CIRCLE,
            scale:5,
            strokeColor: 'red',
            strokeWeight: 1.5,
            strokeOpacity: 1,
            fillColor: 'red',
            fillOpacity: 0.5
        });
    });
    google.maps.event.addListener(this.degreesHandle, 'mouseout', function() {
        that.degreesHandle.setIcon({
            path:google.maps.SymbolPath.CIRCLE,
            scale:5,
            strokeColor: 'red',
            strokeWeight: 1.5,
            strokeOpacity: 1,
            fillColor: '#ffffff',
            fillOpacity: 1
        });
    });
    google.maps.event.addListener(this.degreesHandle, 'drag', function(event) {
        var x1 = c1.lat();
        var y1 = c1.lng();

        var x2 = event.latLng.lat();
        var y2 = event.latLng.lng();

        var dx = x2 - x1;
        var dy = y2 - y1;
        var rad = Math.atan2(dx, dy);
        var degree = (rad*180)/Math.PI ;
        this.guideline.getPath().setAt(1, event.latLng);

        that._degrees = 90-degree;
        that.onChangeDegrees();
    })
}
GoogleMapTransformBox.prototype.setOnChangeDegrees = function(func) {
    this._onChangeDegreesFunc = func;
}
GoogleMapTransformBox.prototype.onChangeDegrees = function() {
    if(this._onChangeDegreesFunc!=null) {
        this._onChangeDegreesFunc(this._degrees);
    }
}
GoogleMapTransformBox.prototype.setOnChangeBounds = function(func) {
    this._onChangeBoundsFunc = func;
}
GoogleMapTransformBox.prototype.onChangeBounds = function() {
    if(this._onChangeBoundsFunc!=null) {
        this._onChangeBoundsFunc(this._bounds);
    }
}
GoogleMapTransformBox.prototype.getCenter = function() {
    var sw = this._bounds.getSouthWest();
    var ne = this._bounds.getNorthEast();

    var lat = ne.lat()+((sw.lat()-ne.lat())/2);
    var lng = sw.lng()+((ne.lng()-sw.lng())/2);

    return new google.maps.LatLng(lat, lng);
}
GoogleMapTransformBox.prototype.updateBounds = function(bounds) {

}
GoogleMapTransformBox.prototype.updateDegrees = function(degrees) {

}
GoogleMapTransformBox.prototype.getBounds = function() {
    return this._bounds;
}
GoogleMapTransformBox.prototype.getDegrees = function() {
    return this._degrees;
}
GoogleMapTransformBox.prototype.setVisible = function(visible) {
    this._visible = visible;
    if(visible==true) {
        this.guideline.setVisible(true);
        this.degreesHandle.setVisible(true);
    } else {
        this.guideline.setVisible(false);
        this.degreesHandle.setVisible(false);
    }
}



/**
 * Ajax 요청 관련
 * @param base_url
 * @constructor
 */
var ServiceRequest = function(base_url) {
    this.base_url = base_url;
    this.ajaxReady = true;
    this._loadingBarElement = null;
}
ServiceRequest.prototype.get = function(url, data, callback_func) {
    console.log('## ServiceRequest>get start');
    console.log('url:'+this.base_url+url);
    var that = this;
    that.ajaxReady = false;
    if(that._loadingBarElement!=null) that._loadingBarElement.style.display = 'block';
    $.ajax({
        type: "get",
        dataType: 'json',
        url: this.base_url + url,
        data: data,
        cache: false
    }).done(function (json) {
        console.log('## ServiceRequest>get done');
        console.log('url:'+that.base_url+url);

		if(typeof(json)=='string') {
			json = JSON.parse(json);
		}

        that.ajaxReady = true;
        callback_func(json);
        if(that._loadingBarElement!=null) that._loadingBarElement.style.display = 'none';
    });
}
ServiceRequest.prototype.post = function(url, data, callback_func) {
    console.log('## ServiceRequest>post');
    console.log('url:'+this.base_url+url)
    var that = this;
    that.ajaxReady = false;
    if(that._loadingBarElement!=null) that._loadingBarElement.style.display = 'block';
    $.ajax({
        type: "post",
        dataType: 'json',
        url: this.base_url+url,
        data: data
    }).done(function( json ) {
        console.log('## ServiceRequest>post done');
        console.log('url:'+that.base_url+url);

		if(typeof(json)=='string') {
			json = JSON.parse(json);
		}

        that.ajaxReady = true;
        callback_func(json);
        if(that._loadingBarElement!=null) that._loadingBarElement.style.display = 'none';
    });
}
ServiceRequest.prototype.bindLoadingBar = function(elementId) {
    this._loadingBarElement = document.getElementById(elementId);
    this._loadingBarElement.style.display = 'none';
}




var FloorService  = function(googleMap, serviceRequest) {
    this.serviceUrl = 'map/floor/list.do';
    this._googleMap = googleMap;
    this._serviceRequest = serviceRequest;
    this._floors = {};
    this._currentFloor = 1;
    this._selectBoxElement = null;
    this._onChangeFloorFunc = null;
    this._defaultFloor = null;
}
FloorService.prototype.getServiceRequest = function() {
    return this._serviceRequest;
}
FloorService.prototype.load = function(pfloor) {
    var that = this;
    this._serviceRequest.post(this.serviceUrl, {}, function(response) {
        if(response.result=='1') {
            var defaultFloor = null;
            if(that._selectBoxElement) that._selectBoxElement.innerHTML = '';

            // 2015-11-27 nohsoo 층 정렬
            response.data.sort(function(a, b) {
                return parseInt(b.floor)-parseInt(a.floor);
            });
            // end

            for(var i=0; i<response.data.length; i++) {
                var floorInfo = response.data[i];
                var intFloor = floorInfo.floor;
                if(defaultFloor==null) {
                    defaultFloor = intFloor;
                }
                that._floors[floorInfo.floorNum] = floorInfo;

                console.log(that._defaultFloor, floorInfo.floor);
                if(that._defaultFloor!=null && that._defaultFloor==floorInfo.floor) {
                    defaultFloor = that._defaultFloor;
                }
            }
            console.log(that._floors);

            var added = [];
            for(var i=0; i<response.data.length; i++) {
                var floorInfo = response.data[i];
                var option = new Option();
                option.value = floorInfo.floor;
                //option.text = floorInfo.floor+'F';
                option.text = floorInfo.floorName;
                if(typeof pfloor!='undefined') {
                    if(pfloor==floorInfo.floor) {
                        option.selected = true;
                    }
                } else {
                    if(defaultFloor==floorInfo.floor) {
                        option.selected = true;
                    }
                }

                if(added.indexOf(floorInfo.floor)==-1) {
                    added.push(floorInfo.floor);
                    that._selectBoxElement.options.add(option);
                }
            }

            if(typeof pfloor!='undefined') {
                console.log('pfloor', pfloor);
                that.changeFloor(pfloor);
            } else {
                console.log('defaultFloor', defaultFloor);
                that.changeFloor(defaultFloor);
            }
        }
        else if(response.result!='1') {
            window.alert(vm.infoError);
        }
    });
}
FloorService.prototype.bindFloorSelectBox = function(elementId) {
    var that = this;
    this._selectBoxElement = document.getElementById(elementId);
    this._selectBoxElement.addEventListener('change', function() {
        that.changeFloor(this.value);
    });

    if(this._selectBoxElement.hasAttribute('data-default-floor')) {
        var defaultFloor = this._selectBoxElement.getAttribute('data-default-floor');
        if(defaultFloor) {
            this._defaultFloor = defaultFloor;
        }
    }
}
FloorService.prototype.getSelectBoxElement = function() {
    return this._selectBoxElement;
}
FloorService.prototype.changeFloor = function(floorNum) {
    this._currentFloor = floorNum;

    var floors = this._googleMap.getShapeObjects('image','floor');
    for(var i=0; i<floors.length; i++) {
        if(floors[i]) floors[i].remove();
    }

    for(var key in this._floors) {
        if(this._floors[key].floor==floorNum) {
            var southWestLatLng = new google.maps.LatLng(this._floors[key].swLat, this._floors[key].swLng);
            var northEastLatLng = new google.maps.LatLng(this._floors[key].neLat, this._floors[key].neLng);
            this._googleMap.createImageOverlay('floor', 'floor-'+this._floors[key].floorNum, southWestLatLng, northEastLatLng, this._floors[key].deg, this._floors[key].imgURL);
            console.log(this._floors[key].imgURL);
        }
    }

    if(this._onChangeFloorFunc!=null) {
        this._onChangeFloorFunc(floorNum);
    }
}
FloorService.prototype.setOnChangeFloor = function(func) {
    this._onChangeFloorFunc = func;
}
FloorService.prototype.getCurrentFloor = function() {
    return this._currentFloor;
}





var MapUndoManager = function() {
    this._jobstack = [];
    this._datastack = [];
    this._onUndoFunc = null;
    this._onPushJobFunc = null;
    this._undoButtonElement = null;
}
MapUndoManager.prototype.pushJob = function(callback, data) {
    this._jobstack.push(callback);
    if(typeof data=='undefined') data = {};
    this._datastack.push(data);

    if(this._onPushJobFunc!=null) {
        this._onPushJobFunc();
    }

    if(this._jobstack.length>0) {
        this._undoButtonElement.disabled = false;
    } else {
        this._undoButtonElement.disabled = true;
    }
}
MapUndoManager.prototype.undo = function() {
    var job = this._jobstack.pop();
    var data = this._datastack.pop();

    if(typeof job!='undefined' && typeof data!='undefined') {
        job(data);
        if(this._onUndoFunc!=null) {
            this._onUndoFunc();
        }
    }

    if(this._jobstack.length>0) {
        this._undoButtonElement.disabled = false;
    } else {
        this._undoButtonElement.disabled = true;
    }
}
MapUndoManager.prototype.setOnPushJob = function(callback) {
    this._onPushJobFunc = callback;
}
MapUndoManager.prototype.setOnUndo = function(callback) {
    this._onUndoFunc = callback;
}
MapUndoManager.prototype.clear = function() {
    this._jobstack = [];
    this._datastack = [];
    if(this._undoButtonElement) this._undoButtonElement.disabled = true;
}
MapUndoManager.prototype.bindUndoButton = function(elementId) {
    this._undoButtonElement = document.getElementById(elementId);

    var that = this;
    this._undoButtonElement.addEventListener('click', function() {
        that.undo();
    });

    if(this._jobstack.length>0) {
        this._undoButtonElement.disabled = false;
    } else {
        this._undoButtonElement.disabled = true;
    }
}






var MapPairLine = function(googleMap) {
    this._googleMap = googleMap;
    this._groupid = 'pairline';
    this._pairs = {};
}
MapPairLine.prototype.eachPairLine = function(func) {
    for(var key in this._pairs) {
        var line = this._googleMap.getShapeObject('line', this._groupid, key);
        func(line);
    }
}
MapPairLine.prototype.getPairLine = function(startPointId, endPointId) {
    return this._pairs[startPointId+'-'+endPointId] || this._pairs[endPointId+'-'+startPointId] || false;
}
MapPairLine.prototype.connect = function(startMarker, endMarker, procFunc) {
    if(typeof startMarker.getId=='undefined' || typeof endMarker.getId=='undefined') {
        return false;
    }
    if(this.getPairLine(startMarker.getId(), endMarker.getId())==false) {
        var pairId = startMarker.getId()+'-'+endMarker.getId();
        var data = {startMarker:startMarker, endMarker:endMarker};
        var line = this._googleMap.createLine(this._groupid, pairId, startMarker.getPosition(), endMarker.getPosition(), {}, data, function() {
            this.setColorset({defaultColor:{fill:'red',stroke:'red'},mouseover:{fill:'blue',stroke:'blue'}});
            this.setMouseOverEffect(true);
            this.setRemoved(true);
            startMarker.addLinePoint(this, 0);
            endMarker.addLinePoint(this, 1);
            if(typeof procFunc!='undefined') {
                procFunc(this);
            } else {
                this.onCreateComplete(true);
            }
        });
        if(line) {
            this._pairs[pairId] = line;
        }
        return line;
    } else {
        return false;
    }
}
MapPairLine.prototype.remove = function(polyline) {
    polyline.remove(true);
    delete this._pairs[polyline.getId()];
}
MapPairLine.prototype.clear = function() {
    this._googleMap.clearShapeObject('line', this._groupid);
    this._pairs = {};
}




var MarkerGenerator = function(googleMap) {
    this._googleMap = googleMap;
    this._groupid = 'markergnrt';
    this._marker1 = this._createMarker('start');
    this._marker2 = this._createMarker('end');
    this._line1 = null;
    this._inputid = this._googleMap.makeId(10);
    this._btnid = this._googleMap.makeId(10);
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
    var marker = this._googleMap.createMarker(this._groupid, this._groupid+type, null);
    if(marker!=false) {
        marker.setMouseOverEffect(true);
        marker.setColorset({defaultColor:{fill:'green'}});
        marker.changeDraggable(true);
        marker.hide();
    }
    return marker;
}
MarkerGenerator.prototype.exec = function() {
    var count = parseInt(document.getElementById(this._inputid).value);
    var that = this;
    this._googleMap.splitLatLng(this._marker1.getPosition(), this._marker2.getPosition(), count, function(latlng, index) {
        if(this._onCreateLatlngHandler!=null) {
            this._onCreateLatlngHandler.call(that, latlng);
        }
    });
    this.hide();
}
MarkerGenerator.prototype.show = function() {
    var bounds = this._googleMap.getMap().getBounds();
    var northEast = bounds.getNorthEast();
    var southWest = bounds.getSouthWest();
    var lat = ((northEast.lat()-southWest.lat())/2)+southWest.lat();
    var leftLng = northEast.lng()+((southWest.lng()-northEast.lng())/4);
    var rightLng = southWest.lng()-((southWest.lng()-northEast.lng())/4);
    var marker1Position = new google.maps.LatLng(lat, leftLng);
    var marker2Position = new google.maps.LatLng(lat, rightLng);
    this._marker1.setPosition(marker1Position);
    this._marker2.setPosition(marker2Position);
    this._marker1.show();
    this._marker2.show();
    this._infoWindow.open(this._googleMap.getMap(), this._marker1);
    var that = this;
    document.getElementById(this._btnid).addEventListener('click', function() {
        that.exec();
    });

    var lineData = {startMarker:this._marker1, endMarker:this._marker2};
    this._line1 = this._googleMap.createLine(this._groupid, this._groupid+'line', this._marker1.getPosition(), this._marker2.getPosition(), {}, lineData, function() {
        this.setColorset({defaultColor:{fill:'red',stroke:'red'},mouseover:{fill:'blue',stroke:'blue'}});
        this.setMouseOverEffect(true);
        this.data.startMarker.addLinePoint(this, 0);
        this.data.endMarker.addLinePoint(this, 1);
    });

}
MarkerGenerator.prototype.hide = function() {
    this._marker1.hide();
    this._marker2.hide();
    this._infoWindow.close();
    this._line1.remove();
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
    mapPopup.html('<div class="center">loading ...</div>');
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
            mapPopup.onClose();
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
            mapPopup.onClose();
            mapPopup.hide();
        });
        if(typeof callback!='undefined') {
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
