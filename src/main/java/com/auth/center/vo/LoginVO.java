package com.auth.center.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 16:27
 **/
@Builder
@Data
public class LoginVO {

    private String username;

    private String token;
}
