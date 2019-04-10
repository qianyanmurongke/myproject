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
	.ShowPermsSave-dialog .modal-dialog {
		width: 350px;
	}
	
	.ShowPermsSave-dialog .modal-body {
		padding: 0px;
	}
</style>
<div class="box-body" style="height: 300px;overflow: auto;">

	<input type="hidden" id="f7_id" value="" />
	<input type="hidden" id="f7_id_Number" value="" />
	<ul id="f7_perm_tree" class="ztree" style="padding-top:5px"></ul>
</div>

<script type="text/javascript">
	$(function() {
		var onModulePermsCheck = function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("f7_perm_tree");
			var nodes = treeObj.getCheckedNodes(true);
			var perms = "";
			for(var i = 0, len = nodes.length; i < len; i++) {
				var p = nodes[i].perms;
				if(p) {
					perms += p + ",";
				}
			}
			if(perms.length > 0) {
				perms = perms.substring(0, perms.length - 1);
			}
			$("#f7_id_Number").val(perms);
		}
		var setting = {
			check: {
				enable: true,
				chkboxType: { "Y": "ps", "N": "s" }
			},
			callback: {
				onCheck: onModulePermsCheck
			},
			view: {
				expandSpeed: ""
			}
		};
		//补足最后一个逗号，用于判断是否包含某个权限
		var perms = new Array();
		<c:forEach var="drAccessRoles" items="${role.drAccessRoles}" varStatus="status1">
		   perms.push("${drAccessRoles.draccesscontrol.id}");
		</c:forEach>
		var isChecked = function(perm) {
			return perms.indexOf(perm) != -1;
		};
		var zNodes = {
			name: "门禁",
			open: true,
			children: [
				<c:forEach var="menu" items="${draccesscontrollist}" varStatus="status1">
					{ name:"${menu.doorName}", isParent:true,"perms":"${menu.id}","checked":isChecked("${menu.id}")}<c:if test="${!status1.last}">,</c:if>
				</c:forEach>

			]
		};
		var ztree = $.fn.zTree.init($("#f7_perm_tree"), setting, zNodes);
		onModulePermsCheck();
	});
</script>