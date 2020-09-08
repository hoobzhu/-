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
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,createTime,updateTime) VALUES ('2', null, 'user_mm', '1', '1', '0','security_m',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =2;
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

-- 系统管理
INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,createTime,updateTime) VALUES ('3', null, 'sysmanage_m', '1', '1', '0','sysmanage_m',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =3;

-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('311', null, 'delete_m', '31', '3', '2','sysmanage_operlogm_delete','/rest/v1/operlog/delete',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =311,url=VALUES(url);
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url,createTime,updateTime) VALUES ('312', null, 'content_m', '31', '3', '2','sysmanage_operlogm_content','/rest/v1/operlog/content/[\w\W]*',NOW(),NOW()) ON DUPLICATE KEY UPDATE id =312,url=VALUES(url);
-- 存储管理
-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url) VALUES ('32', null, 'storage_manage_m', '3', '2', '1','sysmanage_storage_manage_m','/rest/v1/storage_manage/list') ON DUPLICATE KEY UPDATE id =32,url=VALUES(url);

-- INSERT INTO `privilege` (id,description,menuTitle,parentId,menuLevel,menuType,menuCode,url) VALUES ('321', null, 'delete_m', '32', '3', '2','sysmanage_storage_manage_delete','/rest/v1/storage_manage/delete/[\w\W]*') ON DUPLICATE KEY UPDATE id =321,url=VALUES(url);


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





-- 最后执行更新维护enable字段值
update  privilege set enable=0 where enable is null or enable ='';

-- 创建一个默认角色
INSERT INTO `role` (`id`, `createTime`, `updateTime`, `creator`, `description`, `name`) VALUES ('1', NOW(), NOW(), 'sysadmin', '超级管理员初始化权限,不允许随意修改，如有需要请联系管理员', 'admin');
-- 分配超级管理员的默认权限
insert role_privilege (role_id,priv_id)select 1,id from privilege;



