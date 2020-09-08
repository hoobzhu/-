package com.hoob.rs.comm.exception;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常增强，以JSON的形式返回给客服端
 * 异常增强类型：NullPointerException,RunTimeException,ClassCastException,
　　　　　　　　 NoSuchMethodException,IOException,IndexOutOfBoundsException
　　　　　　　　 以及springmvc自定义异常等，如下：
SpringMVC自定义异常对应的status code  

           Exception                       HTTP Status Code  
ConversionNotSupportedException         500 (Internal Server Error)
HttpMessageNotWritableException         500 (Internal Server Error)
HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
NoSuchRequestHandlingMethodException    404 (Not Found) 
TypeMismatchException                   400 (Bad Request)
HttpMessageNotReadableException         400 (Bad Request)
MissingServletRequestParameterException 400 (Bad Request)
 *
 */
@RestControllerAdvice
public class RestExceptionHandler{
	
	private Logger logger = LogManager.getLogger(RestExceptionHandler.class);
	
    //运行时异常
    @ExceptionHandler(RuntimeException.class)  
    @ResponseBody  
    public String runtimeExceptionHandler(RuntimeException ex) { 
    	logger.error("[50000] Runtime exception ...",ex);
        return ReturnFormat.restParam(50000, null);
    }  

    //空指针异常
    @ExceptionHandler(NullPointerException.class)  
    @ResponseBody  
    public String nullPointerExceptionHandler(NullPointerException ex) {  
        logger.error("[50001] Null Pointer Exception ...",ex);
        return ReturnFormat.restParam(50001, null);
    }   
    //类型转换异常
    @ExceptionHandler(ClassCastException.class)  
    @ResponseBody  
    public String classCastExceptionHandler(ClassCastException ex) {  
        logger.error("[50002] Class Cast Exception ...",ex);
        return ReturnFormat.restParam(50002, null);  
    }  

    //IO异常
    @ExceptionHandler(IOException.class)  
    @ResponseBody  
    public String iOExceptionHandler(IOException ex) {  
        logger.error("[50003] IO Exception ...",ex);
        return ReturnFormat.restParam(50003, null); 
    }  
    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)  
    @ResponseBody  
    public String noSuchMethodExceptionHandler(NoSuchMethodException ex) {  
    	logger.error("[50004] No Such Method Exception",ex);
        return ReturnFormat.restParam(50004, null);
    }  

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)  
    @ResponseBody  
    public String indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) { 
    	logger.error("[50005] Index Out Of Bounds Exception",ex);
        return ReturnFormat.restParam(50005, null);
    }
    
    //数据库异常
    @ExceptionHandler(SQLException.class)  
    @ResponseBody  
    public String sqlExceptionHandler(SQLException ex) { 
    	logger.error("[50007] SQL Exception",ex);
        return ReturnFormat.restParam(50007, null);
    }
    
    
    //数学运算异常
    @ExceptionHandler(ArithmeticException.class)  
    @ResponseBody  
    public String arithmeticExceptionHandler(ArithmeticException ex) { 
    	logger.error("[50008] Arithmetic Exception",ex);
        return ReturnFormat.restParam(50008, null);
    }
    
    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public String requestNotReadable(HttpMessageNotReadableException ex){
        logger.error("[400] Http Message Not Readable Exception",ex);
        return ReturnFormat.restParam(400, null);
    }
    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public String requestTypeMismatch(TypeMismatchException ex){
    	logger.error("[400] Type Mismatch Exception",ex);
        return ReturnFormat.restParam(400, null);
    }
    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public String requestMissingServletRequest(MissingServletRequestParameterException ex){
    	logger.error("[400] Missing Servlet Request Parameter Exception",ex);
        return ReturnFormat.restParam(400, null);
    }
    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public String request405(HttpRequestMethodNotSupportedException ex){
    	logger.error("[405] Http Request Method Not Supported Exception",ex);
        return ReturnFormat.restParam(405, null);
    }
    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public String request406(HttpMediaTypeNotAcceptableException ex){
    	logger.error("[406] Http Media Type Not Acceptable Exception",ex);
        return ReturnFormat.restParam(406, null);
    }
    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class,HttpMessageNotWritableException.class})
    @ResponseBody
    public String server500(RuntimeException ex){
    	logger.error("[500] Runtime Exception",ex);
        return ReturnFormat.restParam(500, null);
    }
}