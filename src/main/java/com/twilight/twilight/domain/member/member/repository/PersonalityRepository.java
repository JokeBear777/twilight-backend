package com.twilight.twilight.domain.member.member.repository;

import com.twilight.twilight.domain.member.member.entity.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {
    List<Personality> findByNameIn(List<String> names);
}
