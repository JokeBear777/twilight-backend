package com.twilight.twilight.global.search;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NgramGenerator {

    public List<NgramIndex> generate(
            String title,
            String content,
            Long postId
    ) {
        return generate2Ngrams(
                preprocessing(title, content),
                postId
        );
    }

    private List<NgramIndex> generate2Ngrams(String text, Long postId) {
        List<NgramIndex> ngrams = new ArrayList<>(text.length());
        String[] tokens = text.split(" ");

        for (String token : tokens) {
            if (token.length() < 2) continue;

            for (int i = 0; i < token.length() - 1; i++) {
                ngrams.add(
                        NgramIndex.of(token.substring(i, i + 2), postId)
                );
            }
        }

        return ngrams;
    }

    private String preprocessing(String title, String content) {
        return (title + " " + content)
                .toLowerCase()
                .replaceAll("[^가-힣a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
