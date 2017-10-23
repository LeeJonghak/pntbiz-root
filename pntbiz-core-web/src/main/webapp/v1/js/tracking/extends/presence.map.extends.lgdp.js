


pnt.module.get('presence.map').on('prev', function(module, param) {

    window.console.debug = function() {};

    // 폰트설정
    document.getElementsByTagName("BODY")[0].style.fontFamily = 'Malgun Gothic';

    param.prop.put('control', param.prop.get('map.debug')); // 맵 컨트롤 표시 여부
    param.prop.put('maptile', false); // 맵 타일이미지 표시여부
    param.prop.put('marker.click.tooltip', true); // 마커 클릭시 툴팁표시여부
    param.prop.put('marker.label', false); // 마커이름 표시여부
    param.prop.put('bgcolor', '#34343D'); // 지도 배경색
    param.prop.put('floor', pnt.util.getUrlParameter('floor') || '1');
    param.prop.put('zoom', pnt.util.getUrlParameter('zoom') || 20.74 );
    param.prop.put('socket.io.start', false);
    param.prop.put('beacon.show.time', 60*60*24*7*1000); // 마지막 신호 이후 마커 유지 시간, 기본값 5000
    param.prop.put('url.static', '');

    // iframe 데이터 전송 proxy 경로
    param.prop.put('url.porthole.proxy', 'http://ismdev.lgdisplay.com:3300/resource/domain/proxy.html');
    param.prop.put('url.find.user', 'http://ismdev.lgdisplay.com:3300/rtl/employeeInfo');

    param.prop.put('marker.focus.border', '3px solid #fff'); // 마커 클릭시 테두리 강조
    param.prop.put('marker.focus.borderRadius', '30px');

    param.prop.put('timer.no.signal', 1000*5); // 신호 없음 아이콘 표시 시간
    param.prop.put('timer.sos.big1', 1000*60*15); // 15분뒤에 SOS 아이콘 크게
    param.prop.put('timer.sos.big2', 1000*60*30); // 30분뒤에 SOS 아이콘 더 크게

    // 마커 아이콘
    param.prop.put('icon.normal.display', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_normal_display.png');
    param.prop.put('icon.normal.external', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_normal_external.png');
    param.prop.put('icon.normal.internal', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_normal_internal.png');
    param.prop.put('icon.sos.display', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_SOS_display_x4.png');
    param.prop.put('icon.sos.external', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_SOS_external_x4.png');
    param.prop.put('icon.sos.internal', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_SOS_internal_x4.png');
    param.prop.put('icon.danger.display', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_warning_display.png');
    param.prop.put('icon.danger.external', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_warning_external.png');
    param.prop.put('icon.danger.internal', param.prop.get('url.static')+'/images/lgdp/img_marker/ic_warning_internal.png');

    // 1: 일반모드
    // 2: SOS만 표시
    // 3: Scanner만 표시
    // 4: 아무것도 표시않함
    this.mode = pnt.util.getUrlParameter('mode') || '1';
    if(this.mode=='3') {
        param.prop.put('map.visible.scanner', true);
    }

    if(this.mode=='3' || this.mode=='4' || this.mode=='5') {
        param.prop.put('socket.io.off', true);
    }


    this._geofencePermitPolygons = {};
    this._popupUserInfoCache = {};
    this.getPopupUserInfo = pnt.util.bind(this, function(id, callback) {
        if(typeof(this._popupUserInfoCache[id])=='undefined' || this._popupUserInfoCache[id]==null) {
            var temp = id.split('_');
            var uuid = temp[0].toUpperCase();
            var majorVer = temp[1];
            var minorVer = temp[2];

            var remoteUrl = this.prop.get('url.find.user')+'?uuid='+uuid+'&majorVer='+majorVer+'&minorVer='+minorVer;
            var url =  this.prop.get('url.remoteJson')+'?url='+encodeURIComponent(remoteUrl)+'&_t='+(new Date().getTime());

            pnt.util.fetch({
                url: url,
                method: 'get',
                responseType: 'json',
                success: pnt.util.bind(this, function (json) {
                    this._popupUserInfoCache[id] = json;
                    callback(this.htmlUserInfo(json), json);
                }),
                fail: pnt.util.bind(this, function () {
                    this._popupUserInfoCache[id] = [];
                })
            });

        } else {
            callback(this.htmlUserInfo(this._popupUserInfoCache[id]), this._popupUserInfoCache[id]);
        }
    });
    this.htmlUserInfo = function(response) {

        var html = '';
        if(response.length<=0) {
            html = '정보가 존재하지 않습니다';
        } else {

            html = '<table>';
            for(var i=0; i<response.length; i++) {
                var key = response[i].key;
                var val = response[i].value;

                html += '<tr><td style="font-size:9pt;width:70px;">'+key+'</td><td style="font-size:9pt;">|&nbsp;</td>'
                    + '<td style="font-size:9pt;">'+(val||'')+'</td></tr>'

            }
            html += '</table>';
        }
        return html;
    }

    this.beaconInfoMarkerTypeMap = {};
    this.updateBeaconInfoMarkerType = function() {

        pnt.util.fetch({
            url: param.prop.get('url.beacon')+'&_t='+(new Date().getTime()),
            data: {field: 'UUID,majorVer,minorVer,PARTNER_BIZ_GB'},
            method: 'get',
            responseType: 'json',
            success: function(response) {
                if(typeof(response.result)!='undefined' && response.result=='1') {
                    for(var i=0; i<response.data.length; i++) {
                        var item = response.data[i];
                        var id = item['UUID']+'_'+item['majorVer']+'_'+item['minorVer'];
                        if(typeof(item['PARTNER_BIZ_GB'])!='undefined') {
                            module.beaconInfoMarkerTypeMap[id] = item['PARTNER_BIZ_GB'];

                            if(item['PARTNER_BIZ_GB']=='INC' || item['PARTNER_BIZ_GB']=='EXC') {

                                var om = module.pntmap.getObjectManager();
                                var marker = om.get(pnt.map.object.type.MARKER, id);

                                if(marker!=false && marker.getData('sos')=='1') {
                                    if(item['PARTNER_BIZ_GB']=='INC') {
                                        marker.changeIcon(module.prop.get('icon.sos.internal'));
                                        marker.setData('partnerType', 'I');
                                    }
                                    else if(item['PARTNER_BIZ_GB']=='EXC') {
                                        marker.changeIcon(module.prop.get('icon.sos.external'));
                                        marker.setData('partnerType', 'E');
                                    }
                                } else if(marker!=false) {
                                    if(item['PARTNER_BIZ_GB']=='INC') {
                                        marker.changeIcon(module.prop.get('icon.normal.internal'));
                                        marker.setData('partnerType', 'I');
                                    }
                                    else if(item['PARTNER_BIZ_GB']=='EXC') {
                                        marker.changeIcon(module.prop.get('icon.normal.external'));
                                        marker.setData('partnerType', 'E');
                                    }
                                }

                            }
                        } else {
                            module.beaconInfoMarkerTypeMap[id] = '';
                        }

                    }
                }

            }
        });

        window.setTimeout(function() {
            module.updateBeaconInfoMarkerType();
        }, 60*1*1000); // 1분마다 갱싱
    }
    this.updateRtlsProp = function() {
        pnt.util.fetch({
            url: '/tracking/rtlsprop/list.json?_t=' + (new Date().getTime()),
            data: {comNum: param.prop.get('comNum'), UUID: param.prop.get('uuid')},
            method: 'get',
            responseType: 'json',
            success: function (response) {
                if(typeof(response.result)!='undefined' && response.result=='1') {
                    for(var i=0; i<response.data.length; i++) {
                        var key = response.data[i].key;
                        var value = response.data[i].value;
                        module.rtlsProp[key] = value;
                    }
                }

            }
        });

        window.setTimeout(function() {
            module.updateRtlsProp();
        }, 60*1*1000); // 1분마다 갱싱
    }

    this.showDangerGeofence = function() {
        var geofences = this.pntmap.getObjectManager().findTag('geofence');
        for(var i=0; i<geofences.length; i++) {
            var geofence = geofences[i];
            var field1 = String(geofence.getData().get('field1')).toLowerCase();
            if(field1=='danger') {
                geofence.setStyle({
                    fill:{color:[0, 0, 0, 0.2]}, stroke:{color:[255, 128, 0, 0.8],width:1}
                });
                geofence.show();
            }

        }
    }
    this.hideDangerGeofence = function() {
        var geofences = this.pntmap.getObjectManager().findTag('geofence');
        for(var i=0; i<geofences.length; i++) {
            var geofence = geofences[i];
            var field1 = String(geofence.getData().get('field1')).toLowerCase();
            if(field1=='danger') {
                geofence.hide();
            }
        }
    }

    this.rtlsProp = {};
    this.drawIsmPermitGeofence = function(json) {
        if(typeof(json)=='string') {
            json = JSON.parse(json);
        }

        var convert = function(extent, imageSize, originalXy) {
            var diffLat = extent[3] - extent[1];
            var diffLng = extent[2] - extent[0];

            var lng = extent[0] + originalXy[0]/imageSize[0] * diffLng;
            var lat = extent[1] + (imageSize[1] - originalXy[1]) / imageSize[1] * diffLat;

            return [lng, lat];
        }

        var meter = module.rtlsProp['permitExtendsMeter'];
        for(var i=0; i<json.length; i++) {
            for(var j=0; j<json[i].space.length; j++) {
                var startXy = [json[i].space[j].startX, json[i].space[j].startY];
                var endXy = [json[i].space[j].endX, json[i].space[j].endY];
                var startTime = json[i].startTime;
                var endTime = json[i].endTime;
                var imageWidth = json[i].width;
                var imageHeight = 824*(imageWidth/1467); // 504
                var absDist = imageWidth*0.1539619233952893;
                var floorImageExtent = module.floorManager.getImage().getExtent();

                // 픽셀당 거리M
                var pixelDist = absDist / imageWidth;
                // 사이즈 변경
                var edgePixel = meter / pixelDist;

                var p1 = [startXy[0] - edgePixel, startXy[1] - edgePixel];
                var p2 = [startXy[0] - edgePixel, endXy[1] + edgePixel];
                var p3 = [endXy[0] + edgePixel, endXy[1] + edgePixel];
                var p4 = [endXy[0] + edgePixel, startXy[1] - edgePixel];

                var lonlats = [
                    convert(pnt.util.transformExtentLonLat(floorImageExtent), [imageWidth, imageHeight], p1),
                    convert(pnt.util.transformExtentLonLat(floorImageExtent), [imageWidth, imageHeight], p2),
                    convert(pnt.util.transformExtentLonLat(floorImageExtent), [imageWidth, imageHeight], p3),
                    convert(pnt.util.transformExtentLonLat(floorImageExtent), [imageWidth, imageHeight], p4)
                ];

                var id = 'polygon-permit-ism-'+pnt.util.makeid(10);
                module.pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, id, {
                    label: startTime+' ~ '+endTime, data: {}, coords:lonlats,
                    tag:'polygon-permit-ism', fill:{color:[0,191,255,0.1]}, stroke:{color:[0,191,255,0.5],width:1}
                });

            }
        }
    }

    this.updateBeaconInfoMarkerType();
    this.updateRtlsProp();

});

pnt.module.get('presence.map').on('after', function(module, param) {

    if(this.mode=='5') {

        var tracking = new pnt.map.Tracking(this.pntmap, this.floorManager, {
            speed: 10, // 재생속도
            maxAfterimage: 60*2,// 60*60*4, // 잔상갯수
            afterimageRandomColor: false,
            removeMarkerSec: 30, // 신호데이터가 없어도 마커를 유지할 횟수
            //showLabel: this.prop.get('map.debug'), // 마커 라벨 표시 여부 (재생시간, minorVer표시)
            showLabel: true,
            showMarker: true,
            period: 60*10, // tracking.period 이벤트 발생 주기
            timeController: this.prop.get('map.debug'), // 재생시간 컨트롤러에 표시 여부
            afterimageColor:[229,76,118],
            afterimageWidth: 5,
            keepTrace: true // 경로 삭제 않함
        });
        tracking.draw = function(time, additional) {
            var currentFloor = this._floorManager.getCurrentFloor();

            if(this._period>0) {
                if(this._periodLastTime + this._period < time) {

                    this._offlineMap.getOlMap().dispatchEvent({
                        type: 'tracking.period',
                        period: this._period,
                        time: time,
                        object: this,
                        objectType: 'tracking'
                    });

                    this._periodLastTime = time;
                }
            }

            for(var id in this._data) {
                var data = this._data[id][time];

                var lineCoordinates = [];

                if(typeof(this._lastExistsDataTime)=='undefined') {
                    this._lastExistsDataTime = {};
                }
                if(typeof(this._lastExistsDataTime[id])!='undefined' && this._lastExistsDataTime[id]!=null) {

                    lineCoordinates.push([this._data[id][this._lastExistsDataTime[id]].lng,
                    this._data[id][this._lastExistsDataTime[id]].lat,
                    this._data[id][this._lastExistsDataTime[id]].floor,
                    this._lastExistsDataTime[id]]);

                }

                if(typeof(this._data[id][time])!='undefined' && currentFloor==this._data[id][time].floor) {
                    lineCoordinates.push([this._data[id][time].lng, this._data[id][time].lat, this._data[id][time].floor, time]);
                }

                if(typeof(additional)!='undefined' && additional>0) {
                    for(var lindex=0; lindex<additional; lindex++) {
                        var addData = this._data[id][time+lindex+1];
                        if(typeof(addData)!='undefined' && currentFloor==addData.floor) {
                            lineCoordinates.push([addData.lng, addData.lat, addData.floor, time+lindex+1]);
                        }
                    }
                }

                if(lineCoordinates.length==0) {
                } else if(lineCoordinates.length==1) {
                    this._lastExistsDataTime[id] = lineCoordinates[0][3];
                } else {

                    this._lastExistsDataTime[id] = lineCoordinates[lineCoordinates.length-1][3];

                    /**
                     * 라인 그리기
                     */
					var coords = [];
					for(var i=0; i<lineCoordinates.length; i++) {
						coords.push(pnt.util.transformExtentCoordinates(lineCoordinates[1], lineCoordinates[0]));
					}
                    var options = {
                        coords: coords,
                        tag: 'tracking.line2',
                        stroke: {
                            color: [this._lineColor[0], this._lineColor[1], this._lineColor[2], 0.8],
                            width: this._lineWidth
                        }
                    }

                    if (typeof(this._afterimageLine[id]) == 'undefined') {
                        this._afterimageLine[id] = {};
                    }
                    if (typeof(this._afterimageLine[id][currentFloor]) == 'undefined') {
                        this._afterimageLine[id][currentFloor] = {};
                    }
                    this._afterimageLine[id][currentFloor][time] = this._objectManager.create(pnt.map.object.type.LINE, 'tracking.line2-' + id + '-' + (time), options);


                    /**
                     * 마커 그리기
                     */
                    var dt = new Date((lineCoordinates[lineCoordinates.length-1][3]*1000)-(60*60*9*1000));
                    var timestring = (dt.getHours()<10?'0'+dt.getHours():dt.getHours())+':'
                        +(dt.getMinutes()<10?'0'+dt.getMinutes():dt.getMinutes())+':'
                        +(dt.getSeconds()<10?'0'+dt.getSeconds():dt.getSeconds());
                    if(typeof(this._markers[id])=='undefined') {
                        this._markers[id] = [];

                        var options = {
                            data: this._data[lineCoordinates[lineCoordinates.length-1][3]],
                            position: pnt.util.transformExtentCoordinates(lineCoordinates[lineCoordinates.length-1]),
                            tag:'tracking.marker', url: this.__lgicon,
                            style: {
                                height:'7px', width:'7px'
                            },
                            autoPan: false
                        };

                        this._markers[id][0] = this._objectManager.create(pnt.map.object.type.MARKER, 'tracking.marker-'+id, options);
                        this._markers[id][0].on('click', function() {
                            console.log('marker data', this.getData())
                        });
                        this._markers[id][0].showLabel(timestring);
                        var img = this._markers[id][0].getElement();
                        img.style.width = '19px';
                        img.style.height = '19px';
                        var label = this._markers[id][0].getLabelElement();
                        label.style.marginTop = '10px';
                    } else {
                        this._markers[id][0].move(lineCoordinates[lineCoordinates.length-1]);
                        this._markers[id][0].setLabelText(timestring);
                    }
                }
            }
            this._currentFloor = currentFloor;
        }
        tracking.setDisplayMode('line');

        this.tracking = tracking;
        this.trackingParam = {comNum: this.prop.get('comNum')};
        this.playTrackingDatetime = function(majorVer, minorVer, startDatetime, endDatetime) {
            var sdt = pnt.util.parseDate(startDatetime);
            if (typeof(endDatetime) != 'undefined') {
                var edt = pnt.util.parseDate(endDatetime);
                this.playTrackingTimestamp(majorVer, minorVer, (sdt.getTime() / 1000) + (60 * 60 * 9), (edt.getTime() / 1000) + (60 * 60 * 9));
            } else {
                this.playTrackingTimestamp(majorVer, minorVer, (sdt.getTime() / 1000) + (60 * 60 * 9));
            }
        }
        this.playTrackingTimestamp = function(majorVer, minorVer, startTimestamp, endTimestamp) {

            this.trackingParam.majorVer = majorVer;
            this.trackingParam.minorVer = minorVer;
            this.trackingParam.startRegDate = startTimestamp;
            this.trackingParam.endRegDate = startTimestamp+this.tracking.getPeriod();

            this.tracking.clear();
            pnt.util.fetch({
                url: this.prop.get('url.tracking.log'),
                data: this.trackingParam,
                responseType:'json',
                success: pnt.util.bind(this.tracking, function(response) {
                    this.loadData(response.data);
                    if(typeof(endTimestamp)!='undefined') {
                        this._lastExistsDataTime = {};
                        this.play({startTime:startTimestamp, endTime:endTimestamp, period:this.getPeriod()});
                    } else {
                        this._lastExistsDataTime = {};
                        this.play({startTime:startTimestamp, period:this.getPeriod()});
                    }
                })
            });

        }

        this.pntmap.on('tracking.period', pnt.util.bind(this, function(evt) {
            this.trackingParam.startRegDate = evt.time+evt.period,
                this.trackingParam.endRegDate = evt.time+evt.period+evt.period

            pnt.util.fetch({
                url: this.prop.get('url.tracking.log'),
                data: this.trackingParam,
                responseType:'json',
                success: pnt.util.bind(evt.object, function(response) {
                    this.loadData(response.data);
                })
            });
        }));

    }

    var portholeUrl = module.prop.get('url.static')+'/external/js.porthole/porthole.min.js';
    pnt.util.loadJavascript(portholeUrl, function() {

        module.proxy = new Porthole.WindowProxy(module.prop.get('url.porthole.proxy'));
        module.proxy.addEventListener(function(event) {

            if (event.origin!=location.origin && event.data['command']) {
            //if (event.data['command']) {
                var command = event.data['command'];

                if(typeof(command)=='undefined' || command=='one') { // 선택
                    var beaconData = event.data['beacon'];
                    var uuid = beaconData.uuid.toUpperCase();
                    var majorVer = beaconData.majorVer;
                    var minorVer = beaconData.minorVer;
                    var id = uuid+'_'+majorVer+'_'+minorVer;

                    if(module.pntmap) {
                        module.pntmap.getObjectManager().findTag('beacon', function(object) {
                            object.unfocus();
                        });
                        var marker = module.pntmap.getObjectManager().get(pnt.map.object.type.MARKER, id);
                        if(marker) {
                            marker.focus();

                            var tooltipStatus =  marker.getData('tootip.show');
                            if(tooltipStatus!=true) {
                                marker.showTooltip();

                                var lastTooltip = module.prop.get('tooltip.last');
                                if(lastTooltip) {
                                    lastTooltip.setZindex(100);
                                }
                                marker.getTooltip().setZindex(101);
                                module.prop.put('tooltip.last', marker.getTooltip());

                            } else {
                                marker.hideTooltip();
                            }
                        }
                    }
                }
                else if(command=='all') { // 모든 툴팁 표시
                    module.pntmap.getObjectManager().findTag('beacon', function(object) {
                        if(object.getVisible() == true) {
                            object.showTooltip();
                        }
                    });
                }
                else if(command=='none') { // 모든 툴팁 감춤
                    module.pntmap.getObjectManager().findTag('beacon', function(object) {
                        object.hideTooltip();
                    });
                }
                else if(command=='scanner') { // 스캐너 보기/감추기
                    if(event.data['data']=='show') {
                        param.prop.put('map.visible.scanner', true);
                    }
                    else if(event.data['data']=='hide') {
                        param.prop.put('map.visible.scanner', false);
                    }
                }
                else if( (command=='trace.sos' || command=='trace.danger' || command=='trace.leave')
                    && module.tracking) { // 트래킹 표시

                    var beaconData = event.data['beacon'];
                    var majorVer = beaconData.majorVer;
                    var minorVer = beaconData.minorVer;
                    var startDate = event.data['startDate'];
                    var endDate = event.data['endDate'];
                    var playRate = event.data['playRate'] || 15;
                    var partnerBizGb = String(event.data['partnerBizGb']).toLowerCase();

                    var color = [175,51,233];
                    var icon = param.prop.get('icon.normal.display');
                    if(command=='trace.sos') {
                        color = [243,77,120];
                        if(partnerBizGb=='inc') {
                            icon = param.prop.get('icon.sos.internal');
                        }
                        else if(partnerBizGb=='exc') {
                            icon = param.prop.get('icon.sos.external');
                        }
                        else {
                            icon = param.prop.get('icon.sos.display');
                        }
                    }
                    else if(command=='trace.danger') {
                        color = [247, 148, 29];
                        if(partnerBizGb=='inc') {
                            icon = param.prop.get('icon.normal.internal');
                        }
                        else if(partnerBizGb=='exc') {
                            icon = param.prop.get('icon.normal.external');
                        }
                        else {
                            icon = param.prop.get('icon.normal.display');
                        }
                    }
                    else {
                        if(partnerBizGb=='inc') {
                            icon = param.prop.get('icon.normal.internal');
                        }
                        else if(partnerBizGb=='exc') {
                            icon = param.prop.get('icon.normal.external');
                        }
                    }

                    module.tracking.setLineColor(color);
                    module.tracking.setSpeed(playRate);
                    module.tracking.__lgicon = icon;

                    if(typeof(event.data['color'])!='undefined') {
                        module.tracking.setLineColor(event.data['color'])
                    }
                    if(typeof(event.data['speed'])!='undefined') {
                        module.tracking.setSpeed(event.data['speed'])
                    }

                    module.playTrackingDatetime(majorVer, minorVer, startDate, endDate);

                    // 인가지역 표시
                    var ismPermitPolygons = module.pntmap.getObjectManager().findTag('polygon-permit-ism');
                    if(typeof(ismPermitPolygons)!='undefined') {
                        for(var i=0; i<ismPermitPolygons.length; i++) {
                            module.pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, ismPermitPolygons[i].getId());
                        }
                    }
                    if(typeof(event.data['space'])!='undefined' && event.data['space']!=null) {
                        module.drawIsmPermitGeofence(event.data['space']);
                    }

                }
                else if(command=='danger') {
                    if(event.data['data']=='show') {
                        module.showDangerGeofence();
                    }
                    else if(event.data['data']=='hide') {
                        module.hideDangerGeofence();
                    }
                }
                else if(command=='work') {
                    if(event.data['data']=='show' && typeof(event.data['space'])!='undefined') {
                        // 인가지역 표시
                        var ismPermitPolygons = module.pntmap.getObjectManager().findTag('polygon-permit-ism');
                        if(typeof(ismPermitPolygons)!='undefined') {
                            for(var i=0; i<ismPermitPolygons.length; i++) {
                                module.pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, ismPermitPolygons[i].getId());
                            }
                        }
                        if(typeof(event.data['space'])!='undefined') {
                            module.drawIsmPermitGeofence(event.data['space']);
                        }
                    }
                    else if(event.data['data']=='hide') {
                        var ismPermitPolygons = module.pntmap.getObjectManager().findTag('polygon-permit-ism');
                        if(typeof(ismPermitPolygons)!='undefined') {
                            for (var i = 0; i < ismPermitPolygons.length; i++) {
                                module.pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, ismPermitPolygons[i].getId());
                            }
                        }
                    }
                }

            }
        });
    });

    this.pntmap.on('marker.click', function(evt) {
        if(evt.object.containsTag('beacon') && typeof(module.proxy)!='undefined' && module.proxy!=null) {
            module.proxy.post({beacon:{
                uuid:evt.object.getData('UUID').toUpperCase(),
                majorVer:evt.object.getData('majorVer'),
                minorVer:evt.object.getData('minorVer')
            }});
        }
    });

    this.pntmap.on('click', function(evt) {
        console.log('evt', evt);
    });
    this.pntmap.on('tooltip.show', function(evt) {
        var id = evt.object.getData('UUID')+'_'+evt.object.getData('majorVer')+'_'+evt.object.getData('minorVer');
        var marker = evt.object.getData('marker');
		evt.object.setContent('...');

        if(marker) {
            if(typeof(marker)!='undefined' && marker!=null && marker!=false && marker.getData('sos')=='1') {
                // 직원정보 캐시 제거
                delete module._popupUserInfoCache[id];
            }
            module.getPopupUserInfo(id, pnt.util.bind(this, function(html) {
                evt.object.setContent(html);
            }));
            if(marker) {
                marker.setData('tootip.show', true);
            }
        }
    });
    this.pntmap.on('tooltip.hide', function(evt) {
        var marker = evt.object.getData('marker');
        if(marker) {
            marker.setData('tootip.show', false);
        }
    });

    this.pntmap.on('marker.timer', function(evt) {
        if(evt.object.containsTag('beacon')) {
            if(evt.timerId=='timer.sos.big1') {
                var img = evt.object.getElement();
                img.style.width = '36px';
                img.style.height = '36px';
            }
            else if(evt.timerId=='timer.sos.big2') {
                var img = evt.object.getElement();
                img.style.width = '48px';
                img.style.height = '48px';
            }
            else if(evt.timerId=='timer.no.signal') {
                var img = evt.object.getElement();
                img.style.filter = 'grayscale(1)';
                img.style.WebkitFilter = 'grayscale(1)';
            }
        }
    });
    this.pntmap.on('polygon.rightclick', function(evt) {
        if(evt.object.containsTag('geofence-permit')) {
            module.pntmap.getContextMenu().show(evt.coordinate, {
                01: {text: '감추기', callback: pnt.util.bind(this, function() {
                    this.getObjectManager().remove(pnt.map.object.type.POLYGON, evt.object.getId());
                    module._geofencePermitPolygons = {};
                })}
            });
        }
    });

    this.pntmap.on('floor.change', function(evt) {

        var polygons = module.pntmap.getObjectManager().findTag('geofence-permit');
        for(var i=0; i<polygons.length; i++) {
            module.pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, polygons[i].getId());
        }

        if(typeof(module._geofencePermitPolygons[module.floorManager.getCurrentFloor()])!='undefined') {
            var data = module._geofencePermitPolygons[module.floorManager.getCurrentFloor()];
            for (var i = 0; i < data.length; i++) {
                if (data[i].floor == module.floorManager.getCurrentFloor()) {
                    var latlngs = data[i].latlngs;
                    var coordinates = [];
                    for (var j = 0; j < latlngs.length; j++) {
                        coordinates.push(pnt.util.transformExtentCoordinates([latlngs[j].lng, latlngs[j].lat]));
                    }
                    var id = 'geofence-permit-' + pnt.util.makeid(20);
                    var geofence = module.pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, id, {
                        label: data[i].start+' ~ '+data[i].end,
                        data: {},
                        coords: coordinates,
                        tag: 'geofence-permit',
                        fill: {color: [0, 191, 255, 0.1]},
                        stroke: {color: [0, 191, 255, 0.5], width: 1}
                    });
                }
            }
        }

    });

});

pnt.module.get('presence.map').on('createMarker', function(module, param) {



    // 사내(I), 사외(E) 구분 확인
    var id = param.marker.getData('UUID')+'_'+param.marker.getData('majorVer')+'_'+param.marker.getData('minorVer');
    if(typeof(this.beaconInfoMarkerTypeMap[id])!='undefined' && this.beaconInfoMarkerTypeMap[id]=='INC') {
        param.marker.setData('partnerType', 'I'); // 내부
    }
    else if(typeof(this.beaconInfoMarkerTypeMap[id])!='undefined' && this.beaconInfoMarkerTypeMap[id]=='EXC') {
        param.marker.setData('partnerType', 'E'); // 외부
    }
    else {
        param.marker.setData('partnerType', 'D'); // 기본
    }

    var pntmap = this.pntmap;
    param.marker.on('mousedown', function(evt) {
        if (module.prop.get('map.debug')==true && event.which == 3) {
            pntmap.getContextMenu().show(this.getPosition(), {
                01: {text: '인가구역 보기',
                    callback: pnt.util.bind(this, function (menu) {

                        var geofencePermits = pntmap.getObjectManager().findTag('geofence-permit');
                        for(var i=0; i<geofencePermits.length; i++) {
                            pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, geofencePermits[i].getId());
                        }

                        var UUID = this.getData().UUID || this.getData().uuid;
                        var majorVer = this.getData().majorVer;
                        var minorVer = this.getData().minorVer;

                        var keyPattern = 'CACHE_GEOFENCE_PERMIT_INFO_44790BA4-7EB3-4095-9E14-4B43AE67512B_'+UUID+'_'+majorVer+'_'+minorVer;
                        var url = '/tracking/presence/scanner/findRedisItem.do?keyPattern='+keyPattern+'&type=string';

                        pnt.util.fetch({
                            url: url,
                            responseType:'json',
                            success: function(response) {
                                var data = response.data[0];
                                if(typeof(data)=='string') {
                                    data = JSON.parse(data);
                                }

                                if(data) {
                                    module._geofencePermitPolygons = {};
                                    for (var i = 0; i < data.length; i++) {
                                        if(typeof(module._geofencePermitPolygons[data[i].floor])=='undefined') {
                                            module._geofencePermitPolygons[data[i].floor] = [];
                                        }
                                        module._geofencePermitPolygons[data[i].floor].push(data[i]);
                                        if (data[i].floor == module.floorManager.getCurrentFloor()) {
                                            var latlngs = data[i].latlngs;
                                            var coordinates = [];
                                            for (var j = 0; j < latlngs.length; j++) {
                                                coordinates.push(pnt.util.transformExtentCoordinates([latlngs[j].lng, latlngs[j].lat]));
                                            }
                                            var id = 'geofence-permit-' + pnt.util.makeid(20);
                                            var geofence = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, id, {
                                                label: data[i].start+' ~ '+data[i].end,
                                                data: {},
                                                coords: coordinates,
                                                tag: 'geofence-permit',
                                                fill: {color: [0, 191, 255, 0.1]},
                                                stroke: {color: [0, 191, 255, 0.5], width: 1}
                                            });
                                        }
                                    }
                                } else {
                                    window.alert('데이터 없음');
                                }
                            }
                        });

                    })
                }
            });
        }
    });
});
pnt.module.get('presence.map').on('createMarker,updateMarker', function(module, param) {

    var flagChangeIcon = true;
    /*if(module.prop.get('map.debug')==true && param.marker.getData('redis')==true) {
        flagChangeIcon = false;
    }*/

    if(param.marker.getData('redis.prev')==true && param.marker.getData('redis')!=true) {
        param.marker.setData('sos.prev', undefined);
    }


    if( (this.mode=='3' || this.mode=='4' || this.mode=='5') || (this.mode=='2' && param.data.sos!='1') ) {

        param.marker.hide();
        param.marker.getTooltip().hide(true);
    }


    /**
     * 신호가 없을 경우 아이콘 회색 처리
     */
    param.marker.setTimer('timer.no.signal', this.prop.get('timer.no.signal'));
    var img = param.marker.getElement();
    if(typeof(param.data.redis)!='undefined' && param.data.redis==true) {
        img.style.filter = 'grayscale(1)';
        img.style.WebkitFilter = 'grayscale(1)';
    } else {
        img.style.filter = '';
        img.style.WebkitFilter = '';
    }
    /** **/

    if( (typeof(param.marker.getData('sos.prev'))=='boolean' && param.marker.getData('sos.prev')==false) ||
        param.marker.getData('sos')!=param.marker.getData('sos.prev')) {

        // 직원정보 캐시 제거
        delete this._popupUserInfoCache[param.marker.getId()];

        // 아이콘, 사이즈 변경
        if(param.marker.getData('sos')=='1') {

            if(param.marker.getData('sos.prev')==false && param.marker.getData('check.sos.time')!=true) {
                param.marker.setData('check.sos.time', true);
                this.getPopupUserInfo(param.marker.getId(), function(html, json) {
                    var flagExistsSosTime = false;
                    for(var key in json) {
                        if(json[key].key=='호출시간' && typeof(json[key].value)=='string' && json[key].value.length>=19) {
                            var stime = pnt.util.parseDate(json[key].value.substring(0,19));
                            var nowtime = (new Date()).getTime();

                            if(nowtime-stime<=this.prop.get('timer.sos.big1')) {
                                param.marker.setTimer('timer.sos.big1', this.prop.get('timer.sos.big1')-(nowtime-stime));
                            } else {
                                param.marker.setTimer('timer.sos.big1', 100);
                            }
                            if(nowtime-stime<=this.prop.get('timer.sos.big2')) {
                                param.marker.setTimer('timer.sos.big2', this.prop.get('timer.sos.big2')-(nowtime-stime));
                            } else {
                                param.marker.setTimer('timer.sos.big2', 200);
                            }

                            flagExistsSosTime = true;
                            break;
                        }
                    }
                    if(flagExistsSosTime==false) {
                        param.marker.setTimer('timer.sos.big1', this.prop.get('timer.sos.big1'));
                        param.marker.setTimer('timer.sos.big2', this.prop.get('timer.sos.big2'));
                    }

                    param.marker.setData('check.sos.time', false);
                });
            } else if(param.marker.getData('check.sos.time')!=true){
                param.marker.setTimer('timer.sos.big1', this.prop.get('timer.sos.big1'));
                param.marker.setTimer('timer.sos.big2', this.prop.get('timer.sos.big2'));
            }

            var img = param.marker.getElement();
            img.style.width = '24px';
            img.style.height = '24px';

            if(param.marker.getData('partnerType')=='I' && flagChangeIcon==true) {
                param.marker.changeIcon(module.prop.get('icon.sos.internal'));
            }
            else if(param.marker.getData('partnerType')=='E' && flagChangeIcon==true) {
                param.marker.changeIcon(module.prop.get('icon.sos.external'));
            } else if(flagChangeIcon==true) {
                param.marker.changeIcon(module.prop.get('icon.sos.display'));
            }

            if(this.mode=='1' || this.mode=='2') {
                param.marker.showTooltip();
            }

        } else {
            param.marker.removeTimer('timer.sos.big1');
            param.marker.removeTimer('timer.sos.big2');

            var img = param.marker.getElement();
            img.style.width = '16px';
            img.style.height = '16px';

            if(param.marker.getData('partnerType')=='I' && flagChangeIcon==true) {
                param.marker.changeIcon(module.prop.get('icon.normal.internal'));
            }
            else if(param.marker.getData('partnerType')=='E' && param.marker.getData('redis')!=true) {
                param.marker.changeIcon(module.prop.get('icon.normal.external'));
            }
            else if(flagChangeIcon==true) {
                param.marker.changeIcon(module.prop.get('icon.normal.display'));
            }

        }
    }
    param.marker.setData('sos.prev', param.marker.getData('sos'));
    param.marker.setData('redis.prev', param.marker.getData('redis'))
});
