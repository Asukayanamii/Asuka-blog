package com.asuka.backend.exception;

/** Markdown 或公式资源生成失败时使用的业务异常。 */
public class MarkdownRenderException extends RuntimeException {
    public MarkdownRenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarkdownRenderException(String message) {
        super(message);
    }
}
