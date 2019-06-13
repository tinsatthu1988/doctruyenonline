package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.projections.StoryTop;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.*;
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
 * @author Đời Không Như Là Mơ on 17/10/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/danh-muc/moi-cap-nhat")
public class NewStoryController {
    
    Logger logger = LoggerFactory.getLogger(NewStoryController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoryService storyService;
    @Value("${hthang.truyenonline.title.new.story}")
    private String titleNewStory;
    
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    private void loadData(int pagenumber, Model model) {
        Page< StoryUpdate > page = storyService.findStoryUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                ConstantsListUtils.LIST_STORY_DISPLAY,
                pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        
        // Lấy tổng số trang
        int total = page.getTotalPages();
        
        // Kiểm tra tổng số trang có nhỏ hơn pagenumber không
        if (total < pagenumber) {
            pagenumber = ConstantsUtils.PAGE_DEFAULT;
            page = storyService.findStoryUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                    ConstantsListUtils.LIST_STORY_DISPLAY,
                    pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
            total = page.getTotalPages();
        }
        
        // Lấy List Story
        List< StoryUpdate > lstStory = page.getContent();
        
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
        model.addAttribute("urlIndex", "/danh-muc/hoan-thanh");
    }
    
    private void loadTopView(Model model) {
        //Lấy ngày bắt đầu của tháng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();
        
        //Lấy ngày kết thúc của tháng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();
        
        //Lấy Top View Truyện Vip Trong Tháng
        List< StoryTop > listtop = storyService.
                findStoryTopViewByStatuss(ConstantsListUtils.LIST_STORY_DISPLAY, firstDayOfMonth, lastDayOfMonth,
                        ConstantsStatusUtils.HISTORY_VIEW, ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_TOP_VIEW_DEFAULT)
                .get()
                .collect(Collectors.toList());
        
        model.addAttribute("topview", listtop);
    }
    
    @RequestMapping()
    public String defaultCompleteStory(Model model) {
        
        getMenuAndInfo(model, titleNewStory);
        
        loadData(ConstantsUtils.PAGE_DEFAULT, model);
        
        loadTopView(model);
        
        return "view/catalogPage";
    }
    
    @RequestMapping(value = "/trang-{page}")
    public String pageNewStory(@PathVariable("page") String page, Model model) {
        
        int pagenumber = WebUtils.checkPageNumber(page);
        
        getMenuAndInfo(model, titleNewStory);
        
        loadData(pagenumber, model);
        
        loadTopView(model);
        
        return "view/catalogPage";
    }
    
}