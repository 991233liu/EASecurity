/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.re;

import java.io.Serializable;

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
    String classFullName;
    /**
     * 方法名称
     */
    String methodName;
    /**
     * 方法的Signature
     */
    String methodSignature;

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
