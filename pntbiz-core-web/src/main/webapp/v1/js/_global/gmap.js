/*
 * People&Technology 
 * GoogleMapV3 extend library V.0.1.3
 * Create by jhlee@pntbiz.com
 * History : 
 * 2015-02-10 V.0.0.0 최초작성
 * 2015-04-20 V.0.0.0 Overlay Object > setMethod 작성
 * 2015-04-29 V.0.0.3 Property check 추가
 * 2015-04-30 V.0.0.4 addControl type 추가 : 텍스트 / 버튼 / 콤보박스 (차후에 계속 추가...)
 * 2015-05-11 V.0.0.5 removeMarker에 opt.remove 옵션 추가 > 마커삭제될 때 실행할 함수
 * 2015-06-03 V.0.0.6 $.gmap.center()추가 : show / hide시에 center지점이 변경되는 버그 방어코드 
 * 2015-07-09 V.0.0.7 heatmap 관련 추가
 * 2015-08-13 V.0.0.8 객체 생성 시 opt.field가 0일 경우 항상 false로 인식함.(a: field || 1 일경우 항상 1이 됨.) > is함수를 사용하도록 모두 변경함.
 * 2015-10-22 V.0.0.9 IE ajax 시간 차에 대한 처리 오류 수정. $.gmap.load(func) callback 함수 추가
 * 2015-12-16 V.0.0.10 MarkerLabel속성 처리 추가, Marker Title을 사용한 MarkerLabel Extend
 * 2016-02-11 V.0.0.11 API 키 추가
 * 2016-04-19 V.0.0.12 drawPolyline icons 옵션 추가
 * 2016-07-28 V.0.1.0 addControll class, style, dom 구현 방식 변경
 * 2016-07-29 V.0.1.1 object에 bindData 필드 추가
 * 2016-08-01 V.0.1.2 addControll after, first, wrap 추가
 * 2016-08-29 V.0.1.3 drawPolygon infoWindow 추가
 */
