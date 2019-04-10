<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="saveurl" type="java.lang.String" required="true"
	rtexprvalue="true"%>
<%@ attribute name="imageurl" type="java.lang.String" required="true"
	rtexprvalue="true"%>

<link rel="stylesheet" href="${ctx}/static/vendor/webuploader/webuploader.css" />
<script src="${ctx}/static/vendor/webuploader/webuploader.js"></script>
	<style>
			#btnUploadFiles${id} .webuploader-pick {
				width: 80px;
				height: 80px;
				padding: 0px;
				border-radius: 0px;
				border-radius: 0px;
				background: url(/static/img/sc.png);    
				background-repeat: no-repeat;
				padding: 40px;
				background-color: transparent;
				background-size: 100%;
				border:1px solid #d2d6de;
			}
		</style>

<input type="hidden" name="${id}" value="${imageurl}" id="${id}" />
<div class="pic">
	<div id="gridFileslist">
	
	</div>
	<li id="btnUploadFiles${id}" style="list-style: none;"></li>
	<div class="clear"></div>
</div>



<script>
	var FileMD5${id} = {},
		SelectUploadFiles${id} = new Array(),
		SelectFiles${id};
		var uploader${id} = WebUploader.create({
		resize: false,
		swf: '${ctx}/static/vendor/webuploader/Uploader.swf',
		server: '${saveurl};jsessionid=<%=request.getSession().getId()%>',
		pick: '#btnUploadFiles${id}',
		chunked: true,
		chunkSize: 1024 * 1024,
		//runtimeOrder: 'flash'
		//disableGlobalDnd: true,
		fileNumLimit: 1,
		//fileSizeLimit: 200 * 1024 * 1024,    // 200 M
		//fileSingleSizeLimit: 50 * 1024 * 1024    // 50 M
		accept: {
			title: 'filetype',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		}
	});
	
	if("${imageurl}" != "")
	{
	$("#btnUploadFiles${id} .webuploader-pick").css("background","url(${imageurl})");
	$("#btnUploadFiles${id} .webuploader-pick").css("background-size","100% auto");
	$("#btnUploadFiles${id} .webuploader-pick").css("background-repeat","no-repeat");
	}
			
	uploader${id}.on('filesQueued', function(files) {
		SelectFiles${id} = files;
		if(SelectFiles${id}.length > 0) {
			CallBackAddFiles${id}(0);
		}
	});

	function CallBackAddFiles${id}(fileindex) {
		if(SelectFiles${id}.length == fileindex) {
			uploader${id}.upload();
			return;
		}
		var file = SelectFiles${id}[fileindex];
		SelectUploadFiles${id}.push(file);
		var paramFileType = file.name.substring(file.name.lastIndexOf(".") + 1);


		uploader${id}.md5File(file).progress(function(percentage) {
			//console.log('Percentage:', percentage);
		}).then(function(val, aa) {

			FileMD5${id}[file.id] = val;

			fileindex++;
			CallBackAddFiles${id}(fileindex);
		});

		// 创建缩略图
		// 如果为非图片文件，可以不用调用此方法。
		// thumbnailWidth x thumbnailHeight 为 100 x 100
		uploader${id}.makeThumb(file, function(error, src) {
			if(error) {
				return;
			}
			$("#btnUploadFiles${id} .webuploader-pick").css("background","url(" +  src +")");
			$("#btnUploadFiles${id} .webuploader-pick").css("background-size","100% auto");
			$("#btnUploadFiles${id} .webuploader-pick").css("background-repeat","no-repeat");
		}, 185, 185);

	}

	uploader${id}.on('uploadBeforeSend', function(block, data, headers) {
		data.md5 = FileMD5${id}[data.id]
		data.Method = "";
		if(data.size <= 1024 * 1024) {
			data.chunks = 1;
			data.chunk = 0;
		}
	});

	// 文件上传过程中创建进度条实时显示。
	uploader${id}.on('uploadProgress', function(file, percentage) {


	});
	uploader${id}.on('uploadSuccess', function(block, data) {
	
		$("#${id}").val(data.basePath + data.id);

	});
	uploader${id}.on('uploadError', function(file) {
	});
	// 所有文件上传成功后调用        
	uploader${id}.on('uploadFinished', function(block, data, data1) {
	
		//清空队列
		uploader${id}.reset();
	});
</script>