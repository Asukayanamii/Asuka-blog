package com.asuka.backend.controller.user;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "文章管理")
@RequestMapping("/user/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @GetMapping("/list")
    @Operation(summary = "获取文章列表")
    public Result  getByTopicPage(ArticlePageQueryDTO  articlePageQueryDTO) {
        log.info("按专题与标题分页查询文章列表并按时间倒序排序");
        PageResult pageResult = articleService.getByTopicPage(articlePageQueryDTO);
        return Result.success(pageResult);
    }
}
