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

</style>

<link rel="stylesheet" href="${ctx}/static/vendor/webuploader/webuploader.css" />
<link rel="stylesheet" href="${ctx}/static/vendor/webuploader/inportexcel.css" />
<script src="${ctx}/static/vendor/webuploader/webuploader.js"></script>

<form class="form-horizontal" id="excelFileForm">
	<input type="hidden" value="ajax" name="responseType" />
	<div class="box-body">
		<div class="row">
			<div class="col-sm-12">
				<div id="uploader" class="wu-example">
					<div class="queueList">
						<div id="dndArea" class="placeholder">
							<p>将Excel文件拖到这里</p>
							<div id="filePicker"></div>
						</div>
					</div>
					<div class="statusBar" style="display: none;">
						<div class="progress">
							<span class="text">0%</span> <span class="percentage"></span>
						</div>
						<div class="info" style="clear: both;display: block;"></div>
						<div class="btns">
							<!--<div id="filePicker2"></div>-->
							<div class="uploadBtn"><i class="fa fa-reply"></i>&nbsp;导入数据</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 control-label no-padding-right"></div>
			<a class="templateurl" href="#" target="_blank" style="text-decoration: underline; color: Blue; font-size: 13px; margin-right: 10px;float: right;">下载【#】</a>
		</div>
	</div>
