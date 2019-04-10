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
		<jsp:include page="/WEB-INF/views/head.jsp" />
	</head>

	<body class="skin-blue content-body">
		<style>
			.pointer {
				cursor: pointer;
			}
			
			.col-sm-9 {
				/*min-width: 1000px;*/
			}
			
			.col-sm-6 {
				/*min-width: 450px;*/
				padding-right: 0px;
			}
			
			.panel {
				margin-bottom: 10px;
			}
			
			.online {
				padding-left: 2rem;
				float: left;
			}
			
			.offline {
				padding-left: 2rem;
				float: left;
			}
			
			.online .left {
				float: left;
				background: #1936a2;
				width: 108px;
				height: 80px;
				background-image: url(/static/img/online.png);
				background-repeat: no-repeat;
				background-position: center;
			}
			
			.online .right {
				float: left;
				background: #183cc3;
				width: 108px;
				line-height: 80px;
				text-align: center;
				color: #fff;
				font-size: 3.5rem;
			}
			
			.offline .left {
				float: left;
				background: #b94421;
				width: 108px;
				height: 80px;
				background-image: url(/static/img/offline.png);
				background-repeat: no-repeat;
				background-position: center;
			}
			
			.offline .right {
				float: left;
				background: #e0623c;
				width: 108px;
				line-height: 80px;
				text-align: center;
				color: #fff;
				font-size: 3.5rem;
			}
		</style>
		<div class="content">
			<div class="row">
				<div class="col-md-8">
					<div class="row">
						<div class="col-md-6">
							<!--门禁在线情况-->
							<shiro:hasPermission name="core:accesscontrol:list">								
								<jsp:include page="/WEB-INF/views/core/homepage/control/accesslist.jsp" />
							</shiro:hasPermission>

						</div>
						<div class="col-md-6">
							<!--开门记录-->
							<shiro:hasPermission name="core:accessrecord:list">
								<jsp:include page="/WEB-INF/views/core/homepage/control/recordlist.jsp" />
							</shiro:hasPermission>
							
						</div>
					</div>
				</div>
				<div class="col-md-4">
					
				</div>

			</div>
		</div>
	</body>

</html>