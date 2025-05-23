package com.auth.center.rateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc: 限流
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/22 16:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {

    /**
     * 窗口大小，默认1000ms
     * @return
     */
    long window() default 10000;

    /**
     * 请求限制，默认10
     * @return
     */
    long limit() default 5;
}