if (!jQuery) { throw new Error("PntGmap requires jQuery"); }
(function($){
	$.gmap = {
		apikey: "AIzaSyC5flBQIVK7pcBA6IDA3MZHvIcAWPDMqPQ",
		//version: "3.22",
		lat: 0,
		lng: 0,
		zoom: 20,
		map: null,
		option: null,
		overlay: null,
		controls: [],
		overlays: [],
		layers: [],
		markers: [],
		polygons: [],
		polylines: [],
		rectangles: [],
		circles: [],
		routes: [],
		heatmaps: [],
		load: function(option) {
			if (typeof google === 'object' && typeof google.maps === 'object') {
				$.gmap.initialize();
			} else {				
				$.getScript("https://maps.googleapis.com/maps/api/js?v="+this.version+"&key="+this.apikey+"&libraries=visualization&callback=$.gmap.initialize")
				.done(function(){}).fail(function() {});
			}
			if(option != null) {
				this.option = option;
			}
		},
		initialize: function() {
			var option = {
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				zoom: this.is(this.option.zoom) ? this.option.zoom : this.zoom,
				center: new google.maps.LatLng(this.option.lat, this.option.lng),
				panControl: this.is(this.option.panControl) ? this.option.panControl : true,
				zoomControl: this.is(this.option.zoomControl) ? this.option.zoomControl : true,
				rotateControl: this.is(this.option.rotateControl) ? this.option.rotateControl : false,
				mapTypeControl: this.is(this.option.mapTypeControl) ? this.option.mapTypeControl : false,
				scaleControl: this.is(this.option.scaleControl) ? this.option.scaleControl : true,
				streetViewControl: this.is(this.option.streetViewControl) ? this.option.streetViewControl : false,
				overviewMapControl: this.is(this.option.overviewMapControl) ? this.option.overviewMapControl : false,
				bindData: this.is(this.option.bindData) ? this.option.bindData : null
				//backgroundColor: string,
				//disableDefaultUI: boolean,
				//disableDoubleClickZoom: boolean,
				//draggable: boolean,
				//draggableCursor: string,
				//draggingCursor: string,
				//heading: number,
				//keyboardShortcuts: boolean,
				//mapMaker: boolean, 
				//mapTypeControl: boolean,
				//mapTypeControlOptions: MapTypeControlOptions,				
				//maxZoom: number,
				//minZoom: number,
				//noClear: boolean,
				//overviewMapControl: boolean,
				//overviewMapControlOptions: OverviewMapControlOptions,
				//panControl: boolean,
				//panControlOptions: PanControlOptions,
				//rotateControl: boolean,
				//rotateControlOptions: RotateControlOptions,
				//scaleControl: boolean,
				//scaleControlOptions: ScaleControlOptions,
				//scrollwheel: boolean,
				//streetView: StreetViewPanorama,
				//streetViewControl: boolean,
				//streetViewControlOptions: StreetViewControlOptions,
				//styles: Array<MapTypeStyle>,
				//tilt: number,
				//zoom: number,
				//zoomControl: boolean,
				//zoomControlOptions: ZoomControlOptions
			};
			this.extendOption(this.option, option);
			this.option.div = (this.option.width == null) ? "map-canvas" : this.option.div;
			$("#"+this.option.div).css("width", (this.option.width == null) ? "100%" : this.option.width);
			$("#"+this.option.div).css("height", (this.option.height == null) ? "100%" : this.option.height);			
			this.map = new google.maps.Map(document.getElementById(this.option.div), this.option);
			var evt = ["bounds_changed", "center_changed", "click", "dblclick", "drag", "dragend", "dragstart", 
			"heading_changed", "idle", "maptypeid_changed", "mousemove", "mouseout", "mouseover", 
			"projection_changed", "resize", "rightclick", "tilesloaded", "tilt_changed", "zoom_changed"];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {					
					if(typeof($.gmap.option[name]) != "undefined") {
						google.maps.event.addDomListener($.gmap.map, name, $.gmap.option[name]);
					}
				})($.gmap.map, evt[i]);
			}
			this.onload();
		},
		onload: function() {
			this.extendOverlay();
			this.extendPolygon();
			if(this.option.func != null) {
				this.option.func();
			}
			this.extendMarkerLabel();
		},
		extendOption: function(opt, opt2) {
			if (opt === opt2) return opt;
			for (var key in opt2) { opt[key] = opt2[key];}
			return opt;
		},
		replaceOption: function(opt, opt2) {
			if (opt === opt2) return opt;
			for (var key in opt2) {
				if ((typeof opt[key] != "undefined") && (opt[key] != null)) {
					opt[key] = opt2[key];
				}
			}
			return opt;
		},
		is: function(obj) {
			if(typeof(obj) == "undefined" || obj == null) {
				return false;
			} else {
				return true;
			}
		},
		inKey: function(key, arr) {
			for(k in arr) {
				if(key == k) return true;
			}
			return false;
		},
		inValue: function(val, arr) {
			for(v in arr) {
				if(val == arr[v]) return true;
			}
			return false;
		},
		checkExpTime: function(ltime, ctime) {
			// ltime : Life Time / ctime : Created Time
			var date = new Date();
			var ntime = date.getTime(); // now time
			if((Number(ltime) + Number(ctime)) < Number(ntime)) {
				return true; // expiration
			} else {
				return false;
			}
		},
		getPaths: function(opt) {
			var paths = [];
			for(var i=0; i<opt.length; i++) {
				paths.push(new google.maps.LatLng(opt[i].lat, opt[i].lng));
			}
			return paths;
		},
		getBounds: function(opt) {
			var swLatLng = new google.maps.LatLng(opt[0].lat, opt[0].lng);
			var neLatLng = new google.maps.LatLng(opt[1].lat, opt[1].lng);
			var bounds = new google.maps.LatLngBounds(swLatLng, neLatLng);
			return bounds;
		},
		getLatLng: function(opt) {
			var latLng = new google.maps.LatLng(opt.lat, opt.lng);
			return latLng;
		},
		getPoint: function(opt) {
			var point = new google.maps.Point(opt.x, opt.y);
			return point;
		},
		getSize: function(opt) {
			var size = new google.maps.Size(opt.width, opt.height, opt.widthUnit, opt.heightUnit);
			return size;
		},
		setOption: function(opt) {
			if(this.is(this.map) == true) {
				var prop = ['backgroundColor', 'center', 'disableDefaultUI', 'disableDoubleClickZoom', 'draggable', 'draggableCursor', 'draggingCursor', 
				'heading', 'keyboardShortcuts', 'mapMaker', ' mapTypeControl', 'mapTypeControlOptions', 'mapTypeId: MapTypeId', 'maxZoom', 'minZoom', 
				'noClear', 'overviewMapControl', 'overviewMapControlOptions', 'panControl', 'panControlOptions', 'rotateControl', 'rotateControlOptions', 
				'scaleControl', 'scaleControlOptions', 'scrollwheel', 'streetView', 'streetViewControl', 'streetViewControlOptions', 
				'styles', 'tilt', 'zoom', 'zoomControl', 'zoomControlOptions'];
				for(val in opt) {
					if(this.inValue(val, prop) == false) {
						this.log("Map - \""+ val +"\" property is not supported");
					}
				}
				this.map.setOptions(opt);
			} else {
				this.log("Map Does not exist");
			}
		},
		center: function() {
			// show / hide 시에 map center변경 방어코드
			google.maps.event.trigger(this.map, 'resize');
			this.map.setCenter(new google.maps.LatLng(this.option.lat, this.option.lng));
		},
		log: function(msg) {
			if(this.option.logging == true) {
				console.log(msg);
				//console.log(arguments.callee.caller);
				//console.log(arguments.callee.caller.toString().substring(0, 100));
			}
		}
	}
})(jQuery);

