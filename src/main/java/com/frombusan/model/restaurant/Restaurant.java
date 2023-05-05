package com.frombusan.model.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Restaurant {
		
	private Long restaurant_id;
	private String main_title;
	private String gugun_nm;
	private Double lat;
	private Double lng;
	private String title;
	private String addr1;
	private String cntct_tel;
	private String usage_day_week_and_time;
	private String rprsntv_menu;
	private String main_img_normal;
	private String main_img_thumb;
	private String itemcntnts;
}
