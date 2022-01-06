var app = angular.module('ngApp', ['ngSanitize']);

app.controller('memberCtrl', memberCtrl);

memberCtrl.$inject = ['WebService', '$scope'];

function memberCtrl(WebService, $scope) {

    $scope.listStory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.uID = 0;
    $scope.page = [];

    //Lấy danh sách Chapter Theo storyId, pagenumber, type
    $scope.getListStory = function (pagenumber, type) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        if (type === undefined) {
            type = 1;
        }
        var url = window.location.origin + '/api/story/storyOfMember';
        var data = new FormData();
        data.append('userId', $scope.uID);
        data.append('pagenumber', pagenumber);
        data.append('type', type);
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
        })
    };

    $scope.init = function (uID) {
        $scope.uID = uID;
    };

}

