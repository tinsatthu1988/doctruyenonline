$('document').ready(function () {
    $('body').append("<div id='backToTop' title='Lên Đầu Trang'><span>&#10148;</span></div>");
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('#backToTop').fadeIn();
        } else {
            $('#backToTop').fadeOut();
        }
    });
    $('#backToTop').click(function () {
        $("html, body").bind("scroll mousedown DOMMouseScroll mousewheel keyup", function () {
            $('html, body').stop();
        });
        $('html,body').animate({scrollTop: 0}, 1200);
        return false;
    });
})