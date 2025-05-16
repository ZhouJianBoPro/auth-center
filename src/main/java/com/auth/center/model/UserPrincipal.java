package com.auth.center.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Desc: 用户身份信息（shiro要求序列化，否则认证信息和授权信息无法写入到redis中）
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/14 15:16
 **/
@Data
public class UserPrincipal implements Serializable {

    private String userId;

    private String username;
}
