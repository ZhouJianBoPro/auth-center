package com.auth.center.model;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/14 18:38
 **/
public class JwtToken implements AuthenticationToken {

    private String token;


    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
