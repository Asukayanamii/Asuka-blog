package com.asuka.backend.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章详情VO（含MD原文，供管理端使用）")
public class ArticleDetailWithMdVO {
    private Integer id;
    private String title;
    private String summary;

    @Schema(description = "Markdown原文")
    private String contentMd;

    @Schema(description = "HTML渲染内容")
    private String contentHtml;

    @Schema(description = "专题ID")
    private Integer topicId;

    @Schema(description = "专题名称")
    private String topicName;

    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
