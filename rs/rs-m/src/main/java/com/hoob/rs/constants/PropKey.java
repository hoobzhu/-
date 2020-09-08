/**
 * 
 */
package com.hoob.rs.constants;

/**
 * cms-config.properties属性KEY
 * @author Raul	
 * 2017年8月25日
 */
public enum PropKey {

	Key_1("com.abc"),
	Key_2("com.abc2"),
	Key_3("com.abc3");
		
	
	private String key;
	private PropKey(String key){
		this.key = key;
	}	
	public String getKey(){
		return this.key;
	}
	
}
