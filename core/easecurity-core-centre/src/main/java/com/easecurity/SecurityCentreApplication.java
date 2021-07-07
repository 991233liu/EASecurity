package com.easecurity;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
//@MapperScan
public class SecurityCentreApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SecurityCentreApplication.class);

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
	SpringApplication.run(SecurityCentreApplication.class, args);
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
