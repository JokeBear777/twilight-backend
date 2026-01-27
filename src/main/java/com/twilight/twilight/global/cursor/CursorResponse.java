package com.twilight.twilight.global.cursor;

import java.util.List;


public record CursorResponse<T> (
        List<T> data,
        Cursor nextCursor,
        boolean hasNext
){ }


