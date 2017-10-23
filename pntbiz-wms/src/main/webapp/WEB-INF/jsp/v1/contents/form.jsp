<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/css/contents.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<link type="text/css" rel="stylesheet" href="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.css?v=<spring:eval expression='@config[buildVersion]'/>" />
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/moment.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.tagautocomplete/bootstrap-typeahead.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<script type="text/javascript" src="${viewProperty.staticUrl}/external/js.jquery.bootstrap.datetimepicker/bootstrap-datetimepicker.min.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<c:choose>
    <c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'en'}">
    	<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents_en.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    </c:when>
    <c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'jp'}">
    	<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents_jp.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    </c:when>
    <c:otherwise>
    	<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
    </c:otherwise>
</c:choose>  
<script>
$(document).ready( function() { contents.initForm(); });
</script>
</head>
<body>
<form name="form1" id="form1" class="form-horizontal" role="form">
<input type="hidden" id="comNum" name="comNum" value="${comNum}" />

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.130102" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.130000" /></li>
		<li class="crumb-trail"><spring:message code="menu.130100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-heading"></div> 
		
		<div class="panel-body">	
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.contents.type"/></label>
				<div class="col-sm-10">
					<select name="conType" id="conType" class="form-control">
						<option value="">=<spring:message code="word.contents.type"/>=</option>
						<c:forEach var="conCD" items="${conCD}">
							<option value="${conCD.sCD}" <c:if test="${param.sCD eq conCD.sCD}">selected</c:if>>
								<c:if test="${empty conCD.langCode}">${conCD.sName}</c:if>
								<c:if test="${not empty conCD.langCode}"><spring:message code="${conCD.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.contents.name"/></label>
				<div class="col-sm-10">
					<input type="text" id="conName" name="conName" value="" maxlength="25" class="form-control" placeholder="<spring:message code="word.contents.name"/>"  />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.advertising.provider"/></label>
				<div class="col-sm-10">
					<input type="hidden" id="acNum" name="acNum" value="" />
					<input type="text" id="acName" name="acName" value="" maxlength="25" class="form-control" placeholder="<spring:message code="word.advertising.provider"/>" data-provide="typeahead" data-items="4" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.validity.date"/></label>
				<div class="col-sm-10">
					<div class="input-group date" id="datetimepicker1">
						<input type="text" id="sDate" name="sDate" class="form-control" placeholder="<spring:message code="word.start.date"/>" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="input-group date" id="datetimepicker2">
						<input type="text" id="eDate" name="eDate" class="form-control" placeholder="<spring:message code="word.end.date"/>" />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.visible.option"/></label>
				<div class="col-sm-10">
					<select name="expFlag" id="expFlag" class="form-control">
						<option value="">=<spring:message code="word.visible.option"/>=</option>
						<c:forEach var="expFlag" items="${expFlag}">
							<option value="${expFlag.sCD}">
								<c:if test="${empty expFlag.langCode}">${expFlag.sName}</c:if>
								<c:if test="${not empty expFlag.langCode}"><spring:message code="${expFlag.langCode}"/></c:if>
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">RSSI</label>
				<div class="col-sm-10">
					<input type="text" id="rssi" name="rssi" value="" maxlength="5" class="form-control"  />
				</div>
			</div>
			<!-- 
			<div class="form-group" id="form-imgSrc1">
				<label class="col-sm-2 control-label" id="label-imgSrc1">imgSrc1</label>
				<div class="col-sm-10">
					<input type="file" id="imgSrc1" name="imgSrc1" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-imgSrc2">
				<label class="col-sm-2 control-label" id="label-imgSrc2">imgSrc2</label>
				<div class="col-sm-10">
					<input type="file" id="imgSrc2" name="imgSrc2" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-imgSrc3">
				<label class="col-sm-2 control-label" id="label-imgSrc3">imgSrc3</label>
				<div class="col-sm-10">
					<input type="file" id="imgSrc3" name="imgSrc3" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-imgSrc4">
				<label class="col-sm-2 control-label" id="label-imgSrc4">imgSrc4</label>
				<div class="col-sm-10">
					<input type="file" id="imgSrc4" name="imgSrc4" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-imgSrc5">
				<label class="col-sm-2 control-label" id="label-imgSrc5">imgSrc5</label>
				<div class="col-sm-10">
					<input type="file" id="imgSrc5" name="imgSrc5" class="form-control" placeholder="<spring:message code="word.image.choose" />" />
				</div>
			</div>
			-->
			<div class="form-group" id="form-img1">
				<label class="col-sm-2 control-label" id="label-img1">imgSrc1</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" id="img-thumbnail1" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc1" name="imgSrc1" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="1"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group" id="form-img2">
				<label class="col-sm-2 control-label" id="label-img2">imgSrc2</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" id="img-thumbnail2" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc2" name="imgSrc2" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="2"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group" id="form-img3">
				<label class="col-sm-2 control-label" id="label-img3">imgSrc3</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" id="img-thumbnail3" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc3" name="imgSrc3" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="3"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group" id="form-img4">
				<label class="col-sm-2 control-label" id="label-img4">imgSrc4</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" id="img-thumbnail4" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc4" name="imgSrc4" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="4"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>		
			<div class="form-group" id="form-img5">
				<label class="col-sm-2 control-label" id="label-img5">imgSrc5</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" id="img-thumbnail5" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="imgSrc5" name="imgSrc5" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="5"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>	
			<div class="form-group" id="form-text1">
				<label class="col-sm-2 control-label" id="label-text1">text1</label>
				<div class="col-sm-10">
					<input type="text" id="text1" name="text1" class="form-control" placeholder="text1" />
				</div>
			</div>
			<div class="form-group" id="form-text2">
				<label class="col-sm-2 control-label" id="label-text2">text2</label>
				<div class="col-sm-10">
					<input type="text" id="text2" name="text2" class="form-control" placeholder="text2" />
				</div>
			</div>
			<div class="form-group" id="form-text3">
				<label class="col-sm-2 control-label" id="label-text3">text3</label>
				<div class="col-sm-10">
					<input type="text" id="text3" name="text3" class="form-control" placeholder="text3" />
				</div>
			</div>
			<div class="form-group" id="form-text4">
				<label class="col-sm-2 control-label" id="label-text4">text4</label>
				<div class="col-sm-10">
					<input type="text" id="text4" name="text4" class="form-control" placeholder="text4" />
				</div>
			</div>
			<div class="form-group" id="form-text5">
				<label class="col-sm-2 control-label" id="label-text5">text5</label>
				<div class="col-sm-10">
					<input type="text" id="text5" name="text5" class="form-control" placeholder="text5" />
				</div>
			</div>
			<!--
			<div class="form-group" id="form-soundSrc1">
				<label class="col-sm-2 control-label" id="label-soundSrc1">soundSrc1</label>
				<div class="col-sm-10">
					<input type="file" id="soundSrc1" name="soundSrc1" class="form-control" placeholder="<spring:message code="word.sound.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-soundSrc2">
				<label class="col-sm-2 control-label" id="label-soundSrc2">soundSrc2</label>
				<div class="col-sm-10">
					<input type="file" id="soundSrc2" name="soundSrc2" class="form-control" placeholder="<spring:message code="word.sound.choose" />" />
				</div>
			</div>
			<div class="form-group" id="form-soundSrc3">
				<label class="col-sm-2 control-label" id="label-soundSrc3">soundSrc3</label>
				<div class="col-sm-10">
					<input type="file" id="soundSrc3" name="soundSrc3" class="form-control" placeholder="<spring:message code="word.sound.choose" />" />
				</div>
			</div>
			-->
			<div class="form-group" id="form-sound1">
				<label class="col-sm-2 control-label" id="label-sound1">soundSrc1</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc1}" width="100%" height="50" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="soundSrc1" name="soundSrc1" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="1"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>	
			<div class="form-group" id="form-sound2">
				<label class="col-sm-2 control-label" id="label-sound2">soundSrc2</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc2}" width="100%" height="50" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="soundSrc2" name="soundSrc2" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="2"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>	
			<div class="form-group" id="form-sound3">
				<label class="col-sm-2 control-label" id="label-sound3">soundSrc3</label>
				<div class="col-sm-10">
					<div class="fileupload fileupload-new admin-form" data-provides="fileupload">
						<div class="fileupload-preview thumbnail mb20">
							<img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc3}" width="100%" height="50" />
						</div>
						<div class="row">
							<div class="col-xs-6 pr5">
								<span class="button btn-system btn-file btn-block">
									<span class="fileupload-new"><spring:message code="word.choose" /></span>
									<span class="fileupload-exists"><spring:message code="word.change" /></span>									
									<input type="file" id="soundSrc3" name="soundSrc3" class="form-control">
								</span>
							</div>
							<div class="col-xs-6 pr5">								
								<button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="3"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
				</div>
			</div>	
			<div class="form-group" id="form-url1">
				<label class="col-sm-2 control-label" id="label-url1">url1</label>
				<div class="col-sm-10">
					<input type="text" id="url1" name="url1" class="form-control" placeholder="url1" />
				</div>
			</div>
			<div class="form-group" id="form-url2">
				<label class="col-sm-2 control-label" id="label-url2">url2</label>
				<div class="col-sm-10">
					<input type="text" id="url2" name="url2" class="form-control" placeholder="url2" />
				</div>
			</div>
			<div class="form-group" id="form-url3">
				<label class="col-sm-2 control-label" id="label-url3">url3</label>
				<div class="col-sm-10">
					<input type="text" id="url3" name="url3" class="form-control" placeholder="url3" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.description" /></label>
				<div class="col-sm-10">
					<textarea id="conDesc" name="conDesc" class="form-control" placeholder="<spring:message code="word.description" />" cols="6" rows="5" value=""></textarea>
					<span id="byteLimit">0</span> / 200 byte
				</div>	
			</div>
		</div>
		
		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				<button id="contentsRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="contentsListBtn" type="button" class="btn btn-default btn-sm" onClick="common.redirect('/contents/list.do')"><spring:message code="btn.list" /></button>
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>