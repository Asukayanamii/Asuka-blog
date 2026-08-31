package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.ArticleHtmlUpdateDTO;
import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
import com.asuka.backend.pojo.vo.ArticleMarkdownRow;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/articles")
@Slf4j
@Tag(name = "管理端/管理端文章管理")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    @Operation(summary = "分页获取文章列表")
    public Result<PageResult<ArticleListVO>> list(ArticlePageQueryDTO dto) {
        // 分页条件交由 service 统一处理，controller 只负责封装响应。
        PageResult<ArticleListVO> pageResult = articleService.getByTopicPage(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情（含MD原文）")
    public Result<ArticleDetailWithMdVO> detail(@PathVariable Integer id) {
        return Result.success(articleService.getDetailWithMd(id));
    }

    @PostMapping
    @Operation(summary = "新增文章")
    public Result<Integer> create(@RequestBody ArticleSaveDTO dto) {
        Integer articleId = articleService.createArticle(dto);
        return Result.success(articleId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    public Result<Void> update(@PathVariable Integer id, @RequestBody ArticleSaveDTO dto) {
        // 以路径参数为准，避免客户端提交的 ID 与实际操作资源不一致。
        dto.setId(id);
        articleService.updateArticle(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result<Void> delete(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @GetMapping("/markdown")
    @Operation(summary = "获取全部文章的 Markdown 原文")
    public Result<List<ArticleMarkdownRow>> listMarkdown() {
        // 管理端“一键刷新”按钮先拉取所有 Markdown，前端批量渲染后再逐个更新 HTML。
        return Result.success(articleService.listAllMarkdown());
    }

    @PutMapping("/{id}/html")
    @Operation(summary = "更新文章HTML产物")
    public Result<Void> updateHtml(@PathVariable Integer id, @RequestBody ArticleHtmlUpdateDTO dto) {
        // 以路径参数为准，仅覆盖渲染产物，不触碰 Markdown 原文。
        articleService.updateArticleHtml(id, dto.getContentHtml());
        return Result.success();
    }
}
