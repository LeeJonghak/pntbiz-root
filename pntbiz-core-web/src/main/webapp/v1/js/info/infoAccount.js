$(document).ready( function() {
	$('#infoAccountModBtn').bind('click', function() { infoAccount.mod(); });	
	$('#infoAccountItsmeBtn').bind('click', function() { infoAccount.itsme(); });
});

var infoAccount = {
	_itsmeURL: "/info/account/itsme.do",
	_itsmeokURL: "/info/account/itsmeok.do",
	_mformURL: "/info/account/mform.do",
	_modURL: "/info/account/mod.do",
	_form: "#form1",
	itsme: function() {
		var valid = {
			rules: {
				userPW: { required: true, maxlength: 30, minlength:8, pwd: true }
			},
			messages: { 
				userPW: { required: vm.required, maxlength: vm.maxlength, minlength: vm.minlength, pwd: vm.pwd }
			},
			submitHandler: function (frm) {},
			success: function (e) {}
		};
		$(this._form).validate(valid);
		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._itsmeokURL,
				data: $(this._form).serialize(),
				success: this.itsmeResult
			});
		}
	},
	itsmeResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" : 
				common.success(vm.itsmeSuccess);
				$('#key').val(result.key);
				$(infoAccount._form).attr('method', 'post');
				$(infoAccount._form).attr('action', infoAccount._mformURL);
				$(infoAccount._form).unbind('submit').submit();
//	var $form = $('<form></form>');
//	$form.attr('action', infoAccount._mformURL);
//	$form.attr('method', 'post');
//	$form.appendTo('body');	
//	var key = $('<input type="hidden" value="'+result.key+'" name="key">');
//	$form.append(key);
//	$form.submit();
			break;
			case "2" : common.error(vm.itsmeFail); break;
			default : common.error(vm.itsmeError); break;
		}
	},
	mod: function() {
		var valid = {
			rules: {
				userName: { required: true, maxlength: 25 },
				userEmail: { required: true, maxlength: 50, email: true },
				userPhoneNum: { required: true, minlength:11, digits: true }
			},
			messages: {
				userName: { required: vm.required, maxlength: vm.maxlength },
				userEmail: { required: vm.required, maxlength: vm.maxlength, email:vm.email },
				userPhoneNum: { required: vm.required, minlength: vm.minlength, digits:vm.digits }
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
			case "1" : common.success(vm.modSuccess); break;
			case "2" : common.error(vm.modFail); break;
			case "3" : common.error(vm.noChangePasswd); break;
			default : common.error(vm.modError); break;
		}
	}
};
