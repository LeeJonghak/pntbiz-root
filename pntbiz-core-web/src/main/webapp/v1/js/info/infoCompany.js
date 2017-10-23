$(document).ready( function() {

	$('#map-canvas').hide()
	$('#infoCompanyMapBtn').bind('click', function() {
		if(typeof($.gmap)=='undefined') {
			$('#map-canvas').toggle(function() {
				if(typeof(window.pntmap)=='undefined') {
					window.pntmap = new pnt.map.OfflineMap('map-canvas');
					window.pntmap.on('click', function(evt) {
						var lonlat = pnt.util.transformLonLat(evt.coordinate);
						$('#lat').val(lonlat[1]);
						$('#lng').val(lonlat[0]);
					});
				}
				pntmap.setCenter(pnt.util.transformCoordinates([window.company.lng, window.company.lat]));
			});
		} else {
			$('#map-canvas').toggle(function() {
				$.gmap.center();
				console.log('$.gmap.map', $.gmap.map);

				if(typeof($.gmap.flagRegClick)=='undefined') {
					$.gmap.flagRegClick = true;
					$.gmap.map.addListener('click', function(evt) {
						$('#lat').val(evt.latLng.lat);
						$('#lng').val(evt.latLng.lng);
					});
				}
			});
		}
	});
	$('#infoCompanyModBtn').bind('click', function() { infoCompany.mod(); });

	console.log('$.gmap.map', $.gmap.map);
});

var infoCompany = {
	_modURL: "/info/company/mod.do",
	_form: "#form1",
	_setmap : null,
	_floor: 1,
	_floorList: [],
	init: function() {
		var setmapURL = "/tracking/presence/setmap.info.do";
		var floorURL = "/map/floor/list.do";	
		$.when(
			$.ajax({ type: "POST", url: setmapURL, data: "", success: infoCompany.setmapInfoResult }),
			$.ajax({ type: "POST", url: floorURL, data: "", success: infoCompany.floorInfoResult })
		).then(function(){
			infoCompany.setFloor();
		});
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				UUID: { required: true },
				comName: { required: true }
	        },
	        messages: {
	        	UUID: { required: vm.required },        	
	        	comName: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._modURL,
				data: $(infoCompany._form).serialize(),
				success: this.modResult
			});
		}
	},
	modResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" : common.info(vm.modSuccess); break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	},
	map: function(id, func, option) {
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
				infoCompany._setmap = result.data;
				infoCompany._floor = infoCompany._setmap.initFloor;
				break;
			default : break;
		}
	},
	floorInfoResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" : 
				var fdata = result.data;
				var floor;	
				for(var i=0; i<fdata.length; i++) {
					floor = {floorNum: fdata[i].floorNum, floor: fdata[i].floor, floorName: fdata[i].floorName, 
							swLat : fdata[i].swLat,	swLng : fdata[i].swLng, 
							neLat : fdata[i].neLat, neLng : fdata[i].neLng,
							deg : fdata[i].deg, imgURL : fdata[i].imgURL};
					infoCompany._floorList[fdata[i].floorNum.toString()] = floor;
				}
				break;
			default : break;
		}
	},
	setFloor: function() {
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
