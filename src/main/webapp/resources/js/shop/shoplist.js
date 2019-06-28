$(function () {

	function getlist(e) {
		$.ajax({
			url : "/jwExp/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					//渲染shoplist
					handleList(data.shopList);
					//渲染用户名
					handleUser(data.user);
				}
			}
		});
	}

	function handleUser(data) {
		$('#user-name').text(data.name);
	}

	function handleList(data) {
		var html = '';
		//遍历list信息
		data.map(function (item, index) {
			html += '<div class="row row-shop"><div class="col-40">'+ item.shopName +'</div><div class="col-40">'+ shopStatus(item.enableStatus) +'</div><div class="col-20">'+ goShop(item.enableStatus, item.shopId) +'</div></div>';

		});
		$('.shop-wrap').html(html);
	}

	function goShop(status, id) {
		if (status != 0 && status != -1) {
			return '<a href="/jwExp/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
		} else {
			return '';
		}
	}

	//获取店铺的状态
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else {
			return '审核通过';
		}
	}

	getlist();
});
