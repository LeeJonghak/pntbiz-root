$(document).ready( function() {	
	if($('#codeActionKeyword').val() == "") $('#codeActionKeyword').focus();	
	$('#codeActionKeyword').bind('keyup', function(e) { if(e.keyCode==13) codeAction.search(); });	
	$('#codeActionFormBtn').bind('click', function() { codeAction.form(); });
	$('#codeActionMFormBtn').bind('click', function() { codeAction.mform(); });
	$('#codeActionSearchBtn').bind('click', function() { codeAction.search(); });
	$('#codeActionListBtn').bind('click', function() { codeAction.list(); });
	$('#codeActionRegBtn').bind('click', function() { codeAction.reg(); });
	$('#codeActionModBtn').bind('click', function() { codeAction.mod(); });
	$('#codeActionDelBtn').bind('click', function() { codeAction.del(); });
});

var codeAction = {
	_listURL: "/info/code/list.do",
	_formURL: "/info/code/form.do",
	_mformURL: "/info/code/mform.do",
	_regURL: "/info/code/reg.do",
	_modURL: "/info/code/mod.do",
	_delURL: "/info/code/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "codeType": ""});
		common.redirect(this._formURL);
	},
	mform: function(codeNum) {
		this._mformURL += common.setQueryString({"page": "", "codeType": "", "opt": "", "keyword": "", "codeNum":codeNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "codeType": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var codeType = $("#codeType option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#codeActionKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "codeType":codeType, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				codeType: { required: true },
				code: { required: true, maxlength: 30, alphanumeric: true },
				codeName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	codeType: { required: true },
				code: { required: true, maxlength: vm.maxlength, alphanumeric: vm.alphanumeric },
	        	codeName: { required: vm.required, maxlength: vm.maxlength }
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
				codeAction._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "codeType": ""});
				common.redirect(codeAction._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				codeType: { required: true },
				code: { required: true, maxlength: 30, alphanumeric: true },
				codeName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	codeType: { required: true },
				code: { required: true, maxlength: vm.maxlength, alphanumeric: vm.alphanumeric },
	        	codeName: { required: vm.required, maxlength: vm.maxlength }
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
				codeAction._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "codeType": ""});
				common.redirect(codeAction._listURL);
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
				codeAction._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "codeType": ""});
				common.redirect(codeAction._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
