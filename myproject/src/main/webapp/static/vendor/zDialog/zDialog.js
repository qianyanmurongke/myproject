﻿/**
 * zDialog 2.0
 * 最后修正：2009-12-18
 **/
var IMAGESPATH = CTX + '/static/backframe/images/'; //图片路径配置
//var IMAGESPATH = 'http://www.5icool.org/'; //图片路径配置
/*************************一些公用方法和属性****************************/
var isIE = navigator.userAgent.indexOf('MSIE') != -1;
var isIE6 = navigator.userAgent.indexOf('MSIE 6.0') != -1;
var isIE8 = !!window.XDomainRequest && !!document.documentMode;
if(isIE)
	try{ document.execCommand('BackgroundImageCache',false,true); }catch(e){}

var $id = function (id) {
    return typeof id == "string" ? document.getElementById(id) : id;
};
//if (!$) var $ = $id;

Array.prototype.remove = function (s, dust) { //如果dust为ture，则返回被删除的元素
    if (dust) {
        var dustArr = [];
        for (var i = 0; i < this.length; i++) {
            if (s == this[i]) {
                dustArr.push(this.splice(i, 1)[0]);
            }
        }
        return dustArr;
    }
    for (var i = 0; i < this.length; i++) {
        if (s == this[i]) {
            this.splice(i, 1);
        }
    }
    return this;
}

var $topWindow = function () {
    var parentWin = window;
//    while (parentWin != parentWin.parent) {
//        if (parentWin.parent.document.getElementsByTagName("FRAMESET").length > 0) break;
//        parentWin = parentWin.parent;
//    }
    return parentWin;
};
var $bodyDimensions = function (win) {
    win = win || window;
    var doc = win.document;
    var cw = doc.compatMode == "BackCompat" ? doc.body.clientWidth : doc.documentElement.clientWidth;
    var ch = doc.compatMode == "BackCompat" ? doc.body.clientHeight : doc.documentElement.clientHeight;
    var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
    var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop); //考虑滚动的情况
    var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
    var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight); //考虑滚动的情况
    var w = Math.max(sw, cw); //取scrollWidth和clientWidth中的最大值
    var h = Math.max(sh, ch); //IE下在页面内容很少时存在scrollHeight<clientHeight的情况
    return {
        "clientWidth": cw,
        "clientHeight": ch,
        "scrollLeft": sl,
        "scrollTop": st,
        "scrollWidth": sw,
        "scrollHeight": sh,
        "width": w,
        "height": h
    }
};

var fadeEffect = function(element, start, end, speed, callback){//透明度渐变：start:开始透明度 0-100；end:结束透明度 0-100；speed:速度1-100
	if(!element.effect)
		element.effect = {fade:0, move:0, size:0};
	clearInterval(element.effect.fade);
	var speed=speed||20;
	element.effect.fade = setInterval(function(){
		start = start < end ? Math.min(start + speed, end) : Math.max(start - speed, end);
		element.style.opacity =  start / 100;
		element.style.filter = "alpha(opacity=" + start + ")";
		if(start == end){
			clearInterval(element.effect.fade);
			if(callback)
				callback.call(element);
		}
	}, 20);
};

