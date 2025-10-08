package com.twilight.twilight.domain.bulletin.service;

import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostDetailDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.repository.FreeBoardPostQueryRepository;
import com.twilight.twilight.domain.bulletin.repository.FreeBoardPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FreeBoardPostService {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final FreeBoardPostQueryRepository freeBoardPostQueryRepository;
    private final StringRedisTemplate redisTemplate;
    private static int numberOfPostPerPage = 8; //페이지당 글 몇개씩 보여줄건지

    private static final String TOTAL_COUNT_KEY = "freeBoard:totalCount";

    public long getTotalPostCount() {
        String totalCountKey = redisTemplate.opsForValue().get(TOTAL_COUNT_KEY);
        if (totalCountKey != null) return Long.parseLong(totalCountKey);
        long count = freeBoardPostRepository.count();
        redisTemplate.opsForValue().set(TOTAL_COUNT_KEY, String.valueOf(count), Duration.ofMinutes(10)); // TTL 10분
        return count;
    }

    //일단 8개씩 준다
    public List<GetFreeBoardPostListDto> getFreeBoardPosts() {
        return freeBoardPostQueryRepository.findTopNByOrderByCreatedAtDesc(numberOfPostPerPage);
    }

    public GetFreeBoardPostDetailDto getFreeBoardPostDetail(Long postId) {
        return freeBoardPostRepository.findByFreeBoardPostId(postId)
                .map(GetFreeBoardPostDetailDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + postId));
    }





}
