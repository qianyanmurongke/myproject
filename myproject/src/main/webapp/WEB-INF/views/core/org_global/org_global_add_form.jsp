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

<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<tags:search_params />
	<f:hidden name="oid" value="${bean.id}" />

	<div class="box-body">
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label"><s:message
										code="org.parent" /><em
									class="required">*</em></label>
					<div class="col-sm-10">
						<c:set var="parentName">
							<c:choose>
								<c:when test="${empty parent}">
									
								</c:when>
								<c:otherwise>
									<c:out value="${parent.displayName}" />
								</c:otherwise>
							</c:choose>
						</c:set>
								<f:hidden id="parentId" name="parentId" value="${parent.id}" />
								<f:hidden id="parentIdNumber" value="${parent.id}" />
								<div class="input-group">
									<f:text class="form-control" id="parentIdName" value="${parentName}" readonly="readonly" />
									<span class="input-group-btn">
													<button class="btn btn-default" id="parentIdButton"
														type="button">
														<s:message code='choose' />
													</button>
												</span>
								</div>
								<script type="text/javascript">
									$(function() {
										Cms.f7
											.org(
												"parentId",
												"parentIdName", {
													settings: {
														"title": "选择上级院校"
													},
													params: {
														"excludeChildrenId": "${(oprt=='edit') ? (bean.id) : ''}",
														"treeNumber": "${orgTreeNumber}"
													}
												});
									});
								</script>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label"> <s:message code="org.name" /><em
									class="required">*</em></label>
					<div class="col-sm-10">
						<f:text id="name" name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="150" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group" id="distpicker">
					<label class="col-sm-2 control-label">所在地区</label>
					<div class="col-sm-3">
						<select class="form-control" name="province">
							<option value=""></option>
						</select>
					</div>
					<div class="col-sm-3">
						<select class="form-control" name="city">
							<option value=""></option>
						</select>
					</div>
					<div class="col-sm-3">
						<select class="form-control" name="district">
							<option value=""></option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label"><s:message
										code="org.address" /></label>
					<div class="col-sm-10">
						<f:text name="address" value="${bean.address}" class="form-control {maxlength:255}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label">Logo图标</label>
					<div class="col-sm-10">
						<tags:upload_image imageurl="${bean.logoUrl }" title="校徽" saveurl="upload_logo.do" id="logoUrl"></tags:upload_image>
						<p style="color: #f1993a;margin: 0px;">学校展示图用于分享展示贵校风采请尽量上传高清大图</p>
						<p style="color: #f1993a;">支持jpg,png格式的图片，且文件小于20M</p>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="${ctx}/static/vendor/distpicker/distpicker.data.min.js"></script>
	<script src="${ctx}/static/vendor/distpicker/distpicker.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#distpicker").distpicker({
				province: "${bean.province}",
				city: "${bean.city}",
				district: "${bean.district}",
			});
			$("#validForm").validate();
			$("input[name='name']").focus();

		});
	</script>
</form>