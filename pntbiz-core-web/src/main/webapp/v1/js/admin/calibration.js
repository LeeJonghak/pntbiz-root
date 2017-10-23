$(document).ready( function() {
	if($('#calibrationKeyword').val() == "") $('#calibrationKeyword').focus();	
	$('#calibrationKeyword').bind('keyup', function(e) { if(e.keyCode==13) calibration.search(); });	
	$('#calibrationFormBtn').bind('click', function() { calibration.form(); });
	$('#calibrationMFormBtn').bind('click', function() { calibration.mform(); });
	$('#calibrationSearchBtn').bind('click', function() { calibration.search(); });
	$('#calibrationListBtn').bind('click', function() { calibration.list(); });
	$('#calibrationRegBtn').bind('click', function() { calibration.reg(); });
	$('#calibrationModBtn').bind('click', function() { calibration.mod(); });
	$('#calibrationDelBtn').bind('click', function() { calibration.del(); });
});


var calibration = {
	_listURL: "/admin/calibration/list.do",
	_formURL: "/admin/calibration/form.do",
	_mformURL: "/admin/calibration/mform.do",
	_regURL: "/admin/calibration/reg.do",
	_modURL: "/admin/calibration/mod.do",
	_delURL: "/admin/calibration/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": ""});
		common.redirect(this._formURL);
	},
	mform: function(calNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": "", "calNum":calNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var maker = $("#maker option:selected").val();
		var telecom = $("#telecom option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#calibrationKeyword").val());
		if(opt != "") {
			if(keyword != "") {
				this._listURL += common.setQueryString({"page": 1, "maker":maker, "telecom":telecom, "opt":opt, "keyword":keyword});
			}
		} else {
			this._listURL += common.setQueryString({"page": 1, "maker":maker, "telecom":telecom});
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
				calibration._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": ""});
				common.redirect(calibration._listURL);
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
				calibration._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": ""});
				common.redirect(calibration._listURL);
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
				calibration._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "maker": "", "telecom": ""});
				common.redirect(calibration._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
