package com.frombusan.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CourseLikes {
	
	private Long like_id;
	private Long course_id;
	private String member_id;
	private boolean liked;
	
	public boolean isLiked() {
	    return liked;
	  }
  
	  public void setLiked(boolean liked) {
	    this.liked = liked;
	  }

}
