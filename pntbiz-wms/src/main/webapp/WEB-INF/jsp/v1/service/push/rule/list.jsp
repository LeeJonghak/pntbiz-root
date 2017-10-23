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
			$(".pushSendBtn").click(function(){
				var _pnsId = $(this).data('pnsid');
				window.location.href='<c:url value="/service/push/rule/form.do"/>?pnsId=' + _pnsId;
				
// 				$('#trans_popup').popup('toggle')  
			});
			
			$('#trans_popup').popup({
				autoopen: false,
	            type: 'overlay',
	            blur : false,
	            transition: 'all 0.3s'
	      	});
			
		});
	</script>
</head>
<body>
	<form name="form1" id="form1" class="form-inline" role="form">
	<div class="col-sm-6">
		<h3>푸쉬발송결과관리 <small></small></h3>
	</div>
	<div class="col-sm-6">
		<span class="pull-right">
			<ol class="breadcrumb">
			  <li><a href="#">서비스</a></li>
			  <li class="active">푸쉬발송결과관리</li>
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
<!-- 		<button id="beaconFormBtn" class="btn btn-default btn-sm" type="button" onClick="common.redirect('/service/event/form.do');">이벤트등록</button> -->
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
				<th>발송유형</th>
				<th>구분</th>
				<th>CMS(콘텐츠명)</th>
				<th>이벤트명</th>
				<th>PNS제목</th>
				<th>PNS내용</th>
				<th>상태</th>
				<th>발송시간</th>
				<th>성별</th>
				<th>나이</th>
				<th>생일</th>
				<th>기념일</th>
				<th>방문횟수</th>
				<th>Zone</th>
				<th>Block</th>
				<th>좌석</th>
				<th>선착순</th>
				<th>당첨혜택</th>
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
							<td>
								<c:choose>
									<c:when test="${item.sendType eq 'BP'}">
										비콘
									</c:when>
									<c:otherwise>
										PUSH
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.sendType eq 'AP' && item.contsType != 'SUP' && item.contsType != 'SUS' && item.contsType != 'SUU' }">
										현장이벤트
									</c:when>
									<c:when test="${item.sendType eq 'AP' && ( item.contsType eq 'SUP' || item.contsType eq 'SUS' || item.contsType eq 'SUU' )  }">
										좌석업그레이드
									</c:when>
									<c:otherwise>
										이벤트
									</c:otherwise>
								</c:choose>
							</td>
							<td>${item.contsNm }</td>
							<td>${item.eventNm }</td>
							<td>${item.pnsTitle }</td>
							<td>${item.pnsMsg }</td>
							<td>${item.pnsSRStatusCdNm }</td>
							<td>${item.sendDtti }</td>
							<td>${item.genTxt }</td>
							<td>${item.ageTxt }</td>
							<td>${item.birthTxt }</td>
							<td>${item.anniTxt }</td>
							<td>${item.visitTxt }</td>
							<td>${item.zoneTxt }</td>
							<td>${item.blockTxt }</td>
							<td>${item.seatTxt }</td>
							<td>
								<c:choose>
									<c:when test="${item.orderCntUseYn eq 'Y'}">
										${item.orderCnt}명
									</c:when>
									<c:otherwise>
										사용안함
									</c:otherwise>
								</c:choose> 
							</td>
							<td>${item.benefit1Msg }</td>
							<td>${item.createdNo }</td>
							<td>${item.createdAt }</td>
							<td><button type="button" class="btn btn-default pushSendBtn" data-pnsid="${item.pnsId }" >상세보기</button></td>
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
	<!-- Add popup ui -->
  	<div id="trans_popup" class="well popup_content" style="width:800px;">
  		<div style="width:100%;">
 			<form name="trans_form" id="trans_form" class="form-horizontal" role="form">
			<div class="row">
				<label class="col-xs-6 col-md-4 control-label">CMS(콘텐츠명)</label>
				<div class="col-xs-12 col-md-8">
					<input type="text" id="contsNm" name="contsNm" value="" size="50" maxlength="36" class="form-control" placeholder="CMS(콘텐츠명)" readonly />
				</div>
			</div>
			<!-- 
			<div class="row">
				<label class="col-sm-4 control-label">콘텐츠유형</label>
				<div class="col-sm-8">
					<input type="text" id="contsTypeNm" name="contsTypeNm" value="" size="50" maxlength="36" class="form-control" placeholder="콘텐츠유형" readonly />
				</div>
			</div>
			<div class="row">
				<label class="col-sm-4 control-label">이벤트 명</label>
				<div class="col-sm-8">
					<input type="text" id="eventName" name="eventName" value="" size="50" maxlength="36" class="form-control" placeholder="이벤트명"  readonly/>
				</div>
			</div> 
			<div class="row">
				<label class="col-sm-4 control-label">비고</label>
				<div class="col-sm-8">
					<textarea id="eventDesc" name="eventDesc" class="form-control" placeholder="비고" cols="6" rows="5" value="${param.keyword}" ></textarea>
				</div>
			</div> 
			<div class="row">
				<label class="col-sm-4 control-label">성별</label>
				<div class="col-sm-8"> 
					<input type="radio" name="rdoGender" value="N" checked="checked">전체
					<input type="radio" name="rdoGender" value="M">남
					<input type="radio" name="rdoGender" value="F">여
				</div>
			</div>
			<div class="row">
				<label class="col-sm-4 control-label">나이</label>
				<div class="col-sm-8"> 
					<div class="col-sm-5 form-group">
						<input type="radio" name="rdoAge" value="N" checked="checked">전체
						<input type="radio" name="rdoAge" value="Y">지정
					</div>
					<div class="col-sm-7 form-group">
						<input type="text" name="txtAgeStart" class="form-control" size="5" maxlength="3" value="">세 ~ <input type="text" name="txtAgeEnd" size="5" maxlength="3" class="form-control" value="">세
					</div>
				</div>
			</div> 
			-->
			<div class="row">
				<label class="col-xs-6 col-md-4 control-label">나이</label>
				<div class="col-xs-12 col-md-8">
					<input type="radio" name="rdoAge" value="N" checked="checked">전체
					<input type="radio" name="rdoAge" value="Y">지정
					<input type="text" name="txtAgeStart" class="form-control" size="5" maxlength="3" value="">세 ~
					<input type="text" name="txtAgeEnd" size="5" maxlength="3" class="form-control" value="">세
				</div>
			</div>
			<hr />
			<div class="center">
				<button id="translationRegBtn" type="button" class="btn btn-primary btn-sm">이관하기</button>
				<button type="button" class="btn btn-default btn-sm trans_popup_close">취소</button>
			</div>
			</form>
		</div> 
	</div>
<!-- //end popup -->
</form>
</body>
</html>