// control extend
$.extend($.gmap, {
	addControl: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.controls[id]) == false) {
			var div = document.createElement('div');
			// BOTTOM_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, LEFT_BOTTOM, LEFT_CENTER, 
			// LEFT_TOP, RIGHT_BOTTOM, RIGHT_CENTER, RIGHT_TOP, TOP_CENTER, TOP_LEFT, TOP_RIGHT
			$(div).attr('id', id);			
			opt.position = opt.position || "top_center";			
			if(this.is(opt.position) == true) {
				try {
				this.map.controls[google.maps.ControlPosition[opt.position.toUpperCase()]].push(div);
				} catch(err) {
					
				}
			}	
			if(this.is(opt.index) == true) {
				$(div).index = opt.index;
			}			
			this.controls[id] = div;
			var ctrl = ['text', 'button', 'combobox', 'input', 'div', 'textarea'];			
			for (var i=0; i<ctrl.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						var content;
						if(name == "text") {							
							content = document.createElement('span');
							var evt = ['click', 'dbclick', 'mousedown', 'mouseenter', 'mousemove', 'mouseout', 'mouseover', 'mouseup'];					
						}
						if(name == "combobox") {
							content = document.createElement("select");
							$.each(opt[name].option, function(idx, val){
								var option = document.createElement("option");
								$(option).attr("value", opt[name].option[idx]["value"]);
								if(opt[name].option[idx]["selected"] == true) {
									$(option).attr("selected", "selected");
								}
								var text = document.createTextNode(opt[name].option[idx]["text"]);
								$(option).append($(text));
								$(content).append($(option));						
							});
							var evt = ['change'];
						} 
						if(name == "button") {
							content = document.createElement('button');
							try {
								$(content).append(document.createTextNode(opt[name].attr.value));
							} catch(e) {
								$(content).append(document.createTextNode("button"));
							}
							var evt = ['click', 'dbclick', 'mousedown', 'mouseenter', 'mousemove', 'mouseout', 'mouseover', 'mouseup'];
						}						
						if(name == "input") {
							content = document.createElement('input');
							var evt = ['keyup', 'keydown', 'keypress', 'change'];
						}						
						if(name == "div") {
							content = document.createElement('div');
							$(content).html(opt[name].html);
							var evt = ['click', 'dbclick', 'mousedown', 'mouseenter', 'mousemove', 'mouseout', 'mouseover', 'mouseup'];							
						}
						if(name == "textarea") {
							content = document.createElement('textarea');
							var evt = ['keyup', 'keydown', 'keypress', 'change'];
						}	
						$(div).append($(content));
						for (var j=0; j<evt.length; j++) {
							(function(obj, name) {
								if(typeof(obj[name]) != "undefined") {
									$(content).bind(name, obj[name]);
								}
							})(opt[name], evt[j]);									
						}							
						try {
							for(var key in opt[name].attr) {
								$(content).attr(key, opt[name].attr[key]);
							}
						} catch(e) {}
						try {
							for(var key in opt[name].style) {
								$(content).css(key, opt[name].style[key]);
							}
						} catch(e) {}
						try {
							for(var idx in opt[name].classes) {
								$(content).addClass(opt[name].classes[idx]);
							}
						} catch(e) {}
						try {
							content = $(content).wrap(opt[name].wrap);
						} catch(e) {}
						try {
							content = $('#'+opt[name].attr.id).html(opt[name].html);
						} catch(e) {}
						try {
							content = $('#'+opt[name].attr.id).first(opt[name].first);
						} catch(e) {}
						try {
							content = $('#'+opt[name].attr.id).after(opt[name].after);
						} catch(e) {}
					}
				})($.gmap.controls[id], ctrl[i]);
			}
		} else {
			this.log("Control(id : " + id + ") exists on the map");
		}
	},
	removeControl: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.controls[id]) == true) {
			this.controls[id].remove();
			delete this.controls[id];
		} else {
			this.log("Control(id : " + id + ") Does not exist on the map");
		}
	}
});

// infowindow extend
$.extend($.gmap, {
	addInfoWindow: function(opt) {
		var id = opt.id.toString();
		var infoWindow = new google.maps.InfoWindow({
			id: id,
			content: this.is(opt.content) ? opt.content : "",
			disableAutoPan: this.is(opt.disableAutoPan) ? opt.disableAutoPan : null,
			maxWidth: this.is(opt.maxWidth) ? opt.maxWidth : null,
			pixelOffset: this.is(opt.pixelOffset) ? opt.pixelOffset : null,
			position: this.is(opt.position) ? opt.position : null,
			zIndex: this.is(opt.zIndex) ? opt.zIndex : 1,
			bindData: this.is(opt.bindData) ? opt.bindData : null
		});
		opt.obj.infoWindow = infoWindow;
		if(opt.open == true) {
			infoWindow.open(this.map, opt.obj);
		}
		var evt = ['closeclick','content_changed','domready','position_changed','zindex_changed'];
		for (var i=0; i<evt.length; i++) {
			(function(obj, name) {
				if(typeof(opt[name]) != "undefined") {
					google.maps.event.addListener(opt.obj.infoWindow, name, opt[name]);
				}
			})(opt.obj.infoWindow, evt[i]);
		}
	}
});

