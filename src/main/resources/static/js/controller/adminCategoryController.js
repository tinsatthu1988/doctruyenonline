var app = angular.module('ngApp', []);

app.controller('adminCategoryController', adminChapterCtrl);

adminChapterCtrl.$inject = ['$scope', 'HomeService'];

function adminChapterCtrl($scope, HomeService) {

    $scope.listChapter = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.search = '';
    $scope.storyId = 0;

    $scope.init = function () {
        $scope.getAdListChapter(1);
    };

    $scope.getAdListChapter = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }

        var url = window.location.origin + '/api/theloaiList';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        HomeService.getData(url, data).then(function (response) {
            $scope.listChapter = response.data.content;
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

    $scope.deleteAdChapter = function (id) {
        var url = window.location.origin + '/api/deleteAdCategory/' + id;
        HomeService.deleteData(url).then(function (response) {
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
                $scope.getAdListChapter(1);
            })
        })
    };
}