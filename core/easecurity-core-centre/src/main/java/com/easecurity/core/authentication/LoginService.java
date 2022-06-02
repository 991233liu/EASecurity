/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.authentication.session.CustomUserDetails;
import com.easecurity.core.basis.b.UserEnum;

/**
 * 登录相关服务
 *
 */
@Service
public class LoginService {

//    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${easecurity.jwt.validTime:300}")
    private Integer JWTValidTime;

    private String sql5 = "UPDATE b_user set pd_Error_Times=? where id=?";
    private String sql6 = "UPDATE b_user set pd_Error_Times=?, pd_Status=? where id=?";
    private String sql7 = "UPDATE b_user set pd_Error_Times=?, last_Login_Ttime=? where id=?";

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
    
    /**
     * 创建JWT
     * 
     * @param user
     * @param authentication
     * @return
     */
    // TODO jwt存入数据库
    public JwtClaimsSet createJWT(UserDetails user, Authentication authentication) {
	Instant now = Instant.now();
	String scope = JSON.toJSONString(user);
	String jti = UUID.randomUUID().toString().replaceAll("-", "");
	JwtClaimsSet claims = JwtClaimsSet.builder()
		.issuer("SecurityCentre")
		.issuedAt(now)
		.expiresAt(now.plusSeconds(JWTValidTime))
		.subject(authentication.getName())
		.claim("jti", jti)
		.claim("userDetails", scope).build();
	return claims;
    }
    
    /**
     * 获取JWT
     * 
     * @param user
     * @param authentication
     * @return
     */
    // TODO 获取jwt
    public JwtClaimsSet getJWT(String jti) {
	return null;
    }
    
    /**
     * 获取JWT
     * 
     * @param user
     * @param authentication
     * @return
     */
    // TODO 获取jwt
    public JwtClaimsSet getJWTByAccount(String account) {
	return null;
    }
    
    
}
