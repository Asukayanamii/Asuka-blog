package com.asuka.backend.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    @Schema(description = "总记录数", example = "20")
    private Long total;

    @Schema(description = "当前页数据集合")
    private List<T> records;
}
