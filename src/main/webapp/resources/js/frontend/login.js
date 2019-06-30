$(function(){
	var loginUrl = '/jwExp/person/login';
	
	ifLogin();
	
	$('#submit').click(function(){
		var formData = new FormData();
		var userName = $('#username').val();
		var password = $('#password').val();
		formData.append("userName", userName);
		formData.append("password", password);
		var verifyCodeActual = $('#j_captcha').val();
		//如果为空
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			//通过编辑状态来决定提交哪个页面
			url : loginUrl,
			type : 'POST',
			// contentType: "application/x-www-form-urlencoded; charset=utf-8",
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('登录成功！');
					window.location.href = '/jwExp/frontend/index';
				} else {
					$.toast('登录失败:' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});