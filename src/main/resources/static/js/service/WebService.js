'use strict';

app.service('WebService', ['$http', function ($http) {
    this.getPage = function getPage(url, sID, pagenumber, type) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        if (type === undefined) {
            type = 1;
        }
        var data = new FormData();
        data.append('storyId', sID);
        data.append('pagenumber', pagenumber);
        data.append('type', type);
        var config = {
            headers: {
                'Content-Type': undefined
            }
        };
        return $http.post(url, data, config);
    };

    //Thêm Bình Luận
    this.addComment = function addComment(storyId, commentText) {
        var data = new FormData();
        data.append('storyId', storyId);
        data.append('commentText', encryptText(commentText));
        var url = window.location.origin + '/api/comment/add';
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

    //Load Bình Luận của Truyện
    this.loadComment = function loadComment(storyId, pagenumber, type) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        if (type === undefined) {
            type = 1;
        }
        var data = new FormData();
        data.append('storyId', storyId);
        data.append('pagenumber', pagenumber);
        data.append('type', type);
        var url = window.location.origin + '/api/comment/load';
        var config = {
            headers: {
                'Content-Type': undefined
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

    this.submitForm = function submitForm(url, data) {
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

    this.deleteData = function deleteData(url) {
        var config = {
            headers: {
                'Content-Type': undefined
            },
            transformResponse: function (data, headers, status) {
                return {messageError: data, status: status};
            }
        };
        return $http.delete(url, config);
    };
}]);