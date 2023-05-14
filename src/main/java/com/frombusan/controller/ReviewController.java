package com.frombusan.controller;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.frombusan.model.AttachedImg;
import com.frombusan.model.festival.Festival;
import com.frombusan.model.festival.FestivalLikes;
import com.frombusan.model.member.Member;
import com.frombusan.model.review.Review;
import com.frombusan.model.review.ReviewLikes;
import com.frombusan.model.review.ReviewUpdateForm;
import com.frombusan.model.review.ReviewWriteForm;
import com.frombusan.repository.FestivalMapper;
import com.frombusan.repository.MemberMapper;
import com.frombusan.repository.ReviewMapper;
import com.frombusan.repository.TouristSpotMapper;
import com.frombusan.service.ReviewService;
import com.frombusan.util.PageNavigator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("review")
@Controller
public class ReviewController {
	//의존성주입
    private final ReviewService reviewService;
    
    // 게시판 관련 상수 값
    final int countPerPage = 10;    // 페이지 당 글 수
    final int pagePerGroup = 5;     // 페이지 이동 그룹 당 표시할 페이지 수
    
    @Value("${file.upload.path}")
    private String uploadPath;

    // 후기 글쓰기 페이지 이동
    @GetMapping("write")
    public String writeForm(Model model,RedirectAttributes redirectAttributes) {
        // writeForm.html의 필드 표시를 위해 빈 BoardWriteForm 객체를 생성하여 model 에 저장한다.
        model.addAttribute("writeForm", new ReviewWriteForm());
        List<String> findAllName = reviewService.findAllMainTitle();
        model.addAttribute("findAllName", findAllName);
        
        // board/writeForm.html 을 찾아 리턴한다.
        return "review/write";
    }
  

    // 게시글 쓰기
    @PostMapping("write")
    public String write(@SessionAttribute(value = "loginMember", required = false) Member loginMember,
                        @Validated @ModelAttribute("writeForm") ReviewWriteForm reviewWriteForm,
                        BindingResult result,Model model,RedirectAttributes redirectAttributes) {
        // 로그인 상태가 아니면 로그인 페이지로 보낸다.
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        // findAllName 리스트에 review_place와 일치하는 값이 있는지 확인한다.
        List<String> findAllName = reviewService.findAllMainTitle();
        
        model.addAttribute("findAllName", findAllName);
        
        if (result.hasErrors()) {
            model.addAttribute("findAllName", findAllName);
            return "review/write";}

        if (!findAllName.contains(reviewWriteForm.getReview_place())) {
            model.addAttribute("placeError", "일치하는 장소가 없습니다.");
            model.addAttribute("findAllName", findAllName);
            return "review/write";}

        // title과 review_place에 대한 공백 검사
        if (reviewWriteForm.getTitle()==null || reviewWriteForm.getReview_place()==null) {
            // validation 에러 메시지를 BindingResult에 추가합니다.
            result.rejectValue("title", "NotEmpty");
            result.rejectValue("review_place", "NotEmpty");
            // board/write.html 페이지를 다시 보여줍니다.
            redirectAttributes.addFlashAttribute("findAllName", findAllName);
            return "review/write"; }
      
        // 파라미터로 받은 BoardWriteForm 객체를 Board 타입으로 변환한다.
        Review review = ReviewWriteForm.toReview(reviewWriteForm);
        // board 객체에 로그인한 사용자의 아이디를 추가한다.
        review.setMember_id(loginMember.getMember_id());
        // board 객체를 저장한다.
        
        // board 객체를 저장한다.
        reviewService.saveReview(review);
   
        // board/list 로 리다이렉트한다.
        return "redirect:/review/list";
    }

