<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/WEB-INF/views/head.jsp" />
		<style>
			.dl-horizontal dt {
				width: 70px;
				line-height: 2;
				font-size: 14px;
				border-bottom: 1px dashed #bce8f1;
				margin-bottom: -1px;
			}
			
			.dl-horizontal dd {
				margin-left: 70px;
				padding-left: 10px;
				line-height: 2;
				font-size: 14px;
				border-bottom: 1px dashed #bce8f1;
			}
		</style>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1><s:message code="homepage.personal"/></h1>
		</div>
		<div class="content">
			<div class="nav-tabs-custom">
				<jsp:include page="types.jsp" />
				<div class="tab-content">
					<div class="box box-solid" style="box-shadow: none;">
						<div class="box-body">
							<h3>当前我的头像<br> <small>如果您还没有设置自己的头像，系统会显示默认头像，您需要自己上传一张照片来作为自己的个人头像</small></h3>
							<div class="media">
								<div class="media-left" style="vertical-align: bottom;">
									<a href="javasript;;" class="ad-click-event">
										<img src="/uploads/users/${user.id}/avatar_large.jpg" onerror="this.src='${ctx}/static/vendor/crop/img/user.png';this.onerror='';" alt="${user.realName}" class="media-object" style="width: 220px;height: auto;border-radius: 4px;box-shadow: 0 1px 3px rgba(0,0,0,.15);">
									</a>

								</div>
								<div class="media-left" style="vertical-align: bottom;">
									<a href="javasript;;" class="ad-click-event">
										<img src="/uploads/users/${user.id}/avatar_large.jpg" onerror="this.src='${ctx}/static/vendor/crop/img/user.png';this.onerror='';" alt="${user.realName}" class="media-object" style="width: 120px;height: auto;border-radius: 4px;box-shadow: 0 1px 3px rgba(0,0,0,.15);">
									</a>

								</div>
								<div class="media-left" style="vertical-align: bottom;">
									<a href="javasript;;" class="ad-click-event">
										<img src="/uploads/users/${user.id}/avatar_small.jpg" onerror="this.src='${ctx}/static/vendor/crop/img/user.png';this.onerror='';" alt="${user.realName}" class="media-object" style="width: 50px;height: auto;border-radius: 4px;box-shadow: 0 1px 3px rgba(0,0,0,.15);">
									</a>
								</div>
							</div>
						</div>
						<div class="box-footer">
							<h4>设置我的新头像<br> <small>请选择一个新照片进行上传编辑，头像保存后，您可能需要刷新一下本页面，才能查看最新的头像效果。</small></h4>

							<link href="${ctx}/static/vendor/crop/dist/cropper.min.css" rel="stylesheet">
							<link href="${ctx}/static/vendor/crop/css/main.css" rel="stylesheet">
							<script src="${ctx}/static/vendor/crop/dist/cropper.js"></script>
							<script src="${ctx}/static/vendor/crop/js/main.js"></script>
							<style>
								.crop-avatar-dialog .modal-dialog {
									width: 900px;
								}
							</style>

							<button class="btn btn-primary" type="button" value="" onclick="uploadAvatar();return false;">
								<i class="glyphicon glyphicon-user"></i>上传头像
							</button>
							<div style="width: 0px;height: 0px;display: none;" class="crop-avatar" id="crop-avatar" data-id="avatar" data-title="${title}" data-saveurl="${ctx}/admin/core/user/upload_avatar.do">

								<!-- Current avatar -->
								<div class="avatar-view" title="选择头像">
									<img src="/uploads/users/${user.id}/avatar_large.jpg" onerror="this.src='${ctx}/static/vendor/crop/img/user.png';this.onerror='';" alt="头像">
								</div>
								<input type="hidden" name="avatar" value="/uploads/users/${user.id}/avatar_large.jpg" id="avatar" />
								<!-- Loading state -->
								<div class="loading" aria-label="Loading" role="img" tabindex="-1"></div>
							</div>

							<script>
								function uploadAvatar() {
									$('#crop-avatar img').click();
								}
								$(function() {
									new $CropAvatar($('#crop-avatar'));
								});
							</script>
						</div>
					</div>

				</div>
			</div>
		</div>

	</body>

</html>