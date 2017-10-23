<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<h5><spring:message code="word.beacon.information" /></h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" <c:if test="${param.tab eq null or param.tab=='1'}">class="active"</c:if>><a href="#beacon-info-basic" aria-controls="basic" role="tab" data-toggle="tab"><spring:message code="word.basic.information"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='2'}">class="active"</c:if>><a href="#beacon-info-beaconcontent" aria-controls="beacon-info-beaconcontent" role="tab" data-toggle="tab"><spring:message code="word.contents"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='3'}">class="active"</c:if>><a href="#beacon-info-content-list" aria-controls="beacon-info-content-list" role="tab" data-toggle="tab"><spring:message code="word.assign.content"/></a></li>
    <%--<li role="presentation" <c:if test="${param.tab=='4'}">class="active"</c:if>><a href="#beacon-info-beaconevent" aria-controls="beacon-info-beaconevent" role="tab" data-toggle="tab">이벤트</a></li>--%>
    <%--<li role="presentation" <c:if test="${param.tab=='5'}">class="active"</c:if>><a href="#beacon-info-event-list" aria-controls="beacon-info-event-list" role="tab" data-toggle="tab"><spring:message code="word.assign.event"/></a></li>--%>
    <li role="presentation" <c:if test="${param.tab=='6'}">class="active"</c:if>><a href="#beacon-image" aria-controls="beacon-image" role="tab" data-toggle="tab"><spring:message code="word.install.image"/></a></li>
