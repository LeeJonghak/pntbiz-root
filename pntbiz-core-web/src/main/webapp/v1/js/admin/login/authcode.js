$(document).ready( function() {
	if($('#loginAuthcodeKeyword').val() == "") $('#loginAuthcodeKeyword').focus();
	$('#loginAuthcodeKeyword').bind('keyup', function(e) { if(e.keyCode==13) loginAuthcode.search(); });
	$('#loginAuthcodeFormBtn').bind('click', function() { loginAuthcode.form(); });
	$('#loginAuthcodeMFormBtn').bind('click', function() { loginAuthcode.mform(); });
	$('#loginAuthcodeSearchBtn').bind('click', function() { loginAuthcode.search(); });
	$('#loginAuthcodeListBtn').bind('click', function() { loginAuthcode.list(); });
	$('#loginAuthcodeRegBtn').bind('click', function() { loginAuthcode.reg(); });
	$('#loginAuthcodeModBtn').bind('click', function() { loginAuthcode.mod(); });
	$('#loginAuthcodeDelBtn').bind('click', function() { loginAuthcode.del(); });	
});

var loginAuthcode = {
	_listURL: "/admin/login/authcode/list.do",
	_formURL: "/admin/login/authcode/form.do",
	_mformURL: "/admin/login/authcode/mform.do",
	_regURL: "/admin/login/authcode/reg.do",
	_modURL: "/admin/login/authcode/mod.do",
	_delURL: "/admin/login/authcode/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	}, 
	mform: function(authNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "authNum":authNum});
		common.redirect(this._mformURL);
	}, 
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},	
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#companyKeyword").val());
		if(opt != "") {
			if(keyword != "") {				
				this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
			}
		} else {
			this._listURL += common.setQueryString({"page": 1});
		}
		common.redirect(this._listURL);
	},
	reg: function() {
		var vm = $.validator.messages;
		$(this._form).validate({
			rules: {
				authName: { required: true, maxlength: 50 },
				authCode: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	authName: { required: vm.required, maxlength: vm.maxlength },        	
	        	authCode: { required: vm.required, maxlength: vm.maxlength }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._regURL,
				data: $(loginAuthcode._form).serialize(),
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
				loginAuthcode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAuthcode._listURL); 
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		var vm = $.validator.messages;
		$(this._form).validate({
			rules: {
				authName: { required: true, maxlength: 50 },
				authCode: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	authName: { required: vm.required, maxlength: vm.maxlength },        	
	        	authCode: { required: vm.required, maxlength: vm.maxlength }
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
				loginAuthcode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAuthcode._listURL); 
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
				loginAuthcode._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAuthcode._listURL); 
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
