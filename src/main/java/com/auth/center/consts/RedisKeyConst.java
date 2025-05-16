package com.auth.center.consts;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/13 16:28
 **/
public class RedisKeyConst {

    public static final String TOKEN_USERNAME = "token:username:";

    public static final String TOKEN_BLACKLIST = "token:blacklist:";

    // shiro认证信息
    public static final String USER_AUTHENTICATION = "shiro:cache:com.auth.center.realm.ShiroRealm.authenticationCache:";

    // shiro授权信息
    public static final String USER_AUTHORIZATION = "shiro:cache:com.auth.center.realm.ShiroRealm.authorizationCache:";
}
