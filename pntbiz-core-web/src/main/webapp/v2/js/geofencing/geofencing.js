

elementHandler.ready(function() {

    /**
     * 지오펜스 설정 지도 생성
     */
    /*if(document.getElementById('map-canvas')) {
        elementHandler.googleMap = new GoogleMap('map-canvas', function () {

            /!**
             * 속성 정의
             *!/
            var that = this;
            this.drawingMode = null;
            this.circle = null;
            this.polygon = null;

            /!**
             * 함수 정의
             *!/
            this.changeFloor = function (floor) {
                var floorInfo = that.floors[floor];
                var southWestLatLng = new google.maps.LatLng(floorInfo.swLat, floorInfo.swLng);
                var northEastLatLng = new google.maps.LatLng(floorInfo.neLat, floorInfo.neLng);
                var floor = that.getShapeObject('image', 'floor')
                if (floor) floor.remove();
                var floor = that.createImageOverlay('floor', southWestLatLng, northEastLatLng, floorInfo.deg, floorInfo.imgURL);

            }

            this.changeDrawingMode = function (mode) {
                var polygonDrawingManager = that.getPolygonDrawingManager();
                if (mode == 'P') {
                    /!**
                     * 동그라미 펜스 드로잉 모드
                     *!/
                    that.drawingMode = 'P';
                    if (!polygonDrawingManager.isComplete()) {
                        polygonDrawingManager.startDrawing();
                    }
                    polygonDrawingManager.setVisible(true);

                    if (that.circle != null) {
                        that.circle.setVisible(false);
                    }
                } else {
                    /!**
                     * 폴리곤 펜스 드로잉 모드
                     *!/
                    that.drawingMode = 'C';
                    polygonDrawingManager.stopDrawing();
                    polygonDrawingManager.setVisible(false);

                    if (that.circle != null) {
                        that.circle.setVisible(true);
                    }

                }
            }

            /!**
             * 초기화 스크립트
             *!/
            this.getMap().setZoom(20);
            this.addListener('click', function (event) {
                if (that.drawingMode == 'P') {
                    var polygonDrawingManager = that.getPolygonDrawingManager();
                    if (polygonDrawingManager.isComplete() == true && window.confirm(vm.redrawConfirm)) {
                        polygonDrawingManager.clear();
                        polygonDrawingManager.startDrawing();
                    }
                } else {
                    if (that.circle == null) {
                        that.circle = that.createCircle('circle', 'circle', event.latLng, 10, {}, {});
                        that.circle.changeEditable(true);
                        that.circle.setDraggable(true);
                    } else {
                        that.circle.setCenter(event.latLng);
                    }
                }
            });

            $('input[name=fcShape]').on('change', function () {
                that.changeDrawingMode($(this).val());
            });
            this.changeDrawingMode($('input[name=fcShape]:checked').val());


            /!**
             * 층선택 기능
             *!/
            this.floorService = new FloorService(this, new ServiceRequest(elementHandler.baseurl));
            this.floorService.bindFloorSelectBox('floor-selector');
            this.floorService.load();


            /!**
             * 펜스 기초정보가 있을경우 펜스를 그린다.
             *!/
            if (elementHandler.fence) {
                var fenceType = elementHandler.fence.type;
                var points = elementHandler.fence.points;
                if (fenceType == 'P') {
                    that.changeDrawingMode('P');
                    var latlngs = [];
                    for (var i = 0; i < points.length; i++) {
                        var point = points[i];
                        var latlng = new google.maps.LatLng(point.lat, point.lng);
                        latlngs.push(latlng);
                    }
                    that.getMap().setCenter(latlngs[0]);
                    that.getPolygonDrawingManager().startDrawing(latlngs);
                    that.getPolygonDrawingManager().onComplete();

                } else {
                    that.changeDrawingMode('C');
                    var latlng = new google.maps.LatLng(elementHandler.fence.points[0].lat, elementHandler.fence.points[0].lng);
                    that.circle = that.createCircle('circle', 'circle', latlng, elementHandler.fence.points[0].radius);
                    that.circle.changeEditable(true);
                    that.circle.setDraggable(true);
                }
            }
        });
    }*/


});

/**
 * 폼 전송시 펜스 상태 확인
 */
elementHandler.formPreHandler = function() {
    var googleMap = elementHandler.googleMap;
    var mode = googleMap.drawingMode;
    var json = new Array();
    if(mode=='P') {
        if(googleMap.getPolygonDrawingManager().isComplete()!=true) {
            window.alert(vm.fenceCompleteError);
            return;
        }
        var path = googleMap.getPolygonDrawingManager().getPolygon().getPath();
        path.forEach(function(latLng, index) {
            json.push({lat: latLng.lat(), lng: latLng.lng(), order: index});
        });

    } else {
        var circle = googleMap.getShapeObject('circle', 'circle', 'circle');
        var lat = circle.getCenter().lat();
        var lng = circle.getCenter().lng();
        var radius = circle.getRadius();
        if(lat!=null && lng!=null && radius!=null) {
            json.push({lat:lat, lng:lng, radius: radius, order:0});
        }
    }

    if(json.length>0) {
        $('#shapeData').val(JSON.stringify(json));
        return true;
    } else {
        window.alert(vm.fenceDrawError);
        return false;
    }
}

elementHandler.bind({
    searchBtn: function() {
        $('#form1').submit();
    },
    formBtn: 'geofencing/info/form.do',
    $mformBtn:'geofencing/info/mform.do?fcNum=${fcNum}&page=#{page}&opt=#{opt}&keyword=#{keyword}',
    listBtn: 'geofencing/info/list.do?page=#{page}&opt=#{opt}&keyword=#{keyword}',
    regBtn: {
        action: 'submit',
        form: 'form1',
        url: 'geofencing/info/reg.do',
        preHandler: elementHandler.formPreHandler,
        result: {
            1: {message:vm.regSuccess,
                redirect:'geofencing/info/list.do'},
            2: vm.regError
        }
    },
    modBtn: {
        action: 'submit',
        form: 'form1',
        url: 'geofencing/info/mod.do',
        preHandler: elementHandler.formPreHandler,
        result: {
            1: {message:vm.modSuccess,
                redirect:'geofencing/info/list.do?page=#{page}&opt=#{opt}&keyword=#{keyword}'},
            2: vm.modError
        }
    },
    delBtn: {
        action: 'submit',
        form: 'form1',
        url: 'geofencing/info/del.do',
        preHandler:function() {
            return window.confirm(vm.delConfirm);
        },
        result: {
            1: {message:vm.delSuccess,
                redirect:'geofencing/info/list.do?page=#{page}&opt=#{opt}&keyword=#{keyword}'},
            2: vm.delError
        }
    },
    syncBtn: {
        action: 'submit',
        form: 'form1',
        url: 'geofencing/info/sync.do',
        preHandler:function() {
            return window.confirm(vm.syncConfirm);
        },
        result: {
            1: {message:vm.syncSuccess},
            2: vm.syncError
        }
    }
});

