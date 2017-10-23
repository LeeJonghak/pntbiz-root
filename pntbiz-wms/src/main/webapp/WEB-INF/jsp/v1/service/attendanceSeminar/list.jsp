<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/service.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript">
    var elementHandler = new ElementHandler('<c:url value="/"/>');
    $(document).ready( function() {
        elementHandler.init();
    });
</script>
<script type="text/javascript" src="${viewProperty.staticUrl}/js/service/attendanceSeminar.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>

<div id="manager-seminar-popup" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><spring:message code="word.seminar.manager" /></h4>
            </div>
            <div class="modal-body">
                <p>
                    <form name="form2" id="form2" class="form-inline" role="form">
                        <legend><spring:message code="word.seminar.register" /></legend>
                        <div class="form-group row">
                            <div class="col-sm-3">
                                <input type="text" id="seminarName" name="seminarName" value="" required="required" placeholder="<spring:message code="word.seminar.name" />" class="form-control" />
                            </div>
                            <div class="col-sm-2">
                                <input type="text" id="majorVer" name="majorVer" value="" required="required" min="0" max="65535" placeholder="<spring:message code="word.major.version" />" class="form-control" />
                            </div>
                            <div class="col-sm-2">
                                <input type="text" id="minorVer" name="minorVer" value="" required="required" min="0" max="65535" placeholder="<spring:message code="word.minor.version" />" class="form-control" />
                            </div>
                            <div class="col-sm-3">
                                <input type="text" id="macAddr" name="macAddr" value="" required="required" maxlength="17" placeholder="<spring:message code="word.mac.address" />" class="form-control" />
                            </div>
                            <div class="col-sm-2">
                                <input type="button" id="btnForm2Submit" value="<spring:message code="btn.register"/>" class="btn btn-primary form-control" />
                            </div>
                        </div>

                        <br /><br />

                        <legend><spring:message code="word.seminar.list"/></legend>
                        <table class="table table-bordered table-responsive" >
                            <thead>
                            <tr class="active">
                                <th><spring:message code="word.seminar.name" /></th>
                                <th><spring:message code="word.major.version" /></th>
                                <th><spring:message code="word.minor.version" /></th>
                                <th><spring:message code="word.mac.address" /></th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="seminar" items="${seminarList}">
                                <tr>
                                    <td>${seminar.subject}</td>
                                    <td>${seminar.majorVer}</td>
                                    <td>${seminar.minorVer}</td>
                                    <td>${seminar.macAddr}</td>
                                    <td>
                                        <input type="button" value="<spring:message code="btn.delete" />" data-major-ver="${seminar.majorVer}" data-minor-ver="${seminar.minorVer}" data-mac-addr="${seminar.macAddr}" class="btn btn-danger btn-del-seminar" />
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </form>

                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="btn.close" /></button>
            </div>
        </div>

    </div>
</div>

<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.140400" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.140000" /></li>
		<li class="crumb-trail"><spring:message code="menu.140400" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-9 col-md-9">				
					<div class="input-group date" id="datetimepicker1">
						<input type="text" id="attdDate" name="attdDate" class="form-control" placeholder="<spring:message code="word.attendance.date" />" value="${attdDate}" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<select name="opt" id="opt" class="form-control">
						<option value="">=<spring:message code="word.search.option" />=</option>
						<option value="phoneNumber" ${param.opt == 'phoneNumber' ? 'selected' : ''}><spring:message code="word.phonenumber" /></option>
						<option value="subject" ${param.opt == 'subject' ? 'selected' : ''}><spring:message code="word.seminar.name" /></option>
					</select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="attendanceKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="attendanceSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
					<button id="btnExport" type="button" class="btn btn-default btn-sm"><spring:message code="word.export" />(CSV)</button>
					
				</div>
				<div class="col-xs-3 col-md-3 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
		            <button id="btnReset" type="button" class="btn btn-danger btn-sm"><spring:message code="btn.init" /></button>
		            <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#manager-seminar-popup"><spring:message code="word.seminar.manager" /></button>
				</div>
			</div>
		</div>
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
				<tr class="active">
					<th><spring:message code="word.no" /></th>
					<th><spring:message code="word.seminar.name" /></th>
			        <th><spring:message code="word.major.version" /></th>
			        <th><spring:message code="word.minor.version" /></th>
					<th><spring:message code="word.phonenumber" /></th>
			        <th><spring:message code="word.device.infomation" /></th>
					<th><spring:message code="word.enter.leave" /></th>
					<th><spring:message code="word.date" /></th>
					<th><spring:message code="word.time" /></th>
			        <th></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="list" items="${list}" >
			    <tr align="center">
					<td>${list.logNum}</td>
					<td>${list.subject}</td>
			        <td>${list.majorVer}</td>
			        <td>${list.minorVer}</td>
					<td>${list.phoneNumber}</td>
			        <td>${list.deviceInfo}</td>
					<td style="vertical-align: middle;">
						<c:if test="${list.state eq 'E'}">
							<span class="label label-primary"><spring:message code="word.enter" /></span>
						</c:if>
						<c:if test="${list.state eq 'L'}">
							<span class="label label-success"><spring:message code="word.leave" /></span>
						</c:if>
					</td>
					<td>${list.attdDate}</td>
					<td>
						<jsp:useBean id="regDate" class="java.util.Date"/>
						<jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
						<fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
					</td>
			        <td>
			            <input id="btnDelete" data-log-num="${list.logNum}" data-attd-date="${list.attdDate}" type="button" value="<spring:message code="btn.delete" />" class="btn btn-sm btn-danger btnDelete" />
			        </td>
				</tr>
				</c:forEach>
				<c:choose>
					<c:when test="${cnt == 0}">
						<tr>
							<td colspan="10" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>
		
		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				${page}
			</ul>
		</div>
		
	</div>
</div>
</section>
</form>
</body>
</html>