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
import com.asuka.backend.service.MarkdownRenderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    private final MarkdownRenderService markdownRenderService;

    public ArticleServiceImpl(MarkdownRenderService markdownRenderService) {
        this.markdownRenderService = markdownRenderService;
    }


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
        String contentHtml = markdownRenderService.render(dto.getContentMd());

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
        String contentHtml = markdownRenderService.render(dto.getContentMd());

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
        String contentHtml = markdownRenderService.render(dto.getContentMd());

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

}
