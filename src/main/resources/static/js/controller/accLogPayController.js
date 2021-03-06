var app = angular.module('ngApp', ['ngSanitize']);

app.controller('accLogPayCtrl', accLogPayCtrl);

accLogPayCtrl.$inject = ['WebService', '$scope'];

function accLogPayCtrl(WebService, $scope) {

    $scope.listPay = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.id = 0;
    $scope.getPayPage = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        var url = window.location.origin + '/api/pay/list_pay';
        WebService.getData(url, data).then(function (response) {
            $scope.listPay = response.data.content;
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
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                if (errResponse.status === 403) {
                    window.location.reload(true);
                }
                if (errResponse.status === 500) {
                    var url = window.location.origin + '/logout';
                    window.location.href = url;
                }
            })
        });
    };

    $scope.getMoney = function (item) {
        var result = "";
        if (item.status === 0) {
            result = "0";
        } else {
            if (item.type === 1) {
                result = "+" + item.money;
            } else if (item.type === 5 || item.type === 2 || item.type === 4) {
                result = "-" + item.money;
            } else {
                if (item.sendId.id === 1)
                    result = "-" + item.money;
                else if (item.receivedId.id === 1)
                    result = "+" + item.money;
            }
        }
        return result;
    };

    $scope.getContent = function (item) {
        var result = "";
        if (item.type === 1)
            result = "???? n???p " + item.money + " ?????u v??o t??i kho???n";
        else if (item.type === 2)
            result = "Thanh to??n " + item.money + " ?????u ????? ?????i ngo???i hi???u";
        else if (item.type === 4) {
            result = "Thanh to??n " + item.money + " ?????u ????? c??? " + item.vote + " Nguy???t Phi???u cho Truy???n <a href='/truyen/" + item.story.id + "'>" + item.story.vnName + " </a>";
        } else if (item.type === 5) {
            result = "???? ?????i " + item.money + " ?????u th??nh Ti???n m???t";
        } else {
            if (item.sendId.id === 1)
                result = "Thanh to??n " + item.money + " ?????u ?????c Vip Truy???n <a href='/truyen/" + item.chapter.story.id + "'>" + item.chapter.story.vnName + " </a> Ch????ng <a href='/truyen/" + item.chapter.story.id + "/chuong-" + item.chapter.id + "'> Ch????ng " + item.chapter.chapterNumber + " </a>";
            else if (item.receivedId.id === 1)
                result = "Nh???n " + item.money + " ?????u t??? Vip Truy???n <a href='/truyen/" + item.chapter.story.id + "'>" + item.chapter.story.vnName + " </a> Ch????ng <a href='/truyen/" + item.chapter.story.id + "/chuong-" + item.chapter.id + "'> Ch????ng " + item.chapter.chapterNumber + " </a>";
        }
        return result;
    };

    $scope.init = function (id) {
        $scope.id = id;
        $scope.getPayPage(1);
    }
}