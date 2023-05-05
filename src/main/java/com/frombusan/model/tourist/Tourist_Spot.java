package com.frombusan.model.tourist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Tourist_Spot {
	
	private Long tourist_Spot_id;
	private String main_title;
	private String gugun_nm;
	private Double lat;
	private Double lng;
	private String place;
	private String title;
	private String subTitle;
	private String addr1;
	private String cntct_tel;
	private String homepage_url;
	private String trfc_info;
	private String hldy_info;
	private String usage_day_week_and_time;
	private String usage_amount;
	private String middle_size_rm1;
	private String main_img_normal;
	private String main_img_thumb;
	private String itemcntnts;
	private Long place_like;
	private Long restaurant_id;
	private Long wish_list;
	private Long hit;
	private boolean liked;
	private boolean jjim;
	
	
  public void addPlace_like() {
        this.place_like++;
    }
  
  public void removePlace_like() {
      this.place_like--;
  }
  
  public void addWishList() {
      this.wish_list++;
  }

public void removeWishList() {
    this.wish_list--;
}

  public void setLiked(boolean liked) {
	    this.liked = liked;
	  }
  public void setJjim(boolean jjim) {
	    this.jjim = jjim;
	  }
  
  public void addHit() {
	  this.hit++;
  }

}
