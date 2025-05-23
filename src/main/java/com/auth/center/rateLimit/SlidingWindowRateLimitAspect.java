package com.auth.center.rateLimit;

import com.auth.center.consts.RedisKeyConst;
import com.auth.center.exception.CustomException;
import com.auth.center.model.UserPrincipal;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/22 16:31
 **/
@Aspect
@Component
public class SlidingWindowRateLimitAspect {

    @Resource
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.auth.center.rateLimit.RateLimit)")
    public void pointcut() {}

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodFullName = signature.getDeclaringTypeName() + "." + signature.getName();

        UserPrincipal userPrincipal = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
        String redisKey = RedisKeyConst.RATE_LIMIT + userPrincipal.getUserId() + ":" + methodFullName;

        // 获取方法上指定注解
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        RScript script = redissonClient.getScript();
        Boolean result = script.eval(
                RScript.Mode.READ_WRITE,
                getLuaScript(rateLimit.window(), rateLimit.limit()),
                RScript.ReturnType.BOOLEAN,
                Collections.singletonList(redisKey));

        if(!result) {
            throw new CustomException("请求过于频繁！");
        }
    }

    private String getLuaScript(long window, long limit) {

        long expireSeconds = window * 2;

        return "local key = KEYS[1]\n" +
                // 当前时间戳
                "local timestamp = " + System.currentTimeMillis() + "\n" +
                // 滑动窗口大小
                "local window = " + window * 1000 + "\n" +
                // 请求限制次数
                "local limit = " + limit  + "\n" +

                // 1. 移除窗口外的请求
                "redis.call('zremrangebyscore', key, 0, timestamp - window)\n" +
                // 2. 获取当前窗口内的请求次数
                "local count = redis.call('zcard', key)\n" +
                // 3. 判断是否允许添加新的请求
                "if count >= limit then\n" +
                "   return false\n" +
                "else\n" +
                // 4. 添加当前时间戳作为 member 和 score，并设置key的过期时间为窗口大小的2倍
                "   redis.call('zadd', key, timestamp, timestamp)\n" +
                "   redis.call('expire', key, " + expireSeconds + ")\n" +
                "   return true\n" +
                "end";
    }
}
