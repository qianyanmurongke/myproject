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
		width: 800px;
	}
	
	.color {
		background-color: #CCCCCC;
	}
</style>

<table class="table table-condensed table-bordered table-hover ls-tb">

	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.parent" /></th>
		<td colspan="3" style="text-align: left;">
			${parent.displayName}
		</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.name" /></th>
		<td style="width: 250px;">${bean.name }</td>
		<th class="color" style="width: 150px;"><s:message code="org.number" /></th>
		<td>${bean.number }
		</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.englishname" /></th>
		<td style="width: 250px;">${bean.ename }</td>
		<th class="color" style="width: 150px;"><s:message code="org.organizationnumber" /></th>
		<td>${bean.organizationNumber }
		</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.phone" /></th>
		<td style="width: 250px;">${bean.phone }</td>
		<th class="color" style="width: 150px;"><s:message code="org.fax" /></th>
		<td>${bean.fax }
		</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.schoolyear" /></th>
		<td style="width: 250px;">
			<fmt:formatDate value='${bean.foundingDate }' pattern='yyyy-MM-dd' />
		</td>
		<th class="color" style="width: 150px;"><s:message code="org.decorationday" /></th>
		<td>
			<fmt:formatDate value='${bean.decorationDay }' pattern='yyyy-MM-dd' />
		</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.legalrepresentative" /></th>
		<td style="width: 250px;">${bean.legalRepresentative }</td>
		<th class="color" style="width: 150px;">邮编</th>
		<td>${bean.zipcode }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.corporateidentity" /></th>
		<td style="width: 250px;">${bean.legalIdcard }</td>
		<th class="color" style="width: 150px;"><s:message code="org.corporatenumber" /></th>
		<td>${bean.legalNumber }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.schoolmanage" /></th>
		<td style="width: 250px;">${bean.orgType.name }</td>
		<th class="color" style="width: 150px;"><s:message code="org.companymanage" /></th>
		<td>${bean.unitType.name }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.type" /></th>
		<td style="width: 250px;">${bean.schoolType.name }</td>
		<th class="color" style="width: 150px;"><s:message code="org.district" /></th>
		<td>${bean.nation.name }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;">城乡类型码</th>
		<td style="width: 250px;">${bean.cityType.name }</td>
		<th class="color" style="width: 150px;">经济属性码</th>
		<td>${bean.economics.name }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;">主教学语言码</th>
		<td style="width: 250px;">${bean.primaryLanguage.name }</td>
		<th class="color" style="width: 150px;">辅教学语言码</th>
		<td>${bean.assistedLanguage.name }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;">电子邮箱</th>
		<td style="width: 250px;">${bean.email }</td>
		<th class="color" style="width: 150px;">主页地址</th>
		<td>${bean.webAddress }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;">所在地区</th>
		<td colspan="3" style="text-align: left;">${bean.province }${bean.city }${bean.district }</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;"><s:message code="org.address" /></th>
		<td colspan="3" style="text-align: left;">${bean.address}</td>
	</tr>
	<tr>
		<th class="color" style="width: 150px;">校徽</th>
		<td colspan="3">
			<img src="${ctx}${bean.logoUrl}" style="width: 200px;height: 200px;" onerror="this.src='../../../static/img/school.png';this.onerror='';">
		</td>
	</tr>
</table>