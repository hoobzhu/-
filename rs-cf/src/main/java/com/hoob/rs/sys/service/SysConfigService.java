/**
 * 
 */
package com.hoob.rs.sys.service;

import java.util.Date;
import java.util.List;

import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.sys.constant.ConfigKey;
import com.hoob.rs.sys.model.SysConfig;
import com.hoob.rs.sys.vo.SysConfigVO;

/**
 * @author Raul	
 * 2017年9月6日
 */
public interface SysConfigService extends BaseService {
	
	public void reloadCache();
	
	public SysConfig find(long id);
	
	public SysConfig find(ConfigKey key);
	
	public boolean isEnable(ConfigKey key);
	
	public <T> T getSysConfig(ConfigKey key, Class<T> clazz);
	
	public <T> List<T> getSysConfigList(ConfigKey key, Class<T> clazz);
			
	public void save(SysConfig sysConfig);
	
	public void batchSave(List<SysConfig> sysConfigList);
	
	public List<SysConfig> listAll();
	
	/**
	 * 系统配置列表
	 * 
	 * @return
	 */
	public List<SysConfig> listConfig();

	/**
	 * 系统常量列表
	 * 
	 * @return
	 */
	public List<SysConfig> listConstant();

	public <T> void save(ConfigKey key, T valueConfig);
	
	public <T> void save(ConfigKey key, List<T> valueConfigList);

	public SysConfigVO findConfig(long idLong);

	public List<SysConfigVO> getList(String type);

	public List<SysConfigVO> getConfigList();

	public List<SysConfigVO> getConstantList();

	public void updateSysConfig(SysConfigVO sysConfigVo);

	/**
	 * 启停配置
	 * 
	 * @param idLong
	 */
	public void enableConfig(long idLong);

	public SysConfig find(String upperCase);
	
	public String getDefaultLanguage();
	
	public String getLocalPath();


	/**
	 * 获取指定常量的指定值
	 * 
	 * @param configKey
	 *            常量配置的Key
	 * @param valueKey
	 * @param key
	 * @param value
	 *            贼难描述，我画个实例在这好了-，- [ { "key": "valuekey", "value": 返回的值 }, {
	 *            "label": "MPEG4", "value": 2 }, { "label": "AVS", "value": 3
	 *            }, { "label": "MPEG2", "value": 4 }, { "label": "MP3",
	 *            "value": 5 }, { "label": "WMV", "value": 6 }, { "label":
	 *            "H265", "value": 7 } ]
	 * @return
	 */
	public int getConstantValue(String configKey, String key, String value, String valueKey);

	/**
	 * CMS是否作为播控平台
	 * 
	 * @return
	 */
	public boolean isBroadcastControl();
	/**
	 * CMS是否采用一个统一默认服务配置
	 * 
	 * @return
	 */
	public boolean getGlobalDefaultService();
	/**
	 *获取contentIds 域阀值配置
	 * **/
	public Integer getContentIdWarnNumber();
	/**
	 * 获取审核配置开关
	 * ***/
	public boolean getAuditSwitch();

	/**
	 * 获取审核配置开关
	 * ***/
	public boolean checkPassWordTimeOut(Date passwordtime);

}
