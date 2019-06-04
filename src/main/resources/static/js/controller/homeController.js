var app = angular.module('ngApp', []);

app.controller('homeCtrl', homeCtrl);

homeCtrl.$inject = ['WebService', '$scope'];

function homeCtrl(WebService, $scope) {

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
        var url = '/api/story/topAppoidMonth';
        WebService.getData(url, data).then(function (response) {
            $scope.topViewMonth = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };

    $scope.getStoryNewUpdate = function () {
        var data = new FormData();
        //Lấy Top View 10 Truyện Trong Tuần
        var url = '/api/story/storyNewUpdate';
        WebService.getData(url, data).then(function (response) {
            $scope.storyNewUpdate = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };

    $scope.getTopConveter = function () {
        var data = new FormData();
        //Lấy Top Converter
        var url = '/api/user/topConveter';
        WebService.getData(url, data).then(function (response) {
            $scope.topConveter = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };


}