</ul>
<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보
    -->
    <div role="tabpanel" class="tab-pane <c:if test="${param.tab eq null or param.tab=='1'}">active</c:if>" id="beacon-info-basic">
        <form name="map-popup-form" id="map-popup-form" class="form-horizontal" role="form" style="margin-top:10px;">
            <input type="hidden" id="beaconNum" name="beaconNum" value="${beacon.beaconNum}" />
            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.uuid"/></label>
                <div class="col-sm-9">
                    <input type="text" id="UUID" name="UUID" value="${beacon.UUID}" size="50" required="required" maxlength="36" uuid="uuid"  class="form-control input-sm" placeholder="<spring:message code="word.uuid"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.major.version"/></label>
                <div class="col-sm-9">
                    <input type="number" id="majorVer" name="majorVer" value="${beacon.majorVer}" required="required" size="30" min="0" max="65535" maxlength="25" class="form-control input-sm" placeholder="<spring:message code="word.major.version"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.minor.version"/></label>
                <div class="col-sm-9">
                    <input type="text" id="minorVer" name="minorVer" value="${beacon.minorVer}" required="required" size="30" maxlength="25" class="form-control input-sm" placeholder="<spring:message code="word.minor.version"/>"  />
                </div>
            </div>
            <div class="form-group">
				<label class="col-sm-3 control-label"><spring:message code="word.beacon.group.name" /></label>
				<div class="col-sm-9">
					<select class="form-control" name="beaconGroupNum">
						<option value="">[<spring:message code="word.beacon.group.name" />]</option>
						<c:forEach items="${beaconGroupList}" var="beaconGroupList">
							<option value="${beaconGroupList.beaconGroupNum}" <c:if test="${beaconGroupList.beaconGroupNum eq beacon.beaconGroupNum}">selected</c:if>>${beaconGroupList.beaconGroupName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.beacon.name"/></label>
                <div class="col-sm-9">
                    <input type="text" id="beaconName" name="beaconName" value="${beacon.beaconName}" required="required" size="30" maxlength="25" class="form-control input-sm" placeholder="<spring:message code="word.beacon.name"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.txpower"/></label>
                <div class="col-sm-9">
                    <input type="number" id="txPower" name="txPower" value="${beacon.txPower}" size="15" maxlength="12"  class="form-control input-sm" placeholder="<spring:message code="word.txpower"/>" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.lat.lng"/></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <span>${beacon.lat} / ${beacon.lng}</span>
                </div>
            </div>
            <div class="form-group form-inline">
                <label class="col-sm-3 control-label"><spring:message code="word.beacon.field" /></label>
                <div class="col-sm-9">
                	<input type="text" id="field1" name="field1" value="${beacon.field1}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                	<input type="text" id="field2" name="field2" value="${beacon.field2}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                	<input type="text" id="field3" name="field3" value="${beacon.field3}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                	<input type="text" id="field4" name="field4" value="${beacon.field4}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                	<input type="text" id="field5" name="field5" value="${beacon.field5}" size="3" minlength="1" maxlength="25" class="form-control  input-sm" placeholder="" />
                </div>
            </div>               
            <div class="form-group">
                <label class="col-sm-3 control-label" style="margin-top:7px;"><spring:message code="word.task"/></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <select class="input-sm slt-assign-task" name="codeNum" data-con-num="${item.conNum}" data-sub-type="${item.refSubType}">
                        <option value="0">[<spring:message code="word.none"/>]</option>
                        <c:forEach items="${codeActionList}" var="code">
                            <option value="${code.codeNum}" <c:if test="${codeAction ne null and codeAction.codeNum eq code.codeNum}">selected</c:if>>${code.codeName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <button id="modBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
                <button id="delBtn" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.delete"/></button>
                <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
            </div>
        </form>
    </div>
    <!--
        TAB - 2 : 할당된 컨텐츠 목록
    -->
    <div role="tabpane2" class="tab-pane <c:if test="${param.tab=='2'}">active</c:if>" id="beacon-info-beaconcontent">
        <div style="margin-top:10px; margin-bottom:10px;">
            <div id="popup-content-success-message" class="alert alert-success hide"></div>
            <div id="popup-content-error-message" class="alert alert-danger hide"></div>

            <div class="row">
                <div class="col-sm-3">
                    <button type="button" id="btn-beacon-unassign-content" class="btn btn-danger btn-sm" disabled><spring:message code="word.delete.multiple.item"/></button>
                </div>
            </div>
        </div>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th width="30" class="">
                        <%--<input type="checkbox" style="margin-top:0px;"/>--%>
                    </th>
                    <th><spring:message code="word.no"/></th>
                    <th><spring:message code="word.contents.name"/></th>
                    <th><spring:message code="word.kind" /></th>
                    <th><spring:message code="word.event.name"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${contents}" var="item">
                <tr>
                    <td><input type="checkbox" class="chk-con-num-un" value="${item.conNum}" data-con-num="${item.conNum}" data-sub-type="${item.refSubType}" /></td>
                    <td>${item.conNum}</td>
                    <td>${item.conName}</td>
                    <td><span class="label <c:if test="${item.refSubType eq 'DETECT'}">label-primary</c:if><c:if test="${item.refSubType eq 'EVENT'}">label-success</c:if>">${item.refSubType}</span></td>
                    <td>
                        <select class="input-sm slt-assign-event" data-con-num="${item.conNum}" data-sub-type="${item.refSubType}">
                            <option value="">[<spring:message code="word.none" />]</option>
                            <c:forEach items="${eventList}" var="event">
                                <option value="${event.evtNum}" <c:if test="${item.evtNum eq event.evtNum}">selected="selected" </c:if> >${event.evtName}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${contents.size()==0}">
                <tr>
                    <td class="center" colspan="5"><spring:message code="message.search.notmatch"/></td>
                </tr>
                </c:if>
            </tbody>
        </table>

        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
        </div>
    </div>
    <!--
        TAB - 3 : 컨텐츠 할당(할당 가능한 컨텐츠 목록), DETECT
    -->
    <div role="tabpane3" class="tab-pane <c:if test="${param.tab=='3'}">active</c:if>" id="beacon-info-content-list">
        <div id="content-list-ajax">
            <!--
                Ajax 처리 : /map/contents.ajax.do?refNum=[p1]&refType=BC
            -->
            loading ...
        </div>
        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
        </div>
    </div>
    <!--
        TAB - 4 : 할당된 이벤트 목록, EVENT
        할당된 이벤트 목록은 Tab2 의 컨텐츠목록에서 함께 표시
    
    <div role="tabpane4" class="tab-pane <c:if test="${param.tab=='4'}">active</c:if>" id="beacon-info-beaconevent">

        event list #1

        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn">닫기</button>
        </div>
    </div>
    -->
    <!--
        TAB - 5 : 할당 가능한 이벤트 목록, EVENT    
    <div role="tabpane5" class="tab-pane <c:if test="${param.tab=='5'}">active</c:if>" id="beacon-info-event-list">
        <div id="event-list-ajax">
                Ajax 처리 : /map/event.ajax.do 
                loading ...
        </div>
        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
        </div>
    </div>
    -->
    <!--
        TAB - 6 : 비콘 설치 이미지
    -->
    <div role="tabpane5" class="tab-pane <c:if test="${param.tab=='6'}">active</c:if>" id="beacon-image">
    	<form name="map-popup-upload-form" id="map-popup-upload-form" class="form-horizontal" role="form" enctype="multipart/form-data" style="margin-top:10px;">
	    	<input type="hidden" id="beaconNum" name="beaconNum" value="${beacon.beaconNum}" />
		    <div class="form-group" id="form-img1">
				<div class="col-sm-12">				
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20" style="width:450px;margin: auto;">
							<img src="${beacon.imgUrl}" id="img-thumbnail1" class="img-thumbnail" alt="<spring:message code="word.image" />"  width="450" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc" name="imgSrc" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" id="bcImgDelBtn" class="btn btn-warning btn-block"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>
	
<%-- 	        <img src="http://54.65.251.37/beacon/${beacon.minorVer}.jpg" width="360" onerror="$('#beacon-install-img-not-found').show();$(this).hide();"/>
	        <div id="beacon-install-img-not-found" style="display:none;"><spring:message code="word.none"/></div> --%>
		</form>
        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
        	<button id="bcImgUploadBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
		</div>
    </div>
</div>

<script type="text/javascript">
    /**
     * 할당된 컨텐츠 목록의 체크박스 클릭시, 할당해제 버튼 [활성,비활성] 처리
     */
    $('input[type=checkbox].chk-con-num-un').click(function() {
        console.log($('input[type=checkbox].chk-con-num-un:checked').length);
        if($('input[type=checkbox].chk-con-num-un:checked').length>0) {
            $('#btn-beacon-unassign-content').prop('disabled', false);
        } else {
            $('#btn-beacon-unassign-content').prop('disabled', true);
        }
    });

    /**
     * 할당해제 버튼 클릭시, 비콘에 컨텐츠 할당해제 처리
     */
    $('#btn-beacon-unassign-content').click(function() {
        var array_chk_list = [];
        $('input[type=checkbox].chk-con-num-un:checked').each(function(index) {
            var $checkbox = $(this);
            array_chk_list[array_chk_list.length] = $checkbox.val();
        });
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '<c:url value="/map/unassignBeaconContent.do"/>',
            data: $.param({beaconNum:${param.beaconNum}, conNums:array_chk_list}, true)
        }).done(function(json) {
            console.log(json);
            elementHandler.mapPopup.prevdata.tab = 2;
            elementHandler.mapPopup.reload();
        });
    });
    
    /**
     * nohsoo 2015-04-22 이벤트선택상자 선택변경시 변경된 이벤트 할당
     */
    $('.slt-assign-event').on('change', function() {
        var $select = $(this);
        var conNum = $select.attr('data-con-num');
        var refSubType = $select.attr('data-sub-type');
        var evtNum = $select.val();
        if($.trim(evtNum)=='') {
            var data = {beaconNum:${param.beaconNum}, conNum:conNum, refSubType: refSubType};
        } else {
            var data = {beaconNum:${param.beaconNum}, conNum:conNum, refSubType: refSubType, evtNum:evtNum};
        }
        console.log(data);
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '<c:url value="/map/setBeaconEvent.do"/>',
            data: $.param(data, true)
        }).done(function(json) {
            console.log(json);
        });
    });

    /**
     * 할당 가능한 컨텐츠 목록 갱신
     * @param param 요청파라미터 [refType,refNum]
     */
    function loadContents(param) {
        $.ajax({
            type: "get",
            dataType: 'html',
            url: '<c:url value="/map/contents.ajax.do"/>',
            data: param,
            cache: false
        }).done(function(html) {
            /**
             * 할당 가능한 컨텐츠 목록 검색 처리
             */
            $('#content-list-ajax').html(html);
            $('#btn-content-search').click(function() {
                var column = $('#content-list-s-column').val();
                var keyword = $('#content-list-s-keyword').val();
                loadContents({refNum:${param.beaconNum}, refType:'BC', opt:column, keyword:keyword});
            });

            /**
             * 할당 가능한 컨텐츠 목록의 체크박스,레디오 클릭시, 할당버튼 [활성,비활성] 처리
             */
            $('input[type=checkbox].chk-con-num').click(function() {
                console.log($('input[type=checkbox].chk-con-num:checked').length);
                if($('input[type=checkbox].chk-con-num:checked').length>0) {
                    $('#btn-beacon-assign-content').prop('disabled', false);
                } else {
                    $('#btn-beacon-assign-content').prop('disabled', true);
                }
            });

            /**
             * 비콘에 컨텐츠 할당 처리
             */
            $('#btn-beacon-assign-content').click(function() {
                var array_chk_list = [];
                $('input[type=checkbox].chk-con-num:checked').each(function(index) {
                    var $checkbox = $(this);
                    array_chk_list[array_chk_list.length] = $checkbox.val();
                });
                $.ajax({
                    type: 'post',
                    dataType: 'json',
                    url: '<c:url value="/map/assignBeaconContent.do"/>',
                    data: $.param({beaconNum:${param.beaconNum}, conNums:array_chk_list}, true)
                }).done(function(json) {
                    console.log(json);
                    elementHandler.mapPopup.prevdata.tab = 2;
                    elementHandler.mapPopup.reload();
                });
            });
        });
    }
    loadContents({refNum:${param.beaconNum}, refType:'BC'});


    function loadEventList(param) {
        $.ajax({
            type: "get",
            dataType: 'html',
            url: '<c:url value="/map/event.ajax.do"/>',
            data: param,
            cache: false
        }).done(function(html) {
            /**
             * 할당 가능한 이벤트 목록 검색 처리
             */
            $('#event-list-ajax').html(html);


            $('input[type=radio].chk-evt-num').click(function() {

                if($('input[type=radio].chk-evt-num:checked').length>0) {
                    $('#btn-beacon-assign-event').prop('disabled', false);
                } else {
                    $('#btn-beacon-assign-event').prop('disabled', true);
                }
            });

            $('#btn-beacon-assign-event').click(function() {
                var array_chk_list = [];
                $('input[type=radio].chk-evt-num:checked').each(function(index) {
                    var $checkbox = $(this);
                    array_chk_list[array_chk_list.length] = $checkbox.val();
                });
                $.ajax({
                    type: 'post',
                    dataType: 'json',
                    url: '<c:url value="/map/assignBeaconEvent.do"/>',
                    data: $.param({beaconNum:${param.beaconNum}, evtNums:array_chk_list}, true)
                }).done(function(json) {
                    console.log(json);
                    elementHandler.mapPopup.prevdata.tab = 2;
                    elementHandler.mapPopup.reload();
                });
            });

        });
    }
    loadEventList();
</script>

