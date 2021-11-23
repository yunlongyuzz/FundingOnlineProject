package com.yyl.crowd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient // 当前版本可以不写
@SpringBootApplication
public class CrowdMainClass {

    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass.class, args);
    }

}
