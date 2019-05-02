var app = angular.module('ngApp', []);

app.controller('accHomeCtrl', accHomeCtrl);

accHomeCtrl.$inject = ['WebService', '$scope'];

function accHomeCtrl(WebService, $scope) {

    $scope.changeNick = function () {
        var data = new FormData();
        data.append('txtChangenick', encryptText($("#txtChangenick").val()));
        var url = window.location.origin + '/api/user/changeNick';
        WebService.submitForm(url, data).then(function successCallback(response) {
            swal({
                text: 'Thay đổi ngoại hiệu thành công!',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload(true);
            })
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

    $scope.updateNotification = function () {
        var data = new FormData();
        data.append('notification', encryptText($("#txtAbout").val()));
        var url = window.location.origin + '/api/user/saveNotification';
        WebService.submitForm(url, data).then(function successCallback(response) {
            swal({
                text: 'Cập Nhật Thông Báo thành công!',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload(true);
            })
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
    }

}