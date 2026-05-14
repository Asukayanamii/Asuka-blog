package com.asuka.backend.mapper;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
    Page<ArticleListVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO);
}
