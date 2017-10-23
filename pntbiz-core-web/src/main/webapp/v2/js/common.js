var windowHeight, contentHeight, containerHeight, headerHeight = 71;
var now_url, now_depth; 
var siteTitle;
var pop_max_zindex = 201;
var pop_position_top = 120;
var pop_position_left = 300;
var pop_position_gap = 20;
var pop_open_id = new Array();

var errorMsg = {
	"error.no.address":"주소를 입력해주세요.",
	"error.ajax.404":"요청하신 페이지를 찾을 수 없습니다.",
	"error.ajax.500":"시스템 오류가 발생했습니다. 관리자에게 문의해주세요.",
	"error.ajax.420":"권한이 없습니다."
};

$(function(){
	viewSizeGet();

	pageEvent();

	$(document).on("click",".popClose",function(){
		var id = $(this).parents(".modal").attr("id").replace("pop_","");
		if(pop_open_id.indexOf(id) != -1) pop_open_id.splice(pop_open_id.indexOf(id),1);
		$(this).parents(".modal").remove();
		if(pop_open_id.length == 0){
			$("#overlay").css("z-index",100).hide();
			pop_max_zindex = 201;
			pop_position_top = 120;
			pop_position_left = 300;
		}else{
			var lastId = pop_open_id[(pop_open_id.length-1)];
			$("#overlay").css("z-index",($("#pop_"+lastId).css("z-index")-1));
		}
	});

	$(document).on("click",".btn_mobileOpen",function(e){
		e.preventDefault();
		window.open($(this).attr("href"),'모바일뷰어','width=360,height=640,scrollbars=yes,top=0,left=0');
	});
	
	
	$('#gnb li a').click(function(e){
		if($(this).parent().children("ul").length > 0){
			e.preventDefault();
			if($(this).parent().hasClass('active')){
				$(this).parent().removeClass('active');
			}else{
				$(this).parent().siblings().removeClass('active');
				$(this).parent().addClass('active');
			}
		}
	});

	$('#header a.btnMenu').click(function(){
		if($('#lnb').hasClass('active')){
			$(this).removeClass('active');
			$('#lnb').removeClass('active');
			$('#header, #content, #footer').css({'margin-left':'250px'});
		}else{
			$(this).addClass('active');
			$('#lnb').addClass('active');
			$('#header, #content, #footer').css({'margin-left':'92px'});
		}
	});
	$('#language > a').click(function(){
		if($('#language').hasClass('active')){		
			$(this).parent().removeClass('active');
		}else{
			$(this).parent().addClass('active');
		}
		$('#language ul').toggle();
	});
	
	$('#monitor .monitorData').css({'height':containerHeight});

	//테이블 목록 클릭시 처리
	$(".list.rollover tr td").click(function(){
		if(!$(this).hasClass("notClick")){
			var clickUrl = $(this).parents("table").eq(0).attr("data-clickUrl");
			if(!clickUrl || clickUrl == ""){
				return;
			}
			var paramArr = $(this).parent().attr("data-param").split(",");
			var paramGetStr = "";
			for(i=0; i<paramArr.length; i++){
				if(i!=0) paramGetStr += "&";
				paramGetStr += paramArr[i];
			}
			location.href = clickUrl+"?"+paramGetStr;
		}
	});
});

$(window).resize(function(){
	viewSizeGet();
});

function viewSizeGet(){
	var maxHeight = 0;
	windowHeight = $(window).height();
	contentHeight = $("#content").height();
	containerHeight = $(window).height() - headerHeight;
	if(maxHeight < containerHeight) maxHeight = containerHeight;
	if(containerHeight < contentHeight) maxHeight = contentHeight;
	if($("#lnb").height() < maxHeight){
		$("#lnb").height(maxHeight);
	}
}

