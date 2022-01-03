/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.util.Date;
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
import com.easecurity.core.basis.b.UserEnum;
import com.easecurity.core.basis.b.UserInfo;
import com.easecurity.core.basis.r.RoleUser;
import com.easecurity.core.redis.RedisUtil;

/**
 * 登录相关服务
 *
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RedisUtil redisUtil;

    String sql = "SELECT * FROM b_user where account = ?";
    String sql2 = "SELECT * FROM b_user_info where user_id = ?";
    String sql3 = "SELECT * FROM b_org_user where user_id = ?";
    String sql4 = "SELECT * FROM r_role_user where user_id = ?";
    String sql5 = "UPDATE b_user set pd_Error_Times=? where id=?";
    String sql6 = "UPDATE b_user set pd_Error_Times=?, pd_Status=? where id=?";
    String sql7 = "UPDATE b_user set pd_Error_Times=?, last_Login_Ttime=? where id=?";

    /**
     * 登录，用户名+密码
     * 
     * @param user 账号
     * @param pd   密码
     */
    @Deprecated
    public UserDo login(String user, String pd) {
	log.debug("-----## 登录信息：user={} pd={}", user, pd);
	List<User> us = jdbcTemplate.query("SELECT * FROM b_user where account = ? and pd = ?", new BeanPropertyRowMapper<>(User.class), user, pd);
	if (us.size() == 1) {
	    // TODO 账号状态
	    // TODO 密码状态

	    UserDo userDo = new UserDo();
	    userDo.user = us.get(0);
	    userDo.orgUsers = jdbcTemplate.query("SELECT * FROM b_org_user where user_id = ?", new BeanPropertyRowMapper<>(OrgUser.class), userDo.user.id);

	    // TODO 存入session
	    // TODO 存入Redis
	    redisUtil.set("userdo:" + userDo.user.id, userDo, 3600);
	    System.out.println(redisUtil.get("userdo:" + userDo.user.id));

	    // TODO 更新登录信息
	    // TODO 记录登录日志

	    return userDo;
	} else {
	    // TODO 登录失败：账号或密码错误
	}
	return null;
    }

    /**
     * 获取用户信息(没有缓存实时数据库读取)
     * 
     * @param user 账号
     */
    // TODO 登录性能待优化
    public UserDo getUserDoForLogin(String account) {
	log.debug("-----## 登录信息：user={} pd={}", account);
	List<User> us = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), account);
	if (us.size() == 1) {
	    UserDo userDo = new UserDo();
	    userDo.user = us.get(0);
	    List<UserInfo> userInfos = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(UserInfo.class), userDo.user.id);
	    if (userInfos.size() == 1)
		userDo.userinfo = userInfos.get(0);
	    else
		userDo.userinfo = new UserInfo();
	    userDo.orgUsers = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(OrgUser.class), userDo.user.id);
	    userDo.roleUsers = jdbcTemplate.query(sql4, new BeanPropertyRowMapper<>(RoleUser.class), userDo.user.id);
	    return userDo;
	} else {
	    return null;
	}
    }

    /*
     * 密码校验失败
     */
    public void loginFail(CustomUserDetails user) {
	int pdErrorTimes = 0;
	if (user.pdErrorTimes != null)
	    pdErrorTimes = user.pdErrorTimes;
	pdErrorTimes++;
	if (pdErrorTimes >= 5)
	    jdbcTemplate.update(sql6, pdErrorTimes, UserEnum.PdStatus.MAXTIMES.ordinal(), user.id);
	else
	    jdbcTemplate.update(sql5, pdErrorTimes, user.id);
    }

    /*
     * 成功登录
     */
    public void loginSuccess(CustomUserDetails user) {
	jdbcTemplate.update(sql7, 0, new Date(), user.id);
    }
}
