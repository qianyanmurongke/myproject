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
	<tags:search_params />
	<f:hidden name="oid" value="${bean.id}" />
	<f:hidden name="position" value="${position}" />
	<f:hidden name="orgId" value="${orgId}" />
	<input type="hidden" id="redirect" name="redirect" value="list" />
	<div class="box-body">
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label">公告标题</label>
					<div class="col-sm-10">
						<input class="form-control required " id="title" name="title"
							value="${bean.title}" maxlength="45" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label class="col-sm-4 control-label">发布日期<em
						class="required">*</em></label>
					<div class="col-sm-8">
						<div class="input-group">
							<input class="form-control required" id="repairTime"
								name="repairTime"
								value='<fmt:formatDate value="${publishTime}" pattern="yyyy-MM-dd"/>'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label class="col-sm-4 control-label">失效日期<em
						class="required">*</em></label>
					<div class="col-sm-8">
						<div class="input-group">
							<input class="form-control required" id="repairTime"
								name="repairTime"
								value='<fmt:formatDate value="${bean.expirTtime}" pattern="yyyy-MM-dd"/>'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label">查找部门</label>
					<div class="col-sm-10">
						<input class="form-control required " id="key" name="name"
							type="text" value="" maxlength="45" /> <input type="button"
							value="查找" onclick="ShowDepartment();" />
					</div>
					<ul id="f7_tree" class="ztree" style="padding-top: 5px"></ul>
				</div>

				<script type="text/javascript">
					//部门列表显示
					function ShowDepartment() {
						BootstrapDialog
								.show({
									closeByBackdrop : false,
									closeByKeyboard : true,
									draggable : true,
									title : "通知公告-部门选择",
									message : $('<div></div>').load(
											"${ctx}/admin/education/department/choose_department_tree_multi.do"),
									buttons : [
											{
												label : '<i class="glyphicon glyphicon-ok"></i>&nbsp;保存',
												cssClass : 'btn-success',
												action : function(dialog) {
													var item = dialog
															.getModalBody();

													
												}
											},
											{
												label : '<i class="glyphicon glyphicon-remove"></i>&nbsp;关闭',
												action : function(dialog) {
													dialog.close();
												}
											} ]
								});

					}
				</script>
			</div>
			<div class="col-sm-12">
				<div class="form-group">
					<label for="realName" class="col-sm-2 control-label">查找人员<em
						class="required">*</em></label>
					<div class="col-sm-10">
						<tags:choose_employee id="userid" name="userid"
							value="${bean.user.id }" realname="${bean.user.realName }">
						</tags:choose_employee>
					</div>
				</div>
			</div>



		</div>

	</div>
</form>

<script type="text/javascript">
	$("#validForm").validate();

	
</script>