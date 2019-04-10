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
		<jsp:include page="/WEB-INF/views/head.jsp" />
		<script src="${ctx}/static/vendor/jquery-validation/jquery.validate.ext.js"></script>
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

			function confirmDelete() {
				return confirm("<s:message code='confirmDelete'/>");
			}
		</script>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1>
			<s:message code="org.management" />
			-
			<s:message code="${oprt=='edit' ? 'edit' : 'create'}" />
		</h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
					<tags:search_params />
					<f:hidden name="oid" value="${bean.id}" />
					<f:hidden name="position" value="${position}" />
					<input type="hidden" id="redirect" name="redirect" value="edit" />

					<div class="box-body">
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label class="col-sm-2 control-label"><s:message
										code="org.parent" /></label>
									<div class="col-sm-10">
										<c:set var="parentName">
											<c:choose>
												<c:when test="${empty parent}">
													<s:message code="org.root" />
												</c:when>
												<c:otherwise>
													<c:out value="${parent.displayName}" />
												</c:otherwise>
											</c:choose>
										</c:set>
										<c:choose>
											<c:when test="${orgTreeNumber eq bean.treeNumber}">
												<f:hidden id="parentId" name="parentId" value="${parent.id}" />
												<p class="form-control-static">${parentName}</p>
											</c:when>
											<c:otherwise>
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
																		"title": "<s:message code='org.f7.selectOrg'/>"
																	},
																	params: {
																		"excludeChildrenId": "${(oprt=='edit') ? (bean.id) : ''}",
																		"treeNumber": "${orgTreeNumber}"
																	}
																});
													});
												</script>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required">*</em> <s:message code="org.name" /></label>
									<div class="col-sm-8">
										<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="150" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><s:message
										code="org.number" /></label>
									<div class="col-sm-8">
										<f:text name="number" value="${oprt=='edit' ? bean.number : ''}" class="form-control" maxlength="100" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.englishname" /></label>
									<div class="col-sm-8">
										<f:text name="ename" value="${bean.ename }" class="form-control" maxlength="150" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required">*</em> <s:message
										code="org.organizationnumber" /></label>
									<div class="col-sm-8">
										<f:text name="organizationNumber" class="form-control required" maxlength="150" value="${bean.organizationNumber }" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><s:message
										code="org.phone" /></label>
									<div class="col-sm-8">
										<f:text name="phone" value="${bean.phone}" maxlength="100" class="form-control" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><s:message
										code="org.fax" /></label>
									<div class="col-sm-8">
										<f:text name="fax" value="${bean.fax}" maxlength="100" class="form-control" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required">*</em> <s:message code="org.schoolyear"></s:message></label>
									<div class="col-sm-8">
										<div class="input-group">
											<input class="form-control" name="foundingDate" value="<fmt:formatDate value='${bean.foundingDate }' pattern='yyyy-MM-dd' />" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
											<div class="input-group-addon">
												<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required">*</em> <s:message code="org.decorationday"></s:message></label>
									<div class="col-sm-8">
										<div class="input-group">
											<input class="form-control" name="decorationDay" value="<fmt:formatDate value='${bean.decorationDay }' pattern='yyyy-MM-dd' />" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
											<div class="input-group-addon">
												<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message
										code="org.legalrepresentative" /></label>
									<div class="col-sm-8">
										<f:text name="legalRepresentative" value="${bean.legalRepresentative }" class="form-control required" maxlength="150" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>邮编</label>
									<div class="col-sm-8">
										<f:text name="zipcode" value="${bean.zipcode }" class="form-control" maxlength="150" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.corporateidentity" /></label>
									<div class="col-sm-8">
										<f:text name="legalIdcard" value="${bean.legalIdcard }" class="form-control isIdCardNo" maxlength="150" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.corporatenumber" /></label>
									<div class="col-sm-8">
										<f:text name="legalNumber" value="${bean.legalNumber }" class="form-control required" maxlength="150" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.schoolmanage" /></label>
									<div class="col-sm-8">
										<select name="orgTypeId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList }" itemValue="id" selected="${bean.orgType }" />
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.companymanage" /></label>
									<div class="col-sm-8">
										<select name="unitTypeId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList1}" itemValue="id" selected="${bean.unitType }" />
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.type" /></label>
									<div class="col-sm-8">
										<select name="schoolTypeId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList2}" itemValue="id" selected="${bean.schoolType }" />
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em> <s:message code="org.district" /></label>
									<div class="col-sm-8">
										<select name="nationId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList3}" itemValue="id" selected="${bean.nation }" />
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>城乡类型码</label>
									<div class="col-sm-8">
										<select name="cityTypeId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList4}" itemValue="id" selected="${bean.cityType }" />
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>经济属性码</label>
									<div class="col-sm-8">
										<select name="economicsId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList5}" itemValue="id" selected="${bean.economics }" />
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>主教学语言码</label>
									<div class="col-sm-8">
										<select name="primaryLanguageId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList6}" itemValue="id" selected="${bean.primaryLanguage }" />
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>辅教学语言码</label>
									<div class="col-sm-8">
										<select name="assistedLanguageId" class="form-control input-sm">
											<option value="">--请选择--</option>
											<f:options itemLabel="name" items="${itemList7}" itemValue="id" selected="${bean.assistedLanguage }" />
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label"><em
									class="required"></em>电子邮箱</label>
									<div class="col-sm-8">
										<f:text name="email" value="${bean.email }" class="form-control" maxlength="150" />
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="col-sm-4 control-label">主页地址</label>
									<div class="col-sm-8">
										<f:text name="webAddress" value="${bean.webAddress }" class="form-control url" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group" id="distpicker">
									<label class="col-sm-2 control-label">所在地区</label>
									<div class="col-md-2 col-sm-12">
										<select class="form-control" name="province">
											<option value=""></option>
										</select>
									</div>
									<div class="col-md-2 col-sm-12">
										<select class="form-control" name="city">
											<option value=""></option>
										</select>
									</div>
									<div class="col-md-2 col-sm-12">
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
										<p style="color: #f1993a;">支持jpg,png格式的图片，且文件小于20M</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
						<button class="btn btn-success btn-sm" type="submit">
						<i class="glyphicon glyphicon-ok"></i>
						<s:message code="save" />
					</button>
						<button class="btn  btn-primary btn-sm" type="submit" onclick="$('#redirect').val('list');">
						<i class="glyphicon glyphicon-ok"></i>
						<s:message code="saveAndReturn" />
					</button>
						<c:if test="${oprt=='create'}">
							<button class="btn btn-default  btn-sm" type="submit" onclick="$('#redirect').val('create');">
							<s:message code="saveAndCreate" />
						</button>
						</c:if>
						<button class="btn btn-default btn-sm" type="button" onclick="location.href='list.do?${searchstring}';">
								<s:message code="return" />
							</button>
					</div>
				</form>
			</div>
		</div>
	</body>

</html>