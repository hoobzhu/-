package com.hoob.rs.constants;

public class Constants {

	    // 成功
		public static final int SUCCESS = 0;
		// 部分成功
		public static final int PART_SUCCESS = 1;
		// 失败
		public static final int FAILURE = -1;
		// UserToken无效
		public static final int USERTOKEN_INVALID = 2011;
		// UserToken已经过期
		public static final int USERTOKEN_EXPIRED = 2012;
		// 认证失败
		public static final int VERIFY_FAILURE = 2201;
		// 无认证字符串
		public static final int NO_VERIFY_STRING = 2205;
		// 无认证字符串
		public static final int REQUIRED_PARAMETER_MISSING = 4001;
		public static final String REQUIRED_PARAMETER_MISSING_MSG = "Required parameter is missing from request.";
		// 服务端异常
		public static final int SERVER_EXCEPTION = 5000;
		
		
		public static final String R_SUCCESS="SUCCESS";
		public static final String R_FAIL="FAIL";
}
