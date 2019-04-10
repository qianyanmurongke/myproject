<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>
			<jsp:include page="/WEB-INF/views/title.jsp" />
		</title>
		<meta name="renderer" content="webkit">
		<!-- <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> -->
		<script>
			if(top != this) {
				top.location = this.location;
			}
		</script>
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap-dialog.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/ionicons/css/ionicons.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/jquery-ui/jquery-ui.css" />
		<link rel="stylesheet" href="${ctx}/static/vendor/ztree/css/metroStyle/metroStyle.css" />
		<link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/AdminLTE.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/skins/_all-skins.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/toastr/toastr.min.css" />
		<link rel="stylesheet" href="${ctx}/static/vendor/sweetalert/css/sweet-alert.css" />
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap-select/css/bootstrap-select.min.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap-switch.min.css">
		<link rel="stylesheet" href="${ctx}/static/css/main.css">
		<!--[if lt IE 9]>
  <script src="${ctx}/static/vendor/html5shiv/html5shiv.min.js"></script>
  <script src="${ctx}/static/vendor/respond/respond.min.js"></script>
  <![endif]-->
		<style>
			::-webkit-scrollbar {
				width: 5px;
				height: 5px;
			}
			
			 ::-webkit-scrollbar-thumb {
				background-color: #999;
				-webkit-border-radius: 5px;
				border-radius: 5px;
			}
			
			 ::-webkit-scrollbar-thumb:vertical:hover {
				background-color: #666;
			}
			
			 ::-webkit-scrollbar-thumb:vertical:active {
				background-color: #333;
			}
			
			 ::-webkit-scrollbar-button {
				display: none;
			}
			
			 ::-webkit-scrollbar-track {
				background-color: #f1f1f1;
			}
		</style>
	</head>

	<body class="hold-transition skin-blue sidebar-mini fixed " data-spy="scroll" data-target="#scrollspy" style="background-color: #ecf0f5;">
		<!--  style="overflow:visible;" -->
		<div class="wrapper">
			<header class="main-header">
				<!-- Logo -->
				<a href="core/homepage/welcome.do" target="center" class="logo">
					<!-- mini logo for sidebar mini 50x50 pixels -->
					<span class="logo-mini"><b></b>智慧</span>
					<!-- logo for regular state and mobile devices -->
					<span class="logo-lg"><b>智慧</b>门禁</span>
				</a>

				<!-- Header Navbar -->
				<nav class="navbar navbar-static-top" role="navigation">
					<!-- Sidebar toggle button-->
					<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">Toggle navigation</span>
					</a>
					<!-- Navbar Right Menu -->
					<div class="navbar-custom-menu">
						<ul class="nav navbar-nav">
							<li style="padding:10px 5px 0 ;">
							</li>
							<li class="user-menu dropdown">
								<a href="${ctx}/admin/core/homepage/personal_view.do" target="center">
									<!-- The user image in the navbar-->
									<img src="${ctx}${user.avatar}" onerror="this.src='${ctx}/static/img/default_avatar.png';this.onerror='';" class="user-image" alt="User Image">
									<!-- hidden-xs hides the username on small devices so only the image appears. -->
									<span class="hidden-xs">${user.realName}</span>
								</a>
							</li>
							<li>
								<a href="logout.do" title="退出"> <span>退出</span> <i class="glyphicon glyphicon-log-out"></i>
								</a>
							</li>
						</ul>
					</div>
				</nav>
			</header>
			<!-- Left side column. contains the logo and sidebar -->
			<aside class="main-sidebar">
				<!-- sidebar: style can be found in sidebar.less -->
				<section class="sidebar" id="scrollspy">

					<!-- Sidebar Menu -->
					<ul class="sidebar-menu">
						<!-- <li class="header">功能导航</li> -->
						<c:forEach var="menu" varStatus="status" items="${menus}">
							<c:if test="${menu.id != 0 }">
							<shiro:hasPermission name="${menu.perm}">
								<c:choose>
									<c:when test="${fn:length(menu.children)>0}">
										<li class="treeview<c:if test=" ${status.index==0} "> active</c:if>">
											<a href='javascript:;'><i class="${empty menu.icon ? 'fa fa-circle-o' : menu.icon}"></i>
												<span><s:message code="${menu.name}"
													text="${menu.name}" /></span> <span class="pull-right-container">
												<i class="fa fa-angle-left pull-right"></i>
										</span> </a>
											<ul class="treeview-menu">
												<c:forEach var="m" varStatus="status" items="${menu.children}">
													<shiro:hasPermission name="${m.perm}">
														<c:choose>
															<c:when test="${fn:length(m.children)>0}">
																<li class="treeview">
																	<a href='javascript:;'><i class="fa fa-circle-o"></i>
																		<span><s:message code="${m.name}"
																			text="${m.name}" /></span> <span class="pull-right-container"> <i
																		class="fa fa-angle-left pull-right"></i>
																</span> </a>
																	<ul class="treeview-menu">
																		<c:forEach var="c" varStatus="status" items="${m.children}">
																			<shiro:hasPermission name="${c.perm}">
																				<c:choose>
																					<c:when test="${fn:length(c.children)>0}">
																						<li class="treeview">
																							<a href='javascript:;'><i class="fa fa-circle-o"></i>
																								<span><s:message code="${c.name}"
																									text="${c.name}" /></span> <span class="pull-right-container"> <i
																								class="fa fa-angle-left pull-right"></i>
																							</span> </a>
																							<ul class="treeview-menu">
																								<c:forEach var="d" varStatus="status" items="${c.children}">
																									<shiro:hasPermission name="${d.perm}">
																										<li>
																											<a href="javascript:nav('${d.centerUrl}','${d.leftUrl}');"><i class="${empty d.icon ? 'fa fa-circle-o' : d.icon}"></i>
																												<span><s:message code="${d.name}"
																												text="${d.name}" /></span></a>
																										</li>
																									</shiro:hasPermission>
																								</c:forEach>
																							</ul>
																						</li>
																					</c:when>
																					<c:otherwise>
																						<li>
																							<a href="javascript:nav('${c.centerUrl}','${c.leftUrl}');"><i class="fa fa-circle-o"></i>
																								<span><s:message code="${c.name}"
																								text="${c.name}" /></span></a>
																						</li>
																					</c:otherwise>
																				</c:choose>
																			</shiro:hasPermission>
																		</c:forEach>
																	</ul>
																</li>
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${fn:startsWith(m.perm,'core:user_global:') || fn:startsWith(m.perm,'core:org_global:') || fn:startsWith(m.perm,'core:site:') || fn:startsWith(m.perm,'core:conf_global:')}">
																		<shiro:hasRole name="super">
																			<li>
																				<a href="javascript:nav('${m.centerUrl}','${m.leftUrl}');"><i class="fa fa-circle-o"></i>
																					<span><s:message code="${m.name}"
																						text="${m.name}" /></span></a>
																			</li>
																		</shiro:hasRole>
																	</c:when>
																	<c:otherwise>
																		<li>
																			<a href="javascript:nav('${m.centerUrl}','${m.leftUrl}');"><i class="fa fa-circle-o"></i>
																				<span><s:message code="${m.name}"
																					text="${m.name}" /></span></a>
																		</li>
																	</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose>
													</shiro:hasPermission>
												</c:forEach>
											</ul>
										</li>
									</c:when>
									<c:otherwise>
										<li>
											<a href="javascript:nav('${menu.centerUrl}','${menu.leftUrl}');"><i class="${empty menu.icon ? 'fa fa-circle-o' : menu.icon}"></i>
												<span><s:message code="${menu.name}"
													text="${menu.name}" /></span></a>
										</li>
									</c:otherwise>
								</c:choose>
							</shiro:hasPermission>
							</c:if>
						</c:forEach>
					</ul>
					<!-- /.sidebar-menu -->
				</section>
				<!-- /.sidebar -->
			</aside>

			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper clearfix">
				<div id="leftContentWrapper" class="pull-left" style="width: 0; position: relative;">
					<iframe class="" id="leftFrame" name="left" style="width: 100%; min-height: 100%; border: 0;" src="blank.do"></iframe>
				</div>
				<div id="centerContentWrapper" class="" style="margin-left: 0; position: relative;">
					<iframe class="" id="centerFrame" name="center" scrolling="no" style="width: 100%; min-height: 100%; border: 0;" src="core/homepage/welcome.do"></iframe>
				</div>
			</div>
			<!-- /.content-wrapper -->

		</div>
		<!-- ./wrapper -->

		<script src="${ctx}/static/vendor/jquery/jquery-2.2.3.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap-dialog.min.js"></script>
		<script src="${ctx}/static/vendor/jquery-ui/jquery-ui.js"></script>
		<script src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
		<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
		<script src="${ctx}/static/vendor/jquery-form/jquery-form.min.js"></script>
		<script src="${ctx}/static/vendor/My97DatePicker/cn_WdatePicker.js"></script>
		<script src="${ctx}/static/vendor/adminlte/js/app.min.js"></script>
		<script src="${ctx}/static/vendor/jQuery-slimScroll-master/jquery.slimscroll.min.js"></script>
		<script src="${ctx}/static/vendor/toastr/toastr.min.js"></script>
		<script src="${ctx}/static/vendor/sweetalert/js/sweet-alert.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap-select/js/bootstrap-select.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap-select/js/i18n/defaults-zh_CN.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap/js/bootstrap-switch.min.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/bootstrap-utils.js"></script>
		<script src="${ctx}/static/vendor/ztree/js/jquery.ztree.all.min.js"></script>
		<script>
			$(function() {

				$(".treeview-menu>li:not(.treeview)>a").click(
					function() {
						$(this).parent().parent().find("li").removeClass(
							"active");
						$(this).parent().parent().parent().parent().find("li")
							.not($(this).parent().parent().parent()[0])
							.removeClass("active");
						$(this).parent().addClass("active");
					});
			});
			//var leftFrame = window.frames['left'];
			//var centerFrame = window.frames['center'];
			function nav(centerUrl, leftUrl) {
				//先移除内容，以免调整框架宽度时页面晃动
				try {
					$(window.frames['center'].document.body).empty();
				} catch(ex) {}

				if(leftUrl) {
					//$(window.frames['leftFrame'].document.body).empty();
					if($("#leftContentWrapper").css("width") != "220px") {
						$("#leftContentWrapper").css("width", "220px");
						$("#centerContentWrapper").css("margin-left", "220px");
					}
					window.frames['left'].location.href = leftUrl;
				} else {
					if($("#leftContentWrapper").css("width") != "0px") {
						$("#leftContentWrapper").css("width", "0px");
						$("#centerContentWrapper").css("margin-left", "0px");
						window.frames['left'].location.href = "blank.do";
					}
				}
				window.frames['center'].location.href = centerUrl;
			}
			$(function() {
				function contentResize() {
					var minHeight = $(".content-wrapper").css("min-height");
					$("#leftContentWrapper").css("min-height", minHeight);
					$("#centerContentWrapper").css("min-height", minHeight);
				}
				contentResize();
				$(window).resize(function() {
					contentResize();
				});
				var centerFrame = document.getElementById("centerFrame");
				var leftFrame = document.getElementById("leftFrame");
				var frameHeightInterval = setInterval(function() {
					try {
						var centerHeight = $(window.frames['center'].document.body)
							.height();
						var leftHeight = $(window.frames['left'].document.body)
							.height();
						var minHeight = parseInt($(".content-wrapper").css(
							"min-height")) - 5;
						var height = centerHeight > leftHeight ? centerHeight :
							leftHeight;
						height = height > minHeight ? height : minHeight;
						centerFrame.height = height;
						leftFrame.height = height;
					} catch(ex) {
						//clearInterval(frameHeightInterval);
					}
				}, 200);
			})

			function keepSession() {
				$.get("${ctx}/keep_session?d=" + new Date() * 1);
			}
			setInterval(keepSession, 600000);
		</script>
	</body>

</html>