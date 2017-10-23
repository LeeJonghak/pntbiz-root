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
					$('select[name=selZoneCode]').removeAttr("readonly");
				}else{
					$('select[name=selZoneCode]').attr("readonly","readonly");
				}
			});
			
			$('input[name=rdoBlockYN]').click(function(){
				var _val = $(this).val();
				
				$('input[name=blockUseYn]').val(_val);
				if ( _val == 'Y'){
					$('input[name=blockTxt]').removeAttr("readonly");
				}else{
					$('input[name=blockTxt]').attr("readonly","readonly");
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
					
					$('input[name=birthStart]').val( ((smonth < 10)?"0" + smonth:smonth + "").concat((sday < 10)?"0" + sday:sday+""));
					$('input[name=birthEnd]').val( ((emonth < 10)?"0" + emonth:emonth+"").concat((eday < 10)?"0" + eday:eday+""));
				}
				
				//anniversary
				$('input[name=annivUseYn]').val($('input[name=rdoAnniv]:checked').val());
				if ( 'Y' == $('input[name=annivUseYn]').val()){
					var smonth = $('input[name=AnniversaryStartMonth]').val();
					var sday = $('input[name=AnniversaryStartDay]').val();
					var emonth = $('input[name=AnniversaryEndMonth]').val();
					var eday = $('input[name=AnniversaryEndDay]').val();
					
					$('input[name=annivStart]').val( ((smonth < 10)?"0" + smonth:smonth + "").concat((sday < 10)?"0" + sday:sday + ""));
					$('input[name=annivEnd]').val( ((emonth < 10)?"0" + emonth:emonth + "").concat((eday < 10)?"0" + eday:eday + ""));
				}
				
				//visitCntUseYn
				$('input[name=visitCntUseYn]').val($('input[name=rdoVisitCtnUseYN]:checked').val());
				
				//zone
				$('input[name=zoneUseYn]').val($('input[name=rdoZoneYN]:checked').val());
				$('input[name=zoneVal]').val($("select[name=selZoneCode] option:selected").val());
				
				$('input[name=blockUseYn]').val($('input[name=rdoBlockYN]:checked').val());
				$('input[name=blockVal]').val($('input[name=blockTxt]').val());
					
				$('input[name=seatUseYn]').val($('input[name=rdoSeatYN]:checked').val());
				
				$('input[name=benefit2UseYn]').val($('input[name=rdoBenefit2]:checked').val());
				
				$('input[name=orderUseYn]').val($('input[name=rdoOrderYN]:checked').val());
				
				console.log(">>>>" + $('form[name=form1]').serialize());
				$('form[name=form1]').attr("action","<c:url value='/service/beacon/mform.do'/>");
				$('form[name=form1]').submit();
			});
		});
		
		$(document).ready(function() {

	    });
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form" method="POST">
<input type='hidden' name='sendType' value='${item.sendType }'/>
<input type='hidden' name='eventId' value='${item.eventId }'/>
<input type='hidden' name='contsId' value='${item.contsId }'/>
<input type='hidden' name='genderUseYn' />
<input type='hidden' name='genderCondVal' />
<input type='hidden' name='ageUseYn' />
<input type='hidden' name='birthUseYn' />
<input type="hidden" name="birthStart" />
<input type="hidden" name="birthEnd" />
<input type='hidden' name='annivUseYn' />
<input type="hidden" name="annivStart" />
<input type="hidden" name="annivEnd" />
<input type='hidden' name='visitCntUseYn' />
<input type='hidden' name='zoneUseYn' />
<input type='hidden' name='zoneVal' />
<input type='hidden' name='blockUseYn' />
<input type='hidden' name='blockVal' />
<input type='hidden' name='seatUseYn' />
<input type='hidden' name='benefit1UseYn' value='Y'/>
<input type='hidden' name='benefit2UseYn' />
<input type='hidden' name='orderUseYn' />


