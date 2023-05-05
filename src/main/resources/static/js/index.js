$(document).ready(function() {
	//**** 마우스오버  ****//
	//서브메뉴 숨기고 시작
	$(".dropdown-menu").hide();
	$(".main-nav>ul").mouseover(function() {
		$(".dropdown-menu").stop().slideDown(1);
	}).mouseout(function() {
		$(".dropdown-menu").stop().slideUp(1);
	});
	
});
