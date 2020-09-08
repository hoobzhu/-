package com.hoob.rs.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoob.rs.comm.vo.ListResponse;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.constants.StatusCode;

@RestController
@RequestMapping(path = "/v1/sys/const")
public class ConstController {

	@GetMapping(path="/list")
	public ListResponse<Map<String,Object>> listConst(@RequestParam(name="key")String key){
		
		
		
		try {
			
			Class<?> clz = Class.forName("com.fonsview.sso.constants."+key);
			if(!(clz.isEnum())){
				ListResponse<Map<String,Object>> res = new ListResponse<Map<String,Object>>();
				res.setResultCode(StatusCode.UI.UI_1);
				res.setDescription("const["+key+"] is not exists");
				return res;
			}
			
			Enum<?>[] enums = (Enum[]) clz.getEnumConstants();
			List<Map<String,Object>> enumList = new ArrayList<Map<String,Object>>();
			for(Enum<?> e:enums){
				Map<String,Object> enumMap = new HashMap<String, Object>();
				enumMap.put("label", e.name());
				enumMap.put("value", ReflectionUtils.invokeMethod(e.getClass().getMethod("getValue"), e));
				enumList.add(enumMap);
			}
			
			ListResponse<Map<String,Object>> res = new ListResponse<Map<String,Object>>();
			res.setResultCode(StatusCode.UI.UI_0);
			res.setList(enumList);
			return res;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ListResponse<Map<String,Object>> res = new ListResponse<Map<String,Object>>();
			res.setResultCode(StatusCode.UI.UI_1);
			res.setDescription("const["+key+"] is not exists");
			return res;
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			ListResponse<Map<String,Object>> res = new ListResponse<Map<String,Object>>();
			res.setResultCode(StatusCode.UI.UI_1);
			res.setDescription("const["+key+"] is not support getValue");
			return res;
		} 
		
	}
	
}
