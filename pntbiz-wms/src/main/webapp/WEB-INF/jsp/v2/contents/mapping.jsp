<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/dateutil.tld" prefix="dateutil" %>

<html>
<head>
    <script type="text/javascript">
        window.layoutGnb = {mainmenu: '07', submenu: '02', location: ['콘텐츠', '콘텐츠 매핑 관리']};
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contentsMapping.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.130200" /></b>
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
                <select name="refType" id="refType" class="form-control" is_element_select="true">
                    <option value="">=<spring:message code="word.mapping.type" />=</option>
                    <c:forEach var="refCD" items="${refCD}">
                        <option value="${refCD.sCD}" <c:if test="${param.refType eq refCD.sCD}">selected</c:if>>
                            <c:if test="${empty refCD.langCode}">${refCD.sName}</c:if>
                            <c:if test="${not empty refCD.langCode}"><spring:message code="${refCD.langCode}"/></c:if>
                        </option>
                    </c:forEach>
                    <c:forEach var="refCD2" items="${refCD2}">
                        <option value="${refCD2.sCD}" <c:if test="${param.refType eq refCD2.sCD}">selected</c:if>>
                            <c:if test="${empty refCD2.langCode}">${refCD2.sName}</c:if>
                            <c:if test="${not empty refCD2.langCode}"><spring:message code="${refCD2.langCode}"/></c:if>
                        </option>
                    </c:forEach>
                </select>
                <select name="refSubType" id="refSubType" class="form-control" is_element_select="true">
                    <option value="">=<spring:message code="word.sub.mapping.type" />=</option>
                    <c:forEach var="refSubCD" items="${refSubCD}">
                        <option value="${refSubCD.sCD}" <c:if test="${param.refSubType eq refSubCD.sCD}">selected</c:if>>
                            <c:if test="${empty refSubCD.langCode}">${refSubCD.sName}</c:if>
                            <c:if test="${not empty refSubCD.langCode}"><spring:message code="${refSubCD.langCode}"/></c:if>
                        </option>
                    </c:forEach>
                </select>
                <select name="opt" id="opt" class="w100" is_element_select="true">
                    <option value="">=<spring:message code="word.search.option" />=</option>
                    <option value="conName" ${param.opt == 'conName' ? 'selected' : ''}><spring:message code="word.contents.name" /></option>
                    <option value="evtName" ${param.opt == 'evtName' ? 'selected' : ''}><spring:message code="word.event.name" /></option>
                </select>
                <input type="text" placeholder="" id="contentsMappingKeyword" name="keyword" class="w300" value="${param.keyword}">
                <input type="submit" name="" value="<spring:message code="btn.search" />" class="btn-inline btn-search">
                <a href="mapping.do" class="btn-inline btn-refresh"><spring:message code="word.reset"/></a>
            </form>
        </div>
        <div class="tableTopBtn aright">
            <input type="button" class="btn-inline btn-focus" id="eventMappingDelBtn" value="<spring:message code="word.event.delete" />">
            <input type="button" class="btn-inline btn-regist" id="contentsMappingDelBtn" value="<spring:message code="word.mapping.delete" />">
        </div>
        <div class="list rollover">
            <table>
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
                    <th scope="col"><input type="checkbox" name="" id="checkAll" value=""></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.contents"/> <spring:message code="word.no"/></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.mapping.type" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.reference" /> <spring:message code="word.no"/></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.sub.mapping.type" /></th>
                    <c:if test="${not empty param.refType}">
                        <th onclick="sortColumn(event)"><spring:message code="word.reference.information"/></th>
                    </c:if>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.contents.type" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.contents.name" /></th>
                    <th scope="col" onclick="sortColumn(event)"><spring:message code="word.event.name" /></th>
                    <th scope="col" onclick="sortColumn(event)" class="hidden-xs"><spring:message code="word.register" /></th>
                    <th scope="col" onclick="sortColumn(event)"class="hidden-xs"><spring:message code="word.regdate" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${list}">
                <tr data-param="">
                    <td><input type="checkbox" value="${list.conNum}|${list.refType}|${list.refNum}|${list.refSubTypeText}" id="checkbox" name="checkbox" /></td>
                    <td><a href="/contents/mform.do?conNum=${list.conNum}">${list.conNum}</a></td>
                    <td><spring:message code="${list.refTypeLangCode}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${list.refType eq 'BC'}">
                                <a href="/beacon/info/mform.do?beaconNum=${list.refNum}">${list.refNum}</a>
                            </c:when>
                            <c:when test="${list.refType eq 'GF'}">
                                <a href="/geofencing/info/mform.do?fcNum=${list.refNum}">${list.refNum}</a>
                            </c:when>
                            <c:when test="${list.refType eq 'BCG'}">
                                <a href="/beacon/group/mform.do?beaconGroupNum=${list.refNum}">${list.refNum}</a>
                            </c:when>
                            <c:when test="${list.refType eq 'GFG'}">
                                <a href="/geofencing/group/mform.do?fcGroupNum=${list.refNum}">${list.refNum}</a>
                            </c:when>
                            <c:otherwise>
                                <span class="label bg-info">${list.refNum}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${list.refSubTypeText}</td>
                    <c:if test="${not empty param.refType}">
                        <td>${list.refName}</td>
                    </c:if>
                    <td><spring:message code="${list.conTypeLangCode}"/></td>
                    <td>${list.conName}</td>
                    <td>${list.evtName}</td>
                    <td>${list.userID}</td>
                    <td>${dateutil:timestamp2str(list.regDate,'yyyy-MM-dd HH:mm:ss')}</td>
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
            <span class="total">총 카운트 ${cnt}</span>
            <div class="original">
                ${pagination}
            </div>
        </div>
    </div>
</div>

</body>
</html>