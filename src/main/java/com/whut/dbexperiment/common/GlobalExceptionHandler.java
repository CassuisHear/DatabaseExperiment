package com.whut.dbexperiment.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局的异常处理器类
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 数据库中出现 重名异常(主要是针对user表的用户名) 时的处理方法
     *
     * @param ex 注入的异常对象
     * @return 出错信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {

        //获取错误信息
        String exMessage = ex.getMessage();

        log.error(exMessage);

        //进一步地截取错误信息
        if (exMessage.contains("Duplicate entry")) {
            String[] splits = exMessage.split(" ");
            String errorMsg = splits[2] + "已存在...";
            return R.error(errorMsg);
        }

        return R.error("未知错误...");
    }

    /**
     * 数据库中重名 异常处理方法
     *
     * @param ex 自定义的异常类CustomException对象
     * @return 出错信息
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex) {
        log.error(ex.getMessage());

        //将异常信息返回
        return R.error(ex.getMessage());
    }
}
