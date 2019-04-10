<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/WEB-INF/views/head.jsp" />
		<shiro:hasPermission name="core:role:edit">
			<script type="text/javascript">
				$(function() {
					$("#pagedTable tbody tr").dblclick(function(eventObj) {
						var nodeName = eventObj.target.nodeName.toLowerCase();
						if(nodeName != "input" && nodeName != "select" && nodeName != "textarea") {
							//location.href = $("#edit_opt_" + $(this).attr("beanid")).attr('href');
							$("#edit_opt_" + $(this).attr("beanid")).click();
						}
					});
				});
			</script>
		</shiro:hasPermission>
		<script type="text/javascript">
			$(function() {
				$("#validForm").validate();
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

			function optSingle(opt) {
				if(Cms.checkeds("ids") == 0) {
					toastr.warning("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				if(Cms.checkeds("ids") > 1) {
					toastr.warning("<s:message code='pleaseSelectOne'/>");
					return false;
				}
				var id = $("input[name='ids']:checkbox:checked").val();
				//				location.href = $(opt + id).attr("href");

				$(opt + id).click();
				return false;
			}

			function optMulti(form, action, msg) {
				if(Cms.checkeds("ids") == 0) {
					toastr.warning("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				if(msg && !confirm(msg)) {
					return false;
				}
				form.action = action;
				form.submit();
				return true;
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
				var paramTitle = '<s:message code="role.management"/>' + ((!isedit) ? '<s:message code="create" />' : '<s:message code="edit" />');
				BootstrapDialog.showDialog({
					closeByBackdrop: false,
					closeByKeyboard: true,
					draggable: true,
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

							if($(item).find("#name").val() == "") {
								toastr.warning('角色名称不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}

							var formParam = $(item).find("#validForm").serialize();
							formParam += "&responseType=ajax";

							$.post($(item).find("#validForm").attr("action"), formParam, function(result) {
								if(result.status == 0) {

									toastr.warning(result.messages);
								} else {
									toastr.success('提交数据成功');
									dialog.close();
									window.location.reload();
								}
								$button.enable();
								$button.stopSpin();
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

			function ShowAssigningUsers(roleid) {
				BootstrapDialog.showDialog({
					closeByBackdrop: false,
					closeByKeyboard: true,
					draggable: true,
					title: "分配角色用户",
					cssClass: "ShowAssigningUsers-dialog",
					loadUrl: "assignuser.do?id=" + roleid,
					buttons: [{
						label: '关闭',
						icon: 'glyphicon glyphicon-remove',
						action: function(dialog) {
							dialog.close();
						}
					}]
				});
			}

			function ShowPermsSave(roleid) {

				BootstrapDialog.showDialog({
					closeByBackdrop: false,
					closeByKeyboard: true,
					draggable: true,
					title: "分配功能权限",
					cssClass: "ShowPermsSave-dialog",
					loadUrl: "setmoduleperms.do?id=" + roleid,
					buttons: [{
						label: '<s:message code="save" />',
						icon: 'glyphicon glyphicon-floppy-saved',
						cssClass: 'btn-success',
						action: function(dialog) {
							var $button = this;
							$button.disable();
							$button.spin();
							var item = dialog.getModalBody();

							$.post("savemoduleperms.do", {
								"perms": $(item).find("#f7_id_Number").val(),
								"oid": roleid,
								"responseType": "ajax"
							}, function(result) {
								if(result.status == 0) {

									toastr.warning(result.messages);
								} else {
									toastr.success('提交数据成功');
									dialog.close();
								}
								$button.enable();
								$button.stopSpin();
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
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1><s:message code="role.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form class="form-inline ls-search" action="list.do" method="get">
						<div class="form-group">
							<label><s:message code="role.name"/></label>
							<input class="form-control input-sm" type="text" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}" />
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form id="validForm" action="batch_update.do" method="post">
						<tags:search_params/>
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:role:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="ShowEdit('create.do?${searchstring}', false);">
										<i class="glyphicon glyphicon-plus"></i>
										<s:message code="create" />
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:role:update">
									<button class="btn btn-grid btn-success btn-sm" type="submit">
										<i class="glyphicon glyphicon-ok"></i>
										<s:message code="save"/>
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:role:copy">
									<button class="btn btn-grid  btn-info btn-sm" type="button" onclick="return optSingle('#copy_opt_');">
										<i class="glyphicon glyphicon-copy"></i>
										<s:message code="copy" />
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:role:edit">
									<button class="btn btn-grid btn-primary btn-sm" type="button" onclick="return optSingle('#edit_opt_');">
										<i class="glyphicon glyphicon-pencil"></i>
										<s:message code="edit" />
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:role:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
										<i class="glyphicon glyphicon-trash"></i>
										<s:message code="delete" />
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:workflow_step:update">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="Cms.moveTop('ids');return false;">
										<i class="glyphicon glyphicon-open"></i>
										<s:message code='moveTop'/>
									</button>
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="Cms.moveUp('ids');return false;">
										<i class="glyphicon glyphicon-arrow-up"></i>
										<s:message code='moveUp'/>
									</button>
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="Cms.moveDown('ids');return false;">
										<i class="glyphicon glyphicon-arrow-down"></i>
										<s:message code='moveDown'/>
									</button>
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="Cms.moveBottom('ids');return false;">
										<i class="glyphicon glyphicon-save"></i>
										<s:message code='moveBottom'/>
									</button>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<th width="30"></th>
									<th class="ls-th-sort" style="width: 150px;"><span class="ls-sort" pagesort="name"><s:message code="role.name"/></span></th>
									<th class="ls-th-sort" style="width: 100px;"><span class="ls-sort" pagesort="rank"><s:message code="role.rank"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="description"><s:message code="role.description"/></span></th>
									<th width="80">功能权限</th>
									<th width="130"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${list}">
									<tr beanid="${bean.id}">
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
										<td>${status.index+1}<input type="hidden" name="id" value="${bean.id}" />
										</td>
										<td style="text-align: left;">
											${bean.name}
										</td>
										<td>
											${bean.rank}
										</td>
										<td style="text-align: left;">
											${bean.description}
										</td>
										<td>
											<c:if test="${bean.id>1 }">
											<a><i class="fa fa-key orange" onclick="ShowPermsSave('${bean.id}');return false;"></i></a>
											</c:if>
										</td>
										<td align="center">
											<shiro:hasPermission name="core:role:copy">
												<a id="copy_opt_${bean.id}" onclick="ShowEdit('create.do?id=${bean.id}&workflowId=${workflow.id}', false);return false;" class="ls-opt" title="<s:message code='copy' />"><i class="glyphicon glyphicon-copy "></i></a>
											</shiro:hasPermission>
											<c:if test="${bean.id>1 }">
												<shiro:hasPermission name="core:role:edit">
													<a id="edit_opt_${bean.id}" onclick="ShowEdit('edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}', true);return false;" class="ls-opt"><i class="glyphicon glyphicon-pencil green"></i></a>
												</shiro:hasPermission>
												<shiro:hasPermission name="core:role:delete">
													<a onclick="confirmDelete(function(){window.location='delete.do?ids=${bean.id}&workflowId=${workflow.id}&${searchstring}';});return false;" class="ls-opt">
														<i class="glyphicon glyphicon-trash red"></i>
													</a>
												</shiro:hasPermission>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<c:if test="${fn:length(list) le 0}">
							<div class="ls-norecord"><s:message code="recordNotFound" /></div>
						</c:if>
					</form>
				</div>
			</div>
		</div>
	</body>

</html>