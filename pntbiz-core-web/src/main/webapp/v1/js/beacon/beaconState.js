$(document).ready( function() {
	if($('#beaconStateKeyword').val() == "") $('#beaconStateKeyword').focus();	
	$('#beaconStateKeyword').bind('keyup', function(e) { if(e.keyCode==13) beaconState.search(); });	
	$('#beaconStateSearchBtn').bind('click', function() { beaconState.search(); });
	$('#beaconStateListBtn').bind('click', function() { beaconState.list(); });

});

var beaconState = {
	_listURL: "/beacon/monitor/list.do",
	_form: "#form1",
    _mformURL: "/beacon/info/mform.do",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "field": "", "sort": "",  "state": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var state = $("#state option:selected").val();
		var field = $("#field option:selected").val();
		var sort = $("#sort option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#beaconStateKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "field": field, "sort": sort, "opt":opt, "keyword":keyword, "state":state});
		common.redirect(this._listURL);
	},
    mform: function(beaconNum) {
        this._mformURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "field": "", "sort": "",  "state": "", "beaconNum":beaconNum,"list":"beaconState"});
        common.redirect(this._mformURL);
    }
};