package com.asuka.backend.mapper;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.entity.Article;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
    Page<ArticleListVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO);

    ArticleDetailVO selectById(Integer id);

    ArticleDetailWithMdVO selectByIdWithMd(Integer id);

    void insert(Article article);

    void update(Article article);

    void deleteById(Integer id);
}
