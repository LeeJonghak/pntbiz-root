var combobox = {
	
	/*
	 * 옵션 초기화
	 */
	init: function(id) {		
		$('#'+id).find('option').remove().end();
	},	
	/*
	 * 옵션추가(마지막)
	 */
	addOption: function(id, value, text) {
		$('#'+id).append("<option value='"+value+"'>"+text+"</option>");
	},
	/*
	 * 옵션추가(처음)
	 */
	addOptionFirst: function(id, value, text) {
		$('#'+id).prepend("<option value='"+value+"'>"+text+"</option>");
	},	
	/*
	 * 옵션값이 맞는것 삭제
	 */
	deleteOption: function(id, value) {
		switch(value) {
			case "first" : 
				$('#'+id+' option:first').remove();
				break;
			case "last" :
				$('#'+id+' option:last').remove();
				break;
			default :
				$('#'+id+' option:eq('+value+')').remove();
				break;
		}
	}, 
	
	/**
	 * 멀티콤보박스 옵션 위로이동
	 */
	upOption: function(id) {
		$('#'+id+' option:selected').each(function(){
			$(this).insertBefore($(this).prev());
		});
	},
	/**
	 * 멀티콤보박스 옵션 아래로 이동
	 */
	downOption: function(id) {
		$('#'+id+' option:selected').each(function(){
			$(this).insertAfter($(this).next());
		});
	}, 
	/**
	 * 멀티콤보박스끼리 옵션이동
	 */
	moveOption: function(id, id2) {
		
	},
	
	/**
	 * 이동결과를 반영 (|으로 구분)
	 * id : 콤보박스의 아이디 / id2 : input의 아이디
	 */
	resultValue: function(id, id2) {
		var cnt = 0;			
		res = new Array();	
		$('#'+id+' option').each(function(){			
			res[cnt] = $(this).val();
			cnt++;
		});		
		res = res.join("|");
		$('#'+id2).val(res);
	},
	
	/**
	 * 이동결과를 반영 (|으로 구분)
	 * id : 콤보박스의 아이디 / id2 : input의 아이디
	 */
	resultText: function(id, id2) {
		var cnt = 0;			
		res = new Array();	
		$('#'+id+' option').each(function(){
			res[cnt] = $(this).html();
			cnt++;
		});		
		res = res.join("|");
		$('#'+id2).val(res);
	},
	
	/*
	 * 선택된 텍스트값
	 */
	getText: function(id) {
		var val = $('#'+id+' option:selected').text();
		return val;		
	},
	/*
	 * 선택된 값
	 */
	getValue: function(id) {
		var val = $('#'+id+' option:selected').val();
		return val;		
	},
	/*
	 * 선택된옵션의 인덱스값
	 */
	getIndex: function(id) {
		var val = $('#'+id+' option').index($('#'+id+' option:selected'));
		return val;
	},
	/**
	 * 셀렉트박스의 옵션갯수
	 */
	getSize: function(id) {
		var val = $('#'+id+' option').size();
		return val;
	}
};

var checkbox = {
	
	/*
	 * 체크갯수 체크
	 * <input type='checkbox' name='chk[]' />
	 * <input type='checkbox' name='chk[]' />
	 * .....
	 * 위와 같이 사용할때 선택한 카운트 수 반환
	 */
	cnt: function(name) {
		//var cnt = $('input:checkbox[name='+name+']:checked').length;
		var cnt = $("input[name='"+name+"']:checked").length;
		return cnt;
	},
	
	/*
	 * 전체 체크
	 * type (1:전체체크, 2:전체해제, 3:리버스)
	 */
	check: function(name, type) {		
		switch(type) {
			case "2" :
				$("input:checkbox[name='"+name+"']").attr('checked', false);
				break;
			case "3" : 
				$("input:checkbox[name='"+name+"']").each(function() {
					if($(this).is(':checked')) {
						$(this).attr('checked', false);
					} else {
						$(this).attr('checked', true);
					}
				});
				break;
			case "1" :				
			default : 
				$("input[name='"+name+"']").attr('checked', true);
				//$("input:checkbox[name='"+name+"']").attr("checked", true);
				break;
		}
	},
	
	/**
	 * 선택값 스트링
	 */
	getCheckValue: function(name) {
		var val = "";
		var i = 0;
		$('input:checkbox[name='+name+']').each(function() {
			if($(this).is(':checked')) {
				if(val == "") {
					val += $(this).val();
				} else {
					val += "|" + $(this).val();
				}
			}
			i++;
		});
		return val;
	}, 
	
	/**
	 * 체크여부
	 */
	isCheck: function(name) {
		var checked = $('input:checkbox[name='+name+']').attr("checked");
		if(checked) {
			return true;
		} else {
			return false;
		}		
	},
	
	isCheckByID: function(id) {
		var checked = $('#'+id).attr("checked");
		if(checked) {
			return true;
		} else {
			return false;
		}		
	}
};

var radio = {
	/*
	 * 선택된 값
	 */
	getValue: function(name) {
		var val = $('input:radio[name='+name+']:checked').val();
		return val;
	}
};