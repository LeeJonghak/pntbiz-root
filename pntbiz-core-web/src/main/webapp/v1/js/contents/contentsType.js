$(document).ready( function() {
	if($('#contentsTypeKeyword').val() == "") $('#contentsTypeKeyword').focus();	
	$('#contentsTypeKeyword').bind('keyup', function(e) { if(e.keyCode==13) contentsType.search(); });	
	$('#contentsTypeFormBtn').bind('click', function() { contentsType.form(); });
	$('#contentsTypeMFormBtn').bind('click', function() { contentsType.mform(); });
	$('#contentsTypeSearchBtn').bind('click', function() { contentsType.search(); });
	$('#contentsTypeListBtn').bind('click', function() { contentsType.list(); });
	$('#contentsTypeRegBtn').bind('click', function() { contentsType.reg(); });
	$('#contentsTypeModBtn').bind('click', function() { contentsType.mod(); });
	$('#contentsTypeDelBtn').bind('click', function() { contentsType.del(); });
	$('#contentsTypeCompAddBtn').bind('click', function() { contentsType.addComp(); });
	
	try {
		$('#typeDesc').bind('keydown', function() { common.setTextLimit('typeDesc', 'byteLimit', 200); });
		if($('#typeDesc').length > 0) common.setTextLimit('typeDesc', 'byteLimit', 200);
	} catch(e) {}
	try {
		$('#nestable').nestable({horizontal:false,vertical:true});
		// Init Nestable on list 1
		$('#nestable').nestable({ group: 1 }).on('change', contentsType.updateOutput);
		// nestable serialized output functionality
		contentsType.updateOutput($('#nestable').data('output', $('#fieldJson')));
	} catch(e) {}
});

