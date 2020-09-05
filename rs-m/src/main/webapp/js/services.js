'use strict';
angular.module('myApp.services', []).value('version', '0.1').service(
        'sharedProperties', ['$http', '$q', '$rootScope', function ($http, $q, $rootScope) {
            var language = sessionStorage.getItem("language") || "zh-cn";
            var strPath = window.document.location.pathname; 
            var appName = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            var serverUrl = "http://" + window.location.host + appName;
            var fileUrl = "http://" + window.location.host + "/cms_files/";
            //读取系统配置，如果没有配置相应的服务地址则，使用上面的默认是上面的拼接地址，
            $http.get(serverUrl + '/rest/v1/sys/config/value?key=SERVICE_ENTRY')
            .success(function (data) {
            	console.info("图片服务地址");
                if(data.vo!=null&&data.vo.entry!=null){
                	fileUrl=data.vo.entry;
                }
            });
            
            console.log("serverUrl:",serverUrl);
         //   var mediaTypes = [{"name": "movie", "value": "10"}, {"name": "tvshows", "value": "2"}, {"name": "channel", "value": "3"}, {"name": "music", "value": "4"}, {"name": "ebook", "value": "5"}, {"name": "apk", "value": "27"} , {"name": "radio", "value": "8"}, {"name": "cast", "value": "9"}];
            var CmscontentTypes_en = [{"name": "VOD", "value": "Vod","no":2}, {"name": "Program", "value": "Program","no":1}, {"name": "Channel", "value": "Channel","no":3}, {"name": "Category", "value": "Category","no":23}, {"name": "Service", "value": "Service","no":24},{"name": "Movie", "value": "Movie","no":10},{"name": "Schedule", "value": "Schedule","no":13},{"name": "应用", "value": "Apk","no":27}];
            var CmscontentTypes_zh = [{"name": "合集", "value": "Vod","no":2}, {"name": "分集", "value": "Program","no":1}, {"name": "直播", "value": "Channel","no":3}, {"name": "栏目", "value": "Category","no":23}, {"name": "服务", "value": "Service","no":24},{"name": "媒体内容", "value": "Movie","no":10},{"name": "节目单", "value": "Schedule","no":13},{"name": "应用", "value": "Apk","no":27}];
          //   var programTypes_en = [{"name": "film_content", "value": "film_content","no":"1"}, {"name": "trailer1", "value": "trailer1","no":"3"}, {"name": "trailer2", "value": "trailer2","no":"3"}, {"name": "tryprogram", "value": "tryprogram","no":"4"}, {"name": "outprogram", "value": "outprogram","no":"5"}];
         //    var programTypes_zh = [{"name": "正片", "value": "film_content","no":"1"}, {"name": "预告片", "value": "trailer1","no":"3"}, {"name": "片花", "value": "trailer2","no":"2"}, {"name": "试看片段", "value": "tryprogram","no":"4"}, {"name": "番外篇", "value": "outprogram","no":"5"}];
         //    var programTypes_tw = [{"name": "正片", "value": "film_content","no":"1"}, {"name": "預告片", "value": "trailer1","no":"3"}, {"name": "片花", "value": "trailer2","no":"2"}, {"name": "試看片段", "value": "tryprogram","no":"4"}, {"name": "番外篇", "value": "outprogram","no":"5"}];
            var CdncontentTypes = [{"name": "Schedule", "value": "Schedule","no":13}, {"name": "Theme", "value": "Theme","no":2}, {"name": "Apk", "value": "Apk","no":27}, {"name": "File", "value": "File","no":4}, {"name": "Movie", "value": "Movie","no":10}, {"name": "Channel", "value": "Channel","no":3}];
            var BmscontentTypes_en = [{"name": "Series", "value": "Series","no":2}, {"name": "Program", "value": "Program","no":1}, {"name": "Movie", "value": "Movie","no":10}, {"name": "Channel", "value": "Channel","no":3}, {"name": "Service", "value": "Service","no":24}];
            var BmscontentTypes_zh = [{"name": "合集", "value": "Series","no":2}, {"name": "分集", "value": "Program","no":1}, {"name": "媒体内容", "value": "Movie","no":10}, {"name": "直播", "value": "Channel","no":3}, {"name": "服务", "value": "Service","no":24}];
            var businesscontentTypes_en = [{"name": "VOD", "value": "Vod","no":2}, {"name": "Channel", "value": "Channel","no":3}, {"name": "Category", "value": "Category","no":23}, {"name": "Service", "value": "Service","no":24}];
            var businesscontentTypes_zh = [{"name": "合集", "value": "Vod","no":2},  {"name": "直播", "value": "Channel","no":3}, {"name": "栏目", "value": "Category","no":23}, {"name": "服务", "value": "Service","no":24}];
            var rangepickerLocale = {
            	'zh-cn':{
            		'Locale':{
            			applyLabel: '确定',
                        cancelLabel: '取消',
                        fromLabel: '从',
                        toLabel: '到',
                        weekLabel: '周',
                        customRangeLabel: '自定义范围',
                        firstDay: 1
            		},
            		'Ranges': {
	                    '今天': [moment(), moment()],
	                    '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
	                    '最近七天': [moment().subtract('days', 6), moment()]
            		},
            		'Ranges1': {
	                    '今天': [moment(), moment()],
	                    '近一周': [moment().subtract('days', 6), moment()],
	                    '近一个月':[moment().subtract('days', 29), moment()],
	                    '近半年':[moment().subtract('days', 365/2), moment()],
	                    '近一年':[moment().subtract('days', 365), moment()]
            		},
            		'Ranges2': {
	                    '近三天': [moment().subtract('days', 2), moment()],
	                    '近七天': [moment().subtract('days', 6), moment()],
	                    '近一个月':[moment().subtract('days', 29), moment()]
            		}
            	},
            	'zh-tw':{
            		'Locale':{
            			applyLabel: '確定',
                        cancelLabel: '取消',
                        fromLabel: '從',
                        toLabel: '到',
                        weekLabel: '周',
                        customRangeLabel: '自定義範圍',
                        firstDay: 1
            		},
            		'Ranges':{
            			'今天': [moment(), moment()],
                        '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
                        '最近七天': [moment().subtract('days', 6), moment()]
            		}
            	},
            	'en-us':{
            		'Locale':{
            			 applyLabel: 'Apply',
                         cancelLabel: 'Cancel',
                         fromLabel: 'From',
                         toLabel: 'To',
                         weekLabel: 'W',
                         customRangeLabel: 'Custom Range',
                         firstDay: 1
            		},
            		'Ranges':{
            			'Today': [moment(), moment()],
                        'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                        'Last 7 Days': [moment().subtract('days', 6), moment()],
            		}
            	}
            };
          
            
            //修改时应同时修改js/usergroup_controller.js
           $http.get(serverUrl + "/l10n/"+language+".json")
           .success(function (data) {
               $rootScope.l10n = data;
           });
           //获取图标配置
           $http.get(serverUrl+'/rest/v1/sys/config/bykey?key=logo_pic_config')
			.success(function(largeLoad){
				//$scope.data = largeLoad.sysConfig;
				if(largeLoad.vo!=null){
					if(largeLoad.vo.enable){
						var value = JSON.parse(largeLoad.vo.value);
						if(value.login!=null&&value.login!=''&&value.login!=undefined){
							$rootScope.login_logo = value.login;
						}else{
							$rootScope.login_logo = "img/icon_logo.png";
						}
						if(value.system!=null&&value.system!=''&&value.system!=undefined){
							$rootScope.system_logo = value.system;
						}else{
							$rootScope.system_logo = "img/logo.png";
						}
					}else{
						$rootScope.login_logo = "img/icon_logo.png";
						$rootScope.system_logo = "img/logo.png";
					}
				}else{
					//若配置为启用，采用系统默认配置
					$rootScope.login_logo = "img/icon_logo.png";
					$rootScope.system_logo = "img/logo.png";
				}
				console.log("logining:"+$rootScope.login_logo);
			})
			.error(function(){
				$rootScope.login_logo = "img/icon_logo.png";
				$rootScope.system_logo = "img/logo.png";
			});
            //初始化查询条件中年份
            var queryyear = new Array();
            var y = new Date().getFullYear();
            for (var i = y; i >= y - 116; i--) //以今年为准，前50年
            {
                queryyear.push(i);
            }

            return {
                getServerUrl: function () {
                    return serverUrl;
                },
                setServerUrl: function (value) {
                    serverUrl = value;
                },
              /*  getMediaTypes: function () {
                    return mediaTypes;
                },
*/                getQueryYear: function () {
                    return queryyear;
                },
                getFileUrl: function () {
                    return fileUrl;
                },
                getRangepickerLocale: function (language) {
                    return rangepickerLocale[language];
                },
   
                getContentTypes: function (mode, lan) {
                	if(mode == "6") {
                		if(lan == "zh-cn"){
                    		return CmscontentTypes_zh;
                    	}else{
                    		return CmscontentTypes_en;
                    	}
                	}else if(mode =="5") {
                		return CdncontentTypes;
                	}else if(mode =="3") {
                		if(lan == "zh-cn"){
                    		return BmscontentTypes_zh;
                    	}else{
                    		return BmscontentTypes_en;
                    	}
                	}else if (mode==7){
                		if(lan == "zh-cn"){
                    		return businesscontentTypes_zh;
                    	}else{
                    		return businesscontentTypes_en;
                    	}
                	}
                },
              
                getMullanguages: function () {//从服务器获取所有的地区配置
                    var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
                    $http.get(serverUrl + '/rest/v1/sys/config/value?key=MULLANGUAGES_CONFIG').
                    success(function (data, status, headers, config) {
                     var tempdata=[];
                     //包装结构
                     data.vo.forEach(function(tmp){
                    	 if(tmp.key!='all'){
                    		 tempdata.push({
              					value:tmp.value,
              					name:tmp.key
              				});
                    	 }
         			 });
                     var mullangs= {"vo":tempdata};
                       deferred.resolve(mullangs);  // 声明执行成功，即http请求数据成功，可以返回数据了
                    }).
                    error(function (data, status, headers, config) {
                        deferred.resolve(config);  // 声明执行失败，即服务器返回错误
                    });
                    return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
                },
                getCDNMediaTypes: function () {//从服务器获取所有的cdn内容类型
                    var CDNMediaType =  [
                            	    {"name": "VOD","value": "MOVIE"},
                                    {"name": "channel","value": "CHANNEL"},
                            	    {"name": "APP","value": "APK"},
                            	    {"name": "file_m","value": "FILE"}];
                    return CDNMediaType;
                },
                getUUID: function (len, radix) {//生成UUID
                	var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
                    var uuid = [], i;
                    radix = radix || chars.length;
                    if (len) {
                      // Compact form
                      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
                    } else {
                      // rfc4122, version 4 form
                      var r;
                      // rfc4122 requires these characters
                      //uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
                      //uuid[14] = '4';
                      // Fill in random data.  At i==19 set the high bits of clock sequence as
                      // per rfc4122, sec. 4.1.5
                      for (i = 0; i < 36; i++) {
                        if (!uuid[i]) {
                          r = 0 | Math.random()*16;
                          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                        }
                      }
                    }
                 
                    return uuid.join('');
                },
               
               
                
	                getSysConfigInfo: function (key) {
                    var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
                    $http.get(serverUrl + '/rest/v1/sys/config/value?key='+key).
                    success(function (data, status, headers, config) {
                        deferred.resolve(data);  // 声明执行成功，即http请求数据成功，可以返回数据了
                    }).
                    error(function (data, status, headers, config) {

                        deferred.resolve(config);  // 声明执行失败，即服务器返回错误
                    });
                    return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
                },
                
                
                getLanguage: function () {
                    var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
                    $http.get(serverUrl + '/rest/v1/sys/config/language').
                    success(function (data, status, headers, config) {
                        deferred.resolve(data);  // 声明执行成功，即http请求数据成功，可以返回数据了
                    }).
                    error(function (data, status, headers, config) {
                        deferred.resolve({"tvrates": []});  // 声明执行失败，即服务器返回错误
                    });
                    return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
                },
               
                getCategorys: function (mediaType,cpId) {
                    var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
                    $http.get(serverUrl + '/rest/v1/category/treebymodel?models='+mediaType+"&cpId="+cpId).
                        success(function (data, status, headers, config) {
                            deferred.resolve(data);  // 声明执行成功，即http请求数据成功，可以返回数据了
                        }).
                        error(function (data, status, headers, config) {
                            deferred.resolve({"vo": []});   // 声明执行失败，即服务器返回错误
                        });
                    return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API
                },
                
                getMessageTypes: function() {
                    //消息类型1：直播消息 2：活动消息 3：维护通知  4：追剧通知  5：服务通知"livemsg":"直播消息",
                    var types = [
                        {"name": "livemsg", "value": 1},
                        {"name": "actionmsg", "value": 2},
                        {"name": "maintenancenotice", "value": 3},
                        {"name": "playnotice", "value": 4},
                        {"name": "servicenotice", "value": 5},
                    ];

                    return types;
                }
               
               
               
            };
        }]).service('fileUpload', [ '$http', function ($http) {
        this.uploadFileToUrl = function (file, uploadUrl) {
            var fd = new FormData();
            fd.append('file', file);

            return $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                var url = data.pitcureUrl;
                console.log("upload success!");
                $('#returnUrl').val(url);
                alert($('#returnUrl').val());
            }).error(function () {
                console.log("upload failure!");
            });
        };

    } ]).service('chartOptionService', function () {
    	return {
    		getDefaultOption:function(options, chartData){
    			var echartOption = {
    					title : {
    				        text: options.text, subtext: options.subtext, 
    				    },
    	                tooltip : {
    	                    trigger: 'axis',
    	                },
    	                legend: {
    	                    top:30, data: options.legend
    	                },
    	                xAxis : [
    	                    {
    	                        type : 'category',
    	                        boundaryGap : options.ctype == 'bar',
    	                        axisLine:{
    	                        	lineStyle:{color:"rgba(0,0,0,0.6)", width:1}
    	                        },
    	                        data : chartData.keys
    	                    }
    	                ],
    	                yAxis : [
    	                    {
    	                        type : 'value',
    	                        axisLine:{
    	                        	lineStyle:{color:"rgba(0,0,0,0.6)", width:1}
    	                        },
    	                        axisLabel : options.yAxisLabel || {}
    	                    }
    	                ],
    	                series : []
                };
    			
    			for(var i=0, len=options.legend.length; i<len; i++){
    				echartOption.series.push({
                        name: options.legend[i],
                        type: options.ctype,
                        smooth: true, symbol:'emptyCircle', symbolSize:2, showAllSymbol: true,
                        markLine : {
                        	symbolSize: [5,10],
                        	lineStyle: {
                        		normal: {
                        			width:0
                        		}
                        	},
                            data : [
                                {type : 'max', name: '最大值'}
                            ]
                        },
                        markPoint : {
                        	data : [
                        	    {type : 'min', name: '最小值'}
                        	]
                        },
                        data: chartData.values[i]
                    });
    			}
    			console.log('>>>>>>>>> echartOption', echartOption);

    			return echartOption;
    		},
    	};
     }).service('chartOptionEnService', function () {
    	return {
    		getDefaultOption:function(options, chartData){
    			var echartOption = {
    					title : {
    				        text: options.text, subtext: options.subtext, 
    				    },
    	                tooltip : {
    	                    trigger: 'axis',
    	                },
    	                legend: {
    	                    top:30, data: options.legend
    	                },
    	                xAxis : [
    	                    {
    	                        type : 'category',
    	                        boundaryGap : options.ctype == 'bar',
    	                        axisLine:{
    	                        	lineStyle:{color:"rgba(0,0,0,0.6)", width:1}
    	                        },
    	                        data : chartData.keys
    	                    }
    	                ],
    	                yAxis : [
    	                    {
    	                        type : 'value',
    	                        axisLine:{
    	                        	lineStyle:{color:"rgba(0,0,0,0.6)", width:1}
    	                        },
    	                        axisLabel : options.yAxisLabel || {}
    	                    }
    	                ],
    	                series : []
                };
    			
    			for(var i=0, len=options.legend.length; i<len; i++){
    				echartOption.series.push({
                        name: options.legend[i],
                        type: options.ctype,
                        smooth: true, symbol:'emptyCircle', symbolSize:2, showAllSymbol: true,
                        markLine : {
                        	symbolSize: [5,10],
                        	lineStyle: {
                        		normal: {
                        			width:0
                        		}
                        	},
                            data : [
                                {type : 'max', name: 'Maximum'}
                            ]
                        },
                        markPoint : {
                        	data : [
                        	    {type : 'min', name: 'Minimum'}
                        	]
                        },
                        data: chartData.values[i]
                    });
    			}
    			console.log('>>>>>>>>> echartOption', echartOption);
    			return echartOption;
    		},
    	};
     }).service('commonMethod', ['ngDialog','$rootScope', function(ngDialog,$rootScope) {
    	 return {
    	 	//确认弹框
    		 confirmDialog: function(message, revokeFlag) {
    			 var templateUrl;
    			 $rootScope.message = message;
    			 
    			 if(revokeFlag != undefined) {
    				 $rootScope.revokeFlag = revokeFlag;
    				 templateUrl = 'common/deleteDlg.html';
    			 }else {
    				 templateUrl = 'common/confirmDlg.html';
    			 }
           	  	 return ngDialog.openConfirm({
             		template: templateUrl,
             		scope: $rootScope,
             		closeByDocument: false,
             		width:300
             	})
             },
             //提示弹框
            tipDialog: function(message,message2,message3) {
                 $rootScope.message = message;
                 $rootScope.message2 = message2;
                 $rootScope.message3 = message3;
           	 	return ngDialog.openConfirm({
           	 		template: 'common/tipDlg.html',
           	 		scope: $rootScope,
           	 		closeByDocument: false,
           	 		width:300
           	 	});
            },
            openPicDialog: function(uploadFlag,$event) {
            	$rootScope.uploadFlag = uploadFlag;
            	return ngDialog.open({
            		template: 'common/addPic/addPic.html',	
	                appendClassName: 'add-pic',
	                scope: $rootScope,
	                controller: 'addPicCtrl',
	                closeByDocument: false,
	                width:300
            	})
            },
            openMoiveDialog: function() {
            	return ngDialog.openConfirm({
            		template: 'common/selectMovie/selectMovie.html',
            		appendClassName: 'moive-dialog',
	                scope: $rootScope,
	                controller: 'selectMovieCtrl',
	                closeByDocument: false,
            	})
            },
            retry : function(contentIds,mediaType){
				$rootScope.contentIds = contentIds;
				$rootScope.mediaType = mediaType;
	      		return ngDialog.openConfirm({
	       			templateUrl:"common/retry/retryDialog.html",
	       			scope:$rootScope,
	       			width:512,
	       	 		closeByDocument: false,
	       	 		controller:"retryController",
	       		});
			},
			labels : function(contentIds,mediaType){
				$rootScope.contentIds = contentIds;
				$rootScope.mediaType = mediaType;
	      		return ngDialog.openConfirm({
	       			templateUrl:"common/labels/labelsDialog.html",
	       			scope:$rootScope,
	       			width:512,
	       	 		closeByDocument: false,
	       	 		controller:"labelsController",
	       		});
			},
			oneKeyShutdownDialog: function() {
            	return ngDialog.openConfirm({
            		template: 'common/selectMovie/oneKeyShutdown.html',
            		appendClassName: 'moive-dialog',
	                scope: $rootScope,
	                controller: 'oneKeyShutdownCtrl',
	                closeByDocument: false,
            	})
            }
    	 }
     }]).service('loginService', ['$window',"$injector", function ($window,$injector) {
    	 var strPath = window.document.location.pathname; 
         var appName = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
         var loginFlag = sessionStorage.getItem("login");
         var loginBool=false;
         return {
            isLogin: function () {
            	if(loginFlag=="true"){
            		loginBool=true;
            	}
            	return loginBool;
            },
            setLoginFlag: function (value) {
                sessionStorage.setItem("login", value);
            },
            setToken: function (value) {
                sessionStorage.setItem("token", value);
            },
            getToken: function () {
            	var token = sessionStorage.getItem("token");
                return token;
            },
            gotoLoginPage: function () {
            	sessionStorage.removeItem("token");
            	sessionStorage.setItem("login", false);
                var host = $window.location.host;
                console.log(">>>>>host ip:" + host);
                $window.location.href = "http://" + host + appName+"/login.html";
            },
            gotoIndexPage: function () {
            	var host = $window.location.host;
//                if(sessionStorage.getItem("password")=="111111"){
//                	$window.location.href = "http://" + host +appName+ "/index.html#/security/user/edit";
//                }
//                else 
                if (sessionStorage.username == 'sysadmin') {	
                	$window.location.href = "http://" + host +appName+ "/index.html#/security/role/list/0";
				} else {
					$window.location.href = "http://" + host + appName+"/index.html#/security/role/list/0";
				}
            },
            logout:function(){
        		var $http = $injector.get('$http');
            	var host = $window.location.host;
             	$http["delete"]("http://" + host + appName+"/rest/v1/logout")
        		.success(function(){
        			for(var key in sessionStorage){
        				if(key != "language") sessionStorage.removeItem(key);
        			}
        			sessionStorage.setItem('login', false);
        			localStorage.setItem("exit",Date.now());
        			localStorage.removeItem("exit");
                	sessionStorage.removeItem("token");
                    sessionStorage.setItem("login", false);
                    sessionStorage.removeItem("ContentProviders");
                    var host = $window.location.host;
                    console.log(">>>>>host ip:" + host);
                    $window.location.href = "http://" + host + appName+"/login.html";
        		})
            }
        }
    }]).factory('rootService',function () {
    	var privileges = sessionStorage.getItem("privileges") != null ? sessionStorage.getItem("privileges").split(","):{};
    	var rootPris = {};
    	for(var i = 0;i<privileges.length;i++){
    		rootPris[privileges[i]] = true;
    	}
    	return rootPris;
    }).factory('sortKey', function () {//数组排序
        var sortBykey = function (array, key, direction) {
            return array.sort(function (a, b) {
                var x = parseInt(a[key], 10), y = parseInt(b[key], 10);
                if (isNaN(x)) {
                    x = a[key];
                }
                if (isNaN(y)) {
                    y = b[key];
                }
                if (direction == 'asc') {
                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                } else {
                    return ((x > y) ? -1 : ((x > y) ? 1 : 0));
                }
            });
        };
        return {
            sortByKey: sortBykey
        };
    });
