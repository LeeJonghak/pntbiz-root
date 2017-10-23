<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/service.css?v=<spring:eval expression='@config[buildVersion]'/>" />
	<script type="text/javascript" src="${viewProperty.staticUrl}/js/service/service_event.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.popupoverlay/jquery.popupoverlay.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script>
		$(function(){
			$(".transformBtn").click(function(){
				var _eventId = $(this).data('eventid');
				window.location.href='<c:url value="/service/seat/mform.do"/>?eventId=' + _eventId;
			});
		});
	</script>
</head>
<body>
	<form name="form1" id="form1" class="form-inline" role="form">
	<div class="col-sm-6">
		<h3>좌석업그레이드 관리 <small></small></h3>
	</div>
	<div class="col-sm-6">
		<span class="pull-right">
			<ol class="breadcrumb">
			  <li><a href="#">서비스</a></li>
			  <li >좌석업그레이드 관리</li>
			  <li class="active">좌석업그레이드 목록</li>
			</ol>
		</span>
	</div>
	<hr />
	<div class="col-sm-9">
		<div class="form-group">
			<select name="opt" id="opt" class="form-control">
				<option value="">=전체(검색조건)=</option>			
				<option value="01">CMS컨텐츠명</option>
				<option value="02">이벤트명</option>
			</select>
		</div>
		<div class="form-group">
			<input type="text" id="beaconKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
		</div>
		<div class="form-group">
			<button id="beaconSearchBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
		</div>	
	</div>
	<div class="col-sm-3 text-right">
		<button id="beaconFormBtn" class="btn btn-default btn-sm" type="button" onClick="common.redirect('/service/seat/form.do');">등록</button>
	</div>   
	<div class="clearfix"></div>
	<div class="bh20"></div>
	<div class="col-sm-9">
		<div id="error-opt" class="pull-left"></div>
		<div id="error-beaconKeyword" class="pull-left"></div>
	</div>
	<div class="col-sm-3 text-right">
		<p class="text-info">총 카운트 : ${cnt}건</p>
	</div>
	<div class="clearfix"></div>
	<div class="bh10">
	</div>
	<div class="table-responsive">
	<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">	
		<thead>
			<tr class="active">
				<th>No.</th>
				<th>이벤트ID</th>	
				<th>CMS(콘텐츠명)</th>
				<th>콘텐츠유형</th>
				<th>이벤트명</th>
				<th>메모</th>
				<th>등록자</th>
				<th>등록일</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${cnt == 0}">
					<tr>
						<td colspan="10" height="150" align="center">검색 결과가 없습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${list}" varStatus="status">
						<tr align="center">
							<td>${status.index + 1}</td>
							<td><a href="javascript:beacon.mform()">${item.eventId }</a></td>
							<td>${item.contsNm }</td>
							<td>${item.contsTypeNm}</td>
							<td>${item.eventNm }</td>
							<td>${item.eventDesc }</td>
							<td>${item.createdNo }</td>
							<td>${item.createdAt }</td>
							<td><button type="button" class="btn btn-default transformBtn" data-eventid="${item.eventId }" >이관하기</button></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	</div>
	<div class="center">
		<ul class="pagination">
		    ${page}
		</ul> 
	</div>
</form>
</body>
</html>