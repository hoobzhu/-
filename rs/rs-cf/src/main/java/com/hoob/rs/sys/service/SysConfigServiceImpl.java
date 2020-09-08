/**
 * 
 */
package com.hoob.rs.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.sys.dao.SysDao;
import com.hoob.rs.sys.model.SysConfig;
import com.hoob.rs.sys.vo.SysConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.sys.constant.ConfigKey;
import com.hoob.rs.sys.constant.SysConfigConstant;
import com.hoob.rs.sys.dao.SysDao;
import com.hoob.rs.sys.model.SysConfig;
import com.hoob.rs.sys.vo.SysConfigVO;
import com.hoob.rs.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * @author Raul	
 * 2017年9月6日
 */
@Service
@Transactional
public class SysConfigServiceImpl extends BaseServiceImpl implements SysConfigService {

	Logger logger = LogManager.getLogger(SysConfigService.class);

	private static final Map<String, SysConfig> SYS_CONFIG_CACHE = new HashMap<String, SysConfig>();

	// 读写锁
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Resource
    SysDao sysDao;


	@Override
	public SysConfig find(long id) {
		lock.readLock().lock();
		try{		
			for (SysConfig sysConfig : listAll()) {
				if (sysConfig.getId() == id) {
					return sysConfig;
				}
			}
			return null;	
		}finally{
			lock.readLock().unlock();
		}		
	}


	@Override
	public SysConfig find(ConfigKey key) {
		lock.readLock().lock();
		try {
			return SYS_CONFIG_CACHE.get(key.name()); 
		} finally {
			lock.readLock().unlock();
		}
	}


	@Override
	public boolean isEnable(ConfigKey key) {
		SysConfig config = this.find(key);
		if (config != null) {	
			return config.getEnable();
		}
		return false;
	}


	@Override
	public <T> T getSysConfig(ConfigKey key, Class<T> clazz) {
		SysConfig config = this.find(key);
		if (config != null && StringUtils.isNotBlank(config.getValue())) {		
			try {
				return JsonUtils.json2Obj(config.getValue(), clazz);
			} catch (Exception e) {
				logger.error("parse Json error! message is: {}, key = {}, value = {}", e.getMessage(), key,
						config.getValue());
			}
		}
		return null;
	}


	@Override
	public <T> List<T> getSysConfigList(ConfigKey key, Class<T> clazz) {
		SysConfig config = this.find(key);
		if (config != null && StringUtils.isNotBlank(config.getValue())) {
			try {
				return JsonUtils.parseValueList(config.getValue(), clazz);
			} catch (Exception e) {
				logger.error("parse Json error! message is: {}, key = {}, value = {}", e.getMessage(), key,
						config.getValue());
			}
		}		
		return new ArrayList<T>();
	}


	@Override
	public void save(SysConfig sysConfig) {
		try{
			//	sysDao.save(sysConfig);
			sysDao.merge(sysConfig);
			this.reloadCache();
		}catch(Exception ex){
			logger.error(ex);			
		}	
	}


	@Override
	public void batchSave(List<SysConfig> sysConfigList) {
		try{
			sysDao.saveAll(sysConfigList);
			this.reloadCache();
		}catch(Exception ex){
			logger.error(ex);			
		}	
	}


	@Override
	public List<SysConfig> listAll() {
		lock.readLock().lock();
		try {
			List<SysConfig> sysConfigList = new ArrayList<SysConfig>();			
			for (SysConfig sysConfig : SYS_CONFIG_CACHE.values()) {
				sysConfigList.add(sysConfig);
			}			
			return sysConfigList;
		} finally {
			lock.readLock().unlock();
		}
	}


	@Override
	public <T> void save(ConfigKey key, T valueConfig) {
		lock.writeLock().lock();
		try {			
			SysConfig config = this.find(key);
			if (config != null) {				
				config.setValue(JsonUtils.obj2Json(valueConfig));
				this.save(config);
			} else {
				logger.warn("SysConfig is null - key = " + key +", valueConfig = " + valueConfig);
			}
		} finally {
			lock.writeLock().unlock();
		}

	}


