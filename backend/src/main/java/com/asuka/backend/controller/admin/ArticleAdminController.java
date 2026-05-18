package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/articles")
@Slf4j
@Tag(name = "管理端文章管理")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    @Operation(summary = "分页获取文章列表")
    public Result list(ArticlePageQueryDTO dto) {
        PageResult pageResult = articleService.getByTopicPage(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情（含MD原文）")
    public Result detail(@PathVariable Integer id) {
        return Result.success(articleService.getDetailWithMd(id));
    }

    @PostMapping
    @Operation(summary = "新增文章")
    public Result create(@RequestBody ArticleSaveDTO dto) {
        Integer articleId = articleService.createArticle(dto);
        return Result.success(articleId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    public Result update(@PathVariable Integer id, @RequestBody ArticleSaveDTO dto) {
        dto.setId(id);
        articleService.updateArticle(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result delete(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return Result.success();
    }
}