<div class="col-sm-6">
	<h3><spring:message code="word.beacon.push.manager.infomation" /><small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#"><spring:message code="word.service"/></a></li>
		  <li class="active">><spring:message code="word.beacon.push.manager.infomation" /></li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">CMS(<spring:message code="word.contents.name"/>)</label>
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
	<label class="col-sm-2 control-label"><spring:message code="word.contents.type"/></label>
	<div class="col-sm-10">
		&nbsp;${item.contsTypeNm}
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.event.name"/></label>
	<div class="col-sm-10">
		&nbsp;${item.eventNm}
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.description"/></label>
	<div class="col-sm-10">
		<textarea id="eventDesc" name="eventDesc" class="form-control" placeholder="<spring:message code="word.description"/>" cols="6" rows="5" readonly="readonly">${item.eventDesc}</textarea>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.gender"/></label>
	<div class="col-sm-10">
	
		<input type="radio" name="rdoGender" value="N"checked="checked"/><spring:message code="word.all"/>
		<input type="radio" name="rdoGender" value="M"/><spring:message code="word.male"/>
		<input type="radio" name="rdoGender" value="F"/><spring:message code="word.female"/>
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.age" /></label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoAge" value="N" checked="checked"><spring:message code="word.all" />
			<input type="radio" name="rdoAge" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="ageStartVal" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.age" /> ~</span>
			</div>
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="ageEndVal" size="5" maxlength="3" class="form-control input-sm" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.age" /></span>
			</div>
		</div> 
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.birthday" /></label>
	<div class="col-sm-10 form-group"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoBirth" value="N" checked="checked"><spring:message code="word.all" />
			<input type="radio" name="rdoBirth" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="birthStartMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.month" /></span>
			</div>
			<div class="input-group">
				<input type="text" name="birthStartDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.day" /></span>
			</div>
		</div>
		<div class="col-sm-1 form-group">
		~
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="birthEndMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.month" /></span>
			</div>
			<div class="input-group">
				<input type="text" name="birthEndDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.day" /></span>
			</div>
		</div> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.anniversary"/></label>
	<div class="col-sm-10 form-group"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoAnniv" value="N" checked="checked"><spring:message code="word.all"/>
			<input type="radio" name="rdoAnniv" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="AnniversaryStartMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.month" /></span>
			</div>
			<div class="input-group">
				<input type="text" name="AnniversaryStartDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.day" /></span>
			</div>
		</div>
		<div class="col-sm-1 form-group">
		~
		</div>
		<div class="col-sm-3 form-group">
			<div class="input-group">
				<input type="text" name="AnniversaryEndMonth" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.month" /></span>
			</div>
			<div class="input-group">
				<input type="text" name="AnniversaryEndDay" class="form-control input-sm" size="5" maxlength="2" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.day" /></span>
			</div>
		</div> 
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.visiting.count" /></label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoVisitCtnUseYN" value="N" checked="checked"><spring:message code="word.all" />
			<input type="radio" name="rdoVisitCtnUseYN" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="visitCnt" class="form-control input-sm" size="5" maxlength="3" value="0" readonly="readonly">
			</div>
		</div>
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">Zone</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoZoneYN" value="N" checked="checked"><spring:message code="word.all" />
			<input type="radio" name="rdoZoneYN" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-2 form-group">
			<select name="selZoneCode" id="selZoneCode" class="form-control" readonly="readonly">
				<option value="">=<spring:message code="word.choose"/>=</option>
				<c:forEach var="conCD" items="${conCD}">
					<option value="${conCD.sCD}" >${conCD.sName}</option>
				</c:forEach>
			</select>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label">Block</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoBlockYN" value="N" checked="checked"><spring:message code="word.all" />
			<input type="radio" name="rdoBlockYN" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="blockTxt" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon">&nbsp;</span>
			</div>
		</div>
	</div>
</div>  
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.seat.number" /></label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoSeatYN" value="N" checked="checked"><spring:message code="word.all"/>
			<input type="radio" name="rdoSeatYN" value="Y"><spring:message code="word.setup" />
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="seatVal" class="form-control input-sm" size="5" maxlength="3" value="" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.number"/> </span>
			</div>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.prize"/> 1</label>
	<div class="col-sm-10"> 
		<input type="text" name="benefit1Msg" class="form-control input-sm" size="5" maxlength="255" value="">
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.prize"/> 2</label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoBenefit2" value="Y" ><spring:message code="word.use" />
			<input type="radio" name="rdoBenefit2" value="N" checked="checked"><spring:message code="word.not.use" />
		</div>
		<div class="col-sm-2 form-group">
			<input type="text" name="benefit2Msg" class="form-control input-sm" size="5" maxlength="255" value="" readonly="readonly">
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.prize.fail.message" /></label>
	<div class="col-sm-10"> 
		<!-- 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoNak" value="Y" >사용
			<input type="radio" name="rdoNak" value="N" checked="checked">사용안함
		</div>
		 -->
		<div class="col-sm-2 form-group">
			<input type="text" name="nakchomComm" class="form-control input-sm" size="5" maxlength="255" value="" >
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.number.prize.fcfs"/></label>
	<div class="col-sm-10"> 
		<div class="col-sm-2 form-group">
			<input type="radio" name="rdoOrderYN" value="Y" ><spring:message code="word.use" />
			<input type="radio" name="rdoOrderYN" value="N" checked="checked"><spring:message code="word.not.use" />
		</div>
		<div class="col-sm-2 form-group">
			<div class="input-group">
				<input type="text" name="orderUseCnt" class="form-control input-sm" size="5" maxlength="3" value="0" readonly="readonly">
				<span class="input-group-addon"><spring:message code="word.person"/> </span>
			</div>
		</div>
	</div>
</div>
<hr />
<div class="center">
	<button id="translationRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="word.transfer"/></button>
	<button id="eventListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/service/beacon/list.do')"><spring:message code="btn.list" /></button>
</div>

</form>
</body>
</html>