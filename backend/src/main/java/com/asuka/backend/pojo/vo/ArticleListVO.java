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
@Schema(description = "文章VO")
public class ArticleListVO {
    @Schema(description = "文章ID")
    private Integer id;
    @Schema(description = "文章标题")
    private String title;
    @Schema(description = "文章总结")
    private String summary;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
