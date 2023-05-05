package com.frombusan.model.member;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Member {
    private String member_id;
    private String password;
    private String name;
    private LocalDate birth;
    private String phone_number;
}
