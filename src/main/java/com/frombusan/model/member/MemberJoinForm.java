package com.frombusan.model.member;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class MemberJoinForm {
    
	@Size(min = 5, max = 10)
    private String member_id;
    
    @Size(min = 5, max = 10)
    private String password;
    
    @NotBlank
    private String name;
    
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    
    private String phone_number;

    public static Member toMember(MemberJoinForm memberJoinForm) {
        Member member = new Member();
        member.setMember_id(memberJoinForm.getMember_id());
        member.setPassword(memberJoinForm.getPassword());
        member.setName(memberJoinForm.getName());
        member.setBirth(memberJoinForm.getBirth());
        member.setPhone_number(memberJoinForm.getPhone_number());
        return member;
    }
}
