package com.auth.center.role.mapper;

import com.auth.center.role.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色 Mapper 接口
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    Set<String> getUserRoleCodes(@Param("userId") String userId);

}
