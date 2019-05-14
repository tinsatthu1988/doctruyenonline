package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tim-kiem")
public class SearchController {
    
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    @Autowired
    public SearchController(InformationService informationService,
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
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    //Lấy danh sách truyện theo
    // searchKey - Từ khóa tìm kiếm
    // pagenumber - Số trang
    private void loadData(String searchKey, int pagenumber, Model model) {
        Page< StoryUpdate > page = storyService
                .findStoryBySearchKey(searchKey,
                        ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        
        // Lấy tổng số trang
        int total = page.getTotalPages();
        
        // Kiểm tra tổng số trang có nhỏ hơn pagenumber không
        if (total < pagenumber) {
            pagenumber = ConstantsUtils.PAGE_DEFAULT;
            page = storyService.findStoryBySearchKey(searchKey,
                    ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY,
                    pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
            total = page.getTotalPages();
        }
        
        // Lấy List Story
        List< StoryUpdate > lstStory = page.get().collect(Collectors.toList());
        
        // Lấy số trang hiện tại
        int current = page.getNumber() + 1;
        
        // Lấy số trang bắt đầu
        int begin = Math.max(1, current - 2);
        
        //Lấy số trang kết thúc
        int end = Math.min(begin + 4, page.getTotalPages());
        
        String currentIndex = "/tim-kiem/" + UriUtils.encode(searchKey, "UTF-8");
        model.addAttribute("listStory", lstStory);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("totalIndex", total);
        model.addAttribute("currentIndex", current);
    }
    
    @RequestMapping(value = "/{txtKeyWord}")
    public String pageSearchStory(@PathVariable("txtKeyWord") String txtKeyWord, Model model) {
        
        model.addAttribute("txtKeyWord", txtKeyWord.toLowerCase());
        
        getMenuAndInfo(model, "Tìm Kiếm '" + txtKeyWord + "'");
        
        loadData(txtKeyWord, ConstantsUtils.PAGE_DEFAULT, model);
        
        return "view/searchPage";
    }
    
    @RequestMapping(value = "/{txtKeyWord}/trang-{page}")
    public String pageSearchStory(@PathVariable("txtKeyWord") String txtKeyWord, @PathVariable("page") String page, Model model) {
        
        int pagenumber = WebUtils.checkPageNumber(page);
        
        model.addAttribute("txtKeyWord", txtKeyWord.toLowerCase());
        
        getMenuAndInfo(model, "Tìm Kiếm '" + txtKeyWord + " '");
        
        loadData(txtKeyWord, pagenumber, model);
        
        return "view/searchPage";
    }
}
