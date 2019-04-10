var $CropAvatar = function($element) {
	this.$container = $element;

	this.$avatarView = this.$container.find('.avatar-view');
	this.$avatar = this.$avatarView.find('img');
	this.$loading = this.$container.find('.loading');

	this.$avatarModal = this.$container.find('#avatar-modal');
	this.$avatarForm = this.$avatarModal.find('.avatar-form');
	this.$avatarUpload = this.$avatarForm.find('.avatar-upload');
	this.$avatarSrc = this.$avatarForm.find('.avatar-src');
	this.$avatarX = this.$avatarForm.find('.avatar_x');
	this.$avatarY = this.$avatarForm.find('.avatar_y');
	this.$avatarHeight = this.$avatarForm.find('.avatar_height');
	this.$avatarWidth = this.$avatarForm.find('.avatar_width');
	this.$avatarRotate = this.$avatarForm.find('.avatar_rotate');
	this.$avatarInput = this.$avatarForm.find('.avatar-input');
	this.$avatarSave = this.$avatarForm.find('.avatar-save');
	this.$avatarBtns = this.$avatarForm.find('.avatar-btns');

	this.$avatarWrapper = this.$avatarModal.find('.avatar-wrapper');
	this.$avatarPreview = this.$avatarModal.find('.avatar-preview');

	this.init();
	console.log(this);
}

