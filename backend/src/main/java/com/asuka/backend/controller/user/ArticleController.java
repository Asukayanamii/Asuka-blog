package com.asuka.backend.controller.user;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleUploadDTO;
import com.asuka.backend.pojo.vo.ArticleDetailVO;
import com.asuka.backend.result.PageResult;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@Tag(name = "文章管理")
@RequestMapping("/user/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    @Operation(summary = "获取文章列表")
    public Result getByTopicPage(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("按专题与标题分页查询文章列表并按时间倒序排序");
        PageResult pageResult = articleService.getByTopicPage(articlePageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/detail")
    @Operation(summary = "获取文章详情")
    public Result getDetail(Integer id) {
        log.info("根据ID查询文章详情:{}", id);
        ArticleDetailVO detail = articleService.getDetail(id);
        return Result.success(detail);
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文章（MD文件）")
    public Result upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("topicId") Integer topicId,
            @RequestParam(value = "sort", defaultValue = "0") Integer sort) {

        log.info("上传文章: {}", title);

        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            String contentMd = new String(file.getBytes(), StandardCharsets.UTF_8);

            ArticleUploadDTO dto = ArticleUploadDTO.builder()
                    .title(title)
                    .summary(summary)
                    .contentMd(contentMd)
                    .topicId(topicId)
                    .sort(sort)
                    .build();

            Integer articleId = articleService.uploadArticle(dto);
            return Result.success(articleId);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return Result.error("文件读取失败: " + e.getMessage());
        }
    }

    @PostMapping("/upload/json")
    @Operation(summary = "上传文章（JSON）")
    public Result uploadJson(@RequestBody ArticleUploadDTO dto) {
        log.info("上传文章(JSON): {}", dto.getTitle());
        Integer articleId = articleService.uploadArticle(dto);
        return Result.success(articleId);
    }
}
