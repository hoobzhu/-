/**
 * 
 */
package com.hoob.rs.sys.service;

/**
 * @author Raul	
 * 2017年8月28日
 */
public interface SystemInitService{

	public void initSystemDB();
	
	public void loadSystemProperties();
	
	public void initTimer();
}
