$(document).ready( function() {
	$('#floorCodeFormBtn').bind('click', function() { floorCode.form(); });
	$('#floorCodeMFormBtn').bind('click', function() { floorCode.mform(); });
	$('#floorCodeSearchBtn').bind('click', function() { floorCode.search(); });
	$('#floorCodeListBtn').bind('click', function() { floorCode.list(); });
	$('#floorCodeRegBtn').bind('click', function() { floorCode.reg(); });
	$('#floorCodeModBtn').bind('click', function() { floorCode.mod(); });
	$('#floorCodeDelBtn').bind('click', function() { floorCode.del(); });

    cmmFloorSel._tgtObjName = "upperNodeId";
});

var floorCode = {
	_listURL: "/map/floorCode/list.do",
	_formURL: "/map/floorCode/form.do",
	_mformURL: "/map/floorCode/mform.do",
	_regURL: "/map/floorCode/reg.do",
	_modURL: "/map/floorCode/mod.do",
	_delURL: "/map/floorCode/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(codeNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#floorCodeKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				nodeId: { required: true, maxlength: 10 },
				nodeName1: { required: true, maxlength: 100 },
				nodeField: { maxlength: 50 },
				sortNo: { digits: true }
	        },
	        messages: {
	        	nodeId: { required: vm.required, maxlength: vm.maxlength},
	        	nodeName1: { required: vm.required, maxlength: vm.maxlength},
				nodeField: { maxlength: vm.maxlength },
	        	sortNo: { digits: vm.digits}
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			var levelNo = $(".selLct").size();
			var upperNodeObj = $("select[name=upperNodeId]");

			if($(upperNodeObj).val() == null || $(upperNodeObj).val() ==''){
				levelNo = levelNo -1;

				if(levelNo > 0){
					$(upperNodeObj).find("option:selected").val($(".selLct").eq(levelNo-1).val());
				}
			}
			if(levelNo < 0){
				levelNo = 0;
			}

			$("#levelNo").val(levelNo);
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
				floorCode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.success(vm.regSuccess);
				common.redirect(floorCode._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			case "3" : common.error(vm.dupFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				nodeId: { required: true, maxlength: 10},
				nodeName1: { required: true, maxlength: 100 },
				nodeField : { maxlength: 50 },
				sortNo: { digits: true }
	        },
	        messages: {
	        	nodeId: { required: vm.required, maxlength: vm.maxlength},
	        	nodeName1: { required: vm.required, maxlength: vm.maxlength},
				nodeField : { maxlength: vm.maxlength },
	        	sortNo: { digits: vm.digits}
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
				floorCode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(floorCode._listURL);
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
		switch(result.result) {
			case "1" :
				floorCode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(floorCode._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
