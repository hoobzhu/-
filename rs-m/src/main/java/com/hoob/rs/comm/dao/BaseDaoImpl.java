/**
 * 
 */
package com.hoob.rs.comm.dao;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Raul 2017年5月19日
 */
@Repository("baseDao")
@Transactional(rollbackFor = Exception.class)
public class BaseDaoImpl implements BaseDao {

	static Log log = LogFactory.getLog(BaseDaoImpl.class);

	@PersistenceContext
	protected EntityManager em;	
	
	@Override
	public <T> void save(T entity) {
		if (null != em) {
			em.persist(entity);
		}
	}

	@Override
	public <T> void flush() {
		if (null != em) {
			em.flush();
		}
	}

	@Override
	public <T> T merge(T entity) {
		if (null != em) {
			return em.merge(entity);
		} else {
			return null;
		}
	}

	@Override
	public <T> void remove(Class<T> clazz, Serializable... entityIds) {
		for (Serializable entityId : entityIds) {
			em.remove(em.getReference(clazz, entityId));
		}
	}

	@Override
	public <T> T find(Class<T> clazz, Serializable entityId) {
		return em.find(clazz, entityId);
	}

	@Override
	public <T> T findOne(String jpql,HashMap<String,Object> params) {		
		QueryResult<T> result = this.query(jpql, params, -1, -1);
		if(null!=result && result.getCount() > 0 ){
			return result.getResults().get(0);
		}else{
			return null;
		}		
	}
	
