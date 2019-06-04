var app = angular.module('ngApp', ['ngSanitize']);

app.controller('storyCtrl', storyCtrl);

storyCtrl.$inject = ['WebService', '$scope'];

function storyCtrl(WebService, $scope) {

    $scope.listChapter = [];
    $scope.listStory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.user = {};
    $scope.sid = 0;
    $scope.uID = 0;
    $scope.page = [];
    $scope.commentText = '';
    $scope.listComment = [];
    $scope.commentType = 1;
    $scope.totalCommentPages = 0;
    $scope.currentCommentPage = 1;
    $scope.pageComment = [];
    $scope.totalComment = 0;
    $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';
    $scope.coupon = null;
    $scope.follow = false;

    //Lấy danh sách Chapter Theo storyId, pagenumber, type
    $scope.getListChapter = function (pagenumber, type) {
        var url = window.location.origin + '/api/chapter/chapterOfStory';
        WebService.getPage(url, $scope.sid, pagenumber, type).then(function (response) {
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
        })
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

    $scope.getConverter = function () {
        var data = new FormData();
        data.append('userId', $scope.uID);
        var url = window.location.origin + '/api/user/converterInfo';
        // Lấy Thông Tin Converter
        WebService.getData(url, data).then(function (response) {
            $scope.user = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });

    };
    $scope.getStoryOfConverter = function () {
        var data = new FormData();
        data.append('userId', $scope.uID);
        //Lấy Top 3 Truyện mới của converter
        var url = window.location.origin + '/api/story/storyOfConverter';
        WebService.getData(url, data).then(function (response) {
            $scope.listStory = response.data;
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
    };

    //Thực Hiện Đề Cử Khi Nhấn Submit
    $scope.appoint = function () {
        if ($scope.coupon == null || $scope.coupon === "") {
            callWarningSweetalert('Bạn Chưa nhập số phiếu đề cử');
        } else {
            var data = new FormData();
            data.append('storyId', $scope.sid);
            data.append('coupon', $scope.coupon);
            var url = window.location.origin + '/api/appoint/save';

            WebService.submitForm(url, data).then(function (response) {
                $scope.coupon = null;
                swal({
                    text: 'Đề Cử Thành Công!',
                    type: 'success',
                    confirmButtonText: 'Ok'
                }).then(function () {
                    templates.modal('hide');
                });
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            });
        }
    };

    //Thực Hiện Đăng Ký Theo dõi truyện
    $scope.registRecentReadingStory = function () {
        var url = window.location.origin + '/api/follow/add';
        var data = new FormData();
        data.append('storyId', $scope.sid);
        WebService.submitForm(url, data).then(function (response) {
            $scope.getFollowUser();
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            })
        });
    };

    //Thực Hiện Hủy Đăng Ký Theo dõi truyện
    $scope.unRecentReadingStory = function () {
        var url = window.location.origin + '/api/follow/delete';
        var data = new FormData();
        data.append('storyId', $scope.sid);
        WebService.submitForm(url, data).then(function (response) {
            $scope.getFollowUser();
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            })
        });
    };

    // Lấy Thông Tin Theo dõi Truyện của người dùng
    $scope.getFollowUser = function () {
        var url = window.location.origin + '/api/follow/checkFollow';
        var data = new FormData();
        data.append('storyId', $scope.sid);
        WebService.getData(url, data).then(function (response) {
            $scope.follow = response.data;
            console.log('Kết quả check: ' + $scope.follow );
        });
    }

    $scope.init = function (sID, uID) {
        $scope.sid = sID;
        $scope.uID = uID;
        $scope.getConverter();
        $scope.getStoryOfConverter();
        $scope.getFollowUser();
    };

}

