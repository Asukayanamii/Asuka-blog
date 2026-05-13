package com.asuka.backend.pojo.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "专题VO")
public class TopicVO {
    private Integer id;
    //专题名称
    private String name;
    //专题描述
    private String description;
}
