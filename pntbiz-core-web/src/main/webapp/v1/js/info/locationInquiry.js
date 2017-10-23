$(document).ready( function() {	
	if($('#locationInquiryKeyword').val() == "") $('#locationInquiryKeyword').focus();	
	$('#locationInquiryKeyword').bind('keyup', function(e) { if(e.keyCode==13) locationInquiry.search(); });
	$('#locationInquirySearchBtn').bind('click', function() { locationInquiry.search(); });
	$('#locationInquiryListBtn').bind('click', function() { locationInquiry.list(); });
	
	try {
		$('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
		$('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
	} catch(exception) {} 	
});

var locationInquiry = {
	_listURL: "/info/location/list.do",
	_form: "#form1",	
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "sDate": "", "eDate": "", "os": "", "service": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var sDate = $("#sDate").val();
		var eDate = $("#eDate").val();
		var os = $("#os option:selected").val();
		var service = $("#service option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#locationInquiryKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "sDate":sDate, "eDate":eDate, "os":os, "service":service, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	}
};