/*Ajax Libray*/
function ajaxLayer(id,url,params,option){
	var alertHtml = '<div id="pop_'+id+'" class="modal"><div class="inHeader"><a href="#" class="popClose" title="창닫기">×</a></div><div class="inContent"></div></div>';
	pop_open_id.push(id);
	$("#wrap").append(alertHtml);
	$("#overlay").css("z-index",pop_max_zindex).show();
	pop_max_zindex++;
	pop_position_top += pop_position_gap;
	pop_position_left += pop_position_gap;
	$("#pop_"+id).css({"z-index":pop_max_zindex,"top":pop_position_top+"px","left":pop_position_left+"px"});
	pop_max_zindex++;

	var type = "POST";
	var dataType = "text";
	var contentType = "application/x-wwwform-urlencoded";
	var asyn = true;
	var cache = false;
	var timeout = 3000;
	var useDrag = false;
	var useResize = false;

	if(option){
		if(option['type']) type = option['type'];
		if(option['cache']) cache = option['cache'];
		if(option['asyn']) asyn = option['asyn'];
		if(option['timeout']) timeout = option['timeout'];
		if(option['dataType']) dataType = option['dataType'];
		if(option['contentType']) contentType = option['contentType'];
		if(option['useDrag'] == "Y") useDrag = true;
		if(option['useResize'] == "Y") useResize = true;
	}

	$.ajax({
		type: type,
		url: url,
		data: params,
		dataType: dataType,
		contentType: contentType,
		asyn: asyn,
		cache: cache,
		timeout: timeout,
		success: function(data){
			$("#pop_"+id+" .inContent").html(data);
			pageEvent();

			if(useDrag){
				$("#pop_"+id).draggable({
					handle: $(".inHeader"),
					zIndex: pop_max_zindex
				});
				$("#pop_"+id+" .inHeader").css("cursor","pointer");
			}

			if(useResize){
				$("#pop_"+id).css({"overflow":"hidden","min-height":"58px"}).resizable({
					minWidth: $("#pop_"+id).width()
				});
			}
		},
		error: function(data){
			var error = new ajaxError(data).viewError();
		}
	});
}

function ajaxForm(name){

}

var ajaxError = function(data){
	var status = data.status;
	var responseText = data.responseText;

	var isError = function(){
		switch(status){
			case '420':
				popAlert(errorMsg['error.ajax.420']);
				break;
			case '404':
				popAlert(errorMsg['error.ajax.404']);
				break;
			case '500':
				popAlert(errorMsg['error.ajax.500']);
				break;
			default:
				popAlert(errorMsg['error.ajax.500']);
				break;
		}
		console.log(responseText);
		return;
	};

	this.viewError = function(){
		isError();
	}
};

/*Modal*/
function popAlert(msg,after){
	$("#popAlert").remove();
	var alertHtml = '<div id="popAlert" class="modal"><div class="inHeader"><a href="#" class="popClose" title="창닫기">×</a></div><div class="inContent"><section>'+msg+'<div class="btnArea"><input type="button" class="btn btn-s btn-focus popClose" value="확인"></div></section></div></div>';
	$("#wrap").append(alertHtml);
	$("#overlay").css("z-index",pop_max_zindex).show();
	pop_max_zindex++;
	pop_position_top += pop_position_gap;
	pop_position_left += pop_position_gap;
	$("#popAlert").css({"z-index":pop_max_zindex,"top":pop_position_top+"px","left":pop_position_left+"px"});
	pop_max_zindex++;
	$("#popAlert .inContent .popClose").focus();
	$("#popAlert .popClose").click(function(e){
		if(after) after();
	});
}
function popConfirm(msg,afterYes,afterNo){
	$("#popConfirm").remove();
	var alertHtml = '<div id="popConfirm" class="modal"><div class="inHeader"><a href="#" class="popClose" title="창닫기">×</a></div><div class="inContent"><section>'+msg+'<div class="btnArea"><input type="button" id="btn_confirmYes" class="btn btn-s btn-focus popClose" value="확인"> <a href="#" id="btn_confirmNo" class="btn btn-s popClose">취소</a></div></section></div></div>';
	$("#wrap").append(alertHtml);
	$("#overlay").css("z-index",pop_max_zindex).show();
	pop_max_zindex++;
	pop_position_top += pop_position_gap;
	pop_position_left += pop_position_gap;
	$("#popConfirm").css({"z-index":pop_max_zindex,"top":pop_position_top+"px","left":pop_position_left+"px"});
	pop_max_zindex++;
	$("#btn_confirmNo").focus();
	$("#popConfirm #btn_confirmYes").click(function(e){
		if(afterYes) afterYes();
	});
	$("#popConfirm #btn_confirmNo").click(function(e){
		if(afterNo) afterNo();
	});
}


