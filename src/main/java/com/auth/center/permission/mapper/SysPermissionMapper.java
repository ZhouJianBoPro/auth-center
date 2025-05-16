package com.auth.center.permission.mapper;

import com.auth.center.permission.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author Generator
 * @since 2025-05-14
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    Set<String> getUserPermissionCodes(@Param("userId") String userId);

}
