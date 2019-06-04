var app = angular.module('ngApp', []);

app.controller('adCategoryCtrl', adCategoryCtrl);

adCategoryCtrl.$inject = ['$scope', 'WebService'];

function adCategoryCtrl($scope, WebService) {

    $scope.listCategory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.search = '';
    $scope.categoryDetail = [];
    $scope.init = function () {
        $scope.getAdListChapter(1);
    };

    $scope.getAdListChapter = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/category/list';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append('content', $scope.search.trim());
        WebService.getData(url, data).then(function (response) {
            $scope.listCategory = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalPages);
            var pages = [];
            for (let i = startPage; i <= endPage; i++) {
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

    $scope.deleteCategory = function (id) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn hủy xóa thể loại",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/category/delete/' + id;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: 'Xóa Thể Loại Thành Công',
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        $scope.getAdListChapter(1);
                    })
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
                            var url = window.location.origin + '/logout';
                            window.location.href = url;
                        }
                    })
                })
            }
        })
    };
}