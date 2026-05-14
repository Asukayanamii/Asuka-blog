package com.asuka.backend.handler;

import com.asuka.backend.constant.MessageConstant;
import com.asuka.backend.exception.BaseException;
import com.asuka.backend.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Operation(summary = "全局异常处理")
    @ExceptionHandler
    public Result exceptionHandler(Exception ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error("未知错误");
    }

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}", ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + MessageConstant.ALREADY_EXIST;
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }
}
