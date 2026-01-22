package com.twilight.twilight.domain.book.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookInfoDto {
    private Long bookId;
    private String name;
    private String author;
    private Integer pageCount;
    private String description;

    public GetBookInfoDto(Long bookId,
                          String name,
                          String author,
                          Integer pageCount,
                          String description) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.pageCount = pageCount;
        this.description = description;
    }
}
