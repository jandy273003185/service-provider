package com.example.customermanager;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableDubbo
@SpringBootApplication
public class BustomerManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(BustomerManagerApplication.class, args);
    }

}