// marker extend
$.extend($.gmap, {	
	markerCheckTimeInterval: 5000,
	markerRemoveTimeInterval: 10000,
	markerMoveTimeInterval: 1000,
	markerSetInterval: [],
	markerMoveInfo: [],
	markerMoveUnit: 0.1,
	markerMoveUnitTime: 20,
	addMarker: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.markers[id]) == false) {
			var latLng = this.getLatLng(opt);
			var date = new Date();
			var time = date.getTime();
			this.markers[id] = new google.maps.Marker({
				id: id,
				position: latLng,
				map: this.map,
				title: this.is(opt.title) ? opt.title : "",
				titleLabelOption: (this.is(opt.title) && this.is(opt.titleLabelOption)) ? opt.titleLabelOption : null, // custom title label
				draggable: this.is(opt.draggable) ? opt.draggable : false,
				clickable: this.is(opt.clickable) ? opt.clickable : false,
				animation: this.is(opt.animation) ? opt.animation : google.maps.Animation.DROP,
				icon: this.is(opt.icon) ? opt.icon : "",
				time: this.is(opt.time) ? opt.time : time, // moving marker
				infoWindowOption: this.is(opt.infoWindowOption) ? opt.infoWindowOption : null,				
				infoWindow: null,
				opacity: this.is(opt.opacity) ? opt.opacity : 1,
				visible: this.is(opt.visible) ? opt.visible : true,
				remove: this.is(opt.remove) ? opt.remove : "",
				zIndex: this.is(opt.zIndex) ? opt.zIndex : 1,
				bindData: this.is(opt.bindData) ? opt.bindData : null
				//anchorPoint: Point
				//attribution: Attribution
				//crossOnDrag: boolean
				//cursor: String
				//optimized: boolean
				//place: Place
				//shape: MarkerShape
			});
			if(opt.type == "moving") {
				setInterval(function() { $.gmap.removeMarkerByExp({id:id, remove:opt.remove}); }, this.markerCheckTimeInterval);
			}
			if(this.is(opt.infoWindowOption) == true) {				
				this.extendOption(opt.infoWindowOption, {id: id, obj: this.markers[id]});
				this.addInfoWindow(opt.infoWindowOption);
			}
			var evt = ['animation_changed', 'click', 'clickable_changed', 'cursor_changed', 'dblclick', 
			'drag', 'dragend', 'draggable_changed', 'dragstart', 'flat_changed', 'icon_changed', 
			'mousedown', 'mouseout', 'mouseover', 'mouseup', 'position_changed', 'rightclick', 
			'shape_changed', 'title_changed', 'visible_changed', 'zindex_changed'];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						google.maps.event.addListener($.gmap.markers[id], name, opt[name]);
					}
				})($.gmap.markers[id], evt[i]);
			}			
			if(this.is(opt.titleLabelOption) == true) {
				this.markers[id].titleLabel = new $.gmap.drawMarkerLabel({
					id: id,
					titleLabelOption: opt.titleLabelOption,
					map: this.map
				});
				this.markers[id].titleLabel.bindTo('position', this.markers[id]);
				this.markers[id].titleLabel.bindTo('text', this.markers[id], 'title');
				this.markers[id].titleLabel.bindTo('visible', this.markers[id]);
				this.markers[id].titleLabel.bindTo('clickable', this.markers[id]);
				this.markers[id].titleLabel.bindTo('zIndex', this.markers[id]);				
			}    
		} else {
			this.log("Marker(id : " + id + ") exists on the map");
		}
	},
	removeMarker: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.markers[id]) == true) {
			this.markers[id].setMap(null);
			delete this.markers[id];
		} else {
			this.log("Marker(id : " + id + ") Does not exist on the map");
		}
	},
	removeMarkerByExp: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.markers[id]) == true) {
			if(this.checkExpTime(this.markerRemoveTimeInterval, this.markers[id].time) == true) {
				this.removeMarker({id:id});
				(opt.remove());
			}
		}
	},
	moveMarker: function(opt) {
		// opt : id / lat / lng
		var id = opt.id.toString();
		if(this.is(this.markers[id]) == true) {
			var latLng = new google.maps.LatLng(opt.lat, opt.lng);
			this.markers[id].setPosition(latLng);
		}
	},	
	movingMarkerByTime: function() {
		var date = new Date();
		var time = Number(date.getTime());
		var uTime = Number(this.userTime[id]) + this.userMoveTimeInterval;
		if(uTime < time) {
			return true;
		} else {
			return false;
		}
	},
	movingMarker: function(opt) {
		var id = opt.id.toString();		
		if(this.is(this.markers[id]) == true) {			
			//console.log(this.markers[id].time);
			// 만료가 되어야 움직임
			if(this.checkExpTime(this.markerMoveTimeInterval, this.markers[id].time) == true) {				
				var date = new Date();
				this.markers[id].time = Number(date.getTime());
				var fromLat = this.markers[id].getPosition().lat();
				var fromLng = this.markers[id].getPosition().lng();
				var toLat = Number(opt.lat);
				var toLng = Number(opt.lng);
				var curLat = 0;
				var curLng = 0;
				var frameIndex = 0;
				var mf = [];
				for (var i=0; i<1; i+= this.markerMoveUnit) {
					curLat = fromLat + i * (toLat - fromLat);
					curLng = fromLng + i * (toLng - fromLng);
					mf.push({lat:curLat, lng:curLng});
					frameIndex++;
				}
				this.markerMoveInfo[id] = mf;
				moving = function(id, index, wait) {
					$.gmap.moveMarker({id: id, lat:$.gmap.markerMoveInfo[id][index].lat, lng:$.gmap.markerMoveInfo[id][index].lng});
					if(index != $.gmap.markerMoveInfo[id].length-1) {
						setTimeout(function() { moving(id, index+1, wait); }, wait);
					}  else {
						$.gmap.markers[id].position = new google.maps.LatLng($.gmap.markerMoveInfo[id][index].lat, $.gmap.markerMoveInfo[id][index].lng);
					}
				}
				moving(id, 0, $.gmap.markerMoveUnitTime);
			} else {
				//this.log("Marker(id : " + id + ") is expired");
			}
		} else {
			this.removeMarker({id:id});
		}
	},
	setMarker: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.markers[id]) == true) {
			var prop = ['paths', 'strokeColor', 'strokeOpacity', 'strokeWeight', 'fillColor', 'fillOpacity', 
			'clickable', 'draggable', 'editable', 'visible', 'geodesic', 'strokePosition', 'zIndex'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("Marker - \""+ val +"\" property is not supported");
				}
			}
			this.markers[id].setOptions(opt);
		} else {
			this.log("Marker(id : " + id + ") Does not exist on the map");
		}
	}
});

