package com.twilight.twilight.domain.member.member.repository;

import com.twilight.twilight.domain.member.member.entity.Member;
import com.twilight.twilight.domain.member.member.entity.MemberPersonality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberPersonalityRepository extends JpaRepository<MemberPersonality, Long> {

    List<MemberPersonality> findByMember_memberId(Long memberId);
    void deleteByMember(Member member);
}
