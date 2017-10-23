


function rendSelectFloorCode(selectId, data, defaultFloor) {
	var $root = $('#'+selectId);
	for(var index in data) {
		if(data[index].levelNo == 0) {
			var originalFloor = defaultFloor;

			if(data[index].nodeId == originalFloor) {
				$root.append('<li class="active" id="' + '">' + '<a class="selected" href="#" data-value="' + data[index].nodeId + '">' + data[index].nodeName1 + '</a>'
					+ '<ul id="' + data[index].nodeId + '">' + '</ul>'+'</li>');
				$('#originalFloorB').text(data[index].nodeName1);
			} else {
				$root.append('<li class="active" id="' + '">' + '<a href="#" data-value="' + data[index].nodeId + '">' + data[index].nodeName1 + '</a>'
					+ '<ul id="' + data[index].nodeId + '">' + '</ul>'+'</li>');
			}
		} else if (data[index].levelNo > 0) {
			var upper = data[index].upperNodeId;
			var originalFloor = '${scannerInfo.floor}';

			if(data[index].nodeId == originalFloor) {
				var appendRun = $('#'+upper).append('<li class="" id="' + '">'
					+ '<a href="#" class="selected" data-value="' + data[index].nodeId + '">' + data[index].nodeName1 + '</a>'
					+ '<ul id="' + data[index].nodeId + '">' + '</ul>' +'</li>');
				$('#originalFloorB').text(data[index].nodeName1);
			} else {
				var appendRun = $('#'+upper).append('<li class="" id="' + '">'
					+ '<a href="#" data-value="' + data[index].nodeId + '">' + data[index].nodeName1 + '</a>'
					+ '<ul id="' + data[index].nodeId + '">' + '</ul>' +'</li>');
			}
		}
	}
}