// polygon extend
$.extend($.gmap, {
	drawPolygon: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.polygons[id]) == false) {			
			this.polygons[id] = new google.maps.Polygon({
				id: id,
				//paths: this.alignCoords(coords),
				paths: this.getPaths(opt.paths),
				strokeColor: this.is(opt.strokeColor) ? opt.strokeColor : "#B40404",
				strokeOpacity: this.is(opt.strokeOpacity) ? opt.strokeOpacity : 0.8,
				strokeWeight: this.is(opt.strokeWeight) ? opt.strokeWeight : 2,
				fillColor: this.is(opt.fillColor) ? opt.fillColor : "#FE2E2E",
				fillOpacity: this.is(opt.fillOpacity) ? opt.fillOpacity : 0.35,
				clickable: this.is(opt.clickable) ? opt.clickable : false,
				draggable: this.is(opt.draggable) ? opt.draggable : false,
				editable: this.is(opt.editable) ? opt.editable : false,
				visible: this.is(opt.visible) ? opt.visible : true,
				zIndex: this.is(opt.zIndex) ? opt.zIndex : 1,
				bindData: this.is(opt.bindData) ? opt.bindData : null,
				infoWindowOption: this.is(opt.infoWindowOption) ? opt.infoWindowOption : null,
				infoWindow: null
				//geodesic: boolean
				//strokePosition: StrokePosition	
			});
			this.polygons[id].setMap(this.map);
			if(this.is(opt.infoWindowOption) == true) {				
				this.extendOption(opt.infoWindowOption, {id: id, obj: this.polygons[id]});
				this.addInfoWindow(opt.infoWindowOption);
			}
			var evt = ['click','dblclick','drag','dragend','dragstart', 
			'mousedown','mousemove','mouseout','mouseover','mouseup','rightclick'];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						google.maps.event.addListener($.gmap.polygons[id], name, opt[name]);
					}
				})($.gmap.polygons[id], evt[i]);
			}
			
		} else {
			this.log("Polygon(id : " + id + ") exists on the map");
		}
	},
	removePolygon: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.polygons[id]) == true) {
			this.polygons[id].setMap(null);
			delete this.polygons[id];
		} else {
			this.log("Polygon(id : " + id + ") Does not exist on the map");
		}
	},
	setPolygon: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.polygons[id]) == true) {
			var prop = ['paths', 'strokeColor', 'strokeOpacity', 'strokeWeight', 'fillColor', 'fillOpacity', 
			'clickable', 'draggable', 'editable', 'visible', 'geodesic', 'strokePosition', 'zIndex'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("Polygon - \""+ val +"\" property is not supported");
				}
			}
			if(this.is(opt.paths) == true) {
				opt.paths = this.getPaths(opt.paths);
			}
			this.polygons[id].setOptions(opt);
		} else {
			this.log("Polygon(id : " + id + ") Does not exist on the map");
		}
	},
	extendPolygon: function() {
		if (!google.maps.Polygon.prototype.getBounds) {
			google.maps.Polygon.prototype.getBounds = function(latLng) {
				var bounds = new google.maps.LatLngBounds();
				var paths = this.getPaths();
				for (var p = 0; p < paths.getLength(); p++) {
					path = paths.getAt(p);
					for (var i = 0; i < path.getLength(); i++) {
						bounds.extend(path.getAt(i));
					}
				}
			return bounds;
			}
		}
		google.maps.Polygon.prototype.containsLatLng = function(latLng) {
			var inPoly = false, bounds, lat, lng, numPaths, p, path, numPoints, i, j, vertex1, vertex2;
			if (arguments.length == 2) {
				if (typeof arguments[0] == "number" && typeof arguments[1] == "number") {
					lat = arguments[0];
					lng = arguments[1];
				}
			} else if (arguments.length == 1) {
				bounds = this.getBounds();
				if (!bounds && !bounds.contains(latLng)) { return false; }
				lat = latLng.lat();
				lng = latLng.lng();
			} else {
				console.log("error");
			}
			numPaths = this.getPaths().getLength();
			for (p = 0; p < numPaths; p++) {
				path = this.getPaths().getAt(p);
				numPoints = path.getLength();
				j = numPoints - 1;
				for (i = 0; i < numPoints; i++) {
					vertex1 = path.getAt(i);
					vertex2 = path.getAt(j);
					if (vertex1.lng() <  lng && vertex2.lng() >= lng || vertex2.lng() <  lng && vertex1.lng() >= lng) {
						if (vertex1.lat() + (lng - vertex1.lng()) / (vertex2.lng() - vertex1.lng()) * (vertex2.lat() - vertex1.lat()) < lat) {
							inPoly = !inPoly;
						}
					}
					j = i;
				}
			}
			return inPoly;
		};
	}
});

