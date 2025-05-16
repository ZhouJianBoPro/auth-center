package com.auth.center.util;

import com.alibaba.fastjson.JSON;
import com.auth.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/15 15:53
 **/
@Slf4j
public class WebUtils {

    public static void writeJsonResponse(ServletResponse response, HttpStatus httpStatus) {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(200);

        ResultVO  resultVO = ResultVO.error(httpStatus.value(), httpStatus.getReasonPhrase());
        try {
            httpResponse.getWriter().write(JSON.toJSONString(resultVO));
        } catch (IOException e) {
            log.error("writeJsonResponse error", e);
        }
    }
}
