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
			重新设置密码--<jsp:include page="/WEB-INF/views/title.jsp" />
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
						<input type="hidden" name="key" value="${key}" />
						<div class="f-dneglu">
							<a>重新设置密码</a>
							<span>输入您的新密码</span>
						</div>
						<!--账号登录-->
						<div class="from-box">
							<div class="form-group">
								<img src="${ctx}/static/login/img/mm.png"><input type="password" id="password" name="password" class="form-control " placeholder="请输入新的密码">
								<div class="clearfix"></div>
								<!--<i class="fa fa-user"></i>-->
							</div>
							<div class="clearfix"></div>
							<div class="form-group">
								<img src="${ctx}/static/login/img/mm.png"><input type="password" id="repassword" name="repassword" class="form-control " placeholder="请输入确认密码">
								<div class="clearfix"></div>
								<!--<i class="fa fa-user"></i>-->
							</div>
							<div class="clearfix"></div>
							<div class="btn">
								<button type="submit" id="btnNext">完成</button>
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
					rules: {
						password: {
							required: true,
							minlength: 6,
							maxlength: 20
						},
						repassword: {
							required: true,
							minlength: 6,
							maxlength: 20,
							equalTo: '#password'
						}
					},
					messages: {
						password: {
							required: "请输入新登录密码",
							minlength: "请输入8~20位字符的密码",
							maxlength: "请输入8~20位字符的密码"
						},
						repassword: {
							required: "请输入确认新密码",
							equalTo: "两次密码输入不一致",
							minlength: "请输入8~20位字符的密码",
							maxlength: "请输入8~20位字符的密码"
						}
					},
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
						$.post("retrieve_password_submit.do", formParam, function(result) {
							if(result.status == 1) {
								window.location = "../login.do";
							} else if(result.status == 401) {

								toastr.warning(result.messages);
								window.location = "forgot_password.do";

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

			});
		</script>

	</body>

</html>