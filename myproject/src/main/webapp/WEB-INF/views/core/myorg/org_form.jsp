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

<style>
	.chooseemployee-dialog .modal-dialog {
		width: 1000px;
	}
	
	.nav-tabs-custom {
		margin-bottom: 0px;
		background: #fff;
		box-shadow: none;
		border-radius: 0px;
	}
	
	.crop-avatar-dialog .modal-dialog {
		width: 1500px;
	}
</style>
<form class="form-horizontal" id="validForm" action="${oprt=='edit' ? 'update' : 'save'}.do" method="post">
	<tags:search_params />
	<f:hidden name="oid" value="${bean.id}" />
	<f:hidden name="position" value="${position}" />

	<div class="box-body">

		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label"><s:message code="org.name" /></label>
					<div class="col-sm-10">
						<f:text name="name" value="${oprt=='edit' ? bean.name : ''}" class="form-control required" maxlength="150" readonly="readonly" />
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
					<label class="col-sm-4 control-label"> <s:message
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
					<label class="col-sm-4 control-label"><s:message code="org.schoolyear"></s:message></label>
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
					<label class="col-sm-4 control-label"><s:message code="org.decorationday"></s:message></label>
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
					
					<div class="col-sm-10">
						<f:text  class="form-control required" maxlength="150" value="${bean.province }-${bean.city}-${bean.district}" readonly="readonly"/>
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
					<label class="col-sm-2 control-label">微信公众号Id</label>
					<div class="col-sm-10">
						<f:text name="publicNumber" id="publicNumber" value="${bean.publicNumber}" class="form-control {maxlength:180}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-2 control-label">密钥</label>
					<div class="col-sm-10">
						<f:text name="appSecret" value="${bean.appSecret}" class="form-control {maxlength:240}" />
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
</form>
