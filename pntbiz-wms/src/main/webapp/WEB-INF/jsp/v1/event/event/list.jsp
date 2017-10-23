<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>

<html>
<head>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript"
            src="${viewProperty.staticUrl}/js/event/event.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<form name="form1" id="form1" class="form-inline" role="form">

<header id="topbar" class="alt">
<div class="topbar-left">
	<ol class="breadcrumb">
		<li class="crumb-active"><a href="###"><spring:message code="menu.140101" /></a></li>
		<li class="crumb-icon"><a href="/dashboard/main.do"><span class="glyphicon glyphicon-home"></span></a></li>
		<li class="crumb-trail"><spring:message code="menu.140000" /></li>
		<li class="crumb-trail"><spring:message code="menu.140100" /></li>
	</ol>
</div>
<div class="topbar-right">
	<span class="glyphicon glyphicon-question-sign help" id="help" help="maintenance.progress"></span>
</div>
</header>

<section id="content" class="table-layout animated fadeIn">

<div class="row col-md-12">
	<div class="panel"> 
	
		<div class="panel-menu">
			<div class="row">
				<div class="col-xs-8 col-md-8">
					<select name="opt" id="opt" class="form-control">
		                <option value="">=<spring:message code="word.search.option" />=</option>
		                <option value="evtName" ${param.opt == 'evtName' ? 'selected' : ''}><spring:message code="word.event.name"/></option>
		            </select>
					<div class="input-group">
						<span class="input-group-addon"><i class="fa fa-search"></i></span>
						<input type="text" id="contentsKeyword" name="keyword" class="form-control" placeholder="<spring:message code="btn.search" />" value="${param.keyword}" />
					</div>
					<button type="button" id="evtSearchBtn"  class="btn btn-default btn-sm"><spring:message code="btn.search" /></button>
				</div>
				<div class="col-xs-4 col-md-4 text-right">
					<span id="total-count" class="label bg-system"><spring:message code="word.total.count" /> ${cnt}</span>
					<button type="button" id="eventFormBtn" class="btn btn-primary btn-sm"><spring:message code="btn.register" /></button>
				</div>
			</div>
		</div>	
		
		<div class="panel-body pn">
			<table class="table table-striped table-hover" onclick="sortColumn(event)">
				<thead>
	            <tr>
	                <th><spring:message code="word.no"/></th>
	                <th><spring:message code="word.event.name" /></th>
	                <th class="hidden-xs"><spring:message code="word.event.description"/></th>
	                <th><spring:message code="word.event.type.code"/></th>
	                <th class="hidden-xs"><spring:message code="word.regdate"/></th>
	            </tr>
	            </thead>
	            <tbody>
	            <c:forEach var="list" items="${list}">
                <tr>
                    <td><span class="label bg-primary">${list.evtNum}</span></td>
                    <td>
                        <a href="<c:url value="mform.do?evtNum=${list.evtNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}"/>">${list.evtName}</a>
                    </td>
                    <td class="hidden-xs"><c:out value="${list.evtDesc}" default="-"/></td>
                    <td><c:out value="${list.evtTypeCode}" default="-"/></td>
                    <td class="hidden-xs">${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
                </tr>
	            </c:forEach>
	            <c:choose>
	                <c:when test="${cnt == 0}">
	                    <tr>
	                        <td colspan="5" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
	                    </tr>
	                </c:when>
	                <c:otherwise>
	                </c:otherwise>
	            </c:choose>
	            </tbody>
	        </table>		
		</div>
		
		<div class="panel-footer clearfix">
			<ul class="pagination pull-right">
				${pagination}
			</ul>
		</div>

	</div>
</div>
</section>
</form>
</body>
</html>