$(document).ready( function() {
	if($('#geofencingGroupSearchKeyword').val() == "") $('#geofencingGroupSearchKeyword').focus();	
	$('#geofencingGroupSearchKeyword').bind('keyup', function(e) { if(e.keyCode==13) geofencingGroup.search(); });	
	$('#geofencingGroupFormBtn').bind('click', function() { geofencingGroup.form(); });
	$('#geofencingGroupMFormBtn').bind('click', function() { geofencingGroup.mform(); });
	$('#geofencingGroupSearchBtn').bind('click', function() { geofencingGroup.search(); });
	$('#geofencingGroupListBtn').bind('click', function() { geofencingGroup.list(); });	
	$('#geofencingGroupRegBtn').bind('click', function() { geofencingGroup.reg(); });
	$('#geofencingGroupModBtn').bind('click', function() { geofencingGroup.mod(); });
	$('#geofencingGroupDelBtn').bind('click', function() { geofencingGroup.del(); });
});

var geofencingGroup = {
	_listURL: "/geofencing/group/list.do",	
	_formURL: "/geofencing/group/form.do",
    _mformURL: "/geofencing/group/mform.do",
	_regURL: "/geofencing/group/reg.do",
	_modURL: "/geofencing/group/mod.do",
	_delURL: "/geofencing/group/del.do",	
    _form: "#form1",
    form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._formURL);
	},
	mform: function(conNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "geofencingGroupNum":geofencingGroupNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#geofencingGroupSearchKeyword").val());
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
				geofencingGroupName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	geofencingGroupName: { required: vm.required, maxlength: vm.maxlength }
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
				geofencingGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(geofencingGroup._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				geofencingGroupName: { required: true, maxlength: 25 }
	        },
	        messages: {
	        	geofencingGroupName: { required: vm.required, maxlength: vm.maxlength }
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
				geofencingGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(geofencingGroup._listURL);
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
				geofencingGroup._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
				common.redirect(geofencingGroup._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
