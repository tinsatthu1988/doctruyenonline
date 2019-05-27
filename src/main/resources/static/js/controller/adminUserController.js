var app = angular.module('ngApp', []);

app.controller('adUserCtrl', adUserCtrl);

adUserCtrl.$inject = ['$scope', 'WebService'];

function adUserCtrl($scope, WebService) {

    $scope.listCategory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 1;
    $scope.page = [];
    $scope.search = '';
    $scope.typeList = [
        {'id': 4, 'label': 'USER'},
        {'id': 1, 'label': 'ADMIN'},
        {'id': 2, 'label': 'MOD'},
        {'id': 3, 'label': 'CONVERTER'}
    ];
    $scope.drawCoin = 0;
    $scope.type = $scope.typeList[0];
    $scope.userId = 0;
    $scope.init = function () {
        $scope.getAdListUser(1);
    };

    $scope.setID = function (userId) {
        $scope.userId = userId;
    }
    $scope.reset = function () {
        $scope.drawCoin = 0;
    };

    $scope.getAdListUser = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/user/admin/listUser';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append('search', $scope.search.trim());
        data.append('type', $scope.type.id);
        WebService.getData(url, data).then(function (response) {
            $scope.listCategory = response.data.content;
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

        })
    };

    $scope.deleteUser = function (id) {
        swal({
            title: 'Bạn có chắc?',
            text: "Bạn muốn xóa người dùng này!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng Ý',
            cancelButtonText: 'Hủy'
        }).then(function (result) {
            if (result.value) {
                var url = window.location.origin + '/api/user/admin/delete/' + id;
                WebService.deleteData(url).then(function (response) {
                    swal({
                        text: 'Xóa Thành Công',
                        type: 'success',
                        confirmButtonText: 'Ok'
                    }).then(function () {
                        $scope.getAdListUser(1);
                    })
                }, function errorCallback(errResponse) {
                    swal({
                        text: errResponse.data.messageError,
                        type: 'warning',
                        confirmButtonText: 'Ok'
                    })
                })
            }
        })
    }

    $scope.submitWithDraw = function () {
        var url = window.location.origin + '/api/pay/rechange';
        var data = new FormData();
        data.append("money", $scope.drawCoin);
        data.append("reId", $scope.userId);
        $('#submitWD').val("Đang Xử Lý").attr("disabled", true);
        $('#numberExample').attr('readonly', true);
        $('#canSubmit').attr("disabled", true);
        // $('#submitWD').val("Đang Xử Lý");
        WebService.submitForm(url, data).then(function (response) {
            swal({
                text: "Nạp đậu thành tiền mặt thành công!",
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
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
                    $scope.getAdListUser(1);
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
}