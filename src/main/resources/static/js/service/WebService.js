'use strict';

app.service('HomeService', ['$http', function ($http) {
    this.getPage = function getPage(url, sID, pagenumber, size) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        if (size === undefined) {
            size = 1;
        }
        var data = new FormData();
        data.append('sID', sID);
        data.append('pagenumber', pagenumber);
        data.append('size', size);
        var config = {
            headers: {
                'Content-Type': undefined
            }
        };
        return $http.post(url, data, config);
    };

    this.addComment = function addComment(sID, commentText) {
        var data = new FormData();
        data.append('sID', sID);
        data.append('commentText', encryptText(commentText));
        var url = window.location.origin + '/api/add/commentOfStory';
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messageError: data, status: status};
                return ret;
            }
        };
        return $http.post(url, data, config);
    };

    this.getData = function getData(url, data) {
        var config = {
            headers: {
                'Content-Type': undefined
            }
        };
        return $http.post(url, data, config);
    };

    this.submitForm = function addComment(url, data) {
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messageError: data, status: status};
                return ret;
            }
        };
        return $http.post(url, data, config);
    };

    this.deleteData = function getData(url) {
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                var ret = {messageError: data, status: status};
                return ret;
            }
        };
        return $http.delete(url, config);
    };
}]);
