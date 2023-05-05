package com.frombusan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.frombusan.model.member.Member;
import com.frombusan.model.restaurant.Restaurant;
import com.frombusan.repository.RestaurantMapper;
import com.frombusan.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("restaurant")
@Controller
@RequiredArgsConstructor
public class RestaurantController {

	@Autowired
	private RestaurantMapper restaurantMapper;
	private ReviewService reviewService;
	
	 // 게시판 관련 상수 값
    final int countPerPage = 10;    // 페이지 당 글 수
    final int pagePerGroup = 5;     // 페이지 이동 그룹 당 표시할 페이지 수
	
	@PostMapping("/")
	public ResponseEntity<List<Restaurant>> findRestaurant(Long restaurant_id, Model model) {
		log.info("실행");
		List<Restaurant> findAllRestaurant = restaurantMapper.findAllRestaurant();
		return ResponseEntity.ok(findAllRestaurant);
	}

	@GetMapping("list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "searchText", defaultValue = "") String searchText, Model model) {
		
		log.info("searchText: {}", searchText);
		
		//int total = reviewService.getTotal(searchText);
		//PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);

		// 데이터베이스에 저장된 모든 Board 객체를 리스트 형태로 받는다.
		List<Restaurant> findAllRestaurant = restaurantMapper.findAllRestaurant();

		// Board 리스트를 model 에 저장한다.
		model.addAttribute("findAllRestaurant", findAllRestaurant);
		// PageNavigation 객체를 model 에 저장한다.
		//model.addAttribute("navi", navi);
		model.addAttribute("searchText", searchText);

		return "restaurant/RestaurantList";
	}
	
	// 게시글 읽기
	@GetMapping("/RestaurantInfo")
	public String read(@RequestParam("restaurant_id") Long restaurant_id,@SessionAttribute(value = "loginMember", required = false) Member loginMember,
			 Model model) {

		// board_id 에 해당하는 게시글을 데이터베이스에서 찾는다.
		
		Restaurant restaurant= restaurantMapper.findRestaurant(restaurant_id);
		// board_id에 해당하는 게시글이 없으면 리스트로 리다이렉트 시킨다.
		if (restaurant == null) {
			log.info("레스토랑 없음");
			return "redirect:/restaurant/list";
		}
		// 모델에 restaurant 객체를 저장한다.
		model.addAttribute("restaurant", restaurant);

		// board/read.html 를 찾아서 리턴한다.
		return "restaurant/RestaurantInfo";
	}

   
    
}
