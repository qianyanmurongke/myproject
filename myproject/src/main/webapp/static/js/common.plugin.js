//if(window.top.BootstrapDialog != undefined) {
//	BootstrapDialog = window.top.BootstrapDialog;
//} else {
//	BootstrapDialog = BootstrapDialog;
//}
//if(window.top.toastr != undefined) {
//	toastr = window.top.toastr;
//} else {
//	toastr = toastr;
//}
//if(window.top.swal != undefined) {
//	swal = window.top.swal;
//} else {
//	swal = swal;
//}

Common = {};
Common.plugin = {};
/**
 * 人员多选
 * @param {Object} title "选择人员"
 * @param {Object} cssClass "chooseemployee-multi-dialog"
 * @param {Object} userids []
 * @param {Object} realnames []
 * @param {Object} SelectFunction  callback
 */
Common.plugin.Employee_Multi = function(options) {
	var title = options.title || "选择人员";
	var cssClass = options.cssClass || "chooseemployee-multi-dialog";
	BootstrapDialog.show({
		closeByBackdrop: false,
		closeByKeyboard: true,
		draggable: true,
		cssClass: cssClass,
		title: title,
		message: $('<div class="loadingDialog"><div class="loading-content"style="left: 684px; top: 11.5px;">正在加载界面中，请稍后…</div></div>'),
		onshown: function(dialog) {
			var divContent = $('<div></div>');
			divContent.load(CTX + CMSCP + "/education/employee/choose_employee_multi.do", function(response, status, xhr) {
				dialog.setMessage(divContent);
				dialog.setButtons([{
					label: '<i class="glyphicon glyphicon-ok"></i>&nbsp;确认选择',
					cssClass: 'btn-info',
					action: function(dialog) {
						var item = dialog.getModalBody();
						var paramIds = new Array();
						var paramNames = new Array();
						$(item).find("#selectEmployeeMultiList li").each(function() {
							paramIds.push($(this).attr("userid"));
							paramNames.push($(this).attr("realname"));
						});
						/*	if(paramIds.length <= 0) {
								toastr.warning('请选择人员。');
								return false;
							}*/
						if(options.SelectFunction != undefined) {
							options.SelectFunction(paramIds, paramNames);
						}
						dialog.close();
					}
				}, {
					label: '<i class="glyphicon glyphicon-remove"></i>&nbsp;关闭',
					action: function(dialog) {
						dialog.close();
					}
				}]);

				if(options.userids != undefined) {
					for(var i = 0; i < options.userids.length; i++) {
						if(options.userids[i] == "")
							continue;
						AddSelectEmployeeMultiInfo(options.userids[i], options.realnames[i]);
					}
				}
			});
		}
	});
}

Common.plugin.Student_Multi = function(options) {
	var title = options.title || "选择学生";
	var cssClass = options.cssClass || "chooseemployee-multi-dialog";
	BootstrapDialog.show({
		closeByBackdrop: false,
		closeByKeyboard: true,
		draggable: true,
		cssClass: cssClass,
		title: title,
		message: $('<div></div>').load(CTX + CMSCP + "/education/student/choose_student_multi.do"),
		onshown: function() {
			if(options.userids != undefined) {
				for(var i = 0; i < options.userids.length; i++) {
					if(options.userids[i] == "")
						continue;
					AddSelectStudentMultiInfo(options.userids[i], options.realnames[i]);
				}
			}
		},
		buttons: [{
			label: '<i class="glyphicon glyphicon-ok"></i>&nbsp;确认选择',
			cssClass: 'btn-info',
			action: function(dialog) {
				var item = dialog.getModalBody();
				var paramIds = new Array();
				var paramNames = new Array();
				$(item).find("#selectStudentMultiList li").each(function() {
					paramIds.push($(this).attr("userid"));
					paramNames.push($(this).attr("realname"));
				});
				/*	if(paramIds.length <= 0) {
						toastr.warning('请选择人员。');
						return false;
					}*/
				if(options.SelectFunction != undefined) {
					options.SelectFunction(paramIds, paramNames);
				}
				dialog.close();
			}
		}, {
			label: '<i class="glyphicon glyphicon-remove"></i>&nbsp;关闭',
			action: function(dialog) {
				dialog.close();
			}
		}]
	});
}

/* ================================================
 * For lazy people
 * ================================================ */

/**
 * Shortcut function: show
 *
 * @param {type} options
 * @returns the created dialog instance
 */
BootstrapDialog.showDialog = function(options) {
	options.closeByBackdrop = false;
	options.closeByKeyboard = true;
	options.draggable = true;
	options.message = $('<div class="loadingDialog"><div class="loading-content">正在加载界面中，请稍后…</div></div>');
	options.onshown = function(dialog) {
		var divContent = $('<div></div>');
		divContent.load(options.loadUrl, function(response, status, xhr) {
			dialog.setMessage(divContent);
			dialog.setButtons(options.buttons);
		});
	};
	return BootstrapDialog.show(options);

};