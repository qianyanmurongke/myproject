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

<div class="box-body">
	<input hidden="hidden" id="doorid" name="doorid" value="${bean.id}"/>
	<input hidden="hidden" id="lockaddr" name="lockaddr" value="${bean.pkid}"/>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="form-group">
				<button class="btn btn-info" style="width: 50%;height: 50px;display: block;margin: auto;" onclick="openandclose('1')">开门</button>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="form-group">
				<button class="btn btn-info" style="width: 50%;height: 50px;display: block;margin: auto;" onclick="openandclose('3')">常开</button>
			</div>
		</div>
	</div>
	<script>
    	function openandclose(status){
    		$.ajax({
    			type:"GET",
    			url:"openandclose.do",
    			data:{
    				"status":status,
    				"id":$("#doorid").val(),
    				"lockaddr":$("#lockaddr").val(),
    				"responseType":"ajax"
    			},
    			dataType:"json",
    			success:function(data){
    				if(data.openresult == 0) {
    					checktasksucc(data.taskId,data.status);
    				}else{
    				   toastr.warning("访问失败！");
    				}
    				
    			}
    		})
    		return false;
    	}
    	
    	function checktasksucc(taskId,status){
    		$.ajax({
    			type:"GET",
    			url:"checktasksucc.do",
    			data:{
    				"taskid":taskId,
    				"id":$("#doorid").val(),
    				"status":status,
    				"responseType":"ajax"
    			},
    			dataType:"json",
    			success:function(data){
    				if(data.checkresult == 0) {
    					 toastr.warning("开门成功");
    				}else{
    				   toastr.warning("开门失败！");
    				}
    				window.location.reload();
    			}
    		});
    		return false;
    	}
    </script>
</div>