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

<table class="table table-condensed table-bordered table-hover ls-tb">
	<thead>
		<tr class="ls_table_th">
			<th width="25"><input type="checkbox" onclick="Cms.check('assigninguserids',this.checked);" /></th>
			<th width="30"></th>
			<th class="ls-th-sort" style="width: 200px;">账号</th>
			<th class="ls-th-sort" style="width: 100px;">姓名</th>
			<th class="ls-th-sort" style="width: 150px;">性别</th>
			<th class="ls-th-sort" style="width: 80px;">用户类型</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
			<tr>
				<td><input type="checkbox" name="assigninguserids" value="${bean.user.id}" data-realname="${bean.user.realName}" /></td>
				<td>${status.index+1}</td>
				<td>
					<c:out value="${bean.user.username}" />
				</td>
				<td>
					<c:out value="${bean.user.realName}" />
				</td>
				<td>
					<c:choose>
						<c:when test="${bean.user.gender == 'M' }">
							男
						</c:when>
						<c:when test="${bean.user.gender == 'F' }">
							女
						</c:when>
						<c:otherwise>
							保密
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:out value="${bean.user.group.name}" />
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

<!-- 分页图标 -->
<form action="" method="get" class="ls-page" id="ShowAssigningUsersForm">
	<tags:search_params excludePage="true" />
	<tags:pagination_groupajax pagedList="${pagedList}" formId="ShowAssigningUsersForm" />

	<script>
		$(document).ready(function() {
			$("#ShowAssigningUsers #ShowAssigningUsersForm").submit(function() {

				AssignSearchList(this);
				return false;

			});
		});
	</script>
</form>