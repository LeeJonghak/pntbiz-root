$(document).ready( function() {
	if($('#presenceBeaconLogKeyword').val() == "") $('#presenceBeaconLogKeyword').focus();	
	$('#presenceBeaconLogKeyword').bind('keyup', function(e) { if(e.keyCode==13) presenceBeaconLog.search(); });	
	$('#presenceBeaconLogSearchBtn').bind('click', function() { presenceBeaconLog.search(); });
	$('#presenceBeaconLogListBtn').bind('click', function() { presenceBeaconLog.list(); });
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYYMMDD'});
	} catch(exception) {} 	
});

var presenceBeaconLog = {
	_listURL: "/tracking/presence/beacon/log.do",
	_form: "#form1",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#presenceBeaconLogKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	}
};