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
    public String userid;
    /**
     * 登录账号（冗余）
     */
    public String user;
    /**
     * 组织ID
     */
    public Integer orgid;
    /**
     * 职务ID
     */
    public Integer postsid;
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

    public String getUserid() {
	return userid;
    }

    public void setUserid(String userid) {
	this.userid = userid;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public Integer getOrgid() {
	return orgid;
    }

    public void setOrgid(Integer orgid) {
	this.orgid = orgid;
    }

    public Integer getPostsid() {
	return postsid;
    }

    public void setPostsid(Integer postsid) {
	this.postsid = postsid;
    }

    public Integer getMasterPosts() {
	return masterPosts;
    }

    public void setMaster_posts(Integer masterPosts) {
	this.masterPosts = masterPosts;
    }

}
