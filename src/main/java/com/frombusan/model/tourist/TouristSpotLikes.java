package com.frombusan.model.tourist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TouristSpotLikes {
	
	private Long like_id;
	private Long tourist_Spot_id;
	private String member_id;
	private boolean liked;
	
	public boolean isLiked() {
	    return liked;
	  }
  
	  public void setLiked(boolean liked) {
	    this.liked = liked;
	  }

}
