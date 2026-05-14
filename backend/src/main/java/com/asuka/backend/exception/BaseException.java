package com.asuka.backend.exception;

//业务异常类
public class BaseException extends RuntimeException{
    public BaseException(String message) {
        super(message);
    }
    public BaseException() {
    }
}
