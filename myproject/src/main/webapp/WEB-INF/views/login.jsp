<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="robots" content="none" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>
			<jsp:include page="/WEB-INF/views/title.jsp" />
		</title>
		<meta name="renderer" content="webkit">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<script>
			if(top != this) {
				top.location = this.location;
			}
		</script>
		
		<script>  
(function(){  
    function w() {  
    var r = document.documentElement;  
    var a = r.getBoundingClientRect().width;//获取当前设备的宽度  
        if (a > 1920 ){//720不固定，根据设计稿的宽度定  
            a = 1920;  
        }   
        rem = a / 19.2;  
        r.style.fontSize = rem + "px"  
    }  
    w();  
    window.addEventListener("resize", function() {//监听横竖屏切换  
        w()  
    }, false);  
})();  
</script> 
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="${ctx}/static/accesslogin/css/zzsc.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/toastr/toastr.min.css" />
	</head>

	<body>
		<%
			String defUsername = com.course.core.constant.Constants.DEF_USERNAME;
			String defPassword = com.course.core.constant.Constants.DEF_PASSWORD;
			String username = (String) request.getAttribute("username");
			if(username!=null) {
				defUsername = username;
			}
		%>
		<div class="box">
			<div class="f-dneglu">
				<div class="logo"><img src="${ctx}/static/accesslogin/img/logo.png"></div>
			</div>
			<div class="top">
				<img src="${ctx}/static/accesslogin/img/top1.png">
			</div>
			<div class="demo form-bg">
				<div class="row">
					<form class="form-horizontal" id="validForm" action="login.do" method="post" autocomplete="off">
						<!--账号登录-->
						<div class="from-box1">

							<div class="dl_top">
								<img src="${ctx}/static/accesslogin/img/dl_top.png">
							</div>
							<div class="clearfix"></div>
							<div class="form-group1">
								<input type="text" class="form-control {required:true,messages:{required:'请输入用户名'}}" id="username" name="username" autocomplete="off" value="<%=defUsername%>" placeholder="请输入您的账号">
								<div class="clearfix"></div>
								<!--<i class="fa fa-user"></i>-->
							</div>
							<div class="clearfix"></div>
							<div class="form-group1 help">
								<input type="password" class="form-control" id="password" name="password" autocomplete="off" value="<%=defPassword%>" placeholder="请输入您的密码">
								<div class="clearfix"></div>
								<!--<i class="fa fa-lock"></i>-->
							</div>
							<div class="clearfix"></div>
							<div class="form-group1 help">
								<input type="text" class="form-control yzm {required:true,remote:{url:'${ctx}/captcha',type:'post'},messages:{required:'请输入验证码',remote:'验证码错误'}}" id="captcha" name="captcha" autocomplete="off" placeholder="验证码">
								<img class="yzm1" src="${ctx}/captcha" onclick="this.src='${ctx}/captcha?d='+new Date()*1">
								<div class="clearfix"></div>
							</div>
							<div class="clearfix"></div>
							<div class="btn1">
								<button type="submit" id="btnLogin">登录</button>
								<div class="clearfix"></div>
							</div>
						</div>
					</form>
				</div>
				<div class="clearfix"></div>
				<div class="bottom">
				</div>
			</div>

			<script src="${ctx}/static/vendor/jquery/jquery.min.js"></script>
			<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
			<script src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
			<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
			<script src="${ctx}/static/js/plugins.js"></script>
			<script src="${ctx}/static/vendor/adminlte/js/app.min.js"></script>
			<script src="${ctx}/static/vendor/toastr/toastr.min.js"></script>
			<script type="text/javascript">
				$(function() {

					$("#validForm .f-dneglu a").click(function() {
						$('#example').popover('toggle');
						$("#validForm .f-dneglu a").removeClass("lanse")
						$(this).addClass("lanse");
						$("#validForm .from-box").hide();
						$("#validForm .from-box").eq($(this).index()).show();
						return false;
					});

					$("#username").focus().select();
					$("#validForm").validate({
						success: "valid",
						focusInvalid: false,
						focusCleanup: true,
						onkeyup: false,
						showErrors: function(errorMap, errorList) {
							//this.defaultShowErrors();
							$('.form-control').popover('destroy');
							$(errorList).each(function() {
								$($(this.element)).popover({
									placement: "bottom",
									content: this.message
								});
								$($(this.element)).popover('show');
							});
						},
						success: function(label) {}
					});

					document.onkeydown = function(e) {
						var ev = document.all ? window.event : e;
						if(ev.keyCode == 13) {
							$("#btnLogin").click();
						}
					}
					$(".alert").fadeOut(9000);
				});
			</script>
			<c:if test="${!empty shiroLoginFailure}">
				<c:choose>
					<c:when test="${shiroLoginFailure=='com.course.common.security.IncorrectCaptchaException'}">
						<script type="text/javascript">
							toastr.error("<s:message code='incorrectCaptchaError' />");
						</script>
					</c:when>
					<c:when test="${shiroLoginFailure=='com.course.common.security.CaptchaRequiredException'}">
						<script type="text/javascript">
							toastr.error("<s:message code='captchaRequiredError' />");
						</script>
					</c:when>
					<c:when test="${shiroLoginFailure=='org.apache.shiro.authc.IncorrectCredentialsException'}">
						<script type="text/javascript">
							toastr.error("密码错误");
						</script>
					</c:when>
					<c:otherwise>
						<script type="text/javascript">
							toastr.error("<s:message code='usernameOrPasswordError' />");
						</script>
					</c:otherwise>
				</c:choose>
			</c:if>
	</body>

</html>