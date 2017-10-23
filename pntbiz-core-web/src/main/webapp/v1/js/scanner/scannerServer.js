$(document).ready( function() {	
	$('#scannerServerFormBtn').bind('click', function() { scannerServer.form(); });
	$('#scannerServerMFormBtn').bind('click', function() { scannerServer.mform(); });
	$('#scannerServerListBtn').bind('click', function() { scannerServer.list(); });
	$('#scannerServerRegBtn').bind('click', function() { scannerServer.reg(); });
	$('#scannerServerModBtn').bind('click', function() { scannerServer.mod(); });
	$('#scannerServerDelBtn').bind('click', function() { scannerServer.del(); });	
});

var acMap= {};
var scannerServer = {
	_listURL: "/scanner/server/list.do",
	_formURL: "/scanner/server/form.do",
	_mformURL: "/scanner/server/mform.do",
	_regURL: "/scanner/server/reg.do",
	_modURL: "/scanner/server/mod.do",
	_delURL: "/scanner/server/del.do",
	_form: "#form1",
	form: function() {
		this._formURL += common.setQueryString({"page": ""});
		common.redirect(this._formURL);
	},
	mform: function(servNum) {
		this._mformURL += common.setQueryString({"page": "", "servNum":servNum});
		common.redirect(this._mformURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": ""});
		common.redirect(this._listURL);
	},
	reg: function() {		
		$(this._form).validate({
			rules: {
				ftpHost: { required: true },
				ftpPort: { required: true },
				ftpID: { required: true },
				ftpPW: { required: true }
	        },
	        messages: {
	        	ftpHost: { required: vm.required }, 
	        	ftpPort: { required: vm.required }, 
	        	ftpID: { required: vm.required }, 
	        	ftpPW: { required: vm.required }
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
				scannerServer._listURL += common.setQueryString({"page": ""});
				common.redirect(scannerServer._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				ftpHost: { required: true },
				ftpPort: { required: true },
				ftpID: { required: true },
				ftpPW: { required: true }
	        },
	        messages: {
	        	ftpHost: { required: vm.required }, 
	        	ftpPort: { required: vm.required }, 
	        	ftpID: { required: vm.required }, 
	        	ftpPW: { required: vm.required }
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
				scannerServer._listURL += common.setQueryString({"page": ""});
				common.redirect(scannerServer._listURL);
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
				scannerServer._listURL += common.setQueryString({"page": ""});
				common.redirect(scannerServer._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