$(document).ready( function() {

	$('#language>ul>li>a').click(function(e){
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
	}catch(e) {
		console.error(e);
	}


	/**
	 * PC 시간대를 기준으로 시간을 표시 하기 위해서
	 */
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

	/**
	 * 기존 디자인의 페이징을 그대로 활용하기 위한 부분
	 */
	$('.paging.v2>.original>li>a').each(function(index, element) {
		var className = $(element).parent().attr('class');
		if(typeof className == 'undefined' || className == null || className == '') {
			var atag = document.createElement('a');
			atag.href = element.href;
			atag.innerHTML = element.innerHTML;
			$('.paging.v2').append($(atag));
		}
		else if($(element).parent().hasClass('active')) {
			var atag = document.createElement('a');
			atag.href = element.href;
			atag.className = 'curr';
			atag.innerHTML = element.innerHTML;
			$('.paging.v2').append($(atag));
		}
		else if(!$(element).parent().hasClass('disabled')) {
			if(element.innerHTML == '<<') {
				var atag = document.createElement('a');
				atag.href = element.href;
				atag.className = 'first';
				atag.innerHTML = '처음페이지';
				$('.paging.v2').append($(atag));
			}
			else if(element.innerHTML == '<') {
				var atag = document.createElement('a');
				atag.href = element.href;
				atag.className = 'prev';
				atag.innerHTML = '이전페이지';
				$('.paging.v2').append($(atag));
			}
			else if(element.innerHTML == '>') {
				var atag = document.createElement('a');
				atag.href = element.href;
				atag.className = 'next';
				atag.innerHTML = '다음페이지';
				$('.paging.v2').append($(atag));
			}
			else if(element.innerHTML == '>>') {
				var atag = document.createElement('a');
				atag.href = element.href;
				atag.className = 'last';
				atag.innerHTML = '마지막페이지';
				$('.paging.v2').append($(atag));
			}
		}
	});
	$('.paging.v2>.original').remove();


	/**
	 * 디자인 V2 에서의 선택 메뉴 강조와 네비게이션 텍스트 설정
	 */
	(function() {
		if(typeof(window.layoutGnb)!='undefined') {
			var main = window.layoutGnb.mainmenu;
			var sub = window.layoutGnb.submenu;
			if(typeof(main)!='undefined') {
				$('#gnb-menu-'+main).addClass('active');
			}
			if(typeof(main)!='undefined' && typeof(sub)!='undefined') {
				$('#gnb-menu-'+main+'-'+sub).addClass('active');
			}
			if(typeof(window.layoutGnb.location)!='undefined') {
				var location = window.layoutGnb.location;
				var locs = [];
				for(var i=0; i<location.length; i++) {
					locs.push('<li>'+location[i]+(i<location.length-1?'<em></em>':'')+'</li>')
				}
				$('#location').html(locs.join(''));
			}
		}
	})();

	(function() {

		$('.pnt-datetime-timestamp').each(function(index, element) {

			function updateTimestamp($date, $hour, $minute, $second) {
				var hour = $hour.length>0?$hour.val():'00';
				var minute = $minute.length>0?$minute.val():'00';
				var second = $second.length>0?$second.val():'00';
				var datetime = $date.val()+' '+hour+':'+minute+':'+second;
				$(element).val(moment(datetime, 'YYYY-MM-DD HH:mm:ss').unix());
			}

			var targetPrefix = $(element).data('target-prefix');
			var $date = $('#'+targetPrefix+'-date');
			var $hour = $('#'+targetPrefix+'-hour');
			var $minute = $('#'+targetPrefix+'-minute');
			var $second = $('#'+targetPrefix+'-second');

			if($date.val()=='') {
				$date.val(moment().format("YYYY-MM-DD"));
			}

			$date.datepicker('option', 'onSelect', function(date) {
				updateTimestamp($date, $hour, $minute, $second);
			});
			$hour.on('change', function() {
				updateTimestamp($date, $hour, $minute, $second);
			});
			$minute.on('change', function() {
				updateTimestamp($date, $hour, $minute, $second);
			});
			$second.on('change', function() {
				updateTimestamp($date, $hour, $minute, $second);
			});
			updateTimestamp($date, $hour, $minute, $second);
		});

	})();


	$('.pnt-datetime-format').each(function(index, element) {

		function updateTimestamp($date, $hour, $minute, $second, format) {
			var hour = $hour.length>0?$hour.val():'00';
			var minute = $minute.length>0?$minute.val():'00';
			var second = $second.length>0?$second.val():'00';
			var datetime = $date.val()+' '+hour+':'+minute+':'+second;
			$(element).val(moment(datetime, 'YYYY-MM-DD HH:mm:ss').format(format||'YYYY-MM-DD HH:mm:ss'));
		}

		var targetPrefix = $(element).data('target-prefix');
		var format = $(element).data('format');
		var $date = $('#'+targetPrefix+'-date');
		var $hour = $('#'+targetPrefix+'-hour');
		var $minute = $('#'+targetPrefix+'-minute');
		var $second = $('#'+targetPrefix+'-second');
		var value = $(element).val();

		if($date.val()=='') {
			$date.val(moment().format("YYYY-MM-DD"));
		}

		$date.datepicker('option', 'onSelect', function(date) {
			updateTimestamp($date, $hour, $minute, $second, format);
		});
		$hour.on('change', function() {
			updateTimestamp($date, $hour, $minute, $second, format);
		});
		$minute.on('change', function() {
			updateTimestamp($date, $hour, $minute, $second, format);
		});
		$second.on('change', function() {
			updateTimestamp($date, $hour, $minute, $second, format);
		});

		if(value!='') {
			var m = moment(value, 'YYYY-MM-DD HH:mm:ss');
			$date.val(m.format('YYYY-MM-DD'));
			$hour.val(m.format('H'));
			$minute.val(m.format('m'));
			$second.val(m.format('s'));
		} else {
			updateTimestamp($date, $hour, $minute, $second, format);
		}
	});

});