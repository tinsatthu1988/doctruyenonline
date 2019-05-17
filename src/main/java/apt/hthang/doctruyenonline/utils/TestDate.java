package apt.hthang.doctruyenonline.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author Linh
 * @project doctruyenonline
 */
public class TestDate {
    
    public static void main(String[] args) throws IOException {
        float a = (float) 3.1;
        float b = (float) 4.9;
        System.out.println((int) a);
        System.out.println((int) b);
        System.out.println(a/1);
        System.out.println(b/1);
//        Document doc = Jsoup.connect("https://truyencv.com/ta-se-chi-dai-chieu/chuong-1").timeout(5000).get();
//        String[] title = doc.title().split("-");
//        // Lấy Tên Chương
//        String[] titleText = title[1].trim().split(" ");
//        StringBuilder nameChapter = new StringBuilder();
//        String chapterNumber = "";
//        for (int i = 0; i < titleText.length; i++) {
//            if (i == 1)
//                chapterNumber = titleText[i];
//            if (i > 1)
//                nameChapter.append(titleText[i]).append(" ");
//        }
//        System.out.println(chapterNumber.trim());
//        System.out.println(StringUtils.capitalize(nameChapter.toString().trim()));
//        Element page = doc.select("div#js-truyencv-content").first();
//        String cleanString = Jsoup.parse(page.html()).wholeText();
////        System.out.println(cleanString);
////        System.out.println(page.html());
//
//        System.out.println(cleanString.replaceAll("\n\n", "<br>"));
//        System.out.println(htmlOut);
//        System.out.println(DateUtils.convertStringToDate("17-05-2019"));
//        System.out.println(DateUtils.getCurrentDate());
//        System.out.println(DateUtils.getFirstDayOfMonth());
//        System.out.println(DateUtils.getLastDayOfMonth());
    }
}
