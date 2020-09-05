-- 内容语言配置
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'内容语言配置','内容语言配置',1,'LANGUAGES_CONFIG',NOW(),'\"zh_CN\"')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`),`type` = values(`type`);

-- 系统语言配置
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'系统语言配置','系统语言配置',1,'LANGS_CONFIG',NOW(),'\"zh-cn,en-us\"')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`),`type` = values(`type`);


-- 系统测试模式
INSERT INTO `sys_config` (`type`,`createTime`, `desc`, name,`enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'系统测试模式','系统测试模式',0,'TEST_MODE',NOW(),'')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);


-- logo图片配置
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'CMS登录和系统logo图片配置','CMS登录和系统logo图片配置',0,'LOGO_PIC_CONFIG',NOW(),'{\"login\":\"img/icon_logo.png\",\"system\":\"img/logo.png\"}')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`),`type` = values(`type`);


-- 文件上传地址
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'文件上传地址配置','文件上传地址配置',1,'FILEPATH',NOW(),'{\"localPath\":\"/opt/hoob/data/media/"}')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);

-- 对外服务地址（如图片下载）
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'对外服务地址（如图片下载）','对外服务地址',1,'SERVICE_ENTRY',NOW(),'{\"entry\":\"http://127.0.0.1:6600/"}')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);


-- 用户密码过期配置
INSERT INTO `sys_config` (`type`,`createTime`, `desc`,name, `enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'用户登入密码过期配置:enable(表示是否启用密码过期机制,timeout密码过期时间（单位天,默认值90天）)','登入密码过期配置',0,'PASSWORD_TIMEOUT',NOW(),'{\"enable\":\"false",\"timeout"\:\"90\"}')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);

-- 初始化图片配置，每种图片都包含一个默认图片，不可修改，无需转换
INSERT INTO `sys_config` (`createTime`, `updateTime`, `desc`, `enable`, `key`, `name`, `type`, `value`) VALUES (NOW(),NOW(), '其他图片配置', 1, 'SYS_CONFIG_PIC_OTHER', '其他图片配置', 'pic', '{\"picThumbnail\":{\"name\":\"thumbnail\",\"data\":[{\"width\":\"0\",\"height\":\"0\",\"isDefault\":true,\"process\":false,\"editFlag\":false}]}}') ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);


-- STSC请求ES服务URL前缀
INSERT INTO `sys_config` (`type`,`createTime`, `desc`, name,`enable`, `key`, `updateTime`, `value`) 
value('config',NOW(),'STSC请求ES服务URL前缀','STSC请求ES服务URL前缀',0,'STSC_ES_REQURL_PREFIX',NOW(),'http://172.16.199.128:6601')
ON DUPLICATE KEY UPDATE `desc` = values(`desc`),`name` = values(`name`);



