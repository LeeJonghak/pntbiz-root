$(document).ready( function() {	
	$('#scannerDeployFormBtn').bind('click', function() { scannerDeploy.form(); });
	$('#scannerDeployMFormBtn').bind('click', function() { scannerDeploy.mform(); });
	$('#scannerDeployListBtn').bind('click', function() { scannerDeploy.list(); });
	$('#scannerDeployRegBtn').bind('click', function() { scannerDeploy.reg(); });
	$('#scannerDeployRegBtn2').bind('click', function() { scannerDeploy.reg(); });
	$('#scannerDeployModBtn').bind('click', function() { scannerDeploy.mod(); });
	$('#scannerDeployModBtn2').bind('click', function() { scannerDeploy.mod(); });
	$('#scannerDeployDelBtn').bind('click', function() { scannerDeploy.del(); });
	$('#scannerDeployDelBtn2').bind('click', function() { scannerDeploy.del(); });
	$('.scannerDeployBtn').bind('click', function() { scannerDeploy.dep($(this).attr('depNum')); });	
	$('#scannerDeployMasterBtn').bind('click', function() { scannerDeploy.depMaster(); });	
	$('#scannerDeployJsonMasterBtn').bind('click', function() { scannerDeploy.depJsonMaster(); });	
});

var scannerDeploy = {
	_listURL: "/scanner/deploy/list.do",
	_formURL: "/scanner/deploy/form.do",
	_mformURL: "/scanner/deploy/mform.do",
	_regURL: "/scanner/deploy/reg.do",
	_modURL: "/scanner/deploy/mod.do",
	_delURL: "/scanner/deploy/del.do",
	_deployURL: "/scanner/deploy/dep.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({});
		common.redirect(this._formURL);
	},
	mform: function(servNum) {
		this._mformURL += common.setQueryString({"servNum":servNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({});
		common.redirect(this._listURL);
	},
	reg: function() {		
		$(this._form).validate({
			rules: {
				servNum: { required: true },
				depType: { required: true },
				depName: { required: true },
				depPath: { required: true },
				depFile: { required: true }
	        },
	        messages: {
	        	servNum: { required: vm.required },
				depType: { required: vm.required },
				depName: { required: vm.required },
				depPath: { required: vm.required },
				depFile: { required: vm.required }
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
				scannerDeploy._listURL += common.setQueryString({});
				common.redirect(scannerDeploy._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				servNum: { required: true },
				depType: { required: true },
				depName: { required: true },
				depPath: { required: true },
				depFile: { required: true }
	        },
	        messages: {
	        	servNum: { required: vm.required },
				depType: { required: vm.required },
				depName: { required: vm.required },
				depPath: { required: vm.required },
				depFile: { required: vm.required }
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
				scannerDeploy._listURL += common.setQueryString({});
				common.redirect(scannerDeploy._listURL);
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
				scannerDeploy._listURL += common.setQueryString({});
				common.redirect(scannerDeploy._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	},
	dep: function(depNum) {
		var result = confirm(vm.deployConfirm);
		if(result) {
			$('#depNum').val(depNum);
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._deployURL,
				data: $(this._form).serialize(),
				success: this.depResult
			});
		}
	},
	depResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		$('#depNum').val("0");
		switch(result.result) {
			case "1" :
				common.success(vm.deploySuccess);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	},
	depMaster: function(depNum, type) {
		$(this._form).validate({
			rules: {
				depType: { required: true }
	        },
	        messages: {
				depType: { required: vm.required }
	        },
	        submitHandler: function (frm) {},
	        success: function (e) {}
		});		
		if($(this._form).valid()) {
			var result = confirm(vm.deployConfirm);
			if(result) {
				$.ajax({ type: "POST",
					contentType: 'application/x-www-form-urlencoded',
					processData: false,
					url: this._deployURL,
					data: $(this._form).serialize(),
					success: this.depMasterResult
				});
			}
		}
	},
	depMasterResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				common.success(vm.deploySuccess);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}	
};
