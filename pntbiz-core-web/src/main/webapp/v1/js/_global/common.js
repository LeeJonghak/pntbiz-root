var IMAGES = "/images";
var REQUEST_URI = window.location.pathname.substr(1);
var vm = $.validator.messages;

/**
 * 랜덤 아이디 생성
 * @param length
 * @returns {string}
 */
window.makeid = function(length) {
    var text = "";
    var possible = "abcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < length; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

var common = {	
	tempValidate: function() {
		$('#default-form1').validate({
			errorLabelContainer: '#error-message',
			errorPlacement: function(error, element) {
				var id = $(element).attr("id");
				var errorID = "error-" + id;
				error.appendTo($("body").find("#"+errorID));
			},
			rules: { 
				keyword: { required: true }
			},
			messages: {
				keyword: { required: $.validator.messages.required }
			},
			submitHandler: function (frm) { frm.submit() },
	        success: function (e) {}
		});
	},
	notification: function(title, text, style) {
		new PNotify({
			title: title,
			text: text,
			shadow: true,
			opacity: 1,
			addclass: "stack_bar_top",
			type: style,
			stack: {"dir1": "down", "dir2": "right", "push": "top", "spacing1": 0, "spacing2": 0},
			width: "100%",
			delay: 1400
		});
	},
    success: function(str) {
        this.notification("", str, "success");
    },
	info: function(str) {
		this.notification("", str, "info");
	},
	error: function(str) {
		this.notification("", str, "danger");
	},
	isObj: function(obj) {
		if(obj != "" && obj != null && typeof(obj) != "undefined") {
			return true;
		} else {
			return false;
		}
	},	
	redirect: function(url) {
		if(this.isObj) {
			$(location).attr("href", url);
		} else {
			
		}
	},
	modal: function(id, title, body, footer) {
		$('#'+id).on('click', function(e) {
			$('#modal').modal({
				show: 'false'
			});
			$(".modal-title").text(title);
			$(".modal-body").text(body);
			$(".modal-footer").text(footer);
			e.preventDefault();
		});
	},	
	/**
	 * 브라우져별 아이프레임 리사이즈
	 * iframe과 parent에 각각 document.domain 값이 같아야 한다.
	 */
	resizeFrame: function(iframeObj) {
		$("#"+iframeObj).height($("#"+iframeObj).contents().find("body").height());
	},

	/**
	 * 브라우져체크
	 */
	checkBrowser: function() {
		var clientAgent = navigator.userAgent;
		var clientName = navigator.appName;
		var browserType = null;

		if(clientName.indexOf("MSIE 6") != -1) {
			browserType = "IE6";
		} else if(clientName.indexOf("MSIE 7") != -1) {
			browserType = "IE7";
		} else if(clientName.indexOf("MSIE 8") != -1) {
			browserType = "IE8";
		} else if(clientName.indexOf("Netscape") != -1) {
			if(clientAgent.indexOf("Navigator") != -1) {
				browserType = "NS";
			} else {
				browserType = "FF";
			}
		} else if(clientName.indexOf("Opera") != -1) {
			browserType = "OP";
		} else if(clientName.indexOf("Safari") != -1) {
			browserType = "SP";
		} else {
			browserType = "IE";
		}
		return browserType;
	},

	/**
	 * 브라우져별 크기가져오기 ( calType : [body-바디영역, scroll-스크롤영역포함] / sizeType : [width-넓이, height-높이] )
	 */
	getBrowserSize: function(calType) {
		var browserType = this.checkBrowser();
		browserType = browserType.substring(0,2);
		var browserSize = new Array();
		switch(calType)
		{
			case "body" :
				var scrollbarWidth = 16;
				browserSize[0] = (browserType == "IE") ? document.body.clientWidth : window.innerWidth - scrollbarWidth;
				browserSize[1] = (browserType == "IE") ? document.body.clientHeight : window.innerHeight;
				break;
			case "scroll" :
				browserSize[0] = (browserType == "IE") ? document.body.scrollWidth : document.documentElement.scrollWidth;
				browserSize[1] = (browserType == "IE") ? document.body.scrollHeight : document.documentElement.scrollHeight;
				//browserSize[0] = (browserType == "IE") ? document.documentElement.scrollWidth : document.documentElement.scrollWidth;
				//browserSize[1] = (browserType == "IE") ? document.documentElement.scrollHeight : document.documentElement.scrollHeight;
				break;
			case "scrollOffset" :
				var standardBody = (document.compatMode=="CSS1Compat")? document.documentElement : document.body;
				browserSize[0] = (browserType == "IE") ? standardBody.scrollLeft : standardBody.scrollLeft;
				browserSize[1] = (browserType == "IE") ? standardBody.scrollTop : standardBody.scrollTop;
				break;
		}
		return browserSize;
	},

	/**
	 * 클립보드복사
	 * FF에서는 about:config 에 들어가 Signed.applets.codebase_principal_support 값을 true로 변경해야 함
	 * 주소창에 about:config 넣으면 config화면 나옴
	 */
	copyClip: function(content, msg) {
		if(window.clipboardData) {
			window.clipboardData.setData("Text", content);
		} else if(window.netscape) {
			netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
			var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
			if (!clip) return;
			var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
			if (!trans) return;
			trans.addDataFlavor('text/unicode');
			var str = new Object();
			var len = new Object();
			var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
			var copytext=content;
			str.data=copytext;
			trans.setTransferData("text/unicode",str,copytext.length*2);
			var clipid=Components.interfaces.nsIClipboard;
			if(!clip) return false;
			clip.setData(trans,null,clipid.kGlobalClipboard);
		}
		if(msg) {
			alert(msg);
		} else {
			alert("클립보드에 " + content + " (이)가 복사되었습니다.");
		}
	},

	/**
	 * 클립보드붙여넣기
	 * FF에서는 about:config 에 들어가 Signed.applets.codebase_principal_support 값을 true로 변경해야 함
	 */
	pasteClip: function() {
		//var check=document.board_write;
		var clip_data;
		if (window.clipboardData) {
			clip_data = window.clipboardData.getData("Text"); // 클립보드 복사
		} else if(window.netscape) {
			netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
			var clip = Components.classes["@mozilla.org/widget/clipboard;1"].createInstance(Components.interfaces.nsIClipboard);
			if (!clip) return;
			var trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);
			if (!trans) return;
			trans.addDataFlavor("text/unicode");
			clip.getData(trans, clip.kGlobalClipboard);
			var str = new Object();
			var strLength = new Object();
			trans.getTransferData("text/unicode", str, strLength);
			if(str) str = str.value.QueryInterface(Components.interfaces.nsISupportsString);
			else return;
			if(str) clip_data = str.data.substring(0, strLength.value / 2);
		}
		return clip_data;
	},

	/**
	 * 앞뒤공백문자 제거
	 */
	trim: function(str) {
		var count = str.length;
		var len = count;
		var st = 0;
		while ((st < len) && (str.charAt(st) <= ' '))  st++;
		while ((st < len) && (str.charAt(len - 1) <= ' ')) len--;
		return ((st > 0) || (len < count)) ? str.substring(st, len) : str;
	},

	spaceAll: function(str){
		var index,len;
		while(true){
			index = str.indexOf(" ");
			//공백이 없으면 종료 합니다.
			if(index == -1)break;
			//문자열 길이를 구합니다.
			len = str.length;
			//공백을 잘라 냅니다.
			str = str.substring(0,index) + str.substring((index+1),len);
		}
		return str;
	},

	/**
	 * 숫자콤마넣기
	 */
	numberFormat: function(num, cipher) {
		if(!cipher) cipher = 3;
		var i = 0;
		var str = "";
		var len = num.length;
		var arr = new Array(len);
		while(i <len)
		{
			arr[i]=num.charAt(len-i-1);
			str=arr[i]+str;
			i++;
			if(i%cipher==0 && i!=len)
			{
				str=','+str;
			}
		}
		return str;
	},

	/**
	 * 태그제거
	 */
	stripTags: function(str, allowed_tags) {
		// Strips HTML and PHP tags from a string  
		//
		// version: 909.322
		// discuss at: http://phpjs.org/functions/strip_tags
		// +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +   improved by: Luke Godfrey
		// +      input by: Pul
		// +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +   bugfixed by: Onno Marsman
		// +      input by: Alex
		// +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +      input by: Marc Palau
		// +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +      input by: Brett Zamir (http://brett-zamir.me)
		// +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +   bugfixed by: Eric Nagel
		// +      input by: Bobby Drake
		// +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +   bugfixed by: Tomasz Wesolowski
		// *     example 1: strip_tags('<p>Kevin</p> <b>van</b> <i>Zonneveld</i>', '<i><b>');
		// *     returns 1: 'Kevin <b>van</b> <i>Zonneveld</i>'
		// *     example 2: strip_tags('<p>Kevin <img src="someimage.png" onmouseover="someFunction()">van <i>Zonneveld</i></p>', '<p>');
		// *     returns 2: '<p>Kevin van Zonneveld</p>'
		// *     example 3: strip_tags("<a href='http://kevin.vanzonneveld.net'>Kevin van Zonneveld</a>", "<a>");
		// *     returns 3: '<a href='http://kevin.vanzonneveld.net'>Kevin van Zonneveld</a>'
		// *     example 4: strip_tags('1 < 5 5 > 1');
		// *     returns 4: '1 < 5 5 > 1'
		var key = '', allowed = false;
		var matches = [];
		var allowed_array = [];
		var allowed_tag = '';
		var i = 0;
		var k = '';
		var html = '';
		var replacer = function (search, replace, str) {
			return str.split(search).join(replace);
		};
		// Build allowes tags associative array
		if (allowed_tags) {
			allowed_array = allowed_tags.match(/([a-zA-Z0-9]+)/gi);
		}
		str += '';
		// Match tags
		matches = str.match(/(<\/?[\S][^>]*>)/gi);
		// Go through all HTML tags
		for (key in matches) {
			if (isNaN(key)) {
				// IE7 Hack
				continue;
			}
			// Save HTML tag
			html = matches[key].toString();
			// Is tag not in allowed list? Remove from str!
			allowed = false;
			// Go through all allowed tags
			for (k in allowed_array) {
				// Init
				allowed_tag = allowed_array[k];
				i = -1;
				if (i != 0) { i = html.toLowerCase().indexOf('<'+allowed_tag+'>');}
				if (i != 0) { i = html.toLowerCase().indexOf('<'+allowed_tag+' ');}
				if (i != 0) { i = html.toLowerCase().indexOf('</'+allowed_tag)   ;}
				// Determine
				if (i == 0) {
					allowed = true;
					break;
				}
			}
			if (!allowed) {
				str = replacer(html, "", str); // Custom replace. No regexing
			}
		}
		return str;
	},

	/**
	 * 바이트 계산
	 */
	getByte: function(str) {
		var strLen = str.length;
		var i = 0;
		var textByte = 0;
		var oneChar = "";

		for(i=0; i< strLen; i++)
		{
			// 한글자추출
			oneChar = str.charAt(i);
			if (escape(oneChar).length > 4) {
				// 한글이면 2를 더한다.
				textByte = textByte + 2;
			} else {
				// 그외의 경우는 1을 더한다.
				textByte++;
			}
		}
		return textByte;
	},
	/**
	 * 바이트 포멧 변환
	 * @param bytes
	 * @returns {String}
	 */
	getByteFormat: function(bytes) {
		if(bytes>=1000000000) bytes=(bytes/1000000000).toFixed(2)+' GB';
		else if (bytes>=1000000) bytes=(bytes/1000000).toFixed(2)+' MB';
		else if (bytes>=1000) bytes=(bytes/1000).toFixed(2)+' KB';
		else if (bytes>1) bytes=bytes+' bytes';
	    else if (bytes==1) bytes=bytes+' byte';
		else bytes='0 byte';	
		return bytes;
	},	
	/**
	 * 텍스트에리어 글자수 제한 설정
	 * 텍스트에리어의 이벤트에 적용 onKey
	 */
	setTextLimit: function(id1, id2, maxByte) {
		//var textArea = $('#'+id1);
		//var textBox = $('#'+id2);		
		var str	= $('#'+id1).val();	// 이벤트가 일어난 컨트롤의 value 값
		var strLen = str.length;	// 전체길이
		var i = 0;					// for문에 사용
		var textByte = 0;			// 한글일경우는 2 그밖에는 1을 더함
		var subLen = 0;				// substring하기 위해서 사용
		var oneChar = "";			// 한글자 임시 변수
		var str2 = "";				// 글자수를 초과하면 제한할수 글자전까지만 보여준다.   	
		for(i=0; i< strLen; i++)
		{
			oneChar = str.charAt(i);	 // 한글자추출	
			if (escape(oneChar).length > 4) {
				textByte = textByte + 2;
			} else {
				textByte++;
			}
			if(textByte <= maxByte) {
				subLen = i + 1;
			}
		}
		//$('#'+id2).val(textByte);
		$('#'+id2).html(textByte);
		if(textByte > maxByte) {
			alert( maxByte + "바이트 이상 입력할 수 없습니다.\n추가로 입력된 내용은 자동으로 삭제됩니다.");
			str2 = str.substr(0, subLen);
			$('#'+id1).val(str2);
			//$('#'+id2).val(maxByte);
			$('#'+id2).html(maxByte);
		}
		$('#'+id1).focus();
	},

	/**
	 * 텍스트에리어 글자수 제한 초기화
	 */
	initTextLimit: function(id1, id2, maxByte) {
		$('#'+id1).val("");
		$('#'+id2).html("0");
	},

	/**
	 * 텍스트에리어 바이트계산
	 */
	getTextByte: function(id) {
		var str = $('#'+id).val();	// 이벤트가 일어난 컨트롤의 value 값
		var strLen = str.length;	// 전체길이
		var i = 0;					// for문에 사용
		var textByte = 0;			// 한글일경우는 2 그밖에는 1을 더함
		var oneChar = "";			// 한글자 임시 변수	
		for(i=0; i< strLen; i++)
		{
			oneChar = str.charAt(i);
			if (escape(oneChar).length > 4) {
				textByte = textByte + 2;
			} else {
				textByte++;
			}
		}
		return textByte;
	},

	/**
	 * 브라우저 홈페이지 설정
	 */
	setHomePage: function(url) {
		document.body.style.behavior='url(#default#homepage)';
		document.body.setHomePage(url);
	},

	/**
	 * 쿠키정보
	 */
	getCookie: function(cookieName) {
		var search = cookieName + "=";
		var cookie = document.cookie;
		if(cookie.length > 0) {
			startIndex = cookie.indexOf(cookieName);
			// 만약 존재한다면
			if( startIndex != -1 ) {
				startIndex += cookieName.length;
				endIndex = cookie.indexOf( ";", startIndex );
				if( endIndex == -1) endIndex = cookie.length;
				return unescape( cookie.substring( startIndex + 1, endIndex ) );
			} else {
				return false;
			}
		} else {
			return false;
		}
	},

	/**
	 * 쿠키설정
	 */
	setCookie: function(cookieName, cookieValue, expireDate) {
		var today = new Date();
		today.setDate( today.getDate() + parseInt( expireDate ) );
		document.cookie = cookieName + "=" + escape( cookieValue ) + "; path=/; expires=" + today.toGMTString() + ";";
	},

	/**
	 * 쿠키삭제
	 */
	deleteCookie: function(cookieName, expireDate) {
		var expireDate = new Date();
		//어제 날짜를 쿠키 소멸 날짜로 설정한다.
		expireDate.setDate(expireDate.getDate() - parseInt( expireDate ));
		document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString() + "; path=/";
	},

	/**
	 * 엔터키 금지
	 */
	noEnter: function() {
		if(event.keyCode == 13) event.returnValue=false;
	},
	download: function(src) {
		jQuery('<form action="'+src+'" method="post"></form>').appendTo('body').submit().remove();
	},
	excel: function(id, filename) {
		var obj = $(id);
		$(id + " > table").attr("border", "1px solid #EEE");
		var filename = escape(filename);
		var html = escape(obj.html());
		var url = "/common/excel.do";
		jQuery('<form action="'+url+'" method="post"><input type="hidden" name="html" value="'+html+'"/><input type="hidden" name="filename" value="'+filename+'"/>"</form>').appendTo('body').submit().remove();
		//$(id + " > table").attr("border", "0px");
		//jQuery('<form action="'+url+'" method="post"><textarea name="html">'+html+'</textarea><input type="hidden" name="filename" value="'+filename+'"/>"</form>').appendTo('body').submit().remove();
	},
	/**
	 * 파일명 (ex c:\test\test2\test.txt > test.txt)
	 */
	getFilename: function(src) {
		var finfo = src.split("\\");
		var filename = finfo[finfo.length-1];
		return filename;
	},
	/**
	 * 확장자명 (ex test.txt > txt)
	 */
	getExtname: function(filename) {
		var extinfo = filename.split(".");
		var extname = extinfo[extinfo.length-1];
		return extname;
	},
	/**
	 * 현재도메인정보
	 */
	getDomain: function() {
		var url = document.location.href.split("//");
		var domain  = url[1].substring(0,url[1].indexOf("/"));
		return domain;
	},
	/**
	 * 쿼리스트링
	 * @param key
	 * @returns
	 */
	getQueryString: function(key) {
		if(this.isObj(key)) {
			var url = window.location.search.substring(1);
			var param = url.split('&');
			for (var i = 0; i < param.length; i++) {
				var  sparam = param[i].split('=');
				if (sparam[0] == key) {
					return sparam[1];
				}
			}
			return "";
		} else {
			var url = window.location.search.substring(1);
			var param = url.split('&');
			return param;
		}
	},
	setQueryString: function(param) {
		var cnt = 0;
		var queryString = "";
		for(var key in param) {
			// 파라미터에 값이 전달 될 경우
			if(this.isObj(param[key])) {
				if(cnt == 0) {
					queryString += "?"; 
				} else {
					queryString += "&";
				}
				queryString += key + "=" + param[key];
				cnt++;
			// 파라미터에 값이 전달이 안된 경우
			} else {
				// 기존의 값으로 유지
				var sparam = this.getQueryString(key);
				console.log(key, sparam);
				if(this.isObj(sparam)) {
					if(cnt == 0) {
						queryString += "?" 
					} else {
						queryString += "&"
					}
					queryString += key + "=" + sparam;
					cnt++;
				}
			}
		}
		return queryString;
	},
	/**
	 * string pad
	 */
	pad: function(str, pstr, length) {
		while (str.length < length)
			str = pstr + str;
		return str;
	},
	time2str: function(timestamp, format) {
		if(!this.isObj(format)) format = "";
		var date = new Date(Number(timestamp));
		var ndate = this.date2str(date, format);
		return ndate;
	},
	date2str: function(date, format) {
		if(this.isObj(date) == false) {
			var date = new Date();
		}
		console.log(date);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		var h = date.getHours();
		var i = date.getMinutes();
		var s = date.getSeconds();
		m = (m < 10) ? "0"+m : m;
		d = (d < 10) ? "0"+d : d;
		h = (h < 10) ? "0"+h : h;
		i = (i < 10) ? "0"+i : i;
		s = (s < 10) ? "0"+s : s;
		var ndate;
		switch(format.toUpperCase()) {
			case "YMDHIS" : 
				ndate = y+""+m+""+d+""+h+""+i+""+s;
				break;
			case "YMD" : 
				ndate = y+""+m+""+d;
				break;
			case "Y-M-D" : 
				ndate = y+"-"+m+"-"+d;
				break;
			case "HIS" : 
				ndate = h+i+s;
				break;
			case "H:I:S" : 
				ndate = h+":"+i+":"+s;
				break;
			default :
				ndate = y+"-"+m+"-"+d+" "+h+":"+i+":"+s;
				break;
		}
		console.log(ndate);
		return ndate;
	},
	/**
	 * 체크박스 전체선택, 전체선태해제
	 */
	checkboxId: function(element, name) {
		if($(element).prop("checked")) {
			//해당화면에 전체 checkbox들을 체크해준다
			$("input[type="+name+"]").prop("checked",true);
		// 전체선택 체크박스가 해제된 경우
		} else {
			//해당화면에 모든 checkbox들의 체크를해제시킨다.
			$("input[type="+name+"]").prop("checked",false);
		}	
	}
};

