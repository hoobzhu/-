-- 定时器初始化脚本
-- 可重复执行
-- 注意脚本格式：支持双横线加空格注释及#号注释，sql语句可换行，一条完整的sql语句必须以分号结尾
-- INSERT INTO `role` (id,name,creator) VALUES (1,'admin','sysadmin') ON DUPLICATE KEY UPDATE `id`=1,`name`='admin';

INSERT INTO `user` (id,password,role,enable,enableIPBinding,userId,userlevel,userType,loginTimes,createTime,updateTime) 
    VALUES (1,'A53D5A1D8F67BBD317623DF22BE6A047355FB20682897A0EB26F0B141CE407D120B2CCA0278BDD45475A3D3200EB683B','admin',1,0,'sysadmin',1,'sysadmin',0,NOW(),NOW()) ON DUPLICATE KEY UPDATE id=1,userId='sysadmin',enable = 1;

-- INSERT INTO `user_role` (user_id,role_id) VALUES (1,1) ON DUPLICATE KEY UPDATE user_id=1,role_id=1;

-- 权限根节点
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('1', null, 'menu_m', null, '0', '0','menu_m','/rest/v1/user/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id=1,url=VALUES(url);

-- cpsp管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,createTime,updateTime) VALUES ('2', null, 'csp_m', '1', '1', '0','security_m',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =2;
-- 角色管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('21', null, 'role_m', '2', '2', '1','security_role_m','(/rest/v1/security/role/list)|(/rest/v1/security/role/detail)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =21,url=VALUES(url);
-- 角色增删改
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('211', null, 'add_m', '21', '3', '2','security_role_add','/rest/v1/security/role/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =211,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('212', null, 'edit_m', '21', '3', '2','security_role_edit','/rest/v1/security/role/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =212,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('213', null, 'delete_m', '21', '3', '2','security_role_delete','/rest/v1/security/role/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =213,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('214', null, 'detail_m', '21', '3', '2','security_role_detail','/rest/v1/security/role/detail',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =214,url=VALUES(url);
-- 用户管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('22', null, 'user_m', '2', '2', '1','security_user_m','(/rest/v1/security/user/list)|(/rest/v1/security/check/default/password)|(/rest/v1/security/user/detail)|(/rest/v1/security/user/check/unique)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =22,url=VALUES(url);
-- 用户增删改
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('221', null, 'add_m', '22', '3', '2','security_user_add','/rest/v1/security/user/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =221,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('222', null, 'edit_m', '22', '3', '2','security_user_edit','/rest/v1/security/user/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =222,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('223', null, 'delete_m', '22', '3', '2','security_user_remove','/rest/v1/security/user/remove',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =223,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('224', null, 'uchkdefaultpwd_m', '22', '2', '2','security_user_check_defaultpwd','/rest/v1/security/check/default/password',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =224,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('225', null, 'detail_m', '22', '3', '2','security_user_detail','/rest/v1/security/user/detail',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =225,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('226', null, 'uchkunique_m', '22', '3', '2','security_user_check_unique','/rest/v1/security/user/check/unique',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =226,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('227', null, 'enabel_m', '22', '3', '2','security_user_enabel','/rest/v1/security/user/enable',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =227,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('228', null, 'repasseword_m', '22', '3', '2','security_user_repasseword','(/rest/v1/security/user/password/reset)|(/rest/v1/security/user/password/edit)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =228,url=VALUES(url);
-- cp管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('23', null, 'cp_m', '2', '2', '1','security_cp_m','(/rest/v1/security/cp/list)|(/rest/v1/security/cp/valid)|(/rest/v1/security/cp/judgeNo)|(/rest/v1/security/cp/detail)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =23,url=VALUES(url);
-- cp增删改
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('231', null, 'add_m', '23', '3', '2','security_cp_add','/rest/v1/security/cp/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =231,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('232', null, 'edit_m', '23', '3', '2','security_cp_edit','(/rest/v1/security/cp/edit)|(/v1/sys/cpconfig/edit)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =232,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('233', null, 'enabel_m', '23', '3', '2','security_cp_enabel','/rest/v1/security/cp/enable',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =233,url=VALUES(url);
-- cp配置
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('233', null, 'cpconfig_m', '23', '3', '2','sysmanage_cpconfig_edit','/v1/sys/cpconfig/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =233,url=VALUES(url),menuTitle=VALUES(menuTitle);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('234', null, 'valid_m', '23', '3', '2','security_cp_valid','/rest/v1/security/cp/valid',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =234,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('235', null, 'judgeno_m', '23', '3', '2','security_cp_judgeno','/rest/v1/security/cp/judgeNo',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =235,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('236', null, 'detail_m', '23', '3', '2','security_cp_detail','/rest/v1/security/cp/detail',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =236,url=VALUES(url);
-- sp管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('24', null, 'sp_m', '2', '2', '1','security_sp_m','(/rest/v1/security/sp/list)|(/rest/v1/security/sp/detail)|(/rest/v1/security/sp/valid)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =24,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('24', null, 'sp_m', '2', '2', '1','security_sp_m','(/rest/v1/security/sp/list)|(rest/v1/security/sp/valid/[\\w\\W]*)|(/rest/v1/security/sp/detail)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =24,url=VALUES(url);
-- sp增删改
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('241', null, 'add_m', '24', '3', '2','security_sp_add','/rest/v1/security/sp/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =241,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('242', null, 'edit_m', '24', '3', '2','security_sp_edit','/rest/v1/security/sp/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =242,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('243', null, 'detail_m', '24', '3', '2','security_sp_detail','/rest/v1/security/sp/detail',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =243,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('244', null, 'valid_m', '24', '3', '2','security_sp_valid','/rest/v1/security/sp/valid',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =244,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('235', null, 'enabel_m', '24', '3', '2','security_sp_enabel','/rest/v1/security/sp/enable',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =235,url=VALUES(url);
-- 系统管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,createTime,updateTime) VALUES ('3', null, 'sysmanage_m', '1', '1', '0','sysmanage_m',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =3;
-- 操作日志管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('31', null, 'operlog_m', '3', '2', '1','sysmanage_operlog_m','/rest/v1/operlog/list',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =31,url=VALUES(url);

-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('311', null, 'delete_m', '31', '3', '2','sysmanage_operlogm_delete','/rest/v1/operlog/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =311,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('312', null, 'content_m', '31', '3', '2','sysmanage_operlogm_content','/rest/v1/operlog/content/[\w\W]*',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =312,url=VALUES(url);
-- 存储管理
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url) VALUES ('32', null, 'storage_manage_m', '3', '2', '1','sysmanage_storage_manage_m','/rest/v1/storage_manage/list') ON DUPLICATE KEY UPDATE id =32,url=VALUES(url);

-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url) VALUES ('321', null, 'delete_m', '32', '3', '2','sysmanage_storage_manage_delete','/rest/v1/storage_manage/delete/[\w\W]*') ON DUPLICATE KEY UPDATE id =321,url=VALUES(url);

-- FTP管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('33', null, 'ftpserver_m', '3', '2', '1','sysmanage_ftpserver_m','(/rest/v1/sys/ftpserver/list)|(/rest/v1/sys/ftpserver/detail)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =33,url=VALUES(url);

-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('331', null, 'add_m', '33', '3', '2','sysmanage_ftpserver_add','/rest/v1/ftpserver/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =331,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('332', null, 'edit_m', '33', '3', '2','sysmanage_ftpserver_update','/rest/v1/ftpserver/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =332,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('333', null, 'delete_m', '33', '3', '2','sysmanage_ftpserver_delete','/rest/v1/ftpserver/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =333,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('334', null, 'add_m', '33', '3', '2','sysmanage_ftpserver_add2','/rest/v1/sys/ftpserver/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =334,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('335', null, 'edit_m', '33', '3', '2','sysmanage_ftpserver_update2','/rest/v1/sys/ftpserver/update',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =335,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('336', null, 'delete_m', '33', '3', '2','sysmanage_ftpserver_delete2','/rest/v1/sys/ftpserver/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =336,url=VALUES(url);
-- 系统参数
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('34', null, 'sysconfig_m', '3', '2', '1','sysmanage_sysconfig_m','(/rest/v1/sys/config/list)|(/rest/v1/sys/config/constantlist)|(/rest/v1/sys/config/detail)|(/rest/v1/sys/config/json)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =34,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('341', null, 'add_m', '34', '3', '2','sysmanage_sysconfig_add','/rest/v1/user/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =341,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('342', null, 'edit_m', '34', '3', '2','sysmanage_sysconfig_update','/rest/v1/user/add',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =342,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('343', null, 'delete_m', '34', '3', '2','sysmanage_sysconfig_delete','/rest/v1/user/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =343,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('344', null, 'cacheManage', '34', '3', '2','sysmanage_cache_clean','/v1/sys/config/clean',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =344,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('344', null, 'cacheManage', '34', '3', '2','sysmanage_cache_clean','(/rest/v1/sys/config/refresh)|(/rest/v1/sys/config/clean)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =344,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('345', null, 'enabel_m', '34', '3', '2','sysmanage_sysconfig_enabel','/rest/v1/sys/config/enable',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =345,url=VALUES(url);

-- 定时任务
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('35', null, 'schedulejob_m', '3', '2', '1','sysmanage_schedulejob_m','(/rest/v1/sys/schdljobmng/list)|(/rest/v1/sys/schdljobmng/detail/[\\w\\W]*)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =35,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('351', null, 'edit_m', '35', '3', '2','sysmanage_schedulejob_edit','/rest/v1/schedulejob/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =351,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('352', null, 'edit_m', '35', '3', '2','sysmanage_schedulejob_edit','/rest/v1/sys/schdljobmng/edit',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =352,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('353', null, 'enabel_m', '35', '3', '2','sysmanage_schedulejob_enabel','(/rest/v1/sys/schdljobmng/bathenable)|(/rest/v1/sys/schdljobmng/enable/[\\w\\W]*)',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =353,url=VALUES(url);

-- 权限列表
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('36', null, 'privilege_m', '36', '3', '1','privilege_m','/rest/v1/security/privilege/list',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =36,url=VALUES(url);

-- STSC管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,createTime,updateTime) VALUES ('4', null, 'stsc_m', '1', '1', '0','stsc_m',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =4;
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('401', null, 'stsc_getSeriesInfos', '4', '2', '2','stsc_getSeriesInfos','/stsc/rest/services/v1/content/getSeriesInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =401,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('402', null, 'stsc_getChannelInfos', '4', '2', '2','stsc_getChannelInfos','/stsc/rest/services/v1/content/getChannelInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =402,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('403', null, 'stsc_getScheduleInfos', '4', '2', '2','stsc_getScheduleInfos','/stsc/rest/services/v1/content/getScheduleInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =403,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('404', null, 'stsc_getLiveUserInfos', '4', '2', '2','stsc_getLiveUserInfos','/stsc/rest/services/v1/content/getLiveUserInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =404,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('405', null, 'stsc_getSeriesInfoByCityNames', '4', '2', '2','stsc_getSeriesInfoByCityNames','/stsc/rest/services/v1/content/getSeriesInfoByCityNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =405,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('406', null, 'stsc_getChannelInfoByCityNames', '4', '2', '2','stsc_getChannelInfoByCityNames','/stsc/rest/services/v1/content/getChannelInfoByCityNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =406,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('407', null, 'stsc_getContentOrderInfos', '4', '2', '2','stsc_getContentOrderInfos','/stsc/rest/services/v1/content/getContentOrderInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =407,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('408', null, 'stsc_getCategoryNames', '4', '2', '2','stsc_getCategoryNames','/stsc/rest/services/v1/content/getCategoryNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =408,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('409', null, 'stsc_getCategoryInfos', '4', '2', '2','stsc_getCategoryInfos','/stsc/rest/services/v1/content/CategoryInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =409,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('410', null, 'stsc_getChannelNames', '4', '2', '2','stsc_getChannelNames','/stsc/rest/services/v1/content/getChannelNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =410,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('411', null, 'stsc_getProgramInfos', '4', '2', '2','stsc_getProgramInfos','/stsc/rest/services/v1/content/getProgramInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =411,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('412', null, 'stsc_getUserStatusInfos', '4', '2', '2','stsc_getUserStatusInfos','/stsc/rest/services/v1/user/getUserStatusInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =412,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('413', null, 'stsc_getUserBootInfos', '4', '2', '2','stsc_getUserBootInfos','/stsc/rest/services/v1/user/getUserBootInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =413,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('414', null, 'stsc_getUserInfoByCityNames', '4', '2', '2','stsc_getUserInfoByCityNames','/stsc/rest/services/v1/user/getUserInfoByCityNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =414,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('415', null, 'stsc_getUserHistory', '4', '2', '2','stsc_getUserHistory','/stsc/rest/services/v1/user/getUserHistory',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =415,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('416', null, 'stsc_getUserInfo', '4', '2', '2','stsc_getUserInfo','/stsc/rest/services/v1/user/getUserInfo',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =416,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('417', null, 'stsc_getCityInfos', '4', '2', '2','stsc_getCityInfos','/stsc/rest/services/v1/quality/getCityInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =417,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('418', null, 'stsc_getAddrCatonDetails', '4', '2', '2','stsc_getAddrCatonDetails','/stsc/rest/services/v1/quality/getAddrCatonDetails',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =418,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('419', null, 'stsc_getUpgradeInfos', '4', '2', '2','stsc_getUpgradeInfos','/stsc/rest/services/v1/equipment/getUpgradeInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =419,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('420', null, 'stsc_getOrderInfos', '4', '2', '2','stsc_getOrderInfos','/stsc/rest/services/v1/order/getOrderInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =420,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('421', null, 'stsc_getProductNames', '4', '2', '2','stsc_getProductNames','/stsc/rest/services/v1/order/getProductNames',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =421,url=VALUES(url);

INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('422', null, 'stsc_getServiceLog', '4', '2', '2','stsc_getServiceLog','/stsc/rest/services/v1/service/getServiceLog',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =422,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('423', null, 'stsc_getErrorLog', '4', '2', '2','stsc_getErrorLog','/stsc/rest/services/v1/error/getErrorLog',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =423,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('424', null, 'stsc_getTotalErrorStat', '4', '2', '2','stsc_getTotalErrorStat','/stsc/rest/services/v1/error/getTotalErrorStat',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =424,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('425', null, 'stsc_getIntervalErrorStat', '4', '2', '2','stsc_getIntervalErrorStat','/stsc/rest/services/v1/error/getIntervalErrorStat',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =425,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('426', null, 'stsc_getContentDurationAndPlayCount', '4', '2', '2','stsc_getContentDurationAndPlayCount','/stsc/rest/services/v2/content/getContentDurationAndPlayCount',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =426,url=VALUES(url);
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('427', null, 'stsc_getTvodProgramInfos', '4', '2', '2','stsc_getTvodProgramInfos','/stsc/rest/services/v1/content/getTvodProgramInfos',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =427,url=VALUES(url);
	


-- 最后执行更新维护enable字段值
update  privilege set enable=0 where enable is null or enable ='';

-- 创建一个默认角色
INSERT INTO `role` (`id`, `createTime`, `updateTime`, `creator`, `description`, `name`) VALUES ('1', NOW(), NOW(), 'sysadmin', '超级管理员初始化权限,不允许随意修改，如有需要请联系管理员', 'admin');
-- 分配超级管理员的默认权限
insert role_privilege (role_id,priv_id)select 1,id from privilege;



