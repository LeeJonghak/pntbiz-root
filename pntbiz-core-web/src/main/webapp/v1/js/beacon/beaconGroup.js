$(document).ready( function() {
	if($('#beaconGroupSearchKeyword').val() == "") $('#beaconGroupSearchKeyword').focus();	
	$('#beaconGroupSearchKeyword').bind('keyup', function(e) { if(e.keyCode==13) beaconGroup.search(); });	
	$('#beaconGroupFormBtn').bind('click', function() { beaconGroup.form(); });
	$('#beaconGroupMFormBtn').bind('click', function() { beaconGroup.mform(); });
	$('#beaconGroupSearchBtn').bind('click', function() { beaconGroup.search(); });
	$('#beaconGroupListBtn').bind('click', function() { beaconGroup.list(); });	
	$('#beaconGroupRegBtn').bind('click', function() { beaconGroup.reg(); });
	$('#beaconGroupModBtn').bind('click', function() { beaconGroup.mod(); });
	$('#beaconGroupDelBtn').bind('click', function() { beaconGroup.del(); });
});

var beaconGroup = {
	_listURL: "/beacon/group/list.do",	
	_formURL: "/beacon/group/form.do",
    _mformURL: "/beacon/group/mform.do",
	_regURL: "/beacon/group/reg.do",
	_modURL: "/beacon/group/mod.do",
	_delURL: "/beacon/group/del.do",	
    _form: "#form1",
    form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(conNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "beaconGroupNum":beaconGroupNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#beaconGroupSearchKeyword").val());
		if(!common.isObj(opt) || !common.isObj(keyword)) {
			this._listURL += common.setQueryString({"page": 1 });
		} else {
			this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		}
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				beaconGroupName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	beaconGroupName: { required: vm.required, maxlength: vm.maxlength }
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
		console.log(data);
		switch(result.result) {
			case "1" :
				beaconGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(beaconGroup._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				beaconGroupName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	beaconGroupName: { required: vm.required, maxlength: vm.maxlength }
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
				beaconGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(beaconGroup._listURL);
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
				beaconGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(beaconGroup._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