    // 게시글 전체 보기
    @GetMapping("list")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "searchText", defaultValue = "") String searchText,
                       Model model, @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        log.info("searchText: {}", searchText);
        int total = reviewService.getTotal(searchText);

        PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);

        // 데이터베이스에 저장된 모든 Board 객체를 리스트 형태로 받는다.
        List<Review> reviews = reviewService.findReviews(searchText, navi.getStartRecord(), navi.getCountPerPage());

        // Board 리스트를 model 에 저장한다.
        model.addAttribute("reviews", reviews);
        // PageNavigation 객체를 model 에 저장한다.
        model.addAttribute("navi", navi);
        model.addAttribute("searchText", searchText);
        


        // board/list.html 를 찾아서 리턴한다.
        return "review/list";
    }

    // 게시글 읽기
    @GetMapping("read")
    public String read(@RequestParam Long review_id,
                       Model model) {

        // board_id 에 해당하는 게시글을 데이터베이스에서 찾는다.
        Review review = reviewService.readReview(review_id);
        // board_id에 해당하는 게시글이 없으면 리스트로 리다이렉트 시킨다.
        if (review == null) {
            log.info("게시글 없음");
            return "redirect:/review/list";
        }

        // 모델에 Board 객체를 저장한다.
        model.addAttribute("review", review);

        // 첨부파일을 찾는다.
//        List<AttachedImg> files = reviewService.findFilesByReviewId(review_id);
//        model.addAttribute("files", files);
        
        List<String> findReviewLikes = reviewService.findLikesMemberId(review_id);
		log.info("findReviewLikes:{}",findReviewLikes);
		model.addAttribute("findReviewLikes", findReviewLikes);

        // board/read.html 를 찾아서 리턴한다.
        return "review/read";
    }

    // 게시글 수정 페이지 이동
    @GetMapping("update")
    public String updateForm(@SessionAttribute(value = "loginMember", required = false) Member loginMember,
                             @RequestParam Long review_id,
                             Model model) {
        log.info("review_id: {}", review_id);

        // board_id에 해당하는 게시글이 없거나 게시글의 작성자가 로그인한 사용자의 아이디와 다르면 수정하지 않고 리스트로 리다이렉트 시킨다.
        Review review = reviewService.findReview(review_id);
        if (review_id == null || !review.getMember_id().equals(loginMember.getMember_id())) {
            log.info("수정 권한 없음");
            return "redirect:/review/list";
        }
        // model 에 board 객체를 저장한다.
        model.addAttribute("review", Review.toReviewUpdateForm(review));

        // 첨부파일을 찾는다.
        List<AttachedImg> files = reviewService.findFilesByReviewId(review_id);
        model.addAttribute("files", files);

        // board/update.html 를 찾아서 리턴한다.
        return "review/update";
    }

    // 게시글 수정
    @PostMapping("update")
    public String update(@SessionAttribute(value = "loginMember", required = false) Member loginMember,
                         @RequestParam Long review_id,
                         @Validated @ModelAttribute("review") ReviewUpdateForm updateReview,
                         BindingResult result) {
                         
        // validation 에 에러가 있으면 board/update.html 페이지로 돌아간다.
        if (result.hasErrors()) {
            return "review/update";
        }

        // board_id 에 해당하는 Board 정보를 데이터베이스에서 가져온다.
        Review review = reviewService.findReview(review_id);
        // Board 객체가 없거나 작성자가 로그인한 사용자의 아이디와 다르면 수정하지 않고 리스트로 리다이렉트 시킨다.
        if (review == null || !review.getMember_id().equals(loginMember.getMember_id())) {
            log.info("수정 권한 없음");
            return "redirect:/review/list";
        }
        // 제목을 수정한다.
        review.setTitle(updateReview.getTitle());
        // 내용을 수정한다.
        review.setContents(updateReview.getContents());
        
        // 수정한 Board 를 데이터베이스에 update 한다.
        reviewService.updateReview(review);
        // 수정이 완료되면 리스트로 리다이렉트 시킨다.
        return "redirect:/review/list";
    }

    // 게시글 삭제
    @GetMapping("delete")
    public String remove(@SessionAttribute(value = "loginMember", required = false) Member loginMember,
                         @RequestParam Long review_id) {
        // board_id 에 해당하는 게시글을 가져온다.
        Review review = reviewService.findReview(review_id);
        // 게시글이 존재하지 않거나 작성자와 로그인 사용자의 아이디가 다르면 리스트로 리다이렉트 한다.
        if (review == null || !review.getMember_id().equals(loginMember.getMember_id())) {
            log.info("삭제 권한 없음");
            return "redirect:/review/list";
        }
        // 게시글을 삭제한다.
    	log.info("review_id:{}",review_id);

        reviewService.removeReview(review_id);
        // board/list 로 리다이렉트 한다.
        return "redirect:/review/list";
    }
    
    @DeleteMapping("/deleteFile/{review_id}")
    public ResponseEntity<String> deleteFile(@PathVariable("review_id") Long review_id
    		,@SessionAttribute(value = "loginMember", required = false) Member loginMember						
    		, @RequestParam("img_id") Long img_id) {
    		
    		log.info("img_id:{}",img_id);
	        // board_id 에 해당하는 게시글을 가져온다.
	        Review review = reviewService.findReview(review_id);
	        // 게시글이 존재하지 않거나 작성자와 로그인 사용자의 아이디가 다르면 리스트로 리다이렉트 한다.
	        if (review == null || !review.getMember_id().equals(loginMember.getMember_id())) {
	            log.info("삭제 권한 없음");
	            return ResponseEntity.ok("삭제 권한 없음") ;
	        }
	        // 게시글을 삭제한다.

        reviewService.removeImg(img_id,review_id);
        log.info("review:{}",review);
        // board/list 로 리다이렉트 한다.
        return ResponseEntity.ok("삭제 성공") ;
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws MalformedURLException {
        AttachedImg attachedFile = reviewService.findFileByAttachedFileId(id);
        String fullPath = uploadPath + "/" + attachedFile.getSaved_filename();
        UrlResource resource = new UrlResource("file:" + fullPath);
        String encodingFileName = UriUtils.encode(attachedFile.getOriginal_filename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodingFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
    
    
 // 한 장소 게시글 전체 보기
    @GetMapping("reviewList")
    public String reviewList(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "searchText", defaultValue = "") String searchText,
                       Model model,@RequestParam("main_title") String review_place) {
        log.info("review_place: {}", review_place);
        //int total = reviewService.getTotal(searchText);
        //PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);
        // 데이터베이스에 저장된 모든 Board 객체를 리스트 형태로 받는다.
        //List<Review> reviews = reviewService.findReviews(searchText, navi.getStartRecord(), navi.getCountPerPage());
        
        List<Review> findReviewsByMainTitle = reviewService.findReviewsByMainTitle(review_place);
        log.info("findReviewsByMainTitle: {}", findReviewsByMainTitle);
        
        // Board 리스트를 model 에 저장한다.
        model.addAttribute("findReviewsByMainTitle", findReviewsByMainTitle);
        // PageNavigation 객체를 model 에 저장한다.
        //model.addAttribute("navi", navi);
        //model.addAttribute("searchText", searchText);

        // board/list.html 를 찾아서 리턴한다.
        return "review/reviewList";
    }
    
    
    // 내가 쓴 리뷰 리스트
    @GetMapping("myReviewList")
    public String myReviewList(
                        @SessionAttribute(value = "loginMember", required = false) Member loginMember
                        ,Model model) {
    	
    	List<Review> reviews = reviewService.findReviewsByMemberId(loginMember.getMember_id());
    	if(loginMember!=null) {
    		model.addAttribute("reviews", reviews);
    	}

    	return "member/myReviewList";
    }
    
    
  //좋아요 기능
 	@PostMapping("/like")
	public ResponseEntity<Review> likeReview(@RequestParam("review_id") Long review_id
											,@SessionAttribute(value = "loginMember", required = false) Member loginMember
											) {

 		List<String> findReviewLikes = reviewService.findLikesMemberId(review_id);
		List<Map<String, Object>> findLikesById = reviewService.findLikesById(review_id);
		
		Review review= reviewService.findReview(review_id);
		ReviewLikes reviewLikes = new ReviewLikes();
		String member_id = loginMember.getMember_id();

		Object like_id = null;
		for (int i = 0; i < findLikesById.size(); i++) {
		    Map<String, Object> map = findLikesById.get(i);
		    if (member_id.equals((String)map.get("MEMBER_ID"))) {
		        like_id =map.get("LIKE_ID");
		        break;
		    }
		}

		if (review != null) {
			if(!findReviewLikes.contains(member_id)) {
				review.addReview_like();
				reviewLikes.setMember_id(member_id);
				reviewLikes.setReview_id(review_id);
				reviewService.saveLikes(reviewLikes);
				review.setLiked(true);
			}
			else {
				review.removeReview_like();
				reviewService.deleteLike(like_id);
				review.setLiked(false);
		    }
			log.info("festival:{}",review);
			reviewService.updateReview(review);
	    return ResponseEntity.ok(review);
	  } else {
	    // 관광지 정보가 없는 경우, 오류 응답을 반환합니다.
	    return ResponseEntity.badRequest().build();
	  }
	}
    
}
