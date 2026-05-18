package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.dto.ArticleUploadDTO;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.result.PageResult;

public interface ArticleService {

    PageResult getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO);

    ArticleDetailVO getDetail(Integer id);

    ArticleDetailWithMdVO getDetailWithMd(Integer id);

    Integer uploadArticle(ArticleUploadDTO articleUploadDTO);

    Integer createArticle(ArticleSaveDTO dto);

    void updateArticle(ArticleSaveDTO dto);

    void deleteArticle(Integer id);
}
