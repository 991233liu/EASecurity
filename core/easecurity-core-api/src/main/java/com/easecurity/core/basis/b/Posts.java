/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;

/**
 * b_posts表：职务信息表。
 *
 */
public class Posts implements Serializable {

    private static final long serialVersionUID = 8933584033970773825L;

    public Integer id;
    /**
     * 职务名称
     */
    public String name;
    /**
     * 职务编码
     */
    public String code;
    /**
     * 职级1~99
     */
    public Integer ranking;
    /**
     * 职务类别，1领导，2，职员
     */
    public Integer type;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public Integer getRanking() {
	return ranking;
    }

    public void setRanking(Integer ranking) {
	this.ranking = ranking;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

}
