<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<style type="text/css">
	.ztree li span.button.switch.level0 {
		visibility: hidden;
		width: 1px;
	}
	
	.ztree li ul.level0 {
		padding: 0;
		background: none;
	}
</style>

<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<tags:search_params/>
	<f:hidden name="oid" value="${bean.id}" />
	<f:hidden name="position" value="${position}" />
	<input type="hidden" id="redirect" name="redirect" value="edit" />
	<div class="box-body">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label class="col-sm-4 control-label"><em class="required">*</em><s:message code="role.name"/></label>
					<div class="col-sm-8">
						<f:text id="name" name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="100" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label class="col-sm-4 control-label"><s:message code="role.rank"/><span class="in-prompt" title="<s:message code='role.rank.prompt' htmlEscape='true'/>"></span></label>
					<div class="col-sm-8">
						<f:text name="rank" value="${bean.rank}" class="form-control required digits" maxlength="10" default="999" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label"><s:message code="role.description"/></label>
					<div class="col-sm-10">
						<f:textarea name="description" value="${bean.description}" maxlength="255" class="form-control" />
					</div>
				</div>
			</div>
		</div>
	</div>
</form>