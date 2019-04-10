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
		<script type="text/javascript">
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

			function optSingle(opt) {
				if(Cms.checkeds("ids") == 0) {
					alert("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				if(Cms.checkeds("ids") > 1) {
					alert("<s:message code='pleaseSelectOne'/>");
					return false;
				}
				var id = $("input[name='ids']:checkbox:checked").val();
				location.href = $(opt + id).attr("href");
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
				var paramTitle = (!isedit) ? '学校<s:message code="create" />' : '学校<s:message code="edit" />'
				BootstrapDialog.showDialog({
					title: paramTitle,
					loadUrl: url,
					buttons: [{
						label: '<i class="glyphicon glyphicon-ok"></i>&nbsp;<s:message code="save" />',
						cssClass: 'btn-success',
						action: function(dialog) {
							var $button = this;
							$button.disable();
							$button.spin();
							var item = dialog.getModalBody();

							if($(item).find("#name").val() == "") {
								toastr.warning('学校名称不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}

							var formParam = $(item).find("#validForm").serialize();
							formParam += "&responseType=ajax&status=" + ($("#chkstatus").is(':checked') ? 1 : 0);

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
						label: '<i class="glyphicon glyphicon-remove"></i>&nbsp;关闭',
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
			<h1>
				院校管理 - <s:message code="list"/> 
				<small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small>
			</h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form class="form-inline ls-search" action="list.do" method="get">
						<div class="form-group">
							<label><s:message code="org.name"/></label>
							<input class="form-control input-sm" type="text" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}" style="width:120px;" />
						</div>
						<div class="form-group">
							<label><s:message code="org.number"/></label>
							<input class="form-control input-sm" type="text" name="search_CONTAIN_number" value="${search_CONTAIN_number[0]}" style="width:120px;" />
						</div>
						<div class="form-group">
							<label><s:message code="org.parent"/></label>
							<select class="form-control input-sm" name="queryParentId" onchange="this.form.submit();">
								<option value=""></option>
								<c:forEach var="org" items="${orgList}">
									<c:if test="${fn:length(org.children) gt 0}">
										<option value="${org.id}" <c:if test="${org.id eq queryParentId}"> selected="selected"</c:if>>
									<c:forEach begin="1" end="${org.treeLevel}">--</c:forEach>${org.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="checkbox-inline">
								<f:checkbox name="showDescendants" value="${showDescendants}" default="true" onclick="this.form.submit();"/>
								显示子级院校
							</label>
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form action="batch_update.do" method="post">
						<tags:search_params/>
						<f:hidden name="queryParentId" value="${queryParentId}" />
						<f:hidden name="showDescendants" value="${showDescendants}" />
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:org_global:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="ShowEdit('create.do?showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}', false);">
										<i class="glyphicon glyphicon-plus"></i>
										<s:message code="create" />
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:org_global:copy">
									<button class="btn btn-grid  btn-info btn-sm" type="button" onclick="return optSingle('#copy_opt_');">
										<i class="glyphicon glyphicon-copy"></i>
										<s:message code="copy" />
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:org_global:edit">
									<button class="btn btn-grid btn-primary btn-sm" type="button" onclick="return optSingle('#edit_opt_');">
										<i class="glyphicon glyphicon-pencil"></i>
										<s:message code="edit" />
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:org_global:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
										<i class="glyphicon glyphicon-trash"></i>
										<s:message code="delete"/>
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:org_global:batch_update">
									<button class="btn  btn-grid btn-primary btn-sm" type="button" onclick="Cms.moveTop('ids');">
										<i class="glyphicon glyphicon-open"></i>
										<s:message code='moveTop'/>
									</button>
									<button class="btn  btn-grid btn-primary btn-sm" type="button" onclick="Cms.moveUp('ids');">
										<i class="glyphicon glyphicon-arrow-up"></i>
										<s:message code='moveUp'/>
									</button>
									<button class="btn  btn-grid btn-primary btn-sm" type="button" onclick="Cms.moveDown('ids');">
										<i class="glyphicon glyphicon-arrow-down"></i>
										<s:message code='moveDown'/>
									</button>
									<button class="btn  btn-grid btn-primary btn-sm" type="button" onclick="Cms.moveBottom('ids');">
										<i class="glyphicon glyphicon-save"></i>
										<s:message code='moveBottom'/>
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:org_global:batch_update">
									<button class="btn  btn-grid btn-success btn-sm" type="submit">
										<i class="glyphicon glyphicon-ok"></i>
										<s:message code="save"/>
									</button>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<th width="30"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="org.name" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="number"><s:message code="org.number" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="phone"><s:message code="org.phone" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="address"><s:message code="org.address" /></span></th>
									<th width="145"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${list}">
									<tr beanid="${bean.id}">
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
										<td>${status.index+1}</td>
										<td style="text-align: left;"><span style="padding-left:${bean.treeLevel*12}px">${bean.name}</span></td>
										<td align="center"><span style="width: 100px;">${bean.number}</span></td>
										<td align="center">${bean.phone}</td>
										<td align="center" style="text-align: left;">${bean.province}${bean.city}${bean.district}${bean.address}</td>
										<td align="center">
											<shiro:hasPermission name="core:org_global:copy">
												<a id="copy_opt_${bean.id}" onclick="ShowEdit('create.do?id=${bean.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}',false);return false;" class="ls-opt">
													<i class="glyphicon glyphicon-copy "></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:org_global:edit">
												<a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt">
													<i class="glyphicon glyphicon-pencil green"></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:org_global:delete">
												<a onclick="confirmDelete(function(){window.location='delete.do?ids=${bean.id}&showDescendants=${showDescendants}&queryParentId=${queryParentId}&${searchstring}';});return false;" class="ls-opt">
													<i class="glyphicon glyphicon-trash red"></i>
												</a>
											</shiro:hasPermission>
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