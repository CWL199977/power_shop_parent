package com.powershop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @SpringBootApplication 说明这是一个启动类
 * @EnableDiscoveryClient 开启服务注册
 * @MapperScan("com.bjpowernode.mapper") 扫描mapper接口
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.powershop.mapper")
public class PowerShopItemApp {
    public static void main(String[] args) {
        SpringApplication.run(PowerShopItemApp.class, args);
    }
}
