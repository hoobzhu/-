/**
 * 
 */
package com.hoob.rs.sys.vo;

import java.util.List;

/**
 * @Description  系统常量value结构体映射
 * @author hoob
 * @date 2018年1月15日下午7:21:48
 */
public class ConfigValueMappingVO {
  private String key;
  private String value;
  private List<String> mappingValues;
public String getKey() {
	return key;
}
public String getValue() {
	return value;
}
public List<String> getMappingValues() {
	return mappingValues;
}
public void setKey(String key) {
	this.key = key;
}
public void setValue(String value) {
	this.value = value;
}
public void setMappingValues(List<String> mappingValues) {
	this.mappingValues = mappingValues;
}

}
