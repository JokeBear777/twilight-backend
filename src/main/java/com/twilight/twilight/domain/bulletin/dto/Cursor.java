package com.twilight.twilight.domain.bulletin.dto;

import lombok.*;

import java.time.LocalDateTime;


public record Cursor (
        Long lastId,
        LocalDateTime lastCreatedAt

){
    public boolean isFirst() {
        return lastId == null || lastCreatedAt == null;
    }
}