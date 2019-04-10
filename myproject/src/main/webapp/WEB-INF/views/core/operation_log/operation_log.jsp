<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.jspxcms.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/WEB-INF/views/head.jsp" />
		<script>
			$(function() {
				$("#sortHead").headSort();
			});

			function confirmDelete() {
				return confirm("<s:message code='confirmDelete'/>");
			}

			function optDelete(form) {
				if(Cms.checkeds("ids") == 0) {
					alert("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				if(!confirmDelete()) {
					return false;
				}
				form.action = 'delete.do';
				form.submit();
				return true;
			}
		</script>

	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1><s:message code="operationLog.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
		</div>

	</body>

</html>