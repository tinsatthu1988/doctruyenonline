var app = angular.module('ngApp', ['ngSanitize']);

app.controller('accFollowCtrl', accFollowCtrl);

accFollowCtrl.$inject = ['WebService', '$scope'];

function accFollowCtrl(WebService, $scope) {

    $scope.listStory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.id = 0;

    $scope.getFollowPage = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        var url = window.location.origin + '/api/follow/followOfUser';
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
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                if (errResponse.status === 403) {
                    window.location.reload(true);
                }
                if (errResponse.status === 500) {
                    window.location.href = window.location.origin + '/logout';
                }
            })
        });
    };

    $scope.deleteFollow = function (storyId, name) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn hủy theo dõi truyện " + name,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/follow/cancel_follow/' + storyId + '/' + $scope.id;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: "Hủy thành công theo dõi truyện " + name,
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(
                        $scope.getFollowPage(1)
                    );
                }, function errorCallback(errResponse) {
                    swal({
                        text: errResponse.data.messageError,
                        type: 'warning',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        if (errResponse.status === 400) {
                            $scope.getFollowPage(1);
                        }
                        if (errResponse.status === 403) {
                            window.location.reload(true);
                        }
                        if (errResponse.status === 500) {
                            window.location.href = window.location.origin + '/logout';
                        }
                    })
                });
            }
        });
    };

    $scope.init = function (id) {
        $scope.id = id;
        $scope.getFollowPage(1);
    }
}