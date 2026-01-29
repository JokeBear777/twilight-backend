package com.twilight.twilight.domain.member.member.repository;

import com.twilight.twilight.domain.member.member.entity.Member;
import com.twilight.twilight.domain.member.member.entity.MemberInterests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInterestRepository extends JpaRepository<MemberInterests, Long> {

    List<MemberInterests> findByMember_memberId(Long memberId);
    void deleteByMember(Member member);
}