/*Libary*/
function pageEvent(){
	addInputHandler({input:$(".onlyNum"),dataType:"N",maxlength:7});
	addInputHandler({input:$(".onlyEng"),dataType:"AP"});
	addInputHandler({input:$(".engNum"),dataType:"AN"});
	addInputHandler({input:$(".onlyHan"),dataType:"HA"});
	useDatepicker();
	useFileBtn();
	useTab();
	useTree();
	useSwitchBtn();
	useSelectBox();
	useWordLength();
}
function addInputHandler(conditions){
	var $input = conditions.input;
	var dataType = conditions.dataType;
	var eventType = conditions.eventType;
	if((!$input) || (!dataType)){
		throw {error:"NotEnoughArguments", errorMsg:"required argument is missing " +((!$input)?" target input element":" dataType")}
		return;
	}
	if((!eventType)){
		eventType = "keyup";
	}
	var handlerFunc = conditions.handler;
	if((!handlerFunc)){ 
		handlerFunc = function(event){
			var regEx = null;
			if(dataType == "N"){
				regEx = /[^0-9]/gi;
			}else if(dataType == "AP"){
				regEx = /[^a-z]/gi;
			}else if(dataType == "AN"){
				regEx = /[^a-z0-9]/gi;
			}else if(dataType == "HA"){
				regEx = /[a-z0-9]/gi;
			}else{
				throw {error:"IlregalDataType", errorMsg:"dataType("+dataType+") is incorrect"}     
			}
			remainOnlyTargetValue(regEx, $input,event);
			//return true;
		};  // end of handlerFunc
	} // end of if to check handlerFunc
	$input.on(eventType,handlerFunc);
	if(conditions.maxlength){
		$input.attr("maxlength",conditions.maxlength);
	}
}
function remainOnlyTargetValue(regEx, $input, event) {
	if((!(event.keyCode >= 34 && event.keyCode <= 40)) && event.keyCode != 16){
		var inputVal = $input.val();
		if(regEx.test(inputVal)){
			event.preventDefault ? event.preventDefault() : event.returnValue = false;
			$input.val(inputVal.replace(regEx,''));
		}
	}
}
function useWordLength(){
	$("textarea").each(function(){
		var textName = "none";
		if($(this).attr("name")){
			textName = $(this).attr("name");
		}
		limitByte = parseInt($(this).attr("data-limitByte"));
		if($("wordCount_"+textName).length == 0) $(this).after('<span id="wordCount_'+textName+'" class="wordCount"><b>0</b> / '+limitByte+' Byte</span>');
		$(this).keyup(function(){
			textName = $(this).attr("name");
			limitByte = parseInt($(this).attr("data-limitByte"));
			var totalByte = 0;
			var limitLength = 0;
			var message = $(this).val();
			for(var i =0; i < message.length; i++){
				var currentByte = message.charCodeAt(i);
				if(currentByte > 128) totalByte += 2;
				else totalByte++;
				if(totalByte > limitByte){
					limitLength = i;
					$(this).val(message.substring(0,limitLength));
					totalByte = limitByte;
					break;
					return;
				}
			}
			$("#wordCount_"+textName+" b").text(totalByte);
		});
    });
}
function useSelectBox(){
  $("select[is_element_select='true']").each(function(){ //선택박스 UI제어
		var selectName = "none";
		if($(this).attr("name")){
			selectName = $(this).attr("name");
		}
		var selectUlHtml = '<ul id="selectUl_'+selectName+'" class="selectUl" data-name="'+selectName+'">';
		var selectedText = "";

		$(this).children("option").each(function(index, element) {
			var liClass = '';
			if($(this).attr("selected") && selectedText == ""){
				selectedText = $(this).text();
				liClass = ' class="selected"';
			}

			selectUlHtml += '<li><a href="#" data-value="'+$(this).attr("value")+'"'+liClass+'>'+$(this).text()+'</a></li>';
		});

		selectUlHtml += '</ul>';
		if(selectedText == ""){
			selectedText = $(this).children("option").eq(0).text();
		}
		var selectDivHtml = '<div class="selectUlWrap '+$(this).attr("class")+'"><b>'+selectedText+'</b>'+selectUlHtml+'</div>';

		$(this).before(selectDivHtml);
		$(this).hide();
	});
	$(document).on("click",".selectUl a",function(e){ //선택시 동작
		e.preventDefault();
		var selectName = $(this).parents("ul").eq(0).attr("data-name");
    var eleSelect = $("select[name='"+selectName+"']");
    if (eleSelect.val() != $(this).attr("data-value")) {
      $("select[name='"+selectName+"']").val($(this).attr("data-value"));
      $(this).parents(".selectUlWrap").eq(0).children("b").text($(this).text());
      $(this).parents(".selectUlWrap").eq(0).removeClass("active");
      $(this).parents(".selectUlWrap").eq(0).find("a").removeClass("selected");
      $(this).addClass("selected");
      eleSelect.trigger("change");
    }
	});
	$(document).on("click",".selectUlWrap b",function(e){ //선택박스 열기
		e.preventDefault();
		if(!$(this).parent().hasClass("active")){
			$(".selectUlWrap").removeClass("active");
			$(this).parent().addClass("active");
		}else{
			$(this).parent().removeClass("active");
		}
	});
	$(document).click(function(e){ //선택박스 영역외 클릭시 동작
		if(!$(e.target).parents(".selectUlWrap").length > 0){
			$(".selectUlWrap").removeClass("active");
		}
	});
}
function useSwitchBtn(){			
	$("input[type=checkbox].useSwitch").switchButton({
		labels_placement: "left",
		on_label: 'YES',
		off_label: 'NO',
		width: 100,
		height: 40,
		button_width: 50
	});
	$('.switch-button-label').click(function(){
		if($('.switch-button-label.off').css("display") == "none"){
			$('.switch-button-background').css({'background':'#efe0d1'});
		}else{
			$('.switch-button-background').css({'background':'#dbdbdb'});
		}
	});
}
function useDatepicker(){
	var holidayData = [
		{'mmdd':'1-1','title':'신정'},
		{'mmdd':'3-1','title':'3.1절'},
		{'mmdd':'5-5','title':'어린이날'},
		{'mmdd':'5-10','title':'석가탄신일'},
		{'mmdd':'6-6','title':'현충일'},
		{'mmdd':'8-15','title':'광복절'},
		{'mmdd':'10-3','title':'개천절'},
		{'mmdd':'10-9','title':'한글날'},
		{'mmdd':'12-25','title':'크리스마스'}
	];

	$(".useDatepicker").each(function(){
		var minDate = $(this).attr("data-minDate");
		var maxDate = $(this).attr("data-maxDate");
		var dateFormat = "yy-mm-dd";
		if($(this).attr("data-format")) dateFormat = $(this).attr("data-format");
		$(this).css({"width":"105px"}).datepicker({
			prevText: '이전 달',
			nextText: '다음 달',
			monthNames: ['01','02','03','04','05','06','07','08','09','10','11','12'],
			monthNamesShort: ['01','02','03','04','05','06','07','08','09','10','11','12'],
			dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'],
			dayNamesShort: ['일','월','화','수','목','금','토'],
			dayNamesMin: ['일','월','화','수','목','금','토'],
			dateFormat: dateFormat,
			showMonthAfterYear: true,
			yearSuffix: '/',
			minDate: minDate,
			maxDate: maxDate,
			beforeShowDay: function(date){
				var holidayCheck = false;
				var mmdd = (date.getMonth() + 1)+"-"+date.getDate();
				for(var i=0; i<holidayData.length; i++){
					if(holidayData[i].mmdd == mmdd){
						holidayCheck = true;
						return [true, "date-holiday", holidayData[i].title];
						break;
					}
				}
				if(holidayCheck == false){
					return [true, ""];
				}
			},
			onSelect: function(selectedDate){
			},
			onClose: function(selectedDate){
				if(this.id == "dateFrom" ) {
					if(selectedDate != "" && $("#dateTo").val() != ""){
						if(selectedDate >= $("#dateTo").val()){
							alert("시작날짜는 종료날짜보다 작아야 합니다.");
							$("#dateFrom").val("");
							return;
						}
					}
				}else if(this.id == "dateTo" ) {
					if(selectedDate != "" && $("#dataFrom").val() != ""){
						if($("#dateFrom").val() >= selectedDate){
							alert("종료날짜는 시작날짜보다 커야 합니다.");
							$("#dateTo").val("");
							return;
						}
					}
				}
			}
		});
	});
	
}
function useFileBtn(){
	$("input.fileBtn").each(function(){
		var file_name = $(this).attr("id");
		$(this).after('<span id="for_'+file_name+'"><input type="text" class="w120" value=""> <a href="#" class="btn btn-inline for_fileBtn">찾아보기</a></span>');
		$(this).hide();
		$(this).change(function(){
			$("#for_"+file_name+" input[type='text']").val($(this).val());
		});
	});
	$(".for_fileBtn").click(function(){
		var id = $(this).parent().attr("id").replace("for_","");
		$("#"+id).click();
	});
	$(".btn-multiFile").click(function(e){
		var fileCnt = $(this).parent().find("input[type='file']").length;
		$(this).parent().find("input[type='file']").eq((fileCnt-1)).click();
		e.preventDefault();
	});
}
function useTab(){
	$(".tabList").each(function(){
		if($(this).hasClass("tabBar")){
			var tabBtnWidth = 100/$(this).children("li").length;
			$(this).children("li").css("width",tabBtnWidth+"%");
		}
		$(this).children("li").eq(0).addClass("active");
		$(this).parent().children(".tabPage").eq(0).addClass("active");
	});
	$(".tabList li a").click(function(e){
		if(!$(this).parents(".tabList").hasClass("notUsed")){
			e.preventDefault();
			var tabId = $(this).parent().children("a").attr("href").replace("#","");
			var tabIdLi = $(this).parent().parent().parent();
			$(this).parent().parent().children("li").removeClass("active");
			$(this).parent().addClass("active");
			tabIdLi.find(".tabPage").removeClass("active");
			tabIdLi.find("#"+tabId).addClass("active");
		}
	});

	//Tab Event
	$(".tabBar").each(function(){
		if(!$(this).hasClass("notUsed")){
			var tabBar = $(this);
			if(tabBar.find(".active").length == 0){
				tabBar.find("li").eq(0).not(".notUsed").addClass("active");
				tabBar.siblings(".tabPage").eq(0).addClass("active");
			}
		}
	});
	$(".tabBar li a").on("click",function(e){
		var tabBar = $(this).parent().parent();
		var tabLi = $(this).parent();
		var tabLiAll = $(this).parent().parent().children("li");
		var tabNo = tabLi.index();
		var tabPageAll = tabBar.parent().children(".tabPage");
		var tabPage = tabPageAll.eq(tabNo);
		if(!tabBar.hasClass("notUsed")){
			if(!tabPage.hasClass("active") && tabPage.length > 0){
				tabPageAll.removeClass("active");
				tabPage.addClass("active");
				tabLiAll.removeClass("active");
				tabLi.addClass("active");
				e.preventDefault();
			}
		}
	});
}
function useTree(){
	$(".treeWrap").each(function(){
		var treeWrapLiList = $(this).find("li");
		for(var i=0; i<treeWrapLiList.length; i++){
			var treeWrapLi = treeWrapLiList.eq(i);
			treeWrapLi.prepend("<em></em>");
			var treeWrapLiUl = treeWrapLi.children("ul");
			var treeWrapLiEm = treeWrapLi.children("em");
			treeWrapLiEm.removeClass("none, off, on");
			if(treeWrapLiUl.length == 0){
				treeWrapLiEm.addClass("none");
			}else{
				treeWrapLiEm.append("<sup>"+treeWrapLiUl.find("li").length+"</sup>");
				if(treeWrapLiUl.css("display") == "none"){
					treeWrapLiEm.addClass("off");
				}else{
					treeWrapLiEm.addClass("on");
				}
			}
		}
	});
	$(".treeWrap em").click(function(){
		if($(this).hasClass("on") || $(this).hasClass("off")){
			var treeWrapUl = $(this).parent().children("ul");
			if(treeWrapUl.css("display") == "none"){
				$(this).attr("class","on");
				treeWrapUl.removeClass("hidden");
			}else{
				$(this).attr("class","off");
				treeWrapUl.addClass("hidden");
			}
		}
	});
	$(".treeWrap.useCheckSync input[type='checkbox']").click(function(){
		var treeWrapLi = $(this).parent().parent();
		var treeWrapUl = $(this).parent().parent().parent();
		if($(this).prop("checked") == true){
			treeWrapLi.find("input[type='checkbox']").prop("checked",true);
			for(var i=0; i<treeWrapLi.find("em.off").length; i++){
				var emOff = treeWrapLi.find("em.off").eq(i);
				emOff.attr("class","on");
				emOff.parent().children("ul").removeClass("hidden");
			}
			var allCheck = true;
			for(var i=0; i<treeWrapUl.find("input[type='checkbox']").length; i++){
				var treeWrapUlInput = treeWrapUl.find("input[type='checkbox']").eq(i);
				if(treeWrapUlInput.prop("checked") == false){
					allCheck = false;
					break;
				}
			}
			if(allCheck == true){
				treeWrapUl.parent().children("span.checkBox").children("input").prop("checked",true);
			}
		}else{
			treeWrapLi.find("input[type='checkbox']").prop("checked",false);
			var allCheck = true;
			treeWrapUl.parent().children("span.checkBox").children("input").prop("checked",false);
		}
	});
}
function treeInit(treeObj){
	treeObj.find("em").removeAttr("class");
	for(var i=0; i<treeObj.find("li").length; i++){
		var treeWrapLi = treeObj.find("li").eq(i);
		var treeWrapLiUl = treeWrapLi.children("ul");
		var treeWrapLiEm = treeWrapLi.children("em");
		var treeWrapLiSup = treeWrapLi.children("sup");
		if(treeWrapLiUl.length == 0){
			treeWrapLiEm.addClass("none");
		}else{
			treeWrapLiSup.text(treeWrapLiUl.find("li").length);
			if(treeWrapLiUl.css("display") == "none"){
				treeWrapLiEm.addClass("off");
			}else{
				treeWrapLiEm.addClass("on");
			}
		}
	}
}
function objValCheck(obj,len){
	var title = obj.attr("title");
	if(len > 0){
		if(obj.val().length > len){
			return true;
		}else{
			alert(title+"을(를) "+len+"자 이상 입력해주세요.");
			return false;
		}
	}else{
		if(obj.val().length > 0){
			return true;
		}else{
			alert(title+"을(를) 입력해주세요.");
			return false;
		}
	}
}


/*Cookie*/
function GetCookie(cookieName){
	var search = cookieName+"=";
	var cookie = document.cookie;
	if(cookie.length > 0){
		startIndex = cookie.indexOf(cookieName);
		if(startIndex != -1){
			startIndex += cookieName.length;
			endIndex = cookie.indexOf(";", startIndex);
			if(endIndex == -1) endIndex = cookie.length;
			return unescape(cookie.substring(startIndex + 1, endIndex));
		}else{
			return false;
		}
	}else{
		return false;
	}
}
function SetCookie(cookieName, cookieValue, expireDate){
	var today = new Date();
	today.setDate(today.getDate() + parseInt(expireDate));
	document.cookie = cookieName+"="+escape(cookieValue)+"; path=/; expires="+today.toGMTString()+";";
}
function DelCookie(cookieName){
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() - 1);
	document.cookie = cookieName+"= "+"; expires="+expireDate.toGMTString()+"; path=/";
}