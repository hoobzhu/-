package com.hoob.rs.comm.service;

import java.io.Serializable;

import com.hoob.rs.comm.model.BaseEntity;


public interface BaseService {


	/**
	 * 新增实体
	 * @param entity
	 */
	public void addBaseEntity(BaseEntity entity);	
	
	/**
	 * 修改实体
	 * @param entity
	 */
	public void updateBaseEntity(BaseEntity entity);
	
	/**
	 * 回收实体（等同修改，但是实体的recycle=1）
	 * @param entity
	 */
	public void recycleBaseEntity(BaseEntity entity);
	
	
	
	/**
	 * 回收实体（等同修改，但是实体的recycle=1）
	 * @param entity
	 * @param includeJunior 回收是否包含下级
	 */
	public void recycleBaseEntity(BaseEntity entity,boolean includeJunior);
	
	
	/**
	 * 还原实体
	 * @param entity	 
	 */
	public void restoreBaseEntity(BaseEntity entity);
	
	
	/**
	 * 还原实体
	 * @param entity
	 * @param includeJunior，true:下级也还原，false：只还原自己
	 */
	public void restoreBaseEntity(BaseEntity entity,boolean includeJunior);
	
	/**
	 * 物理删除
	 * @param cls
	 * @param entityId
	 */
	public void removeBaseEntity(Class<? extends BaseEntity> cls ,Serializable entityId);
	
	
	/**
	 * 物理删除
	 * @param entity
	 */
	public void removeBaseEntity(int mediaType,String contentId,boolean includeJunior);	
	
	
}
