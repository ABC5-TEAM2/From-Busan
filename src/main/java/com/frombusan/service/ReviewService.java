package com.frombusan.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.frombusan.model.AttachedImg;
import com.frombusan.model.review.Review;
import com.frombusan.model.review.ReviewLikes;
import com.frombusan.repository.ReviewMapper;
import com.frombusan.util.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String uploadPath;

    public void saveReview(Review review) {
    	reviewMapper.saveReview(review);
    }

    public List<Review> findReviews(String searchText, int startRecord, int countPerPage) {
        // 전체 검색 결과 중 시작 위치와 갯수
        RowBounds rowBounds = new RowBounds(startRecord, countPerPage);
        return reviewMapper.findReviews(searchText, rowBounds);
    }

    
    public Review findReview(Long review_id) {
        return reviewMapper.findReview(review_id);
    }

    public Review readReview(Long review_id) {
    	Review review = findReview(review_id);
    	review.addHit();
        updateReview(review);
        return review;
    }

    @Transactional
    public void updateReview(Review updateReview) {
        if (updateReview != null) {
            log.info("review_id5: {}", updateReview);
            reviewMapper.updateReview(updateReview);
        }
    }

    @Transactional
    public void removeAttachedFile(Long img_id) {
        AttachedImg attachedFile = reviewMapper.findFileByAttachedFileId(img_id);
        if (attachedFile != null) {
            String fullPath = uploadPath + "/" + attachedFile.getSaved_filename();
            fileService.deleteFile(fullPath);
            log.info("기존 파일 삭제: {}", attachedFile);
            reviewMapper.removeAttachedFile(attachedFile.getImg_id());
        }
    }

    public void removeReview(Long review_id) {
    	
    	List<AttachedImg> files = reviewMapper.findFilesForRemove(review_id);
    	
        if (files!= null) {
        	for (int i = 0; i < files.size(); i++) {
				removeAttachedFile(files.get(i).getImg_id());
				reviewMapper.removeAttachedFile(files.get(i).getImg_id());
			}
        }
        reviewMapper.removeReview(review_id);	
    }
    
    public void removeImg(Long img_id,Long review_id) {
    	
	   AttachedImg img = reviewMapper.findImg(img_id);
        if (img!= null) {
            removeAttachedFile(img.getImg_id());
        }
    }
    
    
    
    public AttachedImg findImg(Long review_id,Long img_id) {
        return reviewMapper.findImg(img_id);
    }
    
    public List<AttachedImg> findFilesByReviewId(Long review_id) {
        return reviewMapper.findFilesByReviewId(review_id);
    }

    public AttachedImg findFileByAttachedFileId(Long img_id) {
        return reviewMapper.findFileByAttachedFileId(img_id);
    }

    public int getTotal(String searchText) {
        return reviewMapper.getTotal(searchText);
    }

    public List<Review> findReviewsByMainTitle(String review_place) {
	String result = review_place.substring(1, review_place.length() - 1);
    return reviewMapper.findReviewsByMainTitle(result);
    }
    
    public List<String> findAllMainTitle() {
        return reviewMapper.findAllMainTitle();
        }
    public List<Review> findReviewsByMemberId(String member_id) {
        return reviewMapper.findReviewsByMemberId(member_id);
    }
    
    
    //좋아요
 	public List<String> findLikesMemberId(Long review_id ){
 		return reviewMapper.findLikesMemberId( review_id);
 	}
 	
 	public List<Map<String,Object>> findLikesById(Long review_id){
 		return reviewMapper.findLikesById(review_id);
 	}
 	
 	public void saveLikes(ReviewLikes reviewLikes) {
 		reviewMapper.saveLikes(reviewLikes);
 	}
 	
 	public void deleteLike(Object like_id) {
 		reviewMapper.deleteLike(like_id);
 	}
    
    
    
}
