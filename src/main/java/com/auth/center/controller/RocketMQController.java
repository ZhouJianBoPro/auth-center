package com.auth.center.controller;

import com.auth.center.rocketmq.RocketMQProducer;
import com.auth.center.user.entity.SysUser;
import com.auth.center.user.service.ISysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/1/15 17:54
 **/
@RequestMapping("/rocketmq")
@RestController
public class RocketMQController {

    @Resource
    private RocketMQProducer rocketMQProducer;

    @Resource
    private ISysUserService sysUserService;

    @GetMapping("/test")
    public void test() {
        SysUser sysUser = sysUserService.queryByUsername("18146625631");
        String id = sysUser.getId();
        String tag = "test_tag";
        rocketMQProducer.sendMessage("test_topic", tag, tag + "-" + id, sysUser);
    }
}
