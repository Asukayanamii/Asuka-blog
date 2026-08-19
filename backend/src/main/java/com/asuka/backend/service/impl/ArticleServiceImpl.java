package com.asuka.backend.service.impl;


import com.asuka.backend.mapper.ArticleMapper;
import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.dto.ArticleUploadDTO;
import com.asuka.backend.pojo.entity.Article;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    private static final Pattern TABLE_SEPARATOR = Pattern.compile("^\\|[\\s\\-:|]+\\|$");

    private final Parser markdownParser = Parser.builder().extensions(java.util.List.of(TablesExtension.create())).build();

    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder()
            .extensions(java.util.List.of(TablesExtension.create()))
            .attributeProviderFactory(context -> (node, tagName, attributes) -> {
                if (node instanceof Heading) {
                    StringBuilder sb = new StringBuilder();
                    Node child = node.getFirstChild();
                    while (child != null) {
                        if (child instanceof Text) {
                            sb.append(((Text) child).getLiteral());
                        }
                        child = child.getNext();
                    }
                    String text = sb.toString().strip();
                    if (!text.isEmpty()) {
                        String slug = text.toLowerCase()
                                .replaceAll("[^a-z0-9\\u4e00-\\u9fff\\s-]", "")
                                .trim()
                                .replaceAll("\\s+", "-")
                                .replaceAll("-+", "-");
                        attributes.put("id", slug);
                    }
                }
            })
            .build();


    @Override
    public PageResult<ArticleListVO> getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("按专题与标题分页查询文章列表并按时间倒序排序:{}", articlePageQueryDTO);
        // PageHelper 会将当前线程的分页参数传递给紧随其后的文章查询。
        PageHelper.startPage(articlePageQueryDTO.getPageNum(), articlePageQueryDTO.getPageSize());
        Page<ArticleListVO> page = articleMapper.pageQuery(articlePageQueryDTO);
        // 将数据库分页结果转换为统一的分页响应，供管理端和用户端复用。
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public ArticleDetailVO getDetail(Integer id) {
        log.info("根据ID查询文章详情:{}", id);
        // 用户端只返回渲染后的详情，避免暴露编辑所需的 Markdown 原文。
        return articleMapper.selectById(id);
    }

    @Override
    public ArticleDetailWithMdVO getDetailWithMd(Integer id) {
        log.info("根据ID查询文章详情（含MD原文）:{}", id);
        // 管理端编辑需要同时获取 Markdown 原文和已生成的 HTML 内容。
        return articleMapper.selectByIdWithMd(id);
    }

    @Override
    public Integer uploadArticle(ArticleUploadDTO dto) {
        log.info("上传文章:{}", dto.getTitle());

        // 先将 Markdown 转换为 HTML，保存后前端可以直接展示文章内容。
        Node document = markdownParser.parse(preprocessMarkdown(dto.getContentMd()));
        String contentHtml = htmlRenderer.render(document);

        // 上传和手工新增共用同一套文章持久化字段，未提供排序值时默认排在末尾。
        Article article = Article.builder()
                .title(dto.getTitle())
                .summary(dto.getSummary())
                .contentMd(dto.getContentMd())
                .contentHtml(contentHtml)
                .topicId(dto.getTopicId())
                .sort(dto.getSort() != null ? dto.getSort() : 0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        articleMapper.insert(article);
        // MyBatis 回填自增主键，返回给调用方用于后续定位文章。
        return article.getId();
    }

    @Override
    public Integer createArticle(ArticleSaveDTO dto) {
        log.info("新增文章:{}", dto.getTitle());

        // 保存前统一生成 HTML，确保 Markdown 原文与展示内容保持同步。
        Node document = markdownParser.parse(preprocessMarkdown(dto.getContentMd()));
        String contentHtml = htmlRenderer.render(document);

        Article article = Article.builder()
                .title(dto.getTitle())
                .summary(dto.getSummary())
                .contentMd(dto.getContentMd())
                .contentHtml(contentHtml)
                .topicId(dto.getTopicId())
                .sort(dto.getSort() != null ? dto.getSort() : 0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        articleMapper.insert(article);
        // 返回新文章 ID，便于前端跳转或刷新详情。
        return article.getId();
    }

    @Override
    public void updateArticle(ArticleSaveDTO dto) {
        log.info("更新文章:{}", dto.getId());

        // 更新时重新渲染 HTML，避免只修改 Markdown 后展示内容仍是旧版本。
        Node document = markdownParser.parse(preprocessMarkdown(dto.getContentMd()));
        String contentHtml = htmlRenderer.render(document);

        Article article = Article.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .summary(dto.getSummary())
                .contentMd(dto.getContentMd())
                .contentHtml(contentHtml)
                .topicId(dto.getTopicId())
                .sort(dto.getSort() != null ? dto.getSort() : 0)
                .updateTime(LocalDateTime.now())
                .build();

        // 仅更新请求中允许编辑的字段和更新时间，保留原创建时间。
        articleMapper.update(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        log.info("删除文章:{}", id);
        // 删除由 Mapper 按主键执行，文章关联数据由数据库约束或 Mapper 负责处理。
        articleMapper.deleteById(id);
    }

    /**
     * 预处理 Markdown 内容，在表格定义前自动补空行，
     * 避免 CommonMark 因缺少前导空行把表格当成普通段落。
     */
    private String preprocessMarkdown(String markdown) {
        String[] lines = markdown.split("\n", -1);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // 当前行以 | 开头、下一行是表格分隔符（|--| 模式），且上一行非空 → 补空行
            if (i > 0
                    && !lines[i - 1].trim().isEmpty()
                    && i + 1 < lines.length
                    && line.trim().startsWith("|")
                    && TABLE_SEPARATOR.matcher(lines[i + 1].trim()).matches()) {
                result.append("\n");
            }
            result.append(line);
            if (i < lines.length - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
