package com.easecurity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@SpringBootApplication
public class SecurityCentreApplication implements CommandLineRunner, WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SecurityCentreApplication.class);

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
	SpringApplication.run(SecurityCentreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
	System.out.println(">>>>>>>>>>>>>>>>>服务启动执行");
	showConnection();
	System.out.println(">>>>>>>>>>>>>>>>>服务启动完成");
    }

    private void showConnection() throws SQLException {
	log.info(">>>>>>>>>>>>>>>>>dataSource:{}", dataSource.getClass().getName());
	Connection connection = dataSource.getConnection();
	log.info(">>>>>>>>>>>>>>>>>connection:{}", connection.toString());
    }

    
    /**
     * 修改默认JSON转换器为FastJson
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	// 需要定义一个convert转换消息的对象;
	FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
	// 添加fastJson的配置信息;
	FastJsonConfig fastJsonConfig = new FastJsonConfig();
	fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
	// 全局时间配置
	fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
//      fastJsonConfig.setCharset(Charset.forName("UTF-8"));
//      //处理中文乱码问题
//      List<MediaType> fastMediaTypes = new ArrayList<>();
//      fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//      //在convert中添加配置信息.
//      fastConverter.setSupportedMediaTypes(fastMediaTypes);
	fastConverter.setFastJsonConfig(fastJsonConfig);

	converters.add(fastConverter);
    }
}
