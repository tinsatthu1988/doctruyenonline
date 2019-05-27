var app = angular.module('ngApp', ['ngSanitize']);

app.controller('chapterCtrl', chapterCtrl);

chapterCtrl.$inject = ['$http', '$scope', 'WebService'];

function chapterCtrl($http, $scope, WebService) {

    $scope.sid = 0;
    $scope.reportMss = '';
    $scope.chapterId = 0;
    $scope.commentText = '';
    $scope.listComment = [];
    $scope.totalCommentPages = 0;
    $scope.currentCommentPage = 1;
    $scope.pageComment = [];
    $scope.totalComment = 0;
    $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';
    $scope.commentType = 1;
    $scope.init = function (sID, chapterId) {
        $scope.sid = sID;
        $scope.chapterId = chapterId;
        $scope.getListComment(1, 1);
    };

    $scope.chapterVip = function (chID) {
        var data = new FormData();
        data.append('chapterId', chID);
        var url = window.location.origin + '/api/pay/readingVip';
        WebService.submitForm(url, data).then(function successCallback(response) {
            window.location.reload();
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
            for (var i = startPage; i <= endPage; i++) {
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

    $scope.submitReport = function () {
        if ($scope.reportMss.trim().length === 0) {
            swal({
                text: 'Nội dung lỗi không được để trống',
                type: 'error',
                confirmButtonText: 'Ok'
            })
        } else {
            var data = new FormData();
            data.append('chapterId', $scope.chapterId);
            data.append('content', $scope.reportMss);
            var url = window.location.origin + '/api/report/add';
            WebService.submitForm(url, data).then(function successCallback(response) {
                swal({
                    text: 'Báo Lỗi thành công',
                    type: 'success',
                    confirmButtonText: 'Ok'
                }).then(
                    $('#modal-report').modal('hide')
                );
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            });
        }
    }
}