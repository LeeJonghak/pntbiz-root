
$(document).ready(function () {
    if(window.location.pathname=="/beacon/restrictedZone/list.do") {
        for(var i=1; i<$("tbody>tr").size(); i++) {
            var jsonData = JSON.parse($("tbody>tr:eq("+i+")>td:eq(3)").html());
            var tdStr="";
            for(var key in jsonData)
                tdStr = tdStr + key +" = " + jsonData[key] + ",　";
            $("tbody>tr:eq("+i+")>td:eq(3)").html(tdStr.substr(0,tdStr.length-2));
        }
    }
    if(window.location.pathname=="/beacon/info/mform.do") {
        var jsonData = JSON.parse($("#jsonData").val());
        for(var i=0; i<jsonData.length; i++) {
            if(i!=0)
                $("#addColumn").click();
            $(".txt-col-key:eq("+i+")").val(jsonData[i]["key"]);
            $(".txt-col-value:eq("+i+")").val(jsonData[i]["value"]);
            $(".txt-col-displayName:eq("+i+")").val(jsonData[i]["displayName"]);
        }
    }
    else if(window.location.pathname=="/beacon/restrictedZone/mform.do") {
        var jsonData = JSON.parse($("#jsonData").val());
        var index = 0;
        for(var key in jsonData) {
            if(index!=0)
                $("#addColumn").click();
            $(".txt-col-key:eq("+index+")").val(key);
            $(".txt-col-value:eq("+index+")").val(jsonData[key]);
            index++;
        }
    }
    else if(window.location.pathname=="/beacon/restrictedZone/form.do") {
        $("#zoneType").on("change", function () {
            if($("#zoneType").val()=="FLOOR") {
                $("#floor").show();
                $("#geofence").hide();
            }
            else {
                $("#geofence").show();
                $("#floor").hide();
            }
            $("#zoneId").val("");
        });
    }
    if(window.location.pathname=="/beacon/restrictedZone/mform.do" || window.location.pathname=="/beacon/restrictedZone/form.do") {
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true}).on('dp.change', function(event) {
            if(event.date==null)
                $('#startDateTimestamp').val("");
            else
                $('#startDateTimestamp').val(event.date.unix());
        });
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', sideBySide: true}).on('dp.change', function(event) {
            if(event.date==null)
                $('#endDateTimestamp').val("");
            else
                $('#endDateTimestamp').val(event.date.unix());
        });
    }
    if(window.location.pathname=="/beacon/restrictedZone/list.do") {
        $(".permitted").on("click", function () {
            if($(".panel-body tbody>tr").size()==0)
                return;
            if(!confirm(vm.zoneTypeChange($(':radio[name="permitted"]:checked').val()=='TRUE' ? vm.permitted : vm.restricted))) {
                event.preventDefault();
                return;
            }
            $.ajax({
                type: "POST",
                url: "modpermitted.do",
                data: "beaconNum="+$("#beaconNum").val()+"&permitted="+($(':radio[name="permitted"]:checked').val()=='TRUE' ? 'TRUE' : 'FALSE'),
                success:function(data){
                    if(data=="1")
                        window.location.href = "/beacon/restrictedZone/list.do?beaconNum="+$("#beaconNum").val()+"&UUID="+$("#UUID").val();
                    else
                        alert("변경에 실패하였습니다.");
                },
                error:function(e){
                    alert(e);
                }
            });
        });
        $("#restrictedZoneFormBtn").on("click", function () {
            location.href='/beacon/restrictedZone/form.do?beaconNum='+$("#beaconNum").val()+'&UUID='+$("#UUID").val()+'&permitted='+$(':radio[name="permitted"]:checked').val();
        });
    }
});

