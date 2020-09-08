/**
 * 
 */
package com.hoob.rs.sys.vo;

import java.util.HashMap;
import java.util.Map;

import com.hoob.rs.comm.vo.Response;

/**
 * @author Raul	
 * 2017年9月25日
 */
public class FileUploadResponse extends Response{
	/****/
	public FileUploadResponse(int resultCode){
		super(resultCode);
	}
	
	private Map<String,String>  uploadResult = new HashMap<String,String>();

	public Map<String, String> getUploadResult() {
		return uploadResult;
	}

	public void setUploadResult(Map<String, String> uploadResult) {
		this.uploadResult = uploadResult;
	}		

	
	
}
