package com.hoob.rs.comm.vo;

import java.io.Serializable;

import com.hoob.rs.utils.JsonUtils;


public class JsonResponse implements Serializable{

    /**
     * 返回客户端统一格式，包括状态码，提示信息，以及业务数据
     */
    private static final long serialVersionUID = 1L;
    //状态码
    private int resultCode;
    //必要的提示信息
    private String description;
    //数据总数
    private Long total;
    //业务数据
    private Object data;
    /**
     * 构造单个数据
     * @param resultCode
     * @param description
     * @param data
     */
    public JsonResponse(int resultCode,String description,Object data){
        this.resultCode = resultCode;
        this.description = description;
        this.data = data;
    }
    /**
     * 构造列表数据
     * @param resultCode
     * @param description
     * @param total
     * @param data
     */
    public JsonResponse(int resultCode,String description,Long total ,Object data){
        this.resultCode = resultCode;
        this.description = description;
        this.total = total;
        this.data = data;
    }
    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
		this.description = description;
	}
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    
    public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	@Override
    public String toString(){
        if(null == this.data){
            this.setData(new Object());
        }
        return JsonUtils.obj2Json(this);
    }
}