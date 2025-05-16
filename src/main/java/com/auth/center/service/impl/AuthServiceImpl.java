package com.auth.center.service.impl;

import com.auth.center.consts.RedisKeyConst;
import com.auth.center.exception.CustomException;
import com.auth.center.model.JwtToken;
import com.auth.center.model.UserPrincipal;
import com.auth.center.service.IAuthService;
import com.auth.center.user.entity.SysUser;
import com.auth.center.user.service.ISysUserService;
import com.auth.center.util.JwtUtil;
import com.auth.center.util.MD5Util;
import com.auth.center.vo.LoginVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 16:28
 **/
@Service
public class AuthServiceImpl implements IAuthService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public LoginVO login(String username, String password) {

        SysUser sysUser = sysUserService.queryByUsername(username);
        if(Objects.isNull(sysUser)) {
            throw new CustomException("用户不存在！");
        }

        String encryptPassword = MD5Util.encrypt(password, sysUser.getMd5Salt());
        if(!Objects.equals(encryptPassword, sysUser.getPassword())) {
            throw new CustomException("密码错误！");
        }

        // 1. 生成token
        String token  = jwtUtil.generateToken(username);

        // 2. 缓存token
        redissonClient.getBucket(RedisKeyConst.TOKEN_USERNAME + username).set(token, jwtUtil.getExpire(), TimeUnit.SECONDS);

        // 3. 执行shiro认证
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JwtToken(token));

        return LoginVO.builder().username(username).token(token).build();
    }

    @Override
    public void logout(String token) {

        // 1. 获取当前用户主体，JwtFilter会获取request中的token
        Subject subject = SecurityUtils.getSubject();

        // 2. 获取当前用户的 Principal
        UserPrincipal userPrincipal = (UserPrincipal) subject.getPrincipal();

        // 3. 批量删除 Redis 中的 token，认证信息（授权信息会在subject.logout()自动清理）
        RBatch batch = redissonClient.createBatch();
        // token信息
        batch.getBucket(RedisKeyConst.TOKEN_USERNAME + userPrincipal.getUsername()).deleteAsync();
        // 认证信息
        batch.getBucket(RedisKeyConst.USER_AUTHENTICATION + token).deleteAsync();
        batch.execute();

        // 4. 执行 Shiro 登出（清除Subject），会自动删除redis授权信息
        subject.logout();
    }
}
