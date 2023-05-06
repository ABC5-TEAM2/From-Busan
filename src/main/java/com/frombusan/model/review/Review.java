package com.frombusan.model.review;

import lombok.Data;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

@Data
public class Review {
	@NotBlank
    private Long review_id;
	@NotBlank
    private String member_id;
    private String title;
    private String contents;
    private Long hit;
    private Long review_like;
    private LocalDateTime created_time;
    @NotBlank
    private String review_place;


    public static ReviewUpdateForm toReviewUpdateForm(Review review) {
    	ReviewUpdateForm reviewUpdateForm = new ReviewUpdateForm();
    	
        reviewUpdateForm.setReview_id(review.getReview_id());
        reviewUpdateForm.setTitle(review.getTitle());
        reviewUpdateForm.setContents(review.getContents());
        reviewUpdateForm.setMember_id(review.getMember_id());
        reviewUpdateForm.setHit(review.getHit());
        reviewUpdateForm.setCreated_time(review.getCreated_time());
        reviewUpdateForm.setReview_like(review.getReview_like());
        reviewUpdateForm.setReview_place(review.getReview_place());

        return reviewUpdateForm;
    }

    public void addHit() {
        this.hit++;
    }
}
