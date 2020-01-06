package com.houliu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author houliu
 * @create 2019-12-30 00:22
 */

@SpringBootApplication
@MapperScan("com.houliu.sys.mapper")
public class HouliuApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouliuApplication.class,args);
    }

}
