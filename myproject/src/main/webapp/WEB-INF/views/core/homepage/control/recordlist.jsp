<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<font style="font-size: 15px;color: #669FC7;"><i class="fa fa-thumb-tack"></i>&nbsp;开门记录</font>
		<div style="float: right;">
			<a href="${ctx}/admin/core/accessrecord/list.do"> 更多&nbsp;<i class="fa fa-arrow-right" style="font-size: 14px;"></i></a>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			loadAffairsList();
		});

		function loadAffairsList() {
			$.ajax({
				type: "GET",
				url: "recordlist.do",
				data: {
					"responseType": "ajax"
				},
				dataType: "json",
				success: function(result) {
					if(result.status == 1) {
						$(result.items).each(function() {
							$("#affairs").append("<li class='list-group-item'>" +
								"<a > " +this.doorname+"-"+this.username + "<font style='color:#777;float: right;'>" + this.startdate + "</font ></a> </li>");
						});
					}
				}
			});
		}
	</script>
	<ul class="list-group" id="affairs">
	</ul>
</div>