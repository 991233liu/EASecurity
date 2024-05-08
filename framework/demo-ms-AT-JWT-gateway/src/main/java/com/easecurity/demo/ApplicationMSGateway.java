package com.easecurity.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = { "com.easecurity.demo", "com.easecurity.framework" })
public class ApplicationMSGateway implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMSGateway.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>服务启动执行");
    }
}
