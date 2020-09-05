package com.hoob.rs.comm.exception;

import java.util.HashMap;
import java.util.Map;

import com.hoob.rs.comm.vo.JsonResponse;


//格式化返回客户端数据格式（json）
public class ReturnFormat {
    private static Map<String,String> messageMap = new HashMap<String,String>();
    //初始化状态码与文字说明
    static {
        messageMap.put("0", "Success");
        messageMap.put("-1", "Failure");
        messageMap.put("-2", "Operate Failure");//自定义的操作失败类型
        messageMap.put("400", "Bad Request!");
        messageMap.put("401", "Not Authorization");
        messageMap.put("405", "Method Not Allowed");
        messageMap.put("406", "Not Acceptable");
        messageMap.put("500", "Internal Server Error");

        messageMap.put("50000", "[Service] Runtime Exception");
        messageMap.put("50001", "[Service] Null Pointer Exception");
        messageMap.put("50002", "[Service] Class Cast Exception");
        messageMap.put("50003", "[Service] IO Exception");
        messageMap.put("50004", "[Service] No Such Method Exception");
        messageMap.put("50005", "[Service] Index Out Of Bounds Exception");
        messageMap.put("50006", "[Service] Network Exception");
        messageMap.put("50007", "[Service] SQL Exception");
        messageMap.put("50008", "[Service] Arithmetic Exception");

    }
    public static String restParam(int status,Object data) {
    	JsonResponse json = new JsonResponse(status, messageMap.get(String.valueOf(status)), data);
        return json.toString();
    }
    
    public static String restParam(int status,Long total,Object data) {
    	JsonResponse json = new JsonResponse(status, messageMap.get(String.valueOf(status)), total,data);
        return json.toString();
    }
}