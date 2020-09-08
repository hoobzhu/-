package com.hoob.rs.sys.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
@Aspect
public class ControllerAspect  {
	private String requestPath = null; // 请求地址
	private Map<String, ?> inputParamMap = null; // 传入参数
	private Map<String, Object> outputParamMap = null; // 存放输出结果
	private String method ="";
	
	
	/**
	 * @Title：doBefore
	 * @Description: 方法调用前触发 备用
	 * @author hoob
	 * @date 2017年9月18日下午12:23:39
	 * @param joinPoint
	 */
	public void doBefore(JoinPoint joinPoint) {
	

	}

	/**
	 * @Title：doAfter
	 * @Description: 方法调用后触发  备用
	 * @author hoob
	 * @date 2017年9月18日下午12:23:39
	 * @param joinPoint
	 */
	public void doAfter(JoinPoint joinPoint) {
	
	}

	/**
	 * @Title：doAround
	 * @Description: 环绕触发
	 * @author hoob
	 * @date 2017年9月18日下午12:23:39
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
    @Around("execution(* com.fonsview.sso.*.controller.*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		// 获取输入参数
		inputParamMap = request.getParameterMap();
		// 获取请求地址
		requestPath = request.getRequestURI();
		method =  request.getMethod();
		// 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
		outputParamMap = new HashMap<String, Object>();
		Object result = pjp.proceed();// result的值就是被拦截方法的返回值
		outputParamMap.put("result", result);
		request.setAttribute("result",result);//把结果写入到request中
		return result;
	}

	/***/
	public JoinPoint afterReturning(JoinPoint joinpoint, Object returnValue) {

		// 此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
		Object[] args = joinpoint.getArgs();
		if (args != null) {
			for (Object obj : args) {
				System.out.println(obj);
			}
			
		}
		System.out.println("方法调用成功后返回结果：" + returnValue);
		return joinpoint;
	}
    /***/
	public JoinPoint afterThrowing(JoinPoint joinpoint) {
		joinpoint.getArgs();// 此方法返回的是一个数组，数组中包括request等类对象
		return joinpoint;
	}
	
}
