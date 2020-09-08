-- 定时器初始化脚本本
-- 可重复执行
-- 注意脚本格式：支持双横线加空格注释及#号注释，sql语句可换行，一条完整的sql语句必须以分号结尾
-- jobGroup：填写整形值 ，值范围--->1:cms分发  2：cdn 分发  3 :bms分发 4:epg  5  TVGW&TCGS 6：c2注入   7：c1注入  8：c3注入   9：其他    10:DRM,11:SeaLib',12:Epg模板分发
-- triggerType：填写整形值 ，值范围---> 1：cron、2：simple:3：calendar



		
-- 处理媒体内容子任务		
-- INSERT INTO `schedulerjob` (`createTime`, `updateTime`, `cronExpression`, `description`, `executTime`, `jobGroup`, `jobName`, `status`, `targetObject`, `triggerType`) 
-- VALUES (NOW(),NOW(), '0/5 * * * * ?', '解析媒体内容子任务', NULL, '6', 'C2AnalysisMovieJob', '\0', 'com.fonsview.cms.task.inject.c2.job.child.C2AnalysisMovieJob', '1')
-- ON DUPLICATE KEY UPDATE schedulerjob.targetObject = VALUES(schedulerjob.targetObject),description = VALUES(description);
	
	
