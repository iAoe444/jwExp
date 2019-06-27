/**
 * 这个是shopoperation获取后台信息以及提交页面信息的操作
 */
$(function(){
	//获取传入参数的值
	var shopId = getQueryString('shopId');
	//判断是编辑状态还是注册状态
	var isEdit = shopId?true:false;
	var initUrl = '/jwExp/shopadmin/getshopinitinfo';
	var registerShopUrl = '/jwExp/shopadmin/registershop';
	
	var shopInfoUrl = '/jwExp/shopadmin/getshopbyid?shopId='+shopId;
	var editShopUrl = '/jwExp/shopadmin/modifyshop';
	
	//通过isEdit来判断是哪种情况的数据初始化
	if(!isEdit){
		getShopInitInfo();
	}else{
		getShopInfo(shopId);
	}
	
	//如果是编辑商店的话，那么就返回以下的内容
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if (data.success) {
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempAreaHtml);
				$("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
			}
		});
	}
	
	//获取区域信息和分类信息
	function getShopInitInfo(){
		//建立连接，获取返回的信息
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	//点击提交后提交内容信息
	$('#submit').click(function(){
		var shop = {};
		if(isEdit){
			shop.shopId = shopId;
		}
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
		//获取验证码内容
		var verifyCodeActual = $('#j_captcha').val();
		//如果为空
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			//通过编辑状态来决定提交哪个页面
			url : (isEdit?editShopUrl:registerShopUrl),
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
					$('#captcha_img').click();
				}
			}
		});
	});
})