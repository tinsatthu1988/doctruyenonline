var app = angular.module('ngApp', []);

app.controller('accStoryCtrl', accStoryCtrl);

accStoryCtrl.$inject = ['$scope', 'WebService'];

function accStoryCtrl($scope, WebService) {

    $scope.listOnGoing = [];
    $scope.totalOnGoingPages = 0;
    $scope.currentOnGoingPage = 1;
    $scope.pageOnGoing = [];

    $scope.listComplte = [];
    $scope.totalCompltePages = 0;
    $scope.currentCompltePage = 1;
    $scope.pageComplte = [];

    $scope.listHidden = [];
    $scope.totalHiddenPages = 0;
    $scope.currentHiddenPage = 1;
    $scope.pageHidden = [];

    $scope.listStop = [];
    $scope.totalStopPages = 0;
    $scope.currentStopPage = 1;
    $scope.pageStop = [];

    $scope.init = function () {
        $scope.getListStoryOnGoing(1);
        $scope.getListStoryHidden(1);
        $scope.getListStoryComplte(1);
        $scope.getListStoryStop(1);
    };

    $scope.getListStoryOnGoing = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/story/list_story';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 1);
        WebService.getData(url, data).then(function (response) {
            $scope.listOnGoing = response.data.content;
            $scope.totalOnGoingPages = response.data.totalPages;
            $scope.currentOnGoingPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentOnGoingPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalOnGoingPages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.pageOnGoing = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.getListStoryComplte = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/story/list_story';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 3);
        WebService.getData(url, data).then(function (response) {
            $scope.listComplte = response.data.content;
            $scope.totalCompltePages = response.data.totalPages;
            $scope.currentCompltePage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentCompltePage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalCompltePages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.pageComplte = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.getListStoryHidden = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/story/list_story';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 0);
        WebService.getData(url, data).then(function (response) {
            $scope.listHidden = response.data.content;
            $scope.totalHiddenPages = response.data.totalPages;
            $scope.currentHiddenPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentHiddenPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalHiddenPages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.pageHidden = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.getListStoryStop = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/story/list_story';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 2);
        WebService.getData(url, data).then(function (response) {
            $scope.listStop = response.data.content;
            $scope.totalStopPages = response.data.totalPages;
            $scope.currentStopPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentStopPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalStopPages);
            var pages = [];
            for (var i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.pageStop = pages;
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.storyDelete = function (id, vnName) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn xóa truyện " + vnName,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/story/delete_story/' + id;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: 'Xóa Truyện " + name + " Thành Công',
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        $scope.getListStoryOnGoing(1);
                    })
                }, function errorCallback(errResponse) {
                    swal({
                        text: errResponse.data.messageError,
                        type: 'warning',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        if (errResponse.status === 400) {
                            $scope.getListStoryOnGoing(1);
                        }
                        if (errResponse.status === 403) {
                            window.location.reload(true);
                        }
                        if (errResponse.status === 500) {
                            window.location.href = window.location.origin + '/logout';
                        }
                    })
                })
            }
        })
    };
}