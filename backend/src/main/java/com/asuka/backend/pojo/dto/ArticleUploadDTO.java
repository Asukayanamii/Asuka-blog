package com.asuka.backend.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章上传DTO")
public class ArticleUploadDTO {
    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章总结")
    private String summary;

    @Schema(description = "Markdown内容")
    private String contentMd;

    @Schema(description = "专题ID")
    private Integer topicId;

    @Schema(description = "排序系数")
    private Integer sort;
}
