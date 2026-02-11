package com.twilight.twilight.domain.bulletin.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/social/feed")
@RequiredArgsConstructor
@Slf4j
public class FeedViewController {

    @GetMapping
    public String getFeed() {

        return "feed/feed";
    }

}
