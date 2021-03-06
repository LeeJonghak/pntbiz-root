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
	<!-- 
		ref : http://vast-engineering.github.io/jquery-popup-overlay/
	 -->
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.popupoverlay/jquery.popupoverlay.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script>
		$(function(){
			//gender
			$('input[name=rdoGender]').click(function(){
				var _val = $(this).val();
				if ( _val == 'F' || _val == 'M' ){
					$('input[name=genderUseYn]').val('Y');
					$('input[name=genderCondVal]').val(_val);
				}else{
					$('input[name=genderUseYn]').val('N');
					$('input[name=genderCondVal]').val('');
				}
			});
			
			//age
			$('input[name=rdoAge]').click(function(){
				var _val = $(this).val();
				
				$('input[name=ageUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=ageStartVal]').removeAttr("readonly");
					$('input[name=ageEndVal]').removeAttr("readonly");
				}else{
					$('input[name=ageStartVal]').attr("readonly","readonly");
					$('input[name=ageEndVal]').attr("readonly","readonly");
				}
			});
			
			//birth
			$('input[name=rdoBirth]').click(function(){
				var _val = $(this).val();
				
				$('input[name=birthUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=birthStartMonth]').removeAttr("readonly");
					$('input[name=birthStartDay]').removeAttr("readonly");
					$('input[name=birthEndMonth]').removeAttr("readonly");
					$('input[name=birthEndDay]').removeAttr("readonly");
				}else{
					$('input[name=birthStartMonth]').attr("readonly","readonly");
					$('input[name=birthStartDay]').attr("readonly","readonly");
					$('input[name=birthEndMonth]').attr("readonly","readonly");
					$('input[name=birthEndDay]').attr("readonly","readonly");
				}
			});
			
			//Anniversary
			$('input[name=rdoAnniv]').click(function(){
				var _val = $(this).val();
				
				$('input[name=birthUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=AnniversaryStartMonth]').removeAttr("readonly");
					$('input[name=AnniversaryStartDay]').removeAttr("readonly");
					$('input[name=AnniversaryEndMonth]').removeAttr("readonly");
					$('input[name=AnniversaryEndDay]').removeAttr("readonly");
				}else{
					$('input[name=AnniversaryStartMonth]').attr("readonly","readonly");
					$('input[name=AnniversaryStartDay]').attr("readonly","readonly");
					$('input[name=AnniversaryEndMonth]').attr("readonly","readonly");
					$('input[name=AnniversaryEndDay]').attr("readonly","readonly");
				}
			});
			
			//visit count
			$('input[name=rdoVisitCtnUseYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=visitCntUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=visitCnt]').removeAttr("readonly");
				}else{
					$('input[name=visitCnt]').attr("readonly","readonly");
				}
			});
			
			$('input[name=rdoZoneYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=zoneUseYn]').val(_val);
				if ( _val == 'Y'){
					$('select[name=selZoneCode]').removeAttr("disabled");
				}else{
					$('select[name=selZoneCode]').attr("disabled","disabled");
				}
			});
			
			$('input[name=rdoBlockYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=blockUseYn]').val(_val);
				if ( _val == 'Y'){
					$('select[name=selBlockCode]').removeAttr("disabled");
				}else{
					$('select[name=selBlockCode]').attr("disabled","disabled");
				}
			});
			
			$('input[name=rdoSeatYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=seatUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=seatVal]').removeAttr("readonly");
				}else{
					$('input[name=seatVal]').attr("readonly","readonly");
				}
			});
			
			$('input[name=rdoBenefit2]').click(function(){
				var _val = $(this).val();
				
				$('input[name=benefit2UseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=benefit2Msg]').removeAttr("readonly");
				}else{
					$('input[name=benefit2Msg]').attr("readonly","readonly");
				}
			});
			
			$('input[name=rdoOrderYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=orderUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=orderUseCnt]').removeAttr("readonly");
				}else{
					$('input[name=orderUseCnt]').attr("readonly","readonly");
				}
			});
			
			$('input[name=rdoNak]').click(function(){
				var _value = $(this).val();
				if ( _value == 'Y' ){
					$('input[name=nakchomComm]').removeAttr("readonly");
				}else{
					$('input[name=nakchomComm]').attr("readonly","readonly");
				}
			});   
			
			
			$('#translationRegBtn').click(function(){
				
				//gender
				var _val = $('input[name=rdoGender]:checked').val();
				if ( _val == 'F' || _val == 'M' ){
					$('input[name=genderUseYn]').val('Y');
					$('input[name=genderCondVal]').val(_val);
				}else{
					$('input[name=genderUseYn]').val('N');
					$('input[name=genderCondVal]').val('');
				}
				
				//age
				var _valAge = $('input[name=rdoAge]:checked').val()
				$('input[name=ageUseYn]').val(_valAge);
				
				
				//birth
				$('input[name=birthUseYn]').val($('input[name=rdoBirth]:checked').val());
				if ( 'Y' == $('input[name=birthUseYn]').val()){
					var smonth = $('input[name=birthStartMonth]').val();
					var sday = $('input[name=birthStartDay]').val();
					var emonth = $('input[name=birthEndMonth]').val();
					var eday = $('input[name=birthEndDay]').val();
					
					$('input[name=birthStart]').val( ((parseInt(smonth) < 10)?"0" + parseInt(smonth):parseInt(smonth) + "").concat((parseInt(sday) < 10)?"0" + parseInt(sday):parseInt(sday)+""));
					$('input[name=birthEnd]').val( ((parseInt(emonth) < 10)?"0" + parseInt(emonth):parseInt(emonth)+"").concat((parseInt(eday) < 10)?"0" + parseInt(eday):parseInt(eday)+""));
				}
				
				//anniversary
				$('input[name=annivUseYn]').val($('input[name=rdoAnniv]:checked').val());
				if ( 'Y' == $('input[name=annivUseYn]').val()){
					var smonth = $('input[name=AnniversaryStartMonth]').val();
					var sday = $('input[name=AnniversaryStartDay]').val();
					var emonth = $('input[name=AnniversaryEndMonth]').val();
					var eday = $('input[name=AnniversaryEndDay]').val();
					
					$('input[name=annivStart]').val( ((parseInt(smonth) < 10)?"0" + parseInt(smonth):parseInt(smonth) + "").concat((parseInt(sday) < 10)?"0" + parseInt(sday):parseInt(sday)+""));
					$('input[name=annivEnd]').val( ((parseInt(emonth) < 10)?"0" + parseInt(emonth):parseInt(emonth)+"").concat((parseInt(eday) < 10)?"0" + parseInt(eday):parseInt(eday)+""));
				}
				
				//visitCntUseYn
				$('input[name=visitCntUseYn]').val($('input[name=rdoVisitCtnUseYN]:checked').val());
				if ( $('input[name=rdoVisitCtnUseYN]:checked').val() == 'N'){
					$('input[name=visitCnt]').val("0");
				}
				
				//zone
				$('input[name=zoneUseYn]').val($('input[name=rdoZoneYN]:checked').val());
				$('input[name=zoneVal]').val($("select[name=selZoneCode] option:selected").val());
				
				$('input[name=blockUseYn]').val($('input[name=rdoBlockYN]:checked').val());
				$('input[name=blockVal]').val($("select[name=selZoneCode] option:selected").val());
					
				$('input[name=seatUseYn]').val($('input[name=rdoSeatYN]:checked').val());
				
				console.log(">>>>" + $('form[name=form1]').serialize());
				$('form[name=form1]').attr("action","<c:url value='/service/event/mform.do'/>");
				$('form[name=form1]').submit();
			});
		});
		
		$(document).ready(function() {
			
	    });
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form" method="POST">
<input type='hidden' name='sendType' 				value='${item.sendType}'/>
<input type='hidden' name='eventId' 				value='${item.eventId }'/>
<input type='hidden' name='contsId' 				value='${item.contsId }'/>
<input type='hidden' name='genderUseYn' 			value=''/>
<input type='hidden' name='genderCondVal' 			value=''/>
<input type='hidden' name='ageUseYn' 				value=''/>
<input type='hidden' name='birthUseYn' 				value=''/>
<input type="hidden" name="birthStart" 				value=''/>
<input type="hidden" name="birthEnd" 				value=''/>
<input type='hidden' name='annivUseYn' 				value=''/>
<input type="hidden" name="annivStart" 				value=''/>
<input type="hidden" name="annivEnd" 				value=''/>
<input type='hidden' name='visitCntUseYn' 			value=''/>
<input type='hidden' name='zoneUseYn' 				value=''/>
<input type='hidden' name='zoneVal' 				value=''/>
<input type='hidden' name='blockUseYn' 				value=''/>
<input type='hidden' name='blockVal' 				value=''/>
<input type='hidden' name='seatUseYn' 				value=''/>
<input type='hidden' name='benefit1UseYn' 			value='N'/>
<input type='hidden' name='benefit1Msg'				value=''/>
<input type='hidden' name='benefit1SeatBlock'		value=''/>
<input type='hidden' name='benefit1SeatCol'			value=''/>
<input type='hidden' name='benefit1SeatNum'			value=''/>
<input type='hidden' name='benefit2UseYn' 			value='N'/>
<input type='hidden' name='benefit2Msg'				value=''/>
<input type='hidden' name='benefit2SeatBlock'		value=''/>
<input type='hidden' name='benefit2SeatCol'			value=''/>
<input type='hidden' name='benefit2SeatNum'			value=''/>
<input type='hidden' name='nakchomComm' 			value=''/>
<input type='hidden' name='orderUseYn' 				value='N'/>
<input type='hidden' name='orderUseCnt' 			value='0'/>



