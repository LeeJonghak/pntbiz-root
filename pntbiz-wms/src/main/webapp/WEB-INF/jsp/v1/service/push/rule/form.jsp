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
			$("#srchbtn").click(function(){
				//$('form[name=form1]').submit();
			});
		});
	</script>
</head>
<body>
	<form name="form1" id="form1" class="form-inline" role="form" method="get">
	<input type="hidden" name="pnsId" value="${param.pnsId}"/>
	<div class="col-sm-6">
		<h3>푸쉬발송결과관리 <small></small></h3>
	</div>
	<div class="col-sm-6">
		<span class="pull-right">
			<ol class="breadcrumb">
			  <li><a href="#">서비스</a></li>
			  <li class="active">푸쉬발송결과상세</li>
			</ol>
		</span>
	</div>
	<hr />
	<div class="table-responsive">
	<table class="table table-bordered table-hover table-responsive">	
		<thead>
			<tr class="active">
				<th colspan="2" style="text-align:left;">도달자/이벤트 참여자 결과</th>
			</tr>
		</thead>
		<tbody>
			<tr align="center">
				<th>PNS 제목</th>
				<td>${list[0].pnsTitle}</td>
			</tr>
			<tr align="center">
				<th>PNS 내용</th>
				<td>${list[0].pnsMsg}</td>
			</tr>
			<tr align="center">
				<th>발송건수</th>
				<td>${list[0].sendTotCnt}</td>
			</tr>
			<tr align="center">
				<th>발송성공</th>
				<td>${list[0].sendSuccCnt}</td>
			</tr>
			<tr align="center">
				<th>발송실패</th>
				<td>${list[0].sendSuccCnt}</td>
			</tr>
			<tr align="center">
				<th>이벤트 참여자</th>
				<td>${list[0].eventJoinCnt}</td>
			</tr>
			<tr align="center">
				<th>이벤트 미참여자</th>
				<td>${list[0].eventNonJoinCnt}</td>
			</tr>
			<tr align="center">
				<th>당첨자</th>
				<td>${list[0].winnerCnt}</td>
			</tr>
		</tbody>
	</table>
	</div>
	<hr />
	<div class="col-sm-9">
		<div class="form-group">
			<select name="srchType" id="srchType" class="form-control">
				<option value="">=전체(검색조건)=</option>			
				<option value="01">회원명</option>
				<option value="02">전화번호</option>
			</select>
		</div>
		<div class="form-group">
			<input type="text" id="srchWords" name="srchWords" class="form-control" placeholder="검색" value="${param.srchWords}" />
		</div>
		<div class="form-group">
			<button id="srchbtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
		</div>	
	</div>
	<div class="col-sm-3 text-right">
		<button id="eventListBtn" type="button" class="btn btn-warning btn-md" onClick="common.redirect('/service/push/rule/list.do')">리스트</button>
		&nbsp;
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
				<th colspan="3">단말기</th>
				<th>설치Ver.</th>
				<th>사이트ID</th>
				<th>회원명</th>
				<th>전화번호</th>
				<th>발송성공여부</th>
				<th>수신여부</th>
				<th>이벤트참여여부</th>
				<th>대상여부</th>
				<th>당첨여부</th>
				<th>사용여부</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${cnt == 0}">
					<tr>
						<td colspan="14" height="150" align="center">검색 결과가 없습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${list}" varStatus="status">
						<tr align="center">
							<td>${status.index + 1}</td>
							<td>${item.deviceId }</td>
							<td>
								<c:choose>
									<c:when test="${item.pnsPlatform eq 'A'}">
										안드로이드
									</c:when>
									<c:otherwise>
										아이폰
									</c:otherwise>
								</c:choose>
							</td>
							<td>${item.deviceModel}</td>
							<td>${item.appVer}</td>
							<td>${item.hanUserId}</td>
							<td>${item.name }</td>
							<td>${item.phoneNum }</td>
							<td>${item.sendStatusNm }</td>
							<td>${item.recvStatusNm }</td>
							<td>${item.sendDtti }</td>
							<td>
								<c:choose>
									<c:when test="${item.targetYn eq 'Y'}">
										대상
									</c:when>
									<c:otherwise>
										비대상
									</c:otherwise>							
								</c:choose>
							</td>
							<td>${item.winBenefitNm }</td><!-- 당첨여부 -->
							<td>
								<c:choose>
									<c:when test="${item.isUsed eq 'Y'}">
										사용<br/>${item.useDt}
									</c:when>
									<c:otherwise>
										미사용
									</c:otherwise>							
								</c:choose>
							</td><!-- 사용여부 -->
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