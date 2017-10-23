$(document).ready( function() {	
	if($('#advertCompanyKeyword').val() == "") $('#advertCompanyKeyword').focus();	
	$('#advertCompanyKeyword').bind('keyup', function(e) { if(e.keyCode==13) advertCompany.search(); });	
	$('#advertCompanyFormBtn').bind('click', function() { advertCompany.form(); });
	$('#advertCompanyMFormBtn').bind('click', function() { advertCompany.mform(); });
	$('#advertCompanySearchBtn').bind('click', function() { advertCompany.search(); });
	$('#advertCompanyListBtn').bind('click', function() { advertCompany.list(); });
	$('#advertCompanyRegBtn').bind('click', function() { advertCompany.reg(); });
	$('#advertCompanyModBtn').bind('click', function() { advertCompany.mod(); });
	$('#advertCompanyDelBtn').bind('click', function() { advertCompany.del(); });
});

var advertCompany = {
	_listURL: "/info/ac/list.do",
	_formURL: "/info/ac/form.do",
	_mformURL: "/info/ac/mform.do",
	_regURL: "/info/ac/reg.do",
	_modURL: "/info/ac/mod.do",
	_delURL: "/info/ac/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(acNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "acNum":acNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#advertCompanyKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				acName: { required: true }
	        },
	        messages: {
	        	acName: { required: vm.required }
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
				advertCompany._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(advertCompany._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				acName: { required: true, maxlength: 50 }
	        },
	        messages: {
	        	acName: { required: vm.required, maxlength: vm.maxlength }
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
				advertCompany._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(advertCompany._listURL);
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
				advertCompany._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(advertCompany._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
