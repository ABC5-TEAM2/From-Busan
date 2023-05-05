package com.frombusan.repository;

import org.apache.ibatis.annotations.Mapper;

import com.frombusan.model.member.Member;


@Mapper
public interface MemberMapper {
    void saveMember(Member member);
    Member findMember(String member_id);
}
