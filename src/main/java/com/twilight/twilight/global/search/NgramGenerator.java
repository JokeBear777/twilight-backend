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
        return List.of();
    }

}
