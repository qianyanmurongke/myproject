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
					<div class="tab-pane active">
						<div class="box box-solid" style="box-shadow: none;">
							<div class="box-body">
								<div class="media">
									<div class="media-left">
										<a href="javasript;;" class="ad-click-event">
											<img  src="${ctx}${user.avatarLarge}" onerror="this.src='${ctx}/static/img/default_avatar.png';this.onerror='';"  alt="${user.realName}" class="media-object" style="width: 150px;height: auto;border-radius: 4px;box-shadow: 0 1px 3px rgba(0,0,0,.15);">
										</a>
									</div>
									<div class="media-body">
										<div class="clearfix">

											<h4 style="margin-top: 0">${user.realName}</h4>

											<dl class="dl-horizontal ">
												<dt class="text-light-blue">工号</dt>
												<dd></dd>
												<dt class="text-light-blue">用户名</dt>
												<dd>${user.username}</dd>
												<dt class="text-light-blue">学校</dt>
												<dd>${user.org.name}</dd>
												<dt class="text-light-blue">部门</dt>
												<dd></dd>
												<dt class="text-light-blue">出生日期 </dt>
												<dd>
													<fmt:formatDate value="${user.birthDate}" pattern="yyyy-MM-dd" />
												</dd>
												<dt class="text-light-blue">短号</dt>
												<dd>${user.mobile}</dd>
												<dt class="text-light-blue">邮箱</dt>
												<dd>${user.email}</dd>
											</dl>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="panel panel-info">
									<div class="panel-heading">
										<h3 class="panel-title">操作记录</h3>
									</div>
									<ul class="list-group">
										<c:forEach var="bean" varStatus="status" items="${OperationLogList}">
											<li class="list-group-item">
												<span style="font-size: 14px;font-weight: 600;">
												<span class="text-primary"><c:out value="${bean.name}" /></span>&nbsp;&nbsp;<label>登录IP:</label>
												<font color="blue">${bean.ip}</font> &nbsp;&nbsp;
												<c:out value="${bean.description}"></c:out>
												</span><br><i class="glyphicon glyphicon-time"></i>&nbsp;
												<fmt:formatDate value="${bean.time}" pattern="yyyy-MM-dd HH:mm:ss" />
											</li>
										</c:forEach>
									</ul>
								</div>

							</div>

							<div class="col-sm-6">
								<div class="panel panel-info">
									<div class="panel-heading">
										<h3 class="panel-title">登录记录</h3>
									</div>
									<ul class="list-group">
										<c:forEach var="bean" varStatus="status" items="${LoginLogList}">
											<li class="list-group-item">
												<i class="glyphicon glyphicon-time"></i>&nbsp; <fmt:formatDate value="${bean.time}" pattern="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;<span class="text-primary">(<s:message code="${bean.name}" />)</span>&nbsp;&nbsp;<label>登录IP:</label><font color="blue">${bean.ip}</font>
											</li>
										</c:forEach>
									</ul>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</body>

</html>