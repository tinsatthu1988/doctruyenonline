package online.hthang.truyenonline.controller.web;

import online.hthang.truyenonline.projections.NewStory;
import online.hthang.truyenonline.projections.TopStory;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.service.InformationService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import online.hthang.truyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huy Thang on 17/10/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/danh-muc/moi-cap-nhat")
public class NewStoryController {

    private final InformationService informationService;

    private final CategoryService categoryService;

    private final StoryService storyService;

    Logger logger = LoggerFactory.getLogger(NewStoryController.class);

    @Value("${hthang.truyenmvc.title.new.story}")
    private String titleNewStory;

    @Autowired
    public NewStoryController(InformationService informationService,
                              CategoryService categoryService,
                              StoryService storyService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
    }

    private void getMenuAndInfo(Model model, String title) {

        // Lấy Title Cho Page
        model.addAttribute("title", title);

        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getCategoryMenu());

        // Lấy Information của Webm
        model.addAttribute("information", informationService.getWebInfomation());
    }

    private void loadData(int pagenumber, Model model) {
        Page<NewStory> page = storyService.getStoryNew(pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);

        // Lấy tổng số trang
        int total = page.getTotalPages();

        // Kiểm tra tổng số trang có nhỏ hơn pagenumber không
        if (total < pagenumber) {
            pagenumber = ConstantsUtils.PAGE_DEFAULT;
            page = storyService.getStoryNew(pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
            total = page.getTotalPages();
        }

        // Lấy List Story
        List<NewStory> lstStory = page.get().collect(Collectors.toList());

        // Lấy số trang hiện tại
        int current = page.getNumber() + 1;

        // Lấy số trang bắt đầu
        int begin = Math.max(1, current - 2);

        //Lấy số trang kết thúc
        int end = Math.min(begin + 4, page.getTotalPages());
        model.addAttribute("listStory", lstStory);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("totalIndex", total);
        model.addAttribute("currentIndex", current);
        model.addAttribute("urlIndex", "/danh-muc/moi-cap-nhat");
    }

    private void loadTopView(Model model) {
        //Lấy ngày bắt đầu của tháng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();

        //Lấy ngày kết thúc của tháng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        //Lấy Top View Truyện Vip Trong Tháng
        List<TopStory> listtop = storyService.getTopStory(firstDayOfMonth, lastDayOfMonth,
                ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_SWAPPER)
                .get()
                .collect(Collectors.toList());

        model.addAttribute("topview", listtop);
    }

    @RequestMapping()
    public String defaultCompleteStory(Model model) {

        getMenuAndInfo(model, titleNewStory);

        loadData(ConstantsUtils.PAGE_DEFAULT, model);

        loadTopView(model);

        return "web/catalogPage";
    }

    @RequestMapping(value = "/trang-{page}")
    public String pageNewStory(@PathVariable("page") String page, Model model) {

        int pagenumber = WebUtils.checkPageNumber(page);

        getMenuAndInfo(model, titleNewStory);

        loadData(pagenumber, model);

        loadTopView(model);

        return "web/catalogPage";
    }

}