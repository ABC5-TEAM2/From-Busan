package com.frombusan.model.review;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ReviewWriteForm {
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    private MultipartFile attachedFile;

    public static Review toReview(ReviewWriteForm reviewWriteForm) {
    	Review review = new Review();
    	review.setTitle(reviewWriteForm.getTitle());
    	review.setContents(reviewWriteForm.getContents());
        return review;
    }
}
