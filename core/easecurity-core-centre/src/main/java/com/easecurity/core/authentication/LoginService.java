/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.authentication.form.CustomUserDetails;
import com.easecurity.core.basis.b.UserEnum;
import com.easecurity.core.basis.s.UserToken;
import com.easecurity.util.JsonUtils;

/**
 * 登录相关服务
 *
 */
@Service
public class LoginService {

//    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtEncoder encoder;
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
    public JwtClaimsSet createJwt(UserDetails user) {
	Instant now = Instant.now();
	String jti = UUID.randomUUID().toString().replaceAll("-", "");
	return createJwt(user, jti, now);
    }

    /**
     * 创建JWT
     * 
     * @param user
     * @param authentication
     * @return
     */
    // TODO jwt存入数据库
    private JwtClaimsSet createJwt(UserDetails user, String jti, Instant now) {
	String scope = JSON.toJSONString(user);
	JwtClaimsSet claims = JwtClaimsSet.builder()
		.id(jti)
		.issuer("SecurityCentre")
		.issuedAt(now).expiresAt(now.plusSeconds(JWTValidTime)).subject(user.account)
		.claim("userDetails", scope).build();
	return claims;
    }

    /**
     * 获取JWT
     * 
     * @param jti
     * @return
     */
    // TODO 获取jwt
    public JwtClaimsSet getJwt(String jti) {
	// 缓存中获取，如果存在直接返回
	// 缓存中不存在，数据库中取token，如果还在有效期，则创建新的jwt并返回
	UserDetails user = new UserDetails();
	user.account = "123123123";
	return createJwt(user, jti, Instant.now());
    }
    
    /**
     * 获取JWT
     * 
     * @param account
     * @return
     */
    // TODO 获取jwt
    public JwtClaimsSet getJwtByAccount(String account) {
	// 缓存中获取，如果存在直接返回
	// 缓存中不存在，数据库中取token，如果还在有效期，则创建新的jwt并返回
	return null;
    }

    /**
     * 获取JwtTokenValue
     * 
     * @param jti
     * @return
     */
    public String getJwtTokenValue(JwtClaimsSet jwt) {
	return encoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();
    }

    /**
     * 创建一个token
     * 
     * @param user
     * @return
     */
    public Token creatToken(UserDetails user) {
	return creatToken(user, null, null);
    }

    /**
     * 创建一个token
     * 
     * @param user
     * @param from
     * @param to
     * @return
     */
    public Token creatToken(UserDetails user, String from, String to) {
	Instant now = Instant.now();
	Token token = new Token();
	// TODO 增加系统标识,可以是全局（没有标识），也可以是某个系统独有
	token.access_token = UUID.randomUUID().toString().replaceAll("-", "");
	token.expires = now.plusSeconds(JWTValidTime);
	token.expires_in = JWTValidTime;
	// TODO 增加系统标识,可以是全局（没有标识），也可以是某个系统独有
	token.refresh_token = UUID.randomUUID().toString().replaceAll("-", "");
	createJwt(user, token.access_token, now);
	// TODO usertoken存库
	return token;
    }

    /**
     * 刷新token
     * 
     * @param rt
     * @return
     */
    public Token refreshToken(String rt) {
	UserToken userToken = getValidUserTokenByRefreshToken(rt);
	if (userToken != null) {
	    UserDetails user = JsonUtils.jsonToObject(userToken.userDetails, UserDetails.class);
	    return creatToken(user, null, null);
	}
	return null;
    }

    /**
     * 获取token，如果token不存在或者已过期，则返回null。
     * 
     * @param accessToken
     * @return
     */
    // TODO 过期时间配置
    public UserToken getValidUserToken(String accessToken) {
	// TODO 判断rt是否过期
	if (true) {
	    UserToken userToken = new UserToken();
	    userToken.accessToken = UUID.randomUUID().toString().replaceAll("-", "");
	    userToken.accessTokenExpires = Instant.now();
	    userToken.account = "123";
	    userToken.id = 1;
	    userToken.refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
	    userToken.refreshTokenExpires = Instant.now();
	    userToken.userDetails = "{\"account\":\"liulufeng\",\"id\":\"efd1111\",\"identities\":\"{\\\"user\\\":{\\\"id\\\":\\\"efd1111\\\",\\\"account\\\":\\\"liulufeng\\\"},\\\"posts\\\":{\\\"id\\\":\\\"1,2\\\",\\\"code\\\":\\\"gslingdao,bmjingli\\\",\\\"name\\\":\\\"公司领导,部门经理\\\"},\\\"org\\\":{\\\"id\\\":\\\"4,3\\\",\\\"code\\\":\\\"abumen,banshichu\\\",\\\"name\\\":\\\"a部门,xx办事处\\\",\\\"fullCode\\\":\\\"/gongsi/abumen/,/banshichu/\\\",\\\"fullName\\\":\\\"/xx公司/a部门/,/xx办事处\\\"}}\"}";
	    return userToken;
	}
	return null;
    }
    
    /**
     * 获取token，如果token不存在或者已过期，则返回null。
     * 
     * @param refreshToken
     * @return
     */
    // TODO 过期时间配置
    public UserToken getValidUserTokenByRefreshToken(String refreshToken) {
	// TODO 判断rt是否过期
	if (true) {
	    UserToken userToken = new UserToken();
	    userToken.accessToken = UUID.randomUUID().toString().replaceAll("-", "");
	    userToken.accessTokenExpires = Instant.now();
	    userToken.account = "123";
	    userToken.id = 1;
	    userToken.refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
	    userToken.refreshTokenExpires = Instant.now();
	    userToken.userDetails = "{\"account\":\"liulufeng\",\"id\":\"efd1111\",\"identities\":\"{\\\"user\\\":{\\\"id\\\":\\\"efd1111\\\",\\\"account\\\":\\\"liulufeng\\\"},\\\"posts\\\":{\\\"id\\\":\\\"1,2\\\",\\\"code\\\":\\\"gslingdao,bmjingli\\\",\\\"name\\\":\\\"公司领导,部门经理\\\"},\\\"org\\\":{\\\"id\\\":\\\"4,3\\\",\\\"code\\\":\\\"abumen,banshichu\\\",\\\"name\\\":\\\"a部门,xx办事处\\\",\\\"fullCode\\\":\\\"/gongsi/abumen/,/banshichu/\\\",\\\"fullName\\\":\\\"/xx公司/a部门/,/xx办事处\\\"}}\"}";
	    return userToken;
	}
	return null;
    }
}
