package apt.hthang.doctruyenonline.controller.account;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Đời Không Như Là Mơ
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
    public String listChapterPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, Principal principal) throws NotFoundException {
        getMenuAndInfo(model, "Danh sách Chapter đã đăng");
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
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!story.getUser().getId().equals(user.getId())) {
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Bạn không có quyền quản lý truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        model.addAttribute("story", story);
        
        return "view/account/accChapterPage";
    }
    
    @GetMapping("/them_chuong/{id}")
    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                               Principal principal) throws NotFoundException {
        getMenuAndInfo(model, "Thêm Chương Mới");
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
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!story.getUser().getId().equals(user.getId())) {
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Bạn không có quyền quản lý truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!model.containsAttribute("chapter")) {
            Chapter chapter = new Chapter();
            ChapterSummary newChapter = chapterService.findChapterNewOfStory(id, ConstantsListUtils.LIST_CHAPTER_STATUS_ALL);
            float serialNumber = newChapter.getSerial();
            if (newChapter != null) {
                chapter.setSerial((float) ((int) serialNumber + 1));
            } else {
                chapter.setSerial((float) 1);
            }
            chapter.setStory(story);
            model.addAttribute("chapter", chapter);
        }
        return "view/account/addChapterPage";
    }
    
    @PostMapping("/them_chuong/{id}")
    public String saveStoryAddPage(@PathVariable("id") Long id, @Valid Chapter chapter, BindingResult result, Model model,
                                   Principal principal, RedirectAttributes redirectAttrs) throws NotFoundException {
        getMenuAndInfo(model, "Thêm Chương Mới");
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
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!story.getUser().getId().equals(user.getId())) {
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Bạn không có quyền quản lý truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, "Thêm Chương");
            model.addAttribute("chapter", chapter);
            return "view/account/addChapterPage";
        }
        chapter.setUser(loginedUser.getUser());
        if (story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Truyện đã bị khóa không thể đăng thêm chương");
        } else {
            if (story.getDealStatus().equals(1)) {
                chapter.setStatus(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
                chapter.setPrice(story.getPrice());
                chapter.setDealine(DateUtils.getDateDeal(story.getTimeDeal()));
            }
            boolean check = chapterService.saveNewChapter(chapter);
            redirectAttrs.addFlashAttribute("checkAddChapter", check);
        }
        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
    }
    
    @GetMapping("/sua_chuong/{id}")
    public String editStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                                Principal principal) throws NotFoundException {
        getMenuAndInfo(model, "Cập Nhật Chapter");
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        Chapter chapter = chapterService.findChapterById(id);
        if (chapter == null) {
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Chương không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!chapter.getUser().getId().equals(user.getId())) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền sửa Chapter không do bạn đăng!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        if (chapter.getStory().getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        chapter.setContent(chapter.getContent().replaceAll("(?i)<br */?>", "\n"));
        model.addAttribute("chapter", chapter);
        model.addAttribute("statusList", ConstantsListUtils.LIST_CHAPTER_STATUS_VIEW_ALL);
        return "view/account/editChapterPage";
    }
    
    @PostMapping("/sua_chuong/save")
    public String saveStoryEditPage(@Valid Chapter chapter, BindingResult result, Model model,
                                    Principal principal, RedirectAttributes redirectAttrs) throws NotFoundException {
        getMenuAndInfo(model, "Cập Nhật Chapter");
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        Chapter chapterEdit = chapterService.findChapterById(chapter.getId());
        if (chapterEdit == null) {
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Chương không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!chapterEdit.getUser().getId().equals(user.getId())) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền sửa Chapter không do bạn đăng!");
            return "redirect:/tai-khoan/list_chuong/" + chapterEdit.getStory().getId();
        }
        if (chapterEdit.getStory().getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        boolean hasError = result.hasErrors();
        if (hasError) {
            model.addAttribute("statusList", ConstantsListUtils.LIST_CHAPTER_STATUS_VIEW_ALL);
            model.addAttribute("chapter", chapter);
            return "view/account/editChapterPage";
        }
        Story story = storyService.findStoryById(chapter.getStory().getId());
        if (story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        Chapter editChapter = chapterService.findChapterById(chapter.getId());
        if (chapterEdit.getStatus().equals(ConstantsStatusUtils.CHAPTER_DENIED) && chapter.getStatus().equals(chapterEdit.getStatus())) {
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Bạn không có quyền Cập Nhật Trạng Thái Chapter bị khóa!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        chapter.setContent(chapter.getContent().replaceAll("\n", "<br />"));
        boolean check = chapterService.updateChapter(chapter);
        if (check)
            redirectAttrs.addFlashAttribute("checkEditChapterTrue", "Cập Nhật chương Thành Công!");
        else
            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Cập Nhật chương thất bại! Có lỗi xảy ra!");
        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
    }
}
