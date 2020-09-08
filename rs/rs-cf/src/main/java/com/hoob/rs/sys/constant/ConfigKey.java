package com.hoob.rs.sys.constant;

/**
 * 
 * 配置KEY
 * 
 * @author Faker
 * 
 */
public enum ConfigKey {
	// ///////// 分发配置KEY ///////////
	DIST_TCGS_VOD, // VOD转码
	DIST_TVGW_VOD, // VOD预处理
	DIST_TVGW_LIVE, // LIVE预处理
	DIST_DRM, // DRM分发
	DIST_CDN, // CDN分发
	DIST_CMS, // CMS分发
	DIST_BMS, // BMS分发
	DIST_EPG, // EPG分发
	DIST_EPGTEM, // EPG模板分发
	DIST_CSS, // 上海云
	// ///////// Tcgs转换配置KEY ///////////
	TCGS_TRANS_RULE, // tcgs转换规则
	// ///////// CP ///////////
	CP_DOMAIN, // CP域配置KEY
	OP_DOMAIN, // 操作日志域配置KEY
	ML_CONFIG, // 码率配置KEY

	TJ_CONFIG, // 统计配置key
	/**
	 * 注入配置key
	 */
	INJ_C1, // C1接口注入
	INJ_C2, // C2接口注入
	INJ_C3, // C3接口注入配置
	INJ_C2Plus, // C2+接口注入
	INJ_CE, // CE接口注入

	AREA_CONFIG, // 地区配置key
	BESTV_CONVERT_CONFIG, // 百视通contentid转换规则配置
	BESTV_MAPPING_PREF, // 百视通contentid转换匹配的前缀
	// FILTER_CONFIG,//筛选域配置key

	INJ_RETRY_DOWNPIC, // 图片下载重试次数
	INJ_RETRY_OTHEROBJ, // 其他对象重试的条数
	INJ_RETRY_OTHEROBJTIMEOUT, // 其他对象重试的超时次数

	SYS_CONFIG_PIC_VOD, // VOD
	SYS_CONFIG_PIC_APP, // APP
	SYS_CONFIG_PIC_CAST, // CAST
	SYS_CONFIG_PIC_CATEGORY, // CATEGORY
	SYS_CONFIG_PIC_CHANNEL, // CHANNEL
	SYS_CONFIG_PIC_SPECIALTOPIC, // SPECIALTOPIC
	SYS_CONFIG_PIC_OTHER, // OTHER
	SYS_CONFIG_PIC_AD, // 广告

	FILEPATH_CONFIG, // 文件路径配置
	GAME_CONFIG, // 游戏相关配置
	// NETWORK_CONFIG,//网络配置
	RATES_CONFIG, // 评分相关配置
	CD_CONFIG, // CD配置
	FTP_CONFIG, // 本地Ftp配置
	CAS_CONFIG, // CAS配置
	PORTAL_CONFIG, // Portal配置
	TVMS_CONFIG, // TVMS配置
	LANGUAGES_CONFIG, // 内容语言配置
	LANGS_CONFIG, // 系统语言配置
	MULLANGUAGES_CONFIG, // 语种配置
	CDN_CONFIG, // CDN相关配置
	MEDIACONTENT_LENGTHFROM, // 时长是否取自CMM 1：是 0 ：否
	OTHER_CONFIG, // 其他配置
	IBCP, // CMS是否充当播控平台
	FILTER_CONTENT, // 过滤规则数据配置，主要包含表名和字段名称
	FILTER_OPERATOR, // 过滤规则操作符配置
	PARSER_URL, // 解析器地址
	EPGGROUP_CONFIG, // EPG模板分组配置
	SCENE_CONFIG, // 区分现场配置,主要区分江苏现场JSC和其他other
	// ///////// TEST ///////////
	TEST_MODE, // 测试模式 1是，0否
	CONFIG_NAME, // 配置名称
	SYS_CONFIG_CNTNTID_THRESHOLD, APPROVED_CONFIG, // 审核配置
	/** CMS登录和系统logo图片配置 **/
	LOGO_PIC_CONFIG, EPG_CONTROL_CONFIG, DEFINITION_CONFIG, RESOLUTION_CONFIG, FILEPATH, GLOBAL_DEFAULT_SERVICE, // 全局默认服务配置
	SERVICE_ENTRY, // 服务入口(如图片下载，主要针对发送给下游的工单)
	SCHEDULE_DOWNLOAD_URL, // 节目单下载地址
	BASE_DATA_LABLE_CONFIG, // 基础数据lable列表头配置
	PASSWORD_TIMEOUT, // 用户密码过期配置
	IM2_MAPPING, // xml文件中映射关系，语言与cms系统语言映射配置(IM2节目单导入)，频道号
	IM2_TIME_CONVERT,// 是否开启UTC时间转换(IM2节目单导入)
	ONE_KEY_SHUTDOWN//一键关停相关配置
	
}
