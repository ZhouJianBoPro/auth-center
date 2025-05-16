package com.auth.center.role.service;

import com.auth.center.role.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    Set<String> getUserRoleCodes(String userId);

}
