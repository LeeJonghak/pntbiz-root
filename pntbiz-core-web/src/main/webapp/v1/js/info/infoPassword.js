$(document).ready( function() {
	$('#infoPasswordModBtn').bind('click', function() { infoPassword.mod(); });	
});

var infoPassword = {
	_mformURL: "/info/passwd/mform.do",
	_modURL: "/info/passwd/mod.do",
	_form: "#form1",
	mod: function() {
		var valid = {
			rules: {
				userName: { required: true, maxlength: 25 },
				userPW: { required: true, maxlength: 30, minlength:8, pwd: true },
				userEmail: { required: true, maxlength: 50, email: true },
				userPhoneNum: { required: true, minlength:11, digits: true }
			},
			messages: {
				userName: { required: vm.required, maxlength: vm.maxlength },
				userPW: { required: vm.required, maxlength: vm.maxlength, minlength: vm.minlength, pwd: vm.pwd },
				userEmail: { required: vm.required, maxlength: vm.maxlength, email:vm.email },
				userPhoneNum: { required: vm.required, minlength: vm.minlength, digits:vm.digits }
			},
			submitHandler: function (frm) {},
			success: function (e) {}
		};
		$(this._form).validate(valid);
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
				common.success(vm.modSuccess);
				alert(vm.otpPasswordChange); 
				common.redirect("/"); 
				break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	}
};
