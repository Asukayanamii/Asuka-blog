package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.ArticlePageQueryDTO;
import com.asuka.backend.pojo.dto.ArticleSaveDTO;
import com.asuka.backend.pojo.dto.ArticleUploadDTO;
import com.asuka.backend.pojo.vo.ArticleDetailWithMdVO;
import com.asuka.backend.pojo.vo.ArticleListVO;
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

    @PostMapping("/upload")
    @Operation(summary = "上传文章（MD文件）")
    public Result<Integer> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("topicId") Integer topicId,
            @RequestParam(value = "sort", defaultValue = "0") Integer sort) {

        log.info("上传文章: {}", title);

        if (file.isEmpty()) {
            // 空文件无需进入解析和持久化流程。
            return Result.error("文件不能为空");
        }

        try {
            // 统一按 UTF-8 读取 Markdown 文件，再复用 service 的文章保存逻辑。
            String contentMd = new String(file.getBytes(), StandardCharsets.UTF_8);

            ArticleUploadDTO dto = ArticleUploadDTO.builder()
                    .title(title)
                    .summary(summary)
                    .contentMd(contentMd)
                    .topicId(topicId)
                    .sort(sort)
                    .build();

            // DTO 组装完成后由 service 负责 Markdown 转换和入库。
            Integer articleId = articleService.uploadArticle(dto);
            return Result.success(articleId);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return Result.error("文件读取失败: " + e.getMessage());
        }
    }

    @PostMapping("/upload/json")
    @Operation(summary = "上传文章（JSON）")
    public Result<Integer> uploadJson(@RequestBody ArticleUploadDTO dto) {
        log.info("上传文章(JSON): {}", dto.getTitle());
        Integer articleId = articleService.uploadArticle(dto);
        return Result.success(articleId);
    }
}
