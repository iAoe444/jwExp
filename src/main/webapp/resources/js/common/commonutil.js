//日期按格式化输出
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

// 根据url获取参数名后取它的值，例如?shopId=1，那么就会返回1
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return decodeURIComponent(r[2]);
	}
	return '';
}
function changeVerifyCode(img) {
	// 生成随机数
	img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

function ifLogin(){
	var ifLoginUrl = '/jwExp/person/iflogin';
	$.getJSON(ifLoginUrl,function(data){
		var html = '';
		if(data.login){
			html ='<p><a href="#" class="close-panel">你好，'+data.userName+'</a></p>' 
				+ '<p><a href="/jwExp/shopadmin/shoplist" class="close-panel">管理我的商店</a></p>'
				+ '<p><a href="#" class="close-panel">退出</a></p>'
			$('.panel').html(html);
		}else{
			html = '<p><a href="/jwExp/personadmin/login" class="close-panel">登录</a></p>'
			$('.panel').html(html);
		}
	});
}