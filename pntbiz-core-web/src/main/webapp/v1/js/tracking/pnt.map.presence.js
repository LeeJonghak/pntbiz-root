
pnt.module.create('presence.map', function(module, param) {

	this.notiProp = new pnt.util.Properties();
    this.clear = function() {
        var om = this.pntmap.getObjectManager();
        var beacons = om.findTag('beacon');

        for(var i=0;i<beacons.length; i++) {
            var beacon = beacons[i];

            if(typeof(beacon)!='undefined') {
                var circles = beacon.getData('diff');
                if (circles) {
                    for (var j = 0; j < circles.length; j++) {
                        om.remove(pnt.map.object.type.CIRCLE, circles[j].getId());
                    }
                }

                var polygon = beacon.getData('triang');
                if (polygon) {
                    om.remove(pnt.map.object.type.POLYGON, polygon.getId());
                }
                om.remove(pnt.map.object.type.MARKER, beacon.getId());
            }
        }
    }


    this.updateMarker = function(data) {

		if(typeof(data.id)=='undefined') {
			data.id = (data.uuid || data.UUID)+'_'+data.majorVer+'_'+data.minorVer;
		}

        data.id = data.id.toUpperCase();
        data.UUID = data.UUID.toUpperCase();

        var info = data.id.split('_');
        if(param.prop.get('debug.filter.minorVer')) {
            var checkMinorVer = (param.prop.get('debug.filter.minorVer')+'').split(',');
            if(checkMinorVer.indexOf(info[2])==-1) {
                return;
            } else {
                console.debug('updateMarker', data);
            }
        }

        var om = this.pntmap.getObjectManager();
        var marker = om.get(pnt.map.object.type.MARKER, data.id);

        var triangVisible = param.prop.get('map.visible.triangulation');
        var diffVisible = param.prop.get('map.visible.differentialpos');
        if (marker) {
			var img = marker.getElement();
			img.style.filter = '';
			img.style.WebkitFilter = '';

			if(marker.getData('lostSignal')==true) {
				marker.setData('lostSignal', false);
				if(typeof(data.externalId)!='undefined' && data.externalId!=null && data.externalId!='') {
					marker.setLabelText(data.externalId);
				} else {
					marker.setLabelText(info[2]);
				}
			}

            if (this.floorManager.getCurrentFloor() == data.floor) {

                var coords = [];
                var circles = [];
                var color = marker.getData('color');

                if (prop.get('map.debug') == true) {
                    for (var i = 0; i < data.scannerPos.length; i++) {
                        coords.push(pnt.util.transformCoordinates([data.scannerPos[i].lng, data.scannerPos[i].lat]));

                        var circle = om.get(pnt.map.object.type.CIRCLE, 'diff-' + data.id + '-' + i);
                        if (circle) {
                            circle.setCenterRadius(
								pnt.util.transformCoordinates([data.scannerPos[i].lng, data.scannerPos[i].lat]),
								data.scannerPos[i].r);
                            circles.push(circle);
                        } else {

                            var circle = om.create(pnt.map.object.type.CIRCLE, 'diff-' + data.id + '-' + i, {
                                center: pnt.util.transformCoordinates([data.scannerPos[i].lng, data.scannerPos[i].lat]),
                                radius: data.scannerPos[i].r, tag: 'diff',
                                fill: {color: [color[0], color[1], color[2], 0.2]},
                                stroke: {color: color, width: 1}
                            });
                            if (circle) {
                                if (diffVisible == false) {
                                    circle.hide();
                                }
                                circles.push(circle);
                            }
                        }
                    }

                    var circles = marker.getData('diff');
                    if (data.scannerPos.length < circles.length) {
                        for (var i = data.scannerPos.length; i < circles.length; i++) {
                            om.remove(pnt.map.object.type.CIRCLE, circles[i].getId());
                        }
                    }

                    if(coords.length>3) {
                        coords = pnt.util.sortPolygonCoordinates(coords);
                    }
                    var polygon = marker.getData('triang');
                    polygon.setCoordinates(coords);
                }

                marker.move(pnt.util.transformCoordinates([data.lng, data.lat]));
                marker.setData('diff', circles);
                marker.setData('sos', data.sos);

				/**
				 * SOS 신호가 있을 경우 마커 아이콘 변경
				 */
				if (data.sos=='1') {
					marker.changeIcon(module.prop.get('icon.beaconSos'));
                    marker.getElement().style.zIndex = "1";
				} else {
					marker.changeIcon(module.prop.get('icon.beacon'));
                    marker.getElement().style.zIndex = "0";
				}

                if (data.showMarker == '0') {
                    marker.setTimer('remove', 100);
                } else {
                    marker.setTimer('remove', param.prop.get('beacon.show.time'));
                }

                marker.setData('floor', data.floor);
                marker.setData('sos', data.sos);
                marker.setData('creationDate', data.creationDate);
                marker.setData('lastTime', data.lastTime);
                marker.setData('lastTime2', data.lastTime2);
                marker.setData('redis', data.redis);

                if(param.prop.get('map.debug')==true && marker.getData('menu.event.id')!=false && data.redis!=true) {
                    marker.un(marker.getData('menu.event.id'));
                    marker.setData('menu.event.id', false);
                }

                module.dispatch('updateMarker', {marker: marker, data: data});
            } else {
                var circles = marker.getData('diff');
                if (circles) {
                    for (var i = 0; i < circles.length; i++) {
                        om.remove(pnt.map.object.type.CIRCLE, circles[i].getId());
                    }
                }

                var polygon = marker.getData('triang');
                if (polygon) {
                    om.remove(pnt.map.object.type.POLYGON, polygon.getId());
                }
                om.remove(pnt.map.object.type.MARKER, marker.getId());
            }
        } else if (this.floorManager.getCurrentFloor() == data.floor) {

            var color = pnt.util.randomColor();
            color[3] = 0.8;
            var circles = [];
            var coords = [];

            if (prop.get('map.debug') == true) {
                for (var i = 0; i < data.scannerPos.length; i++) {
                    coords.push(pnt.util.transformCoordinates([data.scannerPos[i].lng, data.scannerPos[i].lat]));

                    var circle = om.create(pnt.map.object.type.CIRCLE, 'diff-' + data.id + '-' + i, {
                        center: pnt.util.transformCoordinates([data.scannerPos[i].lng, data.scannerPos[i].lat]),
                        radius: data.scannerPos[i].r, tag: 'diff',
                        fill: {color: [color[0], color[1], color[2], 0.2]},
                        stroke: {color: color, width: 1}
                    });
                    if (circle) {
                        if (diffVisible == false) {
                            circle.hide();
                        }
                        circles.push(circle);
                    }
                }

                if(coords.length>3) {
                    coords = pnt.util.sortPolygonCoordinates(coords);
                }
            }

            var polygon = om.create(pnt.map.object.type.POLYGON, 'triang-' + data.id, {
                coords: coords, tag: 'triang', fill: {color: [color[0], color[1], color[2], 0.2]},
                stroke: {color: color, width: 1}
            });
            if (triangVisible == false) {
                polygon.hide();
            }

            var options = {
                url: data.sos == '1' ? param.prop.get('icon.beaconSos') : param.prop.get('icon.beacon'),
                timer: param.prop.get('beacon.show.time'),
                position: pnt.util.transformCoordinates([data.lng, data.lat]),
                tag: 'beacon',
                data: {triang: polygon, diff: circles, color: color, sos: data.sos},
                style: {
                    cursor: 'pointer'
                },
                labelStyle: {
                    backgroundColor: 'rgba(255, 230, 230, 0.2)'
                },
                keyword:info[2]
            };
            var marker = om.create(pnt.map.object.type.MARKER, data.id, options);
            marker.setData('UUID', info[0]);
            marker.setData('majorVer', info[1]);
            marker.setData('minorVer', info[2]);
            marker.setData('floor', data.floor);
            marker.setData('sos', data.sos);
            marker.setData('creationDate', data.creationDate);
            marker.setData('lastTime', data.lastTime);
            marker.setData('lastTime2', data.lastTime2);
            marker.setData('redis', data.redis);
			marker.setData('barcode', data.barcode);
			marker.setData('externalId', data.externalId);
			marker.setData('externamAttribute', data.externamAttribute);

            var img = marker.getElement();
            img.style.width = '10px';
            img.style.height = '10px';

			/**
			 * SOS 신호가 있을 경우 마커 아이콘 변경
			 */
			if (data.sos=='1') {
				marker.changeIcon(module.prop.get('icon.beaconSos'));
			}

			/**
			 * 마커 레이블 설정
			 */
            if (param.prop.get('marker.label') == true) {
				if(typeof(data.externalId)!='undefined' && data.externalId!=null && data.externalId!='') {
					//marker.showLabel(data.externalId+'('+info[2]+')');
					marker.showLabel(data.externalId);
				} else {
					marker.showLabel(info[2]);
				}
            }

			/**
			 * 마커 클릭스 강조 스타일 설정
			 */
            if (param.prop.get('marker.focus.border')) {
                marker.setFocusStyle({
                    'border': param.prop.get('marker.focus.border'),
                    'borderRadius': param.prop.get('marker.focus.borderRadius'),
                    'WebkitBoxSizing': 'content-box',
                    'MozBoxSizing': 'content-box',
                    'boxSizing': 'content-box'
                });
            }

            var tooltip = marker.getTooltip();
            tooltip.setData('marker', marker);
            tooltip.setData('UUID', info[0]);
            tooltip.setData('majorVer', info[1]);
            tooltip.setData('minorVer', info[2]);
            tooltip.setContent('...');

			/**
			 * 외부 연동 데이터가 있을 경우 툴팁으로 설정
			 */
			if(typeof(data.externamAttribute)!='undefined' && data.externamAttribute!=null
				&& data.externamAttribute!='' & data.externamAttribute!='[]') {

				(function() {
					var json = data.externamAttribute;
					var html = '<table>';
					for(var i=0; i<json.length; i++) {
						//var key = json[i].key;
						var val = json[i].value;
						var displayName = json[i].displayName;
						html += '<tr><td style="font-size:9pt;width:70px;">'+displayName+'</td><td style="font-size:9pt;">|&nbsp;</td>'
							+ '<td style="font-size:9pt;">'+(val||'')+'</td></tr>';
					}
					html += '</table>';
					tooltip.setContent(html);
					tooltip.setData('originalContent', tooltip.getContent());
				})();
			} else {
				tooltip.setContent('Minor Version : '+data.minorVer);
				tooltip.setData('originalContent', tooltip.getContent());
			}



            if (param.prop.get('marker.click.tooltip') == true && tooltip.isCloseButtonClick() != true && data.sos == '1') {
                marker.showTooltip();
            }
            marker.on('click', function (event) {
                console.log(event);

                pntmap.getObjectManager().findTag('beacon', function (object) {
                    object.unfocus();
                });
                this.focus();

                if (param.prop.get('marker.click.tooltip') == true) {
                    this.showTooltip();

                    var lastTooltip = param.prop.get('tooltip.last');
                    if(lastTooltip) {
                        lastTooltip.setZindex(100);
                    }
                    this.getTooltip().setZindex(101);
                    param.prop.put('tooltip.last', this.getTooltip());
                }

                console.debug('click marker', this.getData());
            });

            if(param.prop.get('map.debug')==true && data.redis==true) {
                var mouseRightDownEventId = marker.on('mousedown', function (event) {
                    if(event.which==3) {
                        pntmap.getContextMenu().show(this.getPosition(), {
                            01: {text: '제거', callback: pnt.util.bind(this, function() {
                                if(window.confirm('정말로 제거하시겠습니까?')) {
                                    var key = 'PRESENCE_BEACON_'
                                        +param.prop.get('uuid')+'_'+param.prop.get('uuid')+'_'
                                        +marker.getData('majorVer')+'_'+marker.getData('minorVer');

                                    pnt.util.fetch({
                                        url: '/tracking/presence/scanner/removeRedisItem.do?key='+key,
                                        method: 'get',
                                        responseType: 'json',
                                        success: pnt.util.bind(this, function (json) {
                                            if(json.success!='1') {
                                                window.alert('실패하였습니다.');
                                            }
                                        }),
                                        fail: function() {
                                            window.alert('실패하였습니다.');
                                        }
                                    });

                                }
                            })}
                        });
                    }
                });
                marker.setData('menu.event.id', mouseRightDownEventId);
            }

			module.notiProp.trigger(info[0]+'_'+info[1]+'_'+info[2], 'put');
			module.notiProp.trigger(info[0]+'_'+info[1]+'_'+info[2]+'-last', 'put');

            if (data.showMarker == '0') {
                marker.setTimer('remove', 100);
            } else {
                marker.setTimer('remove', param.prop.get('beacon.show.time'));
            }

            module.dispatch('createMarker', {marker: marker, data: data});
        }
    }

	this.updateNotification = function(data) {
		var commandTypeName = {
			'FLOOR_IN':'층 입장',
			'FLOOR_OUT':'층 퇴장',
			'RESTRICTION_IN':'비인가 입장', // ok
            'RESTRICTION_STAY':'비인가 체류', // ok
			'RESTRICTION_OUT':'비인가 퇴장',
			'GEOFENCE_IN':'지오펜스 입장',
			'GEOFENCE_OUT':'지오펜스 입장',
			'SOS_ON':'SOS 발생', // ok
			'SOS_OFF':'SOS 종료', // ok
			'LOST_SIGNAL':'신호상실', //ok
		};
		var zoneTypeName = {'floor':'층','geofence':'지오펜스','logicalarea':'논리구역'};

		var beaconInfo = data.beaconInfo;
		var uuid = beaconInfo.uuid;
		var majorVer = beaconInfo.majorVer;
		var minorVer = beaconInfo.minorVer;
		var id = uuid+'_'+majorVer+'_'+minorVer;


		/**
		 * 마커 툴팁 업데이트
		 */
		var notiData = {};
		if(!module.notiProp.has(id)) {
			module.notiProp.on(id, pnt.util.Properties.eventType.PUT, function(id, value) {
				if(value) {
					var marker = module.pntmap.getObjectManager().get(pnt.map.object.type.MARKER, id);
					if(marker) {
						var additionalToolMsg= '';
						var keys = ['floor', 'geofence', 'restriction'];
						for(var i=0; i<keys.length; i++) {
							if(value[keys[i]]) {
								var interfaceCommandType = value[keys[i]].interfaceCommandType;
								var eventZone = value[keys[i]].eventZoneInOutState||'';
								var zoneName = eventZone.name||'';
								var zoneType = eventZone.zoneType||'';

								var commTypeValue = commandTypeName[interfaceCommandType]||'Unknow';
								var zoneTypeValue = zoneTypeName[zoneType]||'Unknow';
								if(!zoneTypeName[zoneType] || !zoneName) {
									additionalToolMsg += commTypeValue+'<br />';
								} else {
									additionalToolMsg += commTypeValue+'['+zoneTypeValue+']'+'('+zoneName+') <br />';
								}
							}
						}
						var originalContent = marker.getTooltip().getData('originalContent');
						if(!originalContent) {
							originalContent = '';
						} else {
							originalContent += '<br />';
						}
						marker.getTooltip().setContent(originalContent + additionalToolMsg);
					}
				}
			});
		} else {
			notiData = module.notiProp.get(id);
		}
		if(data.interfaceCommandType=='FLOOR_IN' || data.interfaceCommandType=='FLOOR_OUT') {
			notiData.floor = data;
			module.notiProp.put(id, notiData);
		} else if(data.interfaceCommandType=='GEOFENCE_IN' || data.interfaceCommandType=='GEOFENCE_OUT') {
			notiData.geofence = data;
			module.notiProp.put(id, notiData);
		} else if(data.interfaceCommandType=='RESTRICTION_IN' || data.interfaceCommandType=='RESTRICTION_OUT'  || data.interfaceCommandType=='RESTRICTION_STAY') {
			notiData.restriction = data;
			module.notiProp.put(id, notiData);
		}

		/**
		 * 마커 레이블 업데이트
		 */
		if(!module.notiProp.has(id+'-last')) {
			module.notiProp.on(id+'-last', pnt.util.Properties.eventType.PUT, function(id, data) {

				console.log('prop put', id, data);

				var interfaceCommandType = data.interfaceCommandType;
				var beaconInfo = data.beaconInfo;
				var eventZone = data.eventZoneInOutState||'';
				var uuid = beaconInfo.uuid;
				var majorVer = beaconInfo.majorVer;
				var minorVer = beaconInfo.minorVer;
				var id = uuid+'_'+majorVer+'_'+minorVer;
				var zoneName = eventZone.name||'';
				var zoneType = eventZone.zoneType||'';

				var marker = module.pntmap.getObjectManager().get(pnt.map.object.type.MARKER, id);
				if(marker) {
					var commTypeValue = commandTypeName[interfaceCommandType]||'Unknow';
					var zoneTypeValue = zoneTypeName[zoneType]||'Unknow';
					if(zoneTypeName[zoneType] && zoneName) {
						var msg = commTypeValue+'['+zoneTypeValue+']'+'('+zoneName+')';
					} else {
						var msg = commTypeValue;
					}

                    if(interfaceCommandType=='RESTRICTION_STAY') {
                        msg = msg + " (" + (eventZone.stayTime - eventZone.inTime) + " s)";
                    }

					var markerName = marker.getData('externalId');
					if(!markerName) {
						markerName = marker.getData('minorVer');
					}
					marker.setLabelText(markerName+' '+msg);


					if(interfaceCommandType=='LOST_SIGNAL') {
						/**
						 * 신호를 잃어도 마커 유지
						 */
						//marker.setTimer('remove', param.prop.get('beacon.show.time'));
                        marker.removeTimer('remove');
						var img = marker.getElement();
						img.style.filter = 'grayscale(1)';
						img.style.WebkitFilter = 'grayscale(1)';
						marker.setData('lostSignal', true);

					}
					/* SOS 아이콘 변경은 updateMaker 이벤트에서 처리
					else if(interfaceCommandType=='SOS_ON' || interfaceCommandType=='SOS_OFF') {
						/!**
						 * SOS 처리
						 *!/
						if (interfaceCommandType == 'SOS_ON') {
							marker.changeIcon(module.prop.get('icon.beaconSos'));
						} else {
							marker.changeIcon(module.prop.get('icon.beacon'));
						}
					}*/
				}
			});
		}
		module.notiProp.put(id+'-last', data);
	}

    this.loadPreRedisBeacon = function(callback) {

        pnt.util.fetch({
            url: '/tracking/presence/scanner/redisBeacon.do?uuid='+param.prop.get('uuid'),
            method: 'get',
            responseType: 'json',
            success: pnt.util.bind(this, function (json) {
                try {
                    var newDatas = [];
                    for(var i=0; i<json.data.length; i++) {
                        var data = json.data[i];
                        data.scannerPos = [];

                        var lastTime = data.lastTime;
                        var lastDatetime = pnt.util.dateformat(lastTime*1000, 'yyyy/MM/dd HH:mm:ss');
                        data.lastTime2 = lastDatetime;
                        data.redis = true;

						/*if(param.prop.get('map.debug')==true) {
						 if (module.floorManager.getCurrentFloor() == data.floor) {
						 var marker = om.get(pnt.map.object.type.MARKER, data.id);
						 if(!marker) {

						 console.debug('redis pre beacon', data.id, lastDatetime, data);

						 newDatas.push(data);
						 }
						 }
						 }*/

                        this.updateMarker(data);
                    }
                    if(typeof(callback)!='undefined') {
                        callback(newDatas);
                    }
                } catch(error) {
                    console.error(error);
                }
            })
        });

    }

    this.debugRemoteMarkerInterval = function(url, getItemScript) {
        if(typeof(this._debugRemotemarkerIntervalMap)=='undefined') {
            this._debugRemotemarkerIntervalMap = [];
        }
        if(typeof(getItemScript)=='undefined') {
            getItemScript = 'data[0]';
        }
        var event = this.pntmap.getObjectManager().drawIterativeSimpleMarker(function(callback) {
            pnt.util.fetch({
                url: url,
                responseType:'json',
                success: function(response) {
                    var data = null;
                    if(getItemScript=='') {
                        data = response;
                    } else {
                        eval('data = response.'+getItemScript);
                    }

                    var coordinate = [data.lng, data.lat];
                    callback(coordinate);
                }
            });
        }, 1000);
        this._debugRemotemarkerIntervalMap.push(event);
    }
    this.debugClearRemoteMarkerInterval = function() {
        if(typeof(this._debugRemotemarkerIntervalMap)!='undefined') {
            for(var i=0; i<this._debugRemotemarkerIntervalMap.length; i++) {
                this._debugRemotemarkerIntervalMap[i].remove();
            }
            this._debugRemotemarkerIntervalMap = [];
        }

    }

    this.onBotCoordinate = function(id, nextCoordinate, botData, bot) {
        var url = module.prop.get('url.api.server') + '/presence/send/' + module.prop.get('uuid');
        //var url = module.prop.get('url.api.server') + '/presence/send/ism';
		var lonlat = pnt.util.transformLonLat(nextCoordinate.coordinate);
        var data = {
            UUID: module.prop.get('uuid'), // SUUID
            id: module.prop.get('uuid') + '_' + botData.majorVer + '_' + botData.minorVer,
			pos:[{lat: lonlat[1], lng: lonlat[0]}],
            floor: module.floorManager.getCurrentFloor(),
            sos: 0,
            battLevel: 100,
            scannerPos: []
        }
        if(typeof(botData.additionalAttr)!='undefined') {
            for(var key in botData.additionalAttr) {
                data[key] = botData.additionalAttr[key];
            }
        }
        if(typeof(nextCoordinate.data.send)=='undefined' || nextCoordinate.data.send==true) {
            pnt.util.fetch({url: url, data: data, method: 'post', bodyType: 'json'});
        }

        var om = module.pntmap.getObjectManager();
        var marker = om.get(pnt.map.object.type.MARKER, 'bot-' + id);
        if (marker) {
            marker.move(nextCoordinate.coordinate);
        } else {
            var options = {
                position: nextCoordinate.coordinate,
                tag: 'simple-marker'
            };

            var marker = om.create(pnt.map.object.type.MARKER, 'bot-' + id, options);
            marker.getElement().style.cursor = 'pointer';
            marker.setData('bot', this);
            marker.on('mousedown', function (event) {
                if (event.which == 3) {
                    pntmap.getContextMenu().show(this.getPosition(), {
                        01: {text: bot.started() == true ? '중지' : '시작',
                            callback: pnt.util.bind(this, function (menu) {
                                if (bot.started() == true) {
                                    bot.stop();
                                } else {
                                    bot.start();
                                }
                            })
                        },
                        02: {text: '재시작', callback: pnt.util.bind(this, function(menu) {
                            bot.restart();
                        })}
                    });
                }
            });
        }
    }



    if(typeof(param.prop)=='undefined') {
        param.prop = new pnt.util.Properties();
        param.prop.put('marker.lable', true);
        param.prop.put('map.visible.node.beacon', false);
        param.prop.put('map.visible.node.scanner', false);
        param.prop.put('map.visible.scanner', false);
        param.prop.put('map.visible.area', false);
        param.prop.put('map.visible.geofence', false);
        param.prop.put('map.visible.triangulation', false); // 삼각측
        param.prop.put('map.visible.differentialpos', false); // 상대위치결정
    }
    param.prop.put('map.cursor.debug.majorver', 40032);
    param.prop.put('map.cursor.debug.minorver', 60004);
    param.prop.put('map.cursor.mode', 1);

	// 비콘 신호 필터링
	param.prop.put('debug.filter.minorVer', pnt.util.getUrlParameter('filter')||false);

	var paramMap = pnt.util.getUrlParameterMap();
	for(var key in paramMap) {
		if(typeof(key)=='string' && key.length>5 && key.substring(0,5)=='prop.') {
			var val = paramMap[key];
			if(val=='true') {
				val = true;
			} else if(val=='false') {
				val = false;
			}
			param.prop.put(key.substring(5), val);
		}
	}


	this.prop = param.prop;

    var bgcolor = this.prop.get('bgcolor');
    if(bgcolor) {
        document.body.style.backgroundColor = bgcolor;
    }


    this.prop.on('map.cursor.mode', 'modify', function(id, value) {
        if (value==2) {
            pntmap.setCursorLabel('좌표 전송(일반)');
        } else if (value==3) {
            pntmap.setCursorLabel('좌표 전송(SOS)');
        } else {
            pntmap.setCursorLabel(null);
        }
    });
    this.prop.on('map.visible.triangulation', 'modify', function(id, value) {
        var polygons = pntmap.getObjectManager().findTag('triang');
        for(var i=0; i<polygons.length; i++) {
            if(value==true) {
                polygons[i].show();
            } else {
                polygons[i].hide();
            }
        }
    });
    this.prop.on('map.visible.differentialpos', 'modify', function(id, value) {
        var polygons = pntmap.getObjectManager().findTag('diff');
        for(var i=0; i<polygons.length; i++) {
            if(value==true) {
                polygons[i].show();
            } else {
                polygons[i].hide();
            }
        }
    });
    this.prop.on('map.visible.scanner', 'modify', function(id, value) {
        var scanners = pntmap.getObjectManager().findTag('scanner');
        for(var i=0; i<scanners.length; i++) {
            if(value==true) {
                scanners[i].show();
            } else {
                scanners[i].hide();
            }
        }
    });
    this.prop.on('map.visible.geofence', 'modify', function(id, value) {
        var geofences = pntmap.getObjectManager().findTag('geofence');
        for(var i=0; i<geofences.length; i++) {
            if(value==true) {
                geofences[i].show();
            } else {
                geofences[i].hide();
            }
        }
    });
    this.prop.on('map.visible.node.scanner', 'modify', function(id, value) {
        var nodes = pntmap.getObjectManager().findTag('node.scanner');
        for(var i=0; i<nodes.length; i++) {
            if(value==true) {
                nodes[i].show();
            } else {
                nodes[i].hide();
            }
        }
        var nodeEdges = pntmap.getObjectManager().findTag('nodeedge.scanner');
        for(var i=0; i<nodeEdges.length; i++) {
            if(value==true) {
                nodeEdges[i].show();
            } else {
                nodeEdges[i].hide();
            }
        }
    });
    this.prop.on('map.visible.area', 'modify', function(id, value) {
        var areas = pntmap.getObjectManager().findTag('area');
        for(var i=0; i<areas.length; i++) {
            if(value==true) {
                areas[i].show();
            } else {
                areas[i].hide();
            }
        }
    });





    var pntmap = new pnt.map.OfflineMap('map-canvas', {
        usetile: prop.get('maptile')==false?false:true,
        control: prop.get('control')==false?false:true,
        dragPan: prop.get('map.debug'),
        doubleClickZoom: prop.get('map.debug'),
        mouseWheelZoom: prop.get('map.debug'),
        maxZoom: prop.get('maxZoom') || 22,
        minZoom: prop.get('minZoom') || 15,
        zoom: prop.get('zoom') || 20
    });
    this.pntmap = pntmap;
    this.pntmap.on('click', function(evt) {
		var lonlat = pnt.util.transformLonLat(evt.coordinate);

        var mode = param.prop.get('map.cursor.mode');
        if(mode==2 || mode==3) {
			var url = module.prop.get('url.api.server') + '/presence/send/' + module.prop.get('uuid');
            //var url = module.prop.get('url.api.server')+'/presence/send/ism';
            var majorVer = param.prop.get('map.cursor.debug.majorver')
            var minorVer = pnt.util.getUrlParameter('clickMinorVer')
            if(typeof(minorVer)=='undefined' || minorVer==null || minorVer==false) {
                minorVer = param.prop.get('map.cursor.debug.minorver')
            }


            var data = {
                UUID: module.prop.get('uuid'), // SUUID
                id: module.prop.get('uuid')+'_'+majorVer+'_'+minorVer,
				pos:[{lat: lonlat[1], lng: lonlat[0]}],
                floor: module.floorManager.getCurrentFloor(),
                sos: mode==3?1:0,
                battLevel: 100,
                scannerPos: []
            }
            //var contentType = 'application/x-www-form-urlencoded';
            //pnt.util.fetch({url: url, data: data, method: 'post', contentType: contentType, bodyType:'json'});
            pnt.util.fetch({url: url, data: data, method: 'post', bodyType:'json'});
        }
    });
    this.pntmap.on('marker.timer', function(evt) {
        if(evt.object.containsTag('beacon') && evt.timerId=='remove') {
            var circles = evt.object.getData('diff');
            var om = pntmap.getObjectManager();
            if(circles) {
                for(var i=0; i<circles.length; i++) {
                    om.remove(pnt.map.object.type.CIRCLE, circles[i].getId());
                }
            }

            var polygon = evt.object.getData('triang');
            if(polygon) {
                om.remove(pnt.map.object.type.POLYGON, polygon.getId());
            }
            om.remove(pnt.map.object.type.MARKER, evt.object.getId());
        }
    });
    this.pntmap.on('tooltip.click', function(evt) {
        var lastTooltip = prop.get('tooltip.last');
        if(lastTooltip) {
            lastTooltip.setZindex(100);
        }
        evt.object.setZindex(101);
        prop.put('tooltip.last', evt.object);
    });
    this.pntmap.on('polygon.rightclick', function(evt) {
        if(evt.object.containsTag('polygon-import')) {
            pntmap.getContextMenu().show(evt.coordinate, {
                01: {text: '제거', callback: pnt.util.bind(this, function() {
                    this.getObjectManager().remove(pnt.map.object.type.POLYGON, evt.object.getId());
                })}
            });
        }
    });

    this.defaultMenu = {
        SV00: {text: '커서모드', submenu: {
            SM01: {
                text: '일반', callback: function () {
                    prop.put('map.cursor.mode', 1);
                }
            },
            SM02: {text: '좌표 전송(일반)', callback: function() {

                var form = new pnt.ui.builder.Form({id:'form',
                    field: {
                        majorVer: { type: 'text', label: 'majorVer', value: param.prop.get('map.cursor.debug.majorver'), required: true},
                        minorVer: { type: 'text', label: 'minorVer', value: param.prop.get('map.cursor.debug.minorver'), required: true}
                    }
                });
                var button = new pnt.ui.builder.Button({text:'확인', data:{form:form}});
                button.on('click' , function() {
                    var majorVer = this.get('form').getFormData().majorVer;
                    var minorVer = this.get('form').getFormData().minorVer;
                    param.prop.put('map.cursor.debug.majorver', majorVer);
                    param.prop.put('map.cursor.debug.minorver', minorVer);
                    pntmap.getPopup().hide();
                    prop.put('map.cursor.mode', 2);
                });
                pntmap.getPopup().title('좌표 전송 - major version, minor version 값 설정');
                pntmap.getPopup().setBodyFoot({body:form.getElement(),foot:button.getElement()});
                pntmap.getPopup().show();
            }
            },
            SM03: {text: '좌표 전송(SOS)', callback: function() {
                var form = new pnt.ui.builder.Form({id:'form',
					field: {
						majorVer: { type: 'text', label: 'majorVer', value: param.prop.get('map.cursor.debug.majorver'), required: true},
						minorVer: { type: 'text', label: 'minorVer', value: param.prop.get('map.cursor.debug.minorver'), required: true}
					}
				});
                var button = new pnt.ui.builder.Button({text:'확인', data:{form:form}});
                button.on('click' , function() {
                    var majorVer = this.get('form').getFormData().majorVer;
                    var minorVer = this.get('form').getFormData().minorVer;
                    param.prop.put('map.cursor.debug.majorver', majorVer);
                    param.prop.put('map.cursor.debug.minorver', minorVer);
                    pntmap.getPopup().hide();
                    prop.put('map.cursor.mode', 3);
                });
                pntmap.getPopup().title('좌표 전송 - major version, minor version 값 설정');
                pntmap.getPopup().setBodyFoot({body:form.getElement(),foot:button.getElement()});
                pntmap.getPopup().show();
            }
            }
        }
        },
        SV01: {text: prop.get('map.visible.triangulation')==true?'상대위치결정 감추기':'상대위치결정 보기', callback: function(menu) {
            prop.put('map.visible.triangulation', !prop.get('map.visible.triangulation'));
            menu.setText(prop.get('map.visible.triangulation')?'상대위치결정 감추기':'상대위치결정 보기');
        }},
        SV02: {text: prop.get('map.visible.differentialpos')==true?'삼각측량 감추기':'삼각측량 보기', callback: function(menu) {
            prop.put('map.visible.differentialpos', !prop.get('map.visible.differentialpos'));
            menu.setText(prop.get('map.visible.differentialpos')?'삼각측량 감추기':'삼각측량 보기');
        }},
        SV03: {text:'보기', submenu: {
            VI01: {text: prop.get('map.visible.scanner')==true?'스캐너 감추기':'스캐너 보기', callback: function(menu) {
                prop.put('map.visible.scanner', !prop.get('map.visible.scanner'));
                menu.setText(prop.get('map.visible.scanner')?'스캐너 감추기':'스캐너 보기');
            }},
            VI02: {text: prop.get('map.visible.geofence')==true?'지오펜스 감추기':'지오펜스 보기', callback: function(menu) {
                prop.put('map.visible.geofence', !prop.get('map.visible.geofence'));
                menu.setText(prop.get('map.visible.geofence')?'지오펜스 감추기':'지오펜스 보기');
            }},
			/*VI03: {text: prop.get('map.visible.area')==true?'구역 감추기':'구역 보기', callback: function(menu) {
			 prop.put('map.visible.area', !prop.get('map.visible.area'));
			 menu.setText(prop.get('map.visible.area')?'구역 감추기':'구역 보기');
			 }},*/
            VI04: {text: prop.get('map.visible.node.scanner')==true?'스캐너노드 감추기':'스캐너노드 보기', callback: function(menu) {
                prop.put('map.visible.node.scanner', !prop.get('map.visible.node.scanner'));
                menu.setText(prop.get('map.visible.node.scanner')?'스캐너노드 감추기':'스캐너노드 보기');
            }}
        }},
        SV12: {text:'좌표가져오기', submenu: {
            RM01: {text: '점 추가', callback: function(menu) {
                var form = new pnt.ui.builder.Form({id:'form',
                    field: {
                        url: { type: 'text', label: 'URL', value: '', required: true},
                        parse: { type: 'text', label: '결과 파싱', value: 'data[0]', required: true}
                    }
                });
                var button = new pnt.ui.builder.Button({text:'확인', data:{form:form}});
                button.on('click' , function() {
                    var url = this.get('form').getFormData().url;
                    var parse = this.get('form').getFormData().parse;
                    pnt.module.get('presence.map').debugRemoteMarkerInterval(url, parse);
                    pntmap.getPopup().hide();
                });
                pntmap.getPopup().title('점 좌표가져오기');
                pntmap.getPopup().setBodyFoot({body:form.getElement(),foot:button.getElement()});
                pntmap.getPopup().show();
            }},
            RM02: {text: '구역 추가', callback: function(menu) {
                var form = new pnt.ui.builder.Form({id:'form',
                    field: {
                        url: { type: 'text', label: 'URL', value: '', required: true},
                        parse: { type: 'text', label: '결과 파싱', value: 'data[0]', required: true}
                    }
                });
                var button = new pnt.ui.builder.Button({text:'확인', data:{form:form}});
                button.on('click' , function() {
                    var url = this.get('form').getFormData().url;
                    var parse = this.get('form').getFormData().parse;

                    pnt.util.fetch({
                        url: url,
                        responseType:'json',
                        success: function(response) {
                            var data = null;
                            if(parse=='') {
                                data = response;
                            } else {
                                eval('data = response.'+parse);
                            }
                            if(typeof(data)=='string') {
                                data = JSON.parse(data);
                            }

                            for(var i=0; i<data.length; i++) {
                                if(data[i].floor==module.floorManager.getCurrentFloor()) {
                                    var latlngs = data[i].latlngs;
                                    var coordinates = [];
                                    for(var j=0; j<latlngs.length; j++) {
                                        coordinates.push(pnt.util.transformCoordinates([latlngs[j].lng, latlngs[j].lat]));
                                    }
                                    var id = 'polygon-import-'+pnt.util.makeid(20);
                                    var geofence = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, id, {
                                        label: '', data: {}, coords:coordinates,
                                        tag:'polygon-import', fill:{color:[0,191,255,0.1]}, stroke:{color:[0,191,255,0.5],width:1}
                                    });
                                }
                            }
                        }
                    });

                    pntmap.getPopup().hide();
                });
                pntmap.getPopup().title('구역 좌표가져오기');
                pntmap.getPopup().setBodyFoot({body:form.getElement(),foot:button.getElement()});
                pntmap.getPopup().show();
            }},
            RM03: {text: '초기화', callback: function(menu) {
                pnt.module.get('presence.map').debugClearRemoteMarkerInterval();
                var polygons = pntmap.getObjectManager().findTag('polygon-import');
                for(var i=0; i<polygons.length; i++) {
                    pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, polygons[i].getId());
                }
            }}
        }},
        SV13: {text:'좌표봇 추가', callback: function(menu) {

            var form = new pnt.ui.builder.Form({id:'form', field: {
                majorVer: {type: 'text', label: 'MajorVer', value: '100', required: true},
                minorVer: {type: 'text', label: 'MinorVer', value: '1', required: true},
                errorRangeMeter: {type: 'text', label: '오차(미터)', value: '1', required: true},
                type: {type: 'select', label: '종류', options:{1:'랜덤',2:'경로'}, value: '1', required: true},
                additionalAttr: {type: 'textarea', label: '추가 속성', value: '', required: false},
            }
            });
            var buttons = [];
            buttons[0] = new pnt.ui.builder.Button({text:'추가', data:{form:form}, click: function() {
                var formData = this.get('form').getFormData();
                var majorVer = formData.majorVer;
                var minorVer = formData.minorVer;
                var errorRangeMeter = formData.errorRangeMeter;
                var targetType = formData.type;
                var botParam = {majorVer: majorVer, minorVer: minorVer, additionalAttr:{}};
                var additionalAttr = formData.additionalAttr;
                if(typeof(additionalAttr)=='string' && additionalAttr!='') {
                    var attr = {};
                    try {
                        attr = JSON.parse(additionalAttr);
                    } catch(error) {
                        console.error(error, additionalAttr);
                        window.alert('추가 속성은 JSON 포맷으로 입력해 주세요.');
                        return;
                    }
                    for(var key in attr) {
                        botParam.additionalAttr[key] = attr[key];
                    }
                }

                pntmap.getPopup().hide();

                if(targetType=='1') {
                    var draw = new ol.interaction.Draw({
                        type: 'Circle',
                        source: new ol.source.Vector({wrapX: false}),
                        geometryFunction: ol.interaction.Draw.createBox()
                    });
                    draw.on('drawend', function (evt) {
                        pntmap.getOlMap().removeInteraction(draw);
                        var extent = evt.feature.getGeometry().getExtent();

                        var bot = new pnt.map.CoordinateBot(pnt.util.makeid(10), extent, botParam, module.onBotCoordinate,
                            1000, pntmap);
                        bot.setErrorRange(errorRangeMeter);
                        bot.setAutoTarget(true);
                        bot.start();
                    });
                    pntmap.getOlMap().addInteraction(draw);
                } else {

                    var pathlines = pntmap.getObjectManager().findTag('bot.path.line');
                    for(var i=0; i<pathlines.length; i++) {
                        pntmap.getObjectManager().remove(pnt.map.object.type.LINE, pathlines[i].getId());
                    }
                    var pathpoints = pntmap.getObjectManager().findTag('bot.path.point');
                    for(var i=0; i<pathpoints.length; i++) {
                        pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, pathpoints[i].getId());
                    }

                    var draw = new ol.interaction.Draw({
                        type: 'LineString',
                        source: new ol.source.Vector({wrapX: false})
                    });
                    draw.on('drawend', function (evt) {
                        pntmap.getOlMap().removeInteraction(draw);

                        var coordinates = evt.feature.getGeometry().getCoordinates();
						var lonlats = [];
						for(var i=0; i<=coordinates.length-1; i++) {
							lonlats.push(pnt.util.transformLonLat(coordinates[i]));
						}
                        var options = {
                            data: {}, coords:lonlats, tag:'bot.path.line',
                            stroke:{color:[0, 153, 255,1],width:2}
                        }
                        var id = 'bot.path.line-'+pnt.util.makeid(10);
                        var pathline = pntmap.getObjectManager().create(pnt.map.object.type.LINE, id, options);
                        var pathpoint = [];
                        var table = new pnt.ui.builder.Table();
                        table.setHeader(['','속도(m/s)','목적지 유지시간(sec)','전송']);
                        for(var i=1; i<coordinates.length; i++) {
                            var input1 = new pnt.ui.builder.Input({id:'speed-'+i,value:'1',style:{width:'80px'}});
                            var input2 = new pnt.ui.builder.Input({id:'stay-'+i,value:'5',style:{width:'80px'}});
                            var useSend = new pnt.ui.builder.Checkbox({id:'send-'+i,value:'1',checked:true});
                            table.addRow(['좌표'+i,input1.getElement(), input2.getElement(), useSend.getElement()]);

                            //
                            var id = 'bot.path.point-'+i;
                            var options = {
                                position: coordinates[i], label:'좌표 '+i, data: {},
                                tag:'bot.path.point',
                                labelStyle: {
                                    backgroundColor:'rgba(230, 255, 230, 0.8)'
                                }
                            };
                            pathpoint.push(pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, id, options));
                        }

                        var button = new pnt.ui.builder.Button({text:'확인', data:{table:table}});
                        button.on('click' , function() {
                            var extent = module.floorManager.getImage().getExtent();
                            var bot = new pnt.map.CoordinateBot(pnt.util.makeid(10), extent, botParam,
                                module.onBotCoordinate, 1000, pntmap);
                            for(var i=0; i<coordinates.length; i++) {

                                if(i>0) {
                                    var speed = document.getElementById('speed-' + i).value;
                                    var stay = document.getElementById('stay-' + i).value;
                                    var send = document.getElementById('send-' + i).checked;
                                    bot.generateTargetCoordinate(coordinates[i], speed, stay, {send:send}, false);
                                } else {
                                    bot.generateTargetCoordinate(coordinates[i], 1, 1, {send:true}, false);
                                }
                            }

                            pntmap.getObjectManager().remove(pnt.map.object.type.LINE, pathline.getId());
                            for(var i=0; i<pathpoint.length; i++) {
                                pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, pathpoint[i].getId());
                            }
                            bot.setErrorRange(errorRangeMeter);
                            bot.setAutoTarget(false);
                            bot.start();
                            pntmap.getPopup().hide();
                        });

                        pntmap.getPopup().title('좌표봇 - 경로설정');
                        pntmap.getPopup().setBodyFoot({body:table.getElement(),foot:button.getElement()});
                        pntmap.getPopup().show();
                    });
                    pntmap.getOlMap().addInteraction(draw);
                }
            }});

            pntmap.getPopup().title('좌표 봇 추가');
            pntmap.getPopup().setBodyFoot({body:form.getElement(),foot:buttons[0].getElement()});
            pntmap.getPopup().show();
        }},
        SV14: {text:'위경도 좌표', callback: function(menu) {
            var tooltip = pntmap.getTooltip();
            var position = this.getPosition();
			var lonlat = pnt.util.transformLonLat(this.getPosition());
            tooltip.setContent('<div>위도: '+lonlat[1]+'</div><div>경도: '+lonlat[0]+'</div>');
            tooltip.show(position);
        }}
		/*SV15: {text:'test', callback: function(menu) {

		 }}*/
    };




    /**
     * Debug 모드에서 컨텍스트 메뉴 활성화
     */
    if(this.prop.get('map.debug')==true) {
        this.pntmap.getContextMenu().setDefaultMenu(this.defaultMenu);
    }



    var dataEvent = {
        scanner: new pnt.util.DataEvent('scanner'),
        geofence: new pnt.util.DataEvent('geofence'),
		/*area: new pnt.util.DataEvent('area'),*/
        nodeScanner: new pnt.util.DataEvent('nodeScanner'),
        nodeEdgeScanner: new pnt.util.DataEvent('nodeEdgeScanner'),
		notification: new pnt.util.DataEvent('notification')
    }
	this.dataEvent = dataEvent;

    dataEvent.scanner.on('load', function(data) {
        var coordinates = pnt.util.transformCoordinates([data.get('lng'),data.get('lat')]);
        var id = data.get('scannerNum');
        var options = {
            position: coordinates, label:data.get('scannerName'), data: data,
            tag:'scanner', url: prop.get('icon.scanner'),
            labelStyle: {
                backgroundColor:'rgba(230, 255, 230, 0.8)'
            }
        };
        var scanner = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'scanner-'+id, options);
        var visible = prop.get('map.visible.scanner');
        if(visible==false) {
            scanner.hide();
        }
    });
    dataEvent.scanner.on('unload', function(data) {
        pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'scanner-'+data.get('scannerNum'));
    });
    dataEvent.geofence.on('load', function(data) {
        var latlngs = data.get('latlngs');
        var coords = [];
        if(typeof(latlngs)!='undefined' && latlngs!=null) {
            for (var i = 0; i < latlngs.length; i++) {
                coords.push(pnt.util.transformCoordinates([latlngs[i].lng, latlngs[i].lat]));
            }
        }

        var label = data.get('fcName');
        if(data.get('groupNames')!='') {
            label += '('+data.get('groupNames')+')';
        }
        var geofence = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, 'fence-'+data.get('fcNum'), {
            label: label, data: data, coords:coords,
            tag:'geofence', fill:{color:[255,0,0,0.1]}, stroke:{color:[255,0,0,0.5],width:1}
        });
        var visible = prop.get('map.visible.geofence');
        if(visible==false) {
            geofence.hide();
        }

    });
    dataEvent.geofence.on('unload', function(data) {
        pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, 'fence-'+data.get('fcNum'));
    });
	/*dataEvent.area.on('load', function(data) {

	 var latlngs = data.get('latlngs');
	 var coords = [];
	 if(typeof(latlngs)!='undefined' && latlngs!=null) {
	 for(var i=0;i<latlngs.length; i++) {
	 coords.push([latlngs[i].lng, latlngs[i].lat]);
	 }
	 }

	 var area = pntmap.getObjectManager().create(pnt.map.object.type.POLYGON, 'area-'+data.get('areaNum'), {
	 label: data.get('areaName'), data: data,
	 coords:coords, tag:'area', fill:{color:[147, 67, 251,0.1]}, stroke:{color:[147, 67, 251,0.5],width:1}
	 });
	 var visible = prop.get('map.visible.area');
	 if(visible==false) {
	 area.hide();
	 }
	 });
	 dataEvent.area.on('unload', function(data) {
	 pntmap.getObjectManager().remove(pnt.map.object.type.POLYGON, 'area-'+data.get('areaNum'));
	 });*/
    dataEvent.nodeScanner.on('load', function(data) {

        var coordinates = pnt.util.transformCoordinates([data.get('lng'),data.get('lat')]);
        var options = {
            position: coordinates, data:data, label: data.get('areaName'),
            tag:'node.scanner', url: prop.get('icon.nodeScanner'),
            labelStyle: {
                backgroundColor:'rgba(255, 230, 230, 0.8)'
            }
        };
        var node = pntmap.getObjectManager().create(pnt.map.object.type.VMARKER, 'node.scanner-'+data.get('nodeID'), options);
        var visible = prop.get('map.visible.node.scanner');
        if(visible==false) {
            node.hide();
        }

    });
    dataEvent.nodeScanner.on('unload', function(data) {
        pntmap.getObjectManager().remove(pnt.map.object.type.VMARKER, 'node.scanner-'+data.get('nodeID'));
    });
    dataEvent.nodeEdgeScanner.on('load', function(data) {

        var startMarker = pntmap.getObjectManager().get('vmarker','node.scanner-'+data.get('startPoint'));
        var endMarker = pntmap.getObjectManager().get('vmarker','node.scanner-'+data.get('endPoint'));

        data.setData({
            startMarker: startMarker,
            endMarker: endMarker
        });

        var startPos = startMarker.getPosition();
        var endPos = endMarker.getPosition();
        if(startPos && endPos) {
            var options = {
                data: data,
                coords:[startPos, endPos], tag:'nodeedge.scanner', stroke:{color:[210, 41, 41,1],width:1}
            }
            var edge = pntmap.getObjectManager().create(pnt.map.object.type.LINE, 'nodeedge.scanner-'+data.get('edgeNum'), options);
            var visible = prop.get('map.visible.node.scanner');
            if(visible==false) {
                edge.hide();
            }

            startMarker.getData().get('lines', []).push(edge);
            endMarker.getData().get('lines', []).push(edge);
        }
    });
    dataEvent.nodeEdgeScanner.on('unload', function(data) {
        pntmap.getObjectManager().remove(pnt.map.object.type.LINE, 'nodeedge.scanner-'+data.get('edgeNum'));
    });



    var dataLoader = new pnt.util.DataLoader();
    dataLoader.addUrl('scannerList', this.prop.get('url.scanner'));
    dataLoader.addUrl('nodeScannerList', this.prop.get('url.node'), {type:'S'});
    dataLoader.addUrl('nodeEdgeScannerList', this.prop.get('url.nodeedge'), {type:'S'});
    dataLoader.addUrl('geofenceList', this.prop.get('url.geofence'));


	/*var areaManager = new pnt.map.AreaManager(pntmap);*/
    var floorManager = new pnt.map.FloorManager(pntmap, {
        defaultFloor: prop.get('floor')||null, autofit: prop.get('floor.autofit')
    });
    this.floorManager = floorManager;
	/*areaManager.setFloorManager(floorManager);*/

    var floorDataLoader = new pnt.util.DataLoader();
    floorDataLoader.addUrl('floorList', prop.get('url.floor'));
	/*floorDataLoader.addUrl('areaList', prop.get('url.area'));*/
    floorDataLoader.load(function(id, data, complete, error) {
        if(complete==true) {
            var floorData = this.getData('floorList').data;

            floorManager.load(floorData);
            floorManager.onChange(function(event) {
                dataEvent.scanner.unload();
                dataEvent.geofence.unload();
				/*dataEvent.area.unload();*/
                dataEvent.nodeScanner.unload();
                dataEvent.nodeEdgeScanner.unload();

                var floorCondition = {floor: event.floor};
				/*if(floorDataLoader.getData('areaList').result=='1') {
				 var areaData = pnt.util.findArrayData(floorDataLoader.getData('areaList').data, floorCondition);
				 dataEvent.area.loadData(areaData);
				 }*/
                dataLoader.reload(floorCondition);
                module.clear();
                //module.loadPreRedisBeacon();

                module.dispatch('changeFloor', {floor: event.floor});
            });

			/*var areaData = this.getData('areaList').data;
			 if(typeof(areaData)!='undefined' && areaData!=null && areaData.length>0) {
			 areaManager.load(areaData);
			 } else {
			 floorManager.changeDefault();
			 }*/
            floorManager.changeDefault();

        }
    });

    if (prop.get('map.debug') == true) {
        dataLoader.onLoad(function (id, data, complete, error) {
            if (id == 'scannerList') {
                dataEvent.scanner.loadData(this.getData('scannerList').data || []);
            }
            else if (id == 'geofenceList') {
                dataEvent.geofence.loadData(this.getData('geofenceList').data || []);
            }

            if (complete == true) {
                dataEvent.nodeScanner.loadData(this.getData('nodeScannerList').data || []);
                dataEvent.nodeEdgeScanner.loadData(this.getData('nodeEdgeScannerList').data || []);
            }
        });
    } else {
        dataLoader.onLoad(function (id, data, complete, error) {
            if (id == 'scannerList') {
                dataEvent.scanner.loadData(this.getData('scannerList').data || []);
            }
            else if (id == 'geofenceList') {
                dataEvent.geofence.loadData(this.getData('geofenceList').data || []);
            }
        });
    }

    if(param.prop.get('socket.io.off')!=true) {

        var socketIo = new pnt.util.SocketIo(prop.get('socket.io.server'));
        this.channel = socketIo.channel(prop.get('socket.io.channel'));
		this.channel.on('updateMarker', function (data) {

            data.redis = false;
			console.log('updateMarker', data);
            module.updateMarker(data);

        });
		this.channel.on('notification', function (data) {

            console.log('notification', data);
			module.updateNotification(data);
        });
    }

});























