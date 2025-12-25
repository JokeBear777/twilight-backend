package com.twilight.twilight.domain.bulletin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public record CursorResponse<T> (
        List<T> data,
        Cursor nextCursor,
        boolean hasNext
){ }


