package com.twilight.twilight.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "freeboard.page")
@Getter
@Setter
public class FreeBoardPageProps {
    private int postSize;
    private int replySize;
    private int childrenReplyFreeViewSize;
}
