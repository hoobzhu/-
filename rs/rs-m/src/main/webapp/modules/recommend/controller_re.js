'use strict';

/* Controllers */

angular.module('myApp.controllers')
.controller('resimilarcontentCtrl', ['$rootScope','$scope', '$http', 'sharedProperties','loginService','$window','$routeParams','$q','$log','commonMethod',function($rootScope,$scope, $http, sharedProperties, loginService,$window,$routeParams,$q,$log,commonMethod) {

	$scope.getPageData = function() {
		if ($scope.pagingOptions.currentPage == 0) {
			$scope.pagingOptions.currentPage = 1;
		}

		var url = sharedProperties.getServerUrl()+"/rest/v1/resimilarcontent?contentid="+$scope.queryOptions.contentId+"&begin="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&pagesize="+$scope.pagingOptions.pageSize;
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.pagingOptions.total = largeLoad.total;//存放list总记录数
			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			$scope.items = largeLoad.list;
		}).error(function(){

		});
	};
	// 搜索
	$scope.search = function() {
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	$scope.reset = function() {
		$scope.queryOptions = {//查询字段
				contentId: null,
				name: null,

		};
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	//select a row
	$scope.selectRow = function(item) {
		$scope.selectItem = item;
	};

	//判断是否选择某个记录
	$scope.selected = function() {
		if ($scope.selectItem == null) {
			return true;
		} else {
			return false;
		}
	};


	$scope.savePageOptions = function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
		sessionStorage.setItem("parentLocation", window.location.href);
		$rootScope.user_pagingOptions = $scope.pagingOptions;
		$rootScope.user_queryOptions = $scope.queryOptions;
	};

	$scope.deleteItem = function() {
		$scope.message = $rootScope.l10n.areyousure;
		commonMethod.confirmDialog($scope.message).then(function(value) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/resimilarcontent/delete?id='+$scope.selectItem.id)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$scope.getPageData();
				}
			}).error(function(data,status){
				$scope.getPageData();
			});
		});
	};
	$scope.init = function() {
		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.user_pagingOptions) {
			$scope.pagingOptions = $rootScope.user_pagingOptions;
			$scope.queryOptions = $rootScope.user_queryOptions;
		} else {
			$scope.pagingOptions = {//分页字段
					pageSizes: [10, 20, 30, 50, 100],
					pageSize: 10,
					currentPage: 1,
					total: 0,//存放list总记录数
					maxPages: 0,//最大页数
			};
			$scope.queryOptions = {//查询字段
					contentId: null,
					name: null
			};
		}
		$scope.selectItem = null;
		$scope.getPageData();
	};

	$scope.init();
}])
.controller('reusercontentCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope', '$q','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope, $q,commonMethod){
	$scope.getPageData = function() {
		if ($scope.pagingOptions.currentPage == 0) {
			$scope.pagingOptions.currentPage = 1;
		}

		var url = sharedProperties.getServerUrl()+"/rest/v1/reusercontent?userid="+$scope.queryOptions.contentId+"&begin="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&pagesize="+$scope.pagingOptions.pageSize;
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.pagingOptions.total = largeLoad.total;//存放list总记录数
			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			$scope.items = largeLoad.list;
		}).error(function(){

		});
	};
	// 搜索
	$scope.search = function() {
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	$scope.reset = function() {
		$scope.queryOptions = {//查询字段
				userId: null,
				name: null,

		};
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	//select a row
	$scope.selectRow = function(item) {
		$scope.selectItem = item;
	};

	//判断是否选择某个记录
	$scope.selected = function() {
		if ($scope.selectItem == null) {
			return true;
		} else {
			return false;
		}
	};


	$scope.savePageOptions = function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
		sessionStorage.setItem("parentLocation", window.location.href);
		$rootScope.user_pagingOptions = $scope.pagingOptions;
		$rootScope.user_queryOptions = $scope.queryOptions;
	};

	$scope.deleteItem = function() {
		$scope.message = $rootScope.l10n.areyousure;
		commonMethod.confirmDialog($scope.message).then(function(value) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/reusercontent/delete?id='+$scope.selectItem.id)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$scope.getPageData();
				}
			}).error(function(data,status){
				$scope.getPageData();
			});
		});
	};
	$scope.init = function() {
		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.user_pagingOptions) {
			$scope.pagingOptions = $rootScope.user_pagingOptions;
			$scope.queryOptions = $rootScope.user_queryOptions;
		} else {
			$scope.pagingOptions = {//分页字段
					pageSizes: [10, 20, 30, 50, 100],
					pageSize: 10,
					currentPage: 1,
					total: 0,//存放list总记录数
					maxPages: 0,//最大页数
			};
			$scope.queryOptions = {//查询字段
					userId: null,
					name: null
			};
		}
		$scope.selectItem = null;
		$scope.getPageData();
	};

	$scope.init();
}])
.controller('retopncontentCtrl', ['$scope','$http','sortKey','$location','sharedProperties','loginService','$routeParams','$q','$rootScope',function($scope,$http,sortKey,$location,sharedProperties,loginService,$routeParams,$q,$rootScope){
	$scope.getPageData = function() {

		var url = sharedProperties.getServerUrl()+"/rest/v1/retopncontent?begin="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&pagesize="+$scope.pagingOptions.pageSize;;
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.pagingOptions.total = largeLoad.total;//存放list总记录数
			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			$scope.items = largeLoad.list;
		}).error(function(){

		});
	};
	// 搜索
	$scope.search = function() {
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	$scope.reset = function() {
		$scope.queryOptions = {//查询字段
				userId: null,
				name: null,

		};
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	//select a row
	$scope.selectRow = function(item) {
		$scope.selectItem = item;
	};

	//判断是否选择某个记录
	$scope.selected = function() {
		if ($scope.selectItem == null) {
			return true;
		} else {
			return false;
		}
	};

	$scope.savePageOptions = function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
		sessionStorage.setItem("parentLocation", window.location.href);
		$rootScope.user_pagingOptions = $scope.pagingOptions;
		$rootScope.user_queryOptions = $scope.queryOptions;
	};
	$scope.deleteItem = function() {
		$scope.message = $rootScope.l10n.areyousure;
		commonMethod.confirmDialog($scope.message).then(function(value) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/retopncontent/delete?id='+$scope.selectItem.id)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$scope.getPageData();
				}
			}).error(function(data,status){
				$scope.getPageData();
			});
		});
	};
	$scope.init = function() {
		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.user_pagingOptions) {
			$scope.pagingOptions = $rootScope.user_pagingOptions;
			$scope.queryOptions = $rootScope.user_queryOptions;
		} else {
			$scope.pagingOptions = {//分页字段
					pageSizes: [10, 20, 30, 50, 100],
					pageSize: 10,
					currentPage: 1,
					total: 0,//存放list总记录数
					maxPages: 0,//最大页数
			};
			$scope.queryOptions = {//查询字段
					userId: null,
					name: null
			};
		}
		$scope.selectItem = null;
		$scope.getPageData();
	};
	$scope.init();
}]).controller('reusercontentEditCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope', '$q','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope, $q,commonMethod){
	$scope.repeat=false;
	$scope.checkContentId=function(){
		$scope.repeat=false;
		var url = sharedProperties.getServerUrl()+"/rest/v1/reusercontent/check?userid="+$scope.data.userId
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			if(largeLoad.resultCode==1){
				$scope.repeat=true;
			}
		}).error(function(){

		});
	};
	$scope.getData = function() {
		var url = sharedProperties.getServerUrl()+"/rest/v1/reusercontent/detail?id="+$scope.id
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.data = largeLoad.vo;
		}).error(function(){

		});
	};
	$scope.init = function() {
		$scope.id = $routeParams.id;
		if($scope.id!=0){
			$scope.getData();
		}

	};
	$scope.saveItem = function() {
		if ($scope.id == 0) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/reusercontent/add',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/reusercontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/reusercontent/list/1');	
			});
		} else {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/reusercontent/edit',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/reusercontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/reusercontent/list/1');	
			});

		};
	};
	$scope.init();
}]).controller('resimilarcontentEditCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope', '$q','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope, $q,commonMethod){
	
	$scope.repeat=false;
	$scope.checkContentId=function(){
		$scope.repeat=false;
		var url = sharedProperties.getServerUrl()+"/rest/v1/resimilarcontent/check?contentid="+$scope.data.contentId
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			if(largeLoad.resultCode==1){
				$scope.repeat=true;
			}
		}).error(function(){

		});
	};
	
	$scope.getData = function() {
		var url = sharedProperties.getServerUrl()+"/rest/v1/resimilarcontent/detail?id="+$scope.id
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.data = largeLoad.vo;
		}).error(function(){

		});
	};
	$scope.init = function() {
		$scope.id = $routeParams.id;
		if($scope.id!=0){
			$scope.getData();
		}

	};
	$scope.saveItem = function() {
		if ($scope.id == 0) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/resimilarcontent/add',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/resimilarcontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/resimilarcontent/list/1');	
			});
		} else {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/resimilarcontent/edit',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/resimilarcontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/resimilarcontent/list/1');	
			});

		};
	};
	$scope.init();
}]).controller('retopncontentEditCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope', '$q','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope, $q,commonMethod){
	
	$scope.repeat=false;
	$scope.checkContentId=function(){
		$scope.repeat=false;
		var url = sharedProperties.getServerUrl()+"/rest/v1/retopncontent/check?contentid="+$scope.data.contentId
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			if(largeLoad.resultCode==1){
				$scope.repeat=true;
			}
		}).error(function(){

		});
	};
	$scope.getData = function() {
		var url = sharedProperties.getServerUrl()+"/rest/v1/retopncontent/detail?id="+$scope.id
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.data = largeLoad.vo;
		}).error(function(){

		});
	};
	$scope.init = function() {
		$scope.id = $routeParams.id;
		if($scope.id!=0){
			$scope.getData();
		}

	};
	$scope.saveItem = function() {
		if ($scope.id == 0) {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/retopncontent/add',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/retopncontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/retopncontent/list/1');	
			});
		} else {
			$http.post(sharedProperties.getServerUrl()+'/rest/v1/retopncontent/edit',$scope.data)
			.success(function(data) {
				if (data.resultCode != 0) {//新增失败						
					$scope.message = $rootScope.l10n.saveFail;
					commonMethod.tipDialog($scope.message);
				} else {
					$location.path('/retopncontent/list/1');
				}
			}).error(function(data,status){
				$location.path('/retopncontent/list/1');	
			});

		};
	};
	$scope.init();
}])
;