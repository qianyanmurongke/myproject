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
		</script>

	</head>

	<body class="skin-blue content-body">

		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1>
			<s:message code="operationLog.management" />
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
					<form class="form-inline ls-search" action="list.do" method="get">
						<form class="form-inline ls-search" action="list.do" method="get">
							<div class="form-group">
								<label for="search_CONTAIN_Juser.realName">姓名</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_Juser.realName" value="${requestScope['search_CONTAIN_Juser.realName'][0]}" />
							</div>
							<div class="form-group">
								<label for="search_CONTAIN_Juser.username">账号</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_Juser.username" value="${requestScope['search_CONTAIN_Juser.username'][0]}" />
							</div>
							<div class="form-group">
								<label>操作模块<span class="in-prompt"
								title="请输入英文名称，可以使用%作为通配符"></span></label> <input class="form-control input-sm" name="search_LIKE_name" value="" style="width: 100px;" type="text">
							</div>

							<div class="form-group">
								<label>IP</label> <input class="form-control input-sm" name="search_LIKE_ip" value="" style="width: 100px;" type="text">
							</div>

							<div class="form-group">
								<label>开始时间</label> <input name="search_GTE_time_Date" style="width: 120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control input-sm" type="text">
							</div>
							<div class="form-group">
								<label>结束时间</label> <input name="search_LTE_time_Date" style="width: 120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control input-sm" type="text">
							</div>
							<button class="btn btn-primary" type="submit">
						<i class="fa fa-search"></i>
						<s:message code="search" />
					</button>
						</form>
					</form>
					<form method="post">
						<tags:search_params />
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:operation_log:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
										<i class="glyphicon glyphicon-trash"></i>
										批量<s:message code="delete" />
									</button>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb form-inline">
							<thead id="sortHead" pagesort="" pagedir="" pageurl="list.do?page_sort={0}&amp;page_sort_dir={1}&amp;">
								<tr class="ls_table_th">
									<th width="25"><input onclick="Cms.check('ids',this.checked);" type="checkbox"></th>
									<th class="ls-th-sort" width="30"></th>
									<th class="ls-th-sort" style="width: 150px;"><span class="ls-sort" pagesort="user.realName">姓名</span></th>
									<th class="ls-th-sort" style="width: 120px;"><span class="ls-sort" pagesort="user.group.name">用户类型</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="name">操作模块</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="dataId">操作内容</span></th>
									<th class="ls-th-sort" style="width: 200px;"><span class="ls-sort" pagesort="time"><s:message
											code="operationLog.time" /></span></th>
									<th class="ls-th-sort" style="width: 150px;"><span class="ls-sort" pagesort="ip"><s:message
											code="operationLog.ip" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="country">国家(地区)</span></th>
									<th width="90"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr beanid="${bean.id}">
										<td><input name="ids" value="${bean.id}" type="checkbox"></td>
										<td>${status.index+1}</td>
										<td>
											<c:out value="${bean.user.realName}" />
										</td>
										<td>
											<c:out value="${bean.user.group.name}" />
										</td>
										<td style="text-align: left;">
											<s:message code="${bean.name}" text="${bean.name}" />
										</td>
										<td style="text-align: left;">
											<c:out value="${bean.description}"></c:out>
										</td>
										<td align="center">
											<fmt:formatDate value="${bean.time}" pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td>
											<c:out value="${bean.ip}" />
										</td>
										<td align="center">${bean.country}(${bean.area})</td>
										<td align="center">
											<shiro:hasPermission name="core:operation_log:delete">
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