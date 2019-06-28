$(function() {
	var shopId = 1;
	var listUrl = '/jwExp/shopadmin/getproductcategorylist';
	var addUrl = '/jwExp/shopadmin/addproductcategorys';

	getList();
	function getList() {
		$
				.getJSON(
						listUrl,
						function(data) {
							if (data.success) {
								var dataList = data.data;
								$('.category-wrap').html('');
								var tempHtml = '';
								dataList
										.map(function(item, index) {
											tempHtml += ''
													+ '<div class="row row-product-category now">'
													+ '<div class="col-33 product-category-name">'
													+ item.productCategoryName
													+ '</div>'
													+ '<div class="col-33">'
													+ item.priority
													+ '</div>'
													+ '<div class="col-33"><a href="#" class="button delete" data-id="'
													+ item.productCategoryId
													+ '">删除</a></div>'
													+ '</div>';
										});
								$('.category-wrap').append(tempHtml);
							}
						});
	}
	// 点击new之后创建一个新行
	$('#new')
			.click(
					function() {
						var tempHtml = '<div class="row row-product-category temp">'
								+ '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
								+ '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
								+ '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
								+ '</div>';
						$('.category-wrap').append(tempHtml);
					});
	// 点击提交数据
	$('#submit').click(function() {
		var tempArr = $('.temp');
		var productCategoryList = [];
		// 遍历数据
		tempArr.map(function(index, item) {
			var tempObj = {};
			tempObj.productCategoryName = $(item).find('.category').val();
			tempObj.priority = $(item).find('.priority').val();
			// 如果权重和店铺名称有的话，才将这个数据放到list去
			if (tempObj.productCategoryName && tempObj.priority) {
				productCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(productCategoryList),
			contentType : 'application/json',
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					getList();
				} else {
					$.toast('提交失败:'+data.errMsg);
				}
			}
		});
	});
});