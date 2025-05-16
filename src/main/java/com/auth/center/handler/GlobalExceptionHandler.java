package com.auth.center.handler;

import com.auth.center.exception.CustomException;
import com.auth.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，只能捕捉Controller层抛出的异常
 * 
 * @Author yoke
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResultVO<?> handleCustomException(CustomException e){
		log.error("自定义异常", e);
		return ResultVO.error(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResultVO<?> handleException(Exception e){
		log.error("系统处理异常", e);
		return ResultVO.error();
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResultVO<?> unauthorizedException(UnauthorizedException e){
		log.error("授权信息校验失败", e);
		return ResultVO.error(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
	}

}
