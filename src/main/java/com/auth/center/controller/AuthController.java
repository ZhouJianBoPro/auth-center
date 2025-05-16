package com.auth.center.controller;

import com.auth.center.dto.LoginDTO;
import com.auth.center.filter.JwtFilter;
import com.auth.center.service.IAuthService;
import com.auth.center.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 16:12
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IAuthService authService;

    @PostMapping("/login")
    public ResultVO<?> login(@RequestBody LoginDTO loginDTO) {
        return ResultVO.ok(authService.login(loginDTO.getUsername(), loginDTO.getPassword()));
    }

    @GetMapping("/logout")
    public ResultVO<?> logout(HttpServletRequest request) {
        authService.logout(request.getHeader(JwtFilter.HEADER));
        return ResultVO.ok();
    }
}
