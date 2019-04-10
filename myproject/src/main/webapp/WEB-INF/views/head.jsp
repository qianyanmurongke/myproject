<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Expires" content="0" />
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>
	<jsp:include page="/WEB-INF/views/title.jsp" />
</title>
<meta name="renderer" content="webkit">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap/css/bootstrap-dialog.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/ionicons/css/ionicons.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/jquery-ui/jquery-ui.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/jquery-minicolors/jquery.minicolors.css">
<link rel="stylesheet" href="${ctx}/static/vendor/ztree/css/metroStyle/metroStyle.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/AdminLTE.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/adminlte/css/skins/skin-blue.min.css">
<link rel="stylesheet" href="${ctx}/static/vendor/swfupload/process.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/editormd/css/editormd.min.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/toastr/toastr.min.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/sweetalert/css/sweet-alert.css" />
<link rel="stylesheet" href="${ctx}/static/css/main.css">
<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap-select/css/bootstrap-select.min.css">
<style>
	.form-group .bootstrap-select.show-tick {
		width: 200px;
		margin-top: -10px;
	}
	
	.table-responsive {
		overflow-x: inherit;
	}
	
	.dropdown-menu {
		z-index: 1800;
	}
</style>
<!--[if lt IE 9]>
<script src="${ctx}/static/vendor/html5shiv/html5shiv.min.js"></script>
<script src="${ctx}/static/vendor/respond/respond.min.js"></script>
<![endif]-->
<script>
	CTX = "${ctx}";
	CMSCP = "/admin";
	window.UEDITOR_HOME_URL = "${ctx}/static/vendor/ueditor/";
</script>
<script src="${ctx}/static/vendor/jquery/jquery-2.2.3.min.js"></script>
<script src="${ctx}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx}/static/vendor/bootstrap/js/bootstrap-dialog.min.js"></script>
<script src="${ctx}/static/vendor/jquery-ui/jquery-ui.js"></script>
<script src="${ctx}/static/vendor/jquery-validation/jquery-validation.min.js"></script>
<script src="${ctx}/static/js/jquery.validation_zh_CN.js"></script>
<script src="${ctx}/static/vendor/jquery-form/jquery-form.min.js"></script>
<script src="${ctx}/static/vendor/jquery-ajaxfileuploader/jquery-ajaxfileuploader.min.js"></script>
<script src="${ctx}/static/vendor/jquery-minicolors/jquery.minicolors.min.js"></script>
<script src="${ctx}/static/vendor/jquery-cookie/jquery-cookie.min.js"></script>
<script src="${ctx}/static/vendor/ztree/js/jquery.ztree.all.min.js"></script>
<script src="${ctx}/static/vendor/My97DatePicker/cn_WdatePicker.js"></script>
<script src="${ctx}/static/vendor/swfupload/swfupload.js"></script>
<script src="${ctx}/static/vendor/swfupload/swfupload.queue.js"></script>
<script src="${ctx}/static/vendor/swfupload/fileprogress.js"></script>
<script src="${ctx}/static/vendor/swfupload/handlers.js"></script>
<script src="${ctx}/static/vendor/ueditor/ueditor.config.js"></script>
<script src="${ctx}/static/vendor/ueditor/ueditor.all.min.js"></script>
<script src="${ctx}/static/vendor/ueditor/lang/zh-cn/zh-cn.js"></script>
<script src="${ctx}/static/vendor/editormd/editormd.min.js"></script>
<script src="${ctx}/static/js/editormd.plugin.js"></script>
<script src="${ctx}/static/vendor/to-markdown.js"></script>
<script src="${ctx}/static/vendor/echarts.common.min.js"></script>
<script src="${ctx}/static/js/plugins.js"></script>
<script src="${ctx}/static/js/course_choose.js"></script>
<script src="${ctx}/static/vendor/toastr/toastr.min.js"></script>
<script src="${ctx}/static/vendor/sweetalert/js/sweet-alert.min.js"></script>
<script src="${ctx}/static/vendor/bootstrap-select/js/bootstrap-select.min.js"></script>
<script src="${ctx}/static/vendor/bootstrap-select/js/i18n/defaults-zh_CN.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/common.plugin.js"></script>