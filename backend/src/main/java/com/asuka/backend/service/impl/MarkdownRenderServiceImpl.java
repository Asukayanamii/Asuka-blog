package com.asuka.backend.service.impl;

import com.asuka.backend.exception.MarkdownRenderException;
import com.asuka.backend.properties.FormulaProperties;
import com.asuka.backend.service.FormulaRenderService;
import com.asuka.backend.service.MarkdownRenderService;
import com.asuka.backend.service.ObjectStorageService;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Flexmark AST 驱动的 Markdown 渲染器，负责公式资源化但不修改数据库中的原文。 */
@Service
public class MarkdownRenderServiceImpl implements MarkdownRenderService {
    private final FormulaProperties properties;
    private final FormulaRenderService formulaRenderService;
    private final ObjectStorageService objectStorageService;
    private final Parser parser;

    public MarkdownRenderServiceImpl(FormulaProperties properties,
                                     FormulaRenderService formulaRenderService,
                                     ObjectStorageService objectStorageService) {
        this.properties = properties;
        this.formulaRenderService = formulaRenderService;
        this.objectStorageService = objectStorageService;
        this.parser = Parser.builder()
                .extensions(List.of(TablesExtension.create()))
                .build();
    }

    @Override
    public String render(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        Node document = parser.parse(markdown);
        List<FormulaOccurrence> formulas = collectFormulas(document);
        String transformedMarkdown = replaceFormulas(markdown, formulas);
        Node transformedDocument = parser.parse(transformedMarkdown);
        HeadingIdGenerator headingIdGenerator = new HeadingIdGenerator();
        return HtmlRenderer.builder()
                .extensions(List.of(TablesExtension.create()))
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider apply(com.vladsch.flexmark.html.renderer.LinkResolverContext context) {
                        return (node, part, attributes) -> {
                            if (node instanceof Heading heading && part == AttributablePart.NODE) {
                                headingIdGenerator.apply(heading, attributes);
                            }
                        };
                    }

                    @Override public java.util.Set<Class<?>> getAfterDependents() { return null; }
                    @Override public java.util.Set<Class<?>> getBeforeDependents() { return null; }
                    @Override public boolean affectsGlobalScope() { return false; }
                })
                .build()
                .render(transformedDocument);
    }

    private List<FormulaOccurrence> collectFormulas(Node document) {
        // Flexmark 0.64 does not publish the TexMathExtension artifact. The scanner
        // below is deliberately stateful: it skips fenced/code spans and escaped
        // dollars, then feeds exact source ranges into the same AST render pipeline.
        List<FormulaOccurrence> formulas = new ArrayList<>();
        String source = document.getChars().toString();
        boolean fenced = false;
        int i = 0;
        while (i < source.length()) {
            if (source.startsWith("```", i) || source.startsWith("~~~", i)) {
                fenced = !fenced;
                i += 3;
                continue;
            }
            if (fenced || source.charAt(i) == '\\' || source.charAt(i) != '$') {
                i++;
                continue;
            }
            boolean block = source.startsWith("$$", i);
            int delimiterLength = block ? 2 : 1;
            if (!block && (i + 1 >= source.length() || Character.isDigit(source.charAt(i + 1)))) {
                i++;
                continue;
            }
            int close = findClosingDelimiter(source, i + delimiterLength, delimiterLength, block);
            if (close > i + delimiterLength) {
                String latex = source.substring(i + delimiterLength, close);
                if ((block || !latex.contains("\n")) && (block || isLikelyInlineFormula(latex))) {
                    formulas.add(new FormulaOccurrence(i, close + delimiterLength, latex, block));
                    i = close + delimiterLength;
                    continue;
                }
            }
            i++;
        }
        formulas.sort(Comparator.comparingInt(FormulaOccurrence::startOffset).reversed());
        return formulas;
    }

    private int findClosingDelimiter(String source, int start, int length, boolean block) {
        for (int i = start; i <= source.length() - length; i++) {
            if (source.charAt(i) == '\\') {
                i++;
                continue;
            }
            if (source.startsWith(length == 2 ? "$$" : "$", i)
                    && (!block || i == 0 || source.charAt(i - 1) != '\\')) {
                return i;
            }
        }
        return -1;
    }

    private boolean isLikelyInlineFormula(String latex) {
        String value = latex.trim();
        return !value.isEmpty() && !value.matches("[0-9]+(?:\\.[0-9]+)?")
                && !(value.length() == 1 && Character.isLetterOrDigit(value.charAt(0)));
    }

    private String replaceFormulas(String markdown, List<FormulaOccurrence> formulas) {
        String transformed = markdown;
        for (FormulaOccurrence occurrence : formulas) {
            String latex = occurrence.latex();
            FormulaRenderService.RenderedFormula image = formulaRenderService.render(latex, occurrence.block());
            String hash = sha256(latex + "\n" + occurrence.block() + "\n" + properties.getFontSize()
                    + "\n" + properties.getDpi() + "\n" + properties.getRenderVersion());
            String key = properties.getObjectPrefix().replaceAll("/$", "") + "/" + hash + ".png";
            String url = objectStorageService.putIfAbsent(key, new ByteArrayInputStream(image.bytes()),
                    image.bytes().length, "image/png");
            String html = "<img class=\"formula-" + (occurrence.block() ? "block" : "inline")
                    + "\" src=\"" + escapeAttribute(url) + "\" alt=\"" + escapeAttribute(latex) + "\">";
            transformed = transformed.substring(0, occurrence.startOffset()) + html
                    + transformed.substring(occurrence.endOffset());
        }
        return transformed;
    }

    private String normalizeLatex(String value, boolean block) {
        String result = value.trim();
        String delimiter = block ? "$$" : "$";
        if (result.startsWith(delimiter) && result.endsWith(delimiter) && result.length() >= delimiter.length() * 2) {
            result = result.substring(delimiter.length(), result.length() - delimiter.length());
        }
        return result.trim();
    }

    private String escapeAttribute(String value) {
        return value.replace("&", "&amp;").replace("\"", "&quot;")
                .replace("<", "&lt;").replace(">", "&gt;");
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte item : digest) {
                result.append(String.format("%02x", item));
            }
            return result.toString();
        } catch (Exception e) {
            throw new MarkdownRenderException("生成公式资源哈希失败", e);
        }
    }

    private record FormulaOccurrence(int startOffset, int endOffset, String latex, boolean block) {
    }
}
