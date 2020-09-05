/**
 * 
 */
package com.hoob.rs.comm.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

/**
 * @author Raul	
 * 2017年5月19日
 */
public interface BaseDao {

	public <T> void save(T entity);

    public <T> void flush();
	
	public <T> void saveAll(Iterable<T> entities);
	   
    public <T> T merge(T entity);
   
    public <T> void remove(Class<T> clazz,Serializable... entityIds);
    
    public <T> void removeAll(Iterable<T> entities);  
    
    public void remove(String jpql);
   
    public <T> void remove(T entity);  
    
    public <T> T find(Class<T> clazz,Serializable entityId);
    
    public <T> T findOne(String jpql,HashMap<String,Object> params);

    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult, String whereSql, Object[] params, HashMap<String, String> orderBy);
    
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult, HashMap<String, String> orderBy);
   
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult, String whereSql, Object[] params);
   
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult);
   
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, String whereSql, Object[] params, HashMap<String, String> orderBy);
   
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(Class<T> clazz, String whereSql, Object[] params);
   
    public <T> Object queryForProperty(Class<T> clazz,String property, Serializable entityId);
    
    public <T> T get(CharSequence queryString,HashMap<String,Object>params);
    
    public <T> com.hoob.rs.comm.dao.QueryResult<T> query(CharSequence queryString, HashMap<String,Object>params, int firstResult, int maxResult);

	/**
	 * 只查询结果集
	 * 
	 * 区别于<T> QueryResult<T> query(CharSequence
	 * queryString,HashMap<String,Object>params,int firstResult, int maxResult)
	 * 
	 * 部分场景统计totalCount无意义，只需调用该接口即可
	 * 
	 * @param queryString
	 * @param params
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public <T> List<T> queryOnlyResults(CharSequence queryString, HashMap<String, Object> params,
			int firstResult, int maxResult);

    public int execNativeUpdate(CharSequence queryString, HashMap<String, Object> params);

    public <T> List<T> execNativeQueryList(Class<T> clazz, CharSequence queryString, HashMap<String, Object> params, int firstResult, int maxResult);

    public <T> com.hoob.rs.comm.dao.QueryResult<T> execNativeQuery(Class<T> clazz, CharSequence queryString, HashMap<String, Object> params, int firstResult, int maxResult);

    Query createQuery(String baseHql, String[] queryConditionsHql, Object[] queryParams, Integer first,
			Integer max, String[] orderBys);
    public <T> void clear();

}
