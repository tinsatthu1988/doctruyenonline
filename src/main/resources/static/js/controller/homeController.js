var app = angular.module('ngApp', []);

app.controller('homeCtrl', homeCtrl);

homeCtrl.$inject = ['HomeService', '$scope'];

function homeCtrl(HomeService, $scope) {

    $scope.topViewMonth = [];
    $scope.storyNewUpdate = [];
    $scope.topConveter = [];
    $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';

    $scope.init = function () {
        $scope.getTopViewMonth();
        $scope.getStoryNewUpdate();
        $scope.getTopConveter();
    };

    $scope.getTopViewMonth = function () {
        var data = new FormData();
        //Lấy Top View 10 Truyện Trong Tháng
        var url = window.location.origin + '/api/home/topAppoidMonth';
        HomeService.getData(url, data).then(function (response) {
            $scope.topViewMonth = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };

    $scope.getStoryNewUpdate = function () {
        var data = new FormData();
        //Lấy Top View 10 Truyện Trong Tuần
        var url = window.location.origin + '/api/home/storyNewUpdate';
        HomeService.getData(url, data).then(function (response) {
            $scope.storyNewUpdate = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };

    $scope.getTopConveter = function () {
        var data = new FormData();
        //Lấy Top View 10 Truyện Trong Tuần
        var url = window.location.origin + '/api/home/topConveter';
        HomeService.getData(url, data).then(function (response) {
            $scope.topConveter = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };
}