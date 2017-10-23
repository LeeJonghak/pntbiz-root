var cmmFloorSel = {
	_floorCodeArr: [],
	_tgtObjName: "",
	// selectbox를 만들지 안만들지 체크 후 만듬
	callLocationSel: function(pObj){
	    var tgtDept = 0;

	    if(pObj){
	    	tgtDept = $(".selLct").index(pObj)+1;
	    	$(pObj).next("label").remove();
	    };
	    //이후select 삭데
	    //$(".selLct:gt("+(tgtDept-1)+")").parent().parent().remove();
	    $("#floorLocationList>div:gt("+(tgtDept-1)+")").remove();

	    var dept2 = cmmFloorSel._floorCodeArr.filter(function (value) {
	            	var secondCondition = (tgtDept > 0)?(value.upperNodeId == $(pObj).val()): true;
	            	return (value.levelNo == (tgtDept) && secondCondition);
	            });

	    if($(pObj).val() == "" || dept2.length == 0){
	        console.log("다음없음");

	        $(pObj).attr("name", cmmFloorSel._tgtObjName);
	    	return false;
	    }else{
	        //���� select ��
	        var tgtSel = cmmFloorSel.makeLocationSel(tgtDept);

	        for(var idx in dept2){
	            $(tgtSel).append("<option value='"+dept2[idx].nodeId+"'>"+dept2[idx].nodeName1+"</option>");
	        }
	    }
	},

	// selectbox 를 만듬
	makeLocationSel: function(pDept){
	    var cloneDiv;
	    var rtnSelObj;
	    console.log("makeLocationSel dept:"+pDept);

	    var labelArr = cmmFloorSel._floorCodeArr.filter(function (value) {
									            	return (value.levelNo == pDept);
									            });

	    cloneDiv = $("#divLctForm").clone();
	    $(cloneDiv).removeAttr("id");
	    $(cloneDiv).removeAttr("style");
	    $(cloneDiv).find("label").text(labelArr[0].typeCode);

	    rtnSelObj = $(cloneDiv).find("select")
	    $(rtnSelObj).addClass("selLct");

		//���� select name �����ϰ� ���� ��Ǵ� select�� name attr �ο�
		$("#floorLocationList select").removeAttr("name");
	    $(rtnSelObj).attr("name", cmmFloorSel._tgtObjName);

	    $("#floorLocationList").append(cloneDiv);

	    return rtnSelObj;
	},
	// floor 값을 가지고 Hierarchy 구조를 가져옴
	// return : array
	getLocationHierarchy: function(pFloor){
		var rtnArr = [];

		//get floor name from _floorCodeArr array
		var floorObj = cmmFloorSel._floorCodeArr.filter(function (item) {
            										return (item.nodeId == pFloor);
            									});
		if(floorObj != null && floorObj.length > 0){
			rtnArr.push(floorObj[0]);

			var upperLevelId = floorObj[0].upperNodeId;
			var upperObj;
			for(var idx=0; idx < floorObj[0].levelNo; ++idx){
				upperObj = cmmFloorSel._floorCodeArr.filter(function (item) {
														return (item.nodeId == upperLevelId);
													});
				if(upperObj != null && upperObj.length > 0){
					rtnArr.unshift(upperObj[0]);
					upperLevelId = upperObj[0].upperNodeId;
				}
			}
		}
		return rtnArr;
	}
};