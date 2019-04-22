encryptText = function (myText) {
    var passphrase = '1234567891234567';

    var iv = CryptoJS.lib.WordArray.random(128 / 8).toString(CryptoJS.enc.Hex);
    var salt = CryptoJS.lib.WordArray.random(128 / 8).toString(CryptoJS.enc.Hex);

    var aesUtil = new AesUtil(128, 1000);
    var ciphertext = aesUtil.encrypt(salt, iv, passphrase, myText);

    var aesSearch = (iv + "::" + salt + "::" + ciphertext);
    return btoa(aesSearch);
};



callWarningSweetalert = function (messager) {
    swal({
        text: messager,
        type: 'warning',
        confirmButtonText: 'Ok'
    })
};

callSuccessSweetalert = function (messager) {
    swal({
        text: messager,
        type: 'success',
        confirmButtonText: 'Ok'
    })
};
