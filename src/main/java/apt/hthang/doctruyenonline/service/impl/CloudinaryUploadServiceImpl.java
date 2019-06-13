package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.service.CloudinaryUploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @author Đời Không Như Là Mơ on 27/09/2018
 * @project truyenonline
 */

@Service
public class CloudinaryUploadServiceImpl implements CloudinaryUploadService {

    private Cloudinary cloudinary;

    @Value("${cloudinary.cloudname}")
    private String cloudname;

    @Value("${cloudinary.apisecret}")
    private String apisecret;

    @Value("${cloudinary.apikey}")
    private String apikey;

    @PostConstruct
    public void initializeCloudinary() {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloudname;
        cloudinary.config.apiKey = apikey;
        cloudinary.config.apiSecret = apisecret;
    }

    @Override
    public String upload(MultipartFile sourceFile) {
//        try {
//
//            //Lấy tên đầy đủ của file
//            String fileName = sourceFile.getOriginalFilename();
//
//            //Tên file mới
//            String newFileName = FilenameUtils.getBaseName(fileName) + "_" + System.nanoTime();
//            Map params = ObjectUtils.asMap("public_id", "truyenmvc/" + fileName);
//            cloudinary.uploader().upload(sourceFile.getBytes(), params);
//
//            //Lấy đường dẫn ảnh vừa upload
//            String url = cloudinary.url().generate("truyenmvc/" + newFileName);
//            return url;
//        } catch (IOException e) {
        return null;
//        }
    }

    @Override
    public String upload(MultipartFile sourceFile, String fileName) {
        try {

            Map params = ObjectUtils.asMap("public_id", "truyenmvc/" + fileName);
            cloudinary.uploader().upload(sourceFile.getBytes(), params);

            //Lấy đường dẫn ảnh vừa upload
            return cloudinary.url().generate("truyenmvc/" + fileName);
        } catch (IOException e) {
            return null;
        }
    }

}
