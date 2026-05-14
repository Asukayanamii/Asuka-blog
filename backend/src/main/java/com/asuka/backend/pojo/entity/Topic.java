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
@Schema(description = "专题")
public class Topic {
    private Integer id;
    //专题名称
    @Schema(description = "专题名称")
    private String name;
    //专题描述
    @Schema(description = "专题描述")
    private String description;
    //创建日期
    @Schema(description = "创建日期")
    private LocalDateTime createTime;
    //更新日期
    @Schema(description = "更新日期")
    private LocalDateTime updateTime;
}