</form>
<script>
	$(function() {
		var BASE_URL = "${ctx}/static/vendor/webuploader/";
		var $wrap = $('#uploader'), // 图片容器
			$queue = $('<ul class="filelist"></ul>')
			.appendTo($wrap.find('.queueList')),

			// 状态栏，包括进度和控制按钮
			$statusBar = $wrap.find('.statusBar'),

			// 文件总体选择信息。
			$info = $statusBar.find('.info'),

			// 上传按钮
			$upload = $wrap.find('.uploadBtn'),

			// 没选择文件之前的内容。
			$placeHolder = $wrap.find('.placeholder'),

			// 总体进度条
			$progress = $statusBar.find('.progress').hide(),

			// 添加的文件数量
			fileCount = 0,

			// 添加的文件总大小
			fileSize = 0,

			// 优化retina, 在retina下这个值是2
			ratio = window.devicePixelRatio || 1,

			// 缩略图大小
			thumbnailWidth = 110 * ratio,
			thumbnailHeight = 110 * ratio,

			// 可能有pedding, ready, uploading, confirm, done.
			state = 'pedding',

			// 所有文件的进度信息，key为file id
			percentages = {},

			FileMD5, FileType,

			supportTransition = (function() {
				var s = document.createElement('p').style,
					r = 'transition' in s ||
					'WebkitTransition' in s ||
					'MozTransition' in s ||
					'msTransition' in s ||
					'OTransition' in s;
				s = null;
				return r;
			})();

		if(!WebUploader.Uploader.support()) {
			toastr.error('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
			throw new Error('WebUploader does not support the browser you are using.');
		}

		// 实例化
		var uploader = WebUploader.create({
			pick: {
				id: '#filePicker',
				label: '点击选择文件'
			},
			dnd: '#uploader .queueList',
			paste: document.body,
			accept: {
				title: 'Images',
				extensions: 'xls,xlsx,jpeg,bmp,png',
				mimeTypes: 'application/vnd.ms-excel,application/x-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
			},
			swf: BASE_URL + 'Uploader.swf',
			disableGlobalDnd: true,
			chunked: true,
			server: '${ctx}/admin/commons/importexportsubmit.do',
			fileNumLimit: 1,
			fileSizeLimit: 5 * 1024 * 1024, // 200 M
			fileSingleSizeLimit: 1 * 1024 * 1024 // 50 M
		});

		// 添加“添加文件”的按钮，
		//		uploader.addButton({
		//			id: '#filePicker2',
		//			label: '继续添加'
		//		});

		// 当有文件添加进来时执行，负责view的创建
		function addFile(file) {
			var $li = $('<li id="' + file.id + '">' +
					'<p class="imgWrap"><img src="${ctx}/static/img/file/office/xls.png"></p>' +
					'<p class="title">' + file.name + '</p>' +
					'<p class="progress"><span></span></p>' +
					'</li>'),

				$btns = $('<div class="file-panel">' +
					'<span class="cancel">删除</span></div>').appendTo($li),
				$prgress = $li.find('p.progress span'),
				$wrap = $li.find('p.imgWrap'),
				$info = $('<p class="error"></p>'),
				showError = function(code) {
					switch(code) {
						case 'exceed_size':
							text = '文件大小超出';
							break;

						case 'interrupt':
							text = '上传暂停';
							break;

						default:
							text = '上传失败，请重试';
							break;
					}

					$info.text(text).appendTo($li);
				};

			FileType = file.name.substring(file.name.lastIndexOf(".") + 1);

			uploader.md5File(file).progress(function(percentage) {

			}).then(function(val, aa) {

				FileMD5 = val;
			});

			if(file.getStatus() === 'invalid') {
				showError(file.statusText);
			} else {

				percentages[file.id] = [file.size, 0];
				file.rotation = 0;
			}

			file.on('statuschange', function(cur, prev) {
				if(prev === 'progress') {
					$prgress.hide().width(0);
				} else if(prev === 'queued') {
					$li.off('mouseenter mouseleave');
					$btns.remove();
				}

				// 成功
				if(cur === 'error' || cur === 'invalid') {
					console.log(file.statusText);
					showError(file.statusText);
					percentages[file.id][1] = 1;
				} else if(cur === 'interrupt') {
					showError('interrupt');
				} else if(cur === 'queued') {
					percentages[file.id][1] = 0;
				} else if(cur === 'progress') {
					$info.remove();
					$prgress.css('display', 'block');
				} else if(cur === 'complete') {
					$li.append('<span class="success"></span>');
				}

				$li.removeClass('state-' + prev).addClass('state-' + cur);
			});

			$li.on('mouseenter', function() {
				$btns.stop().animate({
					height: 30
				});
			});

			$li.on('mouseleave', function() {
				$btns.stop().animate({
					height: 0
				});
			});

			$btns.on('click', 'span', function() {
				var index = $(this).index(),
					deg;

				switch(index) {
					case 0:
						uploader.removeFile(file);
						return;
				}
			});

			$li.appendTo($queue);
		}

		// 负责view的销毁
		function removeFile(file) {
			var $li = $('#' + file.id);

			delete percentages[file.id];
			updateTotalProgress();
			$li.off().find('.file-panel').off().end().remove();
		}

		function updateTotalProgress() {
			var loaded = 0,
				total = 0,
				spans = $progress.children(),
				percent;

			$.each(percentages, function(k, v) {
				total += v[0];
				loaded += v[0] * v[1];
			});

			percent = total ? loaded / total : 0;

			spans.eq(0).text(Math.round(percent * 100) + '%');
			spans.eq(1).css('width', Math.round(percent * 100) + '%');
			updateStatus();
		}

		function updateStatus() {
			var text = '',
				stats;

			if(state === 'ready') {
				text = '选中' + fileCount + '个文件，共' +
					WebUploader.formatSize(fileSize) + '。';
			} else if(state === 'confirm') {
				stats = uploader.getStats();
				if(stats.uploadFailNum) {
					text = '已成功上传' + stats.successNum + '个文件，' +
						stats.uploadFailNum + '个文件上传失败，<a class="retry" href="#">重新上传</a>失败文件或<a class="ignore" href="#">忽略</a>'
				}

			} else {
				stats = uploader.getStats();
				text = '共' + fileCount + '个（' +
					WebUploader.formatSize(fileSize) +
					'），已上传' + stats.successNum + '个';

				if(stats.uploadFailNum) {
					text += '，失败' + stats.uploadFailNum + '个';
				}
			}

			$info.html(text);
		}

		function setState(val) {
			var file, stats;

			if(val === state) {
				return;
			}

			$upload.removeClass('state-' + state);
			$upload.addClass('state-' + val);
			state = val;

			switch(state) {
				case 'pedding':
					$placeHolder.removeClass('element-invisible');
					$queue.parent().removeClass('filled');
					$queue.hide();
					$statusBar.addClass('element-invisible');
					uploader.refresh();
					break;

				case 'ready':
					$placeHolder.addClass('element-invisible');
					//					$('#filePicker2').removeClass('element-invisible');
					$queue.parent().addClass('filled');
					$queue.show();
					$statusBar.removeClass('element-invisible');
					uploader.refresh();
					break;

				case 'uploading':
					//					$('#filePicker2').addClass('element-invisible');
					$progress.show();
					$upload.html('<i class="glyphicon glyphicon-pause"></i>&nbsp;暂停上传');
					break;

				case 'paused':
					$progress.show();
					$upload.html('<i class="glyphicon glyphicon-play"></i>&nbsp;继续上传');
					break;

				case 'confirm':
					$progress.hide();
					$upload.html('<i class="fa fa-reply"></i>&nbsp;导入数据').addClass('disabled');

					stats = uploader.getStats();
					if(stats.successNum && !stats.uploadFailNum) {
						setState('finish');
						return;
					}
					break;
				case 'finish':
					stats = uploader.getStats();
					if(stats.successNum) {
						//toastr.success('上传成功');
						CallBackSaveData()

					} else {
						// 没有成功的图片，重设
						state = 'done';
						//location.reload();
					}
					break;
			}

			updateStatus();
		}

		uploader.onUploadProgress = function(file, percentage) {
			var $li = $('#' + file.id),
				$percent = $li.find('.progress span');

			$percent.css('width', percentage * 100 + '%');
			percentages[file.id][1] = percentage;
			updateTotalProgress();
		};

		uploader.onFileQueued = function(file) {
			fileCount++;
			fileSize += file.size;

			if(fileCount === 1) {
				$placeHolder.addClass('element-invisible');
				$statusBar.show();
			}

			addFile(file);
			setState('ready');
			updateTotalProgress();
		};

		uploader.onFileDequeued = function(file) {
			fileCount--;
			fileSize -= file.size;

			if(!fileCount) {
				setState('pedding');
			}

			removeFile(file);
			updateTotalProgress();

		};

		uploader.onUploadBeforeSend = function(block, data, headers) {
			data.md5 = FileMD5;
			data.Method = "";
			if(data.size <= 1024 * 1024) {
				data.chunks = 1;
				data.chunk = 0;
			}
		};

		uploader.on('all', function(type) {
			var stats;
			switch(type) {
				case 'uploadFinished':
					setState('confirm');
					break;

				case 'startUpload':
					setState('uploading');
					break;

				case 'stopUpload':
					setState('paused');
					break;

			}
		});

		uploader.onError = function(code) {
			if(code == "Q_TYPE_DENIED") {
				toastr.error("请选择正确文件格式(.xls,.xlsx)");
				return;
			}
			if(code == "Q_EXCEED_NUM_LIMIT") {
				toastr.error("一次只能选择一个文件");
				return;
			}
			toastr.error('Eroor: ' + code);
		};

		$upload.on('click', function() {
			if($(this).hasClass('disabled')) {
				return false;
			}

			if(state === 'ready') {
				uploader.upload();
			} else if(state === 'paused') {
				uploader.upload();
			} else if(state === 'uploading') {
				uploader.stop();
			}
		});

		$info.on('click', '.retry', function() {
			uploader.retry();
		});

		$info.on('click', '.ignore', function() {
			$(uploader.getFiles()).each(function() {
				uploader.removeFile(this);

				setState('pedding');
			});

			$upload.html('<i class="fa fa-reply"></i>&nbsp;导入数据').removeClass('disabled');

			//toastr.info('忽略该提示信息！');
		});

		$upload.addClass('state-' + state);
		updateTotalProgress();

		function CallBackSaveData() {

			var text = '<div id="progress" class="modal-body" style="display: none;"><div class="progress progress-striped active"><div class="progress-bar progress-bar-primary" role="progressbar" style="width: 100%;">导入中... 请勿进行其他操作</div></div></div>'
			$wrap.append(text);
			$("#progress").css("display", "block");

			$.ajax({
				type: "post",
				url: $("#excelFileForm").data("saveurl"),
				data: {
					"filepath": FileMD5 + "." + FileType,
					"responseType": "ajax"
				},
				dataType: "json",
				success: function(data) {
					if(data.status == 1) {

						$("#progress").css("display", "none");
						$BootstrapDialogbtnExcel.close();
						swal({
							title: "导入成功",
							type: "success",
							confirmButtonText: "确定"
						});

						toastr.success("导入成功");
					} else {

						toastr.warning(data.messages);
						var text = '文件上传失败，<a class="retry" href="#">重新上传</a>失败文件或<a class="ignore" href="#">忽略</a>'
						$info.html(text);
					}
				},
				error: function(data) {
					var text = '文件上传失败，<a class="retry" href="#">重新上传</a>失败文件或<a class="ignore" href="#">忽略</a>'
					$info.html(text);

					toastr.error("网络异常,请稍后操作");
				}
			});
		}
	});
</script>