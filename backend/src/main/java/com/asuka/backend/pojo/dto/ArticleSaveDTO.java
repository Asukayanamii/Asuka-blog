package com.asuka.backend.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "文章保存/更新DTO")
public class ArticleSaveDTO {
    @Schema(description = "文章ID（更新时使用）")
    private Integer id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章摘要")
    private String summary;

    @Schema(description = "Markdown内容")
    private String contentMd;

    @Schema(description = "专题ID")
    private Integer topicId;

    @Schema(description = "排序权重")
    private Integer sort;
}
