package com.auth.center.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.auth.center.model.UserPrincipal;
import com.auth.center.user.entity.SysUser;
import com.auth.center.user.mapper.SysUserMapper;
import com.auth.center.user.service.ISysUserService;
import com.auth.center.util.MD5Util;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author Generator
 * @since 2025-05-13
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public void saveUser(String username, String password, String realname) {

        String salt = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER_LOWER,12);

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setRealname(realname);
        sysUser.setMd5Salt(salt);
        sysUser.setPassword(MD5Util.encrypt(password, salt));
        baseMapper.insert(sysUser);
    }

    @Override
    public UserPrincipal getByUsername(String username) {

        SysUser sysUser = queryByUsername(username);
        if(Objects.isNull(sysUser)) {
            return null;
        }

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(sysUser.getUsername());
        userPrincipal.setUserId(sysUser.getId());
        return userPrincipal;
    }

    @Override
    public SysUser queryByUsername(String username) {

        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(SysUser::getUsername, username)
                .one();
    }

}
