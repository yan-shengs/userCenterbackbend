package com.qyzen.usercenterbackbend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.qyzen.usercenterbackbend.mapper")
@SpringBootApplication
public class BackbendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackbendApplication.class, args);
    }

}
