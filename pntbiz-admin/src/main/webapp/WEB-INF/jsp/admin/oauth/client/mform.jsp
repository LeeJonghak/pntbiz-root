<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.css?v=<spring:eval expression='@config[buildVersion]'/>" />
    <script type="text/javascript" src="<spring:eval expression='@config[staticURL]'/>/external/js.tablesort/tablesort.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>

<body>
    <form name="form1" id="form1" class="form-horizontal" role="form">
        <div class="col-sm-6">
            <h3>OAuth 클라이언트 등록<small></small></h3>
        </div>
        <div class="col-sm-6">
            <span class="pull-right">
                <ol class="breadcrumb">
                    <li><a href="#">OAuth 클라이언트</a></li>
                    <li class="active">OAuth 클라이언트 등록</li>
                </ol>
            </span>
        </div>

        <hr/>

        <div class="form-group">
            <label for="clientID" class="col-sm-2 control-label">클라이언트 아이디</label>
            <div class="col-sm-10">
                <input type="text" id="clientID" name="clientID" value="${oauthClientInfo.clientID}" readonly="readonly" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label for="clientSecret" class="col-sm-2 control-label">클라이언트 스크릿키</label>
            <div class="col-sm-10">
                <input type="text" id="clientSecret" name="clientSecret" value="${oauthClientInfo.clientSecret}" readonly="readonly" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">그랜트 유형</label>
            <div class="col-sm-10">
                <select id="grantTypes" name="grantTypes" class="form-control input-sm" required="required">
                    <option value="client_credentials">client_credentials</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label for="memo" class="col-sm-2 control-label">비고</label>
            <div class="col-sm-10">
                <textarea id="memo" name="memo" class="form-control" autofocus="autofocus" maxlength="200" rows="3"><c:out value="${oauthClientInfo.memo}" escapeXml="true"/></textarea>
            </div>
        </div>

        <div class="form-group">
            <label for="memo" class="col-sm-2 control-label">토큰</label>
            <div class="col-sm-10">
                <table class="table table-responsive">
                    <thead>
                        <tr class="active">
                            <td class="text-center">엑세스 토큰</td>
                            <%--<td class="text-center">리플레쉬 토큰</td>--%>
                            <td class="text-center">만료시간</td>
                        </tr>
                    </thead>
                    <c:forEach var="item" items="${accessTokenList}" varStatus="status">
                    <tbody>
                        <tr>
                            <td>${item.tokenObject.value}</td>
                            <%--<td>${item.tokenObject.refreshToken.value}</td>--%>
                            <td class="text-center"><fmt:formatDate value="${item.tokenObject.expiration}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                    </tbody>
                    </c:forEach>
                </table>
            </div>
        </div>

        <hr/>

        <div class="center">
            <button id="regBtn" type="button" class="btn btn-primary btn-sm">수정</button>
            <button id="delBtn" type="button" class="btn btn-default btn-sm">삭제</button>
            <button id="listBtn" type="button" class="btn btn-default btn-sm">리스트</button>
        </div>
    </form>
    <script type="text/javascript">
        var handler = new ElementHandler('<c:url value="/"/>');
        handler.bind({
            listBtn: function(){
                var page = common.getQueryString('page');
                var param = common.setQueryString(["page"]);
                common.redirect('list.do'+param);
            },
            regBtn: {
                action: 'submit',
                form: 'form1',
                url: 'oauth/client/mod.do',
                result: {
                    1: {message:'정상적으로 수정되었습니다.',
                        redirect:'oauth/client/list.do?page=\#{page}'},
                    2: '오류가 발생하였습니다',
                    3: 'OAuth2 클라이언트 정보를 찾을 수 없습니다. 시스템 오류이거나 이미 삭제된 정보일 수 있습니다.'
                }
            },
            delBtn: {
                action: 'submit',
                form: 'form1',
                url: 'oauth/client/del.do',
                confirm: '정말로 삭제하시겠습니까?',
                result: {
                    1: {message:'정상적으로 삭제되었습니다.',
                        redirect:'oauth/client/list.do?page=\#{page}'},
                    2: '오류가 발생하였습니다',
                    3: 'OAuth2 클라이언트 정보를 찾을 수 없습니다. 시스템 오류이거나 이미 삭제된 정보일 수 있습니다.'
                }
            }
        });
    </script>
</body>
</html>