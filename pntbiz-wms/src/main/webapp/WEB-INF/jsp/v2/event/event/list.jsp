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
        window.layoutGnb = {mainmenu: '06', submenu: '01', location: ['<spring:message code="menu.140000"/>', '<spring:message code="menu.140100"/>']};
    </script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready(function () {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/event/event.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140101" /></b>
    </div>
    <div class="boxCon">
        <div class="listResearch">
            <form name="form1" id="form1" role="form">
                <select name="opt" id="opt" class="w150" is_element_select="true">
                    <option value="">=<spring:message code="word.search.option"/>=</option>
                    <option value="evtName" ${param.opt == 'evtName' ? 'selected' : ''}><spring:message code="word.event.name"/></option>
                </select>
                <input type="text" id="contentsKeyword" name="keyword" placeholder="<spring:message code="word.enter.search.term"/>" name="keyword" class="w300" value="${param.keyword}">
                <input type="submit" value="<spring:message code="btn.search"/>" class="btn-inline btn-search">
                <a href="list.do" class="btn-inline btn-refresh"><spring:message code="btn.init"/></a>
            </form>
        </div>
        <div class="tableTopBtn aright">
            <input type="button" class="btn-inline btn-regist" id="eventFormBtn" value="<spring:message code="btn.register" />">
        </div>
        <div class="list rollover">
            <table data-clickUrl="mform.do" onclick="sortColumn(event)">
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"><spring:message code="word.no"/></th>
                    <th scope="col"><spring:message code="word.event.name" /></th>
                    <th scope="col"><spring:message code="word.event.description"/></th>
                    <th scope="col"><spring:message code="word.event.type.code"/></th>
                    <th scope="col"><spring:message code="word.regdate"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}">
                    <tr data-param="evtNum=${list.evtNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}">
                        <td>${list.evtNum}</td>
                        <td>${list.evtName}</td>
                        <td><c:out value="${list.evtDesc}" default="-"/></td>
                        <td><c:out value="${list.evtTypeCode}" default="-"/></td>
                        <td>${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
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