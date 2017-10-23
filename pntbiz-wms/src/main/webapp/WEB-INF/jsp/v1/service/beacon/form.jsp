<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
	<!-- 
		ref : http://vast-engineering.github.io/jquery-popup-overlay/
	 -->
	<link rel="stylesheet" href="${viewProperty.staticUrl}/external/js.spiner/ladda-themeless.min.css">
	<script src="${viewProperty.staticUrl}/external/js.spiner/spin.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script src="${viewProperty.staticUrl}/external/js.spiner/ladda.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	
	<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.popupoverlay/jquery.popupoverlay.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
	<script>
		var l;
		var jvPage = {
			ready: function() {
				
				// Initialize the plugin
				$('#my_popup').popup({
					autoopen: false,
		            type: 'overlay',
		            blur : false,
		            transition: 'all 0.3s'
		      	});
				
				$('.my_popup_opendialog').click(function(e){
					l = Ladda.create(this);
					e.preventDefault();
					l.start();
					
					$('#contentifmPopup').attr('src','<c:url value="/service/beacon/popup/content_list.do"/>');
				});
				
				$('#contentifmPopup').load(function(){
					if ( $(this).attr('src') != null ){
						$('#my_popup').popup('show');
						l.stop();
					}
				});
				
				//submit
				$('#eventRegBtn').click(jvPage.OnRegist);
			},
			OnRegist : function(){
				
				$('form[name=form1]').attr("action","<c:url value='/service/beacon/form.do'/>");
// 				$('form[name=form1]').attr("target","ifrmPay");
				$('form[name=form1]').submit();
// 				$('form[name=form1]').attr("target","");
			}
		};


		function bindSelectedContents(conNum, conNm, conTypeText){
			$('#contsId').val(conNum);
			$('#contsNm').val(conNm);
			$('#contsTypeNm').val(conTypeText);
			
			$('.my_popup_close').click();
		}

		$(document).ready(jvPage.ready);
	</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form" method="POST">
<input type="hidden" name="sendType" value="BP"/>
<div class="col-sm-6">
	<h3><spring:message code="word.event.register" /> <small><spring:message code="word.beacon.push" /></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#"><spring:message code="word.service" /></a></li>
		  <li class="active"><spring:message code="word.event.register" /></li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">CMS(<spring:message code="word.contents.name" />)</label>
	<div class="col-sm-10">
		<div class="col-sm-3 form-group">
			<input type="hidden" id="contsId" name="contsId" value="${item.contsId}" />
			<input type="text" id="contsNm" name="contsNm" value="${item.contsNm}" size="50" maxlength="36" class="form-control" placeholder="CMS(<spring:message code="word.contents.name" />)" readonly />
		</div>
		<div class="col-sm-9 form-group">
<!-- 			&nbsp;<button type="button" class="my_popup_opendialog btn btn-info btn-sm">검색선택</button>  -->
			<button type="button" class="my_popup_opendialog btn btn-primary ladda-button" data-style="expand-right"><span class="ladda-label"><spring:message code="word.search.choose" /></span></button>
		</div>
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.contents.type" /></label>
	<div class="col-sm-10">
		<input type="hidden" id="contsType" name="contsType" value="" />
		<input type="text" id="contsTypeNm" name="contsTypeNm" value="" size="50" maxlength="36" class="form-control" placeholder="<spring:message code="word.contents.type" />" readonly />
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.event.name" /></label>
	<div class="col-sm-10">
		<input type="text" id="eventNm" name="eventNm" size="50" maxlength="36" class="form-control" placeholder="<spring:message code="word.event.name" />"  value="${item.eventNm}"/>
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label"><spring:message code="word.description" /></label>
	<div class="col-sm-10">
		<textarea id="eventMemo" name="eventMemo" class="form-control" placeholder="<spring:message code="word.description" />" cols="6" rows="5" >${item.eventMemo}</textarea>
		0 / 200 byte
	</div>
</div> 
<hr />
<div class="center">
	<button id="eventRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
	<button id="eventListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/service/beacon/list.do')"><spring:message code="btn.list" /></button>
</div>

<!-- Add popup ui -->
  	<div id="my_popup" class="well popup_content">
    	<iframe id="contentifmPopup" src='' style="width:1000px;height:600px;"></iframe>
    	<br/>
    	<div style="text-align:right;">
    		<button class="my_popup_close basic_close btn btn-default"><spring:message code="btn.close" /></button>
    	</div>
	</div>
<!-- //end popup -->
</form>
</body>
</html>