<div class="col-sm-6">
	<h3>현장이벤트 관리정보<small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">서비스</a></li>
		  <li class="active">이관하기</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">CMS(콘텐츠명)</label>
	<div class="col-sm-10">
		<div class="col-sm-3 form-group">
			&nbsp;${item.contsNm}
		</div>
		<!-- 
		<div class="col-sm-9 form-group">
			&nbsp;<button id="contsSrchBtn" type="button" class="btn btn-info btn-sm">신규작성</button> 
		</div>
		 -->
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">콘텐츠유형</label>
	<div class="col-sm-10">
		&nbsp;${item.contsTypeNm}
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">이벤트 명</label>
	<div class="col-sm-10">
		&nbsp;${item.eventNm}
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">비고</label>
	<div class="col-sm-10">
		<textarea id="eventDesc" name="eventDesc" class="form-control" placeholder="비고" cols="6" rows="5" readonly="readonly">${item.eventDesc}</textarea>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">성별</label>
	<div class="col-sm-10">
	
		<input type="radio" name="rdoGender" value="N" checked="checked"/>전체
		<input type="radio" name="rdoGender" value="M"/>남성
		<input type="radio" name="rdoGender" value="F"/>여성
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">나이</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoAge" value="N" checked="checked">전체
			<input type="radio" name="rdoAge" value="Y">지정 
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="ageStartVal" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon">세 ~</span>
			</div>
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="ageEndVal" size="5" maxlength="3" class="form-control input-sm" value="" readonly="readonly">
				<span class="input-group-addon">세</span>
			</div>
		</div> 
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">생일</label>
	<div class="col-sm-10 form-group"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoBirth" value="N" checked="checked">전체
			<input type="radio" name="rdoBirth" value="Y">지정
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="birthStartMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">월</span>
			</div>
			<div class="input-group">
				<input type="text" name="birthStartDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">일</span>
			</div>
		</div>
		<div class="col-sm-1 form-group">
		~
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="birthEndMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">월</span>
			</div>
			<div class="input-group">
				<input type="text" name="birthEndDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">일</span>
			</div>
		</div> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">기념일</label>
	<div class="col-sm-10 form-group"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoAnniv" value="N" checked="checked">전체
			<input type="radio" name="rdoAnniv" value="Y">지정
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="AnniversaryStartMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">월</span>
			</div>
			<div class="input-group">
				<input type="text" name="AnniversaryStartDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">일</span>
			</div>
		</div>
		<div class="col-sm-1 form-group">
		~
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="AnniversaryEndMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">월</span>
			</div>
			<div class="input-group">
				<input type="text" name="AnniversaryEndDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon">일</span>
			</div>
		</div> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">방문횟수</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoVisitCtnUseYN" value="N" checked="checked">전체
			<input type="radio" name="rdoVisitCtnUseYN" value="Y">지정 
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="visitCnt" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon">회</span>
			</div>
		</div>
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">Zone별</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoZoneYN" value="N" checked="checked">전체
			<input type="radio" name="rdoZoneYN" value="Y">지정 
		</div>
		<div class="col-sm-2 form-group">
			<select name="selZoneCode" id="selZoneCode" class="form-control" disabled="disabled">
				<option value="">=선택하세요.=</option>
				<c:forEach var="conCD" items="${conCD}">
					<option value="${conCD.sCD}" >${conCD.sName}</option>
				</c:forEach>
			</select>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">블럭</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoBlockYN" value="N" checked="checked">전체
			<input type="radio" name="rdoBlockYN" value="Y">지정 
		</div>
		<div class="col-sm-3 form-group">
			<select name="selBlockCode" id="selBlockCode" class="form-control" disabled="disabled">
				<option value="">=선택하세요.=</option>
				<c:forEach var="blockItem" items="${blocklst}">
					<option value="${blockItem.FCNUM}" >${blockItem.FCNUM} [ ${blockItem.ZONETYPENM} / ${blockItem.FCNAME} ]</option>
				</c:forEach>
			</select>
		</div>
	</div>
</div>  
<div class="form-group">
	<label class="col-sm-2 control-label">좌석번호</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoSeatYN" value="N" checked="checked">전체
			<input type="radio" name="rdoSeatYN" value="Y">지정 
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="seatVal" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon">번</span>
			</div>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">PNS제목</label>
	<div class="col-sm-10">
		<input type="text" id="pnsTitle" name="pnsTitle" size="50" maxlength="36" class="form-control" placeholder="PNS제목" />
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">PNS내용</label>
	<div class="col-sm-10">
		<textarea id="pnsMsgDesc" name="pnsMsg" class="form-control" placeholder="PNS내용" cols="6" rows="5" ></textarea>
		0 / 200 byte
	</div>
</div> 
<hr />
<hr />
<div class="center">
	<button id="translationRegBtn" type="button" class="btn btn-primary btn-sm">발송</button>
	<button id="eventListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/service/push/info/list.do')">리스트</button>
</div>

</form>
</body>
</html>