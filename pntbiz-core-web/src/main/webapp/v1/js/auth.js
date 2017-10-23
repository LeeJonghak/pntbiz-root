$(document).ready( function() {	
	$('#otpNum').bind({	
		keypress: function(evt) {
			var evCode = (window.netscape) ? evt.which : evt.keyCode;
			if(evCode == 13) result = true;
			else result = false;
		}
	});
	$('#otpLoginBtn').bind('click', function() { auth.loginRedirect(); });
	$('#otpActivateBtn').bind('click', function() {
		if(!$(this).hasClass('disabled')) auth.activate(); 
	});
	$('#otpNumBtn').bind('click', function() { auth.sendOtpNum(); });	
	$('#form1').validate({
		rules: {
			userID: { required: true },
			otpNum: { required: true, minlength: 5 }
		},
		messages: {
			userID: { required: $.validator.messages.required },
			otpNum: { required: $.validator.messages.required, minlength: $.validator.messages.minlength }
		},
		submitHandler: function (frm) { frm.submit(); },
		success: function (e) {}
	});
});

$(window).load(function() {
	var failure = $('#failure').val();
	switch(failure) {
		case "2" :
			alert(vm.otpAccountLocked);
			break;
		case "3" : 
			alert(vm.otpAccountExpired);
			break;
		default: break;
	}
});
var auth = {
	_otpURL : "/auth/otpNum.do",
	_activateURL : "/auth/activate.do",
	_pwchangeURL : "/info/passwd/mform.do",
	_form: "#form1",
	loginRedirect: function() {
		common.redirect("/auth/login.do");
	},
	sendOtpNum: function() {
		$("input[id*=otpNum]").rules("remove", "required");
		$("input[id*=otpNum]").rules("remove", "minlength");
		if($(this._form).valid()) {
			$.ajax({ type: "post",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._otpURL,
				data: $(this._form).serialize(),
				success: this.sendOtpNumResult
			});
		} 
	},	
	sendOtpNumResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		if(result.result == "1") {
			auth.timerStart();
			$('#otpActivateBtn').removeClass('disabled');
			$('#otpNum').val("");
			alert(vm.otpAuthCodeSend);	
		} else {
			auth.timerStop();
			alert(vm.otpAuthCodeSendFail);
			$('#otpActivateBtn').addClass('disabled');
		}
	},
	activate: function() {
		$("input[id*=otpNum]").rules("add", "required");
		$("input[id*=otpNum]").rules("add", "minlength");	
		if($(this._form).valid()) {
			$.ajax({ type: "post",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._activateURL,
				data: $(this._form).serialize(),
				success: this.activateResult
			});
		}
	},
	activateResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		if(result.result == "1") {
			auth.timerStop();
			alert(vm.otpAuth);
			$('#key').val(result.key);
			$(auth._form).attr('method', 'post');
			$(auth._form).attr('action', auth._pwchangeURL);
			$(auth._form).submit();
		} else {
			$('#otpNum').val("");	
			alert(vm.otpAuthFail);
		}
	},
	timeInit: 300,
	time: 300,
	timer: null,
	timerStart: function() {
		this.time = this.timeInit;
		this.timer = setInterval('auth.timerMsg()', 1000);
		$('#timer').show();
	},
	timerStop: function() {
		this.time = this.timeInit;
		clearInterval(this.timer);
	},
	timerMsg: function() {
		var msg = vm.otpAuthCodeExpiredTime + " : " + Math.floor(this.time / 60) + " " + vm.otpAuthCodeMin + " " + (this.time % 60) + " " + vm.otpAuthCodeSec;
		$('#timer').html(msg);
		this.time--;
		if (this.time < 0) {
			this.timerStop();
			$('#timer').html(vm.otpAuthCodeExpired);
			$('#otpActivateBtn').addClass('disabled');
		}	
	}
	
};
