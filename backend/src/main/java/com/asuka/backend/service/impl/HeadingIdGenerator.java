package com.asuka.backend.service.impl;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;

import java.util.HashMap;
import java.util.Map;

/** 统一生成文章标题锚点，保持目录链接在渲染器迁移前后稳定。 */
public class HeadingIdGenerator {
    private final Map<String, Integer> occurrences = new HashMap<>();

    public void apply(Heading heading, MutableAttributes attributes) {
        String text = extractText(heading).trim();
        String base = text.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fff\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        if (base.isEmpty()) {
            base = "section";
        }
        int count = occurrences.merge(base, 1, Integer::sum);
        attributes.replaceValue("id", count == 1 ? base : base + "-" + count);
    }

    private String extractText(Node node) {
        StringBuilder result = new StringBuilder();
        Node child = node.getFirstChild();
        while (child != null) {
            if (child instanceof Text textNode) {
                result.append(textNode.getChars());
            } else {
                result.append(extractText(child));
            }
            child = child.getNext();
        }
        return result.toString();
    }
}
