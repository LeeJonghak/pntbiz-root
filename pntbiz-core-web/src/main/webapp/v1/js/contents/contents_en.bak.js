$(document).ready( function() {
	if($('#contentsKeyword').val() == "") $('#contentsKeyword').focus();	
	$('#contentsKeyword').bind('keyup', function(e) { if(e.keyCode==13) contents.search(); });	
	$('#contentsFormBtn').bind('click', function() { contents.form(); });
	$('#contentsMFormBtn').bind('click', function() { contents.mform(); });
	$('#contentsSearchBtn').bind('click', function() { contents.search(); });
	$('#contentsListBtn').bind('click', function() { contents.list(); });
	$('#contentsRegBtn').bind('click', function() { contents.reg(); });
	$('#contentsModBtn').bind('click', function() { contents.mod(); });
	$('#contentsDelBtn').bind('click', function() { contents.del(); });
	$('#desc').bind('keydown', function() { common.setTextLimit('desc', 'byteLimit', 200); });
	$('#conType').bind('change', function() { contents.setForm($('#conType').val()) });
	$('.contentsFileDelBtn').bind('click', function() { contents.fileDel($("#conNum").val(), $(this).attr('fileType'), $(this).attr('num')); });
	try {
		$('#acName').bind('keyup', function() { $('#acNum').val(""); });
		$('#acName').typeahead({
			minLength: 2,
			items: 100,
			source: function(query, process) {	contents.acList(query, process); },
			updater: function(item) { 	$('#acNum').val(acMap[item]); return item; }
		});		
	} catch(exception) {}
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm:SS'});
		$('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm:SS'});
	} catch(exception) {}
});

