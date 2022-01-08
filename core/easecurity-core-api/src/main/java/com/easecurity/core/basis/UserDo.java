/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.List;

import com.easecurity.core.basis.b.OrgUser;
import com.easecurity.core.basis.b.User;
import com.easecurity.core.basis.b.UserInfo;
import com.easecurity.core.basis.r.RoleUser;

/**
 * domain：用户
 *
 */
public class UserDo implements Serializable {

    private static final long serialVersionUID = 6399575043885204917L;

    /**
     * 账号信息
     */
    public User user;

    /**
     * 用户基础信息
     */
    public UserInfo userinfo;

    /**
     * 所在组织、职务信息
     */
    public List<OrgUser> orgUsers;
    
    /**
     * 所在组织、职务信息
     */
    public List<RoleUser> roleUsers;

    /**
     * 获取所有的身份信息
     */
    public String allIdentities() {
	// TODO 初始化？
	// TODO 时间戳，最后更新时间
	return user.identities;
    }
}
