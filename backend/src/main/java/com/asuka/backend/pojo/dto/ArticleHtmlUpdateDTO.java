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
@Schema(description = "文章HTML更新DTO（仅更新渲染产物，不触碰 Markdown 原文）")
public class ArticleHtmlUpdateDTO {
    @Schema(description = "HTML渲染内容")
    private String contentHtml;
}
