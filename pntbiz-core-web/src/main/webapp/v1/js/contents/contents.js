$(document).ready( function() {
	if($('#contentsKeyword').val() == "") $('#contentsKeyword').focus();	
	$('#contentsKeyword').bind('keyup', function(e) { if(e.keyCode==13) contents.search(); });	
	$('#checkAll').bind('click', function() { common.checkboxId('#checkAll','checkbox'); });
	$('#contentsFormBtn').bind('click', function() { contents.form(); });
	$('#contentsMFormBtn').bind('click', function() { contents.mform(); });
	$('#contentsSearchBtn').bind('click', function() { contents.search(); });
	$('#contentsListBtn').bind('click', function() { contents.list(); });
	$('#contentsRegBtn').bind('click', function() { contents.reg(); });
	$('#contentsModBtn').bind('click', function() { contents.mod(); });
	$('#contentsDelBtn').bind('click', function() { contents.del(); });
	$('#conDesc').bind('keydown', function() { common.setTextLimit('conDesc', 'byteLimit', 200); });
	$('#conType').bind('change', function() { contents.setForm($('#conType').val()) });
	$('#selectBoxMapping').bind('change', function() { contents.mappingData(); });
	$('#contentsMappingBtn').bind('click', function() { contents.mapping(); });
	$('.contentsFileDelBtn').bind('click', 
		function() { 
			contents.fileDel($("#conNum").val(), $(this).attr('fileType'), $(this).attr('num'), $(this).parents("#form-"+$(this).attr('fileType')+$(this).attr('num'))); 
	});
	try {
		$('#acName').bind('keyup', function() { $('#acNum').val(""); });
		$('#acName').typeahead({
			minLength: 2,
			items: 100,
			source: function(query, process) {	contents.acList(query, process); },
			updater: function(item) { $('#acNum').val(acMap[item]); return item; }
		});		
	} catch(exception) {}
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm:SS'});
		$('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm:SS'});
	} catch(exception) {}

	// multiselect 박스(일괄매핑)
	$('#multiselect4').multiselect({
		disableIfEmpty: true,
    	enableFiltering: true
    });
	$('#multiselect1').multiselect();
	// multiselect 박스(일괄매핑)
});

