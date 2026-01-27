package com.twilight.twilight.domain.member.follow.Service;

import com.twilight.twilight.global.cursor.CursorResponse;
import com.twilight.twilight.domain.member.follow.Repository.FollowQueryRepository;
import com.twilight.twilight.domain.member.follow.Repository.FollowRepository;
import com.twilight.twilight.domain.member.follow.dto.FollowCursor;
import com.twilight.twilight.domain.member.follow.dto.FollowCursorRequest;
import com.twilight.twilight.domain.member.follow.dto.GetFollowListDto;
import com.twilight.twilight.domain.member.follow.entity.Follow;
import com.twilight.twilight.domain.member.member.entity.Member;
import com.twilight.twilight.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final FollowQueryRepository followQueryRepository;

    @Transactional
    public void followTargetMember(
            Long targetMemberId,
            Long meId
    ) {
        if (targetMemberId == null) {
            throw new IllegalArgumentException("targetMemberId가 null 입니다.");
        }
        if (meId == null) {
            throw new IllegalArgumentException("meId가 null 입니다.");
        }

        Member me = memberRepository.findById(meId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + meId));

        Member targetMember = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + targetMemberId));

        try {
            followRepository.save(Follow.create(me, targetMember));
        }
        catch (DataIntegrityViolationException e) {
            //uk_follow (follower_id, following_id) 중복이면 그냥 무시
            return;
        }
    }

    @Transactional
    public void unfollowTargetMember(
            Long targetMemberId,
            Long meId
    ) {
        if (targetMemberId == null) {
            throw new IllegalArgumentException("targetMemberId가 null 입니다.");
        }
        if (meId == null) {
            throw new IllegalArgumentException("meId가 null 입니다.");
        }

        followQueryRepository.deleteByFollowerIdAndFollowingId(meId, targetMemberId);
    }

    public List<GetFollowListDto> getFollowersByCursor(
            FollowCursorRequest request,
            Long targetMemberId
    ) {
        if (targetMemberId == null) {
            throw new IllegalArgumentException("targetMemberId가 null 입니다.");
        }
        memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + targetMemberId));

        FollowCursor cursor = request.toCursor();

        return followQueryRepository.findFollowerListByCursor(
                cursor,
                request.pageSizeOrDefault() + 1,
                targetMemberId
        );
    }

    public CursorResponse<GetFollowListDto> getCursorResponse(
            List<GetFollowListDto> list,
            int pageSize
    ) {
        boolean hasNext = list.size() > pageSize;
        FollowCursor nextCursor = null;

        if (hasNext) {
            GetFollowListDto last = list.get(list.size() - 1);
            nextCursor = new FollowCursor(last.getFollowerId());
            list.remove(list.size() - 1);
        }

        return new CursorResponse<>(list, nextCursor, hasNext);
    }


}
