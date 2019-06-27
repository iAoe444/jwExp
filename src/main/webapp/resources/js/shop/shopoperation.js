/**
 * 这个是shopoperation获取后台信息以及提交页面信息的操作
 */
$(function(){
	var initUrl = '/jwExp/o2o/getshopinitinfo';
	var registerShopUrl = '/jwExp/shopadmin/registershop';
	alert(initUrl);
	getShopInitInfo();
	//获取区域信息和分类信息
	function getShopInitInfo(){
		//建立连接，获取返回的信息
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				//遍历分类信息
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				//遍历区域信息
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				//将信息放到相应的区域中
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	//点击提交后提交内容信息
	$('#submit').click(function(){
		var shop = {};

		//获取表单中的信息
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();

		//shopCategory通过这种方式来获取子选项
		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		shop.area = {
			areaId : $('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};

		//获取图片流
		var shopImg = $("#shop-img")[0].files[0];
		var formData = new FormData();
		formData.append('shopImg', shopImg);
		//将shop通过json的方式传递
		formData.append('shopStr', JSON.stringify(shop));
//		var verifyCodeActual = $('#j_captcha').val();
//		if (!verifyCodeActual) {
//			$.toast('请输入验证码！');
//			return;
//		}
//		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : registerShopUrl,
			type : 'POST',
			// contentType: "application/x-www-form-urlencoded; charset=utf-8",
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
//					if (isEdit){
//						$('#captcha_img').click();
//					} else{
//						window.location.href="/shop/shoplist";
//					}
				} else {
					$.toast('提交失败！' + data.errMsg);
//					$('#captcha_img').click();
				}
			}
		});
	});
})