$CropAvatar.prototype = {
	constructor: $CropAvatar,

	support: {
		fileList: !!$('<input type="file">').prop('files'),
		blobURLs: !!window.URL && URL.createObjectURL,
		formData: !!window.FormData
	},

	init: function() {
		this.support.datauri = this.support.fileList && this.support.blobURLs;

		if(!this.support.formData) {
			this.initIframe();
		}

		this.initTooltip();
		this.initModal();
		this.addListener();
	},

	addListener: function() {
		this.$avatarView.on('click', $.proxy(this.click, this));
	},

	initTooltip: function() {
		this.$avatarView.tooltip({
			placement: 'bottom'
		});
	},

	initModal: function() {
		this.$avatarModal.modal({
			show: false
		});
	},

	initPreview: function() {
		var url = this.$avatar.attr('src');

		this.$avatarPreview.empty().html('<img src="' + url + '">');
	},

	initIframe: function() {
		var target = 'upload-iframe-' + (new Date()).getTime(),
			$iframe = $('<iframe>').attr({
				name: target,
				src: ''
			}),
			_this = this;

		// Ready ifrmae
		$iframe.one('load', function() {

			// respond response
			$iframe.on('load', function() {
				var data;

				try {
					data = $(this).contents().find('body').text();
				} catch(e) {
					console.log(e.message);
				}

				if(data) {
					try {
						data = $.parseJSON(data);
					} catch(e) {
						console.log(e.message);
					}

					_this.submitDone(data);
				} else {
					_this.submitFail('Image upload failed!');
				}

				_this.submitEnd();

			});
		});

		this.$iframe = $iframe;
		this.$avatarForm.attr('target', target).after($iframe.hide());
	},

	click: function() {
		var paramTitle = this.$container.data("title");
		var paramID = this.$container.data("id");
		var paramSaveURL = this.$container.data("saveurl");
		var paramHTML = $('<form class="avatar-form" action="' + paramSaveURL + '" enctype="multipart/form-data" method="post"><div class="crop-avatar">' +
			'	<div class="avatar-body">' +
			'		<div class="avatar-upload">' +
			'			<input class="avatar-src" name="avatar_src" type="hidden">' +
			'			<input class="avatar_x" name="avatar_x" type="hidden">' +
			'			<input class="avatar_y" name="avatar_y" type="hidden">' +
			'			<input class="avatar_height" name="avatar_height" type="hidden">' +
			'			<input class="avatar_width" name="avatar_width" type="hidden">' +
			'			<input class="avatar_rotate" name="avatar_rotate" type="hidden">' +
			'			<input name="responseType" value="ajax" type="hidden">' +
			'			<label for="avatarInput" class="btn btn-default">选择图片</label>' +
			'			<input class="avatar-input" id="avatarInput" name="file" type="file" style="display: none;">' +
			'		</div>' +
			'		<div class="row">' +
			'			<div class="col-md-9">' +
			'				<div class="avatar-wrapper"></div>' +
			'			</div>' +
			'			<div class="col-md-3">' +
			'				<div class="avatar-preview preview-lg"></div>' +
			'				<div class="avatar-preview preview-md"></div>' +
			'				<div class="avatar-preview preview-sm"></div>' +
			'			</div>' +
			'		</div>' +
			'		<div class="row avatar-btns">' +
			'			<div class="col-md-9">' +
			'				<div class="btn-group">' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="-90" type="button" title="旋转-90°">向左旋转</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="-15" type="button">-15°</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="-30" type="button">-30°</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="-45" type="button">-45°</button>' +
			'				</div>' +
			'				<div class="btn-group">' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="90" type="button" title="旋转90°">向右旋转</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="15" type="button">15°</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="30" type="button">30°</button>' +
			'					<button class="btn btn-primary" data-method="rotate" data-option="45" type="button">45°</button>' +
			'				</div>' +
			'			</div>' +
			'		</div>' +
			'	</div>' +
			'</div></form>');

		var paramthis = this;

		if(this.$BootstrapDialog == undefined) {
			this.$BootstrapDialog = BootstrapDialog.show({
				closeByBackdrop: false,
				closeByKeyboard: true,
				draggable: true,
				title: paramTitle,
				cssClass: 'crop-avatar-dialog',
				message: paramHTML,
				onshow: function(dialog) {
					var item = dialog.getModalBody();
					paramthis.$avatarModal = $(item).find('#avatar-modal');
					paramthis.$avatarForm = $(item).find('.avatar-form');
					paramthis.$avatarUpload = $(item).find('.avatar-upload');
					paramthis.$avatarSrc = $(item).find('.avatar-src');
					paramthis.$avatarX = $(item).find('.avatar_x');
					paramthis.$avatarY = $(item).find('.avatar_y');
					paramthis.$avatarHeight = $(item).find('.avatar_height');
					paramthis.$avatarWidth = $(item).find('.avatar_width');
					paramthis.$avatarRotate = $(item).find('.avatar_rotate');
					paramthis.$avatarInput = $(item).find('.avatar-input');
					//paramthis.$avatarSave = $(item).find('.avatar-save');
					paramthis.$avatarBtns = $(item).find('.avatar-btns');

					paramthis.$avatarWrapper = $(item).find('.avatar-wrapper');
					paramthis.$avatarPreview = $(item).find('.avatar-preview');
					paramthis.active = false;
					paramthis.$avatarInput.unbind("change");

					paramthis.$avatarInput.on('change', $.proxy(paramthis.change, paramthis));
					paramthis.$avatarForm.unbind("submit");
					paramthis.$avatarForm.on('submit', $.proxy(paramthis.submit, paramthis));
					paramthis.$avatarBtns.unbind("click");
					paramthis.$avatarBtns.on('click', $.proxy(paramthis.rotate, paramthis));
				},
				buttons: [{
					label: '<i class="glyphicon glyphicon-ok"></i>&nbsp;确认',
					cssClass: 'btn-info',
					action: function(dialog) {
						//paramthis.rotate(event);
						paramthis.submit()
					}
				}, {
					label: '<i class="glyphicon glyphicon-remove"></i>&nbsp;关闭',
					action: function(dialog) {
						dialog.close();
					}
				}]
			});
		} else {
			this.$BootstrapDialog.open();
		}

		this.initPreview();
	},

	change: function() {
		var files,
			file;

		if(this.support.datauri) {
			files = this.$avatarInput.prop('files');

			if(files.length > 0) {
				file = files[0];

				if(this.isImageFile(file)) {
					if(this.url) {
						URL.revokeObjectURL(this.url); // Revoke the old one
					}

					this.url = URL.createObjectURL(file);
					this.startCropper();
				}
			}
		} else {
			file = this.$avatarInput.val();

			if(this.isImageFile(file)) {
				this.syncUpload();
			}
		}
	},

	submit: function() {
		if(!this.$avatarSrc.val() && !this.$avatarInput.val()) {
			return false;
		}

		if(this.support.formData) {
			this.ajaxUpload();
			return false;
		}
	},

	rotate: function(e) {
		var data;

		if(this.active) {
			data = $(e.target).data();

			if(data.method) {
				this.$img.cropper(data.method, data.option);
			}
		}
	},

	isImageFile: function(file) {
		if(file.type) {
			return /^image\/\w+$/.test(file.type);
		} else {
			return /\.(jpg|jpeg|png|gif)$/.test(file);
		}
	},

	startCropper: function() {
		var _this = this;

		if(this.active) {
			this.$img.cropper('replace', this.url);
		} else {
			this.$img = $('<img src="' + this.url + '">');
			this.$avatarWrapper.empty().html(this.$img);
			this.$img.cropper({
				aspectRatio: 1,
				preview: this.$avatarPreview.selector,
				strict: false,
				crop: function(data) {
					//					var json = [
					//						'{"x":' + data.x,
					//						'"y":' + data.y,
					//						'"height":' + data.height,
					//						'"width":' + data.width,
					//						'"rotate":' + data.rotate + '}'
					//					].join();

					_this.$avatarX.val(data.x);;
					_this.$avatarY.val(data.y);;
					_this.$avatarHeight.val(data.height);;
					_this.$avatarWidth.val(data.width);;
					_this.$avatarRotate.val(data.rotate);;
					//					_this.$avatarData.val(json);
				}
			});

			this.active = true;
		}
	},

	stopCropper: function() {
		if(this.active) {
			this.$img.cropper('destroy');
			this.$img.remove();
			this.active = false;
		}
	},

	ajaxUpload: function() {
		var url = this.$avatarForm.attr('action'),
			_this = this;

		this.$avatarForm.ajaxSubmit({
			type: 'post',
			url: url,
			dataType: 'json',
			beforeSend: function() {
				_this.submitStart();
			},
			success: function(data) {
				_this.submitDone(data);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				//_this.submitFail(textStatus || errorThrown);
				_this.submitFail("上传失败！网络异常,请稍后操作");
			},
			complete: function() {
				_this.submitEnd();
			}
		});
	},

	syncUpload: function() {
		//this.$avatarSave.click();
		this.$avatarForm.submit();
	},

	submitStart: function() {
		this.$loading.fadeIn();
	},

	submitDone: function(data) {
		console.log(data);

		if($.isPlainObject(data) && data.status == 1) {

			this.url = data.fileUrl;
			this.uploaded = false;
			this.cropDone();

			this.$avatarInput.val('');

			/*	if(this.support.datauri || this.uploaded) {
					this.uploaded = false;
					this.cropDone();
				} else {
					this.uploaded = true;
					this.$avatarSrc.val(this.url);
					this.startCropper();
				}*/

		} else {
			this.alert(data.messages[0]);
		}
	},

	submitFail: function(msg) {
		this.alert(msg);
	},

	submitEnd: function() {
		this.$loading.fadeOut();
	},

	cropDone: function() {
		this.$avatarForm.get(0).reset();
		this.$avatar.attr('src', this.url);
		var paramID = this.$container.data("id");
		$("#" + paramID).val(this.url);
		this.stopCropper();
		//		this.$avatarModal.modal('hide');
		this.$BootstrapDialog.close();
	},

	alert: function(msg) {
		//			var $alert = [
		//				'<div class="alert alert-danger avater-alert">',
		//				'<button type="button" class="close" data-dismiss="alert">&times;</button>',
		//				msg,
		//				'</div>'
		//			].join('');
		//
		//			this.$avatarUpload.after($alert);

		toastr.warning(msg);

	}
};