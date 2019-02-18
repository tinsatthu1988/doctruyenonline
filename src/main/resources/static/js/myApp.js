window.TruyenOnlineScript = {
    init: function () {
        this.initApplyConfig();
        this.initAutoSearch();
        this.initAutoSearchMobile();
        this.initLayzLoad();
        this.initSearch();
        this.initSearchMobile();
        this.initSidebar();
        this.initSlideBar();
        this.initDetailSidebarSlider();
        this.initSlider();
    },

    initApplyConfig: function () {
        var $container = $('#js-truyencv-read-content'),
            $content = $('#js-truyencv-content'),
            $body = $('body'),
            lineHeight,
            fontSize,
            fontFamily,
            style,
            containerWidth;

        lineHeight = ($container.attr('data-line-height') || 140) + '%';
        fontSize = ($container.attr('data-font-size') || 22) + 'px';
        fontFamily = $container.attr('data-font-family') || "'Source Sans Pro', sans-serif";
        style = 'style-' + ($container.attr('data-style') || 3);
        containerWidth = ($container.attr('data-container-width') || 100) + '%';

        $content.css({lineHeight: lineHeight, fontSize: fontSize, fontFamily: fontFamily});
        $container.css('width', containerWidth);
        $body.removeClass(function (index, className) {
            return (className.match(/(^|\s)style-\S+/g) || []).join(' ');
        }).addClass(style);
    },

    initAutoSearch: function () {
        // Search Auto Complete Laptop
        $("#txtKeyword").autocomplete({
            minLength: 1,
            // define callback to format results
            source: function (request, response) {
                $.ajax({
                    type: "POST",
                    url: window.location.origin + '/api/search',
                    data: {
                        txtSearch: encryptText(this.term)
                    },
                    success: function (data) {
                        if (data.length === 0) {
                            data.push({
                                vnName: 'Không tìm thấy truyện phù hợp',
                                sID: 0
                            });
                        }
                        response($.map(data, function (item) {
                            return {
                                label: item.vnName,
                                value: item.vnName,
                                image: item.sImages,
                                author: item.sAuthor,
                                id: item.sID
                            }
                        }));
                    }

                });
            },
            // define select handler
            select: function (event, ui) {
                if (ui.item) {
                    if (ui.item.id !== 0) {
                        event.preventDefault();
                        window.location.replace(window.location.origin + '/truyen/' + ui.item.id);
                        return false;
                    }
                    return false;
                }
            }
        }).click(function () {
            $(this).autocomplete('search');
        }).autocomplete("instance")._renderItem = function (ul, item) {
            if (item.id === 0)
                return $("<li></li>")
                    .append("<div class='custom-ui-menu-item'> <strong>" + item.label + "</strong></div>")
                    .appendTo(ul);
            return $("<li></li>")
                .append("<div class='custom-ui-menu-item'> <div><img class='imageClass' src='" + item.image + "' > </div><span><strong>"
                    + item.label + "</strong><br>" + item.author + "</span></div>")
                .appendTo(ul);
        };
    },

    initAutoSearchMobile: function () {
        // Search Auto Complete Laptop
        $("#txtKeywordMobi").autocomplete({
            minLength: 1,
            // define callback to format results
            source: function (request, response) {
                $.ajax({
                    type: "POST",
                    url: window.location.origin + '/api/search',
                    data: {
                        txtSearch: encryptText(this.term)
                    },
                    success: function (data) {
                        if (data.length === 0) {
                            data.push({
                                vnName: 'Không tìm thấy truyện phù hợp',
                                sID: 0
                            });
                        }
                        response($.map(data, function (item) {
                            return {
                                label: item.vnName,
                                value: item.vnName,
                                image: item.sImages,
                                author: item.sAuthor,
                                id: item.sID
                            }
                        }));
                    }

                });
            },
            // define select handler
            select: function (event, ui) {
                if (ui.item) {
                    if (ui.item.id !== 0) {
                        event.preventDefault();
                        window.location.replace(window.location.origin + '/truyen/' + ui.item.id + '/' + ui.item.url);
                        return false;
                    }
                    return false;
                }
            }
        }).click(function () {
            $(this).autocomplete('search');
        }).autocomplete("instance")._renderItem = function (ul, item) {
            if (item.id === 0)
                return $("<li></li>")
                    .append("<div class='custom-ui-menu-item'> <strong>" + item.label + "</strong></div>")
                    .appendTo(ul);
            return $("<li></li>")
                .append("<div class='custom-ui-menu-item'> <div><img class='imageClass' src='" + item.image + "' > </div><span><strong>"
                    + item.label + "</strong><br>" + item.author + "</span></div>")
                .appendTo(ul);
        };
    },

    initLayzLoad: function () {
        $('.lazyload').lazy();
    },

    initSearch: function () {
        //Open Input Search Ipad
        var $searchPanel = $('#js-search-panel');
        $('#js-search-toggle').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            if (!$searchPanel.hasClass('active')) {
                $searchPanel.addClass('active');
                setTimeout(function () {
                    $('#txtKeyword').focus()
                }, 500);
            } else {
                $searchPanel.removeClass('active');
            }
        });

        $('#txtKeyword').click(function (i) {
            i.stopPropagation()
        });

        $('body').on('click', function () {
            $searchPanel.removeClass('active');
        })
    },

    initSearchMobile: function () {
        //Open Input Search Mobile
        $('.js-open-search-box-mobile').on('click', function (event) {
            event.preventDefault();
            $('body').addClass('open-search-box');
            setTimeout(function () {
                $('#js-search-input-mobile').focus()
            }, 500);
        });
        //Close Input Search Mobile
        $('.js-close-search-box').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            $('body').removeClass('open-search-box');
        });
    },

    initSidebar: function () {
        //Open Navbar Moblie
        $('.js-open-sidebar').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            $('body').addClass('open-sidebar');
        });
        //Close Navbar Moblie
        $('.js-close-sidebar').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            $('body').removeClass('open-sidebar');
        });
    },

    initSlideBar: function () {
        var $container = $('.js-editors-choice-slider'), $slider, swiper;
        $slider = $container.find('.swiper-container');
        swiper = new Swiper($slider, {
            pagination: {
                el: $slider.find('.swiper-pagination'),
                clickable: true,
            },
            navigation: {
                nextEl: $slider.find('.swiper-button-next'),
                prevEl: $slider.find('.swiper-button-prev'),
            },
            spaceBetween: 30,
            slidesPerView: 1,
            autoplay: {
                delay: 5000,
                disableOnInteraction: false,
            },
            loop: true
        });
    },

    initDetailSidebarSlider: function () {
        var $container = $('.js-detail-sidebar-related'), $slider, swiper;
        $slider = $container.find('.swiper-container');
        swiper = new Swiper($slider, {
            navigation: {
                nextEl: $slider.find('.swiper-button-next'),
                prevEl: $slider.find('.swiper-button-prev'),
            },
            spaceBetween: 30,
            slidesPerView: 1,
            loop: true,
            autoplay: {
                delay: 5000,
                disableOnInteraction: false,
            },
        });
    },

    initSlider: function () {
        var $containers = $('.js-truyen-slider');
        $containers.each(function () {
            var $container = $(this), $slider, $info, slider, info;
            $slider = $container.find('.slider-thumb .swiper-container');
            $info = $container.find('.slider-info .swiper-container');
            info = new Swiper($info, {
                spaceBetween: 30,
                slidesPerView: 1,
                loop: true,
                freeMode: true,
                watchSlidesVisibility: true,
                watchSlidesProgress: true,
                simulateTouch: false
            });
            slider = new Swiper($slider, {
                navigation: {
                    nextEl: $slider.find('.swiper-button-next'),
                    prevEl: $slider.find('.swiper-button-prev'),
                },
                effect: 'coverflow',
                grabCursor: true,
                centeredSlides: true,
                slidesPerView: 'auto',
                coverflowEffect: {
                    rotate: 0,
                    stretch: 0,
                    depth: 100,
                    modifier: 1,
                    slideShadows: true,
                },
                spaceBetween: 30,
                loop:true,
                thumbs: {
                    swiper: info,
                },
            });
        });
        window.dispatchEvent(new Event('resize'));
    },
    utils: {
        isMobile: function (agent) {
            return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(agent || window.navigator.userAgent);
        }
    },
};

$('document').ready(function () {
    window.TruyenOnlineScript.init();
});