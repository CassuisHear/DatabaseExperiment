package com.whut.dbexperiment.common;

/**
 * 自定义业务异常类，
 * 可以在构造方法中添加更多的异常处理机制
 */
public class CustomException extends RuntimeException {

    public CustomException(String errorMessage) {
        super(errorMessage);
    }
}
