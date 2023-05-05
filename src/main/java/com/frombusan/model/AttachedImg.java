package com.frombusan.model;


import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AttachedImg {


	    private Long img_id;
	    private Long review_id;
	    private String original_filename;
	    private String saved_filename;
	    private Long file_size;

	    public AttachedImg(String original_filename, String saved_filename, Long file_size) {
	        this.original_filename = original_filename;
	        this.saved_filename = saved_filename;
	        this.file_size = file_size;
	}
	    
	    

}
