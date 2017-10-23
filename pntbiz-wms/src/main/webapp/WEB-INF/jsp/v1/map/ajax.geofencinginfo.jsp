<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<h5><spring:message code="word.geofencing" /> <spring:message code="word.info" /></h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" <c:if test="${param.tab eq null or param.tab=='1'}">class="active"</c:if>><a href="#info-basic" aria-controls="info-basic" role="tab" data-toggle="tab"><spring:message code="word.basic.information"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='2'}">class="active"</c:if>><a href="#geofencing-info-content" aria-controls="geofencing-info-content" role="tab" data-toggle="tab"><spring:message code="word.contents"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='3'}">class="active"</c:if>><a href="#geofencing-info-content-list" aria-controls="geofencing-info-content-list" role="tab" data-toggle="tab"><spring:message code="word.assign.content"/></a></li>
    <%--<li role="presentation" <c:if test="${param.tab=='4'}">class="active"</c:if>><a href="#beacon-info-task" aria-controls="beacon-info-task" role="tab" data-toggle="tab">작업</a></li>--%>
</ul>
<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보
    -->
    <div role="tabpanel" class="tab-pane <c:if test="${param.tab eq null or param.tab=='1'}">active</c:if>" id="info-basic">
        <form name="map-popup-form" id="map-popup-form" class="form-horizontal" role="form" style="margin-top:10px;">
            <input type="hidden" id="fcNum" name="fcNum" value="${geofencing.fcNum}" />
            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>


            <div class="form-group">
                <div class="row" style="margin:10px;">
                    <label class="col-sm-3 control-label"><spring:message code="word.geofencing.name" /></label>
                    <div class="col-sm-9">
                        <input type="text" id="fcName" name="fcName" value="${geofencing.fcName}" size="50" required="required" maxlength="100" class="form-control input-sm" placeholder="펜스명"  />
                    </div>
                </div>
                <div class="row" style="margin:10px;">
                    <label class="col-sm-3 control-label"><spring:message code="word.geofencing.group.name" /></label>
                    <div class="col-sm-9">
                        <select class="form-control input-sm" name="fcGroupNum">
                            <option value="">[<spring:message code="word.geofencing.group.name" />]</option>
                            <c:forEach items="${fcGroupList}" var="fcGroupList">
							<option value="${fcGroupList.fcGroupNum}" <c:if test="${fcGroupList.fcGroupNum eq geofencing.fcGroupNum}">selected</c:if>>${fcGroupList.fcGroupName}</option>
						</c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row" style="margin:10px;">
                    <label class="col-sm-3 control-label"><spring:message code="word.geofencing.field" /></label>
                    <div class="col-sm-9">
                    	<input type="text" id="field1" name="field1" value="${geofencing.field1}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                    	<input type="text" id="field2" name="field2" value="${geofencing.field2}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                    	<input type="text" id="field3" name="field3" value="${geofencing.field3}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                    	<input type="text" id="field4" name="field4" value="${geofencing.field4}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                    	<input type="text" id="field5" name="field5" value="${geofencing.field5}" size="3" minlength="1" maxlength="25" class="form-control input-sm" placeholder="" />
                    </div>
                </div>
                <div class="row" style="margin:10px;padding-top:10px;">
                    <%--<label class="col-sm-3 control-label">보정치</label>--%>
                    <div class="col-sm-12">
                        <table class="table table-striped table-hover">
                            <thead>
                            <tr>
                                <th></th>
                                <th style="min-width:120px;"><spring:message code="word.correction" /></th>
                                <th style="min-width:120px;"><spring:message code="word.maximum" /></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>ENTER</td>
                                <td>
                                    <input type="number" name="evtEnter" value="${geofencing.evtEnter}" min="0" max="9999" maxlength="4" class="form-control input-sm" />
                                </td>
                                <td>

                                    <div class="input-group">
                                        <span class="input-group-addon" style="background-color:#ffffff; padding:0px; margin:0px; border:0px;">
                                            <div class="checkbox input-sm">
                                                <label>
                                                    <input type="checkbox" id="chk-Unlimited-enter" <c:if test="${geofencing.numEnter eq -1}">checked</c:if>> <spring:message code="word.unlimited" />
                                                </label>
                                            </div>
                                        </span>
                                        <input type="number" name="numEnter" id="input-Unlimited-enter" value="${geofencing.numEnter}" <c:if test="${geofencing.numEnter eq -1}">readonly</c:if> min="-0" max="9999" maxlength="4" class="form-control input-sm" />

                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>LEAVE</td>
                                <td>
                                    <input type="number" name="evtLeave" value="${geofencing.evtLeave}" min="0" max="9999" maxlength="4" class="form-control input-sm" />
                                </td>
                                <td>
                                    <div class="input-group">
                                        <span class="input-group-addon" style="background-color:#ffffff; padding:0px; margin:0px; border:0px;">
                                            <div class="checkbox input-sm">
                                                <label>
                                                    <input type="checkbox" id="chk-Unlimited-leave" <c:if test="${geofencing.numLeave eq -1}">checked</c:if>> <spring:message code="word.unlimited" />
                                                </label>
                                            </div>
                                        </span>
                                        <input type="number" name="numLeave" id="input-Unlimited-leave" value="${geofencing.numLeave}" <c:if test="${geofencing.numLeave eq -1}">readonly</c:if> min="-0" max="9999" maxlength="4" class="form-control input-sm" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>STAY</td>
                                <td>
                                    <input type="number" name="evtStay" value="${geofencing.evtStay}" min="0" max="9999" maxlength="4" class="form-control input-sm" />
                                </td>
                                <td>
                                    <div class="input-group">
                                        <span class="input-group-addon" style="background-color:#ffffff; padding:0px; margin:0px; border:0px;">
                                            <div class="checkbox input-sm">
                                                <label>
                                                    <input type="checkbox" id="chk-Unlimited-stay" <c:if test="${geofencing.numStay eq -1}">checked</c:if>> <spring:message code="word.unlimited" />
                                                </label>
                                            </div>
                                        </span>
                                        <input type="number" name="numStay" id="input-Unlimited-stay" value="${geofencing.numStay}" <c:if test="${geofencing.numStay eq -1}">readonly</c:if> min="-0" max="9999" maxlength="4" class="form-control input-sm" />
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>

            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <a id="linkBtn" href="<c:url value="/geofencing/info/mform.do?fcNum=${geofencing.fcNum}"/>" target="_blank" class="btn btn-default btn-sm"><spring:message code="btn.modify" /> ...</a>
                <button id="modBtn" type="button" class="btn btn-primary btn-sm">저장</button>
                <%--<button id="delBtn" type="button" class="btn btn-danger btn-sm">삭제</button>--%>
                <button type="button" class="btn btn-default btn-sm popup-close-btn">닫기</button>
            </div>
        </form>
    </div>
    <!--
        TAB - 2 : 할당된 컨텐츠 목록
    -->
    <div role="tabpane2" class="tab-pane <c:if test="${param.tab=='2'}">active</c:if>" id="geofencing-info-content">
        <div style="margin-top:10px; margin-bottom:10px;">
            <div id="popup-content-success-message" class="alert alert-success hide"></div>
            <div id="popup-content-error-message" class="alert alert-danger hide"></div>

            <div class="row">
                <div class="col-sm-3">
                    <button type="button" id="btn-geofencing-unassign-content" class="btn btn-danger btn-sm" disabled><spring:message code="btn.delete" /></button>
                </div>
            </div>
        </div>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th width="30" class="">
                    <input type="checkbox" style="margin-top:0px;"/>
                </th>
                <th><spring:message code="word.fence.type" /></th>
                <th><spring:message code="word.contents.name" /></th>
                <th><spring:message code="word.task" /></th>
                <th><spring:message code="word.event" /></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contents}" var="item">
                <tr>
                    <td style="display:table-cell; vertical-align: middle;">
                        <input type="checkbox" class="chk-con-num-un" value="${item.conNum}" data-con-num="${item.conNum}" data-sub-type="${item.refSubType}" />
                    </td>
                    <td style="display:table-cell; vertical-align: middle;"><span class="label label-primary">${item.refSubType}</span></td>
                    <td style="display:table-cell; vertical-align: middle;">${item.conName}</td>
                    <td>
                        <select class="input-sm slt-assign-task" data-con-num="${item.conNum}" data-sub-type="${item.refSubType}">
                            <option value="">[<spring:message code="word.none" />]</option>
                            <c:forEach items="${codeActionList}" var="code">
                                <option value="${code.codeNum}" <c:if test="${myCodeActionMap[item.refSubType].codeNum eq code.codeNum}">selected</c:if>>${code.codeName}</option>
                            </c:forEach>
                        </select>
                    </td>
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
                    <td class="center" colspan="4"><spring:message code="message.search.notmatch" /></td>
                </tr>
            </c:if>
            </tbody>
        </table>

        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close" /></button>
        </div>
    </div>
    <!--
        TAB - 3 : 컨텐츠 할당(할당 가능한 컨텐츠 목록)
    -->
    <div role="tabpane3" class="tab-pane <c:if test="${param.tab=='3'}">active</c:if>" id="geofencing-info-content-list">
        <div id="content-list-ajax">
            <!--
                Ajax 처리 : /map/contents.ajax.do?refNum=[p1]&refType=GF
            -->
            loading ...
        </div>
        <hr style="margin-top:10px;margin-bottom:10px;"/>
        <div class="center">
            <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close" /></button>
        </div>
    </div>
