package com.asuka.backend.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "统一返回结果类")
public class Result<T> {

    @Schema(description = "状态码：1 表示成功，0 表示失败", example = "1")
    private Integer code; //编码：1成功，0为失败

    @Schema(description = "响应消息", example = "success")
    private String msg; //错误信息

    @Schema(description = "响应数据")
    private T data; //数据

    /**
     * 返回无数据的成功响应。
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    /**
     * 返回包含指定类型数据的成功响应。
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.code = 1;
        result.msg = "success";
        return result;
    }

    /**
     * 返回不包含数据的失败响应。
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
