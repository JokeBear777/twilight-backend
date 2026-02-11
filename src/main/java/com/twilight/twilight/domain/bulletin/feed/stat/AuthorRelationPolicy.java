package com.twilight.twilight.domain.bulletin.feed.stat;

public interface AuthorRelationPolicy {
    void enforce(Long viewerId);
}
