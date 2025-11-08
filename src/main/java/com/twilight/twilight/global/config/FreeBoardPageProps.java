package com.twilight.twilight.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "freeboard.page")
public class FreeBoardPageProps {
    private int postSize;
    private int replySize;

    public int getPostSize() {
        return postSize;
    }

    public void setPostSize(int postSize) {
        this.postSize = postSize;
    }

    public int getReplySize() {
        return replySize;
    }

    public void setReplySize(int replySize) {
        this.replySize = replySize;
    }
}
