package com.auth.center.role.service.impl;

import com.auth.center.role.entity.SysUserRole;
import com.auth.center.role.mapper.SysUserRoleMapper;
import com.auth.center.role.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public Set<String> getUserRoleCodes(String userId) {

        return baseMapper.getUserRoleCodes(userId);
    }
}
