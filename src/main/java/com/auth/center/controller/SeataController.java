package com.auth.center.controller;

import com.auth.center.seata.ISeataService;
import com.auth.center.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/29 18:28
 **/
@RestController
@RequestMapping("/seata")
public class SeataController {

    @Resource
    private ISeataService seataService;

    @GetMapping("/test")
    public ResultVO<?> test() {
        seataService.testTransaction();
        return ResultVO.ok();
    }
}
