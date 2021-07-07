/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.b.OrgUser;
import com.easecurity.core.basis.b.User;
//import com.easecurity.core.redis.RedisUtil;

/**
 * 登录相关服务
 *
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
//    @Autowired
//    RedisUtil redisUtil;

    /**
     * 登录，用户名+密码
     * 
     * @param user 账号
     * @param pd   密码
     */
    public UserDo login(String user, String pd) {
	log.debug("-----## 登录信息：user={} pd={}", user, pd);
	List<User> us = jdbcTemplate.query("SELECT * FROM b_user where user = ? and pd = ?", new BeanPropertyRowMapper<>(User.class), user, pd);
	if (us.size() == 1) {
	    // TODO 账号状态
	    // TODO 密码状态

	    UserDo userDo = new UserDo();
	    userDo.user = us.get(0);
	    userDo.orgUsers = jdbcTemplate.query("SELECT * FROM b_org_user where userid = ?", new BeanPropertyRowMapper<>(OrgUser.class), userDo.user.id);

	    // TODO 存入session
	    // TODO 存入Redis
//	    redisUtil.set("userdo:" + userDo.user.id, userDo, 3600);
//	    System.out.println(redisUtil.get("userdo:" + userDo.user.id));

	    // TODO 更新登录信息
	    // TODO 记录登录日志

	    return userDo;
	} else {
	    // TODO 登录失败：账号或密码错误
	}
	return null;
    }
}