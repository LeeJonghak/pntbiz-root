/**
 * Created by sl.kim on 2017-09-04.
 */

$(document).ready(function () {
    if(window.location.pathname=="/info/interface/list.do") {
        $("#interfaceBindingType").on("change", function () {
           list($("#interfaceBindingType").val(), $("#interfaceCommandType").val());
        });
        $("#interfaceCommandType").on("change", function () {
            list($("#interfaceBindingType").val(), $("#interfaceCommandType").val());
        });
        $("#init").on("click", function () {
            list("", "");
        });

        for(var i=1; i<$(".panel-body tr").size(); i++) {
            var targetInfoJson = JSON.parse($(".panel-body tr:eq("+i+") #targetInfo").val());
            $(".panel-body tr:eq("+i+") #protocol").html(targetInfoJson['protocol']);
            $(".panel-body tr:eq("+i+") #host").html(targetInfoJson['host']);
            $(".panel-body tr:eq("+i+") #port").html(targetInfoJson['port']);
            $(".panel-body tr:eq("+i+") #uri").html(targetInfoJson['uri']);
            $(".panel-body tr:eq("+i+") #method").html(targetInfoJson['method']);
        }

    }
    if(window.location.pathname=="/info/interface/mform.do") {
        var targetInfoJson = JSON.parse($("#targetInfoJson").val());
        $("#"+targetInfoJson['protocol']).attr("selected", "selected");
        $("#host").val(targetInfoJson['host']);
        $("#port").val(targetInfoJson['port']);
        $("#uri").val(targetInfoJson['uri']);
        $("#method").val(targetInfoJson['method']);

        var messageHeaderJson = JSON.parse($("#messageHeaderJson").val());
        var index = 0;
        for(var key in messageHeaderJson) {
            if(index!=0)
                $("#addColumn").click();
            $(".txt-col-key:eq("+index+")").val(key);
            $(".txt-col-value:eq("+index+")").val(messageHeaderJson[key]);
            index++;
        }
    }
    if(window.location.pathname=="/info/interface/form.do") {
        appendInterfaceCommandType();
        appendZones("");
        $("#interfaceBindingType").on("change", function () {
                $.ajax({
                    type : 'POST',
                    url:'/info/interface/selectzones.do',
                    data : $("#interfaceBindingType"),
                    success:function(data){
                        appendZones(data);
                    }
                });
            appendInterfaceCommandType();
        });
        /*$("#bindingZoneId").on("change", function () {
            var id = $("#bindingZoneId option:selected").attr("id").replace("zoneId", "");
            for(var j=0; j<$("#bindingZoneName").children().length; j++) {
                $("#bindingZoneName").children().eq(j).removeAttr("selected");
            }
            $("#zoneName"+id).attr("selected", "selected");
            for(var j=0; j<$("#bindingZoneId").children().length; j++) {
                if($("#bindingZoneName").children().eq(j).attr("selected")=="selected") {
                    console.log($("#bindingZoneName").children().eq(j).attr("value"));
                }
            }
        });*/
    }
});

function appendZones(data) {
    if(data=="") {
        $("#bindingZoneId").empty();
        /*$("#bindingZoneName").empty();*/
        $("#bindingZoneId").attr("disabled", "disabled");
    }
    else if(data!="") {
        $("#bindingZoneId").empty();
        /*$("#bindingZoneName").empty();*/
        $("#bindingZoneId").removeAttr("disabled");
        for(var i=0; i<data.length; i++) {
            var bindingZoneId = data[i]["bindingZoneId"];
            var bindingZoneName = data[i]["bindingZoneName"];
            $("#bindingZoneId").append("<option value='"+bindingZoneId+"' id='zoneId"+i+"'>"+bindingZoneId+"("+bindingZoneName+")"+"</option>");
            /*$("#bindingZoneName").append("<option value='"+bindingZoneName+"' id='zoneName"+i+"'>"+bindingZoneName+"</option>");*/
        }
    }
}