var acMap= {};
var contents = {
	_acListURL : '/info/ac/listall.do',
	_listURL: "/contents/list.do",
	_formURL: "/contents/form.do",
	_mformURL: "/contents/mform.do",
	_regURL: "/contents/reg.do",
	_modURL: "/contents/mod.do",
	_delURL: "/contents/del.do",
	_fileDelURL: "/contents/fileDel.do",
	_form: "#form1",
	_field: ["imgSrc1", "imgSrc2", "imgSrc3", "imgSrc4", "imgSrc5", "text1", "text2", "text3", "text4", "text5", 
	         "soundSrc1", "soundSrc2", "soundSrc3", "url1", "url2", "url3"],
	_conField: {
		// URI형
		"URI" : [{"id" : "url1", "label" : "URL"}, {"id" : "soundSrc1", "label" : "Sound"}],
		// 텍스트형
		"TXT" : [{"id" : "text1", "label" : "Message"}, {"id" : "soundSrc1", "label" : "Sound"}],		
		// 이미지형
		"IMG" : [{"id" : "text1", "label" : "Text1"}, {"id" : "imgSrc1", "label" : "Image"}, {"id" : "soundSrc1", "label" : "Sound"}],	
		// 사운드형	
		"SND" : [{"id" : "soundSrc1", "label" : "Sound"}],	
		// 웰컴
		"WEL" : [{"id" : "text1", "label" : "Welcome Message"}, {"id" : "soundSrc1", "label" : "Sound"}],		
		// 굿바이
		"BYE" : [{"id" : "text1", "label" : "Goodbye Message"}, {"id" : "soundSrc1", "label" : "Sound"}],
		// 쿠폰 
		"CPN" : [{"id" : "imgSrc1", "label" : "BG-Image"}, {"id" : "text1", "label" : "Text"}, {"id" : "soundSrc1", "label" : "Sound"}],		
	    // 동영상
		"VDO" : [{"id" : "text1", "label" : "Text"}, {"id" : "soundSrc1", "label" : "Sound"}, {"id" : "url1", "label" : "Video URL"}],
		// 어린이
		"CHD" : [{"id" : "text1", "label" : "Name"}, {"id" : "imgSrc1", "label" : "Picture"} ],
		// 노티
		"NTF" : [{"id" : "text1", "label" : "Sender Email"}, {"id" : "text2", "label" : "Receiver Email"}, {"id" : "text3", "label" : "Subject"}, {"id" : "text4", "label" : "Content"}],
		// 자재
		"MAT" : [{"id" : "imgSrc1", "label" : "Icon"}],
		// 복합
		"CPX" : [{"id" : "text1", "label" : "Text1"}, {"id" : "text2", "label" : "Text2"}, {"id" : "text3", "label" : "Text3"}, 
		         {"id" : "text4", "label" : "Text4"}, {"id" : "text5", "label" : "Text5"},
		         {"id" : "imgSrc1", "label" : "Image1"}, {"id" : "imgSrc2", "label" : "Image2"}, {"id" : "imgSrc3", "label" : "Image3"}, 
		         {"id" : "imgSrc4", "label" : "Image4"}, {"id" : "imgSrc5", "label" : "Image5"},
		         {"id" : "soundSrc1", "label" : "Sound1"}, {"id" : "soundSrc2", "label" : "Sound2"}, {"id" : "soundSrc3", "label" : "Sound3"}, 
		         {"id" : "url1", "label" : "URL1"}, {"id" : "url2", "label" : "URL2"}, {"id" : "url3", "label" : "URL3"}
		         
		],
		// 회원
		"MEM" : [{"id" : "imgSrc1", "label" : "Picture"}, {"id" : "text1", "label" : "Name"}, {"id" : "text2", "label" : "PhoneNumber"}]
	},	
	acList: function(query, process) {
		$.ajax({
			url: contents._acListURL, 
			type: 'POST',
			data: 'comNum=' + $('#comNum').val() + '&opt=acName' + '&keyword=' + $('#acName').val(),
			success: function(data) {
				var result = {};
				if(typeof(data)=='string') {
					result = $.parseJSON(data);
				} else {
					result = data;
				}
				var list = [];
				for(var i=0; i<result.data.length; i++) {
					acMap[result.data[i].acName] = result.data[i].acNum;
					list.push(result.data[i].acName);						
				}
				process(list);
			}
		});
	},
	initForm: function() {
		for(var i=0; i<this._field.length; i++) {
			var id = this._field[i];
			$("#form-"+id).hide();
			$("#label-"+id).html("");
			$("#"+id).attr("placeholder", "");
		}
	},
	setForm: function(conType) {
		if(conType != "") {
			var conField = this._conField[conType];
			this.initForm();
			for(var i=0; i<conField.length; i++) {
				var id = conField[i].id;
				var label = conField[i].label;
				$("#form-"+id).show();
				$("#label-"+id).html(label);
				$("#"+id).attr("placeholder", label);
			}
		}
	},
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
		common.redirect(this._formURL);
	},
	mform: function(conNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": "", "conNum":conNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var conType = $("#conType option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#contentsKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "conType": conType, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		var imgSize = 1048576 * 2;
		var imgFormat = "png|jpe?g|gif";
		var soundSize = 1048576 * 8;
		var soundFormat = "mp3|wav";
		$(this._form).validate({
			rules: {
				conType: { required: true },
				conName: { required: true, maxlength: 50 },
				imgSrc1: { extension: imgFormat, filesize: imgSize },
				imgSrc2: { extension: imgFormat, filesize: imgSize },
				imgSrc3: { extension: imgFormat, filesize: imgSize },
				imgSrc4: { extension: imgFormat, filesize: imgSize },
				imgSrc5: { extension: imgFormat, filesize: imgSize },
				soundSrc1: { extension: soundFormat, filesize: soundSize },
				soundSrc2: { extension: soundFormat, filesize: soundSize },
				soundSrc3: { extension: soundFormat, filesize: soundSize }
	        },
	        messages: {
	        	conType: { required: vm.required },
	        	conName: { required: vm.required, maxlength: vm.maxlength },
	        	imgSrc1: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc2: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc3: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc4: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc5: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc1: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc2: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc3: { extension: vm.extension, filesize: vm.filesize }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				url: this._regURL,
				data: new FormData($(this._form)[0]),
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
				contents._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contents._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		var imgSize = 1048576 * 2;
		var imgFormat = "png|jpe?g|gif";
		var soundSize = 1048576 * 8;
		var soundFormat = "mp3|wav";
		$(this._form).validate({
			rules: {
				conName: { required: true, maxlength: 50 },
				imgSrc1: { extension: imgFormat, filesize: imgSize },
				imgSrc2: { extension: imgFormat, filesize: imgSize },
				imgSrc3: { extension: imgFormat, filesize: imgSize },
				imgSrc4: { extension: imgFormat, filesize: imgSize },
				imgSrc5: { extension: imgFormat, filesize: imgSize },
				soundSrc1: { extension: soundFormat, filesize: soundSize },
				soundSrc2: { extension: soundFormat, filesize: soundSize },
				soundSrc3: { extension: soundFormat, filesize: soundSize }
	        },
	        messages: {
	        	conName: { required: vm.required, maxlength: vm.maxlength },
	        	imgSrc1: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc2: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc3: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc4: { extension: vm.extension, filesize: vm.filesize },
	        	imgSrc5: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc1: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc2: { extension: vm.extension, filesize: vm.filesize },
	        	soundSrc3: { extension: vm.extension, filesize: vm.filesize }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});		
		
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				url: this._modURL,
				data: new FormData($(this._form)[0]),
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
				contents._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contents._listURL);
				break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	},
	del: function() {
		var result = confirm(vm.delConfirm);
		if(result) {
			$.ajax({ type: "POST",
				contentType: false,
				processData: false,
				url: this._delURL,
				data: new FormData($(this._form)[0]),
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
		switch(result.result) {
			case "1" :
				contents._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contents._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	},
	fileDel: function(conNum, fileType, num) {
		var result = confirm(vm.delConfirm);
		if(result) {
			var data = "conNum=" + conNum + "&fileType=" + fileType + "&num=" + num;
			$.ajax({ type: "POST", url: this._fileDelURL, data: data, success: this.fileDelResult });	
		}
	},	
	fileDelResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" : common.info(vm.delSuccess); $('#form-'+result.fileType+result.num).hide(); break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
