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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.easecurity.core.utils.Locker;

@SpringBootApplication
public class SecurityCentreApplication implements CommandLineRunner, WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SecurityCentreApplication.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public static void main(String[] args) {
        SpringApplication.run(SecurityCentreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>服务启动执行");
        showConnection();
        Locker.getInstance(jdbcTemplate, transactionManager);
        System.out.println(">>>>>>>>>>>>>>>>>服务启动完成");
    }

    private void showConnection() throws SQLException {
        log.info(">>>>>>>>>>>>>>>>>dataSource:{}", dataSource.getClass().getName());
        Connection connection = dataSource.getConnection();
        log.info(">>>>>>>>>>>>>>>>>connection:{}", connection.toString());
    }
}
