package com.auth.center.user.service;

import com.auth.center.model.UserPrincipal;
import com.auth.center.user.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author Generator
 * @since 2025-05-13
 */
public interface ISysUserService extends IService<SysUser> {

    void saveUser(String username, String password, String realname);

    UserPrincipal getByUsername(String username);

    SysUser queryByUsername(String username);

}
