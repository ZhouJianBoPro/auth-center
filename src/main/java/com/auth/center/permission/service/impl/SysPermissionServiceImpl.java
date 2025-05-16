package com.auth.center.permission.service.impl;

import com.auth.center.permission.entity.SysPermission;
import com.auth.center.permission.mapper.SysPermissionMapper;
import com.auth.center.permission.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Override
    public Set<String> getUserPermissionCodes(String userId) {
        return baseMapper.getUserPermissionCodes(userId);
    }
}
