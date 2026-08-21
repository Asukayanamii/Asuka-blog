package com.asuka.backend.service;

/** 将原始 Markdown 转换为最终 HTML，原文不会被改写。 */
public interface MarkdownRenderService {
    String render(String markdown);
}
