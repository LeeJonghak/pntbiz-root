$(document).ready( function() {	
	$('#adminLogSearchBtn').bind('click', function() { adminLog.search(); });
	$('#adminLogListBtn').bind('click', function() { adminLog.list(); });
});

var adminLog = {
	_listURL: "/admin/log/list.do",
	_form: "#form1",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "crudType": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var crudType = $("#crudType option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#adminLogKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "crudType":crudType, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	}
};