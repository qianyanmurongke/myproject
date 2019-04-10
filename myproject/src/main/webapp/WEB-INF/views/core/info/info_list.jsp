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
		<style>
			.tabs {}
			
			.tabs li {
				float: left;
				background-color: #f4f4f4;
				border-left: 1px solid #e2e2e2;
				border-top: 1px solid #ddd;
				border-right: 1px solid #ddd;
				margin-right: 5px;
			}
			
			.tabs li a {
				color: #555555;
				float: left;
				text-decoration: none;
				padding: 5px 12px;
			}
			
			.tabs li a:link,
			.tabs li a:visited,
			.tabs li a:hover,
			.tabs li a:active {
				text-decoration: none;
			}
			
			.tabs li.active {
				background-color: #FFFFFF;
				border-left: 1px solid #ddd;
				border-top: 1px solid #ddd;
				border-right: 1px solid #ddd;
			}
			
			.tabs li.active a {
				color: #000;
			}
			
			.tabs li.hover {
				background-color: #e7e7e7;
				border-left: 1px solid #ddd;
				border-top: 1px solid #ddd;
				border-right: 1px solid #ddd;
			}
			
			.tabs li.hover a {
				color: #000;
			}
		</style>
		<script>
			$(function() {
				$("#radio").buttonset();
				$("#sortHead").headSort();
				$("#tabs li").each(function() {
					$(this).hover(function() {
						if(!$(this).hasClass("active")) {
							$(this).addClass("hover");
						}
					}, function() {
						$(this).removeClass("hover");
					});
				});

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
				var url = $(opt + id).attr("href");
				if(url) {
					location.href = $(opt + id).attr("href");
				} else {
					alert("<s:message code='noPermission'/>");
				}
			}

			function optMulti(form, action, confirmMsg) {
				if(Cms.checkeds("ids") == 0) {
					toastr.warning("<s:message code='pleaseSelectRecord'/>");
					return false;
				}

				if(confirmMsg) {
					confirmDelete(function() {

						form.action = action;
						form.submit();
					});
					return false;
				}
				form.action = action;
				form.submit();

				return false;
			}

			function optDelete(form) {
				optMulti(form, "delete.do", confirmDelete);

				return false;
			}
		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1><s:message code="info.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${pagedList.totalElements}"/>)</small></h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form id="searchForm" action="list.do" method="get" class="form-inline ls-search">
						<div class="form-group">
							<label for="search_CONTAIN_detail.title"><s:message code="info.title"/></label>
							<input class="form-control input-sm" style="width: 200px;" type="text" id="search_CONTAIN_detail.title" name="search_CONTAIN_detail.title" value="${requestScope['search_CONTAIN_detail.title'][0]}" style="width:150px;" />
						</div>

						<div class="form-group">
							<label for="search_GTE_publishDate_Date"><s:message code="beginTime"/></label>
							<input class="form-control input-sm" style="width: 200px;" type="text" id="search_GTE_publishDate_Date" name="search_GTE_publishDate_Date" value="${search_GTE_publishDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;" />
						</div>
						<div class="form-group">
							<label for="search_LTE_publishDate_Date"><s:message code="endTime"/></label>
							<input class="form-control input-sm" style="width: 200px;" type="text" id="search_LTE_publishDate_Date" name="search_LTE_publishDate_Date" value="${search_LTE_publishDate_Date[0]}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" style="width:100px;" />
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
						<f:hidden name="queryNodeId" value="${queryNodeId}" />
						<f:hidden name="queryNodeType" value="${queryNodeType}" />
						<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}" />
					</form>
					<form method="post">
						<tags:search_params/>
						<f:hidden name="queryNodeId" value="${queryNodeId}" />
						<f:hidden name="queryNodeType" value="${queryNodeType}" />
						<f:hidden name="queryInfoPermType" value="${queryInfoPermType}" />
						<f:hidden name="queryStatus" value="${queryStatus}" />
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:info:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="location.href='create.do?queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';">
										<i class="glyphicon glyphicon-plus"></i>
										<s:message code="create"/>
									</button>
								</shiro:hasPermission>
							</div>

							<div class="btn-group">
								<shiro:hasPermission name="core:info:delete">
									<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return optDelete(this.form);">
										<i class="glyphicon glyphicon-trash"></i>
										<s:message code="completelyDelete"/>
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:info:recall">
									<button class="btn btn-grid btn-success btn-sm" type="button" onclick="return optMulti(this.form,'recall.do');">
										<i class="glyphicon glyphicon-trash"></i>
										<s:message code="info.recall"/>
									</button>
								</shiro:hasPermission>
							</div>

						</div>
						<ul id="tabs" class="tabs list-unstyled">
							<shiro:hasPermission name="core:info:status">
								<li<c:if test="${empty queryStatus}"> class="active"</c:if>>
									<a href="javascript:void(0);" onclick="$('#queryStatus').val('');$('#searchForm').submit();"><s:message code="info.status.all" /></a>
									</li>
									<li<c:if test="${queryStatus eq 'G'}"> class="active"</c:if>>
										<a href="javascript:void(0);" onclick="$('#queryStatus').val('G');$('#searchForm').submit();"><s:message code="info.status.G" /></a>
										</li>
										<shiro:hasPermission name="core:info:recall">
											<li<c:if test="${queryStatus eq 'X'}"> class="active"</c:if>>
												<a href="javascript:void(0);" onclick="$('#queryStatus').val('X');$('#searchForm').submit();"><s:message code="info.status.X" /></a>
												</li>
										</shiro:hasPermission>
							</shiro:hasPermission>
						</ul>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="25"><input type="checkbox" onclick="Cms.check('ids',this.checked);" /></th>
									<td style="width: 30px;"></td>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="detail.title"><s:message code="info.title"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="publishDate"><s:message code="info.publishDate"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="priority"><s:message code="info.priority"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="views"><s:message code="info.views"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="status"><s:message code="info.status"/></span></th>
									<th style="width: 120px;"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									<tr<shiro:hasPermission name="core:info:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>
										<td><input type="checkbox" name="ids" value="${bean.id}" /></td>
										<td>${status.index+1}</td>
										<td style="text-align: left;">
											<a href="view.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" title="<c:out value='${bean.title}'/>">
												<c:out value="${fnx:substringx_sis(bean.title,30,'...')}" />
											</a>
										</td>
										<td>
											<fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd HH:mm:ss" />
											<c:if test="${!empty bean.offDate}">
												~
												<fmt:formatDate value="${bean.offDate}" pattern="yyyy-MM-dd HH:mm:ss" />

											</c:if>
										</td>
										<td align="right">
											<c:choose>
												<c:when test="${bean.priority eq 999}">
													<font color="red">置顶</font>
												</c:when>
												<c:otherwise>
													--
												</c:otherwise>
											</c:choose>
										</td>
										<td align="right">
											<c:out value="${bean.bufferViews}" />
										</td>
										<td align="center">
											<c:choose>
												<c:when test="${bean.status eq '1'}">
													${bean.stepName}
												</c:when>
												<c:when test="${bean.status eq 'A'}">
													<a href="${bean.url}" target="_blank"><s:message code="info.status.${bean.status}" /></a>
												</c:when>
												<c:otherwise>
													<s:message code="info.status.${bean.status}" />
												</c:otherwise>
											</c:choose>

										</td>
										<td align="center">
											<shiro:hasPermission name="core:info:edit">
												<a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><i class="glyphicon glyphicon-pencil green"></i></a>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:info:logic_delete">
												<c:choose>
													<c:when test="${bean.auditPerm}">
														<a onclick="confirmDelete(function(){window.location='logic_delete.do?ids=${bean.id}&queryNodeId=${queryNodeId}&queryNodeType=${queryNodeType}&queryInfoPermType=${queryInfoPermType}&queryStatus=${queryStatus}&${searchstring}';});return false;" class="ls-opt"><i class="glyphicon glyphicon-trash red"></i></a>
													</c:when>
													<c:otherwise>
														<span class="disabled"><s:message code="delete"/></span>
													</c:otherwise>
												</c:choose>
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
					<form action="list.do" method="get" class="ls-page">
						<tags:search_params excludePage="true" />
						<f:hidden name="queryNodeId" value="${queryNodeId}" />
						<f:hidden name="queryNodeType" value="${queryNodeType}" />
						<f:hidden name="queryInfoPermType" value="${queryInfoPermType}" />
						<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}" />
						<tags:pagination pagedList="${pagedList}" />
					</form>
				</div>
			</div>
		</div>
	</body>

</html>