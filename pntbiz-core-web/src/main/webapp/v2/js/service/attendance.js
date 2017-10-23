$(document).ready( function() {
	if($('#attendanceKeyword').val() == "") $('#attendanceKeyword').focus();	
	$('#attendanceKeyword').bind('keyup', function(e) { if(e.keyCode==13) attendance.search(); });	
	$('#attendanceSearchBtn').bind('click', function() { attendance.search(); });
	$('#attendanceListBtn').bind('click', function() { attendance.list(); });
	$('#resetBtn').bind('click', function() {
		if(confirm(vm.delConfirm)) {
			attendance.reset();
		}
	});
});

var attendance = {
	_listURL: "/service/attendance/list.do",
	_resetURL: "/service/attendance/reset.do",
	_form: "#form1",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "attdDate": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var attdDate = common.trim($("#attdDate").val());
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#attendanceKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "attdDate": attdDate, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	reset: function() {

		if($(this._form).valid()) {
			$.ajax({ type: "POST",
				contentType: 'application/x-www-form-urlencoded',
				processData: false,
				url: this._resetURL,
				data: $(this._form).serialize(),
				success: this.resetResult
			});
		}
	},
	resetResult: function(data) {
		var vm = $.validator.messages;
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}

		switch(result.result) {
			case "1" :
				var url = attendance._listURL + common.setQueryString({"page": "", "opt": "", "keyword": "", "attdDate": ""});
				common.redirect(url);
				break;
			case "2" : common.error(vm.regFail); break;
			default : common.error(vm.regError); break;
		}
	}
};