/**
 * 글자수 바이트 확인
 *     String 객체 확장
 *
 * @returns {number}
 */
String.prototype.getBytes = function() {
    var contents = this;
    var str_character;
    var int_char_count;
    var int_contents_length;

    int_char_count = 0;
    int_contents_length = contents.length;

    for (var k = 0; k < int_contents_length; k++) {
        str_character = contents.charAt(k);
        if (escape(str_character).length > 4)
            int_char_count += 2;
        else
            int_char_count++;
    }
    return int_char_count;
}

/**
 * DOM 이벤트 핸들링 헬퍼
 * @param url
 * @constructor
 */
var ElementHandler = function(url) {
    this.baseurl = url;
    this.initFuncs = new Array();
	this.initiated = false;
	var that = this;
	$(document).ready( function() {
		that.init();
	});
}
ElementHandler.prototype.ready = function(initFunc) {
    this.initFuncs.push(initFunc);
}
ElementHandler.prototype.assignUrl = function(url, $element) {
    var regex = /#{([a-zA-Z0-9]+)}/gi;
    var match = regex.exec(url);
    var result = url;
    while (match != null) {
        var key = match[1];
        var value = common.getQueryString(key);
        if(key=='page' && ($.isEmptyObject(value) || $.trim(value)=='') ) {
            value = '1';
        }

        result = result.replace(match[0], value);
        match = regex.exec(url);
    }
    var regex = /\${([a-zA-Z0-9]+)}/gi;
    var match = regex.exec(url);
    while (match != null) {
        var key = match[1];
        var value = $element.attr(key);
        result = result.replace(match[0], value);
        match = regex.exec(url);
    }

    return result;
}
ElementHandler.prototype.init = function() {
	if(this.initiated==true) return;

    $.each(this.initFuncs, function(k, v) {
        v();
    });

    if(this.bindData) {
        this.runBind(this.bindData);
    }

	this.initiated = true;
}
ElementHandler.prototype.bind = function(data) {
    this.bindData = data;
}
ElementHandler.prototype.runBind = function(data) {
    var elementHandler = this;
    $.each(data, function(elementId, metadata) {
        var elementList = new Array();

        if(elementId.substr(0,1)=='$') {
            elementList = $('.'+elementId.replace('$',''));
        } else {
            elementList.push(document.getElementById(elementId));
        }
        if(elementList==null) {
            return;
        }
        $.each(elementList, function(k, element) {
            var $element = $(element);

            var handler = null;
            var eventType = 'click';

            if (typeof(metadata) == 'string') {
                handler = function () {
                    if(metadata.confirm) if(!window.confirm(metadata.confirm)) return;
                    common.redirect(elementHandler.baseurl + elementHandler.assignUrl(metadata, $(this)));
                }
            }
            else if (typeof(metadata) == 'function') {
                handler = metadata;
            }
            else {
                var action = metadata.action;

                if (metadata.event) eventType = metadata.event;

                if (typeof(action) == 'function') {
                    handler = action;
                }
                else if (action == 'post') {
                    var url = '/';
                    var callback = null;
                    var data = {};

                    if(typeof metadata.url == 'string') {
                        url = metadata.url;
                    } else if(typeof metadata.url == 'function') {
                        url = metadata.url.call(element);
                    }

                    if(typeof metadata.data == 'object') {
                        data = metadata.data;
                    } else if(typeof metadata.data == 'function') {
                        data = metadata.data.call(element);
                    }

                    if (typeof(metadata.result) == 'function') {
                        callback = metadata.result;
                    } else if (typeof(metadata.result) == 'object') {
                        callback = function (data) {
							var result = {};
							if(typeof(data)=='string') {
								result = $.parseJSON(data);
							} else {
								result = data;
							}
                            var resultCode = result.result;
                            var callbackMeta = metadata.result[resultCode];
                            if (callbackMeta) {
                                if (typeof(callbackMeta) == 'string') {
                                    window.alert(callbackMeta);
                                } else if (callbackMeta.message) {
                                    if (callbackMeta.message) window.alert(callbackMeta.message);
                                    if (callbackMeta.reload && callbackMeta.reload == true) {
                                        window.location.reload();
                                    } else if (callbackMeta.redirect) {
                                        common.redirect(elementHandler.baseurl + elementHandler.assignUrl(callbackMeta.redirect, $(this)));
                                    }
                                }
                            } else if (resultCode != '1') {
                                common.error(vm.error);
                            }
                        }
                    }

                    handler = function () {
                        if(metadata.confirm) if(!window.confirm(metadata.confirm)) return;

                        if (typeof(metadata.preHandler) == 'function') {
                            if (!metadata.preHandler()) {
                                return false;
                            }
                        }

						if(typeof(metadata.stateful)!='undefined' && metadata.stateful==true) {
							var $loadingButton = $element.button('loading');
						}

                        $.ajax({ type: "POST",
                            contentType: 'application/x-www-form-urlencoded',
                            url: elementHandler.baseurl + url,
                            data: data,
                            success: callback,
							complete: function() {
								if(typeof($loadingButton)!='undefined') {
									$loadingButton.button('reset')
								}
							}
                        });
                    }

                }
                else if (action == 'submit') {
                    var $form = $('#' + metadata.form);
                    var url = metadata.url;
                    var callback = null;

                    if (typeof(metadata.result) == 'function') {
                        callback = metadata.result;
                    } else if (typeof(metadata.result) == 'object') {
                        callback = function (data) {
							var result = {};
							if(typeof(data)=='string') {
								result = $.parseJSON(data);
							} else {
								result = data;
							}
                            var resultCode = result.result;
                            var resultData = result.data;
                            var callbackMeta = metadata.result[resultCode];
                            metadata.result['data'] = resultData;
                            
                            if (callbackMeta) {
                                if (typeof(callbackMeta) == 'string') {
									window.alert(callbackMeta);
								} else if (typeof(callbackMeta) == 'function') {
									callbackMeta();
                                } else if (callbackMeta.message) {
									if (callbackMeta.message) window.alert(callbackMeta.message);
                                    if (callbackMeta.reload && callbackMeta.reload == true) {
                                        window.location.reload();
                                    } else if (callbackMeta.redirect) {
                                    	if(metadata.result.data != null) callbackMeta.redirect += resultData;
                                        common.redirect(elementHandler.baseurl + elementHandler.assignUrl(callbackMeta.redirect, $(this)));
                                    }
                                }
                            } else if (resultCode != '1') {
                                common.error(vm.error);
                            }
                        }
                    }

                    handler = function () {
                        if(metadata.confirm) if(!window.confirm(metadata.confirm)) return;

                        if (metadata.validate) {
                            $form.validate(metadata.validate);
                        }

                        if ($form.valid()) {
                            if (typeof(metadata.preHandler) == 'function') {
                                if (!metadata.preHandler()) {
                                    return false;
                                }
                            }

							if(typeof(metadata.stateful)!='undefined' && metadata.stateful==true) {
								var $loadingButton = $element.button('loading');
							}

                            $.ajax({ type: "POST",
                                contentType: 'application/x-www-form-urlencoded',
                                processData: false,
                                url: elementHandler.baseurl + url,
                                data: $form.serialize(),
                                success: callback,
								complete: function() {
									if(typeof($loadingButton)!='undefined') {
										$loadingButton.button('reset');
									}
								}
                            });
                        }
                    }
                }
            }

            $element.on(eventType, handler);
        });
    });
};


