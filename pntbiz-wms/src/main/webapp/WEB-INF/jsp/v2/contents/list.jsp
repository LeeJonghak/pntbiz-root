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
        window.layoutGnb = {mainmenu: '07', submenu: '01', location: ['<spring:message code="menu.130000"/>', '<spring:message code="menu.130100"/>']};
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.130101" /></b>
    </div>
    <div class="boxCon">
        <div class="listResearch">
            <form name="form1" id="form1" role="form">
                <select name="conType" id="conType" class="w150" is_element_select="true">
                    <option value="">=<spring:message code="word.contents.type" />=</option>
                    <c:forEach var="conCD" items="${conCD}">
                        <option value="${conCD.sCD}" <c:if test="${param.conType eq conCD.sCD}">selected</c:if>>
                            <c:if test="${empty conCD.langCode}">${conCD.sName}</c:if>
                            <c:if test="${not empty conCD.langCode}"><spring:message code="${conCD.langCode}"/></c:if>
                        </option>
                    </c:forEach>
                </select>
                <select name="opt" id="opt" class="w100"  is_element_select="true">
                    <option value="">=<spring:message code="word.search.option" />=</option>
                    <option value="conName" ${param.opt == 'conName' ? 'selected' : ''}><spring:message code="word.contents.name" /></option>
                </select>
                <input type="text" placeholder="<spring:message code="word.enter.search.term"/>" id="contentsKeyword" name="keyword" class="w300" value="${param.keyword}">
                <input type="submit" name="" value="<spring:message code="btn.search" />" class="btn-inline btn-search">
                <a href="list.do" class="btn-inline btn-refresh"><spring:message code="word.reset"/></a>
            </form>
        </div>
        <div class="tableTopBtn aright">
            <select name="selectBoxMapping" id="selectBoxMapping" class="w100"  is_element_select="true">
                <option value="">=<spring:message code="word.mapping.type" />=</option>
                <option value="BC"><spring:message code="word.beacon" /></option>
                <option value="BCG"><spring:message code="word.beacon.group" /></option>
                <option value="GF"><spring:message code="word.geofencing" /></option>
                <option value="GFG"><spring:message code="word.geofencing.group" /></option>
            </select>
            <select name="fcEventType" id="fcEventType" class="w100" style="display: none;" >
                <option value="">=<spring:message code="word.sub.mapping.type" />=</option>
                <option value="ENTER">ENTER</option>
                <option value="STAY">STAY</option>
                <option value="LEAVE">LEAVE</option>
            </select>

            <div class="selectUlWrap w150">
                <b><spring:message code="word.event.no" /></b>
                <ul id="multiselect1" class="selectUl">
                    <li><a href="#" data-value="0" class=""><spring:message code="word.event.no" /></a></li>
                    <li class="title">Event List</li>
                    <c:forEach var="eventList" items="${eventList}">
                        <li><a href="#" data-value="${eventList.evtNum}" class=""><spring:message code="${eventList.evtName}"/></a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="selectUlWrap w150">
                <b>All Selected</b>
                <ul id="multiselect4" class="selectUl">
                    <li><label class='checkBox'><input type='checkbox' id="checkAll_target" value='' data-class="targetCheckbox">All Selected</label></li>
                    <li class="title">Reference List</li>
                    <span id="targetlist"></span>
                </ul>
                <input type="hidden" name="select3" value="">
            </div>
            <input type="button" class="btn-inline btn-focus" id="contentsMappingBtn" value="<spring:message code="btn.mapping" />">
            <input type="button" class="btn-inline btn-regist" id="contentsFormBtn" value="<spring:message code="btn.register" />">
        </div>
        <div class="list rollover">
            <table data-clickUrl="mform.do">
                <colgroup>
                    <!--
                    <col style="width:100%;">
                    <col style="width:100px;">
                    <col class="w100">
                    -->
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"><input type="checkbox" name="" id="checkAll_contents" value="" data-class="contentCheckBox"></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.contents.type" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.contents.name" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.advertising.provider" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.register" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.validity.date" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}">
                <tr data-param="conNum=${list.conNum}&page=${page}&opt=${param.opt}&keyword=${param.keyword}&conType=${param.conType}">
                    <td class="notClick"><input type="checkbox" value="${list.conNum}" id="checkbox" name="checkbox" class="contentCheckBox" /></td>
                    <td>
                        <c:if test="${empty list.langCode}">${list.conTypeText}</c:if>
                        <c:if test="${not empty list.langCode}"><spring:message code="${list.langCode}"/></c:if>
                    </td>
                    <td>${list.conName}</td>
                    <td><c:out value="${list.acName}" default="-"/></td>
                    <td><spring:message code="word.register"/></td>
                    <td>${list.sDateText}<br/>${list.eDateText}</td>
                </tr>
                </c:forEach>
                <c:choose>
                    <c:when test="${cnt == 0}">
                        <tr>
                            <td colspan="7" height="150" align="center"><spring:message code="message.search.notmatch" /></td>
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