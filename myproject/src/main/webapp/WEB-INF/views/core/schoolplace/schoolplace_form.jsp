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
</script>
<form class="form-horizontal" id="validForm"
	action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<tags:search_params />
	<f:hidden name="oid" value="${bean.id}" />
	<f:hidden name="position" value="${position}" />
	<input type="hidden" id="redirect" name="redirect" value="list" />
	<div class="box-body">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">场所名称<em
						class="required">*</em></label>
					<div class="col-sm-8">
						<input class="form-control required" id="placeName"
							name="placeName" value="${bean.placeName}" maxlength="100" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">场所序号<em class="required">*</em></label>
					<div class="col-sm-8">
						<input class="form-control required" id="placeSerial"
							name="placeSerial" value="${bean.placeSerial}" maxlength="100" />
					</div>
				
					
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">建筑物号<em class="required">*</em></label>
					<div class="col-sm-8">
						<input class="form-control required" id="buildingNo"
							name="buildingNo" value="${bean.buildingNo}" maxlength="100" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">场所顺序</label>
					<div class="col-sm-8">
						<input class="form-control number" id="placeOrder"
							name="placeOrder" value="${bean.placeOrder}"  />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label for="id" class="col-sm-2 control-label">备注</label>
					<div class="col-sm-10">
						<f:textarea class="form-control" id="remarks" name="remarks"
							value="${bean.remarks }" maxlength="255" rows="3" />
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	$(function() {
		$("#validForm").validate();
		
		$("#commonitemId").selectpicker();	
		
	
	});
</script>