/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var dateFormat = function () {
	var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = dateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var _ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

// Some common format strings
dateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
	dayNames: [
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	],
	monthNames: [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
	return dateFormat(this, mask, utc);
};

var PNTTableGrid = function(elementId) {
	this._id = elementId;
	PNTTableGrid._instance[this._id] = this;

	this._option = {
		autorender: true,
		autoscrolltop: true
	};

	this._url = null;
	this._param = {};
	this._page = 1;
	this._element = document.getElementById(elementId);
	if(this._element==null) {
		window.alert('not found element:'+elementId);
	}

	this._resultData = {};
	this._columns = {'beaconNum':'고유번호'};
	this._onLoadSuccess = function(json) {};
	this._onRowRender = function(columnId, columnValue, element, row) {};
}
PNTTableGrid._instance = {};
PNTTableGrid.getInstance = function(id) {
	return PNTTableGrid._instance[id];
}
PNTTableGrid.prototype.getId = function() {
	return this._id;
}
PNTTableGrid.prototype.setOption = function(key, value) {
	this._option[key] = value;
}
PNTTableGrid.prototype.getOption = function(key) {
	return this._option[key];
}
PNTTableGrid.prototype.setColumnset = function(columnset) {
	this._columns = columnset;
}
PNTTableGrid.prototype.getColumnset = function() {
	return this._columns;
}
PNTTableGrid.prototype.getUrl = function() {
	return this._url;
}
PNTTableGrid.prototype.setParam = function(key, value) {
	this._param[key] = value;
}
PNTTableGrid.prototype.getParam = function() {
	return this._param;
}
PNTTableGrid.prototype.setPage = function(page) {
	this._page = page;
}
PNTTableGrid.prototype.setOnLoadSuccess = function(func) {
	this._onLoadSuccess = func;
}
PNTTableGrid.prototype.setOnRowRender = function(func) {
	this._onRowRender = func;
}
PNTTableGrid.prototype.reload = function() {
	this._param['page'] = this._page;
	this.load(this._url, this._param);
}
PNTTableGrid.prototype.load = function(url, param) {
	this._url = url;
	if(typeof param!='undefined') {
		for(var pkey in param) {
			var pvalue = param[pkey];
			if(typeof pvalue=='undefined' || pvalue==null || pvalue=='') delete param[pkey];
		}
		this._param = param;
		if(typeof param.page!='undefined' && !isNaN(param.page)) {
			this._page = param.page;
		} else {
			this._param['page'] = this._page;
		}
	} else {
		this._param = {page: this._page};
	}

	var that = this;
	$.ajax({
		type: "get",
		dataType: 'json',
		url: this._url,
		data: this._param!=null?this._param:{},
		cache: false
	}).done(function(json) {
		that._resultData = json;
		that._onLoadSuccess.call(that, json);
		if(that._option.autorender==true) {
			that.render();
		}

	}).fail(function() {
		window.alert(vm.infoError);
	});
}
PNTTableGrid.prototype.render = function() {
	var that = this;
	$(this._element).html('');
	var table = $('<table class="table table-bordered table-hover table-responsive pull-left" />');
	var thead = $('<thead></thead>');
	var headTr = $('<tr />');
	for(var columnId in this._columns) {
		var columnName = this._columns[columnId];
		headTr.append($('<th>'+columnName+'</th>'));
	}
	thead.append(headTr);
	table.append(thead);

	var tbody = $('<tbody />');
	if(typeof(this._resultData.list)=='undefined' || this._resultData.list==null) {
		window.alert(vm.infoError);
		return;
	}

	var listData = this._resultData.list;
	for(var index in listData) {
		var row = listData[index];
		var bodyTr = $('<tr align="center" />');

		for(var columnId in this._columns) {
			var tdText = row[columnId];
			var td = $('<td />');
			td.addClass(columnId);
			td.html(tdText);

			if(this._onRowRender!=null && typeof this._onRowRender!='undefined') {
				this._onRowRender.call(this, columnId, tdText, td.get(0), row);
			}

			bodyTr.append(td);
		}
		tbody.append(bodyTr);
	}
	table.append(tbody);
	$(this._element).append(table);


	var pagingDiv = $('<div class="center"></div>');
	var pagingUl = $('<ul class="pagination"></div>');
	pagingUl.append($(this._resultData.pagination));
	pagingUl.find('a').each(function(index, element) {
		var hrefValue = $(element).attr('href');
		if(hrefValue=='?') $(element).removeAttr('href');
		else {
			var chref = hrefValue.replace('\?', '#ptg/'+that._id+'/');
			$(element).attr('href', chref);
		}
	});

	pagingDiv.append(pagingUl);
	$(this._element).append(pagingDiv);

}

$(window).on('hashchange load', function() {
	var hash = window.location.hash.substr(1);
	var raw = hash.split('?')[0];

	if(raw.substring(0,3)=='ptg') {
		var rawitems = raw.split('/');
		var instanceId = rawitems[1];
		var params = rawitems[2];
		var pTableGrid = PNTTableGrid.getInstance(instanceId);

		var paramArray = params.split('&');
		for(var i=0; i<paramArray.length; i++) {
			var paramItem = paramArray[i];
			var paramItemArray = paramItem.split('=');
			if(paramItemArray[0]=='page') {
				pTableGrid.setPage(paramItemArray[1]);
			} else {
				if(typeof paramItemArray[1]!='undefined' && paramItemArray[1]!=null && paramItemArray[1]!='') {
					pTableGrid.setParam(paramItemArray[0], paramItemArray[1]);
				}
			}
		}

		pTableGrid.reload();
		if(pTableGrid.getOption('autoscrolltop')==true) {
			window.scroll(0, 0);
		}
	}
});


var PNTFormDataLoader = function(elementId) {
	this._option = {
		autoassign: true
	};
	this._element = document.getElementById(elementId);
	this._resultData = {};
	this._onLoadSuccess = function(json){};
	this._onRenderComplete = function() {};
	this._onColumnAssign = function(columnId, columnValue, element, data){};
}
PNTFormDataLoader.prototype.setOption = function(key, value) {
	this._option[key] = value;
}
PNTFormDataLoader.prototype.getOption = function(key) {
	return this._option[key];
}
PNTFormDataLoader.prototype.setOnLoadSuccess = function(func) {
	this._onLoadSuccess = func;
}
PNTFormDataLoader.prototype.setOnRenderComplete = function(func) {
	this._onRenderComplete = func;
}
PNTFormDataLoader.prototype.setOnColumnAssign = function(func) {
	this._onColumnAssign = func;
}
PNTFormDataLoader.prototype.load = function(url, param) {
	var that = this;
	$.ajax({
		type: "get",
		dataType: 'json',
		url: url,
		data: param||{},
		cache: false
	}).done(function(json) {
		that._resultData = json;
		that._onLoadSuccess.call(that, json);
		if(that._option.autoassign==true) {
			that.assignAll();
		}
	}).fail(function() {
		window.alert(vm.infoError);
	});
}
PNTFormDataLoader.prototype.assignAll = function() {
	console.log(this._resultData);
	var data = this._resultData.data;
	console.log(data);
	var that = this;
	for(var columnId in data) {
		var columnValue = data[columnId];

		var elements = $(this._element).find('[name='+columnId+']');
		if(elements.length>0) {
			elements.each(function(index, element) {
				var tagName = $(element).prop("tagName").toLowerCase();
				if(tagName=='select') {
					$(element).find('option').each(function (i, el) {
						if ($(el).val() == columnValue) {
							$(el).prop('selected', 'selected');
						}
					});
				} else if(tagName=='input' && $(element).prop('type').toLowerCase()=='radio') {
					if(element.value == columnValue) {
						$(element).prop('checked', 'checked');
					}
				} else {
					$(element).val(columnValue);
				}
				that._onColumnAssign.call(that, columnId, columnValue, element, data);
			});
		} else {
			that._onColumnAssign.call(that, columnId, columnValue, null, data);
		}

	}

	that._onRenderComplete.call(that);
}

