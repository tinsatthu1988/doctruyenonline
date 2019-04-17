var app = angular.module('ngApp', ['ngSanitize']);

app.controller('chapterCtrl', chapterCtrl);

chapterCtrl.$inject = ['$http', '$scope', 'WebService'];

function chapterCtrl($http, $scope, WebService) {

    $scope.sid = 0;
    $scope.commentText = '';
    $scope.listComment = [];
    $scope.totalCommentPages = 0;
    $scope.currentCommentPage = 1;
    $scope.pageComment = [];
    $scope.totalComment = 0;
    $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';
    $scope.commentType = 1;
    $scope.init = function (sID) {
        $scope.sid = sID;
        $scope.getListComment(1, 1);
    };

    $scope.chapterVip = function (chID) {
        var data = new FormData();
        data.append('chID', chID);
        var url = window.location.origin + '/api/chapterVip';
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messageError: data, status: status};
                return ret;
            }
        };
        $http.post(url, data, config).then(function successCallback(response) {
            swal({
                text: 'Thanh toán thành công',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(
                function () {
                    window.location.reload();
                }
            );
        }, function errorCallback(errResponse) {
            callWarningSweetalert(errResponse.data.messageError);
        });
    };

    $scope.getListComment = function (pagenumber, type) {
        WebService.loadComment($scope.sid, pagenumber, type).then(function (response) {
            $scope.totalComment = response.data.totalElements;
            $scope.listComment = response.data.content;
            $scope.totalCommentPages = response.data.totalPages;
            $scope.currentCommentPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentCommentPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalCommentPages);
            var pages = [];
            for (let i = startPage; i <= endPage; i++) {
                pages.push(i);
            }
            $scope.pageComment = pages;
            $scope.commentType = type;
        });
    };

    $scope.addComment = function () {
        if ($scope.commentText.trim().length !== 0) {
            WebService.addComment($scope.sid, $scope.commentText).then(function (response) {
                $scope.commentText = '';
                $scope.getListComment(1, $scope.commentType);
                callSuccessSweetalert('Bình luận thành công!');
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            })
        } else {
            callWarningSweetalert('Nội dung bình luận không được để trống!');
        }
    };
}