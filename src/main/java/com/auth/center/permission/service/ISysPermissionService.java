package com.auth.center.permission.service;

import com.auth.center.permission.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
public interface ISysPermissionService extends IService<SysPermission> {

    Set<String> getUserPermissionCodes(String userId);

}
