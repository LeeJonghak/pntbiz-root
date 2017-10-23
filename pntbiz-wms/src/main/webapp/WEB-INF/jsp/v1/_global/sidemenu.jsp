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

<!-- Start: Sidebar -->
<aside id="sidebar_left" class="nano nano-light affix">
	<!-- Start: Sidebar Left Content -->
	<div class="sidebar-left-content nano-content">

		<header class="sidebar-header">
		</header>

		<!-- Start: Sidebar Menu -->
		<ul class="nav sidebar-menu">
			<c:if test="${authenticated}">
			<sec:authorize access="hasAnyRole('BF_DASHBOARD')">
			<li>
				<a href="/dashboard/main.do">
					<span class="glyphicons glyphicons-dashboard"></span>
					<span class="sidebar-title"><spring:message code="menu.000000" /></span>
				</a>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_SCANNER')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-router"></span>
					<span class="sidebar-title"><spring:message code="menu.110000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<li><a href="/scanner/list.do"><spring:message code="menu.110100" /></a></li>
					<li><a href="/scanner/deploy/info.do"><spring:message code="menu.110200" /></a></li>
					<li><a href="/scanner/server/list.do"><spring:message code="menu.110300" /></a></li>
					<li><a href="/scanner/deploy/list.do"><spring:message code="menu.110400" /></a></li>
				</ul>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_BEACON', 'BF_BEACON_STATE', 'BF_STAT')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="fa fa-rss"></span>
					<span class="sidebar-title"><spring:message code="menu.100000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<sec:authorize access="hasAnyRole('BF_BEACON')">
					<li><a href="/beacon/info/list.do"><spring:message code="menu.100100" /></a></li>
					<li><a href="/beacon/group/list.do"><spring:message code="menu.100300" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_BEACON_STATE', 'BF_STAT')">
					<li>
						<a href="#" class="accordion-toggle"><spring:message code="menu.100200" />
							<span class="caret"></span>
						</a>
						<ul class="nav sub-nav">
							<sec:authorize access="hasAnyRole('BF_BEACON_STATE')">
							<li><a href="/beacon/monitor/list.do"><spring:message code="menu.100201" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('BF_STAT')">
							<li><a href="/stat/beacon/monitor.do"><spring:message code="menu.100202" /></a></li>
							</sec:authorize>
						</ul>
					</li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_FLOOR', 'BF_BEACON_PLAN', 'BF_GEOFENCING_PLAN', 'BF_SCANNER_PLAN', 'BF_PRESENCE_SETMAP', 'BF_FLOOR_TREE')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="imoon imoon-map2"></span>
					<span class="sidebar-title"><spring:message code="menu.120000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<sec:authorize access="hasAnyRole('BF_FLOOR')">
						<c:if test="${empty mapType or mapType eq 'googlemaps'}">
							<li><a href="/map/floor/floor.do"><spring:message code="menu.120100" /></a></li>
						</c:if>
						<c:if test="${mapType eq 'openlayers'}">
							<li><a href="/map/floor/floor2.do"><spring:message code="menu.120100" /></a></li>
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
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-vector_path_all"></span>
					<span class="sidebar-title"><spring:message code="menu.160000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<sec:authorize access="hasAnyRole('BF_GEOFENCING')">
					<li><a href="/geofencing/info/list.do"><spring:message code="menu.160100" /></a></li>
					<li><a href="/geofencing/group/list.do"><spring:message code="menu.160200" /></a></li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT', 'BF_SERVICE_SEAT', 'BF_SERVICE_PUSH', 'BF_SERVICE_ATTENDANCE', 'BF_SERVICE_ATTENDANCE_SEMINAR')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-stopwatch"></span>
					<span class="sidebar-title"><spring:message code="menu.140000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT')">
					<li><a href="/event/event/list.do"><spring:message code="menu.140100" /></a></li>
					<li><a href="/event/type/list.do"><spring:message code="menu.140200" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE')">
					<li><a href="/service/attendance/list.do"><spring:message code="menu.140300" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE_SEMINAR')">
					<li><a href="/service/attendanceSeminar/list.do"><spring:message code="menu.140400" /></a></li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_CONTENTS')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-picture"></span>
					<span class="sidebar-title"><spring:message code="menu.130000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<li><a href="/contents/list.do"><spring:message code="menu.130100" /></a></li>
					<li><a href="/contents/mapping.do"><spring:message code="menu.130200" /></a></li>
					<!--
					<li><a href="/contents/info/list.do"><spring:message code="menu.130300" /></a></li>
					<li><a href="/contents/type/list.do"><spring:message code="menu.130300" /></a></li>
					-->
				</ul>
			</li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('BF_STAT', 'BF_TRACKING', 'BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-charts"></span>
					<span class="sidebar-title"> <spring:message code="menu.170000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<%--<li><a href="/tracking/main.do"><spring:message code="menu.170100" /></a></li>--%>
					<li><a href="/tracking/main2.do"><spring:message code="menu.170100" /></a></li>
					<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
					<li>
						<a href="#" class="accordion-toggle"><spring:message code="menu.170200" />
							<span class="caret"></span>
						</a>
						<ul class="nav sub-nav">
							<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER')">
							<%--<li><a href="/tracking/presence/scanner/target.do"><spring:message code="menu.170201" /></a></li>--%>
							<li><a href="/tracking/presence/scanner/target2.do"><spring:message code="menu.170201" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('BF_TRACKING_BEACON')">
							<%--<li><a href="/tracking/presence/beacon/target.do"><spring:message code="menu.170211" /></a></li>--%>
							<li><a href="/tracking/presence/beacon/target2.do"><spring:message code="menu.170211" /></a></li>
							</sec:authorize>
						</ul>
					</li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
					<li>
						<a href="#" class="accordion-toggle"><spring:message code="menu.170300" />
							<span class="caret"></span>
						</a>
						<ul class="nav sub-nav">
							<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER')">
							<li><a href="/tracking/presence/scanner/log.do"><spring:message code="menu.170301" /></a></li>
							<li><a href="/tracking/presence/scanner/gflog.do"><spring:message code="menu.170302" /></a></li>
							<li><a href="/tracking/presence/scanner/iolog.do"><spring:message code="menu.170303" /></a></li>
							<li><a href="/tracking/presence/scanner/floorlog.do"><spring:message code="menu.170305" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('BF_TRACKING_BEACON')">
							<li><a href="/tracking/presence/beacon/log.do"><spring:message code="menu.170311" /></a></li>
							</sec:authorize>
						</ul>
					</li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>

            <sec:authorize access="hasAnyRole('BT_SERVER_MONITORING', 'BT_SCANNER_MONITORING')">
            <li>
                <a class="accordion-toggle" href="#">
                    <span class="glyphicons glyphicons-search"></span>
                    <span class="sidebar-title"><spring:message code="menu.990000" /></span>
                </a>
                <ul class="nav sub-nav">
                    <sec:authorize access="hasAnyRole('BT_SERVER_MONITORING')">
                    <!--<li><a href="/monitoring/server/list.do"><spring:message code="menu.180100" /></a></li>-->
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('BT_SCANNER_MONITORING')">
                    <li><a href="/monitoring/scanner/list.do"><spring:message code="menu.180200" /></a></li>
                    </sec:authorize>
                </ul>
            </li>
            </sec:authorize>

			<sec:authorize access="hasAnyRole('BF_INFO_COMPANY', 'BF_INFO_CODE', 'BF_INFO_ADVERT', 'BF_OAUTH', 'BF_INFO_ACCOUNT', 'BF_INFO_INTERFACE')">
			<li>
				<a class="accordion-toggle" href="#">
					<span class="glyphicons glyphicons-settings"></span>
					<span class="sidebar-title"><spring:message code="menu.990000" /></span>
					<span class="caret"></span>
				</a>
				<ul class="nav sub-nav">
					<sec:authorize access="hasAnyRole('BF_INFO_COMPANY')">
					<li><a href="/info/company/mform.do"><spring:message code="menu.990100" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_INFO_CODE')">
					<li><a href="/info/code/list.do"><spring:message code="menu.990200" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_INFO_ADVERT')">
					<li><a href="/info/ac/list.do"><spring:message code="menu.990300" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_INFO_LOCATION')">
					<li><a href="/info/location/list.do"><spring:message code="menu.990400" /></a></li>
					<li><a href="/info/location/record.do"><spring:message code="menu.990500" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_OAUTH')">
					<li><a href="/oauth/client/list.do"><spring:message code="menu.990600" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_INFO_ACCOUNT')">
					<li><a href="/info/account/mform.do"><spring:message code="menu.990700" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('BF_INFO_INTERFACE')">
					<li><a href="/info/interface/list.do"><spring:message code="menu.990800" /></a></li>
					</sec:authorize>
				</ul>
			</li>
			</sec:authorize>
			</c:if>
			<!-- End: Sidebar Menu -->
		</ul>
		<!-- Start: Sidebar Collapse Button -->
		<div class="sidebar-toggle-mini">
			<a href="#"><span class="fa fa-sign-out"></span></a>
		</div>
		<!-- End: Sidebar Collapse Button -->
	</div>
	<!-- End: Sidebar Left Content -->
</aside>
<!-- End: Sidebar Left -->
