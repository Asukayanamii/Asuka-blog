package com.asuka.backend.pojo.entity;

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
@Schema(description = "文章")
public class Article {
    @Schema(description = "文章ID")
    private Integer id;
    @Schema(description = "文章标题")
    private String title;
    @Schema(description = "文章总结")
    private String summary;
    @Schema(description = "文章内容MD格式")
    private String contentMd;
    @Schema(description = "文章内容HTML格式")
    private String contentHtml;
    @Schema(description = "排序系数")
    private Integer sort;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}
