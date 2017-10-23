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
<script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contentsInfo.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
<style>
  /* demo styles -summernote */
  .btn-toolbar > .btn-group.note-fontname {
    display: none;
  }
  /* demo styles - hides several ckeditor toolbar buttons */
  #cke_52,
  #cke_53 {
    display: none !important;
  }
  </style>

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
				<label class="col-sm-2 control-label"><spring:message code="word.contents.type" /></label>
				<div class="col-sm-10">
					<div class="form-inline">
						<select name="typeNum" id="typeNum" class="form-control">
							<option value="">==<spring:message code="word.contents.type" />==</option>
							<c:forEach var="contentsTypeList" items="${contentsTypeList}">
								<option value="${contentsTypeList.typeNum}">${contentsTypeList.typeName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label"><spring:message code="word.contents.type.component"/></label>
				<div class="col-sm-10" id="component">
					<h2>이미지</h2>
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
					<h2>사운드</h2>
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
					<h2>비디오</h2>
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
					<h2>텍스트에리어</h2>
					<div>
						<textarea cols="600" rows="5"></textarea>
					</div>
					<h2>텍스트</h2>
					<div>
						<input type="text" id="text1" name="text1" class="form-control" placeholder="text1" />
					</div>
					<h2>URL</h2>
					<div>
						<input type="text" id="url1" name="url1" class="form-control" placeholder="url1" />
					</div>
					<h2>HTML</h2>
					<div class="panel panel-widget compose-widget">
						<div class="panel-heading"><span class="panel-icon"><i class="fa fa-pencil"></i></span>
						<span class="panel-title"> Quick Post</span>
					</div>
					<div class="panel-body">
						<div class="summernote-quick">Compose your message here...</div>
					</div>
					<div class="panel-footer text-right">
						<button class="btn btn-default btn-sm ph15 mr5" type="button">Draft</button>
						<button class="btn btn-primary btn-sm ph15" type="button">Send</button>
					</div>
					
					<!-- Summernote Editor -->
			          <h4 class="micro-header">Summernote - Panel & Live Editor</h4>
			          <div class="panel">
			            <div class="panel-body pn of-h" id="summer-demo">
			              <div class="summernote" style="height: 400px;">This is the
			                <b>Summernote</b> Editor...</div>
			            </div>
			            <div class="panel-body bg-light br-t-n ptn ph20" id="summer-demo2">
			              <div class="summernote-edit">
			                <h3>Highlight this text to Inline Edit!</h3>
			                <p>
			                  <strong> Lorem ipsum dolor sit amet dolor. Duis blandit vestibulum faucibus a, tortor. </strong>
			                </p>
			                <p> Proin nunc justo felis mollis tincidunt, risus risus pede, posuere cubilia Curae, Nullam euismod, enim. Etiam nibh ultricies dolor ac dignissim erat volutpat. Vivamus fermentum
			                  <a href="http://ckeditor.com/">nisl nulla sem in</a> metus. Maecenas wisi. Donec nec erat volutpat. </p>
			                <blockquote>
			                  <p> Libero nunc, rhoncus ante ipsum non ipsum. Nunc eleifend pede turpis id sollicitudin fringilla. Phasellus ultrices, velit ac arcu. </p>
			                </blockquote>
			                <p>Pellentesque nunc. Donec suscipit erat. Pellentesque habitant morbi tristique ullamcorper.</p>
			                <p>
			                  <s>Mauris mattis feugiat lectus nec mauris. Nullam vitae ante.</s>
			                </p>
			              </div>
			            </div>
			          </div>
			
								
				</div>
			</div>
			
		</div>
		
		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				<button id="contentsTypeRegBtn" type="button" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				<button id="contentsTypeListBtn" type="button" class="btn btn-default btn-sm"><spring:message code="btn.list" /></button>
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>