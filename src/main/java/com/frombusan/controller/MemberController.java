package com.frombusan.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.frombusan.model.course.Course;
import com.frombusan.model.festival.Festival;
import com.frombusan.model.member.LoginForm;
import com.frombusan.model.member.Member;
import com.frombusan.model.member.MemberJoinForm;
import com.frombusan.model.tourist.Tourist_Spot;
import com.frombusan.repository.CourseMapper;
import com.frombusan.repository.FestivalMapper;
import com.frombusan.repository.MemberMapper;
import com.frombusan.repository.ReviewMapper;
import com.frombusan.repository.TouristSpotMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("member")
@Controller
public class MemberController {

    // 데이터베이스 접근을 위한 MemberMapper 필드 선언
    private final MemberMapper memberMapper;
    
	private final FestivalMapper festivalMapper;
	private final ReviewMapper reviewMapper;
	private final TouristSpotMapper touristMapper;
	private final CourseMapper courseMapper;
	
	
    // 회원가입 페이지 이동
    @GetMapping("join")
    public String joinForm(Model model) {
        // joinForm.html 의 필드 세팅을 위해 model 에 빈 MemberJoinForm 객체 생성하여 저장한다
        model.addAttribute("joinForm", new MemberJoinForm());
        // member/joinForm.html 페이지를 리턴한다.
        return "member/joinForm";
    }

    // 회원가입
    @PostMapping("join")
    public String join(@Validated @ModelAttribute("joinForm") MemberJoinForm joinForm,
                       BindingResult result) {
        log.info("joinForm: {}", joinForm);

        // validation 에 에러가 있으면 가입시키지 않고 member/joinForm.html 페이지로 돌아간다.
        if (result.hasErrors()) {
            return "member/joinForm";
        }
        
        // 사용자로부터 입력받은 아이디로 데이터베이스에서 Member 를 검색한다.
        Member member = memberMapper.findMember(joinForm.getMember_id());
        // 사용자 정보가 존재하면
        if (member != null) {
            log.info("이미 가입된 아이디 입니다.");
            // BindingResult 객체에 GlobalError 를 추가한다.
            result.reject("duplicate ID", "이미 가입된 아이디 입니다.");
            // member/joinForm.html 페이지를 리턴한다.
            return "member/joinForm";
        }
        // MemberJoinForm 객체를 Member 타입으로 변환하여 데이터베이스에 저장한다.
        memberMapper.saveMember(MemberJoinForm.toMember(joinForm));
        // 메인 페이지로 리다이렉트한다.
        return "redirect:/";
    }

    // 로그인 페이지 이동
    @GetMapping("login")
    public String loginForm(Model model) {
        // member/loginForm.html 에 필드 셋팅을 위해 빈 LoginForm 객체를 생성하여 model 에 저장한다.
        model.addAttribute("loginForm", new LoginForm());
        // member/loginForm.html 페이지를 리턴한다.
        return "member/loginForm";
    }

    // 로그인 처리
    @PostMapping("login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult result,
                        HttpServletRequest request,
                        @RequestParam(defaultValue = "/") String redirectURL) {
        log.info("redirectURL: {}", redirectURL);
        log.info("loginForm: {}", loginForm);
        // validation 에 실패하면 member/loginForm 페이지로 돌아간다.
        if (result.hasErrors()) {
            return "member/loginForm";
        }
        // 사용자가 입력한 이이디에 해당하는 Member 정보를 데이터베이스에서 가져온다.
        Member member = memberMapper.findMember(loginForm.getMember_id());
        // Member 가 존재하지 않거나 패스워드가 다르면
        if (member == null || !member.getPassword().equals(loginForm.getPassword())) {
            // BindingResult 객체에 GlobalError 를 발생시킨다.
            result.reject("loginError", "아이디가 없거나 패스워드가 다릅니다.");
            // member/loginForm.html 페이지로 돌아간다.
            return "member/loginForm";
        }

        // Request 객체에서 Session 객체를 꺼내온다.
        HttpSession session = request.getSession();
        // Session 에 'loginMember' 라는 이름으로 Member 객체를 저장한다.
        session.setAttribute("loginMember", member);
        // 메인 페이지로 리다이렉트 한다.
        return "redirect:" + redirectURL;
    }

    // 로그아웃
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        // Request 객체에서 Session 정보를 가져온다.
        HttpSession session = request.getSession(false);
        // 세션이 존재하면 세션의 모든 데이터를 리셋한다.
        if (session != null) {
            session.invalidate();
        }
        // 메인 페이지로 리다이렉트 한다.
        return "redirect:/";
    }
    
    
    @GetMapping("myPage")
    public String myPage(Model model,@SessionAttribute(value="loginMember",required = false) Member loginMember) {
    	model.addAttribute("loginMember",loginMember.getMember_id());
        return "member/mypage"  ;
    }
    

    
    @GetMapping("jjimList")
    public String myList(@Validated @ModelAttribute("loginForm") LoginForm loginForm
                        ,@SessionAttribute(value = "loginMember", required = false) Member loginMember
                        ,Model model) {
    	
    	//명소 찜 목록 **서비스 처리 필요
    	List<Map<String, Object>> resultList = touristMapper.findMyListByMemberId(loginMember.getMember_id());
    	List<Tourist_Spot> findMyListSpots = new ArrayList<>();
    	for (int i = 0; i < resultList.size(); i++) {
    		 Map<String, Object> resultMap = resultList.get(i);
		    Object idObj = resultMap.get("TOURIST_SPOT_ID");
		    Long touristSpotid = ((Number) idObj).longValue(); // id가 Number 타입일 수도 있으므로 longValue()로 Long 타입으로 변환
		    Tourist_Spot touristSpot= touristMapper.findTouristSpot(touristSpotid);
		    findMyListSpots.add(touristSpot);
    	}
    	model.addAttribute("findMyListSpots", findMyListSpots);
    	
    	
    	//축제 찜 목록  **서비스 처리 필요
    	List<Map<String, Object>> resultList2 = festivalMapper.findMyListByMemberId(loginMember.getMember_id());
    	List<Festival> findMyListFes = new ArrayList<>();
    	
    	for (int i = 0; i < resultList2.size(); i++) {
    		 Map<String, Object> resultMap2 = resultList2.get(i);
		    Object idObj2 = resultMap2.get("FESTIVAL_ID");
		    Long festival_id = ((Number) idObj2).longValue(); // id가 Number 타입일 수도 있으므로 longValue()로 Long 타입으로 변환
		    Festival festival= festivalMapper.findFestival(festival_id);
		    findMyListFes.add(festival);
    	}
    	model.addAttribute("findMyListFes", findMyListFes);
    	
    	
    	//코스 찜 목록 **서비스 처리 필요
    	List<Map<String, Object>> resultList3 = courseMapper.findMyListByMemberId(loginMember.getMember_id());
    	List<Course> findMyListCos = new ArrayList<>();

    	for (int i = 0; i < resultList3.size(); i++) {
    		 Map<String, Object> resultMap3 = resultList3.get(i);
		    Object idObj3 = resultMap3.get("COURSE_ID");
		    Long courseId = ((Number) idObj3).longValue(); // id가 Number 타입일 수도 있으므로 longValue()로 Long 타입으로 변환
		    Course course= courseMapper.findCourse(courseId);
		    findMyListCos.add(course);
    	}
    	model.addAttribute("findMyListCos", findMyListCos);
    	
    	
        return "member/myJjimList"  ;
    }
    
}
