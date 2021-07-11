package org.example.spring_boot2.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lifei
 */
@MapperScan
@SpringBootApplication
public class SpringBoot2DataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot2DataApplication.class, args);
    }

}
