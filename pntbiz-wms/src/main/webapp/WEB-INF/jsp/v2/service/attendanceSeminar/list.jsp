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
        window.layoutGnb = {mainmenu: '06', submenu: '04', location: ['<spring:message code="menu.140000"/>', '<spring:message code="menu.140400"/>']};
    </script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
            $("#seminarManagerBtn").on("click", function () {
                popUp("test", $("#contentHtml").html());
            });
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/service/attendanceSeminar.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div style="display: none;">
    <div id="contentHtml">
        <p>
        <form name="form2" id="form2" class="form-inline" role="form">
        <table class="view">
            <colgroup>
                <col width="18%">
                <col width="">
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code="word.seminar.register" /></th>
                <td><input type="text" id="seminarName" name="seminarName" value="" required="required" placeholder="<spring:message code="word.seminar.name" />" class="form-control" /></td>
                <td><input type="text" id="majorVer" name="majorVer" value="" required="required" min="0" max="65535" placeholder="<spring:message code="word.major.version" />" class="form-control" /></td>
                <td><input type="text" id="minorVer" name="minorVer" value="" required="required" min="0" max="65535" placeholder="<spring:message code="word.minor.version" />" class="form-control" /></td>
                <td><input type="text" id="macAddr" name="macAddr" value="" required="required" maxlength="17" placeholder="<spring:message code="word.mac.address" />" class="form-control" /></td>
                <td><input type="button" id="btnForm2Submit" value="<spring:message code="btn.register"/>" class="btn-inline" /></td>
            </tr>
            </tbody>
        </table>
        <hr>
         <span><spring:message code="word.seminar.list"/></span>
        <div class="list">
            <table>
                <thead>
                <tr>
                    <th><spring:message code="word.seminar.name" /></th>
                    <th><spring:message code="word.major.version" /></th>
                    <th><spring:message code="word.minor.version" /></th>
                    <th><spring:message code="word.mac.address" /></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="seminar" items="${seminarList}">
                    <tr>
                        <td>${seminar.subject}</td>
                        <td>${seminar.majorVer}</td>
                        <td>${seminar.minorVer}</td>
                        <td>${seminar.macAddr}</td>
                        <td>
                            <input type="button" value="<spring:message code="btn.delete" />" data-major-ver="${seminar.majorVer}" data-minor-ver="${seminar.minorVer}" data-mac-addr="${seminar.macAddr}" class="btn btn-danger btn-del-seminar" />
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        </form>
        </p>
    </div>
</div>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140400" /></b>
    </div>
    <div class="boxCon">
        <div class="listResearch">
            <form name="form1" id="form1" role="form">
                <input type="text" id="attdDate" name="attdDate" class="useDatepicker" placeholder="<spring:message code="word.attendance.date" />" value="${attdDate}">
                <select name="opt" id="opt" class="w150" is_element_select="true">
                    <option value="">=<spring:message code="word.search.option" />=</option>
                    <option value="phoneNumber" ${param.opt == 'phoneNumber' ? 'selected' : ''}><spring:message code="word.phone.number" /></option>
                    <option value="subject" ${param.opt == 'subject' ? 'selected' : ''}><spring:message code="word.seminar.name" /></option>
                </select>
                <input type="text" id="attendanceKeyword" name="keyword" class="w300" placeholder="<spring:message code="word.enter.search.term"/>" value="${param.keyword}">
                <input type="submit" value="<spring:message code="btn.search" />" class="btn-inline btn-search">
                <a href="list.do" class="btn-inline btn-refresh"><spring:message code="btn.init" /></a>
            </form>
        </div>
        <div class="tableTopBtn aright">
            <a href="#" class="btn-inline btn-excel"><spring:message code="word.export"/>(CSV)</a>
            <a href="#" class="btn-inline btn-focus" id="seminarManagerBtn"><spring:message code="word.seminar.manager"/></a>
        </div>
        <div class="list">
            <table onclick="sortColumn(event)">
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col style="width:70px;">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"><spring:message code="word.no"/></th>
                    <th scope="col"><spring:message code="word.seminar.name"/></th>
                    <th scope="col"><spring:message code="word.major.version"/></th>
                    <th scope="col"><spring:message code="word.minor.version"/></th>
                    <th scope="col"><spring:message code="word.phone.number"/></th>
                    <th scope="col"><spring:message code="word.device.info"/></th>
                    <th scope="col"><spring:message code="word.enter.leave"/></th>
                    <th scope="col"><spring:message code="word.date"/></th>
                    <th scope="col"><spring:message code="word.time"/></th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}" >
                    <tr>
                        <td>${list.logNum}</td>
                        <td>${list.subject}</td>
                        <td>${list.majorVer}</td>
                        <td>${list.minorVer}</td>
                        <td>${list.phoneNumber}</td>
                        <td>${list.deviceInfo}</td>
                        <td style="vertical-align: middle;">
                            <c:if test="${list.state eq 'E'}">
                                <spring:message code="word.enter" />
                            </c:if>
                            <c:if test="${list.state eq 'L'}">
                                <spring:message code="word.leave" />
                            </c:if>
                        </td>
                        <td>${list.attdDate}</td>
                        <td>
                            <jsp:useBean id="regDate" class="java.util.Date"/>
                            <jsp:setProperty name="regDate" property="time" value="${list.regDate}000"/>
                            <fmt:formatDate value="${regDate}" pattern="yyyy-MM-dd HH:mm:ss "/>
                        </td>
                        <td>
                            <input id="btnDelete" data-log-num="${list.logNum}" data-attd-date="${list.attdDate}" type="button" value="<spring:message code="btn.delete" />" />
                        </td>
                    </tr>
                </c:forEach>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="10" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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