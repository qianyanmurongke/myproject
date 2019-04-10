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
<style>
	#validForm .form-group .bootstrap-select.show-tick {
		margin-top: -3px;
	}
</style>
<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<tags:search_params />
	<f:hidden name="oid" value="${bean.id}" />
	<f:hidden name="position" value="${position}" />
	<input type="hidden" id="redirect" name="redirect" value="edit" />
	<div class="box-body">

		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">姓名</label>
					<div class="col-sm-8">
						<input id="realName" name="realName" class="form-control"  value="${bean.realName}" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="username" class="col-sm-4 control-label">
						<em class="required">*</em>
								用户名</label>
					<div class="col-sm-8">
						<f:text id="username" name="username" value="${oprt=='edit' ? (bean.username) : ''}" class="form-control {required:true,remote:{url:'check_username.do',type:'post',data:{original:'${oprt=='edit' ? (bean.username) : ''}'}},messages:{remote:'${usernameExist}'}}" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">手机号码</label>
					<div class="col-sm-8">
						<input id="mobile" name="mobile" class="form-control"  value="${bean.mobile}" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="carCode" class="col-sm-4 control-label">一卡通账号</label>
					<div class="col-sm-8">
						<input id="carCode" name="carCode" class="form-control"  value="${bean.carCode}" />
					</div>
				</div>
			</div>			
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="rawPassword" class="col-sm-4 control-label"><s:message
										code="user.password" /></label>
					<div class="col-sm-8">
						<input class="form-control" id="rawPassword" type="password" name="rawPassword" />
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="againPassword" class="col-sm-4 control-label"><s:message
										code="user.pwdAgain" /></label>
					<div class="col-sm-8">
						<input id="againPassword" type="password" name="againPassword" class="form-control {equalTo:'#rawPassword'}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="groupId" class="col-sm-4 control-label">用户类型</label>
					<div class="col-sm-8">
						<select class="form-control" id="groupId" name="groupId">
							<f:options items="${groupList}" selected="${bean.group.id}" itemLabel="name" itemValue="id" />
						</select>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="realName" class="col-sm-4 control-label">部门</label>
					<div class="col-sm-8">
						<input id="departmentName" name="departmentName" class="form-control disabled"  value="${bean.departmentName}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="status" class="col-sm-4 control-label"><s:message
										code="user.status" /></label>

					<div class="col-sm-8">
						<div class="switch switch-large">
							<input type="checkbox" id="chkstatus" ${bean.status==0? "checked": ""}>
						</div>
					</div>
					<script type="text/javascript">
						$("#chkstatus").bootstrapSwitch();
					</script>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label for="type" class="col-sm-2 control-label">
						所属角色
					</label>
					<div class="col-sm-10">

						<select id="roleIds" name="roleIds" data-width="100%" data-icon="icon-ok" data-size="10" class="selectpicker show-tick form-control  input-sm" data-live-search="true" multiple>
							<c:forEach var="role" items="${roleList}">

								<option value="${role.id}" <c:if test="${fnx:contains_cso(bean.roles,'id',role.id)}"> selected</c:if>>${role.name}
								</option>

							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<script>
	
	$("#validForm").validate();
	
	$('.selectpicker').selectpicker({}); 
	<c:if test="${fn:length(bean.roleIds)> 0}">
		$('#roleIds').selectpicker('val', ${bean.roleIds}); 
	</c:if>

</script>