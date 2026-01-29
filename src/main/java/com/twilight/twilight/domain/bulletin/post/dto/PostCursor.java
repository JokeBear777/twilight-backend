package com.twilight.twilight.domain.bulletin.post.dto;

import com.twilight.twilight.global.cursor.Cursor;

import java.time.LocalDateTime;


public record PostCursor(
        Long lastId,
        LocalDateTime lastCreatedAt

) implements Cursor
{
    public boolean isFirst() {
        return lastId == null || lastCreatedAt == null;
    }
}