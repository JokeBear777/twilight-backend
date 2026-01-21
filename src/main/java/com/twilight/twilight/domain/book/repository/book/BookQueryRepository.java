package com.twilight.twilight.domain.book.repository.book;

import com.twilight.twilight.domain.book.dto.GetBookInfoDto;

import java.util.List;

public interface BookQueryRepository {
    List<GetBookInfoDto> findBooksByTagsLimit(List<Long> tagIds, int limit);
}
