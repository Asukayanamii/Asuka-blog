package com.asuka.backend.service.impl;


import com.asuka.backend.mapper.ArticleMapper;
import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public PageResult getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("按专题与标题分页查询文章列表并按时间倒序排序:{}", articlePageQueryDTO);
        PageHelper.startPage(articlePageQueryDTO.getPageNum(), articlePageQueryDTO.getPageSize());
        Page<ArticleListVO> page = articleMapper.pageQuery(articlePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
