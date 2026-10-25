package com.vt.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CmsApplication {

    /**
     * Thực hiện xử lý main. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }

}
