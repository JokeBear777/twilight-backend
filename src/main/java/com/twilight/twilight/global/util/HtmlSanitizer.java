package com.twilight.twilight.global.util;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {

    private static final PolicyFactory POLICY =
            new HtmlPolicyBuilder()
                    .allowElements(
                            "p", "br", "div", "span",
                            "b", "strong", "i", "em", "u",
                            "ul", "ol", "li",
                            "h1", "h2", "h3",
                            "blockquote",
                            "img"
                    )
                    .allowAttributes("src", "alt").onElements("img")
                    .toFactory();

    public static String sanitize(String html) {
        if (html == null) return "";
        return POLICY.sanitize(html);
    }
}
