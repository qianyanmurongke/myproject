<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="f" uri="http://www.course.com/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/WEB-INF/views/head.jsp" />
		<script>
			$(function() {
				$("input.minicolors").minicolors();
				$("#validForm").validate({
					submitHandler: function(form) {
						if(!$("#linkCheck").is(":checked")) {
							$("#link").val("");
						}
						form.submit();
					}
				});
				$("input[name='title']").focus();
			});

			function imgCrop(name) {
				if($("#" + name).val() == "") {
					alert("<s:message code='noImageToCrop'/>");
					return;
				}
				Cms.imgCrop("../../commons/img_area_select.do", name);
			}



			function confirmDelete() {
				return confirm("<s:message code='confirmDelete'/>");
			}
		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<c:set var="usernameExist"><s:message code="info.management" /></c:set>
		<div class="content-header">
			<h1>${node.name} - <s:message code="${oprt=='edit' ? 'edit' : 'create'}"/><c:if test="${oprt=='edit'}"> <small>(<s:message code="info.status"/>: <s:message code="info.status.${bean.status}"/>, ID:${bean.id})</small></c:if></h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
					<tags:search_params/>
					<f:hidden id="nodeId" name="nodeId" value="${node.id}" />
					<f:hidden id="nodeIdNumber" value="${node.id}" />
					<f:hidden name="queryNodeId" value="${queryNodeId}" />
					<f:hidden name="queryNodeType" value="${queryNodeType}" />
					<f:hidden name="queryInfoPermType" value="${queryInfoPermType}" />
					<f:hidden id="queryStatus" name="queryStatus" value="${queryStatus}" />
					<f:hidden name="oid" value="${bean.id}" />
					<f:hidden name="position" value="${position}" />
					<input type="hidden" id="redirect" name="redirect" value="edit" />

					<div class="box-body">
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label for="realName" class="col-sm-2 control-label">标题<em class="required">*</em></label>
									<div class="col-sm-10">
										<f:text name="title" value="${bean.title}" class="required form-control" maxlength="150" />

									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label for="realName" class="col-sm-4 control-label">是否置顶</label>
									<div class="col-sm-8">
										<select name="priority" class="form-control">
											<option value="0" <c:if test="${0==bean.priority}"> selected="selected"</c:if>>否</option>
											<option value="999" <c:if test="${999==bean.priority}"> selected="selected"</c:if>>是</option>

										</select>

									</div>
								</div>
							</div>

							<div class="col-sm-6">
								<div class="form-group">
									<label for="realName" class="col-sm-4 control-label">发布时间</label>

									<div class="col-sm-8">
										<div class="input-group">
											<input type="text" name="publishDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.publishDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control ${oprt=='edit' ? 'required' : ''}" style="padding-left:3px;padding-right:3px;" />
											<span class="input-group-addon" style="padding-left:3px;padding-right:3px;"><s:message code="info.to"/></span>
											<input type="text" name="offDate" value="<c:if test="${oprt=='edit'}"><fmt:formatDate value="${bean.offDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>" onclick="WdatePicker({dateFmt:'yyyy-MM-ddTHH:mm:ss'});" class="form-control" style="padding-left:3px;padding-right:3px;" />
										</div>
									</div>

								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label for="realName" class="col-sm-2 control-label">封面图片</label>
									<div class="col-sm-10">
										<tags:image_upload name="smallImage" value="${bean.smallImage}" width="${field.customs['imageWidth']}" height="${field.customs['imageHeight']}" watermark="${field.customs['imageWatermark']}" scale="${field.customs['imageScale']}" exact="${field.customs['imageExact']}" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div id="editor_text"></div>
							</div>
							<script type="text/javascript">
								var ueditor_text;

								function create_ueditor_text(content) {
									$("#editor_text").append("<script id='clobs_text' name='clobs_text' type='text/plain'><\/script>");
									ueditor_text = UE.getEditor("clobs_text", {

										serverUrl: "/admin/core/ueditor.do?ueditor=true"
									});
									ueditor_text.ready(function() {
										ueditor_text.setContent(content);
									});
								}

								function delete_ueditor_text() {
									var content = toMarkdown(ueditor_text.getContent());
									UE.delEditor("clobs_text");
									$("#clobs_text").remove();
									$('.edui-default').remove();
									return content;
								}
								var editormd_text;

								function create_editormd_text(markdown) {
									$("#editor_text").append("<div id='clobs_text'></div>");
									editormd_text = editormd("clobs_text", {
										name: "clobs_text_markdown",
										markdown: markdown,

										toolbarIcons: editormd.toolbar_StandardPage,

										width: "100%",
										height: 360,
										watch: false,
										autoFocus: false,
										lineNumbers: false,
										styleActiveLine: true,
										styleSelectedText: true,
										sequenceDiagram: true,
										codeFold: false,
										placeholder: "",
										syncScrolling: "single",
										path: "/static/vendor/editormd/lib/",
										saveHTMLToTextarea: true,
										imageUpload: true,
										imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
										imageUploadURL: "/admin/core/upload_image.do?editormd=true",
										onfullscreen: function() {
											this.watch();
										},
										onfullscreenExit: function() {
											this.unwatch();
										},
										onload: function() {
											base64UploadPlugin(this, "/admin/core/upload_image.do");
										}
									});
								}

								function delete_editormd_text() {
									var content = editormd_text.getHTML();
									editormd_text.editor.remove();
									return content;
								}

								create_ueditor_text("${fnx:escapeEcmaScript(bean.clobs['text'])}");
							</script>

						</div>

					</div>

					<div class="box-footer">
						<button class="btn btn-primary" type="submit" <c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled</c:if>><s:message code="save"/></button>
						<c:if test="${oprt=='create'}">
							<input type="hidden" id="draft" name="draft" value="false" />
							<button class="btn btn-default" type="submit" onclick="$('#draft').val('true');"><s:message code="info.saveAsDraft"/></button>
						</c:if>
						<c:if test="${oprt=='edit'}">
							<input type="hidden" id="pass" name="pass" value="false" />
							<button class="btn btn-default" type="submit" onclick="$('#pass').val('true');" <c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled</c:if>><s:message code="info.saveAndPass"/></button>
						</c:if>
						<button class="btn btn-default" type="submit" onclick="$('#redirect').val('list');" <c:if test="${oprt=='edit' && !bean.auditPerm}"> disabled="disabled"</c:if>><s:message code="saveAndReturn"/></button>
						<c:if test="${oprt=='create'}">
							<button class="btn btn-default" type="submit" onclick="$('#redirect').val('create');"><s:message code="saveAndCreate"/></button>
						</c:if>
					</div>
				</form>
			</div>
		</div>

	</body>

</html>