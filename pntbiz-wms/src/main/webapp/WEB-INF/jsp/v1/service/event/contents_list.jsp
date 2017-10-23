<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/contents.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script>
	$(function(){
		var _listpopURL = "/service/event/popup/content_list.do";
		$('#contentsSearchPopupBtn').click(function(){
			var conType = $("#conType option:selected").val();
			var opt = $("#opt option:selected").val();
			var keyword = common.trim($("#contentsKeyword").val());
			_listpopURL += common.setQueryString({"page": "", "conType": conType, "opt":opt, "keyword":keyword});
			common.redirect(_listpopURL);
		});	
		
		$('.btnSelContents').click(function(){
			var _conNum = $(this).data("connum");
			var _conName = $(this).data("connm");
			var _conTypeText = $(this).data("contypetext");
			
			parent.bindSelectedContents(_conNum, _conName, _conTypeText);
		});
	});
	
</script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">
<div class="col-sm-6">
	<h3>콘텐츠 목록 <small></small></h3>
</div>
<div class="col-sm-6">
<!-- 	<span class="pull-right"> -->
<!-- 		<ol class="breadcrumb"> -->
<!-- 		  <li><a href="#">콘텐츠</a></li> -->
<!-- 		  <li class="active">콘텐츠 목록</li> -->
<!-- 		</ol> -->
<!-- 	</span>  -->
</div>
<hr />
<div class="col-sm-9">
	<div class="form-group">
		<select name="conType" id="conType" class="form-control">
			<option value="">=콘텐츠유형=</option>
			<c:forEach var="conCD" items="${conCD}">
				<option value="${conCD.sCD}" <c:if test="${param.conType eq conCD.sCD}">selected</c:if>>${conCD.sName}</option>
			</c:forEach>
		</select>
	</div>
	<div class="form-group">
		<select name="opt" id="opt" class="form-control">
			<option value="">=검색옵션=</option>
			<option value="conName" ${param.opt == 'conName' ? 'selected' : ''}>콘텐츠명</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" id="contentsKeyword" name="keyword" class="form-control" placeholder="검색" value="${param.keyword}" />
	</div>
	<div class="form-group">
		<button id="contentsSearchPopupBtn" type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
	</div>	
</div>
<div class="col-sm-3 text-right">
	&nbsp;
</div>   
<div class="clearfix"></div>
<div class="bh20"></div>
<div class="col-sm-9">
	<div id="error-opt" class="pull-left"></div>
	<div id="error-keyword" class="pull-left"></div>
</div>
<div class="col-sm-3 text-right">
	<p class="text-info">총 카운트 : ${cnt}</p>
</div>
<div class="clearfix"></div>
<div class="bh10"></div>
<div class="table-responsive">
<table class="table table-bordered table-hover table-responsive" onclick="sortColumn(event)">	
	<thead>
	<tr class="active">
		<th></th>
		<th>No.</th>
		<th>콘텐츠유형</th>	
		<th>콘텐츠명</th>
		<th>광고제공자</th>
		<th>등록자</th>
		<th>유효기간</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="list" items="${list}" >		
	<tr align="center">
		<td><button type="button" class="btn btn-danger btnSelContents" data-connum="${list.conNum}" data-connm="${list.conName}" data-contypetext="${list.conTypeText}">선택</button></td>
		<td>${list.conNum}</td>
		<td>${list.conTypeText}</td>
<%-- 		<td><a href="javascript:contents.mform('${list.conNum}')">${list.conName}</a></td> --%>
 		<td>${list.conName}</td>
		<td>${list.acName}</td>
		<td>${list.userID}</td>
		<td>
			<c:if test="${list.sDateText!='' || list.eDateText!=''}">
			${list.sDateText}<br />${list.eDateText}
			</c:if>
		</td>
	</tr>
	</c:forEach>
	<c:choose>
		<c:when test="${cnt == 0}">
			<tr>
				<td colspan="7" height="150" align="center">검색 결과가 없습니다.</td>
			</tr>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	</tbody>
</table>
</div>
<c:if test="${cnt > 0}">
<div class="center">
	<ul class="pagination">
	    ${page}
	</ul>
</div>
</c:if>
</form>
</body>
</html>