package com.auth.center.service;

import com.auth.center.vo.LoginVO;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 16:24
 **/
public interface IAuthService {

    LoginVO login(String username, String password);

    void logout(String token);
}
