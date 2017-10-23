$(document).ready( function() {		
	$('#authpackageFormBtn').bind('click', function() { authpackage.form(); });
	$('#authpackageMFormBtn').bind('click', function() { authpackage.mform(); });
	$('#authpackageSearchBtn').bind('click', function() { authpackage.search(); });
	$('#authpackageListBtn').bind('click', function() { authpackage.list(); });
	$('#authpackageRegBtn').bind('click', function() { authpackage.reg(); });
	$('#authpackageModBtn').bind('click', function() { authpackage.mod(); });
	$('#authpackageDelBtn').bind('click', function() { authpackage.del(); });
});

var authpackage = {
	_listURL: "/admin/authpackage/list.do",
	_formURL: "/admin/authpackage/form.do",
	_mformURL: "/admin/authpackage/mform.do",
	_regURL: "/admin/authpackage/reg.do",
	_modURL: "/admin/authpackage/mod.do",
	_delURL: "/admin/authpackage/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(packageName) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "packageName":packageName});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#authpackageKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		var vm = $.validator.messages;
		$(this._form).validate({
			rules: {
				packageName: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	packageName: { required: vm.required, maxlength: vm.maxlength }
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
				authpackage._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(authpackage._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			case "3" : common.error(vm.dupFail); break;
			default : common.error(vm.dupError); break;
		}
	},
	mod: function() {
		var vm = $.validator.messages;
		$(this._form).validate({
			rules: {
				packageName: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	packageName: { required: vm.required, maxlength: vm.maxlength }
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
		var msg = "";
		switch(result.result) {
			case "1" :
				authpackage._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(authpackage._listURL);
				break;
			case "2" : common.error(vm.modFail); break;
			case "2" : common.error(vm.dulFail); break;
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
				authpackage._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(authpackage._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}	
};