// ExternalAttribute 유효성 검사 (key값이 중복되는지, key나 value가 비어있는지)
function validateExternalAttribute() {
    for(var i=0; i<$(".txt-col-key").size()-1; i++) {
        for(var j=i+1; j<$(".txt-col-key").size(); j++) {
            if($(".txt-col-key:eq("+i+")").val()!="" && ($(".txt-col-key:eq("+i+")").val()==$(".txt-col-key:eq("+j+")").val())) {
                alert(vm.duplicateAttributeKey((i+1), (j+1)));
                return false;
            }
        }
    }
    for(var i=0; i<$(".txt-col-key").size(); i++) {
        if(($(".txt-col-key:eq("+i+")").val()=="") && ($(".txt-col-value:eq("+i+")").val()!="")) {
            alert(vm.inputAttribute((i+1), "key"));
            return false;
        }
        else if(($(".txt-col-key:eq("+i+")").val()!="") && ($(".txt-col-value:eq("+i+")").val()=="")) {
            alert(vm.inputAttribute((i+1), "value"));
            return false;
        }
        else if(($(".txt-col-key:eq("+i+")").val()=="") && ($(".txt-col-value:eq("+i+")").val()=="") && ($(".txt-col-displayName:eq("+i+")").val()!="")) {
            alert(vm.inputAttributes((i+1), "key", "value"));
            return false;
        }
    }
    return true;
}

