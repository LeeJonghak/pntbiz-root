$(document).ready( function() {
	if($('#loginRoleKeyword').val() == "") $('#loginRoleKeyword').focus();
	$('#loginRoleKeyword').bind('keyup', function(e) { if(e.keyCode==13) loginRole.search(); });
	$('#loginRoleFormBtn').bind('click', function() { loginRole.form(); });
	$('#loginRoleMFormBtn').bind('click', function() { loginRole.mform(); });
	$('#loginRoleSearchBtn').bind('click', function() { loginRole.search(); });
	$('#loginRoleListBtn').bind('click', function() { loginRole.list(); });
	$('#loginRoleRegBtn').bind('click', function() { loginRole.reg(); });
	$('#loginRoleModBtn').bind('click', function() { loginRole.mod(); });
	$('#loginRoleDelBtn').bind('click', function() { loginRole.del(); });
});

var loginRole = {
	_listURL: "/admin/login/role/list.do",
	_formURL: "/admin/login/role/form.do",
	_mformURL: "/admin/login/role/mform.do",
	_regURL: "/admin/login/role/reg.do",
	_modURL: "/admin/login/role/mod.do",
	_delURL: "/admin/login/role/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	}, 
	mform: function(roleNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "roleNum":roleNum});
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
				roleName: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	roleName: { required: vm.required, maxlength: vm.maxlength, alphanumeric: vm.alphanumeric }
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
				loginRole._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginRole._listURL); 
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				roleName: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	roleName: { required: vm.required, maxlength: vm.maxlength, alphanumeric: vm.alphanumeric }
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
				loginRole._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginRole._listURL); 
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
				loginRole._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginRole._listURL); 
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
