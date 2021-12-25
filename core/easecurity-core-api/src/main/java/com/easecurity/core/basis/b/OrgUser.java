/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;

/**
 * b_org_user表：组织用户关系表，含职务。
 *
 */
public class OrgUser implements Serializable {

    private static final long serialVersionUID = 1135766150664450572L;

    public Integer id;
    /**
     * 用户ID
     */
    public String userId;
    /**
     * 登录账号（冗余）
     */
    public String account;
    /**
     * 组织ID
     */
    public Integer orgId;
    /**
     * 职务ID
     */
    public Integer postsId;
    /**
     * 是否主职
     */
    public Integer masterPosts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPostsId() {
        return postsId;
    }

    public void setPostsId(Integer postsId) {
        this.postsId = postsId;
    }

    public Integer getMasterPosts() {
        return masterPosts;
    }

    public void setMasterPosts(Integer masterPosts) {
        this.masterPosts = masterPosts;
    }
}
