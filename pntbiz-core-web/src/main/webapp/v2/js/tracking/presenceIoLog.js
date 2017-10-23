$(document).ready( function() {
	if($('#presenceLogKeyword').val() == "") $('#presenceLogKeyword').focus();	
	$('#presenceLogKeyword').bind('keyup', function(e) { if(e.keyCode==13) presenceLog.search(); });	
	$('#presenceLogSearchBtn').bind('click', function() { presenceLog.search(); });
	$('#presenceLogListBtn').bind('click', function() { presenceLog.list(); });
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYYMMDD'});
	} catch(exception) {} 	
});

var presenceLog = {
	_listURL: "/tracking/presence/scanner/iolog.do",
	_form: "#form1",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#presenceLogKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	}
};