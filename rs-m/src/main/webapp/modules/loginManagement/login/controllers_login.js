'use strict';

/* Controllers */

angular.module('login.controllers', [])
.controller('LoginCtrl',['$scope','$http','$log','$window','sharedProperties','loginService','$translate','$rootScope','$cookieStore',function($scope, $http,$log,$window, sharedProperties, loginService,$translate,$rootScope,$cookieStore) {
	// 若发现token存在直接跳转到index界面
	if (sessionStorage.getItem("token") != null && sessionStorage.getItem("token") != "") {
		if(sessionStorage.getItem("login") == "true"){
			loginService.gotoIndexPage();
		}else{
			sessionStorage.removeItem("token");
		}
	}
	$scope.requestid = "123456";
	$http.get(sharedProperties.getServerUrl()+"/rest/v1/sys/config/language")
	.success(function(data){
		var data = data.vo;
		$rootScope.lang1 = data.substring(0,data.indexOf(","));
		$rootScope.lang2 = data.substring(data.indexOf(",")+1);
		$rootScope.lang11 = $rootScope.lang1.replace("-","_");
		$rootScope.lang22 = $rootScope.lang2.replace("-","_");
		
		$rootScope.language = (sessionStorage.getItem("language")=="undefined" || sessionStorage.getItem("language")==null || sessionStorage.getItem("language")=="")?$rootScope.lang1:sessionStorage.getItem("language");
		$translate.use($rootScope.language);
		sessionStorage.setItem('language', $rootScope.language);
		$scope.getLanguageJson($rootScope.language);
		$scope.username = "";
		$scope.password = "";
		$scope.captcha = "";
		$scope.data = "";
		$scope.showMessage = false;
		if ($rootScope.language==$rootScope.lang1) {
			$("#lang2").attr("checked",false);
			$("#lang1").attr("checked",true);
		} else {
			$("#lang1").attr("checked",false);
			$("#lang2").attr("checked",true);
		}
	}).error(function (data) {});
	$scope.changeLanguage = function(lang,flag) {
		if (flag == 1) {
			$("#lang2").attr("checked",false);
			$("#lang1").attr("checked",true);
		} else {
			$("#lang1").attr("checked",false);
			$("#lang2").attr("checked",true);
		}
		$translate.use(lang);
		$rootScope.language=lang;
		sessionStorage.setItem('language', $rootScope.language);
		$scope.getLanguageJson(lang);
	};
	$scope.getLanguageJson=function(lang){
		$http.get(sharedProperties.getServerUrl()+"/l10n/"+lang+".json")
		.success(function(data){
			$rootScope.l10n = data;
		});
	};
	
	$scope.flushValidateCode = function() {
		var validateImgObject = document.getElementById("codeValidateImg");
		validateImgObject.src = sharedProperties.getServerUrl() + "/rest/v1/generate/captcha?requestid="+$scope.requestid+"&language=1"+"&rad="+Math.random();
	};
	
	
	

	$scope.dologin = function() {
		var fd = new FormData(document.getElementById("login_form"));
		var username = $scope.username;
		var password = $scope.password;
	    var captcha = $scope.captcha;
	    
		if(!username || !password) {
			$scope.showMessage = false;
			$scope.isNull = true;
		}else{
			$scope.username= $scope.username.toLowerCase();
			username=username.toLowerCase();
			if (username != null && username != "") {
				username = encodeURIComponent(username);
			}
			if (password != null && password != "") {
				var encryptedPwd = CryptoJS.MD5(CryptoJS.MD5(password).toString()).toString();
				encryptedPwd = encodeURIComponent(encryptedPwd);
			}
			if (captcha != null && captcha != "") {
				captcha = encodeURIComponent(captcha);
			}
			fd.append("username", username);
			fd.append("password", password);
			fd.append("captcha", captcha);
			
			$http.get(sharedProperties.getServerUrl() + '/rest/v1/security/publicKey?userId=' + username).success(function (data) {
                $scope.resultCode = data.resultCode;
                if (data.resultCode == 0) {
                    var publicKey = data.vo;
                    sessionStorage.setItem('rsaPublicKey', publicKey);
                    var rsaEncrypt = new JSEncrypt();
                    rsaEncrypt.setPublicKey(publicKey);
                    var rsaEncryptedPwd = rsaEncrypt.encrypt(encryptedPwd);
                    $scope.data = {
                        'userId': username,
                        'password': rsaEncryptedPwd,
                        'captcha': captcha,
                        'requestId': $scope.requestid
                    };
					$http.post(sharedProperties.getServerUrl() + '/rest/v1/login', $scope.data)
						.success(function(data) {
							console.log(data);
							$scope.resultCode=data.resultCode;
							if (data.resultCode == 20003) {
								$scope.captcha = "";
								$scope.showMessageCaptcha = true;
								$scope.flushValidateCode();
								$log.info(">>>>>login failed.");
							} else if(data.resultCode == 20005){
								//$scope.username = "";
								$scope.showMessage = true;
								$scope.captcha = "";
								$scope.flushValidateCode();
								$log.info(">>>>>login failed.");
							}else if(data.resultCode == 20011){
								$scope.showMessage = true;
								$scope.captcha = "";
								$scope.flushValidateCode();
								$log.info(">>>>>login failed.");
							}else if(data.resultCode == 20012){
								$scope.showMessage = true;
								$scope.captcha = "";
								$scope.flushValidateCode();
								$log.info(">>>>>login failed.");
							}else if(data.resultCode == 0){
								$log.info(">>>>>login success,token str:" , data);
								sessionStorage.setItem('username', $scope.username);
								$rootScope.username = $scope.username;
								//sessionStorage.setItem('password', encryptedPwd);
								sessionStorage.setItem('language', $rootScope.language);
								//保存language的cookie
		                        $cookieStore.put("language", $rootScope.language);
								sessionStorage.setItem('privileges',data.privileges);
								sessionStorage.setItem("role",data.role);
								$rootScope.role=data.role;
								loginService.setLoginFlag(true);
								loginService.setToken(data.token);
								loginService.gotoIndexPage();
							}
						})
						.error(function(xhr, data) {
							$scope.password = "";
							alert($rootScope.l10n.loginFail);
							$log.info(">>>>>login failed.");
						});
                } else {
		            $scope.showMessage = true;
		            $scope.captcha = "";
		            $scope.flushValidateCode();
		            $log.info(">>>>>login failed, get rsa publicKey error.");
		        }
		    }).error(function (data) {
		        $scope.password = "";
		        alert($rootScope.l10n.loginFail);
		        $log.info(">>>>>login failed, get rsa publicKey error.");
		    });
		} 
	};



		//top，bottom上下占比
		$scope.loginTop=Math.max(($(document).height()-346-25-10)*0.5,128)+"px";
		$scope.loginBottom=Math.max(($(document).height()-346-25-10)*0.5,128)+"px";
		
	}]);
