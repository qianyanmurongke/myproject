<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<ul class="nav nav-tabs">
	<li role="presentation" <c:if test="${type eq 'personal_view'}"> class="active"</c:if>>
		<a href="personal_view.do"><i class="glyphicon glyphicon-align-justify"></i>&nbsp;我的信息</a>
	</li>
	<li role="presentation" <c:if test="${type eq 'personal_avatar'}"> class="active"</c:if>>
		<a href="personal_avatar.do"><i class="glyphicon glyphicon-user"></i>&nbsp;头像设置</a>
	</li>
	<li role="presentation"  <c:if test="${type eq 'password_edit'}"> class="active"</c:if>>
		<a href="password_edit.do"><i class="glyphicon glyphicon-lock"></i>&nbsp;安全设置</a>
	</li>
</ul>