</div>

<script type="text/javascript">

    /**
     * 펜스이벤트별[ENTER,STAY,LEAVE] 컨텐츠 할당 상태
     */
    var assignedEventState = {
        enter: ${assignedEvent.enter},
        stay: ${assignedEvent.stay},
        leave: ${assignedEvent.leave}
    };

    /**
     * 할당된 컨텐츠 목록의 체크박스 클릭시, 할당해제 버튼 [활성,비활성] 처리
     */
    $('input[type=checkbox].chk-con-num-un').click(function() {
        console.log($('input[type=checkbox].chk-con-num-un:checked').length);
        if($('input[type=checkbox].chk-con-num-un:checked').length>0) {
            $('#btn-geofencing-unassign-content').prop('disabled', false);
        } else {
            $('#btn-geofencing-unassign-content').prop('disabled', true);
        }
    });

    /**
     * 할당해제 버튼 클릭시, 지오펜스에 컨텐츠 할당해제 처리
     */
    $('#btn-geofencing-unassign-content').click(function() {

        if(window.confirm('정말로 할당 해제 하시겠습니까?')) {

            var array_chk_list = [];

            $('input[type=checkbox].chk-con-num-un:checked').each(function(index) {
                var $checkbox = $(this);
                var conNum = $checkbox.attr('data-con-num');
                var refSubType = $checkbox.attr('data-sub-type');
                array_chk_list[array_chk_list.length] = conNum+'::'+refSubType;
            });
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: '<c:url value="/map/unassignGeofencingContent.do"/>',
                data: $.param({fcNum:${param.fcNum}, conNums:array_chk_list}, true)
            }).done(function(json) {
                console.log(json);
                elementHandler.mapPopup.prevdata.tab = 2;
                elementHandler.mapPopup.reload(function(){});
            });
        }
    });

    /**
     * 함수: 선택된 컨텐츠를 할당 처리
     * @author nohsoo 2015-03-12
     * @param fEventType 펜스이벤트[ENTER,STAY,LEAVE]
     */
    function ajaxGeofencingAssignContent(fEventType) {
        var array_chk_list = [];
        $('input[type=radio].chk-con-num:checked').each(function(index) {
            var $radios = $(this);
            array_chk_list[array_chk_list.length] = $radios.val();
        });

        var data = {fcNum:${param.fcNum}, eventType: fEventType, conNums:array_chk_list};
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '<c:url value="/map/assignGeofencingContent.do"/>',
            data: $.param(data, true)
        }).done(function(json) {
            console.log(json);
            elementHandler.mapPopup.prevdata.tab = 2;
            elementHandler.mapPopup.reload(function(){});
        });
    }

    $('.slt-assign-task').on('change', function() {
        var $select = $(this);
        var refSubType = $select.attr('data-sub-type');
        var codeNum = $select.val();
        if($.trim(codeNum)=='') {
            var data = {fcNum:${param.fcNum}, eventType: refSubType};
        } else {
            var data = {fcNum:${param.fcNum}, eventType: refSubType, codeNum:codeNum};
        }
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '<c:url value="/map/setGeofencingCodeAction.do"/>',
            data: $.param(data, true)
        }).done(function(json) {
            console.log(json);
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
            var data = {fcNum:${param.fcNum}, conNum:conNum, refSubType: refSubType};
        } else {
            var data = {fcNum:${param.fcNum}, conNum:conNum, refSubType: refSubType, evtNum:evtNum};
        }
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '<c:url value="/map/setGeofencingEvent.do"/>',
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
            $('#content-list-ajax').html(html);

            /**
             * 할당 가능한 컨텐츠 목록 검색 처리
             */
            $('#btn-content-search').click(function() {
                var column = $('#content-list-s-column').val();
                var keyword = $('#content-list-s-keyword').val();
                loadContents({refNum:${param.fcNum}, refType:'GF', opt:column, keyword:keyword});
            });

            /**
             * 펜스이벤트별 컨텐츠 할당 상태에 따라서 할당 버튼 활성
             */
            if(assignedEventState.enter!=true) {
                $('#btn-geofencing-assign-content-enter').parent().prop('disabled', false);
                $('#btn-geofencing-assign-content-enter').parent().removeClass('disabled');
            }
            if(assignedEventState.stay!=true) {
                $('#btn-geofencing-assign-content-stay').parent().prop('disabled', false);
                $('#btn-geofencing-assign-content-stay').parent().removeClass('disabled');
            }
            if(assignedEventState.leave!=true) {
                $('#btn-geofencing-assign-content-leave').parent().prop('disabled', false);
                $('#btn-geofencing-assign-content-leave').parent().removeClass('disabled');
            }

            /**
             * 할당 가능한 컨텐츠 목록의 체크박스,레디오 클릭시, 할당버튼 [활성,비활성] 처리
             */
            $('input[type=radio].chk-con-num').click(function() {
                console.log($('input[type=radio].chk-con-num:checked').length);
                if($('input[type=radio].chk-con-num:checked').length>0) {
                    $('#btn-geofencing-assign-content').prop('disabled', false);
                    $('#btn-geofencing-assign-content').removeClass('disabled');
                } else {
                    $('#btn-geofencing-assign-content').prop('disabled', true);
                    $('#btn-geofencing-assign-content').addClass('disabled');
                }
            });

            /**
             * 지오펜스에 컨텐츠 할당 버튼 클릭 이벤트
             */
            $('#btn-geofencing-assign-content-enter').click(function() {
                if(assignedEventState.enter!=true) {
                    ajaxGeofencingAssignContent('ENTER');
                }
            });
            $('#btn-geofencing-assign-content-stay').click(function() {
                if(assignedEventState.stay!=true) {
                    ajaxGeofencingAssignContent('STAY');
                }
            });
            $('#btn-geofencing-assign-content-leave').click(function() {
                if(assignedEventState.leave!=true) {
                    ajaxGeofencingAssignContent('LEAVE');
                }
            });
        });
    }
    loadContents({refNum:${param.fcNum}, refType:'GF'});

    /**
     * 최대 횟수 체크박스
     */
    $('#chk-Unlimited-enter').click(function() {
        var checked = $(this).prop('checked');
        if(checked==true) {
            this.dataVal = $('#input-Unlimited-enter').val();
            if(!this.dataVal) this.dataVal = '0';
            $('#input-Unlimited-enter').val('-1');
            $('#input-Unlimited-enter').prop('readonly', true);
        } else {
            $('#input-Unlimited-enter').val(typeof this.dataVal=='undefined'?'0':this.dataVal);
            $('#input-Unlimited-enter').prop('readonly', false);
        }
    });
    $('#chk-Unlimited-leave').click(function() {
        var checked = $(this).prop('checked');
        if(checked==true) {
            this.dataVal = $('#input-Unlimited-leave').val();
            if(!this.dataVal) this.dataVal = '0';
            $('#input-Unlimited-leave').val('-1');
            $('#input-Unlimited-leave').prop('readonly', true);
        } else {
            $('#input-Unlimited-leave').val(typeof this.dataVal=='undefined'?'0':this.dataVal);
            $('#input-Unlimited-leave').prop('readonly', false);
        }
    });
    $('#chk-Unlimited-stay').click(function() {
        var checked = $(this).prop('checked');
        if(checked==true) {
            this.dataVal = $('#input-Unlimited-stay').val();
            if(!this.dataVal) this.dataVal = '0';
            $('#input-Unlimited-stay').val('-1');
            $('#input-Unlimited-stay').prop('readonly', true);
        } else {
            $('#input-Unlimited-stay').val(typeof this.dataVal=='undefined'?'0':this.dataVal);
            $('#input-Unlimited-stay').prop('readonly', false);
        }
    });

</script>