var acMap= {};
var contents = {
	_acListURL : '/info/ac/listall.do',
	_listURL: "/contents/list.do",
	_mappingDataURL: "/contents/mappingData.ajax.do",
	_mappingURL: "/contents/mapping.ajax.do",
	_formURL: "/contents/form.do",
	_mformURL: "/contents/mform.do",
	_regURL: "/contents/reg.do",
	_modURL: "/contents/mod.do",
	_delURL: "/contents/del.do",
	_fileDelURL: "/contents/fileDel.do",
	_form: "#form1",
	_field: ["img1", "img2", "img3", "img4", "img5", "text1", "text2", "text3", "text4", "text5", 
	         "sound1", "sound2", "sound3", "url1", "url2", "url3"],
	_conField: {
		// URI형
		"URI" : [{"id" : "url1", "label" : "URL"}, {"id" : "sound1", "label" : "사운드"}],
		// 텍스트형
		"TXT" : [{"id" : "text1", "label" : "웰컴 메세지"}, {"id" : "sound1", "label" : "사운드"}],		
		// 이미지형
		"IMG" : [{"id" : "text1", "label" : "텍스트1"}, {"id" : "img1", "label" : "이미지"}, {"id" : "sound1", "label" : "사운드"}],	
		// 사운드형	
		"SND" : [{"id" : "sound1", "label" : "사운드"}],	
		// 웰컴
		"WEL" : [{"id" : "text1", "label" : "웰컴 메세지"}, {"id" : "sound1", "label" : "사운드"}],		
		// 굿바이
		"BYE" : [{"id" : "text1", "label" : "굿바이 메세지"}, {"id" : "sound1", "label" : "사운드"}],
		// 쿠폰 
		"CPN" : [{"id" : "img1", "label" : "BG-이미지"}, {"id" : "text1", "label" : "텍스트"}, {"id" : "sound1", "label" : "사운드"}],		
	    // 동영상
		"VDO" : [{"id" : "text1", "label" : "텍스트"}, {"id" : "sound1", "label" : "사운드"}, {"id" : "url1", "label" : "동영상URL"}],
		// 어린이
		"CHD" : [{"id" : "text1", "label" : "이름"}, {"id" : "img1", "label" : "사진"} ],
		// 노티
		"NTF" : [{"id" : "text1", "label" : "보내는메일주소"}, {"id" : "text2", "label" : "받는메일주소"}, {"id" : "text3", "label" : "제목"}, {"id" : "text4", "label" : "본문"}],
		// 자재
		"MAT" : [{"id" : "img1", "label" : "자재아이콘"}],
		// 복합
		"CPX" : [{"id" : "text1", "label" : "텍스트1"}, {"id" : "text2", "label" : "텍스트2"}, {"id" : "text3", "label" : "텍스트3"}, 
		         {"id" : "text4", "label" : "텍스트4"}, {"id" : "text5", "label" : "텍스트5"},
		         {"id" : "img1", "label" : "이미지1"}, {"id" : "img2", "label" : "이미지2"}, {"id" : "img3", "label" : "이미지3"}, 
		         {"id" : "img4", "label" : "이미지4"}, {"id" : "img5", "label" : "이미지5"},
		         {"id" : "sound1", "label" : "사운드1"}, {"id" : "sound2", "label" : "사운드2"}, {"id" : "sound3", "label" : "사운드3"}, 
		         {"id" : "url1", "label" : "URL1"}, {"id" : "url2", "label" : "URL2"}, {"id" : "url3", "label" : "URL3"}
		         
		],
		// 회원
		"MEM" : [{"id" : "img1", "label" : "회원사진"}, {"id" : "text1", "label" : "회원명"}, {"id" : "text2", "label" : "휴대폰번호"}]
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
	multselectReset: function(optionText) {
		var data = "<select id='multiselect4' multiple='multiple'> <optgroup label='Reference List'>"
		 		+ optionText +
		 		+ "</optgroup></select>";

		$(".multi-select").html(data);
	
		$('#multiselect4').multiselect({
			enableFiltering: true,
			disableIfEmpty: true,
			maxHeight: true,
			includeSelectAllOption : true,
			enableCaseInsensitiveFiltering: true,
			numberDisplayed: 1,
	        enableClickableOptGroups: true,
			selectAllText: ' All Selete',
			templates: {
				ul: '<ul class="multiselect-container dropdown-menu" style="max-height:550px;overflow:hidden;"></ul>'
			}
	    });
	},
	mappingData: function() {
		var mappingType = $("#selectBoxMapping option:selected").val();

		if(mappingType != "") {
			if(mappingType == 'GF' || mappingType == 'GFG') {
				$('#fcEventType').show();
				$("#fcEventType option:eq(0)").prop("selected", true);
			}else {
				$('#fcEventType').hide();
			}
			
			$.ajax({ type: "POST",
				url: this._mappingDataURL,
				data: 'mappingType=' + mappingType,
				success: this.mappingDataResult
			});

		}else {
			$('#fcEventType').hide();
			contents.multselectReset('');
		}

	},
	mappingDataResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				var _list = result.list;
				
				var optionText = '';
					
				for (var i = 0; i < _list.length; i++) {
					optionText = optionText + "<option value='"+_list[i].refNum+"'>"+_list[i].refName+"</option>";
                }

				contents.multselectReset(optionText);
				
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	},
	mapping: function() {
		var conNums = $('input[name=checkbox]:checked');			// 콘텐츠 번호
		var refNums = $("#multiselect4 option:selected");			// 매핑 대상 번호
		var mappingType = $("#selectBoxMapping option:selected").val();	// 매핑유형
		var evnetNum = $("#multiselect1 option:selected").val();	// 이벤트

		var fcEventType;
		if(mappingType == 'GF' || mappingType == 'GFG') {
			fcEventType = $("#fcEventType option:selected").val();	// 이벤트 타입
		}else {
			fcEventType = 'DETECT';
		}

		if(conNums.val() == undefined) {
			alert(vm.mappingContentsError);
			
		}else if(mappingType == '') {
			alert(vm.mappingTypeError);
			
		}else if(fcEventType == '') {
			alert(vm.mappingFcEventTypeError);
			
		}else if(refNums.val() == undefined) {
			alert(vm.mappingReferenceNoError);
			
		}else {
			var refNumList = [];
			refNums.each(function(index) {
				var $checkbox = $(this);
				refNumList[refNumList.length] = $checkbox.val();
			});
			 
			var conNumList = [];
			conNums.each(function(index) {
				var $checkbox = $(this);
				conNumList[conNumList.length] = $checkbox.val();
			});

			$.ajax({ type: "POST",
				url: this._mappingURL,
				data: $.param({mappingType:mappingType, refNums:refNumList, conNums:conNumList, fcEventType:fcEventType, evnetNum:evnetNum}, true),
				success: this.mappingResult
			});
		}

	},
	mappingResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				alert(vm.mappingSuccess);
				
				break;
			case "2" : common.error(vm.mappingFail); break;
			default : common.error(vm.mappingError); break;
		}
	},
	reg: function() {
		var imgSize = 1048576 * 2;
		var imgFormat = "png|jpe?g|gif";
		var soundSize = 1048576 * 5;
		var soundFormat = "mp3|wav|ogg";
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
		var soundSize = 1048576 * 5;
		var soundFormat = "mp3|wav|ogg";
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
	fileDel: function(conNum, fileType, num, form) {
		var result = confirm(vm.delConfirm);
		if(result) {
			switch(fileType) {
				case "img" : $(form).find(".fileupload-preview > img").attr("src", ""); break; // 이미지 초기화
				case "sound" : $(form).find(".fileupload-preview > img").attr("alt", ""); break; // alt 초기화
				default : break;
			}
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
			//case "1" : common.info(vm.delSuccess); $('#form-'+result.fileType+result.num).hide(); break; // basic bootstrap
			case "1" : common.info(vm.delSuccess); break; // absoulte bootstrap
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}	
	}
};
