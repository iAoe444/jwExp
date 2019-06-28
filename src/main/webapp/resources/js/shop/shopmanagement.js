$(function(){
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/jwExp/shopadmin/getshopmanagementinfo?shopId='+shopId;
	//这里主要起到一个拦截器的作用，如果是误闯进去的，那么就重定向
	$.getJSON(shopInfoUrl,function(data){
		if(data.redirect){
			window.location.href=data.url;
		}else{
			if(data.shopId!=undefined&&data.shopId!=null){
				shopId = data.shopId;
			}
			$('#shopinfo')
				.attr('href','/jwExp/shopadmin/shopoperation?shopId='+shopId);
		}
	})
});