// AdditionalAttribute 유효성 검사 (key값 중복되는지, key나 value가 비어있는지)
function validateAdditionalAttribute() {
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

// startDate와 endDate 유효성 검사 ( startDate < endDate )
function validateStartEndDate() {
    if($("#startDateTimestamp").val()!="" && $("#endDateTimestamp").val()!="" && Number($("#startDateTimestamp").val())>Number($("#endDateTimestamp").val())) {
        alert(vm.startNendDateValidation);
        return false;
    }
    return true;
}

elementHandler.ready(function() {
//        $('#UUID').mask('99999999-9999-9999-9999-999999999999');
    $('#map-canvas').hide();

    if($('#descByte').length>0) {
        var bytes = $('#beaconDesc').val().getBytes();
            $('#descByte').html(bytes+' / 200 bytes');
            $('#beaconDesc').keyup(function() {
                var bytes = $(this).val().getBytes();
                $('#descByte').html(bytes+' / 200 bytes');
            });
        }

    });
    elementHandler.bind({
        searchBtn: function() {
            $('#form1').attr('action', 'list.do');
            $('#form1').submit();
        },
        formBtn: 'beacon/info/form.do',
        $mformBtn:  'beacon/info/mform.do?beaconNum=${beaconNum}&page=#{page}&opt=#{opt}&keyword=#{keyword}',
        listBtn: function(){
            var page = common.getQueryString('page');
            var opt = common.getQueryString('opt');
            var keyword = common.getQueryString('keyword');
            var list = common.getQueryString('list');
            if(typeof list!='undefined' && list=='beaconState') {
                var field = common.getQueryString('field');
                var sort = common.getQueryString('sort');
                var param = common.setQueryString({"page": "", "opt": "", "keyword":"", "field": field, "sort": sort});
                common.redirect(elementHandler.baseurl+'beacon/monitor/list.do'+param);
            } else{
                var param = common.setQueryString({"page": 1, "opt":opt, "keyword":keyword});
                common.redirect(elementHandler.baseurl+'beacon/info/list.do'+param);
            }
            console.log(list, param);
        },
        regBtn: {
            action: 'submit',
            form: 'form1',
            url: 'beacon/info/reg.do',
            preHandler : function () {
                return validateExternalAttribute();
            },
            result: {
                1: {message:vm.regSuccess,
                    redirect:'beacon/info/list.do'},
                2: vm.regError
            }
        },
        modBtn: {
            event: 'click',
            action: 'submit',
            form: 'form1',
            url: 'beacon/info/mod.do',
            preHandler : function () {
                return validateExternalAttribute();
            },
            result: {
                1: {message:vm.modSuccess,
                    redirect:'beacon/info/list.do?page=#{page}&opt=#{opt}&keyword=#{keyword}'},
                2: vm.modError
            }
        },
        delBtn: {
            action: 'submit',
            form: 'form1',
            url: 'beacon/info/del.do',
            confirm: vm.delConfirm,
            result: {
                1: {message:vm.delSuccess,
                    redirect:'beacon/info/list.do?page=#{page}&opt=#{opt}&keyword=#{keyword}'},
                2: vm.delError
            }
        },
        restrictedZoneRegBtn: {
            event : 'click',
            action: 'submit',
            form: 'form1',
            url: 'beacon/restrictedZone/reg.do',
            confirm: vm.regConfirm,
            preHandler : function () {
                return (validateAdditionalAttribute()&&validateStartEndDate() )
            },
            result: {
                1: {message:vm.regSuccess,
                    redirect:'beacon/restrictedZone/list.do?beaconNum=#{beaconNum}&UUID=#{UUID}'},
                2: vm.regError
            }
        },
        restrictedZoneModBtn: {
            event : 'click',
            action: 'submit',
            form: 'form1',
            url: 'beacon/restrictedZone/mod.do',
            confirm: vm.modConfirm,
            preHandler : function () {
                return (validateAdditionalAttribute()&&validateStartEndDate() )
            },
            result: {
                1: {message:vm.modSuccess,
                    redirect:'beacon/restrictedZone/list.do?beaconNum=#{beaconNum}&UUID=#{UUID}'},
                2: vm.regError
            }
        },
        restrictedZoneDelBtn: {
            action: 'submit',
            form: 'form1',
            url: 'beacon/restrictedZone/del.do',
            confirm: vm.delConfirm,
            result: {
                1: {message:vm.delSuccess,
                    redirect:'beacon/restrictedZone/list.do?beaconNum=#{beaconNum}&UUID=#{UUID}'},
                2: vm.delError
            }
        },
        beaconMapBtn: function() {
            $(this).prop( "disabled", true );
            $('#map-canvas').toggle(400, function() {
                $('#beaconMapBtn').prop( "disabled", false );
                if(elementHandler.googleMap==null) {
                    elementHandler.googleMap = new GoogleMap('map-canvas', function() {
                        this.map.setZoom(20);
                        var _this = this;

                        this.addEventListener('click', function(event) {
                            if(_this.marker) {
                                _this.marker.setPosition(event.latLng);
                            } else {
                                _this.marker = new google.maps.Marker({
                                    position: event.latLng,
                                    title: '',
                                    draggable: false,
                                    icon: 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png',
                                    map: _this.map
                                });
                            }
                            var lat = event.latLng.lat();
                            var lng = event.latLng.lng();
                            $('#lat').val(lat);
                            $('#lng').val(lng);
                        });

                        /**
                         * 기초데이터가 있을경우
                         */
                        if(elementHandler.beacon) {
                            var latlng = new google.maps.LatLng(elementHandler.beacon.lat, elementHandler.beacon.lng);
                            if(this.marker) {
                                this.marker.setPosition(latlng);
                            } else {
                                this.marker = new google.maps.Marker({
                                    position: latlng,
                                    title: '',
                                    draggable: false,
                                    icon: 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png',
                                    map: this.map
                                });
                            }
                            elementHandler.googleMap.map.setCenter(latlng);
                        }

                        /**
                         * 층이미지
                         */
                        elementHandler.bssServiceRequest = new BssServiceRequest(elementHandler.baseurl);
                        elementHandler.floorMap = new FloorMap(this, elementHandler.bssServiceRequest);
                        elementHandler.floorMap.setOnFloorData(function(floors) {
                            elementHandler.floorMap.changeFloor(parseInt($('#floor-selector').val()));
                        });

                        /**
                         * 층 선택 박스
                         */
                        $('#floor-selector').on('change', function() {
                            var $select = $(this);
                            var floor = $select.val();
                            elementHandler.floorMap.changeFloor(floor);
                        });
                    });
                }
            });
        },
        addColumn: function() {
            var source = $('#column-source').clone();
            source.find('.txt-col-key').val('');
            source.find('.txt-col-value').val('');
            source.find('.txt-col-displayName').val('');
            source.find('.btn-col-remove').click(function() {
                $(this).parent().parent().remove();
            });
            $('#dynamic-column').append(source);
        }
    });
