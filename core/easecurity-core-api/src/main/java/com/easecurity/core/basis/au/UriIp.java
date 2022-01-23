/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.au;

import com.easecurity.core.basis.au.UriIpEnum.*;

import java.io.Serializable;

/**
 * au_uri_ip表：基于ip的接口（服务）授权
 *
 */
public class UriIp implements Serializable {

    private static final long serialVersionUID = 2796966878732593998L;
    
    public Integer id;
    /**
     * URI资源ID
     */
    public Integer uriId;
    /**
     * IP地址，多值","分割
     */
    public String ips;
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

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
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
