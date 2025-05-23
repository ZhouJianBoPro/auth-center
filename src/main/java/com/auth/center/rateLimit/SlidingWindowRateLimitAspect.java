package com.auth.center.rateLimit;

import com.auth.center.consts.RedisKeyConst;
import com.auth.center.exception.CustomException;
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

        //UserPrincipal userPrincipal = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
        //String redisKey = RedisKeyConst.RATE_LIMIT + userPrincipal.getUserId() + ":" + methodFullName;
        String redisKey = RedisKeyConst.RATE_LIMIT + methodFullName;

        // 获取方法上指定注解
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        Object[] argv = new Object[] {
                System.currentTimeMillis(),
                rateLimit.window(),
                rateLimit.limit()
        };

        RScript script = redissonClient.getScript();
        Boolean result = script.eval(
                RScript.Mode.READ_WRITE,
                getLuaScript(),
                RScript.ReturnType.BOOLEAN,
                Collections.singletonList(redisKey),
                argv);

        if(!result) {
            throw new CustomException("请求过于频繁！");
        }
    }

    private String getLuaScript() {

        return "local key = KEYS[1]\n" +
                // 当前时间戳
                "local timestamp = tonumber(ARGV[1])\n" +
                // 滑动窗口大小
                "local window = tonumber(ARGV[2])\n" +
                // 请求限制次数
                "local limit = tonumber(ARGV[3])\n" +

                // 1. 移除窗口外的请求
                "redis.call('zremrangebyscore', key, 0, timestamp - window)\n" +
                // 2. 获取当前窗口内的请求次数
                "local count = redis.call('zcard', key)\n" +
                // 3. 判断是否允许添加新的请求
                "if tonumber(count) >= tonumber(limit) then\n" +
                "   return false\n" +
                "else\n" +
                // 4. 添加当前时间戳作为 member 和 score
                "   redis.call('zadd', key, timestamp, timestamp)\n" +
                "   redis.call('expire', key, window * 2)\n" +
                "end";
    }
}
