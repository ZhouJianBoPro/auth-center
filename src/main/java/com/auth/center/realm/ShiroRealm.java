package com.auth.center.realm;

import com.auth.center.consts.RedisKeyConst;
import com.auth.center.model.JwtToken;
import com.auth.center.model.UserPrincipal;
import com.auth.center.permission.service.ISysPermissionService;
import com.auth.center.role.service.ISysUserRoleService;
import com.auth.center.user.service.ISysUserService;
import com.auth.center.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/13 14:51
 **/
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Resource
    private ISysPermissionService sysPermissionService;

    @Resource
    private ISysUserService sysUserService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 启用认证信息缓存
     * @return
     */
    @Override
    public boolean isAuthenticationCachingEnabled() {
        return true;
    }

    /**
     * 启用授权信息缓存
     * @return
     */
    @Override
    public boolean isAuthorizationCachingEnabled() {
        return true;
    }

    /**
     * 授权（查询数据库中用户角色和权限）
     * 1. 首次访问需要授权检查的接口时，会进行授权处理（ @RequiresPermissions、@RequiresRoles）
     * 2. 授权查询到的角色和权限会缓存到ShiroConfig中的redis中，后续访问授权检查接口时会先查询缓存，缓存中没有时再调用该授权查询
     * 3. 当缓存中没有该用户的授权信息（缓存已失效）时，会调用该授权查询
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        UserPrincipal principal = (UserPrincipal) principals.getPrimaryPrincipal();
        log.info("授权开始, principal = {}", principal);

        String userId = principal.getUserId();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 设置用户角色
        Set<String> roleCodeSet = sysUserRoleService.getUserRoleCodes(userId);
        authorizationInfo.setRoles(roleCodeSet);

        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        Set<String> permissionSet = sysPermissionService.getUserPermissionCodes(userId);
        authorizationInfo.addStringPermissions(permissionSet);
        return authorizationInfo;
    }

    /**
     * 认证
     * 1. 首次访问需要认证的接口时，会进行认证处理
     * 2. 认证获取到的信息会缓存到redis中，后续访问需要认证的接口时先从redis中获取认证信息，缓存中没有时再调用该认证查询
     * 3. 当缓存中没有该用户的认证信息（缓存已失效）时，会调用该认证查询
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String) authenticationToken.getCredentials();

        // 1. 通过签名解析token，验证token是否合法
        String username = jwtUtil.getUsernameFromToken(token);
        if(StringUtils.isBlank(username)) {
            throw new AuthenticationException("token解析失败，非法的token！");
        }

        // 2. 校验是否为该用户当前有效的 Token（踢下线控制）
        RBucket<String> tokenBucket = redissonClient.getBucket(RedisKeyConst.TOKEN_USERNAME + username);
        if(!tokenBucket.isExists()) {
            throw new AuthenticationException("token已失效，请重新登录！");
        } else if(!Objects.equals(tokenBucket.get(), token)) {
            throw new AuthenticationException("账号已在别处登录！");
        }

        // 3. 校验 Token 是否被吊销（Redis 黑名单机制）
        RBucket<String> blacklistBucket = redissonClient.getBucket(RedisKeyConst.TOKEN_BLACKLIST + token);
        if(blacklistBucket.isExists()) {
            throw new AuthenticationException("token已被吊销！");
        }

        // 4. 校验用户是否有效
        UserPrincipal principal = sysUserService.getByUsername(username);
        if(Objects.isNull(principal)) {
            throw new AuthenticationException("用户不存在！");
        }

        // 5. JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
        tokenBucket.expire(jwtUtil.getExpire(), TimeUnit.SECONDS);

        // 6. 返回认证信息（Principal）
        return new SimpleAuthenticationInfo(principal, token, getName());
    }
}
