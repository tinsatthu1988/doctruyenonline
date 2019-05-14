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
                    window.location.href = window.location.origin + '/logout';
                }
            })
        });
    };

    $scope.deleteDraw = function (payId) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn hủy giao dịch rút tiền với mã GD: " + payId,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/pay/cancel_pay/' + payId;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: "Bạn hủy thành công đăng ký rút tiền có mã GD: " + payId,
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(
                        $scope.getPayPage(1)
                    );
                }, function errorCallback(errResponse) {
                    swal({
                        text: errResponse.data.messageError,
                        type: 'warning',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        if (errResponse.status === 400) {
                            $scope.getPayPage(1);
                        }
                        if (errResponse.status === 403) {
                            window.location.reload(true);
                        }
                        if (errResponse.status === 500) {
                            window.location.href = window.location.origin + '/logout';
                        }
                    })
                });
            }
        });
    };

    $scope.changeValue = function () {
        $scope.drawCoin = $scope.drawMoney + ($scope.drawMoney * 0.1);
    };

    $scope.reset = function () {
        $scope.drawCoin = 0;
        $scope.drawMoney = 0;
    };

    $scope.submitWithDraw = function () {
        var url = window.location.origin + '/api/pay/save_draw_pay';
        var data = new FormData();
        data.append("money", $scope.drawCoin);
        data.append("moneyVND", $scope.drawMoney);
        $('#submitWD').val("Đang Xử Lý").attr("disabled", true);
        $('#numberExample').attr('readonly', true);
        $('#canSubmit').attr("disabled", true);
        // $('#submitWD').val("Đang Xử Lý");
        WebService.submitForm(url, data).then(function (response) {
            swal({
                text: "Bạn đăng ký rút: " + $scope.drawCoin + " đậu thành tiền mặt thành công!",
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                $scope.getPayPage(1);
                $scope.reset();
                templates.modal('hide');
                $("#submitWD").attr("disabled", false).val("Đăng Ký");
                $('#numberExample').attr('readonly', false);
                $('#canSubmit').attr("disabled", false);
            });
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messageError,
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                $("#submitWD").attr("disabled", false).val("Đăng Ký");
                $('#numberExample').attr('readonly', false);
                $('#canSubmit').attr("disabled", false);
                if (errResponse.status === 400) {
                    $scope.getPayPage(1);
                }
                if (errResponse.status === 403) {
                    window.location.reload(true);
                }
                if (errResponse.status === 500) {
                    window.location.href = window.location.origin + '/logout';
                }
            })
        });
    };

    $scope.init = function (id) {
        $scope.id = id;
        $scope.getPayPage(1);
    }
}