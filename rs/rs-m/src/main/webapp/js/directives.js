'use strict';

/* Directives */


angular.module('myApp.directives', []).
directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
        elm.text(version);
    };
}])
    .directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function(){
                    scope.$apply(function(){
                        if (element[0].files) {
                            modelSetter(scope, element[0].files[0]);
                        } else {
                            modelSetter(scope, element[0].children[0].files[0]);
                        }

                    });
                });
            }
        };
    }])
    /**
     * table 列大小可拖动
     */
    .directive('colresizeable', function() {
        return {
            restrict: 'A',
            link: function(scope, elem) {
                setTimeout(function() {
                    elem.colResizable({
                        //具体配置google colResizeable
                    });
                },1000);
            }
        };
    })

    /*
     * 创建自定义翻页指令
     */
    
    .directive('atSelectstatustoseries',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" authFlag="0" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"no-authorise"  | translate}}</label>' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig" authFlag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"authorised" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("authFlag");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.authFlag=$status;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        var $status = $(this).attr("authFlag");
                        $scope.queryOptions.authFlag=$status==0 ? 1: 0;
                        $scope.getPageData();
                    }
                });
            }
        }
    })
       .directive('atSelectstatustoserieswhile',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" authFlag="0" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"whilelitsbook"  | translate}}{{"no-select"  | translate}}</label>' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig" authFlag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"whilelitsbook"  | translate}}{{"selected" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("authFlag");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.authFlag=$status;
                    	$scope.pagingOptions.currentPage = 1;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        var $status = $(this).attr("authFlag");
                        $scope.queryOptions.authFlag=$status==0 ? 1: 0;
                        $scope.getPageData();
                    }
                });
            }
        }
    })
        .directive('atSelectstatustoseriesblack',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" authFlag="0" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"blacklitsbook"  | translate}}{{"no-select"  | translate}}</label>' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig" authFlag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"blacklitsbook"  | translate}}{{"selected" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("authFlag");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.authFlag=$status;
                    	$scope.pagingOptions.currentPage = 1;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        var $status = $(this).attr("authFlag");
                        $scope.queryOptions.authFlag=$status==0 ? 1: 0;
                        $scope.getPageData();
                    }
                });
            }
        }
    })
    .directive('atSelectstatustochannel',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" authFlag="0" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"no-authorise"  | translate}}</label>' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig" authFlag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"authorised" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("authFlag");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.authFlag=$status;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        var $status = $(this).attr("authFlag");
                        $scope.queryOptions.authFlag=$status==0 ? 1: 0;
                        $scope.getPageData();
                    }
                });
            }
        }
    })
    .directive('atSelectstatustoservice',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" authFlag="0" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"no-authorise"  | translate}}</label>' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig" authFlag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"authorised" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("authFlag");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.authFlag=$status;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        var $status = $(this).attr("authFlag");
                        $scope.queryOptions.authFlag=$status==0 ? 1: 0;
                        $scope.getPageData();
                    }
                });
            }
        }
    })
    
      .directive('fsvSelectstatusbycdn',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
            	list:'&',
            	setPublishStatus:'&'
            },
            template:
            '<div class="pull-right">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-ava" name="radio-fig" id="radio-ava" ng-model="checked[2]" ng-change="changeStatus(2)">' +
            '<label for="radio-ava" style="color: #82af6f">{{"_publishsuccess" | translate}}</label>' +
            '<input type="checkbox" class="radio-pro" name="radio-fig" ng-model="checked[0]" ng-change="changeStatus(0)" id="radio-pro">' +
            '<label for="radio-pro" style="color: #a0a0a0">{{"_initial" | translate}}</label>' +
            '<input type="checkbox" class="radio-part" id="radio-part" name="radio-fig" ng-model="checked[1]" ng-change="changeStatus(1)">' +
            '<label for="radio-part" style="color: #d68748">{{"_publishing" | translate}}</label>' +
            '<input type="checkbox" name="radio-fig" class="radio-unava" ng-model="checked[3]" ng-change="changeStatus(3)" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"_publishfailed" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $scope.checked = {"2":false,"4":false,"0":false,"3":false};

                $scope.changeStatus = function(status){
                    for(var k in $scope.checked){
                        if(k != status){
                            $scope.checked[k] = false;
                        }
                    }
                    if($scope.checked[status]){
                        $scope.setPublishStatus({'status':status});
                        $scope.list();
                    }else{
                        $scope.setPublishStatus({'status':""});
                        $scope.list();
                    }

                    
                }
            }
        }
    })
    .directive('fsvSelectstatusbycdnV2',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
            	list:'&',
            	publishStatus:'=ngModel'
            },
            template:
            	'<div class="pull-right">' +
                '<div class="fig-tab">' +
                '<input type="checkbox" class="radio-ava" name="radio-fig" ng-model="publishStatus" id="radio-ava" data-type="2">' +
                '<label for="radio-ava" style="color: #82af6f">{{"_publishsuccess" | translate}}</label>' +
                '<input type="checkbox" class="radio-pro" name="radio-fig" ng-model="publishStatus" data-type="0" id="radio-pro">' +
                '<label for="radio-pro" style="color: #a0a0a0">{{"_initial" | translate}}</label>' +
                '<input type="checkbox" class="radio-part" id="radio-part" ng-model="publishStatus" name="radio-fig" data-type="1">' +
                '<label for="radio-part" style="color: #d68748">{{"_publishing" | translate}}</label>' +
                '<input type="checkbox" name="radio-fig" class="radio-unava" ng-model="publishStatus" data-type="3" id="radio-unava">' +
                '<label for="radio-unava" style="color: #d20000">{{"_publishfailed" | translate}}</label>' +
                '</div>' +
                '</div>',
            link: function($scope, element, attrs) {
            	$(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");

                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("data-type");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.publishStatus = $status;
                    }else {
                        $flag = true;
                        $scope.publishStatus='';
                    }
                    $scope.$$postDigest($scope.list);
                });
                
                $scope.$watch('publishStatus', function (newVal, oldVal) {
            		if (newVal !== oldVal) {
            			$(".fig-tab").find("input").each(function(index){
            		       if($(this).attr("data-type")==newVal){
            		    	   $(this).prop('checked',true);
            		       }
            		       if($(this).attr("data-type")==oldVal){
            		    	   $(this).prop('checked',false);
            		       }
            		    });
            		}
            	}, true);
            }
        }
    })
    .directive('atSelectstatusbycdn',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="pull-right">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-ava" name="radio-fig" id="radio-ava" data-type="2">' +
            '<label for="radio-ava" style="color: #82af6f">{{"_publishsuccess" | translate}}</label>' +
            '<input type="checkbox" class="radio-pro" name="radio-fig" data-type="0" id="radio-pro">' +
            '<label for="radio-pro" style="color: #a0a0a0">{{"_initial" | translate}}</label>' +
            '<input type="checkbox" class="radio-part" id="radio-part" name="radio-fig" data-type="1">' +
            '<label for="radio-part" style="color: #d68748">{{"_publishing" | translate}}</label>' +
            '<input type="checkbox" name="radio-fig" class="radio-unava" data-type="3" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"_publishfailed" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");

                    if($flag){
                        // $flag_default = $flag;
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("data-type");
                        // 判断 查询cdn---objectStatus(cdnStatus)
                        $scope.queryOptions.totalPublishStatus=$status;
                        $scope.getPageData();
                    }else {
                        // $(".fig-tab").find('input').prop('checked',true);
                        // $(this).prop('checked', false);
                        $flag = true;
                        $scope.queryOptions.totalPublishStatus='null';
                        $scope.getPageData();
                    }
                });
                $scope.$watch('queryOptions.totalPublishStatus', function (newVal, oldVal) {
            		if (newVal !== oldVal) {
            			$(".fig-tab").find("input").each(function(index){
            		       if($(this).attr("data-type")==newVal){
            		    	   $(this).prop('checked',true);
            		       }
            		       if($(this).attr("data-type")==oldVal){
            		    	   $(this).prop('checked',false);
            		       }
            		    });
            		}
            	}, true);
            }
        }
    }).directive('atSelectstatustointeract',function() {
    return {
        restrict: 'EA',
        replace: true,
        scope: true,
        template:
        '<div class="col-xs-4" style="z-index: 9999; float: right;">' +
        '<div class="fig-tab">' +
        '<input type="checkbox" class="radio-unava" name="radio-fig" actionFlag="2" id="radio-unava">' +
        '<label for="radio-unava" style="color: #d20000">{{"no-select"  | translate}}</label>' +
        '<input type="checkbox" class="radio-ava"   name="radio-fig" actionFlag="1" id="radio-ava" checked="checked" >' +
        '<label for="radio-ava"   style="color: #82af6f">{{"selected" | translate}}</label>' +
        '</div>' +
        '</div>',
        link: function($scope, element, attrs) {
            $(".fig-tab").find("input").click(function() {
                var $flag = $(this).is(":checked");
                if($flag){
                    $(this).siblings("input").attr("checked",false);
                    $flag = false;
                    var $status = $(this).attr("actionFlag");
                    $scope.queryOptions.actionFlag=$status;
                    $scope.getPageData();
                }else {
                    $(".fig-tab").find('input').prop('checked',true);
                    $(this).prop('checked', false);
                    $flag = true;
                    $scope.queryOptions.actionFlag=null;
                    $scope.getPageData();
                }
            });
        }
    }
}).directive('atSelectstatustolabel',function() {
    return {
        restrict: 'EA',
        replace: true,
        scope: true,
        template:
        '<div class="col-xs-4" style="z-index: 9999; float: right;">' +
        '<div class="fig-tab">' +
        '<input type="checkbox" class="radio-unava" name="radio-fig" editType="2" id="radio-unava">' +
        '<label for="radio-unava" style="color: #d20000">{{"no-select"  | translate}}</label>' +
        '<input type="checkbox" class="radio-ava"   name="radio-fig" editType="1" id="radio-ava" checked="checked" >' +
        '<label for="radio-ava"   style="color: #82af6f">{{"selected" | translate}}</label>' +
        '</div>' +
        '</div>',
        link: function($scope, element, attrs) {
            $(".fig-tab").find("input").click(function() {
                var $flag = $(this).is(":checked");
                if($flag){
                    $(this).siblings("input").attr("checked",false);
                    $flag = false;
                    var $status = $(this).attr("editType");
                    $scope.queryOptions.editType=$status;
                    $scope.pagingOptions.currentPage = 1;
                    $scope.getPageData();
                }else {
                    $(".fig-tab").find('input').prop('checked',true);
                    $(this).prop('checked', false);
                    $flag = true;
                    var $status = $(this).attr("editType");
                    $scope.queryOptions.editType=$status==1 ? 2: 1;
                    $scope.pagingOptions.currentPage = 1;
                    $scope.getPageData();
                }
            });
        }
    }
})
//栏目选择框指令
    .directive('selectCategory', function() {
        return {
            restrict: 'E',
            scope: true,
            templateUrl: 'common/selectCategory/selectCategoryNew.html',
            link: function($scope, element, attrs) {
            }
        }
    })
    //审核指令
    .directive('atSelectstatustoreview',function() {
        return {
            restrict: 'EA',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-4" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-pro" name="radio-fig" data-type="0" id="radio-pro" checked>' +
            '<label for="radio-pro" style="color: #a0a0a0">{{"unReview" | translate}}</label>' +
            '<input type="checkbox" class="radio-ava" name="radio-fig" id="radio-ava" data-type="1">' +
            '<label for="radio-ava" style="color: #82af6f">{{"reviewPassed" | translate}}</label>' +
            '<input type="checkbox" name="radio-fig" class="radio-unava" data-type="-1" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"reviewUnpassed" | translate}}</label>' +


            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {
                    var $flag = $(this).is(":checked");
                    if($flag){
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("data-type");
                        $scope.queryOptions.auditStatus=$status;
                        $scope.getPageData();
                    }else {
                        //$(".fig-tab").find('input').prop('checked',true);
                        // $(this).prop('checked', false);
                        $(this).siblings("input").attr("checked",false);
                        $("#radio-ava").prop('checked',true);
                        $flag = true;
                        $scope.queryOptions.auditStatus=1;
                        $scope.getPageData();
                    }
                });
            }
        }
    }) .directive('atSelectservice',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: false,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig"  edittypeflag="1" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"associated" | translate}}</label>' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" edittypeflag="2" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"not_associated"  | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("edittypeflag");
                        $scope.editType=$status;
                        $scope.reset();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        $scope.editType=1;
                        $scope.reset();
                    }
                });
            }
        }
    }).directive('atSelectcategory',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: false,
            template:
            '<div class="col-xs-4" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-ava"   name="radio-fig"  associatedType="0" id="radio-ava" checked="checked" >' +
            '<label for="radio-ava"   style="color: #82af6f">{{"associated" | translate}}</label>' +
            '<input type="checkbox" class="radio-unava" name="radio-fig" associatedType="1" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"not_associated"  | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("associatedType");
                        $scope.associatedType=$status;
                    	$scope.pagingOptions.currentPage=1;
                        $scope.getPageData();
                    }else {
                        $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        $scope.associatedType=0;
                        $scope.pagingOptions.currentPage=1;
                        $scope.getPageData();
                    }
                });
            }
        }
    }).directive('atForservice',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: false,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab1">' +
            '<input type="checkbox" class="radio-unava"   name="radio-fig"  edittypeflag="1" id="radio-unava1" checked="checked" >' +
            '<label for="radio-unava1"   style="color: #d20000">{{"not_associated" | translate}}</label>' +
            '<input type="checkbox" class="radio-ava" name="radio-fig" edittypeflag="2" id="radio-ava1">' +
            '<label for="radio-ava1" style="color: #82af6f">{{"associated"  | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab1").find("input").click(function() {

                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("edittypeflag");
                        $scope.editType=$status;
                      //  $scope.getServiceData();
                        $scope.forServiceRest();
                    }else {
                        $(".fig-tab1").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $flag = true;
                        $scope.editType=1;
                      //  $scope.getServiceData();
                        $scope.forServiceRest();
                    }
                });
            }
        }
    })
    //关联内容选项指令
    .directive('associatedContent', function() {
        return {
            restrict: 'E',
            scope: false,
            templateUrl: 'common/associatedContent/associatedContent.html',
            link: function($scope, element, attrs) {
            }
        }
    }).directive('selectService',['$http','sharedProperties', function($http,sharedProperties) {
        return {
            restrict: 'E',
            scope: false,
            templateUrl: 'common/selectService/forSelectService.html',
            link: function($scope, element, attrs) {
        	    //合集关联服务开始
        	    
         	    $scope.checkAll2=false;
         	    $scope.openServiceDomal=function(item,type){
         	    	 $('#modalForService').modal('toggle');	
         	    	 $scope.selectItem=item;
         	    	 $scope.selectModel=type;
         	    	 $scope.getServiceData();
         	    };
         	    $scope.closeServiceDomal=function(){
         	    	 $('#modalForService').modal('toggle');	
         	    };
         	    //加载的服务内容进行分页操作js
         	    //分页参数控制器
         	    $scope.pagingOptionsForService = {//分页字段
         				pageSizes: [5,10, 20, 30],
         				pageSize: 5,
         			    currentPage: 1,
         			    total: 0,//存放list总记录数
         			    maxPages: 0,//最大页数
         		};
         	    //查询条件控制器
         		$scope.queryOptionsForService = {//查询字段
         				name: null,
         		};
         		$scope.selectForServiceselectForService = function(item) {
         			 
         				$scope.selectForServiceData= item;
         			};
         	    // --分页函数 start--
         		$scope.cantPageBackwardForService = function(pagingOptions,flag) {// 判断是否可以向前一页或者跳转到第一页
         			if (pagingOptions.currentPage == "" || pagingOptions.currentPage <= 1) {
         				return true;
         			} else {
         				return false;
         			}
         		};
         		$scope.pageToFirstForService = function(pagingOptions) {
         			$scope.pagingOptionsForService.currentPage = 1;
         			$scope.getServiceData();
         		};
         		$scope.pageBackwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage > 1) {
         				$scope.pagingOptionsForService.currentPage -= 1;
         			}
         			$scope.getServiceData();
         		};
         		$scope.cantPageForwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage != "" && pagingOptions.currentPage < pagingOptions.maxPages) {
         				return false;
         			} else {
         				return true;
         			}
         		};
         		$scope.pageForwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage < pagingOptions.maxPages) {
         				$scope.pagingOptionsForService.currentPage = parseInt(pagingOptions.currentPage) + 1;
         			}
         			$scope.getServiceData();
         		};
         		$scope.pageToLastForService = function(pagingOptions) {
         			$scope.pagingOptionsForService.currentPage = pagingOptions.maxPages;
         			$scope.getServiceData();
         		};
         		$scope.forServiceRest=function(){
         			$scope.queryOptionsForService.name=null;
         	        $scope.pagingOptionsForService.currentPage = 1;
         			$scope.getServiceData();
         		}
         		$scope.forServiceSearch=function(){
         			$scope.getServiceData();
         		}
         		//分页监视器
         		$scope.$watch('pagingOptionsForService', function (newVal, oldVal) {
             		if (newVal.currentPage !== oldVal.currentPage) {
             			if ($scope.pagingOptionsForService.currentPage != '') {
             				if (!isNaN($scope.pagingOptionsForService.currentPage)) {// 为数字
             					if ($scope.pagingOptionsForService.currentPage > $scope.pagingOptionsForService.maxPages) {
             						$scope.pagingOptionsForService.currentPage = $scope.pagingOptionsForService.maxPages
             					}
             					$scope.getServiceData();
             				} else {// 不为数字
             					$scope.pagingOptionsForService.currentPage = oldVal.currentPage;
             				}
             			}
             		}
             		  if (newVal.pageSize !==oldVal.pageSize) {
                          $scope.pagingOptionsForService.currentPage = 1;
                          $scope.getServiceData();
                      }
             	}, true);
         		  $scope.selectAllForService = function(){
         			  $scope.checkAll2=!$scope.checkAll2;
         			  if($scope.checkAll2==false||$scope.checkAll2=="false"){
         				 $scope.checkAll2=true;
         			  }
         			  else {
         				 $scope.checkAll2=false;
         			  }
         	            angular.forEach($scope.serviceData, function(item) {
         	            	if(item.isDefault == false||item.isDefault == "false"){
         	            		 item.checked = $scope.checkAll2;
             	        	}
         	               
         	            });
         	        };
         	        // select a row
         	        $scope.selectRowForService = function(item) {
         	        if(item.isDefault == true||item.isDefault == "true"){
         	        		return ;
         	        	}
         	        	item.checked=!item.checked;
         	        	$scope.ForService= item;
         	        }
         		//服务加载数据
         		$scope.getServiceData=function(){
         			//通过合集的cp_id   去加载当前合集可以关联的服务
         			 if($("#radio-ava1").prop("checked")===true){
         				 if($scope.editType==1){
         					 //列表切换，页数重置
         			        $scope.pagingOptionsForService.currentPage = 1;
         				 }
              			 $scope.editType = 0;
              		 }else if($("#radio-unava1").prop("checked")===true){
              			 if($scope.editType==0){
         					 //列表切换，页数重置
              		        $scope.pagingOptionsForService.currentPage = 1;
         				 }
              			 $scope.editType = 1;
              		 }
     				//未关联
     				$http.get(sharedProperties.getServerUrl()+"/rest/v1/service/getservicesbyobject"
     						+"?contentid="+$scope.selectItem.contentId+"&cpid="+$scope.selectItem.cpId+"&mediatype="+$scope.selectModel+"&name="+$scope.queryOptionsForService.name
     						+"&associatedtype="+$scope.editType
     						+"&first="+($scope.pagingOptionsForService.currentPage-1)*$scope.pagingOptionsForService.pageSize+"&max="+$scope.pagingOptionsForService.pageSize)
     	            .success(function(data,status){
     	            	$scope.checkAll2=false;
     	                $scope.serviceData = data.list;
     	                $scope.pagingOptionsForService.total = data.total;
     	        		$scope.pagingOptionsForService.maxPages = Math.ceil($scope.pagingOptionsForService.total/$scope.pagingOptionsForService.pageSize);
     	        		
     	            }).error(function() {
     	            	
     	            });
             	};
         		//合集与服务的关联及解除关联
             	$scope.association=function(){
             		var serviceDataIds = []
             		var contentIds=[]
             		  angular.forEach($scope.serviceData, function(data, arrayIndex){
                          	if(data.checked==true){
                          		serviceDataIds.push(data.id);
                          	}
                           });
             		   contentIds.push($scope.selectItem.contentId);
             		   var data = {"contentIds":contentIds,"ids":serviceDataIds,"associatedType":$scope.editType+1,"mediaType": $scope.selectModel};
         	             $http.post(sharedProperties.getServerUrl()+'/rest/v1/service/mapping',data)
         	             .success(function() {
         	         		$scope.getServiceData();
         	             });
             	}
             	
             	$scope.selectedForService=function(){
             		var isSelected = false;
             		
             			 angular.forEach($scope.serviceData, function(item) {
                  			  if(true == item.checked){
                  				  isSelected = true;
                  			  }
                  		 });
                  	    return !isSelected;
             		}
             	
         	    //合集关联服务结束
            }
        }
    }]).directive('selectCategorynew',['$http','sharedProperties','$q','$rootScope','commonMethod', function($http,sharedProperties,$q,$rootScope,commonMethod) {
        return {
            restrict: 'E',
            scope: false,
            templateUrl: 'common/selectCategory/selectCategoryNew.html',
            link: function($scope, element, attrs) {
        	   

                $("#categorydialog").hide();
                $("#test").hide();
              	 //选择栏目模块开始
              	 $scope.loadingCategoryData=function(item,type){
              		 //初始关闭栏目选择框
                 	  $("#test").hide();
                      $("#categorydialog").hide();
                      
                 	 //加载栏目数据
                     var mediaType = [];
                     if (type != null) {
                    	 if(type==2){
                    		  mediaType = [0, 8];
                    	 }
                    	 else if(type==3){
                    		 mediaType = [5, 8];
                    	 }
                    	 else if(type==9){
                    		 mediaType = [9, 8];
                    	 }
                     } else {
                         mediaType = [8];
                     }
                     $http.get(sharedProperties.getServerUrl() + '/rest/v1/category/treebymodel?models='+mediaType+"&cpid="+item.cpId).
                     success(function (results, status, headers, config) {
              			    // 初始化分类
              		        $scope.catelist = results.vo;
                            $scope.categoriesRaw=results.vo;
                             
                     		 if(type==2){
                       			$http.get(sharedProperties.getServerUrl()+'/rest/v1/vod/series/detail/'+item.id).success(function(data){
                                    	console.log(data);
                                        $scope.datac = data.vo;
                                        $scope.cateStrArray=$scope.datac.categoryContentIds;  
                                        console.info($scope.cateStrArray);
                                        $scope.reateCategoryTree();
                                    	$("#categorydialog").show();
                                    	$("#test").show();
                                    	$("#catescroll").scrollTop(0);
                                    
                                     tree.openAllItems($scope.parentCategoryFlag.parentContentId);
                                	 });
                       		 }
                       		 else if(type==3){
                       			 $http.get(sharedProperties.getServerUrl()+'/rest/v1/tvod/channel/detail?contentid='+item.contentId).success(function(data){
                       	         	console.log(data);
                       	             $scope.datac = data.vo;
                       	             $scope.cateStrArray=$scope.datac.categoryContentIds;  
                       	             console.info($scope.cateStrArray);
                       	             $scope.reateCategoryTree();
                       	         	$("#categorydialog").show();
                       	         	$("#test").show();
                       	         	$("#catescroll").scrollTop(0);
                                   tree.openAllItems($scope.parentCategoryFlag.parentContentId);
                       			 });
                       		 } else if(type==9){
                                  $http.get(sharedProperties.getServerUrl()+'/rest/v1/base/cast/detail?id='+item.id).success(function(data){
                                      console.log(data);
                                      $scope.datac = data.vo;
                                      $scope.cateStrArray=$scope.datac.categoryContentIds;
                                      console.info($scope.cateStrArray);
                                      $scope.reateCategoryTree();
                                      $("#categorydialog").show();
                                      $("#test").show();
                                      $("#catescroll").scrollTop(0);
                                      tree.openAllItems($scope.parentCategoryFlag.parentContentId);
                                  });
                              }
                              
              			});
              	 };
              	 //展开栏目树
              	 $scope.openCategoryTree=function(item,type){
              		//初始化数据
                   	 $scope.loadingCategoryData(item,type);
                   	 
              		 $scope.selectCategoryModel=type;
     
                   	//加载当前直播,查询当前已经关联的栏目,因为要过滤，先调整下看看
                  
                   };
                   $scope.closeCategoryDlg=function(){
                	   $scope.formattedCategory=[]
                	 var cateCodeStr=tree.getAllCheckedBranches();
                	 if(cateCodeStr != ""){
                		 $scope.cateStrArray=cateCodeStr.split(',');
                		 for(var i = 0; i < $scope.cateStrArray.length; i++) {
                			 if(!tree.hasChildren($scope.cateStrArray[i])){
                				 $scope.formattedCategory.push($scope.cateStrArray[i]);
                			 }
                		 }
                	 }
                	 $scope.datat = {"mediaType":$scope.selectCategoryModel,"categoryContentIds":$scope.formattedCategory,"contentIds":[$scope.datac.contentId]};
                      $http.post(sharedProperties.getServerUrl()+"/rest/v1/category/mappingforobject",$scope.datat)
                      .success(function(data,status){
                         
                      }).error(function(data,status){
                    	  
                      });
                  
                         $("#categorydialog").hide();
                         $("#test").hide();
                   };
                   $scope.cancelCategoryDlg=function(){
                   	 $("#categorydialog").hide();
                   	 $("#test").hide();
                   };
                   var tree=null;
              	 $scope.reateCategoryTree=function(){
                    $scope.parentCategoryFlag=null;
                  	$scope.categories=[];
              			angular.forEach($scope.catelist,function(l,i){
                              var cateMeta= new Array();
                              cateMeta.push(l.contentId);
                              cateMeta.push(l.parentContentId);
                              cateMeta.push(l.name);
                              $scope.categories.push(cateMeta);
              			});
              		// 设置根栏目	
              			if($scope.catelist!=null&&$scope.catelist.length>0){
    						for(var i=0;i<$scope.catelist.length;i++){
    							if($scope.catelist[i].spcategory==true||$scope.catelist[i].spcategory=="true"){
    								$scope.parentCategoryFlag=$scope.catelist[i];
    							}
    						}
    					}
                      $("#categoryTree").text("");
                      tree= new dhtmlXTreeObject("categoryTree", "100%", "100%",$scope.parentCategoryFlag.parentContentId);
                      tree.setSkin('dhx_skyblue');
                      tree.setImagePath("plugin/ace/js/dhtmlx/imgs/csh_dhx_skyblue/");
                      tree.enableCheckBoxes(1);
                      tree.enableThreeStateCheckboxes(true);
                      tree.loadJSArray($scope.categories);
                      tree.setOnCheckHandler(function(id,state){
                    	  var modelflag="";
                    	  if($scope.selectCategoryModel=="2"||$scope.selectCategoryModel==2){
                    		  modelflag=0;
                    	  }
                    	  else if($scope.selectCategoryModel=="3"||$scope.selectCategoryModel==3){
                    		  modelflag=5;
                    	  }
                    	  if(state==1||state=="1"){
                    		  for(var i = 0; i < $scope.categoriesRaw.length; i++) {
                        		  if(id==$scope.categoriesRaw[i].id){
                        			  if($scope.categoriesRaw[i].model!=8&&$scope.categoriesRaw[i].model!=modelflag){
                        				  tree.setCheck($scope.categoriesRaw[i].contentId,false);
                            			  $scope.messageCategoryType = $rootScope.l10n.messageCategoryType;
                          	        	  commonMethod.tipDialog($scope.messageCategoryType);
                            			  return ; 
                        			  }
                        		  }
                               };
                    	  }
                    	
                      });

                      if ($scope.cateStrArray != null) {
                          for(var i = 0; i < $scope.cateStrArray.length; i++) {
                    		  tree.setCheck( $scope.cateStrArray[i],true);
                          };
                      }
              	 }
              	 //选择栏目模块结束
              	 
              	$scope.get_unix_time_m1=function(dateStr)
              	{
              		if (dateStr == null) {
              			return null;
              		} else if (!isNaN(dateStr)) {
              			var newDate = new Date();
              			newDate.setTime(dateStr);
              			return newDate.toISOString();
              		}
              	    var newstr = dateStr.replace(/-/g,'/'); 
              	    var date =  new Date(newstr); 
              	    var time_str = date.getTime().toString();
              	    return time_str;
              	};
            }
        }
    }]).directive('selectServiceforaddpage',['$http','sharedProperties', function($http,sharedProperties) {
        return {
            restrict: 'E',
            scope: false,
            templateUrl: 'commonHTML/selectService/forSelectServiceAddPage.html',
            link: function($scope, element, attrs) {
        	    //合集关联服务开始
        	    
         	    $scope.checkAll3=false;
         	    $scope.openServiceDomalForAddPage=function(type){
         	    	 $('#modalForServiceforpage').modal('toggle');	
         	    	 $scope.getService();
         	    if($scope.id!=0){
                	 $scope.selectModel=type;
                	 $scope.getServiceData();
         	    	 }
         	    };
         	    $scope.closeServiceDomalForAddPage=function(){
         	    	 $('#modalForServiceforpage').modal('toggle');
         	    	 if($scope.id!=0){
         	    		$scope.toSeePage();
         	    	 }
         	    };
         	    //加载的服务内容进行分页操作js
         	    //分页参数控制器
         	    //查询条件控制器
         		$scope.queryOptionsForServiceForAddPage = {//查询字段
         				name: null,
         		};
         	    $scope.selectAllForServiceForAddPage = function(){
         			  if($scope.checkAll3==false||$scope.checkAll3=="false"){
         				 $scope.checkAll3=true;
         			  }
         			  else {
         				 $scope.checkAll3=false;
         			  }
         	           angular.forEach($scope.serviceDataforpage, function(item) {
         	        	   if(item.isDefault==false||item.isDefault=='false'){
         	        		  item.checked = $scope.checkAll3;
         	        	   }
         	              
         	            });
         	        };
         	        // select a row
         	        $scope.selectModelServices=[];
         		//服务加载数据
         	       $scope.getService=function(){
         	          if($scope.cp_id==null||$scope.cp_id==undefined||$scope.cp_id=="undefined"||$scope.cp_id==""){
         	          		return ;
         	          	}
         	    
         	  		$http.get(sharedProperties.getServerUrl()+"/rest/service/getservice/"+$scope.cp_id+"?name="+$scope.queryOptionsForServiceForAddPage.name)
         	  	            .success(function(data,status){
         	  	         	$scope.serviceDataforpage =  data.services;
         	  	         	if($scope.id!=0){
         	  	         	  if($scope.data!=null&&($scope.data.services_id!=null||$scope.data.services_id.length>0)){
              	  	             $scope.data.servicesvalue ="";
              	  	             $scope.serviceInputShow1=false;
              	  	             console.info($scope.data.services_id);
              	  	            	   angular.forEach($scope.data.services_id,function(g){
              	  	           			angular.forEach($scope.serviceDataforpage,function(item){
              	  	           				if (item.id == g) {
              	  	           					item.checked = true;
              	  	           				    $scope.selectModelServices.push(item.id);
              	  	           			     if(!$scope.contains(item.name)){
          	  	           				        $scope.formattedService.push({"text":item.name});
          	  	           				    }
      	  	           				      $scope.serviceInputShow1=true;
              	  	           			 $scope.data.servicesvalue += item.name + " ";
              	  	           				}
              	  	           			});
              	  	           		});
              	  	            	$scope.serviceInputShow=$scope.serviceInputShow1;
              	  	             $scope.serviceInput();
              	  	            }
         	  	         	}
         	  	         	else{
         	  	         	 if($scope.services_id!=null||$scope.services_id.length>0){
              	  	             $scope.serviceInputShow1=false;
              	  	            	   angular.forEach($scope.services_id,function(g){
              	  	           			angular.forEach($scope.serviceDataforpage,function(item){
              	  	           				if (item.id == g) {
              	  	           					item.checked = true;
              	  	           				    $scope.selectModelServices.push(item.id);
              	  	           			 if(!$scope.contains(item.name)){
          	  	           				   $scope.formattedService.push({"text":item.name});
          	  	           				    }
      	  	           				   
              	  	           				    $scope.serviceInputShow1=true;
              	  	           				}
              	  	           			});
              	  	           		});
              	  	            	$scope.serviceInputShow=$scope.serviceInputShow1;
              	  	             $scope.serviceInput();
              	  	            }
         	  	         	}
         	  	            }).error(function() {
         	  	         });
         	          }
         	       //處理被選中的服務
         	       $scope.sureService=function(){
         	    	   //先质空 
         	    	  $scope.selectModelServices=[];
         	    	 $scope.formattedService=[];
         	    	  angular.forEach($scope.serviceDataforpage,function(item){
 	           				if (item.checked == true) {
 	           				   $scope.selectModelServices.push(item.id);
 	           				    if(!$scope.contains(item.name)){
	  	           				   $scope.formattedService.push({"text":item.name});
	  	           				  }	           				   
 	           				}
 	           			});
         	    	 if($scope.selectModelServices.length>=1){
                     	$scope.serviceInputShow=true;
                     }else{
                     	$scope.serviceInputShow=false;
                     }
         	    	$scope.services_id=$scope.selectModelServices;   
         	    	 $scope.closeServiceDomalForAddPage();
         	    	 $scope.serviceInput();
         	       }
         	      $scope.forServiceSearchForAddPage=function(){
         	    	 $scope.getService();
         	      }
         	      $scope.forServiceRestForAddPage=function(){
         	    	 $scope.queryOptionsForServiceForAddPage.name=null;
         	    	 $scope.getService();
         	      }
         	     $scope.selectRowForServiceForAddPage = function(item) {
          	        if(item.isDefault == true||item.isDefault == "true"){
          	        		return ;
          	        	}
          	        	item.checked=!item.checked;
          	        	$scope.ForService= item;
          	        }
         	      
         	     $scope.serviceInput=function(){
                 	$http.get(sharedProperties.getAppName()+'/index.html').success(function(data){
                 		$("#servicevaluess div div .tag-list").children("li").children("a").remove();
                 		$("#servicevaluess div div input").remove();
                 	});
                 }
         	    $scope.serviceInput();
         	    
         	    
         	    //修改时进入的方法
               //合集关联服务开始
        	    
         	    $scope.checkAll2=false;
         	
         	    //加载的服务内容进行分页操作js
         	    //分页参数控制器
         	    $scope.pagingOptionsForService = {//分页字段
         				pageSizes: [5,10, 20, 30],
         				pageSize: 5,
         			    currentPage: 1,
         			    total: 0,//存放list总记录数
         			    maxPages: 0,//最大页数
         		};
         	    //查询条件控制器
         		$scope.queryOptionsForService = {//查询字段
         				name: null,
         		};
         		$scope.selectForServiceselectForService = function(item) {
         			 
         				$scope.selectForServiceData= item;
         			};
         	    // --分页函数 start--
         		$scope.cantPageBackwardForService = function(pagingOptions,flag) {// 判断是否可以向前一页或者跳转到第一页
         			if (pagingOptions.currentPage == "" || pagingOptions.currentPage <= 1) {
         				return true;
         			} else {
         				return false;
         			}
         		};
         		$scope.pageToFirstForService = function(pagingOptions) {
         			$scope.pagingOptionsForService.currentPage = 1;
         			$scope.getServiceData();
         		};
         		$scope.pageBackwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage > 1) {
         				$scope.pagingOptionsForService.currentPage -= 1;
         			}
         			$scope.getServiceData();
         		};
         		$scope.cantPageForwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage != "" && pagingOptions.currentPage < pagingOptions.maxPages) {
         				return false;
         			} else {
         				return true;
         			}
         		};
         		$scope.pageForwardForService = function(pagingOptions) {
         			if (pagingOptions.currentPage < pagingOptions.maxPages) {
         				$scope.pagingOptionsForService.currentPage = parseInt(pagingOptions.currentPage) + 1;
         			}
         			$scope.getServiceData();
         		};
         		$scope.pageToLastForService = function(pagingOptions) {
         			$scope.pagingOptionsForService.currentPage = pagingOptions.maxPages;
         			$scope.getServiceData();
         		};
         		$scope.forServiceRest=function(){
         			$scope.queryOptionsForService.name=null;
         			$scope.getServiceData();
         		}
         		$scope.forServiceSearch=function(){
         			$scope.getServiceData();
         		}
         		//分页监视器
         		$scope.$watch('pagingOptionsForService', function (newVal, oldVal) {
             		if (newVal.currentPage !== oldVal.currentPage) {
             			if ($scope.pagingOptionsForService.currentPage != '') {
             				if (!isNaN($scope.pagingOptionsForService.currentPage)) {// 为数字
             					if ($scope.pagingOptionsForService.currentPage > $scope.pagingOptionsForService.maxPages) {
             						$scope.pagingOptionsForService.currentPage = $scope.pagingOptionsForService.maxPages
             					}
             					$scope.getServiceData();
             				} else {// 不为数字
             					$scope.pagingOptionsForService.currentPage = oldVal.currentPage;
             				}
             			}
             		}
             		  if (newVal.pageSize !==oldVal.pageSize) {
                          $scope.pagingOptionsForService.currentPage = 1;
                          $scope.getServiceData();
                      }
             	}, true);
         		$scope.selectAllForService = function(){

         			if($scope.checkAll2==false||$scope.checkAll2=="false"){
         				$scope.checkAll2=true;
         			}
         			else {
         				$scope.checkAll2=false;
         			}
         			angular.forEach($scope.serviceData, function(item) {
         				if(item.isDefault == false||item.isDefault == "false"){
         					item.checked = $scope.checkAll2;
         				}

         			});
         		};
         	        // select a row
         	        $scope.selectRowForService = function(item) {
         	        if(item.isDefault == true||item.isDefault == "true"){
         	        		return ;
         	        	}
         	        	item.checked=!item.checked;
         	        	$scope.ForService= item;
         	        }
         		//服务加载数据
         		$scope.getServiceData=function(){
         			  $scope.checkAll2=false;
         			  $("#two").prop("checked",false);
         			//通过合集的cp_id   去加载当前合集可以关联的服务
         			 if($("#radio-ava1").prop("checked")===true){
              			 $scope.editType = 2;
              		 }else if($("#radio-unava1").prop("checked")===true){
              			 $scope.editType = 1;
              		 }
         			if($scope.editType==null||$scope.editType==1||$scope.editType=="1"){
         				//未关联
         				$scope.checkAll2=false;
         				$http.get(sharedProperties.getServerUrl()+"/rest/service/listbycporsp/"+$scope.cp_id+"/"+$scope.contentId+"/"+ $scope.selectModel+"?first="+($scope.pagingOptionsForService.currentPage-1)*$scope.pagingOptionsForService.pageSize+"&max="+$scope.pagingOptionsForService.pageSize+"&name="+$scope.queryOptionsForService.name)
         	            .success(function(data,status){
         	                $scope.serviceData = data.services;
         	                $scope.pagingOptionsForService.total = data.total;
         	        		$scope.pagingOptionsForService.maxPages = Math.ceil($scope.pagingOptionsForService.total/$scope.pagingOptionsForService.pageSize);
         	        		
         	            }).error(function() {
         	            	
         	            });
         			}
         			else{
         				//已关联
         				$scope.checkAll2=false;
         				$http.get(sharedProperties.getServerUrl()+"/rest/service/getlistByObjectContentId/"+$scope.contentId+"/"+ $scope.selectModel+"?first="+($scope.pagingOptionsForService.currentPage-1)*$scope.pagingOptionsForService.pageSize+"&max="+$scope.pagingOptionsForService.pageSize+"&name="+$scope.queryOptionsForService.name)
         	            .success(function(data,status){
         	                $scope.serviceData = data.services;
         	                $scope.pagingOptionsForService.total = data.total;
         	        		$scope.pagingOptionsForService.maxPages = Math.ceil($scope.pagingOptionsForService.total/$scope.pagingOptionsForService.pageSize);
         	        	
         	            }).error(function() {
         	            	
         	            });
         			}
             	};
         		//合集与服务的关联及解除关联
             	$scope.association=function(){
             		var serviceDataIds = []
             		var contentIds=[]
             		  angular.forEach($scope.serviceData, function(data, arrayIndex){
                          	if(data.checked==true){
                          		serviceDataIds.push(data.id);
                          	}
                           });
             		   contentIds.push($scope.contentId);
             		   var data = {"contentIds":contentIds,"ids":serviceDataIds,"deleteFlag":$scope.editType-1,"status": $scope.selectModel};
         	             $http.put(sharedProperties.getServerUrl()+'/rest/service/association',data)
         	             .success(function() {
         	         		$scope.getServiceData();
         	             });
             	}
             	
             	$scope.selectedForService=function(){
             		var isSelected = false;
             		
             			 angular.forEach($scope.serviceData, function(item) {
                  			  if(true == item.checked){
                  				  isSelected = true;
                  			  }
                  		 });
                  	    return !isSelected;
             		}
             	
         	    //合集关联服务结束
         	    //修改界面
             	$scope.toSeePage=function(){
             		//已关联
     				$http.get(sharedProperties.getServerUrl()+"/rest/service/getlistByObjectContentId/"+$scope.contentId+"/"+ $scope.selectModel+"?first=0&max=9999&name=")
     	            .success(function(data,status){
     	                $scope.serviceData1 = data.services;
     	                $scope.serviceInputShow1=false;
     	                $scope.selectModelServices=[];
     	                $scope.formattedService=[];
	  	            	   angular.forEach( $scope.serviceData1,function(g){
	  	           			angular.forEach($scope.serviceDataforpage,function(item){
	  	           				if (item.id == g.id) {
	  	           				    $scope.selectModelServices.push(item.id);
	  	           				     if(!$scope.contains(item.name)){
    	  	           				   $scope.formattedService.push({"text":item.name});
    	  	           				   }
	  	           				    $scope.serviceInputShow1=true;
	  	           				    $scope.data.servicesvalue += item.name + " ";
	  	           				}
	  	           			});
	  	           		});
	  	            	$scope.serviceInputShow=$scope.serviceInputShow1;
	  	             $scope.serviceInput();
     	            }).error(function() {
     	            	
     	            });
             	}
             	$scope.contains=function(obj) { 
             		
             		var flagg=false;
             	    for(var i=0;i<$scope.formattedService.length;i++){
             	    	if($scope.formattedService[i].text==obj){
             	    		    flagg=true;
             	    		    return flagg;
             	    		}
             	    	}
             	    
             	    return flagg;  
             	}  
            }
        }
    }])
    //服务bms色块过滤,未回收
    .directive('atSelectservicebms',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            template:
            '<div class="col-xs-8" style="z-index: 9999; float: right;">' +
            '<div class="fig-tab">' +
            '<input type="checkbox" class="radio-ava" name="radio-fig" id="radio-ava" data-type="2">' +
            '<label for="radio-ava" style="color: #82af6f">{{"_publishsuccess" | translate}}</label>' +
            '<input type="checkbox" class="radio-part" id="radio-part" name="radio-fig" data-type="1">' +
            '<label for="radio-part" style="color: #d68748">{{"_publishing" | translate}}</label>' +
            '<input type="checkbox" class="radio-pro" name="radio-fig" data-type="0" id="radio-pro">' +
            '<label for="radio-pro" style="color: #a0a0a0">{{"initial_v" | translate}}</label>' +
            '<input type="checkbox" name="radio-fig" class="radio-unava" data-type="3" id="radio-unava">' +
            '<label for="radio-unava" style="color: #d20000">{{"_publishfailed" | translate}}</label>' +
            '</div>' +
            '</div>',
            link: function($scope, element, attrs) {
                $(".fig-tab").find("input").click(function() {
                    var $flag = $(this).is(":checked");
                    $scope.checkAll = false;
                    if($flag){
                        $(this).siblings("input").attr("checked",false);
                        $flag = false;
                        var $status = $(this).attr("data-type");
                        $scope.queryOptions.distributestatus=$status;
                        $scope.getPageData();
                    }else {
                       // $(".fig-tab").find('input').prop('checked',true);
                        $(this).prop('checked', false);
                        $scope.queryOptions.distributestatus='null';
                        $scope.getPageData();
                    }
                });
            }
        }
    })
    .directive('onFinishRender',['$timeout', '$parse', function ($timeout, $parse) {  
    return {  
        restrict: 'A',  
        link: function (scope, element, attr) {  
            if (scope.$last === true) {  
                $timeout(function () {  
                    scope.$emit('ngRepeatFinished'); //事件通知  
                        var fun = scope.$eval(attr.onFinishRender);  
                        if(fun && typeof(fun)=='function'){    
                            fun();  //回调函数  
                        }    
                });  
            }  
        }  
    }  
  }]);
