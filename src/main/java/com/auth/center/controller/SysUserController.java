package com.auth.center.controller;


import com.auth.center.user.entity.SysUser;
import com.auth.center.user.service.ISysUserService;
import com.auth.center.vo.ResultVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author Generator
 * @since 2025-05-13
 */
@RestController
@RequestMapping("/user/sysUser")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    @GetMapping("/addUser")
    public ResultVO<?> addUser(@RequestParam String username, @RequestParam String password, @RequestParam String realname) {
        sysUserService.saveUser(username, password, realname);
        return ResultVO.ok();
    }

    /**
     * 授权检查（用户角色&用户权限）
     * @param username
     * @return
     */
    @RequiresRoles("admin")
    @RequiresPermissions("user:list")
    @GetMapping("/queryByUsername")
    public ResultVO<SysUser> queryByUsername(@RequestParam String username) {
        return ResultVO.ok(sysUserService.queryByUsername(username));
    }

}

