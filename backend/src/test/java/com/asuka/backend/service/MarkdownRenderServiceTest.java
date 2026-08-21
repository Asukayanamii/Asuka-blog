package com.asuka.backend.service;

import com.asuka.backend.properties.FormulaProperties;
import com.asuka.backend.service.impl.FormulaRenderServiceImpl;
import com.asuka.backend.service.impl.MarkdownRenderServiceImpl;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownRenderServiceTest {
    @Test
    void formulaPngUsesOpaqueWhiteBackground() throws Exception {
        FormulaProperties properties = new FormulaProperties();
        FormulaRenderService.RenderedFormula rendered = new FormulaRenderServiceImpl(properties)
                .render("x^2+y^2", false);

        var image = ImageIO.read(new ByteArrayInputStream(rendered.bytes()));
        int corner = image.getRGB(0, 0);
        assertEquals(0xFFFFFFFF, corner);
        assertEquals(0xFF, (corner >>> 24) & 0xFF);
    }

    @Test
    void rendersInlineAndBlockFormulasWithoutChangingOriginalMarkdown() {
        FormulaProperties properties = new FormulaProperties();
        properties.setObjectPrefix("test/formulas");
        RecordingStorage storage = new RecordingStorage();
        MarkdownRenderService renderer = new MarkdownRenderServiceImpl(
                properties,
                new FormulaRenderServiceImpl(properties),
                storage
        );

        String markdown = "# 重复标题\n"
                + "## 重复标题\n"
                + "## [链接 **标题**]\n"
                + "价格 $5，不应当被识别。\n"
                + "行内公式 $x^2+y^2$。\n\n"
                + "$$\n\\\\int_0^1 x^2 dx\n$$\n\n"
                + "代码块中的 $5 不应识别。";

        String html = renderer.render(markdown);

        assertEquals(2, storage.keys.size());
        assertTrue(html.contains("class=\"formula-inline\""));
        assertTrue(html.contains("class=\"formula-block\""));
        assertTrue(html.contains("id=\"重复标题\""));
        assertTrue(html.contains("id=\"重复标题-2\""));
        assertTrue(html.contains("id=\"链接-标题\""));
        assertFalse(html.contains("$x^2+y^2$"));
    }

    @Test
    void rendersProvidedCalculusDocument() throws Exception {
        Path source = Path.of("D:/py_project/高数/期末考试知识点总结.md");
        if (!Files.exists(source)) {
            return;
        }
        FormulaProperties properties = new FormulaProperties();
        properties.setObjectPrefix("test/formulas");
        RecordingStorage storage = new RecordingStorage();
        MarkdownRenderService renderer = new MarkdownRenderServiceImpl(
                properties,
                new FormulaRenderServiceImpl(properties),
                storage
        );

        String html = renderer.render(Files.readString(source));

        assertFalse(html.isBlank());
        assertTrue(html.contains("formula-inline") || html.contains("formula-block"));
        assertTrue(storage.keys.size() > 10);
    }

    private static final class RecordingStorage implements ObjectStorageService {
        private final List<String> keys = new ArrayList<>();

        @Override
        public String putIfAbsent(String objectKey, InputStream content, long contentLength, String contentType) {
            keys.add(objectKey);
            assertEquals("image/png", contentType);
            assertTrue(contentLength > 0);
            return "https://cdn.example.test/" + objectKey;
        }
    }
}
