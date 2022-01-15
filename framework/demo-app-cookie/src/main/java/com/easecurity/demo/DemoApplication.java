package com.easecurity.demo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.easecurity.demo","com.easecurity.framework" })
public class DemoApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
	SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
	System.out.println(">>>>>>>>>>>>>>>>>服务启动执行");
	showConnection();
    }

    private void showConnection() throws SQLException {
	logger.info(">>>>>>>>>>>>>>>>>dataSource:{}", dataSource.getClass().getName());
	Connection connection = dataSource.getConnection();
	logger.info(">>>>>>>>>>>>>>>>>connection:{}", connection.toString());
    }
}
