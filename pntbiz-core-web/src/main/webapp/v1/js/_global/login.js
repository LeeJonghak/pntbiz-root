$(document).ready( function() {
	$('#userid').bind({		
		keypress: function(evt) {
			var evCode = (window.netscape) ? evt.which : evt.keyCode;
			if(evCode == 13) result = true;
			else result = false;
			if(result == true) login.login('userid', 'userpw', 'remember', REQUEST_URI);
		}
	});		
	$('#userpw').bind({		
		keypress: function(evt) {
			var evCode = (window.netscape) ? evt.which : evt.keyCode;
			if(evCode == 13) result = true;
			else result = false;
			if(result == true) login.login('userid', 'userpw', 'remember', REQUEST_URI);
		}
	});
	$('#loginBtn').bind('click', function() { login.login('userid', 'userPW', 'remember', REQUEST_URI); });
	$('#logoutBtn').bind('click', function() { login.logout(); });
	$('#loginTopBtn').bind('click', function() { login.auth(); });
	$('#logoutTopBtn').bind('click', function() { login.authLogout(); });
	$('#loginPasswordBtn').bind('click', function() { common.redirect("/auth/otp.do") });
	$('#loginOtpBtn').bind('click', function() { common.redirect("/auth/otp.do") });
	
	$('#loginForm').validate({
		rules: {
			userid: { required: true, maxlength: 20, minlength: 1 },
			userpw: { required: true, maxlength: 70, minlength: 1 }
        },
        messages: {
        	userid: { required: $.validator.messages.required, minlength: $.validator.messages.minlength },
            userpw: { required: $.validator.messages.required, minlength: $.validator.messages.minlength }
        },
        submitHandler: function (frm) { frm.submit(); },
        success: function (e) {}
	});
	
	$('#loginForm').submit(function(event) {
		var checkbox = $('#remember');
		if(checkbox.is(':checked')){
			var userid = $('#userid').val();
			$.cookie('remember-me-userid', userid, { expires: 3650 }); //SETS IN DAYS (10 YEARS)
		} else {
			$.removeCookie('remember-me-userid');
		}
	});
	if($.cookie('remember-me-userid')) {
		$('#userid').val($.cookie('remember-me-userid'));
		$('#remember').attr('checked', true);
	}
});

var login = {
	auth: function() {
		common.redirect("/auth/login.do");
	},
	authLogout: function() {
		common.redirect("/auth/logout");
	},
	login: function(userid, passwd, saveid, returl) {
		var userid = encodeURIComponent($('#'+userid).val());
		var passwd = encodeURIComponent($('#'+passwd).val());
		var saveid = ($('input:checkbox[name='+saveid+']').attr('checked') == 'checked') ? $('#'+saveid).val() : "0";
		var returl = encodeURIComponent(returl);
//		if(this.valID(userid) == false) { return false; }
//		if(this.valPasswd(passwd) == false) { return false; }
		$('#loginForm').submit();
	},	
	loginResult: function(data) {
		//alert(data);
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		if(result.result == "1") {
			common.redirect(result.url);
		} else {
			$('#passwd').val("");
//			common.dialogModalClose();
//			common.dialog("로그인 실패", "아이디 또는 비밀번호를 확인하시길 바랍니다.");
		}
	},
	logout: function() {
		$('#loginForm').attr('method', 'post');
		$('#loginForm').attr('action', '/auth/logout');
		$('#loginForm').submit();
	}
};
