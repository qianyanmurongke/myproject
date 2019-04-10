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
<style>
	.ShowAssigningUsers-dialog .modal-dialog {
		width: 900px;
	}
	
	.ShowAssigningUsers-dialog .modal-body {
		padding: 0px;
	}
	
	#ShowAssigningUsers .nav-tabs-custom {
		margin-bottom: 0px;
		background: #fff;
		box-shadow: none;
		border-radius: 0px;
	}
	
	#ShowAssigningUsers .nav-tabs-custom>.tab-content {
		padding: 0px;
	}
</style>
<div class="box" style="border-radius: 0px;border-top: none;box-shadow: none;margin-bottom: 0px;">
	<div class="box-body" id="ShowAssigningUsers">
		<form method="get" class="form-inline ls-search">
			<div class="form-group">
				<label>姓名</label> <input class="form-control input-sm searchName" type="text" name="search_CONTAIN_Juser.realName" value="" />
			</div>
			<button type="button" class="btn btn-primary btn-sm" id="btnAssignSearch">
				<i class="fa fa-search"></i>
				<s:message code="search" />
			</button>
		</form>
		<div class="btn-toolbar ls-btn-bar">
			<div class="btn-group">
				<button class="btn btn-grid btn-info btn-sm" type="button" onclick="AddAssigningUser();return false;">
					<i class="glyphicon glyphicon-plus"></i>
					添加老师
				</button>
			</div>
			<div class="btn-group">
				<button class="btn btn-grid btn-info btn-sm" type="button" onclick="AddStudentUser();return false;">
					<i class="glyphicon glyphicon-plus"></i>
					添加学生
				</button>
			</div>
			<div class="btn-group">
				<button class="btn btn-grid btn-warning btn-sm" type="button" onclick="return DeleteAssigningUser(this.form);">
					<i class="glyphicon glyphicon-trash"></i>
					<s:message code="delete" />
				</button>
			</div>
		</div>
		<div id="ShowAssigningUsersList">
			<table class="table table-condensed table-bordered table-hover ls-tb">
				<thead>
					<tr class="ls_table_th">
						<th width="25"><input type="checkbox" onclick="SelectEmployeeMulti('assigninguserids',this.checked);" /></th>
						<th width="30"></th>
						<th class="ls-th-sort" style="width: 200px;">账号</th>
						<th class="ls-th-sort" style="width: 100px;">姓名</th>
						<th class="ls-th-sort" style="width: 150px;">性别</th>
						<th class="ls-th-sort" style="width: 80px;">用户类型</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

	</div>
</div>

<script>
	function AddStudentUser(){
		Common.plugin.Student_Multi({
			SelectFunction: function(ids, realNames) {
				$.post("addassignuser.do", {
					"userids": ids,
					"oid": "${bean.id}",
					"responseType": "ajax"
				}, function(result) {
					if(result.status == 0) {

						toastr.warning(result.messages);
					} else {
						var formParam = "search_CONTAIN_Juser.realName=" + $("#ShowAssigningUsers .searchName").val();
						formParam += "&search_EQ_Jrole.id=${bean.id}";
						$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?" + formParam)
					}
				}, "json").error(function(err) {

					toastr.error("网络异常,请稍后操作");

				});

			}
		});
	}
	function AddAssigningUser() {
		Common.plugin.Employee_Multi({
			SelectFunction: function(ids, realNames) {
				$.post("addassignuser.do", {
					"userids": ids,
					"oid": "${bean.id}",
					"responseType": "ajax"
				}, function(result) {
					if(result.status == 0) {

						toastr.warning(result.messages);
					} else {
						var formParam = "search_CONTAIN_Juser.realName=" + $("#ShowAssigningUsers .searchName").val();
						formParam += "&search_EQ_Jrole.id=${bean.id}";
						$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?" + formParam)
					}
				}, "json").error(function(err) {

					toastr.error("网络异常,请稍后操作");

				});

			}
		});
	}

	function DeleteAssigningUser(form) {
		if(Cms.checkeds("assigninguserids") == 0) {
			toastr.warning("<s:message code='pleaseSelectRecord'/>");
			return false;
		}
		confirmDelete(function() {
			var ids = new Array();
			$("input[name='assigninguserids']:checkbox:checked").each(function() {
				ids.push($(this).val());
			});

			$.post("deleteassignuser.do", {
				"userids": ids,
				"oid": "${bean.id}",
				"responseType": "ajax"
			}, function(result) {
				if(result.status == 0) {

					toastr.warning(result.messages);
				} else {
					toastr.success('删除成功');
					var formParam = "search_CONTAIN_Juser.realName=" + $("#ShowAssigningUsers .searchName").val();
					formParam += "&search_EQ_Jrole.id=${bean.id}";
					$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?" + formParam)
				}
			}, "json").error(function(err) {

				toastr.error("网络异常,请稍后操作");

			});

		});

		return false;
	}
	$("#btnAssignSearch").click(function() {

		var formParam = "search_CONTAIN_Juser.realName=" + $("#ShowAssigningUsers .searchName").val();
		formParam += "&search_EQ_Jrole.id=${bean.id}";
		$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?" + formParam)

	});
	$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?search_EQ_Jrole.id=${bean.id}")

	function AssignSearchList(object) {

		var formParam = $(object).serialize();
		formParam += "&search_EQ_Jrole.id=${bean.id}";
		$("#ShowAssigningUsers  #ShowAssigningUsersList").load("${ctx}/admin/core/role/assignuserlist.do?" + formParam)
	}
</script>