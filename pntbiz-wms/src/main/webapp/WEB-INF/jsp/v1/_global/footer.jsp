<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<footer id="content-footer" class="hidden-xs affix">
	<div class="row">
		<div class="col-md-6">
			<c:choose>
				<c:when test="${pageContext.request.serverName eq 'wms.idatabank.com'}">					
					<span class="footer-legal">COPYRIGHT@DATABANK SYSTEMS. ALL RIGHT RESERVED.</span>
				</c:when>
				<c:when test="${pageContext.request.serverName eq 'onticplace.cwit.co.kr'}">					
					<span class="footer-legal">COPYRIGHT© 2013 (주)중외정보기술 All Rights Reserved.</span>
				</c:when>
				<c:otherwise>					
					<span class="footer-legal">Copyright ⓒ 2014 People&Technology All rights reserved.</span>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="col-md-6 text-right">
			<a href="#content" class="footer-return-top"><span class="fa fa-arrow-up"></span></a>			
		</div>
	</div>
</footer>