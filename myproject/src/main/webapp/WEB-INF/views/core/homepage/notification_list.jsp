<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

			function getnotification(id, backendurl) {

				$.ajax({
					type: "GET",
					url: "changeunread.do",
					data: {
						"id": id,
						"responseType": "ajax"
					},
					dataType: "json",

				});
debugger
				BootstrapDialog.show({
					closeByBackdrop: false,
					closeByKeyboard: true,
					draggable: true,
					cssClass: 'chooseemployee-dialog',
					title: "消息—查看",
					message: $('<div></div>').load(backendurl),
					buttons: [{
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
			<h1>系统消息 - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form action="notification_list.do" method="get" class="form-inline ls-search">
						<div class="form-group">
							<label for="search_CONTAIN_content">内容</label>
							<input class="form-control input-sm" type="text" name="search_CONTAIN_content" value="${requestScope['search_CONTAIN_content'][0]}" />
						</div>
						<div class="form-group">
							<label for="search_GTE_sendTime_Date">发送时间</label>
							<input class="form-control input-sm" type="text" name="search_GTE_sendTime_Date" value="${search_GTE_sendTime_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;" />
						</div>
						<div class="form-group">
							<label for="search_LTE_sendTime_Date">至</label>
							<input class="form-control input-sm" type="text" name="search_LTE_sendTime_Date" value="${search_LTE_sendTime_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;" />
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form method="post" class="form-inline">
						<tags:search_params/>
						<div class="btn-toolbar ls-btn-bar">
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="notification_list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="30"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="content"><s:message code="notification.content"/></span></th>
									<th width="120"><s:message code="notification.source" /></th>
									<th width="120" class="ls-th-sort"><span class="ls-sort" pagesort="sender.realName">发送人</span></th>
									<th width="160" class="ls-th-sort"><span class="ls-sort" pagesort="sendTime"><s:message code="notification.sendTime"/></span></th>
									<th width="90" class="ls-th-sort"><span class="ls-sort" pagesort="unread">状态</span></th>
									<th width="90">查看</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr <shiro:hasPermission name="core:homepage:notification:view"> ondblclick="$('#display_opt_${bean.id}').click();"</shiro:hasPermission>>
										<td>${status.index+1}</td>
										<td style="text-align: left;">${bean.contentBackend}</td>
										<td>
											<!--<c:forEach var="source" items="${bean.sources}" varStatus="status">${source}
												<c:if test="${!status.last}">, </c:if>
											</c:forEach>-->
											${bean.typeName}
										</td>
										<td>${bean.receiver.realName}</td>
										<td>
											<fmt:formatDate value="${bean.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td>
											<c:choose>
												<c:when test="${bean.unread}">
													<strong style="color:red;"><s:message code="mail.unread.1"/></strong>
												</c:when>
												<c:otherwise>
													<s:message code="mail.unread.0" />
												</c:otherwise>
											</c:choose>
										</td>
										<td>
											<shiro:hasPermission name="core:homepage:notification:view">
												<a id="display_opt_${bean.id}" onclick="getnotification(${bean.id},'${ctx}/admin${bean.bankUrl}')" data-url="${bean.bankUrl}" class="ls-opt" title="查看">
													<i class="fa fa-reorder"></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:homepage:notification:delete">
												<a onclick="confirmDelete(function(){window.location='notification_delete.do?ids=${bean.id}&${searchstring}';});return false;" class="ls-opt">
													<i class="glyphicon glyphicon-trash red"></i>
												</a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<c:if test="${fn:length(pagedList.content) le 0}">
							<div class="ls-norecord"><s:message code="recordNotFound" /></div>
						</c:if>
					</form>
					<form action="notification_list.do" method="get" class="ls-page">
						<tags:search_params excludePage="true" />
						<tags:pagination pagedList="${pagedList}" />
					</form>
				</div>
			</div>
		</div>

	</body>

</html>