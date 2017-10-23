$(document).ready( function() {
	if($('#codeKeyword').val() == "") $('#codeKeyword').focus();	
	$('#codeKeyword').bind('keyup', function(e) { if(e.keyCode==13) code.search(); });	
	$('#codeFormBtn').bind('click', function() { code.form(); });
	$('#codeMFormBtn').bind('click', function() { code.mform(); });
	$('#codeSearchBtn').bind('click', function() { code.search(); });
	$('#codeListBtn').bind('click', function() { code.list(); });
	$('#codeRegBtn').bind('click', function() { code.reg(); });
	$('#codeModBtn').bind('click', function() { code.mod(); });
	$('#codeDelBtn').bind('click', function() { code.del(); });
});


var code = {
	_listURL: "/admin/code/list.do",
	_formURL: "/admin/code/form.do",
	_mformURL: "/admin/code/mform.do",
	_regURL: "/admin/code/reg.do",
	_modURL: "/admin/code/mod.do",
	_delURL: "/admin/code/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(gCD, sCD) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "gCD":gCD, "sCD":sCD});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#codeKeyword").val());
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
				gCD: { required: true, maxlength: 10 },
				gName: { required: true, maxlength: 20 },
				sCD: { required: true, maxlength: 10 },
				sName: { required: true, maxlength: 20 }
	        },
	        messages: {
	        	gCD: { required: vm.required, maxlength: vm.maxlength },
	        	gName: { required: vm.required, maxlength: vm.maxlength },
	        	sCD: { required: vm.required, maxlength: vm.maxlength },
	        	sName: { required: vm.required, maxlength: vm.maxlength }
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
				code._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(code._listURL);
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
				gCD: { required: true, maxlength: 10 },
				gName: { required: true, maxlength: 20 },
				sCD: { required: true, maxlength: 10 },
				sName: { required: true, maxlength: 20 }
	        },
	        messages: {
	        	gCD: { required: vm.required, maxlength: vm.maxlength },
	        	gName: { required: vm.required, maxlength: vm.maxlength },
	        	sCD: { required: vm.required, maxlength: vm.maxlength },
	        	sName: { required: vm.required, maxlength: vm.maxlength }
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
				code._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(code._listURL);
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
				code._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(code._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
