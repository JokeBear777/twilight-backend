package com.twilight.twilight.domain.bulletin.service;

import com.twilight.twilight.domain.bulletin.common.RecommendResult;
import com.twilight.twilight.domain.bulletin.dto.*;
import com.twilight.twilight.domain.bulletin.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostRecommendation;
import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostReply;
import com.twilight.twilight.domain.bulletin.repository.*;
import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.member.type.Role;
import com.twilight.twilight.global.config.BulletinPageProps;
import com.twilight.twilight.global.config.FreeBoardPageProps;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FreeBoardPostService {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final FreeBoardPostQueryRepository freeBoardPostQueryRepository;
    private final StringRedisTemplate redisTemplate;
    private final FreeBoardPageProps pageProps;
    private final FreeBoardPostRecommendationRepository freeBoardPostRecommendationRepository;
    private final FreeBoardPostReplyRepository freeBoardPostReplyRepository;

    private static final String TOTAL_COUNT_KEY = "freeBoard:totalCount";

    public long getTotalPostCount() {
        String totalCountKey = redisTemplate.opsForValue().get(TOTAL_COUNT_KEY);
        if (totalCountKey != null) return Long.parseLong(totalCountKey);
        long count = freeBoardPostRepository.count();
        redisTemplate.opsForValue().set(TOTAL_COUNT_KEY, String.valueOf(count), Duration.ofMinutes(5)); // TTL 5분
        return count;
    }

    public List<GetFreeBoardPostListDto> getFreeBoardPosts(int count) {
        return freeBoardPostQueryRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    public List<GetFreeBoardPostListDto> getFreeBoardPostsByStaticVariable() {
        return freeBoardPostQueryRepository.findTopNByOrderByCreatedAtDesc(pageProps.getPostSize());
    }

    @Transactional
    public GetFreeBoardPostDetailDto getFreeBoardPostDetail(Long postId) {
        return freeBoardPostRepository.findByFreeBoardPostId(postId)
                .map(post -> {
                    post.incrementViews();
                    return GetFreeBoardPostDetailDto.fromEntity(post);
                })
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + postId));
    }

    public List<GetFreeBoardPostReplyDto> getFreeBoardPostParentsReplies(Long postId) {
        List<GetFreeBoardPostReplyDto> dtoList =
                freeBoardPostQueryRepository
                        .findTopNParentRepliesOrderByCreatedAtDesc(postId, pageProps.getReplySize());

        Collections.reverse(dtoList); // 역순으로 뒤집기 (내림 → 오름)

        log.info("list size = {}", (dtoList != null ? dtoList.size() : null));
        return dtoList;
    }

    public Map<Long, List<GetFreeBoardPostReplyDto>> getChildrenReplies(List<GetFreeBoardPostReplyDto> parentsDtoList) {

        if (parentsDtoList == null || parentsDtoList.isEmpty()) {
            return Map.of();
        }

        List<Long> parentIds = parentsDtoList.stream()
                .map(GetFreeBoardPostReplyDto::getFreeBoardPostReplyId)
                .toList();

        List<GetFreeBoardPostReplyDto> children =
                freeBoardPostQueryRepository.findChildrenByParentIds(parentIds);

        return children.stream()
                        .collect(Collectors.groupingBy(GetFreeBoardPostReplyDto::getParentReplyId));

    }

    public void setPreviewChildren(
            List<GetFreeBoardPostReplyDto> parentDtoList,
            Map<Long, List<GetFreeBoardPostReplyDto>> childrenMap) {

        int preViewSize = pageProps.getChildrenReplyFreeViewSize();

        for (GetFreeBoardPostReplyDto parentDto : parentDtoList) {
            List<GetFreeBoardPostReplyDto> allChildren =
                    childrenMap.getOrDefault(parentDto.getFreeBoardPostReplyId(), List.of());

            List<GetFreeBoardPostReplyDto> preview =
                    allChildren.stream().limit(preViewSize).toList();

            parentDto.setChildrenList(preview);
            parentDto.setHasMoreChildren(allChildren.size() > preViewSize);
        }
    }

    //getFreeBoardPostParentsReplies 사용하는 컨트롤러들 이 함수 완성되면 이거쓰도록 수정
    public List<GetFreeBoardPostReplyDto> getParentsWithChildrenPreview(Long postId) {
        List<GetFreeBoardPostReplyDto> parentDtoList = getFreeBoardPostParentsReplies(postId);

        if (parentDtoList == null || parentDtoList.isEmpty()) {
            return parentDtoList;
        }

        Map<Long, List<GetFreeBoardPostReplyDto>> childrenMap = getChildrenReplies(parentDtoList);
        setPreviewChildren(parentDtoList, childrenMap);

        return parentDtoList;
    }

    public List<GetFreeBoardPostReplyDto> getFreeBoardPostAllReplies(Long postId) {
        return freeBoardPostReplyRepository.findByFreeBoardPost_FreeBoardPostId(postId).stream()
                .map(reply -> {
                    return GetFreeBoardPostReplyDto.fromEntity(reply);
                })
                .toList();
    }

    @Transactional
    public void savePost(Member member, FreeBoardPostForm form ) {
        freeBoardPostRepository.save(
                FreeBoardPost.builder()
                        .member(member)
                        .title(form.getTitle())
                        .content(form.getContent())
                        .build()
        );
    }

    @Transactional
    public void editPost(Member member, Long postId, FreeBoardPostEditForm form) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
    }

    public GetFreeBoardPostEditDto getEditablePost(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }
        return GetFreeBoardPostEditDto.fromEntity(post);
    }

    @Transactional
    public void deletePost(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getMember().getMemberId().equals(member.getMemberId())
            || member.getRole().equals(Role.ROLE_ADMIN.toString())
        ) {
            throw new AccessDeniedException("본인 또는 관리자만 삭제 할 수 있습니다.");
        }

        freeBoardPostReplyRepository.deleteByFreeBoardPost_FreeBoardPostId(postId);

        freeBoardPostRepository.delete(post);
    }

    @Transactional
    public RecommendResult increasePostRecommendation(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + postId));

        if (post.getMember().getMemberId().equals(member.getMemberId())) {
            return RecommendResult.SELF_RECOMMEND;
        }

        if (freeBoardPostRecommendationRepository.existsByMemberAndPost(member,post)) {
            return RecommendResult.ALREADY_RECOMMENDED;
        }

        
        post.increaseNumberOfRecommendations();
        freeBoardPostRepository.save(post);

        freeBoardPostRecommendationRepository.save(
                FreeBoardPostRecommendation.builder()
                        .post(post)
                        .member(member)
                        .build()
        );

        return RecommendResult.OK;
    }

    @Transactional
    public void postFreeBoardReply(Long postId, Member member, FreeBoardPostReplyForm form) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + postId));
        post.increaseNumberOfComments();

        freeBoardPostReplyRepository.save(
                FreeBoardPostReply.builder()
                        .member(member)
                        .freeBoardPost(post)
                        .content(form.getContent())
                        .build()
        );
    }

    @Transactional
    public void deleteReply(Member member, Long replyId, Long postId) {

        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다. id=" + postId));

        FreeBoardPostReply reply = freeBoardPostReplyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다. id=" + replyId));

        if (!reply.getMember().getMemberId().equals(member.getMemberId())) {
            throw new AccessDeniedException("본인 또는 관리자만 삭제 할 수 있습니다.");
        }

        post.decreaseNumberOfComments();

        freeBoardPostReplyRepository.delete(reply);
    }



}
