function is_weixin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger') {
		return true;
	} else {
		return false;　
	}
}

function is_ios() {
	var u = navigator.userAgent,
		app = navigator.appVersion;
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //g
	var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	if(isAndroid) {
		return false;
	}
	if(isIOS) {
		return true;
	}
}

$(function() {
	if(is_weixin()) {
		if(is_ios()) {
			//ios
			window.addEventListener("popstate", function(e) {
				alert("后退");
				//self.location.reload();
			}, false);
			var state = {
				title: "",
				url: "#"
			};
			window.history.replaceState(state, "", "#");
		} else {
			//Android
			var needRefresh = sessionStorage.getItem("need-refresh");
			if(needRefresh) {
				sessionStorage.removeItem("need-refresh");
				alert("后退");
				//location.reload();
			}

			sessionStorage.setItem("need-refresh", true);
		}
	}
});