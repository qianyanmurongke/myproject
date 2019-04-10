/// <reference path="jquery-1.10.2.js" />
/*
    <style>
        input.has-error {
            border-color: #a94442;
            box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
        }
        input.has-error:focus {
                border-color: #843534;
                box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 6px #ce8483;
            }
    </style>
*/

// 错误信息显示位置 left top right bottom
//
//var validatorPlacement = "right";
//
//$.validator.setDefaults({
//    errorClass: "has-error",
//	errorPlacement: function (error, element) {
//	    $(element).parent().addClass("has-error");
//		$(element).popover('destroy');
//		$(element).popover({ content: $(error).text(), placement: validatorPlacement });
//		$(element).popover("show");
//	},
//	success: function (error, element) {
//	    $(element).popover('destroy');
//	    $(element).parent().removeClass("has-error");
//	}
//});

$.extend($.validator.messages, {
	required: "必选字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
	minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
	rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
	range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: $.validator.format("请输入一个最大为 {0} 的值"),
	min: $.validator.format("请输入一个最小为 {0} 的值")
});

// 验证两次输入值是否不相同  
$.validator.addMethod("notEqualTo", function (value, element, param) {
	return value != $(param).val();
}, $.validator.format("两次输入不能相同!"));


//只能输入数字  
$.validator.addMethod("isNum", function (value, element) {
	var RegExp = /^\d+$/;
	return RegExp.test(value);
}, $.validator.format("只能为数字!"));


//规则名：buga,value检测对像的值    
$.validator.addMethod("buga", function (value) {
	return value == "buga";
}, 'Please enter "buga"!');


//规则名：chinese，value检测对像的值，element检测的对像    
$.validator.addMethod("chinese", function (value, element) {
	var chinese = /^[\u4e00-\u9fa5]+$/;
	return (chinese.test(value)) || this.optional(element);
}, "只能输入中文");


//规则名：byteRangeLength，value检测对像的值，element检测的对像,param参数    
$.validator.addMethod("byteRangeLength", function (value, element, param) {
	var length = value.length;
	for (var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length >= param[0] && length <= param[1]);
}, $.validator.format("请确保输入的值在{0}-{1}个字节之间(一个中文字算2个字节)"));


// 联系电话(手机/电话皆可)验证  
$.validator.addMethod("isPhone", function (value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	var tel = /^\d{3,4}-?\d{7,9}$/;
	return this.optional(element) || (tel.test(value) || mobile.test(value));


}, "请正确填写您的联系电话");


// 邮政编码验证  
$.validator.addMethod("isZipCode", function (value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

// 字符验证  
$.validator.addMethod("string", function (value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "不允许包含特殊符号!");

// 必须以特定字符串开头验证  
$.validator.addMethod("begin", function (value, element, param) {
	var begin = new RegExp("^" + param);
	return this.optional(element) || (begin.test(value));
}, $.validator.format("必须以 {0} 开头!"));


// 验证值不允许与特定值等于  
$.validator.addMethod("notEqual", function (value, element, param) {
	return value != param;
}, $.validator.format("输入值不允许为{0}!"));


// 验证值必须大于特定值(不能等于)  
$.validator.addMethod("gt", function (value, element, param) {
	return value > param;
}, $.validator.format("输入值必须大于{0}!"));


//验证值必须大于特定值(不能等于)  
$.validator.addMethod("lte", function (value, element, param) {
	return value <= param;
}, $.validator.format("输入值必须小于等{0}!"));


// 验证值小数位数不能超过两位  
$.validator.addMethod("decimal", function (value, element) {
	var decimal = /^-?\d+(\.\d{1,2})?$/;
	return this.optional(element) || (decimal.test(value));
}, $.validator.format("小数位数不能超过两位!"));


//字母数字  
$.validator.addMethod("alnum", function (value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, "只能包括英文字母和数字");


// 身份证号码验证（加强验证）  
$.validator.addMethod("isIdCardNo", function (value, element) {
	return this.optional(element) || /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test(value) || /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/.test(value);
}, "请正确输入您的身份证号码");


// 手机号码验证  
$.validator.addMethod("isMobile", function (value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");


// 电话号码验证  
$.validator.addMethod("isTel", function (value, element) {
	var tel = /^\d{3,4}-?\d{7,9}$/;    //电话号码格式010-12345678  
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的电话号码");

//区号验证规则  
$.validator.addMethod("ac", function (value, element) {
    var ac = /^0\d{2,3}$/;
    return this.optional(element) || (ac.test(value));
}, "区号如：010或0371");