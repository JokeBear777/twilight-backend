package com.twilight.twilight.global.search;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NgramIndex {

    private String ngram;
    private Long postId;

    private NgramIndex(String ngram, Long postId) {
        this.ngram = ngram;
        this.postId = postId;
    }

    public static NgramIndex of(String ngram, Long postId) {
        return new NgramIndex(ngram, postId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NgramIndex)) return false;
        NgramIndex that = (NgramIndex) o;
        return Objects.equals(ngram, that.ngram)
                && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ngram, postId);
    }
}
