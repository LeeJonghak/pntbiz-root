<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<h5><spring:message code="word.scanner.information"/></h5>
<hr style="margin-top:10px;margin-bottom:10px;"/>

<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" <c:if test="${param.tab eq null or param.tab=='1'}">class="active"</c:if>><a href="#info-basic" aria-controls="basic" role="tab" data-toggle="tab"><spring:message code="word.basic.information"/></a></li>
</ul>

<div class="tab-content" style="margin-top:4px;">
    <!--
        TAB - 1 : 기본정보
    -->
    <div role="tabpanel" class="tab-pane <c:if test="${param.tab eq null or param.tab=='1'}">active</c:if>" id="info-basic">
        <form name="map-popup-form" id="map-popup-form" class="form-horizontal" role="form" style="margin-top:10px;">
            <input type="hidden" name="scannerNum" value="${scannerInfo.scannerNum}" />

            <div id="success-message" class="alert alert-success hide"></div>
            <div id="error-message" class="alert alert-danger hide"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.scanner.name"/></label>
                <div class="col-sm-9">
                    <input type="text" id="scannerName" name="scannerName" value="${scannerInfo.scannerName}" size="50" maxlength="50" class="form-control input-sm" placeholder="<spring:message code="word.scanner.name"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.mac.address"/></label>
                <div class="col-sm-9">
                    <input type="text" id="macAddr" name="macAddr" value="${scannerInfo.macAddr}" size="35" maxlength="35" class="form-control input-sm" placeholder="<spring:message code="word.mac.address"/>"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.identifier"/>(sid)</label>
                <div class="col-sm-9">
                    <input type="text" id="sid" name="sid" value="${scannerInfo.sid}" size="4" maxlength="4" class="form-control input-sm" placeholder="<spring:message code="word.identifier"/>(sid)"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">RSSI</label>
                <div class="col-sm-9">
                    <input type="text" id="rssi" name="rssi" value="${scannerInfo.rssi}" size="6" maxlength="6" class="form-control input-sm" placeholder="RSSI"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Sole RSSI</label>
                <div class="col-sm-9">
                    <input type="text" id="srssi" name="srssi" value="${scannerInfo.srssi}" size="6" maxlength="6" class="form-control input-sm" placeholder="Sole RSSI"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Min RSSI</label>
                <div class="col-sm-9">
                    <input type="text" id="mrssi" name="mrssi" value="${scannerInfo.mrssi}" size="6" maxlength="6" class="form-control input-sm" placeholder="Min RSSI"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Max Diff RSSI</label>
                <div class="col-sm-9">
                    <input type="text" id="drssi" name="drssi" value="${scannerInfo.drssi}" size="6" maxlength="6" class="form-control input-sm" placeholder="Max Diff RSSI"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Exclusive Meter</label>
                <div class="col-sm-9">
                    <input type="text" id="exMeter" name="exMeter" value="${scannerInfo.exMeter}" size="7" maxlength="7" class="form-control input-sm" placeholder="Exclusive Meter"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Cal Point Number</label>
                <div class="col-sm-9">
                    <input type="text" id="calPoint" name="calPoint" value="${scannerInfo.calPoint}" size="4" maxlength="4" class="form-control input-sm" placeholder="Cal Point Number"  />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><spring:message code="word.lat.lng"/></label>
                <div class="col-sm-9" style="padding-top:7px;">
                    <span>${scannerInfo.lat} / ${scannerInfo.lng}</span>
                </div>
            </div>
            <hr style="margin-top:10px;margin-bottom:10px;"/>
            <div class="center">
                <button id="scannerModBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.modify"/></button>
                <button type="button" class="btn btn-default btn-sm popup-close-btn"><spring:message code="btn.close"/></button>
            </div>
        </form>
    </div>
</div>


<script type="text/javascript">


</script>

