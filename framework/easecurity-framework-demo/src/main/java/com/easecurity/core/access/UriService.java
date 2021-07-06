/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

//import com.easecurity.core.redis.RedisUtil;

/**
 * 接口（访问）访问控制服务
 *
 */
@Service
public class UriService {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
//    @Autowired
//    RedisUtil redisUtil;
}
