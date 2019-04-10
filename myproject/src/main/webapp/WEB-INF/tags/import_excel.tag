<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="saveurl" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="classname" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="templateurl" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="templatename" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"
	rtexprvalue="true" description="导入数据"%>

<button id="${id}" class="${classname }" type="button"
	onclick="ShowImportExcel${id}();return false;">
	<i class="fa fa-file-excel-o"></i> 导入Excel
</button>

<script type="text/javascript">
var $BootstrapDialog${id};
	function ShowImportExcel${id}() {
		if($BootstrapDialog${id} == undefined){
			$BootstrapDialog${id} = BootstrapDialog.show({
			closeByBackdrop : false,
			closeByKeyboard : true,
			draggable : true,
			title:"${title}",
			onshown:function(dialog){
				 var $message = $('<div></div>').load("${ctx}/admin/commons/importexcel.do", function(){				 
            
	                var item = dialog.getModalBody();
	                
					$(item).find("#excelFileForm").data("saveurl", "${saveurl}");
					$(item).find("#excelFileForm .templateurl").attr("href", "${templateurl}");
					$(item).find("#excelFileForm .templateurl").html("下载【${templatename}】");
				 });
                dialog.setMessage($message);
			},	
			message: $('<div class="loadingDialog"><div class="loading-content"style="left: 684px; top: 11.5px;">正在加载界面中，请稍后…</div></div>')	
		});
	}
	else{
		$BootstrapDialog${id}.open();
		}
	}
	
</script>