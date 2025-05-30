package com.auth.center.seata.impl;

import com.auth.center.role.service.ISysRoleService;
import com.auth.center.seata.ISeataService;
import com.auth.center.user.service.ISysUserService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/29 18:29
 **/
@Service
public class SeataServiceImpl implements ISeataService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysRoleService sysRoleService;

    @Autowired
    private DataSource dataSource;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void testTransaction() {

        System.out.println("DataSource type: " + dataSource.getClass().getName());

        System.out.println("TC XID = " + RootContext.getXID());

        sysRoleService.testSave();

        // 会抛出异常
        sysUserService.testSave();
    }
}
