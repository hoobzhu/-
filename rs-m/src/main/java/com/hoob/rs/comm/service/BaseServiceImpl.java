package com.hoob.rs.comm.service;

import java.io.Serializable;








import java.util.Date;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.model.BaseMappingEntity;
import com.hoob.rs.comm.model.BaseMediaEntity;


@Service("baseService")
@Transactional
public class BaseServiceImpl implements BaseService{
	
	final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);	

	
	
	@Resource
	BaseDao baseDao; 
	

	
	

	@Override
	public void addBaseEntity(BaseEntity entity) {
		if(null==entity) {
            return;
        }
		entity.setCreateTime(new Date());
		entity.setUpdateTime(entity.getCreateTime());
		baseDao.save(entity);

	}
	

	@Override
	public void updateBaseEntity(BaseEntity entity) {
		if(null==entity) {
            return;
        }
		baseDao.merge(entity);
		
	}

	@Override
	public void recycleBaseEntity(BaseEntity entity) {
		if(null==entity) {
            return;
        }
		baseDao.merge(entity);
	
	}
	
	@Override
	public void recycleBaseEntity(BaseEntity entity, boolean includeJunior) {
		if(null==entity) {
            return;
        }
		baseDao.merge(entity);
		if(entity instanceof BaseMediaEntity){
		
			
		}else if(entity instanceof BaseMappingEntity){			
			
		}
	}
	
	@Override
    public void restoreBaseEntity(BaseEntity entity){
		if(null==entity) {
            return;
        }
		baseDao.merge(entity);
	}
	
	@Override
	public void restoreBaseEntity(BaseEntity entity, boolean includeJunior) {
		if(null==entity) {
            return;
        }
		baseDao.merge(entity);
	}
	
	
	@Override
	public void removeBaseEntity(Class<? extends BaseEntity> cls, Serializable entityId) {
		if(null==cls || null==entityId) {
            return;
        }
		BaseEntity entity = baseDao.find(cls, entityId);
		if(entity instanceof BaseMediaEntity){
			BaseMediaEntity mEntity = (BaseMediaEntity)entity;
			this.removeBaseEntity(mEntity.getMediaType(), mEntity.getContentId(), true);
		}else if(entity instanceof BaseMappingEntity){
			BaseMappingEntity mEntity = (BaseMappingEntity)entity;
			this.removeBaseEntity(mEntity.getMediaType(), mEntity.getContentId(), true);
		}else{
			baseDao.remove(entity);
		}
	}


	/**
	 * @Title removeBaseEntity
	 * @Description 
	 * @param 
	 * @return BaseService
	 * @throws 
	 */
	@Override
	public void removeBaseEntity(int mediaType, String contentId,
			boolean includeJunior) {
		// TODO Auto-generated method stub
		
	}		

}
