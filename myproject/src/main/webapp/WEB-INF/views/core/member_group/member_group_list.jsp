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

			function confirmDelete() {
				return confirm("<s:message code='confirmDelete'/>");
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
					alert("<s:message code='pleaseSelectRecord'/>");
					return false;
				}
				if(!confirmDelete()) {
					return false;
				}
				form.action = 'delete.do';
				form.submit();
				return true;
			}
		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1><s:message code="memberGroup.management"/> - <s:message code="list"/> <small>(<s:message code="totalElements" arguments="${fn:length(list)}"/>)</small></h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form class="form-inline ls-search" action="list.do" method="get">
						<div class="form-group">
							<label><s:message code="memberGroup.name"/></label>
							<input class="form-control input-sm" type="text" name="search_CONTAIN_name" value="${search_CONTAIN_name[0]}" />
						</div>
						<button class="btn btn-primary" type="submit">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form method="post">
						<tags:search_params/>
						<div class="btn-toolbar ls-btn-bar">
							<div class="btn-group">
								<shiro:hasPermission name="core:member_group:create">
									<button class="btn btn-grid btn-info btn-sm" type="button" onclick="location.href='create.do?${searchstring}';">
										<i class="glyphicon glyphicon-plus"></i>
										<s:message code="create"/>
									</button>
								</shiro:hasPermission>
							</div>
							<div class="btn-group">
								<shiro:hasPermission name="core:member_group:copy">
									<button class="btn btn-grid  btn-info btn-sm" type="button" onclick="return optSingle('#copy_opt_');">
										<i class="glyphicon glyphicon-copy"></i>
										<s:message code="copy" />
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="core:member_group:edit">
									<button class="btn btn-grid btn-primary btn-sm" type="button" onclick="return optSingle('#edit_opt_');">
										<i class="glyphicon glyphicon-pencil"></i>
										<s:message code="edit" />
									</button>
								</shiro:hasPermission>
							</div>
							
						</div>
						<table id="pagedTable" class="table table-condensed table-bordered table-hover ls-tb">
							<thead id="sortHead" pagesort="<c:out value='${page_sort[0]}' />" pagedir="${page_sort_dir[0]}" pageurl="list.do?page_sort={0}&page_sort_dir={1}&${searchstringnosort}">
								<tr class="ls_table_th">
									<th width="30"></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="name"><s:message code="memberGroup.name"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="type"><s:message code="memberGroup.type"/></span></th>
									<th class="ls-th-sort"><span class="ls-sort" pagesort="description"><s:message code="memberGroup.description"/></span></th>
									<th width="130"><s:message code="operate" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bean" varStatus="status" items="${list}">
									<tr<shiro:hasPermission name="core:member_group:edit"> ondblclick="location.href=$('#edit_opt_${bean.id}').attr('href');"</shiro:hasPermission>>

										<td>
											${status.index+1}
										</td>
										<td>
											<c:out value="${bean.name}" />
										</td>
										<td><s:message code="memberGroup.type.${bean.type}" /></td>
										<td>
											<c:out value="${bean.description}" />
										</td>

										<td align="center">
											<shiro:hasPermission name="core:member_group:copy">
												<c:choose>
													<c:when test="${bean.id < 5}">
														<span class="disabled"><s:message code="copy"/></span>
													</c:when>
													<c:otherwise>
														<a id="copy_opt_${bean.id}" href="create.do?id=${bean.id}&${searchstring}" class="ls-opt"><s:message code="copy" /></a>
													</c:otherwise>
												</c:choose>
											</shiro:hasPermission>
											<shiro:hasPermission name="core:member_group:edit">
												<a id="edit_opt_${bean.id}" href="edit.do?id=${bean.id}&position=${pagedList.number*pagedList.size+status.index}&${searchstring}" class="ls-opt"><s:message code="edit" /></a>
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