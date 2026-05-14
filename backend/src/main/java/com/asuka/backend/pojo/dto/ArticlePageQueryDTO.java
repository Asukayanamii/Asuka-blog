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
@Schema(description = "文章分页查询DTO")
public class ArticlePageQueryDTO {
    @Schema(description = "页码")
    private Integer pageNum;
    @Schema(description = "每页数量")
    private Integer pageSize;
    @Schema(description = "专题ID")
    private Integer topicId;
    @Schema(description = "标题")
    private String title;

}
