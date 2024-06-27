/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.b.OrgUser;
import com.easecurity.core.basis.b.User;
import com.easecurity.core.basis.b.UserInfo;
import com.easecurity.core.basis.r.RoleUser;
import com.easecurity.core.db.BeanPropertyRowMapper;
import com.easecurity.core.redis.RedisUtil;

/**
 * 人员查询相关服务和密码管理服务。
 *
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisUtil redisUtil;

    String sql = "SELECT * FROM b_user where account = ?";
    String sql2 = "SELECT * FROM b_user_info where user_id = ?";
    String sql3 = "SELECT * FROM b_org_user where user_id = ?";
    String sql4 = "SELECT * FROM r_role_user where user_id = ?";
    String sql5 = "SELECT * FROM b_user where id = ?";

    @Value("${easecurity.cache.user.validTime:3600}")
    Integer validTime;

    /**
     * 获取用户信息(没有缓存实时数据库读取)
     * 
     * @param user 账号
     */
    // TODO 登录性能待优化
    public UserDo getUserDoForLogin(String account) {
        log.debug("-----## 登录用户信息：user={}", account);
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

    /**
     * 获取用户信息（高性能）
     * 
     * @param user 账号
     */
    public UserDo getUserDoByAccount(String account) {
        UserDo userDo = (UserDo) redisUtil.get("userdo:account:" + account);
        if (userDo == null) {
            userDo = getUserDoByAccountNoCache(account);
            redisUtil.set("userdo:account:" + account, userDo, validTime);
            redisUtil.set("userdo:id:" + userDo.user.id, userDo, validTime);
        }
        return userDo;
    }

    /**
     * 获取用户信息（高性能）
     * 
     * @param userId 账号id
     */
    public UserDo getUserDoById(String userId) {
        UserDo userDo = (UserDo) redisUtil.get("userdo:id:" + userId);
        if (userDo == null) {
            userDo = getUserDoByIdNoCache(userId);
            redisUtil.set("userdo:account:" + userDo.user.account, userDo, validTime);
            redisUtil.set("userdo:id:" + userId, userDo, validTime);
        }
        return userDo;
    }

    /**
     * 获取用户信息（没有缓存实时数据库读取）
     * 
     * @param user 账号
     */
    public UserDo getUserDoByAccountNoCache(String account) {
        List<User> us = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), account);
        if (us.size() == 1) {
            return getUserDo(us.get(0));
        } else {
            return null;
        }
    }

    /**
     * 获取用户信息（没有缓存实时数据库读取）
     * 
     * @param userId 账号id
     */
    public UserDo getUserDoByIdNoCache(String userId) {
        List<User> us = jdbcTemplate.query(sql5, new BeanPropertyRowMapper<>(User.class), userId);
        if (us.size() == 1) {
            return getUserDo(us.get(0));
        } else {
            return null;
        }
    }

    private UserDo getUserDo(User user) {
        UserDo userDo = new UserDo();
        userDo.user = user;
        List<UserInfo> userInfos = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(UserInfo.class), userDo.user.id);
        if (userInfos.size() == 1)
            userDo.userinfo = userInfos.get(0);
        else
            userDo.userinfo = new UserInfo();
        userDo.orgUsers = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(OrgUser.class), userDo.user.id);
        userDo.roleUsers = jdbcTemplate.query(sql4, new BeanPropertyRowMapper<>(RoleUser.class), userDo.user.id);
        return userDo;
    }
}
