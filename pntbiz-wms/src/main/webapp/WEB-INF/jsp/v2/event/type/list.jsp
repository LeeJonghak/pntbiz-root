<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>

<html>
<head>
    <script type="text/javascript">
        window.layoutGnb = {mainmenu: '06', submenu: '02', location: ['<spring:message code="menu.140000"/>', '<spring:message code="menu.140200"/>']};
    </script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/event/eventType.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140201" /></b>
    </div>
    <div class="boxCon">
        <div class="tableTopBtn aright">
            <input type="button" class="btn-inline btn-regist" id="eventTypeFormBtn" value="<spring:message code="btn.register" />">
        </div>
        <div class="list rollover">
            <table data-clickUrl="mform.do" onclick="sortColumn(event)">
                <colgroup>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"><spring:message code="word.event.type.code"/></th>
                    <th scope="col"><spring:message code="word.event.type.name"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}">
                    <tr data-param="evtTypeCode=${list.evtTypeCode}&page=${page}">
                        <td>${list.evtTypeCode}</td>
                        <td>${list.evtTypeName}</td>
                    </tr>
                </c:forEach>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="2" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
        <!-- Paging -->
        <div class="paging v2">
            <span class="total"><spring:message code="word.total.count" /> ${cnt}</span>
            <div class="original">
                ${pagination}
            </div>
        </div>
    </div>
</div>

</body>
</html>