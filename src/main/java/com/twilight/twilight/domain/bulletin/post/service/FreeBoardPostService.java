package com.twilight.twilight.domain.bulletin.post.service;

import com.twilight.twilight.domain.bulletin.post.common.RecommendResult;
import com.twilight.twilight.domain.bulletin.post.dto.*;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPostRecommendation;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPostReply;
import com.twilight.twilight.domain.bulletin.post.repository.*;
import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.member.type.Role;
import com.twilight.twilight.global.config.FreeBoardPageProps;
import com.twilight.twilight.global.outbox.repository.IndexingOutboxRepository;
import com.twilight.twilight.global.outbox.service.FreeBoardSearchSyncPublisher;
import com.twilight.twilight.global.policy.ThresholdGenerator;
import com.twilight.twilight.global.search.NgramGenerator;
import com.twilight.twilight.global.util.ContentNormalizer;
import com.twilight.twilight.global.util.HtmlSanitizer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
    private final IndexingOutboxRepository indexingOutboxRepository;
    private final FreeBoardSearchSyncPublisher freeBoardSearchSyncPublisher;
    private final ThresholdGenerator thresholdGenerator;
    private final NgramGenerator ngramGenerator;
    private final FreeBoardSearchRepository freeBoardSearchRepository;


    private static final String TOTAL_COUNT_KEY = "freeBoard:totalCount";

    public long getTotalPostCount() {
        String totalCountKey = redisTemplate.opsForValue().get(TOTAL_COUNT_KEY);
        if (totalCountKey != null) return Long.parseLong(totalCountKey);
        long count = freeBoardPostRepository.countByDeletedAtIsNull();
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

    public List<GetFreeBoardPostReplyDto> getFreeBoardPostParentsReplies(Long postId, Long page) {
        List<GetFreeBoardPostReplyDto> dtoList =
                freeBoardPostQueryRepository
                        .findParentRepliesOrderByCreatedAtAsc(postId, page, pageProps.getPostSize());
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

        int preViewSize = pageProps.getReplyFreeViewSize();

        for (GetFreeBoardPostReplyDto parentDto : parentDtoList) {
            List<GetFreeBoardPostReplyDto> allChildren =
                    childrenMap.getOrDefault(parentDto.getFreeBoardPostReplyId(), List.of());

            List<GetFreeBoardPostReplyDto> preview =
                    allChildren.stream().limit(preViewSize).toList();

            parentDto.setChildrenList(preview);
            parentDto.setHasMoreChildren(allChildren.size() > preViewSize);
        }
    }

    public List<GetFreeBoardPostReplyDto> getParentsWithChildrenPreview(Long postId, Long page) {
        List<GetFreeBoardPostReplyDto> parentDtoList = getFreeBoardPostParentsReplies(postId, page);

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
        log.info("테스트 : = {}",form.getContent());
        FreeBoardPost post = FreeBoardPost.builder()
                .member(member)
                .title(form.getTitle())
                .content(
                        contentPreprocessing(form.getContent())
                )
                .build();

        freeBoardPostRepository.save(post);

        freeBoardSearchSyncPublisher.publishPostUpsert(post.getFreeBoardPostId());
    }

    @Transactional
    public void editPost(Member member, Long postId, FreeBoardPostEditForm form) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시물은 수정할 수 없습니다.");
        }
        if (!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(form.getTitle());
        post.setContent(
                contentPreprocessing(form.getContent())
        );

        freeBoardSearchSyncPublisher.publishPostUpsert(post.getFreeBoardPostId());
    }

    public GetFreeBoardPostEditDto getEditablePost(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시물은 수정할 수 없습니다.");
        }
        if (!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }
        return GetFreeBoardPostEditDto.fromEntity(post);
    }

    //Hard delete 버전
    /**
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
     **/

    //Soft delete 버전
    @Transactional
    public void deletePost(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 게시글 입니다.");
        }
        if (!post.getMember().getMemberId().equals(member.getMemberId())
                || member.getRole().equals(Role.ROLE_ADMIN.toString())
        ) {
            throw new AccessDeniedException("본인 또는 관리자만 삭제 할 수 있습니다.");
        }

        post.softDelete();

        freeBoardSearchSyncPublisher.publishPostDelete(post.getFreeBoardPostId());
    }

    @Transactional
    public RecommendResult increasePostRecommendation(Member member, Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + postId));

        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시글은 추천할 수 없습니다.");
        }

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

        FreeBoardPostReply parent = null;
        if (form.getParentReplyId() != null) {
            parent = freeBoardPostReplyRepository.findById(form.getParentReplyId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다. id=" + form.getParentReplyId()));
        }

        freeBoardPostReplyRepository.save(
                FreeBoardPostReply.builder()
                        .member(member)
                        .freeBoardPost(post)
                        .parentReply(parent)
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

    //후에 인덱스 무조건 걸어야함, 풀스캔해야됨
    private Long countParentReplies (Long postId) {
        return freeBoardPostQueryRepository.countParentRepliesByPostId(postId);
    }

    private Long countPageNumber(long countParentReplies) {
        long size = pageProps.getReplySize();
        return (countParentReplies + size - 1) / size;
    }

    public ReplyPageInfo getReplyPageInfo(Long postId, Long currentPage) {
        Long countParentReplies = countParentReplies(postId);
        Long countPageNumber = countPageNumber(countParentReplies);
        return ReplyPageInfo.parentsOnly(countParentReplies, countPageNumber, currentPage);
    }

    //추후에 인덱스 달아서 업그레이드
    public List<GetFreeBoardPostReplyDto> getRepliesByPage(Long postId, Long page) {
        List<GetFreeBoardPostReplyDto> parentDtoList = getFreeBoardPostParentsReplies(postId, page);

        if (parentDtoList == null || parentDtoList.isEmpty()) {
            return parentDtoList;
        }

        Map<Long, List<GetFreeBoardPostReplyDto>> childrenMap = getChildrenReplies(parentDtoList);
        setPreviewChildren(parentDtoList, childrenMap);

        return parentDtoList;

    }

    public List<GetFreeBoardPostListDto> getPostsByCursor(PageCursorRequest pageCursorRequest) {
        Cursor requestCursor = pageCursorRequest.toCursor();
        int pageSizeOrDefault = pageCursorRequest.pageSizeOrDefault();

        return freeBoardPostQueryRepository.findPostsByCursor(requestCursor, pageSizeOrDefault + 1);
    }

    public CursorResponse<GetFreeBoardPostListDto> getCursorResponse(
            List<GetFreeBoardPostListDto> postLists,
            int pageSize
    ) {
        boolean hasNext = postLists.size() > pageSize;
        Cursor nextCursor = null;

        if (hasNext) {
            GetFreeBoardPostListDto last = postLists.get(postLists.size() - 1);
            nextCursor = new Cursor(last.getFreeBoardPostId(), last.getCreatedAt());
            postLists.remove(postLists.size() - 1);
        }

        return new CursorResponse<>(postLists, nextCursor, hasNext);
    }

    public CursorResponse<GetFreeBoardPostReplyDto> getReplyCursorResponse(
            List<GetFreeBoardPostReplyDto> replyDtoList,
            int pageSize
    ) {
        boolean hasNext = replyDtoList.size() > pageSize;
        Cursor nextCursor = null;

        if (hasNext) {
            GetFreeBoardPostReplyDto last = replyDtoList.get(replyDtoList.size() - 1);
            nextCursor = new Cursor(last.getFreeBoardPostReplyId(), last.getCreatedAt());
            replyDtoList.remove(replyDtoList.size() - 1);
        }

        log.info("[Test] ReplyCursor nextCursor = {}, replyDtoList = {} ", nextCursor, replyDtoList);

        return new CursorResponse<>(replyDtoList, nextCursor, hasNext);
    }


    public List<GetFreeBoardPostReplyDto> getChildrenRepliesByCursor(
            PageCursorRequest pageCursorRequest,
            Long postId,
            Long parentReplyId
            ) {
        Cursor requestCursor = pageCursorRequest.toCursor();
        int pageSizeOrDefault = pageCursorRequest.pageSizeOrDefault();

        return  freeBoardPostQueryRepository.findChildReplyByCursor(
                requestCursor,
                pageSizeOrDefault,
                postId,
                parentReplyId
        );
    }

    public List<GetFreeBoardPostListDto> getSearchPostsByCursor(PageCursorRequest pageCursorRequest, String keyword) {
        Cursor requestCursor = pageCursorRequest.toCursor();
        int pageSizeOrDefault = pageCursorRequest.pageSizeOrDefault();
        List<String> ngrams = ngramGenerator.generateByKeyword(keyword);
        int threshold = thresholdGenerator.searchThresholdGenerator(ngrams.size());

        if (requestCursor == null) {
            //첫페이지
            return freeBoardSearchRepository.findFirstSearchPosts(
                    ngrams,
                    threshold,
                    pageSizeOrDefault + 1
            );

        }

        return freeBoardSearchRepository.findSearchPostsByCursor(
                ngrams,
                threshold,
                requestCursor,
                pageSizeOrDefault + 1
        );
    }

    private String contentPreprocessing(String rawContent) {
        log.info("RAW = {}", rawContent);
        String normalizedContent = ContentNormalizer.normalizeContent(rawContent);
        log.info("NORMALIZED = {}", normalizedContent );
        String sanitizedContent =  HtmlSanitizer.sanitize(normalizedContent);
        log.info("SANITIZED = {}", sanitizedContent);
        return sanitizedContent;
    }


}
