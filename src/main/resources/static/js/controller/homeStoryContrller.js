var app = angular.module('ngApp', ['ui.tinymce', 'ngSanitize']);

app.controller('storyCtrl', storyCtrl);

storyCtrl.$inject = ['HomeService', '$scope'];

function storyCtrl(HomeService, $scope,) {

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
    $scope.totalCommentPages = 0;
    $scope.currentCommentPage = 1;
    $scope.pageComment = [];
    $scope.totalComment = 0;
    $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';
    $scope.coupon = null;

    //Lấy danh sách Chapter Theo sID, pagenumber, size
    $scope.getListChapter = function (pagenumber, size) {
        var url = window.location.origin + '/api/chapterOfStory';
        HomeService.getPage(url, $scope.sid, pagenumber, size).then(function (response) {
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
        })
    };

    $scope.getListComment = function (pagenumber, size) {
        var url = window.location.origin + '/api/commentOfStory';
        HomeService.getPage(url, $scope.sid, pagenumber, size).then(function (response) {
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
        });
    };

    $scope.addComment = function () {
        if ($scope.commentText.trim().length !== 0) {
            HomeService.addComment($scope.sid, $scope.commentText).then(function (response) {
                $scope.commentText = '';
                $scope.getListComment(1, 1);
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            })
        } else {
            callWarningSweetalert('Nội dung bình luận không được để trống!');
        }
    };

    $scope.getConverter = function () {
        var data = new FormData();
        data.append('uID', $scope.uID);
        var url = window.location.origin + '/api/converterInfo';
        // Lấy Thông Tin Converter
        HomeService.getData(url, data).then(function (response) {
            $scope.user = response.data;
            console.log($scope.user);
        }, function errorCallback(errResponse) {
            console.log('Có lỗi xảy ra!');
        });
        //Lấy Top 3 Truyện mới của converter
        url = window.location.origin + '/api/storyOfConverter';
        HomeService.getData(url, data).then(function (response) {
            $scope.listStory = response.data;
            console.log($scope.listStory);
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
            data.append('sID', $scope.sid);
            data.append('coupon', $scope.coupon);
            var url = window.location.origin + '/api/appoint';

            HomeService.submitForm(url, data).then(function (response) {
                $scope.coupon = null;
                swal({
                    text: 'Đề Cử Thành Công!',
                    type: 'success',
                    confirmButtonText: 'Ok'
                }).then(function () {
                    $('#appointModal').modal('hide');
                });
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            });
        }
    };

    $scope.init = function (sID, uID) {
        $scope.sid = sID;
        $scope.uID = uID;
        $scope.getConverter();
    };

    $scope.tinymceOptions = {
        plugins: ' emoticons image link lists textcolor ',
        menubar: false,
        skin: 'lightgray',
        theme: 'modern',
        relative_urls: false,
        toolbar1: ' formatselect fontselect fontsizeselect | numlist bullist outdent indent | removeformat',
        toolbar2: 'undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | forecolor  emoticons | link image',
        automatic_uploads: true,
        image_description: false,
        file_picker_types: 'image',
        file_picker_callback: function (cb, value, meta) {
            var input = document.createElement('input');
            input.setAttribute('type', 'file');
            input.setAttribute('accept', 'image/*');
            input.onchange = function () {
                var file = this.files[0];
                var reader = new FileReader();

                reader.onload = function () {
                    var id = 'blobid' + (new Date()).getTime();
                    var blobCache = tinymce.activeEditor.editorUpload.blobCache;
                    var base64 = reader.result.split(',')[1];
                    var blobInfo = blobCache.create(id, file, base64);
                    blobCache.add(blobInfo);
                    cb(blobInfo.blobUri(), {title: file.name});
                };
                reader.readAsDataURL(file);
            };
            input.click();
        }
    };
}

