var presenceMap = {
	comNum: 0,
	UUID: "",
	floor: 1,
	floorList: [],
	floorGroup: [],
	materials: [],
	markers: [],
	cnt: 0,
	scannerPrefix: "SCANNER",
	site: {"socketURL": "https://beacon.pntbiz.com:10000"},
	socket: null,
	init: function(com) {
		if(com.socketURL != null && com.socketURL != "") presenceMap.site.socketURL = com.socketURL;
		$.when(	
		).then(function(){			
			presenceMap.comNum = com.comNum;
			presenceMap.UUID = com.UUID;
			presenceMap.floor = com.initFloor;
			$.gmap.markerCheckTimeInterval = com.checkTimeInterval;
			$.gmap.markerMoveTimeInterval = com.moveTimeInterval;
			$.gmap.markerMoveUnit = com.moveUnit;
			$.gmap.markerRemoveTimeInterval = com.removeTimeInterval;	
			$.gmap.load({
				lat: com.lat,
				lng: com.lng,
				zoom: com.initZoom,
				div: "map-canvas",
				width: com.width,
				height: com.height,
				func: function() { presenceMap.setdata(com); }		
				//logging: true
			});
		});
	},	
	setdata: function(com) {
		$.when(
		).then(function(){
			presenceMap.load();
		});
	},	
	load: function() {		
		var floorOption = [];
		for(var i=0; i<presenceMap.floorGroup.length; i++) {
			var opt = {
				value: presenceMap.floorGroup[i],
				text: presenceMap.floorGroup[i] + "F"
			}
			if(presenceMap.floorGroup[i] == this.floor) opt["selected"] = true;
			floorOption.push(opt);
		}		
		$.gmap.addControl({id:'floor-combo', position:'top_center', combobox: { option: floorOption, attr: {id: 'floorList'}, style: { margin: '5px'}, change:function(e) { presenceMap.changeFloor(); }} });
		$.gmap.addControl({
			id:'filter-input', 
			position:'left_top', 
			input: { attr: { id: 'filter', placeholder: 'filtering'}, style:{ margin:'5px'},
				keyup: function(e) { 
					presenceMap.removeObjectFilter();
				}
			} 
		});
		this.setOverlay();
		this.initMode("S");
	},
	initMode: function() {
		this.socket = io.connect(this.site.socketURL, {
			transports: [ 'websocket' ],
			forceNew: true,
			reconnection: false
		});
		this.socket.on('connect', function () {	
			presenceMap.socket.emit('join', "presence-" + presenceMap.UUID);
			presenceMap.socket.on('msg',function(msg){
				common.info(msg);
			});
	    });
		this.socket.on('disconnect', function () {			
			console.log("client disconnect");
			presenceMap.socket.emit('leave', "presence-" + presenceMap.UUID);
	    });
		this.socket.on('updateMarker', function (data) {	
			if(typeof(data.mode) == "undefined") {
				var UUID = data.UUID.toUpperCase();
				var id = data.id.toUpperCase();
				var beacon = data.id.toUpperCase().split("_");
				var majorVer = beacon[1];
				var minorVer = beacon[2];
				var lat = data.lat;
				var lng = data.lng
				var floor = data.floor.toString();
				var scannerPos = data.scannerPos;				
				// 자재정보가 없는 경우 처리
				try {
					var imgURL = presenceMap.materials[id].imgURL;
					var beaconName = presenceMap.materials[id].beaconName;
					var imgMargin = 5;
				} catch(e) {
					var imgURL = "";
					var imgMargin = 10;
					var minorStr = "" + minorVer;
					var beaconName = "00000".substring(0, 5 - minorStr.length) + minorStr;
				}
				// UUID / floor filtering
				if(presenceMap.UUID.toUpperCase() == UUID && presenceMap.floor == floor) {
					// filtering
					var reg = new RegExp($('#filter').val(), "g");
					if($('#filter').val() == "" || reg.test(beaconName) == true) {
						if($.gmap.is($.gmap.markers[id]) == false) {
							var icon = (imgURL == "" || imgURL == null || typeof(imgURL) == "undefiend") ? {
								path: google.maps.SymbolPath.CIRCLE,
								scale: 5,
								fillColor: "#33FF00",
								fillOpacity: 1,
								strokeColor: "#339900",
								strokeOpacity: 1, 
								strokeWeight: 1
							} : imgURL;
							$.gmap.addMarker({
								id: id,
								lat: lat, 
								lng: lng, 
								icon: icon,
								type:'moving',
								zIndex: 1,
//									visible: true,
								infoWindowOption:{ content:beaconName },
								title: beaconName,
								titleLabelOption:{
									style: 'left: -50%; top: '+ imgMargin +'px; ' + 
										'white-space: nowrap; ' +
										'border: 1px solid black; ' + 
										'padding: 2px; ' + 
										'background-color: white'
								},
								click:function(e) {this.infoWindow.open(this.map, this)},
								clickable: false,
								remove: function(e) { 			
									delete presenceMap.markers[id];
									// title label remove
									$.gmap.removeMarkerLabel({id: id});
								}
							});								        
							presenceMap.markers[id] = id;
						} else {
							$.gmap.movingMarker({id: id, lat: lat, lng: lng});
						}					
					} // if end filtering
				} // if end UUID/floor filtering 
				else {					
					// 다른 층에 있는 marker제거
					if($.gmap.is($.gmap.markers[id]) == true) {
						$.gmap.removeMarker({id: id});						
						// title label remove
						$.gmap.removeMarkerLabel({id: id});
						
					}
				} // else end UUID/floor filtering
				
			}
		});			
	},
	setFloorGroupList: function() {
		this.floorGroup = [];
		for(var key in this.floorList) {
			var length = this.floorGroup.length;
			var cnt = 0;
			if(length > 0) {
				for(var i=0; i<length; i++) { 
					if(this.floorGroup[i] == this.floorList[key].floor) {
						cnt++;
					}
				}
				if(cnt == 0) {
					this.floorGroup.push(this.floorList[key].floor);
				}
			} else {
				this.floorGroup.push(this.floorList[key].floor);
			}
		}		
	},		
	changeFloor: function() {
		var id = this.floor.toString();
		this.floor = $("#floorList option:selected").val();
		this.setOverlay();
		this.initTargetMarker();
	},
	setOverlay: function() {
		for(var id in this.floorList) { 
			$.gmap.removeOverlay({id: id}); 
		}
		for(var id in this.floorList) {
			if(this.floor == this.floorList[id].floor) {				
				var floor = this.floorList[id];
				$.gmap.addOverlay({
					id: floor.floorNum.toString(),	
					bounds:[{lat:floor.swLat, lng:floor.swLng},{lat:floor.neLat, lng:floor.neLng}],
					image: floor.imgURL, 
					deg: floor.deg,
					opacity: 1
				});
			}
		}
	},	
	initTargetMarker: function() {
		for(var key in this.markers) {
			if(key.substring(0, this.scannerPrefix.length) != this.scannerPrefix) {
				delete presenceMap.markers[key];		
				$.gmap.removeMarker({id:key});
				$.gmap.removeMarkerLabel({id:key});
			}
		}
	},
	removeObjectFilter: function() {		
		for(var key in $.gmap.markers) {
			if($('#filter').val() != "") {
				var reg = new RegExp($('#filter').val(), "g");
				if(reg.test($.gmap.markers[key].title) == false) {
					if(key.substring(0, this.scannerPrefix.length) != this.scannerPrefix) {
						$.gmap.removeMarker({id:key});
						$.gmap.removeMarkerLabel({id:key});
					}
				}
			}
		}
	}
}