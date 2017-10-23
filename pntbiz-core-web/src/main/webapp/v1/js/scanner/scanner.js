$(document).ready( function() {
	if($('#scannerKeyword').val() == "") $('#scannerKeyword').focus();	
	$('#scannerKeyword').bind('keyup', function(e) { if(e.keyCode==13) scanner.search(); });	
	$('#scannerFormBtn').bind('click', function() { scanner.form(); });
	$('#scannerMFormBtn').bind('click', function() { scanner.mform(); });
	$('#scannerBFormBtn').bind('click', function() { scanner.bform(); });
	$('#scannerSearchBtn').bind('click', function() { scanner.search(); });
	$('#scannerListBtn').bind('click', function() { scanner.list(); });
	$('#scannerRegBtn').bind('click', function() { scanner.reg(); });
	$('#scannerModBtn').bind('click', function() { scanner.mod(); });
	$('#scannerDelBtn').bind('click', function() { scanner.del(); });	
	$('#scannerBatchBtn').bind('click', function() { scanner.batch(); });	
	$('#map-canvas').hide();
	$('#scannerMapBtn').bind('click', function() {
		$('#map-canvas').toggle(function() { $.gmap.center(); });
	});
	$('#floor').bind('change', function() {
		var floor = $(this).val();
		if(floor != "") {
			scanner._floor = floor;
			scanner.setFloor();	
		}
		if($("#lat").val() != 0 && $("#lng").val() != 0) {
			$.gmap.addMarker({id:1, lat: $("#lat").val(), lng: $("#lng").val() });
		}
	});
});

var acMap= {};
var scanner = {
	_listURL: "/scanner/list.do",
	_formURL: "/scanner/form.do",
	_mformURL: "/scanner/mform.do",
	_bformURL: "/scanner/bform.do",
	_regURL: "/scanner/reg.do",
	_modURL: "/scanner/mod.do",
	_batchURL: "/scanner/batch.do",
	_delURL: "/scanner/del.do",
	_form: "#form1",
	_setmap : null,
	_floor: 1,
	_floorList: [],
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(scannerNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "scannerNum":scannerNum});
		common.redirect(this._mformURL);
	},
	bform: function(scannerNum) {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._bformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#scannerKeyword").val());
		if(!common.isObj(opt) || !common.isObj(keyword)) {
			this._listURL += common.setQueryString({"page": 1 });
		} else {
			this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		}
		common.redirect(this._listURL);
	},
	reg: function() {		
		$(this._form).validate({
			rules: {
				macAddr: { required: true, rangelength: [17, 35] },
				majorVer: {  required: true },
				scannerName: { required: true },
				lat: { required: true, number: true },
				lng: { required: true, number: true },
				floor: { required: true }
	        },
	        messages: {
	        	macAddr: { required: vm.required, range: vm.rangelength},
	        	majorVer: { required: vm.required },
	        	scannerName: { required: vm.required },
				lat: { required: vm.required, number: vm.number },
				lng: { required: vm.required, number: vm.number },
				floor: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._regURL,
				data: $(this._form).serialize(),
				success: this.regResult
			});
		}
	},
	regResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				scanner._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(scanner._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				macAddr: { required: true, rangelength: [17, 35] },
				majorVer: {  required: true },
				scannerName: { required: true },
				lat: { required: true, number: true },
				lng: { required: true, number: true },				
				floor: { required: true }
	        },
	        messages: {
	        	macAddr: { required: vm.required, range: vm.rangelength},
	        	majorVer: { required: vm.required },
				scannerName: { required: vm.required },
				lat: { required: vm.required, number: vm.number },
				lng: { required: vm.required, number: vm.number },
				floor: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});		
		
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._modURL,
				data: $(this._form).serialize(),
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
			case "1" :
				scanner._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(scanner._listURL);
				break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	},
	batch: function() {
		$(this._form).validate({
			rules: {
//				rssi: { required: true, number: true },
//				srssi: { required: true, number: true },
//				mrssi: { required: true, number: true },
//				drssi: { required: true, number: true },
//				exMeter: { required: true, number: true },
//				calPoint: { required: true, number: true },
//				maxSig: { required: true, number: true },
//				maxBuf: { required: true, number: true }
	        },
	        messages: {
//	        	rssi: { required: vm.required, number: vm.number}, 
//	        	srssi: { required: vm.required, number: vm.number}, 
//	        	mrssi: { required: vm.required, number: vm.number}, 
//	        	drssi: { required: vm.required, number: vm.number},
//	        	exMeter: { required: vm.required, number: vm.number}, 
//	        	calPoint: { required: vm.required, number: vm.number},
//	        	maxSig: { required: vm.required, number: vm.number},
//	        	maxBuf: { required: vm.required, number: vm.number}
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});		
		
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._batchURL,
				data: $(this._form).serialize(),
				success: this.modResult
			});
		}
	},
	batchResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				scanner._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(scanner._listURL);
				break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	},
	del: function() {
		var result = confirm(vm.delConfirm);
		if(result) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._delURL,
				data: $(this._form).serialize(),
				success: this.delResult
			});
		}
	},
	delResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		}  else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				scanner._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(scanner._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
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
				scanner._setmap = result.data;
				scanner._floor = scanner._setmap.initFloor;
				break;
			default : break;
		}
	},
	floorInfoResult: function(data) {
		//var result = $.parseJSON(data);
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
					scanner._floorList[fdata[i].floorNum.toString()] = floor;     
				}
				break;
			default : break;
		}
	},
	setFloor: function() {
		for(var id in this._floorList) {
			$.gmap.removeOverlay({id: id});
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
