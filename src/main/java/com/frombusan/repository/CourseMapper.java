package com.frombusan.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.frombusan.model.course.Course;
import com.frombusan.model.course.CourseLikes;
import com.frombusan.model.course.CourseMyList;
import com.frombusan.model.course.CoursePic;




@Mapper
public interface CourseMapper {
	

		
	Course findCourse(Long course_id);

	List<Course> findAllCourse();
	
	//review_place를 위해서
	List<Object> findAllCourseName();

//좋아요 기능	
	void updateCourse(Course updateCourse);
	
	List<String> findLikesMemberId(Long course_id);
	
	List<Map<String,Object>> findLikesById(Long course_id);
	
	void saveLikes(CourseLikes CourseLikes);
	
	void deleteLike(Object like_id);
//	
	//찜하기
	
	void saveMyList(CourseMyList CourseMyList);
	
	List<Map<String,Object>> findMyListById(Long course_id);
	
	List<String> findMyListMemberId(Long course_id);
	
	//테이블 다른걸로!
	void deleteMyList(Object wishboard_id);
	
//
	
	//
	List<Map<String, Object>> findMyListByMemberId(String member_id);
	
	//조회수
	void addHit(Course Course);
	
	
	//코스 사진 가져오기!	
	List<CoursePic> findCoursePic(Long course_set);
	
	

}

