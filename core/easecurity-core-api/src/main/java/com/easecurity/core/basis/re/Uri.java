/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.re;

import java.io.Serializable;

import com.easecurity.core.access.annotation.EasType;

/**
 * re_uri表：接口（服务）
 *
 */
public class Uri implements Serializable {

    private static final long serialVersionUID = 3179394360135652776L;

    public Integer id;

    /**
     * 接口（服务）
     */
    public String uri;
    /**
     * 类的全称
     */
    public String classFullName;
    /**
     * 方法名称
     */
    public String methodName;
    /**
     * 方法的Signature
     */
    public String methodSignature;
    /**
     * 授权（控制）方式
     */
    public EasType easType;
    /**
     * 来源，1人工、2程序源代码。如果是来着程序源代码，则只能禁用，不能删除
     */
    public String fromTo;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getUri() {
	return uri;
    }

    public void setUri(String uri) {
	this.uri = uri;
    }
}
