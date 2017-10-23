$(document).ready( function() {	
	if($('#locationRecordKeyword').val() == "") $('#locationRecordKeyword').focus();	
	$('#locationRecordKeyword').bind('keyup', function(e) { if(e.keyCode==13) locationRecord.search(); });
	$('#locationRecordFormBtn').bind('click', function() { locationRecord.form(); });
	$('#locationRecordSearchBtn').bind('click', function() { locationRecord.search(); });
	$('#locationRecordListBtn').bind('click', function() { locationRecord.list(); });
	$('#locationRecordRegBtn').bind('click', function() { locationRecord.reg(); });
	
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
		$('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
	} catch(exception) {} 	
});

var locationRecord = {
	_listURL: "/info/location/record.do",
	_formURL: "/info/location/recordForm.do",
	_regURL: "/info/location/recordReg.do",
	_form: "#form1",	
	form: function() {
		this._formURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "sDate": "", "eDate": ""});
		common.redirect(this._formURL);
	},
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "sDate": "", "eDate": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#locationRecordKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "sDate":sDate, "eDate":eDate, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reg: function() {
		$(this._form).validate({
			rules: {
				reqName: { required: true, maxlength: 15 },
				useDesc: { required: true, maxlength: 120 }
	        },
	        messages: {
	        	reqName: { required: vm.required, maxlength: vm.maxlength },
	        	useDesc: { required: vm.required, maxlength: vm.maxlength }
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
				locationRecord._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "sDate": "", "eDate": ""});
				common.redirect(locationRecord._listURL);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	}
};
