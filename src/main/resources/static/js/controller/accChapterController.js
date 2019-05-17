var app = angular.module('ngApp', []);

app.controller('accChapterCtrl', accChapterCtrl);

accChapterCtrl.$inject = ['$scope', 'WebService'];

function accChapterCtrl($scope, WebService) {

    $scope.listChapter = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.storyId = 0;
    $scope.typeList = [
        {'id': 0, 'label': 'Thứ Tự Cao xuống Thấp'},
        {'id': 1, 'label': 'Thứ Tự Thấp đến Cao '},
        {'id': 2, 'label': 'Ngày Đăng Từ Mới đến Cũ'},
        {'id': 3, 'label': 'Ngày Đăng Từ Cũ đến Mới'}
    ];
    $scope.type = $scope.typeList[0];

    $scope.init = function (id) {
        $scope.storyId = id;
        $scope.getListChapter(1);
    };

    $scope.getListChapter = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/chapter/chapterOfUser';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("type", $scope.type.id);
        data.append("storyId", $scope.storyId);
        WebService.getData(url, data).then(function (response) {
            $scope.listChapter = response.data.content;
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
                window.location.href = window.location.origin + '/tai-khoan/list_chuong/' + $scope.id;
            })
        })
    };

    $scope.deleteChapter = function (id) {

    };

}