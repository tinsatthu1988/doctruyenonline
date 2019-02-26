package apt.hthang.doctruyenonline.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Huy Thang on 16/11/2018
 * @project truyenonline
 */
public class ConstantsListUtils {

//    public static final List< Status > LIST_STORY_STATUS_VIP = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(ConstantsUtils.STORY_NOT_VIP , "Miễn Phí"));
//                add(new Status(ConstantsUtils.STORY_VIP, "Trả Phí"));
//            }}
//    );
//    public static final List< Status > LIST_STORY_STATUS_CONVERTER = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(ConstantsUtils.STORY_STATUS_GOING_ON, "Đang ra"));
//                add(new Status(ConstantsUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
//                add(new Status(ConstantsUtils.STORY_STATUS_STOP, "Tạm Dừng"));
//            }}
//    );
//
//    public static final List< Status > LIST_STORY_STATUS = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(ConstantsUtils.STORY_STATUS_GOING_ON, "Đang ra"));
//                add(new Status(ConstantsUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
//                add(new Status(ConstantsUtils.STORY_STATUS_STOP, "Tạm Dừng"));
//                add(new Status(ConstantsUtils.STORY_STATUS_HIDDEN, "Ẩn"));
//            }}
//    );
//    public static final List< Status > LIST_STORY_STATUS_SELECTED = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(-1, "--Tất Cả --"));
//                add(new Status(ConstantsUtils.STORY_STATUS_GOING_ON, "Đang ra"));
//                add(new Status(ConstantsUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
//                add(new Status(ConstantsUtils.STORY_STATUS_STOP, "Tạm Dừng"));
//                add(new Status(ConstantsUtils.STORY_STATUS_HIDDEN, "Bị Ẩn"));
//            }}
//    );
//    public static final List< Status > LIST_CHAPTER = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(ConstantsUtils.CHAPTER_VIP_ACTIVED, "Vip - Hiển Thị"));
//                add(new Status(ConstantsUtils.CHAPTER_ACTIVED, "Hiển Thị"));
//                add(new Status(ConstantsUtils.CHAPTER_DENIED, "Ẩn"));
//            }}
//    );
//
//    public static final List< Status > LIST_PAY_STATUS_SELECTED = Collections.unmodifiableList(
//            new ArrayList< Status >() {{
//                add(new Status(-1, "--Tất Cả --"));
//                add(new Status(ConstantsUtils.PAY_RECHARGE_TYPE, "Nạp Tiền"));
//                add(new Status(ConstantsUtils.PAY_DNAME_TYPE, "Đổi Ngoại Hiệu"));
//                add(new Status(ConstantsUtils.PAY_CHAPTER_VIP_TYPE, "Trả Vip"));
//                add(new Status(ConstantsUtils.PAY_STORY_APPOINT_TYPE, "Ủng Hộ"));
//            }}
//    );
    
    //List Status của chapter có trạng thái hiển thị
    public static final List< Integer > LIST_STORY_DISPLAY = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.STORY_STATUS_COMPLETED);
                add(ConstantsStatusUtils.STORY_STATUS_GOING_ON);
                add(ConstantsStatusUtils.STORY_STATUS_STOP);
            }}
    );

//    public static final List< Integer > LIST_STORY_ALL = Collections.unmodifiableList(
//            new ArrayList< Integer >() {{
//                add(ConstantsUtils.STORY_STATUS_COMPLETED);
//                add(ConstantsUtils.STORY_STATUS_GOING_ON);
//                add(ConstantsUtils.STORY_STATUS_STOP);
//                add(ConstantsUtils.STORY_STATUS_HIDDEN);
//            }}
//    );
    
    //List Status của chapter có trạng thái hiển thị
    public static final List< Integer > LIST_CHAPTER_DISPLAY = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.CHAPTER_ACTIVED);
                add(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
            }}
    );

//    public static final List< Integer > LIST_PAY_TYPE = Collections.unmodifiableList(
//            new ArrayList< Integer >() {{
//                add(ConstantsUtils.PAY_RECHARGE_TYPE);
//                add(ConstantsUtils.PAY_DNAME_TYPE);
//                add(ConstantsUtils.PAY_CHAPTER_VIP_TYPE);
//                add(ConstantsUtils.PAY_STORY_APPOINT_TYPE);
//            }}
//    );
}
