$(document).ready( function() {	
	//$('#loginTopBtn').bind('click', function() { auth.login(); });
});

var init = {
	login: function() {
		common.redirect("/auth");
	},
	logout: function() {
		common.redirect("/auth/logout");
	}
};