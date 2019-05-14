var app = angular.module('ngApp', []);

app.controller('accountAdModController', accountAdModCtrl);

accountAdModCtrl.$inject = ['$scope', 'WebService'];

app.controller('payController', payCtrl);

payCtrl.$inject = ['$scope', 'WebService'];

function accountAdModCtrl($scope, WebService) {
    $scope.selectStatus = [];
    $scope.listStatus = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.search = '';

    $scope.init = function () {
        $scope.getList();
        $scope.getListChapter(1);
    };

    $scope.getSelectd = function () {
        $scope.getListChapter(1);
    };

    $scope.getList = function () {
        var data = new FormData();
        var url = window.location.origin + '/api/home/getStatusStoryList';
        WebService.getData(url, data).then(function (response) {
            $scope.listStatus = response.data;
            $scope.selectStatus = $scope.listStatus[0];
        });
    };
    $scope.getListChapter = function (pagenumber) {
        var url = window.location.origin + '/api/storyByAdmin';
        var data = new FormData();
        if ($scope.selectStatus.id === undefined) {
            data.append("statusId", -1);
        } else {
            data.append("statusId", $scope.selectStatus.id);
        }
        data.append('pagenumber', pagenumber);
        data.append("search", encryptText($scope.search));
        WebService.getData(url, data).then(function (response) {
            $scope.listStory = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalPages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.page = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload();
            })
        })
    };

    $scope.deleteStory= function (id) {
        var url = window.location.origin + '/api/deleteAdModStory/' + id;
        WebService.deleteData(url).then(function (response) {
            swal({
                text: 'Xóa Truyện Thành Công',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                $scope.getListChapter(1);
            })
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                $scope.getListChapter(1);
            })
        })
    };

}

function payCtrl($scope, WebService) {
    $scope.listPay = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];

    $scope.init = function () {
        $scope.getListPay(1);
    };

    $scope.getListPay = function (pagenumber) {
        var url = window.location.origin + '/api/payByUser';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        WebService.getData(url, data).then(function (response) {
            $scope.listPay = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalPages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.page = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload();
            })
        })
    };

}