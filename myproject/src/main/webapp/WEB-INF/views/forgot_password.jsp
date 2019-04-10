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
		<title>
			找回密码--<jsp:include page="/WEB-INF/views/title.jsp" />
		</title>
		<meta name="renderer" content="webkit">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<script>
			if(top != this) {
				top.location = this.location;
			}
		</script>
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/toastr/toastr.min.css" />
		<link rel="stylesheet" href="${ctx}/static/login/css/forgot_password.css">
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
		<div class="box">
			<div class="demo form-bg">

				<div class="row">
					<p><img src="${ctx}/static/login/img/logo.png"></p>
					<form class="form-horizontal" id="validForm" action="forgot_change_password.do" method="post" autocomplete="off">
						<input type="hidden" name="responseType" value="ajax" />
						<div class="f-dneglu">
							<a>找回密码</a>
							<span>输入您的手机号和接收到的验证码</span>
						</div>
						<!--账号登录-->
						<div class="from-box">
							<div class="form-group">
								<img src="${ctx}/static/login/img/sj.png"><input type="tel" id="mobile" name="mobile" class="form-control  {required:true,messages:{required:'请输入手机号码'}}" placeholder="请输入手机号码">
								<div class="clearfix"></div>
								<!--<i class="fa fa-user"></i>-->
							</div>
							<div class="clearfix"></div>
							<div class="form-group help">
								<img src="${ctx}/static/login/img/yzm.png"><input type="text" class="form-control yzm {required:true,messages:{required:'请输入验证码',remote:'验证码错误'}}" id="captcha" name="captcha" autocomplete="off" placeholder="验证码">
								<img id="imgCaptcha" class="yzm1" src="${ctx}/captcha" onclick="this.src='${ctx}/captcha?d='+new Date()*1" style="height: 43px;margin-left: 4px;border-radius: 4px;">
								<div class="clearfix"></div>

							</div>
							<div class="clearfix"></div>
							<div class="form-group help">
								<img src="${ctx}/static/login/img/yzm.png"><input type="text" id="verificationCode" name="verificationCode" class="form-control yzm {required:true,messages:{required:'请输入短信验证码'}}" placeholder="短信验证码">
								<a id="btnVerificationCode" href="javascript:void();">获取短信验证码</a>
								<div class="clearfix"></div>

							</div>
							<div class="clearfix"></div>
							<div class="btn">
								<button type="submit" id="btnNext">下一步</button>
							</div>
							<div class="clearfix"></div>
						
							<div class="f-foot">
								<span>已有账号，<a href="${ctx}/admin/login.do">现在登录</a></span>
							</div>
						</div>
					</form>
				</div>
				<div class="clearfix"></div>
				<div class="bottom">
					<a>Copyrignt © 2017 shanyu-tech All Rights Reserved. 智●媒体 版权所有</a>
				</div>
			</div>
		</div>
		<script src="${ctx}/static/vendor/jquery/jquery.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
		<script src="${ctx}/static/vendor/toastr/toastr.min.js"></script>
		<script src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
		<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
		<script src="${ctx}/static/js/plugins.js"></script>
		<script src="${ctx}/static/vendor/adminlte/js/app.min.js"></script>
		<script type="text/javascript">
			var issendverifycode = false;
			var mobile_regex = /^[1][34587][0-9]{9}$/;
			$(function() {

				$("#tel").focus().select();

				$("#validForm").validate({
					success: "valid",
					focusInvalid: false,
					focusCleanup: true,
					onkeyup: false,
					showErrors: function(errorMap, errorList) {
						//this.defaultShowErrors();
						$(errorList).each(function() {
							toastr.warning(this.message);
						});
					},
					success: function(label) {},
					submitHandler: function(form) {
						var formParam = $("#validForm").serialize();

						$("#btnNext").val("正在提交...");
						$("#btnNext").attr("disabled", "disabled");
						$("#btnNext").addClass("disabled");
						$.post("forgot_password_submit.do", formParam, function(result) {
							if(result.status == 1) {
								window.location = "retrieve_password.do";
							} else {
								toastr.warning(result.messages);
							}
							$("#btnNext").removeAttr("disabled");
							$("#btnNext").removeClass("disabled");
							$("#btnNext").val("下一步");
						}, "json").error(function() {

							$("#btnNext").removeAttr("disabled");
							$("#btnNext").removeClass("disabled");
							$("#btnNext").val("下一步");
							toastr.warning("网络异常,请稍后操作");

						});

					}
				});

				document.onkeydown = function(e) {
					var ev = document.all ? window.event : e;
					if(ev.keyCode == 13) {
						$("#btnNext").click();
					}
				}

				$("#btnVerificationCode").click(function() {

					if(issendverifycode) {
						return
					}
					if($("#mobile").val() == "") {
						toastr.warning("请输入手机号码");
						$("#mobile").focus();
						return false;
					} else if((!mobile_regex.test($("#mobile").val()))) {
						toastr.warning("请输入13、14、15、18或17开头的11位手机号码");
						$("#mobile").focus();
						return;
					}

					if($("#captcha").val() == "") {
						toastr.warning("请输入验证码");
						$("#captcha").focus();
						return false;
					}
					issendverifycode = true;
					$(this).text("正在发送...");
					$(this).attr("disabled", "disabled");
					$(this).addClass("disabled");

					$.post("sendforgotpasswordmobile.do", {
						"mobile": $("#mobile").val(),
						"captcha": $("#captcha").val(),
						"responseType": "ajax"
					}, function(result) {
						if(result.status == 1) {
							waiting_fun(60);
						} else if(result.status == 2) {

							$("#btnVerificationCode").text("获取短信验证码");
							$("#btnVerificationCode").removeClass("disabled").text("获取短信验证码");
							$("#btnVerificationCode").removeAttr("disabled");
							issendverifycode = false;
							toastr.warning(result.message);
							$("#imgCaptcha").click();
						} else {
							$("#btnVerificationCode").text("获取短信验证码");
							$("#btnVerificationCode").removeClass("disabled").text("获取短信验证码");
							$("#btnVerificationCode").removeAttr("disabled");
							issendverifycode = false;
							toastr.warning("手机短信验证码获取失败，请稍后操作!");
						}
					}, "json");
					return false;
				});

			});

			var waittings;

			function waiting_fun(time) {
				waittings && window.clearTimeout(waittings);
				if(!(--time)) {
					$("#btnVerificationCode").removeClass("disabled").text("获取短信验证码");
					$("#btnVerificationCode").removeAttr("disabled");
					issendverifycode = false;
					return
				}
				$("#btnVerificationCode").attr("disabled", "disabled");
				$("#btnVerificationCode").addClass("disabled");
				var msg = time + "秒后重新获取";
				$("#btnVerificationCode").text(msg);
				waittings = setTimeout(function() {
					waiting_fun(time);
				}, 1000);
			}
		</script>

	</body>

</html>