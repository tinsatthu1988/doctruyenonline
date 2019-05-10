var app = angular.module('ngApp', ['ngSanitize']);

app.controller('accDrawPayCtrl', accDrawPayCtrl);

accDrawPayCtrl.$inject = ['WebService', '$scope'];

function accDrawPayCtrl(WebService, $scope) {

    $scope.listPay = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.id = 0;
    $scope.drawMoney = 0;
    $scope.drawCoin = 0;
    $scope.getPayPage = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        var url = window.location.origin + '/api/pay/list_pay_draw';
        WebService.getData(url, data).then(function (response) {
            $scope.listPay = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = response.data.number + 1;
            var startPage = Math.max(1, $scope.currentPage - 2);
            var endPage = Math.min(startPage + 4, $scope.totalPages);
            var pages = [];
            for (let i = startPage; i <= endPage; i++) {
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

    $scope.deleteDraw = function (id) {
        alert(id);
    };

    $scope.changeValue = function () {
        $scope.drawCoin = $scope.drawMoney + ($scope.drawMoney * 0.1);
    };

    $scope.reset = function () {
        $scope.drawCoin = 0;
        $scope.drawMoney = 0;
    };

    $scope.submitWithDraw = function () {
        alert("Bạn đăng ký rút " + $scope.drawCoin + " đậu thành tiền mặt");
    };

    $scope.init = function (id) {
        $scope.id = id;
        $scope.getPayPage(1);
    }
}