package com.hoob.rs.comm.exception;

/**
 * cms 自定义异常
 * @author ding
 * 20140630
 */
public class CMSException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final int code;
	private final String msg;

	/**
	 * 构造OSS异常对象。
	 * @param code 错误码（参见{@linkplain ErrorCode}）
	 * @param msg 错误信息
	 */
	public CMSException(int code, String msg) {
		super(exceptionMessage(code, msg));
		this.code = code;
		this.msg = msg;
	}
	
	/**
	 * 构造异常对象。
	 * @param code 错误码（参见{@linkplain ErrorCode}）
	 * @param msg 错误信息
	 * @param e 原始异常。
	 */
	public CMSException(int code, String msg, Throwable e) {
		super(exceptionMessage(code, msg), e);
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 获取错误码。
	 * @return 返回错误码。
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 获取错误信息。
	 * @return 返回错误信息。
	 */
	public String getMsg() {
		return msg;
	}
	
	private static String exceptionMessage(int code, String msg) {
		return String.format("CMS Exception: (%1$d) %2$s", code, msg);
	}
}
