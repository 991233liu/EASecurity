package com.easecurity;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.easecurity.core.utils.Locker;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@SpringBootApplication
public class SecurityCentreApplication implements CommandLineRunner, WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SecurityCentreApplication.class);

    @Autowired
    DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private BootStrap bootStrap;
    
    @Value("${easecurity.jwt.publicKey}")
    private RSAPublicKey key;
    @Value("${easecurity.jwt.privateKey}")
    private RSAPrivateKey priv;

    public static void main(String[] args) {
        SpringApplication.run(SecurityCentreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>服务启动执行");
        showConnection();
        Locker.getInstance(jdbcTemplate, transactionManager);
        bootStrap.init();
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
    
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
