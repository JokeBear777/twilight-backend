package com.twilight.twilight.domain.bulletin.post.dto;

import java.util.List;


public record CursorResponse<T> (
        List<T> data,
        Cursor nextCursor,
        boolean hasNext
){ }


