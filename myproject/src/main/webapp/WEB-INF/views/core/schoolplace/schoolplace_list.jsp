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
		<script>
			$(function() {
				$("#sortHead").headSort();
			});

			function confirmDelete(callback) {
				swal({
					title: "删除后不可恢复",
					text: "是否确认删除",
					type: "warning",
					showCancelButton: true,
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "是的，确定删除！",
					cancelButtonText: "取消",
					closeOnConfirm: true
				}, function(isconfirm) {
					if(isconfirm) {
						callback();
					}
					return false;
				});
			}


			function optDelete(form) {
				if(Cms.checkeds("ids") == 0) {
					toastr.warning("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				confirmDelete(function() {

					form.action = 'delete.do';
					form.submit();
				});

				return false;
			}

			function ShowEdit(url, isedit) {
				var paramTitle = (!isedit) ? '<s:message code="school.place" /><s:message code="create" />' :
					'<s:message code="school.place" /><s:message code="edit" />'
				if(!isedit)
					url = url + "?orgId=" + $("#orgId").val();
				BootstrapDialog.showDialog({
					title: paramTitle,
					loadUrl: url,
					buttons: [{
						label: '<s:message code="save" />',
						icon: 'glyphicon glyphicon-floppy-saved',
						cssClass: 'btn-success',
						action: function(dialog) {

							var $button = this;
							$button.disable();
							$button.spin();
							var item = dialog.getModalBody();

							if($(item).find("#commonitemId").val() == "") {
								toastr.warning('所属区域不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}
							if($(item).find("#placeName").val() == "") {
								toastr.warning('场所名称不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}
							if($(item).find("#placeSerial").val() == "") {
								toastr.warning('场所序号不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}
							if($(item).find("#buildingNo").val() == "") {
								toastr.warning('建筑物号不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}

							if(!$(item).find("#validForm").valid()) {
								$button.enable();
								$button.stopSpin();
								return;
							}

							var formParam = $(item).find("#validForm").serialize();
							formParam += "&responseType=ajax";

							$.post($(item).find("#validForm").attr("action"), formParam, function(result) {
								if(result.status == 0) {

									toastr.warning(result.messages);
									$button.enable();
									$button.stopSpin();
								} else {
									toastr.success('提交数据成功');
									dialog.close();
									window.location.reload();
								}
							}, "json").error(function(err) {
								toastr.error("网络异常,请稍后操作");
								$button.enable();
								$button.stopSpin();
							});

						}
					}, {
						label: '关闭',
						icon: 'glyphicon glyphicon-remove',
						action: function(dialog) {
							dialog.close();
						}
					}]
				});

			}

		</script>

		<style>
			.SchoolRoom-Resultdialog .modal-dialog {
				width: 900px;
			}
			
			.SchoolRoom-Resultdialog .modal-body {
				padding: 0px;
				height: 500px;
			}
		</style>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1>
			<s:message code="school.place" />
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
							<label for="search_CONTAIN_placeName">场所名称</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_placeName" value="${search_CONTAIN_placeName[0]}" style="width: 200px;" />
						</div>
						<button class="btn btn-primary" type="submit">
						<i class="fa fa-search"></i>
						<s:message code="search" />
					</button>
					</form>
					<form method="post">
						<f:hidden name="orgId" value="${orgId}" id="orgId" />
						<tags:search_params />
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:schoolplace:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="ShowEdit('create.do?${searchstring}', false);">
									<i class="glyphicon glyphicon-plus"></i>
									<s:message code="create" />场所
								</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:schoolplace:edit">
									<button class="btn btn-grid btn-primary btn-sm" type="button" onclick="return optSingle('#edit_opt_');">
									<i class="glyphicon glyphicon-pencil"></i>
									<s:message code="edit" />场所
								</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:schoolplace:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
									<i class="glyphicon glyphicon-trash"></i>
									批量<s:message code="delete" />
								</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:classes:importexcel">
									<tags:import_excel id="btnExcel" templateurl="${ctx}/uploads/exceltemp/场所模版.xls" templatename="场所导入模版.xls" classname="btn btn-grid btn-success btn-sm" title="导入场所数据" saveurl="${ctx}/admin/core/schoolplace/importexcel.do?${searchstring}"></tags:import_excel>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<th width="30"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="placeName">场所名称</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="placeSerial">场所序号</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="buildingNo">建筑物号</span></th>
									<th width="120"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr <shiro:hasPermission name="core:schoolplace:edit"> ondblclick="$('#edit_opt_${bean.id}').click();"</shiro:hasPermission>>
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
										<td>${status.index+1}</td>
										<td>${bean.placeName}</td>
										<td align="center">${bean.placeSerial }</td>
										<td align="center">${bean.buildingNo }</td>
										<td align="center">
											<shiro:hasPermission name="core:schoolplace:edit">
												<a id="edit_opt_${bean.id}" onclick="ShowEdit('edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}', true);return false;" class="ls-opt"><i class="glyphicon glyphicon-pencil green"></i></a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:schoolplace:delete">
												<a onclick="confirmDelete(function(){window.location='delete.do?ids=${bean.id}&${searchstring}';});return false;" class="ls-opt"> <i class="glyphicon glyphicon-trash red"></i>
												</a>
											</shiro:hasPermission>
										</td>
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