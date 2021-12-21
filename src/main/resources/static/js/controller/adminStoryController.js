var app = angular.module('ngApp', []);

app.controller('adStoryCtrl', adStoryCtrl);

adStoryCtrl.$inject = ['$scope', 'WebService'];

function adStoryCtrl($scope, WebService) {

    $scope.listCategory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.search = '';
    $scope.typeList = [
        {'id': -1, 'label': 'Tất Cả'},
        {'id': 0, 'label': 'Khóa'},
        {'id': 1, 'label': 'Đang Ra'},
        {'id': 2, 'label': 'Hoàn Thành'},
        {'id': 3, 'label': 'Vip'},

    ];
    $scope.type = $scope.typeList[0];

    $scope.init = function () {
        $scope.getAdListStory(1);
    };

    $scope.getAdListStory = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/story/admin/listStory';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append('search', $scope.search.trim());
        data.append('type', $scope.type.id);
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
            console.log($scope.listCategory);
        }, function errorCallback(errResponse) {

        })
    };

    $scope.deleteStory = function (id) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn xóa truyện này!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/story/admin/delete/' + id;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: 'Xóa Thành Công',
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        $scope.getAdListUser(1);
                    })
                }, function errorCallback(errResponse) {
                    swal({
                        text: errResponse.data.messageError,
                        type: 'warning',
                        confirmButtonText: 'Ok'
                    })
                })
            }
        })
    }
}