// polyline extend
$.extend($.gmap, {
	drawPolyline: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.polylines[id]) == false) {
			var paths = this.getPaths(opt.paths);
			this.polylines[id] = new google.maps.Polyline({
				id: id,
				path: paths, // paths X
				strokeColor: this.is(opt.strokeColor) ? opt.strokeColor : "#B40404",
				strokeOpacity: this.is(opt.strokeOpacity) ? opt.strokeOpacity : 0.8,
				strokeWeight: this.is(opt.strokeWeight) ? opt.strokeWeight : 2,
				clickable: this.is(opt.clickable) ? opt.clickable : false,
				draggable: this.is(opt.draggable) ? opt.draggable : false,
				editable: this.is(opt.editable) ? opt.editable : false,
				visible: this.is(opt.visible) ? opt.visible : true,
				zIndex: this.is(opt.zIndex) ? opt.zIndex : true,
				icons: this.is(opt.icons) ? opt.icons : null,
				bindData: this.is(opt.bindData) ? opt.bindData : null
				//geodesic: boolean
			});
			this.polylines[id].setMap(this.map);
			var evt = ['click', 'dblclick', 'drag', 'dragend', 'dragstart', 
			'mousedown', 'mousemove', 'mouseout', 'mouseover', 'mouseup', 'rightclick'];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						google.maps.event.addListener($.gmap.polylines[id], name, opt[name]);
					}
				})($.gmap.polylines[id], evt[i]);
			}
		} else {
			this.log("Polyline(id : " + id + ") exists on the map");
		}
	},
	removePolyline: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.polylines[id]) == true) {
			this.polylines[id].setMap(null);
			delete this.polylines[id];
		} else {
			this.log("polyline(id : " + id + ") Does not exist on the map");
		}
	},
	setPolyline: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.polylines[id]) == true) {
			var prop = ['path', 'strokeColor', 'strokeOpacity', 'strokeWeight', 'clickable', 'draggable', 
			'editable', 'visible', 'geodesic', 'icons', 'zIndex'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("Polyline - \""+ val +"\" property is not supported");
				}
			}
			if(this.is(opt.paths) == true) {
				opt.paths = this.getPaths(opt.paths);
			}
			this.polylines[id].setOptions(opt);
		} else {
			this.log("Polyline(id : " + id + ") Does not exist on the map");
		}
	}
});

// rectangle extend
$.extend($.gmap, {
	drawRectangle: function(opt) {
		var id = opt.id.toString();		
		if(this.is(this.rectangles[id]) == false) {		
			this.rectangles[id] = new google.maps.Rectangle({
				id: id,
				bounds: this.getBounds(opt.bounds),
				strokeColor: this.is(opt.strokeColor) ? opt.strokeColor : "#B40404",
				strokeOpacity: this.is(opt.strokeOpacity) ? opt.strokeOpacity : 0.8,
				strokeWeight: this.is(opt.strokeWeight) ? opt.strokeWeight : 2,
				fillColor: this.is(opt.fillColor) ? opt.fillColor : "#FE2E2E",
				fillOpacity: this.is(opt.fillOpacity) ? opt.fillOpacity : 0.35,
				clickable: this.is(opt.clickable) ? opt.clickable : false,
				draggable: this.is(opt.draggable) ? opt.draggable : false,
				editable: this.is(opt.editable) ? opt.editable : false,
				visible: this.is(opt.visible) ? opt.visible : true,
				zIndex: this.is(opt.zIndex) ? opt.zIndex : 1,
				bindData: this.is(opt.bindData) ? opt.bindData : null
				//strokePosition: StrokePosition
				//
			});
			this.rectangles[id].setMap(this.map);
			var evt = ['bounds_changed', 'click', 'dblclick', 'drag', 'dragend', 'dragstart', 
			'mousedown', 'mousemove', 'mouseout', 'mouseover', 'mouseup', 'rightclick'];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						google.maps.event.addListener($.gmap.rectangles[id], name, opt[name]);
					}
				})($.gmap.rectangles[id], evt[i]);
			}
		} else {
			this.log("Rectangle(id : " + id + ") exists on the map");
		}
	},
	removeRectangle: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.rectangles[id]) == true) {
			this.rectangles[id].setMap(null);
			delete this.rectangles[id];
		} else {
			this.log("Rectangle(id : " + id + ") Does not exist on the map");
		}
	},
	setRectangle: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.rectangles[id]) == true) {
			var prop = ['bounds', 'strokeColor', 'strokeOpacity', 'strokeWeight', 
			'fillColor', 'fillOpacity', 'clickable', 'draggable', 'editable', 'visible', 'strokePosition', 'zIndex'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("Rectangle - \""+ val +"\" property is not supported");
				}
			}
			if(this.is(opt.bounds) == true) {
				opt.bounds = this.getBounds(opt.bounds);
			}
			this.rectangles[id].setOptions(opt);
		} else {
			this.log("Rectangle(id : " + id + ") Does not exist on the map");
		}
	}
});

