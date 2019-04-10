<%@ tag pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="s" uri="http://www.springframework.org/tags"%><%@ taglib prefix="fnx" uri="http://java.sun.com/jsp/jstl/functionsx"%>
<%@ attribute name="pagedList" type="org.springframework.data.domain.Page" required="true" rtexprvalue="true"%>

<ul class="pagination pull-left" style="margin:0;">
  <li<c:if test="${fnx:bool(pagedList,'isFirst')}"> class="disabled"</c:if>>
    <a onclick="<c:if test="${!fnx:bool(pagedList,'isFirst')}">$('#chooseEmployeePage').val('${pagedList.number}');ChooseUserPage();</c:if>return false;" aria-label="上一页">
      <span aria-hidden="true">«</span>
    </a>
  </li>
  <li<c:if test="${fnx:bool(pagedList,'isFirst')}"> class="active"</c:if>>
    <a onclick="<c:if test="${!fnx:bool(pagedList,'isFirst')}">$('#chooseEmployeePage').val('1');ChooseUserPage();</c:if>return false;">
      <span>1<c:if test="${pagedList.number >= 4}"> ...</c:if></span>
    </a>
  </li>
  <c:set var="begin" value="${pagedList.number+1-2}"/> <c:if test="${begin<2}"><c:set var="begin" value="${2}"/></c:if>
  <c:set var="end" value="${begin+4}"/> <c:if test="${end>=pagedList.totalPages}"><c:set var="end" value="${pagedList.totalPages-1}"/></c:if>
  <c:if test="${end-begin<4}"><c:set var="begin" value="${end-4}"/></c:if>
  <c:if test="${begin<2}"><c:set var="begin" value="${2}"/></c:if>
  <c:if test="${end>=begin}">
	  <c:forEach var="i" begin="${begin}" end="${end}" step="1">  
	  <li<c:if test="${pagedList.number+1==i}"> class="active"</c:if>>
	    <a onclick="<c:if test="${!(pagedList.number+1==i)}">$('#chooseEmployeePage').val('${i}');ChooseUserPage();</c:if>return false;">
	      <span>${i}</span>
	    </a>
	  </li>
	  </c:forEach>
  </c:if>
  <c:if test="${pagedList.totalPages > 1}">
  <li<c:if test="${pagedList.number+1 == pagedList.totalPages}"> class="active"</c:if>>
    <a   onclick="$('#chooseEmployeePage').val('${pagedList.totalPages}');ChooseUserPage();return false;">
      <span><c:if test="${pagedList.totalPages - pagedList.number > 4}"> ...</c:if>${pagedList.totalPages}</span>
    </a>			    	
  </li>
  </c:if>
  <li<c:if test="${fnx:bool(pagedList,'isLast')}"> class="disabled"</c:if>>
    <a onclick="<c:if test="${pagedList.number+2<=pagedList.totalPages}">$('#chooseEmployeePage').val('${pagedList.number+2}');ChooseUserPage();</c:if>return false;" aria-label="下一页">
      <span aria-hidden="true">»</span>
    </a>
  </li>
</ul>
<input type="hidden" id="chooseEmployeePage" name="page" value="${pagedList.number+1}"/>
<div class="pull-left" style="padding:0 6px;">
	&nbsp;每页
	<select class="form-control" name="page_size" id="pageSize" onchange="$.cookie('page_size',$(this).val(),{expires:36500});ChooseUserPage();" style="border-radius:4px;padding-left:3px;padding-right:3px;width:60px;display:inline-block;">
		<option<c:if test="${pagedList.size<=10}"> selected</c:if>>10</option>
		<option<c:if test="${pagedList.size>10&&pagedList.size<=20}"> selected</c:if>>20</option>
		<option<c:if test="${pagedList.size>20&&pagedList.size<=50}"> selected</c:if>>50</option>
		<option<c:if test="${pagedList.size>50&&pagedList.size<=100}"> selected</c:if>>100</option>
		<option<c:if test="${pagedList.size>100&&pagedList.size<=200}"> selected</c:if>>200</option>
		<option<c:if test="${pagedList.size>200&&pagedList.size<=400}"> selected</c:if>>400</option>
		<option<c:if test="${pagedList.size>400}"> selected</c:if>>800</option>
	</select>
	条 &nbsp; 共 ${pagedList.totalElements} 条
</div>
<script type="text/javascript">
$(function(){
	<c:if test="${pagedList.number!=0 && pagedList.number+1>pagedList.totalPages}">
	$("#chooseEmployeePage").val(${pagedList.totalPages});
	</c:if>
});
</script>