<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<h5><spring:message code="word.node.information" /></h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" <c:if test="${param.tab eq null or param.tab=='1'}">class="active"</c:if>><a href="#node-info-basic" aria-controls="basic" role="tab" data-toggle="tab"><spring:message code="word.basic.information"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='2'}">class="active"</c:if>><a href="#node-info-nodecontent" aria-controls="node-info-beaconcontent" role="tab" data-toggle="tab"><spring:message code="word.contents"/></a></li>
    <li role="presentation" <c:if test="${param.tab=='3'}">class="active"</c:if>><a href="#node-info-content-list" aria-controls="node-info-content-list" role="tab" data-toggle="tab"><spring:message code="word.assign.content"/></a></li>
</ul>

<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보
    -->
    <div role="tabpanel" class="tab-pane <c:if test="${param.tab eq null or param.tab=='1'}">active</c:if>" id="node-info-basic">
        <form name="map-popup-form-node" id="map-popup-form-node" class="form-horizontal" role="form" style="margin-top:10px;">
            <input type="hidden" id="nodeNum" name="nodeNum" value="${node.nodeNum}" />
            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.node.name" /></label>
                <div class="col-sm-9">
                    <input type="text" id="nodeName2" name="nodeName2" value="${node.nodeName}" size="50" maxlength="36" class="form-control input-sm" placeholder="<spring:message code="word.node.name" />"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.node.id"/></label>
                <div class="col-sm-9">
                    <input type="text" value="${node.nodeID}" size="50" readonly maxlength="36" class="form-control input-sm" placeholder="<spring:message code="word.node.id"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.lat.lng"/></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <span>${node.lat} / ${node.lng}</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.category" /></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <select class="input-sm" name="cate">
                        <option value="-">[<spring:message code="word.none"/>]</option>
                        <c:forEach items="${poiCategory}" var="item">
                            <option value="${item.key}" <c:if test="${node.cate eq item.key}">selected</c:if>>${item.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.intersection.name"/></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <input type="text" id="jointName" name="jointName" value="${node.jointName}" size="50" maxlength="36" class="form-control input-sm" placeholder="<spring:message code="word.intersection.name"/>"  />
                </div>
            </div>
            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <button id="nodeModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
                <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
            </div>
        </form>
    </div>
    <!--
        TAB - 2 : 할당된 컨텐츠 목록
    -->
    <div role="tabpane2" class="tab-pane <c:if test="${param.tab=='2'}">active</c:if>" id="node-info-nodecontent">
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
                <th><spring:message code="word.kind"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contents}" var="item">
                <tr>
                    <td><input type="checkbox" class="chk-con-num-un" value="${item.conNum}" /></td>
                    <td>${item.conNum}</td>
                    <td>${item.conName}</td>
                    <td><span class="label <c:if test="${item.refSubType eq 'DETECT'}">label-primary</c:if><c:if test="${item.refSubType eq 'EVENT'}">label-success</c:if>">${item.refSubType}</span>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${contents.size()==0}">
                <tr>
                    <td class="center" colspan="4"><spring:message code="message.search.notmatch"/></td>
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
    <div role="tabpane3" class="tab-pane <c:if test="${param.tab=='3'}">active</c:if>" id="node-info-content-list">
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
            url: '<c:url value="/map/unassignNodeContent.do"/>',
            data: $.param({nodeNum:${param.nodeNum}, conNums:array_chk_list}, true)
        }).done(function(json) {
            console.log(json);
            elementHandler.mapPopup.prevdata.tab = 2;
            elementHandler.mapPopup.reload();
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
                loadContents({refNum:${param.nodeNum}, refType:'ND', opt:column, keyword:keyword});
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
                console.log('## checked list');
                console.log(array_chk_list);
                $.ajax({
                    type: 'post',
                    dataType: 'json',
                    url: '<c:url value="/map/assignNodeContent.do"/>',
                    data: $.param({nodeNum:${param.nodeNum}, conNums:array_chk_list}, true)
                }).done(function(json) {
                    console.log(json);
                    elementHandler.mapPopup.prevdata.tab = 2;
                    elementHandler.mapPopup.reload();
                });
            });
        });
    }
    loadContents({refNum:${param.nodeNum}, refType:'ND'});

</script>

