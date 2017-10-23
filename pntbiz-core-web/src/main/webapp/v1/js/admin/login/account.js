$(document).ready( function() {
	if($('#loginAccountKeyword').val() == "") $('#loginAccountKeyword').focus();
	$('#loginAccountKeyword').bind('keyup', function(e) { if(e.keyCode==13) loginAccount.search(); });
	$('#loginAccountFormBtn').bind('click', function() { loginAccount.form(); });
	$('#loginAccountMFormBtn').bind('click', function() { loginAccount.mform(); });
	$('#loginAccountSearchBtn').bind('click', function() { loginAccount.search(); });
	$('#loginAccountListBtn').bind('click', function() { loginAccount.list(); });
	$('#loginAccountRegBtn').bind('click', function() { loginAccount.reg(); });
	$('#loginAccountModBtn').bind('click', function() { loginAccount.mod(); });
	$('#loginAccountDelBtn').bind('click', function() { loginAccount.del(); });
});

var loginAccount = {
	_listURL: "/admin/login/account/list.do",
	_formURL: "/admin/login/account/form.do",
	_mformURL: "/admin/login/account/mform.do",
	_regURL: "/admin/login/account/reg.do",
	_modURL: "/admin/login/account/mod.do",
	_delURL: "/admin/login/account/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	}, 
	mform: function(userID) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "userID":userID});
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
				userID: { required: true, maxlength: 20, minlength: 4, alphanumeric: true },
				userPW: { required: true, maxlength: 30, minlength: 8, pwd: true },
				userName: { required: true, maxlength: 25 },
				roleNum: { required: true }
	        },
	        messages: {
	        	userID: { required: vm.required, maxlength: vm.maxlength, minlength: vm.minlength, alphanumeric: vm.alphanumeric },        	
	        	userPW: { required: vm.required, maxlength: vm.maxlength, minlength: vm.minlength, pwd: vm.pwd },
	        	userName: { required: vm.required, maxlength: vm.maxlength },
	        	roleNum: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._regURL,
				data: $(loginAccount._form).serialize(),
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
				loginAccount._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAccount._listURL); 
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		var vm = $.validator.messages;

        var valid = {
            rules: {
                userID: { required: true, maxlength: 20, minlength:4, alphanumeric: true },
                userName: { required: true, maxlength: 25 },
                roleNum: { required: true }
            },
            messages: {
                userID: { required: vm.required, maxlength: vm.maxlength, alphanumeric: vm.alphanumeric },
                userName: { required: vm.required, maxlength: vm.maxlength },
                roleNum: { required: vm.required }
            },


            submitHandler: function (frm) {},
            success: function (e) {}
        };
        if($('#userPW').val().length>0) {
            $('#userPW').val($('#userPW').val().trim());
            valid.rules.userPW = { maxlength: 30, minlength:8, pwd: true};
            valid.messages.userPW = { maxlength: vm.maxlength, pwd: vm.pwd };
        }

		$(this._form).validate(valid);
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._modURL,
				data: $(loginAccount._form).serialize(),
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
				loginAccount._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAccount._listURL); 
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
				loginAccount._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(loginAccount._listURL); 
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
