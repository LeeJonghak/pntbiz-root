$(document).ready( function() {
	$('#floorForm').hide();
	$('#floorRegBtn').bind('click', function() { fplan.reg(); });
	$('#floorModBtn').bind('click', function() { fplan.mod(); });
	$('#floorDelBtn').bind('click', function() { fplan.del(); });
	$('#floorFormBtn').bind('click', function() { fplan.initForm(); fplan.initImage(); });
	$('#floorFormBtn2').bind('click', function() { fplan.test(); });
	$('.companyImageDelBtn').bind('click', function() { fplan.imageDel( $(this).attr('num') , $(this).attr('imgName')); });
	$("#floorModBtn").hide();
	$("#floorDelBtn").hide();
	$("#swLat").bind("change", function() { fplan.updateExtent(); });
	$("#swLng").bind("change", function() { fplan.updateExtent(); });
	$("#neLat").bind("change", function() { fplan.updateExtent(); });
	$("#neLng").bind("change", function() { fplan.updateExtent(); });
	$("#deg").bind("change", function() { fplan.updateExtent(); });

    cmmFloorSel._tgtObjName = "floor";
});

$(window).load(function(){
	//fplan.load();
	fplan.setFloorList();
	var degOption = [{value: 0.1, text: '0.1˚'},{value: 0.5, text: '0.5˚'},{value: 1, text: '1˚'},{value: 5, text: '5˚'},{value: 10, text: '10˚'}];
	$.gmap.addControl({id: 'degunit-combo-div', position:'top_center', combobox: { option: degOption, attr: {id: 'degUnit'}, style: { margin: '5px', height:'30px'} } });
	$.gmap.addControl({id: 'deg-input-div', position: 'top_center',
		input: {
			attr: {id: 'deg', name:'deg', size: '4', placeholder: 'degree'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"],
			change: function() { var id = $('#floorNum').val(); $.gmap.setOverlay({ id: id, deg: this.value }); }
		}
	});
	$.gmap.addControl({id: 'left-btn-div', position: 'top_center',
		button: {
			attr: { id:'leftBtn', value: '<', type: 'button'}, style:{ margin:'5px 5px 5px 15px', width:'30px' }, classes:["btn", "btn-info", "btn-sm"],
			mousedown: function() { fplan._flag = true; fplan.rotateImage("L"); },
			mouseup: function() { fplan._flag = false; clearInterval(fplan.interval); },
			mouseout: function() { fplan._flag = false; clearInterval(fplan.interval); }
		}
	});
	$.gmap.addControl({id: 'right-btn-div', position: 'top_center',
		button: {
			attr: { id:'rightBtn', value: '>', type: 'button'}, style:{ margin:'5px', width:'30px' }, classes:["btn", "btn-info", "btn-sm"],
			mousedown: function() { fplan._flag = true; fplan.rotateImage("R"); },
			mouseup: function() { fplan._flag = false; clearInterval(fplan.interval); },
			mouseout: function() { fplan._flag = false; clearInterval(fplan.interval); }
		}
	});
	$.gmap.addControl({id: 'swLat-input', position: 'left_top',
		input: {
			attr: {id: 'swLat', name:'swLat', size: 10, placeholder: 'swLat', type: 'swLat'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"],
			change: function() {
				var id = $('#floorNum').val();
				$.gmap.setOverlay({ id: id, bounds:[{lat:$("#swLat").val(), lng:$("#swLng").val()},{lat:$("#neLat").val(), lng:$("#neLng").val()}] });
			}
		}
	});
	$.gmap.addControl({id: 'swLng-input', position: 'left_top',
		input: {
			attr: {id: 'swLng', name:'swLng', size: 10, placeholder: 'swLng', type: 'swLng'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"],
			change: function() {
				var id = $('#floorNum').val();
				$.gmap.setOverlay({ id: id, bounds:[{lat:$("#swLat").val(), lng:$("#swLng").val()},{lat:$("#neLat").val(), lng:$("#neLng").val()}] });
			}
		}
	});
	$.gmap.addControl({id: 'neLat-input', position: 'left_top',
		input: {
			attr: {id: 'neLat', name:'neLat', size: 10, placeholder: 'neLat', type: 'neLat'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"],
			change: function() {
				var id = $('#floorNum').val();
				$.gmap.setOverlay({ id: id, bounds:[{lat:$("#swLat").val(), lng:$("#swLng").val()},{lat:$("#neLat").val(), lng:$("#neLng").val()}] });
			}
		}
	});
	$.gmap.addControl({id: 'neLng-input', position: 'left_top',
		input: {
			attr: {id: 'neLng', name:'neLng', size: 10, placeholder: 'neLng', type: 'neLng'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"],
			change: function() {
				var id = $('#floorNum').val();
				$.gmap.setOverlay({ id: id, bounds:[{lat:$("#swLat").val(), lng:$("#swLng").val()},{lat:$("#neLat").val(), lng:$("#neLng").val()}] });
			}
		}
	});
	$.gmap.addControl({id: 'lat-input', position: 'left_top',
		input: {
			attr: {id: 'lat', name:'lat', size: 10, placeholder: 'latitude', type: 'lat'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"]
		}
	});
	$.gmap.addControl({id: 'lng-input', position: 'left_top',
		input:  {
			attr: {id: 'lng', name:'lng', size: 10, placeholder: 'longtitude', type: 'lng'}, style: { margin: '5px', height: '30px'}, classes: ["gui-input"]
		}
	});
//	$("#lng-input").addClass("admin-form");


});

var fplan = {
	_floorURL: "/map/floor/floor.do",
	_listURL: "/map/floor/list.do",
	_dupURL: "/map/floor/dup.do",
	_infoURL: "/map/floor/info.do",
	_regURL: "/map/floor/reg.do",
	_modURL: "/map/floor/mod.do",
	_delURL: "/map/floor/del.do",
	_form: "#form1",
	_setmap : null,
	_floor: 1,
	_floorList: [],
	_initLat: 0,
	_initLng: 0,
	_initScale: 0.0003,
	_cnt: 0,
	_deg: 0,
	_flag: false,
	_interval: null,
	_attach: false,
	_mode: "R",
	reg: function() {
		var imgSize = 1048576 * 5;
		var imgFormat = "png|jpe?g|gif";
		$(fplan._form).validate({
			rules: {
				floor: { required: true},
				imgSrc: { extension: imgFormat, filesize: imgSize }
	        },
	        messages: {
	        	floor: { required: vm.required },
	        	imgSrc: { extension: vm.extension, filesize: vm.filesize }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});

		if($(fplan._form).valid()) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				url: fplan._regURL,
				data: new FormData($(fplan._form)[0]),
				success: fplan.regResult
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
				fplan.initForm();
				fplan.initFloor();
				fplan.initImage();
				fplan.setFloorList();
				common.success(vm.regSuccess);
				break;
			case "2" : common.error(vm.regFail); break;
			case "3" : common.error(vm.dupFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		var imgSize = 1048576 * 5;
		var imgFormat = "png|jpe?g|gif";
		$(this._form).validate({
			rules: {
				floor: { required: true},
				imgSrc: { extension: imgFormat, filesize: imgSize }
	        },
	        messages: {
	        	floor: { required: vm.required },
	        	imgSrc: { extension: vm.extension, filesize: vm.filesize }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				url: fplan._modURL,
				data: new FormData($(fplan._form)[0]),
				success: fplan.modResult
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
				fplan.initForm();
				fplan.initFloor();
				fplan.initImage();
				fplan.setFloorList();
				var floor = result.floor;
				fplan.setFloor(fplan.setFloorData(result.floor['floorNum']
												, result.floor['floor']
												, result.floor['swLat']
												, result.floor['swLng']
												, result.floor['neLat']
												, result.floor['neLng']
												, result.floor['deg']
												, result.floor['imgURL']));
				common.success(vm.modSuccess);
				break;
			case "2" : common.error(vm.modFail); break;
			case "3" : common.error(vm.dupFail); break;
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
		} else {
			result = data;
		}
		var msg = "";
		switch(result.result) {
			case "1" :
				fplan.initForm();
				fplan.initFloor();
				fplan.initImage();
				fplan.setFloorList();
				common.success(vm.delSuccess);
				break;
			case "2" : msg = common.error(vm.delFail); break;
			default : msg = common.error(vm.delError); break;
		}
	},
	initForm: function() {
		$('#floorNum').val("");
		$('#imgSrc').val("");
		$('#locaion1st').val("");
		$('#swLat').val(this.initSWLat);
		$('#swLng').val(this.initSWLng);
		$('#neLat').val(this.initNELat);
		$('#neLng').val(this.initNELng);
		$('#deg').val("");
		$('#floorForm').show("slow");
		$('#floorRegBtn').show();
		$('#floorModBtn').hide();
		$("#floorDelBtn").hide();
		$("#floor").attr("readonly", false);
		$(this._form).find(".fileupload-preview > img").attr("src", "");

		$("#floorLocationList").html("");
		cmmFloorSel.callLocationSel();
	},
	setForm: function(obj) {
		$('#floorNum').val(obj['floorNum']);
		$('#imgSrc').val("");
		$('#locaion1st').val("");
		$('#swLat').val(obj['swLat']);
		$('#swLng').val(obj['swLng']);
		$('#neLat').val(obj['neLat']);
		$('#neLng').val(obj['neLng']);
		$('#deg').val(obj['deg']);
		$('#floorForm').show("slow");
		$('#floorRegBtn').hide();
		$('#floorModBtn').show();
		$("#floorDelBtn").show();
		$("#floor").attr("readonly", true);

		$("#floorLocationList").html("");


		console.log('obj', obj);


		var floor = obj['floor'];
		var tgtSel, nxtDept;
		var rtlLctArr2 = [];

		rtlLctArr2 = cmmFloorSel.getLocationHierarchy(floor);

		if(rtlLctArr2 != null && rtlLctArr2.length>0){
			for(var idx in rtlLctArr2){
				tgtSel = cmmFloorSel.makeLocationSel(rtlLctArr2[idx].levelNo);
				nxtDept = cmmFloorSel._floorCodeArr.filter(function (value) {
										            	return (value.levelNo == rtlLctArr2[idx].levelNo);
										            });

				for(var j in nxtDept){
		            $(tgtSel).append("<option value='"+nxtDept[j].nodeId+"'>"+nxtDept[j].nodeName1+"</option>");
		        }

				$(tgtSel).val(rtlLctArr2[idx].nodeId);
			}
		}else{
			cmmFloorSel.callLocationSel();
		}
	},
	initFloor: function() {
		$("#floorList").html("");
	},
	setFloorList: function() {
		$.ajax({ type: "POST",
			contentType: 'application/x-www-form-urlencoded',
			processData: false,
			url: fplan._listURL,
			data: "",
			success: this.getFloorListResult
		});
	},
	getFloorListResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data)
		} else {
			result = data;
		}

		switch(result.result) {
			case "1" :
				var list = result.data;
				var cnt = list.length;
				if(cnt > 0) {
					for(var i=0; i<cnt; i++) {
						fplan.addFloor(fplan.setFloorData(list[i]['floorNum']
														, list[i]['floor']
														//, list[i]['floorName']
														, list[i]['swLat']
														, list[i]['swLng']
														, list[i]['neLat']
														, list[i]['neLng']
														, list[i]['deg']
														, list[i]['imgURL']));
						fplan._floorList[i] = list[i];
					}
				}
				break;
			default :
				common.error(vm.infoFail);
				break;
		}
	},
	setFloor: function(obj) {


		console.log('setFloor', obj);


		this.setForm(obj);
		this.initImage();
		if(!(obj['imgURL'] == "" || obj['imgURL'] == null || obj['imgURL'] == "undefinded")) {
			this.setImage(obj);
			this.editImage(obj);
			$('#deg').val(obj['deg']);
			this._deg = obj['deg'];
		}

		for(var id in this._floorList) {
			if(obj.floor == this._floorList[id].floor && obj.floorNum != this._floorList[id].floorNum) {
				var _obj = this.setFloorData(this._floorList[id].floorNum
											, this._floorList[id].floor
											, this._floorList[id].swLat
											, this._floorList[id].swLng
											, this._floorList[id].neLat
											, this._floorList[id].neLng
											, this._floorList[id].deg
											, this._floorList[id].imgURL);
				this.setImage(_obj);
			}
		}
	},
	setFloorData: function(floorNum, floor
							/*, floorName*/
							, swLat, swLng, neLat, neLng, deg, imgURL) {
		var obj = [];
		obj["floorNum"] = floorNum;
		obj["floor"] = floor;
		//obj["floorName"] = floorName;
		obj["swLat"] = swLat;
		obj["swLng"] = swLng;
		obj["neLat"] = neLat;
		obj["neLng"] = neLng;
		obj["deg"] = deg;
		obj["imgURL"] = imgURL;

		return obj;
	},
	addFloor: function(obj) {
		var div = $('<div />', {id:'floorDiv'+obj['floorNum']});
		var btn = $('<button />', {id:'floor'+obj['floorNum'], type: 'button'});
		$("#floorList").append(div);
		div.append(btn);
		console.log(obj['floor']);

		//get floor name from _floorCodeArr array
		var floorNameObj = cmmFloorSel._floorCodeArr.filter(function (item) {
	            										return (item.nodeId == obj['floor']);
	            									});
		console.log(floorNameObj);
		$("#floor"+obj['floorNum']).html((floorNameObj!= null && floorNameObj.length > 0)? floorNameObj[0].nodeName1: "");

		$("#floor"+obj['floorNum']).button().addClass('btn btn-default btn-block form-control');
		$("#floor"+obj['floorNum']).bind("click", function() { fplan.setFloor(obj); });
	},
	initImage: function() {
		/*try {
			for(var id in $.gmap.overlays) {
				$.gmap.removeOverlay({ id: id });
			}
			for(var id in $.gmap.rectangles) {
				$.gmap.removeRectangle({ id: id });
			}
		} catch(e) {
		}*/


		if(window.pntmap) {
			var om = window.pntmap.getObjectManager();
			var images = om.findTag('floor');
			for(var i=0; i<images.length; i++) {
				om.remove(pnt.map.object.type.IMAGE, images[i].getId());
			}
		}
	},
	setImage: function(obj) {
		if(obj['swLat'] == "" || obj['swLat'] == "0" || obj['swLat'] == 0 ||
			obj['swLng'] == "" || obj['swLng'] == "0" || obj['swLat'] == 0 ||
			obj['neLat'] == "" || obj['neLat'] == "0" || obj['neLat'] == 0 ||
			obj['neLng'] == "" || obj['neLng'] == "0" || obj['neLng'] == 0) {
			/*obj['swLat'] = this._initLat - this._initScale;
			obj['swLng'] = this._initLng - this._initScale;
			obj['neLat'] = this._initLat + this._initScale;
			obj['neLng'] = this._initLng + this._initScale;*/

			var center = pnt.util.transformLonLat(window.pntmap.getOlMap().getView().getCenter());
			obj['swLat'] = center[1] - this._initScale;
			obj['swLng'] = center[0] - this._initScale;
			obj['neLat'] = center[1] + this._initScale;
			obj['neLng'] = center[0] + this._initScale;
		}
		/*
		$.gmap.addOverlay({
			id: obj.floorNum,
			bounds:[{lat:obj.swLat, lng:obj.swLng},{lat:obj.neLat, lng:obj.neLng}],
			image: obj.imgURL,
			deg: obj.deg,
			opacity: 0.7
		});*/

		if(window.pntmap) {

			var om = window.pntmap.getObjectManager();
			var extent = [obj.swLng, obj.swLat, obj.neLng, obj.neLat];
			var option = {tag:'floor', extent: pnt.util.transformExtent(extent), url: obj.imgURL,
				angle:$('#deg').val(), tag:'floor', pixelRatio: 1};
			var image = om.create(pnt.map.object.type.IMAGE, 'floor-'+obj.floorNum, option);
			image.setOpacity(0.6);

			var size = window.pntmap.getOlMap().getSize();
			window.pntmap.getOlMap().getView().fit(pnt.util.transformExtent(extent), size);

			$('#swLat').val(obj.swLat);
			$('#swLng').val(obj.swLng);
			$('#neLat').val(obj.neLat);
			$('#neLng').val(obj.neLng);

		}
	},
	editImage: function(obj) {
		/*var id = obj.floorNum;
		$.gmap.drawRectangle({
			id: obj.floorNum,
			clickable: true,
			draggable: true,
			editable: true,
			fillOpacity: 0,
			zIndex: 10,
			bounds: [{lat:obj['swLat'], lng:obj['swLng']},{lat:obj['neLat'], lng:obj['neLng']}],
			bounds_changed: function() {
				var bounds = $.gmap.rectangles[obj.floorNum].getBounds();
				var sw = bounds.getSouthWest();
				var ne = bounds.getNorthEast();
				$.gmap.setOverlay({
					id: id,
					bounds: [{lat:sw.lat(), lng:sw.lng()}, {lat:ne.lat(), lng:ne.lng()}]
				});
				$("#swLat").val(sw.lat());
				$("#swLng").val(sw.lng());
				$("#neLat").val(ne.lat());
				$("#neLng").val(ne.lng());

			}
		});*/

		var om = window.pntmap.getObjectManager();
		var image = om.get(pnt.map.object.type.IMAGE, 'floor-'+obj.floorNum);
		image.setEditable(true);
	},
	rotateImage: function(type) {
		if(!this._flag) return;
		if(this._flag) this.interval = setInterval("fplan.rotateInterval('"+type+"')", 100);
	},
	rotateInterval: function(type) {
		var degUnit = $("#degUnit option:selected").val();
		if(type == "R") {
			this._deg = Number(this._deg) + Number(degUnit);
		}
		if(type == "L") {
			this._deg = Number(this._deg) - Number(degUnit);
		}
		this._deg = this._deg.toFixed(1);
		$("#deg").val(this._deg);
		$.gmap.setOverlay({
			id: $("#floorNum").val(),
			deg: this._deg
		});
	},
	updateExtent: function() {
		if($('#swLat').val()!='' && $('#swLng').val()!='' && $('#neLat').val()!='' && $('#neLng').val()!='') {
			var sw = [parseFloat($('#swLng').val()), parseFloat($('#swLat').val())];
			var ne = [parseFloat($('#neLng').val()), parseFloat($('#neLat').val())];
			var swCoordinates = pnt.util.transformCoordinates(sw);
			var neCoordinates = pnt.util.transformCoordinates(ne);

			var om = window.pntmap.getObjectManager();
			var floors = pntmap.getObjectManager().findTag('floor');
			if(typeof(floors)!='undefined' && floors!=null && floors.length>0) {
				var imgUrl = floors[0]._imageUrl;
				var objId = floors[0].getId();
				om.remove(pnt.map.object.type.IMAGE, floors[0].getId());

				var extent = [swCoordinates[0],swCoordinates[1],neCoordinates[0],neCoordinates[1]];
				console.log('extent', extent);
				var option = {tag:'floor', extent: extent, url: imgUrl,
					angle:$('#deg').val(), tag:'floor', pixelRatio: 1};
				var image = om.create(pnt.map.object.type.IMAGE, objId, option);
				image.setOpacity(0.6);
				image.setEditable(true);
			}



		}
	}
};
