$(document).ready( function() {
	$('#setmapModBtn').bind('click', function() { setmap.mod(); });
});

var setmap = {
	_mformURL: "/tracking/presence/setmap.do",
	_modURL: "/tracking/presence/mod.do",
	_form: "#form1",	
	mform: function(conNum) {
		this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "conType": "", "conNum":conNum});
		common.redirect(this._mformURL);
	},
	mod: function() {
		$(this._form).validate({
			rules: {
				initZoom: { required: true, number: vm.number, maxlength: 2 },
				initFloor: { required: true },
				checkTimeInterval: { required: true, number: true },
				removeTimeInterval: { required: true, number: true },
				moveTimeInterval: { required: true, number: true },
				moveUnit: { required: true, number: true }
	        },
	        messages: {
	        	initZoom: { required: vm.required, number: vm.number, maxlength: vm.maxlength },
	        	initFloor: { required: vm.required },
	        	checkTimeInterval: { required: vm.required, number: vm.number },
	        	removeTimeInterval: { required: vm.required, number: vm.number },
	        	moveTimeInterval: { required: vm.required, number: vm.number },
	        	moveUnit: { required: vm.required, number: vm.number }
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
				setmap._mformURL += common.setQueryString({});
				common.redirect(setmap._mformURL);
				break;
			case "2" : common.error(vm.modFail); break;
			default : common.error(vm.modError); break;
		}
	}
};
