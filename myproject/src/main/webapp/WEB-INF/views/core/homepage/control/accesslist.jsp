<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="col-md-6 col-sm-6 col-xs-12" title="门禁在线数量" onclick="window.location='/admin/core/accesscontrol/list.do';" style="padding-left: 0px; padding-right: 5px;cursor: pointer;">
	<div class="info-box" style="background-color: #183cc3;">
		<span class="info-box-icon" style="background-color: #1936a2;"><img src="/static/img/online.png"></span>

		<div class="info-box-content">
			<span class="info-box-number" style="font-size: 5.5rem;text-align: center; color: white;" id="online">--</span>
		</div>
		<!-- /.info-box-content -->
	</div>
	<!-- /.info-box -->
</div>
<div class="col-md-6 col-sm-6 col-xs-12" title="门禁离线数量" onclick="window.location='/admin/core/accesscontrol/list.do';" style="cursor: pointer;">
	<div class="info-box" style="background-color: #e0623c;">
		<span class="info-box-icon" style="background-color: #b94421;"><img src="/static/img/offline.png"></span>

		<div class="info-box-content">
			<span class="info-box-number" style="font-size: 5.5rem;text-align: center; color: white;" id="offline">--</span>
		</div>
		<!-- /.info-box-content -->
	</div>
	<!-- /.info-box -->
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$.ajax({
			type: "GET",
			url: "${ctx}/admin/core/accesscontrol/getaccessonlinestatus.do",
			data: {
				"responseType": "ajax"
			},
			dataType: "json",
			success: function(result) {
				if(result.status == 1) {
					$("#online").html(result.onlineCount);
					$("#offline").html(result.unOnlineCount);
				}
			}
		});
	});
</script>