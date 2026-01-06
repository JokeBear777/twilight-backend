package com.twilight.twilight.global.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class NgramGenerator {

    public List<NgramIndex> generateByPost(
            String title,
            String content,
            Long postId
    ) {
        return generate2NgramIndexes(
                preprocessing(title, content),
                postId
        );
    }

    public List<String> generateByKeyword(String keyword) {
        String[] words = keyword.split(" ");
        List<String> result = new ArrayList<>();

        for (String word : words) {
            log.info("[Test] word: {}", word);
            result.addAll(generateNgrams(word));
        }

        return result;
    }

    public List<String> generateNgrams(String text) {
        List<String> ngrams = new ArrayList<>(text.length());

        String[] tokens = text.split(" ");

        for (String token : tokens) {
            if (token.length() < 2) continue;

            for (int i = 0; i < token.length() - 1; i++) {
                ngrams.add(
                        token.substring(i, i + 2)
                );
            }
        }

        return ngrams;
    }

    private List<NgramIndex> generate2NgramIndexes(String text, Long postId) {
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
