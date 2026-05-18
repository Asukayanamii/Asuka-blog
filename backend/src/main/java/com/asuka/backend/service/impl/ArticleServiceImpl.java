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
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    private final Parser markdownParser = Parser.builder().build();
    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();


    @Override
    public PageResult getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("按专题与标题分页查询文章列表并按时间倒序排序:{}", articlePageQueryDTO);
        PageHelper.startPage(articlePageQueryDTO.getPageNum(), articlePageQueryDTO.getPageSize());
        Page<ArticleListVO> page = articleMapper.pageQuery(articlePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public ArticleDetailVO getDetail(Integer id) {
        log.info("根据ID查询文章详情:{}", id);
        return articleMapper.selectById(id);
    }

    @Override
    public ArticleDetailWithMdVO getDetailWithMd(Integer id) {
        log.info("根据ID查询文章详情（含MD原文）:{}", id);
        return articleMapper.selectByIdWithMd(id);
    }

    @Override
    public Integer uploadArticle(ArticleUploadDTO dto) {
        log.info("上传文章:{}", dto.getTitle());

        Node document = markdownParser.parse(dto.getContentMd());
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
        return article.getId();
    }

    @Override
    public Integer createArticle(ArticleSaveDTO dto) {
        log.info("新增文章:{}", dto.getTitle());

        Node document = markdownParser.parse(dto.getContentMd());
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
        return article.getId();
    }

    @Override
    public void updateArticle(ArticleSaveDTO dto) {
        log.info("更新文章:{}", dto.getId());

        Node document = markdownParser.parse(dto.getContentMd());
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

        articleMapper.update(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        log.info("删除文章:{}", id);
        articleMapper.deleteById(id);
    }
}
