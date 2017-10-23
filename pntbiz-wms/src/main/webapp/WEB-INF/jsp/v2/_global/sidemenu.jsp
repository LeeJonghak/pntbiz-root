<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sform" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/WEB-INF/taglib/sitemesh-page.tld" prefix="page" %>

<sec:authorize var="authenticated" access="isAuthenticated()" />
<spring:eval expression="@config['map.type']" var="mapType"/>

<ul id="gnb">
	<li id="gnb-menu-01" class="icon1"><a href="#"><spring:message code="menu.000000" /></a>
		<ul>
			<li id="gnb-menu-01-01"><a href="<c:url value="/dashboard/main.do"/>"><spring:message code="menu.000100" /></a></li>
			<li id="gnb-menu-01-02"><a href="<c:url value="/dashboard/monitor.do"/>"><spring:message code="menu.000101" /></a></li>
		</ul>
	</li>
	<sec:authorize access="hasAnyRole('BF_SCANNER')">
		<li id="gnb-menu-02" class="icon2"><a href="#"><spring:message code="menu.110000" /></a>
			<ul>
				<li id="gnb-menu-02-01"><a href="<c:url value="/scanner/list.do" />"><spring:message code="menu.110000" /></a></li>
				<li id="gnb-menu-02-02"><a href="<c:url value="/scanner/deploy/info.do" />"><spring:message code="menu.110200" /></a></li>
				<li id="gnb-menu-02-03"><a href="<c:url value="/scanner/server/list.do" />"><spring:message code="menu.110300" /></a></li>
				<li id="gnb-menu-02-04"><a href="<c:url value="/scanner/deploy/list.do" />"><spring:message code="menu.110400" /></a></li>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_BEACON', 'BF_BEACON_STATE', 'BF_STAT')">
		<li id="gnb-menu-03" class="icon3"><a href="#"><spring:message code="menu.100000"/></a>
			<ul>
				<sec:authorize access="hasAnyRole('BF_BEACON')">
					<li id="gnb-menu-03-01"><a href="<c:url value="/beacon/info/list.do"/>"><spring:message code="menu.100100"/></a></li>
					<li id="gnb-menu-03-02"><a href="<c:url value="/beacon/group/list.do"/>"><spring:message code="menu.100300"/></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_BEACON_STATE', 'BF_STAT')">

					<sec:authorize access="hasAnyRole('BF_BEACON_STATE')">
						<li id="gnb-menu-03-03"><a href="<c:url value="/beacon/monitor/list.do"/>"><spring:message code="menu.100201" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_STAT')">
						<li id="gnb-menu-03-04"><a href="<c:url value="/stat/beacon/monitor.do"/>"><spring:message code="menu.100202" /></a></li>
					</sec:authorize>
				</sec:authorize>
				<%--<li><a href="#"><spring:message code="menu.100200" /></a>
					<ul>
						<li><a href="<c:url value="/beacon/monitor/list.do"/>"><spring:message code="menu.100201" /></a></li>
						<li><a href="<c:url value="/stat/beacon/monitor.do"/>"><spring:message code="menu.100202" /></a></li>
					</ul>
				</li>--%>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_FLOOR', 'BF_BEACON_PLAN', 'BF_GEOFENCING_PLAN', 'BF_SCANNER_PLAN', 'BF_PRESENCE_SETMAP', 'BF_FLOOR_TREE')">
		<li id="gnb-menu-04" class="icon4"><a href="#"><spring:message code="menu.120000" /></a>
			<ul>
				<sec:authorize access="hasAnyRole('BF_FLOOR')">
				<c:if test="${empty mapType or mapType eq 'googlemaps'}">
					<li id="gnb-menu-04-01"><a href="/map/floor/floor.do"><spring:message code="menu.120100" /></a></li>
				</c:if>
				<c:if test="${mapType eq 'openlayers'}">
					<li id="gnb-menu-04-02"><a href="/map/floor/floor2.do"><spring:message code="menu.120100" /></a></li>
				</c:if>
			</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_BEACON_PLAN')">
					<c:if test="${empty mapType or mapType eq 'googlemaps'}">
						<li><a href="/map/bplan.do"><spring:message code="menu.120200" /></a></li>
					</c:if>
					<c:if test="${mapType eq 'openlayers'}">
						<li><a href="/maptool/map.do?enable=beacon,node.beacon,geofence,area&show=beacon&title.code=menu.120200"><spring:message code="menu.120200" /></a></li>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_GEOFENCING_PLAN')">
					<c:if test="${empty mapType or mapType eq 'googlemaps'}">
						<li><a href="/map/gplan.do"><spring:message code="menu.120300" /></a></li>
					</c:if>
					<c:if test="${mapType eq 'openlayers'}">
						<li><a href="/maptool/map.do?enable=beacon,geofence,area&show=geofence&title.code=menu.120300"><spring:message code="menu.120300" /></a></li>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_SCANNER_PLAN')">
					<c:if test="${empty mapType or mapType eq 'googlemaps'}">
						<li><a href="/map/splan.do"><spring:message code="menu.120400" /></a></li>
					</c:if>
					<c:if test="${mapType eq 'openlayers'}">
						<li><a href="/maptool/map.do?enable=beacon,scanner,node.scanner,geofence,area&show=scanner&title.code=menu.120400"><spring:message code="menu.120400" /></a></li>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_BEACON_STATE')">
					<c:if test="${empty mapType or mapType eq 'googlemaps'}">
						<li><a href="/map/bsplan.do"><spring:message code="menu.120500" /></a></li>
					</c:if>
					<c:if test="${mapType eq 'openlayers'}">
						<li><a href="/maptool/map.do?enable=beacon,geofence,area,beacon.status&show=beacon&title.code=menu.120500"><spring:message code="menu.120500" /></a></li>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_AREA_PLAN')">
					<c:if test="${empty mapType or mapType eq 'googlemaps'}">
						<li><a href="/map/dplan.do"><spring:message code="menu.120800" /></a></li>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_PRESENCE_SETMAP')">
					<li><a href="/tracking/presence/setmap.do"><spring:message code="menu.120600" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_FLOOR_TREE')">
					<li><a href="/map/floorCode/list.do"><spring:message code="menu.120700" /></a></li>
				</sec:authorize>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_GEOFENCING')">
		<li id="gnb-menu-05" class="icon5"><a href="#"><spring:message code="menu.160000" /></a>
			<ul>
				<li id="gnb-menu-05-01"><a href="/geofencing/info/list.do"><spring:message code="menu.160100" /></a></li>
				<li id="gnb-menu-05-02"><a href="/geofencing/group/list.do"><spring:message code="menu.160200" /></a></li>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT', 'BF_SERVICE_SEAT', 'BF_SERVICE_PUSH', 'BF_SERVICE_ATTENDANCE', 'BF_SERVICE_ATTENDANCE_SEMINAR')">
		<li id="gnb-menu-06" class="icon6"><a href="#"><spring:message code="menu.140000" /></a>
			<ul>
				<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT')">
					<li id="gnb-menu-06-01"><a href="/event/event/list.do"><spring:message code="menu.140100" /></a></li>
					<li id="gnb-menu-06-02"><a href="/event/type/list.do"><spring:message code="menu.140200" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE')">
					<li id="gnb-menu-06-03"><a href="/service/attendance/list.do"><spring:message code="menu.140300" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE_SEMINAR')">
					<li id="gnb-menu-06-04"><a href="/service/attendanceSeminar/list.do"><spring:message code="menu.140400" /></a></li>
				</sec:authorize>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_CONTENTS')">
		<li id="gnb-menu-07" class="icon7"><a href="#"><spring:message code="menu.130000" /></a>
			<ul>
				<li id="gnb-menu-07-01"><a href="<c:url value="/contents/list.do"/>"><spring:message code="menu.130100" /></a></li>
				<li id="gnb-menu-07-02"><a href="<c:url value="/contents/mapping.do"/>"><spring:message code="menu.130200" /></a></li>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_STAT', 'BF_TRACKING', 'BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
		<li id="gnb-menu-08" class="icon8"><a href="#"><spring:message code="menu.170000" /></a>
			<ul>
				<li id="gnb-menu-08-01"><a href="/tracking/main2.do"><spring:message code="menu.170100" /></a></li>
				<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
					<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER')">
						<%--<li><a href="/tracking/presence/scanner/target.do"><spring:message code="menu.170201" /></a></li>--%>
						<li id="gnb-menu-08-02"><a href="/tracking/presence/scanner/target2.do"><spring:message code="menu.170201" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_TRACKING_BEACON')">
						<%--<li><a href="/tracking/presence/beacon/target.do"><spring:message code="menu.170211" /></a></li>--%>
						<li id="gnb-menu-08-03"><a href="/tracking/presence/beacon/target2.do"><spring:message code="menu.170211" /></a></li>
					</sec:authorize>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
					<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER')">
						<li id="gnb-menu-08-04"><a href="/tracking/presence/scanner/log.do"><spring:message code="menu.170301" /></a></li>
						<li id="gnb-menu-08-05"><a href="/tracking/presence/scanner/gflog.do"><spring:message code="menu.170302" /></a></li>
						<li id="gnb-menu-08-06"><a href="/tracking/presence/scanner/iolog.do"><spring:message code="menu.170303" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_TRACKING_BEACON')">
						<li id="gnb-menu-08-07"><a href="/tracking/presence/beacon/log.do"><spring:message code="menu.170311" /></a></li>
					</sec:authorize>
				</sec:authorize>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BT_SERVER_MONITORING', 'BT_SCANNER_MONITORING')">
		<li id="gnb-menu-09" class="icon9"><a href="#"><spring:message code="menu.180000" /></a>
			<ul>
				<sec:authorize access="hasAnyRole('BT_SERVER_MONITORING')">
					<!--<li><a href="/monitoring/server/list.do"><spring:message code="menu.180100" /></a></li>-->
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BT_SCANNER_MONITORING')">
					<li id="gnb-menu-09-02"><a href="/monitoring/scanner/list.do"><spring:message code="menu.180200" /></a></li>
				</sec:authorize>
			</ul>
		</li>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('BF_INFO_COMPANY', 'BF_INFO_CODE', 'BF_INFO_ADVERT', 'BF_OAUTH', 'BF_INFO_ACCOUNT')">
		<li id="gnb-menu-10" class="icon10"><a href="#"><spring:message code="menu.990000" /></a>
			<ul>
				<sec:authorize access="hasAnyRole('BF_INFO_COMPANY')">
					<li id="gnb-menu-10-01"><a href="/info/company/mform.do"><spring:message code="menu.990100" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_INFO_CODE')">
					<li id="gnb-menu-10-02"><a href="/info/code/list.do"><spring:message code="menu.990200" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_INFO_ADVERT')">
					<li id="gnb-menu-10-03"><a href="/info/ac/list.do"><spring:message code="menu.990300" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_INFO_LOCATION')">
					<li id="gnb-menu-10-04"><a href="/info/location/list.do"><spring:message code="menu.990400" /></a></li>
					<li id="gnb-menu-10-05"><a href="/info/location/record.do"><spring:message code="menu.990500" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_OAUTH')">
					<li id="gnb-menu-10-06"><a href="/oauth/client/list.do"><spring:message code="menu.990600" /></a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('BF_INFO_ACCOUNT')">
					<li id="gnb-menu-10-07"><a href="/info/account/mform.do"><spring:message code="menu.990700" /></a></li>
				</sec:authorize>
			</ul>
		</li>
	</sec:authorize>
	<%--<li><a href="#">레퍼런스</a>
		<ul>
			<li><a href="./example/form.html">폼</a></li>
			<li><a href="./example/list.html">목록화면</a></li>
			<li><a href="./example/view.html">수정/상세화면</a></li>
			<li><a href="./example/button.html">버튼</a></li>
			<li><a href="./example/popup.html">팝업</a></li>
		</ul>
	</li>--%>
</ul>