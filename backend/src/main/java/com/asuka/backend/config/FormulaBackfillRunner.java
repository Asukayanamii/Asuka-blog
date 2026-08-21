package com.asuka.backend.config;

import com.asuka.backend.mapper.ArticleMapper;
import com.asuka.backend.pojo.vo.ArticleMarkdownRow;
import com.asuka.backend.service.MarkdownRenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** 可选的存量文章回填入口；默认关闭，避免应用启动时意外改写数据。 */
@Slf4j
@Component
@ConditionalOnProperty(name = "blog.formula.backfill.enabled", havingValue = "true")
public class FormulaBackfillRunner implements CommandLineRunner {
    private final ArticleMapper articleMapper;
    private final MarkdownRenderService markdownRenderService;

    public FormulaBackfillRunner(ArticleMapper articleMapper, MarkdownRenderService markdownRenderService) {
        this.articleMapper = articleMapper;
        this.markdownRenderService = markdownRenderService;
    }

    @Override
    public void run(String... args) {
        int success = 0;
        int failure = 0;
        for (ArticleMarkdownRow article : articleMapper.selectAllMarkdown()) {
            try {
                // 只重算展示 HTML，content_md 永远保留可编辑的原始 Markdown。
                articleMapper.updateContentHtml(article.getId(), markdownRenderService.render(article.getContentMd()));
                success++;
            } catch (RuntimeException e) {
                failure++;
                log.error("文章公式回填失败，articleId={}", article.getId(), e);
            }
        }
        log.info("公式回填完成，成功={}，失败={}", success, failure);
    }
}
