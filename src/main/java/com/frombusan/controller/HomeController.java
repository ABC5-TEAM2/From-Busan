package com.frombusan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.frombusan.model.festival.Festival;
import com.frombusan.model.restaurant.Restaurant;
import com.frombusan.model.tourist.Tourist_Spot;
import com.frombusan.repository.FestivalMapper;
import com.frombusan.repository.RestaurantMapper;
import com.frombusan.repository.TouristSpotMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private RestaurantMapper spotMapper;
	
	@Autowired
	private TouristSpotMapper tourMapper;
	
	@Autowired
	private FestivalMapper festivalMapper;
	
	
	@GetMapping("/")
	public String home(Model model) {
		List<Tourist_Spot> findAllTourist = tourMapper.findAllTourist();
		List<Festival> findAllFestival = festivalMapper.findAllFestival();
		model.addAttribute("tourist",findAllTourist);
		model.addAttribute("festival",findAllFestival);
		return "main/index";
	}

	/*
	 * @PostMapping("restaurant/{restaurant_id}") public String findRestaurant(Long
	 * restaurant_id, Model model) { List<Restaurant> findAllRestaurant =
	 * spotMapper.findAllRestaurant();
	 * 
	 * model.addAttribute(findAllRestaurant);
	 * 
	 * return "main/index"; }
	 */

}
