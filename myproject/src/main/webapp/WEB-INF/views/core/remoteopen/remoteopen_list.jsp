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
		<script>
			$(function() {
				$("#sortHead").headSort();
			});


			function ShowEdit(url) {
				BootstrapDialog.showDialog({
					title: "远程开启",
					loadUrl: url,
					cssClass: 'Resultdialog',
				});
			}
		</script>

		<style>
			.Resultdialog .modal-dialog {
				width: 300px;
			}
			
			.Resultdialog .modal-body {
				padding: 0px;
				height: 150px;
			}
			
			@charset "utf-8";
			/* CSS Document */
			
			* {
				margin: 0;
				padding: 0;
			}
			
			li {
				list-style-type: none;
			}
			
			body,
			ul,
			ol,
			li,
			p,
			h1,
			h2,
			h3,
			h4,
			h5,
			h6,
			form,
			fieldset,
			table,
			td,
			img,
			div,
			tr {
				margin: 0;
				padding: 0;
			}
			
			body {
				color: #000;
				background: #FFF;
				font: 14px/22px Verdana, Arial, sans-serif, "Times New Roman";
				font-family: "微软雅黑";
			}
			
			a {
				outline: none;
				star: expression(this.onFocus=this.blur());
				text-decoration: none;
			}
			
			.clearfix:before,
			.clearfix:after {
				content: " ";
				display: table;
			}
			
			.clearfix:after {
				clear: both;
			}
			
			.mj li {
				float: left;
				text-align: center;
				padding: 20px 40px;
				position: relative;
			}
			
			.mj li img {
				width: 100px;
			}
			
			.mj li p {
				font-size: 16px;
				line-height: 52px;
			}
			
			.mj a{
				float: right;
				margin-left: 10px;
				color: #333;
				position: absolute;
				background: #fff;
				border-radius: 8px;
				padding: 1px 3px;
				right: 45px;
				top: 25px;
			}
		</style>
	</head>

	<body class="skin-blue content-body">
		<jsp:include page="/WEB-INF/views/commons/show_message.jsp" />
		<div class="content-header">
			<h1>
			<s:message code="remote.open" />
			-
			<s:message code="list" />
			<small>(<s:message code="totalElements"
					arguments="${pagedList.totalElements}" />)
			</small>
		</h1>
		</div>
		<div class="content">
			<div class="box box-primary">
				<div class="box-body table-responsive">
					<form action="remoteopen.do" method="get" class="form-inline ls-search">
						<div class="form-group">
							<label for="search_EQ_place.id">场所</label> &nbsp;&nbsp;
							<select class="form-control input-sm" id="placeId" onchange="getaccess()">
								<option value=""><s:message code="allSelect" /></option>
								<c:forEach var="schoolplace" items="${schoolplacelist}">
									<option value="${schoolplace.id}" <c:if test="${schoolplace.id eq requestScope['search_EQ_place.id'][0]}"> selected="selected"</c:if>> ${schoolplace.placeName}
									</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label for="doorName">名称</label> <input class="form-control input-sm" type="text" id="doorName"  value="${search_CONTAIN_doorName[0]}" style="width: 200px;" />
						</div>
						<button class="btn btn-primary" type="button" onclick="getaccess();return false;">
							<i class="fa fa-search"></i>
							<s:message code="search" />
						</button>
					</form>
					<form method="post">
						<tags:search_params />
						<div id="draccess" style="padding-top: 20px;">
							<div class="loadingDialog">
								<div class="loading-content">正在加载中，请稍后…</div>
							</div>
						</div>
						<div class="mj">
							<ul >
								<c:forEach var="bean" varStatus="status" items="${pagedList.content}">
									
								</c:forEach>
								<div class="clearfix"></div>
							</ul>
						</div>
						<script type="text/javascript">
								$(function() {
									getaccess();
								});
								//
								function getaccess() {
									$("#draccess").html('<div class="loadingDialog">' +
										'<div class="loading-content">正在加载中，请稍后…</div>' +
										'</div>')
									$.ajax({
										type: "GET",
										url: "draccessList.do",
										data: {
											"placeId": $("#placeId").val(),
											"doorName":$("#doorName").val(),
											"online":1,
											"responseType": "ajax"
										},
										dataType: "json",
										success: function(data) {
											$("#draccess").html("")
											if(data.status == 0) {
												var HTML1 = "";
												var paramHTML = "";
												$(data.placeitems).each(function(index, el) {
													paramHTML = '<div class="row">' +
														' 	<div class="col-lg-12">' +
														'    	<div class="card">' +
														'       	 <div class="card-block">' +
														'            	<label class="col-md-12 form-control-label" for="textarea-input" style="font-size:18px;font-weight:700">' + this.placename + '</label>' +
														'        	</div>' +
														'    	</div>' +
														'	</div>' +
														'	<div class="mj">' +
														'			<ul >';
													var placeId = this.placeId;
													var HTML2 = "";
													var i=0;
													$(data.draccesscontrolitems).each(function(index, el) {
														i++;
														if(placeId == this.placeId) {
															HTML2 += '<li <shiro:hasPermission name="core:accesscontrol:edit"> ondblclick ="ShowEdit(\'open.do?id='+this.id+'\');return false;"</shiro:hasPermission>>';
																	if(this.status==1){
																		HTML2 +='<a style="color: #3eb370;">常开</a>' +
																  				'	<img src="${ctx}/static/img/accesscontrol/open.png" >' +
																  				'		<p>'+this.doorName+'</p>';
																	}else if(this.status==0){
																		HTML2 +='<a style="color: #ca5114;">关闭</a>' +
																				'	<img src="${ctx}/static/img/accesscontrol/close.png">' +
																				'		<p>'+this.doorName+'</p>';
																	}
																HTML2 +='</li>'
														}
													});
													if(HTML2 == "") {
														HTML2 = '<p style="padding-left: 32px;font-size: 16px;font-weight: 500; margin: 0px 0px 10px;">暂无门禁</p>';
													}
													paramHTML += HTML2 + '   </ul>' +
															'	</div>' +
														'</div>';
													if(i==0){
														paramHTML="";
													}
													$("#draccess").append(paramHTML);
												});
												$(data.draccesscontrolitem).each(function(index, el) {
													HTML1+= '<li <shiro:hasPermission name="core:accesscontrol:edit"> ondblclick ="ShowEdit(\'open.do?id='+this.id+'\');return false;"</shiro:hasPermission>>';
																	if(this.status==1){
																		HTML1 +='<a style="color: #3eb370;">常开</a>' +
																  				'	<img src="${ctx}/static/img/accesscontrol/open.png" >' +
																  				'		<p>'+this.doorName+'</p>';
																	}else if(this.status==0){
																		HTML1 +='<a style="color: #ca5114;">关闭</a>' +
																				'	<img src="${ctx}/static/img/accesscontrol/close.png">' +
																				'		<p>'+this.doorName+'</p>';
																	}
																	HTML1 +='</li>'
												});
												var HTML3='<div class="mj"><ul >'+HTML1+'</ul></div>';
												$("#draccess").append(HTML3);					
											} else {
												$("#draccess").html("");
											}
										}
									});
									return false;
								}
							</script>						
					</form>
				</div>
			</div>
		</div>
	</body>
</html>