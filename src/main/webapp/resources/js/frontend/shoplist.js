$(function() {
	var loading = false;
	// 允许返回的最大条数
	var maxItems = 999;
	// 单页最多的数量
	var pageSize = 10;
	// 查询的url
	var listUrl = '/jwExp/frontend/listshops';
	// 获取区域和店铺列表的url
	var searchDivUrl = '/jwExp/frontend/listshopspageinfo';
	// 页码
	var pageNum = 1;
	var parentId = getQueryString('parentId');
	var areaId = '';
	var shopCategoryId = '';
	var shopName = '';

	// 初始化渲染页面
	getSearchDivData();
	// 预先加载10条店铺信息
	addItems(pageSize, pageNum);

	/**
	 * 获取店铺分类和店铺的分类
	 */
	function getSearchDivData() {
		// 这里有两种情况一种是parentId为空，一种是不为空
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								// 遍历拼接
								var shopCategoryList = data.shopCategoryList;
								var html = '';
								// 将类别渲染进去
								html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
								shopCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-category-id='
													+ item.shopCategoryId
													+ '>'
													+ item.shopCategoryName
													+ '</a>';
										});
								$('#shoplist-search-div').html(html);
								var selectOptions = '<option value="">全部区域</option>';
								var areaList = data.areaList;
								areaList.map(function(item, index) {
									selectOptions += '<option value="'
											+ item.areaId + '">'
											+ item.areaName + '</option>';
								});
								// 将区域渲染进去
								$('#area-search').html(selectOptions);
							}
						});
	}

	/**
	 * 获取分页展示的所有店铺信息
	 */
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
				+ '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
		loading = true;
		$.getJSON(url, function(data) {
			if (data.success) {
				maxItems = data.count;
				var html = '';
				// 遍历添加商品信息
				data.shopList.map(function(item, index) {
					html += '' + '<div class="card" data-shop-id="'
							+ item.shopId + '">' + '<div class="card-header">'
							+ item.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.shopImg + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.shopDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				$('.list-div').append(html);
				// 获取总数
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else{
					$('.infinite-scroll-preloader').show();
				}
				// 还没有达到上限的时候，还可以无限滚动
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}

	// 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// 点击某个店铺就到某个店铺的详情里面去
	$('.shop-list').on('click', '.card', function(e) {
		var shopId = e.currentTarget.dataset.shopId;
		window.location.href = '/jwExp/frontend/shopdetail?shopId=' + shopId;
	});

	// 选择新的店铺类别后，重置页面，清空原先的店铺列表，按新的类别去查询
	$('#shoplist-search-div').on(
			'click',
			'.button',
			function(e) {
				if (parentId) {// 如果传递过来的是一个父类下的子类
					shopCategoryId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						shopCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
				} else {// 如果传递过来的父类为空，则按照父类查询
					parentId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						parentId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
					parentId = '';
				}

			});

	// 店铺名字如果改变，则清空设置名称
	$('#search').on('change', function(e) {
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 获取选择的区域
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
});