// circle extend
$.extend($.gmap, {
	drawCircle: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.circles[id]) == false) {
			var center = this.getLatLng(opt.center);
			this.circles[id] = new google.maps.Circle({
				id: id,
				center: center,
				radius: this.is(opt.radius) ? opt.radius : 1,
				strokeColor: this.is(opt.strokeColor) ? opt.strokeColor : "#B40404",
				strokeOpacity: this.is(opt.strokeOpacity) ? opt.strokeOpacity : 0.8,
				strokeWeight: this.is(opt.strokeWeight) ? opt.strokeWeight : 2,
				fillColor: this.is(opt.fillColor) ? opt.fillColor : "#FE2E2E",
				fillOpacity: this.is(opt.fillOpacity) ? opt.fillOpacity : 0.35,
				clickable: this.is(opt.clickable) ? opt.clickable : false,
				draggable: this.is(opt.draggable) ? opt.draggable : false,
				editable: this.is(opt.editable) ? opt.editable : false,
				visible: this.is(opt.visible) ? opt.visible : true,
				zIndex: this.is(opt.zIndex) ? opt.zIndex : 1,
				bindData: this.is(opt.bindData) ? opt.bindData : null
				//strokePosition: StrokePosition				
				
			});
			this.circles[id].setMap(this.map);
			var evt = ['center_changed', 'click', 'dblclick', 'drag', 'dragend', 'dragstart', 
			'mousedown', 'mousemove', 'mouseout', 'mouseover', 'mouseup', 'radius_changed', 'rightclick'];
			for (var i=0; i<evt.length; i++) {
				(function(obj, name) {
					if(typeof(opt[name]) != "undefined") {
						google.maps.event.addListener($.gmap.circles[id], name, opt[name]);
					}
				})($.gmap.circles[id], evt[i]);
			}
		} else {
			this.log("Circle(id : " + id + ") exists on the map");
		}
	},
	removeCircle: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.circles[id]) == true) {
			this.circles[id].setMap(null);
			delete this.circles[id];
		} else {
			this.log("Circle(id : " + id + ") Does not exist on the map");
		}
	},
	setCircle: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.circles[id]) == true) {
			var prop = ['center', 'radius', 'strokeColor', 'strokeOpacity', 'strokeWeight', 
			'fillColor', 'fillOpacity', 'clickable', 'draggable', 'editable', 'visible', 'strokePosition', 'zIndex'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("\""+ val +"\" property is not supported");
				}
			}
			if(this.is(opt.center) == true) {
				opt.center = this.getLatLng(opt.center);
			}
			this.circles[id].setOptions(opt);
		} else {
			this.log("Circle(id : " + id + ") Does not exist on the map");
		}
	},
	setCircleCenter: function(center) {
		var center = new google.maps.LatLng(center.lat, center.lng);
		return center;
	}
});

// alignCooords extend
$.extend($.gmap, {
	alignCoords: function(coords) {
		var newCoords = [];
		var tmpCoords = [];
		var nCoords;
		var nDistance = 0;
		var tDistance = 0;
		var n = 0;
		for(var i=0; i<coords.length; i++) {
			tmpCoords.push(coords[i]);
		}		
		for(var i=0; i<coords.length; i++) {
			if(i==0) {
				newCoords.push(tmpCoords[i]);
				nCoords = tmpCoords[i];
				tmpCoords.splice(i,1);
			} else {
				n = 0;
				for(var j=0; j<tmpCoords.length; j++) {
					if(j == 0) {
						nDistance = this.getDistance(nCoords, tmpCoords[j]);
					} else {
						tDistance = this.getDistance(nCoords, tmpCoords[j]);
						if(nDistance > tDistance) {
							n = j;
							nDistance = tDistance;
						} 
					}
				}
				nCoords = tmpCoords[n];
				newCoords.push(tmpCoords[n]);							
				tmpCoords.splice(n,1);
			}
		}
		return newCoords;
	}
});

// overlay extend
$.extend($.gmap, {	
	addOverlay: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.overlays[id]) == false) {
			var bounds = this.getBounds(opt.bounds);
			this.overlays[id] = new this.drawOverlay(bounds, opt.image, this.map, opt.deg, opt.opacity);
		} else {
			this.log("Overlay(id : " + id + ") exists on the map");
		}
	},	
	drawOverlay: function(bounds, image, map, deg, opacity) {
		this.bounds_ = bounds;
		this.image_ = image;
		this.map_ = map;
		this.div_ = null;
		this.deg = deg || 0;
		this.opacity = opacity || 1;
		this.setMap(map);
	},
	removeOverlay: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.overlays[id]) == true) {
			this.overlays[id].setMap(null);
			delete this.overlays[id];		
		} else {
			this.log("Overlay(id : " + id + ") Does not exist on the map");
		}
	},
	setOverlay: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.overlays[id]) == true) {
			var prop = ['bounds', 'deg', 'opacity'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("Overlay - \""+ val +"\" property is not supported");
				}
			}
			if(this.is(opt.bounds) == true) {
				opt.bounds = this.getBounds(opt.bounds);
				this.overlays[id].updateBounds(opt.bounds);
			}			
			if(this.is(opt.deg) == true) {
				var deg = Number(opt.deg).toFixed(1);
				this.overlays[id].div.style.webkitTransform = 'rotate('+deg+'deg)'; 
				this.overlays[id].div.style.mozTransform = 'rotate('+deg+'deg)'; 
				this.overlays[id].div.style.msTransform = 'rotate('+deg+'deg)'; 
				this.overlays[id].div.style.oTransform = 'rotate('+deg+'deg)'; 
				this.overlays[id].div.style.transform = 'rotate('+deg+'deg)';	
			}
			if(this.is(opt.opacity) == true) {
				this.overlays[id].updateOpacity(opt.opacity);
			}
		} else {
			this.log("Overlay(id : " + id + ") Does not exist on the map");
		}
	},
	extendOverlay: function() {
		this.drawOverlay.prototype = new google.maps.OverlayView();
		this.drawOverlay.prototype.onAdd = function() {
			this.div = document.createElement('div');
			this.img = document.createElement('img');
			$(this.div).css('borderWidth', '0px');
			$(this.div).css('position', 'absolute');
			$(this.div).css('webkitTransform', 'rotate('+this.deg+'deg)');
			$(this.div).css('mozTransform', 'rotate('+this.deg+'deg)');
			$(this.div).css('msTransform', 'rotate('+this.deg+'deg)');
			$(this.div).css('oTransform', 'rotate('+this.deg+'deg)');
			$(this.div).css('transform', 'rotate('+this.deg+'deg)');
			$(this.img).attr('src', this.image_);
			$(this.img).css('width', '100%');
			$(this.img).css('height', '100%');
			$(this.img).css('opacity', this.opacity);
			$(this.img).css('position', 'absolute');
			this.div.appendChild(this.img);
			this.div_ = this.div;
			var panes = this.getPanes();
			panes.overlayLayer.appendChild(this.div);
		};
		this.drawOverlay.prototype.draw = function() {
			var overlayProjection = this.getProjection();
			var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
			var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());
			this.div = this.div_;
			$(this.div).css('left', sw.x + 'px');
			$(this.div).css('top', ne.y + 'px');
			$(this.div).css('width', (ne.x - sw.x) + 'px');
			$(this.div).css('height', (sw.y - ne.y) + 'px');
			$(this.img).css('opacity', this.opacity);
		};
		this.drawOverlay.prototype.updateBounds = function(bounds){
			this.bounds_ = bounds;
			this.draw();
		};
		this.drawOverlay.prototype.updateOpacity = function(opacity){
			this.opacity = opacity;
			this.draw();
		};
		this.drawOverlay.prototype.onRemove = function() {
		  this.div_.parentNode.removeChild(this.div_);
		  this.div_ = null;
		};
	}
});

