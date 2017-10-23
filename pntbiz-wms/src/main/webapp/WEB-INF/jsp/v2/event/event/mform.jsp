<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<html>
<head>
    <script type="text/javascript">
        window.layoutGnb = {mainmenu: '06', submenu: '01', location: ['<spring:message code="menu.140000"/>', '<spring:message code="menu.140100"/>']};
    </script>
    <script type="text/javascript">
        var elementHandler = new ElementHandler('<c:url value="/"/>');
        $(document).ready( function() {
            elementHandler.init();
        });
    </script>
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/event/event.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140103" /></b>
        <a href="#" class="btnClose"><spring:message code="btn.close"/></a>
    </div>
    <div class="boxCon">
        <form name="form1" id="form1"  role="form">
            <input type="hidden" name="evtNum" value="${eventInfo.evtNum}" />
            <div class="btnArea">
                <div class="fl-l">
                    <input type="button" id="listBtn1" value="<spring:message code="btn.list"/>" class=" btn-inline btn-list">
                </div>
                <div class="fl-r">
                    <input type="button" id="modBtn1" value="<spring:message code="btn.modify"/>" class="btn-inline btn-regist">
                    <input type="button" id="delBtn1" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete">
                </div>
            </div>
            <div class="view">
                <table>
                    <colgroup>
                        <col width="18%">
                        <col width="">
                    </colgroup>
                    <tbody>
                    <tr>
                        <th scope="row"><spring:message code="word.event.name" /></th>
                        <td>
                            <input type="text" id="evtName" name="evtName" class="w100p" value="${eventInfo.evtName}" required="required" maxlength="50" placeholder="<spring:message code="word.event.name" />"  />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><spring:message code="word.event.description" /></th>
                        <td>
                            <input type="text" id="evtDesc" name="evtDesc" class="w100p" value="${eventInfo.evtDesc}" maxlength="200" placeholder="<spring:message code="word.event.description" />"  />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><spring:message code="word.event.type"/></th>
                        <td>
                            <select id="evtTypeCode" name="evtTypeCode" class="w100p" required="required" class="form-control">
                                <option value="">=<spring:message code="word.event.type.name" />=</option>
                                <c:forEach items="${eventTypeList}" var="eventType">
                                    <option value="${eventType.evtTypeCode}" <c:if test="${eventType.evtTypeCode eq eventInfo.evtTypeCode}">selected="selected"</c:if>>${eventType.evtTypeName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><spring:message code="word.event.condition"/></th>
                        <td id="ajax-condition">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="btnArea">
                <div class="fl-l">
                    <input type="button" id="listBtn2" value="<spring:message code="btn.list"/>" class="btn-inline btn-list">
                </div>
                <div class="fl-r">
                    <input type="button" id="modBtn2" value="<spring:message code="btn.modify"/>" class="btn-inline btn-regist">
                    <input type="button" id="delBtn2" value="<spring:message code="btn.delete"/>" class="btn-inline btn-delete">
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>