<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>

<html>
<head>
    <script type="text/javascript">
        window.layoutGnb = {mainmenu: '07', submenu: '01', location: ['<spring:message code="menu.130000"/>', '<spring:message code="menu.130100"/>']};
    </script>
    <c:choose>
        <c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'en'}">
            <script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents_en.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
        </c:when>
        <c:when test="${fn:substring(pageContext.response.locale, 0, 2) eq 'jp'}">
            <script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents_jp.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript" src="${viewProperty.staticUrl}/js/contents/contents.js?v=<spring:eval expression='@config[buildVersion]'/>"></script>
        </c:otherwise>
    </c:choose>
</head>
<body>
<div class="contentBox">
    <div class="title">
        <b><spring:message code="menu.130102" /></b>
        <a href="#" class="btnClose"><spring:message code="btn.close"/></a>
    </div>
    <div class="boxCon">
        <form name="form1" id="form1"  role="form">
        <div class="btnArea">
            <div class="fl-l">
                <a href="<c:url value="/contents/list.do"/>" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
            </div>
            <div class="fl-r">
                <input type="button" value="<spring:message code="btn.register"/>" class="contentsRegBtn btn-inline btn-regist">
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
                    <th scope="row"><spring:message code="word.contents.type" /></th>
                    <td>
                        <select name="conType" id="conType" class="w100p" is_element_select="true">
                            <option value="">=<spring:message code="word.contents.type" />=</option>
                            <c:forEach var="conCD" items="${conCD}">
                                <option value="${conCD.sCD}">
                                    <c:if test="${empty conCD.langCode}">${conCD.sName}</c:if>
                                    <c:if test="${not empty conCD.langCode}"><spring:message code="${conCD.langCode}"/></c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="word.contents.name" /></th>
                    <td><input type="text" id="conName" name="conName" value="" maxlength="25" class="w100p" placeholder="<spring:message code="word.contents.name" />"  /></td>
                </tr>
                <%-- 광고제공자
                <tr>
                    <th scope="row"><spring:message code="word.advertising.provider" /></th>
                    <td>
                        <input type="hidden" id="acNum" name="acNum" value="" />
                        <input type="text" id="acName" name="acName" value="" maxlength="25" class="form-control" placeholder="<spring:message code="word.advertising.provider"/>" data-provide="typeahead" data-items="4" />
                    </td>
                </tr>--%>
                <tr>
                    <th scope="row"><spring:message code="word.validity.date" /></th>
                    <td>
						<input type="hidden" id="sDate" name="sDate" data-target-prefix="start" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
						<input type="text" id="start-date" class="useDatepicker" value="">
						<select id="start-hour">
							<c:forEach var="hour" varStatus="i" begin="0" end="23" step="1">
								<option value="${hour}">${hour}<spring:message code="word.hour" /></option>
							</c:forEach>
						</select>
						<select id="start-minute">
							<c:forEach var="minute" varStatus="i" begin="0" end="59" step="1">
								<option value="${minute}">${minute}<spring:message code="word.minute" /></option>
							</c:forEach>
						</select>

						~

						<input type="hidden" id="eDate" name="eDate" data-target-prefix="end" data-format="YYYY-MM-DD HH:mm:ss" class="pnt-datetime-format" />
						<input type="text" id="end-date" class="useDatepicker" value="">
						<select id="end-hour">
							<c:forEach var="hour" varStatus="i" begin="0" end="23" step="1">
								<option value="${hour}">${hour}<spring:message code="word.hour" /></option>
							</c:forEach>
						</select>
						<select id="end-minute">
							<c:forEach var="minute" varStatus="i" begin="0" end="59" step="1">
								<option value="${minute}">${minute}<spring:message code="word.minute" /></option>
							</c:forEach>
						</select>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="word.visible.option" /></th>
                    <td>
                        <select name="expFlag" id="expFlag" class="w100p" is_element_select="true">
                            <option value="">=<spring:message code="word.visible.option" />=</option>
                            <c:forEach var="expFlag" items="${expFlag}">
                                <option value="${expFlag.sCD}">
                                    <c:if test="${empty expFlag.langCode}">${expFlag.sName}</c:if>
                                    <c:if test="${not empty expFlag.langCode}"><spring:message code="${expFlag.langCode}"/></c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th scope="row">RSSI</th>
                    <td><input type="text" id="rssi" name="rssi" value="" maxlength="5" class="w100p"  /></td>
                </tr>
                <tr id="form-img1">
                    <th scope="row" id="label-img1">imgSrc1</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" id="img-thumbnail1" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="imgSrc1" name="imgSrc1" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="1"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-img2">
                    <th scope="row" id="label-img2">imgSrc2</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" id="img-thumbnail2" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="imgSrc2" name="imgSrc2" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="2"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-img3">
                    <th scope="row" id="label-img3">imgSrc3</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" id="img-thumbnail3" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="imgSrc3" name="imgSrc3" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="3"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-img4">
                    <th scope="row" id="label-img4">imgSrc4</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" id="img-thumbnail4" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="imgSrc4" name="imgSrc4" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="4"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                 </tr>
                <tr id="form-img5">
                    <th scope="row" id="label-img5">imgSrc5</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" id="img-thumbnail5" class="img-thumbnail" alt="<spring:message code="word.image" />" width="150" height="100" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="imgSrc5" name="imgSrc5" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="img" num="5"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-text1">
                    <th scope="row" id="label-text1">text1</th>
                    <td>
                        <input type="text" id="text1" name="text1" class="w100p" placeholder="text1" />
                    </td>
                </tr>
                <tr id="form-text2">
                    <th scope="row" id="label-text2">text2</th>
                    <td>
                        <input type="text" id="text2" name="text2" class="w100p" placeholder="text2" />
                    </td>
                </tr>
                <tr id="form-text3">
                    <th scope="row" id="label-text3">text3</th>
                    <td>
                        <input type="text" id="text3" name="text3" class="form-control" placeholder="text3" />
                    </td>
                </tr>
                <tr id="form-text4">
                    <th scope="row" id="label-text4">text4</th>
                    <td>
                        <input type="text" id="text4" name="text4" class="w100p" placeholder="text4" />
                    </td>
                </tr>
                <tr id="form-text5">
                    <th scope="row" id="label-text5">text5</th>
                    <td>
                        <input type="text" id="text5" name="text5" class="w100p" placeholder="text5" />
                    </td>
                </tr>
                <tr id="form-sound1">
                    <th scope="row" id="label-sound1">text5</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc1}" width="100%" height="50" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="soundSrc1" name="soundSrc1" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="1"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-sound2">
                    <th scope="row" id="label-sound2">text5</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc2}" width="100%" height="50" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="soundSrc2" name="soundSrc2" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="2"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-sound3">
                    <th scope="row" id="label-sound3">text5</th>
                    <td>
                        <div class="fileupload fileupload-new admin-form" data-provides="fileupload">
                            <div class="fileupload-preview thumbnail mb20">
                                <img src="" class="img-thumbnail" alt="${contentsInfo.soundSrc3}" width="100%" height="50" />
                            </div>
                            <div class="row">
                                <div class="col-xs-6 pr5">
                                <span class="button btn-system btn-file btn-block">
                                    <span class="fileupload-new"><spring:message code="word.choose" /></span>
                                    <span class="fileupload-exists"><spring:message code="word.change" /></span>
                                    <input type="file" id="soundSrc3" name="soundSrc3" class="form-control">
                                </span>
                                </div>
                                <div class="col-xs-6 pr5">
                                    <button type="button" class="btn btn-warning btn-block contentsFileDelBtn" fileType="sound" num="3"><spring:message code="btn.delete" /></button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="form-url1">
                    <th scope="row" id="label-url1">text5</th>
                    <td>
                        <input type="text" id="url1" name="url1" class="form-control" placeholder="url1" />
                    </td>
                </tr>
                <tr id="form-url2">
                    <th scope="row" id="label-url2">text5</th>
                    <td>
                        <input type="text" id="url2" name="url2" class="form-control" placeholder="url2" />
                    </td>
                </tr>
                <tr id="form-url3">
                    <th scope="row" id="label-url3">text5</th>
                    <td>
                        <input type="text" id="url3" name="url3" class="form-control" placeholder="url3" />
                    </td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="word.description" /></th>
                    <td><textarea <%--name="wordlength"--%> id="conDesc" name="conDesc"  rows="10" class="w100p" data-limitByte="200" placeholder="<spring:message code="word.description" />" ></textarea></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="btnArea">
            <div class="fl-l">
                <a href="/contents/list.do" class="btn-inline btn-list"><spring:message code="btn.list"/></a>
            </div>
            <div class="fl-r">
                <input type="button" value="<spring:message code="btn.register"/>" class="contentsRegBtn btn-inline btn-regist">
            </div>
        </div>
        </form>
    </div>
</div>

</body>
</html>