<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<sec:authorize var="authenticated" access="isAuthenticated()" />
<c:if test="${authenticated}"><sec:authentication var="loginCompanyNumber" property="principal.companyNumber" /></c:if>

<nav class="navbar navbar-default" role="navigation">
	<div class="navbar-header">
		<!-- <a class="navbar-brand" href="#">Pntbiz WMS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>  -->
		<c:choose>
			<c:when test="${pageContext.request.serverName eq 'wms.idatabank.com'}">
				<a href="/"><img src="<spring:eval expression='@config[staticURL]'/>/images/gnb/logo_databank.png" width="120" height="45" class="logo" /></a>
			</c:when>
			<c:when test="${pageContext.request.serverName eq 'admin.indoorplus.co.kr'}">
				<a href="/"><img src="<spring:eval expression='@config[staticURL]'/>/images/gnb/logo_indoorplus.png" width="120" height="45" class="logo" /></a>
			</c:when>
			<c:otherwise>
				<a href="/"><img src="<spring:eval expression='@config[staticURL]'/>/images/gnb/logo.png" class="logo" /></a>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<sec:authorize access="hasAnyRole('BF_ADMIN_COMPANY')">
				<li><a href="/admin/company/list.do">업체</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_ADMIN_CODE')">
				<li><a href="/admin/code/list.do">코드</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_ADMIN_AUTHPACKAGE')">
				<li><a href="/admin/authpackage/list.do">인증</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_ADMIN_CALIBRATION')">
				<li><a href="/admin/calibration/list.do">캘리브레이션</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_ADMIN_LOG')">
				<li><a href="/admin/log/list.do">로그</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_ADMIN_LOGIN_ACCOUNT')">
				<li class="dropdown">
					<a href="/admin/login/account/list.do" class="dropdown-toggle" data-toggle="dropdown">계정 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="/admin/login/account/list.do">계정 관리</a></li>
						<li><a href="/admin/login/role/list.do">역할 관리</a></li>
						<li><a href="/admin/login/authcode/list.do">권한 관리</a></li>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_BEACON', 'BF_BEACON_STATE', 'BF_GEOFENCING')">
				<li class="dropdown">
					<a href="/beacon/beacon/list.do" class="dropdown-toggle" data-toggle="dropdown">비콘 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasAnyRole('BF_BEACON')">
						<li><a href="/beacon/beacon/list.do">비콘 관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_BEACON_STATE')">
						<li><a href="/beacon/monitor/list.do">비콘 관제 목록</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_GEOFENCING')">
						<li><a href="/beacon/geofencing/list.do">지오펜싱 관리</a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_SCANNER')">
				<li class="dropdown">
					<a href="/map/floor.do" class="dropdown-toggle" data-toggle="dropdown">스캐너 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasAnyRole('BF_SCANNER')">
						<li><a href="/scanner/list.do">스캐너 관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SCANNER')">
						<li><a href="/scanner/deploy/info.do">스캐너정보 배포</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SCANNER')">
						<li><a href="/scanner/server/list.do">스캐너 서버 관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SCANNER')">
						<li><a href="/scanner/deploy/list.do">스캐너정보 배포 관리 </a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_FLOOR', 'BF_BEACON_PLAN', 'BF_GEOFENCING_PLAN', 'BF_SCANNER_PLAN')">
				<li class="dropdown">
					<a href="/map/floor.do" class="dropdown-toggle" data-toggle="dropdown">맵 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasAnyRole('BF_FLOOR')">
						<li><a href="/map/floor/floor.do">층 관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_BEACON_PLAN')">
						<li><a href="/map/bplan.do">비콘 배치도</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_GEOFENCING_PLAN')">
						<li><a href="/map/gplan.do">지오펜싱 배치도</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SCANNER_PLAN')">
						<li><a href="/map/splan.do">스캐너 배치도</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_BEACON_STATE')">
							<li><a href="/map/bsplan.do">비콘관제 배치도</a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_CONTENTS')">
				<li class="dropdown">
					<a href="/contents/list.do" class="dropdown-toggle" data-toggle="dropdown">콘텐츠 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="/contents/list.do">콘텐츠 관리</a></li>
						<li><a href="/contents/mapping.do">콘텐츠 매핑 관리</a></li>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT', 'BF_SERVICE_SEAT', 'BF_SERVICE_PUSH', 'BF_SERVICE_ATTENDANCE', 'BF_SERVICE_ATTENDANCE_SEMINAR')">
				<li class="dropdown">
					<a href="/service/event/list.do" class="dropdown-toggle" data-toggle="dropdown">서비스 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<%--<sec:authorize access="hasAnyRole('BF_SERVICE_EVENT')">
						<li><a href="/service/event/list.do">이벤트 관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SERVICE_SEAT')">
						<li><a href="/service/seat/list.do">좌석업그레이드 관리</a></li>
						</sec:authorize>
						<li><a href="/service/beacon/list.do">비콘푸쉬이벤트관리</a></li>
						<sec:authorize access="hasAnyRole('BF_SERVICE_PUSH')">
						<li><a href="/service/push/info/list.do">푸쉬발송대기목록</a></li>
						<li><a href="/service/push/rule/list.do">푸쉬발송결과관리</a></li>
						</sec:authorize>--%>
                        <sec:authorize access="hasAnyRole('BF_SERVICE_EVENT')">
                            <li><a href="/event/event/list.do">이벤트 관리</a></li>
                            <li><a href="/event/type/list.do">이벤트 유형 관리</a></li>
                        </sec:authorize>
                        <sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE')">
                            <li><a href="/service/attendance/list.do">출석체크</a></li>
                        </sec:authorize>
						<sec:authorize access="hasAnyRole('BF_SERVICE_ATTENDANCE_SEMINAR')">
							<li><a href="/service/attendanceSeminar/list.do">세미나 입장,퇴장</a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_TRACKING', 'BF_TRACKING_SCANNER', 'BF_TRACKING_BEACON')">
				<li class="dropdown">
					<a href="/contents/list.do" class="dropdown-toggle" data-toggle="dropdown">실시간 위치 측위 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="/tracking/main.do">대시보드</a></li>
						<li class="divider"></li>
						<sec:authorize access="hasAnyRole('BF_TRACKING_SCANNER')">
						<li><a href="/tracking/presence/scanner/target.do">스캐너 개별 대상자 측위</a></li>
						<li><a href="/tracking/presence/scanner/log.do">스캐너 위치측위 로그</a></li>
						<li><a href="/tracking/presence/scanner/gflog.do">스캐너 지오펜스 로그</a></li>
						<li><a href="/tracking/presence/scanner/iolog.do">스캐너 인/아웃 로그</a></li>
						<li class="divider"></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_TRACKING_BEACON')">
						<li><a href="/tracking/presence/beacon/target.do">비콘 개별 대상자 측위</a></li>
						<li><a href="/tracking/presence/beacon/log.do">비콘 위치측위 로그</a></li>
						<li class="divider"></li>
						</sec:authorize>
						<li><a href="/tracking/presence/setmap.do">프레즌스맵 설정</a></li>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_USER')">
				<li class="dropdown">
					<a href="/user/list.do" class="dropdown-toggle" data-toggle="dropdown">회원 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="/user/list.do">회원 관리</a></li>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_STAT')">
				<li class="dropdown">
					<a href="/stat/stat.do" class="dropdown-toggle" data-toggle="dropdown">통계 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="/stat/beacon/monitor.do">비콘 관제 통계</a></li>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('BF_INFO_COMPANY', 'BF_INFO_CODE', 'BF_INFO_ADVERT', 'BF_OAUTH')">
				<li class="dropdown">
					<a href="/info/company/mform.do" class="dropdown-toggle" data-toggle="dropdown">기본정보 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasAnyRole('BF_INFO_COMPANY')">
						<li><a href="/info/company/mform.do">업체정보</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_INFO_CODE')">
						<li><a href="/info/code/list.do">코드관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_INFO_ADVERT')">
						<li><a href="/info/ac/list.do">광고업체관리</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_INFO_LOCATION')">
						<li><a href="/info/location/list.do">위치정보 이용현황</a></li>
						<li><a href="/info/location/record.do">위치정보 열람대장</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('BF_OAUTH')">
						<li><a href="/oauth/client/list.do">OAuth클라이언트</a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
		</ul>
		<ul class="nav navbar-nav navbar-right navbar-vertical login-top" style="padding-top:6px;">
			<c:choose>
				<c:when test="${authenticated}">
					<li><button id="logoutTopBtn" type="button" class="btn btn-primary"><sec:authentication property="principal.displayName" />님 로그아웃</button></li>
				</c:when>
				<c:otherwise>
					<li><button id="loginTopBtn" type="button" class="btn btn-primary">로그인</button></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</nav>