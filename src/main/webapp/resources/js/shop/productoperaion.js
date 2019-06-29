$(function() {
	var productId = getQueryString('productId');
	var infoUrl = '/jwExp/shopadmin/getproductbyid?productId=' + productId;
	var categoryUrl = '/jwExp/shopadmin/getproductcategorylist';
	var productPostUrl = '/jwExp/shopadmin/modifyproduct';
	var isEdit = false;
	if (productId) {
		// 如果有productId则为编辑操作
		getInfo(productId);
		isEdit = true;
	} else {
		getCategory();
		productPostUrl = '/jwExp/shopadmin/addproduct';
	}

	// 编辑操作的渲染页面
	function getInfo(id) {
		$
				.getJSON(
						infoUrl,
						function(data) {
							if (data.success) {
								var product = data.product;
								$('#product-name').val(product.productName);
								$('#product-desc').val(product.productDesc);
								$('#priority').val(product.priority);
								$('#normal-price').val(product.normalPrice);
								$('#promotion-price').val(
										product.promotionPrice);

								var optionHtml = '';
								var optionArr = data.productCategoryList;
								var optionSelected = product.productCategory.productCategoryId;
								optionArr
										.map(function(item, index) {
											var isSelect = optionSelected === item.productCategoryId ? 'selected'
													: '';
											optionHtml += '<option data-value="'
													+ item.productCategoryId
													+ '"'
													+ isSelect
													+ '>'
													+ item.productCategoryName
													+ '</option>';
										});
								$('#category').html(optionHtml);
							}
						});
	}

	// 获取店铺所有的商品类别
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				var productCategoryList = data.data;
				var optionHtml = '';
				productCategoryList.map(function(item, index) {
					optionHtml += '<option data-value="'
							+ item.productCategoryId + '">'
							+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	// 上传商品详情图片的操作，点击后就出现新的控件，达到六个后，无法再添加
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});

	$('#submit').click(
			function() {
				var product = {};
				product.productName = $('#product-name').val();
				product.productDesc = $('#product-desc').val();
				product.priority = $('#priority').val();
				product.normalPrice = $('#normal-price').val();
				product.promotionPrice = $('#promotion-price').val();
				product.productCategory = {
					productCategoryId : $('#category').find('option').not(
							function() {
								return !this.selected;
							}).data('value')
				};
				product.productId = productId;

				// 获取缩略图文件流
				var thumbnail = $('#small-img')[0].files[0];
				console.log(thumbnail);
				// 生成表单对象
				var formData = new FormData();
				formData.append('thumbnail', thumbnail);
				// 获取详情图文件流
				$('.detail-img').map(
						function(index, item) {
							// 判断控件是否已经选择了文件
							if ($('.detail-img')[index].files.length > 0) {
								// 传递productImg+ index的文件信息给formData
								formData.append('productImg' + index,
										$('.detail-img')[index].files[0]);
							}
						});
				formData.append('productStr', JSON.stringify(product));
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				$.ajax({
					url : productPostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功！');
							$('#captcha_img').click();
						} else {
							$.toast('提交失败：' + data.errMsg);
							$('#captcha_img').click();
						}
					}
				});
			});

});