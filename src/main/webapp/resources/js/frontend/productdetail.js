$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/jwExp/frontend/listproductdetailpageinfo?productId='
			+ productId;
	
	ifLogin();

	$
			.getJSON(
					productUrl,
					function(data) {
						if (data.success) {
							var product = data.product;
							$('#product-img').attr('src', product.imgAddr);
							$('#product-time').text(
									new Date(product.lastEditTime)
											.Format("yyyy-MM-dd"));
							$('#product-name').text("产品名：" + product.productName);
							$('#product-desc').text("产品描述：" +product.productDesc);
							$('#product-normal-price').text("原价："+product.normalPrice);
							$('#product-promotion-price').text("现价："+product.promotionPrice);
							var imgListHtml = '';
							product.productImgList.map(function(item, index) {
								imgListHtml += '<div> <img src="'
										+ item.imgAddr + '"/></div>';
							});
							$('#imgList').html(imgListHtml);
						}
					});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
