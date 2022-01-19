/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.Serializable;
import java.util.Map;

import com.easecurity.core.basis.UserDo;
import com.easecurity.util.JsonUtils;

/**
 * 用户身份信息（用于身份认证）
 *
 */
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 813507675251382711L;

    public String id;

    /**
     * 登录账号
     */
    public String account;
    /**
     * 身份串
     */
    public String identities;
    /**
     * 姓名
     */
    public String name;
    /**
     * 用户图标
     */
    public String icon;

    public UserDetails() {
    }

    public UserDetails(UserDo userDo) {
	this.id = userDo.user.id;
	this.account = userDo.user.account;
	if (userDo.userinfo != null) {
	    this.name = userDo.userinfo.name;
	    this.icon = userDo.userinfo.icon;
	}
    }

    /**
     * 获取所有的身份信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> allIdentitiesWithMap() {
	if (_AllIdentitiesWithMap == null)
	    _AllIdentitiesWithMap = (Map<String, String>) JsonUtils.jsonToObject(identities);
	return _AllIdentitiesWithMap;
    }

    private Map<String, String> _AllIdentitiesWithMap;

    @Override
    public String toString() {
	return "[account=" + account + ", identities=" + identities + "]";
    }
}
