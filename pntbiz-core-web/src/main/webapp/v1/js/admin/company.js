$(document).ready( function() {
	if($('#companyKeyword').val() == "") $('#companyKeyword').focus();	
	$('#companyKeyword').bind('keyup', function(e) { if(e.keyCode==13) company.search(); });	
	$('#companyFormBtn').bind('click', function() { company.form(); });
	$('#companySearchBtn').bind('click', function() { company.search(); });
	$('#companyListBtn').bind('click', function() { company.list(); });
	$('#companyRegBtn').bind('click', function() { company.reg(); });
	$('#companyModBtn').bind('click', function() { company.mod(); });
	$('#companyDelBtn').bind('click', function() { company.del(); });
});

var company = {
	_listURL: "/admin/company/list.do",
	_formURL: "/admin/company/form.do",
	_mformURL: "/admin/company/mform.do",
	_regURL: "/admin/company/reg.do",
	_modURL: "/admin/company/mod.do",
	_delURL: "/admin/company/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	}, 
	mform: function(comNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "comNum":comNum});
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
				UUID: { required: true },
				comName: { required: true },
				role: { required: true }
	        },
	        messages: {
	        	UUID: { required: vm.required },        	
	        	comName: { required: vm.required },
	        	role: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._regURL,
				data: $(company._form).serialize(),
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
		var msg = "";
		switch(result.result) {
			case "1" : 
				company._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(company._listURL); 
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		var vm = $.validator.messages;
		$(this._form).validate({
			rules: {
				UUID: { required: true },
				comName: { required: true },
				role: { required: true }
	        },
	        messages: {
	        	UUID: { required: vm.required },        	
	        	comName: { required: vm.required },
	        	role: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._modURL,
				data: $(company._form).serialize(),
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
				company._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(company._listURL); 
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
				data: $("#form1").serialize(),
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
				company._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(company._listURL); 
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
