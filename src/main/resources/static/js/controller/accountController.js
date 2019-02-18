var app = angular.module("ngApp", []);

app.controller("myAccount", ['$compile', '$http', '$scope', function ($compile, $http, $scope) {
    $scope.changeNick = function () {
        var data = new FormData();
        data.append('uDname', $("#txtChangenick").val());
        var url = window.location.origin + '/api/saveDName';
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messager: data, status: status};
                return ret;
            }
        };
        $http.post(url, data, config).then(function successCallback(response) {
            swal({
                text: 'Thay đổi ngoại hiệu thành công!',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload(true);
            })
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messager,
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
        data.append('notification', $("#txtAbout").val());
        var url = window.location.origin + '/api/saveNotification';
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messager: data, status: status};
                return ret;
            }
        };
        $http.post(url, data, config).then(function successCallback(response) {
            swal({
                text: 'Cập Nhật Thông Báo thành công!',
                type: 'success',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.reload(true);
            })
        }, function errorCallback(errResponse) {
            swal({
                text: errResponse.data.messager,
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
}]);