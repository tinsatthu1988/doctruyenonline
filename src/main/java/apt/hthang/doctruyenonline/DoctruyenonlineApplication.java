package apt.hthang.doctruyenonline;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableEncryptableProperties
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class DoctruyenonlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctruyenonlineApplication.class, args);
    }

}

