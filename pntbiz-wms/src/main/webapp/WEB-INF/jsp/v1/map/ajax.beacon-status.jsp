<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script type="text/javascript">
var $form = $('#map-popup-form');

$('#bcImgDelBtn').click(function() {
	if(window.confirm(vm.delConfirm)) {
		$(".fileupload-preview > img").attr("src", ""); // 이미지 초기화

		 $.ajax({
            type: 'POST',
            url: '/beacon/info/bcImgDel.do',
            dataType: 'json',
            contentType: 'application/x-www-form-urlencoded',
            processData: false,
            data: $form.serialize()
        }).done(function(json) {
            if(json.result=='1') {
            	window.alert('설치사진이 삭제되었습니다.');
            }
           }).fail(function() {
               window.alert('오류가 발생하였습니다. #2');
           });
	};
});

$('#bcImgUploadBtn').click(function() {
	var imgSize = 1048576 * 5;
	var imgFormat = "png|jpe?g|gif";
	$form.validate({
		rules: {
			imgSrc: { extension: imgFormat, filesize: imgSize }
        },
        messages: {
        	imgSrc: { extension: vm.extension, filesize: vm.filesize }
        },
        submitHandler: function (frm) {},
        success: function (e) {}
	});

	$.ajax({
	       type: 'POST',
	       url: '/beacon/info/bcImgUpload.do',
	       dataType: 'json',
	       processData: false,
	       contentType: false,
	       data: new FormData($form[0])
	     }).done(function(json) {
	       if(json.result=='1') {
	    	   window.alert('설치사진이 등록되었습니다.');
	       }
	      }).fail(function() {
	          window.alert('오류가 발생하였습니다. #2');
	      }); 
});

$('#btnStateUpdate').click(function() {
    var state = $('#beaconState').val();
    var stateReason = $("#beaconStateReason").val();
    var beaconNum = ${beacon.beaconNum};
    var lastDate = ${beacon.lastDate};
    $.ajax({
        type: 'post',
        url: 'beacon/state.do',
        data: {beaconNum:${beacon.beaconNum}, state:state, reason:stateReason},
        dataType: 'json'
    }).done(function(json) {
        if(json.result=='1') {
            /**
             * 비콘 상태 정상 업데이트되면 마커 아이콘 변경
             */
            var googleMap = elementHandler.googleMap;
            var marker = googleMap.getShapeObject('marker','beacon','beacon-'+beaconNum);
            var mtime = 60*60*24*30; // 30일 동안 업데이트 되지 않은 경우 주황색 마크로 표시
            var nowtime = new Date().getTime()/1000;
            var icon = '';
            if(state=='2') {
                /*icon = elementHandler.baseurl + 'images/inc/marker_red_10.png';*/
                icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_red_10.png';
            } else if(state=='1') {
                icon = 'https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_green_10.png';
            } else {
                if(nowtime-lastDate>=mtime) {
                    icon = 'https://static.pntbiz.com/indoorplus/' + 'images/inc/marker_orange_10.png';
                } else {
                    icon = 'https://static.pntbiz.com/indoorplus/'+'images/inc/marker_blue_10.png';
                }
            }
            marker.setIcon(icon);

            //window.alert('저장되었습니다.');
        } else if(json.result=='3') {
            window.alert('존재하지 않는 비콘 정보입니다.');
        } else {
            window.alert('오류가 발생하였습니다. #1');
        }
    }).fail(function() {
        window.alert('오류가 발생하였습니다. #2');
    });

});
</script>
<h5><spring:message code="word.beacon.control.information"/></h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#beacon-info-basic" aria-controls="basic" role="tab" data-toggle="tab"><spring:message code="word.basic.information"/></a></li>
</ul>
<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보보
    -->
    <div role="tabpanel" class="tab-pane active" id="beacon-info-basic">
            
            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.uuid"/></label>
                <div class="col-sm-9">
                    <input type="text" id="UUID" name="UUID" value="${beacon.UUID}" readonly="readonly"  class="form-control input-sm" placeholder="<spring:message code="word.uuid"/>"  />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.major.version"/></label>
                <div class="col-sm-9">
                    <input type="text" id="majorVer" name="majorVer" value="${beacon.majorVer}" readonly="readonly" class="form-control input-sm" placeholder="<spring:message code="word.major.version"/>"  />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.minor.version"/></label>
                <div class="col-sm-9">
                    <input type="text" id="minorVer" name="minorVer" value="${beacon.minorVer}" readonly="readonly" class="form-control input-sm" placeholder="<spring:message code="word.minor.version"/>"  />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.beacon.install.image"/></label>
                <div class="col-sm-9">
                	<form name="map-popup-form" id="map-popup-form" class="form-horizontal" role="form" enctype="multipart/form-data" style="margin-top:10px;">
                	<input type="hidden" id="beaconNum" name="beaconNum" value="${beacon.beaconNum}" />
	                <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="${beacon.imgUrl}" id="img-thumbnail1" class="img-thumbnail" alt="<spring:message code="word.image" />" />
						</div>
						<div class="row">
							<div class="col-xs-4 pr5">
								<span class="button btn-system btn-file btn-block">
									<input type="file" id="imgSrc" name="imgSrc">
									<span class="fileupload-new"><spring:message code="word.choose" /></span> 
									<span class="fileupload-exists"><spring:message code="word.change" /></span>					
								</span>
							</div>
							<div class="col-xs-4 pr5">			
								<input type="button" id="bcImgUploadBtn" class="btn btn-primary btn-block" value="<spring:message code="btn.modify"/>" />					
							</div>
							<div class="col-xs-4 pr5">			
								<input type="button" id="bcImgDelBtn" class="btn btn-warning btn-block" value="<spring:message code="btn.delete"/>" />					
							</div>
						</div>
					</div>
					</form>
				
<%--                     <img src="http://54.65.251.37/beacon/${beacon.minorVer}.jpg" width="160" onerror="$('#beacon-install-img-not-found').show();$(this).hide();"/>
                    <div id="beacon-install-img-not-found" style="display:none;"><spring:message code="word.none"/></div> --%>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.status"/></label>
                <div class="col-sm-9">
                    <div class="input-group">
                        <select id="beaconState" class="form-control input-sm" onchange="if(this.value=='0') {this.style.backgroundColor='white';} else if(this.value=='1'){this.style.backgroundColor='lawngreen';} else if(this.value=='2') {this.style.backgroundColor='red';}" <c:if test="${beacon.state eq '0'}">style="background-color:white;"</c:if><c:if test="${beacon.state eq '1'}">style="background-color:lawngreen;"</c:if><c:if test="${beacon.state eq '2'}">style="background-color:red;"</c:if>>
                            <option value="0" <c:if test="${beacon.state eq '0'}">selected="selected"</c:if> style="background-color:white;"><spring:message code="word.unconfirmed"/></option>
                            <option value="1" <c:if test="${beacon.state eq '1'}">selected="selected"</c:if> style="background-color:lawngreen;"><spring:message code="word.confirm"/></option>
                            <option value="2" <c:if test="${beacon.state eq '2'}">selected="selected"</c:if> style="background-color:red;"><spring:message code="word.loss"/></option>
                        </select>
                        <span class="input-group-btn">
                            <input type="button" id="btnStateUpdate" value="<spring:message code="btn.modify"/>" class="btn btn-default btn-sm" />
                        </span>
                    </div>
                    <div style="margin-top:5px;">
                        <input type="text" id="beaconStateReason" value="${beacon.stateReason}" placeholder="<spring:message code="word.reason"/>" class="form-control" />
                    </div>
                </div>
            </div>

            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
            </div>
    </div>
</div>

