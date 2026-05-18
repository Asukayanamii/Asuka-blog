package com.asuka.backend.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专题保存/更新DTO")
public class TopicSaveDTO {
    @Schema(description = "专题名称")
    private String topicName;

    @Schema(description = "专题描述")
    private String description;
}
