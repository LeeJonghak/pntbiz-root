$(document).ready( function() {
	$('#presenceTargetRunBtn').bind('click', function() { 
			presenceTarget.run();
	});
	$('#presenceLogDownloadBtn').bind('click', function() {
		var filename = "target";
		var beaconName = $("#beaconName").val();
		var date1 = $("#sDate").val();
		var date2 = $("#eDate").val();
		var sDate = date1.substring(0,4) + date1.substring(5,7) +  date1.substring(8,10) + date1.substring(11,13) + date1.substring(14,16) + date1.substring(17,19);
		var eDate = date2.substring(0,4) + date2.substring(5,7) +  date2.substring(8,10) + date2.substring(11,13) + date2.substring(14,16) + date2.substring(17,19);
		if(presenceTarget.flag == true) {
			filename = filename + "_" + beaconName + "_" + sDate + "_" + eDate;
			$('#presenceLogTable').tableExport({			
				separator: ',',
				//ignoreColumn: [2,3],
				tableName:filename,
				type:'csv',
				//pdfFontSize:14,
				//pdfLeftMargin:20,
				escape:'false',
				htmlContent:'false',
				consoleLog:'false'
			}); 
		}		
	});
		
	try {
		$('#datetimepicker1')
			.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true})
			.on('dp.change', function(event) {
				$('#sDateTimestamp').val(event.date.unix());
			});
		$('#datetimepicker2')
			.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true})
			.on('dp.change', function(event) {
				$('#eDateTimestamp').val(event.date.unix());
			});
	} catch(exception) {} 		
	try {
		$('#beaconName').bind('keyup', function() { 
			$('#beaconNum').val("");
			$('#presenceTargetRunBtn').removeClass().addClass("btn btn-default btn-sm");
			presenceTarget.flag = false;
		});
		$('#beaconName').typeahead({
			minLength: 2,
			items: 100,
			source: function(query, process) {	presenceTarget.beaconList(query, process); },
			updater: function(item) { 	
				$('#beaconNum').val(beaconMap[item]); 
				$('#presenceTargetRunBtn').removeClass().addClass("btn btn-primary btn-sm");				
				presenceTarget.flag = true;
				return item; 
			}
		});
	} catch(exception) {}
});
var beaconMap= {};
var presenceTarget = {
	_listURL: "/tracking/presence/scanner/target.do",
	_beaconListURL: "/tracking/presence/scanner/target.beacon.list.do",
	_logListURL: "/tracking/presence/scanner/target.log.list.do",
	_form: "#form1",
	_setmap : null,
	_floor: 1,
	_floorList: [],
	markers: [],	
	hmdata: [],
	polylines: [],
	flag: false, 
	init: function() {
		var setmapURL = "/tracking/presence/setmap.info.do";
		var floorURL = "/map/floor/list.do";	
		$.when(
			$.ajax({ type: "POST", url: setmapURL, data: "", success: presenceTarget.setmapInfoResult }),
			$.ajax({ type: "POST", url: floorURL, data: "", success: presenceTarget.floorInfoResult })
		).then(function(){ 		
			presenceTarget.setFloor();

			var floorOption = [];
			for(var id in presenceTarget._floorList) {
				var opt = {
					value: presenceTarget._floorList[id].floor,
					text: presenceTarget._floorList[id].floor + "F"
				}
				if(presenceTarget._floorList[id].floor == presenceTarget._floor) {
					opt["selected"] = true;
				}
				floorOption.push(opt);
			}
			$.gmap.addControl({id:'floor-combo', name:'floor', position:'top_center', combobox: {
				option: floorOption,
				attr: {id: 'floorList'},
				style: { margin: '5px'},
				change:function(e) {
					presenceTarget.setFloor(this.value);
					//$('input[name=floor]').val(this.value);
					presenceTarget.runProc(this.value);
				}}
			});
		});		
//		var sTime = new Date().getTime() - 60 * 30 * 1000;
//		var eTime = new Date().getTime();
//		$("#sDate").val(common.time2str(sTime,""));
//		$("#eDate").val(common.time2str(eTime,""));


	},
	run: function() {
		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();
		var sTime = new Date(sDate).getTime();
		var eTime = new Date(eDate).getTime();			
		var beaconNum = $("#beaconNum").val();
		if(sDate == "") {
			alert(vm.sdateChooseError);
			return false;
		}
		if(eDate == "") {
			alert(vm.edateChooseError);
			return false;
		}
		if(sTime > eTime || eTime - sTime > 60 * 30 * 1000) {
			alert(vm.dateChooseError);
			return false;
		}
		if(beaconNum == "") {
			alert(vm.targetBeaconChooseError);
			return false;
		}
		$.ajax({ type: "POST",
			contentType: 'application/x-www-form-urlencoded',
			processData: false,
			url: this._logListURL,
			data: $(this._form).serialize(),
			success: this.runResult
		});
	},
	runProc: function(floor) {
		var log = [];
		if(typeof(presenceTarget._resultData[floor])!='undefined') {
			log = presenceTarget._resultData[floor];
		}

		var viewType = $("#viewType option:selected").val();

		$('#logBadge').html(log.length);
		$("#presenceLogTable tbody tr").remove();
		for(var i = 0; i<presenceTarget.markers.length; i++) {
			$.gmap.removeMarker({id:presenceTarget.markers[i]});
		}
		presenceTarget.markers = [];
		for(var i = 0; i<presenceTarget.polylines.length; i++) {
			$.gmap.removePolyline({id:presenceTarget.polylines[i]});
		}
		presenceTarget.polylines = [];
		$.gmap.removeHeatmap({id: 'heatmap'});
		presenceTarget.hmdata = [];
		for (var i = 0; i < log.length; i++) {
			var html = "<tr align='center'>";
			html += "<td>" + log[i].logNum + "</td>";
			html += "<td>" + log[i].uuid + "</td>";
			html += "<td>" + log[i].targetName + "</td>";
			html += "<td>" + log[i].beaconName + "</td>";
			html += "<td>" + log[i].logDesc + "</td>";
			html += "<td>" + log[i].lat + "</td>";
			html += "<td>" + log[i].lng + "</td>";
			html += "<td>" + log[i].floor + "</td>";
			html += "<td><span class=\"pnt-timestamp\" data-timestamp=\""+log[i].regDate+"\" data-format=\"YYYY-MM-DD HH:mm:ss\" /></td>";
			html += "</tr>";
			$('#presenceLogTable > tbody:last-child').append(html);
		}

		if(log.length <= 0) {
			alert(vm.infoFail);
		} else {

			if(viewType == "marker") {
				for (var i = 0; i < log.length; i++) {
					setTimeout(
						(function(num){
							return function() {
								//					        	var latLng =  new google.maps.LatLng(log[num].lat, log[num].lng);
								//					            presenceTarget.addMarker(latLng);
								var mid = "marker" + log[num].logNum;
								$.gmap.addMarker({
									id: mid,
									lat: log[num].lat,
									lng: log[num].lng,
									icon: "https://static.pntbiz.com/indoorplus/images/inc/marker_blue_5.png",
									animation: false
								});
								presenceTarget.markers.push(mid);
							};
						})(i), 10*i);
				}
			} else if(viewType == "heatmap") {
				for (var i = 0; i < log.length; i++) {
					presenceTarget.hmdata.push(new google.maps.LatLng(log[i].lat, log[i].lng));
				}
				$.gmap.drawHeatmap({
					id: 'heatmap',
					dissipating: true,
					gradient: null,
					maxIntensity: 0,
					opacity: 0.7,
					radius: 10,
					data: presenceTarget.hmdata
				});
			} else if(viewType == "movement") {
				var time = log[log.length-1].regDate - log[0].regDate;
				var stime = log[0].regDate;
				var etime = log[log.length-1].regDate;
				var prelog = null;
				for(var i=0; i<=time; i++) {
					setTimeout((function(ntime){ return function() {
						if(log.length > 0) {
							var cnt = 0;
							while(true) {
								if(log[0].regDate == Number(stime + ntime)) {
									if(cnt == 0 || (prelog != null && (prelog.lat != log[0].lat || prelog.lng != log[0].lng))) {
										//											var mid = "marker"+log[0].logNum;
										//								            $.gmap.addMarker({
										//								            	id: mid,
										//								            	lat: log[0].lat,
										//								            	lng: log[0].lng,
										//								            	icon: "http://static.pntbiz.com/indoorplus/images/inc/marker_blue_5.png",
										//								            	animation: false
										//								            });
										//								            presenceTarget.markers.push(mid);
										//									        console.log(mid + " / add marker");
										//								            if(ntime != time) {
										//									            setTimeout((function(id){
										//													return function() {
										//														$.gmap.setMarker({id: id, opacity: 0.2});
										//															console.log(mid + " / opacity marker");
										//													}
										//												})(mid), 1000);
										////									            setTimeout((function(id){
										////													return function() {
										////														$.gmap.removeMarker({id: id});
										////															 console.log(mid + " / remove marker");
										////													}
										////												})(mid), 300);
										//								            }
										try {
											var pid = "polyline"+log[0].logNum;
											$.gmap.drawPolyline({
												id: pid,
												paths: [{lat:prelog.lat,lng:prelog.lng},{lat:log[0].lat, lng:log[0].lng}],
												icons: [{
													icon: {
														path: google.maps.SymbolPath.CIRCLE,
														strokeColor: 'red',
														scale: 1.5
													},
													offset: '100%'
												}],
												strokeWeight: 2,
												strokeColor: 'red',
												map: $.gmap.map
											});
											//										        console.log(pid + " / add Polyline");
											presenceTarget.polylines.push(pid);
											if(ntime != time) {
												setTimeout((function(id){
													return function() {
														$.gmap.setPolyline({id: id, strokeOpacity: 0.2, strokeWeight: 0.3, strokeColor: 'red'});
														//																console.log(pid + " / opacity Polyline");
													}
												})(pid), 1000);
												//										            setTimeout((function(id){
												//														return function() {
												//															$.gmap.removePolyline({id: id});
												//																console.log(pid + " / remove Polyline");
												//														}
												//													})(pid), 300);
											}
										} catch(e) {

										}
										cnt++;
										prelog = log[0];
										log.shift();

									} else {
										prelog = log[0];
										log.shift();
									}
									if(log.length <= 0) break;
								} else {
									break;
								}
							}
						} else {

						}

						if(ntime == time) {
							alert(vm.end);
						}
					};
					})(i), 100*i);
				}
			}
		}
	},
	runResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :

				presenceTarget._resultData = {};
				for(var i = 0; i < result.data.length; i++) {
					if(typeof(presenceTarget._resultData[result.data[i].floor])=='undefined') {
						presenceTarget._resultData[result.data[i].floor] = [];
					}
					presenceTarget._resultData[result.data[i].floor].push(result.data[i]);
				}
				$('#floorList>option').each(function(index, element) {
					var val = $(element).val();
					if(typeof(presenceTarget._resultData[val])!='undefined' &&
						presenceTarget._resultData[val].length>0) {
						$(element).html(val+'F ('+presenceTarget._resultData[val].length+')');
					}
				});

				presenceTarget.runProc(presenceTarget._floor);
				break;
			case "2" : alert(vm.infoFail); break;
			default : alert(vm.infoError); break;
		}
	},
	beaconList: function(query, process) {
		$.ajax({
			url: presenceTarget._beaconListURL, 
			type: 'POST',
			data: 'opt=beaconName' + '&keyword=' + $('#beaconName').val(),
			success: function(data) {
				var result = {};
				if(typeof(data)=='string') {
					result = $.parseJSON(data);
				} else {
					result = data;
				}
				var list = [];
				for(var i=0; i<result.data.length; i++) {
					beaconMap[result.data[i].beaconName] = result.data[i].beaconNum;
					list.push(result.data[i].beaconName);
				}
				process(list);
			}
		});
	},
	excel: function() {
		var sDate = $("#sDate").val();
		var eDate = $("#sDate").val();
		var beaconNum = $("#beaconNum").val();		
		if(sDate == "") {
			alert(vm.sdateChooseError);
			return false;
		}
		if(eDate == "") {
			alert(vm.edateChooseError);
			return false;
		}	
		if(beaconNum == "") {
			alert(vm.targetBeaconChooseError);
			return false;
		}
		$.ajax({ type: "POST",
			contentType: 'application/x-www-form-urlencoded',
			processData: false,
			url: this._logExcelURL,
			data: $(this._form).serialize(),
			success: this.excelResult
		});
	},
	excelResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				alert(vm.excelDataCreate);
				break;
			case "2" : alert(vm.infoFail); break;
			default : alert(vm.infoError); break;
		}
	},
	setmapInfoResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" : 
				presenceTarget._setmap = result.data;
				presenceTarget._floor = presenceTarget._setmap.initFloor;
				$('input[name=floor]').val(presenceTarget._floor);
				break;
			default : break;
		}
	},
	floorInfoResult: function(data) {
		/*var result = $.parseJSON(data);*/
		var result = data;
		switch(result.result) {
			case "1" : 
				var fdata = result.data;

				for(var i=0; i<fdata.length; i++) {
					var floor = {floorNum: fdata[i].floorNum, floor: fdata[i].floor, floorName: fdata[i].floorName,
							swLat : fdata[i].swLat,	swLng : fdata[i].swLng, 
							neLat : fdata[i].neLat, neLng : fdata[i].neLng,
							deg : fdata[i].deg, imgURL : fdata[i].imgURL};
					presenceTarget._floorList[fdata[i].floorNum.toString()] = floor;     
				}
				break;
			default : break;
		}
	},
	setFloor: function(pfloor) {
		if(typeof(pfloor)!='undefined') {
			this._floor = pfloor;
		}
		for(var id in this._floorList) {
			$.gmap.removeOverlay({id: this._floorList[id].floorNum.toString()});
		}
		for(var id in this._floorList) {
			if(this._floor == this._floorList[id].floor) {
				var floor = this._floorList[id];
				$.gmap.addOverlay({
					id: floor.floorNum.toString(),	
					bounds:[{lat:floor.swLat, lng:floor.swLng},{lat:floor.neLat, lng:floor.neLng}],
					image: floor.imgURL, 
					deg: floor.deg,
					opacity: 1
				});
			}
		}
	}
	
};
