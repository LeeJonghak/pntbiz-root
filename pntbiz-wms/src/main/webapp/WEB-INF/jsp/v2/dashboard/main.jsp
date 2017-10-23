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
		window.layoutGnb = { mainmenu: '01', submenu: '01',
			location: ['<spring:message code="menu.000000" />', '<spring:message code="menu.000100" />']
		};
	</script>
</head>
<body>

	<div class="dashBox boxType1 col1">
		<div class="boxInner">
			<div class="title">
				<b>기간</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<input type="text" name="dateFrom" id="dateFrom" class="useDatepicker" value=""> ~
				<input type="text" name="dateTo" id="dateTo" class="useDatepicker" value="">
				<div class="dayTerm">
					<a href="#">오늘</a>
					<a href="#">어제</a>
					<a href="#">이번주</a>
					<a href="#">이번달</a>
				</div>
				<input type="reset" value="Represh" class="btn-inline btn-refresh">
				<input type="submit" value="검색" class="btn-inline btn-search">
			</div>
		</div>
	</div>
	<div class="dashBox boxType2 col4">
		<div class="boxInner">
			<div class="title">
				<b>비콘</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<b><em>Active</em>30</b>
				<em class="sep"></em>
				<span><em>Total</em>100</span>
			</div>
		</div>
		<div class="boxInner">
			<div class="title">
				<b>스캐너</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<b><em>Active</em>30</b>
				<em class="sep"></em>
				<span><em>Total</em>100</span>
			</div>
		</div>
		<div class="boxInner">
			<div class="title">
				<b>지오펜싱</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<b><em>Active</em>30</b>
				<em class="sep"></em>
				<span><em>Total</em>100</span>
			</div>
		</div>
		<div class="boxInner">
			<div class="title">
				<b>층수</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<b><em>Active</em>30</b>
				<em class="sep"></em>
				<span><em>Total</em>100</span>
			</div>
		</div>
	</div>
	<div class="dashBox col3">
		<div class="boxInner boxType3">
			<div class="title">
				<b>층별 진입 비콘수</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<div id="chart1" class="chartBox"></div>
			</div>
		</div>
		<div class="boxInner boxType3">
			<div class="title">
				<b>층별 진입 회수</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<div id="chart2" class="chartBox"></div>
			</div>
		</div>
		<div class="boxInner boxType4">
			<div class="title">
				<b>층별 진입 비콘정보</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<table class="basic">
					<colgroup>
						<col />
						<col width="35%">
						<col width="35%">
					</colgroup>
					<thead>
					<tr>
						<th scope="col">층</th>
						<th scope="col">진입횟수</th>
						<th scope="col">진입비콘수</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td>1층</td>
						<td class="aright">10,000</td>
						<td class="aright">100</td>
					</tr>
					<tr>
						<td>2층</td>
						<td class="aright">10,000</td>
						<td class="aright">100</td>
					</tr>
					<tr>
						<td>3층</td>
						<td class="aright">10,000</td>
						<td class="aright">100</td>
					</tr>
					<tr>
						<td>4층</td>
						<td class="aright">10,000</td>
						<td class="aright">100</td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="dashBox boxType5 col2">
		<div class="boxInner">
			<div class="title">
				<b>층별 진입 비콘 수</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<div id="chart3" class="chartBox"></div>
			</div>
		</div>
		<div class="boxInner">
			<div class="title">
				<b>층별 진입 회수</b>
				<a href="#" class="btnClose">닫기</a>
			</div>
			<div class="boxCon">
				<div id="chart4" class="chartBox"></div>
			</div>
		</div>
	</div>


</body>
</html>