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
			<s:message code="loginLog.management" />
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
							<label for="search_CONTAIN_Juser.realName">姓名</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_Juser.realName" value="${requestScope['search_CONTAIN_Juser.realName'][0]}" />
						</div>
						<div class="form-group">
							<label for="search_CONTAIN_Juser.username">账号</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_Juser.username" value="${requestScope['search_CONTAIN_Juser.username'][0]}" />
						</div>
						<div class="form-group">
							<label for="search_CONTAIN_name">动作</label> <input class="form-control input-sm" type="text" name="search_CONTAIN_name" value="${requestScope['search_CONTAIN_name'][0]}" />
						</div>
						<div class="form-group">
							<label for="search_EQ_operationType">登录类型</label>
							<select class="form-control input-sm" name="search_EQ_operationType">
								<option value="">--全部--</option>
								<f:option value="0" selected="${requestScope['search_EQ_operationType'][0] }">Web</f:option>
								<f:option value="1" selected="${requestScope['search_EQ_operationType'][0] }">APP</f:option>
								<f:option value="2" selected="${requestScope['search_EQ_operationType'][0] }">微信</f:option>
							</select>
						</div>
						<div class="form-group">
							<label for="search_GTE_time_Date">操作日期</label> <input class="form-control input-sm" type="text" name="search_GTE_time_Date" value="${requestScope['search_GTE_time_Date'][0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width: 100px;" />
						</div>
						<div class="form-group">
							<label for="search_LTE_time_Date">至</label> <input class="form-control input-sm" type="text" name="search_LTE_time_Date" value="${requestScope['search_LTE_time_Date'][0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width: 100px;" />
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
								<shiro:hasPermission name="core:operation_log:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
									<i class="glyphicon glyphicon-trash"></i>
									批量<s:message code="delete" />
								</button>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<th width="30" class="ls-th-sort"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="user.realName">姓名</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="user.group.name">用户类型</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="operationType">登录类型</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="time"><s:message code="operationLog.time" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="name">动作</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="ip"><s:message
											code="operationLog.ip" /></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="country">国家(地区)</span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="os">系统版本</span></th>
									<th width="60"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr beanid="${bean.id}">
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
										<td>${status.index+1}</td>
										<td>
											<c:out value="${bean.user.realName}" />
										</td>
										<td>
											<c:out value="${bean.user.group.name}" />
										</td>
										<td>
											<c:choose>
												<c:when test="${bean.operationType == 1 }">
													APP
												</c:when>
												<c:when test="${bean.operationType == 2 }">
													微信
												</c:when>
												<c:otherwise>
													Web
												</c:otherwise>
											</c:choose>
										</td>
										<td align="center">
											<fmt:formatDate value="${bean.time}" pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td><s:message code="${bean.name}" /></td>
										<td>
											<c:out value="${bean.ip}" />
										</td>
										<td align="center">${bean.country}(${bean.area})</td>
										<td>${bean.os}</td>
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