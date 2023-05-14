package com.frombusan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.frombusan.model.course.Course;
import com.frombusan.model.course.CourseLikes;
import com.frombusan.model.course.CourseMyList;
import com.frombusan.model.course.CoursePic;
import com.frombusan.model.member.Member;
import com.frombusan.repository.CourseMapper;
import com.frombusan.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("course")
@Controller
public class CourseController {
	
	@Autowired
	private CourseMapper courseMapper;
	
	 // 게시판 관련 상수 값
    final int countPerPage = 10;    // 페이지 당 글 수
    final int pagePerGroup = 5;     // 페이지 이동 그룹 당 표시할 페이지 수
    
    @PostMapping("/{course_id}")
    public ResponseEntity<List<Course>> findCourse(Long course_id, Model model) {
		List<Course> findAllCourse = courseMapper.findAllCourse();
    	return ResponseEntity.ok(findAllCourse);
    }
    
    @GetMapping("list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page,
			 Model model) {
    	
    	log.info("안녕"); 	
		List<Course> findAllCourse = courseMapper.findAllCourse();
		log.info("추천리스트 :{}",findAllCourse);
		model.addAttribute("findAllCourse", findAllCourse);
		
		return "course/CourseList";
	}
    
 // 게시글 읽기
 	@GetMapping("/CourseInfo")
 	public String read(@RequestParam Long course_id, @SessionAttribute(value = "loginMember", required = false) Member loginMember,
 			 Model model) {
 		log.info("course_id:{}",course_id);
 		// board_id 에 해당하는 게시글을 데이터베이스에서 찾는다.
 		Course course= courseMapper.findCourse(course_id);
 		
 		// board_id에 해당하는 게시글이 없으면 리스트로 리다이렉트 시킨다.
 		if (course == null) {
 			log.info("축제 없음");
 			return "redirect:/course/list";
 		}
 		
 		course.addHit();
		courseMapper.updateCourse(course);
		
		//한 코스의 사진들 가져오기
		List<CoursePic> coursePics = courseMapper.findCoursePic(course.getCourse_set());
		model.addAttribute("coursePics", coursePics);
		
 		
 		// 모델에 restaurant 객체를 저장한다.
 		model.addAttribute("course", course);
 		
 		List<String> findCourseLikes = courseMapper.findLikesMemberId(course_id);
		model.addAttribute("findCourseLikes", findCourseLikes);
		
		List<String> findCourseMyList = courseMapper.findMyListMemberId(course_id);
		model.addAttribute("findCourseMyList", findCourseMyList);
		model.addAttribute("member_id", loginMember.getMember_id());
		

 		// board/read.html 를 찾아서 리턴한다.
 		return "course/CourseInfo";
 	}

 	//좋아요 기능
 	@PostMapping("/like")
	public ResponseEntity<Course> likeTouristSpot(@RequestParam("courseId") Long course_id
														,@SessionAttribute(value = "loginMember", required = false) Member loginMember
														) {

 		List<String> findCourseLikes = courseMapper.findLikesMemberId(course_id);
		List<Map<String, Object>> findLikesById = courseMapper.findLikesById(course_id);
		
		Course Course= courseMapper.findCourse(course_id);
		CourseLikes CourseLikes = new CourseLikes();
		String member_id = loginMember.getMember_id();
		
		Object like_id = null;
		for (int i = 0; i < findLikesById.size(); i++) {
		    Map<String, Object> map = findLikesById.get(i);
		    if (member_id.equals((String)map.get("MEMBER_ID"))) {
		        like_id =map.get("LIKE_ID");
		        break;
		    }
		}
		if (Course != null) {
			if(!findCourseLikes.contains(member_id)) {
				Course.addPlace_like();
				CourseLikes.setMember_id(member_id);
				CourseLikes.setCourse_id(course_id);
				courseMapper.saveLikes(CourseLikes);
				Course.setLiked(true);
			}
			else {
				Course.removePlace_like();
				courseMapper.deleteLike(like_id);
				Course.setLiked(false);
		    }
			courseMapper.updateCourse(Course);
	    return ResponseEntity.ok(Course);
	  } else {
	    // 관광지 정보가 없는 경우, 오류 응답을 반환합니다.
	    return ResponseEntity.badRequest().build();
	  }
	}
    
 	@PostMapping("/myList")
	public ResponseEntity<Course> myTouristSpot(@RequestParam("course_id") Long course_id
														,@SessionAttribute(value = "loginMember", required = false) Member loginMember
														) {

		List<String> findCourseMyList = courseMapper.findMyListMemberId(course_id);
		List<Map<String, Object>> findMyListById = courseMapper.findMyListById(course_id);
		Course Course= courseMapper.findCourse(course_id);
		
		
		CourseMyList CourseMyList = new CourseMyList();
		String member_id = loginMember.getMember_id();
		
		
		Object wishboard_id = null;
		for (int i = 0; i < findMyListById.size(); i++) {
		    Map<String, Object> map = findMyListById.get(i);
		    if (member_id.equals((String)map.get("MEMBER_ID"))) {
		    	wishboard_id =map.get("WISHBOARD_ID");
		        break;
		    }
		}
		if (Course != null) {
			if(!findCourseMyList.contains(member_id)) {
				//Course.addWishList();
				CourseMyList.setMember_id(member_id);
				CourseMyList.setCourse_id(course_id);
				courseMapper.saveMyList(CourseMyList);
				Course.setJjim(true);
			}
			else {
				//Course.removeWishList();
				courseMapper.deleteMyList(wishboard_id);
				Course.setJjim(false);
		    }
		    
		    
	    return ResponseEntity.ok(Course);
	  } else {
	    // 관광지 정보가 없는 경우, 오류 응답을 반환합니다.
	    return ResponseEntity.badRequest().build();
	  }
	}
    
}
