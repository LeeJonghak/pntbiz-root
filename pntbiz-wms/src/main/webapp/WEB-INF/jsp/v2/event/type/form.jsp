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
    <script type="text/javascript" src="${viewProperty.staticUrl}/js/event/eventType.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.140202" /></b>
        <a href="#" class="btnClose"><spring:message code="btn.close"/></a>
    </div>
    <div class="boxCon">
        <form name="form1" id="form1"  role="form">
            <div class="btnArea">
                <div class="fl-l">
                    <input type="button" id="listBtn1" value="<spring:message code="btn.list"/>" class=" btn-inline btn-list">
                </div>
                <div class="fl-r">
                    <input type="button" id="regBtn1" value="<spring:message code="btn.register"/>" class="btn-inline btn-regist">
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
                        <th scope="row"><spring:message code="word.event.type.code" /></th>
                        <td>
                            <input type="text" id="evtTypeCode" name="evtTypeCode" class="w100p" value="" required="required" maxlength="10" placeholder="<spring:message code="word.event.type.code" />"  />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><spring:message code="word.event.type" /></th>
                        <td>
                            <input type="text" id="evtTypeName" name="evtTypeName" class="w100p" value="" required="required" maxlength="50" placeholder="<spring:message code="word.event.type" />"  />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><spring:message code="word.event.condition"/></th>
                        <td>
                            <ul id="dynamic-column">
                                <li>
                                    <select name="evtColType" class="slct-col-type w100" is_element_select="true">
                                        <option value="">=<spring:message code="word.column.type"/>=</option>
                                        <c:forEach items="${columnCodeList}" var="code">
                                            <option value="${code.key}">${code.value}</option>
                                        </c:forEach>
                                    </select>
                                    <input type="text" name="evtColID" class="txt-col-id w150" placeholder="<spring:message code="word.column.id"/>">
                                    <input type="text" name="evtColName" class="txt-col-name w150" placeholder="<spring:message code="word.column.name"/>">
                                    <input type="text" name="items" class="txt-col-enum w150" value="" placeholder="<spring:message code="word.selectable.item"/>">
                                    <input type="button" id="addColumn" class="addColumn btn-inline btn-focus" value="<spring:message code="word.column.register" />">
                                </li>
                                <%--<div style="display: none;">
                                    <li id="column-source">
                                        <select class="slct-col-type w100" is_element_select="true">
                                            <option value=""><spring:message code="word.column.type"/></option>
                                            <c:forEach items="${columnCodeList}" var="code">
                                                <option value="${code.key}">${code.value}</option>
                                            </c:forEach>
                                        </select>
                                        <input type="text" class="txt-col-id w150" placeholder="<spring:message code="word.column.id"/>">
                                        <input type="text" class="txt-col-name w150" placeholder="<spring:message code="word.column.name"/>">
                                        <input type="text" class="txt-col-enum w150" value="" placeholder="<spring:message code="word.selectable.item"/>">
                                        <input type="button" class="btn-col-remove btn-inline btn-focus" value="<spring:message code="btn.delete"/>">
                                    </li>
                                </div>--%>
                            </ul>
                            <p>
                                *범위 항목일 경우 컬럼명에 구분자(,)를 사용하여 시작 컬럼명과 종료 컬럼명을 구분하여 주세요 예) startDate,endDate<br />
                                *단일선택항목, 다중선택항목일 경우 선택가능 항목에 구분자(,)를 사용하여 여러 항목을 입력해주세요. 예) 월,화,수,목,금,토,일<br />
                                *구분자(,)를 사용할 경우 공백없이 입력해 주세요.
                            </p>
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
                    <input type="button" id="regBtn2" value="<spring:message code="btn.register"/>" class="btn-inline btn-regist">
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>