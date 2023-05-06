package com.frombusan.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.frombusan.model.AttachedImg;
import com.frombusan.model.review.Review;

@Mapper
public interface ReviewMapper {
	
	
    // 게시글 저장
    void saveReview(Review review);
    // 전체 게시글 갯수
    int getTotal(String searchText);
    // 전체 게시글 검색
    List<Review> findReviews(String searchText, RowBounds rowBounds);
    // 게시글 아이디로 검색
    Review findReview(Long review_id);
    // 게시글 수정
    void updateReview(Review updateReview);
    // 게시글 삭제
    void removeReview(Long review_id);
    
    // 첨부파일 저장
    void saveImg(AttachedImg attachedImg);
    
    List<AttachedImg> findFilesByReviewId(Long review_id);
    
    List<AttachedImg> findFilesForRemove(Long review_id);
    
    // 첨부파일 아이디로 첨부파일 검색
    AttachedImg findFileByAttachedFileId(Long img_id);
    // 첨부파일 삭제
    void removeAttachedFile(Long img_id);

    AttachedImg findImg(Long img_id);
    
    
    //장소 이름으로 리뷰찾기(명소)
   	List<Review> findReviewsByMainTitle(String review_place);
   	
   // Member ID로 작성한 글 찾기
   List<Review> findReviewsByMemberId(String member_id);
   
   
   //모든mainTitle가져오기
   List<String> findAllMainTitle();
   
   
   
   
}
