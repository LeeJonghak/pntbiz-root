$(document).ready( function() {
	if($('#attendanceKeyword').val() == "") $('#attendanceKeyword').focus();	
	$('#attendanceKeyword').bind('keyup', function(e) { if(e.keyCode==13) attendance.search(); });	
	$('#attendanceSearchBtn').bind('click', function() { attendance.search(); });
	$('#attendanceListBtn').bind('click', function() { attendance.list(); });

	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYYMMDD'});
	} catch(exception) {} 	
});

var attendance = {
	_listURL: "/service/attendanceSeminar/list.do",
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
	}
};

elementHandler.bind({
    btnForm2Submit: {
        action: 'submit',
        form: 'form2',
        url: 'service/attendanceSeminar/reg.do',
        result: {
            1: {message:vm.regSuccess, reload:true},
            2: vm.regError
        }
    },
    '$btn-del-seminar': {
        action: 'post',
        url: 'service/attendanceSeminar/del.do',
        data: function() {
            var majorVer = $(this).attr('data-major-ver');
            var minorVer = $(this).attr('data-minor-ver');
            var macAddr = $(this).attr('data-mac-addr');
            return {majorVer: majorVer, minorVer: minorVer, macAddr:macAddr};
        },
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess, reload: true},
            2: vm.delError
        }
    },
    btnReset: {
        action: 'post',
        url: 'service/attendanceSeminar/reset.do',
        data: function() {
            return {attdDate:$('#attdDate').val()};
        },
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess, reload: true},
            2: vm.delError
        }
    },
    btnExport: function() {
        var attdDate = common.getQueryString('attdDate');
        var opt = common.getQueryString('opt');
        var keyword = common.getQueryString('keyword');
        common.redirect('export.do?attdDate='+attdDate+'&opt='+opt+'&keyword='+keyword);
    },
    $btnDelete: {
        action: 'post',
        url: 'service/attendanceSeminar/delLog.do',
        data: function() {
            return {
                logNum:$(this).attr('data-log-num'),
                attdDate:$(this).attr('data-attd-date')
            };
        },
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess, reload: true},
            2: vm.delError
        }
    }
});
