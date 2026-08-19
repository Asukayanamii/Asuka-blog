package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.dto.ArticleUploadDTO;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.result.PageResult;

public interface ArticleService {

    /** 按专题和标题分页查询文章列表。 */
    PageResult<ArticleListVO> getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO);

    /** 查询用户端文章详情。 */
    ArticleDetailVO getDetail(Integer id);

    /** 查询管理端编辑所需的文章详情和 Markdown 原文。 */
    ArticleDetailWithMdVO getDetailWithMd(Integer id);

    /** 上传 Markdown 文章并保存。 */
    Integer uploadArticle(ArticleUploadDTO articleUploadDTO);

    /** 新增文章。 */
    Integer createArticle(ArticleSaveDTO dto);

    /** 更新文章。 */
    void updateArticle(ArticleSaveDTO dto);

    /** 删除文章。 */
    void deleteArticle(Integer id);
}
