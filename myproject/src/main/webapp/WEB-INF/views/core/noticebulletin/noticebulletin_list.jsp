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
<!DOCTYPE html>
<html>

<head>
<jsp:include page="/WEB-INF/views/head.jsp" />
<link rel="stylesheet" href="${ctx}/static/vendor/webuploader/webuploader.css" />
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap-switch.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/webuploader/inportexcel.css" />
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap-switch.min.js"></script>
		<script src="${ctx}/static/vendor/webuploader/webuploader.js"></script>
<script type="text/javascript">
	$(function() {
		$("#sortHead").headSort();
	});
	
	function confirmDelete(callback) {
		
		swal({
			title : "删除后不可恢复",
			text : "是否确认删除",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "是的，确定删除！",
			cancelButtonText : "取消",
			closeOnConfirm : true
		}, function(isconfirm) {
			if (isconfirm) {
				callback();
			}
			return false;
		});
	}

	function optSingle(opt) {
		if (Cms.checkeds("ids") == 0) {
			toastr.warning("<s:message code='pleaseSelectRecord'/>");
			return false;
		}
		if (Cms.checkeds("ids") > 1) {
			toastr.warning("<s:message code='pleaseSelectOne'/>");
			return false;
		}
		var id = $("input[name='ids']:checkbox:checked").val();

		$(opt + id).click();
		return false;
	}

	function optMulti(form, action, msg) {
		if (Cms.checkeds("ids") == 0) {
			toastr.warning("<s:message code='pleaseSelectRecord'/>");
			return false;
		}
		if (msg && !confirm(msg)) {
			return false;
		}
		form.action = action;
		form.submit();
		return true;
	}

	function optDelete(form) {
		if (Cms.checkeds("ids") == 0) {
			toastr.warning("<s:message code='pleaseSelectRecord'/>");
			return false;
		}
		confirmDelete(function() {

			form.action = 'delete.do';
			form.submit();
		});

		return false;
	}

	//派单更新
	 function ShowEdit(url, isedit) {
		
		var paramTitle = (!isedit) ? '<s:message code="notice.bulletin" /><s:message code="create" />'
				: '<s:message code="notice.bulletin" /><s:message code="edit" />'

		BootstrapDialog
				.show({
					closeByBackdrop : false,
					closeByKeyboard : true,
					draggable : true,
					title: "报修管理—派单更新",
					message : $('<div></div>').load(url),
					buttons : [
							{
								label : '<i class="glyphicon glyphicon-ok"></i>&nbsp;保存',
								cssClass : 'btn-success',             
								action : function(dialog) {
									var item = dialog.getModalBody();
									if($("#userNameId").val() == ""){
										toastr.warning('维修人不能为空。');
										return;
										
									}
									if($("#repairMode").val() == ""){
										toastr.warning('维修人方式不能为空。');
										return;
										
									}
									
									if (!$(item).find("#validForm").valid()) {
										return;
									}

									var formParam = $(item).find("#validForm")
											.serialize();
									formParam += "&responseType=ajax";

									$
											.post(
													$(item).find("#validForm")
															.attr("action"),
													formParam,
													function(result) {
														if (result.status == 0) {
															toastr
																	.warning(result.messages);
														} else {
															toastr
																	.success('提交数据成功');
															dialog.close();
															window.location
																	.reload();
														}
													}, "json")										
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
</head>

<body class="skin-blue content-body">
	<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
	<div class="content-header">
		<h1>
			<s:message code="notice.bulletin" />
			-
			<s:message code="list" />
			<small>(<s:message code="totalElements"
					arguments="${pagedList.totalElements}" />)
			</small>
		</h1>
	</div>
	<div class="content">
		<div class="box box-primary">
			<div class="box-body table-responsive">
				<form action="list.do" method="get" class="form-inline ls-search">
				<div class="form-group">
						<label for="search_CONTAIN_title">公告标题</label> 
						<input class="form-control input-sm" type="text"
							name="search_CONTAIN_title"
							value="${requestScope['search_CONTAIN_title'][0]}" />
					</div>						
					<div class="form-group">
						<label>发布日期</label>
						<div class="input-group">
							<input class="form-control input-sm" type="text"
								name="search_GTE_publishTime_Date"
								value="${requestScope['search_GTE_publishTime_Date'][0] }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label>至</label>
						<div class="input-group">
							<input class="form-control input-sm" type="text"
								name="search_LTE_publishTime_Date"
								value="${requestScope['search_LTE_publishTime_Date'][0] }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>																		
				<div class="form-group">
						<label for="search_EQ_Juser.id">发布部门</label> 
						<select class="form-control input-sm"
							id="selectOrg" style="width: 220px"
							name="search_EQ_Juser.id">
							<option value="">--请选择--</option>
							<f:options itemLabel="realName" itemValue="id" items="${userList}"
								selected="${requestScope['search_EQ_Juser.id'][0] }" />
						</select>
					</div>
													
										
					<div class="form-group">
						<label>有效期期</label>
						<div class="input-group">
							<input class="form-control input-sm" type="text"
								name="search_GTE_repairTime_Date"
								value="${requestScope['search_GTE_repairTime_Date'][0] }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label>至</label>
						<div class="input-group">
							<input class="form-control input-sm" type="text"
								name="search_LTE_repairTime_Date"
								value="${requestScope['search_LTE_repairTime_Date'][0] }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="search_CONTAIN_repairCode">发布人</label>  <input
							class="form-control input-sm" type="text"
							name="search_CONTAIN_repairCode"
							value="${requestScope['search_CONTAIN_repairCode'][0]}" />
					</div>	
					&nbsp;&nbsp;&nbsp;							
					<button class="btn btn-primary" type="submit" id="search">
						<i class="fa fa-search"></i>
						<s:message code="search" />
					</button>
				</form>
				<form method="post">
					<tags:search_params />
					<div class="btn-toolbar ls-btn-bar">
					<div class="btn-group">
							<shiro:hasPermission name="core:noticebulletin:create">
								<button class="btn btn-grid btn-info btn-sm" type="button"
									onclick="ShowEdit('create.do', false);">
									<i class="glyphicon glyphicon-plus"></i>
									申请
								</button>
							</shiro:hasPermission>
						</div>	
						<div class="btn-group">
							<shiro:hasPermission name="equipment:repairapply:delete">
								<button class="btn btn-grid btn-warning btn-sm" type="button"
									onclick="return optDelete(this.form);">
									<i class="glyphicon glyphicon-trash"></i>
									<s:message code="delete" />
								</button>
							</shiro:hasPermission>
						</div>					
					</div>
					<table id="pagedTable"
						class="table table-condensed table-bordered table-hover ls-tb">
						<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />"
							pagedir="${page_sort_dir[0]}"
							pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
							<tr class="ls_table_th">
								<th width="25"><input type="checkbox"
									onclick="Cms.check('ids',this.checked);" /></th>
								<th width="30"></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="repairCode">公告标题</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="repairGoods">发布部门</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="user.realName">发布人</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="repairTime">发布日期</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="repairRemarks">有效日期</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="repairDepartment.name">审批状态</span></th>
								<th class="ls-th-sort"><span class="ls-sort" pagesort="dispatch">发布状态</span></th>								
								<th class="ls-th-sort"><span class="ls-sort" pagesort="status">查阅情况</span></th>																
								<th width="120"><s:message code="operate" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="bean" varStatus="status"
								items="${pagedList.content}">
								<tr
									<shiro:hasPermission name="equipment:repairmanage:edit"> ondblclick="$('#edit_opt_${bean.id}').click();"</shiro:hasPermission>> 
									<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
									<td>${status.index+1}</td>
									<td>${bean.title}</td>
									<td></td>										
									<td></td>
									<td><fmt:formatDate value="${bean.publishTime }" pattern="yyyy-MM-dd" /></td>
									<td></td>
									<td></td>																																															
									<td></td>
									<td></td>
									<td>
										<shiro:hasPermission name="equipment:repairmanage:display">
											<a onclick="ShowDisplay('display.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}', true);return false;" class="ls-opt" title="查看">
													<i class="fa fa-reorder"></i>
											</a>
											</shiro:hasPermission></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<c:if test="${fn:length(pagedList.content) le 0}">
						<div class="ls-norecord">
							<s:message code="recordNotFound" />
						</div>
					</c:if>
				</form>
				<form action="list.do" method="get" class="ls-page">
					<tags:search_params excludePage="true" />
					<tags:pagination pagedList="${pagedList}" />
				</form>
			</div>
		</div>
	</div>
</body>

</html>