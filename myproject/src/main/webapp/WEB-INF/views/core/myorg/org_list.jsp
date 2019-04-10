<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>

	<head>
	<head>
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
		<link rel="stylesheet" href="${ctx}/static/vendor/sweetalert/css/sweetalert.css" />
		<link rel="stylesheet" href="${ctx}/static/css/main.css">
		<link rel="stylesheet" href="${ctx}/static/vendor/bootstrap-select/css/bootstrap-select.min.css">
	
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
		<script src="${ctx}/static/vendor/sweetalert/js/sweetalert-dev.js"></script>
		<script src="${ctx}/static/vendor/bootstrap-select/js/bootstrap-select.min.js"></script>
		<script src="${ctx}/static/vendor/bootstrap-select/js/i18n/defaults-zh_CN.min.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/common.plugin.js"></script>
	</head>

		<link rel="stylesheet" href="${ctx}/static/css/org/org.css">
		<script type="text/javascript">
			$(function() {
				$("#sortHead").headSort();
				<shiro:hasPermission name="core:org:edit">
					$("#pagedTable tbody tr").dblclick(function(eventObj) {
						var nodeName = eventObj.target.nodeName.toLowerCase();
						if(nodeName != "input" && nodeName != "select" && nodeName != "textarea") {
							location.href = $("#edit_opt_" + $(this).attr("beanid")).attr('href');
						}
					}); 
					</shiro:hasPermission>
			});


			//回调同步
			function Synchronization(callback){
		
				swal({
					title: "确定同步",		
					type: "warning",
					showCancelButton: true,
					closeOnConfirm: false,
					confirmButtonColor: "#3085d6",
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					showLoaderOnConfirm: true
				}, function(isconfirm) {
					
					if(isconfirm){
				
					callback();
					
					}					
					
				});
				
			}
	

			function ShowEdit(url, isedit) {
				var paramTitle = (!isedit) ? '学校<s:message code="create" />' : '学校<s:message code="edit" />'								
				
				BootstrapDialog.showDialog({
					title: paramTitle,
					loadUrl: url,
					cssClass: 'chooseemployee-dialog',
					buttons: [{
						label: '微信消息模板同步',
						icon: 'glyphicon fa fa-refresh',
						cssClass: 'btn-success',
						action: function(dialog) {
								
							var item = dialog.getModalBody();				
							
							if($(item).find("#publicNumber").val() == ""){
								toastr.warning("微信公众号不能为空！")								
								return;
							}	
								
							Synchronization(function(){	
																				
							$.post("templatesynchronization.do", {
									"responseType": "ajax"
								}, function(result) {
									if(result.status == 1) {
										swal("同步成功", "微信消息模板已同步", "success");									
										
									} else {
										swal("提交失败", "" + result.messages + "", "error");
									}
								}, "json").error(function(err) {
									swal("提交失败", "提交失败！请稍后重试", "error");
								})
							
							
							
							})													
					
						}
					},{
						label: '<s:message code="save" />',
						icon: 'glyphicon glyphicon-floppy-saved',
						cssClass: 'btn-success',
						cssClass: 'btn-success',
						action: function(dialog) {
							var $button = this;
							$button.disable();
							$button.spin();
							var item = dialog.getModalBody();

							if($(item).find("#name").val() == "") {
								toastr.warning('学校名称不能为空。');
								$button.enable();
								$button.stopSpin();
								return;
							}

							var formParam = $(item).find("#validForm").serialize();
							formParam += "&responseType=ajax";

							$.post($(item).find("#validForm").attr("action"), formParam, function(result) {
								if(result.status == 0) {

									toastr.warning(result.messages);
									
									$button.enable();
									$button.stopSpin();
								} else {
									toastr.success('提交数据成功');
									dialog.close();
									window.location.reload();
								}
							}, "json").error(function(err) {

								toastr.error("网络异常,请稍后操作");
								$button.enable();
								$button.stopSpin();

							});

						}
					}, {
						label: '关闭',
						icon: 'glyphicon glyphicon-remove',
						action: function(dialog) {
							dialog.close();
						}
					}]
				});

			}

		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		
		<div class="content">

			<div class="box box-primary">
				<div class="box-body table-responsive">
				<div class="top">
					<div class="logo">
						<div class="l-left">												
							<img src="${org.logoUrl}" onerror="this.src='${ctx}/static/img/org_logo.png';this.onerror='';">							
						
							
						</div>
						<div class="l-right">
							<h2 style="font-size: 40px;">${org.fullName}</h2>
							<p>${org.ename}</p>
						</div>

						<div class="clearfix"></div>
					</div>
					<shiro:hasPermission name="core:myorg:edit">
					<div class="modify" onclick="ShowEdit('edit.do?id=${org.id}', true);return false;" style="background-color: #00E765;color: white;">																		
						修改
					</div>
						</shiro:hasPermission>
					<div class="clearfix"></div>
				</div>
				<ul class="information">
					<li>
						<h2>建校年月</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p><fmt:formatDate value="${org.foundingDate}" pattern="yyyy年MM月dd日" /></p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>组织机构号码</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>						
						<p>${org.organizationNumber}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>电子邮箱</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${org.email }</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>传真</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${org.fax}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>主页地址</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p><a href="http://${org.webAddress}"  target="_blank">${org.webAddress}</a></p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>所在地区</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${org.province}-${org.city}-${org.district}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>详细地址</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${org.province}${org.city}${org.district}${org.address}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>联系电话</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${org.phone}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>班级数量</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${classNumber}</p>
						<div class="clearfix"></div>
					</li>
					<li>
						<h2>教师数量</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${employeeNumber}人(男:${emloyeeGender}人  / 女:${employeeNumber-emloyeeGender}人)   </p>
						<div class="clearfix"></div>
					</li>					
					<li>
						<h2>学生数量</h2>
						<span style="width: 1px;height: 8px; background: #666;  margin: 22px 20px  10px  20px;float: left;"></span>
						<p>${studentNumber}人(男:${studentGender}人  / 女:${studentNumber-studentGender}人)</p>
						<div class="clearfix"></div>
					</li>
				</ul>
				<div class="box-body table-responsive">

				</div>
			</div>
			</div>
		</div>
	</body>

</html>