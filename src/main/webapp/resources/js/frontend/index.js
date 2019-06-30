$(function() {
	var url = '/jwExp/frontend/listmainpageinfo';

	ifLogin();
	
	$.getJSON(url, function(data) {
		// 获取初始化信息
		if (data.success) {
			var headLineList = data.headLineList;
			var swiperHtml = '';
			// 渲染头条信息
			headLineList.map(function(item, index) {
				swiperHtml += '' + '<div class="swiper-slide img-wrap" onclick="headline(this)" data-link="'
						+ item.lineLink +'">'
						+ '<img class="banner-img" src="' + item.lineImg
						+ '" alt="' + item.lineName + '">' + '</a></div>';
			});
			// 置入头条
			$('.swiper-wrapper').html(swiperHtml);
			$(".swiper-container").swiper({
				autoplay : 1000,
				autoplayDisableOnInteraction : false
			});
			// 置入商品分类信息
			var shopCategoryList = data.shopCategoryList;
			var categoryHtml = '';
			shopCategoryList.map(function(item, index) {
				categoryHtml += ''
						+ '<div class="col-50 shop-classify" data-category='
						+ item.shopCategoryId + '>' + '<div class="word">'
						+ '<p class="shop-title">' + item.shopCategoryName
						+ '</p>' + '<p class="shop-desc">'
						+ item.shopCategoryDesc + '</p>' + '</div>'
						+ '<div class="shop-classify-img-warp">'
						+ '<img class="shop-img" id="head" src="' + item.shopCategoryImg
						+ '">' + '</div>' + '</div>';
			});
			$('.row').html(categoryHtml);
		}
	});

	// 打开侧边栏
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	// 进入某个分类
	$('.row').on('click', '.shop-classify', function(e) {
		var shopCategoryId = e.currentTarget.dataset.category;
		var newUrl = '/jwExp/frontend/shoplist?parentId=' + shopCategoryId;
		window.location.href = newUrl;
	});
	
});

function headline(e){
	var link = $(e)[0].dataset.link;
	window.location.href = link;
}