	@Override
	public <T> QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult, String whereSql, Object[] params,
			HashMap<String, String> orderBy) {
		StringBuilder sb = new StringBuilder("");
		// 拼凑where语句
		if (null != whereSql && !"".equals(whereSql.trim())) {
			sb.append(" where 1=1 ").append(whereSql.trim()).append(" ");
		}
		// 拼凑orderBy语句
		if (null != orderBy && !orderBy.isEmpty()) {
			sb.append(" order by ");
			for (String key : orderBy.keySet()) {
				sb.append("o.").append(key).append(" ").append(orderBy.get(key)).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		List<T> results = null;
		long count = 0L;

		String jpql = "select o from " + clazz.getSimpleName() + " o ";
		log.debug("jpql---->" + jpql + sb.toString());

		Query query = em.createQuery(jpql + sb.toString(), clazz);
		// 设置参数
		setParameters(query, params);
		if (firstResult > -1 && maxResult > -1) {
            query.setFirstResult(firstResult).setMaxResults(maxResult);
        }
		results = query.getResultList();
		query = em.createQuery("select count(o) from " + clazz.getSimpleName() + " o " + sb.toString());
		setParameters(query, params);
		count = (Long) query.getSingleResult();
		return new QueryResult<T>(results, count);
	}
	

	

	@Override
	public <T> QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult,
			HashMap<String, String> orderBy) {
		return query(clazz, firstResult, maxResult, null, null, orderBy);
	}

	@Override
	public <T> QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult, String whereSql, Object[] params) {
		return query(clazz, firstResult, maxResult, whereSql, params, null);
	}

	@Override
	public <T> QueryResult<T> query(Class<T> clazz, int firstResult, int maxResult) {
		return query(clazz, firstResult, maxResult, null, null, null);
	}

	@Override
	public <T> QueryResult<T> query(Class<T> clazz, String whereSql, Object[] params,HashMap<String, String> orderBy) {
		return query(clazz, -1, -1, whereSql, params, orderBy);
	}

	@Override
	public <T> QueryResult<T> query(Class<T> clazz, String whereSql, Object[] params) {
		return query(clazz, -1, -1, whereSql, params, null);
	}

	@Override
	public <T> Object queryForProperty(Class<T> clazz, String property, Serializable entityId) {
		String jpql = "select o." + property + " from " + getEntityName(clazz) + " o where o." + getEntityId(clazz)
				+ "=?1";
		log.debug("jpql----->" + jpql);
		Query query = em.createQuery(jpql);
		query.setParameter(1, entityId);
		Object result = null;
		try {
			result = query.getSingleResult();
		} catch (RuntimeException e) {
		}
		return result;
	}

	private <T> String getEntityId(Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Id id = null;
		String entityId = null;
		// 先看看字段有没有注解Id
		for (Field field : fields) {
			id = field.getAnnotation(Id.class);
			if (null != id) {
				entityId = field.getName();
				break;
			}
		}
		// 如果字段上没有注解，则在getter方法上面找.
		if (null == id) {
			try {
				PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
				Method readMethod = null;
				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					readMethod = propertyDescriptor.getReadMethod();
					id = readMethod.getAnnotation(Id.class);
					if (null != id) {
						entityId = propertyDescriptor.getName();
						break;
					}
				}
			} catch (IntrospectionException e) {
				throw new RuntimeException(e);
			}
		}

		if (entityId == null) {
            throw new RuntimeException("no primary key!!!");
        }

		return entityId;
	}

	private <T> String getEntityName(Class<T> clazz) {
		String entityName = clazz.getSimpleName();
		Entity entity = clazz.getAnnotation(Entity.class);
		String name = null;
		if (null != entity) {
			name = entity.name();
		}
		return null == name || "".equals(name.trim()) ? entityName : name.trim();
	}

	@Override
	public <T> void saveAll(Iterable<T> entities) {
		for (Iterator<T> localIterator = entities.iterator(); localIterator.hasNext();) {
			Object entity = localIterator.next();
			save(entity);
		}
	}

	@Override
	public <T> void removeAll(Iterable<T> entities) {
		for (Iterator<T> localIterator = entities.iterator(); localIterator.hasNext();) {
			Object entity = localIterator.next();
			em.remove(entity);			
		}
	}

	protected boolean isAlpha(char c) {  
        return ((c == '_') || (('0' <= c) && (c <= '9')) || (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z')));  
    }  
	
	protected boolean followWithWord(String s, String sub, int pos) {  
        int i = 0;  
        for (; (pos < s.length()) && (i < sub.length()); ++i) {  
            if (s.charAt(pos) != sub.charAt(i)) {
				return false;
			}
            ++pos;
        }  
  
        if (i < sub.length()) {  
            return false;  
        }  
  
        if (pos >= s.length()) {  
            return true;  
        }  
        return (!(isAlpha(s.charAt(pos))));  
    }  
	
	
	protected String parseSelectCount(String queryString) {  
        String hql = queryString.toLowerCase();  
        int noBlankStart = 0;  
        for (int len = hql.length(); noBlankStart < len; ++noBlankStart) {  
            if (hql.charAt(noBlankStart) > ' ') {  
                break;  
            }  
        }  
  
        int pair = 0;  
  
        if (!(followWithWord(hql, "select", noBlankStart))) {  
            pair = 1;  
        }  
        int fromPos = -1;  
        for (int i = noBlankStart; i < hql.length();) {  
            if (followWithWord(hql, "select", i)) {  
                ++pair;  
                i += "select".length();  
            } else if (followWithWord(hql, "from", i)) {  
                --pair;  
                if (pair == 0) {  
                    fromPos = i;  
                    break;  
                }  
                i += "from".length();  
            } else {  
                ++i;  
            }  
        }  
        if (fromPos == -1) {  
            throw new IllegalArgumentException("parse count sql error, check your sql/hql");  
        }  
  
        String countHql = "select count(*) " + queryString.substring(fromPos);  
        return countHql;  
    }  

	@Override
	public <T> QueryResult<T> query(CharSequence queryString, HashMap<String, Object> params,int firstResult, int maxResult) {
		List<T> results = queryOnlyResults(queryString, params, firstResult, maxResult);
		long count = 0L;  
		  
        if ((firstResult >= 0) && (maxResult > 0)) {
            String jpql = parseSelectCount(queryString.toString());  
			count = ((Long) get(jpql, params)).longValue();
        } else {  
        	count = results.size();  
        }  	          
        return new QueryResult<T>(results, count);
	}

	@Override
	public <T> List<T> queryOnlyResults(CharSequence queryString, HashMap<String, Object> params, int firstResult,
			int maxResult) {
		Query query = em.createQuery(queryString.toString());

		// 设置参数
		setParameters(query, params);
		if (firstResult > -1 && maxResult > -1) {
			query.setFirstResult(firstResult).setMaxResults(maxResult);
		}
		return query.getResultList();
	}

	private void setParameters(Query query, Object[] params) {
		if(null==params) {
            return;
        }
		if (null != query && null != params && params.length > 0) {
			for (int i = 1; i <= params.length; i++) {
				query.setParameter(i, params[i - 1]);
			}
		}
	}

	private void setParameters(Query query, HashMap<String, Object> parameterMap) {  
		if(null==parameterMap) {
            return;
        }
        for (@SuppressWarnings("rawtypes")  
        Iterator<String> iterator = parameterMap.keySet().iterator(); iterator.hasNext();) {  
            String key = iterator.next();
			query.setParameter(key, parameterMap.get(key));
        }  
    }

	@Override
	public <T> T get(CharSequence queryString, HashMap<String, Object> params) {
		Query query = em.createQuery(queryString.toString());  
		 
		// 设置参数
		setParameters(query, params);
		List<T> list = query.getResultList();
		if(null!=list && list.size() > 0) {
            return (T)list.get(0);
        }
		return null;
	}

	@Override
	public int execNativeUpdate(CharSequence queryString, HashMap<String, Object> params) {
		if(null==queryString  || StringUtils.isAllBlank(queryString)) {
            return 0;
        }
		Query query = em.createNativeQuery(queryString.toString());
		setParameters(query, params);
//		em.joinTransaction();
		return query.executeUpdate();	
	}

	@Override
	public <T> List<T> execNativeQueryList(Class<T> clazz, CharSequence queryString, HashMap<String, Object> params, int firstResult, int maxResult) {
        Query query = null;
	    if (clazz != null) {
            query = em.createNativeQuery(queryString.toString(), clazz);
        } else {
            query = em.createNativeQuery(queryString.toString());
        }

		setParameters(query, params);

		if (firstResult > -1 && maxResult > -1) {
			query.setFirstResult(firstResult).setMaxResults(maxResult);
		}
		List<T> results = query.getResultList();
		return results;
	}

	/**
	 * 原生SQL
	 * @param queryString
	 * @param params
	 * @param firstResult
	 * @param maxResult
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> QueryResult<T> execNativeQuery(Class<T> clazz, CharSequence queryString, HashMap<String, Object> params, int firstResult, int maxResult) {
		List<T> results = execNativeQueryList(clazz, queryString, params, firstResult, maxResult);

		long count = 0L;
		if ((firstResult >= 0) && (maxResult > 0)) {
			String jpql = parseSelectCount(queryString.toString());
			Query countQuery = em.createNativeQuery(jpql);
			setParameters(countQuery, params);
			List<T> list = countQuery.getResultList();
			if(null!=list && list.size() > 0) {
				count = Long.valueOf(list.get(0)+"");
			}
		} else {
			count = results.size();
		}
		return new QueryResult<T>(results, count);
	}


	/**
	 * 
	 * @param baseHql
	 * @param queryConditionsHql
	 * @param queryParams
	 * @param orderBys
	 * @return
	 */
	@Override
	public Query createQuery(String baseHql, String[] queryConditionsHql, Object[] queryParams, Integer first,
			Integer max, String[] orderBys) {
		StringBuilder finalHqlSb = new StringBuilder();
		finalHqlSb.append(baseHql);
		
		HashMap<String, Object> queryParamMap = new HashMap<String, Object>();
		//拼接查询条件
		if (queryConditionsHql != null && queryConditionsHql.length > 0) {
			finalHqlSb.append(" WHERE 1 = 1 ");
			Map<String, String> queryConditionMap = new HashMap<String, String>();
			for (int i = 0; i < queryConditionsHql.length; i++) {
				String queryCondition = queryConditionsHql[i];
				if(StringUtils.isNotEmpty(queryCondition)){
					String[] arr = queryCondition.split(":");
					if(arr.length>=3){//两个以上的参数
						for (int j = 1; j < arr.length; j++) {
							String key = arr[j].substring(0, arr[j].indexOf(" "));
							queryConditionMap.put(key, queryCondition);
						}
					}else{
						String key = StringUtils.substringBetween(queryCondition, ":", " ");
						queryConditionMap.put(key, queryCondition);
					}
				}
			}
			for (int i = 0; i < queryParams.length; i += 2) {
				String key = (String) queryParams[i];
				Object value = queryParams[i + 1];
				String queryCondition = queryConditionMap.get(key);
				if (value != null && queryCondition != null ) {
					queryParamMap.put(key, value);
					if(finalHqlSb.indexOf(queryCondition)==-1){
						finalHqlSb.append(" AND ");
						finalHqlSb.append(queryCondition);
					}
					
				}

			}

		} else {
			for (int i = 0; i < queryParams.length; i += 2) {
				String key = (String) queryParams[i];
				Object value = queryParams[i + 1];
				if (value != null) {
					queryParamMap.put(key, value);
				}

			}

		}
		
		// 拼凑orderBy语句
		if (null != orderBys && orderBys.length > 0) {
			finalHqlSb.append(" ORDER BY ");
			for (int i = 0; i < orderBys.length; i += 2) {
				String orderKey = orderBys[i];
				String orderType = orderBys[i + 1];

				if (orderKey != null && orderType != null) {
					finalHqlSb.append(orderKey).append(" ").append(orderType).append(",");
				}

			}
			finalHqlSb.deleteCharAt(finalHqlSb.length() - 1);
		}

		String finalHql = finalHqlSb.toString();
		Query query = em.createQuery(finalHql);
		if(first!=null){
			query.setFirstResult(first);
		}
		if(max!=null){
			query.setMaxResults(max);
		}
		setParameters(query, queryParamMap);
		return query;
	}

	@Override
	public <T> void remove(T entity) {
		em.remove(entity);
	}

	@Override
	public void remove(String jpql) {
		if(StringUtils.isEmpty(jpql)) {
            return;
        }
		Query query = em.createQuery(jpql);
		query.executeUpdate();		
	}

	@Override
	public <T> void clear() {
		if (null != em) {
			em.clear();
		}
	}

}
