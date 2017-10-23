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
					
					$('#contentifmPopup').attr('src','<c:url value="/service/seat/popup/content_list.do"/>');
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
				
				$('form[name=form1]').attr("action","<c:url value='/service/seat/form.do'/>");
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
<input type="hidden" name="sendType" value="AP"/>
<div class="col-sm-6">
	<h3>이벤트 등록 <small></small></h3>
</div>
<div class="col-sm-6">
	<span class="pull-right">
		<ol class="breadcrumb">
		  <li><a href="#">서비스</a></li>
		  <li class="active">이벤트 등록</li>
		</ol>
	</span>
</div>
<hr />
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">CMS(콘텐츠명)</label>
	<div class="col-sm-10">
		<div class="col-sm-3 form-group">
			<input type="hidden" id="contsId" name="contsId" value="${item.contsId}" />
			<input type="text" id="contsNm" name="contsNm" value="${item.contsNm}" size="50" maxlength="36" class="form-control" placeholder="CMS(콘텐츠명)" readonly />
		</div>
		<div class="col-sm-9 form-group">
<!-- 			&nbsp;<button type="button" class="my_popup_opendialog btn btn-info btn-sm">검색선택</button>  -->
			<button type="button" class="my_popup_opendialog btn btn-primary ladda-button" data-style="expand-right"><span class="ladda-label">검색선택</span></button>
		</div>
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">콘텐츠유형</label>
	<div class="col-sm-10">
		<input type="hidden" id="contsType" name="contsType" value="" />
		<input type="text" id="contsTypeNm" name="contsTypeNm" value="" size="50" maxlength="36" class="form-control" placeholder="콘텐츠유형" readonly />
	</div>
</div>
<div id="error-message" class="alert alert-danger hide"></div>
<div class="form-group">
	<label class="col-sm-2 control-label">이벤트 명</label>
	<div class="col-sm-10">
		<input type="text" id="eventNm" name="eventNm" size="50" maxlength="36" class="form-control" placeholder="이벤트명"  value="${item.eventNm}"/>
	</div>
</div> 
<div class="form-group">
	<label class="col-sm-2 control-label">비고</label>
	<div class="col-sm-10">
		<textarea id="eventMemo" name="eventMemo" class="form-control" placeholder="비고" cols="6" rows="5" >${item.eventMemo}</textarea>
		0 / 200 byte
	</div>
</div> 
<hr />
<div class="center">
	<button id="eventRegBtn" type="button" class="btn btn-primary btn-sm">등록</button>
	<button id="eventListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/service/seat/list.do')">리스트</button>
</div>

<!-- Add popup ui -->
  	<div id="my_popup" class="well popup_content">
    	<iframe id="contentifmPopup" src='' style="width:1000px;height:600px;"></iframe>
    	<br/>
    	<div style="text-align:right;">
    		<button class="my_popup_close basic_close btn btn-default">닫기</button>
    	</div>
	</div>
<!-- //end popup -->
</form>
</body>
</html>