package com.twilight.twilight.domain.bulletin.feed.service;

import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedQueryRepository;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedRepository;
import com.twilight.twilight.global.cursor.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedQueryRepository feedQueryRepository;

    //여기서 버퍼있는지 확인하고 없으면 db에서 조회
    public List<GetFeedPostListDto> getFeedByCursor(
            FeedCursorRequest cursorRequest,
            Long memberId
    ) {
        FeedCursor requestFeedCursor = cursorRequest.toCursor();
        int pageSizeOrDefault = cursorRequest.pageSizeOrDefault();

        return List.of();
    }

    public CursorResponse<GetFeedPostListDto> getCursorResponse(
            List<GetFeedPostListDto> feedPostListDto,
            int pageSize
            ) {


        return null;
    }
}
