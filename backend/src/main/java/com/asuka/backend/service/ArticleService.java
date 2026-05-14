package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.result.PageResult;

public interface ArticleService {

    PageResult getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO);
}
