package com.auth.center.filter;

import com.auth.center.model.JwtToken;
import com.auth.center.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Desc: 鉴权登录拦截器
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/14 18:36
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    public static final String HEADER = "Authorization";

    /**
     * shiroFilter中的异常不能通过@RestControllerAdvice处理，因此将返回信息直接写入到httpResponse中，但是授权校验信息能够被全局异常拦截处理
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(HEADER);

        try {
            // 如果请求头没有token，则可能是匿名访问
            if (token == null) {
                throw new AuthenticationException("token为空，无法匿名访问");
            }

            // 执行登录
            return executeLogin(request, response);
        } catch (AuthenticationException e) {
            log.error("认证失败", e);
            WebUtils.writeJsonResponse(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("JwtFilter isAccessAllowed error", e);
            WebUtils.writeJsonResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return false;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(HEADER);

        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误会抛出异常并被捕获
        Subject subject = getSubject(request, response);
        subject.login(jwtToken);
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
