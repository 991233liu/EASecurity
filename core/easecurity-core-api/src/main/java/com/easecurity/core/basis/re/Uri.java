/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis.re;

import java.io.Serializable;

import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.re.UriEnum.*;

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
    public EasType easType = EasType.DATABASE_ONLY;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public EasType getEasType() {
        return easType;
    }

    public void setEasType(EasType easType) {
        this.easType = easType;
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
