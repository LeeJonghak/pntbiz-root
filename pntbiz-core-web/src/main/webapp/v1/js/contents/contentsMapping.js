$(document).ready( function() {
	if($('#contentsMappingKeyword').val() == "") $('#contentsMappingKeyword').focus();	
	$('#contentsMappingKeyword').bind('keyup', function(e) { if(e.keyCode==13) contentsMapping.search(); });	
	$('#checkAll').bind('click', function() { common.checkboxId('#checkAll','checkbox'); });
	$('#contentsMappingSearchBtn').bind('click', function() { contentsMapping.search(); });
	$('#contentsMappingListBtn').bind('click', function() { contentsMapping.list(); });
	$('#contentsMappingDelBtn').bind('click', function() { contentsMapping.del('mapping'); });
	$('#evnetMappingDelBtn').bind('click', function() { contentsMapping.del('event'); });
});

var contentsMapping = {
	_listURL: "/contents/mapping.do",
	_delURL: "/contents/deleteMapping.ajax.do",
	_form: "#form1",
	list: function() {
		this._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "refType": "", "refSubType": "", "conType": ""});
		common.redirect(this._listURL);
	},
	search: function() {
		var refType = $("#refType option:selected").val();
		var refSubType = $("#refSubType option:selected").val();
		var conType = $("#conType option:selected").val();
		var opt = $("#opt option:selected").val();
		var keyword = common.trim($("#contentsMappingKeyword").val());
		this._listURL += common.setQueryString({"page": 1, "refType": refType, "refSubType": refSubType, "conType": conType, "opt":opt, "keyword":keyword});
		common.redirect(this._listURL);
	},
	del: function(delType) {
		var mappingInfo = $('input[name=checkbox]:checked');			
		
		var mappingInfoList = [];
		mappingInfo.each(function(index) {
			var $checkbox = $(this);
			mappingInfoList[mappingInfoList.length] = $checkbox.val();
		});
		
		var result = confirm(vm.delConfirm);
		if(result) {
			$.ajax({ type: "POST",
				url: this._delURL,
				data: $.param({mappingInfoList:mappingInfoList, delType:delType}, true),
				success: this.delResult
			});
		}
	},
	delResult: function(data) {
		var result = {};
		if(typeof(data)=='string') {
			result = $.parseJSON(data);
		} else {
			result = data;
		}
		switch(result.result) {
			case "1" :
				contentsMapping._listURL += common.setQueryString({"page": "", "opt": "", "keyword": "", "refType": "", "refSubType": "", "conType": ""});
				common.redirect(contentsMapping._listURL);
				break;
			case "2" : common.error(vm.delFail); break;
			default : common.error(vm.delError); break;
		}
	}
};
