package com.auth.center.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *   接口返回数据格式
 * @author yoke
 * @email zoloos@163.com
 * @date  2019年1月19日
 */
@Data
public class ResultVO<T> implements Serializable {


	private static Integer SC_OK_200 = 200;

	private static Integer SERVER_ERROR_500 = 500;

	/**
	 * 成功标志
	 */
	private boolean success = true;



	/**
	 * 返回处理消息
	 */
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	private Integer code = 0;
	
	/**
	 * 返回数据对象 data
	 */
	private T ResultVO;
	
	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	public ResultVO() {}
	
	public ResultVO<T> success(String message) {
		this.message = message;
		this.code = SC_OK_200;
		this.success = true;
		return this;
	}

	
	public static ResultVO<Object> ok() {
		ResultVO<Object> r = new ResultVO<Object>();
		r.setSuccess(true);
		r.setCode(SC_OK_200);
		r.setMessage("成功");
		return r;
	}
	
	public static <T> ResultVO<T> ok(String msg) {
		ResultVO<T> r = new ResultVO<>();
		r.setSuccess(true);
		r.setCode(SC_OK_200);
		r.setMessage(msg);
		return r;
	}
	
	public static <T> ResultVO<T> ok(T data) {
		ResultVO<T> r = new ResultVO<>();
		r.setSuccess(true);
		r.setCode(SC_OK_200);
		r.setResultVO(data);
		return r;
	}
	
	public static <T> ResultVO<T> error() {
		return error("处理失败");
	}

	public static <T> ResultVO<T> error(String msg) {
		return error(SERVER_ERROR_500, msg);
	}
	
	public static <T> ResultVO<T> error(int code, String msg) {
		ResultVO<T> r = new ResultVO<>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}
}