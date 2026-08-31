package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.pojo.vo.ArticleMarkdownRow;
import com.asuka.backend.result.PageResult;

import java.util.List;

public interface ArticleService {

    /** 按专题和标题分页查询文章列表。 */
    PageResult<ArticleListVO> getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO);

    /** 查询用户端文章详情。 */
    ArticleDetailVO getDetail(Integer id);

    /** 查询管理端编辑所需的文章详情和 Markdown 原文。 */
    ArticleDetailWithMdVO getDetailWithMd(Integer id);

    /** 新增文章（HTML 由前端渲染后传入，后端不再解析 Markdown）。 */
    Integer createArticle(ArticleSaveDTO dto);

    /** 更新文章（HTML 由前端渲染后传入，后端不再解析 Markdown）。 */
    void updateArticle(ArticleSaveDTO dto);

    /** 删除文章。 */
    void deleteArticle(Integer id);

    /** 查询所有文章的 Markdown 原文，供前端批量重渲染 HTML。 */
    List<ArticleMarkdownRow> listAllMarkdown();

    /** 仅更新文章的 HTML 渲染产物，保留 Markdown 原文不变。 */
    void updateArticleHtml(Integer id, String contentHtml);
}
