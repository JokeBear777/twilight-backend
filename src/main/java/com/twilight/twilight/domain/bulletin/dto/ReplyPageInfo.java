package com.twilight.twilight.domain.bulletin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyPageInfo {
    private Long countParents;
    private Long countChildren;
    private Long countTotal;
    private Long pageSize;
    private Long currentPage;

    public static ReplyPageInfo parentsOnly(Long parentsCount, Long pageSize) {
        //long totalPages = (parentsCount + pageSize - 1) / pageSize;

        return new ReplyPageInfo(parentsCount,
                null,
                null,
                pageSize,
                null);
    }

    public static ReplyPageInfo parentsOnly(Long parentsCount, Long pageSize, Long currentPage) {
        //long totalPages = (parentsCount + pageSize - 1) / pageSize;

        return new ReplyPageInfo(parentsCount,
                null,
                null,
                pageSize,
                currentPage);
    }

}
