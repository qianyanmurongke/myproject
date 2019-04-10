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
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap-switch.min.css">
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap-switch.min.js"></script>
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

			function confirmDeletePassword(callback) {
				swal({
					title: "删除密码后不可恢复",
					text: "是否确认删除密码",
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
					alert("<s:message code='pleaseSelectRecord'/>");
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

			function optDeletePassword(form) {
				if(Cms.checkeds("ids") == 0) {
					toastr.warning("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				confirmDeletePassword(function() {

					form.action = 'delete_password.do';
					form.submit();
				});

				return false;
			}

			function ShowEdit(url) {
				var paramTitle = '<s:message code="user.management" /><s:message code="edit" />';
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

							if($(item).find("#realName").val() == "") {
								toastr.warning('姓名不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}
							if($(item).find("#username").val() == "") {
								toastr.warning('账号不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}
							if($(item).find("#mobile").val() == "") {
								toastr.warning('手机号码不能为空。');
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
							formParam += "&responseType=ajax&status=" + ($("#chkstatus").is(':checked') ? 0 : 1);

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
			
			function goExport(data){
				if(data){
					toastr.warning("没有找到数据");
					return;
				}
				window.open("export.do?${searchstring}")
			}
		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1>
			<s:message code="user.management" />
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
							<label for="search_CONTAIN_username">用户名</label> <input class="form-control input-sm" type="text" id="search_CONTAIN_username" name="search_CONTAIN_username" value="${search_CONTAIN_username[0]}" style="width: 120px;" />
						</div>
						<div class="form-group">
							<label for="search_CONTAIN_realName">姓名</label> <input class="form-control input-sm" type="text" id="search_CONTAIN_realName" name="search_CONTAIN_realName" value="${requestScope['search_CONTAIN_realName'][0]}" style="width: 120px;" />
						</div>
						<div class="form-group">
							<label for="search_EQ_JuserRoles.role.id"><s:message
								code="user.role" /></label>
							<select class="form-control input-sm" id="search_EQ_JuserRoles.role.id" name="search_EQ_JuserRoles.role.id">
								<option value=""><s:message code="allSelect" /></option>
								<c:forEach var="role" items="${roleList}">
									<option value="${role.id}" <c:if test="${role.id eq requestScope['search_EQ_JuserRoles.role.id'][0]}"> selected="selected"</c:if>>${role.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label for="search_EQ_type">用户级别</label>
							<select class="form-control input-sm" id="search_EQ_type" name="search_EQ_type">
								<option value=""><s:message code="allSelect" /></option>
								<option value="0" <c:if test="${'0' eq search_EQ_type[0]}"> selected="selected"</c:if>>普通用户</option>
								<option value="1" <c:if test="${'1' eq search_EQ_type[0]}"> selected="selected"</c:if>><s:message code="user.type.1" /></option>
							</select>
						</div>
						<div class="form-group">
							<label for="search_EQ_status"><s:message
								code="user.status" /></label>
							<select class="form-control input-sm" id="search_EQ_status" name="search_EQ_status">
								<option value=""><s:message code="allSelect" /></option>
								<option value="0" <c:if test="${'0' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.0" /></option>
								<option value="1" <c:if test="${'1' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.1" /></option>
								<option value="2" <c:if test="${'2' eq search_EQ_status[0]}"> selected="selected"</c:if>><s:message code="user.status.2" /></option>
							</select>
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form method="post">
						<tags:search_params />
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:user:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="ShowEdit('create.do', false);">
									<i class="glyphicon glyphicon-plus"></i>
									<s:message code="create" />
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:user:edit">
									<button class="btn btn-grid btn-primary btn-sm" type="button" onclick="return optSingle('#edit_opt_');">
									<i class="glyphicon glyphicon-pencil"></i>
									<s:message code="edit" />
								</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:user:delete_password">
									<button class="btn btn-grid btn-danger btn-sm" type="button" onclick="return optDeletePassword(this.form);">
									<i class="fa fa-key" style="font-size: 17px;font-weight: 400;"></i>
									<s:message code="user.deletePassword" />
								</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:user:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
									<i class="glyphicon glyphicon-trash"></i>
									<s:message code="delete" />
								</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:user:importexcel">
									<tags:import_excel id="btnExcel" templateurl="${ctx}/uploads/exceltemp/人员导入模板.xls" templatename="人员导入模版.xls" classname="btn btn-grid btn-success btn-sm" title="导入人员数据" saveurl="${ctx}/admin/core/user/importexcel.do${searchstring}"></tags:import_excel>
								</shiro:hasPermission>
							</div>	
							<div class="btn-group">
									<button class="btn btn-grid btn-success btn-sm" type="button" onclick="goExport(${fn:length(pagedList.content) le 0});return false">  
										<i class="fa fa-file-excel-o"></i>
										<s:message code="exportExcel"/>
									</button>
							
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<th width="30"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="org.treeNumber">部门</span></th>
									<th class="ls-th-sort" ><span class="ls-sort" pagesort="username">用户名</span></th>
									<th class="ls-th-sort" ><span class="ls-sort" pagesort="realName">姓名</span></th>
									<th class="ls-th-sort" ><span class="ls-sort" pagesort="carCode">一卡通账号</span></th>
									<th><s:message code="user.roles" /></th>
									<th class="ls-th-sort" ><span class="ls-sort" pagesort="group.id">用户类型</span></th>
									<th class="ls-th-sort" ><span class="ls-sort" pagesort="type">用户级别</span></th>
									<th class="ls-th-sort" style="width: 80px;"><span class="ls-sort" pagesort="status"><s:message code="user.status" /></span></th>
									<th style="width: 180px;"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr <shiro:hasPermission name="core:user:edit"> ondblclick="$('#edit_opt_${bean.id}').click();"</shiro:hasPermission>>
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>

										<td>${status.index+1}</td>
										<td >${bean.departmentName}</td>
										<td >
											<c:out value="${bean.username}" />
										</td>
										<td >
											<c:if test="${!empty bean.realName}">
												<c:out value="${bean.realName}" />
											</c:if>
										</td>
										<td>
											<c:out value="${bean.carCode}"/>
										</td>
										<td>
											<c:forEach var="role" items="${bean.currRoles}" varStatus="status">
												${role.name}
												<c:if test="${!status.last}">,</c:if>
											</c:forEach>
										</td>
										<td>${bean.group.name}</td>
										<td>
											<c:choose>
												<c:when test="${bean.type == 0}">
													普通用户
												</c:when>
												<c:when test="${bean.type == 1}">
													<s:message code="user.type.1" />
												</c:when>
											</c:choose>
										</td>
										<td align="center">
											<c:if test="${bean.status!=0}">
												<strong style="color: red;">
										</c:if> 
										<s:message code="user.status.${bean.status}" /> 
										<c:if test="${bean.status!=0}">
											</strong>
											</c:if>
										</td>
											
										<td align="center">
											<shiro:hasPermission name="core:user:edit">
												<a id="edit_opt_${bean.id}" onclick="ShowEdit('edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}');return false;" class="ls-opt"><i class="glyphicon glyphicon-pencil  green"></i></a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:user:delete_password">
												<a title="删除密码" onclick="confirmDeletePassword(function(){window.location='delete_password.do?ids=${bean.id}&${searchstring}';});return false;" class="ls-opt">
													<i class="fa fa-key red"></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:user:delete">
												<c:choose>
													<c:when test="${bean.id gt 1 && bean.id!=currentUser.id}">
														<a title="删除用户" onclick="confirmDelete(function(){window.location='delete.do?ids=${bean.id}&${searchstring}';});return false;" class="ls-opt">
															<i class="glyphicon glyphicon-trash red"></i>
														</a>
													</c:when>
													<c:otherwise>
														<a class="ls-opt-disabled"><i class="glyphicon glyphicon-trash" style="color: #999;"></i></a>
													</c:otherwise>
												</c:choose>
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
					</form>
				</div>
			</div>
		</div>

	</body>

</html>