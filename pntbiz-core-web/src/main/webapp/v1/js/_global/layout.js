$(document).ready( function() {	
	$('.login-top .dropdown-menu li > a').click(function(e){
		var locale = $(this).attr('locale');
		var pathname = $(location).attr('pathname');
		var search = $(location).attr('search');
		var params = $(location).attr('search').substring(1, search.length);
		var param = params.split("&");
		var sparam = "?";
		var cnt = 0;
		for(var key in param) {
			if(param[key] != "") {
				var v = param[key].split("=");
				if(cnt > 0) sparam += "&"; 
				if(v[0] != "lang") {
					sparam += v[0] + "=" + v[1];
					cnt++;
				} else {
				}
			}
		}
		if(cnt > 0) sparam += "&";
		sparam += "lang=" + locale;
		common.redirect(pathname + sparam);
	});
	// error page
	try {
		$('#errorHomeBtn').bind('click', function() { common.redirect("/")});
	}catch(e) {}
});

var layout = {
};


(function() {

	var pntTimestampObServer = null;
	var registerTimestampElement = function(element) {
		var timestamp = $(element).attr('data-timestamp');
		var format = $(element).attr('data-format');
		$(element).removeClass('pnt-timestamp');
		$(element).addClass('pnt-timestamp-prepared');

		if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
			if(element.tagName=='INPUT') {
				element.value = moment(timestamp*1000).format(format);
			} else {
				element.innerHTML = moment(timestamp*1000).format(format);
			}
		}

		if(typeof(pntTimestampObServer)!='undefined' && pntTimestampObServer!=null) {
			pntTimestampObServer.observe(element, {
				attributes: true,
				childList: true,
				characterData: true
			});
		} else {

			if (element.addEventListener) { // all browsers except IE before version 9
				element.addEventListener ('DOMAttrModified', function(event) {
					var timestamp = $(event.target).attr('data-timestamp');
					var format = $(event.target).attr('data-format');
					event.target.innerHTML = moment(timestamp*1000).format(format);

					if(element.tagName=='INPUT') {
						if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
							event.target.value = moment(timestamp*1000).format(format);
						} else {
							event.target.value = '';
						}
					} else {
						if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
							event.target.innerHTML = moment(timestamp*1000).format(format);
						} else {
							event.target.innerHTML = '';
						}

					}

				}, false);
			}
			else if (element.attachEvent) {  // Internet Explorer and Opera
				element.attachEvent ('onpropertychange', function(event) {
					var timestamp = $(element).attr('data-timestamp');
					var format = $(element).attr('data-format');

					if(element.tagName=='INPUT') {
						if(datetime!=element.value) {
							if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
								element.value = moment(timestamp*1000).format(format);
							} else {
								element.value = '';
							}
						}
					} else {
						if(datetime!=element.innerHTML) {
							if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
								element.innerHTML = moment(timestamp * 1000).format(format);
							} else {
								element.innerHTML = '';
							}
						}
					}
				});
			}
		}
	}

	$(document).on('DOMNodeInserted', function(event) {
		$(event.target).find('.pnt-timestamp').each(function(index, element) {
			registerTimestampElement(element);
		});
	});

	$(document).ready(function() {

		if(typeof(MutationObserver)!='undefined') {
			pntTimestampObServer = new MutationObserver(function (mutations) {
				mutations.forEach(function (mutation) {
					if (mutation.attributeName === 'data-timestamp' || mutation.attributeName === 'data-format') {
						var element = mutation.target;
						var timestamp = $(element).attr('data-timestamp');
						var format = $(element).attr('data-format');

						if(element.tagName=='INPUT') {
							if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
								element.value = moment(timestamp * 1000).format(format);
							} else {
								element.value = '';
							}
						} else {
							if(typeof(timestamp)!='undefined' && timestamp!=null && timestamp!='') {
								element.innerHTML = moment(timestamp * 1000).format(format);
							} else {
								element.innerHTML = '';
							}
						}
					}
				});
			});
		}

		$('.pnt-timestamp').each(function(index, element) {
			registerTimestampElement(element);
		});
	});

	/* momentjs format doc : https://momentjs.com/docs/#/displaying/ */
})();






