<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="robots" content="none" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><jsp:include page="/WEB-INF/views/title.jsp" /></title>
<meta name="renderer" content="webkit">
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<script>
	if (top != this) {
		top.location = this.location;
	}
</script>
<link rel="stylesheet"
	href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/toastr/toastr.min.css" />
<link rel="stylesheet" href="${ctx}/static/login/css/login.css">
<!--[if lt IE 9]>
  <script src="${ctx}/static/vendor/html5shiv/html5shiv.min.js"></script>
  <script src="${ctx}/static/vendor/respond/respond.min.js"></script>
  <![endif]-->
<style>
.alert {
	position: absolute;
	width: 100%;
	top: -30px;
	padding: 15px;
}

.popover-content {
	color: red;
}
</style>
</head>

<body>
	<%
		String defUsername = com.course.core.constant.Constants.DEF_USERNAME;
		String defPassword = com.course.core.constant.Constants.DEF_PASSWORD;
		String username = (String) request.getAttribute("username");
		if (username != null) {
			defUsername = username;
		}
	%>
	<div class="demo form-bg">

		<div class="row">
			<p>
				<img src="${ctx}/static/login/img/logo.png">
			</p>
			<form class="form-horizontal" id="validForm" action="login.do"
				method="post" autocomplete="off">
				<div class="f-dneglu">
					<a class="lanse" href="">账号登录</a> <a href="">二维码登录</a>
				</div>
				<!--账号登录-->
				<div class="from-box" style="position: relative;">

					<div class="form-group">
						<img src="${ctx}/static/login/img/yhm.png"> <input
							type="text"
							class="form-control {required:true,messages:{required:'请输入用户名'}}"
							id="username" name="username" autocomplete="off"
							value="<%=defUsername%>" placeholder="用户名">
						<div class="clearfix"></div>
						<!--<i class="fa fa-user"></i>-->
					</div>
					<div class="clearfix"></div>
					<div class="form-group help">
						<img src="${ctx}/static/login/img/mm.png"><input
							type="password" class="form-control" id="password"
							name="password" autocomplete="off" value="<%=defPassword%>"
							placeholder="密码">
						<div class="clearfix"></div>
						<!--<i class="fa fa-lock"></i>-->
					</div>
					<c:if
						test="${sessionScope.shiroCaptchaRequired || GLOBAL.captchaErrors<=0}">
						<div class="clearfix"></div>
						<div class="form-group help">
							<img src="${ctx}/static/login/img/yzm.png"><input
								type="text"
								class="form-control yzm {required:true,remote:{url:'${ctx}/captcha',type:'post'},messages:{required:'请输入验证码',remote:'验证码错误'}}"
								id="captcha" name="captcha" autocomplete="off" placeholder="验证码">
							<img class="yzm1" src="${ctx}/captcha"
								onclick="this.src='${ctx}/captcha?d='+new Date()*1">
							<div class="clearfix"></div>

						</div>
						<div class="clearfix"></div>
					</c:if>
					<div class="btn">
						<a href="${ctx}/admin/forgotpassword/forgot_password.do">忘记密码?</a><br>
						<button type="submit" id="btnLogin">立刻登录</button>
						<div class="clearfix"></div>
					</div>
					<div class="qita">
						<a href=""><img src="${ctx}/static/login/img/qq.png">QQ登录</a>
						<a href=""><img src="${ctx}/static/login/img/wx.png">微信登录</a>
					</div>
				</div>
				<!--二维码登录-->
				<div class="from-box erweima" style="display: none;">
					<img src="${ctx}/static/login/img/erweima.jpg">
					<div class="sys">
						<img src="${ctx}/static/login/img/sys.png"> <a>扫一扫登录</a>
					</div>
				</div>
			</form>

		</div>
		<div class="clearfix"></div>
	</div>
	<div class="bottom">
		<a>善喻科技有限公司 版权所有 ©2018 Zhejiang ShanYu information technology co. LTD 浙ICP备17034</a>
	</div>
	<script src="${ctx}/static/vendor/jquery/jquery.min.js"></script>
	<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="${ctx}/static/vendor/toastr/toastr.min.js"></script>
	<script
		src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
	<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
	<script src="${ctx}/static/js/plugins.js"></script>
	<script src="${ctx}/static/vendor/adminlte/js/app.min.js"></script>
	<script type="text/javascript">
		$(function() {

			$("#username").focus().select();

			$("#validForm").validate({
				success : "valid",
				focusInvalid : false,
				focusCleanup : true,
				onkeyup : false,
				showErrors : function(errorMap, errorList) {
					//this.defaultShowErrors();
					$(errorList).each(function() {
						toastr.warning(this.message);
					});
				},
				success : function(label) {
				}
			});

			document.onkeydown = function(e) {
				var ev = document.all ? window.event : e;
				if (ev.keyCode == 13) {
					$("#btnLogin").click();
				}
			}
		});
	</script>

	<script>
	<c:if test="${!empty shiroLoginFailure}">
		<c:choose>
			<c:when test="${shiroLoginFailure=='com.course.common.security.IncorrectCaptchaException'}">
				toastr.warning("<s:message code='incorrectCaptchaError' />");									
			</c:when>
			<c:when test="${shiroLoginFailure=='com.course.common.security.CaptchaRequiredException'}">				
				toastr.warning("<s:message code='captchaRequiredError' />");
			</c:when>
			<c:otherwise>			
				toastr.warning("<s:message code='usernameOrPasswordError' />");
			</c:otherwise>
		</c:choose>
	</c:if>		
	</script>
</body>

</html>