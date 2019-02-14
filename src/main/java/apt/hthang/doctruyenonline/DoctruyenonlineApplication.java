package apt.hthang.doctruyenonline;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class DoctruyenonlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctruyenonlineApplication.class, args);
    }

}