//heatmap extend
// init libraries=visualization 필요
$.extend($.gmap, {
	drawHeatmap: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.heatmaps[id]) == false) {
			this.heatmaps[id] = new google.maps.visualization.HeatmapLayer({
				id: id,
				dissipating: this.is(opt.dissipating) ? opt.dissipating : false,
				gradient: this.is(opt.gradient) ? opt.gradient : null,
				maxIntensity: this.is(opt.maxIntensity) ? opt.maxIntensity : 0,
				opacity: this.is(opt.opacity) ? opt.opacity : 1,
				radius: this.is(opt.radius) ? opt.radius : 15,
				data: opt.data,
				bindData: this.is(opt.bindData) ? opt.bindData : null
			});
			this.heatmaps[id].setMap(this.map);
		} else {
			this.log("Heatmap(id : " + id + ") exists on the map");
		}
	},
	removeHeatmap: function(opt) {
		var id = opt.id.toString();
		if(this.is(this.heatmaps[id]) == true) {
			this.heatmaps[id].setMap(null);
			delete this.heatmaps[id];
		} else {
			this.log("Heatmap(id : " + id + ") Does not exist on the map");
		}
	},
	setHeatmap: function(opt) {
		var id = opt.id.toString();
		delete opt.id;
		if(this.is(this.heatmaps[id]) == true) {
			var prop = ['dissipating', 'gradient', 'maxIntensity', 'opacity', 'radius', 'data'];
			for(val in opt) {
				if(this.inValue(val, prop) == false) {
					this.log("\""+ val +"\" property is not supported");
				}
			}
			this.heatmaps[id].set(opt.prop, opt.value);
		} else {
			this.log("Heatmap(id : " + id + ") Does not exist on the map");
		}
	}
});

// marker label extend
$.extend($.gmap, {
	markerLabelPrefix: "Label",
	drawMarkerLabel: function(opt) {
		this.setValues(opt);
		var span = this.span_ = document.createElement('span');
		span.style.cssText = 'position: relative; ' + opt.titleLabelOption.style;
		this.div = this.div_ = document.createElement('div');
		this.div.id = $.gmap.markerLabelPrefix + opt.id;
		this.div.appendChild(span);
		this.div.style.cssText = 'position: absolute; display: none';
	},
	extendMarkerLabel: function() {		
		this.drawMarkerLabel.prototype = new google.maps.OverlayView();
		this.drawMarkerLabel.prototype.onAdd = function() {
			var pane = this.getPanes().overlayImage;
			pane.appendChild(this.div_);
			var me = this;
			this.listeners_ = [
			    google.maps.event.addListener(this, 'position_changed', function() { me.draw(); }),
			    google.maps.event.addListener(this, 'visible_changed', function() { me.draw(); }),
			    google.maps.event.addListener(this, 'clickable_changed', function() { me.draw(); }),
			    google.maps.event.addListener(this, 'text_changed', function() { me.draw(); }),
			    google.maps.event.addListener(this, 'zindex_changed', function() { me.draw(); }),
			    google.maps.event.addDomListener(this.div_, 'click', function() {
			    	if (me.get('clickable')) {
			    		google.maps.event.trigger(me, 'click');
			    	}
			    })
			];
		};
		this.drawMarkerLabel.prototype.onRemove = function() {
			this.div_.parentNode.removeChild(this.div_);
			for (var i = 0, I = this.listeners_.length; i < I; ++i) {
				google.maps.event.removeListener(this.listeners_[i]);
			}
		};
		this.drawMarkerLabel.prototype.draw = function() {
			var projection = this.getProjection();
			var position = projection.fromLatLngToDivPixel(this.get('position'));
			this.div = this.div_;
			$(this.div).css('left', position.x + 'px');
			$(this.div).css('top', position.y + 'px');				
			var visible = this.get('visible');			
			$(this.div).css('display', visible ? 'block' : 'none');			
			var clickable = this.get('clickable');
			$(this.span_).css('cursor', clickable ? 'pointer' : '');			
			var zIndex = this.get('zIndex');
			$(this.div).css('zIndex', zIndex);			
			$(this.span_).html(this.get('text').toString());
		};
	},
	removeMarkerLabel: function(opt) {
		var id = opt.id.toString();
		try {
			$("#"+ this.markerLabelPrefix + id).remove();
		} catch(e) {
			this.log("Label(id : " + id + ") Does not exist on the map");
		}
	}
});