<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
    <script type="text/javascript">
        window.layoutGnb = {mainmenu: '06', submenu: '03', location: ['<spring:message code="menu.140000"/>', '<spring:message code="menu.140300"/>']};
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/service/attendance.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140300" /></b>
    </div>
    <div class="boxCon">
        <div class="listResearch">
            <form name="form1" id="form1" role="form">
                <input type="text"  id="attdDate" name="attdDate" class="useDatepicker" placeholder="<spring:message code="word.attendance.date"/>" value="${attdDate}">
                <select name="opt" id="opt" class="w150" is_element_select="true">
                    <option value=""><spring:message code="word.search.option"/></option>
                    <option value="sPhoneNumber" ${param.opt == 'sPhoneNumber' ? 'selected' : ''}><spring:message code="word.phone.number"/></option>
                    <option value="sidNum" ${param.opt == 'sidNum' ? 'selected' : ''}><spring:message code="word.attendance.student.id"/></option>
                    <option value="subject" ${param.opt == 'subject' ? 'selected' : ''}><spring:message code="word.attendance.subject"/></option>
                    <option value="sName" ${param.opt == 'sName' ? 'selected' : ''}><spring:message code="word.attendance.student.name"/></option>
                </select>
                <input type="text" id="attendanceKeyword" name="keyword" class="w300" placeholder="<spring:message code="word.enter.search.term"/>" value="${param.keyword}">
                <input type="submit" value="<spring:message code="btn.register"/>" class="btn-inline btn-search">
                <a href="list.do" class="btn-inline btn-refresh"><spring:message code="btn.init"/></a>
            </form>
        </div>
        <div class="list" onclick="sortColumn(event)">
            <table>
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"><spring:message code="word.attendance.subject"/></th>
                    <th scope="col"><spring:message code="word.attendance.student.id"/></th>
                    <th scope="col"><spring:message code="word.attendance.student.name"/></th>
                    <th scope="col"><spring:message code="word.phone.number"/></th>
                    <th scope="col"><spring:message code="word.attendance.date"/></th>
                    <th scope="col"><spring:message code="word.attendance.regdate"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}" >
                    <tr>
                        <td>${list.subject}</td>
                        <td>${list.sidNum}</td>
                        <td>${list.sName}</td>
                        <td>${list.sPhoneNumber}</td>
                        <td>${list.attdDate}</td>
                        <td>
                            <jsp:useBean id="regDate" class="java.util.Date"/>
                            <jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
                            <fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
                        </td>
                    </tr>
                </c:forEach>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="6" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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