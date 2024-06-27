/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.easecurity.core.db.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.authentication.form.SecurityCentreUserDetails;
import com.easecurity.core.basis.b.UserEnum;
import com.easecurity.core.basis.s.UserToken;
import com.easecurity.core.utils.CacheUtil;
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
    @Value("${easecurity.jwt.validTime:1800}")
    private Integer JWTValidTime;
    @Value("${easecurity.refreshToken.validTime:604800}")
    private Integer refreshTokenValidTime;

    private String sql = "SELECT * FROM s_user_token WHERE access_token = ?";
    private String sql2 = "SELECT * FROM s_user_token WHERE access_token = ? AND refresh_token = ?";
    private String sql3 = "SELECT id FROM s_user_token WHERE account = ?";
    private String sql5 = "UPDATE b_user set pd_Error_Times=? where id=?";
    private String sql6 = "UPDATE b_user set pd_Error_Times=?, pd_Status=? where id=?";
    private String sql7 = "UPDATE b_user set pd_Error_Times=?, last_Login_Ttime=? where id=?";
    private String sql8 = "UPDATE s_user_token SET account = ?, access_token = ?, access_token_expires = ?, refresh_token = ?, refresh_token_expires = ?, user_details = ?, jwt = ?, date_created = ? WHERE id = ?";
    private String sql9 = "INSERT INTO s_user_token(account, access_token, access_token_expires, refresh_token, refresh_token_expires, user_details, jwt, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /*
     * 密码校验失败
     */
    public void loginFail(SecurityCentreUserDetails user) {
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
    public void loginSuccess(SecurityCentreUserDetails user) {
        jdbcTemplate.update(sql7, 0, new Date(), user.id);
    }

    /**
     * 创建JWT
     * 
     * @param user
     * @param authentication
     * @return
     */
    public JwtClaimsSet createJwt(final UserDetails user) {
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
    private JwtClaimsSet createJwt(final UserDetails user, String jti, Instant now) {
        String scope = JSON.toJSONString(user);
        JwtClaimsSet claims = JwtClaimsSet.builder().id(jti).issuer("SecurityCentre").issuedAt(now).expiresAt(now.plusSeconds(JWTValidTime)).subject(user.account)
                .claim("userDetails", scope).build();
        return claims;
    }

    /**
     * 获取JwtTokenValue
     * 
     * @param jti
     * @return
     */
    public String getJwtTokenValue(final JwtClaimsSet jwt) {
        return encoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();
    }

    /**
     * 创建一个token
     * 
     * @param user
     * @return
     */
    public Token creatToken(final UserDetails user) {
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
    public Token creatToken(final UserDetails user, String from, String to) {
        Token token = newToken(user, from, to);
        JwtClaimsSet claims = createJwt(user, token.access_token, token.expires);
        String jwt = getJwtTokenValue(claims);
        // usertoken存库
        UserToken newUserToken = newUserToken(user, token, jwt);
        saveUserToken(newUserToken);
        return token;
    }

    /**
     * 创建一个token
     * 
     * @param user
     * @param from
     * @param to
     * @return
     */
    private Token newToken(final UserDetails user, String from, String to) {
        Instant now = Instant.now();
        Token token = new Token();
        // TODO 增加系统标识,可以是全局（没有标识），也可以是某个系统独有
        token.access_token = UUID.randomUUID().toString().replaceAll("-", "");
        token.expires = now.plusSeconds(JWTValidTime);
        token.expires_in = JWTValidTime;
        // TODO 增加系统标识,可以是全局（没有标识），也可以是某个系统独有
        token.refresh_token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 刷新token。accessToken下的所有缓存会立即失效。
     * 
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public Token refreshToken(String accessToken, String refreshToken) {
        UserToken userToken = getValidUserTokenByAccessTokenAndRefreshToken(accessToken, refreshToken);
        if (userToken != null) {
            UserDetails user = JsonUtils.jsonToObject(userToken.userDetails, UserDetails.class);
            Token token = newToken(user, null, null);
            JwtClaimsSet claims = createJwt(user, token.access_token, token.expires);
            String jwt = getJwtTokenValue(claims);
            // usertoken存库
            UserToken newUserToken = newUserToken(user, token, jwt);
            newUserToken.id = userToken.id;
            saveUserToken(newUserToken);
            CacheUtil.delAccessTokenCache(accessToken);
            return token;
        }
        return null;
    }

    /**
     * 获取有效的UserToken，如果accessToken不存在或者已过期，则返回null。
     * 
     * @param accessToken
     * @return
     */
    public UserToken getValidUserToken(String accessToken) {
        List<UserToken> userTokens = jdbcTemplate.query(sql, new RowMapper<UserToken>() {
            @Override
            public UserToken mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserToken userToken = new UserToken();
                userToken.id = rs.getInt("id");
                userToken.account = rs.getString("account");
                userToken.accessToken = rs.getString("access_token");
                userToken.accessTokenExpires = rs.getTimestamp("access_token_expires").toInstant();
                userToken.refreshToken = rs.getString("refresh_token");
                userToken.refreshTokenExpires = rs.getTimestamp("refresh_token_expires").toInstant();
                userToken.userDetails = rs.getString("user_details");
                userToken.jwt = rs.getString("jwt");
                userToken.dateCreated = rs.getTimestamp("date_created");
                return userToken;
            }
        }, accessToken);
        if (userTokens.size() > 0) {
            UserToken userToken = userTokens.get(0);
            // 判断rt是否过期
            if (userToken.accessTokenExpires.isAfter(Instant.now())) {
                return userToken;
            }
        }
        return null;
    }

    /**
     * 获取有效的UserToken，如果refreshToken不存在或者已过期，则返回null。
     * 
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserToken getValidUserTokenByAccessTokenAndRefreshToken(String accessToken, String refreshToken) {
//	List<UserToken> userTokens = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(UserToken.class), accessToken, refreshToken);
        List<UserToken> userTokens = jdbcTemplate.query(sql2, new RowMapper<UserToken>() {
            @Override
            public UserToken mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserToken userToken = new UserToken();
                userToken.id = rs.getInt("id");
                userToken.account = rs.getString("account");
                userToken.accessToken = rs.getString("access_token");
                userToken.accessTokenExpires = rs.getTimestamp("access_token_expires").toInstant();
                userToken.refreshToken = rs.getString("refresh_token");
                userToken.refreshTokenExpires = rs.getTimestamp("refresh_token_expires").toInstant();
                userToken.userDetails = rs.getString("user_details");
                userToken.jwt = rs.getString("jwt");
                userToken.dateCreated = rs.getTimestamp("date_created");
                return userToken;
            }
        }, accessToken, refreshToken);
        if (userTokens.size() > 0) {
            UserToken userToken = userTokens.get(0);
            // 判断rt是否过期
            if (userToken.refreshTokenExpires.isAfter(Instant.now())) {
                return userToken;
            }
        }
        return null;
    }

    /**
     * 创建一个新的UserToken，如果accessToken不存在或者已过期，则返回null。
     * 
     * @param user
     * @param token
     * @return
     */
    private UserToken newUserToken(final UserDetails user, final Token token, final String jwt) {
        // 判断token是否过期
        if (token.expires.isAfter(Instant.now())) {
            UserToken userToken = new UserToken();
            userToken.accessToken = token.access_token;
            userToken.accessTokenExpires = token.expires;
            userToken.account = user.account;
            userToken.refreshToken = token.refresh_token;
            userToken.refreshTokenExpires = token.expires.plusSeconds(refreshTokenValidTime - JWTValidTime);
            userToken.userDetails = JsonUtils.objectToJson(user);
            userToken.jwt = jwt;
            userToken.dateCreated = new Date();
            return userToken;
        }
        return null;
    }

    /**
     * 保存UserToken到数据库
     * 
     * @param userJwt
     */
    public void saveUserToken(UserToken userToken) {
        if (userToken.id == null) {
            List<UserToken> userTokens = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(UserToken.class), userToken.account);
            if (userTokens.size() > 0) {
                userToken.id = userTokens.get(0).id;
            }
        }
        if (userToken.id != null) {
            jdbcTemplate.update(sql8, userToken.account, userToken.accessToken, Date.from(userToken.accessTokenExpires), userToken.refreshToken,
                    Date.from(userToken.refreshTokenExpires), userToken.userDetails, userToken.jwt, userToken.dateCreated, userToken.id);
        } else {
            jdbcTemplate.update(sql9, userToken.account, userToken.accessToken, Date.from(userToken.accessTokenExpires), userToken.refreshToken,
                    Date.from(userToken.refreshTokenExpires), userToken.userDetails, userToken.jwt, userToken.dateCreated);
        }
    }

    /**
     * 获取AccessToken的有效时长
     */
    public int getAccessTokenValidTime() {
        return JWTValidTime;
    }
}