function appendInterfaceCommandType() {
    $("#interfaceCommandType").empty();
    if($("#interfaceBindingType").val()=="LOCATION_COMMON") {
        $("#interfaceCommandType").append("<option value='LOCATION_CHANGE' id='LOCATION_CHANGE'>"+vm.locationChange+"</option>");
    }
    else {
        if($("#interfaceBindingType").val()=="FLOOR_COMMON" || $("#interfaceBindingType").val()=="FLOOR" ) {
            $("#interfaceCommandType").append("<option value='FLOOR_IN' id='FLOOR_IN'>"+vm.floorIn+"</option>");
            $("#interfaceCommandType").append("<option value='FLOOR_STAY' id='FLOOR_STAY'>"+vm.floorStay+"</option>");
            $("#interfaceCommandType").append("<option value='FLOOR_OUT' id='FLOOR_OUT'>"+vm.floorOut+"</option>");
        }
        else if($("#interfaceBindingType").val()=="GEOFENCE_COMMON" || $("#interfaceBindingType").val()=="GEOFENCE_GROUP") {
            $("#interfaceCommandType").append("<option value='GEOFENCE_IN' id='GEOFENCE_IN'>"+vm.geofenceIn+"</option>");
            $("#interfaceCommandType").append("<option value='GEOFENCE_STAY' id='GEOFENCE_STAY'>"+vm.geofenceStay+"</option>");
            $("#interfaceCommandType").append("<option value='GEOFENCE_OUT' id='GEOFENCE_OUT'>"+vm.geofenceOut+"</option>");
        }
        $("#interfaceCommandType").append("<option value='RESTRICTION_IN' id='RESTRICTION_IN'>"+vm.restrictionIn+"</option>");
        $("#interfaceCommandType").append("<option value='RESTRICTION_STAY' id='RESTRICTION_STAY'>"+vm.restrictionStay+"</option>");
        $("#interfaceCommandType").append("<option value='RESTRICTION_OUT' id='RESTRICTION_OUT'>"+vm.restrictionOut+"</option>");
    }
}

function list(interfaceBindingType, interfaceCommandType) {
    var url = "/info/interface/list.do";
    var param = "?interfaceBindingType="+interfaceBindingType+"&interfaceCommandType="+interfaceCommandType;
    location.href=url+param;
}

// AdditionalAttribute 유효성 검사 (key값 중복되는지, key나 value가 비어있는지)
function validateAttributes() {
    for(var i=0; i<$(".txt-col-key").size()-1; i++) {
        for(var j=i+1; j<$(".txt-col-key").size(); j++) {
            if($(".txt-col-key:eq("+i+")").val()!="" && ($(".txt-col-key:eq("+i+")").val()==$(".txt-col-key:eq("+j+")").val())) {
                alert(vm.duplicateAttributeKey((i+1), (j+1)));
                return false;
            }
        }
        if($(".txt-col-key:eq("+i+")").val()=="" && $(".txt-col-value:eq("+i+")").val()!="") {
            alert(vm.inputAttribute((i+1), "key"));
            return false;
        }
        else if($(".txt-col-key:eq("+i+")").val()!="" && $(".txt-col-value:eq("+i+")").val()=="") {
            alert(vm.inputAttribute((i+1), "value"));
            return false;
        }
    }
    return true;
}

function checkDuplication(strClass) {
    for(var i=0; i<$("."+strClass).size()-1; i++) {
        for(var j=i+1; j<$("."+strClass).size(); j++) {
            if($("."+strClass+":eq("+i+")").val()!="" && ($("."+strClass+":eq("+i+")").val()==$("."+strClass+":eq("+j+")").val())) {
                alert(vm.duplicateAttributeKey((i+1), (j+1)));
                return false;
            }
        }
    }
}

elementHandler.bind({
    formBtn: 'info/interface/form.do',
    regBtn: {
        event: 'click',
        action: 'submit',
        form: 'form1',
        url: 'info/interface/reg.do',
        preHandler : function () {
            return validateAttributes();
        },
        result: {
            1: {message:vm.regSuccess,
                redirect:'info/interface/list.do'},
            2: vm.regError
        }
    },
    modBtn: {
        event: 'click',
        action: 'submit',
        form: 'form1',
        url: 'info/interface/mod.do',
        preHandler : function () {
            return validateAttributes();
        },
        result: {
            1: {message:vm.modSuccess,
                redirect:'info/interface/list.do?page=#{page}&interfaceBindingType=#{interfaceBindingType}&interfaceCommandType=#{interfaceCommandType}'},
            2: vm.modError
        }
    },
    delBtn: {
        action: 'submit',
        form: 'form1',
        url: 'info/interface/del.do',
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess,
                redirect:'info/interface/list.do'},
            2: vm.delError
        }
    },
    listBtn: function(){
        var page = common.getQueryString('page');
        var interfaceBindingType = common.getQueryString('interfaceBindingType');
        var interfaceCommandType = common.getQueryString('interfaceCommandType');
        var list = common.getQueryString('list')
        var param;
        if(typeof list!='undefined')
            param = common.setQueryString({"page": "", "interfaceBindingType": "", "interfaceCommandType":""});
        else
            param = common.setQueryString({"page": 1, "interfaceBindingType":interfaceBindingType, "interfaceCommandType":interfaceCommandType});
        common.redirect(elementHandler.baseurl+'info/interface/list.do'+param);
        console.log(list, param);
    },
    addColumn: function() {
         var source = $('#replication-target .form-group').clone(true);
        /*source.children().first().children().first().attr("name", "key key"+$(".txt-col-key").size());
        source.children().eq(1).children().first().attr("name", "value value"+$(".txt-col-value").size());*/
         source.find('.btn-col-remove').click(function() {
            $(this).parent().parent().remove();
         });
         $('#dynamic-column').append(source);
    }
});
