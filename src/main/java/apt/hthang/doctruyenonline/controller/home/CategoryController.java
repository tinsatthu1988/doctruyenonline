package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.CategorySummary;
import apt.hthang.doctruyenonline.projections.StoryTop;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Đời Không Như Là Mơ
 */
@Controller
@RequestMapping("/the-loai")
public class CategoryController {
    
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    public CategoryController(InformationService informationService, CategoryService categoryService, StoryService storyService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
    }
    
    private CategorySummary checkCategoryID(String cid) throws Exception {
        
        // Kiểm tra cid != null
        // Kiểm tra cid có phải kiểu int
        if (cid == null || !WebUtils.checkIntNumber(cid)) {
            throw new NotFoundException();
        }
        
        return categoryService
                .getCategoryByID(Integer.parseInt(cid), ConstantsStatusUtils.CATEGORY_ACTIVED);
    }
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    private void loadData(CategorySummary category, int pagenumber, Model model) {
        Page< StoryUpdate > page = storyService
                .findStoryNewUpdateByCategoryId(category.getId(),
                        pagenumber,
                        ConstantsUtils.PAGE_SIZE_DEFAULT,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        
        // Lấy tổng số trang
        int total = page.getTotalPages();
        
        // Kiểm tra tổng số trang có nhỏ hơn pagenumber không
        if (total < pagenumber) {
            pagenumber = ConstantsUtils.PAGE_DEFAULT;
            page = storyService.findStoryNewUpdateByCategoryId(category.getId(),
                    pagenumber,
                    ConstantsUtils.PAGE_SIZE_DEFAULT,
                    ConstantsListUtils.LIST_STORY_DISPLAY,
                    ConstantsListUtils.LIST_CHAPTER_DISPLAY);
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
        
        String urlIndex = "/the-loai/" + category.getId() + "/" + category.getMetatitle();
        model.addAttribute("listStory", lstStory);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("totalIndex", total);
        model.addAttribute("currentIndex", current);
        model.addAttribute("urlIndex", urlIndex);
    }
    
    private void loadTopViewAndVote(Model model, CategorySummary category) {
        //Lấy ngày bắt đầu của tháng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();
        
        //Lấy ngày bắt đầu của tuần
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek();
        
        //Lấy ngày kết thúc của tháng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();
        
        //Lấy ngày kết thúc của tuần
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek();
        
        //Lấy Top View Truyện Theo Thể Loại Trong tuần
        List< StoryTop > listTopViewWeek = storyService
                .findStoryTopViewByCategoryId(category.getId(),
                        ConstantsStatusUtils.HISTORY_VIEW,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        firstDayOfWeek, lastDayOfWeek,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .get()
                .collect(Collectors.toList());
        
        //Lấy Top Truyện Đề cử Theo Thể Loại Trong Tháng
        List< StoryTop > listTopAppointMonth = storyService
                .findStoryTopVoteByCategoryId(category.getId(),
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsPayTypeUtils.PAY_APPOINT_TYPE, ConstantsStatusUtils.PAY_COMPLETED,
                        firstDayOfMonth, lastDayOfMonth,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .get()
                .collect(Collectors.toList());
        
        model.addAttribute("listTopViewWeek", listTopViewWeek);
        
        model.addAttribute("listTopAppointMonth", listTopAppointMonth);
    }
    
    @RequestMapping("/{cid}")
    public String theloaiPage(@PathVariable("cid") String cid) throws Exception {
        
        // Lấy Category theo cID
        CategorySummary category = checkCategoryID(cid);
        
        return "redirect:/the-loai/" + cid + "/" + category.getMetatitle();
    }
    
    @RequestMapping("/{cid}/{cmetaTitle}")
    public String theloaiPage(@PathVariable("cid") String cid,
                              @PathVariable("cmetaTitle") String cmetaTitle,
                              Model model) throws Exception {
    
        CategorySummary category = checkCategoryID(cid);
        
        // Kiểm tra Metatitle có đúng hay không
        // Nếu không chuyển trang sang định dạng đúng
        if (!cmetaTitle.equalsIgnoreCase(category.getMetatitle())) {
            
            return "redirect:/the-loai/" + cid + "/" + category.getMetatitle();
            
        }
        
        String title = "Truyện " + category.getName();
        
        // Lấy Thông tin Category cho menu và Information Web
        getMenuAndInfo(model, title);
        
        loadData(category, ConstantsUtils.PAGE_DEFAULT, model);
    
        loadTopViewAndVote(model, category);
        
        return "view/categoryPage";
    }
    
    @RequestMapping("/{cid}/{cmetaTitle}/trang-{page}")
    public String doLoadStoryByCategoryAndPage(@PathVariable("cid") String cid,
                                               @PathVariable("cmetaTitle") String cmetaTitle,
                                               @PathVariable("page") String page,
                                               Model model) throws Exception {
    
        CategorySummary category = checkCategoryID(cid);
        // Kiểm tra Metatitle có đúng hay không
        // Nếu không chuyển trang sang định dạng đúng
        if (!cmetaTitle.equalsIgnoreCase(category.getMetatitle())) {
            
            return "redirect:/the-loai/" + cid + "/" + category.getMetatitle() + "/trang-" + page;
        }
        
        
        int pagenumber = WebUtils.checkPageNumber(page);
        
        String title = "Truyện " + category.getName();
        
        // Lấy Thông tin Category cho menu và Information Web
        getMenuAndInfo(model, title);
        
        loadData(category, pagenumber, model);
    
        loadTopViewAndVote(model, category);
        
        return "view/categoryPage";
    }
}
