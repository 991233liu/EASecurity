/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
}
