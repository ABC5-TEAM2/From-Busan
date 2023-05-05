package com.frombusan.repository;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.frombusan.model.restaurant.Restaurant;

@Mapper
public interface RestaurantMapper {
	
	Double findRestaurantLat(Long restaurant_id) ;
	
	Double findRestaurantLng(Long restaurant_id) ;
	
	Restaurant findRestaurant(Long restaurant_id);
	
	List<Restaurant> findAllRestaurant();
}