	@Override
	public <T> void save(ConfigKey key, List<T> valueConfigList) {
		lock.writeLock().lock();
		try {

			SysConfig config = this.find(key);
			if (config != null) {
				config.setValue(JsonUtils.obj2Json(valueConfigList));
				this.save(config);
			} else {
				logger.error("!!!!!!!Not found SysConfig by key : " + key.name());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}


	@Override
	public void reloadCache() {
		lock.writeLock().lock();
		try{		
			SYS_CONFIG_CACHE.clear();
			List<SysConfig> sysConfigList = sysDao.listAll();
			if(null==sysConfigList) {
                return;
            }
			for (SysConfig sysConfig : sysConfigList) {				
				SYS_CONFIG_CACHE.put(sysConfig.getKey(), sysConfig);
			}			
		}finally{
			lock.writeLock().unlock();
		}		
	}

	@Override
	public SysConfigVO findConfig(long id) {
		try {
			SysConfig sysConfig = find(id);
			SysConfigVO sysConfigVo = new SysConfigVO();
			BeanUtils.copyProperties(sysConfig, sysConfigVo);
			return sysConfigVo;
		} catch (Exception e) {
			throw new RuntimeException("find sysConfig fail for id:" + id, e);
		}
	}

	@Override
	public List<SysConfigVO> getList(String type) {
		List<SysConfig> sysConfigList = null;
		if (StringUtils.isNotBlank(type)) {
			if (SysConfigConstant.SYS_CONFIG_TYPE_CONFIG.equals(type)) {
				sysConfigList = listConfig();
			} else if (SysConfigConstant.SYS_CONFIG_TYPE_CONSTANT.equals(type)) {
				SysConfigConstant.SYS_CONFIG_TYPE_CONSTANT.equals(type);
				sysConfigList = listConstant();
			}
		} else {
			return null;
		}
		List<SysConfigVO> sysConfigVoList = new ArrayList<SysConfigVO>();
		if (null == sysConfigList || sysConfigList.isEmpty()) {
			return null;
		}
		for (SysConfig sysConfig : sysConfigList) {
			SysConfigVO sysConfigVo = new SysConfigVO();
			BeanUtils.copyProperties(sysConfig, sysConfigVo);
			sysConfigVoList.add(sysConfigVo);
		}

		return sysConfigVoList;
	}

	@Override
	public void updateSysConfig(SysConfigVO sysConfigVo) {
		lock.writeLock().lock();
		try {
			SysConfig sysConfig = sysDao.find(SysConfig.class, sysConfigVo.getId());
			if (null == sysConfig) {
				throw new RuntimeException("ftpServer(id=" + sysConfigVo.getId() + ") is null!");
			}
			Date createTime = sysConfig.getCreateTime();
			BeanUtils.copyProperties(sysConfigVo, sysConfig);
			if (null == createTime) {
				createTime = new Date();
			}
			sysConfig.setCreateTime(createTime);
			sysConfig.setUpdateTime(new Date());
			updateBaseEntity(sysConfig);

		} finally {
			lock.writeLock().unlock();
		}
		reloadCache();// 更新完成，刷新缓存
	}

	@Override
	public void enableConfig(long idLong) {
		lock.writeLock().lock();
		try {
			SysConfig s = sysDao.find(SysConfig.class, idLong);
			if (s != null) {
				if (s.getEnable()) {
					s.setEnable(false);
				} else {
					s.setEnable(true);
				}
				sysDao.save(s);
			} else {
				throw new RuntimeException("The config is null !");
			}
		} finally {
			lock.writeLock().unlock();
		}
		reloadCache();// 配置启停修改完成，刷新缓存
	}

	@Override
	public SysConfig find(String upperCase) {
		lock.readLock().lock();
		try {
			return SYS_CONFIG_CACHE.get(upperCase.toUpperCase());
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 获取系统默认语言 zh_CN中文，en_US英文
	 * @return LANGUAGE_CONFIG中languages中的第一个配置为默认语言
	 * 		若没有配置，则返回zh_CN
	 */
	@Override
	public String getDefaultLanguage(){
		try{
			SysConfig cofig=find(ConfigKey.LANGUAGES_CONFIG);
			if (cofig == null) {
				reloadCache();
				cofig = find(ConfigKey.LANGUAGES_CONFIG);
			}
			String  value=JSONUtils.parse(cofig.getValue()).toString();
			String[] valueArray = value.split(",");
			if(valueArray.length > 0){
				return  valueArray[0];
			}else{
				//若系统中没有配置语言项时，返回默认值zh_CN
				logger.error("databases don't config the language!");
				return "zh_CN";
			}
		}catch(Exception e){
			logger.error("databases don't config the language!");
			return "zh_CN";
		}
	}

	@Override
	public List<SysConfig> listConfig() {
		lock.readLock().lock();
		try {
			List<SysConfig> sysConfigList = new ArrayList<SysConfig>();
			for (SysConfig sysConfig : SYS_CONFIG_CACHE.values()) {
				if (SysConfigConstant.SYS_CONFIG_TYPE_CONFIG.equals(sysConfig.getType())) {
					sysConfigList.add(sysConfig);
				}

			}
			return sysConfigList;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public List<SysConfig> listConstant() {
		lock.readLock().lock();
		try {
			List<SysConfig> sysConfigList = new ArrayList<SysConfig>();
			for (SysConfig sysConfig : SYS_CONFIG_CACHE.values()) {
				if (SysConfigConstant.SYS_CONFIG_TYPE_CONSTANT.equals(sysConfig.getType())) {
					sysConfigList.add(sysConfig);
				}
			}
			return sysConfigList;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public List<SysConfigVO> getConfigList() {
		return getList(SysConfigConstant.SYS_CONFIG_TYPE_CONFIG);
	}

	@Override
	public List<SysConfigVO> getConstantList() {
		return getList(SysConfigConstant.SYS_CONFIG_TYPE_CONSTANT);
	}


	@Override
	public String getLocalPath() {
		String localPath="";
		try {
			SysConfig sysc = find(ConfigKey.FILEPATH.toString());
			if(sysc!=null){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtils.json2Obj(sysc.getValue(), Map.class);
				if(map != null){
					localPath = map.get("localPath");
					if(localPath !=null && !localPath.endsWith(File.separator)){
						localPath+=File.separator;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return localPath;
	}

	@Override
	public int getConstantValue(String configKey, String key, String value, String injectKey) {
		SysConfig sysc = find(configKey);
		JsonArray returnData = new JsonParser().parse(sysc.getValue()).getAsJsonArray();
		int returnValue = 0;
		for (JsonElement gson : returnData) {
			JsonObject jsonObject = new Gson().fromJson(gson, JsonObject.class);
			if (injectKey.equals(jsonObject.get(key).getAsString())) {
				returnValue = jsonObject.get(value).getAsInt();
				break;
			}
		}
		return returnValue;
	}
	


	/**
	 * @Title getContentIdWarnNumber
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public Integer getContentIdWarnNumber() {
		Integer warmnumber=5000;
		try {
			SysConfig sysc = find(ConfigKey.SYS_CONFIG_CNTNTID_THRESHOLD.toString());
			if(sysc!=null){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtils.json2Obj(sysc.getValue(), Map.class);
				if(map != null){
					warmnumber = Integer.parseInt(map.get("SYS_CONFIG_CNTNTID_THRESHOLD"));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return warmnumber;
	}

	/**
	 * @Title checkPassWordTimeOut
	 * @Description 根据配置检查用户密码是否过期
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public boolean checkPassWordTimeOut(Date passwordtime) {
		Boolean flag=true;
		try {
			SysConfig sysc = find(ConfigKey.PASSWORD_TIMEOUT.toString());
			if(sysc!=null){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtils.json2Obj(sysc.getValue(), Map.class);
				if(map == null){
					//没有配置，默认不做密码过期检查
					return flag;
				}
				if(map.get("enable")==null||"false".equals(map.get("enable"))){
					//相当于未启用  改配置
					return flag;
				}else{
					//配置了  且启用
					if("true".equals(map.get("enable"))){
					   //获取配置的时间
						String  number=map.get("timeout");
						if(com.hoob.rs.utils.StringUtils.isNotEmpty(number)){
							double numberInt=Double.parseDouble(number);
							long time1=passwordtime.getTime();//上次的密码更新时间
							long time2=System.currentTimeMillis();//当前时间
							if((time2-time1)>=numberInt*24*60*60*1000){
								flag=false;
								return flag;
							}
						}else{
							//配置了错误的值
							return flag;
						}
					}else{
						//配置了错误的值
						return flag;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return flag;
	}
}
