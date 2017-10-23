<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="bh10"></div>
<div class="center">
	<c:choose>
		<c:when test="${pageContext.request.serverName eq 'wms.idatabank.com'}">
			COPYRIGHT@DATABANK SYSTEMS. ALL RIGHT RESERVED.
		</c:when>
		<c:when test="${pageContext.request.serverName eq 'wms.indoorplus.co.kr'}">
			Copyright(C) 2015 People&Technology All rights reserved.
		</c:when>
		<c:otherwise>
			Copyright(C) 2014 People&Technology All rights reserved.
		</c:otherwise>
	</c:choose>
</div>
