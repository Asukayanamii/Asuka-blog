package com.asuka.backend.mapper;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.entity.Article;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.pojo.vo.ArticleMarkdownRow;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    Page<ArticleListVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO);

    ArticleDetailVO selectById(Integer id);

    ArticleDetailWithMdVO selectByIdWithMd(Integer id);

    void insert(Article article);

    void update(Article article);

    void deleteById(Integer id);

    List<ArticleMarkdownRow> selectAllMarkdown();

    void updateContentHtml(Integer id, String contentHtml);
}
