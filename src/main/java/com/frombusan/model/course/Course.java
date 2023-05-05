package com.frombusan.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Course {
	
	private Long course_id;
	private Long course_set;
	private String course_title;
	private String course_sequense;
	private String course_content1;
	private String course_content2;
	private String course_content3;
	private String course_content4;
	private String course_content5;
	private String main_img;
	private Long place_like;
	private Long hit;

	
	//private Long wish_list; 코스도 혹시 찜한 총갯수 보여줄려면쓰고;;
	private boolean liked;
	private boolean jjim;

	
	
	  public void addPlace_like() {
	        this.place_like++;
	    }
	  
	  public void removePlace_like() {
	      this.place_like--;
	  }
	  
//	  public void addWishList() {
//	      this.wish_list++;
//	  }
//
//	public void removeWishList() {
//	    this.wish_list--;
//	}

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
