package com.auth.center.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 14:00
 **/
@Data
@Configuration
public class ShiroProperties {

    @Value("${shiro.anonUrl}")
    private String annoUrl;
}