var contentsType = {
	_listURL: "/contents/type/list.do",
	_formURL: "/contents/type/form.do",
	_mformURL: "/contents/type/mform.do",
	_regURL: "/contents/type/reg.do",
	_modURL: "/contents/type/mod.do",
	_delURL: "/contents/type/del.do",
	_form: "#form1",	
	getComp: function(compType, compField, compName, compNum) {	
		/*
		<li class="dd-item dd-info" data-id="H">
			<div class="dd-handle"><span class="glyphicons glyphicons-hospital_h"> Html</span></div>
			<div class="dd-content">
				<div class="col-sm-5">
					<input type="text" name="compName[]" value="" required="required" size="30" maxlength="25" class="form-control input-sm" placeholder="<spring:message code="word.beacon.name" />" required />
				</div>
				<div class="col-sm-5">
					<input type="text" name="compField[]" value="" required="required" size="30" maxlength="25" class="form-control input-sm" placeholder="<spring:message code="word.beacon.name" />" required />
				</div>
				<div class="col-sm-2">
					<span class="glyphicons glyphicons-circle_remove" style="cursor: pointer;"></span>
				</div>
			</div>
		</li>
		 */		
		var li = document.createElement("li");
		$(li).addClass("dd-item dd-info").attr("data-id", compType);		
		var handleDiv = document.createElement("div");
		$(handleDiv).addClass("dd-handle");		
		var handleSpan = document.createElement("span");
		$(handleSpan).addClass("glyphicons");
		var contentDiv = document.createElement("div");
		$(contentDiv).addClass("dd-content");		
		var fieldDiv = document.createElement("div");
		$(fieldDiv).addClass("col-sm-5");
		var fieldInput = document.createElement("input");
		$(fieldInput).attr("name", "field");
		$(fieldInput).attr("size", "30");
		$(fieldInput).attr("maxlength", "25");
		$(fieldInput).attr("placeholder", "field");
//		$(fieldInput).attr("required", true);
		$(fieldInput).addClass("form-control input-sm");		
		if(typeof(compField) != "undefined") $(fieldInput).attr("value", compField);
		$(fieldDiv).append($(fieldInput));
		var nameDiv = document.createElement("div");
		$(nameDiv).addClass("col-sm-5");
		var nameInput = document.createElement("input");
		$(nameInput).attr("name", "fieldName");
		$(nameInput).attr("size", "30");
		$(nameInput).attr("maxlength", "25");
		$(nameInput).attr("placeholder", "field name");
//		$(nameInput).attr("required", true);
		$(nameInput).addClass("form-control input-sm");
		if(typeof(compName) != "undefined") $(nameInput).attr("value", compName);
		$(nameDiv).append($(nameInput));		
		$(contentDiv).append($(fieldDiv));
		$(contentDiv).append($(nameDiv));
		if(typeof(compNum) != "undefined") {
			var numDiv = document.createElement("div");
			$(numDiv).addClass("col-sm-2 hidden");
			var numInput = document.createElement("input");
			$(numInput).attr("name", "fieldNum");
			$(numInput).attr("value", compNum);
			$(numDiv).append($(numInput));
			$(contentDiv).append($(numDiv));
		} else {
			var typeDiv = document.createElement("div");
			$(typeDiv).addClass("hidden");
			var typeInput = document.createElement("input");
			$(typeInput).attr("name", "fieldType");
			$(typeInput).attr("value", compType);
			$(typeDiv).append($(typeInput));
			$(contentDiv).append($(typeDiv));			
			var removeDiv = document.createElement("div");
			$(removeDiv).addClass("col-sm-2");
			var removeSpan = document.createElement("span");
			$(removeSpan).addClass("glyphicons glyphicons-circle_remove");
			$(removeSpan).css("cursor", "pointer");
			$(removeSpan).click(function() { contentsType.delComp($(this)); });
			$(removeDiv).append($(removeSpan));
			$(contentDiv).append($(removeDiv));
		}
		switch(compType) {
			case "I":
				$(handleSpan).addClass("glyphicons-picture");
				$(handleSpan).html(" Image");
				$(handleDiv).append($(handleSpan));
				break;
			case "U":
				$(handleSpan).addClass("glyphicons-link");
				$(handleSpan).html(" URL");
				$(handleDiv).append($(handleSpan));
				break;
			case "V":
				$(handleSpan).addClass("glyphicons-film");
				$(handleSpan).html(" Vedio");
				$(handleDiv).append($(handleSpan));
				break;
			case "S":
				$(handleSpan).addClass("glyphicons-volume_up");
				$(handleSpan).html(" Sound");
				$(handleDiv).append($(handleSpan));
				break;
			case "T":
				$(handleSpan).addClass("glyphicons-font");
				$(handleSpan).html(" Text");
				$(handleDiv).append($(handleSpan));
				break;
			case "A":
				$(handleSpan).addClass("glyphicons-notes");
				$(handleSpan).html(" Textarea");
				$(handleDiv).append($(handleSpan));
				break;
			case "H":
				$(handleSpan).addClass("glyphicons-hospital_h");
				$(handleSpan).html(" Html");
				$(handleDiv).append($(handleSpan));
				break;
		}
		$(li).append($(handleDiv));
		$(li).append($(contentDiv));
		return li;
	},	
	addComp: function() {
		var compType = $("#compType option:selected").val();
		var li = this.getComp(compType);
//		console.log(li);
		$("#nestable > ol").last().append($(li));
		this.updateOutput($('#nestable').data('output', $('#fieldJson')));
		
	},
	setComp: function(compType, compField, compName, compNum) {
		var li = this.getComp(compType, compField, compName, compNum);
		$("#nestable > ol").last().append($(li));
		this.updateOutput($('#nestable').data('output', $('#fieldJson')));
	},
	delComp: function(obj) {
		obj.parent().parent().parent().remove();
		this.updateOutput($('#nestable').data('output', $('#fieldJson')));
	},
	updateOutput: function(e) {
		var list = e.length ? e : $(e.target);
		var output = list.data('output');
		try {
			if (window.JSON) {
				output.val(window.JSON.stringify(list.nestable('serialize'))); //, null, 2));
			} else {
				common.error('JSON browser support required for this demo.');
			}
		} catch(e) {}
	},
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(typeNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "typeNum":typeNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var conType = $("#conType option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#contentsTypeKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				typeName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	typeName: { required: vm.required, maxlength: vm.maxlength }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {
	        }
		});
		if($(this._form).valid()) {
			// 항목 추가 체크
			if($("#fieldJson").val() == "[]") {
				common.error(vm.regComboboxAddError);
				return false;
			}
//			$('input[name=field]').each(function () {
//				$(this).rules('add', { required: true, messages: { required: vm.required } });
//			}
			// 항목 필드 체크
			var chk = true;
			$('input[name*=field]').each(function () {
				if($.trim($(this).val()) == "") {
					chk = false;
				}
		    });
			if(chk == false) {
				common.error(vm.regArrayInputError);
				return false;
			}
//			alert(this._regURL);
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
				contentsType._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contentsType._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				typeName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	typeName: { required: vm.required, maxlength: vm.maxlength }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {
	        }
		});
		if($(this._form).valid()) {
			// 항목 추가 체크
			if($("#fieldJson").val() == "[]") {
				common.error(vm.regComboboxAddError);
				return false;
			}
//			$('input[name=field]').each(function () {
//				$(this).rules('add', { required: true, messages: { required: vm.required } });
//			}
			// 항목 필드 체크
			var chk = true;
			$('input[name*=field]').each(function () {
				if($.trim($(this).val()) == "") {
					chk = false;
				}
		    });
			if(chk == false) {
				common.error(vm.regArrayInputError);
				return false;
			}
//			alert(this._regURL);
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
				contentsType._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contentsType._listURL);
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
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				contentsType._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": ""});
				common.redirect(contentsType._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
