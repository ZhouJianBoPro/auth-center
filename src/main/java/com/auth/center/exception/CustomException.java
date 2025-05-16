package com.auth.center.exception;

/**
 * @Description: 自定义金蝶异常
 * @Author: zjb
 * @createTime: 2023年08月02日 11:56:43
 */
@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }
}
