/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.au;

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
    public Integer uriid;
    /**
     * 组织ID
     */
    public Integer orgid;
    /**
     * 分组。同一组内多个条件，满足任一条件则此组为true；多组条件，任一组是false，则最终结果为false。
     * 值的范围1~99
     */
    public Integer group;
    /**
     * 授权来源，1人工、2程序源代码。如果是来自程序源代码，则只能禁用，不能删除
     */
    public String fromTo;
    /**
     * 状态，0启用、1禁用
     */
    public String status;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUriid() {
        return uriid;
    }
    public void setUriid(Integer uriid) {
        this.uriid = uriid;
    }
    public Integer getOrgid() {
        return orgid;
    }
    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }
    public Integer getGroup() {
        return group;
    }
    public void setGroup(Integer group) {
        this.group = group;
    }
    public String getFromTo() {
        return fromTo;
    }
    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
