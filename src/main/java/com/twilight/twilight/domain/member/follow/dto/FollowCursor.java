package com.twilight.twilight.domain.member.follow.dto;

import com.twilight.twilight.global.cursor.Cursor;

public record FollowCursor (
        Long lastId
) implements Cursor
{
    public boolean isFirst() {
        return lastId == null;
    }

}
