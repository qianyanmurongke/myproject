<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!-- 用于显示操作结果信息。配合Controller的RedirectAttributes.addFlashAttribute("message","xxx")，可以只在第一次访问页面时显示信息，刷新页面不会重复显示  -->

<s:message code="operationSuccess" var="operationSuccess"/>
	<s:message code="${message}" text="${message}" var="msg"/>
<script type="text/javascript">
<c:if test="${!empty message}">
$(function() {
	

	<c:choose>
		<c:when test="${msg!=''}">  toastr.info('${msg}');</c:when>
		<c:otherwise> toastr.success('<s:message code="operationSuccess"/>');</c:otherwise>
	</c:choose>
	/*     toastr.success('操作成功', "作业推送");
	    toastr.success('提交数据成功');
	    toastr.error('Error');
	    toastr.warning('只能选择一行进行编辑');
	    toastr.info('info'); */
});
</c:if>

</script>