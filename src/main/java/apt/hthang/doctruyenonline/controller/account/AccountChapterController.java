package apt.hthang.doctruyenonline.controller.account;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan/")
public class AccountChapterController {
    
    private final static Logger logger = LoggerFactory.getLogger(AccountChapterController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private UserService userService;
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping("/list_chuong/{id}")
    public String listChapterPage(@PathVariable("id") Long id, Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        Story story = storyService.findStoryById(id);
        if (story == null) {
            model.addAttribute("checkListChapter", "Truyện không tồn tại");
        }
        if (!story.getUser().getId().equals(user.getId())) {
            model.addAttribute("checkListChapter", "Bạn không có quyền quản lý truyện không do bạn đăng!");
        }
        getMenuAndInfo(model, "Danh sách Chapter đã đăng");
        
        model.addAttribute("story", story);
        
        return "web/account/listChapterPage";
    }

//    @GetMapping("/them_chuong/{id}")
//    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
//                               Principal principal) throws NotFoundException {
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        User user = userService.findUserById(loginedUser.getUser().getId());
//        if (user == null) {
//            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
//        }
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
//        Story story = storyService.findStoryById(id);
//        if (story == null) {
//            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//        if (!story.getUser().getId().equals(user.getId())) {
//            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền thêm Chapter thuộc Truyện không do bạn đăng!");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//
//        Chapter chapter = new Chapter();
//        ChapterSummary newChapter = chapterService.getChapterIDNew(id, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
//        if (newChapter != null) {
//            chapter.setSerial(newChapter.getSerial());
//        } else {
//            chapter.setSerial((float) 1);
//        }
//        chapter.setStory(story);
//        model.addAttribute("chapter", chapter);
//
//        getMenuAndInfo(model, titleHome);
//
//        return "web/account/addChapterPage";
//    }
//
//    @PostMapping("/them_chuong/{id}")
//    public String saveStoryAddPage(@PathVariable("id") Long id, @Valid Chapter chapter, BindingResult result, Model model,
//                                   Principal principal, RedirectAttributes redirectAttrs) throws NotFoundException {
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        User user = userService.findUserById(loginedUser.getUser().getId());
//        if (user == null) {
//            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
//        }
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
//        boolean hasError = result.hasErrors();
//        if (hasError) {
//            getMenuAndInfo(model, "Thêm Chương");
//            model.addAttribute("chapter", chapter);
//            return "web/account/addChapterPage";
//        }
//        chapter.setUser(loginedUser.getUser());
//        Story story = storyService.findStoryById(id);
//        if (story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
//            redirectAttrs.addFlashAttribute("checkAddStoryHidden", "Truyện đã bị khóa không thể đăng thêm chương");
//        } else {
//            if (story.getDealStatus().equals(1)) {
//                chapter.setStatus(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
//                chapter.setPrice(story.getPrice());
//                chapter.setDealine(DateUtils.getDateDeal(story.getTimeDeal()));
//            }
//            Chapter newChapter = chapterService.saveNewChapter(chapter);
//            if (newChapter.getId() != null)
//                redirectAttrs.addFlashAttribute("checkAddChapter", true);
//            else
//                redirectAttrs.addFlashAttribute("checkAddChapter", false);
//        }
//        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
//    }
//
//    @GetMapping("/sua_chuong/{id}")
//    public String editStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
//                                Principal principal) throws NotFoundException {
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        User user = userService.findUserById(loginedUser.getUser().getId());
//        if (user == null) {
//            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
//        }
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
//        Chapter chapter = chapterService.getChapterByID(id);
//        if (chapter == null) {
//            logger.info("Đã null");
//            redirectAttrs.addFlashAttribute("checkEditStory", "Chương không tồn tại");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//        if (!chapter.getUser().getId().equals(user.getId())) {
//            logger.info("Không quyền");
//            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter không do bạn đăng!");
//            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
//        }
//        if (chapter.getStory().getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
//            logger.info("Story khóa");
//            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//        getMenuAndInfo(model, titleHome);
//
//        model.addAttribute("chapter", chapter);
//
//        return "web/account/editChapterPage";
//    }
//
//    @PostMapping("/sua_chuong/{id}")
//    public String saveStoryEditPage(@PathVariable("id") Long id, @Valid Chapter chapter, BindingResult result, Model model,
//                                    Principal principal, RedirectAttributes redirectAttrs) throws NotFoundException {
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        User user = userService.findUserById(loginedUser.getUser().getId());
//        if (user == null) {
//            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
//        }
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
//        boolean hasError = result.hasErrors();
//        if (hasError) {
//            getMenuAndInfo(model, titleHome);
//            model.addAttribute("chapter", chapter);
//            return "web/account/editChapterPage";
//        }
//        Story story = storyService.getStoryById(chapter.getStory().getId());
//        if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
//            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//        if (chapter.getStatus().equals(ConstantsUtils.CHAPTER_DENIED)) {
//            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter bị khóa!");
//            return "redirect:/tai-khoan/quan_ly_truyen";
//        }
//        boolean check = chapterService.saveEditChapter(chapter);
//        if (check)
//            redirectAttrs.addFlashAttribute("checkEditChapterTrue", "Sửa chương Thành Công!");
//        else
//            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Sửa chương thất bại! Có lỗi xảy ra!");
//        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
//    }
}