/*************************弹出框类实现****************************/
var topWin = $topWindow();
var topDoc = topWin.document;
var Dialog = function () {
    /****以下属性以大写开始，可以在调用show()方法前设置值****/
    this.ID = null;
    this.Width = null;
    this.Height = null;
    this.URL = null;
	this.OnLoad=null;
    this.InnerHtml = ""
    this.InvokeElementId = ""
    this.Top = "50%";
    this.Left = "50%";
    this.Title = "";
    this.OKEvent = null; //点击确定后调用的方法
    this.CancelEvent = null; //点击取消及关闭后调用的方法
    this.ShowButtonRow = false;
    this.MessageIcon = "window.gif";
    this.MessageTitle = "";
    this.Message = "";
    this.ShowMessageRow = false;
    this.Modal = true;
    this.Drag = true;
    this.AutoClose = null;
    this.ShowCloseButton = true;
	this.Animator = true;
    /****以下属性以小写开始，不要自行改变****/
    this.dialogDiv = null;
	this.bgDiv=null;
    this.parentWindow = null;
    this.innerFrame = null;
    this.innerWin = null;
    this.innerDoc = null;
    this.zindex = 900;
    this.cancelButton = null;
    this.okButton = null;

    if (arguments.length > 0 && typeof(arguments[0]) == "string") { //兼容旧写法
        this.ID = arguments[0];
    } else if (arguments.length > 0 && typeof(arguments[0]) == "object") {
        Dialog.setOptions(this, arguments[0])
    }
	if(!this.ID)
        this.ID = topWin.Dialog._Array.length + "";

};
Dialog._Array = [];
Dialog.bgDiv = null;
Dialog.setOptions = function (obj, optionsObj) {
    if (!optionsObj) return;
    for (var optionName in optionsObj) {
        obj[optionName] = optionsObj[optionName];
    }
};
Dialog.attachBehaviors = function () {
    if (isIE) {
        document.attachEvent("onkeydown", Dialog.onKeyDown);
        window.attachEvent('onresize', Dialog.resetPosition);
    } else {
        document.addEventListener("keydown", Dialog.onKeyDown, false);
        window.addEventListener('resize', Dialog.resetPosition, false);
    }
};
Dialog.prototype.attachBehaviors = function () {
    if (this.Drag && topWin.Drag) topWin.Drag.init(topWin.$id("_Draghandle_" + this.ID), topWin.$id("_DialogDiv_" + this.ID)); //注册拖拽方法
    if (!isIE && this.URL) { //非ie浏览器下在拖拽时用一个层遮住iframe，以免光标移入iframe失去拖拽响应
        var self = this;
        topWin.$id("_DialogDiv_" + this.ID).onDragStart = function () {
            topWin.$id("_Covering_" + self.ID).style.display = ""
        }
        topWin.$id("_DialogDiv_" + this.ID).onDragEnd = function () {
            topWin.$id("_Covering_" + self.ID).style.display = "none"
        }
    }
};
Dialog.prototype.displacePath = function () {
    if (this.URL.substr(0, 7) == "http://" || this.URL.substr(0, 1) == "/" || this.URL.substr(0, 11) == "javascript:") {
        return this.URL;
    } else {
        var thisPath = this.URL;
        var locationPath = window.location.href;
        locationPath = locationPath.substring(0, locationPath.lastIndexOf('/'));
        while (thisPath.indexOf('../') >= 0) {
            thisPath = thisPath.substring(3);
            locationPath = locationPath.substring(0, locationPath.lastIndexOf('/'));
        }
        return locationPath + '/' + thisPath;
    }
};
Dialog.prototype.setPosition = function () {
    var bd = $bodyDimensions(topWin);
    var thisTop = this.Top,
        thisLeft = this.Left,
		thisdialogDiv=this.getDialogDiv();
    if (typeof this.Top == "string" && this.Top.substring(this.Top.length - 1, this.Top.length) == "%") {
        var percentT = this.Top.substring(0, this.Top.length - 1) * 0.01;
        thisTop = bd.clientHeight * percentT - thisdialogDiv.scrollHeight * percentT + bd.scrollTop;
    }
    if (typeof this.Left == "string" && this.Left.substring(this.Left.length - 1, this.Left.length) == "%") {
        var percentL = this.Left.substring(0, this.Left.length - 1) * 0.01;
        thisLeft = bd.clientWidth * percentL - thisdialogDiv.scrollWidth * percentL + bd.scrollLeft;
    }
    thisdialogDiv.style.top = Math.round(thisTop) + "px";
    thisdialogDiv.style.left = Math.round(thisLeft) + "px";
};
Dialog.setBgDivSize = function () {
    var bd = $bodyDimensions(topWin);
	if(Dialog.bgDiv){
			if(isIE6){
				Dialog.bgDiv.style.height = bd.clientHeight + "px";
				Dialog.bgDiv.style.top=bd.scrollTop + "px";
				Dialog.bgDiv.style.display = "none";//让div重渲染,修正IE6下尺寸bug
				Dialog.bgDiv.style.display = "";
			}else{
				Dialog.bgDiv.style.height = bd.scrollHeight + "px";
			}
		}
};
Dialog.resetPosition = function () {
    Dialog.setBgDivSize();
    for (var i = 0, len = topWin.Dialog._Array.length; i < len; i++) {
        topWin.Dialog._Array[i].setPosition();
    }
};
Dialog.prototype.create = function () {
    var bd = $bodyDimensions(topWin);
    if (typeof(this.OKEvent)== "function") this.ShowButtonRow = true;
    if (!this.Width) this.Width = Math.round(bd.clientWidth * 4 / 10);
    if (!this.Height) this.Height = Math.round(this.Width / 2);
    if (this.MessageTitle || this.Message) this.ShowMessageRow = true;
    var DialogDivWidth = this.Width + 13 + 13;
    var DialogDivHeight = this.Height + 33 + 13 + (this.ShowButtonRow ? 40 : 0) + (this.ShowMessageRow ? 50 : 0);

    if (DialogDivWidth > bd.clientWidth) this.Width = Math.round(bd.clientWidth - 26);
    if (DialogDivHeight > bd.clientHeight) this.Height = Math.round(bd.clientHeight - 46 - (this.ShowButtonRow ? 40 : 0) - (this.ShowMessageRow ? 50 : 0));

    var html = '\
  <table id="_DialogTable_' + this.ID + '" class="aui_border" cellspacing="0" cellpadding="0" border="0" onselectstart="return false;" style="-moz-user-select: none; font-size:12px; line-height:1.4;width:' + this.Width + 'px;">\
    <tbody>\
    <tr><td class="aui_nw"></td><td class="aui_n"></td><td class="aui_ne"></td></tr>\
    <tr><td class="aui_w"></td>\
        <td class="aui_c">\
            <div class="aui_inner">\
                <table class="aui_dialog">\
                    <tbody>\
                        <tr>\
                            <td class="aui_header" id="_Draghandle_' + this.ID + '">\
                                <div class="aui_titleBar">\
                                    <div  class="aui_title" style="' + (this.Drag ? "cursor: move;" : "") + '">' + this.Title + '</div>\
                                    <a href="javascript:/*close*/;" class="aui_close" style="' + (this.ShowCloseButton ? "" : "display:none;") + '" onclick="Dialog.getInstance(\'' + this.ID + '\').cancelButton.onclick.apply(Dialog.getInstance(\'' + this.ID + '\').cancelButton,[]);" >×</a>\
                                </div>\
                            </td>\
                        </tr>\
                        <tr>\
                            <td style="padding:0;">\
                             <div id="_Container_' + this.ID + '" class="aui_main" style="min-width: 200px; width: ' + this.Width + 'px; height: ' + this.Height + 'px;">\
                                <div  id="_Container_html_' + this.ID + '" class="aui_content aui_state_full" style="padding: 20px 25px;">\
                                <div style="position: absolute; height: 100%; width: 100%; display: none; background-color:#fff; opacity: 0.5;" id="_Covering_' + this.ID + '">&nbsp;</div>\
	                ' + (function (obj) {
	                    if (obj.InnerHtml) return obj.InnerHtml;
	                    if (obj.URL) return '<iframe width="100%" height="100%" frameborder="0" style="border:none 0;" allowtransparency="true" id="_DialogFrame_' + obj.ID + '" src="' + obj.displacePath() + '"></iframe>';
	                    return "";
	                })(this) + '\
                              </div>\
                              </div>\
                            </td>\
                        </tr>\
                        <tr>\
                            <td class="aui_footer"  id="_ButtonRow_' + this.ID + '" style="' + (this.ShowButtonRow ? "" : "display:none") + '">\
                                <div class="aui_buttons" id="_DialogButtons_' + this.ID + '" >\
                                    <button value="确定" id="_ButtonOK_' + this.ID + '" class="z-dlg-button z-dialog-cancelbutton aui_state_highlight">确定</button>\
                                    <button value="取消"  onclick="Dialog.getInstance(\'' + this.ID + '\').close();" id="_ButtonCancel_' + this.ID + '" class="z-dlg-button z-dialog-cancelbutton">取消</button>\
                                </div>\
                            </td>\
                        </tr>\
                    </tbody>\
                </table>\
            </div>\
        </td>\
        <td class="aui_e"></td>\
    </tr>\
  </table>';
  html='<div class="modal-dialog" id="_DialogTable_' + this.ID + '">'
    	 + '	<div class="modal-content">'
    	 + '		<div class="modal-header" id="_Draghandle_' + this.ID + '">'
    	 + '			<div class="bootstrap-dialog-header"  style="' + (this.Drag ? "cursor: move;" : "") + '">'
    	 + '				<div class="bootstrap-dialog-close-button" style="' + (this.ShowCloseButton ? "display: block;" : "display:none;") + '">'
    	 + '					<button class="close"  onclick="Dialog.getInstance(\'' + this.ID + '\').cancelButton.onclick.apply(Dialog.getInstance(\'' + this.ID + '\').cancelButton,[]);">×</button>'
    	 + '				</div>'
    	 + '				<div class="bootstrap-dialog-title">' + this.Title + '</div>'
    	 + '			</div>'
    	 + '		</div>'
    	 + '		<div class="modal-body" id="_Container_' + this.ID + '" style="min-width: 200px; width: ' + this.Width + 'px; height: ' + this.Height + 'px;">'
    	 + '			<div class="bootstrap-dialog-body" >'
    	 + '				<div id="_Container_html_' + this.ID + '">'
    	 + (function (obj) {
	         if (obj.InnerHtml) return obj.InnerHtml;
	         if (obj.URL) return '<iframe width="100%" height="100%" frameborder="0" style="border:none 0;" allowtransparency="true" id="_DialogFrame_' + obj.ID + '" src="' + obj.displacePath() + '"></iframe>';
	         return "";
	     })(this)
    	 + '				</div>'
    	 + '			</div>'
    	 + '		</div>'
    	 + '	</div>'
    	 + '	<div class="modal-footer"  style="' + (this.ShowButtonRow ? "display: block;" : "display:none") + '"  id="_ButtonRow_' + this.ID + '">'
    	 + '		<div class="bootstrap-dialog-footer"></div>'
    	 + '	</div>'
    	 + '</div>';


    var div = topWin.$id("_DialogDiv_" + this.ID);
    if (!div) {
        div = topDoc.createElement("div");
        div.id = "_DialogDiv_" + this.ID;
        topDoc.getElementsByTagName("BODY")[0].appendChild(div);
    }
    div.style.position = "fixed";
    div.style.width = this.Width + "px";
    div.style.left = "-9999px";
    div.style.top = "-9999px";
    div.style.display = "block";
    div.className = "modal bootstrap-dialog type-primary fade size-normal in";
    div.innerHTML = html;
    if (this.InvokeElementId) {
        var element = $id(this.InvokeElementId);
        element.style.position = "";
        element.style.display = "";
        if (isIE) {
            var fragment = topDoc.createElement("div");
            fragment.innerHTML = element.outerHTML;
            element.outerHTML = "";
            topWin.$id("_Covering_" + this.ID).parentNode.appendChild(fragment)
        } else {
            topWin.$id("_Covering_" + this.ID).parentNode.appendChild(element)
        }
    }
    this.parentWindow = window;
    if (this.URL) {
        if (topWin.$id("_DialogFrame_" + this.ID)) {
            this.innerFrame = topWin.$id("_DialogFrame_" + this.ID);
        };
        var self = this;
        innerFrameOnload = function () {
            try {
				self.innerWin = self.innerFrame.contentWindow;
				self.innerWin.parentDialog = self;
                self.innerDoc = self.innerWin.document;
                if (!self.Title && self.innerDoc && self.innerDoc.title) {
                    if (self.innerDoc.title) topWin.$id("_Title_" + self.ID).innerHTML = self.innerDoc.title;
                };
            } catch(e) {
                if (console && console.log) console.log("可能存在访问限制，不能获取到iframe中的对象。")
            }
            if (typeof(self.OnLoad)== "function")self.OnLoad();
        };
        if (this.innerFrame.attachEvent) {
            this.innerFrame.attachEvent("onload", innerFrameOnload);
        } else {
            this.innerFrame.onload = innerFrameOnload;
        };
    };
    topWin.$id("_DialogDiv_" + this.ID).dialogId = this.ID;
    topWin.$id("_DialogDiv_" + this.ID).dialogInstance = this;
    this.attachBehaviors();
    this.okButton = topWin.$id("_ButtonOK_" + this.ID);
    this.cancelButton = topWin.$id("_ButtonCancel_" + this.ID);
	div=null;
};
Dialog.prototype.setHtml = function(paramHtml){
	$id("_Container_html_" + this.ID).style.overflow = "auto";
	$($id("_Container_html_" + this.ID)).append(paramHtml)
};
Dialog.prototype.setSize = function (w, h) {
    if (w && +w > 20) {
        this.Width = +w;
        topWin.$id("_DialogTable_" + this.ID).width = this.Width + 26;
        topWin.$id("_Container_" + this.ID).style.width = this.Width + "px";
    }
    if (h && +h > 10) {
        this.Height = +h;
        topWin.$id("_Container_" + this.ID).style.height = this.Height + "px";
    }
    this.setPosition();
};
Dialog.prototype.show = function () {
    this.create();
    var bgdiv = Dialog.getBgdiv(),
		thisdialogDiv=this.getDialogDiv();
    this.zindex = thisdialogDiv.style.zIndex = Dialog.bgDiv.style.zIndex + 1;
    if (topWin.Dialog._Array.length > 0) {
        this.zindex = thisdialogDiv.style.zIndex = topWin.Dialog._Array[topWin.Dialog._Array.length - 1].zindex + 2;
    } else {
        var topWinBody = topDoc.getElementsByTagName(topDoc.compatMode == "BackCompat" ? "BODY" : "HTML")[0];
        //topWinBody.styleOverflow = topWinBody.style.overflow; 去除滚动条
        //topWinBody.style.overflow = "hidden"; 去除滚动条
        bgdiv.style.display = "none";
    }
    topWin.Dialog._Array.push(this);
    if (this.Modal) {
        bgdiv.style.zIndex = topWin.Dialog._Array[topWin.Dialog._Array.length - 1].zindex - 1;
        Dialog.setBgDivSize();
		if(bgdiv.style.display == "none"){
			if(this.Animator){
		/*		var bgMask=topWin.$id("_DialogBGMask");
				bgMask.style.opacity = 0;
				bgMask.style.filter = "alpha(opacity=0)";
        		bgdiv.style.display = "";
				fadeEffect(bgMask,0,40,isIE6?20:10);
				bgMask=null;*/
        		bgdiv.style.display = "block";
			}else{
        		bgdiv.style.display = "block";
			}
		}
    }
    this.setPosition();
    if (this.CancelEvent) {
        this.cancelButton.onclick = this.CancelEvent;
        if(this.ShowButtonRow)this.cancelButton.focus();
    }
    if (this.OKEvent) {
        this.okButton.onclick = this.OKEvent;
        if(this.ShowButtonRow)this.okButton.focus();
    }
    if (this.AutoClose && this.AutoClose > 0) this.autoClose();
    this.opened = true;
	bgdiv=null;
};
Dialog.prototype.close = function () {
	var thisdialogDiv=this.getDialogDiv();
    if (this == topWin.Dialog._Array[topWin.Dialog._Array.length - 1]) {
        var isTopDialog = topWin.Dialog._Array.pop();
    } else {
        topWin.Dialog._Array.remove(this)
    }
    if (this.InvokeElementId) {
        var innerElement = topWin.$id(this.InvokeElementId);
        innerElement.style.display = "none";
        if (isIE) {
            //ie下不能跨窗口拷贝元素，只能跨窗口拷贝html代码
            var fragment = document.createElement("div");
            fragment.innerHTML = innerElement.outerHTML;
            innerElement.outerHTML = "";
            document.getElementsByTagName("BODY")[0].appendChild(fragment)
        } else {
            document.getElementsByTagName("BODY")[0].appendChild(innerElement)
        }

    }
    if (topWin.Dialog._Array.length > 0) {
        if (this.Modal && isTopDialog) Dialog.bgDiv.style.zIndex = topWin.Dialog._Array[topWin.Dialog._Array.length - 1].zindex - 1;
    } else {
        Dialog.bgDiv.style.zIndex = "900";
        Dialog.bgDiv.style.display = "none";
        var topWinBody = topDoc.getElementsByTagName(topDoc.compatMode == "BackCompat" ? "BODY" : "HTML")[0];
        //if (topWinBody.styleOverflow != undefined) topWinBody.style.overflow = topWinBody.styleOverflow; 去除滚动条
    }
    if (isIE) {
		/*****释放引用，以便浏览器回收内存**/
		thisdialogDiv.dialogInstance=null;
		if(this.innerFrame)this.innerFrame.detachEvent("onload", innerFrameOnload);
		this.innerFrame=null;
		this.parentWindow=null;
		this.bgDiv=null;
		if (this.CancelEvent){this.cancelButton.onclick = null;};
		if (this.OKEvent){this.okButton.onclick = null;};
		topWin.$id("_DialogDiv_" + this.ID).onDragStart=null;
		topWin.$id("_DialogDiv_" + this.ID).onDragEnd=null;
		topWin.$id("_Draghandle_" + this.ID).onmousedown=null;
		topWin.$id("_Draghandle_" + this.ID).root=null;
		/**end释放引用**/
        thisdialogDiv.outerHTML = "";
		CollectGarbage();
    } else {
        var RycDiv = topWin.$id("_RycDiv");
        if (!RycDiv) {
            RycDiv = topDoc.createElement("div");
            RycDiv.id = "_RycDiv";
        }
        RycDiv.appendChild(thisdialogDiv);
        RycDiv.innerHTML = "";
		RycDiv=null;
    }
	thisdialogDiv=null;
    this.closed = true;
};
Dialog.prototype.autoClose = function () {
    if (this.closed) {
        clearTimeout(this._closeTimeoutId);
        return;
    }
    this.AutoClose -= 1;
    topWin.$id("_Title_" + this.ID).innerHTML = this.AutoClose + " 秒后自动关闭";
    if (this.AutoClose <= 0) {
        this.close();
    } else {
        var self = this;
        this._closeTimeoutId = setTimeout(function () {
            self.autoClose();
        },
        1000);
    }
};
Dialog.getInstance = function (id) {
    var dialogDiv = topWin.$id("_DialogDiv_" + id);
    if (!dialogDiv) alert("没有取到对应ID的弹出框页面对象");
	try{
    	return dialogDiv.dialogInstance;
	}finally{
		dialogDiv = null;
	}
};
Dialog.prototype.addButton = function (id, txt, func) {
    topWin.$id("_ButtonRow_" + this.ID).style.display = "";
    this.ShowButtonRow = true;
    var button = topDoc.createElement("button");
    button.className ="z-dlg-button z-dialog-cancelbutton aui_state_highlight"
    button.id = "_Button_" + this.ID + "_" + id;
    button.type = "button";
    button.style.cssText = "margin-right:5px";
    button.value = txt;
    button.innerHTML = txt;
    button.onclick = func;
    var input0 = topWin.$id("_DialogButtons_" + this.ID).getElementsByTagName("button")[0];
    input0.parentNode.insertBefore(button, input0);
    return button;
};
Dialog.prototype.removeButton = function (btn) {
    var input0 = topWin.$id("_DialogButtons_" + this.ID).getElementsByTagName("button")[0];
    input0.parentNode.removeChild(btn);
};
Dialog.getBgdiv = function () {
    if (Dialog.bgDiv) return Dialog.bgDiv;
    var bgdiv = topWin.$id("_DialogBGDiv");
    if (!bgdiv) {
        bgdiv = topDoc.createElement("div");
        bgdiv.id = "_DialogBGDiv";
        bgdiv.style.cssText = "z-index:1050;";
        bgdiv.className = "modal-backdrop fade in";
        topDoc.getElementsByTagName("BODY")[0].appendChild(bgdiv);
       /* var bgIframeBox = '<div style="position:relative;width:100%;height:100%;">';
		var bgIframeMask = '<div id="_DialogBGMask" style="position:absolute;background-color:#333;opacity:0.4;filter:alpha(opacity=40);width:100%;height:100%;"></div>';
		var bgIframe = isIE6?'<iframe src="about:blank" style="filter:alpha(opacity=0);" width="100%" height="100%"></iframe>':'';
		bgdiv.innerHTML=bgIframeBox+bgIframeMask+bgIframe+'</div>';
        topDoc.getElementsByTagName("BODY")[0].appendChild(bgdiv);
        if (isIE6) {
            var bgIframeDoc = bgdiv.getElementsByTagName("IFRAME")[0].contentWindow.document;
            bgIframeDoc.open();
            bgIframeDoc.write("<body style='background-color:#333' oncontextmenu='return false;'></body>");
            bgIframeDoc.close();
			bgIframeDoc=null;
        }*/
    }
    Dialog.bgDiv = bgdiv;
	bgdiv=null;
    return Dialog.bgDiv;
};
Dialog.prototype.getDialogDiv = function () {
	var dialogDiv=topWin.$id("_DialogDiv_" + this.ID)
	if(!dialogDiv)alert("获取弹出层页面对象出错！");
	try{
		return dialogDiv;
	}finally{
		dialogDiv = null;
	}
};
Dialog.onKeyDown = function (event) {
    if (event.shiftKey && event.keyCode == 9) { //shift键
        if (topWin.Dialog._Array.length > 0) {
            stopEvent(event);
            return false;
        }
    }
    if (event.keyCode == 27) { //ESC键
        Dialog.close();
    }
};
Dialog.close = function (id) {
    if (topWin.Dialog._Array.length > 0) {
        var diag = topWin.Dialog._Array[topWin.Dialog._Array.length - 1];
        diag.cancelButton.onclick.apply(diag.cancelButton, []);
    }
};
Dialog.alert = function (msg, func, w, h) {
    var w = w || 250,
        h = h || 80;
    var diag = new Dialog({
        Width: w,
        Height: h
    });
    diag.ShowButtonRow = true;
    diag.Title = "系统提示";
    diag.OKEvent = function () {
        diag.close();
        if (func) func();
    };

    //diag.InnerHtml = '<table height="100%" border="0" align="center" cellpadding="10" cellspacing="0">\
	//	<tr><td align="right"><img id="Icon_' + this.ID + '" src="' + IMAGESPATH + 'icon_alert.gif" width="34" height="34" align="absmiddle"></td>\
	//		<td align="left" id="Message_' + this.ID + '" style="font-size:9pt">' + msg + '</td></tr>\
	//</table>';

    diag.InnerHtml = '<div class="aui_icon">\
                        <div class="aui_iconBg" id="Icon_' + this.ID + '" style="background-image: url(' + IMAGESPATH + 'icon_alert.gif);"></div>\
                      </div>\
                      <div class="aui_main" id="Message_' + this.ID + '" style="min-width: 200px;">\
                        <div class="aui_content" style="padding: 20px 25px;">' + msg + '</div>\
                      </div>';
    diag.show();
    // diag.okButton.parentNode.style.textAlign = "center";
    diag.cancelButton.style.display = "none";
    diag.okButton.focus();
};
Dialog.confirm = function (msg, funcOK, funcCal, w, h) {
    var w = w || 250,
        h = h || 60;
    var diag = new Dialog({
        Width: w,
        Height: h
    });
    diag.ShowButtonRow = true;
    diag.Title = "信息确认";
    diag.CancelEvent = function () {
        diag.close();
        if (funcCal) {
            funcCal();
        }
    };
    diag.OKEvent = function () {
        diag.close();
        if (funcOK) {
            funcOK();
        }
    };
    //diag.InnerHtml = '<table height="100%" border="0" align="center" cellpadding="10" cellspacing="0">\
	//	<tr><td align="right"><img id="Icon_' + this.ID + '" src="' + IMAGESPATH + 'icon_query.gif" width="34" height="34" align="absmiddle"></td>\
	//		<td align="left" id="Message_' + this.ID + '" style="font-size:9pt">' + msg + '</td></tr>\
	//</table>';

    diag.InnerHtml = '<div class="aui_icon">\
                        <div class="aui_iconBg" id="Icon_' + this.ID + '" style="background-image: url(' + IMAGESPATH + 'icon_query.gif);"></div>\
                      </div>\
                      <div class="aui_main" id="Message_' + this.ID + '" style="min-width: 200px;">\
                        <div class="aui_content" style="padding: 20px 25px;">' + msg + '</div>\
                      </div>';
    diag.show();
    //diag.okButton.parentNode.style.textAlign = "center";
    diag.okButton.focus();
};
Dialog.open = function (arg) {
    var diag = new Dialog(arg);
    diag.show();
    return diag;
};
if (isIE) {
    window.attachEvent("onload", Dialog.attachBehaviors);
} else {
    window.addEventListener("load", Dialog.attachBehaviors, false);
}