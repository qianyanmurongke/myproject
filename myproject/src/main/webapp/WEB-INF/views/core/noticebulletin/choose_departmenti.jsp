<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<style type="text/css">
<c:if test="${allowRoot || !empty treeNumber}">
.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
.ztree li ul.level0 {padding:0; background:none;}
</c:if>
</style>
<script type="text/javascript">

	var setting = {
		view: {
			expandSpeed: "",
			dblClickExpand: function(treeId, treeNode) {
				return false;				
			}
		},
		callback: {
			onClick: onClick,
			onDblClick: function(event, treeId, treeNode) {
				onClick(event,treeId,treeNode);
				$("#f7_ok").click();
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	var zNodes =[
	  <c:if test="${allowRoot}">
		{"id":-1,"pId":null,"name":"<s:message code='org.root'/>","displayName":"<s:message code='org.root'/>","open":true},
		</c:if>
  	<c:forEach var="item" items="${list}" varStatus="status">
  	<c:if test="${empty excludeChildrenBean || !fn:startsWith(item.treeNumber,excludeChildrenBean.treeNumber)}">
  	{"id":${item.id},"pId":<c:out value="${item.parent.id}" default="-1"/>,"name":"${item.name}","displayName":"${item.displayName}",<c:choose><c:when test="${item.treeNumber eq treeNumber}">"open":true</c:when><c:otherwise>"open":false</c:otherwise></c:choose>}<c:if test="${!status.last}">,</c:if>
  	</c:if>
  	</c:forEach>
  ];
	var ztree = $.fn.zTree.init($("#f7_tree"), setting, zNodes);
	
	</script>
<ul id="f7_tree" class="ztree" style="padding-top:5px"></ul>