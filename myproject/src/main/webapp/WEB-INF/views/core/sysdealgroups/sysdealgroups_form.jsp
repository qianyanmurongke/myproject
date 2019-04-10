<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<form class="form-horizontal" id="validForm"
	action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<f:hidden name="oid" value="${bean.id}" />
	<div class="box-body">
		<div class="row">
			<div class="form-group">
				<label for="realName" class="col-sm-3 control-label">名  称<em
					class="required">*</em></label>
				<div class="col-sm-6">
					<input class="form-control required" id="name"
						name="name" value="${bean.name}" maxlength="100" />
				</div>
			</div>
		</div>
	</div>
</form>
