/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.au;

import com.easecurity.core.basis.au.UriOrgEnum.*;

import java.io.Serializable;

/**
 * au_uri_org表：基于组织的接口（服务）授权
 *
 */
public class UriOrg implements Serializable {

    private static final long serialVersionUID = 2796966878732593998L;
    
    public Integer id;
    /**
     * URI资源ID
     */
    public Integer uriId;
    /**
     * 组织ID
     */
    public Integer orgId;
    /**
     * 组织code
     */
    public String code;
    /**
     * 组织name
     */
    public String name;
    /**
     * 组织fullCode
     */
    public String fullCode;
    /**
     * 组织fullName
     */
    public String fullName;
    /**
     * 分组。同一组内多个条件，满足任一条件则此组为true；多组条件，任一组是false，则最终结果为false。
     * 值的范围1~99
     */
    public Integer group1;
    /**
     * 来源，0人工、1程序源代码。如果是来着程序源代码，则只能禁用，不能删除
     */
    public FromTo fromTo;
    /**
     * 状态，0启用、1禁用
     */
    public Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUriId() {
        return uriId;
    }

    public void setUriId(Integer uriId) {
        this.uriId = uriId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getGroup1() {
        return group1;
    }

    public void setGroup1(Integer group1) {
        this.group1 = group1;
    }

    public FromTo getFromTo() {
        return fromTo;
    }

    public void setFromTo(FromTo fromTo) {
        this.fromTo = fromTo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
